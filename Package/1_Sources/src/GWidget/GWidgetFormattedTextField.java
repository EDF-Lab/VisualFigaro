package GWidget;

import java.awt.BorderLayout;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Formatter;
//import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;


import org.jdom.Element;

//import bsh.ParseException;

import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;

public class GWidgetFormattedTextField extends GWidget{
	private static final long serialVersionUID = 1L;

	//The formattedTextfield
	/**
	 * @uml.property  name="formattedTextfield"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JFormattedTextField dateField;
	
	//The XML keyword associated
	/**
	 * @uml.property  name="xmlKeyword"
	 */
	private String xmlKeyword;
	
	public GWidgetFormattedTextField() {
		super();		
		
		initialization();
	}
	
	public GWidgetFormattedTextField(GObject p, GObjectInformation info) {
		super(p, info);

		initialization();
	}
	
	private void initialization() {
		
		xmlKeyword = null;
		
		setLayout(new BorderLayout());
		
	    MaskFormatter formatter;
		try {
			formatter = new MaskFormatter("####-##-##");
			formatter.setPlaceholderCharacter('0');
			dateField = new JFormattedTextField(formatter);
			/*Calendar c = GregorianCalendar.getInstance();
			c.setTime(new Date());
			int day = c.get(Calendar.DAY_OF_MONTH);
			int month = c.get(Calendar.MONTH)+1;
			int year = c.get(Calendar.YEAR);
			String date;
			String strMonth;
			String strDay;
			if(month<10)
				strMonth="0"+month;
			else
				strMonth=""+month;
			if(day<10)
				strDay="0"+day;
			else
				strDay=""+day;
			date=year+"-"+strMonth+"-"+strDay;
			dateField.setValue(date);*/
			add(dateField);
		}
	    catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void translateMessage(GMessage message) {
		
		switch(message.getMessage()) {
				
			case REPLACEDEFAULTVALUES:
				System.out.println("REPLACEMENT REQUIERED");
				
				//If the message do no contain any argument just return
				if(message.getArguments() == null)
					return;
				
				//Otherwise we change the formattedTextfield text
				dateField.setText(message.getArguments().get(0).toString());
				
				break;
				
			default:
				System.out.println("VisualFigaro : GWidgetFormattedTextFields : Message not handled");
		}
	}
	
	public boolean loadXML(Vector<Element> e, boolean deeplyRooted) {
		
		if(e == null)
			return false;
		
		if(e.size() <= 0)
			return false;
		
		//Modifies the value of the xml keyword
		xmlKeyword = e.get(0).getName();
		
		//Then put the value in the formattedTextfield
		if(!e.get(0).getText().equals(""))
			dateField.setText(e.get(0).getText());
		
		System.out.println("FORMATTEDTEXTFIELD : " + xmlKeyword);
		
		return true;
	}
	
	public Vector<Element> saveXML() {
		
		//We return null if the tag is not defined
		if(xmlKeyword == null)
			return null;
		
		Vector<Element> resultVector = new Vector<Element>();
		Element result = new Element(xmlKeyword);
		result.setText(dateField.getText());
		resultVector.add(result);
		
		return resultVector;
	}
}
