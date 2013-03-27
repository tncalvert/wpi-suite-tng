package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.EditRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveRequirementModelRequestObserver;
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
	
	public static void getAllRequirements(RequirementsCallback callback) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel",  HttpMethod.GET);
		request.addObserver(new RetrieveRequirementModelRequestObserver(callback));
		request.send();
	}
	
	public static void updateRequirements(RequirementModel req, SingleRequirementCallback callback) {
		System.out.println(req.toJSON());
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel/" + req.getId(),  HttpMethod.POST);
		request.setBody(req.toJSON());
		request.addObserver(new EditRequirementModelRequestObserver(callback));
		request.send();
	}
}