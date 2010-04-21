/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 25 March 2010                                  
 * Author       : D.WEYAND/ALL4TEC                               
 * Bug Id       :                                            
 * Modification : Suppress "Text" tab 
 * VF Version   : 1.3               
 * **************************************************************
 * Date         : 29 March 2010                              
 * Author       : D.WEYAND/ALL4TEC                               
 * Bug Id       : 49                                        
 * Modification : Add missing ICONCHOOSED management for Node GV
 * VF Version   : 1.4
 * **************************************************************
 * Date         : 1 April 2010                              
 * Author       : D.WEYAND/ALL4TEC                               
 * Bug Id       : 50 & 41                                       
 * Modification : Get only file name for .sym (suppress path and ext)
 *                + Correction from new specification 30 March 2010
 * VF Version   : 1.5
 * **************************************************************/

package GWindow;

import global.ControlTypes;
import global.ListTypes;
import global.Messages;
import global.NameRetrieverClasses;
import global.WidgetClasses;
import global.WindowClasses;

import java.awt.BorderLayout;
import java.awt.CardLayout;
//import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;

import org.jdom.Element;

import com.ctreber.aclib.image.ico.BitmapDescriptor;
import com.ctreber.aclib.image.ico.ICOFile;

import Factories.GXMLElementFactory;
import GIcon.GIcon;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidgetControledList;
import GWidget.GWidgetExpression;
import GWidget.GWidgetGridForm;
import GWidget.GWidgetLoadIcon;
import GWidget.GWidgetOKCancel;
import GWidget.GWidgetTextField;

public class GWindowVarNode extends GWindow {

	private static final long serialVersionUID = 1L;
	
	//On the top of everything there is a JMenuBar
	/**
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
	 * @uml.property  name="inheritanceItem"
	 * @uml.associationEnd  
	 */
	private JRadioButtonMenuItem inheritanceItem;
	/**
	 * @uml.property  name="definitionItem"
	 * @uml.associationEnd  
	 */
	private JRadioButtonMenuItem definitionItem;
	
	//The ok/cancel panel
	/**
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetOKCancel okCancelWidget;
	
	//We define a cardlayout in order to switch between the user defined panel and the inherited panel
	/**
	 * @uml.property  name="cardPanel"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JPanel cardPanel;
	/**
	 * @uml.property  name="cardLayout"
	 */
	private CardLayout cardLayout;
	
	/**
	 * \ The first layout concerned the inheritance           * \
	 * @uml.property  name="inheritancePanel"
	 * @uml.associationEnd  
	 */
	
	//In the inheritance layout there is just a panel with a gridform
	private JPanel inheritancePanel;
	/**
	 * @uml.property  name="inheritanceGridForm"
	 * @uml.associationEnd  
	 */
	private GWidgetGridForm inheritanceGridForm;
	
	
	/***************************************************************\
	\***************************************************************/
	
	
	
	
	/***************************************************************\
	 *        The second layout concerned the definition           *
	\***************************************************************/
	
	
	/**
	 * \ TabbedPane for defintion part* \
	 * @uml.property  name="definitionPanel"
	 * @uml.associationEnd  
	 */
	private JPanel definitionPanel;	
	//The textfield storing the name of the variante
	/**
	 * @uml.property  name="nameTextField"
	 * @uml.associationEnd  
	 */
	private GWidgetTextField nameTextField;
	/******************************\
	\******************************/
	
	/**
	 * \ TabbedPane for the tabs    * \
	 * @uml.property  name="tabs"
	 * @uml.associationEnd  
	 */
	private JTabbedPane tabs;	
	
	/******************************\
	\******************************/
	
	/**
	 * \ Panel of the first tab   * \
	 * @uml.property  name="firstPanel"
	 * @uml.associationEnd  
	 */
	private JPanel firstPanel;
	//The widget containing the fields
	/**
	 * @uml.property  name="visualisationList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList visualisationList;
	//The widget containing the icon
	/**
	 * @uml.property  name="conditionTextField"
	 * @uml.associationEnd  
	 */
	private GWidgetTextField conditionTextField;
	
	/******************************\
	\******************************/
	
	
	/**
	 * \ Panel of the second tab   * \
	 * @uml.property  name="secondPanel"
	 * @uml.associationEnd  
	 */
	private JPanel secondPanel;
	//The widget containing the fields
	/**
	 * @uml.property  name="generalCharacteristicsGridForm"
	 * @uml.associationEnd  
	 */
	private GWidgetGridForm generalCharacteristicsGridForm;
	//The widget containing the icon
	/**
	 * @uml.property  name="loadIcon"
	 * @uml.associationEnd  
	 */
	private GWidgetLoadIcon loadIcon;
	
	/******************************\
	\******************************/
	
	
	/**
	 * \ Panel of the third tab     * \
	 * @uml.property  name="thirdPanel"
	 * @uml.associationEnd  
	 */
	private JPanel thirdPanel;	
	//The widget containing the expression
	/**
	 * @uml.property  name="expressionWidget"
	 * @uml.associationEnd  
	 */
	private GWidgetExpression expressionWidget;
	//The widget containing color and position information
	/**
	 * @uml.property  name="colorPositionGridForm"
	 * @uml.associationEnd  
	 */
	private GWidgetGridForm colorPositionGridForm;
	
	/******************************\
	\******************************/
	
	/**
	 * \ \
	 * @uml.property  name="position"
	 */
	
	//The position of the element edited in the window in case of being created from a list trough the edit button
	private int position;
	
	@Deprecated
	public GWindowVarNode(GObject p, GObjectInformation info) {
		super(p, info);
		
		position = -1;
		
		initialization();
	}
	
	public GWindowVarNode(GObject p, GObjectInformation info, int pos) {
		super(p, info);
	
		position = pos;
		
		initialization();
	}
	
	private void initialization() {
		
		//First the inheritance panel
		inheritancePanelInitialization();
		
		//Then the tabs panel
		defineInitialization();
		
		//Now we set up the cardLayout manager
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		cardPanel.add(inheritancePanel, "inherit");
		cardPanel.add(definitionPanel, "defined");
		this.framePanel.add(cardPanel, BorderLayout.CENTER);
		
		//Finally we create and add the ok cancel panel
		okCancelWidget = new GWidgetOKCancel(this, information);
		this.framePanel.add(okCancelWidget, BorderLayout.SOUTH);
		
		this.add(this.framePanel);
		
		this.pack();
		this.setTitle("Graphic Variant");
		this.setSize(347,320);
		
		//And finally we initialize the JMenuBar
		menuBarInitialization();
	}
	
	private void menuBarInitialization() {
		//The menu bar
		menuBar = new JMenuBar();
		
		//The menu
		menu = new JMenu("Definition");
		menuBar.add(menu);
		
		//A button group to have only one selected button at a time
		ButtonGroup buttonGroup = new ButtonGroup();
		
		//The first item
		inheritanceItem = new JRadioButtonMenuItem("Inherit From Parent");
		inheritanceItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWindowVarNode.this.cardLayout.show(GWindowVarNode.this.cardPanel, "inherit");
			}
		});
		buttonGroup.add(inheritanceItem);
		menu.add(inheritanceItem);
		
		//And the second one
		definitionItem = new JRadioButtonMenuItem("User Defined");
		definitionItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWindowVarNode.this.cardLayout.show(GWindowVarNode.this.cardPanel, "defined");
			}
		});
		buttonGroup.add(definitionItem);
		menu.add(definitionItem);

		//Set the JMenuBar to the window
		setJMenuBar(menuBar);
		
		//Set the panel viewed by default as the inheritance panel
		inheritanceItem.setSelected(true);
	}
	
	private void inheritancePanelInitialization() {
		
		//First we initialize the inheritance panel
		inheritancePanel = new JPanel(new BorderLayout());
		
		//We have to get back the ancestors of the figaro type
		Vector<Object> types = new Vector<Object>();
		for(String s : figaroLoader.findAncestors(information.getTypeConcerned()))
		{
			System.err.println("voici s " + s);
				types.add(s);
		}
		
		//Set the labels
		Vector<String> labels = new Vector<String>();
		labels.add("Inherit From Type : ");
		labels.add("Inherit Graphic Variant : ");
		
		//Set the types of the widgets included in the gridForm
		Vector<WidgetClasses> widgetClasses = new Vector<WidgetClasses>();
		widgetClasses.add(WidgetClasses.COMBO);
		widgetClasses.add(WidgetClasses.COMBO);
		
		//The parameters (the name of the types)
		Vector<Vector<Object>> parameters = new Vector<Vector<Object>>();
		parameters.add(types);
		Vector<Object> variantes = new Vector<Object>();
		if(types.size() > 0)
			for(String s : xmlLoader.findVariantesGraphiquesNames((String)types.get(0)))
				variantes.add(s);
		parameters.add(variantes);
			
		//Create the gridform
		inheritanceGridForm = new GWidgetGridForm(this, information, labels, 2, 2, widgetClasses, parameters);
		
		//And put it into the panel
		inheritancePanel.add(inheritanceGridForm, BorderLayout.NORTH);
	}
	
	private void defineInitialization() {
		
		//First we initialize the define panel
		definitionPanel = new JPanel(new BorderLayout());
		
		//We create and add the name textfield
		JPanel textFieldPanel = new JPanel(new BorderLayout());
		textFieldPanel.add(new JLabel("Name : "), BorderLayout.WEST);
		nameTextField = new GWidgetTextField(this, information);
		textFieldPanel.add(nameTextField, BorderLayout.CENTER);
		definitionPanel.add(textFieldPanel, BorderLayout.NORTH);
		
		//Then we initialize the tab panel and add it to the define panel
		tabsInitialization();
		definitionPanel.add(tabs, BorderLayout.CENTER);
	}
	
	private void tabsInitialization() {
		
		//First of all we initialize the tabbedPane
		this.tabs = new JTabbedPane();
		
		//Then comes the turn of the first tab
		this.initializeFirstPanel();
		tabs.add("General", firstPanel);
		
		//Then comes the turn of the second tab
		this.initializeSecondPanel();
		tabs.add("Icon", secondPanel);
		
		//And the initialization of the third tab
		this.initializeThirdPanel();
		tabs.add("Text", thirdPanel);
	}
	
	private void initializeFirstPanel() {
		//First we initialize the panel with a border layout manager
		firstPanel = new JPanel(new BorderLayout());
		
		//Then we add the visualization list
		visualisationList = new GWidgetControledList(this, information, "Visualizations", null, ListTypes.SIMPLEARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDEL, WindowClasses.VISUALIZATION, null);
		firstPanel.add(visualisationList, BorderLayout.NORTH);
		
		//Finally we create the panel for the textfield
		JPanel textPanel = new JPanel(new BorderLayout());
		textPanel.add(new JLabel("Condition : "), BorderLayout.WEST);
		conditionTextField = new GWidgetTextField(this, information);
		textPanel.add(conditionTextField, BorderLayout.CENTER);
		firstPanel.add(textPanel, BorderLayout.SOUTH);
	}
	
	private void initializeSecondPanel() {
		//First we initialize the panel with a border layout manager
		secondPanel = new JPanel(new BorderLayout());
		
		//Creation of the labels
		Vector<String> labels = new Vector<String>();
		labels.add("Main Color :");
		labels.add("Thickness :");
		labels.add("Background :");
		labels.add("File :");
		
		//Set the type of widget we want in the grid widget
		Vector<WidgetClasses> widgetClasses = new Vector<WidgetClasses>();
		widgetClasses.add(WidgetClasses.TEXTFIELD);
		widgetClasses.add(WidgetClasses.TEXTFIELD);
		widgetClasses.add(WidgetClasses.TEXTFIELD);
		widgetClasses.add(WidgetClasses.TEXTFIELD);
		
		//Give the arguments to the widgets.
		Vector<Vector<Object>> objectArgs = new Vector<Vector<Object>>();
		objectArgs.add(null);
		objectArgs.add(null);
		objectArgs.add(null);
		objectArgs.add(null);
		
		//Creation of the grid panel with four columns and 3 lines
		generalCharacteristicsGridForm = new GWidgetGridForm(this, information, labels, 4, 2, widgetClasses, objectArgs); 
		//Finally we add the gridform to the panel
		secondPanel.add(generalCharacteristicsGridForm, BorderLayout.NORTH);
		
		//We create the icon widget
		Image image = loadIcon(System.getenv("VISUAL_FIGARO") + "test.ico");
		loadIcon = new GWidgetLoadIcon(this, information, image);
		
		//Then we add this panel to the first panel
		secondPanel.add(loadIcon, BorderLayout.SOUTH);
	}
	
	private void initializeThirdPanel() {
		//First we initialize the panel
		thirdPanel = new JPanel(new BorderLayout());
		
		//Create the arguments for the expressionGridForm
		Vector<String> labels = new Vector<String>();
		labels.add("FORMAT");
		labels.add("");
		Vector<WidgetClasses> widgetClasses = new Vector<WidgetClasses>();
		widgetClasses.add(WidgetClasses.TEXTFIELD);
		widgetClasses.add(WidgetClasses.CONTROLEDLISTADDDEL);
		//We create a new gridform to store all the information concerning the expression
		expressionWidget = new GWidgetExpression(this, information);
		thirdPanel.add(expressionWidget, BorderLayout.CENTER);
		
		//Create the arguments for the expressionGridForm
		labels = new Vector<String>();
		labels.add("Text Color : ");
		labels.add("Text Position : ");
		widgetClasses = new Vector<WidgetClasses>();
		widgetClasses.add(WidgetClasses.TEXTFIELD);
		widgetClasses.add(WidgetClasses.TEXTFIELD);
		//We create a new gridform to store all the information concerning the expression
		colorPositionGridForm = new GWidgetGridForm(this, information, labels, 2, 2, widgetClasses, null);
		thirdPanel.add(colorPositionGridForm, BorderLayout.SOUTH);
	}
	
	
	private Image loadIcon(String path) {
		//The image we will try to fill
		Image image;
		
		//We create the file and return null if it does not exist
		File file = new File(path);
		if(!file.exists())
			return null;

		//Otherwise we create an icon file and try to fill it with the resource
		ICOFile icon = null;
		try {
			icon = new ICOFile(file.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("VisualFigaro : GWindowDefaultVarNode : Error while reading the icon file : " + file + " ." + e);
		}
		
		//If everything succeeds then we try to extract the picture from the icon 
		BitmapDescriptor bmpdesc = icon.getDescriptor(0);
		image = bmpdesc.getImageRGB();
		
		return image;
	}
	
	
	private boolean copyFile(File origin, File destination) {
		
		try {
			
			//The file reader and the file writer to manipulate the files
			FileReader reader = new FileReader(origin);
			FileWriter writer = new FileWriter(destination);
			
			//The variable to store the characters read in the origin file and which will be written in the destination file
			int readCharacter;
			
			//Now we transfer the file. While the end character (-1) is not reached copy the character from the origin file to the destination file
			do {
				readCharacter = reader.read();
				if(readCharacter != -1)
					writer.write(readCharacter);
			} while(readCharacter != -1);
			
			reader.close();
			writer.close();
		} catch (FileNotFoundException e) {
			System.err.println("Visual Figaro : VisualFigaro : The file cannot be copied : " + e);
		} catch (IOException e) {
			System.err.println("Visual Figaro : VisualFigaro : The file cannot be copied : " + e);
		}
		
		return true;
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
			
			case ICONCHOOSED:
				
				GIcon messageIcons = null;
				
				try {
					messageIcons = (GIcon)message.getArguments().get(0);
				} catch(Exception ex) {
					System.err.println("Visual Figaro : GWindowDefaultVarNode : An exception occured during the processing of message : " + message + " : " + ex);
					return;
				}
				
				//First we have to check whether the icon was chosen from the icon folder
				if(messageIcons.isCompletePath()) {
					
					//First we have to ask for a new name
					GWindowNameAsker nameAskerWindow = new GWindowNameAsker(this, this.information, messageIcons.getPath());
					nameAskerWindow.setModal(true);
					nameAskerWindow.setAlwaysOnTop(true);
					nameAskerWindow.setVisible(true);
					
					//Then we copy the icon
					File originIco = new File(messageIcons.getPath());
					File originSym = new File(messageIcons.getPath().substring(0, messageIcons.getPath().lastIndexOf(".")) + ".sym");
					File destinationIco = new File(this.information.getKnowledgeBasePath() + "\\icons\\" + nameAskerWindow.getIconName() + ".ico");
					File destinationSym = new File(this.information.getKnowledgeBasePath() + "\\icons\\" + nameAskerWindow.getIconName() + ".sym");
					copyFile(originIco, destinationIco);
					copyFile(originSym, destinationSym);
					
					generalCharacteristicsGridForm.translateMessage(new GMessage(this.information, Messages.SENDTOWIDGET, new Object[]{3, new GMessage(this.information, Messages.REPLACEDEFAULTVALUES, new Object[]{nameAskerWindow.getIconName()})}));
				} else {
					String symFileName = messageIcons.getPath().substring(messageIcons.getPath().lastIndexOf("\\")+1, messageIcons.getPath().lastIndexOf("."));
					generalCharacteristicsGridForm.translateMessage(new GMessage(this.information, Messages.SENDTOWIDGET, new Object[]{3, new GMessage(this.information, Messages.REPLACEDEFAULTVALUES, new Object[]{symFileName})}));
				}
				//Move the icon to the database folder and change the textfield
				System.out.println("ICON CHOOSED");
				break;
				
			case NOTIFYCHANGE:
				
				System.out.println("Aarg first element : " + message.getArguments().get(0));
				
				//First we retrieve the message if it is not null
				if(message.getArguments().get(0) != null && inheritanceGridForm != null) {
				
					//First we get the arguments of the widget
					String selectedItem = (String)message.getArguments().get(0);
					int emiter = -1;
					try {
						emiter = Integer.parseInt(message.getSender().getLastPartOfThePath());
					} catch (NumberFormatException e) {
						return;
					}
					
					if(emiter == 0) {
						System.out.println("NOTIFYCHANGE : " + emiter + " : " + selectedItem);
						
						//Then we update the widget
						Vector<Object> arguments = new Vector<Object>();
						arguments.add(1);
						GMessage dummyMessage = new GMessage(information, Messages.REPLACEDEFAULTVALUES);
						for(String varName : xmlLoader.findVariantesGraphiquesNames(selectedItem))
							dummyMessage.addArgument(varName);
						arguments.add(dummyMessage);
						GMessage messageToEmit = new GMessage(information, Messages.SENDTOWIDGET, arguments);
						inheritanceGridForm.translateMessage(messageToEmit);
					}
				}
				break;
				
			default:
				System.out.println("VisualFigaro : GWindowDepart : Unknown message received");
		}
	}
	
	public Element fillDocument() {
		this.root = new Element(information.getLanguage().getBDCTranslation("VARIANTE_GRAPHIQUE"));
		
		//Now we have to check which checkbox is selected and trigger the appropriate behavior
		if(inheritanceItem.isSelected()) {
			
			//In case the inheritance panel is selected then we fill the appropriate fields in the xml
			GXMLElementFactory.saveElements(root, inheritanceGridForm.saveXML());
			
		} else {
			
			//If it is not inheritance which is selected then it is the user defined panel
			
			//First we save the general properties
			GXMLElementFactory.saveElements(root, nameTextField.saveXML());
			GXMLElementFactory.saveElements(root, visualisationList.saveXML());
			GXMLElementFactory.saveElements(root, conditionTextField.saveXML());
			
			//Then we save the icon properties
			Element element = new Element(information.getLanguage().getBDCTranslation("ICONE"));
			GXMLElementFactory.saveElements(element, generalCharacteristicsGridForm.saveXML());
			GXMLElementFactory.saveElement(root, element);
			
			//Then we save the text properties
			element = new Element(information.getLanguage().getBDCTranslation("TEXTE"));
			Element bufferElement = new Element("EXPRESSION");
			GXMLElementFactory.saveElements(bufferElement, expressionWidget.saveXML());
			
			GXMLElementFactory.saveElement(element, bufferElement);
			GXMLElementFactory.saveElements(element, colorPositionGridForm.saveXML());
			
			GXMLElementFactory.saveElement(root, element);
		}
		
		return root;
	}
	
	public void loadXml(Element e) {

		//If the element to load is null we just select the define panel as the default panel otherwise we use the specific part of the xml
		if(e != null)
			//First we have to check which of the panel to select
			if(e.getChild(information.getLanguage().getBDCTranslation("HERITE_DU_TYPE")) != null) {
				
				//If there is an inherit node we have to select the inheritance panel
				inheritanceItem.setSelected(true);
				cardLayout.show(cardPanel, "inherit");
				
				//
				
			} else {
				
				//If there is no inherit node we select the used defined panel
				definitionItem.setSelected(true);
				cardLayout.show(cardPanel, "defined");
			
			}
		
		Vector<Element> elemVect = new Vector<Element>();
		elemVect.add(e);
		
		//We load the elements related to the general panel
		nameTextField.loadXML(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("NOM")), false);
		visualisationList.loadXML(GXMLElementFactory.refactorElements(elemVect, information.getLanguage().getBDCTranslation("VISUALISATION")), false);
		System.err.println("Test : " + GXMLElementFactory.refactorElements(elemVect, information.getLanguage().getBDCTranslation("VISUALISATION")).get(0) + " - " + GXMLElementFactory.refactorElements(elemVect, information.getLanguage().getBDCTranslation("VISUALISATION")).get(0).getText());
		conditionTextField.loadXML(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("CONDITION")), false);
		
		//We are going to retrieve the elements concerning the icon
		Vector<Element> iconFields = new Vector<Element>();
		iconFields.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("COULEUR_PPLAN")).get(0));
		iconFields.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("EPAISSEUR_TRAIT")).get(0));
		iconFields.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("COULEUR_FOND")).get(0));
		iconFields.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("FICHIER")).get(0));
		generalCharacteristicsGridForm.loadXML(iconFields, false);
		
		//Now we create and load the expression elements
		Vector<Element> expressionFields = new Vector<Element>();
		expressionFields.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("FORMAT")).get(0));
		expressionFields.addAll(GXMLElementFactory.refactorElements(elemVect, information.getLanguage().getBDCTranslation("PARAMETRES")));
		expressionWidget.loadXML(expressionFields, false);
		
		//Now we create and load the color elements
		Vector<Element> colorPositionFields = new Vector<Element>();
		colorPositionFields.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("COULEUR_TEXTE")).get(0));
		colorPositionFields.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("POSITION_TEXTE")).get(0));
		colorPositionGridForm.loadXML(colorPositionFields, false);
		
	}
}
