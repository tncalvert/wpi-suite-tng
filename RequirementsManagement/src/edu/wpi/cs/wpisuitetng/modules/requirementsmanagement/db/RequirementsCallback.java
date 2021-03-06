/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alex Henning
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db;

import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 * An interface for callbacks that return all of the requirements.
 * 
 * @author alex
 */
public interface RequirementsCallback {

	/**
	 * Implements an action to be performed on the requirement being passed back.
	 * 
	 * @param reqs
	 *            The requirements being returned
	 */
	void callback(List<RequirementModel> reqs);
}
