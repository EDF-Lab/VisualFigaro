package GWindow;

import global.Messages;
import global.WidgetClasses;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jdom.Element;

import com.ctreber.aclib.image.ico.BitmapDescriptor;
import com.ctreber.aclib.image.ico.ICOFile;

import Factories.GXMLElementFactory;
import GIcon.GIcon;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidgetExpression;
import GWidget.GWidgetGridForm;
import GWidget.GWidgetLoadIcon;
import GWidget.GWidgetOKCancel;

public class GWindowDefaultVarNode extends GWindow {

	private static final long serialVersionUID = 1L;
	
	//The ok/cancel panel
	/**
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetOKCancel okCancelWidget;
	
	
	/***************************************************************\
	 *        The second layout concerned the definition           *
	\***************************************************************/
	
	/**
	 * \ TabbedPane for the tabs    * \
	 * @uml.property  name="tabs"
	 * @uml.associationEnd  
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
	//The widget containing the icon
	/**
	 * @uml.property  name="loadIcon"
	 * @uml.associationEnd  
	 */
	private GWidgetLoadIcon loadIcon;
	
	/******************************\
	\******************************/
	
	
	/**
	 * \ Panel of the second tab    * \
	 * @uml.property  name="secondPanel"
	 * @uml.associationEnd  
	 */
	private JPanel secondPanel;	
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
	
	/***************************************************************\
	\***************************************************************/
	
	@Deprecated
	public GWindowDefaultVarNode(GObject p, GObjectInformation info) {
		super(p, info);
		
		initialization();
	}
	
	public GWindowDefaultVarNode(GObject p, GObjectInformation info, int pos) {
		super(p, info);
		
		initialization();
	}
	
	private void initialization() {
		
		//Then the tabs panel
		tabsInitialization();
		this.framePanel.add(this.tabs, BorderLayout.CENTER);
		
		//Finally we create and add the ok cancel panel
		okCancelWidget = new GWidgetOKCancel(this, information);
		this.framePanel.add(okCancelWidget, BorderLayout.SOUTH);
		
		this.add(this.framePanel);
		
		this.pack();
		this.setTitle("Default Graphic Variant");
	}
	
	private void tabsInitialization() {
		
		//First of all we initialize the tabbedPane
		this.tabs = new JTabbedPane();
		
		//Then comes the turn of the first tab
		this.initializeFirstPanel();
		tabs.add("Icon", firstPanel);
		
		//And the initialization of the second tab
		this.initializeSecondPanel();
		tabs.add("Text", secondPanel);
	}
	
	private void initializeFirstPanel() {
		//First we initialize the panel with a border layout manager
		firstPanel = new JPanel(new BorderLayout());
		
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
		firstPanel.add(generalCharacteristicsGridForm, BorderLayout.NORTH);
		
		//We create the icon widget
		Image image = loadIcon(System.getenv("VISUAL_FIGARO") + "test.ico");
		loadIcon = new GWidgetLoadIcon(this, information, image);
		
		//Then we add this panel to the first panel
		firstPanel.add(loadIcon, BorderLayout.SOUTH);
	}
	
	private void initializeSecondPanel() {
		//First we initialize the panel
		secondPanel = new JPanel(new BorderLayout());
		
		//Create the arguments for the expressionGridForm
		Vector<String> labels = new Vector<String>();
		labels.add("FORMAT");
		labels.add("");
		Vector<WidgetClasses> widgetClasses = new Vector<WidgetClasses>();
		widgetClasses.add(WidgetClasses.TEXTFIELD);
		widgetClasses.add(WidgetClasses.CONTROLEDLISTADDDEL);
		//We create a new gridform to store all the information concerning the expression
		expressionWidget = new GWidgetExpression(this, information);
		secondPanel.add(expressionWidget, BorderLayout.CENTER);
		
		//Create the arguments for the expressionGridForm
		labels = new Vector<String>();
		labels.add("Text Color : ");
		labels.add("Text Position : ");
		widgetClasses = new Vector<WidgetClasses>();
		widgetClasses.add(WidgetClasses.TEXTFIELD);
		widgetClasses.add(WidgetClasses.TEXTFIELD);
		//We create a new gridform to store all the information concerning the expression
		colorPositionGridForm = new GWidgetGridForm(this, information, labels, 2, 2, widgetClasses, null);
		secondPanel.add(colorPositionGridForm, BorderLayout.SOUTH);
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
	
	//http://cermics.enpc.fr/polys/info1/main/node33.html passage par ""valeur"" sous java!! BRAVO m(_ _)m quelle maitrise
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
			case OK:
				fillDocument();
				//###En attente de traduction complete
				parent.translateMessage(new GMessage(information, Messages.ADDELEMENT, new Object[]{-1, root}));
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
					generalCharacteristicsGridForm.translateMessage(new GMessage(this.information, Messages.SENDTOWIDGET, new Object[]{3, new GMessage(this.information, Messages.REPLACEDEFAULTVALUES, new Object[]{messageIcons.getPath()})}));
				}
				
				//Move the icon to the knowledge base icon folder and change the textfield
				break;
				
			default:
				System.err.println("VisualFigaro : GWindowDepart : Unknown message received");
		}
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
	
	public Element fillDocument() {
		this.root = new Element(information.getLanguage().getBDCTranslation("VARIANTE_GRAPHIQUE_DEFAUT"));
		
		
		//First we save the icon properties
		Element element = new Element(information.getLanguage().getBDCTranslation("ICONE"));
		GXMLElementFactory.saveElements(element, generalCharacteristicsGridForm.saveXML());
		GXMLElementFactory.saveElement(root, element);
		
		//Then we save the text properties
		element = new Element(information.getLanguage().getBDCTranslation("TEXTE"));
		Element bufferElement = new Element(information.getLanguage().getBDCTranslation("EXPRESSION"));
		GXMLElementFactory.saveElements(bufferElement, expressionWidget.saveXML());
		GXMLElementFactory.saveElement(element, bufferElement);
		GXMLElementFactory.saveElements(element, colorPositionGridForm.saveXML());
		GXMLElementFactory.saveElement(root, element);
		
		return root;
	}
	
	@SuppressWarnings("unchecked")
	public void loadXml(Element e) {
		
		//If the element is null just exit
		if(e == null)
			return;
		
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
		expressionFields.addAll(GXMLElementFactory.refactorElements(new Vector<Element>(e.getChildren()), information.getLanguage().getBDCTranslation("PARAMETRES")));
		expressionWidget.loadXML(expressionFields, false);
		
		//Now we create and load the color elements
		Vector<Element> colorPositionFields = new Vector<Element>();
		colorPositionFields.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("COULEUR_TEXTE")).get(0));
		colorPositionFields.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("POSITION_TEXTE")).get(0));
		colorPositionGridForm.loadXML(colorPositionFields, false);
		
	}
}
