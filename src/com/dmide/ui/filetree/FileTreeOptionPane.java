package com.dmide.ui.filetree;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.dmide.DMIDE;
import com.dmide.ui.UIUtil;
import com.dmide.util.events.IDEEvent;
import com.dmide.util.events.IDEEventHandler;
import com.dmide.util.events.IDEEventWatcher;

public class FileTreeOptionPane extends JPanel implements IDEEventWatcher {

	FileTree fileTree;

	public FileTreeOptionPane(FileTree ft) {
		this.fileTree = ft;
		IDEEventHandler.addWatcher(this);
	}

	@Override
	public void eventRecieved(IDEEvent e) {
		final FileTreeOptionPane op = this;
		switch(e.getEventName()) {
		case "ide.windows.loaded":
			Runnable r = new Runnable() {
				@Override
				public void run() {
					op.createPreferencesPage();
				}
			};
			UIUtil.toEventQueue(r);
			break;
		case "prefs.page.opened:appearance":
			this.loadSettings();
			break;
		}
	}

	protected void createPreferencesPage() {
		JTabbedPane tp = DMIDE.getProperty("prefs.page.appearance.tabs", JTabbedPane.class);
		tp.addTab("File Tree", new ImageIcon(this.getClass().getResource("/com/dmide/assets/node.png")), this);
	}

	private void loadSettings() {
	}

}
