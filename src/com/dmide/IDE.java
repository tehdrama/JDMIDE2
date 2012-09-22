package com.dmide;

import java.util.Properties;

import com.dmide.util.PropertiesHolder;

public class IDE implements PropertiesHolder {

	Properties properties;

	@Override
	public void setProperty(String key, Object value) {
		if(this.properties != null) this.properties.put(key, value);
		System.out.println("Property Set: " + key + " = " + value);
	}

	@Override
	public Object getProperty(String key) {
		return this.properties != null ? this.properties.get(key) : null;
	}

	private IDE() {
		this.properties = new Properties();
	}

	static IDE instance;
	public static IDE getInstance() {
		if(instance == null) { instance = new IDE(); }
		return instance;
	}

}
