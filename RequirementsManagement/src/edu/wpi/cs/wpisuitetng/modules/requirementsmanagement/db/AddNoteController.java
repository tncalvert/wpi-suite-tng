/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Chris Casola
 *    Andrew Hurle
 *    Tim Calvert
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.AbstractAction;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.viewrequirement.NoteMainPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.viewrequirement.RequirementsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementNote;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.AddNoteObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 *
 * Handles saving requirement notes to the server
 * @author Tim
 * @author andrew hurle
 * @author Chris Casola
 */
@SuppressWarnings("serial")
public class AddNoteController extends AbstractAction implements ActionListener{
	
	/** The note panel */
	private final NoteMainPanel view;
	/** The requirement containing the notes */
	private final RequirementModel model;
	/** The requirement panel that has the note panel in it */
	private final RequirementsPanel parentView;
	
	/**
	 * Default constructor for Notes
	 *
	 * @param view The NewNotePanel containing the comment field
	 * @param model The Requirement model being commented on
	 * @param parentView The RequirementPanel displaying the requirment
	 */
	public AddNoteController(final NoteMainPanel view, final RequirementModel model, final RequirementsPanel parentView) {
		this.view = view;
		this.model = model;
		this.parentView = parentView;
	}
	
	/**
	 * Called by pressing add note button
	 *
	 * @param e The action performed when pressing add note
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		saveNote();
	}

	
	/**
	 * Save the new note to the server
	 *
	 */
	public void saveNote() {
		final String noteText = view.ta.getText();
		if (noteText.length() > 0) {
			final Request request = Network.getInstance().makeRequest(
					"requirementsmanagement/requirementnote", HttpMethod.PUT);
			request.setBody((new RequirementNote(model.getId(),
					CurrentUserPermissionManager.getInstance().getCurrentUser(),
					noteText)).toJSON());
			request.addObserver(new AddNoteObserver(this));
			request.send();
		} 
	}
	
	
	/**
	 * Add the comment to the view if the server responded with a success message
	 *
	 * @param response The response from the server
	 */
	public void receivedAddConfirmation(ResponseModel response) {
		Gson gson = new Gson();
		RequirementNote note = gson.fromJson(response.getBody(), RequirementNote.class);
		// Update the text area and the button after successfully adding a note
		view.ta.setText("");
		view.setInputEnabled(true);
		
		DB.getSingleRequirement(Integer.toString(note.getRequirementId()), new SingleRequirementCallback() {
			@Override
			public void callback(RequirementModel req) {
				// Update only the model and notes but not other (possibly unsaved) fields
				parentView.setModel(req);
				parentView.updateNotes();
			}
		});
	}
	
	
	/**
	 * Alert the user that an error occurred sending the note to the server
	 *
	 * @param response The response from the server
	 */
	public void receivedAddFailure(ResponseModel response) {
		System.err.println("Received note failure");
	}
}
