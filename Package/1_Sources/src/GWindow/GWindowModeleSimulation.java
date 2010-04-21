package GWindow;

import global.Messages;
import global.WidgetClasses;

import java.awt.BorderLayout;
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

public class GWindowModeleSimulation extends GWindow {
	
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
	
	public GWindowModeleSimulation(GObject p, GObjectInformation info) {
		super(p, info);
		this.position = -1;
		
		initialize();
	}
	
	public GWindowModeleSimulation(GObject p, GObjectInformation info, Element e, int pos) {
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
		
		//First we create a vector for the label
		Vector<Vector<String>> args = new Vector<Vector<String>>();
		Vector<String> labels = new Vector<String>();
		labels.add("Rules Run Number : ");
		labels.add("Value : ");
		args.add(labels);
		labels = new Vector<String>();
		labels.add("Failure Treatment : ");
		labels.add("Value : ");
		args.add(labels);
		labels = new Vector<String>();
		labels.add("Fault-Tree Behaviour : ");
		labels.add("Value : ");
		args.add(labels);
		labels = new Vector<String>();
		labels.add("Treatment Rules Groups : ");
		labels.add("");
		args.add(labels);
		labels = new Vector<String>();
		labels.add("Allowed Visualizations : ");
		labels.add("");
		args.add(labels);
		
		//Then the vector for the widget type
		Vector<WidgetClasses> widgetClasses = new Vector<WidgetClasses>();
		widgetClasses.add(WidgetClasses.TEXTFIELD);
		widgetClasses.add(WidgetClasses.COMBO);
		widgetClasses.add(WidgetClasses.COMBO);
		widgetClasses.add(WidgetClasses.CONTROLEDLISTADDDEL);
		widgetClasses.add(WidgetClasses.CONTROLEDLISTADDDEL);
		
		//And the vector for the parameters of each widget
		Vector<Vector<Object>> parameters = new Vector<Vector<Object>>();
		Vector<Object> parameter = new Vector<Object>();
		parameter.addAll(figaroLoader.findSteps());
		parameters.add(null);
		parameters.add(parameter);
		parameters.add(parameter);
		parameters.add(null);
		parameters.add(null);
		
		//Now we can create the gridform
		modifiableGridForm = new GWidgetModifiableGridForm(this, information, args, widgetClasses, parameters);
		this.framePanel.add(modifiableGridForm, BorderLayout.CENTER);
		
		//Initialization of the ok/cancel panel
		okCancelWidget = new GWidgetOKCancel(this, information);
		this.framePanel.add(okCancelWidget, BorderLayout.SOUTH);
		
		this.add(this.framePanel);
		this.pack();
		this.setTitle("Simulation Model");
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
		this.root = new Element(information.getLanguage().getBDCTranslation("MODELE_SIMULATION"));
		
		//Then we have to take care of the name independently of other widgets
		elem = new Element(information.getLanguage().getBDCTranslation("NOM"));
		elem.setText(nameTextField.getText());
		GXMLElementFactory.saveElement(root, elem);
		
		//Then we have to save the gridForm
		GXMLElementFactory.saveElements(root, modifiableGridForm.saveXML());
		
		return root;
	}
	
	public void loadXml(Element e) {
		
		if(e.getChild(information.getLanguage().getBDCTranslation("NOM")) != null)
			nameTextField.setText(e.getChildText(information.getLanguage().getBDCTranslation("NOM")));
		
		Vector<Element> fieldsLoad = new Vector<Element>();
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("NB_TOURS_REGLES")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("PRISE_EN_COMPTE_DEFAILLANCES")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("ASSURER_COHERENCE_ADD")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("GROUPES_REGLES_TRAITEMENT")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("VISUALISATIONS_AUTORISEES")).get(0));
		
		modifiableGridForm.loadXML(fieldsLoad, false);
	}
}
