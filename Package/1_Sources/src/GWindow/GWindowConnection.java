/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 24 June 2015                            
 * Author       : L.RAFFAELLI/ALL4TEC                              
 * Bug Id       : n°73                                        
 * Modification : Change of data structure to have the output interface name in an INTERFACE structure
 * VF version   : 2.0
 * **************************************************************/

package GWindow;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JPanel;

import global.ControlTypes;
import global.ListTypes;
import global.Messages;
import global.NameRetrieverClasses;
import global.WidgetClasses;
import global.WindowClasses;

import org.jdom.Element;

import Factories.GXMLElementFactory;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidgetControledList;
import GWidget.GWidgetGridForm;
import GWidget.GWidgetOKCancel;

public class GWindowConnection extends GWindow {

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
	 * @uml.property  name="nodeInterfacesFilledByLinkList"
	 * @uml.associationEnd  
	 */
	GWidgetControledList nodeInterfacesFilledByLinkList;
	/**
	 * @uml.property  name="linkInterfacesFilledByNodeList"
	 * @uml.associationEnd  
	 */
	GWidgetControledList linkInterfacesFilledByNodeList;
	
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
	public GWindowConnection(GObject p, GObjectInformation info) {
		super(p, info);
	}
	
	public GWindowConnection(GObject p, GObjectInformation info, int pos) {
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
		this.setTitle("Connection");
		
		this.loadXml(null);
	}
	
	public void initializeFieldsPanel() {
		
		//Creation of the labels
		Vector<String> labels = new Vector<String>();
		labels.add("Name :");
		labels.add("Link :");
		
		//Set the type of widget we want in the grid widget
		Vector<WidgetClasses> widgetClasses = new Vector<WidgetClasses>();
		widgetClasses.add(WidgetClasses.COMBO);
		widgetClasses.add(WidgetClasses.COMBO);
		
		//Give some default values to the combo
		Vector<Vector<Object>> objectArgs = new Vector<Vector<Object>>();
		
		Vector<Object> name = new Vector<Object>();
		name.add("DEPART");
		name.add("ARRIVEE");
		
		objectArgs.add(name);
		
		Vector<Object> values = new Vector<Object>();
		values.addAll(xmlLoader.findLinks());
		
		//Add FIGARO in the list
		boolean figaro=false;
		for(int i=0;i<values.size();i++){
			if(values.get(i).equals("FIGARO"))
				figaro=true;
		}
		if(!figaro)
			values.add("FIGARO");
		
		objectArgs.add(values);
		
		//Creation of the grid panel
		gridForm = new GWidgetGridForm(this, information, labels, 2, 2, widgetClasses, objectArgs); 
	}
	
	public void initializeListPanel() {
		
		//Initialization of the list panel
		listsPanel = new JPanel(new GridLayout(2,2,5,5));
		
		//Initialization of the OD list
		nodeInterfacesFilledByLinkList = new GWidgetControledList(this, information, "Interface of the node filled by the link", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.INTERFACE, information.getTypeConcerned());
		listsPanel.add(nodeInterfacesFilledByLinkList);
		
		//Initialization of the system types list
		//if(xmlLoader.findLinks().size() > 0)
		//linkInterfacesFilledByNodeList = new GWidgetControledList(this, information, "Interface of the link filled by the node", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.INTERFACE, xmlLoader.findLinks().get(0));
		//else
		linkInterfacesFilledByNodeList = new GWidgetControledList(this, information, "Interface of the link filled by the node", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.INTERFACE, null);
		listsPanel.add(linkInterfacesFilledByNodeList);
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
				
			case NOTIFYCHANGE:
				
				//First we retrieve the message if it is not null
				if(message.getArguments().get(0) != null && (linkInterfacesFilledByNodeList != null || nodeInterfacesFilledByLinkList != null)) {
				
					//First we get the arguments of the widget
					String selectedItem = (String)message.getArguments().get(0);
					int emiter = -1;
					try {
						emiter = Integer.parseInt(message.getSender().getLastPartOfThePath());
					} catch (NumberFormatException e) {
						return;
					}
					
					if(emiter == 1) {
						System.out.println("NOTIFYCHANGE : " + emiter + " : " + selectedItem);
						
						//We create a message to update the controlled list with the new list of interfaces
						message = new GMessage(information, Messages.SETWINDOWARGUMENTS, selectedItem);
					
						//Then we update them
						//if(nodeInterfacesFilledByLinkList != null)
						//	nodeInterfacesFilledByLinkList.translateMessage(message);
					
						linkInterfacesFilledByNodeList.translateMessage(message);
					}
					
				}
				break;
				
			default:
				System.out.println("VisualFigaro : GWindowDepart : Unknown message received");
		}
	}
	
	public Element fillDocument() {
		this.root = new Element(information.getLanguage().getBDCTranslation("CONNEXION_ACCEPTEE"));
		
		//Save elements from the fieldsPanel
		GXMLElementFactory.saveElements(root, gridForm.saveXML());
		
		//Save the elements from the typeOD list
		GXMLElementFactory.saveElements(root, nodeInterfacesFilledByLinkList.saveXML());
		
		//Save the elements from the systemNames list
		GXMLElementFactory.saveElements(root, linkInterfacesFilledByNodeList.saveXML());
		
		return root;
	}
	
	public void loadXml(Element e) {
		
		//Find the elements from the XML
		Vector<Element> fieldsLoad = new Vector<Element>();
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("NOM")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("TYPE_POINT_CONNEXION")).get(0));
		gridForm.loadXML(fieldsLoad, false);
		
		//Takes care of initializing the lists
		nodeInterfacesFilledByLinkList.loadXML(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("REGLE_NOEUD_LIEN")), true);
		linkInterfacesFilledByNodeList.loadXML(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("REGLE_LIEN_NOEUD")), true);
	}
}
