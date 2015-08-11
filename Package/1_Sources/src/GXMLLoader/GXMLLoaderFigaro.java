/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 22 June 2015                            
 * Author       : L.RAFFAELLI/ALL4TEC                              
 * Bug Id       : N°84 
 * Modification : Taken in account of Figp evolution for managing GROUPS
 * VF version   : 2.0
 * **************************************************************/

package GXMLLoader;

import global.FilterTypes;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.Filter;
import org.jdom.input.SAXBuilder;

import GFilters.GFilterElementWithSubElementWithValue;
import GFilters.GFilterTagName;

/**
 * This class performs operations on xml files and xml trees. It is specialized and has high level methods to parse Figaro like xml trees. There is another class called <code>GXMLLoader</code> which does almost the same thing but for BDC like xml trees.
 * @author Guillaume Torrente & Marc Bouissou
 * @see GXMLLoader 
 */
public class GXMLLoaderFigaro {
	
	//Document to be parsed and what will be considered as its root
	/**
	 * @uml.property  name="document"
	 * @uml.associationEnd  
	 */
	private Document document;
	/**
	 * @uml.property  name="root"
	 * @uml.associationEnd  
	 */
	private Element root;
	
	
	/**
	 * Load the XML file stored in "path" using JDOM
	 * @param path
	 * @return org.jdom.Document
	 */
	public Document loadXmlFileJDOM(String path) {
		//On crée une instance de SAXBuilder
		//SAXBuilder instance creation
		SAXBuilder sxb = new SAXBuilder();
		try
		{
			//On crée un nouveau document JDOM avec en argument le fichier XML
			//Creation of a new JDOM document with the XML file as argument
			//Le parsing est terminé ;)
			//Parsing is finished ;)
			document = sxb.build(new File(path));
		}
		catch(Exception e){
			System.out.println("Erreur lors du chargement du fichier suivant : " + path);
			return null;
		}

		return document;
	}
	
	/**
	 * Change the current xml file used for parsing to a new one
	 * @param doc New file which has to be used during parsing
	 * @return Boolean true if everything was fine false otherwise
	 */
	public boolean setXmlFile(Document doc) {
		
		//We update the values
		this.document = doc;
		this.root = doc.getRootElement();
		
		return true;
	}
	
	/**
	 * Find the fields "step" in the KB and return their name.
	 * @return <code>Vector</code> of <code>String</code> containing all the names of the steps.
	 */
	@SuppressWarnings("unchecked")
	public Vector<String> findSteps() {
		
		//The result vector
		Vector<String> result = new Vector<String>();
		
		//And a buffer vector to contain the specified Elements
		Vector<Element> elements = new Vector<Element>();
		
		//We find all the elements related to steps
		elements.addAll(root.getContent(createFilter(FilterTypes.STEP)));
		
		//And translate them into strings
		for(Iterator<Element> iter = elements.iterator(); iter.hasNext();)
			result.add(iter.next().getChildText("NAME"));
		
		return result;
	}

	/**
	 * Find the fields "group" in the KB and return their name.
	 * @return <code>Vector</code> of <code>String</code> containing all the name of the groups.
	 */
	@SuppressWarnings("unchecked")
	public Vector<String> findGroups() {
		
		//The result vector
		Vector<String> result = new Vector<String>();
		
		//And a buffer vector to contain the specified Elements
		Vector<Element> elements = new Vector<Element>();
		
		//We find all the elements related to steps
		elements.addAll(root.getContent(createFilter(FilterTypes.GROUP)));
		
		//And translate them into strings
		for(Iterator<Element> iter = elements.iterator(); iter.hasNext();)
			result.add(iter.next().getChildText("NAME"));
		
		return result;
	}
	
	/**
	 * Find the types in the KB and return them.
	 * @return <code>Vector</code> of <code>Element</code> containing all the types.
	 */
	@SuppressWarnings("unchecked")
	public Vector<Element> findTypes() {
		return new Vector<Element>(root.getContent(createFilter(FilterTypes.TYPE)));
	}

	/**
	 * Find a particular type in the KB and return it.
	 * @return <code>Element</code> containing the type.
	 */
	@SuppressWarnings("unchecked")
	public Element findType(String name) {
		
		Vector<Element> possibleTypes = new Vector<Element>(root.getContent(new GFilterElementWithSubElementWithValue("CLASS", "NAME", name)));
		
		if(possibleTypes.size() > 0)
			return possibleTypes.get(0);
		else
			return null;
	}
	
	/**
	 * Find the types in the KB and return their names.
	 * @return <code>Vector</code> of <code>String</code> containing all the names of the types.
	 */
	public Vector<String> findTypesName() {
		
		//The vector for the result
		Vector<String> result = new Vector<String>();
		
		//First we have to find all the types using the findTypes method
		Vector<Element> types = findTypes();
		
		//Then we just have to go trough the vector and get the name of the type
		for(Iterator<Element> iter = types.iterator(); iter.hasNext(); )
			result.add(iter.next().getChildText("NAME"));
		
		return result;
	}
	
	/**
	 * Find the fathers of a particular type in the KB and return their names.
	 * @param name Name of the type
	 * @return <code>Vector</code> of <code>String</code> containing the names of all the fathers of the type.
	 */
	@SuppressWarnings("unchecked")
	public Vector<String> findFathers(String name) {
		
		//The result vector
		Vector<String> result = new Vector<String>();
		
		//First we have to retrieve the type
		Element typeElement = findType(name);
		
		//If there is at least one type called name then return continue the process
		if(typeElement != null) {
			
			//We are going to look for father nodes under this element
			Vector<Element> fathers = new Vector<Element>(typeElement.getChildren("FATHER"));
			
			//And go trough the father launching the same process. We got the guaranty that there is no loop because the Figaro cannot be compiled if there is such loops.
			for(Element father : fathers)
				result.addAll(findFathers(father.getChildText("NAME")));
		}
		
		return result;
	}
	
	/**
	 * Find the ancestors of a particular type in the KB and return their name. By ancestors we mean all the fathers and the father of the fathers... until reaching the highest level.
	 * @param name Name of the type
	 * @return <code>Vector</code> of <code>String</code> containing all the names of the ancestors.
	 */
	@SuppressWarnings("unchecked")
	public Vector<String> findAncestors(String name) {
		
		System.err.println("We are looking for ancestors of type : " + name);
		
		//The result vector
		Vector<String> result = new Vector<String>();
		
		//First we have to retrieve the type
		Element typeElement = findType(name);
		
		//If there is at least one type called name then return continue the process otherwise we juste return the empty vector
		if(typeElement == null)
			return result;
		
		Vector<Element> fathers = new Vector<Element>(typeElement.getChildren("FATHER"));
		
		
		//Now we have to retrieve the ancestors of this node
		for(Element father : fathers) {
			result.add(father.getText());
			result.addAll(findAncestors(father.getText()));
		}
			
		System.err.println("We have found " + result.size() + "ancestors");
		
		return result;
	}
	
	/**
	 * Find the interfaces of a particular type in the KB and return their names.
	 * @param name Name of the type
	 * @return <code>Vector</code> of <code>String</code> containing the names of all the interfaces of the type.
	 */
	@SuppressWarnings("unchecked")
	public Vector<String> findInterfaces(String name) {
		
		//The result vector
		Vector<String> result = new Vector<String>();
		
		//First we have to retrieve the type
		Element typeElement = findType(name);
		
		//If there is at least one type called name then return continue the process
		if(typeElement != null) {
		
			//Because the interfaces are inherited from the ancestors we have to do the same process for all the ancestors. Therefore the first step is to retrieve the ancestors.
			Vector<String> ancestors = findAncestors(name);
			
			//In order to keep only one clean method we add the name of the type at the end of the vector and launch the procedure for all the element in the ancestor vector which now includes the type itself.
			ancestors.add(name);
			
			for(String ancestor : ancestors) {
				
				typeElement = findType(ancestor);
				
				if(typeElement != null) {
		
					//Now we have to retrieve the interfaces
					Vector<Element> interfaces = new Vector<Element>(typeElement.getChildren("INTERFACE"));
					
					//And finally we retrieve the name of the interfaces and put them in the result vector
					if(interfaces != null)
						for(Element interf : interfaces)
							if(interf.getChildText("NAME") != null)
								result.add(interf.getChildText("NAME"));
				}
			}
		}
		
		return result;
	}
	
	/**
	 * This method creates a particular type of filter for internal usage in the <code>GXMLFigaroLoader</code> class.
	 * @param ft The filter type defined in the <code>FilterTypes</code> class.
	 * @return The ready to use <code>Filter</code>
	 */
	private Filter createFilter(FilterTypes ft) {
		
		//We create a filter according to the type given as argument
		switch(ft) {
			case STEP:
				return new GFilterTagName("STAGE");
			case TYPE:
				return new GFilterTagName("CLASS");
			case GROUP:
				return new GFilterTagName("GROUP");
		}
		
		return null;
	}
}
