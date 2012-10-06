package com.dmide.environment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;

import com.dmide.DMIDE;
import com.dmide.ui.DMIDEUI;
import com.dmide.util.IDEFileComparator;
import com.dmide.util.events.IDEEvent;
import com.dmide.util.events.IDEEventHandler;

public class DMEnvironment {
	static ArrayList<File> recentEnvironments;
	static DMEnvironment instance;
	File dmeFile;
	ArrayList<DMEnvironmentInclude> includes = new ArrayList<>();
	public static IDEFileComparator ideFileComparator = new IDEFileComparator();

	boolean DEBUG_MODE = false;
	boolean FILEDIR_MODE = false;

	public void build() {
		DMEWriter writer = this.createWriter();
		writer.write();
		String dmeString = writer.build();
		FileWriter fw = null;
		try {
			fw = new FileWriter(this.dmeFile);
			fw.write(dmeString);
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

	public boolean fileDirOn() {return this.FILEDIR_MODE;}
	public boolean debugOn() {return this.DEBUG_MODE;}

	public static void buildDME() {
		if(getInstance() != null) getInstance().build();
	}

	/**
	 * @return the includes
	 */
	public ArrayList<DMEnvironmentInclude> getIncludes() {
		return this.includes;
	}

	/**
	 * @param includes the includes to set
	 */
	public void setIncludes(ArrayList<DMEnvironmentInclude> includes) {
		this.includes = includes;
	}

	/**
	 * Removes the specified {@link DMEnvironmentInclude} from this environment's
	 * list of includes.
	 * @param inc
	 */
	public void removeInclude(DMEnvironmentInclude inc) {
		while(this.includes.contains(inc)) this.includes.remove(inc);
	}

	public boolean findInclude(File f) {
		for(DMEnvironmentInclude include : this.includes) {
			if(include.getFile().equals(f)) {return true;}
		}
		return false;
	}

	/**
	 * Adds the specified {@link DMEnvironmentInclude} to the environment's
	 * list of includes.
	 * @param inc
	 */
	public void addInclude(DMEnvironmentInclude inc) {
		if(!this.includes.contains(inc)) this.includes.add(inc);
	}

	/**
	 * Clears this environment's includes.
	 */
	public void clearIncludes() {
		this.includes.clear();
	}

	/**
	 * Creates a DMEnvironment using the specified file.
	 * @param f
	 */
	protected DMEnvironment(File f) {
		this.dmeFile = f;
	}

	/**
	 *
	 * @return a new {@link DMWriter} for this environment.
	 */
	public DMEWriter createWriter() {
		return new DMEWriter(this);
	}

	/**
	 *
	 * @return a new {@link DMReader} for this environment.
	 */
	public DMEReader createReader() {
		return new DMEReader(this);
	}

	/**
	 *
	 * @return the file of this DMEnvironment/
	 */
	public File getFile() {
		return this.dmeFile;
	}

	//---Static Methods:

	/**
	 * Creates a DMEnvironment using the file specified.
	 * @param f
	 */
	private static void newInstance(File f) {
		instance = new DMEnvironment(f);
		instance.createReader().read();
		IDEEventHandler.watchDirectory(f);
	}

	/**
	 *
	 * @return the current instance of DMEnvironment if there is one.
	 */
	public static DMEnvironment getInstance() {
		return instance;
	}

	static {
		recentEnvironments = new ArrayList<File>();
	}

	/**
	 * Closes the current environment by calling {@link DMEnvironment#closeEnv()} for
	 * the currently loaded DME.
	 */
	public static void closeEnvironment() {
		if(getInstance() != null) getInstance().closeEnv();
	}

	/**
	 * Closes the current environment by calling {@link DMEnvironment#saveProjectSettings()}
	 */
	public void closeEnv() {
		try {
			this.saveProjectSettings();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Saves the settings of the current DME,
	 * Also sends the 'environment.settings.saving' event
	 * using the {@link Properties} holding the info as the argument,
	 * before the process of saving is finished.
	 * </p>
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void saveProjectSettings() throws FileNotFoundException, IOException  {
		if(this.getWorkingDir() == null) return;
		Properties saveProperties = new Properties();
		saveProperties.put("mode.debug", Boolean.toString(this.DEBUG_MODE));
		saveProperties.put("mode.filedir", Boolean.toString(this.FILEDIR_MODE));
		IDEEventHandler.sendIDEEvent(new IDEEvent("environment.settings.saving", saveProperties));
		File savingFile = new File(this.getWorkingDir(), ".jdmide-p");
		if(!savingFile.exists()) savingFile.createNewFile();
		saveProperties.storeToXML(new FileOutputStream(savingFile),
				String.format("Properties for Environment: ", FilenameUtils.getBaseName(this.getFile().getName())));
	}

	/**
	 *
	 * @return the current working directory (the parent of the current DME file) if there is one.
	 */
	public File getWorkingDir() {
		return this.getFile() != null ? this.getFile().getParentFile() : null;
	}

	/**
	 *
	 * @return the current working directory (the parent of the current DME file) if there is one.
	 */
	public static File getWorkingDirectory() {
		if(getInstance().dmeFile != null) return getInstance().dmeFile.getParentFile();
		return null;
	}

	/**
	 * Changes the DME being edited by the IDE. Calls close environment
	 * to save the settings for the current DME if there is one.
	 * @param f
	 */
	public static void setDMEFile(File f) {
		closeEnvironment();
		newInstance(f);
		System.out.println("Setting DME: " + f.getPath());
		IDEEventHandler.sendIDEEvent(new IDEEvent("environment.open", getInstance()));
		addRecentEnvironment(getInstance().dmeFile);
	}

	/**
	 *
	 * @return the current DME file being edited by the IDE or null if one does
	 *         not exist.
	 */
	public static File getDMEFile() {
		if(getInstance() != null) return getInstance().dmeFile;
		else return null;
	}

	/**
	 * Saves the list of recent environments in a text format
	 * inside of the saved property "environment.recents".
	 */
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

	/**
	 * Adds the recent environments stored in the "environment.recents"
	 * to the list of recent environments.
	 */
	public static void loadRecentEnvironments() {
		recentEnvironments.clear();
		if(DMIDE.getProperty("environment.recents") != null) {
			String s = (String) DMIDE.getProperty("environment.recents");
			if(s.contains("|")) {
				String[] r = s.split("\\|");
				for(String _s : r) {
					if(_s == null || _s.length() < 1) continue;
					System.out.println("Found: " + _s);
					recentEnvironments.add(new File(_s));
				}
			} else {
				recentEnvironments.add(new File(s));
			}
		}
	}

	/**
	 * Used to add an environment to the recent environments
	 * list of the IDE. If there are too many recent environments
	 * in the list of recent environments, the oldest entry is deleted.
	 * The maximum number of recent environments allowed is stored in the
	 * saved property "environment.maxrecents".
	 * @param nrec
	 */
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



	/**
	 * Adds the recent environments to the
	 * "Recent Environments" menu. This method retrieves the
	 * recent environments menu using the "menus.file.recentenvironment"
	 * property of the IDE. The "environment.maxrecents" is used
	 * to set how many items are allowed to exist in the Recent Environments
	 * menu at the same time, if said property does not exist, it will be created.
	 *
	 * @see DMEnvironment#addRecentEnvironment(File)
	 */
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
