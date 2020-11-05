package GXSDOperations;


import jEditInterface.VisualFigaro;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.gjt.sp.jedit.jEdit;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import GFilters.GFilterParametrized;
import GFilters.GFilterTagName;
import GFilters.GFilterXSDChild;
import GXMLLoader.GXMLLoaderDefaultFiles;

/**
 * This class performs the manipulations on the XSD like retrieving the allowed values for an element or retrieving the descendants or children of an elements. Almost all of the data and methods are statics because the XSD is common to every components.
 * @author Guillaume Torrente & Marc Bouissou
 *
 */
public class GXSDOperations {
	
	//The XSD document used
	private static Document document;
	
	//The language used
	private static String language;
	
	/**
	 * Methods used to switch between the differents XSD schema in different languages.
	 * @param l The name of the language which has to be used.
	 */
	public static void setLanguage(String newLanguage) {
		
		//Initialize the default variables
		language = newLanguage;
		
		//The xmlLoader to retrieve the name of the xsd file to load
		GXMLLoaderDefaultFiles xlDefaultFiles = new GXMLLoaderDefaultFiles();
		
		//If the language is not available just return
		if(!xlDefaultFiles.isAvailableLanguage(language))
			return;
		
		String xsdFileName;
		
		if (System.getProperty("os.name").contains("Windows"))
		//If it is available we have to retrieve the name of the xsd file
			xsdFileName = jEdit.getJEditHome() + "/VisualFigaro/" + "\\" + xlDefaultFiles.getSchemaFilenameForLanguage(language);
		else
			xsdFileName = jEdit.getJEditHome() + "/VisualFigaro/" + xlDefaultFiles.getSchemaFilenameForLanguage(language);
		
		//SAXBuilder instance creation
		SAXBuilder sxb = new SAXBuilder();
		try
		{
			//Creation of a new JDOM document with the XML file as argument
			document = sxb.build(new File(xsdFileName));
		}
		catch(Exception e){
			System.out.println("VisualFigaro : GXSDOperations : The xsd file " + xsdFileName + " cannot be load.");
		}
	}
	
	
	/**
	 * Return the element which has the attribute "attr" with value "value" in the current document.
	 * @param attr Name of the attribute.
	 * @param value Value of the attribute.
	 * @return Element concerned.
	 */
	@SuppressWarnings("unchecked")
	public static Element getElement(String attr, String value) {

		Element retVal = null;
		
		if(document == null)
			System.err.println("VisualFigaro : GXSDOperations : XSD not found");

		//Launch the filter above all the XML tree
		GFilterParametrized filter = new GFilterParametrized(attr, value);
		Iterator<Element> iter = document.getRootElement().getDescendants(filter);
		
		// There can be other objects with the same name. We are looking for an element.
		while( iter.hasNext() )
		{
			Element current = iter.next();
			if( current.getName() == "element")
			{
				retVal = current;
			}
		}
		
		return retVal;
	}
	
	/**
	 * Get the first child which has a name in the sense that it is referenced by the property <code>ref</code> in the descendants list of the father.
	 * @param name Name of the father.
	 * @return The first named element or <code>null</code> if it does not exist.
	 */
	@SuppressWarnings("unchecked")
	public static Element getFirstNamedChildInXSD(String name) {
		
		//First we retrieve the element from the bdc
		Element element = getElement("name", name);
		
		//The filter
		GFilterParametrized filter = new GFilterParametrized("ref", "");
		
		//The list of all the elements
		Iterator<Element> iter = element.getDescendants(filter);
	
		//If there is no element we return null
		if(iter.hasNext() == false)
			return null;
		
		//Else return the first element
		return getElement("name", iter.next().getAttributeValue("ref"));
	}
	
	/**
	 * Get the all the children which have a name in the sense that they are referenced by the property <code>ref</code> in the descendants list of the father.
	 * @param name Name of the father.
	 * @return The <code>Vector</code> of all named <code>Element</code> or <code>null</code> if no one does exist.
	 */
	@SuppressWarnings("unchecked")
	public static Vector<Element> getNamedChildrenInXSD(String name) {
		
		//First we retrieve the element from the bdc
		Element element = getElement("name", name);
		
		//The filter
		GFilterParametrized filter = new GFilterParametrized("ref", "");
		
		//The list of all the elements
		Iterator<Element> iter = element.getDescendants(filter);
		
		//If there is not element we return null
		if(iter.hasNext() == false)
			return null;

		//Return the vector of all children
		Vector<Element> result = new Vector<Element>();
		for(; iter.hasNext(); )
			result.add(getElement("name", iter.next().getAttributeValue("ref")));
		
		return result;
	}
	
	/**
	 * Get the name of the first child which has a name in the sense that it is referenced by the property <code>ref</code> in the descendants list of the father.
	 * @param name Name of the father.
	 * @return The name of the first named element or <code>""</code> if it does not exist.
	 */
	public static String getFirstNamedChildInXSDName(String name) {
		
		//Retrieve the first child under e
		Element firstChild = getFirstNamedChildInXSD(name);
		
		//If there is not such child return the empty string ""
		if(firstChild == null)
			return "";
		
		//Else return its name (we are sure there is a name because it is the parameter used to recognize a child
		return firstChild.getAttributeValue("name");
	}
	
	/**
	 * Get the name of all the children which have a name in the sense that they are referenced by the property <code>ref</code> in the descendants list of the father.
	 * @param name Name of the father
	 * @return The <code>Vector</code> of all the name of the <code>Element</code> or the empty <code>Vector</code> if no one does exist.
	 */
	public static Vector<String> getNamedChildrenInXSDName(String name) {

		//Retrieve all the children

		Vector<Element> children = getNamedChildrenInXSD(name);
		
		
		Vector<String> result = new Vector<String>();


		for(Iterator<Element> iter = children.iterator(); iter.hasNext();)
			result.add(iter.next().getAttributeValue("name"));
		
		//Else return its name (we are sure there is a name because it is the parameter used to recognize a child
		return result;
	}
	
	/**
	 * Get all the allowed values for a specific node.
	 * @param name Name of the node.
	 * @return The <code>Vector</code> of <code>String</code> of all the allowed values.
	 */
	@SuppressWarnings("unchecked")
	public static Vector<String> getAllowedValuesForNode(String name) {
		
		System.err.println("Le langage est : " + language + " pour " + name);
		
		Vector<String> result = new Vector<String>();
		
		//First we have to find the element in the xsd
		Element element = getElement("name", name);
		
		//Then we find the element restriction (if any)
		Iterator<Element> iter = element.getDescendants(new GFilterTagName("restriction"));

		//If their is no such element we return the empty vector
		if(!iter.hasNext())
			return result;
		
		//Otherwise we have to find the children of restriction node
		iter = iter.next().getDescendants(new GFilterTagName("enumeration"));
		
		//If their is no such element we return the empty vector. It means that someone have to review the schema
		if(!iter.hasNext())
			return result;
		
		for(;iter.hasNext();)
			result.add(iter.next().getAttributeValue("value"));
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static int getMinimumCardinality(String rootName, String childName) {
		
		//First we have to retrieve the root element
		Element rootElement = getElement("name", rootName);
		
		//If there is no such element just return -1
		if(rootElement == null)
			return -1;
		
		//Otherwise we have to retrieve the child named "childName"
		Iterator<Element> childIterator = rootElement.getDescendants(new GFilterXSDChild(childName));
		
		//If there is no such child just return -1
		if(!childIterator.hasNext())
			return -1;
		
		//Then we look for the attribute minOccur and return its value. If there is no such attribute return 0.
		Element childElement = childIterator.next();
		if(childElement.getAttribute("minOccurs") != null)
			return Integer.parseInt(childElement.getAttributeValue("minOccurs"));
		else
			return 1;
	}
}
