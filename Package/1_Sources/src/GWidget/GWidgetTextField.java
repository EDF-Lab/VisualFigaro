package GWidget;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JTextField;


import org.jdom.Element;

import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;

public class GWidgetTextField extends GWidget {

	private static final long serialVersionUID = 1L;

	//The textfield
	/**
	 * @uml.property  name="textfield"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JTextField textfield;
	
	//The XML keyword associated
	/**
	 * @uml.property  name="xmlKeyword"
	 */
	private String xmlKeyword;
	
	public GWidgetTextField() {
		super();		
		
		initialization();
	}
	
	public GWidgetTextField(GObject p, GObjectInformation info) {
		super(p, info);

		initialization();
	}
	
	private void initialization() {
		
		xmlKeyword = null;
		
		setLayout(new BorderLayout());
		textfield = new JTextField();
		add(textfield);
	}
	
	public void translateMessage(GMessage message) {
		
		switch(message.getMessage()) {
				
			case REPLACEDEFAULTVALUES:
				System.out.println("REPLACEMENT REQUIERED");
				
				//If the message do no contain any argument just return
				if(message.getArguments() == null)
					return;
				
				//Otherwise we change the textfield text
				textfield.setText(message.getArguments().get(0).toString());
				
				break;
				
			default:
				System.out.println("VisualFigaro : GWidgetTextFields : Message not handled");
		}
	}
	
	public boolean loadXML(Vector<Element> e, boolean deeplyRooted) {
		if(e == null)
			return false;
		
		if(e.size() <= 0)
			return false;

		//Modifies the value of the xml keyword
		xmlKeyword = e.get(0).getName();
			
		//Then put the value in the textfield
		textfield.setText(e.get(0).getText());
	
		System.out.println("TEXTFIELD : " + xmlKeyword);
		
		return true;
	}
	
	public Vector<Element> saveXML() {
		//We return null if the tag is not defined
		if(xmlKeyword == null)
			return null;
		Vector<Element> resultVector = new Vector<Element>();
		Element result = new Element(xmlKeyword);
		result.setText(textfield.getText());
		resultVector.add(result);

		return resultVector;
	}
	
	public Vector<Element> saveXML(String nom) {
			xmlKeyword=information.getLanguage().getBDCTranslation("NOM");
			Vector<Element> resultVector = new Vector<Element>();
			Element result = new Element(xmlKeyword);
			result.setText(textfield.getText());
			resultVector.add(result);

			return resultVector;
		}
}
