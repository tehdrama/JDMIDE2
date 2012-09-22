package com.dmide.ui;

import javax.swing.Icon;

/**
 * Cool things happen with TabbedPanes, figure it out.
 * @author Adolph C.
 *
 */
public interface TabbedPaneUI {
	/**
	 *Reveals the header to the tabbed pane UI, make sure to save it!
	 */
	public void setHeader(TabbedPaneUIHeader header);

	/**
	 *
	 * @return the title of the tab.
	 */
	public String getTabTitle();

	/**
	 *
	 * @return the icon displayed to the left of the title.
	 */
	public Icon getTabIcon();

	/**
	 *
	 * @return true if the close button should be visible.
	 */
	public boolean displayTabCloseButton();

	/**
	 *
	 * @return a unique identifier with a .equals method to find this tab.
	 */
	public Object getIdentifier();

	/**
	 *
	 * @return true if this tab can be closed, false otherwise.
	 */
	public boolean canCloseTab();

	/**
	 * Called when the tab is closed successfully.
	 */
	public void onClose();
}
