package com.dmide.environment;

import java.io.File;


public class DMEnvironmentInclude {
	public static final int type_LIBRARY = 0;
	public static final int type_FILE = 1;
	File file;
	int type = type_FILE;

	/**
	 *
	 * @return the file of this include.
	 */
	public File getFile() {
		return this.file;
	}

	/**
	 * Returns the string representation of this DMEnvironmentInclude,
	 * or null if one cannot be created properly.
	 */
	@Override
	public String toString() {
		if(this.type == type_LIBRARY) {
			return this.toString_lib();
		} else if(this.type == type_FILE) {
			return this.toString_file();
		} else return null;
	}

	private String toString_lib() {
		return null;
	}

	private String toString_file() {
		return null;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Creates an DMEnvironmentInclude from the specified string.
	 * @param s String from which to create the include.
	 * @return A new include created using the specified string.
	 */
	public static DMEnvironmentInclude parseInclude(String s) {
		if(s.length() < 3) return null;
		s = s.trim();
		if(s.charAt(0) == '<' && s.charAt(s.length() - 1) == '>') return createLibInclude(s.substring(1, s.length() - 1));
		else if(s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') return createFileInclude(s.substring(1, s.length() - 1));
		return null;
	}

	/**
	 * Creates a DMEnvironmentInclude for a library using the specified string.
	 * @param substring
	 * @return A new include created using the specified string.
	 */
	private static DMEnvironmentInclude createFileInclude(String substring) {
		return new DMEnvironmentInclude(new File(substring), type_FILE);
	}

	/**
	 * Creates a DMEnvironmentInclude for a normal file using the specified string.
	 * @param substring
	 * @return A new include created using the specified string.
	 */
	private static DMEnvironmentInclude createLibInclude(String substring) {
		return null;
	}

	/**
	 * @param file
	 * @param type
	 */
	public DMEnvironmentInclude(File file, int type) {
		this.file = file;
		this.type = type;
	}


}
