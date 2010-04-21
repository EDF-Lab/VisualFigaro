package GWidget;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import global.Messages;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;

public class GWidgetAddDelUpDown extends GWidgetControl {

	private static final long serialVersionUID = 1L;

	//Four different types of button
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
	 * @uml.property  name="upButton"
	 * @uml.associationEnd  
	 */
	private JButton upButton;
	/**
	 * @uml.property  name="downButton"
	 * @uml.associationEnd  
	 */
	private JButton downButton;
	
	public GWidgetAddDelUpDown() {
		super();
	}
	
	public GWidgetAddDelUpDown(GObject p, GObjectInformation info) {
		super(p, info);
		
		//Set the specific layout
		this.setLayout(new GridLayout(3,1,5,5));
		
		//Creation of the add button
		addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetAddDelUpDown.this.parent.translateMessage(new GMessage(information, Messages.ADD));
			}
		});

		//Creation of the delete button
		delButton = new JButton("Del");
		delButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetAddDelUpDown.this.parent.translateMessage(new GMessage(information, Messages.DEL));
			}
		});
		
		//Creation of the up/down panel
		JPanel upDownPanel = new JPanel(new GridLayout(1,2,5,5));
		upButton = new JButton("/\\");
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetAddDelUpDown.this.parent.translateMessage(new GMessage(information, Messages.UP));
			}
		});
		upDownPanel.add(upButton);
		downButton = new JButton("\\/");
		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetAddDelUpDown.this.parent.translateMessage(new GMessage(information, Messages.DOWN));
			}
		});
		upDownPanel.add(downButton);
		
		//All the buttons are put in the top panel
		this.add(addButton);
		this.add(delButton);
		this.add(upDownPanel);
	}
}
