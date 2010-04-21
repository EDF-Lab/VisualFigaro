package GWidget;

import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;


import org.jdom.Element;

import Factories.GWidgetFactory;
import global.WidgetClasses;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;

public class GWidgetGridForm extends GWidget {
	
	private static final long serialVersionUID = 1L;

	//Size of the grid in cells
	/**
	 * @uml.property  name="sizeX"
	 */
	private int sizeX;

	/**
	 * @uml.property  name="sizeY"
	 */
	private int sizeY;
	
	//List of all the widgets in this widget
	/**
	 * @uml.property  name="widgets"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="GWidget.GWidget"
	 */
	private Vector<GWidget> widgets;
	
	public GWidgetGridForm() {
		super();
		
		//Create an empty widget
		sizeX = sizeY = 0;
		widgets = new Vector<GWidget>();
		setLayout(new GridLayout(0,0,5,5));
	}
	
	public GWidgetGridForm(GObject p, GObjectInformation info, Vector<String> labelsName, int sx, int sy, Vector<WidgetClasses> wc, Vector<Vector<Object>> objectsArgs) {
		super(p, info);
		
		//Set the sizes
		sizeX = sx;
		sizeY = sy;
		
		//We have to check if the size on X is an even number. If not we fix it by adding 1
		if(sizeX % 2 != 0)
			sizeX += 1;
		
		//Create the widgets list
		widgets = new Vector<GWidget>();
				
		//Set the layout manager
		setLayout(new GridLayout(sizeY, sizeX, 5, 5));
				
		//Then we get the number of widgets and by the same occasion test if the lengths of all vectors  are the same otherwise we continue based on the shortest one
		int numberOfElements = labelsName.size();
		if(labelsName.size() != wc.size()) {
			System.err.println("VisualFigaro : GWidgetGridForm : The sizes of the vectors are not all the same");
			if(labelsName.size() > wc.size())
				numberOfElements = wc.size();
		}

		//Puts the widget into the grid
		for(int i=0; i<numberOfElements; ++i) {
			add(new JLabel((String)labelsName.get(i)));
			GObjectInformation widgetInformation = new GObjectInformation(information);
			widgetInformation.addStepToPath("" + i);
			if(objectsArgs == null) {
				//widgets.add(widgetFactory.createWidget(wc.get(i), this, new Object[]{}));
				widgets.add(GWidgetFactory.createWidget(wc.get(i), this, widgetInformation, null));
			} else {
				widgets.add(GWidgetFactory.createWidget(wc.get(i), this, widgetInformation, objectsArgs.get(i)));
			}
			add(widgets.get(i));
		}
		
		//If there is still some space in the gridpanel add dummy elements
		int remainingSpace = sizeX*sizeY - numberOfElements*2;
		for(int i=0; i<remainingSpace; i++)
			add(new JPanel());
		
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
				
				System.out.println("READY TO SEND TO WIDGET");
				
				widgets.get((Integer)message.getArguments().get(0)).translateMessage((GMessage)message.getArguments().get(1));
				
				System.out.println("SENT TO WIDGET");
				break;
				
			case NOTIFYCHANGE:
				parent.translateMessage(message);
				break;
				
			default:
				System.out.println("VisualFigaro : GWidgetGridForm : Message not handled");
		}
	}
	
	public boolean loadXML(Vector<Element> elements, boolean deeplyRooted) {
		
		if(elements.size() != widgets.size())
			return false;
	
		//The size of elements
		int elementsSize = elements.size();
		
		//Creation of a small vector to store the data before calling loadXML
		Vector<Element> bufferVector = new Vector<Element>(1);
		
		//We load the XML part for each document
		for(int i=0; i<elementsSize; ++i) {
			bufferVector.add(elements.get(i));
			widgets.get(i).loadXML(bufferVector, false);
			bufferVector.clear();
		}
		
		return true;
	}
	
	public Vector<Element> saveXML() {
	
		//First we get the number of widget
		int numberOfWidgets = widgets.size();
		
		Vector<Element> resultVector = new Vector<Element>(numberOfWidgets);
		
		for(int i=0; i<numberOfWidgets; ++i) {
			//###A modifier pour prendre en compte les listes
			if(widgets.get(i).saveXML() != null)
				resultVector.add(widgets.get(i).saveXML().get(0));
		}
		
		return resultVector;
	}
}
