package GWidget;

import global.Messages;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;

public class GWidgetNodeLinkNeitherUpDownEdit extends GWidgetControl {
	
	private static final long serialVersionUID = 1L;

	//Six different types of button
	/**
	 * @uml.property  name="nodeButton"
	 * @uml.associationEnd  
	 */
	private JButton nodeButton;
	/**
	 * @uml.property  name="linkButton"
	 * @uml.associationEnd  
	 */
	private JButton linkButton;
	/**
	 * @uml.property  name="neitherButton"
	 * @uml.associationEnd  
	 */
	private JButton neitherButton;
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
	/**
	 * @uml.property  name="editButton"
	 * @uml.associationEnd  
	 */
	private JButton editButton;
	
	public GWidgetNodeLinkNeitherUpDownEdit() {
		super();
	}
	
	public GWidgetNodeLinkNeitherUpDownEdit(GObject p, GObjectInformation info) {
		super(p, info);
		
		//Set the specific layout
		this.setLayout(new GridLayout(5,1,5,5));
		
		//Creation of the node button
		nodeButton = new JButton("Node");
		nodeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetNodeLinkNeitherUpDownEdit.this.parent.translateMessage(new GMessage(information, Messages.NODE));
			}
		});

		//Creation of the link button
		linkButton = new JButton("Link");
		linkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetNodeLinkNeitherUpDownEdit.this.parent.translateMessage(new GMessage(information, Messages.LINK));
			}
		});
		
		//Creation of the neither button
		neitherButton = new JButton("Neither");
		neitherButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetNodeLinkNeitherUpDownEdit.this.parent.translateMessage(new GMessage(information, Messages.NEITHER));
			}
		});
		
		//Creation of the up/down panel
		JPanel upDownPanel = new JPanel(new GridLayout(1,2,5,5));
		upButton = new JButton("/\\");
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetNodeLinkNeitherUpDownEdit.this.parent.translateMessage(new GMessage(information, Messages.UP));
			}
		});
		upDownPanel.add(upButton);
		downButton = new JButton("\\/");
		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetNodeLinkNeitherUpDownEdit.this.parent.translateMessage(new GMessage(information, Messages.DOWN));
			}
		});
		upDownPanel.add(downButton);
		
		//Creation of the edit button
		editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetNodeLinkNeitherUpDownEdit.this.parent.translateMessage(new GMessage(information, Messages.EDIT));
			}
		});
		
		//All the buttons are put in the top panel
		this.add(nodeButton);
		this.add(linkButton);
		this.add(neitherButton);
		this.add(upDownPanel);
		this.add(editButton);
	}
}
