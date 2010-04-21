package GWidget;


import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

import global.Messages;
import global.NameRetrieverClasses;
import Factories.GNameRetrieverFactory;
import GMessage.GMessage;
import GNameRetriever.GNameRetriever;
import GObject.GObject;
import GObjectInformation.GObjectInformation;

public class GWidgetListComplexArray extends GWidgetList {	

	private static final long serialVersionUID = 1L;

	//Boolean to keep a trace if the widget has been initialized with a real XML tree or just the schema
	//boolean schemaOnly;
	
	//The array to store the complex XML elements
	/**
	 * @uml.property  name="xmlValues"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="org.jdom.Element"
	 */
	Vector<Element> xmlValues;
	
	public GWidgetListComplexArray() {
		super();
		
		xmlValues = new Vector<Element>();
	}
	
	public GWidgetListComplexArray(GObject p, GObjectInformation info, String tag, GNameRetriever n) {
		super(p, info, n);
		
		xmlValues = new Vector<Element>();
	}
	
	public boolean listAddElement(Element e) {
		
		//First we check if the list has been initialized with strings. In this case we cannot add an element
		if(initializedWithStrings)
			return false;
		
		//Then we check that the element which has to be inserted is not null
		if(e == null)
			return false;
	
		//If the element is not empty then it will be added to the list
		listModel.addElement(nameRetriever.retrieveName(e));
		xmlValues.add(e);
		return true;
	}
	
	public boolean listReplaceElement(int position, Element e) {
		
		//First we check that the element which has to be inserted is not null
		if(e == null)
			return false;
		
		//We have to distinguish two different types of behaviour
		if(initializedWithStrings) {
			
			//If there is a strange position we exit with false
			if(position >= listModel.size()) {
				return false;
			
			//Otherwise we replace the element
			} else {			
				listModel.setElementAt(nameRetriever.retrieveName(e), position);
				if(listModel.size()==xmlValues.size()){
					xmlValues.setElementAt(e, position);
				}
				else
				{
					boolean found =false;
					for(int i=0; i<xmlValues.size();i++)
					{
						String s1=xmlValues.get(i).getChildText(information.getLanguage().getBDCTranslation("NOM"));
						String s2= e.getChildText(information.getLanguage().getBDCTranslation("NOM"));
						
						if(s1.equals(s2))
						{
							xmlValues.setElementAt(e, i);
							found = true;
							break;
						}
					}
					if(!found){
						xmlValues.add(e);
					}
				}
			}
			
		} else {
			//If the element is not empty then it will replace the element already present in the list
			if(position >= xmlValues.size()) {
				//Add the element
				listModel.addElement(nameRetriever.retrieveName(e));
				xmlValues.add(e);
			} else {
				if(position < 0)
					position = 0;
				//Replace the element
				listModel.setElementAt(nameRetriever.retrieveName(e), position);
				xmlValues.setElementAt(e, position);
			}
		}

		list.repaint();
		list.revalidate();
		return true;
	}
	
public boolean listReplaceElementBis(int position, Element e) {
		
		//First we check that the element which has to be inserted is not null
		if(e == null)
			return false;
		
		//We have to distinguish two different types of behaviour
		if(initializedWithStrings) {
			
			//If there is a strange position we exit with false
			if(position >= listModel.size()) {
				return false;
			
			//Otherwise we replace the element
			} else {					
				listModel.setElementAt(e.getName(), position);
				if(listModel.size()==xmlValues.size()){
					xmlValues.setElementAt(e, position);
				}
				else
				{
					boolean found =false;
					for(int i=0; i<xmlValues.size();i++)
					{
						if(xmlValues.get(i).getChildText("NOM").equals(e.getChildText("NOM")))
						{
							xmlValues.setElementAt(e, i);
							found = true;
							break;
						}
					}
					if(!found){
						xmlValues.add(e);
					}
				}
			}
			
		} else {
			//If the element is not empty then it will replace the element already present in the list
			if(position >= xmlValues.size()) {
				//Add the element
				listModel.addElement(nameRetriever.retrieveName(e));
				xmlValues.add(e);
			} else {
				if(position < 0)
					position = 0;
				//Replace the element
				listModel.setElementAt(nameRetriever.retrieveName(e), position);
				xmlValues.setElementAt(e, position);
			}
		}

		list.repaint();
		list.revalidate();
		return true;
	}
	
	public boolean listDeleteElement(int position) {
		
		//First we check if the list has been initialized with strings. In this case we cannot delete an element
		if(initializedWithStrings)
			return false;
		
		//Test if there is something to remove
		if(xmlValues.size() == 0)
			return true;
		
		//Rebound the position to get an existing index
		if(position < 0)
			position = 0;
		if(position >= xmlValues.size())
			position = xmlValues.size() - 1;
		
		//Remove the element
		listModel.remove(position);
		xmlValues.remove(position);

		return true;
	}
	
	public boolean DeleteXMLvalues(String name) {
		
		//Test if there is something to remove
		if(xmlValues.size() == 0)
			return true;
		
		//Remove the element

		Element element;
		int position;

		for(position=0; position<xmlValues.size(); position++){
			element= xmlValues.get(position);

			if(element.getChildText("NOM").equals(name)){
				xmlValues.remove(position);
			}
		}

		return true;
	}
	
	public Element listGetElement(int position) { 
	
		//Then we have to check if the list has been initialized with strings or not and trigger the appropriate behavior
		if(initializedWithStrings)
			if(position > xmlValues.size())
				return null;
			
		//Rebound the position to get an existing index
		if(position < 0)
			position = 0;

		if(position >= xmlValues.size())
			position = xmlValues.size() - 1;
		
		if(xmlValues.size()==0)
			return null;

		return xmlValues.get(position);
	}
	
	public int listGetElement(String name) { 
		Element element;
		int position;

		for(position=0; position<xmlValues.size(); position++){
			element= xmlValues.get(position);

			if(element.getChildText(information.getLanguage().getBDCTranslation("NOM")).equals(name)){
				return position;
			}
		}

		return xmlValues.size()+1;
	}
	
	public boolean listMoveUp() {
		
		//Retrieve the index from the list
		int selectedIndex = list.getSelectedIndex();
		
		//If it 0 or less do nothing
		if(selectedIndex <= 0)
			return true;
		
		//Otherwise move the component up by removing it from its position and replacing it one place upper
		//Element bufferElement = xmlValues.remove(selectedIndex);
		String bufferName = (String)listModel.remove(selectedIndex);
		//xmlValues.add(selectedIndex-1, bufferElement);
		listModel.add(selectedIndex-1, bufferName);
		list.setSelectedIndex(selectedIndex-1);
		
		return true;
	}
	
	public boolean listMoveDown() {
		
		//Retrieve the index from the list
		int selectedIndex = list.getSelectedIndex();
		
		//If it 0 or less do nothing
		if(selectedIndex >= this.listGetNumberOfElement()-1 )
			return true;
		
		//Otherwise move the component up by removing it from its position and replacing it one place upper
		//Element bufferElement = xmlValues.remove(selectedIndex);
		String bufferName = (String)listModel.remove(selectedIndex);
		//xmlValues.add(selectedIndex+1, bufferElement);
		listModel.add(selectedIndex+1, bufferName);
		list.setSelectedIndex(selectedIndex+1);
		
		return true;
	}
	
	public int listGetSelectedIndex() {
		return list.getSelectedIndex();
	}
	
	public boolean loadXML(Element e) {
		
		//Test if the element is not null. If it is the case then exit.
		if(e == null)
			return false;
		
		//Add the element to the end of the two lists
		listModel.addElement(nameRetriever.retrieveName(e));
		xmlValues.add(e);
		
		//We notify the parent that a string has been added
		parent.translateMessage(new GMessage(information, Messages.NOTIFYCHANGE, "ADD"));
		
		return true;
	}
	
	public boolean loadXML(Vector<Element> elements, boolean deeplyRooted) {

		//First we check if it is not a dummy value
		if(elements == null)
			return false;
		
		if(elements.size() == 0)
			return true;
		
		//Then we have to study two cases. The first one is when a list has been initialized with strings, the second one is for an usual list which has not been initialized
		if(initializedWithStrings) {		
		
			//We need the list of all the strings in the list model
			Object[] listModelItemsArray = listModel.toArray();
			listModel.removeAllElements();
			Vector<String> listModelItemsVector = new Vector<String>();
			for(int i=0; i<listModelItemsArray.length; i++)
				listModelItemsVector.add(listModelItemsArray[i].toString());
			
			//We need two different nameretrievers. The first one with just the name to compare the xml element name and the string and the second one with the advanced xml name retrieved from the xml structure 
			GNameRetriever simpleNameRetriever = GNameRetrieverFactory.createNameRetriever(information.getLanguage(), NameRetrieverClasses.NAMERETRIEVER);
			
			//We will put all the strings which has an xml in the top of the others respecting the order in the xml argument
			for(int i = 0; i<elements.size(); ++i) {
				
				if(listModelItemsVector.contains(simpleNameRetriever.retrieveName(elements.get(i)))) {
					
					listModelItemsVector.removeElement(simpleNameRetriever.retrieveName(elements.get(i)));
					listModel.addElement(nameRetriever.retrieveName(elements.get(i)));
					xmlValues.add(elements.get(i));
					
					//We notify the parent that a string has been added
					parent.translateMessage(new GMessage(information, Messages.NOTIFYCHANGE, "ADD"));
				} else {
					System.out.println("VisualFigaro : GWidgetListComplexArray : BDC type name not present in Figaro");
				}
			}
			
			//Then we just add the remaining elements in the listmodel
			for(Iterator<String> iter = listModelItemsVector.iterator(); iter.hasNext();) {
				listModel.addElement(iter.next());
				xmlValues.add(null);
				
				//We notify the parent that a string has been added
				parent.translateMessage(new GMessage(information, Messages.NOTIFYCHANGE, "ADD"));
			}
			
		} else {
			if(elements.get(0).getAttribute("dummy") == null) {
				//Call the default function as many time as necessary
				for(Iterator<Element> iter = elements.iterator(); iter.hasNext();)
					loadXML(iter.next());
			}
		}
		
		while(xmlValues.removeElement(null));
		
		return true;
	}
	
	public Vector<Element> saveXML() {
		
		//First we detach the elements
		for(Element element : xmlValues)
			if(element != null)
				element.detach();

		while(xmlValues.removeElement(null));
		
		//Then we simply return the vector
		return xmlValues;
	}
}
