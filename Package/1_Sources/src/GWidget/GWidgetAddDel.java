package GWidget;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import global.Messages;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;

public class GWidgetAddDel extends GWidgetControl {
	
	private static final long serialVersionUID = 1L;
	
	//Two different types of button
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
	
	public GWidgetAddDel() {
		super();
	}
	
	public GWidgetAddDel(GObject p, GObjectInformation info) {
		super(p, info);
		
		//Set the specific layout
		setLayout(new GridLayout(3,1,5,5));
		
		//Creation of the add button
		addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetAddDel.this.parent.translateMessage(new GMessage(information, Messages.ADD));
			}
		});

		//Creation of the delete button
		delButton = new JButton("Del");
		delButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetAddDel.this.parent.translateMessage(new GMessage(information, Messages.DEL));
			}
		});
		
		//All the buttons are put in the top panel
		add(addButton);
		add(delButton);
	}
}
