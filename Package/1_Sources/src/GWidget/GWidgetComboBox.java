package GWidget;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComboBox;

import org.jdom.Element;

import global.Messages;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GXSDOperations.GXSDOperations;


public class GWidgetComboBox extends GWidget {
	
	private static final long serialVersionUID = 1L;

	//The java comboBox
	/**
	 * @uml.property  name="comboBox"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	JComboBox comboBox;
	
	//The keyword which will be filled
	/**
	 * @uml.property  name="xmlKeyword"
	 */
	String xmlKeyword;
	
	//The list of all the values
	/**
	 * @uml.property  name="values"
	 */
	Vector<String> values;
	
	public GWidgetComboBox() {
		super();

		initialization();
	}
	
	public GWidgetComboBox(GObject p, GObjectInformation info, Vector<String> args) {
		super(p, info);
		
		initialization();
		
		if(args != null) {
			//If there are some arguments it means that there are acceptable values
			this.setAcceptedValues(args);
		}
	}
	
	private void initialization() {
		
		//Set the xml keyword to null. It will be initialized in loadXml
		xmlKeyword = null;
		
		//Initialization of the values vector
		values = new Vector<String>();
		
		//Set the layout and add the main component. Here a combobox
		setLayout(new BorderLayout());
		comboBox = new JComboBox();
		//We add an action listener to be informed if the content change and, for example, notify the other widgets of this change
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(GWidgetComboBox.this.comboBox.getSelectedIndex() >= 0) {
					Vector<Object> args = new Vector<Object>();
					args.add(GWidgetComboBox.this.comboBox.getSelectedItem().toString());
					GMessage message = new GMessage(information, Messages.NOTIFYCHANGE, args);
					GWidgetComboBox.this.parent.translateMessage(message);
				}
			}
		});
		add(comboBox);
	}
	
	public void setAcceptedValues(Vector<String> acceptedValues) {
		//First we add all the accepted values to the object
		values.clear();
		values.addAll(acceptedValues);
		
		//Then we have to modify the comboBox
		comboBox.removeAllItems();
		for(Iterator<String> iter = acceptedValues.iterator(); iter.hasNext();)
			comboBox.addItem(iter.next());
	}
	
	public void translateMessage(GMessage message) {
		
		switch(message.getMessage()) {
			
			case ADDDEFAULTVALUES:
				if(message.getArguments() == null)
					return;
				
				if(message.getArguments().size() < 1)
					return;
				
				for(Object obj : message.getArguments())
					comboBox.addItem(obj.toString());
				break;
				
			case REPLACEDEFAULTVALUES:
				System.out.println("REPLACEMENT REQUIERED");
				
				if(message.getArguments() == null)
					return;
				/*
				if(message.getArguments().size() < 1)
					return;
				*/
				
				comboBox.removeAllItems();
				for(Object obj : message.getArguments())
					comboBox.addItem(obj.toString());
				break;
				
			case SETMODIFIABLE:
				
				if(message.getArguments() == null)
					return;
				
				if(message.getArguments().size() <= 0)
					return;
				
				comboBox.setEnabled((Boolean)message.getArguments().get(0));
				
				break;
				
			default:
				System.out.println("VisualFigaro : GWidgetComboBox : Message not handled");
		}
	}
	
	public boolean loadXML(Vector<Element> e, boolean deeplyRooted) {
		
		if(e == null)
			return false;
		
		if(e.size() <= 0)
			return false;
		
		if(e.get(0) == null)
			return false;
		
		//Modifies the value of the xml keyword
		xmlKeyword = e.get(0).getName();
		
		//We are going to retrieve the eventual default values of this element
		Vector<String> allowedValues = GXSDOperations.getAllowedValuesForNode(xmlKeyword);
		if(allowedValues != null)
			if(allowedValues.size() > 0) {
				comboBox.removeAllItems();
				for(Iterator<String> iter = allowedValues.iterator(); iter.hasNext();)
					comboBox.addItem(iter.next());
			}
				
		
		//###Then we select the value in the combobox or return false if there is no such item
		comboBox.setSelectedItem(e.get(0).getText());
		
		return true;
	}
	
	public Vector<Element> saveXML() {
		
		//We return null if the field is empty or if the tag is not defined
		if(xmlKeyword == null || comboBox.getSelectedIndex() < 0)
			return null;
		
		Vector<Element> resultVector = new Vector<Element>();
		Element result = new Element(xmlKeyword);
		result.setText(comboBox.getSelectedItem().toString());
		resultVector.add(result);
		
		return resultVector;
	}
}
