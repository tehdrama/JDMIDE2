package com.dmide.util.misc;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FilenameUtils;

public class MiscIO {

	public boolean cachefileicons = true;
	/**
	 * Associations between icon extensions and their icons.
	 */
	Map<String, Icon> extIconAssoc;

	static MiscIO instance;

	public Icon getFileTypeIcon(File f) {
		if(!f.exists()) return null;
		if(!this.cachefileicons) return FileSystemView.getFileSystemView().getSystemIcon(f);
		if(this.extIconAssoc == null) {
			this.extIconAssoc = new HashMap<String, Icon>();
		}
		String ext = FilenameUtils.getExtension(f.getName());
		if(!this.extIconAssoc.containsKey(ext)) {
			Icon icon = FileSystemView.getFileSystemView().getSystemIcon(f);
			this.extIconAssoc.put(ext, icon);
		}
		return this.extIconAssoc.get(ext);
	}

	public static MiscIO getInstance() {
		if(instance == null) {instance = new MiscIO();}
		return instance;
	}
}
