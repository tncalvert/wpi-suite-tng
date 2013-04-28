/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite

 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    David Modica
 *    Tim Calvert
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.SingleRequirementCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * This observer is called when a response is received from a request to the server
 * to edit a requirement.
 * 
 * @author David
 * @author Tim Calvert
 *
 * @version $Revision: 1.0 $
 */
public class EditRequirementModelRequestObserver implements RequestObserver {

	/** Callback paired with this */
	private SingleRequirementCallback callback;
	
	/**
	 * Default constructor
	 * @param callback Controller paired with this observer
	 */
	public EditRequirementModelRequestObserver(SingleRequirementCallback callback){
		this.callback = callback;
	}
	
	/**
	 * Server reported a successful request
	 *
	 * @param iReq Request returned from server
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// Get the response to the given request
		final ResponseModel response = iReq.getResponse();
		
		// Parse the message out of the response body
		final RequirementModel requirement = RequirementModel.fromJSON(response.getBody());
		
		// Pass the messages back to the controller
		callback.callback(requirement);
	}

	/**
	 * Server reported an error in the request
	 *
	 * @param iReq Request returned from the server
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to edit a requirement failed.");
	}

	/**
	 * Server reported an failure in the request
	 *
	 * @param iReq Request returned from the server
	 * @param exception Exception thrown on the server
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(IRequest, Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to edit a requirement failed.");
	}

}
