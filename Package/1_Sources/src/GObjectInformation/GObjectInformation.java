package GObjectInformation;

import global.WindowTypes;

import java.util.Vector;

import GLanguage.GLanguage;

/**
 * This class is used by each <code>GWindow</code> to describe which part of the XML tree it concerns.
 * @author Guillaume Torrente & Marc Bouissou
 * @see GWindow, WindowTypes
 */
public class GObjectInformation {

	//The current language
	/**
	 * @uml.property  name="language"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	GLanguage language;
	
	//The path from the root window to the current window
	/**
	 * @uml.property  name="windowPath"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	Vector<String> windowPath;
	
	//Tha path of the knowledge base in the hard drive
	/**
	 * @uml.property  name="knowledgeBasePath"
	 */
	String knowledgeBasePath;
	
	//Indicates if the window is a window concerning nodes, links or neither
	/**
	 * @uml.property  name="type"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	WindowTypes type;
	
	//Indicates the node concerned
	/**
	 * @uml.property  name="typeConcerned"
	 */
	String typeConcerned;
	
	/**
	 * The default constructor initializes the variables <code>path</code> and <code>type</code> to the empty vector and to <code>NEITHER</code> respectively.
	 */
	public GObjectInformation() {
		language = new GLanguage();
		windowPath = new Vector<String>();
		knowledgeBasePath = "";
		type = WindowTypes.NEITHER;
		typeConcerned = "";
	}
	
	/**
	 * A kind of copy constructor from another <code>GObjectInformation</code> given as argument.
	 * @param info The <code>GObjectInformation</code> the data will be copied from
	 */
	public GObjectInformation(GObjectInformation info) {
		windowPath = new Vector<String>();
		if(info != null) {
		
			language = info.getLanguage();
			
			if(info.getWindowPath() != null)
				for(String s : info.getWindowPath())
					windowPath.add(new String(s));
			
			knowledgeBasePath = info.getKnowledgeBasePath();
			
			type = info.getType();
			
			typeConcerned = info.getTypeConcerned();
		}
	}

	/**
	 * Setter for the <code>language</code> variable.
	 * @param language  The new language which will be stored in the object.
	 * @uml.property  name="language"
	 */
	public void setLanguage(GLanguage language) {
		this.language = language;
	}

	/**
	 * Getter for the <code>language</code> variable.
	 * @return  The language of the window.
	 * @uml.property  name="language"
	 */
	public GLanguage getLanguage() {
		return language;
	}
	
	/**
	 * Setter for the <code>windowPath</code> variable.
	 * @param newPath The new path which will be stored in the object.
	 */
	public void setWindowPath(Vector<String> newPath) {
		windowPath = newPath;
	}
	
	/**
	 * Getter for the <code>windowPath</code> variable.
	 * @return The path to the window.
	 */
	public Vector<String> getWindowPath() {
		return windowPath;
	}
	
	
	/**
	 * Getter for the <code>knowledgeBasePath</code> variable.
	 * @return  The path to the knowledge base.
	 * @uml.property  name="knowledgeBasePath"
	 */
	public void setKnowledgeBasePath(String knowledgeBasePath) {
		this.knowledgeBasePath = knowledgeBasePath;
	}

	/**
	 * Getter for the <code>knowledgeBasePath</code> variable.
	 * @return  The path to the knowledge base.
	 * @uml.property  name="knowledgeBasePath"
	 */
	public String getKnowledgeBasePath() {
		return knowledgeBasePath;
	}

	/**
	 * Add a new step to the current path. For example adding "document" to the current "file/test" path will give the "file/test/document" path.
	 * @param stepName The name of the new step which has to be added. 
	 */
	public void addStepToPath(String stepName) {
		windowPath.add(stepName);
	}
	
	/**
	 * Setter for the <code>type</code> variable.
	 * @param newType  The new type which will be stored in the object.
	 * @uml.property  name="type"
	 */
	public void setType(WindowTypes newType) {
		type = newType;
	}
	
	/**
	 * Getter for the <code>type</code> variable.
	 * @param newType  The new type which will be stored in the object.
	 * @uml.property  name="type"
	 */
	public WindowTypes getType() {
		return type;
	}
	
	/**
	 * Return the path to the window in order to be printed in the title bar of the window according to the Visual Figaro standards.
	 * @return String containing the path in a readable form.
	 */
	public String getPrintablePath() {
		return "";
	}
	
	/**
	 * Get the last value of the path which usually means the name of the <code>GObject</code> owner of this information.
	 * @return The last element of the path or the empty string.
	 */
	public String getLastPartOfThePath() {
		if(windowPath == null)
			return "";
		
		if(windowPath.size() <= 0)
			return "";
		
		return windowPath.get(windowPath.size() - 1);
	}
	
	/**
	 * Get the node concerned by this information.
	 * @return  The name of the node concerned or the empty <code>String</code> if there is no such node.
	 * @uml.property  name="typeConcerned"
	 */
	public String getTypeConcerned() {
		return typeConcerned;
	}
	
	/**
	 * Set the node concerned by this information.
	 * @param node  The name of the node concerned or the empty <code>String</code> if the information is not attached to any particular node
	 * @uml.property  name="typeConcerned"
	 */
	public void setTypeConcerned(String node) {
		this.typeConcerned = node;
	}
}
