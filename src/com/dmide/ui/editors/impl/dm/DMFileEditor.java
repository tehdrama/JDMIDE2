package com.dmide.ui.editors.impl.dm;

import java.io.File;

import com.dmide.DMIDE;
import com.dmide.ui.DMIDEUI;
import com.dmide.ui.editors.FileEditor;

public class DMFileEditor extends FileEditor {

	@Override
	public String getEditorName() {
		return "DM Editor";
	}

	@Override
	public void edit(File file) {
		System.out.println("Editing DM File: " + file.getName());
		DMFileEditorPane editorPane = new DMFileEditorPane();
		editorPane.setFile(file);

		DMIDE.trackEditor(editorPane);
		DMIDEUI.getInstance().addTab(editorPane);
	}

}
