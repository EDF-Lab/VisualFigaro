package GWidget;


import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

import GNameRetriever.GNameRetriever;
import GObject.GObject;
import GObjectInformation.GObjectInformation;

public class GWidgetListSimpleArray extends GWidgetList {
	private static final long serialVersionUID = 1L;

	//String to keep the name of the xml elements we have to return
	/**
	 * @uml.property  name="tagName"
	 */
	private String tagName;
	
	public GWidgetListSimpleArray() {
		super();
	}
	
	public GWidgetListSimpleArray(GObject p, GObjectInformation info, String tag, GNameRetriever n) {
		super(p, info, n);
	}
	
	public boolean listAddElement(Element e) {
		
		//First we check that the element which has to be inserted is not null
		if(e == null)
			return false;
	
		//If the element is not empty then it will be added to the list
		listModel.addElement(nameRetriever.retrieveName(e));
		
		return true;
	}
	
	public boolean listReplaceElement(int position, Element e) {
		
		//First we check that the element which has to be inserted is not null
		if(e == null)
			return false;
		
		//If the element is not empty then it will replace the element already present in the list
		if(position >= listModel.getSize()) {
			//Add the element
			listModel.addElement(nameRetriever.retrieveName(e));
		} else {
			if(position < 0)
				position = 0;
			//Replace the element
			listModel.setElementAt(nameRetriever.retrieveName(e), position);
		}
		
		return true;
	}
	
	public boolean listReplaceElementBis(int position, Element e) {
		
		//First we check that the element which has to be inserted is not null
		if(e == null)
			return false;
		
		//If the element is not empty then it will replace the element already present in the list
		if(position >= listModel.getSize()) {
			//Add the element
			listModel.addElement(nameRetriever.retrieveName(e));
		} else {
			if(position < 0)
				position = 0;
			//Replace the element
			listModel.setElementAt(e.getName(), position);
		}
		
		return true;
	}
	
	public boolean listDeleteElement(int position) {
		
		//Test if there is something to remove
		if(listModel.getSize() == 0)
			return true;
		
		//Rebound the position to get an existing index
		if(position < 0)
			position = 0;
		if(position >= listModel.getSize())
			position = listModel.getSize() - 1;
		
		//Remove the element
		listModel.remove(position);
		
		return true;
	}
	
	public boolean DeleteXMLvalues(String name) {
		System.err.println("ERROR");
		return true;
	}
	
	public Element listGetElement(int position) {
		
		//Rebound the position to get an existing index
		if(position < 0)
			position = 0;
		if(position >= listModel.getSize())
			position = listModel.getSize() - 1; 
		
		return new Element(listModel.get(position).toString());
	}
	
	public int listGetElement(String name) { 
		return -1;
	}
	
	public boolean listMoveUp() {
		
		//Retrieve the index from the list
		int selectedIndex = list.getSelectedIndex();
		
		//If it 0 or less do nothing
		if(selectedIndex <= 0)
			return true;
		
		//Otherwise move the component up by removing it from its position and replacing it one place upper
		String bufferName = (String)listModel.remove(selectedIndex);
		listModel.add(selectedIndex-1, bufferName);
		list.setSelectedIndex(selectedIndex-1);
		
		return true;
	}
	
	public boolean listMoveDown() {
		
		//Retrieve the index from the list
		int selectedIndex = list.getSelectedIndex();
		
		//If it 0 or less do nothing
		if(selectedIndex >= listModel.getSize()-1 )
			return true;
		
		//Otherwise move the component up by removing it from its position and replacing it one place upper
		String bufferName = (String)listModel.remove(selectedIndex);
		listModel.add(selectedIndex+1, bufferName);
		list.setSelectedIndex(selectedIndex+1);
		
		return true;
	}
	
	public int listGetSelectedIndex() {
		return list.getSelectedIndex();
	}
	
	private boolean loadXML(Element e) {
		
		//Test if the element is not null. If it is the case then exit.
		if(e == null)
			return false;
		
		//Add the element to the end of the two lists
		listModel.addElement(nameRetriever.retrieveName(e));
		
		return true;
	}
	
	public boolean loadXML(Vector<Element> elements, boolean deeplyRooted) {

		//First we check if it is not a dummy value
		if(elements == null)
			return false;
		
		if(elements.size() == 0)
			return true;
		
		//If the element is really an xml data we retrieve the name of the tag
		tagName = elements.get(0).getName();
		
		if(elements.get(0).getAttribute("dummy") == null) {
			
			//Call the default function as many time as necessary
			for(Iterator<Element> iter = elements.iterator(); iter.hasNext();)
				loadXML(iter.next());
		}
		
		return true;
	}
	
	public Vector<Element> saveXML() {
		
		//We create the result vector and a buffer element
		Vector<Element> result = new Vector<Element>();
		Element elem;
		
		//We get the number of elements in the list
		int numberOfElements = listModel.getSize();
		
		//For each element in the list we create an element with the string attached
		for(int i=0; i<numberOfElements; ++i) {
			elem = new Element(tagName);
			elem.setText(listModel.get(i).toString());
			result.add(elem);
		}
		
		return result; 
	}
}
