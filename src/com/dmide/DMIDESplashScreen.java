package com.dmide;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class DMIDESplashScreen extends JFrame {

	public DMIDESplashScreen() {
		this.initComponents();
	}

	public void initComponents() {
		this.setTitle("JDMIDE is starting...");
		this.setUndecorated(true);
		this.getContentPane().setLayout(null); // X Y Absolute.
		ImageIcon imageIcon = new ImageIcon( this.getClass().getResource("/com/dmide/assets/JDMIDE-Splashscreen.png") );
		JLabel bg = new JLabel();
		bg.setIcon(imageIcon);
		bg.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
		bg.setLocation(0, 0);

		JPanel mainPanel = new JPanel();
		mainPanel.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
		mainPanel.setLayout(null);
		mainPanel.add(bg);
		mainPanel.setBorder(new LineBorder(Color.black));
		mainPanel.setLocation(1, 1);

		this.getContentPane().add(mainPanel);
		this.setSize(imageIcon.getIconWidth() + 1, imageIcon.getIconHeight()  + 1);
		this.position();
	}

	public void position() {
		Dimension screenD = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenD.getWidth() / 2 - (this.getWidth() / 2),
				screenD.getHeight() / 2 - (this.getHeight() / 2));
	}

	private void setLocation(double w, double h) {
		this.setLocation((int)w, (int)h);
	}

	public void displaySplash() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void hideSplash() {
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setVisible(false);
	}

	static DMIDESplashScreen instance;
	public static DMIDESplashScreen getInstance() {
		if(instance == null) instance = new DMIDESplashScreen();
		return instance;
	}
}
