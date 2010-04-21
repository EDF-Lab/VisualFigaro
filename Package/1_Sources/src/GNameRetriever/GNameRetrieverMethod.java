package GNameRetriever;

import org.jdom.Element;

import GLanguage.GLanguage;

/**
 * This interface is used as prototype for all the method which retrieve a name from a type.
 * @author Guillaume Torrente & Marc Bouissou
 * @see GNameRetriever
 */
public abstract class GNameRetrieverMethod {
	
	//The language used in the KB
	/**
	 * @uml.property  name="language"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected GLanguage language;
	
	protected GNameRetrieverMethod(GLanguage language) {
		this.language = language;
	}
	
	/**
	 * Method which has to be implemented by all the GNameRetrieverMethod's
	 * @param e The name of the <code>Element</code> from which the name has to be retreived
	 * @return <code>String</code> containing the name of the type.
	 */
	public abstract String retrieveName(Element e);
}