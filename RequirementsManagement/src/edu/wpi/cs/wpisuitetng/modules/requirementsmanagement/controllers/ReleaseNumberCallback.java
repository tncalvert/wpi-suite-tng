/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;

/**
 *
 * Interface that release number callbacks can inherit from
 * @author Tim
 *
 */
public interface ReleaseNumberCallback {

	/**
	 * Implements an action to be called on the release number being passed back
	 *
	 * @param releaseNumbers Release numbers returned from the database
	 */
	public void callback(List<ReleaseNumber> releaseNumbers);
}
