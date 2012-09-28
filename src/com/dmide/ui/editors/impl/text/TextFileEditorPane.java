package com.dmide.ui.editors.impl.text;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.dmide.ui.editors.FileEditorPane;
import com.dmide.util.misc.Misc;

public class TextFileEditorPane extends FileEditorPane {

	@Override
	public void onClose() {
	}

	@Override
	public void createUI() {
		this.textArea = new RSyntaxTextArea(25, 70);
		this.scrollPane = new RTextScrollPane(this.textArea);
		this.setLayout(new BorderLayout());
		this.add(this.scrollPane, BorderLayout.CENTER);
		this.textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
		Misc.ui.removeMappedMenuKeyStrokesFromComponent(this.textArea);
	}

	@Override
	public void onFileChanged() {
		File f = this.getFile();
		if(f != null) {
			String s = Misc.io.readFileString(f);
			if(this.textArea != null) {
				this.textArea.setCaretPosition(0);
				this.textArea.setText(s);
				this.textArea.discardAllEdits();
			}
		}
	}

	@Override
	public void save() {
		try {

			if(!this.getFile().exists()) this.getFile().createNewFile();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		FileWriter fw = null;
		try {

			fw = new FileWriter(this.getFile());
			fw.write(this.textArea.getText());
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fw != null) fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	RSyntaxTextArea textArea;
	RTextScrollPane scrollPane;

}
