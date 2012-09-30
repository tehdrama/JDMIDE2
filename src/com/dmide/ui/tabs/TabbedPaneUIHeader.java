package com.dmide.ui.tabs;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * The header component of a TabbedPaneUI.
 * @author Adolph C.
 *
 */
@SuppressWarnings("serial")
public class TabbedPaneUIHeader extends JPanel {
	/**
	 * The label used to display text and the icon in the tab header.
	 */
	JLabel headerLabel;

	/**
	 * The close button in the tab header.
	 */
	TabHeaderCloseButton headerCloseButton;

	/**
	 * Used to paint the tab header's components.
	 */
	TabbedPaneUIHeaderPainter headerPainter;

	/**
	 * Handles the closing of the {@link TabbedPaneUI} associated with this header.
	 */
	TabCloseHandler tabCloseHandler;

	/**
	 * The {@link TabbedPaneUI} associated with this component.
	 */
	TabbedPaneUI tabbedPaneUI;

	/**
	 * The {@link JTabbedPane} associated with this component.
	 */
	JTabbedPane tabbedPane;

	public TabbedPaneUIHeader() {
		this.createComponents();
		this.setHeaderPainter(DefaultTabbedPaneUIHeaderPainter.getInstance());
	}

	/**
	 * Creates and positions the components that need to go into the JPanel.
	 */
	public void createComponents() {
		this.setLayout( new FlowLayout(FlowLayout.LEADING, 2, 0) );

		this.headerLabel = new JLabel();
		this.headerCloseButton = new TabHeaderCloseButton(this.headerPainter);

		this.headerLabel.setOpaque(false);
		this.headerCloseButton.setOpaque(false);
		this.setOpaque(false);

		this.add(this.headerLabel);
		this.add(this.headerCloseButton);

		this.addActionListeners();
	}

	public void addActionListeners() {
		final TabbedPaneUIHeader tpuh = this;
		this.headerCloseButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tpuh.tabCloseButtonClicked();
			}
		});
	}

	public void tabCloseButtonClicked() {
		if(this.getTabCloseHandler() == null ||
				this.getTabCloseHandler().canClose(this.getTabbedPaneUI())) {
			this.closeSelfTab();
		}
	}

	public void closeSelfTab() {
		//Can't close the tab in the following cases.
		if(this.getTabbedPane() == null || this.getTabbedPaneUI() == null) return;
		if(!(this.getTabbedPaneUI() instanceof Component) ) return;

		this.getTabbedPane().remove((Component)this.getTabbedPaneUI());
		this.getTabbedPane().remove(this);
	}

	/**
	 * Installs this header on the specified {@link JTabbedPane}
	 * and the specified {@link TabbedPaneUI}
	 * @param tpu The {@link TabbedPaneUI} to install this header for.
	 * @param jtp The {@link JTabbedPane}  to install this header on.
	 */
	public void install(TabbedPaneUI tpu, JTabbedPane jtp) {
		if(!(tpu instanceof Component)) return;
		this.setTabbedPaneUI(tpu);
		this.setTabbedPane(jtp);
		this.updateComponents();
		int componentindex = getComponentIndex((Component)tpu, jtp);
		if(componentindex < 0) return;
		jtp.setTabComponentAt(componentindex, this);
	}

	/**
	 * Calls {@link TabbedPaneUIHeader#install(TabbedPaneUI, JTabbedPane)} with the default
	 * @param jtp
	 */
	public void install(JTabbedPane jtp) {
		this.install(this.tabbedPaneUI, jtp);
	}

	/**
	 * Finds the specified component in the specified JTabbedPane
	 * and returns the index it was found at.
	 * @param c
	 * @param jtp
	 * @return the index the specified component was found at.
	 *         returns -1 if the component could not be found.
	 */
	public static int getComponentIndex(Component c, JTabbedPane jtp) {
		for(int i = 0; i < jtp.getComponentCount(); i++) {
			Component cai = jtp.getComponentAt(i);
			if(cai != null && cai.equals(c)) {return i;}
		}
		return -1;
	}

	/**
	 * Updates the components using {@link TabbedPaneUIHeader#getTabbedPaneUI()}.
	 */
	public void updateComponents() {
		TabbedPaneUI tpu = this.getTabbedPaneUI();
		if(tpu == null) return;
		this.getHeaderLabel().setIcon(tpu.getTabIcon());
		this.getHeaderLabel().setText(tpu.getTabTitle());
	}


	/**
	 * @return the headerLabel
	 */
	public JLabel getHeaderLabel() {
		return this.headerLabel;
	}

	/**
	 * @param headerLabel the headerLabel to set
	 */
	public void setHeaderLabel(JLabel headerLabel) {
		this.headerLabel = headerLabel;
	}

	/**
	 * @return the headerCloseButton
	 */
	public TabHeaderCloseButton getHeaderCloseButton() {
		return this.headerCloseButton;
	}

	/**
	 * @param headerCloseButton the headerCloseButton to set
	 */
	public void setHeaderCloseButton(TabHeaderCloseButton headerCloseButton) {
		this.headerCloseButton = headerCloseButton;
	}

	/**
	 * @return the headerPainter
	 */
	public TabbedPaneUIHeaderPainter getHeaderPainter() {
		return this.headerPainter;
	}

	/**
	 * @param headerPainter the headerPainter to set
	 */
	public void setHeaderPainter(TabbedPaneUIHeaderPainter headerPainter) {
		this.headerPainter = headerPainter;
		if(this.getHeaderCloseButton() != null) this.getHeaderCloseButton().setBpainter(headerPainter);
	}

	/**
	 * @return the tabCloseHandler
	 */
	public TabCloseHandler getTabCloseHandler() {
		return this.tabCloseHandler;
	}

	/**
	 * @param tabCloseHandler the tabCloseHandler to set
	 */
	public void setTabCloseHandler(TabCloseHandler tabCloseHandler) {
		this.tabCloseHandler = tabCloseHandler;
	}

	/**
	 * @return the tabbedPaneUI
	 */
	public TabbedPaneUI getTabbedPaneUI() {
		return this.tabbedPaneUI;
	}

	/**
	 * @param tabbedPaneUI the tabbedPaneUI to set
	 */
	public void setTabbedPaneUI(TabbedPaneUI tabbedPaneUI) {
		this.tabbedPaneUI = tabbedPaneUI;
	}

	/**
	 * @return the tabbedPane
	 */
	public JTabbedPane getTabbedPane() {
		return this.tabbedPane;
	}

	/**
	 * @param tabbedPane the tabbedPane to set
	 */
	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	public void updateTabUI() {
		this.updateComponents();
		this.revalidate();
	}



}
