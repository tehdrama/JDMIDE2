package com.dmide.ui;

import javax.swing.KeyStroke;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

/**
 * A revised Syntax text area to remove some weird key bindings and change them.
 * @author Adolph C.
 *
 */
@SuppressWarnings("serial")
public class DMSyntaxTextArea extends RSyntaxTextArea {
	public DMSyntaxTextArea() {
		super();
		this.getInputMap().put(KeyStroke.getKeyStroke("ctrl K"), "none");
	}

	public DMSyntaxTextArea(int i, int j) {
		super(i, j);
		this.getInputMap().put(KeyStroke.getKeyStroke("ctrl K"), "none");
	}
}
