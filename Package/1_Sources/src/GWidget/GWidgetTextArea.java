package GWidget;


import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.jdom.Element;

import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;

public class GWidgetTextArea extends GWidget {
	private static final long serialVersionUID = 1L;

	//The textfield
	/**
	 * @uml.property  name="textArea"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JTextArea textArea;
	
	//The XML keyword associated
	/**
	 * @uml.property  name="xmlKeyword"
	 */
	private String xmlKeyword;
	
	public GWidgetTextArea() {
		super();		
		
		xmlKeyword = null;
		
		initialization();
	}
	
	public GWidgetTextArea(GObject p, GObjectInformation info, String name) {
		super(p, info);

		initialization();
		
		if(name != null)
			if(name.length() > 0) {
				Border blackLine;
				blackLine = BorderFactory.createLineBorder(Color.black);
				setBorder(BorderFactory.createTitledBorder(blackLine, name, TitledBorder.LEFT, TitledBorder.DEFAULT_JUSTIFICATION , null));
			}
	}
	
	private void initialization() {
		
		//First we set up the layout		
		setLayout(new BorderLayout());
		
		//We also have to create a JScrollPane for the textarea
		textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		
		add(scrollPane);
	}
	
	public void translateMessage(GMessage message) {
	}
	
	public boolean loadXML(Vector<Element> e, boolean deeplyRooted) {
		if(e == null)
			return false;
		
		if(e.size() <= 0)
			return false;
		
		//Modifies the value of the xml keyword
		xmlKeyword = e.get(0).getName();
		
		//Then put the value in the textfield
		textArea.setText(e.get(0).getText());
		
		return true;
	}
	
	public Vector<Element> saveXML() {
		
		//We return null if the field is empty or if the tag is not defined
		if(xmlKeyword == null || textArea.getText().length() == 0)
			return null;
		
		Vector<Element> resultVector = new Vector<Element>();
		Element result = new Element(xmlKeyword);
		result.setText(textArea.getText());
		resultVector.add(result);
		
		return resultVector;
	}
}
