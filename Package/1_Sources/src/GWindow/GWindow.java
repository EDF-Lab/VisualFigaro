package GWindow;

import java.awt.BorderLayout;


import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jdom.Element;

import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GXMLLoader.GXMLLoaderFigaro;
import GXMLLoader.GXMLLoader;

public abstract class GWindow extends JFrame implements GObject {

	private static final long serialVersionUID = 1L;

	//The parent object. It can be either a widget or a window
	/**
	 * @uml.property  name="parent"
	 * @uml.associationEnd  
	 */
	protected GObject parent;
	
	//The top level panel
	/**
	 * @uml.property  name="framePanel"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected JPanel framePanel;
	
	//The root element filled and explored in the window
	/**
	 * @uml.property  name="root"
	 * @uml.associationEnd  
	 */
	protected Element root;
	
	//All the windows have access to those two variables which represents the figaro and the bdc file edited in the current session 
	protected static GXMLLoader xmlLoader;
	protected static GXMLLoaderFigaro figaroLoader;
	
	//The object used to store information about the window
	/**
	 * @uml.property  name="information"
	 * @uml.associationEnd  
	 */
	protected GObjectInformation information;
	
	public GWindow() {
		parent = null;
		information = null;
		initialization();
	}
	
	public GWindow(GObject p, GObjectInformation info) {
		parent = p;
		information = info;
		initialization();
	}
	
	private void initialization() {
		root = null;
		framePanel = new JPanel(new BorderLayout());
	}
	
	public static void setXmlLoader(GXMLLoader xl) {
		xmlLoader = xl;
	}
	
	public static void setFigaroLoader(GXMLLoaderFigaro fl) {
		figaroLoader = fl;
	}
	
	public abstract void loadXml(Element e);
	
	public abstract Element fillDocument();
}
