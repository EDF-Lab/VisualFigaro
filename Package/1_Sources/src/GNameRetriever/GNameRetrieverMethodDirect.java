package GNameRetriever;

import org.jdom.Element;

import GLanguage.GLanguage;

/**
 * Method retrieving the name directly from the tag of an object.
 * @author Guillaume Torrente & Marc Bouissou
 * @see GNameRetrieverMethod
 */
public class GNameRetrieverMethodDirect extends GNameRetrieverMethod {
	
	public GNameRetrieverMethodDirect(GLanguage language) {
		super(language);
	}
	
	/**
	 * The implemented method which actually retrieves the name of the type.
	 * @param e The name of the <code>Element</code> from which the name has to be retrieved.
	 * @return <code>String</code> containing the name of the type.
	 */
	public String retrieveName(Element e) {
		System.out.println("Execution Method Direct");
		if(e == null)
			return "Unknown";
		else
			return e.getName();
	}
}
