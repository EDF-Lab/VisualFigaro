package GWindow;

import global.Messages;
import global.WidgetClasses;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdom.Element;

import Factories.GXMLElementFactory;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidgetModifiableGridForm;
import GWidget.GWidgetOKCancel;

public class GWindowModeleTraitementExterne extends GWindow {

	private static final long serialVersionUID = 1L;
	
	//The ok/cancel panel
	/**
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetOKCancel okCancelWidget;
	
	//Les composants de la fenetre de generation d'arbres de defaillances
	//The various components of the fault tree generation window
	/**
	 * @uml.property  name="modifiableGridForm"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetModifiableGridForm modifiableGridForm;
	
	//The two checkboxes used in this window to not create another complicated panel
	/**
	 * @uml.property  name="addTextField"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JTextField addTextField;
	/**
	 * @uml.property  name="figaro0TextField"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JTextField figaro0TextField;
	
	//Keep a trace from the parent window adn other parameter from parent window
	/**
	 * @uml.property  name="position"
	 */
	private int position;
	
	//A textfield for the name of the object
	/**
	 * @uml.property  name="nameTextField"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JTextField nameTextField;
	
	public GWindowModeleTraitementExterne(GObject p, GObjectInformation info) {
		super(p, info);
		this.position = -1;
		
		initialize();
	}
	
	public GWindowModeleTraitementExterne(GObject p, GObjectInformation info, Element e, int pos) {
		super(p, info);
		this.position = pos;
		
		initialize();
	}
	
	public void initialize() {
		
		//Just a simple name panel on the top of the window
		JPanel namePanel = new JPanel(new BorderLayout());
		namePanel.add(new JLabel("Name : "), BorderLayout.WEST);
		nameTextField = new JTextField();
		namePanel.add(nameTextField, BorderLayout.CENTER);
		this.framePanel.add(namePanel, BorderLayout.NORTH);
		
		//Initialization of the main panel
		
		//We have to create a gridbaglayout to emulate the modifiablegridform
		JPanel fakeModifiableGridForm = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 5;
		constraints.gridheight = 3;
		
		//First we create a vector for the label
		Vector<Vector<String>> args = new Vector<Vector<String>>();
		Vector<String> labels = new Vector<String>();
		labels.add("Treatment Rules Groups : ");
		labels.add("");
		args.add(labels);
		//Then the vector for the widget type
		Vector<WidgetClasses> widgetClasses = new Vector<WidgetClasses>();
		widgetClasses.add(WidgetClasses.CONTROLEDLISTADDDEL);
		//And the vector for the parameters of each widget
		Vector<Vector<Object>> parameters = new Vector<Vector<Object>>();
		parameters.add(null);
		//Now we can create the gridform
		modifiableGridForm = new GWidgetModifiableGridForm(this, information, args, widgetClasses, parameters);
		fakeModifiableGridForm.add(modifiableGridForm, constraints);
		
		//We update the constraints and create the add checkboxpanel
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		fakeModifiableGridForm.add(new JLabel("Add : "), constraints);
		constraints.gridx = 1;
		addTextField = new JTextField();
		fakeModifiableGridForm.add(addTextField, constraints);
		
		//We update the constraints and create the figaro 0 checkboxpanel
		constraints.gridx = 0;
		constraints.gridy = 4;
		fakeModifiableGridForm.add(new JLabel("Figaro 0 : "), constraints);
		constraints.gridx = 1;
		figaro0TextField = new JTextField();
		fakeModifiableGridForm.add(figaro0TextField, constraints);
		
		//Then we add the fake panel to the top panel
		this.framePanel.add(fakeModifiableGridForm, BorderLayout.CENTER);
		
		//Initialization of the ok/cancel panel
		okCancelWidget = new GWidgetOKCancel(this, information);
		this.framePanel.add(okCancelWidget, BorderLayout.SOUTH);
		
		this.add(this.framePanel);
		this.pack();
		this.setTitle("External Treatment Model");
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
		
		//A buffer element
		Element elem;
		
		//First we initialize the root
		this.root = new Element(information.getLanguage().getBDCTranslation("MODELE_TRAITEMENT_EXTERNE"));
		
		//Then we have to take care of the name independently of other widgets
		elem = new Element(information.getLanguage().getBDCTranslation("NOM"));
		elem.setText(nameTextField.getText());
		GXMLElementFactory.saveElement(root, elem);
		
		//Then we have to save the gridForm
		GXMLElementFactory.saveElements(root, modifiableGridForm.saveXML());
		
		//Take care of the add combo
		elem = new Element(information.getLanguage().getBDCTranslation("ADD"));
		elem.setText(addTextField.getText());
		GXMLElementFactory.saveElement(root, elem);
		
		//Take care of the figaro 0 combo
		elem = new Element(information.getLanguage().getBDCTranslation("FIG0"));
		elem.setText(figaro0TextField.getText());
		GXMLElementFactory.saveElement(root, elem);
		
		return root;
	}
	
	public void loadXml(Element e) {

		if(e.getChild(information.getLanguage().getBDCTranslation("NOM")) != null)
			nameTextField.setText(e.getChildText(information.getLanguage().getBDCTranslation("NOM")));
		
		Vector<Element> fieldsLoad = new Vector<Element>();
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("CODES")).get(0));
		modifiableGridForm.loadXML(fieldsLoad, false);
		
		if(e.getChild(information.getLanguage().getBDCTranslation("ADD")) != null)
			addTextField.setText(e.getChildText(information.getLanguage().getBDCTranslation("ADD")));
		
		if(e.getChild(information.getLanguage().getBDCTranslation("FIG0")) != null)
			figaro0TextField.setText(e.getChildText(information.getLanguage().getBDCTranslation("FIG0")));
	}
}
