package org.lsmr.selfcheckout.control.gui.states;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class RedState implements GUIState {
	
	int counter = 0;
	private StateHandler <GUIState> stateController;

	@Override
	public void init(StateHandler<GUIState> stateController, ReducedState reducedState) {
		this.stateController = stateController;
		
	}

	@Override
	public void onDataUpdate(StateData<?> data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JPanel getPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBackground(Color.RED);
		JButton b = new JButton();
		
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				counter++;
				System.out.println(counter);
			}
		});
		
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stateController.setState(new BuyingState());
			}
		});
		
		p.add(b);
		p.add(back);
		
		return p;
	}

	@Override
	public ReducedState reduce() {
		return new BaseReducedState(String.valueOf(counter));
	}
}
