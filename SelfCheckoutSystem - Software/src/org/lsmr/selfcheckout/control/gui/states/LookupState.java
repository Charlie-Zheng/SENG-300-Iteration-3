package org.lsmr.selfcheckout.control.gui.states;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.lsmr.selfcheckout.control.gui.StateHandler;
import org.lsmr.selfcheckout.control.gui.statedata.StateData;

public class LookupState implements GUIState {

	@Override
	public void init(StateHandler<GUIState> stateController, ReducedState reducedState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDataUpdate(StateData<?> data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JPanel getPanel() {

// ***************************** Look Up Item ********************************//		
		
		
		JTextArea codeInput = new JTextArea();
		JPanel inputPanel = new JPanel();
	/*	  editTextArea.setForeground(Color.WHITE);

	        //SET CONTENT PANE
	        Container c = getContentPane();

	        //ADD COMPONENTS TO CONTENT PANE        
	        c.add(uneditTextArea, BorderLayout.CENTER);
	        c.add(editTextArea, BorderLayout.SOUTH);
	        c.add(inputButton, BorderLayout.WEST);
		*/
		JPanel lookUpPanel = new JPanel();
		lookUpPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 100, 100)); // for margins
		//keyPadPanel.setLayout(new BorderLayout());
		lookUpPanel.setLayout(new GridLayout(3,10,10,10));
		lookUpPanel.add(new JButton("Q"));
		lookUpPanel.add(new JButton("W"));
		lookUpPanel.add(new JButton("E"));
		lookUpPanel.add(new JButton("R"));
		lookUpPanel.add(new JButton("T"));
		lookUpPanel.add(new JButton("Y"));
		lookUpPanel.add(new JButton("U"));
		lookUpPanel.add(new JButton("I"));
		lookUpPanel.add(new JButton("O"));
		lookUpPanel.add(new JButton("P"));
		lookUpPanel.add(new JButton("A"));
		lookUpPanel.add(new JButton("S"));
		lookUpPanel.add(new JButton("D"));
		lookUpPanel.add(new JButton("F"));
		lookUpPanel.add(new JButton("G"));
		lookUpPanel.add(new JButton("H"));
		lookUpPanel.add(new JButton("J"));
		lookUpPanel.add(new JButton("K"));
		lookUpPanel.add(new JButton("L"));
		lookUpPanel.add(new JButton("OK"));
		lookUpPanel.add(new JButton("Z"));
		lookUpPanel.add(new JButton("X"));
		lookUpPanel.add(new JButton("C"));
		lookUpPanel.add(new JButton("V"));
		lookUpPanel.add(new JButton("B"));
		lookUpPanel.add(new JButton("N"));
		lookUpPanel.add(new JButton("M"));
		lookUpPanel.add(new JButton("Back"));
		lookUpPanel.setLayout(new GridLayout(0, 3, 50, 50));
		
		
		
		inputPanel.add(codeInput);
		codeInput.setBackground(Color.WHITE);
		codeInput.setForeground(Color.WHITE);
		inputPanel.setBackground(Color.BLUE);
		inputPanel.setBorder(BorderFactory.createEmptyBorder(100, 10, 10, 10));
		
		return inputPanel;
	}

	@Override
	public ReducedState reduce() {
		// TODO Auto-generated method stub
		return null;
	}

}
