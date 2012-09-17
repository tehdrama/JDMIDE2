package com.dmide.util;

/**
 * Events passed to plugins for processing.
 *
 * @author Adolph C.
 *
 */
public class IDEEvent {
	String evtName;
	Object arg;
	boolean handled = false;
	private boolean blockOnHandled;

	/**
	 * @param evtName
	 */
	public IDEEvent(String evtName) {
		this.evtName = evtName;
	}

	/**
	 * @param evtName
	 * @param arg
	 */
	public IDEEvent(String evtName, Object arg) {
		this.evtName = evtName;
		this.arg = arg;
	}

	/**
	 * @param evtName
	 * @param arg
	 * @param blockOnHandled
	 */
	public IDEEvent(String evtName, Object arg, boolean blockOnHandled) {
		this.evtName = evtName;
		this.arg = arg;
		this.blockOnHandled = blockOnHandled;
	}

	/**
	 *
	 * @return the name of this event.
	 */
	public String getEventName() {
		return this.evtName;
	}

	/**
	 *
	 * @return the argument (object) attached with this PluginEvent.
	 */
	public Object getArgument() {
		return this.arg;
	}

	/**
	 * @param referenceClass
	 * @return the argument of this PluginEvent as an instance of the Reference
	 *         Class, if the arg cannot be casted correctly, null is returned.
	 */
	public <T> T getArgument(Class<T> referenceClass) {
		if (referenceClass.isInstance(this.arg)) {
			return referenceClass.cast(this.arg);
		} else {
			return null;
		}
	}

	/**
	 * @return whether of not this event has been handled, if true, the plugin
	 *         container will no longer pass this event to other plugins.
	 */
	public boolean isHandled() {
		return this.handled;
	}

	/**
	 * Sets whether or not this plugin has been handled.
	 */
	public void setHandled(boolean handled) {
		this.handled = handled;
	}

	public boolean blockOnHandled() {
		return this.blockOnHandled;
	}

}
