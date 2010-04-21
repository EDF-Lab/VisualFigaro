package GXMLLoader;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;

import GFilters.GFilterElementWithSubElementWithValue;
import GFilters.GFilterTagName;

/**
 * This class performs operations on xml files and xml trees. It is specialized and has high level methods to parse Figaro like xml trees. There is another class called <code>GXMLLoader</code> which does almost the same thing but for BDC like xml trees.
 * @author Guillaume Torrente & Marc Bouissou
 * @see GXMLLoader 
 */
public class GXMLLoaderDefaultFiles {
	
	//Document to be parsed and what will be considered as its root
	/**
	 * @uml.property  name="document"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	Document document;
	/**
	 * @uml.property  name="root"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	Element root;
	
	/**
	 * The default constructor.
	 */
	public GXMLLoaderDefaultFiles() {
		
		GXMLLoader xl = new GXMLLoader(null);
		document = xl.loadXmlFileJDOM(System.getenv("VISUAL_FIGARO") + "copiedFiles.xml");
		root = document.getRootElement();
	}
	
	@SuppressWarnings("unchecked")
	public Vector<String> getAvailableLanguages() {
		
		//The result vector
		Vector<String> result = new Vector<String>();
		
		//If the root is null just return the empty vector
		if(root == null)
			return result;
		
		//First we have to retrieve all the languages one by one and put the language name in the result vector
		for(Iterator<Element> languageIterator = root.getDescendants(new GFilterTagName("language")); languageIterator.hasNext(); )
			result.add(languageIterator.next().getText());
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Hashtable<String, String> getSchemaByLanguage() {
		
		Hashtable<String, String> result = new Hashtable<String, String>();
		
		//If the root is null just return the empty hashtable
		if(root == null)
			return result;
		
		//First we have to retrieve all the configurations one by one and inside a particular configurations we will find which schema is used and which language is concerned
		Vector<Element> configurations = new Vector<Element>(root.getChildren("configuration"));
		
		for(Element configuration : configurations)
			if(configuration.getChild("language") != null && configuration.getChild("schemaFile") != null)
				result.put(configuration.getChildText("language"), configuration.getChildText("schemaFile"));
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public String getSchemaFilenameForLanguage(String language) {
		
		System.err.println("a");
		
		//If the root is null just return the empty string
		if(root == null)
			return "";
		
		System.err.println("b : " + language);
		
		//Now e just have to retrieve the configuration for the language given as argument and return the text under the schemaFile tag
		Vector<Element> configurations = new Vector<Element>(root.getContent(new GFilterElementWithSubElementWithValue("configuration", "language", language)));
		if(configurations.size() <= 0 || configurations.size() > 1)
			return "";
		
		System.err.println("c");
		
		//The files are stored under the tag "file"
		return configurations.get(0).getChildText("schemaFile");
	}
	
	@SuppressWarnings("unchecked")
	public String getLanguageForSchemaFile(String schemaFile) {
	
		//If the root is null just return the empty hashtable
		if(root == null)
			return "";
		
		//Now we just have to retrieve the configuration for the schemaFile given as argument and return the text under the language tag
		Vector<Element> configurations = new Vector<Element>(root.getContent(new GFilterElementWithSubElementWithValue("configuration", "schemaFile", schemaFile)));
		if(configurations.size() <= 0 || configurations.size() > 1)
			return "";
		
		//The files are stored under the tag "file"
		return configurations.get(0).getChildText("language");
	}
	
	@SuppressWarnings("unchecked")
	public Vector<String> getDefaultFiles(String language, String element) {
		
		Vector<String> result = new Vector<String>();
		
		//If the root is null just return the empty vector
		if(root == null)
			return result;
		
		//Otherwise we have to find he corresponding language and return the empty vector if it is not present or if there is more than one configuration correponding to the language
		Vector<Element> configurations = new Vector<Element>(root.getContent(new GFilterElementWithSubElementWithValue("configuration", "language", language)));
		if(configurations.size() <= 0 || configurations.size() > 1)
			return result;
		
		//The files are stored under the tag "file"
		Vector<Element> files = new Vector<Element>(configurations.get(0).getChildren(element));
		for(Element file : files)
			result.add(file.getText());
		
		return result;
	}

	@SuppressWarnings("unchecked")
	public String getDefaultTextForLanguage(String language) {
		
		//If the root is null just return the empty string
		if(root == null)
			return "";
		
		//Now e just have to retrieve the configuration for the language given as argument and return the text under the schemaFile tag
		Vector<Element> configurations = new Vector<Element>(root.getContent(new GFilterElementWithSubElementWithValue("configuration", "language", language)));
		if(configurations.size() <= 0 || configurations.size() > 1)
			return "";
		
		//The files are stored under the tag "initializationText"
		return configurations.get(0).getChildText("initializationText");
	}
	
	public boolean isAvailableLanguage(String language) {
		
		//First we have to retrieve the vector of all languages and then we have to check if the language is inside
		return getAvailableLanguages().contains(language);
	}
}
