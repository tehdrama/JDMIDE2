package com.dmide.ui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JMenuItem;

import com.dmide.DMIDE;
import com.dmide.IDE;
import com.dmide.compiler.DMCompiler;

/**
 * Sets the actions for the menus.
 * @author Adolph C.
 *
 */
public class DefaultMenuActions {
	public static void setActions() {
		addAction("menus.options.preferences", new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				DMIDEUI.getInstance().getPreferencesDialog().setVisible(true);
			}});

		addAction("menus.file.new", new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				DMIDEUI.getInstance().newFileDialog.setVisible(true);
			}});

		addAction("menus.build.compile", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DMCompiler.getInstance().compile();
			}});

		addAction("menus.file.close", new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				WindowEvent wev = new WindowEvent(DMIDEUI.getInstance().getMainWindow(),
						WindowEvent.WINDOW_CLOSING);
				Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
				DMIDEUI.getInstance().getMainWindow().setVisible(false);
				DMIDEUI.getInstance().getMainWindow().dispose();
			}});
		addAction("menus.file.save", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DMIDE.saveCurrentFile();
			}
		});

		addAction("menus.file.openenvironment", new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				IDE.getInstance().promtOpenDME();
			}});

		addAction("menus.file.saveall", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DMIDE.saveAll();
			}
		});
	}

	private static void addAction(String prop, ActionListener actionL) {
		if(DMIDE.getProperty(prop) != null) {
			if(DMIDE.getProperty(prop) instanceof JMenuItem) {
				System.out.println("Adding action for: " + prop);
				JMenuItem i = (JMenuItem) DMIDE.getProperty(prop);
				i.addActionListener(actionL);
			}
		}
	}
}
