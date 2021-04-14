package org.lsmr.selfcheckout.control.gui.statedata;

import org.lsmr.selfcheckout.control.gui.Pair;


public class AttendantStateData implements StateData<Pair<Integer, Integer>> {

	public static final int REMOVE = 0;
	public static final int APPROVE_WEIGHT = 1;

	private int request;
	private int index;

	public AttendantStateData(int request) {
		this(request, 0);
	}

	public AttendantStateData(int request, int index) {
		this.request = request;
		this.index = index;
	}

	@Override
	public Pair<Integer, Integer> obtain() {
		return new Pair<>(request, index);
	}
}
