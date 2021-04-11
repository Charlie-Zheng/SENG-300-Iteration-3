package org.lsmr.selfcheckout.control.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Static class that can perform some GUI animations
 * @author Vianney Nguyen
 *
 */
public class GUIUtils {

	public enum Property {
		BACKGROUND_COLOR, LABEL
	}

	private GUIUtils() {
	}

	/**
	 * Flashes the [view] red at the given [section] for [duration] seconds.
	 * 
	 * @param view
	 * @param property
	 * @param duration
	 */
	public static void flashError(JComponent view, float duration) {
		final Color red = new Color(255, 80, 80);
		flashProperty(view, Property.BACKGROUND_COLOR, duration, red);
	}
	
	public static void flashText(JLabel view, float duration, String text) {
		flashProperty(view, Property.LABEL, duration, text);
	}
	
	
	/**
	 * Flashes the view at a given section with the specified color, for the given number of seconds
	 * @param view
	 * @param property
	 * @param duration
	 * @param color
	 */
	public static <T> void flashProperty(JComponent view, Property property, float duration, T data) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				setItem(view, property, data);
			}
		});
		

		// restore view after a specified period of time
		final T original = retrieveOriginal(view, property);
		Timer restoreTimer = new Timer((int) (duration * 1000), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setItem(view, property, original);
			}
		});
		restoreTimer.setRepeats(false);
		restoreTimer.start();
	}

	/**
	 * Helper method that sets a property of a view
	 * @param view
	 * @param section
	 * @param item
	 */
	private static void setItem(JComponent view, Property section, Object item) {
		switch(section) {
		case BACKGROUND_COLOR:
			view.setBackground((Color) item);
			break;
		case LABEL:
			((JLabel) view).setText((String) item);
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T retrieveOriginal(JComponent view, Property section) {
		switch(section) {
		case BACKGROUND_COLOR:
			return (T) view.getBackground();
		case LABEL:
			return (T) ((JLabel) view).getText();
		default:
			return null;
		}
	}

}
