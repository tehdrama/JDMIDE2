package com.dmide.ui;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

public class UIUtil {
	public static void toEventQueue(Runnable r) {
		if(EventQueue.isDispatchThread()) {
			r.run();
		} else {
			try {
				EventQueue.invokeAndWait(r);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
