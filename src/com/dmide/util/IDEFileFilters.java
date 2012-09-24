package com.dmide.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FilenameUtils;

public class IDEFileFilters {
	DMEnvironmentFileFilter dmeFileFilter;

	public DMEnvironmentFileFilter getDMEFileFilter() {
		if(this.dmeFileFilter == null) this.dmeFileFilter = new DMEnvironmentFileFilter();
		return this.dmeFileFilter;
	}

	public class DMEnvironmentFileFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			return (f.isDirectory() || FilenameUtils.getExtension(f.getName()).equals("dme"));
		}

		@Override
		public String getDescription() {
			return "DM Environment (*.dme)";
		}

	}

	static IDEFileFilters instance;

	public static IDEFileFilters getInstance() {
		if(instance == null) instance = new IDEFileFilters();
		return instance;
	}
}
