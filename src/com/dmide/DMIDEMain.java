package com.dmide;

import java.io.File;
import java.io.IOException;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;

import com.dmide.compiler.DMCompiler;
import com.dmide.environment.DMEnvironment;
import com.dmide.files.FileOpenHandler;
import com.dmide.plugins.PluginManager;
import com.dmide.ui.DMIDEUI;
import com.dmide.util.events.IDEEventHandler;

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
	}

	public void start() throws IOException {
		DMIDEUI.getInstance().openSplashScreen();
		this.init();
		DMIDEMain.pluginManager = PluginManager.getInstance();
		DMIDEMain.pluginManager.getPlugins(DMIDEMain.pluginLookUp);
		this.uiInstance = DMIDEUI.getInstance();
		this.uiInstance.generate();
	}

	public void registerDMTokenMaker() {
		AbstractTokenMakerFactory atmf =
				(AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
		atmf.putMapping("text/dm", "com.dmide.syntax.DMTokenMaker");
	}

	public void init() {
		IDE.getInstance();
		DMIDE.load();
		this.registerDMTokenMaker();
		IDEEventHandler.addWatcher(DMCompiler.getInstance());
		IDEEventHandler.addWatcher(FileOpenHandler.getInstance());

		DMEnvironment.loadRecentEnvironments();
	}
}
