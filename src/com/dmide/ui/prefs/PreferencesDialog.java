package com.dmide.ui.prefs;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.l2fprod.common.swing.JButtonBar;


@SuppressWarnings("serial")
public class PreferencesDialog extends JDialog {

	public PreferencesDialog() {
		super();
	}

	public PreferencesDialog(Frame frame) {
		super(frame);
	}

	JPanel displayPanel;
	JButtonBar buttonBar;
	JPanel content;
	ButtonGroup bgroup;
	CardLayout layout;

	public void createUI() {
		this.setTitle("Preferences");
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		this.setSize(480, 320);
		this.getContentPane().setLayout(new BorderLayout());
		this.content = new JPanel(new BorderLayout());
		this.getContentPane().add(this.content, BorderLayout.CENTER);
		this.layout = new CardLayout();
		this.displayPanel = new JPanel(this.layout);
		this.buttonBar = new JButtonBar(JButtonBar.WEST);
		this.buttonBar.setBackground(UIManager.getColor("List.background"));
		this.content.add(this.displayPanel, BorderLayout.CENTER);
		this.content.add(this.buttonBar, BorderLayout.WEST);
		this.bgroup = new ButtonGroup();

		this.displayPanel.add(new JPanel(), "null");

		this.layout.show(this.displayPanel, "null");
	}

	public void displayPage(PreferencesPage prefs) {
		this.setTitle("Preferences - ".concat(prefs.getTitle()));
		this.layout.show(this.displayPanel, prefs.getcardID());
		prefs.onDisplay(this);
	}

	public void addPreferencesPage(final PreferencesPage prefsPage) {
		JToggleButton b = new JToggleButton();
		b.setIcon(prefsPage.getButtonIcon());
		b.setText(prefsPage.getButtonText());

		if(!prefsPage.hasCardID()) {
			prefsPage.setCardID(Long.toString(System.nanoTime()));
		}

		this.displayPanel.add(prefsPage, prefsPage.getcardID());
		final PreferencesDialog pd = this;

		b.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pd.displayPage(prefsPage);
			}});


		this.buttonBar.add(b);
		this.bgroup.add(b);
	}

}
