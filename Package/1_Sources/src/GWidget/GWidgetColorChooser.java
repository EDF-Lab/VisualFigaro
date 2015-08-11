/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 01 July 2015                            
 * Author       : L.RAFFAELLI/ALL4TEC                              
 * Bug Id       : 
 * Evol Id      : n°23                                     
 * Modification : Selection of a color via a graphical interface
 * VF version   : 2.00
 * **************************************************************/

package GWidget;

import global.Messages;
import global.WindowClasses;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jdom.Element;

import Factories.GWindowFactory;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWindow.GWindow;

public class GWidgetColorChooser extends GWidget {

	private static final long serialVersionUID = 1L;

	//The main buttonfield
	/**
	 * @uml.property  name="textfield"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JButton buttonfield;
	
	//The reset button
	private JButton resetbutton;
	
	//The XML keyword associated
	/**
	 * @uml.property  name="xmlKeyword"
	 */
	private String xmlKeyword;
	
	public GWidgetColorChooser() {
		super();		
		
		initialization();
	}
	
	public GWidgetColorChooser(GObject p, GObjectInformation info) {
		super(p, info);

		initialization();
	}
	
	private void initialization() {
		
		xmlKeyword = null;
		
		this.setLayout(new BorderLayout());
		buttonfield = new JButton();
		this.add(buttonfield, BorderLayout.CENTER);
		
		buttonfield.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetColorChooser.this.translateMessage(new GMessage(information, Messages.SELECTCOLOR));
			}
		});
		
		resetbutton = new JButton("X");
		this.add(resetbutton, BorderLayout.EAST);
		
		resetbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetColorChooser.this.translateMessage(new GMessage(information, Messages.RESETCOLOR));
			}
		});
	}
	
	public void translateMessage(GMessage message) {
		
		//Some local variables
		GWindow window = null;
		
		switch(message.getMessage()) {
			
			case SELECTCOLOR:
				window = GWindowFactory.createWindow(WindowClasses.COLORCHOOSER, this, information, new Object[]{buttonfield.getText()});
				window.setVisible(true);
				window.setAlwaysOnTop(true);
				break;
				
			case RESETCOLOR:
				buttonfield.setText("Empty");
				buttonfield.setBackground(new Color(255,255,255));
				break;
		
			case REPLACEDEFAULTVALUES:
				System.out.println("REPLACEMENT REQUIERED");
				
				//If the message do no contain any argument just return
				if(message.getArguments() == null){
					buttonfield.setText("Empty");
					buttonfield.setBackground(new Color(255,255,255));
					return;
				}
				
				//Otherwise load the color
				loadColorFromString(message.getArguments().get(0).toString());
	
				break;
				
			default:
				System.out.println("VisualFigaro : GWidgetColorChooser : Message not handled");
		}
	}
	
	//load and apply the background color according to the colorString in hexa
	public void loadColorFromString(String colorString){
		
		if (colorString.length() != 6) {
			buttonfield.setText("Empty");
			buttonfield.setBackground(new Color(255,255,255));
		}
		else{
			try{
				int r = Integer.parseInt(colorString.substring(0, 2), 16);
				int g = Integer.parseInt(colorString.substring(2, 4), 16);
				int b = Integer.parseInt(colorString.substring(4, 6), 16);
				
				buttonfield.setText(colorString);
				buttonfield.setBackground(new Color(r,g,b));
			}
			catch(Exception e){
				buttonfield.setText("Empty");
				buttonfield.setBackground(new Color(255,255,255));
			}
		}
	}
	
	public boolean loadXML(Vector<Element> e, boolean deeplyRooted) {
		if(e == null)
			return false;
		
		if(e.size() <= 0)
			return false;

		//Modifies the value of the xml keyword
		xmlKeyword = e.get(0).getName();
			
		//Then load the color
		loadColorFromString(e.get(0).getText());

		System.out.println("TEXTFIELD : " + xmlKeyword);
		
		return true;
	}
	
	public Vector<Element> saveXML() {
		//We return null if the tag is not defined or no color has been selected
		if(xmlKeyword == null || buttonfield.getText().equals("Empty"))
			return null;
		Vector<Element> resultVector = new Vector<Element>();
		Element result = new Element(xmlKeyword);
		result.setText(buttonfield.getText());
		resultVector.add(result);

		return resultVector;
	}
	
	public Vector<Element> saveXML(String nom) {
			xmlKeyword=information.getLanguage().getBDCTranslation("NOM");
			Vector<Element> resultVector = new Vector<Element>();
			Element result = new Element(xmlKeyword);
			result.setText(buttonfield.getText());
			resultVector.add(result);

			return resultVector;
		}
}
