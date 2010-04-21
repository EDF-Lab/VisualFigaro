package GFilters;

import java.util.Vector;

import org.jdom.Element;
import org.jdom.filter.Filter;


/**
 * This filter matches the elements in the xml tree structure which have  a particular tag with a particular subtag with a particular value.
 * 
 * @author Torrente Guillaume & Marc Bouissou
 * @see FilterTypes
 */
public class GFilterElementWithSubElementWithValue implements Filter {

	private static final long serialVersionUID = 1L;
	
	/**
	 * @uml.property  name="root"
	 */
	private String root;
	/**
	 * @uml.property  name="subRoot"
	 */
	private String subRoot;
	/**
	 * @uml.property  name="subRootValue"
	 */
	private String subRootValue;
	
	/**
	 * This constructor is used to specify the tag, subtag and value which will be searched in the xml tree.
	 * @param r The tag of the root.
	 * @param sr The tag of the root's child.
	 * @param srv The value of the root's child.
	 */
	public GFilterElementWithSubElementWithValue(String r, String sr, String srv) {
		root = r;
		subRoot = sr;
		subRootValue = srv;
	}
	
	/**
	 * Function which indicates true if the arguments has the specific tag and the specific child with the right child value.
	 * @return True when the configuration has been found false otherwise.  
	 */
	@SuppressWarnings("unchecked")
	public boolean matches(Object ob) {
		//First we check if the object is really an element
		if(!(ob instanceof Element))
			return false;
		
		//The object is an element cf previous test so we can safely cast it to Element
		Element element = (Element)ob;
		
		//Then comes the test
		if(element.getName().equals(root))
		{
			Vector<Element> childs = new Vector<Element>(element.getChildren(subRoot));
			if(childs.size() > 0)
			{
				for(Element child : childs)
					if(child.getText().equals(subRootValue))
						return true;
			}
		}
		
		return false;
	}
}
