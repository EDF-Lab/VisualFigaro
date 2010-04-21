package GNameRetriever;

import org.jdom.Element;

import GLanguage.GLanguage;

/**
 * Method retrieving the name from an ALGORITHM_OD object.
 * @author Guillaume Torrente & Marc Bouissou
 * @see GNameRetrieverMethod
 */
public class GNameRetrieverMethodAlgo extends GNameRetrieverMethod {
	
	public GNameRetrieverMethodAlgo(GLanguage language) {
		super(language);
	}
	
	/**
	 * The implemented method which actually retrieves the name of the type.
	 * @param e The name of the <code>Element</code> from which the name has to be retrieved.
	 * @return <code>String</code> containing the name of the type.
	 */
	public String retrieveName(Element e) {
		System.out.println("Execution Method Algo Name");
		if(e.getChild(language.getBDCTranslation("NOM_ALGO")) == null)
			return "Unknown";
		else
			return e.getChildText(language.getBDCTranslation("NOM_ALGO"));
	}
}
