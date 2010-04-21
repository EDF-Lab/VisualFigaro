package GWindow;

import java.awt.BorderLayout;
import java.util.Vector;

import global.ControlTypes;
import global.ListTypes;
import global.Messages;
import global.NameRetrieverClasses;
import global.WindowClasses;

import org.jdom.Element;

import Factories.GXMLElementFactory;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidgetControledList;
import GWidget.GWidgetOKCancel;

public class GWindowLinkAcceptedConnections extends GWindow {

private static final long serialVersionUID = 1L;
	
	//And the declaration of all the lists
	/**
	 * @uml.property  name="acceptedConnectionsList"
	 * @uml.associationEnd  
	 */
	GWidgetControledList acceptedConnectionsList;
	
	//Panel for the ok/cancel buttons
	/**
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetOKCancel okCancelWidget;
	
	//The position of the element in case of edition
	/**
	 * @uml.property  name="position"
	 */
	private int position;
	
	//To indicate if the window is for a start port or an ending port
	/**
	 * @uml.property  name="isStart"
	 */
	private boolean isStart;
	
	@Deprecated
	public GWindowLinkAcceptedConnections(GObject p, GObjectInformation info) {
		super(p, info);
		
		initialization();
	}
	
	public GWindowLinkAcceptedConnections(GObject p, GObjectInformation info, int pos, boolean start) {
		super(p, info);
		
		position = pos;
		isStart = start;
		
		initialization();
	}
	
	private void initialization() {
		
		this.initializeListPanel();
		this.framePanel.add(acceptedConnectionsList, BorderLayout.CENTER);
		
		okCancelWidget = new GWidgetOKCancel(this, information);
		this.framePanel.add(okCancelWidget, BorderLayout.SOUTH);
		
		this.add(this.framePanel);
		
		this.pack();
		this.setTitle("Accepted Connections");
		
		this.loadXml(null);
	}
	
	public void initializeListPanel() {
		
		//Initialization of the OD list
		acceptedConnectionsList = new GWidgetControledList(this, information, "Accepted Connections", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.NAMERETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.PORTLINK, isStart);
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
				
				break;
				
			default:
				System.out.println("VisualFigaro : GWindowDepart : Unknown message received");
		}
	}
	
	public Element fillDocument() {
		
		if(isStart)
			this.root = new Element(information.getLanguage().getBDCTranslation("POINT_DEPART"));
		else
			this.root = new Element(information.getLanguage().getBDCTranslation("POINT_ARRIVEE"));
		
		//Save the elements from the systemNames list
		GXMLElementFactory.saveElements(root, acceptedConnectionsList.saveXML());
		
		return root;
	}
	
	public void loadXml(Element e) {
		
		//Takes care of initializing the lists
		Vector<Element> bufferVect = new Vector<Element>();
		bufferVect.add(e);
		acceptedConnectionsList.loadXML(GXMLElementFactory.refactorElements(bufferVect, information.getLanguage().getBDCTranslation("CONNEXION_ACCEPTEE")), false);
	}
}
