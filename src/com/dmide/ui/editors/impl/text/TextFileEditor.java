package com.dmide.ui.editors.impl.text;

import java.io.File;

import com.dmide.ui.DMIDEUI;
import com.dmide.ui.editors.FileEditor;

public class TextFileEditor extends FileEditor {

	@Override
	public String getEditorName() {
		return "DM Editor";
	}

	@Override
	public void edit(File file) {
		System.out.println("Editing Text File: " + file.getName());
		TextEditorPane editorPane = new TextEditorPane();
		editorPane.setFile(file);
		DMIDEUI.getInstance().addTab(editorPane);
	}

}
