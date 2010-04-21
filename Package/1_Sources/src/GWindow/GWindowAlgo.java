package GWindow;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JPanel;

import org.jdom.Element;

import Factories.GXMLElementFactory;
import global.ControlTypes;
import global.ListTypes;
import global.Messages;
import global.NameRetrieverClasses;
import global.WidgetClasses;
import global.WindowClasses;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidgetControledList;
import GWidget.GWidgetGridForm;
import GWidget.GWidgetOKCancel;

/**
 * This window is used to fill the xml part concerning the Algorithms which tells how to use the algorithms.
 * @author Guillaume Torrente & Marc Bouissou
 * @see GWindow
 */
public class GWindowAlgo extends GWindow {
	
	private static final long serialVersionUID = 1L;
	
	//The GWidget grid form
	/**
	 * @uml.property  name="gridForm"
	 * @uml.associationEnd  
	 */
	private GWidgetGridForm gridForm;

	//Panel for the lists
	/**
	 * @uml.property  name="listsPanel"
	 * @uml.associationEnd  
	 */
	private JPanel listsPanel;
	//And the declaration of all the lists
	/**
	 * @uml.property  name="typeODList"
	 * @uml.associationEnd  
	 */
	GWidgetControledList typeODList;
	/**
	 * @uml.property  name="systemNamesList"
	 * @uml.associationEnd  
	 */
	GWidgetControledList systemNamesList;
	/**
	 * @uml.property  name="reglesTypeList"
	 * @uml.associationEnd  
	 */
	GWidgetControledList reglesTypeList;
	
	//Panel for the ok/cancel buttons
	/**
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  
	 */
	private GWidgetOKCancel okCancelWidget;
	
	//The position of the element in case of edition
	/**
	 * @uml.property  name="position"
	 */
	private int position;
	
	@Deprecated
	public GWindowAlgo(GObject p, GObjectInformation info) {
		super(p, info);
	}
	
	public GWindowAlgo(GObject p, GObjectInformation info, int pos) {
		super(p, info);
		
		position = pos;
		
		initialization();
	}
	
	private void initialization() {
		
		this.initializeFieldsPanel();
		this.framePanel.add(gridForm, BorderLayout.NORTH);
		
		this.initializeListPanel();
		this.framePanel.add(listsPanel, BorderLayout.CENTER);
		
		okCancelWidget = new GWidgetOKCancel(this, information);
		this.framePanel.add(okCancelWidget, BorderLayout.SOUTH);
		
		this.add(this.framePanel);
		
		this.pack();
		this.setTitle("Algorithms");
		
		this.loadXml(null);
	}
	
	public void initializeFieldsPanel() {
		
		//Creation of the labels
		Vector<String> labels = new Vector<String>();
		labels.add("Name :");
		labels.add("Loop :");
		labels.add("Composant Type :");
		labels.add("bo Interface Composant :");
		labels.add("bo Interface Aval :");
		labels.add("bo Interface Amont :");
		labels.add("comp Interface Aval :");
		labels.add("comp Interface Amont :");
		labels.add("comp Interface Bo :");
		labels.add("type Nommage :");
		labels.add("interface Nommage :");
		
		//Set the type of widget we want in the grid widget
		Vector<WidgetClasses> widgetClasses = new Vector<WidgetClasses>();
		for(int i=0; i<11; ++i)
			widgetClasses.add(WidgetClasses.TEXTFIELD);
		
		//Creation of the grid panel
		gridForm = new GWidgetGridForm(this, information, labels, 4, 6, widgetClasses, null); 
	}
	
	public void initializeListPanel() {
		
		//Initialization of the list panel
		listsPanel = new JPanel(new GridLayout(2,2,5,5));
		
		//Initialization of the OD list
		typeODList = new GWidgetControledList(this, information, "type OD", null, ListTypes.SIMPLEARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.NAMESIMPLE, null);
		listsPanel.add(typeODList);
		
		//Initialization of the system types list
		systemNamesList = new GWidgetControledList(this, information, "Systems Names", null, ListTypes.SIMPLEARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.NAMESIMPLE, null);
		listsPanel.add(systemNamesList);
		
		//Initialization of the regles type list
		reglesTypeList = new GWidgetControledList(this, information, "Regle Types", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.TYPERETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.REGLESTYPE, null);
		listsPanel.add(reglesTypeList);
	}
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
			case OK:
				fillDocument();
				//###En attente de traduction complete
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
		this.root = new Element(information.getLanguage().getBDCTranslation("ALGORITHME_OD"));
		
		//Save elements from the fieldsPanel
		GXMLElementFactory.saveElements(root, gridForm.saveXML());

		//Save the elements from the typeOD list
		GXMLElementFactory.saveElements(root, typeODList.saveXML());
		
		//Save the elements from the systemNames list
		GXMLElementFactory.saveElements(root, systemNamesList.saveXML());
		
		//Save the elements from the reglesType list
		GXMLElementFactory.saveElements(root, reglesTypeList.saveXML());
		
		return root;
	}
	
	
	public void loadXml(Element e) {
		
		//Find the elements from the XML
		Vector<Element> fieldsLoad = new Vector<Element>();
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("NOM_ALGO")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("BOUCLE")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("TYPE_COMPOSANT")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("BO_INTERFACE_COMP")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("BO_INTERFACE_AVAL")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("BO_INTERFACE_AMONT")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("COMP_INTERFACE_AVAL")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("COMP_INTERFACE_AMONT")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("COMP_INTERFACE_BO")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("TYPE_NOMMAGE")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("INTERFACE_NOMMAGE")).get(0));
		gridForm.loadXML(fieldsLoad, false);
		
		//Takes care of initializing the lists
		Vector<Element> bufferVect = new Vector<Element>();
		bufferVect.add(e);
		typeODList.loadXML(GXMLElementFactory.refactorElements(bufferVect, information.getLanguage().getBDCTranslation("TYPE_OD")), false);
		systemNamesList.loadXML(GXMLElementFactory.refactorElements(bufferVect, information.getLanguage().getBDCTranslation("NOM_SYSTEME")), false);
		reglesTypeList.loadXML(GXMLElementFactory.refactorElements(bufferVect, information.getLanguage().getBDCTranslation("REGLES_TYPE")), false);
	}
}
