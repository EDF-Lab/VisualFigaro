package GFilters;

import org.jdom.Element;
import org.jdom.filter.Filter;

/**
 * This filter matches the elements in the xml tree structure which have a particular attribute and a particular value for this attribute.
 * 
 * @author Guillaume Torrente & Marc Bouissou
 * @see FilterTypes
 */
public class GFilterParametrized implements Filter {
		
	private static final long serialVersionUID = 1L;
	
	/**
	 * @uml.property  name="attribute"
	 */
	private String attribute;
	/**
	 * @uml.property  name="value"
	 */
	private String value;
	
	/**
	 * This constructor is used to specify the attribute and the attribute's value which will be searched in the xml tree.
	 * @param attr The attribute.
	 * @param val The value of the attribute.
	 */
	public GFilterParametrized(String attr, String val) {
		attribute = attr;
		value = val;
	}

	/**
	 * Function which indicates true if the argument has the specific attribute with the specified value.
	 * @return True when the configuration has been found false otherwise.  
	 */
	public boolean matches(Object ob) {
		//First we check if the object is really an element
		if(!(ob instanceof Element))
			return false;
		
		//The object is an element cf previous test so we can safely cast it to Element
		Element element = (Element)ob;
		
		//Then comes the test
		if(element.getAttributeValue(attribute) != null) {
			if(element.getAttributeValue(attribute).equals(value) || value.equals(""))
				return true;
		}
		
		return false;
	}

}
