package GXMLLoader;

/*
 * XMLLoader.java
 * part of the VisualFigaro plugin for the jEdit text editor
 * Copyright (C) 2008 Guillaume Torrente & Marc Bouissou
 * guillaumetorrente@yahoo.fr
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import java.io.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

import GFilters.GFilterElementWithSubElementWithValue;
import GFilters.GFilterTagName;
import GFilters.GFilterXMLTypes;
import GLanguage.GLanguage;

import java.util.Iterator;
import java.util.Vector;

/**
 * This class performs operations on xml files and xml trees. It is specialized and has high level methods to parse BDC like xml trees. There is another class called <code>GXMLFigaroLoader</code> which does almost the same thing but for Figaro like xml trees.
 * @author Guillaume Torrente & Marc Bouissou
 * @see GXMLLoaderFigaro 
 */
public class GXMLLoader {

	//Le document a parser et ce qui lui servira de racine
	//Document to be parsed and what will be considered as its root
	/*static *//**
	 * @uml.property  name="document"
	 * @uml.associationEnd  
	 */
	private org.jdom.Document document;

	/**
	 * @uml.property  name="language"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GLanguage language;
	
	/**
	 * Default constructor
	 *
	 */
	public GXMLLoader(GLanguage language) {
		this.language = language;
	}

	/**
	 * Loads the XML file stored in "path" using JDOM
	 * @param path The path to the file to be loaded
	 * @return org.jdom.Document
	 */
	public org.jdom.Document loadXmlFileJDOM(String path) {
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
	 * Loads the XML file stored in "path" using JDOM and convert it into a w3c DOM document.
	 * @param path The path to the file which has to be loaded
	 * @return org.w3c.dom.document
	 */
	public org.w3c.dom.Document loadXmlFile(String path)
	{
		loadXmlFileJDOM(path);

		org.w3c.dom.Document documentDOM = null;
		DOMOutputter domOutputter = new DOMOutputter();
		try{
			documentDOM = domOutputter.output(document);
		} catch (Exception e) {
			return null;
		}

		return documentDOM;
	}

	/**
	 * Returns ALL the elements at depth "depth" in the tree with root "e"
	 * @param depth Depth from which the elements will be extracted
	 * @param e Root of the tree from which the elements will be extracted
	 * @return Vector of all elements
	 */
	public Vector<Element> getElementsAtDepth(int depth, Element e) {

		Vector<Element> result = new Vector<Element>();
		Vector<Element> tamponVect = new Vector<Element>();
		
		if(depth == 0) {
			result.add(e);
		} else {
			if(e == null) {
				return new Vector<Element>();
			}

			for(int i=0; i<e.getContentSize(); i++) {
				if(e.getContent(i) instanceof Element) {
					tamponVect = getElementsAtDepth(depth-1, (Element)e.getContent(i));
					for(int j=0; j<tamponVect.size(); j++) {
						result.add(tamponVect.get(j));
					}
				}
			}
		}

		return result;
	}


	/**
	 * Returns the first element at depth "depth" which holds a tag "balise" with value "valeur"
	 * @param e Root of the tree from which the element will be extracted
	 * @param depth Depth of the element which has to be extracted
	 * @param balise The tag which will be used to find the element
	 * @param value The value of the tag "balise" which will be used to filter all the elements
	 * @return The desired element or null
	 */
	public Element findElementWithValue(Element e, int depth, String balise, String value) {
	
		Vector<Element> tamponVect = new Vector<Element>();
	
		//On trouve tout les elements a la profondeur donnee
		//First we find all the elements at a given depth
		tamponVect = getElementsAtDepth(depth, e);
		//System.out.println("Voici le nombre d'element trouves a la profondeur : " + depth + " => " + tamponVect.size());
		
		//On va maintenant rechercher l'element
		//Now we are going to search the specified element
		for(int i=0; i<tamponVect.size(); i++)
			if(tamponVect.get(i).getName().equals(balise))
				if(tamponVect.get(i).getText().equals(value))
					return tamponVect.get(i);
		
		return null;
	}


	/**
	 * Returns the first element at depth "depth" which has for tag "balise" and for tag of this tag the tag "subBalise" with value "value"
	 * @param e Root of the tree from which the element will be extracted
	 * @param depth Depth of the element which has to be extracted
	 * @param balise The tag which will be used to find the element
	 * @param subBalise The tag pending from the previous tag which will also be used to select the element 
	 * @param value Value of the sub tag
	 * @return The desired element or null
	 */
	public Element findElementWithSubElementWithValue(Element e, int depth, String balise, String subBalise, String value) {
	
		Vector<Element> tamponVect = new Vector<Element>();
		
		//On trouve tout les elements a la profondeur donnee
		//First we find all the elements at a given depth
		tamponVect = getElementsAtDepth(depth, e);
		//System.out.println("Voila le nombre d'element trouves a la profondeur : " + depth + " => " + tamponVect.size());
		
		for(int i=0; i<tamponVect.size(); i++) {
			//System.out.println("Voici le nom : " + tamponVect.get(i).getName());
			if(tamponVect.get(i).getName().equals(balise))
				if(findElementWithValue(tamponVect.get(i), 1, subBalise, value) != null) {
					//System.out.println("On ajoute le noeud : " + tamponVect.get(i).getName());
					return tamponVect.get(i);
				}
				
		}
		
		return null;
	}
	
	/**
	 * Returns all the elements at depth "depth" which has for tag "balise" and for tag under them the tag "subBalise"
	 * @param e Root of the tree from which the elements will be extracted
	 * @param depth Depth of the lemenets which has to be extracted
	 * @param balise The tag which will be used to find the element
	 * @param subBalise The tag pending from the previous tag which also be used to select the elements
	 * @return Vector of all the elements matching the conditions
	 */
	public Vector<Element> findElementWithSubBalise(Element e, int depth, String balise, String subBalise) {
		Vector<Element> tamponVect = new Vector<Element>();
		Vector<Element> result = new Vector<Element>();
		
		//On trouve tout les elements a la profondeur donnee
		//First we find all the elements at a given depth
		tamponVect = getElementsAtDepth(depth, e);
		//System.out.println("Voila le nombre d'element trouves a la profondeur : " + depth + " => " + tamponVect.size());
		
		for(int i=0; i<tamponVect.size(); i++) {
			//System.out.println("Voici le nom : " + tamponVect.get(i).getName());
			if(tamponVect.get(i).getName().equals(balise))
				if(tamponVect.get(i).getChild(subBalise) != null)
					result.add(tamponVect.get(i));
		}
		
		return result;
	}
	
	/**
	 * Returns the root element of the currently loaded xml file.
	 * @return Root element of the document
	 */
	public Element getRootElement() {
		if(document == null)
			return null;
		return document.getRootElement();
	}
	
	/**
	 * Returns the <code>Element</code> corresponding to a particular type.
	 * @param name Name of the type which has to be retrieved.
	 * @return The corresponding <code>Element</code> or <code>null</code> if it is not in the tree.
	 */
	@SuppressWarnings("unchecked")
	public Element findType(String name) {
		
		//First we find all the types
		Vector<Element> possibleTypes = new Vector<Element>(document.getRootElement().getContent(new GFilterElementWithSubElementWithValue(language.getBDCTranslation("TYPE"), language.getBDCTranslation("NOM"), name)));
		
		//And then we return the first one or null
		if(possibleTypes.size() > 0)
			return possibleTypes.get(0);
		else
			return null;
	}
	
	/**
	 * Returns a <code>Vector</code> of <code>Element</code> containing all the types present in the file.
	 * @return <code>Vector</code> of <code>Element</code>.
	 */
	@SuppressWarnings("unchecked")
	public Vector<Element> findTypes() {
		
		//First we have to create a filter
		GFilterXMLTypes filterTypes = new GFilterXMLTypes(language);
		
		//Then we use it to retrieve the types and return the vector
		return new Vector<Element>(document.getRootElement().getContent(filterTypes));
	}
	
	/**
	 * Returns the name of all the visualizations present in the file.
	 * @return <code>Vector</code> of <code>String</code> or the empty <code>Vector</code> if there is no such elements in the tree.
	 */
	@SuppressWarnings("unchecked")
	public Vector<String> findVisualizations() {
		
		//The vector for the result
		Vector<String> result = new Vector<String>();
		
		//And a buffer vector to contain the specified Elements
		Vector<Element> elements = new Vector<Element>();
		
		//We find all the elements related to steps
		elements.addAll(document.getRootElement().getContent(new GFilterTagName(language.getBDCTranslation("VISUALISATION"))));
		
		//And translate them into strings
		for(Iterator<Element> iter = elements.iterator(); iter.hasNext();)
			result.add(iter.next().getText());
		
		return result;
	}
	
	/**
	 * Returns the name of all the visualizations present in the file.
	 * @return <code>Vector</code> of <code>String</code> or the empty <code>Vector</code> if there is no such elements in the tree.
	 */
	@SuppressWarnings("unchecked")
	public Vector<String> findMenus() {
		
		//The vector for the result
		Vector<String> result = new Vector<String>();
		
		//And a buffer vector to contain the specified Elements
		Vector<Element> elements = new Vector<Element>();
		
		//We find all the elements related to steps
		elements.addAll(document.getRootElement().getContent(new GFilterTagName(language.getBDCTranslation("FAMILLE_TYPE_PALETTE"))));
		
		//And translate them into strings
		for(Iterator<Element> iter = elements.iterator(); iter.hasNext();)
			result.add(iter.next().getText());
		
		return result;
	}
	
	/**
	 * Returns a <code>Vector</code> of <code>String</code> containing all the names of the nodes present in the file.
	 * @return <code>Vector</code> of <code>String</code> of all the names of the nodes.
	 */
	public Vector<String> findNodes() {
		
		//The result vector
		Vector<String> result = new Vector<String>();
		
		//First we retrieve all the types
		Vector<Element> types = findTypes();
		
		//Then we will go trough the vector to find the elements which have a subElement called NOEUD
		for(Element element : types)
			//If the element is not null we launch the process
			if(element != null)
				if(element.getChild(language.getBDCTranslation("NOEUD")) != null)
					result.add(element.getChildText(language.getBDCTranslation("NOM")));
		
		return result;
	}
	
	/**
	 * Returns a <code>Vector</code> of <code>String</code> containing all the names of the links present in the file.
	 * @return <code>Vector</code> of <code>String</code> of all the names of the links.
	 */
	public Vector<String> findLinks() {
		
		//The result vector
		Vector<String> result = new Vector<String>();
		
		//First we retrieve all the types
		Vector<Element> types = findTypes();
		
		System.err.println("Recherche de liens");
		
		//Then we will go trough the vector to find the elements which have a subElement called LINK
		for(Element element : types)
			//If the element is not null we launch the process
			if(element != null)
				if(element.getChild(language.getBDCTranslation("LIEN")) != null)
					result.add(element.getChildText(language.getBDCTranslation("NOM")));
		
		System.err.println("Liens trouves : " + result.size());
		
		return result;
	}
	
	/**
	 * Returns a <code>Vector</code> of <code>String</code> containing all the names of the graphic variants present in the file.
	 * @return <code>Vector</code> of <code>String</code> of all the names of the graphics variants.
	 */
	@SuppressWarnings("unchecked")
	public Vector<String> findVariantesGraphiquesNames(String typeName) {
		
		//The result vector
		Vector<String> result = new Vector<String>();
		
		//First we retrieve the type
		Element type = findType(typeName);
		
		//And finally we will return the children named VARIANTE_GRAPHIQUE if the type is not null otherwise just return
		if(type == null) 
			return result;

		Iterator<Element> variantesGraphiquesIterator = type.getDescendants(new GFilterTagName(language.getBDCTranslation("VARIANTE_GRAPHIQUE")));
		
		for(;variantesGraphiquesIterator.hasNext();)
			result.add(variantesGraphiquesIterator.next().getChildText(language.getBDCTranslation("NOM")));
		
		return result;
	}
	
	/**
	 * Returns a <code>Vector</code> of <code>String</code> containing all the names of the ports present in the file.
	 * @return <code>Vector</code> of <code>String</code> of all the names of the ports.
	 */
	@SuppressWarnings("unchecked")
	public Vector<String> findPortsNames(String typeName) {
		
		//The result vector
		Vector<String> result = new Vector<String>();
		
		//First we retrieve the type
		Element type = findType(typeName);
		
		//And finally we will return the children named PORTS if the type is not null and if it is a node otherwise just return
		if(type == null) 
			return result;

		if(type.getChild(language.getBDCTranslation("NOEUD")) == null)
			return result;
		
		Iterator<Element> pointConnexionIterator = type.getDescendants(new GFilterTagName(language.getBDCTranslation("POINT_CONNEXION")));
		
		for(;pointConnexionIterator.hasNext();)
			result.add(pointConnexionIterator.next().getChildText(language.getBDCTranslation("NOM")));
		
		return result;
	}
}