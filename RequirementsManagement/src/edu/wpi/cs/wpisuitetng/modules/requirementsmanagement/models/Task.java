package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;


public class Task extends AbstractModel {
	
	//TODO Add fields to Tasks
	//Assigned to, Description, etc...

	/**
	 * Add dependencies necessary for Gson to interact with this class
	 * @param builder Builder to modify
	 */
	public static void addGsonDependencies(GsonBuilder builder) {
		//TODO if needed, this is where (de)serializers are added.
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}
	/**
	 * @return String representation of this task
	 */
	@Override
	public String toJSON() {
		String json;
		Gson gson = new Gson();
		json = gson.toJson(this, Task.class);
		return json;
	}

	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

}
