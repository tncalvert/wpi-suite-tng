/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andrew Hurle
 *    Chris Casola
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.entitymanagers;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.entitymanagers.ModelMapper.MapCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.FieldChange;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEvent;

/**
 * Responsible for filling in a changeset after being passed to
 * {@link ModelMapper#map(Model, Model, MapCallback)} 
 * @author Chris Casola
 * @author Andrew Hurle
 */
class PermissionEventCallback implements MapCallback {
	
	/** The changeset to be filled in */
	private final ProjectEvent changeset;
	private boolean wasCalled = false;
	
	//don't add these fields as changes
	private static final Set<String> dontRecord =
			new HashSet<String>();
	
	/**
	 * Create a callback that will fill in the given changeset.
	 * @param changeset The changeset to add changes to
	 */
	PermissionEventCallback(ProjectEvent changeset) {
		this.changeset = changeset;
	}
	
	/**
	 *TODO: DOCUMENT THIS
	 *
	 * @param source
	 * @param destination
	 * @param fieldName
	 * @param sourceValue
	 * @param destinationValue
	 * @return
	 */
	@Override
	public Object call(Model source, Model destination, String fieldName,
			Object sourceValue, Object destinationValue) {
		if(!wasCalled) {
			changeset.setDate(new Date());
			wasCalled = true;
		}
		if(!dontRecord.contains(fieldName)) {
			if(!objectsEqual(sourceValue, destinationValue)) {
				/*
				 * this field has changed - indicate the change in the changeset
				 * remember that fields from updated model are being copied to old one
				 * destinationValue is the old value
				 */
				changeset.getChanges().put(fieldName, new FieldChange<Object>(destinationValue, sourceValue));
			}
		}
		return sourceValue;
	}
	
	/**
	 * checks to see if two objects are equal
	 *
	 * @param a one object
	 * @param b the other object
	 * @return if a and b are equal
	 */
	private boolean objectsEqual(Object a, Object b) {
		// Java 7 has Objects.equals... we're on Java 6
		if(a == b) {
			return true;
		}
		if(a == null || b == null) {
			return false;
		}
		return a.equals(b);
	}
	
}