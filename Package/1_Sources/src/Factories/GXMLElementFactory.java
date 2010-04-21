package Factories;

//import java.awt.List;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

import GFilters.GFilterTagName;
import GXSDOperations.GXSDOperations;


public class GXMLElementFactory {
	
	@SuppressWarnings("unchecked")
	public static Vector<Element> refactorElement(Element e, String xk) {

		//The buffer vector
		Vector<Element> bufferVector = new Vector<Element>();
		
		//Case in which the element is null so we create a dummy element
		if(e == null) {
			System.out.println("Dummy element for " + xk);
			Element element = new Element(xk);
			element.setAttribute("dummy", "dummyElement");
			bufferVector.add(element);
		} else {
			//If the element correspond to the string then return the element itself
			if(e.getName().equals(xk)) {
				System.out.println("The element " + xk + "is at top level");
				bufferVector.add((Element)e.detach());
			} else {
			
				//Otherwise we are going to search trough its descendants
				Iterator<Element> iter = e.getDescendants(new GFilterTagName(xk));
				

				//If we find one we add it
				if(iter.hasNext()) {
					System.out.println("We finally found an element for " + xk + " in descendants");
					bufferVector.add(iter.next());
				} else {
					System.out.println("Dummy element for " + xk);
					//If we don't find one we create a dummy element
					Element element = new Element(xk);
					element.setAttribute("dummy", "dummyElement");
					bufferVector.add(element);
				}
			}
		}
		
		return bufferVector;
	}
	
	@SuppressWarnings("unchecked")
	public static Vector<Element> refactorElements(Vector<Element> e, String xk) {
		//########IL FAUT RAJOUTER LE DETACH POUR LES ELEMENTS
		//The buffer vector
		Vector<Element> bufferVector = new Vector<Element>();
	
		//Case in which the element is null so we create a dummy element
		if(e == null) {
			Element element = new Element(xk);
			element.setAttribute("dummy", "");
			bufferVector.add(element);
		} else if(e.size() == 0) {
			Element element = new Element(xk);
			element.setAttribute("dummy", "");
			bufferVector.add(element);
		} else {
	
			//Some temporary variables
			boolean elementFound = false;
			Element element;
			
			//Now we will go trough all the vector and the descendants of each element of the vector
			for(Iterator<Element> iter = e.iterator(); iter.hasNext();) {
			
				element = iter.next();
				
				//If the element is null just go to the next loop
				if(element != null) {
					//If the element correspond to the string then return the element itself
					
					
					//If the element has the right name then add it to the vector and go to the next
					if(element.getName().equals(xk)) {
						bufferVector.add(element);
						elementFound = true;
					} else {
			
						//Otherwise we will search in its descendants using a filter
						Iterator<Element> descendantIterator = element.getDescendants(new GFilterTagName(xk));
						
						if(descendantIterator.hasNext()) {
							//We find in the children of the first element of the vector. This characteristic is useful when we have to search amongst the children of an element already in a vector
							for(;descendantIterator.hasNext();)
								bufferVector.add(descendantIterator.next());
							elementFound = true;
						}
					}
				}
			}
			
			//If there is no such element then throw an exception #### to be modified
			if(!elementFound) {
				//throw new GExceptionElement(e, xk);
				element = new Element(xk);
				element.setAttribute("dummy", "");
				bufferVector.add(element);
			}
		}
		
		return bufferVector;
	}
	
	@SuppressWarnings("unchecked")
	public static Vector<Element> refactorElementsOnlyChildren(Vector<Element> e, String xk) {
		//########IL FAUT RAJOUTER LE DETACH POUR LES ELEMENTS
		//The buffer vector
		Vector<Element> bufferVector = new Vector<Element>();

		//Case in which the element is null so we create a dummy element
		if(e == null) {
			Element element = new Element(xk);
			element.setAttribute("dummy", "");
			bufferVector.add(element);
		} else if(e.size() == 0) {
			Element element = new Element(xk);
			element.setAttribute("dummy", "");
			bufferVector.add(element);
		} else {
	
			//Some temporary variables
			boolean elementFound = false;
			Element element;
			
			//Now we will go trough all the vector and the descendants of each element of the vector
			for(Iterator<Element> iter = e.iterator(); iter.hasNext();) {
			
				element = iter.next();
				
				//If the element is null just go to the next loop
				if(element != null) {
					//If the element correspond to the string then return the element itself				
					
					//If the element has the right name then add it to the vector and go to the next
					if(element.getName().equals(xk)) {
						bufferVector.add(element);
						elementFound = true;

					} else {
			
						//Otherwise we will search in its descendants using a filter
						Vector<Element> children = new Vector<Element>(element.getChildren(xk));
						
						if(children.size() > 0)
							elementFound = true;
						
						//We find in the children of the first element of the vector. This characteristic is useful when we have to search amongst the children of an element already in a vector
						for(Iterator<Element> childrenIter = children.iterator(); childrenIter.hasNext();)
								bufferVector.add(childrenIter.next());
					}
				}
			}
			
			//If there is no such element then throw an exception #### to be modified
			if(!elementFound) {
				//throw new GExceptionElement(e, xk);
				element = new Element(xk);
				element.setAttribute("dummy", "");
				bufferVector.add(element);
			}
		}

		return bufferVector;
	}
	
	public static boolean saveElement(Element root, Element child) {
		
		//We create a vector from the only child
		Vector<Element> childVect = new Vector<Element>();
		childVect.add(child);
		
		//Then we call the saveElements method with the vector
		return saveElements(root, childVect);
	}
	
	public static boolean saveElements(Element root, Vector<Element> children) {
		
		//If the root is null return false
		if(root == null)
			return false;
		
		//If the children vector is null return false
		if(children == null)
			return false;
		
		//For each children we have to retrieve the xsd of the root and find the cardinality of the son and adapt the process
		int minCardinality = 0;
		for(Element child : children) {
			
			//First we retrieve the minCardinality
			minCardinality = GXSDOperations.getMinimumCardinality(root.getName(), child.getName());
			
			//Now if it is :
			//-1 we don't add the child because it means that there is a problem with the schema
			//0 we have to check if the child is null and in this case we do not add it other we add it
			//>0 we add the child anyway
			switch(minCardinality) {
			
				case -1:
					break;
					
				case 0:
					//Check if the child is not null
					if(child.getChildren().size() > 0 || child.getAttributes().size() > 0 || !child.getText().equals(""))
						root.addContent(child);
					break;
					
				default:
					root.addContent(child);
			}
		}
		
		return true;
	}
}