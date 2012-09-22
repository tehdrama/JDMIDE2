package com.dmide.ui;

import java.awt.Component;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.OfficeBlack2007Skin;
import org.pushingpixels.substance.api.tabbed.VetoableTabCloseListener;

import com.dmide.IDE;
import com.dmide.ui.editors.FileEditorPane;
import com.dmide.ui.windows.IDEMainWindow;
import com.dmide.util.events.IDEEventHandler;
import com.dmide.util.misc.Misc;

public class DMIDEUI {

	IDEMainWindow mainWindow;
	IDEProgressHandler progressHandler;

	void setLaf() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel(new SubstanceLookAndFeel(new OfficeBlack2007Skin()){
				private static final long serialVersionUID = 1L;});
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	void createMainWindow() {
		final DMIDEUI dmideUI = this;
		Runnable mainWindowCreate = new Runnable() {
			@Override
			public void run() {
				dmideUI.mainWindow = new IDEMainWindow();

				//Creates the progress handler:
				dmideUI.progressHandler = new IDEProgressHandler(dmideUI.mainWindow.getProgressBar());
				IDEEventHandler.addWatcher(dmideUI.progressHandler);

				//Creates the main menu bar:
				File menuBarSettings = new File("settings/mainmenubar.xml");
				if(menuBarSettings.exists()) {
					JMenuBar mbar = Misc.ui.createMenuBar(menuBarSettings, IDE.getInstance());
					dmideUI.mainWindow.setJMenuBar(mbar);
				} else {
					System.out.println("Menubar settings do not exist!");
				}

				dmideUI.mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				dmideUI.mainWindow.setVisible(true);
			}
		};
		UIUtil.toEventQueue(mainWindowCreate);
	}

	public void generate() {
		this.setLaf();
		this.createMainWindow();
		this.initTabs();
	}

	public void initTabs() {
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
	}

	public void addTab(TabbedPaneUI tabbedPaneUI) {
		if(tabbedPaneUI instanceof Component) {
			if(tabbedPaneUI instanceof FileEditorPane) {
				int index = this.containsEditorTab((FileEditorPane)tabbedPaneUI);
				if(index >= 0) {
					this.mainWindow.getFileEditorPane().setSelectedIndex(index);
					return;
				}
			}
			this.mainWindow.getFileEditorPane().addTab(tabbedPaneUI.getTabTitle(),
					tabbedPaneUI.getTabIcon(), (Component) tabbedPaneUI);
			this.mainWindow.getFileEditorPane().
				setSelectedIndex(this.mainWindow.getFileEditorPane().getComponentCount() - 1);
		}
	}

	public int containsEditorTab(FileEditorPane ep) {
		if(this.mainWindow.getFileEditorPane().getComponentCount() > 0) {
			for(int i = 0; i < this.mainWindow.getFileEditorPane().getComponentCount(); i++) {
				Component c = this.getMainWindow().getFileEditorPane().getComponentAt(i);
				if(c instanceof FileEditorPane) {
					FileEditorPane e = (FileEditorPane) c;
					if(e.getFile().equals(ep.getFile())) {
						return i;
					}
				}
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


	static DMIDEUI instance;
	public static DMIDEUI getInstance() {
		if(instance == null) {
			instance = new DMIDEUI();
		}
		return instance;
	}
}
