package com.dmide.environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Scanner;

public class DMEWriter {
	DMEnvironment environment;
	StringBuilder stringBuilder;

	boolean wrote_INTERNALS = false;
	boolean wrote_FILE_DIRS = false;
	boolean wrote_PREFERENCES = false;
	boolean wrote_INCLUDES = false;

	boolean nowrite = false;


	static final byte mode_DEFAULT = 0;
	static final byte mode_INTERNALS = 1;
	static final byte mode_FILE_DIRS = 2;
	static final byte mode_PREFERENCES = 3;
	static final byte mode_INCLUDES = 4;

	byte mode = mode_DEFAULT;

	static final String linebreak = "\n";


	public DMEWriter(DMEnvironment dmEnv) {
		this.environment = dmEnv;
	}

	/**
	 * Reads the DME and writes data to the appropriate
	 * locations.
	 */
	public void write() {
		this.stringBuilder = new StringBuilder();
		Scanner scanner = null;
		try {
			scanner = new Scanner(this.environment.getFile());
			while(scanner.hasNextLine()) {
				this.readLine(scanner.nextLine().trim());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}

		this.finalWrites();
	}

	/**
	 * Processes lines from the write function.
	 * @param line
	 */
	public void readLine(String line) {
		if(!this.nowrite) {this.stringBuilder.append(line); this.stringBuilder.append(linebreak);}

		switch(this.mode) {
		case mode_DEFAULT:
			if(line.endsWith("BEGIN_INTERNALS")) this.setMode(mode_INTERNALS);
			if(line.endsWith("BEGIN_FILE_DIR")) this.setMode(mode_FILE_DIRS);
			if(line.endsWith("BEGIN_PREFERENCES")) this.setMode(mode_PREFERENCES);
			if(line.endsWith("BEGIN_INCLUDE")) this.setMode(mode_INCLUDES);
			break;
		case mode_FILE_DIRS:
			if(!this.wrote_FILE_DIRS) this.writeFileDirs();
			if(line.endsWith("END_FILE_DIR")) {this.setMode(mode_DEFAULT);this.addLine(line);break;}
			break;
		case mode_INCLUDES:
			if(!this.wrote_INCLUDES) this.writeIncludes();
			if(line.endsWith("END_INCLUDE")) {this.setMode(mode_DEFAULT);this.addLine(line);break;}
			break;
		case mode_INTERNALS:
			if(!this.wrote_INTERNALS) this.writeInternals();
			if(line.endsWith("END_INTERNALS")) {this.setMode(mode_DEFAULT);this.addLine(line);break;}
			break;
		case mode_PREFERENCES:
			if(!this.wrote_PREFERENCES) this.writePreferences();
			if(line.endsWith("END_PREFERENCES")) {this.setMode(mode_DEFAULT);this.addLine(line);break;}
			break;
		}
	}

	/**
	 * Sets the mode of the Writer.
	 * @param newmode
	 */
	public void setMode(byte newmode) {
		this.mode = newmode;
		if(this.mode != mode_DEFAULT) {this.nowrite = true;}
		else this.nowrite = false;
	}

	/**
	 * If one necessary location has not been written
	 * to, this method will add the section to the DME.
	 */
	public void finalWrites() {
		if(!this.wrote_INTERNALS) {
			this.beginBlock("internals");
			this.writeInternals();
			this.endBlock("internals");
		}

		if(!this.wrote_FILE_DIRS) {
			this.beginBlock("file_dir");
			this.writeFileDirs();
			this.endBlock("file_dir");
		}

		if(!this.wrote_PREFERENCES) {
			this.beginBlock("preferences");
			this.writePreferences();
			this.endBlock("preferences");
		}

		if(!this.wrote_INCLUDES) {
			this.beginBlock("include");
			this.writeIncludes();
			this.endBlock("include");
		}
	}

	/**
	 * Begins a block in the output text.
	 * @param blockname
	 */
	private void beginBlock(String blockname) {
		this.stringBuilder.append(String.format("// BEGIN_%s", blockname.toUpperCase()));
		this.stringBuilder.append(linebreak);
	}

	/**
	 * Ends a block in the output text.
	 * @param blockname
	 */
	private void endBlock(String blockname) {
		this.stringBuilder.append(String.format("// BEGIN_%s", blockname.toUpperCase()));
		this.stringBuilder.append(linebreak);
		this.stringBuilder.append(linebreak);
	}

	/**
	 * Writes the proper data into the internals block.
	 */
	private void writeInternals() {
		this.addLine("//\t-- internals");
		this.wrote_INTERNALS = true;
	}

	/**
	 * Writes the proper data into the preferences section.
	 */
	private void writePreferences() {
		this.addLine("//\t-- preferences");
		this.wrote_PREFERENCES = true;
	}

	/**
	 * Writes the proper data into the includes section.
	 */
	private void writeIncludes() {
		//this.addLine("//\t-- includes");
		for(DMEnvironmentInclude inc : this.environment.getIncludes()) {
			this.addLine("#include ".concat(inc.toString()));
		}
		this.wrote_INCLUDES = true;
	}

	/**
	 * Writes the file dirs into the file dirs block.
	 */
	private void writeFileDirs() {
		this.addLine("//\t-- file dirs");
		this.addLine("#define FILE_DIR .");
		if(this.environment.fileDirOn()) {
			File wdir = this.environment.getWorkingDir();
			this.writeFileDirsFor(wdir, wdir.toPath());
		}
		this.wrote_FILE_DIRS = true;
	}

	private void writeFileDirsFor(File dir, Path cutoff) {
		File[] fs = dir.listFiles();
		for(File f : fs) {
			if(f.isDirectory()) {
				Path p = f.toPath();
				System.out.println("Path: " + p.toString() + " (" + p.getNameCount() + ")");
				System.out.println("Cutoff: " + cutoff.toString() + " (" + cutoff.getNameCount() + ")");
				this.addLine(String.format("#define FILE_DIR \"%s\"",
						p.subpath(cutoff.getNameCount(), p.getNameCount()).toString()));
				this.writeFileDirsFor(f, cutoff);
			}
		}
	}

	/**
	 * Appends a line break to the output String.
	 */
	private void ln() {
		this.stringBuilder.append(linebreak);
	}

	/**
	 * Adds a line to the output string.
	 * @param s
	 */
	private void addLine(String s) {
		this.stringBuilder.append(s);
		this.ln();
	}

	/**
	 * Builds the final string, should only be called
	 * after {@link DMEWriter#write()}.
	 * @return the final string generated by this writer.
	 */
	public String build() {
		return this.stringBuilder != null ? this.stringBuilder.toString() : null;
	}
}
