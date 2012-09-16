package com.dmide;

import java.io.File;
import java.io.IOException;

import com.dmide.plugins.PluginManager;

public class DMIDEMain {
	static PluginManager pluginManager;
	static File pluginLookUp;

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
	}

	public void start() throws IOException {
		DMIDEMain.pluginManager = PluginManager.getInstance();
		DMIDEMain.pluginManager.getPlugins(DMIDEMain.pluginLookUp);
	}
}
