package com.dmide.ui.tabs;

import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Used to paint the UI of the TabbedPaneUIHeader class.
 * @author Adolph C.
 *
 */
public interface TabbedPaneUIHeaderPainter {
	/**
	 * Paints the close button.
	 * @param g the graphics object of the button.
	 */
	public void paintCloseButton(TabHeaderCloseButton button, Graphics g);

	/**
	 *
	 * @return the size of the close button that is to be painted.
	 */
	public Dimension getClosebuttonSize();
}
