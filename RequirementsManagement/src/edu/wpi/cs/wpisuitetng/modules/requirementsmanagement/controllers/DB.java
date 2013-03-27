package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveSingleRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class DB {
	
	public static void getSingleRequirement(String id, SingleRequirementCallback callback) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel/" + id, HttpMethod.GET);
		request.addObserver(new RetrieveSingleRequirementRequestObserver(callback));
		request.send();
	}
}
