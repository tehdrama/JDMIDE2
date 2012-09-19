package com.dmide.files;

import java.util.HashMap;
import java.util.Map;

import com.dmide.ui.editors.FileEditor;
import com.dmide.util.events.IDEEvent;
import com.dmide.util.events.IDEEventWatcher;

public class FileOpenHandler implements IDEEventWatcher {
	static Map<String, FileEditor> fileExtensionAssoc;

	static {
		fileExtensionAssoc = new HashMap<String, FileEditor>();
	}

	public static void setEditorAssociation(String extension, FileEditor editor) {
		fileExtensionAssoc.put(extension, editor);
	}

	@Override
	public void eventRecieved(IDEEvent e) {
		switch(e.getEventName()) {
		case "file.tree.openfile":
			System.out.println("Opening file from tree: " + e.getArgument());
			break;
		}
	}

	static FileOpenHandler instance;
	public static FileOpenHandler getInstance() {
		if(instance == null) {instance = new FileOpenHandler();}
		return instance;
	}
}
