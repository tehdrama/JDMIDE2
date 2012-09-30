package com.dmide.ui.filetree;

import java.awt.Graphics;

import javax.swing.JCheckBox;

import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.ui.SubstanceCheckBoxUI;
import org.pushingpixels.substance.internal.ui.SubstanceRadioButtonUI;


@SuppressWarnings("serial")
public class CustomTreeCheckbox extends JCheckBox {
	StateTransitionTracker stateTransitionTracker;
	SubstanceRadioButtonUI rbu;

	public CustomTreeCheckbox() {
		this.rbu = (SubstanceRadioButtonUI) SubstanceCheckBoxUI.createUI(this);
	}

	@Override
	public void paint(Graphics g) {
		//SubstanceCoreUtilities.getOriginalIcon(this, this.rbu.getDefaultIcon()).paintIcon(this, g, 0, 0);
		//g.setColor(Color.white);
		SubstanceColorScheme cs = SubstanceLookAndFeel.getCurrentSkin().getColorScheme(this, ComponentState.DEFAULT);
		//System.out.println("Foreground: " + cs.getForegroundColor().toString());
		//if(this.isSelected() && this.getSelectedIcon() != null) this.getSelectedIcon().paintIcon(this, g, 0, 0);
		this.rbu.getDefaultIcon().paintIcon(this, g, 0, 0);
		if(this.isSelected()) {
			g.setColor(cs.getForegroundColor());
			g.fillOval(4, 4, 8, 8);
		}
	}
}