package com.dmide.ui.tabs;

public interface TabCloseHandler {
	/**
	 * Called to check if a tab can be closed. Returns true if the tab
	 * should be closed, false otherwise.
	 * @param ui the {@link TabbedPaneUI} associated with this TabCloseHandler.
	 * @return true if the tab should be closed, false otherwise.
	 */
	public boolean canClose(TabbedPaneUI ui);

	/**
	 * Called after a tab has been closed.
	 * @param ui the {@link TabbedPaneUI} associated with this TabCloseHandler.
	 */
	public void onClose(TabbedPaneUI ui);
}
