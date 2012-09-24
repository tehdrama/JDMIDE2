package com.dmide.ui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.dmide.DMIDE;

public class IDEMainWindowListener implements WindowListener {

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		DMIDEUI.getInstance().saveIDEWindowState();
		DMIDE.save();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

}
