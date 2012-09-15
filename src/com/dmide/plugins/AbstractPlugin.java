package com.dmide.plugins;

/**
 * @author Adolph C.
 *
 *         The abstract plugin class is used by the Plugin Manager to extend the
 *         IDE.
 */
public abstract class AbstractPlugin {
	/**
	 * Called by the PluginManager to start the Plugin.
	 */
	public abstract void start();

	/**
	 * Called by the PluginManager to stop the Plugin.
	 */
	public abstract void stop();

	/**
	 * @return the plugin's author.
	 */
	public abstract String getPluginAuthor();

	/**
	 * @return the actual name of the plugin, e.g. "Abstract Plugin".
	 */
	public abstract String getPluginName();

	/**
	 * @return the short name of the plugin, by default, this returns
	 *         getPluginName() in lowercase with whitespace removed.
	 */
	public String getShortPluginName() {
		return this.getPluginName().replaceAll("\\s+", "").toLowerCase();
	}

	/**
	 * @return the short name of the plugin's author, by default, this returns
	 *         getPluginAuthor() in lowercase with whitespace removed.
	 */
	public String getShortAuthorName() {
		return this.getPluginAuthor().replaceAll("\\s+", "").toLowerCase();
	}

	/**
	 * Used to process events sent by the PluginContainer, or other plugins.
	 * @param evt the event that was passed.
	 */
	public abstract void pluginEvent(PluginEvent evt);
}
