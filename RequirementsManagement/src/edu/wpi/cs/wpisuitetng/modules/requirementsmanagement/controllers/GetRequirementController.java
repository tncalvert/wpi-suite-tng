/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.JanewayModule;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller responds to a click even to retrieve Requirements from the db
 * @author Tim Calvert
 *
 */
public class GetRequirementController implements ActionListener {
	// TODO This current gets all requirements (despite the name). We'll need a new one for all, and convert this one to a single requirement.

	private final JanewayModule mainBoard;
	//private final JPanel buttonPanel;

	public GetRequirementController(JPanel buttonPanel, JanewayModule mB) {
		//this.buttonPanel = buttonPanel;  /* not needed at the moment */
		this.mainBoard = mB;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
				
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel",  HttpMethod.GET);
		// no need for a body
		request.addObserver(new RetrieveRequirementModelRequestObserver(this));
		request.send();
	}
	
	public List<RequirementModel> receivedGetConfirmation(List<RequirementModel> reqs) {
		return mainBoard.getRequirements();
	}

}
