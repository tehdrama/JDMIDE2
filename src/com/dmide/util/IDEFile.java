package com.dmide.util;

import java.io.File;
import java.nio.file.Path;

import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class IDEFile extends DefaultMutableTreeNode {
	Path path;
	File absolute;

	public File getAbsolute() {
		return this.absolute;
	}

	public void setAbsolute(File f) {
		this.absolute = f;
	}

	public IDEFile(File file) {
		if(file != null) this.path = file.toPath();
	}

	public IDEFile(Path path) {
		this.path = path;
	}

	@Override
	public String toString() {
		if(this.path != null) return this.path.getFileName().toString();
		else return "Undefined File.undf";
	}

	/**
	 *
	 * @return the path of the file held by this node.
	 */
	public Path getPathObject() {
		return this.path;
	}

	/**
	 *
	 * @return the file held by this node.
	 */
	public File getFileObject() {
		return this.path.toFile();
	}

	@Override
	public boolean equals(Object o) {
		//System.out.println("Matching: " + o.toString() + " => " + this.toString());
		if(o instanceof Path) {
			return o.equals(this.getPathObject());
		} else if(o instanceof File) {
			return this.equals(((File)o).toPath());
		} else return super.equals(o);
	}
}
