/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    //Josh
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
<<<<<<< HEAD
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.IterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.RequirementsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleRequirementCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ViewReqTable.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;

@SuppressWarnings("serial")
public class ListRequirementsPanel extends JPanel {
	
	ListRequirementsTab parent;
	boolean inputEnabled;
	JTable table;
	ViewReqTable tableModel;
	JButton editButton;
	JButton saveButton;
	JButton cancelButton;
	JPanel editPanel;
	
	public ListRequirementsPanel(final ListRequirementsTab parent) {
		this.parent = parent;
		
		// Indicate that input is enabled
		inputEnabled = true;

		// Add all components to this panel
		addComponents();
		updateAllRequirementList();
		
		table.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2 && tableModel.getMode() != Mode.EDIT) {
                	DB.getSingleRequirement((String) table.getModel().getValueAt(table.getSelectedRow(), 0),
                			new SingleRequirementCallback() {
						@Override
						public void callback(RequirementModel req) {
							parent.tabController.addEditRequirementTab(req);
						}
                	});
                }
			}
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseClicked(MouseEvent arg0) {}
		});
	}

	/**
	 * adds the components to the panel and places constraints on them
	 * for the SpringLayout manager.
	 * @param layout the layout manager
	 */
	protected void addComponents() {
		//borderlayout so the table can expand while the filter area remains constant
		setLayout(new BorderLayout());
		
		//create the table part of the GUI
		tableModel = new ViewReqTable();
		tableModel.setMode(Mode.VIEW);
		table = new JTable(tableModel);
		table.setPreferredScrollableViewportSize(new Dimension(500, 100));
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// create panel and button to change table to edit mode
		editPanel = new JPanel();
		editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setEditTable();
			}
		});
		
		// create the save and cancel buttons
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO THIS NEEDS TO MAKES SURE ALL CHANGES ARE VALID AND THEN SAVE THEM, THEN BRING THE TABLE BACK TO VIEW MODE
				// After everything else is done, call setViewTable()
				setViewTable(false);
			}
			
		});
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO THIS NEEDS TO REMOVE ALL CHANGES MADE AND BRING THE TABLE BACK TO VIEW MODE
				// After everything else is done, call setViewTable()
				setViewTable(true);
				
			}
			
		});
		
		editPanel.add(editButton);
		
		setUpIterationColumn(table, table.getColumnModel().getColumn(2));
		setUpStatusColumn(table, table.getColumnModel().getColumn(3));
		setUpPriorityColumn(table, table.getColumnModel().getColumn(4));
		
		//Add the table to a scrollpane and add it
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(200, 100));
		add(scrollPane, BorderLayout.CENTER);
		add(editPanel, BorderLayout.PAGE_END);
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

	public void updateAllRequirementList() {
		DB.getAllRequirements(new UpdateTableCallback());
	}
	
	public ViewReqTable getTable(){
		return this.tableModel;
	}
	
	/**
	 * Function to turn the table into edit mode
	 *
	 */
	public void setEditTable() {
		tableModel.setMode(Mode.EDIT);
		editPanel.remove(editButton);
		editPanel.add(saveButton);
		editPanel.add(cancelButton);
		editPanel.revalidate();
		editPanel.repaint();
		//TODO: SET THE TABLE INTO A MODE WHERE ALL THE FIELDS CAN BE EDITED
		setUpPriorityColumn(table, table.getColumnModel().getColumn(4));
		setUpStatusColumn(table, table.getColumnModel().getColumn(3));
		setUpIterationColumn(table, table.getColumnModel().getColumn(2));
	}
	
	/**
	 * Function to turn the table into view mode
	 *
	 */
	public void setViewTable(boolean cancelled) {
		boolean noErrors = true;
		if(cancelled) {
			updateAllRequirementList();
		} else {
			// validate fields
			noErrors = validateModels();
			if(noErrors) {  // no errors, update models and save
				// get all models from data base and continue from callback
				RetrieveAllRequirementsCallback cb = new RetrieveAllRequirementsCallback();
				DB.getAllRequirements(cb);
			}
			
		}
		if(noErrors) { // no errors (or cancelled), so go back to view
			tableModel.setMode(Mode.VIEW);
			editPanel.remove(saveButton);
			editPanel.remove(cancelButton);
			editPanel.add(editButton);
			editPanel.revalidate();
			editPanel.repaint();
		}
		//TODO: MAKE THE FIELDS NOT EDITABLE ANYMORE
	}
	
	/**
	 * make the iteration comboboxes in the iteration column
	 *
	 * @param table the JTable that has the column
	 * @param iterColumn the iteration column
	 */
	public void setUpIterationColumn(JTable table, TableColumn iterColumn) {
		JComboBox iterationBox = new JComboBox();
		FillIterationDropdown iterationDropdown = new FillIterationDropdown(iterationBox);
		DB.getAllIterations(iterationDropdown);
		iterColumn.setCellEditor(new DefaultCellEditor(iterationBox));
		
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setToolTipText("Click to change iteration");
		iterColumn.setCellRenderer(renderer);
	}
	
	/**
	 * make the status comboboxes in the status column
	 *
	 * @param table the JTable that has the column
	 * @param statusColumn the status column
	 */
	public void setUpStatusColumn(JTable table, TableColumn statusColumn) {

		JComboBox statusBox = new JComboBox();
		statusBox.addItem("NEW");
		statusBox.addItem("OPEN");
		statusBox.addItem("IN_PROGRESS");
		statusBox.addItem("COMPLETE");
		statusBox.addItem("DELETED");
		
		statusColumn.setCellEditor(new DefaultCellEditor(statusBox));
		
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setToolTipText("Click to change status");
		statusColumn.setCellRenderer(renderer);
		
	}
	
	/**
	 * make the priority comboxes in the priority column
	 *
	 * @param table the JTable that hs the column
	 * @param priorityColumn the priority column
	 */
	public void setUpPriorityColumn(JTable table, TableColumn priorityColumn) {
		JComboBox priorityBox = new JComboBox();
		priorityBox.addItem("NONE");
		priorityBox.addItem("LOW");
		priorityBox.addItem("MEDIUM");
		priorityBox.addItem("HIGH");
		
		priorityColumn.setCellEditor(new DefaultCellEditor(priorityBox));
		

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setToolTipText("Click to change priority");
		priorityColumn.setCellRenderer(renderer);
	}
	
	public List<RequirementModel> updateModels(List<RequirementModel> reqs, List<Iteration> iterations) {

		if(reqs != null && reqs.size() >= 1) {
			for(int i = 0; i < tableModel.getRowCount(); i++) {
				for(RequirementModel req : reqs) {
					if(req.getId() == Integer.valueOf((String)tableModel.getValueAt(i, 0))) {
						// we have the correct requirement, update values
						req.setName((String)tableModel.getValueAt(i, 1));
						// Find the right iteration from the list
						//if(tableModel.getValueAt(i, 2) == "Backlog") {
							req.setIteration(null);
						//}
						//else {
							for(Iteration iteration : iterations) {
								System.out.println("** Iteration: " + iteration.toString());
								if(iteration.getIterationNumber().equals((String)tableModel.getValueAt(i, 2))) {
									System.out.println("Chose this iteration: " + iteration.toString());
									req.setIteration(iteration);
									break;
								}
							}
						//}
						req.setStatus(RequirementStatus.valueOf((String)tableModel.getValueAt(i, 3)));
						req.setPriority(RequirementPriority.valueOf((String)tableModel.getValueAt(i, 4)));
						req.setEstimate(Integer.valueOf((String)tableModel.getValueAt(i, 5)));
						break;
					}
				}
			}
		}
		
		return reqs;
	}
	
	public boolean validateModels() {
		boolean noErrors = true;
		
		for(int i = 0; i < tableModel.getRowCount(); i++) {
			// check name
			if(((String)tableModel.getValueAt(i, 1)).length() < 1) {
				// highlight field
				System.out.println("Error in name for Requirement in row " + i);
				noErrors = false;
			}
			// check estimate
			if((Integer.valueOf((String)tableModel.getValueAt(i, 5))) < 0) {
				// highlight field
				System.out.println("Error in estimate for Requirement in row " + i);
				noErrors = false;
			}
		}

		return noErrors;
	}
	
	public void sendRequirementsToDatabase(List<RequirementModel> reqs) {
		for(RequirementModel req : reqs) {
			DB.updateRequirements(req, new UpdateRequirementCallback());
		}
	}
	
	class UpdateRequirementCallback implements SingleRequirementCallback {

		@Override
		public void callback(RequirementModel req) {
			// nothing to do
		}
		
	}
	
	class RetrieveAllRequirementsCallback implements RequirementsCallback {
		
		@Override
		public void callback(List<RequirementModel> reqs) {
			DB.getAllIterations(new RetrieveAllIterationsCallback(reqs));
		}
		
	}
	
	class RetrieveAllIterationsCallback implements IterationCallback {
		
		List<RequirementModel> reqs;
		
		public RetrieveAllIterationsCallback(List<RequirementModel> reqs) {
			this.reqs = reqs;
		}

		@Override
		public void callback(List<Iteration> iterationss) {
			List<RequirementModel> requirements = updateModels(reqs, iterationss);
			sendRequirementsToDatabase(requirements);
			
		}
		
	}
	
	class UpdateTableCallback implements RequirementsCallback {
		@Override
		public void callback(List<RequirementModel> reqs) {
			if (reqs.size() > 0) {
				// put the data in the table
				Object[][] entries = new Object[reqs.size()][6];
				int i = 0;
				for(RequirementModel req : reqs) {
					entries[i][0] = String.valueOf(req.getId());
					entries[i][1] = req.getName();
					if (req.getIteration() != null) {
						entries[i][2] = req.getIteration().getIterationNumber().toString();	
					}
					else {
						entries[i][2] = "Backlog";
					}
					if (req.getStatus() != null) {
						entries[i][3] = req.getStatus().toString();
					}
					else {
						entries[i][3] = "Error: Status set to null";
					}
					if (req.getPriority() != null) {
						entries[i][4] = req.getPriority().toString();
					}
					else {
						entries[i][4] = "";
					}
					entries[i][5] = req.getEstimate()+"";
					i++;
				}
				getTable().setData(entries);
				getTable().fireTableStructureChanged();
			}
			else {
				// do nothing, there are no requirements
			}
		
			TableColumn column = null;
			for (int i = 0; i < 6; i++) {
				column = table.getColumnModel().getColumn(i);
				if (i == 0) {
					column.setPreferredWidth(25); //third column is bigger
				}
				else if (i == 1) {
					column.setPreferredWidth(700);
				}
				else {
					column.setPreferredWidth(200);
				}
			}
		}
		
	}
	
	/**
	 *
	 * Class to take the list of iterations from the database and fill in a combobox with their names
	 * @author James
	 *
	 */
	class FillIterationDropdown implements IterationCallback {
		
		/**
		 * The combo box that will get filled in
		 */
		JComboBox iterationBox;
		
		/**
		 * Constructor
		 * @param iterationBox the combobox that will get filled in
		 */
		public FillIterationDropdown(JComboBox iterationBox) {
			this.iterationBox = iterationBox;
			iterationBox.addItem("Backlog");
		}

		/**
		 * Go through the list of iterations and add their name to the combobox
		 *
		 * @param iterationss the list of iterations
		 */
		@Override
		public void callback(List<Iteration> iterationss) {
			if(iterationss.size() > 0) {
				final Date now = new Date();
				for(Iteration iteration : iterationss) {
					// Make sure the iteration the only iterations that are added are still in progress
					if(now.before(iteration.getEndDate()) || now == iteration.getEndDate()) {
						iterationBox.addItem("" + iteration.getIterationNumber());
					}
				}
			}
		}
		
	}

}
