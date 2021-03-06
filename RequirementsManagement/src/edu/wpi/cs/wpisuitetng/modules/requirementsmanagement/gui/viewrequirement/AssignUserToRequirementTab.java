/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Josh
 *    Deniz
 *    William
 *    Jacob Palnick
 *    vpatara
 *    Cindy
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.viewrequirement;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.CurrentUserPermissionManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.SinglePermissionCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.UsersCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.Tab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ViewUserTable;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabView;


/**
 * Main panel for viewing/editing users
 * @author Josh
 * @author Deniz
 * @author Jacob Palnick
 * @author William Terry
 * @author cynthia
 */
@SuppressWarnings("serial")
public class AssignUserToRequirementTab extends JPanel {

	public static final int ID = 0;
	public static final int NAME = 1;
	public static final int USERNAME = 2;
	public static final int PERMISSIONLEVEL = 3;
	public static final int COLUMN = 4; //change to 4 when we can acquire permissions from the db

	/** the panel this is shown in */
	RequirementsPanel parent;
	/** tableModel to display currently assigned users */
	ViewUserTable assignedUserTableModel;
	/** tableModel to display other users that can be assigned */
	ViewUserTable possibleUserTableModel;
	/** tableModle to display currently assigned users */
	JTable assignedUserTable;
	/** tableModel to display other users that can be assigned */
	JTable possibleUserTable;
	/** user list scroll pane */
	JScrollPane assignedUserTableScrollPane;
	/** possible user list scroll pane */
	JScrollPane possibleUserTableScrollPane;
	/** list of user*/
	List<User> assignees;
	JButton addUserButton;
	JButton removeUserButton;
	int selectedRow;
	int rowsInAssignedTable;
	int rowsInPossibleTable;
	String permissionLevel;

	/**
	 * Constructs a panel for notes
	 * 
	 * @param requirementsPanel NoteTab that contains this object
	 */
	public AssignUserToRequirementTab(RequirementsPanel requirementsPanel) {
		parent = requirementsPanel;
		assignees = requirementsPanel.model.getAssignees();
		// Add all components to this panel
		addComponents();
		
		update();
	}

	/**
	 * Adds the components to the panel and places constraints on them
	 * for the SpringLayout manager.
	 * @param layout the layout manager
	 */
	protected void addComponents() {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		JLabel asgnULabel = new JLabel("Assigned Users");
		JLabel otherULabel = new JLabel("All Other Users");

		assignedUserTableModel = new ViewUserTable();
		assignedUserTable = new JTable(assignedUserTableModel);
		assignedUserTable.setPreferredScrollableViewportSize(new Dimension(500, 100));
		assignedUserTable.setFillsViewportHeight(true);
		assignedUserTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		assignedUserTable.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 1){
					updateSelectedPossible(getSelectedSubId());
				}	
			}
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseClicked(MouseEvent arg0) {}
		});
		
		assignedUserTableScrollPane = new JScrollPane(assignedUserTable);
		assignedUserTableScrollPane.setPreferredSize(new Dimension(300, 300));
		assignedUserTableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		assignedUserTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		possibleUserTableModel = new ViewUserTable();
		possibleUserTable = new JTable(possibleUserTableModel);
		possibleUserTable.setPreferredScrollableViewportSize(new Dimension(500, 100));
		possibleUserTable.setFillsViewportHeight(true);
		possibleUserTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		possibleUserTable.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 1) {
                	updateSelectedPossible(getSelectedPosId());
                }
			}
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseClicked(MouseEvent arg0) {}
		});
		possibleUserTableScrollPane = new JScrollPane(possibleUserTable);
		possibleUserTableScrollPane.setPreferredSize(new Dimension(300, 300));
		possibleUserTableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		possibleUserTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		addUserButton = new JButton("Assign selected user to requirement");
		addUserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedRow >= 0 && selectedRow < rowsInPossibleTable) {
					parent.addUser((String) possibleUserTable.getModel().getValueAt(selectedRow, USERNAME));
				}
			}
			
		});

		removeUserButton = new JButton("Remove user from requirement");
		removeUserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedRow >= 0 && selectedRow < rowsInAssignedTable) {
					parent.remUser((String) assignedUserTable.getModel().getValueAt(selectedRow, USERNAME));
				}
			}
			
		});
		
		int maxPreferedWidth = (addUserButton.getPreferredSize().width > removeUserButton.getPreferredSize().width ? addUserButton.getPreferredSize().width : removeUserButton.getPreferredSize().width);
		
		Dimension pref = new Dimension(maxPreferedWidth, addUserButton.getPreferredSize().height);
		addUserButton.setPreferredSize(pref);
		addUserButton.setMinimumSize(pref);
		removeUserButton.setPreferredSize(pref);
		removeUserButton.setMinimumSize(pref);
		
		//layout the components
		layout.putConstraint(SpringLayout.WEST, asgnULabel, 4, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, asgnULabel, 4, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, assignedUserTableScrollPane, 4, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, assignedUserTableScrollPane, -4, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, assignedUserTableScrollPane, 4, SpringLayout.SOUTH, asgnULabel);
		
		layout.putConstraint(SpringLayout.WEST, otherULabel, 4, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, otherULabel, 5, SpringLayout.SOUTH, assignedUserTableScrollPane);
		
		layout.putConstraint(SpringLayout.WEST, possibleUserTableScrollPane, 4, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, this, 4, SpringLayout.EAST, possibleUserTableScrollPane);
		layout.putConstraint(SpringLayout.NORTH, possibleUserTableScrollPane, 4, SpringLayout.SOUTH, otherULabel);
		layout.putConstraint(SpringLayout.SOUTH, this, 3 + 4 + addUserButton.getPreferredSize().height, SpringLayout.SOUTH, possibleUserTableScrollPane);
		
		layout.putConstraint(SpringLayout.EAST, addUserButton, -2, SpringLayout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, addUserButton, 5, SpringLayout.SOUTH, possibleUserTableScrollPane);
		
		layout.putConstraint(SpringLayout.WEST, removeUserButton, 2, SpringLayout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, removeUserButton, 0, SpringLayout.NORTH, addUserButton);
		
		
		// Add elements to the main panel
		add(asgnULabel);
		add(assignedUserTableScrollPane);
		
		// Allow access to users with certain permission levels
		// The username info should be ready, so use the non-blocking version
		switch (CurrentUserPermissionManager.getInstance().getCurrentProfile().getPermissionLevel()) {
		case ADMIN:
			// Administrator can edit assignees
			add(possibleUserTableScrollPane);
			add(otherULabel);
			add(addUserButton);
			add(removeUserButton);
			break;

		default:
			// "None" can't do anything, not even viewing possible assignees
			break;
		}
	}
	
	
	/**
	 * called when the selection in the possible list changes.
	 * @param selectedId the id of the selected requirement
	 */
	private void updateSelectedPossible(String selectedId) {
		rowsInAssignedTable = assignedUserTable.getRowCount();
		rowsInPossibleTable = possibleUserTable.getRowCount();
		if (selectedId == null || selectedId.equals("") || parent.submit.getText().equals("Save")) {
			addUserButton.setEnabled(false);
			removeUserButton.setEnabled(false);
		} else {
			addUserButton.setEnabled(true);
			removeUserButton.setEnabled(true);
			if(possibleUserTable.isRowSelected(selectedRow)) {
				removeUserButton.setEnabled(false);
			} else {
				addUserButton.setEnabled(false);
			}
		}
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		assignedUserTableScrollPane.setEnabled(enabled);
		possibleUserTableScrollPane.setEnabled(enabled);
		assignedUserTable.setEnabled(enabled);
		possibleUserTable.setEnabled(enabled);
		if (enabled) {
			updateSelectedPossible(getSelectedPosId());
		} else {
			addUserButton.setEnabled(false);
			removeUserButton.setEnabled(false);
		}
	}

	public void update() {
		update(parent.model);
	}
	
	public void update(RequirementModel model) {
		assignees = model.getAssignees();
		String selectedSubId = getSelectedSubId();
		String selectedPossibleId = getSelectedPosId();
		
		//TODO figure out how to do sync network request.
		addUserButton.setEnabled(false);
		removeUserButton.setEnabled(false);
		assignedUserTableScrollPane.setEnabled(false);
		possibleUserTableScrollPane.setEnabled(false);
		assignedUserTable.setEnabled(false);
		possibleUserTable.setEnabled(false);
		DB.getAllUsers(new UpdateTablesCallback(selectedSubId, selectedPossibleId));
	}

	public AssignedUserTabChangeListener addChangeListenerTo (MainTabView mainView) {
		AssignedUserTabChangeListener l = new AssignedUserTabChangeListener(this.parent.parent);
		mainView.addChangeListener(l);
		return l;
	}

	private String getSelectedSubId() {
		selectedRow = assignedUserTable.getSelectedRow();
		if (selectedRow >= 0 && selectedRow < assignedUserTable.getRowCount()) {
			return (String) assignedUserTable.getModel().getValueAt(assignedUserTable.getSelectedRow(), ID);
		} else {
			return "";
		}
	}
	
	private String getSelectedPosId() {
		selectedRow = possibleUserTable.getSelectedRow();
		if (selectedRow >= 0 && selectedRow < possibleUserTable.getRowCount()) {
			return (String) possibleUserTable.getModel().getValueAt(possibleUserTable.getSelectedRow(), ID);
		} else {
			return "";
		}
	}
	
	/**
	 *
	 * Callback to populate the table with all the requirements
	 * @author Josh
	 * @author Jacob Palnick
	 * @author William Terry
	 * @author David Modica
	 */
	class UpdateTablesCallback implements UsersCallback {
		
		String selectedSub;
		String selectedPos;
		
		public UpdateTablesCallback(String selectedSub, String selectedPos) {
			this.selectedSub = selectedSub;
			this.selectedPos = selectedPos;
			
		}

		/**
		 * Callback function to populate the tables with all the requirements
		 *
		 * @param users a list of all requirements
		 */
		@Override
		public void callback(List<User> users) {
			if (users.size() > 0) {
				//put the data in the table
				ArrayList<Object[]> joinedEntryList = new ArrayList<Object[]>();
				ArrayList<Object[]> disjointEntryList = new ArrayList<Object[]>();
				for(User user : users) {
					String id = String.valueOf(user.getIdNum());
					String name = user.getName();
					String username = user.getUsername();
					if (assignees.contains(user)) {
						DB.getSinglePermission(username, new PermissionLevelRetrievalCallback(assignedUserTableModel, joinedEntryList.size()));
						Object[] joinedEntry = new Object[COLUMN];
						joinedEntry[ID] = id;
						joinedEntry[NAME] = name;
						joinedEntry[USERNAME] = username;
						joinedEntry[PERMISSIONLEVEL] = null;
						joinedEntryList.add(joinedEntry);
					} else {
						DB.getSinglePermission(username, new PermissionLevelRetrievalCallback(possibleUserTableModel, disjointEntryList.size()));
						Object[] disjointEntry = new Object[COLUMN];
						disjointEntry[ID] = id;
						disjointEntry[NAME] = name;
						disjointEntry[USERNAME] = username;
						disjointEntry[PERMISSIONLEVEL] = null;
						disjointEntryList.add(disjointEntry);
					}
				}
				Object[][] joinedEntries = {};
				Object[][] disjointEntries = {};
				if(joinedEntryList.size()>0) {
					joinedEntries = joinedEntryList.toArray(new Object[1][1]);
				}
				if(disjointEntryList.size()>0) {
					disjointEntries = disjointEntryList.toArray(new Object[1][1]);
				}

				assignedUserTableModel.setData(joinedEntries);
				assignedUserTableModel.fireTableStructureChanged();
				possibleUserTableModel.setData(disjointEntries);
				possibleUserTableModel.fireTableStructureChanged();
			}
			else {
				// do nothing, there are no users
			}
		
			TableColumn column = null;
			TableColumn column2 = null;
			for (int i = 0; i < COLUMN; i++) {
				column = assignedUserTable.getColumnModel().getColumn(i);
				column2 = possibleUserTable.getColumnModel().getColumn(i);
				if (i == ID) {
					column.setPreferredWidth(50); //third column is bigger
					column2.setPreferredWidth(50); //third column is bigger
				}
				else if (i == NAME) {
					column.setPreferredWidth(500);
					column2.setPreferredWidth(500);
				}
				else if (i == USERNAME) {
					column.setPreferredWidth(500);
					column2.setPreferredWidth(500);
				}
				else {
					column.setPreferredWidth(200);
					column2.setPreferredWidth(200);
				}
			}
			assignedUserTableScrollPane.setEnabled(true);
			possibleUserTableScrollPane.setEnabled(true);
			assignedUserTable.setEnabled(true);
			possibleUserTable.setEnabled(true);
			
			//reselect the previous selections if possible
			for (int i = 0; i<assignedUserTable.getRowCount(); i++) {
				String tmp = (String) assignedUserTable.getModel().getValueAt(i, ID);
				if(tmp != null && tmp.equals(selectedSub)) {
					assignedUserTable.setRowSelectionInterval(i, i);
				}
			}
			for (int i = 0; i<possibleUserTable.getRowCount(); i++) {
				String tmp = (String) possibleUserTable.getModel().getValueAt(i, ID);
				if(tmp != null && tmp.equals(selectedPos)) {
					possibleUserTable.setRowSelectionInterval(i, i);
				}
			}
			updateSelectedPossible(getSelectedPosId());

		}
		
	}
	
	class PermissionLevelRetrievalCallback implements SinglePermissionCallback {
		ViewUserTable table;
		int row;

		PermissionLevelRetrievalCallback(ViewUserTable table, int row){
			this.table = table;
			this.row = row;
		}
		
		@Override
		public void callback(Permissions profile) {
			String permissionLevel = profile.getPermissionLevel().toString();
			table.setValueAt(permissionLevel, row, 3);
		}

		@Override
		public void failure() {
			// TODO: show an error message
		}
	}
	
	class AssignedUserTabChangeListener implements ChangeListener{
		RequirementsTab attentiveTab;
		
		public AssignedUserTabChangeListener(RequirementsTab parent){
			this.attentiveTab = parent;
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			if(((JTabbedPane) e.getSource()).getSelectedComponent() == null){
				//TODO: find a way to remove this listener if tab has been closed
			} else if( ((JTabbedPane) e.getSource()).getSelectedComponent().equals(attentiveTab) ){
				DB.getAllUsers(new UpdateTablesCallback(getSelectedSubId(), getSelectedPosId()));
			}
			
		}
		
	}
}
