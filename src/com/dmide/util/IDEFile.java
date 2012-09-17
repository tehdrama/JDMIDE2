package com.dmide.util;

import java.io.File;
import java.nio.file.Path;

public class IDEFile {
	Path path;

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
}
