package com.dmide.files;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.dmide.ui.editors.DefaultEditors;
import com.dmide.ui.editors.FileEditor;
import com.dmide.util.events.IDEEvent;
import com.dmide.util.events.IDEEventWatcher;

public class FileOpenHandler implements IDEEventWatcher {
	static Map<String, FileEditor> fileExtensionAssoc;

	static {
		fileExtensionAssoc = new HashMap<String, FileEditor>();
		DefaultEditors.addDefaultEditors();
	}

	public static void setEditorAssociation(String extension, FileEditor editor) {
		fileExtensionAssoc.put(extension, editor);
	}

	@Override
	public void eventRecieved(IDEEvent e) {
		switch(e.getEventName()) {
		case "file.tree.openfile":
			this.tryOpenFile(e.getArgument(File.class));
			e.setHandled(true);
			break;
		}
	}

	private void tryOpenFile(File argument) {
		if(argument == null) return;
		String ext = FilenameUtils.getExtension(argument.getName());
		if(fileExtensionAssoc.containsKey(ext)) {fileExtensionAssoc.get(ext).edit(argument);}
	}

	static FileOpenHandler instance;
	public static FileOpenHandler getInstance() {
		if(instance == null) {instance = new FileOpenHandler();}
		return instance;
	}
}
