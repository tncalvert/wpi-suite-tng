package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.IterationPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveIterationRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

@SuppressWarnings("serial")
public class CreateIterationController extends AbstractAction implements ActionListener {
	
	private final IterationPanel panel;
	
	public CreateIterationController(IterationPanel panel) {
		this.panel = panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/iteration",  HttpMethod.GET);
		// no need for a body
		request.addObserver(new RetrieveIterationRequestObserver(this));
		request.send();

	}
	
	public void receivedGetIterationConfirmation(List<Iteration> reqs) {
		System.out.println("receivedGetIterationConfirmation: " +reqs.toString());
		panel.close();
	}


}
