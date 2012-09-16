package com.dmide.plugins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PluginBundleHandler {
	public ArrayList<PluginBundle> bundles = new ArrayList<>();

	public void addBundle(PluginBundle b) {
		this.bundles.add(b);
	}

	public void addBundle(File bundleFile) {
		PluginBundle b = (new BundleReader(bundleFile)).read();
		if(b != null) {
			this.addBundle( b );
			System.out.println("--Added Bundle: ");
			System.out.println(b.toString());
		}
	}

	public ArrayList<PluginBundle> getBundles() {
		return this.bundles;
	}

	public boolean checkDependencyExists(String dependency) {
		for(PluginBundle b : this.bundles) {
			if(b.getPluginIdentifier().equals(dependency)) {
				return true;
			}
		}
		return false;
	}

	public class BundleReader {
		public File bundleFile;

		ArrayList<String> dependencies;
		ArrayList<File> includes;

		String bauthor = "Anonymous";
		String bname = "Anonymous Bundle";
		String bversiont = "1.0";
		int bversion = 1;

		public BundleReader(File bundleFile) {
			this.bundleFile = bundleFile;
		}

		public PluginBundle read() {
			PluginBundle bundle = null;

			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = factory.newDocumentBuilder();
				Document doc = docBuilder.parse(this.bundleFile);
				doc.normalize();

				Element bundleElem = (Element) doc.getElementsByTagName("bundle").item(0);
				NodeList nl = bundleElem.getChildNodes();

				for(int i = 0; i < nl.getLength(); i++) {
					Node n = nl.item(i);
					if(n instanceof Element) {
						Element elem = (Element) n;
						if(elem.getTagName().equals("name")) {
							this.bname = elem.getTextContent();
						}
						if(elem.getTagName().equals("author")) {
							this.bauthor = elem.getTextContent();
						} else if(elem.getTagName().equals("version")) {
							this.bversiont = elem.getTextContent();
							if(elem.hasAttribute("int")) {
								this.bversion = Integer.parseInt(elem.getAttribute("int"));
							}
						} else if(elem.getTagName().equals("dependency")) {
							this.addDependency(elem.getTextContent());
						} else if(elem.getTagName().equals("include")) {
							this.addInclude(new File(elem.getTextContent()));
						}
					}
				}

				String[] d = null;
				File[] i = null;

				if(this.dependencies != null) {
					d = this.dependencies.toArray(new String[this.dependencies.size()]);
				} else {
					d = new String[0];
				}
				if(this.includes != null) {
					i = this.includes.toArray(new File[this.includes.size()]);
				} else {
					i = new File[0];
				}


				bundle = new PluginBundle(this.bname, this.bauthor,
						d,
						this.bversion, this.bversiont,
						i);

			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}

			return bundle;
		}

		public void addDependency(String d) {
			if(this.dependencies == null) {
				this.dependencies = new ArrayList<>();
			}
			this.dependencies.add(d);
		}

		public void addInclude(File f) {
			if(this.includes == null) {
				this.includes = new ArrayList<>();
			}
			this.includes.add(f);
		}
	}
}
