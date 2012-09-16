package com.dmide.plugins;

import java.io.File;
import java.util.Arrays;

public class PluginBundle {
	String name;
	String author;
	String[] dependencies;
	int version;
	String versionText;
	File[] includes;

	/**
	 * @param name
	 * @param author
	 * @param dependencies
	 * @param version
	 * @param versionText
	 * @param includes
	 */
	public PluginBundle(String name, String author, String[] dependencies,
			int version, String versionText, File[] includes) {
		this.name = name;
		this.author = author;
		this.dependencies = dependencies;
		this.version = version;
		this.versionText = versionText;
		this.includes = includes;
	}

	/**
	 *
	 * @return the name of this bundle.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the name of the author of this plugin.
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * @return the dependencies (other bundles) of this bundle.
	 */
	public String[] getDependencies() {
		return this.dependencies;
	}

	/**
	 * @return the version of this bundle.
	 */
	public int getVersion() {
		return this.version;
	}

	/**
	 * @return the full version of this bundle as text.
	 */
	public String getVersionText() {
		return this.versionText;
	}

	/**
	 * @return the included files of this bundle.
	 */
	public File[] getIncludes() {
		return this.includes;
	}

	/**
	 *
	 * @return the simple lowercase version of the name of this plugin with whitespace removed.
	 */
	public String getSimpleName() {
		return this.getName().replaceAll("\\s+", "").toLowerCase();
	}

	/**
	 *
	 * @return the simple lowercase version of the author of this plugin with whitespace removed.
	 */
	public String getSimpleAuthor() {
		return this.getAuthor().replaceAll("\\s+", "").toLowerCase();
	}

	/**
	 *
	 * @return {simple-author-name}.{simple-name}
	 */
	public String getPluginIdentifier() {
		return this.getSimpleAuthor().concat(".").concat(this.getSimpleName());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PluginBundle [getName()=" + this.getName() + ", getAuthor()="
				+ this.getAuthor() + ", getDependencies()="
				+ Arrays.toString(this.getDependencies()) + ", getVersion()="
				+ this.getVersion() + ", getVersionText()=" + this.getVersionText()
				+ ", getIncludes()=" + Arrays.toString(this.getIncludes())
				+ ", getSimpleName()=" + this.getSimpleName()
				+ ", getSimpleAuthor()=" + this.getSimpleAuthor()
				+ ", getPluginIdentifier()=" + this.getPluginIdentifier() + "]";
	}


}
