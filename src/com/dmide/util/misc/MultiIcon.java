/**
 *
 */
package com.dmide.util.misc;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

/**
 * Create an icon by mixing multiple icons together.
 * @author Adolph C.
 *
 */
public class MultiIcon implements Icon {

	Icon[] icons;
	int width, height;
	BufferedImage img;

	public MultiIcon(Icon...icons) {
		this.icons = icons;
	}

	/**
	 * Layers the icons in their respective order (specified by the array).
	 */
	public void mix() {
		this.calculateSize();
		BufferedImage bi = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bi.createGraphics();
		for(Icon i : this.icons) {
			if(i == null) continue;
			i.paintIcon(null, g, 0, 0);
		}
		g.dispose();
		this.img = bi;
	}

	/**
	 * Clear the list of icons to free up memory.
	 */
	public void freeIcons() {
		this.icons = null;
	}

	/**
	 * Sets the width and height to match the largest width
	 * and height of the icon.
	 */
	public void calculateSize() {
		if(this.icons == null) return;
		int w = 0;
		int h = 0;
		for(Icon i : this.icons) {
			if(i==null) continue;
			if(i.getIconWidth() > w) w = i.getIconWidth();
			if(i.getIconHeight() > h) h = i.getIconHeight();
		}
		this.width = w;
		this.height = h;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#getIconHeight()
	 */
	@Override
	public int getIconHeight() {
		// TODO Auto-generated method stub
		return this.width;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#getIconWidth()
	 */
	@Override
	public int getIconWidth() {
		// TODO Auto-generated method stub
		return this.height;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.drawImage(this.img, 0, 0, null);
	}

}
