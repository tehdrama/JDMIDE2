package com.dmide.util.misc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.dmide.util.PropertiesHolder;

public class MiscUI {
	ArrayList<KeyStroke> mappedKeyStrokes = new ArrayList<>();
	public JMenuBar createMenuBar(File xmlFile) { return this.createMenuBar(xmlFile, null); }
	public JMenuBar createMenuBar(File xmlFile, PropertiesHolder props) {
		Document xmlDoc;
		try {
			xmlDoc = Misc.io.getXMLDocument(xmlFile);

			Element elem = (Element) xmlDoc.getFirstChild();

			if(elem == null) return null;

			String id = elem.hasAttribute("id") ? elem.getAttribute("id") : null;

			JMenuBar menuBar = new JMenuBar();

			if(id != null && props != null) {props.setProperty(id, menuBar);}

			NodeList children = elem.getChildNodes();
			if(children != null && children.getLength() > 0) {
				for(int i = 0; i < children.getLength(); i++) {
					Object o = children.item(i);
					if(o == null || !(o instanceof Element)) {continue;}
					Element celem = (Element) o;
					this.parseUnknownMenuBarElem(celem, menuBar, props);
				}
			}
			return menuBar;
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void parseUnknownMenuBarElem(Element elem, JComponent parent, PropertiesHolder props) {
		String n = elem.getNodeName().toLowerCase();
		switch(n) {
		case "menu":
			this.parseMenu(elem, parent, props);
			break;
		case "item":
			this.parseMenuItem(elem, parent, props);
			break;
		case "separator":
			if(parent != null) {parent.add(new JSeparator());}
		}
	}

	private void parseMenu(Element elem, JComponent parent, PropertiesHolder props) {
		String text = elem.hasAttribute("text") ? elem.getAttribute("text") : null;
		String id = elem.hasAttribute("id") ? elem.getAttribute("id") : null;

		JMenu _menu = new JMenu(text);
		if(id != null && props != null) {props.setProperty(id, _menu);}

		if(parent != null) {parent.add(_menu);}

		NodeList children = elem.getChildNodes();

		if(children == null || children.getLength() < 0) {return;}
		else {
			for(int i = 0; i < children.getLength(); i++) {
				Object o = children.item(i);
				if(o == null || !(o instanceof Element)) {continue;}
				Element celem = (Element) o;
				this.parseUnknownMenuBarElem(celem, _menu, props);
			}
		}
	}

	private void parseMenuItem(Element elem, JComponent parent, PropertiesHolder props) {
		String text = elem.hasAttribute("text") ? elem.getAttribute("text") : null;
		String id = elem.hasAttribute("id") ? elem.getAttribute("id") : null;
		String iconU = elem.hasAttribute("icon") ? elem.getAttribute("icon") : null;
		String acc = elem.hasAttribute("acc") ? elem.getAttribute("acc") : null;
		JMenuItem _item = new JMenuItem(text);
		if(iconU != null) {
			iconU = iconU.replace("%assets%", "/com/dmide/assets");
			_item.setIcon(new ImageIcon(this.getClass().getResource(iconU)));
		}
		if(acc != null) {
			KeyStroke _k = KeyStroke.getKeyStroke(acc);
			_item.setAccelerator(_k);
			this.mappedKeyStrokes.add(_k);
		}
		if(id != null && props != null) {props.setProperty(id, _item);}
		if(parent != null) {parent.add(_item);}
	}



	/**
	 * @return the mappedKeyStrokes
	 */
	public ArrayList<KeyStroke> getMappedKeyStrokes() {
		return this.mappedKeyStrokes;
	}

	public void removeMappedMenuKeyStrokesFromComponent(JComponent component) {
		for(KeyStroke ks : this.mappedKeyStrokes) {
			/**
			 * Ignore it if it's not being used.
			 */
			if(component.getInputMap().get(ks) == null) {
				component.getInputMap().put(ks, "none");
			} else {
			}
		}
	}

	static MiscUI instance;

	public static MiscUI getInstance() {
		if(instance == null) {instance = new MiscUI();}
		return instance;
	}
}
