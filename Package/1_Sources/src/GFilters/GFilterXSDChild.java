package GFilters;

import org.jdom.Element;
import org.jdom.filter.Filter;

public class GFilterXSDChild implements Filter {

	private static final long serialVersionUID = 1L;
	
	/**
	 * @uml.property  name="childName"
	 */
	private String childName;
	
	/**
	 * This constructor is used to specify the name of the child which has to be retrieved.
	 * @param r The tag of the root.
	 * @param rv The value of the root's child.
	 */
	public GFilterXSDChild(String childName) {
		this.childName = childName;
	}
	
	/**
	 * Function which indicates true if the arguments has the specific tag and the specific child with the right child value.
	 * @return True when the configuration has been found false otherwise.  
	 */
	public boolean matches(Object ob) {
		//First we check if the object is really an element
		if(!(ob instanceof Element))
			return false;
		
		//The object is an element of previous test so we can safely cast it to Element
		Element element = (Element)ob;
		
		//Then comes the test
		if(element.getAttribute("name") != null)
			if(element.getAttributeValue("name").equals(childName))
				return true;
			else
				return false;
		else if(element.getAttribute("ref") != null)
			if(element.getAttributeValue("ref").equals(childName))
				return true;
			else
				return false;
		
		return false;
	}
}
