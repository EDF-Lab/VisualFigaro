package GFilters;

import org.jdom.Element;
import org.jdom.filter.Filter;


/**
 * This filter matches the elements in the xml tree structure which have a particular tag with a particular value.
 * 
 * @author Torrente Guillaume & Marc Bouissou
 * @see FilterTypes
 */
public class GFilterElementWithValue implements Filter {

	private static final long serialVersionUID = 1L;
	
	/**
	 * @uml.property  name="root"
	 */
	private String root;
	/**
	 * @uml.property  name="rootValue"
	 */
	private String rootValue;
	
	/**
	 * This constructor is used to specify the tag and value which will be searched in the xml tree.
	 * @param r The tag of the root.
	 * @param rv The value of the root's child.
	 */
	public GFilterElementWithValue(String r, String rv) {
		root = r;
		rootValue = rv;
	}
	
	/**
	 * Function which indicates true if the arguments has the specific tag and the specific child with the right child value.
	 * @return True when the configuration has been found false otherwise.  
	 */
	public boolean matches(Object ob) {
		//First we check if the object is really an element
		if(!(ob instanceof Element))
			return false;
		
		//The object is an element cf previous test so we can safely cast it to Element
		Element element = (Element)ob;
		
		//Then comes the test
		if(element.getName().equals(root))
			if(element.getText().equals(rootValue))
				return true;
		
		return false;
	}
}
