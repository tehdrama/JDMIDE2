package com.dmide.ui.editors;

import java.io.File;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.dmide.ui.DMIDEUI;
import com.dmide.ui.TabbedPaneUI;
import com.dmide.ui.TabbedPaneUIHeader;
import com.dmide.util.misc.Misc;

@SuppressWarnings("serial")
public abstract class FileEditorPane extends JPanel implements TabbedPaneUI {
	File file;
	int changes = 0;
	TabbedPaneUIHeader tabHeader;

	@Override
	public void setHeader(TabbedPaneUIHeader header) {
		this.tabHeader = header;
	}

	/**
	 *
	 * @return the tab header of thsi component.
	 */
	public TabbedPaneUIHeader getTabHeader() {
		return this.tabHeader;
	}

	public FileEditorPane() {
		this.createUI();
	}

	/**
	 * Called to create the UI of the editor.
	 */
	public abstract void createUI();

	/**
	 *
	 * @return the file of this editor.
	 */
	public File getFile() {return this.file;}

	/**
	 * Sets the file of this editor.
	 *
	 * @param f
	 */
	public void setFile(File f) {this.file = f; this.onFileChanged();}

	/**
	 * Called when the file of this editor is changed / set.
	 */
	public abstract void onFileChanged();

	/**
	 *
	 * @return the icon of this editor, returns the file's icon if possible by
	 *         default.
	 */
	public Icon getIcon() {return this.file != null ? Misc.io.getFileTypeIcon(this.file) : null;}

	/**
	 *
	 * @return returns the name of this editor's file by default, "null" (the
	 *         string) if a file does not exist.
	 */
	public String getTitle() {return this.file != null ? this.file.getName() : "null";}

	/**
	 * Called when the file in the editor has been changed.
	 */
	public void change() {this.changes++;}

	/**
	 * Called when a change has been undone in the editor.
	 */
	public void removeChange() {this.changes--;}

	/**
	 *
	 * @return the number of changes that have taken place.
	 */
	public int getChanges() {return this.changes;}

	/**
	 * Clears changes from the file, call after save.
	 */
	public void clearChanges() {this.changes = 0;}

	/**
	 * Called to save this file.
	 */
	public abstract void save();

	/**
	 * Swaps the file of the editor and saves.
	 * @param newfile
	 */
	public void saveAs(File newfile){this.file = newfile; this.save();}


	@Override
	public Icon getTabIcon() {return this.getIcon();}

	@Override
	public boolean displayTabCloseButton() {return true;}


	@Override
	public String getTabTitle() {return this.getTitle();}

	@Override
	public Object getIdentifier() {return this.getFile();}

	/**
	 * Will attempt to display an option dialog to allow the user to save first,
	 * or cancel altogether.
	 */
	@Override
	public boolean canCloseTab() {
		if(this.file != null && this.getChanges() > 0) {
			JOptionPane.showOptionDialog(DMIDEUI.getInstance().getMainWindow(),
					String.format("Are you sure you would like to close %s without saving?", this.file.getName()),
					"Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE,
					null, null, null);
			return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj) && obj instanceof FileEditorPane) {
			FileEditorPane e = (FileEditorPane) obj;
			return e.getFile().equals(this.getFile());
		} else return false;
	}
}
