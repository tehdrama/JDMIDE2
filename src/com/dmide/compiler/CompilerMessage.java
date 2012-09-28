package com.dmide.compiler;

public class CompilerMessage {
	CompilerMessageType messageType;
	int line;
	String file, message;

	// code\game\enemies.dm:1:warning: #warn derp
	/*
	 * loading Pandora's Box.dme loading interface.dmf loading map.dmm saving
	 * Pandora's Box.dmb (DEBUG mode)
	 *
	 * Pandora's Box.dmb - 0 errors, 0 warnings
	 */

	/**
	 * @param messageType
	 * @param message
	 * @param file
	 * @param line
	 */
	public CompilerMessage(CompilerMessageType messageType, String message,
			String file, int line) {
		this.messageType = messageType;
		this.message = message;
		this.file = file;
		this.line = line;
	}

	/**
	 * Creates a CompilerMessage from a string. This format is used:
	 *
	 * [{file}:{line}:(error | warning | info):]message
	 *
	 * @param string
	 * @return the generated CompilerMessage, or null if this failed.
	 */
	public static CompilerMessage fromString(String string) {
		String[] _split = string.split(":");
		if (_split.length > 0) {
			if (_split.length < 2) {
				return new CompilerMessage(CompilerMessageType.INFORMATION,
						_split[0], null, -1);
			} else if (_split.length >= 4) {
				StringBuilder sb = new StringBuilder();
				for(int i = 3; i < _split.length; i++) {
					if(i + 1 != _split.length - 1) sb.append(':');
					sb.append(_split[i]);
				}
				return new CompilerMessage(messageTypeFromString(_split[2]),
						sb.toString(), _split[0], Integer.parseInt(_split[1]));
			}
		}
		return null;
	}

	/**
	 *
	 * @param s
	 * @return the CompilerMessageType represented by the specified string of
	 *         CompilerMessageType.INFORMATION if this string does not match any
	 *         known type.
	 */
	public static CompilerMessageType messageTypeFromString(String s) {
		s = s.trim().toLowerCase();
		switch (s) {
		case "warning":
			return CompilerMessageType.WARNING;
		case "error":
			return CompilerMessageType.ERROR;
		case "info":
			return CompilerMessageType.INFORMATION;
		default:
			return CompilerMessageType.INFORMATION; // Might change this later.
		}
	}

	/**
	 * @return the messageType
	 */
	public CompilerMessageType getMessageType() {
		return this.messageType;
	}

	/**
	 * @param messageType the messageType to set
	 */
	public void setMessageType(CompilerMessageType messageType) {
		this.messageType = messageType;
	}

	/**
	 * @return the line
	 */
	public int getLine() {
		return this.line;
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(int line) {
		this.line = line;
	}

	/**
	 * @return the file
	 */
	public String getFile() {
		return this.file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return this.getMessage();
	}
}
