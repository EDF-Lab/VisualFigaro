package GLanguage;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.jdom.Element;

import GFilters.GFilterElementWithSubElementWithValue;
import GFilters.GFilterTagName;
import GXMLLoader.GXMLLoader;

public class GLanguage {
	
	//The currently selected language
	/**
	 * @uml.property  name="language"
	 * @uml.associationEnd  
	 */
	private String language;
	/**
	 * @uml.property  name="languagePosition"
	 */
	private int languagePosition;
	
	//The list of all the languages available
	/**
	 * @uml.property  name="availableLanguages"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	private Vector<String> availableLanguages;
	
	//The xml document from which the completion tags are extracted
	/**
	 * @uml.property  name="completionDocumentRoot"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Element completionDocumentRoot;
	
	//The xml docuement from which the bdc tags are extracted
	/**
	 * @uml.property  name="bdcDocumentRoot"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Element bdcDocumentRoot;
	
	//The xml document from which the figaro tags are extracted
	/**
	 * @uml.property  name="figaroDocumentRoot"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Element figaroDocumentRoot;
	
	@SuppressWarnings("unchecked")
	public GLanguage() {
		
		//Initialize the class variables
		language = "";
		languagePosition = -1;
		availableLanguages = new Vector<String>();
		completionDocumentRoot = null;
		bdcDocumentRoot = null;
		figaroDocumentRoot = null;
		
		//We are going to load the syntaxemultilanguage.xml file in order to get the list of the available languages
		GXMLLoader xl = new GXMLLoader(null);
		Element syntaxeElement = xl.loadXmlFileJDOM(System.getenv("VISUAL_FIGARO") + "syntaxemultilangage.xml").getRootElement();
		for(Iterator<Element> languageIterator = syntaxeElement.getDescendants(new GFilterTagName("language")); languageIterator.hasNext();)
			availableLanguages.add(languageIterator.next().getText());
		
		//Now we set the language to be the first of the availableLanguage vector
		if(availableLanguages.size() <= 0)
			return;
		
		language = availableLanguages.get(0);
		languagePosition = 0;
		
		completionDocumentRoot = xl.loadXmlFileJDOM(System.getenv("VISUAL_FIGARO") + "syntaxemultilangage.xml").getRootElement();
		bdcDocumentRoot = xl.loadXmlFileJDOM(System.getenv("VISUAL_FIGARO") + "keywordsTranslationBDCFile.xml").getRootElement();
		figaroDocumentRoot = xl.loadXmlFileJDOM(System.getenv("VISUAL_FIGARO") + "keywordsTranslationBDCFile.xml").getRootElement();
	}
	
	/**
	 * Set the language used in this object.
	 * @param newLanguage String containing the name of the language.
	 * @return True if everything goes fine, False if the language is not available.
	 */
	public boolean setLanguage(String newLanguage) {
		
		//If it is not an available language we return false
		if(!availableLanguages.contains(newLanguage))
			return false;
		
		//Otherwise we change the language and update the two roots
		language = newLanguage;
		languagePosition = availableLanguages.indexOf(language);
		
		System.err.println("LangagePosition : " + languagePosition);
		
		return true;
	}
	
	/**
	 * @return
	 * @uml.property  name="language"
	 */
	public String getLanguage() {
		return language;
	}
	
	@SuppressWarnings("unchecked")
	public Vector<String> getAvailableLanguages() {
		
		//The result vector
		Vector<String> result = new Vector<String>();
		
		//We retrieve the available languages in the syntaxeMultilanguage.xml file
		Iterator<Element> languagesIterator = completionDocumentRoot.getDescendants(new GFilterTagName("language"));
		
		//Now we extract all the possible values from this list
		for(;languagesIterator.hasNext();)
			result.add(languagesIterator.next().getText());
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Vector<JMenu> getCompletionMenu(String word) {
		
		//First we create the result vector constituted of JMenu
		Vector<JMenu> menuVector = new Vector<JMenu>();
		
		//Now we have to find the corresponding tag in syntaxeMultilanguage.xml
		Iterator<Element> tagIterator = completionDocumentRoot.getDescendants(new GFilterElementWithSubElementWithValue("tag", "parent", word));
		
		//If there is no such tag we just return the current, empty, menuVector
		if(!tagIterator.hasNext())
			return menuVector;
		
		//Otherwise we only use the first element of the iterator and extract the information. We get all the sons "son" of this element
		Vector<Element> sonsVector = new Vector<Element>(tagIterator.next().getChildren("son"));
		
		//The key point is that now we will retrieve the languagePosition-th son of the "son" elements
		for(Element son : sonsVector) {
			
			//If there is no languagePosition-th element, just continue
			if(son.getChildren().size() < languagePosition)
				continue;
			
			//Otherwise we use it to create the menu
			menuVector.add(new JMenu(((Element)son.getChildren().get(languagePosition)).getChildText("menuName")));
			
			//We get all the codes which will be added under the menu
			Vector<Element> codesVector = new Vector<Element>(((Element)son.getChildren().get(languagePosition)).getChildren("code"));
			for(Element code : codesVector) {
				
				//Finally we create the subMenu and add it to the menu
				JMenuItem codeItem = new JMenuItem(code.getText());
				codeItem.addActionListener(new copyListener(code.getText()));
				menuVector.lastElement().add(codeItem);
			}
		}
		
		return menuVector;
	}
	
	@SuppressWarnings("unchecked")
	public String getBDCTranslation(String word) {
		
		//First we find the tag in the xml tree
		Vector<Element> elements = new Vector<Element>(bdcDocumentRoot.getContent(new GFilterElementWithSubElementWithValue("word", "spell", word)));
		
		//If an element has been found then process to the translation otherwise return the original word or "" for debugging
		if(elements.size() <= 0)
			return "";
		
		//An element has been found. Because the order of the language match the order of the word tag we just have to return the languagePos element of the children list of the word element
		return ((Element)elements.get(0).getChildren().get(languagePosition)).getText();
	}
	
	@SuppressWarnings("unchecked")
	public String getFigaroTranslation(String word) {
		
		//First we find the tag in the xml tree
		Vector<Element> elements = new Vector<Element>(figaroDocumentRoot.getContent(new GFilterElementWithSubElementWithValue("word", "spell", word)));
		
		//If an element has been found then process to the translation otherwise return the original word or "" for debugging
		if(elements.size() <= 0)
			return "";
		
		//An element has been found. Because the order of the language match the order of the word tag we just have to return the languagePos element of the children list of the word element
		return ((Element)elements.get(0).getChildren().get(languagePosition)).getText();
	}
	
	@SuppressWarnings("unchecked")
	public Vector<String> getAllBDCKeywords() {
		
		//The result vector
		Vector<String> result = new Vector<String>();
		
		//First we find all the words in the file
		Vector<Element> elements = new Vector<Element>(bdcDocumentRoot.getContent(new GFilterTagName("word")));
		
		//An element has been found. Because the order of the language match the order of the word tag we just have to return the languagePos element of the children list of the word element
		for(Element element : elements)
			result.add(((Element)element.getChildren().get(languagePosition)).getText());
		
		return result;
	}
	
	private class copyListener implements ActionListener {
		
		String valueToBeCopied;
		
		public copyListener(String value) {
			valueToBeCopied = value;
		}
		
		public void actionPerformed(ActionEvent e) {
			Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			cb.setContents(new StringSelection(valueToBeCopied + "\n"), null);
		}
	}
}
