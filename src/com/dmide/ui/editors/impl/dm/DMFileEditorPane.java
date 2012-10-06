package com.dmide.ui.editors.impl.dm;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.dmide.ui.editors.FileEditorPane;
import com.dmide.util.misc.IDEProcess;
import com.dmide.util.misc.Misc;

@SuppressWarnings("serial")
public class DMFileEditorPane extends FileEditorPane implements DocumentListener {

	@Override
	public void onClose() {
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
				this.textArea.getDocument().addDocumentListener(this);
			}
		}
	}

	@Override
	public void save() {
		IDEProcess ideProcess = new IDEProcess("Saving " + this.getFile().getName());
		ideProcess.start();
		ideProcess.setProgress(-1);
		System.out.printf("Saving %s...\n", this.getFile().getPath());

		try {

			if(!this.getFile().exists()) this.getFile().createNewFile();

		} catch (IOException e1) {
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
				e.printStackTrace();
			}
		}
		ideProcess.end();
		this.clearChanges();
		System.out.printf("Saved %s.\n", this.getFile().getPath());
	}

	@Override
	public void createUI() {
		this.textArea = new RSyntaxTextArea(25, 70);
		this.scrollPane = new RTextScrollPane(this.textArea);
		this.setLayout(new BorderLayout());
		this.add(this.scrollPane, BorderLayout.CENTER);
		this.textArea.setSyntaxEditingStyle("text/dm");
		Misc.ui.removeMappedMenuKeyStrokesFromComponent(this.textArea);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		//Adds a change to the file.
		this.change();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		//Adds a change to the file.
		this.change();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		//Adds a change to the file.
		this.change();
	}

	RSyntaxTextArea textArea;
	RTextScrollPane scrollPane;

}
