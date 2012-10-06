package com.dmide.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.io.FilenameUtils;

public class IDEFileComparator implements Comparator<File> {

	ArrayList<String> extLocs;

	public IDEFileComparator() {
		this.setExtLocs();
	}

	/**
	 * Sets the order of extensions.
	 */
	public void setExtLocs() {
		this.extLocs = new ArrayList<>();
		Collections.addAll(this.extLocs, "dme", "dm", "dms", "dmi", "dmf", "dmm", "dmp");
	}

	@Override
	public int compare(File o1, File o2) {
		// Everything must be done in reverse because lists
		// go from least to greatest :P
		if(o1.isDirectory() && !o2.isDirectory()) {return 1;}
		else if(!o1.isDirectory() && o2.isDirectory()) {return -1;}
		if(!o1.isDirectory() && !o2.isDirectory()) {
			int extCheck = this.checkExt(o1, o2);
			if(extCheck != 0) return extCheck;
		}
		String o1s = o1.getName();
		String o2s = o2.getName();
		o1s = o1s.replace('_', (char)('0'-1));
		o2s = o2s.replace('_', (char)('0'-1));
		return o1s.compareTo(o2s);
	}

	public int checkExt(File o1, File o2) {
		String ext1 = FilenameUtils.getExtension(o1.getName());
		String ext2 = FilenameUtils.getExtension(o2.getName());

		int l1 = this.extLocs.indexOf(ext1);
		int l2 = this.extLocs.indexOf(ext2);

		if(l1 == -1) l1 = this.extLocs.size();
		if(l2 == -1) l2 = this.extLocs.size();

		if(l1 == l2) return 0;

		if(l1 > l2) return 1;
		else return -1;
	}


}
