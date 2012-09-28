package com.dmide;

import java.util.Properties;

import com.dmide.ui.editors.FileEditorPane;

public class DMIDE {
	static IDE ide;

	static {
		ide = IDE.getInstance();
	}

	/**
	 * @param key
	 * @param value
	 * @see com.dmide.IDE#setProperty(java.lang.String, java.lang.Object)
	 */
	public static void setProperty(String key, Object value) {
		ide.setProperty(key, value);
	}

	/**
	 * @param key
	 * @return
	 * @see com.dmide.IDE#getProperty(java.lang.String)
	 */
	public static Object getProperty(String key) {
		return ide.getProperty(key);
	}

	/**
	 * @param key
	 * @param value
	 * @see com.dmide.IDE#saveProperty(java.lang.String, java.lang.Object)
	 */
	public static void saveProperty(String key, Object value) {
		ide.saveProperty(key, value);
	}

	/**
	 *
	 * @see com.dmide.IDE#save()
	 */
	public static void save() {
		ide.save();
	}

	/**
	 *
	 * @see com.dmide.IDE#load()
	 */
	public static void load() {
		ide.load();
	}

	/**
	 * @return
	 * @see com.dmide.IDE#getAllProperties()
	 */
	public static Properties getAllProperties() {
		return ide.getAllProperties();
	}

	/**
	 * @param p
	 * @return
	 * @see com.dmide.IDE#isSavableProperty(java.lang.String)
	 */
	public boolean isSavableProperty(String p) {
		return ide.isSavableProperty(p);
	}

	/**
	 *
	 * @see com.dmide.IDE#saveCurrentFile()
	 */
	public static void saveCurrentFile() {
		ide.saveCurrentFile();
	}

	/**
	 * @param index
	 * @see com.dmide.IDE#saveFileAt(int)
	 */
	public static void saveFileAt(int index) {
		ide.saveFileAt(index);
	}

	/**
	 *
	 * @see com.dmide.IDE#promtOpenDME()
	 */
	public static void promtOpenDME() {
		ide.promtOpenDME();
	}

	/**
	 * @param fep
	 * @see com.dmide.IDE#trackEditor(com.dmide.ui.editors.FileEditorPane)
	 */
	public static void trackEditor(FileEditorPane fep) {
		ide.trackEditor(fep);
	}



}
