package GWindow;


import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdom.Element;

import global.Messages;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidgetOKCancel;

public class GWindowName extends GWindow {
	
	private static final long serialVersionUID = 1L;
	
	//The OK/Cancel buttons
	/**
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetOKCancel okCancelWidget;	

	//The textfield
	/**
	 * @uml.property  name="textField"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JTextField textField;
	
	@Deprecated
	public GWindowName(GObject p, GObjectInformation info) {
		super(p, info);
		
		initialization();
	}
	
	public GWindowName(GObject p, GObjectInformation info, String name, int pos) {
		super(p, info);
		
		initialization();
		this.setTitle(name);
	}
	
	private void initialization() {
		
		//Initialization of the label and the textfield
		JLabel label = new JLabel("Name : ");
		textField = new JTextField();
		
		//Then we put them into a simple panel and put this simple panel into the top panel
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(label, BorderLayout.WEST);
		panel.add(textField, BorderLayout.CENTER);
		this.framePanel.add(panel, BorderLayout.CENTER);
		
		//Finally we initilize and put the ok/cancel widget and put it at the bottom of the top panel
		this.okCancelWidget = new GWidgetOKCancel(this, information);
		this.framePanel.add(okCancelWidget, BorderLayout.SOUTH);
		
		this.add(this.framePanel);
		
		this.pack();
		this.setTitle("Name Form");
	}
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
			case OK:
				Element returnedElement = new Element(textField.getText());
				returnedElement.setText(textField.getText());
				parent.translateMessage(new GMessage(information, Messages.ADDSIMPLEELEMENT, new Object[]{-1, returnedElement}));
				dispose();
				break;
			
			case CANCEL:
				dispose();
				break;
				
			default:
				System.out.println("VisualFigaro : GWindowDepart : Unknown message received");
		}
	}
	
	public Element fillDocument() {
		return null;
	}
	
	public void loadXml(Element e) {
	}
}
