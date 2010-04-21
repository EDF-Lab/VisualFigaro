package GWindow;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdom.Element;

import Factories.GXMLElementFactory;
import GException.GExceptionElement;
import global.ControlTypes;
import global.ListTypes;
import global.Messages;
import global.NameRetrieverClasses;
import global.WindowClasses;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidgetControledList;
import GWidget.GWidgetOKCancel;
import GWidget.GWidgetTextField;


public class GWindowReglesType extends GWindow {

	private static final long serialVersionUID = 1L;
	
	//The OK/Cancel buttons
	/**
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetOKCancel okCancelWidget;
	
	//The panel containing the textfield and the lists
	/**
	 * @uml.property  name="generalPanel"
	 * @uml.associationEnd  
	 */
	private JPanel generalPanel;
	/**
	 * @uml.property  name="nameTextField"
	 * @uml.associationEnd  
	 */
	private GWidgetTextField nameTextField;
	/**
	 * @uml.property  name="equationsList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList equationsList;
	/**
	 * @uml.property  name="departsList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList departsList;
	/**
	 * @uml.property  name="entreesList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList entreesList;
	/**
	 * @uml.property  name="sortiesList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList sortiesList;
	
	/**
	 * @uml.property  name="position"
	 */
	private int position;
	
	@Deprecated
	public GWindowReglesType(GObject p, GObjectInformation info) {
		super(p, info);
		this.position = -1;
		
		initialization();
	}
	
	public GWindowReglesType(GObject p, GObjectInformation info, int pos) {
		super(p, info);
		this.position = pos;
		
		initialization();
	}
	
	private void initialization() {
		
		for(Iterator<String> iter = (figaroLoader.findSteps()).iterator(); iter.hasNext();)
			System.out.println("L'etape est : " + iter.next());		
		
		this.initializeGeneralPanel();
		this.framePanel.add(generalPanel, BorderLayout.CENTER);
		
		okCancelWidget = new GWidgetOKCancel(this, information);
		this.framePanel.add(okCancelWidget, BorderLayout.SOUTH);
		
		this.add(this.framePanel);
		
		this.pack();
		this.setTitle("Regles Type");
		
		this.loadXml(null);
	}
	
	public void initializeGeneralPanel() {
		
		//Initialization of the general panel with a gridbag layout
		generalPanel = new JPanel(new BorderLayout());
		
		//Initialization of the textfield with its label
		JPanel textPanel = new JPanel(new BorderLayout());
		JLabel nameTextFieldLabel = new JLabel("Name : ");
		nameTextField = new GWidgetTextField(this, information);
		textPanel.add(nameTextFieldLabel, BorderLayout.WEST);
		textPanel.add(nameTextField, BorderLayout.CENTER);
		
		//A panel with a grid layout for all the lists
		JPanel listsGridPanel = new JPanel(new GridLayout(4,1,5,5));
		
		//Initialization of the equations list
		equationsList = new GWidgetControledList(this, information, null, null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.EQUATIONRETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.EQUATION, null);
		listsGridPanel.add(equationsList);
		
		//Initialization of the depart list
		departsList = new GWidgetControledList(this, information, null, null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.RULERETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.DEPART, null);
		listsGridPanel.add(departsList);
				
		//Initialization of the depart list
		entreesList = new GWidgetControledList(this, information, null, null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.RULERETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.ENTREE, null);
		listsGridPanel.add(entreesList);
				
		//Initialization of the depart list
		sortiesList = new GWidgetControledList(this, information, null, null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.RULERETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.SORTIE, null);
		listsGridPanel.add(sortiesList);
				
		//Then we add everything
		generalPanel.add(textPanel, BorderLayout.NORTH);
		generalPanel.add(listsGridPanel, BorderLayout.CENTER);
	}
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
			case OK:
				fillDocument();
				parent.translateMessage(new GMessage(information, Messages.ADDELEMENT, new Object[]{position, root}));
				dispose();
				break;
			
			case CANCEL:
				dispose();
				break;
				
			default:
				System.out.println("VisualFigaro : GWindowDepart : Unknown message received");
		}
	}
	
	public Element fillDocument() {
		//Element bufferElement;
		
		//Creation of the new root
		this.root = new Element(information.getLanguage().getBDCTranslation("REGLES_TYPE"));
		
		//Save the XML from widgets
		GXMLElementFactory.saveElements(root, nameTextField.saveXML());
		GXMLElementFactory.saveElements(root, equationsList.saveXML());
		GXMLElementFactory.saveElements(root, departsList.saveXML());
		GXMLElementFactory.saveElements(root, entreesList.saveXML());
		GXMLElementFactory.saveElements(root, sortiesList.saveXML());
		
		return root;
	}
	
	public void loadXml(Element e) {
		
		//We create a dummy vector to store the element
		Vector<Element> elemVect = new Vector<Element>();
		elemVect.add(e);

		//Then we load the xml
		try {
			equationsList.loadXML(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("EQUATIONS")), true);
			departsList.loadXML(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("DEPART")), true);
			entreesList.loadXML(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("ENTREE")), true);
			sortiesList.loadXML(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("SORTIE")), true);
			nameTextField.loadXML(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("NOM_TYPE")), false);
		} catch (GExceptionElement ex) {
			System.out.println("VisualFigaro : GWindowReglesType : " + ex);
		}
	}
}
