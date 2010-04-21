package GNameRetriever;

import org.jdom.Element;

import GLanguage.GLanguage;

/**
 * Method retrieving the name from a NODE or a LINK object putting the tag "NODE :" or "LINK :" just in front of the name.
 * @author Guillaume Torrente & Marc Bouissou
 * @see GNameRetrieverMethod
 */
public class GNameRetrieverMethodTypeNodeLink extends GNameRetrieverMethod {
	
	public GNameRetrieverMethodTypeNodeLink(GLanguage language) {
		super(language);
	}
	
	/**
	 * The implemented method which actually retrieves the name of the type.
	 * @param e The name of the <code>Element</code> from which the name has to be retrieved.
	 * @return <code>String</code> containing the name of the type.
	 */
	public String retrieveName(Element e) {
		System.out.println("Execution Method Type Node Link");
		
		String name = "";
		
		//First we retrieve the name
		if(e.getChild(language.getBDCTranslation("NOM")) == null)
			name = "NoName";
		else
			name = e.getChildText(language.getBDCTranslation("NOM"));
		
		//Then we have to check if it is a node or a link
		if(e.getChild(language.getBDCTranslation("NOEUD")) != null)
			name = "NODE : " + name;
		else if(e.getChild(language.getBDCTranslation("LIEN")) != null)
			name = "LINK : " + name;
		else
			name = "UNKNOWN : " + name;
		
		return name;
	}
}
