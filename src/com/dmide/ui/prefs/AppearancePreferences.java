package com.dmide.ui.prefs;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.dmide.DMIDE;
import com.dmide.util.events.IDEEvent;
import com.dmide.util.events.IDEEventHandler;

public class AppearancePreferences extends PreferencesPage {

	ImageIcon bicon;

	public AppearancePreferences() {
		super();
		this.createUI();
	}

	public void createUI() {
		this.setLayout(new BorderLayout());
		JTabbedPane jtp = new JTabbedPane();
		this.add(jtp, BorderLayout.CENTER);
		DMIDE.setProperty("prefs.page.appearance", this);
		DMIDE.setProperty("prefs.page.appearance.tabs", jtp);
	}

	@Override
	public Icon getButtonIcon() {
		if(this.bicon == null) {
			this.bicon = new ImageIcon(this.getClass().getResource("/com/dmide/assets/color.png"));
		}
		return this.bicon;
	}

	@Override
	public String getButtonText() {
		return "Appearance";
	}

	@Override
	public String getTitle() {
		return "Appearance";
	}

	@Override
	public void onDisplay(PreferencesDialog dialog) {
		//Tells the IDE that the appearance page has been opened.
		IDEEventHandler.sendIDEEvent(new IDEEvent("prefs.page.opened:appearance", this));
	}

}
