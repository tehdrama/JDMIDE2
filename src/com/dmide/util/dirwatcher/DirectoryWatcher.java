package com.dmide.util.dirwatcher;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

import com.dmide.util.events.IDEEventWatcher;

public class DirectoryWatcher {

	boolean watching = false;
	boolean dw = false;
	Path directory;

	ArrayList<IDEEventWatcher> _watchers;

	public void addWatcher(IDEEventWatcher w) {
		this._watchers.add(w);
	}

	public void removeWatcher(IDEEventWatcher w) {
		this._watchers.remove(w);
	}

	public void addAllWatchers(Collection< ? extends IDEEventWatcher > w) {
		this._watchers.addAll(w);
	}

	public DirectoryWatcher() {
		this._watchers = new ArrayList<>();
	}

	public DirectoryWatcher(Path watchPath) {
		this();
		this.directory = watchPath;
	}

	public DirectoryWatcher(File watchDirectory) {
		this();
		if(watchDirectory != null) this.directory = watchDirectory.toPath();
	}

	/**
	 * Starts watching with the specified delay
	 * in between polls.
	 * @param delay delay between polls in milliseconds.
	 */
	public void watch(final long delay) {
		if(this.watching) return;
		else {
			final DirectoryWatcher dirWatcher = this;
			Runnable r = new Runnable() {
				@Override
				public void run() {
					dirWatcher.backgroundWatch(delay);
				}
			};
			Thread t = new Thread(r, "Directory Watcher: ".concat(Long.toString(System.currentTimeMillis())));
			this.watching = true;
			t.start();
		}
	}

	/**
	 * Starts polling for changes on a background thread.
	 * Calls {@link DirectoryWatcher#watch(long)} using
	 * 500 milliseconds as the delay.
	 */
	public void watch() {
		this.watch(500);
	}

	/**
	 * Will force the watcher to stop watching its directory.
	 */
	public void stopwatching() {
		this.watching = false;
	}

	/**
	 * The watching that takes place in the background thread.
	 */
	private void backgroundWatch(long delay) {
		try {
			this.watching = true;
			this.dw = true;
			while( this.watching && this.dw ) {
				System.out.println("Directory watching...");
				Thread.sleep(delay);
			}
			this.dw = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @return Whether or not this watcher is watching it's
	 *         assigned directory.
	 */
	public boolean isWatching() {
		return (this.watching || this.dw);
	}

	/**
	 *
	 * @return The directory for this watcher to listen in on.
	 */
	public Path getWatchDirectory() {
		return this.directory;
	}

	/**
	 * Sets the directory to be watched.
	 * @param watchDirectory
	 */
	public void setWatchDirectory(Path watchDirectory) {
		this.directory = watchDirectory;
	}
}
