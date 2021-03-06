/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tim Calvert
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.entitymanagers;

import java.util.List;
import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEvent;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.ProjectEventValidator;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.ValidationIssue;


/**
 * Entity manager for project events (history logs)
 *
 * @author Tim
 */
public class ProjectEventEntityManager implements EntityManager<ProjectEvent> {
	/** the database */
	private final Data db;
	/** validator for project events */
	private final ProjectEventValidator validator;
	
	/**
	 * Default constructor
	 * @param data database
	 */
	public ProjectEventEntityManager(Data data) {
		db = data;
		validator = new ProjectEventValidator(db);
	}

	/**
	 * Makes a ProjectEvent to add to the database
	 *
	 * @param s
	 * @param content
	 * @return
	 * @throws BadRequestException
	 * @throws ConflictException
	 * @throws WPISuiteException
	 */
	@Override
	public ProjectEvent makeEntity(Session s, String content) throws BadRequestException,
			ConflictException, WPISuiteException {
		ProjectEvent newProjectEvent = ProjectEvent.fromJSON(content);
		
		newProjectEvent.setId(Count() + 1);
		
		List<ValidationIssue> issues = validator.validate(s, newProjectEvent, Mode.CREATE);
		if(issues.size() > 0) {
			for(ValidationIssue issue : issues) {
				System.out.println("Validation issue: " + issue.getMessage());
			}
			throw new BadRequestException();
		}
		
		if(!db.save(newProjectEvent, s.getProject())) {
			throw new WPISuiteException();
		}
		
		return newProjectEvent;
	}

	/**
	 * Retrieves an ProjectEvent by id
	 *
	 * @param s
	 * @param id
	 * @return
	 * @throws NotFoundException
	 * @throws WPISuiteException
	 */
	@Override
	public ProjectEvent[] getEntity(Session s, String id) throws NotFoundException,
			WPISuiteException {

		final int intId = Integer.parseInt(id);
		if(intId < 1) {
			throw new NotFoundException();
		}

		ProjectEvent[] projectEvents = null;
		try {
			projectEvents = db.retrieve(ProjectEvent.class, "id", intId, s.getProject()).toArray(new ProjectEvent[0]);
		} catch (WPISuiteException e) {
			e.printStackTrace();
		}
		
		if(projectEvents.length < 1 || projectEvents[0] == null) {
			throw new NotFoundException();
		}
		return projectEvents;
	}

	/**
	 * Retrieves all ProjectEvents in the database
	 *
	 * @param s
	 * @return
	 * @throws WPISuiteException
	 */
	@Override
	public ProjectEvent[] getAll(Session s) throws WPISuiteException {
		return db.retrieveAll(new ProjectEvent(), s.getProject()).toArray(new ProjectEvent[0]);
	}

	/**
	 * Updates a ProjectEvent in the database
	 *
	 * @param s
	 * @param content
	 * @return
	 * @throws WPISuiteException
	 */
	@Override
	public ProjectEvent update(Session s, String content) throws WPISuiteException {
		
		throw new WPISuiteException("updating ProjectEvents is not allowed");
		
	}

	/**
	 * Saves a ProjectEvent
	 *
	 * @param s
	 * @param model
	 * @throws WPISuiteException
	 */
	@Override
	public void save(Session s, ProjectEvent model) throws WPISuiteException {
		db.save(model, s.getProject());
	}

	//TODO should probably remove the delete functions....
	
	/**
	 * Deletes a ProjectEvent by id
	 *
	 * @param s
	 * @param id
	 * @return
	 * @throws WPISuiteException
	 */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		return (db.delete(getEntity(s, id)[0]) != null);
	}
	
	/**
	 * Deletes all ProjectEvents in the db
	 *
	 * @param s
	 * @throws WPISuiteException
	 */
	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		db.deleteAll(new ProjectEvent(), s.getProject());
	}
	
	/**
	 * Returns the number of ProjectEvents in the db
	 *
	 * @return
	 * @throws WPISuiteException
	 */
	@Override
	public int Count() throws WPISuiteException {
		return db.retrieveAll(new ProjectEvent()).size();
	}

	/**
	 * This is not implemented
	 *
	 * @param s
	 * @param args
	 * @return
	 * @throws NotImplementedException
	 */
	@Override
	public String advancedGet(Session s, String[] args)
			throws NotImplementedException {
		throw new NotImplementedException();

	}

	/**
	 * This is not implemented
	 *
	 * @param s
	 * @param args
	 * @param content
	 * @return
	 * @throws NotImplementedException
	 */
	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws NotImplementedException {
		throw new NotImplementedException();
	}

	/**
	 * This is not implemented
	 *
	 * @param s
	 * @param string
	 * @param content
	 * @return
	 * @throws NotImplementedException
	 */
	@Override
	public String advancedPost(Session s, String string, String content)
			throws NotImplementedException {
		throw new NotImplementedException();
	}
}
