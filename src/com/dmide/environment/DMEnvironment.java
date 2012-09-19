package com.dmide.environment;

import java.io.File;

import com.dmide.util.events.IDEEvent;
import com.dmide.util.events.IDEEventHandler;

public class DMEnvironment {
	static File dmeFile;

	public static File getWorkingDirectory() {
		if(dmeFile != null) return dmeFile.getParentFile();
		return null;
	}

	public static void setDMEFile(File f) {
		dmeFile = f;
		IDEEventHandler.sendIDEEvent(new IDEEvent("environment.open", dmeFile));
	}
}
