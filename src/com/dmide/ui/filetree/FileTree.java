package com.dmide.ui.filetree;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingEvent;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import com.dmide.DMIDE;
import com.dmide.environment.DMEnvironment;
import com.dmide.environment.DMEnvironmentInclude;
import com.dmide.util.IDEFile;
import com.dmide.util.events.IDEEvent;
import com.dmide.util.events.IDEEventHandler;
import com.dmide.util.events.IDEEventWatcher;
import com.dmide.util.misc.IDEProcess;

@SuppressWarnings("serial")
public class FileTree extends CheckboxTree implements IDEEventWatcher {

	Map<Path, IDEFile> ideFiles = new HashMap<Path, IDEFile>();
	ArrayList<TreePath> checkedFiles;
	IDEFile root;
	DMEnvironment DME;

	Icon dirOpenIcon;
	Icon dirClosedIcon;

	FileTreeOptionPane optionPane;

	public Icon getDirOpenedIcon() {
		if(this.dirOpenIcon == null) {
			this.dirOpenIcon = new ImageIcon(this.getClass().getResource("/com/dmide/assets/blue-folder-horizontal-open.png"));
		}
		return this.dirOpenIcon;
	}
	public Icon getDirClosedIcon() {
		if(this.dirClosedIcon == null) {
			this.dirClosedIcon = new ImageIcon(this.getClass().getResource("/com/dmide/assets/blue-folder-horizontal.png"));
		}
		return this.dirClosedIcon;
	}

	public ArrayList<Path> getDirectoryPaths(File f) {
		return this.getDirectoryPaths(f, f.toPath().getNameCount());
	}

	public ArrayList<Path> getDirectoryPaths(File f, int pathcutoff) {
		File[] files = f.listFiles();
		Arrays.sort(files, DMEnvironment.ideFileComparator);
		if(files == null || files.length < 1) {return null;}
		ArrayList<Path> paths = new ArrayList<>();
		for(File file : files) {
			Path fullpath = file.toPath();
			Path filepath = fullpath.subpath(pathcutoff, fullpath.getNameCount());
			paths.add(filepath);
			//System.out.println(filepath.toString() + " -- " + filepath.getNameCount());
			if(file.isDirectory()) {
				//System.out.println("Checking: " + file.getPath());
				ArrayList<Path> retrievedPaths = this.getDirectoryPaths(file, pathcutoff);
				if(retrievedPaths != null) {
					paths.addAll(retrievedPaths);
				}
				//System.out.println("----");
			}
		}
		return paths;
	}

	public IDEFile getPathNode(Path p) {
		if(this.ideFiles.containsKey(p)) {
			return this.ideFiles.get(p);
		} else {
			return this.addPath(p);
		}
	}

	public IDEFile addPath(Path p) {
		if(this.ideFiles.containsKey(p)) return this.ideFiles.get(p);
		IDEFile ifile = new IDEFile(p);
		if(p.getNameCount() <= 1) {
			this.root.add(ifile);
		} else {
			this.getPathNode(p.subpath(0, p.getNameCount() - 1)).add(ifile);
		}
		this.ideFiles.put(p, ifile);
		this.tryCheckFile(ifile);
		return ifile;
	}

	/**
	 * Attempts to check the file if it is
	 * included in the DME.
	 */
	public void tryCheckFile(IDEFile f) {
		if(this.DME.findInclude(f.getFileObject())) {
			this.checkedFiles.add(new TreePath(f.getPath()));
		}
	}

	public IDEFile createTree(File dme) {
		this.root = new IDEFile(new File(dme.getName()));
		IDEProcess treeLoadProcess = new IDEProcess("Loading File Tree");
		treeLoadProcess.start();
		treeLoadProcess.setProgress(-1);
		ArrayList<Path> paths = this.getDirectoryPaths(dme.getParentFile());
		if(paths.size() > 0) {
			for(int i = 0; i < paths.size(); i++) {
				Path p = paths.get(i);
				if( p == null ) continue;
				this.addPath(p);
				//treeLoadProcess.setProgress( ((double)i) / ((double)paths.size()) * 100 );
			}
		}
		treeLoadProcess.end();
		return this.root;
	}

	public void setEnvironment(DMEnvironment dmeFile) {
		if(dmeFile == null) return; // This means that the argument for the environment.open
									// event was not a file.
		this.checkedFiles = new ArrayList<>();
		this.ideFiles.clear();
		//this.walkDirectoryTree(dmeFile.getParentFile());
		//this.getDirectoryPaths(dmeFile.getParentFile());
		this.DME = dmeFile;
		this.createTree(dmeFile.getFile());
		DefaultTreeModel newModel = new DefaultTreeModel(this.root);
		this.setModel(newModel);
		TreePath[] tps = this.checkedFiles.toArray(new TreePath[0]);
		this.getCheckingModel().addCheckingPaths(tps);
		this.revalidate();
		System.out.println("File tree opening environment: " + dmeFile.getFile().getName());
		this.checkedFiles.clear();
		this.checkedFiles = null;
	}

	/**
	 * Creates a new instance of the file tree.
	 */
	public FileTree() {
		this.setTreeCellProperties();
		this.addEventHandlers();
		IDEEventHandler.addWatcher(this);
		this.optionPane = new FileTreeOptionPane(this);
	}

	/**
	 * Sets the TreeCell Renderer and Editor.
	 */
	public void setTreeCellProperties() {
		IDEEventHandler.sendIDEEvent(new IDEEvent("filetree.properties.setting", this));
		if(DMIDE.getProperty("filetree.cells.renderer", TreeCellRenderer.class) == null) {
			DMIDE.setProperty("filetree.cells.renderer", new FileTreeCellRenderer(this));
		}
		this.setCellRenderer(DMIDE.getProperty("filetree.cells.renderer", TreeCellRenderer.class));
	}

	@Override
	public void eventRecieved(IDEEvent e) {
		if(e.getEventName().equals("environment.open")) {
			e.setHandled(true);
			this.setEnvironment(e.getArgument(DMEnvironment.class));
		}
	}

	public File getAbsoluteFile(IDEFile ideFile) {
		if(this.DME == null) return ideFile.getFileObject();
		if(ideFile.getAbsolute() != null) return ideFile.getAbsolute();
		ideFile.setAbsolute(new File(this.DME.getFile().getParentFile(),
				ideFile.getFileObject().getPath()));
		return ideFile.getAbsolute();
	}

	public void addEventHandlers() {
		this.addMouseListener();
		this.addCheckBoxListener();
	}

	private void addCheckBoxListener() {
		final FileTree ft = this;
		this.addTreeCheckingListener(new TreeCheckingListener() {
			@Override
			public void valueChanged(TreeCheckingEvent e) {
				Object o = e.getPath().getLastPathComponent();
				if(o instanceof IDEFile) {
					IDEFile ideFile = (IDEFile) o;
					TreePath tp = new TreePath(ideFile.getPath());
					if(ft.getCheckingModel().isPathChecked(tp)) {
						ft.DME.addInclude(new DMEnvironmentInclude(ideFile.getFileObject(),
								DMEnvironmentInclude.type_FILE));
					} else {
						ft.DME.removeInclude(new DMEnvironmentInclude(ideFile.getFileObject(),
								DMEnvironmentInclude.type_FILE));
					}
				}
			}
		});
	}
	private void addMouseListener() {

		final FileTree ft = this;
		this.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					ft.clicked(e.getX(), e.getY());
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}
		});
	}

	/**
	 * Reloads the file tree.
	 */
	public void reload() {
		if(this.DME != null) this.setEnvironment(this.DME);
	}

	public void clicked(int x, int y) {
		TreePath path = this.getPathForLocation(x, y);
		if(path == null) return;
		Object o = path.getLastPathComponent();
		if(o == null) return;
		if(o instanceof IDEFile) {
			File f  = this.getAbsoluteFile(((IDEFile) o));
			IDEEventHandler.sendIDEEvent(new IDEEvent("file.tree.openfile", f));
			//System.out.println("File Clicked: " + f.getPath());
		}
	}
}
