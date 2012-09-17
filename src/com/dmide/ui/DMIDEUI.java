package com.dmide.ui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.GraphiteAquaSkin;

import com.dmide.ui.windows.IDEMainWindow;

public class DMIDEUI {

	IDEMainWindow mainWindow;

	void setLaf() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel(new SubstanceLookAndFeel(new GraphiteAquaSkin()){
				private static final long serialVersionUID = 1L;});
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	void createMainWindow() {
		final DMIDEUI dmideUI = this;
		Runnable mainWindowCreate = new Runnable() {
			@Override
			public void run() {
				dmideUI.mainWindow = new IDEMainWindow();
				dmideUI.mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				dmideUI.mainWindow.setVisible(true);
			}
		};
		UIUtil.toEventQueue(mainWindowCreate);
	}

	public void generate() {
		this.setLaf();
		this.createMainWindow();
	}


	static DMIDEUI instance;
	public static DMIDEUI getInstance() {
		if(instance == null) {
			instance = new DMIDEUI();
		}
		return instance;
	}
}
