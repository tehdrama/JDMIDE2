package com.dmide.ui.editors;

import com.dmide.files.FileOpenHandler;
import com.dmide.ui.editors.impl.dm.DMFileEditor;

/**
 * @author Adolph C.
 *
 */
public class DefaultEditors {
	public static void addDefaultEditors() {
		FileOpenHandler.setEditorAssociation("dm", new DMFileEditor());
	}
}
