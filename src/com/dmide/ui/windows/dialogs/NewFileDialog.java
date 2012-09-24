package com.dmide.ui.windows.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

public class NewFileDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public JTextField txtFileName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewFileDialog dialog = new NewFileDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public NewFileDialog(Frame f) {
		super(f);
		this.setTitle("New File");
		this.setBounds(100, 100, 400, 140);
		this.getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		this.contentPanel.setLayout(new MigLayout("", "[][grow]", "[][]"));
		{
			JLabel lblFileType = new JLabel("File Type:");
			this.contentPanel.add(lblFileType, "cell 0 0,alignx trailing");
		}
		{
			JComboBox comboBoxFileType = new JComboBox();
			this.contentPanel.add(comboBoxFileType, "cell 1 0,growx");
		}
		{
			JLabel lblFileName = new JLabel("File Name:");
			this.contentPanel.add(lblFileName, "cell 0 1,alignx trailing");
		}
		{
			this.txtFileName = new JTextField();
			this.contentPanel.add(this.txtFileName, "cell 1 1,growx");
			this.txtFileName.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			this.getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				this.getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
