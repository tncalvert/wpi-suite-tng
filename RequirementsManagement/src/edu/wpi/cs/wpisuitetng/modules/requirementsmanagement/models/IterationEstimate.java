/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    James Megin
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.RequirementsCallback;

/**
 *
 * Description goes here
 * @author James
 *
 */
public class IterationEstimate implements RequirementsCallback {
	
	/**
	 * The Iteration that an estimate is needed for
	 */
	private Iteration iteration;
	
	/**
	 * A field to hold the Iterations estimate
	 */
	private int estimate;
	
	/**
	 * Constructor
	 * @param iteration the iteration
	 */
	public IterationEstimate(Iteration iteration) {
		this.iteration = iteration;
		estimate = 0;
	}

	/**
	 * Get the total estimate for all of the requirements in an iteration
	 * TODO: when sub-children are implemented for requirements make sure that this still works
	 *
	 * @param reqs list of requirements
	 */
	@Override
	public void callback(List<RequirementModel> reqs) {
		estimate = 0;
		if (iteration != null) {
			for(RequirementModel req : reqs) {
				// If the iteration is the same as the requirment's iteration
				if (req.getIteration() != null &&
						req.getIteration().getIterationNumber().equals(iteration.getIterationNumber())) {
					// Add the requirment's estimate to the iteration's estimate
					this.estimate += req.getEstimate();
				}
			}
		}
	}
	
	/**
	 * Returns the estimate for the iteration
	 *
	 * @return The estimate for the iteration
	 */
	public int getEstimate() {
		return this.estimate;
	}

}
