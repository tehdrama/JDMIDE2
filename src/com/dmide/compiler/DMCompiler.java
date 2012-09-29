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
import com.dmide.util.events.IDEEvent;
import com.dmide.util.events.IDEEventHandler;
import com.dmide.util.events.IDEEventWatcher;
import com.dmide.util.misc.IDEProcess;

public class DMCompiler implements IDEEventWatcher {

	boolean compiling = false;

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

				}
			};
			UIUtil.toEventQueue(r);
		}
	}
}
