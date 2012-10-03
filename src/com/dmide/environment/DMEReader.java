package com.dmide.environment;

import java.io.IOException;
import java.util.Scanner;


public class DMEReader {
	DMEnvironment environment;

	static final byte mode_DEFAULT = 0;
	static final byte mode_INTERNALS = 1;
	static final byte mode_FILE_DIRS = 2;
	static final byte mode_PREFERENCES = 3;
	static final byte mode_INCLUDES = 4;

	byte mode = mode_DEFAULT;

	public DMEReader(DMEnvironment dmEnv) {
		this.environment = dmEnv;
	}

	public void read() {
		Scanner scanner = null;
		try {
			scanner = new Scanner(this.environment.getFile());
			while(scanner.hasNextLine()) {
				this.processLine(scanner.nextLine());
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(scanner != null) scanner.close();
		}
	}

	/**
	 * Processes each line of the DME. Sets the mode using
	 * {@link DMEReader#setMode(byte)} when necessary, and controls
	 * the settings of the current DME.
	 * @param line
	 */
	public void processLine(String line) {
		line = line.trim(); // Remove surrounding whitespace 'n shit.
		if(line.length() < 2) {return;} // we dun want it.
		switch(this.mode) {
		case mode_DEFAULT:
			if(line.endsWith("BEGIN_INTERNALS")) this.setMode(mode_INTERNALS);
			if(line.endsWith("BEGIN_FILE_DIR")) this.setMode(mode_FILE_DIRS);
			if(line.endsWith("BEGIN_PREFERENCES")) this.setMode(mode_PREFERENCES);
			if(line.endsWith("BEGIN_INCLUDE")) this.setMode(mode_INCLUDES);
			break;
		case mode_FILE_DIRS:
			if(line.endsWith("END_FILE_DIR")) {this.setMode(mode_DEFAULT);break;}
			break;
		case mode_INCLUDES:
			if(line.endsWith("END_INCLUDE")) {this.setMode(mode_DEFAULT);break;}
			if(line.startsWith("#include"))
				this.environment.addInclude(DMEnvironmentInclude.parseInclude(line.substring(8)));
			break;
		case mode_INTERNALS:
			if(line.endsWith("END_INTERNALS")) {this.setMode(mode_DEFAULT);break;}
			break;
		case mode_PREFERENCES:
			if(line.endsWith("END_PREFERENCES")) {this.setMode(mode_DEFAULT);break;}
			break;
		}
	}

	public void setMode(byte newmode) {
		this.mode = newmode;
	}
}
