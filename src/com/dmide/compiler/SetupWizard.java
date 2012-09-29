package com.dmide.compiler;

import javax.swing.JOptionPane;

import com.dmide.DMIDE;
import com.dmide.ui.DMIDEUI;

public class SetupWizard {

	public boolean runWizard() {
		
		boolean setupFailed = false;
		
		int setupYesNo = JOptionPane.showConfirmDialog(DMIDEUI.getInstance().getMainWindow(),
				"Before starting JDMIDE, you must complete a first-time setup wizard.\n\nDo you wish to setup JDMIDE now?",
				"Setup Wizard", JOptionPane.YES_NO_OPTION);

		if (setupYesNo == JOptionPane.NO_OPTION) {
			setupFailed = true;
		}
		
		while(!setupFailed) {

			// Scan for their BYOND installation folder and profile folder.
			int allowFindInstall = JOptionPane.showConfirmDialog(DMIDEUI.getInstance().getMainWindow(),
					"JDMIDE would like to try and find your BYOND directory, is this okay?",
					"Setup Wizard", JOptionPane.YES_NO_OPTION);
			if(allowFindInstall == JOptionPane.YES_OPTION) {
				try {
					BYONDValidation ValidateDirs = new BYONDValidation();
					if(ValidateDirs.setBYONDDir() == false) {setupFailed = true;}
					if(ValidateDirs.setProfileDir() == false) {setupFailed = true;}
				} catch (Exception e) {
					e.printStackTrace();
					setupFailed = true;
				}
				
				JOptionPane.showMessageDialog(DMIDEUI.getInstance().getMainWindow(),
				String.format("Found BYOND directory at %s.", DMIDE.getProperty("byond.location")));
			
			} else {
				setupFailed = true;
			}
			
			// Ask what skin color they would like to use.
			
			JOptionPane.showMessageDialog(DMIDEUI.getInstance().getMainWindow(), "Setup is now complete.");
			return true;
			
		}
		
		if(setupFailed == true) {
			// An error occurred during setup, or the user selected no to completing the wizard.
			JOptionPane.showMessageDialog(DMIDEUI.getInstance().getMainWindow(),
					"Setup did not complete sucessfully.\nBefore attempting to run JDMIDE, you must complete the first-time setup wizard.",
					"Setup Wizard", JOptionPane.ERROR_MESSAGE);
					// FIXME: Close the application.
					return false;
		}
	
	return false;

	}
	
}
