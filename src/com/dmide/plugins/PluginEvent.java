package com.dmide.plugins;

/**
 * Events passed to plugins for processing.
 *
 * @author Adolph C.
 *
 */
public class PluginEvent {
	String evtName;
	Object arg;
	boolean handled = false;

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

}
