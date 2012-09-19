package com.dmide;

import java.io.File;
import java.io.IOException;

import com.dmide.environment.DMEnvironment;
import com.dmide.plugins.PluginManager;
import com.dmide.ui.DMIDEUI;

public class DMIDEMain {
	static PluginManager pluginManager;
	static File pluginLookUp;

	DMIDEUI uiInstance;

	public static void main(String[] args) {
		if(args.length >= 1) {
			pluginLookUp = new File(args[0]);
		} else {
			pluginLookUp = new File("plugins/");
		}
		System.out.println("Plugin Lookup Directory Set To: " + pluginLookUp.getPath());
		try {
			(new DMIDEMain()).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DMEnvironment.setDMEFile(new File("C:/Documents and Settings/Saoude1/My Documents/Dropbox/BYOND Projects/Relics/Relics.dme"));
	}

	public void start() throws IOException {
		DMIDEMain.pluginManager = PluginManager.getInstance();
		DMIDEMain.pluginManager.getPlugins(DMIDEMain.pluginLookUp);
		this.uiInstance = DMIDEUI.getInstance();
		this.uiInstance.generate();
	}
}
