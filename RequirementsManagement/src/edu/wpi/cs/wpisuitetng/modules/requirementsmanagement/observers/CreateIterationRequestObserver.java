/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite

 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   David Modica
 *    Tim Calvert
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.AddIterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.AddRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;
/**
 * This observer is called when a response is received from a request 
 * to the server to create an iteration.
 * 
 * @author David
 * @author Jacob Palnick
 *
 */
public class CreateIterationRequestObserver implements RequestObserver {

	private AddIterationController controller;
	
	public CreateIterationRequestObserver(AddIterationController controller){
		this.controller = controller;
	}

	@Override
	public void responseSuccess(IRequest iReq) {
		// Get the response to the given request
		final ResponseModel response = iReq.getResponse();
		
		// Parse the message out of the response body
		final Iteration iteration = Iteration.fromJSON(response.getBody());
		
		// Pass the messages back to the controller
		controller.receivedAddConfirmation(iteration);
	}

	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to add a message had an error.");
		System.err.println("\tResponse: "+iReq.getResponse().getStatusCode()+" --- "
							+iReq.getResponse().getBody());
	}

	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to add a message has failed.");
	}
	
}