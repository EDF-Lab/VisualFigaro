package GWidget;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import global.Messages;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;


public class GWidgetAddDelEdit extends GWidgetControl {
	
	private static final long serialVersionUID = 1L;

	//Three different types of button
	/**
	 * @uml.property  name="addButton"
	 * @uml.associationEnd  
	 */
	private JButton addButton;
	/**
	 * @uml.property  name="delButton"
	 * @uml.associationEnd  
	 */
	private JButton delButton;
	/**
	 * @uml.property  name="editButton"
	 * @uml.associationEnd  
	 */
	private JButton editButton;
	
	public GWidgetAddDelEdit() {
		super();
	}
	
	public GWidgetAddDelEdit(GObject p, GObjectInformation info) {
		super(p, info);
		
		//Set the specific layout
		this.setLayout(new GridLayout(3,1,5,5));
		
		//Creation of the add button
		addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetAddDelEdit.this.parent.translateMessage(new GMessage(information, Messages.ADD));
			}
		});

		//Creation of the delete button
		delButton = new JButton("Del");
		delButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetAddDelEdit.this.parent.translateMessage(new GMessage(information, Messages.DEL));
			}
		});
		
		//Creation of the edit button
		editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetAddDelEdit.this.parent.translateMessage(new GMessage(information, Messages.EDIT));
			}
		});
		
		//All the buttons are put in the top panel
		this.add(addButton);
		this.add(delButton);
		this.add(editButton);
	}
	
	public void translateMessage(Messages m, Object arg) {
		
	}
}
