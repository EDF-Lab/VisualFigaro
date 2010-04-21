package GException;

import org.jdom.Element;

public class GExceptionSave extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * @uml.property  name="element"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Element element;
	
	public GExceptionSave(Element e) {
		element = e;
	}
	
	public String toString() {
		return "VisualFigaro : GExceptionSave : " + element.toString() + " has failed to save";
	}
}
