package com.dmide.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import com.dmide.DMIDE;
import com.dmide.DMIDESplashScreen;
import com.dmide.IDE;
import com.dmide.compiler.SetupWizard;
import com.dmide.environment.DMEnvironment;
import com.dmide.ui.editors.FileEditorPane;
import com.dmide.ui.laf.DefaultLafHandler;
import com.dmide.ui.laf.LafHandler;
import com.dmide.ui.prefs.AppearancePreferences;
import com.dmide.ui.prefs.GeneralPreferences;
import com.dmide.ui.prefs.PreferencesDialog;
import com.dmide.ui.tabs.TabbedPaneUI;
import com.dmide.ui.tabs.TabbedPaneUIHeader;
import com.dmide.ui.windows.IDEMainWindow;
import com.dmide.ui.windows.dialogs.NewFileDialog;
import com.dmide.util.events.IDEEvent;
import com.dmide.util.events.IDEEventHandler;
import com.dmide.util.misc.Misc;
import com.l2fprod.common.swing.JDirectoryChooser;

public class DMIDEUI {

	public IDEMainWindow mainWindow;
	public IDEProgressHandler progressHandler;
	public JFileChooser fileChooser;
	public JDirectoryChooser dirChooser;
	public PreferencesDialog preferencesDialog;
	public NewFileDialog newFileDialog;
	public ArrayList<FileEditorPane> fileEditorPanes;

	void setLaf() {
		IDEEventHandler.sendIDEEvent(new IDEEvent("ui.laf.set", this));
		if(!DMIDE.hasProperty("ui.laf.handler", LafHandler.class)) {
			DMIDE.setProperty("ui.laf.handler", new DefaultLafHandler());
		}
		LafHandler h = DMIDE.getProperty("ui.laf.handler", LafHandler.class);
		if(h == null) return;
		else h.setLaf();
	}

	public void openSplashScreen() {
		Runnable openSplash = new Runnable() {
			@Override
			public void run() {
				DMIDESplashScreen.getInstance().displaySplash();
			}
		};
		UIUtil.toEventQueue(openSplash);
	}

	void createMainWindow() {
		final DMIDEUI dmideUI = this;
		Runnable closeSplash = new Runnable() {
			@Override
			public void run() {
				DMIDESplashScreen.getInstance().hideSplash();
			}
		};
		Runnable mainWindowCreate = new Runnable() {
			@Override
			public void run() {
				JFrame.setDefaultLookAndFeelDecorated(true);
				JDialog.setDefaultLookAndFeelDecorated(true);

				//FIXME: Not sure if this is in the right place?
				File ideproperties = new File("./settings/ideproperties.xml");
				if (!ideproperties.exists()) {
					SetupWizard SW = new SetupWizard();
					SW.runWizard();
				}

				dmideUI.mainWindow = new IDEMainWindow();
				dmideUI.mainWindow.addWindowListener(new IDEMainWindowListener());

				DMIDE.setProperty("windows.main", dmideUI.mainWindow);

				//Creates the progress handler:
				dmideUI.progressHandler = new IDEProgressHandler(dmideUI.mainWindow.getProgressBar());
				IDEEventHandler.addWatcher(dmideUI.progressHandler);

				//Creates the main menu bar:
				File menuBarSettings = new File("settings/prereq/mainmenubar.xml");
				if(menuBarSettings.exists()) {
					JMenuBar mbar = Misc.ui.createMenuBar(menuBarSettings, IDE.getInstance());
					dmideUI.mainWindow.setJMenuBar(mbar);
				} else {
					System.out.println("Menubar settings do not exist!");
				}

				//Creates the main file chooser:
				dmideUI.fileChooser = new JFileChooser();
				DMIDE.setProperty("windows.dialogs.filechooser", dmideUI.fileChooser);
				//DMIDEUI.this.fileChooser.showOpenDialog(null);

				//Create the main dir chooser:
				dmideUI.dirChooser = new JDirectoryChooser();
				DMIDE.setProperty("windows.dialogs.dirchooser", dmideUI.dirChooser);
				//dmideUI.dirChooser.showOpenDialog(null);

				//Creates the main preferences dialog:
				dmideUI.preferencesDialog = new PreferencesDialog(dmideUI.mainWindow);
				dmideUI.preferencesDialog.createUI();
				dmideUI.preferencesDialog.addPreferencesPage(new GeneralPreferences());
				dmideUI.preferencesDialog.addPreferencesPage(new AppearancePreferences());
				DMIDE.setProperty("windows.dialogs.preferencesDialog", dmideUI.preferencesDialog);

				//Creates new file dialog:
				dmideUI.newFileDialog = new NewFileDialog(dmideUI.mainWindow);
				DMIDE.setProperty("windows.dialogs.newfiledialog", dmideUI.newFileDialog);

				DMIDEUI.getInstance().loadIDEWindowState();

				dmideUI.mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				dmideUI.mainWindow.setVisible(true);

				DMEnvironment.createRecentsMenu();

				DefaultMenuActions.setActions();
			}
		};
		UIUtil.toEventQueue(mainWindowCreate);
		UIUtil.toEventQueue(closeSplash);
		IDEEventHandler.sendIDEEvent(new IDEEvent("ide.windows.loaded", null));
	}

	public void loadIDEWindowState() {
		Dimension d = this.getMainWindow().getSize();
		Point p = this.getMainWindow().getLocation();
		if(DMIDE.getProperty("mainwindow.width") != null) {
			d.width = Integer.parseInt((String) DMIDE.getProperty("mainwindow.width"));
		}
		if(DMIDE.getProperty("mainwindow.height") != null) {
			d.height = Integer.parseInt((String) DMIDE.getProperty("mainwindow.height"));
		}
		if(DMIDE.getProperty("mainwindow.x") != null) {
			p.x = Integer.parseInt((String) DMIDE.getProperty("mainwindow.x"));
		}
		if(DMIDE.getProperty("mainwindow.y") != null) {
			p.y = Integer.parseInt((String) DMIDE.getProperty("mainwindow.y"));
		}
		if(DMIDE.getProperty("mainwindow.maximized") != null) {
			Boolean b = Boolean.parseBoolean((String) DMIDE.getProperty("mainwindow.maximized"));
			if(b.equals(Boolean.TRUE)) {
				this.getMainWindow().
					setExtendedState(this.getMainWindow().getExtendedState() | JFrame.MAXIMIZED_BOTH);
			} else {
				this.getMainWindow().
					setExtendedState( JFrame.NORMAL );
			}
		}
		if(DMIDE.getProperty("mainwindow.splitter1.location") != null) {
			int spl1 = Integer.parseInt((String) DMIDE.getProperty("mainwindow.splitter1.location"));
			this.getMainWindow().getSplitPane().setDividerLocation(spl1);
		}
		if(DMIDE.getProperty("mainwindow.splitter2.location") != null) {
			int spl2 = Integer.parseInt((String) DMIDE.getProperty("mainwindow.splitter2.location"));
			this.getMainWindow().getSplitPane_1().setDividerLocation(spl2);
		}
		this.getMainWindow().setSize(d);
		this.getMainWindow().setLocation(p);
	}

	public void saveIDEWindowState() {
		boolean m = false;
		if((this.getMainWindow().getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0) {
			DMIDE.saveProperty("mainwindow.maximized", Boolean.toString(Boolean.TRUE));
			m = true;
		} else {
			DMIDE.saveProperty("mainwindow.maximized", Boolean.toString(Boolean.FALSE));
			m = false;
		}
		this.getMainWindow().setExtendedState( JFrame.NORMAL );
		DMIDE.saveProperty("mainwindow.x", Integer.toString(this.getMainWindow().getX()));
		DMIDE.saveProperty("mainwindow.y", Integer.toString(this.getMainWindow().getY()));
		DMIDE.saveProperty("mainwindow.width", Integer.toString(this.getMainWindow().getWidth()));
		DMIDE.saveProperty("mainwindow.height", Integer.toString(this.getMainWindow().getHeight()));
		DMIDE.saveProperty("mainwindow.splitter1.location",
				Integer.toString(this.getMainWindow().getSplitPane().getDividerLocation()));
		DMIDE.saveProperty("mainwindow.splitter2.location",
				Integer.toString(this.getMainWindow().getSplitPane_1().getDividerLocation()));
		if(m) this.getMainWindow().
			setExtendedState(this.getMainWindow().getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}

	public void generate() {
		this.setLaf();
		this.createMainWindow();
		this.initTabs();
	}

	public void initTabs() {
		/*
		this.mainWindow.getFileEditorPane().putClientProperty(
				SubstanceLookAndFeel.TABBED_PANE_CLOSE_BUTTONS_PROPERTY,
				Boolean.TRUE);
		SubstanceLookAndFeel.registerTabCloseChangeListener(this.mainWindow.getFileEditorPane(),
				new VetoableTabCloseListener() {

					@Override
					public void tabClosed(JTabbedPane tabbedPane, Component tabComponent) {

					}

					@Override
					public void tabClosing(JTabbedPane tabbedPane, Component tabComponent) {

					}

					@Override
					public boolean vetoTabClosing(JTabbedPane tabbedPane,
							Component tabComponent) {
						return false;
					}});
		*/
	}

	public void addTab(TabbedPaneUI tabbedPaneUI) {
		if(tabbedPaneUI instanceof Component) {
			if(tabbedPaneUI instanceof FileEditorPane) {
				int index = this.containsEditorTab((FileEditorPane)tabbedPaneUI);
				if(index >= 0) {
					this.mainWindow.getFileEditorPane().setSelectedIndex(index);
					return;
				}
				this.fileEditorPaneOpened((FileEditorPane) tabbedPaneUI);
			}
			this.mainWindow.getFileEditorPane().addTab(tabbedPaneUI.getTabTitle(),
					tabbedPaneUI.getTabIcon(), (Component) tabbedPaneUI);
			this.mainWindow.getFileEditorPane().
				setSelectedIndex(TabbedPaneUIHeader.getComponentIndex((Component) tabbedPaneUI,
						this.mainWindow.getFileEditorPane()));
			if(tabbedPaneUI instanceof FileEditorPane) {
				FileEditorPane fep = (FileEditorPane) tabbedPaneUI;
				if(fep.getTabHeader() != null) {
					fep.getTabHeader().install(fep, this.mainWindow.getFileEditorPane());
				}
			}
		}
	}

	/**
	 * Called when a new tab is added for a {@link FileEditorPane}.
	 * @param p
	 */
	public void fileEditorPaneOpened(FileEditorPane p) {
		if(this.fileEditorPanes == null) this.fileEditorPanes = new ArrayList<>();
		this.fileEditorPanes.add(p);
	}

	/**
	 * Called when a {@link FileEditorPane} is removed.
	 * @param p
	 */
	public void fileEditorPaneClosed(FileEditorPane p) {
		while(this.fileEditorPanes.contains(p)) this.fileEditorPanes.remove(p);
	}

	public int containsEditorTab(FileEditorPane ep) {
		if(this.mainWindow.getFileEditorPane().getComponentCount() > 0) {
			try {
				int i = 0;
				for(Component c : this.mainWindow.getFileEditorPane().getComponents()) {
					if(c instanceof FileEditorPane) {
						FileEditorPane e = (FileEditorPane) c;
						if(e.getFile().equals(ep.getFile())) {
							return i;
						}
					}
					i++;
				}
			} catch(IndexOutOfBoundsException e) {
				return -1;
			}
		}
		return -1;
	}


	/**
	 * @return the mainWindow
	 */
	public IDEMainWindow getMainWindow() {
		return this.mainWindow;
	}


	/**
	 * @return the progressHandler
	 */
	public IDEProgressHandler getProgressHandler() {
		return this.progressHandler;
	}

	/**
	 * @param progressHandler the progressHandler to set
	 */
	public void setProgressHandler(IDEProgressHandler progressHandler) {
		this.progressHandler = progressHandler;
	}

	/**
	 * @return the fileChooser
	 */
	public JFileChooser getFileChooser() {
		return this.fileChooser;
	}

	/**
	 * @param fileChooser the fileChooser to set
	 */
	public void setFileChooser(JFileChooser fileChooser) {
		this.fileChooser = fileChooser;
	}

	/**
	 * @return the dirChooser
	 */
	public JDirectoryChooser getDirChooser() {
		return this.dirChooser;
	}

	/**
	 * @param dirChooser the dirChooser to set
	 */
	public void setDirChooser(JDirectoryChooser dirChooser) {
		this.dirChooser = dirChooser;
	}


	/**
	 * @return the preferencesDialog
	 */
	public PreferencesDialog getPreferencesDialog() {
		return this.preferencesDialog;
	}

	/**
	 * @param preferencesDialog the preferencesDialog to set
	 */
	public void setPreferencesDialog(PreferencesDialog preferencesDialog) {
		this.preferencesDialog = preferencesDialog;
	}

	/**
	 * @return the newFileDialog
	 */
	public NewFileDialog getNewFileDialog() {
		return this.newFileDialog;
	}

	/**
	 * @return the fileEditorPanes
	 */
	public ArrayList<FileEditorPane> getFileEditorPanes() {
		return this.fileEditorPanes;
	}

	public void clearFileChooser() {
		this.fileChooser.setFileFilter(null);
		this.fileChooser.setMultiSelectionEnabled(false);
	}
	public void clearDirectoryChooser() {
		this.dirChooser.setFileFilter(null);
		this.dirChooser.setMultiSelectionEnabled(false);
	}


	static DMIDEUI instance;
	public static DMIDEUI getInstance() {
		if(instance == null) {
			instance = new DMIDEUI();
		}
		return instance;
	}
}
