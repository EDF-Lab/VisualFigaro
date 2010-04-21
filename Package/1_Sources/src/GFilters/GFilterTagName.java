package GFilters;

import org.jdom.Element;
import org.jdom.filter.Filter;

/**
 * This filter matches the elements in the xml tree structure which have a particular tag name.
 * 
 * @author Guillaume Torrente & Marc Bouissou
 * @see FilterTypes
 */
public class GFilterTagName implements Filter {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * @uml.property  name="tagName"
	 */
	private String tagName;
	
	/**
	 * This constructor is used to specify the tag name which will be searched in the xml tree.
	 * @param tn The name.
	 */
	public GFilterTagName(String tn) {
		tagName = tn;
	}
	
	/**
	 * Function which indicates true if the argument has the specific tag name.
	 * @return True when the configuration has been found false otherwise.  
	 */
	public boolean matches(Object ob) {
		//First we check if the object is really an element
		if(!(ob instanceof Element))
			return false;
		
		//The object is an element cf previous test so we can safely cast it to Element
		Element element = (Element)ob;
		
		//Then comes the test
		if(element.getName().equals(tagName))
			return true;
		
		return false;
	}
}