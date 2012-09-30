package com.dmide;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

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

		System.out.println("---------Defaults------");
		UIDefaults ud = UIManager.getDefaults();
		for(Object s : ud.keySet()) {
			System.out.println("key: " + s.toString());
		}
		System.out.println("---------End Defaults------");
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

	public static void restartApplication()
	{
		System.out.println("Restarting...");
		try {
			final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
			final File currentJar = new File(DMIDEMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());

			/* is it a jar file? */
			if(!currentJar.getName().endsWith(".jar")) {
				JOptionPane.showMessageDialog(DMIDEUI.getInstance().getMainWindow(),
						"Failed to automatically restart IDE.\n" +
						"Reason: Failed to locate IDE jar.", "Failed to Restart",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			/* Build command: java -jar application.jar */
			final ArrayList<String> command = new ArrayList<String>();
			command.add(javaBin);
			command.add("-jar");
			command.add(currentJar.getPath());

			final ProcessBuilder builder = new ProcessBuilder(command);
			builder.start();
		} catch(IOException | URISyntaxException e) {

		}

		DMIDEUI.getInstance().saveIDEWindowState();
		DMIDE.save();

		System.exit(0);
	}


}
