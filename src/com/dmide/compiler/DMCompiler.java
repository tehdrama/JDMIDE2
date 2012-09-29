package com.dmide.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;

import com.dmide.DMIDE;
import com.dmide.environment.DMEnvironment;
import com.dmide.ui.DMIDEUI;
import com.dmide.ui.UIUtil;
import com.dmide.util.WinRegistry;
import com.dmide.util.events.IDEEvent;
import com.dmide.util.events.IDEEventHandler;
import com.dmide.util.events.IDEEventWatcher;
import com.dmide.util.misc.IDEProcess;
import com.l2fprod.common.swing.JDirectoryChooser;
import com.l2fprod.common.util.OS;

public class DMCompiler implements IDEEventWatcher {

	boolean compiling = false;
	final String[] commonLocations = new String[] {"C:\\Program Files\\BYOND", "C:\\Program Files (x86)\\BYOND"};

	/**
	 * Searches for and assigns the BYOND installation path.
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void check() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		System.out.println("Checking Compiler...");
		if (DMIDE.getProperty("byond.location") == null) {
			
			int c = JOptionPane.showConfirmDialog(DMIDEUI.getInstance()
					.getMainWindow(),
					"The IDE would like to search for your installation of BYOND. "
							+ "Proceed?", "The Quest for BYOND",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (c == JOptionPane.YES_OPTION) {
				if(OS.isWindows()) {
		
					// Checks windows registry for installation path.
					System.out.println("Checking Registry...");
					String regVal = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE,
							"SOFTWARE\\Dantom\\BYOND", "installpath");
					if(regVal != null && regVal.length() > 0) {
						if (verifyInstallationDirectory(regVal) == true) {
							DMIDE.saveProperty("byond.location", regVal);
							System.out.println("Compiler Location: " + DMIDE.getProperty("byond.location"));
							JOptionPane.showMessageDialog(DMIDEUI.getInstance().getMainWindow(),
									String.format("Found BYOND directory at%s.", DMIDE.getProperty("byond.location")));
							return;
						}
					}
					
					// Checks common folders for installation.
					for (String path : commonLocations) {
						if (verifyInstallationDirectory(path) == true) {
							DMIDE.saveProperty("byond.location", path);
							System.out.println("Compiler Location: " + DMIDE.getProperty("byond.location"));
							JOptionPane.showMessageDialog(DMIDEUI.getInstance().getMainWindow(),
									String.format("Found BYOND directory at %s.", DMIDE.getProperty("byond.location")));
							return;
						}
					}
					
					
				}
				c = JOptionPane.showConfirmDialog(DMIDEUI.getInstance().getMainWindow(),
							"The IDE failed to find your installation of BYOND! " +
							"Would you like to locate it manually?\n" +
							"Possible Locations:\n" +
							"- C:\\Program Files\\BYOND\n" +
							"- C:\\Program Files (x86)\\BYOND",
							"O BYOND, BYOND, Wherefore Art Thou BYOND?",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE);

				// Find the installation manually by user input.
				if(c == JOptionPane.YES_OPTION) {
					
					String selectedPath = null;
					
					while(selectedPath == null) {
					
						DMIDEUI.getInstance().getDirChooser().setMultiSelectionEnabled(false);
						DMIDEUI.getInstance().getDirChooser().
							setFileSelectionMode(JDirectoryChooser.FILES_ONLY);
						if(DMIDEUI.getInstance().getDirChooser().
								showDialog(DMIDEUI.getInstance().
										getMainWindow(), "Select") == JDirectoryChooser.APPROVE_OPTION) {
							
							File selectedFile = DMIDEUI.getInstance().getDirChooser().getSelectedFile();
							if (verifyInstallationDirectory(selectedFile.getPath()) == true) {
								// The path they selected is valid.
								selectedPath = selectedFile.getPath();
								DMIDE.saveProperty("byond.location", DMIDEUI.getInstance().
										getDirChooser().getSelectedFile().getPath());
								System.out.println("Compiler Location: " + DMIDE.getProperty("byond.location"));
							} else {
								// Invalid path, we should prompt them again.
								JOptionPane.showMessageDialog(DMIDEUI.getInstance().getMainWindow(),
										"That folder is not a valid BYOND installation directory.");
								selectedPath = null;
							}
							
						}
						
					}
				}
			}
		}
		
		System.out.println("Compiler Location: " + DMIDE.getProperty("byond.location"));
	}
	
	/**
	 * Ensures that the path given as the installation directory is valid.
	 * @param installationPath
	 * @return
	 */
	private boolean verifyInstallationDirectory(String installationPath) {
		// Verify that the path given is an actual BYOND installation.
		
		File checkValidPath = new File(installationPath);
		if (checkValidPath.exists() && checkValidPath.isDirectory()) {
			
			// Verify that the \bin folder exists.
			File checkValidPath1 = new File(installationPath + "\\bin");
			if (!checkValidPath1.exists() || !checkValidPath1.isDirectory()) {
				return false;
			}
			
			// Verify that the \\cfg folder exists.
			File checkValidPath2 = new File(installationPath + "\\cfg");
			if (!checkValidPath2.exists() || !checkValidPath2.isDirectory()) {
				return false;
			}
			
			// Verify that the \\help folder exists.
			File checkValidPath3 = new File(installationPath + "\\help");
			if (!checkValidPath3.exists() || !checkValidPath3.isDirectory()) {
				return false;
			}
			
			// Verify that the uninstall file exists.
			File checkValidPath4 = new File(installationPath + "\\uninst.exe");
			if (!checkValidPath4.exists() || !checkValidPath4.isFile()) {
				return false;
			}
			
			// The installation appears to be valid.
			return true;
		}
		
		return false;
	}

	/**
	 * Searches for and assigns the User's BYOND config path.  C:\Users\Chris\Documents\BYOND
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void userCheck() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		System.out.println("Checking User Directory...");
		if(DMIDE.getProperty("byond.userdir") == null) {
			if(OS.isWindows()) {
				
				System.out.println("Checking Registry...");
				String regVal = WinRegistry.readString(WinRegistry.HKEY_CURRENT_USER,
						"Software\\Dantom\\BYOND", "userpath");
				DMIDE.saveProperty("byond.userdir", regVal);
				System.out.println("User Dir: " + DMIDE.getProperty("byond.userdir"));
				return;
				
			}

			String _user_dir = JOptionPane.showInputDialog(DMIDEUI.getInstance().getMainWindow(),
					"Please input the path of your BYOND user directory.");
			DMIDE.saveProperty("byond.userdir", _user_dir);
			System.out.println("User Dir: " + DMIDE.getProperty("byond.userdir"));

		}
	}

	/**
	 * Compiles the current environment, calls _compile on a separate thread.
	 */
	public void compile() {
		Runnable compileRunner = new Runnable() {
			@Override
			public void run() {
				try {
					DMCompiler.getInstance()._compile();
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		Thread compileThread = new Thread(compileRunner, "Compile");
		compileThread.start();
	}

	public File getDMCompiler() {
		if(DMIDE.getProperty("byond.location") != null) {
			String byondlocation = (String) DMIDE.getProperty("byond.location");
			File c = new File(byondlocation, "bin/dm.exe");
			if(c.exists()) return c;
		}
		return null;
	}

	void _compile() throws IOException, InterruptedException {
		if(DMEnvironment.getDMEFile() == null || !DMEnvironment.getDMEFile().exists()) {
			return;
		}
		IDEProcess idecp = new IDEProcess("Compiling");
		idecp.start();
		idecp.setProgress(-1);
		IDEEventHandler.sendIDEEvent(new IDEEvent("compiling.start", this));
		File compilerFile = this.getDMCompiler();
		if(compilerFile == null) {
			System.out.println("Failed to find compiler!");
			JOptionPane.showMessageDialog(DMIDEUI.getInstance().getMainWindow(),
					String.format("Failed to find bin/dm.exe in directory: %s.",
							(String) DMIDE.getProperty("byond.location")),
					"Failed to locate compiler", JOptionPane.ERROR_MESSAGE);
			return;
		}
		System.out.println("Compiler: " + compilerFile.getPath());

		ProcessBuilder builder = new ProcessBuilder(compilerFile.getPath(), "-o",
				DMEnvironment.getDMEFile().getPath());
		builder.redirectErrorStream(true);
		Process process = builder.start();
		InputStream stdout = process.getInputStream ();
		BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
		String line;

		File tf = new File("test.txt");
		tf.createNewFile();
		FileWriter fw = new FileWriter(tf);

		/*
		 * modes:
		 * 0 - COMPILER MESSAGE MODE
		 * 1 - OBJECT TREE MODE
		 */
		int mode = 0;

		while((line = reader.readLine()) != null) {
			if(line.length() < 1) {mode++; continue;}
			if(mode == 0) {
				IDEEventHandler.sendIDEEvent(new IDEEvent("compiling.message",
						CompilerMessage.fromString(line)));
			}
			fw.write(line.concat("\n"));
		}

		fw.close();
		process.waitFor();

		IDEEventHandler.sendIDEEvent(new IDEEvent("compiling.end", this));
		idecp.end();
	}

	/*
	 *
	 * HKEY_LOCAL_MACHINE\SOFTWARE\Dantom\BYOND
	 *
	 * var: installpath
	 *
	 * HKEY_CURRENT_USER\Software\Dantom\BYOND
	 *
	 * var: userpath
	 */

	static DMCompiler instance;

	public static DMCompiler getInstance() {
		if (instance == null)
			instance = new DMCompiler();
		return instance;
	}

	@Override
	public void eventRecieved(IDEEvent e) {
		if (e.getEventName().equals("ide.windows.loaded")) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						DMCompiler.getInstance().check();
						DMCompiler.getInstance().userCheck();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			};
			UIUtil.toEventQueue(r);
		}
	}
}
