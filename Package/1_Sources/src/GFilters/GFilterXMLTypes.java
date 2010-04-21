package GFilters;

import org.jdom.Element;
import org.jdom.filter.Filter;

import GLanguage.GLanguage;

/**
 * This filter matches the elements in the xml tree structure which have the characteristics of a type in the BDC format.
 * 
 * @author Torrente Guillaume & Marc Bouissou
 * @see FilterTypes
 */
public class GFilterXMLTypes implements Filter {

	private static final long serialVersionUID = 1L;
	/**
	 * @uml.property  name="language"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GLanguage language;
	
	/**
	 * The default constructor.
	 */
	public GFilterXMLTypes(GLanguage language) {
		this.language = language;
	}
	
	/**
	 * Function which indicates true if the arguments is a BDC type.
	 * @return True when the configuration has been found false otherwise.  
	 */
	public boolean matches(Object ob) {
		//First we check if the object is really an element
		if(!(ob instanceof Element))
			return false;
		
		//The object is an element cf previous test so we can safely cast it to Element
		Element element = (Element)ob;
		
		//Then comes the test
		if(element.getName().equals(language.getBDCTranslation("TYPE")))
			return true;
		
		return false;
	}
}
