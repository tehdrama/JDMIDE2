package com.dmide.ui.prefs;

import javax.swing.Icon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class PreferencesPage extends JPanel {

	String cardID;

	/**
	 *
	 * @return the icon of the button that points to this page.
	 */
	public abstract Icon getButtonIcon();

	/**
	 *
	 * @return the text under the button that points to this page.
	 */
	public abstract String  getButtonText();

	/**
	 *
	 * @return the title of the preferences dialog when this page is open.
	 */
	public abstract String getTitle();

	/**
	 *
	 * @return the id for the card layout of the preferences page.
	 */
	public String getcardID() {return this.cardID;}

	/**
	 *
	 * @return true if this has a cardID.
	 */
	public boolean hasCardID() { return (this.cardID != null); }

	/**
	 * Sets the card id of this preferences page.
	 * @param v
	 */
	public void setCardID(String v) {this.cardID = v;}

	/**
	 * Called when this is displayed in a preferences dialog.
	 * @param dialog the dialog displaying this page.
	 */
	public abstract void onDisplay(PreferencesDialog dialog);
}
