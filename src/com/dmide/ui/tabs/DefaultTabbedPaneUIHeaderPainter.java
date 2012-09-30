package com.dmide.ui.tabs;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class DefaultTabbedPaneUIHeaderPainter implements
		TabbedPaneUIHeaderPainter {
	static ImageIcon closeButtonIcon_inactive;
	static ImageIcon closeButtonIcon_active;
	static ImageIcon closeButtonIcon_superactive;

	static Dimension bDim = new Dimension(10, 10);

	public static DefaultTabbedPaneUIHeaderPainter instance;

	public static DefaultTabbedPaneUIHeaderPainter getInstance() {
		if(instance == null)
			instance = new DefaultTabbedPaneUIHeaderPainter();
		return instance;
	}

	public static Icon getCloseButtonIcon_inactive() {
		if(closeButtonIcon_inactive == null)
			closeButtonIcon_inactive = new ImageIcon(getInstance().getClass()
					.getResource("/com/dmide/assets/tabclosebutton-inactive.png"));
		return closeButtonIcon_inactive;
	}
	public static Icon getCloseButtonIcon_active() {
		if(closeButtonIcon_active == null)
			closeButtonIcon_active = new ImageIcon(getInstance().getClass()
					.getResource("/com/dmide/assets/tabclosebutton-active.png"));
		return closeButtonIcon_active;
	}
	public static Icon getCloseButtonIcon_superactive() {
		if(closeButtonIcon_superactive == null)
			closeButtonIcon_superactive = new ImageIcon(getInstance().getClass()
					.getResource("/com/dmide/assets/tabclosebutton-active-down.png"));
		return closeButtonIcon_superactive;
	}

	@Override
	public void paintCloseButton(TabHeaderCloseButton button, Graphics g) {
		if(button.isMouseOver()) {
			if(button.isMouseDown()) {
				Icon downIcon = getCloseButtonIcon_superactive();
				downIcon.paintIcon(button, g, 0, 0);
			} else {
				Icon overIcon = getCloseButtonIcon_active();
				overIcon.paintIcon(button, g, 0, 0);
			}
		} else {
			Icon inactiveIcon = getCloseButtonIcon_inactive();
			inactiveIcon.paintIcon(button, g, 0, 0);
		}
	}

	@Override
	public Dimension getClosebuttonSize() {
		return bDim;
	}

}
