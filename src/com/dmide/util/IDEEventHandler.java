package com.dmide.util;

import java.util.ArrayList;

/**
 * Handles events being sent across the IDE.
 *
 * @author Saoude1
 *
 */
public class IDEEventHandler {
	static ArrayList<IDEEventWatcher> eventWatchers;

	static {
		eventWatchers = new ArrayList<>();
	}

	public static boolean sendIDEEvent(IDEEvent e) {
		for(IDEEventWatcher watcher : eventWatchers) {
			watcher.eventRecieved(e);
			if(e.blockOnHandled() && e.isHandled()) break;
		}
		return e.isHandled();
	}

	public static void addWatcher(IDEEventWatcher w) {
		eventWatchers.add(w);
	}

	public static void removeWatcher(IDEEventWatcher w) {
		eventWatchers.remove(w);
	}
}
