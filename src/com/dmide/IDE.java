package com.dmide;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import javax.swing.JFileChooser;

import com.dmide.environment.DMEnvironment;
import com.dmide.ui.DMIDEUI;
import com.dmide.ui.editors.FileEditorPane;
import com.dmide.util.IDEFileFilters;
import com.dmide.util.PropertiesHolder;

public class IDE implements PropertiesHolder {

	Properties properties;
	/**
	 * Properties that will be saved when the IDE closes.
	 */
	Properties savableProperties;

	@Override
	public void setProperty(String key, Object value) {
		if(this.properties != null) this.properties.put(key, value);
		System.out.println("Property Set: " + key + " = " + value);
	}

	@Override
	public Object getProperty(String key) {
		return this.properties != null ? this.properties.get(key) : null;
	}

	/**
	 * Sets a property and saves it when the IDE closes.
	 * @param key
	 * @param value
	 */
	public void saveProperty(String key, Object value) {
		this.setProperty(key, value);
		this.savableProperties.put(key, value);
		System.out.println("Property Saved: " + key + " = " + value);
	}

	/**
	 * Saves properties that can be saved.
	 */
	public void save() {
		DMEnvironment.saveRecentEnvironments();
		System.out.println("Saving properties...");
		File sfile = new File("settings/ideproperties.xml");
		if(!sfile.exists()) {
			try {
				sfile.getParentFile().mkdirs();
				sfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			this.savableProperties.storeToXML(new FileOutputStream(sfile), "Saved JDMIDE properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads properties saved using the save method.
	 * @see com.dmide.IDE#save()
	 */
	public void load() {
		File sfile = new File("settings/ideproperties.xml");
		if(sfile.exists()) {
			try {
				this.savableProperties.loadFromXML(new FileInputStream(sfile));
				for(Object o : this.savableProperties.keySet()) {
					if(o instanceof String) {
						this.setProperty((String)o, this.savableProperties.get(o));
					}
				}
			} catch (InvalidPropertiesFormatException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Properties getAllProperties() {return this.properties;}
	public boolean isSavableProperty(String p) {
		return this.savableProperties.containsKey(p);
	}

	private IDE() {
		this.properties = new Properties();
		this.savableProperties = new Properties();
	}

	static IDE instance;
	public static IDE getInstance() {
		if(instance == null) { instance = new IDE(); }
		return instance;
	}

	public void saveCurrentFile() {
		this.saveFileAt(DMIDEUI.getInstance().getMainWindow()
				.getFileEditorPane().getSelectedIndex() );
	}

	public void saveFileAt(int index) {
		if(index < 0) return;
		Component C = DMIDEUI.getInstance().getMainWindow()
				.getFileEditorPane().getComponentAt(index);
		if(C instanceof FileEditorPane) {
			FileEditorPane fep = (FileEditorPane) C;
			fep.save();
		}
	}

	public void promtOpenDME() {
		System.out.println("Opening dat dme file chooser...");
		DMIDEUI.getInstance().getFileChooser().setFileFilter(IDEFileFilters.getInstance().getDMEFileFilter());
		int r = DMIDEUI.getInstance().getFileChooser().
				showOpenDialog(DMIDEUI.getInstance().getMainWindow());
		if(r == JFileChooser.APPROVE_OPTION) {
			DMEnvironment.setDMEFile(DMIDEUI.getInstance().
					getFileChooser().getSelectedFile());
		}
	}

	public void trackEditor(FileEditorPane fep) {
	}

}
