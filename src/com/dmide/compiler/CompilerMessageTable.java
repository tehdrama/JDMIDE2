package com.dmide.compiler;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.dmide.ui.UIUtil;
import com.dmide.util.events.IDEEvent;
import com.dmide.util.events.IDEEventHandler;
import com.dmide.util.events.IDEEventWatcher;

@SuppressWarnings("serial")
public class CompilerMessageTable extends JTable implements IDEEventWatcher {

	boolean compiling = false;
	DefaultTableModel _model;

	ImageIcon infoIcon, errorIcon, warningIcon;

	public CompilerMessageTable() {
		super();
		IDEEventHandler.addWatcher(this);
		this.createIcons();
		this.createModel();
		this.setHeaders();
	}

	public void createIcons() {
		this.infoIcon = new ImageIcon(this.getClass().getResource(
				"/com/dmide/assets/information.png"));
		this.errorIcon = new ImageIcon(this.getClass().getResource(
				"/com/dmide/assets/compile-error.png"));
		this.warningIcon = new ImageIcon(this.getClass().getResource(
				"/com/dmide/assets/compile-warning.png"));
	}

	public void createModel() {
		this._model = new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		this.setModel(this._model);
	}

	public void setHeaders() {
		this._model.setColumnIdentifiers(new Object[]{"Type", "Message", "Line", "File"});

		this.getColumnModel().getColumn(0).setPreferredWidth(48);
		this.getColumnModel().getColumn(0).setMaxWidth(48);
		this.getColumnModel().getColumn(0).setMinWidth(48);

		this.getColumnModel().getColumn(2).setPreferredWidth(64);
		this.getColumnModel().getColumn(2).setMaxWidth(64);
	}

	@Override
	public void eventRecieved(IDEEvent e) {
		final CompilerMessageTable t = this;
		if(!this.compiling && e.getEventName().equals("compiling.start")) {
			this.compiling = true;
			Runnable r = new Runnable() {@Override
			public void run() {t.clearTable();}};
			UIUtil.toEventQueue(r);
		} else if(this.compiling && e.getEventName().equals("compiling.end")) {
			this.compiling = false;
		} else if(this.compiling && e.getEventName().equals("compiling.message")) {
			final CompilerMessage cm = e.getArgument(CompilerMessage.class);
			if(cm == null) {return;}
			else {
				System.out.println("Compiler Message!: " + cm.getMessage());
				Runnable r = new Runnable() {@Override
				public void run() {t.addCompilerMessage(cm);}};
				UIUtil.toEventQueue(r);
			}
		}
	}

	public void addCompilerMessage(CompilerMessage cm) {
		Icon _i = null;
		switch(cm.getMessageType()) {
		case ERROR:
			_i = this.errorIcon;
			break;
		case WARNING:
			_i = this.warningIcon;
			break;
		default:
			_i = this.infoIcon;
			break;
		}

		this._model.addRow(new Object[] {
				_i, cm,
				cm.getLine() > 0 ? Integer.toString(cm.getLine()) : "",
				cm.getFile() != null ? cm.getFile() : ""});
	}

	public void clearTable() {
		while(this._model.getRowCount() > 0) {
			this._model.removeRow(0);
		}
		this.revalidate();
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Class getColumnClass(int column)
	{
	    return this.getValueAt(0, column).getClass();
	}


}
