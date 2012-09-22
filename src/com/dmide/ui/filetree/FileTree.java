package com.dmide.ui.filetree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.dmide.util.IDEFile;
import com.dmide.util.events.IDEEvent;
import com.dmide.util.events.IDEEventHandler;
import com.dmide.util.events.IDEEventWatcher;
import com.dmide.util.misc.IDEProcess;

@SuppressWarnings("serial")
public class FileTree extends JTree implements IDEEventWatcher {

	Map<Path, IDEFile> ideFiles = new HashMap<Path, IDEFile>();
	IDEFile root;
	File DME;

	Icon dirOpenIcon;
	Icon dirClosedIcon;

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
		return ifile;
	}

	public IDEFile createTree(File dme) {
		this.root = new IDEFile(dme);
		IDEProcess treeLoadProcess = new IDEProcess();
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

	public void setEnvironment(File dmeFile) {
		if(dmeFile == null) return; // This means that the argument for the environment.open
									// event was not a file.
		this.ideFiles.clear();
		//this.walkDirectoryTree(dmeFile.getParentFile());
		//this.getDirectoryPaths(dmeFile.getParentFile());
		this.createTree(dmeFile);
		DefaultTreeModel newModel = new DefaultTreeModel(this.root);
		this.setModel(newModel);
		this.revalidate();
		this.DME = dmeFile;
		System.out.println("File tree opening environment: " + dmeFile.getName());
	}

	/**
	 * Creates a new instance of the file tree.
	 */
	public FileTree() {
		this.setTreeCellProperties();
		this.addEventHandlers();
		IDEEventHandler.addWatcher(this);
	}

	/**
	 * Sets the TreeCell Renderer and Editor.
	 */
	public void setTreeCellProperties() {
		this.setCellRenderer(new FileTreeCellRenderer(this));
	}

	@Override
	public void eventRecieved(IDEEvent e) {
		if(e.getEventName().equals("environment.open")) {
			e.setHandled(true);
			this.setEnvironment(e.getArgument(File.class));
		}
	}

	public File getAbsoluteFile(IDEFile ideFile) {
		if(this.DME == null) return ideFile.getFileObject();
		if(ideFile.getAbsolute() != null) return ideFile.getAbsolute();
		ideFile.setAbsolute(new File(this.DME.getParent(), ideFile.getFileObject().getPath()));
		return ideFile.getAbsolute();
	}

	public void addEventHandlers() {
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
