package com.dmide.ui.filetree;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JTree;

import com.dmide.util.IDEEvent;
import com.dmide.util.IDEEventWatcher;
import com.dmide.util.IDEFile;

@SuppressWarnings("serial")
public class FileTree extends JTree implements IDEEventWatcher {

	ArrayList<IDEFile> ideFiles = new ArrayList<>();

	public void setEnvironment(File dmeFile) {
		if(dmeFile == null) return; // This means that the argument for the environment.open
									// event was not a file.

	}

	/**
	 * Creates a new instance of the file tree.
	 */
	public FileTree() {
		this.setTreeCellProperties();
	}

	/**
	 * Sets the TreeCell Renderer and Editor.
	 */
	public void setTreeCellProperties() {
		this.setCellRenderer(new FileTreeCellRenderer());
	}

	@Override
	public void eventRecieved(IDEEvent e) {
		if(e.getEventName().equals("environment.open")) {
			e.setHandled(true);
			this.setEnvironment(e.getArgument(File.class));
		}
	}
}
