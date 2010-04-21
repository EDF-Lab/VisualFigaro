package GWindow;


import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JPanel;

import org.jdom.Element;

import Factories.GXMLElementFactory;
import global.Messages;
import global.WidgetClasses;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidgetGridForm;
import GWidget.GWidgetOKCancel;

public class GWindowReglesTypeIfThenElse extends GWindow {

	private static final long serialVersionUID = 1L;
	
	//The OK/Cancel buttons
	/**
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetOKCancel okCancelWidget;	
	
	/**
	 * @uml.property  name="generalPanel"
	 * @uml.associationEnd  
	 */
	private JPanel generalPanel;
	
	//The xml tag which will be used on the top of the window
	/**
	 * @uml.property  name="xmlTag"
	 */
	private String xmlTag;
	
	//The position in the list if the window is used to edit an element of a list
	/**
	 * @uml.property  name="position"
	 */
	private int position;
	
	//The gridform widget to put all the fields
	/**
	 * @uml.property  name="gridForm"
	 * @uml.associationEnd  
	 */
	private GWidgetGridForm gridForm;
	
	@Deprecated
	public GWindowReglesTypeIfThenElse(GObject p, GObjectInformation info) {
		super(p, info);
		this.position = -1;
		
		initialization();
	}
	
	public GWindowReglesTypeIfThenElse(GObject p, GObjectInformation info, String name, int pos) {
		super(p, info);
		this.position = pos;
		
		initialization();
		this.setTitle(name);
	}
	
	private void initialization() {
		
		this.xmlTag = "";
		
		this.initializeGeneralPanel();
		this.framePanel.add(generalPanel, BorderLayout.CENTER);
		
		this.okCancelWidget = new GWidgetOKCancel(this, information);
		this.framePanel.add(okCancelWidget, BorderLayout.SOUTH);
		
		this.add(this.framePanel);
		
		this.pack();
		this.setTitle("IfThenElse Form");
		
		this.loadXml(null);
	}
	
	public void initializeGeneralPanel() {
		
		generalPanel = new JPanel(new BorderLayout());
		
		//Creation of the labels
		Vector<String> labels = new Vector<String>();
		labels.add("Equation Name :");
		labels.add("Given :");
		labels.add("If :");
		labels.add("Then :");
		labels.add("Else :");
		
		//Set the type of widget we want in the grid widget
		Vector<WidgetClasses> widgetClasses = new Vector<WidgetClasses>();
		for(int i=0; i<5; ++i)
			widgetClasses.add(WidgetClasses.TEXTFIELD);
		
		//Creation of the gridform
		gridForm = new GWidgetGridForm(this, information, labels, 1, 5, widgetClasses, null/*, xmlKeyword*/);
		
		//Finally we add the gridform to the panel
		generalPanel.add(gridForm);
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
		this.root = new Element(xmlTag);
		
		//Save elements from the general panel
		GXMLElementFactory.saveElements(root, gridForm.saveXML());
		
		return root;
	}
	
	public void loadXml(Element e) {
		
		if(e != null)
			xmlTag = e.getName();
		
		//Find the elements from the XML
		Vector<Element> fieldsLoad = new Vector<Element>();
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("NOM_REGLE")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("SOIT")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("SI")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("ALORS")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("SINON")).get(0));
		gridForm.loadXML(fieldsLoad, false);
	}
}
