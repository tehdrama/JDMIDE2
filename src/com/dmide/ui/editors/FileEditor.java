package com.dmide.ui.editors;

import java.io.File;

import javax.swing.Icon;

public abstract class FileEditor {
	public Icon getEditorIcon() {return null;}
	public String getEditorName() {return "null";}
	public abstract void edit(File file);
}
