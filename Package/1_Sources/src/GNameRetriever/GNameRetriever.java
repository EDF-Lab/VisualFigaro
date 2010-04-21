package GNameRetriever;

import org.jdom.Element;

/**
 * This class use a particular method described by the interface <code>GNameRetrieverMethod</code> to retrieve the name from an xml object. In KB3 the tag in which is stored the name has different name and sometimes it even does not exist and the name of an object is the composite of some xml tags and xml tag values included in the object or its descendants. We have made the choice to create a method for each different
 * retrieving method and not an only one big and general to force the programmer to check what he is doing.  
 * @author Guillaume Torrente & Marc Bouissou
 * @see GNameRetrieverMethod
 */
public class GNameRetriever {
	
	//The method used by the current GNameRetriever to retrieve the name of the object
	/**
	 * @uml.property  name="methode"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GNameRetrieverMethod methode;
	
	/**
	 * The default constructor take a <code>GNameRetrieverMethod</code> as argument. It will be used to retrieve the name from the real object.
	 * @param met <code>GNameRetrieverMethod</code> used to retrieve the name.
	 */
	public GNameRetriever(GNameRetrieverMethod met) {
		methode = met;
	}
	
	/**
	 * Method which actually retrieves the name from the real object.
	 * @param e <code>Element</code> from which the name has to be retrieved.
	 * @return <code>String</code> containing the name of the type.
	 */
	public String retrieveName(Element e) {
		//We just call the Method method ;-)
		return methode.retrieveName(e);
	}
}
