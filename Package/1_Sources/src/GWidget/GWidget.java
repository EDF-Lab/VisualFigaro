package GWidget;


import java.util.Vector;

import javax.swing.JPanel;

import org.jdom.Element;

import GObject.GObject;
import GObjectInformation.GObjectInformation;

/**
 * The GWidget class is one of the most important classes of the architecture. It defines the basic methods which have to be implemented by all the widget. By definition a <code>GWidget</code> does not care about
 * the Figaro language. Therefore if the widget has to be initialized with some Figaro strings like the steps available in the KB it is the window which contains the <code>GWidget</code> which will pass them as argument to the <code>GWidget</code>.
 * Another important point for a <code>GWidget</code> is that it cannot write or edit xml tag directly. Basically the initialization consists of two steps :
 * <p>The first step is to create the <code>GWidget</code> using the standard constructor or the <code>GWidgetFactory</code>. It gives the <code>GWidget</code> its shape and initializes all the mechanisms behind.</p>
 * <p>The second step is to call the <code>loadXML</code> method which will bind some important characteristics of the widget to the xml skeleton passed as argument to the method. This xml part will be used for example to fill the
 * field of a textfield with the current value in the tree or by looking in the schema for the tag retrieving the available values for a combobox. This step will give the <code>GWidget</code> a meaning in the xml editor.</p>
 * 
 * @author Torrente Guillaume & Marc Bouissou
 * @see GWidgetFactory
 *
 */
public abstract class GWidget extends JPanel implements GObject {
	
	private static final long serialVersionUID = 1L;

	//The parent widget
	/**
	 * @uml.property  name="parent"
	 * @uml.associationEnd  
	 */
	protected GObject parent;
	
	//The xml root which edited in this widget
	/**
	 * @uml.property  name="xmlRoot"
	 * @uml.associationEnd  
	 */
	protected Element xmlRoot;
	
	//The type of the widget
	//protected WidgetClasses widgetClass;
	
	//The object used to store information about the widget
	/**
	 * @uml.property  name="information"
	 * @uml.associationEnd  
	 */
	protected GObjectInformation information; 
	
	/**
	 * The default constructor of <code>GWidget</code>.
	 */
	public GWidget() {
		this.parent = null;
		this.xmlRoot = null;
		this.information = null;
	}
	
	/**
	 * This constructor is the most used one. It takes as argument the parent of the <code>GWidget</code> which allows the widget to communicate with its parent and therefore perform load and save operations.
	 * @param p The parent of the widget.
	 */
	public GWidget(GObject p, GObjectInformation info) {
		this.parent = p;
		this.xmlRoot = null;
		this.information = info;
	}
	
	/**
	 * All the widgets have to implement this function because it the only way to bind the widget components (Swing Objects or other <code>GWidget</code>) to xml code.
	 * @param e The xml element(s) which has(ve) to be loaded and bind to the <code>GWidget</code>.
	 * @param deeplyRooted This argument indicated whether the xml which has to be load is under a root or directly available in the first argument. 
	 * @return A boolean indicating if the process failed or succeed
	 */
	public abstract boolean loadXML(Vector<Element> e, boolean deeplyRooted);
	
	/**
	 * All the widgets have to implement this function because when a <code>GWidget</code> has to save the xml it has edited it will call recursively this method on all the <code>GWidget</code> it may contain.
	 * @return The xml contained in the <code>GWidget</code> which has been formatted to the BDC standards.
	 */
	public abstract Vector<Element> saveXML();
}
