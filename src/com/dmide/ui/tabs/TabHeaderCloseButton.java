package com.dmide.ui.tabs;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;


public class TabHeaderCloseButton extends JButton {
	TabbedPaneUIHeaderPainter bpainter;

	/**
	 * Whether or not the mouse is over this component.
	 */
	boolean mouseOver;

	/**
	 * Whether or not the mouse is down on this component.
	 */
	boolean mouseDown;

	/**
	 *
	 */
	public TabHeaderCloseButton(TabbedPaneUIHeaderPainter bpainter) {
		super();
		this.installMouseListeners();
		this.bpainter = bpainter;
	}

	/**
	 * @param a
	 */
	public TabHeaderCloseButton(Action a, TabbedPaneUIHeaderPainter bpainter) {
		super(a);
		this.installMouseListeners();
		this.bpainter = bpainter;
	}

	/**
	 * @param icon
	 */
	public TabHeaderCloseButton(Icon icon, TabbedPaneUIHeaderPainter bpainter) {
		super(icon);
		this.installMouseListeners();
		this.bpainter = bpainter;
	}

	/**
	 * @param text
	 * @param icon
	 */
	public TabHeaderCloseButton(String text, Icon icon, TabbedPaneUIHeaderPainter bpainter) {
		super(text, icon);
		this.installMouseListeners();
		this.bpainter = bpainter;
	}

	/**
	 * @param name
	 */
	public TabHeaderCloseButton(String name, TabbedPaneUIHeaderPainter bpainter) {
		super(name);
		this.installMouseListeners();
		this.bpainter = bpainter;
	}

	@Override
	public void paintComponent(Graphics g) {
		if(this.bpainter != null) {
			if(!this.getSize().equals(this.bpainter.getClosebuttonSize())) {
				this.setPreferredSize(this.bpainter.getClosebuttonSize());
				this.setSize(this.bpainter.getClosebuttonSize());
			}
			this.bpainter.paintCloseButton(this, g);
		}
		else super.paintComponent(g);
	}

	public void installMouseListeners() {
		final TabHeaderCloseButton b = this;
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				b.setMouseOver(true);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				b.setMouseOver(false);
			}
			@Override
			public void mousePressed(MouseEvent e) {
				b.setMouseDown(true);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				b.setMouseDown(false);
			}});
	}

	/**
	 * @return the bpainter
	 */
	public TabbedPaneUIHeaderPainter getBpainter() {
		return this.bpainter;
	}

	/**
	 * @param bpainter the bpainter to set
	 */
	public void setBpainter(TabbedPaneUIHeaderPainter bpainter) {
		this.bpainter = bpainter;
	}

	/**
	 * @return the mouseOver
	 */
	public boolean isMouseOver() {
		return this.mouseOver;
	}

	/**
	 * @param mouseOver the mouseOver to set
	 */
	void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	/**
	 * @return the mouseDown
	 */
	public boolean isMouseDown() {
		return this.mouseDown;
	}

	/**
	 * @param mouseDown the mouseDown to set
	 */
	void setMouseDown(boolean mouseDown) {
		this.mouseDown = mouseDown;
	}




}
