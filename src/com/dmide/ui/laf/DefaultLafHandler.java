package com.dmide.ui.laf;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class DefaultLafHandler extends LafHandler {

	@Override
	public void setLaf() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

}
