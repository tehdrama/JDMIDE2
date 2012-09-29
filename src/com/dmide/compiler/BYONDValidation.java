package com.dmide.compiler;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import com.dmide.DMIDE;
import com.dmide.ui.DMIDEUI;
import com.dmide.util.WinRegistry;
import com.l2fprod.common.swing.JDirectoryChooser;
import com.l2fprod.common.util.OS;

public class BYONDValidation {

	final String[] commonLocations = new String[] {"C:\\Program Files\\BYOND", "C:\\Program Files (x86)\\BYOND"};
	final String[] userdirfiles = new String[] {"bin"};
	final String[] installationFiles = new String[] {"bin\\", "cfg\\", "help\\"};

	public boolean setBYONDDir() {
		if(DMIDE.getProperty("byond.location") == null) {
			// Check the registry for the BYOND installation location.
			if(!this.checkRegistryInstall()) {
				// Check common folders for the BYOND installation.
				if(!this.checkCommonInstall()) {
					// Prompt the user to enter the path manually.
					if(!this.promptInstallDir()) {
						// If the BYOND installation path still was not found, fail.
						return false;
					}
				}
			}
			
			if (DMIDE.getProperty("byond.location") == null) {
				return false;
			} else {
				String inst = DMIDE.getProperty("byond.location") == null ? "" : ((String) DMIDE.getProperty("byond.location"));
				System.out.println("BYOND Location: " + inst);
				return true;
			}
		}
		return true;
	}

	private boolean checkRegistryInstall() {
		System.out.println("Checking registry...");
		String regVal;
		try {
			regVal = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE,
					"SOFTWARE\\Dantom\\BYOND", "installpath");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if(regVal != null && regVal.length() > 0) {
			DMIDE.saveProperty("byond.location", regVal);
			System.out.println("Compiler Location: " + DMIDE.getProperty("byond.location"));
			return true;
		}
		return false;
	}

	private boolean checkCommonInstall() {
		System.out.println("Checking commons...");
		for(String p : this.commonLocations) {
			File f = new File(p);
			if(f.exists() && f.isDirectory()) {DMIDE.saveProperty("byond.location", f.getPath()); return true;}
		}
		return false;
	}

	private boolean promptInstallDir() {
		System.out.println("Using prompt...");
		int c = JOptionPane.showConfirmDialog(DMIDEUI.getInstance().getMainWindow(),
				"The IDE failed to find your installation of BYOND." +
				"Would you like to locate it manually?\n" +
				"Possible Locations:\n" +
				"- C:\\Program Files\\BYOND\n" +
				"- C:\\Program Files (x86)\\BYOND",
				"Environment Setup Wizard",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE);

		// Find the installation manually by user input.
		if(c == JOptionPane.YES_OPTION) {
			File selectedFile = this.getDirectory();
			if(!this.validateInstallDir(selectedFile)) {
				JOptionPane.showMessageDialog(DMIDEUI.getInstance().getMainWindow(),
						"Attempted to assign an invalid directory as the byond installation location!",
						"Invalid Directory", JOptionPane.ERROR_MESSAGE);
			} else {
				DMIDE.saveProperty("byond.location", selectedFile.getPath());
				return true;
			}
		} else {
			return false;
		}

		return false;
	}

	private boolean validateInstallDir(File d) {
		File[] files = d.listFiles();
		List<File> fs = Arrays.asList(files);
		for(String f : this.installationFiles) {
			File test = new File(d, f);
			if(fs.contains(test)) {continue;}
			return false;
		}
		return true;
	}

	private File getDirectory() {
		DMIDEUI.getInstance().clearDirectoryChooser();
		int r = DMIDEUI.getInstance().getDirChooser().
				showOpenDialog(DMIDEUI.getInstance().getMainWindow());
		if(r == JDirectoryChooser.APPROVE_OPTION) {
			File f = DMIDEUI.getInstance().getDirChooser().getSelectedFile();
			return f;
		} else {
			return null;
		}
	}

	/**
	 * Searches for and assigns the User's BYOND config path.  C:\Users\Chris\Documents\BYOND
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public boolean setProfileDir() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		System.out.println("Checking User Directory...");
		if(DMIDE.getProperty("byond.userdir") == null) {
			if(OS.isWindows()) {

				System.out.println("Checking Registry...");
				String regVal = WinRegistry.readString(WinRegistry.HKEY_CURRENT_USER,
						"Software\\Dantom\\BYOND", "userpath");
				DMIDE.saveProperty("byond.userdir", regVal);
				System.out.println("User Dir: " + DMIDE.getProperty("byond.userdir"));
				return true;

			}

			String _user_dir = JOptionPane.showInputDialog(DMIDEUI.getInstance().getMainWindow(),
					"Please input the path of your BYOND user/profile directory.");
			DMIDE.saveProperty("byond.userdir", _user_dir);
			System.out.println("User Dir: " + DMIDE.getProperty("byond.userdir"));
			return true;

		}
	
		return true;
	
	}
}
