package GWidget;

import global.ControlTypes;
import global.ListTypes;
import global.NameRetrieverClasses;
import global.WindowClasses;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.jdom.Element;

import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;

public class GWidgetExpression extends GWidget {

	private static final long serialVersionUID = 1L;

	//The text field
	/**
	 * @uml.property  name="formatTextField"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetTextField formatTextField;
	
	//The list of arguments
	/**
	 * @uml.property  name="parametersList"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetControledList parametersList;
	
	public GWidgetExpression() {
		super();

		initialization();
	}
	
	public GWidgetExpression(GObject p, GObjectInformation info) {
		super(p, info);
		
		initialization();
	}
	
	private void initialization() {
		
		Border blackLine;
		blackLine = BorderFactory.createLineBorder(Color.black);
		
		//Set the layout manager
		setLayout(new BorderLayout());
		
		//Create a small panel to store the textfield and its label
		JPanel textPanel = new JPanel(new BorderLayout());
		textPanel.add(new JLabel("Format : "), BorderLayout.WEST);
		formatTextField = new GWidgetTextField();
		textPanel.add(formatTextField, BorderLayout.CENTER);
		add(textPanel, BorderLayout.NORTH);
		
		//Create the list and store it in the widget
		parametersList = new GWidgetControledList(this, information, null, null, ListTypes.SIMPLEARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDEL, WindowClasses.NAMESIMPLE, null);
		add(parametersList, BorderLayout.CENTER);
		parametersList.setBorder(BorderFactory.createTitledBorder(blackLine, "Arguments", TitledBorder.LEFT, TitledBorder.DEFAULT_JUSTIFICATION, null));
		
		setBorder(BorderFactory.createTitledBorder(blackLine, "Expression", TitledBorder.LEFT, TitledBorder.DEFAULT_JUSTIFICATION , null));
	}
	
	public void translateMessage(GMessage message) {
	}
	
	public boolean loadXML(Vector<Element> elements, boolean deeplyRooted) {
		
		if(elements == null)
			return false;
		
		if(elements.size() == 0)
			return false;

		//Creation of a small vector to store the data before calling loadXML
		Vector<Element> bufferVector = new Vector<Element>(1);
		
		//We create the iterator to navigate trough the vector of elements
		Iterator<Element> iter = elements.iterator();
		
		//First we take care of the first element which contains the format
		bufferVector.add(iter.next());
		formatTextField.loadXML(bufferVector, false);
		bufferVector.clear();
		
		//Then comes the turn of the parameters which have to be loaded in the list 
		for(;iter.hasNext();) {
			bufferVector.add(iter.next());
		}
		parametersList.loadXML(bufferVector, false);
		
		return true;
	}
	
	public Vector<Element> saveXML() {
	
		//The vector in which the result will be stored
		Vector<Element> resultVector = new Vector<Element>();
		
		//First we save the format
		Vector<Element> formatTextFieldVector = new Vector<Element>(formatTextField.saveXML());
		if(!formatTextFieldVector.get(0).getText().equals(""))
			resultVector.addAll(formatTextField.saveXML());
		
		//Then we save the content of the list
		resultVector.addAll(parametersList.saveXML());
		
		return resultVector;
	}
}
