/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 24 March 2010                                  
 * Author       : D.WEYAND/ALL4TEC                               
 * Bug Id       : n°24 & 44                                           
 * Modification : Move the <FAMILLE_TYPE_PALETTE> tags just after
 *                the <ABBREVIATION> tag  
 * VF Version   : 1.3               
 * **************************************************************
 * Date         :                                  
 * Author       :                                
 * Bug Id       :                                           
 * Modification :    
 * **************************************************************/

package GWindow;

import global.ControlTypes;
import global.ListTypes;
import global.Messages;
import global.NameRetrieverClasses;
import global.WidgetClasses;
import global.WindowClasses;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jdom.Element;

import Factories.GXMLElementFactory;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidgetControledList;
import GWidget.GWidgetGridForm;
import GWidget.GWidgetOKCancel;
import GWidget.GWidgetTextArea;

public class GWindowNode extends GWindow {

	private static final long serialVersionUID = 1L;
	
	/**
	 * \ TabbedPane for the tabs    * \
	 * @uml.property  name="tabs"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JTabbedPane tabs;	
	
	/******************************\
	\******************************/
	
	
	/**
	 * \ Panel of the first tab    * \
	 * @uml.property  name="firstPanel"
	 * @uml.associationEnd  
	 */
	private JPanel firstPanel;
	//The widget containing the fields
	/**
	 * @uml.property  name="generalCharacteristicsGridForm"
	 * @uml.associationEnd  
	 */
	private GWidgetGridForm generalCharacteristicsGridForm;
	//The widget containing the description of the type
	/**
	 * @uml.property  name="descriptionTextArea"
	 * @uml.associationEnd  
	 */
	private GWidgetTextArea descriptionTextArea;
	//And the declaration of the two controled lists
	/**
	 * @uml.property  name="menuItemNamesList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList menuItemNamesList;
	/**
	 * @uml.property  name="variantesGraphiquesList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList variantesGraphiquesList;
	
	/******************************\
	\******************************/
	
	
	/**
	 * \ Panel of the second tab    * \
	 * @uml.property  name="secondPanel"
	 * @uml.associationEnd  
	 */
	private JPanel secondPanel;
	//The widget containing the list
	/**
	 * @uml.property  name="portsList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList portsList;
	/**
	 * \ \
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	
	//The ok/cancel widget
	private GWidgetOKCancel okCancelWidget;
	
	//The position of the window
	/**
	 * @uml.property  name="position"
	 */
	private int position;
	
	//The name of the node being edited
	/**
	 * @uml.property  name="nodeName"
	 */
	private String nodeName;
	
	@Deprecated
	public GWindowNode(GObject p, GObjectInformation info) {
		super(p, info);
		
		initialization();
	}
	
	public GWindowNode(GObject p, GObjectInformation info, int pos) {
		super(p, info);
	
		this.position = pos;
		this.nodeName = info.getTypeConcerned();
		
		initialization();
	}
	
	private void initialization() {
		
		//First of all we initialize the tabbedPane
		this.tabs = new JTabbedPane();
		
		//Then comes the turn of the first tab
		this.initializeFirstPanel();
		tabs.add("General", firstPanel);
		
		//And the initialization of the second tab
		this.initializeSecondPanel();
		tabs.add("Ports", secondPanel);
		
		//Finally we add the tabs panel to the frame panel and the frame panel to the window
		this.framePanel.add(tabs, BorderLayout.CENTER);

		//We now have to create the okcancel panel
		okCancelWidget = new GWidgetOKCancel(this, information);
		this.framePanel.add(okCancelWidget, BorderLayout.SOUTH);
		
		this.add(this.framePanel);
		
		this.pack();
		this.setTitle("Node : " + this.nodeName);
	}
	
	private void initializeFirstPanel() {
		//First we initialize the panel with a border layout manager
		firstPanel = new JPanel(new BorderLayout());
		
		//Then we initialize and add the fields to the first panel
		this.initializeFirstPanelFields();
		
		//Then we add the text area and the two controlled list for the visualizations and the menu items
		initializeFirstPanelBorderedWidgets();
	}
	
	private void initializeFirstPanelFields() {
		
		//Creation of the labels
		Vector<String> labels = new Vector<String>();
		labels.add("Abbreviation :");
		labels.add("Presence Palette :");
		labels.add("Default Variant :");
		labels.add("Graphic Instance :");
		
		//Set the type of widget we want in the grid widget
		Vector<WidgetClasses> widgetClasses = new Vector<WidgetClasses>();
		widgetClasses.add(WidgetClasses.TEXTFIELD);
		widgetClasses.add(WidgetClasses.COMBO);
		widgetClasses.add(WidgetClasses.EDITDEFAULTVAR);
		widgetClasses.add(WidgetClasses.COMBO);
		
		Vector<Vector<Object>> objectArgs = new Vector<Vector<Object>>();
		objectArgs.add(null);
		Vector<Object> values = new Vector<Object>();
		values.add("TRUE");
		values.add("FALSE");
		objectArgs.add(values);
		values = new Vector<Object>();
		values.add("Edit Default GV");
		values.add(true);
		objectArgs.add(values);
		values = new Vector<Object>();
		values.add("TRUE");
		values.add("FALSE");
		objectArgs.add(values);
		
		//Creation of the grid panel with four columns and 3 lines
		generalCharacteristicsGridForm = new GWidgetGridForm(this, information, labels, 4, 2, widgetClasses, objectArgs); 
		//Finally we add the gridform to the panel
		firstPanel.add(generalCharacteristicsGridForm, BorderLayout.NORTH);
	}
	
	private void initializeFirstPanelBorderedWidgets() {
		//In order to structure the panel we will use a grid panel for the three big composants
		JPanel bufferPanel = new JPanel(new GridLayout(3,1,5,5));
		
		//First we create
		descriptionTextArea = new GWidgetTextArea(this, information, "Description");
		bufferPanel.add(descriptionTextArea);
		
		//Then we create the visualization list and add it to the first panel
		//menuItemNamesList = new GWidgetControledList(this, information, "Menu Items", null, ListTypes.SIMPLEARRAY, NameRetrieverClasses.DIRECTRETRIEVER, ControlTypes.ADDDEL, WindowClasses.MENU, null);
		menuItemNamesList = new GWidgetControledList(this, information, "Menu Items", null, ListTypes.SIMPLEARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDEL, WindowClasses.MENU, null);
		bufferPanel.add(menuItemNamesList);
		
		//We clear the argument vector to put the name on the border of the widget
		variantesGraphiquesList = new GWidgetControledList(this, information, "Graphic Variants", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.NAMERETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.VARNODE, null);
		bufferPanel.add(variantesGraphiquesList);
		
		//Then we add this panel to the first panel
		firstPanel.add(bufferPanel, BorderLayout.CENTER);
	}
	
	private void initializeSecondPanel() {
		//First we initialize the panel
		secondPanel = new JPanel(new BorderLayout());
		
		//And then we create and add the controled list to the panel
		portsList = new GWidgetControledList(this, information, "Ports", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.NAMERETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.PORTNODE, null);
		secondPanel.add(portsList, BorderLayout.CENTER);
	}
	
	//http://cermics.enpc.fr/polys/info1/main/node33.html passage par valeur sous java!! BRAVO m(_ _)m quelle maitrise
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
			case OK:
				fillDocument();
				//###En attente de traduction complete
				parent.translateMessage(new GMessage(information, Messages.NODEMODIFICATION, new Object[]{position, root}));
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
		this.root = new Element(information.getLanguage().getBDCTranslation("TYPE"));
		
		//All the element from the general Characteristic panel the systemNames and variantesGraphiques lists are saved just under the root
		//There is a particular case for the node name which is stored in the class parameters
		Element name = new Element(information.getLanguage().getBDCTranslation("NOM"));
		name.setText(nodeName);
		GXMLElementFactory.saveElement(root, name);
		
		//Save elements from the fieldsPanel. The main vector is cut in more small pieces in order to fill the root element in the right order.
		Vector<Element> generalCharacteristicsGridFormVector = new Vector<Element>(generalCharacteristicsGridForm.saveXML());
		//root.addContent(generalCharacteristicsGridForm.saveXML());
		GXMLElementFactory.saveElement(root, generalCharacteristicsGridFormVector.get(1));
		GXMLElementFactory.saveElement(root, generalCharacteristicsGridFormVector.get(0));
		GXMLElementFactory.saveElements(root, menuItemNamesList.saveXML());
		
		GXMLElementFactory.saveElements(root, descriptionTextArea.saveXML());
		GXMLElementFactory.saveElement(root, generalCharacteristicsGridFormVector.get(2));
			
		GXMLElementFactory.saveElements(root, variantesGraphiquesList.saveXML());
		
		//The element from the variante graphique lists and the ports lists are saved under a specific element called NODE
		Element element = new Element(information.getLanguage().getBDCTranslation("NOEUD"));
		GXMLElementFactory.saveElement(element, generalCharacteristicsGridFormVector.get(3));
		GXMLElementFactory.saveElements(element, variantesGraphiquesList.saveXML());
		GXMLElementFactory.saveElements(element, portsList.saveXML());
		
		GXMLElementFactory.saveElement(root, element);
		
		return root;
	}
	
	public void loadXml(Element e) {
		
		//Find the elements from the XML
		Vector<Element> fieldsLoad = new Vector<Element>();
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("ABREVIATION")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("PRESENCE_PALETTE")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("VARIANTE_GRAPHIQUE_DEFAUT")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("INSTANCE_GRAPHIQUE")).get(0));
		generalCharacteristicsGridForm.loadXML(fieldsLoad, false);
		
		//Takes care of initializing the text area
		descriptionTextArea.loadXML(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("DESCRIPTION")), false);
		
		//Takes care of initializing the lists in the first tab
		Vector<Element> elem = new Vector<Element>();
		elem.add(e);
		menuItemNamesList.loadXML(GXMLElementFactory.refactorElements(elem, information.getLanguage().getBDCTranslation("FAMILLE_TYPE_PALETTE")), false);
		variantesGraphiquesList.loadXML(GXMLElementFactory.refactorElements(elem, information.getLanguage().getBDCTranslation("VARIANTE_GRAPHIQUE")), false);

		//Now it is the turn of the list in the second tab
		portsList.loadXML(GXMLElementFactory.refactorElements(elem, information.getLanguage().getBDCTranslation("POINT_CONNEXION")), false);
	}
}
