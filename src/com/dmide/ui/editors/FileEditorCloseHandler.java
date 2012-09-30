package com.dmide.ui.editors;

import java.io.File;

import javax.swing.JOptionPane;

import com.dmide.ui.DMIDEUI;
import com.dmide.ui.tabs.TabCloseHandler;
import com.dmide.ui.tabs.TabbedPaneUI;

public class FileEditorCloseHandler implements TabCloseHandler {

	static final int CANCEL_TAB_CLOSE = 0;
	static final int CLOSE_TAB_NO_SAVE = 1;
	static final int CLOSE_TAB_SAVE = 2;

	@Override
	public boolean canClose(TabbedPaneUI ui) {
		if(ui != null && (ui instanceof FileEditorPane)) {
			FileEditorPane fep = (FileEditorPane) ui;
			if(fep.getChanges() > 0) {
				int request = this.requestCloseFile(fep.getFile());
				switch(request) {
				case CLOSE_TAB_SAVE:
					fep.save();
					break;
				case CANCEL_TAB_CLOSE:
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void onClose(TabbedPaneUI ui) {
	}

	/**
	 * Asks the user if they want to save the file or close with unsaved changes.
	 */
	public int requestCloseFile(File f) {
		int r = JOptionPane.showConfirmDialog(DMIDEUI.getInstance().getMainWindow(),
				String.format("'%s' has been modified. Would you like to save your changes?", f.getName()),
				"Closing Unsaved File", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		switch(r) {
		case JOptionPane.CANCEL_OPTION:
			return CANCEL_TAB_CLOSE;
		case JOptionPane.YES_OPTION:
			return CLOSE_TAB_SAVE;
		default:
			return CLOSE_TAB_NO_SAVE;
		}
	}
}
