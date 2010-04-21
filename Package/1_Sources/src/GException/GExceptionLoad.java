package GException;

import org.jdom.Element;

public class GExceptionLoad extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * @uml.property  name="element"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Element element;
	
	public GExceptionLoad(Element e) {
		element = e;
	}
	
	public String toString() {
		return "VisualFigaro : GExceptionLoad : " + element.toString() + " has failed to load";
	}
}
