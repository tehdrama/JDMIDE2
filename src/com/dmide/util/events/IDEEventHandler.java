package com.dmide.util.events;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

import com.dmide.util.dirwatcher.DirectoryWatcher;

/**
 * Handles events being sent across the IDE.
 *
 * @author Adolph C.
 *
 */
public class IDEEventHandler {
	static ArrayList<IDEEventWatcher> eventWatchers;
	static ArrayList<IDEEventWatcher> directoryEventWatchers;
	static DirectoryWatcher mainDirectoryWatcher;

	static {
		eventWatchers = new ArrayList<>();
		directoryEventWatchers = new ArrayList<>();
	}

	public static boolean sendIDEEvent(IDEEvent e) {
		System.out.println("Sending Event: " + e.getEventName());
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

	public static void addDirectoryWatcher(IDEEventWatcher w) {
		directoryEventWatchers.add(w);
		if(mainDirectoryWatcher != null) mainDirectoryWatcher.addWatcher(w);
	}

	public static void removeDirectoryWatcher(IDEEventWatcher w) {
		directoryEventWatchers.remove(w);
		if(mainDirectoryWatcher != null) mainDirectoryWatcher.removeWatcher(w);
	}

	/**
	 * Will use the main DirectoryWatcher to watch the specified
	 * directory.
	 * @param f
	 */
	public static void watchDirectory(File f) {
		if(f == null) watchDirectory((Path) null);
		else watchDirectory(f.toPath());
	}

	/**
	 * Will use the main DirectoryWatcher to watch the specified
	 * directory.
	 * @param p
	 */
	public static void watchDirectory(Path p) {
		if(mainDirectoryWatcher == null) {
			mainDirectoryWatcher = new DirectoryWatcher();
			mainDirectoryWatcher.addAllWatchers(directoryEventWatchers);
		} else {
			if(mainDirectoryWatcher.isWatching()) {
				mainDirectoryWatcher.stopwatching();
				try {
					while(mainDirectoryWatcher.isWatching()) {Thread.sleep(20);} // Wait until watching has stopped.
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		mainDirectoryWatcher.setWatchDirectory(p);
		mainDirectoryWatcher.watch();
	}
}
