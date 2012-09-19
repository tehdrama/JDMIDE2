package com.dmide.util.events;

/**
 * Sent events by the IDEEventHandler.
 * @author Adolph C.
 *
 */
public interface IDEEventWatcher {
	public void eventRecieved(IDEEvent e);
}
