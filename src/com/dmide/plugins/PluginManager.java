package com.dmide.plugins;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.ServiceLoader;

public class PluginManager {


	URLClassLoader urlClassLoader;
	ArrayList<URL> urls;
	ServiceLoader<AbstractPlugin> pluginsService;

	File local;

	public void getPlugins(File dir) throws MalformedURLException {
		this.local = new File(".").getParentFile(); // Current directory.

		if(this.urls == null) {this.urls = new ArrayList<>();}
		else this.urls.clear();

		PluginBundleHandler bundleHandler = null;

		try {
			bundleHandler = this.lookUpBundles(dir);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(bundleHandler == null) {return;} // Failure!

		for(PluginBundle b : bundleHandler.getBundles()) {
			boolean pluginFailed = false;
			for(String de : b.getDependencies()) {
				if(!bundleHandler.checkDependencyExists(de)) {
					System.out.println("Plugin Failed: " + b.getPluginIdentifier());
					pluginFailed = true; break;
				}
			}
			if(pluginFailed) { continue; }
			else this.includeBundle(b);
		}
		this.createServices();
	}

	/**
	 * Creates the service loader and the URLClassLoader.
	 */
	public void createServices() {
		this.urlClassLoader =
				new URLClassLoader(this.urls.toArray(new URL[this.urls.size()]));
		this.pluginsService = ServiceLoader.load(AbstractPlugin.class, this.urlClassLoader);
	}

	public void includeBundle(PluginBundle b) throws MalformedURLException {
		for(File f : b.getIncludes()) {
			if(f.exists()) {
				this.urls.add(f.toURI().toURL());
				System.out.println("Including: " + f.getPath());
			} else {
				System.out.printf("Failed to include %s from bundle %s.\n", f.getPath(), b.getName());
			}
		}
	}

	/**
	 * Finds all plugins in the specified directory.
	 * @param dir The directory to search.
	 * @throws IOException
	 */
	private PluginBundleHandler lookUpBundles(File dir) throws IOException {
		PluginBundleHandler bundleHandler = new PluginBundleHandler();
		if(!dir.isDirectory()) {
			throw new IOException(String.format("%s is not a directory!", dir.getPath()));
		}
		File[] files = dir.listFiles();
		for(File file : files) {
			if(file.isDirectory()) {
				this.lookUpBundlesEx(file, bundleHandler);
			}
		}
		return bundleHandler;
	}

	/**
	 * Attempts to find the *.bundle file in the specified directory.
	 * @param dir
	 * @throws IOException
	 */
	private void lookUpBundlesEx(File dir, PluginBundleHandler bundleHandler) throws IOException {
		if(!dir.isDirectory()) {
			throw new IOException(String.format("%s is not a directory!", dir.getPath()));
		}
		File[] files = dir.listFiles();
		for(File file : files) {
			if(file.getName().toLowerCase().equals("bundle.xml")) {
				bundleHandler.addBundle(file);
			}
		}
	}

	/**
	 * Constructor made private so that new instances cannot
	 * be created outside of this class.
	 */
	private PluginManager() {
	}

	static PluginManager instance;

	/**
	 *
	 * @return the current instance of the PluginManager.
	 */
	public static PluginManager getInstance() {
		if(PluginManager.instance == null) {
			PluginManager.instance = new PluginManager();
		}
		return PluginManager.instance;
	}
}
