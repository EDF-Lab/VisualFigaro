package GWidget;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import global.Messages;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;


public class GWidgetOKCancel extends GWidgetControl {
	
	private static final long serialVersionUID = 1L;
	
	//OK and Cancel buttons
	/**
	 * @uml.property  name="okButton"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JButton okButton;
	/**
	 * @uml.property  name="cancelButton"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JButton cancelButton;
	
	public GWidgetOKCancel() {
		super();
		initialization();
	}
	
	public GWidgetOKCancel(GObject p, GObjectInformation info) {
		super(p, info);
		initialization();
	}
	
	private void initialization() {
		
		//Add a simple layout
		setLayout(new GridLayout(1,2,5,5));
		
		//Create the ok button
		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.translateMessage(new GMessage(information, Messages.OK));
			}
		});
		
		//Create the cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.translateMessage(new GMessage(information, Messages.CANCEL));
			}
		});
		
		//Add the two buttons to the widget
		add(okButton);
		add(cancelButton);
	}
}
