/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    //TODO who did this
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXDatePicker;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.AddIterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.IterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleIterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.ValidationIssue;

/**
 *
 * The view for creating an iteration
 * @author TODO
 *
 */
@SuppressWarnings("serial")
public class IterationPanel extends JPanel{
	
	/** the model to hold the iteration */
	Iteration model;
	
	/** the tab that created this panel */
	IterationTab parent;
	/** the layout for this panel */
	private GridBagLayout panelLayout;
	/** labels to describe the text fields */
	JLabel lbl1, lbl2, lbl3;
	/** text fields for the iteration's data to be entered into */
	JTextField iterationNumber;
	/** text field that displays if the iteration was saved or not */
	JTextField result;
	/** button to submit the iteration */
	JButton submit;
	
	JXDatePicker startDatePicker;
	JXDatePicker endDatePicker;

	/** An enum indicating if the form is in create mode or edit mode */
	protected Mode editMode;

	/** A flag indicating if input is enabled on the form */
	protected boolean inputEnabled;

	/**
	 * Constructor
	 * @param iterationTab the tab that created this panel
	 */
	public IterationPanel(IterationTab iterationTab){
		this.parent = iterationTab;
		model = parent.iteration;
		
		editMode = Mode.CREATE;
		
		if(editMode == Mode.CREATE){
			DB.getAllIterations(new UpdateIterationIdCallback());
		}
		
		// Indicate that input is enabled
		inputEnabled = true;
		
		// Add all components to this panel
		addComponents();
//		parent.buttonGroup.update(mode, model);

		
		// Populate the form with the contents of the Iteration model and update the TextUpdateListeners.
		updateFields();
	}

	
	/**
	 * add all the components (labels, text fields, buttons) to this panel's view
	 *
	 */
	private void addComponents(){
		panelLayout =new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(panelLayout);
		
		lbl1 = new JLabel("Start Date (mm/dd/yyyy)");
		lbl2 = new JLabel("End Date (mm/dd/yyyy)");
		lbl3 = new JLabel ("Iteration Number");
		

//		startDate = new JTextField();
//		endDate = new JTextField();
		Date startDate = model.getStartDate();
		Date endDate = model.getEndDate();
		startDatePicker = new JXDatePicker(startDate != null ? startDate : new Date());
		endDatePicker = new JXDatePicker(endDate != null ? endDate : new Date());

		result = new JTextField();
		iterationNumber = new JTextField();
		
		if(editMode == Mode.CREATE) {
			submit = new JButton("Submit");
			submit.addActionListener(new AddIterationController(this));
		} else {
			submit = new JButton("Update");
			submit.addActionListener(new EditIterationAction());
		}
		
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		add(lbl1, c);
		c.gridx = 1;
		add(startDatePicker, c);
		c.gridy = 1;
		c.gridx = 0;
		add(lbl2, c);
		c.gridx = 1;
		add(endDatePicker, c);
		c.gridy = 2;
		c.gridx = 0;
		add(lbl3, c);
		c.gridx = 1;
		add(iterationNumber, c);
		c.gridy = 3;
		add(submit, c);
		c.gridy = 4;
		add(result, c);
		
		result.setEditable(false);
	}
	
	/**
	 * close this tab/panel
	 *
	 */
	public void close() {
		parent.tabController.closeCurrentTab();
	}
	
	/**
	 * Sets whether input is enabled for this panel and its children. This should be used instead of 
	 * JComponent#setEnabled because setEnabled does not affect its children.
	 * 
	 * @param enabled	Whether or not input is enabled.
	 */
	protected void setInputEnabled(boolean enabled) {
		inputEnabled = enabled;

		// TODO: implement
	}
	
	/**
	 * Check if dates overlap
	 * TODO: IS THIS WHAT WE REALLY WANT THIS TO DO? DO WE WE EVEN USE THIS?
	 *
	 * @return if dates overlap
	 */
	public boolean doDatesOverlap() {
		return false;
	}
	
	/**
	 * Updates the IterationPanel's model to contain the values of the given Iteration and sets the 
	 * IterationPanel's editMode to {@link Mode#EDIT}.
	 * 
	 * @param iteration	The Iteration which contains the new values for the model.
	 */
	public void refreshModel() {
		updateModel(model, Mode.EDIT);
	}
	
	/**
	 * Updates the IterationPanel's model to contain the values of the given Iteration and sets the 
	 * IterationPanel's editMode to {@link Mode#EDIT}.
	 * 
	 * @param iteration	The Iteration which contains the new values for the model.
	 */
	public void updateModel(Iteration iteration) {
		updateModel(iteration, Mode.EDIT);
	}

	/**
	 * Updates the IterationPanel's model to contain the values of the given Iteration.
	 * 
	 * @param	iteration	The Iteration which contains the new values for the model.
	 * @param	mode	The new editMode.
	 */
	protected void updateModel(Iteration iteration, Mode mode) {
		editMode = mode;
		model = iteration;
		updateFields();	
	}
	
	/**
	 * Updates the IterationPanel's fields to match those in the current model
	 *
	 */
	private void updateFields() {
		//TODO finish this
	}

	/**
	 * Returns a boolean representing whether or not input is enabled for the IterationPanel and its children.
	 * 
	 * @return	A boolean representing whether or not input is enabled for the IterationPanel and its children.
	 */
	public boolean getInputEnabled() {
		return inputEnabled;
	}

	/**
	 * Gets the IterationPanel's internal model.
	 * @return the iteration model
	 */
	public Iteration getModel() {
		model.setIterationNumber(iterationNumber.getText());

		//TODO handle the exceptions better
		model.setStartDate(startDatePicker.getDate());
		model.setEndDate(endDatePicker.getDate());
		
		return model;
	}
	
	/**
	 * Returns the parent IterationTab.
	 * 
	 * @return the parent IterationTab.
	 */
	public IterationTab getParent() {
		return parent;
	}

	/**
	 * Returns the edit {@link Mode} for this IterationPanel.
	 * 
	 * @return	The edit {@link Mode} for this IterationPanel.
	 */
	public Mode getEditMode() {
		return editMode;
	}

	public void setStatus(String string) {
		result.setText(string);
	}
	
	class EditIterationAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(!validateFields()){
				setStatus("Iteration is invalid");
			}
			DB.updateIteration(model, new SingleIterationCallback() {
				@Override
				public void callback(Iteration iteration) {
					setStatus("Iteration Updated");
				}
			});
		}
	}
	
	
	/**
	 *
	 * callback class to update the iteration's id
	 * @author TODO
	 *
	 */
	class UpdateIterationIdCallback implements IterationCallback {
		@Override
		public void callback(List<Iteration> iterationList) {
			model.setId(iterationList.size()+1);
		}
	}

	/**
	 *
	 * Callback class to make sure that the iterations dates don't overlap with other dates
	 * @author TODO
	 *
	 */
	class CheckDateOverlapCallback implements IterationCallback {
//		List<Iteration> iters;
//		
//		public CheckDateOverlapCallback (List<Iteration> iters){
//			this.iters = iters;
//		}
		
		@Override
		public void callback(List<Iteration> iterationList) {
			List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
			for (Iteration i : iterationList) {
				if(i.getId() != model.getId()) {
					if(model.getStartDate().after(i.getStartDate()) && model.getStartDate().before(i.getEndDate())) {
						issues.add(new ValidationIssue("startDate overlaps with Iteration "+i.getIterationNumber(), "startDate"));
						setStatus("Start date overlaps with another iteration.");
						startDatePicker.getEditor().setBackground(Color.RED);
					}
					if(model.getEndDate().after(i.getStartDate()) && model.getEndDate().before(i.getEndDate())) {
						issues.add(new ValidationIssue("endDate overlaps with Iteration "+i.getIterationNumber(), "endDate"));
						setStatus("End date overlaps with another iteration.");
						endDatePicker.getEditor().setBackground(Color.RED);
					}
//					if(i.getStartDate().after(model.getStartDate()) && model.getStartDate().before(i.getEndDate()) ||
//							i.getEndDate().after(model.getStartDate()) && model.getEndDate().before(i.getEndDate())) {
//						issues.add(new ValidationIssue("iteration overlaps with Iteration "+i.getIterationNumber()));
//						setStatus("Both dates overlap with another iteration.");
//						startDatePicker.getEditor().setBackground(Color.RED);
//						endDatePicker.getEditor().setBackground(Color.RED);
//					}
				}
			}
			//TODO figure out how to display the issues...

		}
	}

	public boolean validateFields() {
		if(model.getStartDate().after(model.getEndDate())){
			setStatus("Dates are backwards.");
			startDatePicker.getEditor().setBackground(Color.RED);
			endDatePicker.getEditor().setBackground(Color.RED);
			return false;
		}
		if(model.getIterationNumber().length() < 1){
			setStatus("Need a valid name");
			iterationNumber.setBackground(Color.RED);
			return false;
		}
		DB.getAllIterations(new CheckDateOverlapCallback());
		if (!result.getText().equals("Iteration saved"))
			return false;
		else
			return true;
	}
	
}
