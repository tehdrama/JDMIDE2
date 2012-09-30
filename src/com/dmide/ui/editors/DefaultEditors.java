package com.dmide.ui.editors;

import com.dmide.files.FileOpenHandler;
import com.dmide.ui.editors.impl.dm.DMFileEditor;
import com.dmide.ui.editors.impl.text.TextFileEditor;

/**
 * @author Adolph C.
 *
 */
public class DefaultEditors {
	public static void addDefaultEditors() {
		FileOpenHandler.setEditorAssociation("dm", new DMFileEditor());
		FileOpenHandler.setEditorAssociation("dme", new DMFileEditor());
		FileOpenHandler.setEditorAssociation("txt", new TextFileEditor());
	}
}
