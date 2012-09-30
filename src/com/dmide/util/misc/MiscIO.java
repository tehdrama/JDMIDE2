package com.dmide.util.misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class MiscIO {

	public boolean cachefileicons = true;
	/**
	 * Associations between icon extensions and their icons.
	 */
	Map<String, Icon> extIconAssoc;
	String ln = "\n";
	Icon unsavedIcon;

	public Icon getUnsavedIcon() {
		if(this.unsavedIcon == null) {
			this.unsavedIcon = new ImageIcon(this.getClass().getResource("/com/dmide/assets/disk-small-black.png"));
		}
		return this.unsavedIcon;
	}

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

	public Icon getFileTypeIcon_unsaved(File f) {
		MultiIcon multiIcon = new MultiIcon(this.getFileTypeIcon(f), this.getUnsavedIcon());
		multiIcon.mix();
		multiIcon.freeIcons();
		return multiIcon;
	}

	public String readFileString(File f) {
		System.out.println("Reading: " + f.getPath());
		Scanner scanner = null;
		try {
			scanner = new Scanner(f);
			StringBuilder b = new StringBuilder();
			while(scanner.hasNextLine()) {
				b.append(scanner.nextLine());
				b.append(this.ln);
			}
			scanner.close();
			return b.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(scanner != null) {scanner.close();}
		}
		return null;
	}

	public Document getXMLDocument(File xmlFile) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();
		Document doc = docBuilder.parse(xmlFile);
		return doc;
	}

	static MiscIO instance;

	public static MiscIO getInstance() {
		if(instance == null) {instance = new MiscIO();}
		return instance;
	}
}
