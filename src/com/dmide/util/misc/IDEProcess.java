package com.dmide.util.misc;

import com.dmide.util.events.IDEEvent;
import com.dmide.util.events.IDEEventHandler;

public class IDEProcess {
	int progress;
	int id;
	static int IDCounter = 0;

	public IDEProcess() {
		this.id = IDEProcess.IDCounter++;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IDEProcess) {
			IDEProcess p = (IDEProcess) obj;
			return (p.id == this.id);
		}
		return super.equals(obj);
	}

	public int getProgress() {return this.progress;}
	public void setProgress(int val) {this.progress = val; this.valChange();}
	public void setProgress(double val) {this.progress = (int)val; this.valChange();}

	public void start() {this.progress = 0; IDEEventHandler.sendIDEEvent(new IDEEvent("progress.start", this));}
	public void end() {this.progress = 0; IDEEventHandler.sendIDEEvent(new IDEEvent("progress.end", this));}

	/**
	 * Sends an event telling alerting components that values have changed.
	 */
	private void valChange() {
		IDEEventHandler.sendIDEEvent(new IDEEvent("progress.change", this));
	}

}
