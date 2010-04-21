package GWidget;

import global.WidgetClasses;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import org.jdom.Element;

import Factories.GWidgetFactory;
import Factories.GXMLElementFactory;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GXSDOperations.GXSDOperations;

public class GWidgetModifiableGridForm extends GWidget {
	
	private static final long serialVersionUID = 1L;

	//List of all the widgets in this widget
	/**
	 * @uml.property  name="widgets"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="GWidget.GWidget"
	 */
	private Vector<GWidget> widgets;
	
	//List of all the checkboxes in the widget
	/**
	 * @uml.property  name="checkBoxes"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.awt.Component"
	 */
	private Vector<JCheckBox> checkBoxes;
	
	//List of all the tags associated to widgets
	/**
	 * @uml.property  name="xmlTags"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	private Vector<String>	xmlTags;
	
	@Deprecated
	public GWidgetModifiableGridForm() {
		super();
		
		//Create an empty widget
		widgets = new Vector<GWidget>();
		checkBoxes = new Vector<JCheckBox>();
		setLayout(new GridBagLayout());
		xmlTags = new Vector<String>();
	}
	
	public GWidgetModifiableGridForm(GObject p, GObjectInformation info, Vector<Vector<String>> args, Vector<WidgetClasses> wc, Vector<Vector<Object>> objectsArgs) {
		super(p, info);
		
		//Create the widgets list and the checkboxes list
		widgets = new Vector<GWidget>();
		checkBoxes = new Vector<JCheckBox>();
		
		//Create a vector to store the xml tags
		xmlTags = new Vector<String>();
		
		//Set the layout manager
		setLayout(new GridBagLayout());
				
		//Then we get the number of widgets and by the same occasion test if the lengths of all vectors  are the same otherwise we continue based on the shortest one
		int numberOfElements = args.size();
		if(args.size() != wc.size()) {
			System.out.println("VisualFigaro : GWidgetModifiableGridForm : The sizes of the vectors are not all the same");
			if(args.size() > wc.size())
				numberOfElements = wc.size();
		}
		
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		//The counter used to augment the gridy propoerty given that some "line" of the grid represent in reality 3 lines 
		int positionIncrementY = 0;
		
		//Puts the widget into the grid
		for(int i=0; i<numberOfElements; ++i) {
			//The args are now considered as vector<vector<string>(3 elements)>
			
			//First of all we have to update the constraints used to add the component to the gridbaglayout
			//Declaration of the constraints for the gridbaglayout
			//
			//You have to pay attention to the order because the increment of the property gridy depend of the previous heighty
			//
			constraints.gridx = 0;
			constraints.gridy += positionIncrementY;
			if(wc.get(i).equals(WidgetClasses.CONTROLEDLISTADDDEL)) {
				constraints.gridheight = 3;
				positionIncrementY = 3;
			} else {
				constraints.gridheight = 1;
				positionIncrementY = 1;
			}
			
			
			//Then add the first label retrieved from the vector
			add(new JLabel(args.get(i).get(0)), constraints);
			++constraints.gridx;
			
			//Then we add the word modifiable
			add(new JLabel("Modifiable "), constraints);
			++constraints.gridx;
			
			//Then comes the check box to indicate if the property is modifiable or not
			checkBoxes.add(new JCheckBox());
			add(checkBoxes.get(i), constraints);
			++constraints.gridx;
			
			//After we have the final label get from the argument list
			add(new JLabel(args.get(i).get(1)), constraints);
			++constraints.gridx;
			
			//And finally we have the widget
			constraints.weightx = 1;
			if(objectsArgs == null)
				//widgets.add(widgetFactory.createWidget(wc.get(i), this, new Object[]{}));
				widgets.add(GWidgetFactory.createWidget(wc.get(i), this, information, null));
			else
				widgets.add(GWidgetFactory.createWidget(wc.get(i), this, information, objectsArgs.get(i)));
			add(widgets.get(i), constraints);
			
		}
		
		validate();
	}
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
			case SENDTOWIDGET:
				if(message.getArguments() == null)
					return;
				
				//If there is less than two arguments return
				if(message.getArguments().size() < 2)
					return;
				
				//Otherwise we have to check that the first argument is an integer bounded between 0 and the number of widget. Otherwise we exit
				if((Integer)message.getArguments().get(0) < 0 || (Integer)message.getArguments().get(0) >= widgets.size())
					return;
				
				widgets.get((Integer)message.getArguments().get(0)).translateMessage((GMessage)message.getArguments().get(1));
				break;
				
			default:
				System.out.println("VisualFigaro : GWidgetGridForm : Message not handled");
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean loadXML(Vector<Element> elements, boolean deeplyRooted) {
		
		if(elements.size() != widgets.size())
			return false;
		
		//if(elements.size()==1)
		//	return false;

		//The size of elements
		int elementsSize = elements.size();
		
		//Creation of a small vector to store the data before calling loadXML
		Vector<Element> bufferVector = new Vector<Element>(1);
		Vector<Element> children = new Vector<Element>();
		
		//We load the XML part for each document
		for(int i=0; i<elementsSize; ++i) {
			
			//First we store the xml tag
			xmlTags.add(elements.get(i).getName());
			
			//Then we get the children
			children.addAll(elements.get(i).getChildren());
			
			if(children.size() > 0) {
				//There are two main parts in the children. The first one will be used to fill the check box and the second one will be passed to the loadXml function of the widget.
				checkBoxes.get(i).setSelected(children.get(0).getText().equals(information.getLanguage().getBDCTranslation("VRAI")));
			}
			
			//We fill the buffer for the widget
			for(int j=1; j<children.size(); ++j)
				bufferVector.add(children.get(j));
			
			//If there is no children under the element we have to give to the widget a dummy one
			//String key = /*GXMLLoader.getNamedChildrenInXSDName(elements.get(i)).get(1)*/"BONJOUR";
			
			System.err.println("Tag : " + xmlTags.get(i));
			
			widgets.get(i).loadXML(GXMLElementFactory.refactorElements(bufferVector, GXSDOperations.getNamedChildrenInXSDName(xmlTags.get(i)).get(1)), false);
			
			//Then we reset the vectors
			bufferVector.clear();
			children.clear();
		}
		
		return true;
	}
	
	public Vector<Element> saveXML() {
	
		//First we get the number of widget
		int numberOfWidgets = widgets.size();
System.err.println("xmlTags.size() : "+xmlTags.size());
		//The result vector
		Vector<Element> resultVector = new Vector<Element>(numberOfWidgets);
		
		//A buffer vector and a buffer element to store the data
		Element bufferElem;
		
		for(int i=0; i<numberOfWidgets; ++i) {
			
			//First we have to create a new element under which a line of the widget will be summarized
			resultVector.add(new Element(xmlTags.get(i)));
			
			//Then we create the modifiable part under the element
			bufferElem = new Element(information.getLanguage().getBDCTranslation("MODIFIABLE"));
			bufferElem.setText(checkBoxes.get(i).isSelected() ? information.getLanguage().getBDCTranslation("VRAI") : information.getLanguage().getBDCTranslation("FAUX"));
			//The last step for the modifiable element is to add it under the result element
			resultVector.get(i).addContent(bufferElem);
			
			//Then comes the turn of the widget in the concerned line
			resultVector.get(i).addContent(widgets.get(i).saveXML());
		}
		
		return resultVector;
	}
}
