package GWindow;

import global.ControlTypes;
import global.ListTypes;
import global.Messages;
import global.NameRetrieverClasses;
import global.WidgetClasses;
import global.WindowClasses;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.Vector;

import javax.swing.JPanel;

import org.jdom.Element;

import Factories.GXMLElementFactory;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidgetControledList;
import GWidget.GWidgetGridForm;
import GWidget.GWidgetOKCancel;

public class GWindowLinkPort extends GWindow {

	private static final long serialVersionUID = 1L;
	
	//The OK/Cancel buttons
	/**
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetOKCancel okCancelWidget;	
	
	//We define a cardlayout in order to switch between the user defined panel and the inherited panel
	/**
	 * @uml.property  name="cardPanel"
	 * @uml.associationEnd  
	 */
	private JPanel cardPanel;
	/**
	 * @uml.property  name="cardLayout"
	 */
	private CardLayout cardLayout;
	
	//The combo panel
	/**
	 * @uml.property  name="comboPanel"
	 * @uml.associationEnd  
	 */
	private JPanel comboPanel;
	//The gridform to show the two combo boxes in a pretty format
	/**
	 * @uml.property  name="comboGridForm"
	 * @uml.associationEnd  
	 */
	private GWidgetGridForm comboGridForm;
	
	//The controledlist widget to show the interfaces
	/**
	 * @uml.property  name="interfacesLinkFilledByStartingNodeControledList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList interfacesLinkFilledByStartingNodeControledList;
	/**
	 * @uml.property  name="interfacesLinkFilledByEndingNodeControledList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList interfacesLinkFilledByEndingNodeControledList;
	/**
	 * @uml.property  name="interfacesEndingNodeFilledByLinkControledList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList interfacesEndingNodeFilledByLinkControledList;
	/**
	 * @uml.property  name="interfacesEndingNodeFilledByStartingNodeControledList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList interfacesEndingNodeFilledByStartingNodeControledList;
	/**
	 * @uml.property  name="interfacesStartingNodeFilledByLinkControledList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList interfacesStartingNodeFilledByLinkControledList;
	/**
	 * @uml.property  name="interfacesStartingNodeFilledByEndingNodeControledList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList interfacesStartingNodeFilledByEndingNodeControledList;
	
	//The position in the list if the window is used to edit an element of a list
	/**
	 * @uml.property  name="position"
	 */
	private int position;
	
	//A boolean to indicate if it is the window represents a starting port or an ending port
	/**
	 * @uml.property  name="isStart"
	 */
	private boolean isStart;
	
	
	/**
	 * \ VARIABLES TO MANAGE THE COMMUNICATION BETWEEN THE WIDGETS * \
	 * @uml.property  name="numberOfItems"
	 */
	//The number of items in the lists
	private int numberOfItems;
	
	@Deprecated
	public GWindowLinkPort(GObject p, GObjectInformation info) {
		super(p, info);
		//this.xmlTag = "";
		this.position = -1;
		isStart = true;
		numberOfItems = 0;
		initialization();
	}
	
	public GWindowLinkPort(GObject p, GObjectInformation info, int pos, boolean isStart) {
		super(p, info);
		//this.xmlTag = e.getName();
		this.position = pos;
		this.isStart = isStart;
		numberOfItems = 0;
		initialization();
	}
	
	private void initialization() {
		this.initializeComboPanel();
		this.framePanel.add(comboPanel, BorderLayout.NORTH);
		
		this.initializeListPanel();
		this.framePanel.add(cardPanel, BorderLayout.CENTER);
		
		this.okCancelWidget = new GWidgetOKCancel(this, information);
		this.framePanel.add(okCancelWidget, BorderLayout.SOUTH);
		
		this.add(this.framePanel);
		
		this.pack();
		if(isStart)
			this.setTitle("Start Port Window");
		else
			this.setTitle("End Port Window");
		
		this.loadXml(null);
	}
	
	public void initializeComboPanel() {
		
		//First we initialize the panel
		comboPanel = new JPanel(new BorderLayout());
		
		//Creation of the labels
		Vector<String> labels = new Vector<String>();
		labels.add("Name : ");
		labels.add("Class : ");
		labels.add("Interface : ");
		
		//Set the type of widget we want in the grid widget
		Vector<WidgetClasses> widgetClasses = new Vector<WidgetClasses>();
		widgetClasses.add(WidgetClasses.TEXTFIELD);
		widgetClasses.add(WidgetClasses.COMBO);
		widgetClasses.add(WidgetClasses.COMBO);
		
		Vector<Vector<Object>> parameters = new Vector<Vector<Object>>();
		parameters.add(null);
		Vector<Object> types = new Vector<Object>();
		
		//Add FIGARO in the list
		boolean figaro=false;
		for(String s : xmlLoader.findNodes()){
			types.add(s);
			if(s.equals("FIGARO"))
				figaro=true;
		}
		if(!figaro)
			types.add("FIGARO");
		
		parameters.add(types);
		Vector<Object> interfaces = new Vector<Object>();
		interfaces.add("Interfaces of the link filled by the starting node");
		interfaces.add("Interfaces of the link filled by the ending node");
		interfaces.add("Interfaces of the ending node filled by the link");
		interfaces.add("Interfaces of the ending node filled by the starting node");
		interfaces.add("Interfaces of the starting node filled by the link");
		interfaces.add("Interfaces of the starting node filled by the ending node");
		parameters.add(interfaces);
		
		//Then we initialize the gridform
		comboGridForm = new GWidgetGridForm(this, information, labels, 2, 3, widgetClasses, parameters);
		
		//Finally we add te gridform to the panel
		comboPanel.add(comboGridForm, BorderLayout.NORTH);
	}
	
	public void initializeListPanel() {
		
		//First we initialize the card panel which will contain all the lists
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		
		//Creation of the controledlists
		GObjectInformation listInformation = new GObjectInformation(information);
		listInformation.addStepToPath("interfaceList");
		interfacesLinkFilledByStartingNodeControledList = new GWidgetControledList(this, listInformation, "Interfaces of the link filled by the starting node", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDEL, WindowClasses.INTERFACE, information.getTypeConcerned());
		interfacesLinkFilledByEndingNodeControledList = new GWidgetControledList(this, listInformation, "Interfaces of the link filled by the ending node", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDEL, WindowClasses.INTERFACE, information.getTypeConcerned());
		interfacesEndingNodeFilledByLinkControledList = new GWidgetControledList(this, listInformation, "Interfaces of the ending node filled by the link", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDEL, WindowClasses.INTERFACE, xmlLoader.findNodes().get(0));
		interfacesEndingNodeFilledByStartingNodeControledList = new GWidgetControledList(this, listInformation, "Interfaces of the ending node filled by the starting node", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDEL, WindowClasses.INTERFACE, xmlLoader.findNodes().get(0));
		interfacesStartingNodeFilledByLinkControledList = new GWidgetControledList(this, listInformation, "Interfaces of the ending node filled by the starting node", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDEL, WindowClasses.INTERFACE, xmlLoader.findNodes().get(0));
		interfacesStartingNodeFilledByEndingNodeControledList = new GWidgetControledList(this, listInformation, "Interfaces of the starting node filled by the ending node", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDEL, WindowClasses.INTERFACE, xmlLoader.findNodes().get(0));
		
		//Then we add them to the cardPanel
		cardPanel.add(interfacesLinkFilledByStartingNodeControledList, "Interfaces of the link filled by the starting node");
		cardPanel.add(interfacesLinkFilledByEndingNodeControledList, "Interfaces of the link filled by the ending node");
		cardPanel.add(interfacesEndingNodeFilledByLinkControledList, "Interfaces of the ending node filled by the link");
		cardPanel.add(interfacesEndingNodeFilledByStartingNodeControledList, "Interfaces of the ending node filled by the starting node");
		cardPanel.add(interfacesStartingNodeFilledByLinkControledList, "Interfaces of the starting node filled by the link");
		cardPanel.add(interfacesStartingNodeFilledByEndingNodeControledList, "Interfaces of the starting node filled by the ending node");
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
				
			case NOTIFYCHANGE:
				if(message.getArguments().get(0) != null && comboPanel != null) {
				
					//First we get the arguments of the widget
					String selectedItem = (String)message.getArguments().get(0);
					String emiter = message.getSender().getLastPartOfThePath();
					
					if(emiter.equals("1")) {
						
						System.out.println("NOTIFYCHANGE : " + emiter + " : " + selectedItem);
						
						//We create a message to update the controled list with the new list of interfaces
						message = new GMessage(information, Messages.SETWINDOWARGUMENTS, selectedItem);
						
						//Then we update them
						if(interfacesEndingNodeFilledByLinkControledList != null)
							interfacesEndingNodeFilledByLinkControledList.translateMessage(message);
						
						if(interfacesEndingNodeFilledByStartingNodeControledList != null)
							interfacesEndingNodeFilledByStartingNodeControledList.translateMessage(message);
						
						if(interfacesStartingNodeFilledByLinkControledList != null)
							interfacesStartingNodeFilledByLinkControledList.translateMessage(message);
						
						if(interfacesStartingNodeFilledByEndingNodeControledList != null)
							interfacesStartingNodeFilledByEndingNodeControledList.translateMessage(message);
					}
					
					if(emiter.equals("2")) {
						System.out.println("NOTIFYCHANGE : " + emiter + " : " + selectedItem);
						
						if(cardLayout != null)
							cardLayout.show(cardPanel, selectedItem);
					}
					
					if(emiter.equals("interfaceList")) {
						
						//We check what kind of message has been received and we increment or decrement the counter according to its type
						if(selectedItem.equals("DEL")) {
							numberOfItems--;
						}
						if(selectedItem.equals("ADD")) {
							numberOfItems++;
						}

						//Then we create the message to update the comboBox trough the GridForm
						GMessage messageToBeSent;
						if(numberOfItems > 0)
							messageToBeSent = new GMessage(null, Messages.SETMODIFIABLE, false);
						else
							messageToBeSent = new GMessage(null, Messages.SETMODIFIABLE, true);
						
						if(comboGridForm != null)
							comboGridForm.translateMessage(new GMessage(null, Messages.SENDTOWIDGET, new Object[]{1, messageToBeSent}));
					}
				}
				break;
				
			default:
				System.out.println("VisualFigaro : GWindowLinkPort : Unknown message received");
		}
	}
	
	public Element fillDocument() {
		//Creation of the new root
		this.root = new Element(information.getLanguage().getBDCTranslation("CONNEXION_ACCEPTEE"));
		
		//Save elements from the gridform
		GXMLElementFactory.saveElements(root, comboGridForm.saveXML());
		
		//Save elements from the lists
		GXMLElementFactory.saveElements(root, interfacesLinkFilledByStartingNodeControledList.saveXML());
		GXMLElementFactory.saveElements(root, interfacesLinkFilledByEndingNodeControledList.saveXML());
		GXMLElementFactory.saveElements(root, interfacesEndingNodeFilledByLinkControledList.saveXML());
		GXMLElementFactory.saveElements(root, interfacesEndingNodeFilledByStartingNodeControledList.saveXML());
		GXMLElementFactory.saveElements(root, interfacesStartingNodeFilledByLinkControledList.saveXML());
		GXMLElementFactory.saveElements(root, interfacesStartingNodeFilledByEndingNodeControledList.saveXML());
		
		return root;
	}
	
	public void loadXml(Element e) {
		
		//We load the xml into the gridform
		Vector<Element> fieldsLoad = new Vector<Element>();
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("NOM")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("TYPE_POINT_CONNEXION")).get(0));
		fieldsLoad.add(null);
		comboGridForm.loadXML(fieldsLoad, false);
		
		//We load the xml into the lists
		Vector<Element> bufferVect = new Vector<Element>();
		bufferVect.add(e);
		interfacesLinkFilledByStartingNodeControledList.loadXML(GXMLElementFactory.refactorElements(bufferVect, information.getLanguage().getBDCTranslation("REGLE_LIEN_DEPART")), true);
System.err.println("NB ELEMENT 1 : "+interfacesLinkFilledByStartingNodeControledList.GetNumberOfElement());
		interfacesLinkFilledByEndingNodeControledList.loadXML(GXMLElementFactory.refactorElements(bufferVect, information.getLanguage().getBDCTranslation("REGLE_LIEN_ARRIVEE")), true);
System.err.println("NB ELEMENT 2 : "+interfacesLinkFilledByEndingNodeControledList.GetNumberOfElement());
		interfacesEndingNodeFilledByLinkControledList.loadXML(GXMLElementFactory.refactorElements(bufferVect, information.getLanguage().getBDCTranslation("REGLE_ARRIVEE_LIEN")), true);
		interfacesEndingNodeFilledByStartingNodeControledList.loadXML(GXMLElementFactory.refactorElements(bufferVect, information.getLanguage().getBDCTranslation("REGLE_ARRIVEE_DEPART")), true);
		interfacesStartingNodeFilledByLinkControledList.loadXML(GXMLElementFactory.refactorElements(bufferVect, information.getLanguage().getBDCTranslation("REGLE_DEPART_LIEN")), true);
		interfacesStartingNodeFilledByEndingNodeControledList.loadXML(GXMLElementFactory.refactorElements(bufferVect, information.getLanguage().getBDCTranslation("REGLE_DEPART_ARRIVEE")), true);
	}
}
