package com.dmide.ui;

import java.util.ArrayList;

import javax.swing.JProgressBar;

import com.dmide.util.events.IDEEvent;
import com.dmide.util.events.IDEEventWatcher;
import com.dmide.util.misc.IDEProcess;

public class IDEProgressHandler implements IDEEventWatcher {

	JProgressBar progressBar;

	ArrayList<IDEProcess> processes = new ArrayList<>();

	public IDEProgressHandler(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	@Override
	public void eventRecieved(IDEEvent e) {
		if(e.getEventName().equals("progress.start")) {
			this.addProcess(e.getArgument(IDEProcess.class));
		} else if(e.getEventName().equals("progress.change")) {
			this.processProgressChanged(e.getArgument(IDEProcess.class));
		} else if(e.getEventName().equals("progress.end")) {
			this.removeProcess(e.getArgument(IDEProcess.class));
		}
	}

	public void addProcess(IDEProcess p) {
		if(p != null) {this.processes.add(p); this.updateProgress();}
	}

	public void removeProcess(IDEProcess p) {
		if(p != null) {this.processes.remove(p); this.updateProgress();}
	}

	public void processProgressChanged(IDEProcess p) {
		if(this.processes.contains(p)) {
			this.updateProgress();
		}
	}

	public void updateProgress() {
		if(this.progressBar == null) return;
		int max = 0;

		for(IDEProcess p : this.processes) {
			max += p.getProgress();
			if(p.getProgress() < 0) {
				max = -1; // indeterminate...
				break;
			}
		}

		final int _max = max;
		final int n = this.processes.size();
		final JProgressBar pbar = this.progressBar;

		Runnable prun = null;

		if(max == -1) {
			prun = new Runnable()
			{@Override
			public void run() {
				pbar.setIndeterminate(true);
			}};
		} else if(max == 0 || n < 1) {
			prun = new Runnable() {
				@Override
			public void run() {
				pbar.setIndeterminate(false);
				pbar.setValue(0);
			}};
		} else {
			prun = new Runnable() {
				@Override
			public void run() {
				pbar.setValue( _max / n );
			}};
		}

		UIUtil.toEventQueue(prun);
	}

}
