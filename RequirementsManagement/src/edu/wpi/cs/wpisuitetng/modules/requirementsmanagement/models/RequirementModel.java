package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;


import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
//import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class RequirementModel extends AbstractModel {

	private int id;
	private int releaseNumber;
	private RequirementStatus status;
	private RequirementPriority priority;
	private String name;
	private String description;
	private String estimate;
	private String actualEffort;
	private User creator;
	private User assignee;
	private Date creationDate, lastModifiedDate;
	private List<RequirementEvent> events;
	
	//TODO associate with other requirements
	//TODO add attachments
	//TODO add tasks
	//TODO add notes
	//TODO add acceptance tests
	
	/**
	 * Constructs a new RequirementModel with default properties.
	 */
	public RequirementModel() {
		id = -1;
		name = "";
		description = "";
		status = RequirementStatus.NEW;
		creator = new User("", "", "", -1);
		assignee = new User("", "", "", -1);
		estimate = "";
		actualEffort = "";
		creationDate = new Date();
		lastModifiedDate = new Date();
	}
	
	//TODO finish the documentation of this constructor
	/**
	 * Constructs a new RequirementModel with the given properties
	 * Other properties are the same as the default constructor
	 * 
	 * @param id the unique id of the RequirementModel
	 * @param releaseNumber the release number for the requirement
	 * @param status the status of the RequirementModel
	 * @param priority the priority of the requirement
	 */
	public RequirementModel(int id, int releaseNumber, RequirementStatus status,
			RequirementPriority priority, String name, String description,
			String estimate, String actualEffort, User creator, User assignee,
			Date creationDate, Date lastModifiedDate) {
		super();
		this.id = id;
		this.releaseNumber = releaseNumber;
		this.status = status;
		this.priority = priority;
		this.name = name;
		this.description = description;
		this.estimate = estimate;
		this.actualEffort = actualEffort;
		this.creator = creator;
		this.assignee = assignee;
		this.creationDate = creationDate;
		this.lastModifiedDate = lastModifiedDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	public User getCreator() {
		 return creator;
	}
	
	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}
	
//	public void setAssigneeWithAutoStatusUpdate(User assignee) {
//		this.assignee = assignee;
//		
//		//TODO consider improving this
//		//this will auto-set the status when assigning a user
//		if (assignee != null && assignee.getIdNum() != -1 ) { //make sure it is assigned to a real user
//			this.status = RequirementStatus.IN_PROGRESS;
//		} else { 
//			status = RequirementStatus.OPEN;//if not a real user then it is currently open.
//		}
//	}
	
	public User getAssignee() {
		return assignee;
	}

	public int getReleaseNumber() {
		return releaseNumber;
	}

	public void setReleaseNumber(int releaseNumber) {
		this.releaseNumber = releaseNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RequirementStatus getStatus() {
		return status;
	}

	public void setStatus(RequirementStatus status) {
		this.status = status;
	}

	public RequirementPriority getPriority() {
		return priority;
	}

	public void setPriority(RequirementPriority priority) {
		this.priority = priority;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEstimate() {
		return estimate;
	}

	public void setEstimate(String estimate) {
		this.estimate = estimate;
	}

	public String getActualEffort() {
		return actualEffort;
	}

	public void setActualEffort(String actualEffort) {
		this.actualEffort = actualEffort;
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
	 * Converts this Requirement to a JSON string
	 * @return a string in JSON representing this Defect
	 */
	@Override
	public String toJSON() {
		String json;

		//the following method works but if wee need special serializers then the second method is needed
		Gson gson = new Gson();

		//this is used is special serializers are needed
//		GsonBuilder builder = new GsonBuilder();
//		addGsonDependencies(builder);
//		Gson gson = builder.create();
		
		json = gson.toJson(this, RequirementModel.class);
		return json;
	}

	//TODO finish this code...
	/**
	 * @param json Json string to parse containing Requirement
	 * @return The Requirement given by json
	 */
	public static RequirementModel fromJSON(String json) {
		GsonBuilder builder = new GsonBuilder();
		addGsonDependencies(builder);
		return builder.create().fromJson(json, RequirementModel.class);
	}
	
	/**
	 * @param json Json string to parse containing Requirement array
	 * @return The Requirement array given by json
	 */
	public static RequirementModel[] fromJSONArray(String json) {
		GsonBuilder builder = new GsonBuilder();
		addGsonDependencies(builder);
		return builder.create().fromJson(json, RequirementModel[].class);
	}
	
	/**
	 * Add dependencies necessary for Gson to interact with this class
	 * @param builder Builder to modify
	 */
	public static void addGsonDependencies(GsonBuilder builder) {
		//TODO add dependencies for future class associations. Only needed if normal (de)serializer does not work
//		Task.addGsonDependencies(builder);
//		Note.addGsonDependencies(builder);
//		AbstractAttachment.addGsonDependencies(builder);
	}
	
	// interface documentation says this is necessary for the mock database
	// not sure if this is still needed otherwise
	@Override
	public Boolean identify(Object o) {
		Boolean returnValue = false;
		if(o instanceof RequirementModel && id == ((RequirementModel) o).getId()) {
			returnValue = true;
		}
		if(o instanceof String && Integer.toString(id).equals(o)) {
			returnValue = true;
		}
		return returnValue;
	}

	@Override
	public String toString() {
		return toJSON();
	}

}
