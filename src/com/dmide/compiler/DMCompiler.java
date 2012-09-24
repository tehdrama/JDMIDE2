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

	/**
	 * Checks the location of the compiler.
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
							+ "Is this okay?", "The Quest for BYOND",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (c == JOptionPane.YES_OPTION) {
				if(OS.isWindows()) {
					System.out.println("Checking Registry...");
					String regVal = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE,
							"SOFTWARE\\Dantom\\BYOND", "installpath");
					if(regVal != null && regVal.length() > 0) {
						DMIDE.saveProperty("byond.location", regVal);
						System.out.println("Compiler Location: " + DMIDE.getProperty("byond.location"));
						return;
					}
				}
				c = JOptionPane.showConfirmDialog(DMIDEUI.getInstance().getMainWindow(),
							"The IDE failed to find your installation of BYOND! " +
							"Would you like to locate it manually?\n" +
							"Possible Locations:\n" +
							"- C:\\Program Files\\BYOND\n" +
							"- C:\\Program Files x86\\BYOND",
							"O BYOND, BYOND, Wherefore Art Thou BYOND?",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
				if(c == JOptionPane.YES_OPTION) {
					DMIDEUI.getInstance().getDirChooser().setMultiSelectionEnabled(false);
					DMIDEUI.getInstance().getDirChooser().
						setFileSelectionMode(JDirectoryChooser.FILES_ONLY);
					if(DMIDEUI.getInstance().getDirChooser().
							showDialog(DMIDEUI.getInstance().
									getMainWindow(), "Select") == JDirectoryChooser.APPROVE_OPTION) {
						DMIDE.saveProperty("byond.location", DMIDEUI.getInstance().
								getDirChooser().getSelectedFile().getPath());
						System.out.println("Compiler Location: " + DMIDE.getProperty("byond.location"));
					}
				}
			}
		}
		System.out.println("Compiler Location: " + DMIDE.getProperty("byond.location"));
	}

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

		while((line = reader.readLine()) != null) {
			System.out.println("Read: " + line);
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