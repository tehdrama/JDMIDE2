package com.dmide.ui.prefs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import com.dmide.DMIDE;
import com.dmide.IDE;
import com.dmide.util.events.IDEEvent;
import com.dmide.util.events.IDEEventHandler;

@SuppressWarnings("serial")
public class GeneralPreferences extends PreferencesPage {

	JTable ptable;

	public GeneralPreferences() {
		this.createUI();
	}

	public void createUI() {
		final GeneralPreferences gp = this;

		JTabbedPane jtabbedpane = new JTabbedPane();

		JPanel propertiesListHolder = new JPanel(new BorderLayout());
		this.ptable = new JTable();
		JScrollPane sp = new JScrollPane(this.ptable);
		propertiesListHolder.add(sp, BorderLayout.CENTER);
		jtabbedpane.addTab("IDE Properties", propertiesListHolder);
		JPanel bpanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		JButton b = new JButton("Refresh");
		b.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gp.updatePropertiesTable();
			}});
		bpanel.add(b);
		propertiesListHolder.add(bpanel, BorderLayout.SOUTH);

		this.setLayout(new BorderLayout());
		this.add(jtabbedpane, BorderLayout.CENTER);
		DMIDE.setProperty("prefs.page.general", this);
		DMIDE.setProperty("prefs.page.general.tabs", jtabbedpane);
		DMIDE.setProperty("prefs.page.general.tabs.properties", propertiesListHolder);
		IDEEventHandler.sendIDEEvent(new IDEEvent("prefs.general.created", this));
	}

	public void updatePropertiesTable() {
		final DefaultTableModel defaultTableModel = new DefaultTableModel();
		defaultTableModel.setColumnIdentifiers(new String[]{"Property", "Value"});
		Set<Object> propkeys = IDE.getInstance().getAllProperties().keySet();
		ArrayList<String> ar = new ArrayList<>();
		for(Object o : propkeys) {
			if(o instanceof String) {
				ar.add((String)o);
			}
		}
		Collections.sort(ar);
		for(Object o : ar) {
			defaultTableModel.addRow(new Object[]{o,
					IDE.getInstance().getAllProperties().get(o)});
		}

		TableModelListener listener = new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				int r = e.getFirstRow();
				String k = (String) defaultTableModel.getValueAt(r, 0);
				String v = defaultTableModel.getValueAt(r, 1).toString();
				if(IDE.getInstance().isSavableProperty(k)) {
					IDE.getInstance().saveProperty(k, v);
				} else {
					IDE.getInstance().setProperty(k, v);
				}
			}
		};

		defaultTableModel.addTableModelListener(listener);

		this.ptable.setModel(defaultTableModel);


		this.revalidate();
	}

	@Override
	public Icon getButtonIcon() {
		if(this.bicon == null) {
			this.bicon = new ImageIcon(this.getClass().getResource("/com/dmide/assets/application-blue.png"));
		}
		return this.bicon;
	}

	@Override
	public String getButtonText() {
		return "General";
	}

	@Override
	public String getTitle() {
		return "General";
	}

	ImageIcon bicon;

	@Override
	public void onDisplay(PreferencesDialog dialog) {
		IDEEventHandler.sendIDEEvent(new IDEEvent("prefs.page.opened:general", this));
		//this.updatePropertiesTable();
	}

}
