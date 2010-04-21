/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 24 March 2010                                  
 * Author       : D.WEYAND/ALL4TEC                               
 * Bug Id       : n°45                                          
 * Modification : Modify fillDocument()(process language information) 
 * VF Version   : 1.3                
 * **************************************************************
 * Date         : 13 April 2010                            
 * Author       : D.WEYAND/ALL4TEC                              
 * Bug Id       :                                         
 * Modification : Code cleanup to avoid warnings
 * VF version   : 1.7
 
 * **************************************************************
 * Date         : 16 April 2010                            
 * Author       : M.BOUISSOU/EDF                              
 * Bug Id       :                                         
 * Modification : Fix syntax name bug in "bdcfr.xsd" 
 * VF version   : 1.71
 * **************************************************************/

package GWindow;

import jEditInterface.VisualFigaro;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Vector;

//import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import Factories.GXMLElementFactory;
import global.ControlTypes;
import global.ListTypes;
import global.NameRetrieverClasses;
import global.WidgetClasses;
import global.WindowClasses;
//import GLanguage.GLanguage;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidgetControledList;
import GWidget.GWidgetGridForm;
import GWidget.GWidgetTextArea;
import GXMLLoader.GXMLLoader;
import GXSDOperations.GXSDOperations;

public class GWindowMain extends GWindow {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * \ The JMenuBar          * \
	 * @uml.property  name="menuBar"
	 * @uml.associationEnd  
	 */
	private JMenuBar menuBar;
	/**
	 * @uml.property  name="menu"
	 * @uml.associationEnd  
	 */
	private JMenu menu;
	/**
	 * @uml.property  name="saveItem"
	 * @uml.associationEnd  
	 */
	private JMenuItem saveItem;
	/**
	 * @uml.property  name="exitItem"
	 * @uml.associationEnd  
	 */
	private JMenuItem exitItem;
	
	/******************************\
	\******************************/
	
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
	//The widget containing the description of the knowledge base
	/**
	 * @uml.property  name="descriptionTextArea"
	 * @uml.associationEnd  
	 */
	private GWidgetTextArea descriptionTextArea;
	//And the declaration of the two controled lists
	/**
	 * @uml.property  name="visualizationNamesList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList visualizationNamesList;
	/**
	 * @uml.property  name="menuItemNamesList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList menuItemNamesList;
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
	 * @uml.property  name="typeList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList typeList;
	/******************************\
	\******************************/
	
	
	/**
	 * \ Panel of the third tab    * \
	 * @uml.property  name="thirdPanel"
	 * @uml.associationEnd  
	 */
	private JPanel thirdPanel;
	//The widgets containing the lists
	/**
	 * @uml.property  name="faultTreeGenerationModelsList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList faultTreeGenerationModelsList;
	/**
	 * @uml.property  name="simulationModelsList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList simulationModelsList;
	/**
	 * @uml.property  name="figaro0SinstanciationModelsList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList figaro0SinstanciationModelsList;
	/**
	 * @uml.property  name="externalTreatmentsModelsList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList externalTreatmentsModelsList;
	/******************************\
	\******************************/
	
	
	/**
	 * \ Panel of the fourth tab    * \
	 * @uml.property  name="fourthPanel"
	 * @uml.associationEnd  
	 */
	private JPanel fourthPanel;
	//The widget containing the list
	/**
	 * @uml.property  name="algoList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList algoList;
	/******************************\
	\******************************/
	
	/**
	 * \ For the communication with the plugin    * \
	 * @uml.property  name="visualFigaro"
	 * @uml.associationEnd  
	 */
	private VisualFigaro visualFigaro;
	/***********************************************\
	\***********************************************/
	
	@Deprecated
	public GWindowMain(GObject p, GObjectInformation info) {
		super(p, info);
		
		initialization();
	}
	
	public GWindowMain(GObject p, GObjectInformation info, int pos) {
		super(p, info);
		
		initialization();
	}
	
	public GWindowMain(VisualFigaro vf, GObject p, GObjectInformation info, int pos) {
		super(p, info);
		
		//Initialization of the class variable
		visualFigaro = vf;
		
		//Initialization of the XSD manipulator
		GXSDOperations.setLanguage(info.getLanguage().getLanguage());
		
		initialization();
	}
	
	private void initialization() {

		/*******************************\
		 *  Figaro initialization      *
		\*******************************/

		for(Iterator<String> iter = (figaroLoader.findSteps()).iterator(); iter.hasNext();)
			System.out.println("L'etape est : " + iter.next());
		
		/*******************************\
		\*******************************/
		
		
		/*******************************\
		 *  Graphical initialization   *
		\*******************************/
		
		//First of all we initialize the JMenuBar
		this.initializeMenuBar();
		this.setJMenuBar(menuBar);
		
		//Then we initialize the tabbedPane
		this.tabs = new JTabbedPane();
		
		//Then comes the turn of the first tab
		this.initializeFirstPanel();
		tabs.add("General", firstPanel);
		
		//And the initialization of the second tab
		this.initializeSecondPanel();
		tabs.add("Classes", secondPanel);
		
		//And the initialization of the third tab
		this.initializeThirdPanel();
		tabs.add("Models", thirdPanel);
		
		//And the initialization of the last, the fourth tab
		this.initializeFourthPanel();
		tabs.add("Algorithms", fourthPanel);
		
		//Finally we add the tabs panel to the frame panel
		this.framePanel.add(tabs, BorderLayout.CENTER);
		
		this.add(this.framePanel);
		
		this.pack();
		this.setTitle("BDC Editor");
		
		/*******************************\
		\*******************************/
		
		this.loadXml(xmlLoader.getRootElement());
	}
	
	private void initializeMenuBar() {
		
		//First we create the menuBar
		menuBar = new JMenuBar();
		
		//Then the menu
		menu = new JMenu("Menu");
		
		//The saveItem
		saveItem = new JMenuItem("Save");
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveXmlToFile();
			}
		});
		menu.add(saveItem);
		
		//And finally the exitItem
		exitItem = new JMenuItem("Exit without saving");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		menu.add(exitItem);
		
		menuBar.add(menu);
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
		labels.add("Name :");
		labels.add("Author :");
		labels.add("Creation Date :");
		labels.add("Modification Date :");
		labels.add("Step RM :");
		labels.add("Step EI :");
		
		//Set the type of widget we want in the grid widget
		Vector<WidgetClasses> widgetClasses = new Vector<WidgetClasses>();
		widgetClasses.add(WidgetClasses.TEXTFIELD);
		widgetClasses.add(WidgetClasses.TEXTFIELD);
		widgetClasses.add(WidgetClasses.FORMATTEDTEXTFIELD);
		widgetClasses.add(WidgetClasses.FORMATTEDTEXTFIELD);
		widgetClasses.add(WidgetClasses.COMBO);
		widgetClasses.add(WidgetClasses.COMBO);
		
		Vector<Vector<Object>> objectArgs = new Vector<Vector<Object>>();
		objectArgs.add(null);
		objectArgs.add(null);
		objectArgs.add(null);
		objectArgs.add(null);
		Vector<Object> values = new Vector<Object>();
		values.add(information.getLanguage().getBDCTranslation("Etape par defaut"));
		values.addAll(figaroLoader.findSteps());
		objectArgs.add(values);
		objectArgs.add(values);
		
		//Creation of the grid panel with four columns and 3 lines
		generalCharacteristicsGridForm = new GWidgetGridForm(this, information, labels, 4, 3, widgetClasses, objectArgs); 
		//Finally we add the gridform to the panel
		firstPanel.add(generalCharacteristicsGridForm, BorderLayout.NORTH);
	}
	private void initializeFirstPanelBorderedWidgets() {
		//In order to structure the panel we will use a grid panel for the three big composants
		JPanel bufferPanel = new JPanel(new GridLayout(3,1,5,5));
		
		//Then we create the widget and add it to the first panel
		descriptionTextArea = new GWidgetTextArea(this, information, "Description");
		bufferPanel.add(descriptionTextArea);
		
		//Then we create the visualization list and add it to the first panel
		visualizationNamesList = new GWidgetControledList(this, information, "Visualizations", null, ListTypes.SIMPLEARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDELUPDOWN, WindowClasses.NAMESIMPLE, null);
		bufferPanel.add(visualizationNamesList);
		
		//We do the same for the menu item list and add it to the first panel
		menuItemNamesList = new GWidgetControledList(this, information, "Menu Items", null, ListTypes.SIMPLEARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDELUPDOWN, WindowClasses.NAMESIMPLE, null);
		bufferPanel.add(menuItemNamesList);
		
		//Then we add this panel to the first panel
		firstPanel.add(bufferPanel, BorderLayout.CENTER);
	}
	
	private void initializeSecondPanel() {
		//First we initialize the panel
		secondPanel = new JPanel(new BorderLayout());
		
		//And then we create and add the controled list to the panel
		typeList = new GWidgetControledList(this, information, "Types", figaroLoader.findTypesName(), ListTypes.COMPLEXARRAY, NameRetrieverClasses.TYPENODELINKRETRIEVER, ControlTypes.NODELINKNEITHERUPDOWNEDIT, null, null);
		secondPanel.add(typeList, BorderLayout.CENTER);
	}
	
	//http://cermics.enpc.fr/polys/info1/main/node33.html passage par valeur sous java!! BRAVO m(_ _)m quelle maitrise
	
	private void initializeThirdPanel() {
		//First we initialize the panel
		thirdPanel = new JPanel(new GridLayout(4,1,5,5));
		
		//And then we create and add the controled lists to the panel
		faultTreeGenerationModelsList = new GWidgetControledList(this, information, "Fault Trees Generation Models", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.NAMERETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.FAULTTREEGENERATIONMODEL, null);
		thirdPanel.add(faultTreeGenerationModelsList);
		simulationModelsList = new GWidgetControledList(this, information, "Simulation Models", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.NAMERETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.SIMULATIONMODEL, null);
		thirdPanel.add(simulationModelsList);
		figaro0SinstanciationModelsList = new GWidgetControledList(this, information, "Figaro 0 Instanciation Models", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.NAMERETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.FIGARO0INSTANCIATIONMODEL, null);
		thirdPanel.add(figaro0SinstanciationModelsList);
		externalTreatmentsModelsList = new GWidgetControledList(this, information, "External Treatments Models", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.NAMERETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.EXTERNALTREATMENTMODEL, null);
		thirdPanel.add(externalTreatmentsModelsList);
	}
	
	private void initializeFourthPanel() {
		//First we initialize the panel
		fourthPanel = new JPanel(new BorderLayout());
		
		//And then we create and add the controled list to the panel
		algoList = new GWidgetControledList(this, information, "Algorithms", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.ALGORETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.ALGO, null);
		fourthPanel.add(algoList, BorderLayout.CENTER);
	}
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
			case OK:
				fillDocument();
				//###En attente de traduction complete
				//parent.translateMessage(Messages.ADDELEMENT, new Object[]{position, root});
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
		this.root = new Element(information.getLanguage().getBDCTranslation("BDC"));
		this.root.addNamespaceDeclaration(Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance"));
		
		String currentlanguage = new String(information.getLanguage().getLanguage().toString());
		String xsdfilename = new String("bdcfr.xsd");
		if (currentlanguage.equals("English"))
		   xsdfilename = "bdceng.xsd";

	    this.root.setAttribute("noNamespaceSchemaLocation", xsdfilename, Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance"));
		//Save elements from the fieldsPanel. The main vector is cut in more small pieces in order to fill the root element in the right order.
		Vector<Element> generalCharacteristicsGridFormVector = new Vector<Element>(generalCharacteristicsGridForm.saveXML());
		
		//Save nom
		GXMLElementFactory.saveElement(root, generalCharacteristicsGridFormVector.get(0));

		//Save elements from the description textArea
		GXMLElementFactory.saveElements(root, descriptionTextArea.saveXML());
		
		//Save auteur
		GXMLElementFactory.saveElement(root, generalCharacteristicsGridFormVector.get(1));
		
		//Save date
		GXMLElementFactory.saveElement(root, generalCharacteristicsGridFormVector.get(2));
		GXMLElementFactory.saveElement(root, generalCharacteristicsGridFormVector.get(3));
	
		//GXMLElementFactory.saveElement(root, generalCharacteristicsGridFormVector.get(1));

		//Save the elements from the fault tree generation models list
		GXMLElementFactory.saveElements(root, faultTreeGenerationModelsList.saveXML());
		
		//Save the elements from the simulation models list
		GXMLElementFactory.saveElements(root, simulationModelsList.saveXML());
		
		//Save the elements from the Figaro 0 instanciation models list
		GXMLElementFactory.saveElements(root, figaro0SinstanciationModelsList.saveXML());
		
		//Save the elements from the external treatments models list
		GXMLElementFactory.saveElements(root, externalTreatmentsModelsList.saveXML());
		
		GXMLElementFactory.saveElement(root, generalCharacteristicsGridFormVector.get(4));
		GXMLElementFactory.saveElement(root, generalCharacteristicsGridFormVector.get(5));
		
		//Save the elements from the typeOD list
		GXMLElementFactory.saveElements(root, visualizationNamesList.saveXML());
		
		//Save the elements from the systemNames list dix mina
		GXMLElementFactory.saveElements(root, menuItemNamesList.saveXML());
		
		//Save the elements from the types list
		GXMLElementFactory.saveElements(root, typeList.saveXML());
		
		//Save the algorithms list
		GXMLElementFactory.saveElements(root, algoList.saveXML());
		
		return root;
	}
	
	public void loadXml(Element e) {
		
		//System.err.println("Voici le nom : " + e.getName() + " 1 " + e.getText() + " 2 " + e.getChildren().get(0) + " 3");

		//Find the elements from the XML
		Vector<Element> fieldsLoad = new Vector<Element>();
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("NOM")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("AUTEUR")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("DATE_CREATION")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("DATE_MODIFICATION")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("ETAPE_RM")).get(0));
		fieldsLoad.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("ETAPE_EI")).get(0));
		generalCharacteristicsGridForm.loadXML(fieldsLoad, false);
		
		//Initialize the description textarea
		descriptionTextArea.loadXML(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("DESCRIPTION")), false);
		
		//Takes care of initializing the lists on the first tab
		Vector<Element> listLoad = new Vector<Element>();
		listLoad.add(e);
		
		//visualizationNamesList
		visualizationNamesList.loadXML(GXMLElementFactory.refactorElementsOnlyChildren(listLoad, information.getLanguage().getBDCTranslation("VISUALISATION")), false);
		menuItemNamesList.loadXML(GXMLElementFactory.refactorElementsOnlyChildren(listLoad, information.getLanguage().getBDCTranslation("FAMILLE_TYPE_PALETTE")), false);

		//Takes care of initializing the lists on the second tab
		typeList.loadXML(GXMLElementFactory.refactorElements(listLoad, information.getLanguage().getBDCTranslation("TYPE")), false);
		
		//Takes care of initializing the lists on the third tab
		faultTreeGenerationModelsList.loadXML(GXMLElementFactory.refactorElements(listLoad, information.getLanguage().getBDCTranslation("MODELE_GENERATION_ADD")), false);
		simulationModelsList.loadXML(GXMLElementFactory.refactorElements(listLoad, information.getLanguage().getBDCTranslation("MODELE_SIMULATION")), false);
		figaro0SinstanciationModelsList.loadXML(GXMLElementFactory.refactorElements(listLoad, information.getLanguage().getBDCTranslation("MODELE_INST_FIG0")), false);
		externalTreatmentsModelsList.loadXML(GXMLElementFactory.refactorElements(listLoad, information.getLanguage().getBDCTranslation("MODELE_TRAITEMENT_EXTERNE")), false);
		
		//Takes care of initializing the lists on the fourth tab
		algoList.loadXML(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("LISTE_ALGO")), true);
	}
	
	public void loadXml(String fileName) {
		GXMLLoader xl = new GXMLLoader(information.getLanguage());
		root = xl.loadXmlFileJDOM(fileName).getRootElement();
	}
	
	public void saveXmlToFile() {
		fillDocument();
		
		String fileName = visualFigaro.getCurrentSelectedTree().substring(0, visualFigaro.getCurrentSelectedTree().length() - 2 >= 0 ? visualFigaro.getCurrentSelectedTree().length() - 2 : 0) + "bdc";
		
		try {
			FileOutputStream fichier = new FileOutputStream(fileName);
			
		    XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat().setEncoding("UTF-8"));
	
		    sortie.output(new Document(root), fichier);
		    fichier.close();
		    
		} catch (java.io.IOException ioe) {
			System.out.println("VisualFigaro : GWindowMain : Cannot save xml to file : " + ioe);
		}
		
		//Now we update the icons and repaint the tree
		visualFigaro.setIconsUpToDate();
		visualFigaro.repaintFigTree();
	}
}
