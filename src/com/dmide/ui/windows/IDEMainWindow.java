package com.dmide.ui.windows;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.miginfocom.swing.MigLayout;

import com.dmide.ui.filetree.FileTree;

@SuppressWarnings("serial")
public class IDEMainWindow extends JFrame {

	private JPanel contentPane;
	private JSplitPane splitPane;
	private JPanel panel;
	private JTabbedPane tabbedPane;
	private JSplitPane splitPane_1;
	private JTabbedPane tabbedPane_1;
	private JTabbedPane fileEditorPane;
	private JPanel panel_1;
	private JScrollPane scrollPane;
	private FileTree tree;
	private JPanel panel_2;
	private JProgressBar progressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					IDEMainWindow frame = new IDEMainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public IDEMainWindow() {
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(IDEMainWindow.class.getResource("/com/dmide/assets/JDMIDE-Icon.png")));
		this.setTitle("JDMIDE");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 680, 500);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(new BorderLayout(0, 0));
		this.contentPane.add(this.getSplitPane(), BorderLayout.CENTER);
		this.contentPane.add(this.getPanel_2(), BorderLayout.SOUTH);
	}

	public JSplitPane getSplitPane() {
		if (this.splitPane == null) {
			this.splitPane = new JSplitPane();
			this.splitPane.setResizeWeight(0.5);
			this.splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			this.splitPane.setRightComponent(this.getPanel());
			this.splitPane.setLeftComponent(this.getSplitPane_1());
			this.splitPane.setDividerLocation(320);
		}
		return this.splitPane;
	}
	public JPanel getPanel() {
		if (this.panel == null) {
			this.panel = new JPanel();
			this.panel.setLayout(new BorderLayout(0, 0));
			this.panel.add(this.getTabbedPane(), BorderLayout.CENTER);
		}
		return this.panel;
	}
	public JTabbedPane getTabbedPane() {
		if (this.tabbedPane == null) {
			this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		}
		return this.tabbedPane;
	}
	public JSplitPane getSplitPane_1() {
		if (this.splitPane_1 == null) {
			this.splitPane_1 = new JSplitPane();
			this.splitPane_1.setLeftComponent(this.getTabbedPane_1());
			this.splitPane_1.setRightComponent(this.getFileEditorPane());
			this.splitPane_1.setDividerLocation(200);
		}
		return this.splitPane_1;
	}
	public JTabbedPane getTabbedPane_1() {
		if (this.tabbedPane_1 == null) {
			this.tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
			this.tabbedPane_1.addTab("File Tree", null, this.getPanel_1(), null);
		}
		return this.tabbedPane_1;
	}
	public JTabbedPane getFileEditorPane() {
		if (this.fileEditorPane == null) {
			this.fileEditorPane = new JTabbedPane(JTabbedPane.TOP);
		}
		return this.fileEditorPane;
	}
	public JPanel getPanel_1() {
		if (this.panel_1 == null) {
			this.panel_1 = new JPanel();
			this.panel_1.setLayout(new BorderLayout(0, 0));
			this.panel_1.add(this.getScrollPane(), BorderLayout.CENTER);
		}
		return this.panel_1;
	}
	public JScrollPane getScrollPane() {
		if (this.scrollPane == null) {
			this.scrollPane = new JScrollPane();
			this.scrollPane.setViewportView(this.getTree_1());
		}
		return this.scrollPane;
	}
	public FileTree getTree_1() {
		if (this.tree == null) {
			this.tree = new FileTree();
			this.tree.setModel(new DefaultTreeModel(
				new DefaultMutableTreeNode("No Environment") {
					{
					}
				}
			));
		}
		return this.tree;
	}
	public JPanel getPanel_2() {
		if (this.panel_2 == null) {
			this.panel_2 = new JPanel();
			this.panel_2.setLayout(new MigLayout("", "[grow]", "[20.00]"));
			this.panel_2.add(this.getProgressBar_1(), "cell 0 0,growx");
		}
		return this.panel_2;
	}
	public JProgressBar getProgressBar_1() {
		if (this.progressBar == null) {
			this.progressBar = new JProgressBar();
			this.progressBar.setToolTipText("No tasks at this time...");
		}
		return this.progressBar;
	}
	public JProgressBar getProgressBar() {
		return this.getProgressBar_1();
	}
}
