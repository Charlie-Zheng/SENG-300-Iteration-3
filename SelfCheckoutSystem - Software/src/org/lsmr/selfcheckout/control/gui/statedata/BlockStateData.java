package org.lsmr.selfcheckout.control.gui.statedata;

public class BlockStateData implements StateData<Boolean> {

	private boolean b ;

	public BlockStateData(boolean b) {
		this.b = b;
	}

	@Override
	public Boolean obtain() {
		return b;
	}
}
