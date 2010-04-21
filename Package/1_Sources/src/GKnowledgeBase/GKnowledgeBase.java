package GKnowledgeBase;

import java.util.Hashtable;

import figaroInterface.GTree;
import GLanguage.GLanguage;

public class GKnowledgeBase {

	//The name of the knowledge base
	/**
	 * @uml.property  name="knowledgeBaseName"
	 */
	private String knowledgeBaseName;
	
	//The language used for the knowledge base
	/**
	 * @uml.property  name="knowledgeBaseLanguage"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GLanguage knowledgeBaseLanguage;
	
	//The GTree used to represent the tree in the plugin
	/**
	 * @uml.property  name="knowledgeBaseTree"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GTree knowledgeBaseTree;
	
	//The hashtable for the icons of the knowledgeBase
	/**
	 * @uml.property  name="typeIconPathTable"
	 * @uml.associationEnd  qualifier="type:java.lang.String java.lang.String"
	 */
	private Hashtable<String, String> typeIconPathTable;
	
	public GKnowledgeBase() {
		knowledgeBaseName = "";
		knowledgeBaseLanguage = new GLanguage();
		knowledgeBaseTree = new GTree();
	}
	
	public GKnowledgeBase(String name, GLanguage language, GTree tree) {
		knowledgeBaseName = name;
		knowledgeBaseLanguage = language;
		knowledgeBaseTree = tree;
	}

	/**
	 * @return
	 * @uml.property  name="knowledgeBaseName"
	 */
	public String getKnowledgeBaseName() {
		return knowledgeBaseName;
	}

	/**
	 * @param knowledgeBaseName
	 * @uml.property  name="knowledgeBaseName"
	 */
	public void setKnowledgeBaseName(String knowledgeBaseName) {
		this.knowledgeBaseName = knowledgeBaseName;
	}

	/**
	 * @return
	 * @uml.property  name="knowledgeBaseLanguage"
	 */
	public GLanguage getKnowledgeBaseLanguage() {
		return knowledgeBaseLanguage;
	}

	/**
	 * @param knowledgeBaseLanguage
	 * @uml.property  name="knowledgeBaseLanguage"
	 */
	public void setKnowledgeBaseLanguage(GLanguage knowledgeBaseLanguage) {
		this.knowledgeBaseLanguage = knowledgeBaseLanguage;
	}

	/**
	 * @return
	 * @uml.property  name="knowledgeBaseTree"
	 */
	public GTree getKnowledgeBaseTree() {
		return knowledgeBaseTree;
	}

	/**
	 * @param knowledgeBaseTree
	 * @uml.property  name="knowledgeBaseTree"
	 */
	public void setKnowledgeBaseTree(GTree knowledgeBaseTree) {
		this.knowledgeBaseTree = knowledgeBaseTree;
	}

	public Hashtable<String, String> getTypeIconPathTable() {
		return typeIconPathTable;
	}

	public void setTypeIconPathTable(Hashtable<String, String> typeIconPathTable) {
		this.typeIconPathTable = typeIconPathTable;
	}
}
