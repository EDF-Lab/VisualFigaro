package GException;

import org.jdom.Element;

public class GExceptionElement extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * @uml.property  name="element"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Element element;
	/**
	 * @uml.property  name="xmlKeyword"
	 */
	private String xmlKeyword;
	
	public GExceptionElement(Element e, String xk) {
		element = e;
		xmlKeyword = xk;
	}
	
	public String toString() {
		return "VisualFigaro : GExceptionElement : " + element.toString() + " is matched against " + xmlKeyword;
	}
}
