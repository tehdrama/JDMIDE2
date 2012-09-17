package com.dmide.environment;

import java.io.File;

public class DMEnvironment {
	static File dmeFile;

	public static File getWorkingDirectory() {
		if(dmeFile != null) return dmeFile.getParentFile();
		return null;
	}

	public static void setDMEFile(File f) {
	}
}
