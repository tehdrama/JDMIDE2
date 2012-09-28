package com.dmide.environment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.dmide.DMIDE;
import com.dmide.ui.DMIDEUI;
import com.dmide.util.events.IDEEvent;
import com.dmide.util.events.IDEEventHandler;

public class DMEnvironment {
	static File dmeFile;
	static ArrayList<File> recentEnvironments;

	static {
		recentEnvironments = new ArrayList<File>();
	}

	public static File getWorkingDirectory() {
		if(dmeFile != null) return dmeFile.getParentFile();
		return null;
	}

	public static void setDMEFile(File f) {
		dmeFile = f;
		System.out.println("Setting DME: " + f.getPath());
		IDEEventHandler.sendIDEEvent(new IDEEvent("environment.open", dmeFile));
		addRecentEnvironment(dmeFile);
	}

	public static File getDMEFile() {
		return dmeFile;
	}

	public static void saveRecentEnvironments() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(File f : recentEnvironments) {
			if(!first) {sb.append("|");}
			else {first = false;}
			sb.append(f.getPath());
		}
		DMIDE.saveProperty("environment.recents", sb.toString());
	}

	public static void loadRecentEnvironments() {
		recentEnvironments.clear();
		if(DMIDE.getProperty("environment.recents") != null) {
			String s = (String) DMIDE.getProperty("environment.recents");
			if(s.contains("|")) {
				String[] r = s.split("|");
				for(String _s : r) {
					recentEnvironments.add(new File(_s));
				}
			} else {
				recentEnvironments.add(new File(s));
			}
		}
	}

	public static void addRecentEnvironment(File nrec) {
		while(recentEnvironments.contains(nrec)) {recentEnvironments.remove(nrec);}
		if(DMIDE.getProperty("environment.maxrecents") == null) {
			DMIDE.saveProperty("environment.maxrecents", "5");
		}
		int mrecents = Integer.parseInt((String) DMIDE.getProperty("environment.maxrecents"));
		while(recentEnvironments.size() > 0 && recentEnvironments.size() > mrecents)
			{recentEnvironments.remove(recentEnvironments.size() - 1);}
		recentEnvironments.add(nrec);
		createRecentsMenu();
	}



	public static void createRecentsMenu() {
		if(DMIDE.getProperty("environment.maxrecents") == null) {
			DMIDE.saveProperty("environment.maxrecents", "5");
		}
		int mrecents = Integer.parseInt((String) DMIDE.getProperty("environment.maxrecents"));
		JMenu recentEnvironmentMenu;

		if(DMIDE.getProperty("menus.file.recentenvironment") == null) {
			System.out.println("Recent environment menu not found!");
			return;
		}
		if(!(DMIDE.getProperty("menus.file.recentenvironment") instanceof JMenu)) {
			System.out.println("Recent environment menu is of the wrong type!");
			return;
		}
		recentEnvironmentMenu = (JMenu) DMIDE.getProperty("menus.file.recentenvironment");

		recentEnvironmentMenu.removeAll();

		int count = 0;

		for(int i = recentEnvironments.size() - 1; i >= 0; i--) {
			File f = recentEnvironments.get(i);
			JMenuItem env_item = new JMenuItem();
			env_item.setText(f != null ? f.getName() : "-null-");
			env_item.setToolTipText(f != null ? f.getPath() : "-null-");
			final File _f = f;
			env_item.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if(_f != null && _f.exists()) DMEnvironment.setDMEFile(_f);
					else {JOptionPane.showMessageDialog(DMIDEUI.getInstance().getMainWindow(),
							"Failed to locate specified DME file!", "DME Not found!",
							JOptionPane.ERROR_MESSAGE);}
				}});
			recentEnvironmentMenu.add(env_item);
			count ++;
			if(count >= mrecents) {break;}
		}

	}
}
