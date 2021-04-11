package org.lsmr.selfcheckout.control.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Static class that can perform some GUI animations
 * @author Vianney Nguyen
 *
 */
public class GUIUtils {

	private JComponent view;

	public enum Property {
		BACKGROUND_COLOR, LABEL,

		RESTORE_RECENT, RESTORE_OLDEST, WAIT
	}

	public static GUIUtils begin(JComponent view) {
		return new GUIUtils(view);
	}
	
	private Queue<GUIJob> queue = new LinkedList<>();
	private LinkedList<GUIJob> restoreJobs = new LinkedList<>();

	private GUIUtils(JComponent view) {
		this.view = view;
	}
	


	/**
	 * Sets the view to have an error bg color
	 */
	public GUIUtils setError() {
		final Color red = new Color(255, 80, 80);
		queueSetProperty(Property.BACKGROUND_COLOR, red);

		return this;
	}

	/**
	 * Sets the bg color to th e given color
	 * @param color
	 * @return
	 */
	public GUIUtils setBgColor(Color color) {
		queueSetProperty(Property.BACKGROUND_COLOR, color);
		
		return this;
	}
	
	/**
	 * Set the text of the textfield
	 * @param text
	 * @return
	 */
	public GUIUtils setText(String text) {
		queueSetProperty(Property.LABEL, text);

		return this;
	}


	/**
	 * Waits for the specified amounto f seconds before running the next task
	 * @param seconds
	 * @return
	 */
	public GUIUtils waitFor(float seconds) {
		queue.add(new GUIJob(Property.WAIT, seconds));
		return this;
	}

	/**
	 * Restore the most recent change
	 * @return
	 */
	public GUIUtils restore() {
		queue.add(new GUIJob(Property.RESTORE_RECENT, null));
		return this;
	}

	/**
	 * Restore the oldest change
	 * @return
	 */
	public GUIUtils restoreOldest() {
		queue.add(new GUIJob(Property.RESTORE_OLDEST, null));
		return this;
	}
	
	
	public void execute() {
		GUIJob job = queue.peek();
		if (job == null) return; // termination - end of jobs

		LinkedList<GUIJob> compiledJobs = new LinkedList<>();

		while (job != null && (job.property != Property.WAIT)) {
			compiledJobs.add(job);
			job = queue.poll();
		}

		// execute all jobs
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for (GUIJob job : compiledJobs)  {
					if (job.property == Property.RESTORE_OLDEST) {
						GUIJob oldest = restoreJobs.pollFirst();
						setItem(oldest.property, oldest.data);

					} else if (job.property == Property.RESTORE_RECENT) {
						GUIJob recent = restoreJobs.pollLast();
						setItem(recent.property, recent.data);

					} else {
						setItem(job.property, job.data);
					}
				}
			}
		});

		// no more jobs
		if (job == null) return;
		
		if (job.property == Property.WAIT) {
			Timer t = new Timer((int) (((float) job.data) * 1000), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					execute();
				}
			});
			t.setRepeats(false);;
			t.start();
		}
	}
	
	
	private <T> void queueSetProperty(Property property, T data) {
		Object originalData = retrieveOriginal(property);
		queue.add(new GUIJob(property, data));
		restoreJobs.add(new GUIJob(property, originalData));
	}


	/**
	 * Helper method that sets a property of a view
	 * @param view
	 * @param section
	 * @param item
	 */
	private void setItem(Property section, Object item) {
		switch(section) {
		case BACKGROUND_COLOR:
			view.setBackground((Color) item);
			break;
		case LABEL:
			if (view instanceof JLabel) {
				((JLabel) view).setText((String) item);
			} else if (view instanceof JTextField) {
				((JTextField) view).setText((String) item);
				
			}
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private Object retrieveOriginal(Property section) {
		switch(section) {
		case BACKGROUND_COLOR:
			return view.getBackground();
		case LABEL:
			if (view instanceof JLabel) {
				return ((JLabel) view).getText();
			} else if (view instanceof JTextField) {
				return ((JTextField) view).getText();
			}
		default:
			return null;
		}
	}
	
	private static class GUIJob {

		public final Property property;
		public final Object data;

		public GUIJob(Property property, Object data) {
			this.property = property;
			this.data = data;
		}

	}

}
