/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 25 June 2015                                  
 * Author       : L.RAFFAELLI/ALL4TEC                               
 * Bug Id       : n°75                                          
 * Modification : Add the port inheritance functions 
 * VF Version   : 2.0               
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
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

import org.gjt.sp.jedit.jEdit;
import org.jdom.Element;

import Factories.GXMLElementFactory;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidgetControledList;
import GWidget.GWidgetGridForm;
import GWidget.GWidgetManageIcon;
import GWidget.GWidgetOKCancel;
import GWidget.GWidgetTextField;

import com.ctreber.aclib.image.ico.BitmapDescriptor;
import com.ctreber.aclib.image.ico.ICOFile;

public class GWindowNodePort extends GWindow {

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
	
	
	
	
	/**
	 * \ The second layout concerned the definition           * \
	 * @uml.property  name="definitionPanel"
	 * @uml.associationEnd  
	 */
	
	
	
	private JPanel definitionPanel;	
	//The textfield storing the name of the port
	/**
	 * @uml.property  name="nameTextField"
	 * @uml.associationEnd  
	 */
	private GWidgetTextField nameTextField;
	//The list for all the connections
	/**
	 * @uml.property  name="connectionList"
	 * @uml.associationEnd  
	 */
	private GWidgetControledList connectionList;
	//The icon panel
	/**
	 * @uml.property  name="manageIcon"
	 * @uml.associationEnd  
	 */
	private GWidgetManageIcon manageIcon;
	
	
	/**
	 * \ \
	 * @uml.property  name="position"
	 */
	
	//The position of the element edited in the window in case of being created from a list trough the edit button
	private int position;
	
	@Deprecated
	public GWindowNodePort(GObject p, GObjectInformation info) {
		super(p, info);
		
		position = -1;
		
		initialization();
	}
	
	public GWindowNodePort(GObject p, GObjectInformation info, int pos) {
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
		this.setTitle("Port Window");
		
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
				GWindowNodePort.this.cardLayout.show(GWindowNodePort.this.cardPanel, "inherit");
			}
		});
		buttonGroup.add(inheritanceItem);
		menu.add(inheritanceItem);
		
		//And the second one
		definitionItem = new JRadioButtonMenuItem("User Defined");
		definitionItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWindowNodePort.this.cardLayout.show(GWindowNodePort.this.cardPanel, "defined");
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
		labels.add("Inherit Port : ");
		labels.add("Inherit From Class : ");
		
		//Set the types of the widgets included in the gridForm
		Vector<WidgetClasses> widgetClasses = new Vector<WidgetClasses>();
		widgetClasses.add(WidgetClasses.COMBO);
		widgetClasses.add(WidgetClasses.COMBO);
		
		//The parameters (the name of the ports, then the name of the types)
		Vector<Vector<Object>> parameters = new Vector<Vector<Object>>();
		Vector<Object> ports = new Vector<Object>();
		if(types.size() > 0)
			for(String s : xmlLoader.findPortsNames((String)types.get(0)))
				ports.add(s);
		parameters.add(ports);
		parameters.add(types);
			
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
		
		//We create and add the accepted connection list
		connectionList = new GWidgetControledList(this, information, "Accepted Connections", null, ListTypes.COMPLEXARRAY, NameRetrieverClasses.NAMERETRIEVER, ControlTypes.ADDDELEDIT, WindowClasses.CONNECTION, null);
		definitionPanel.add(connectionList, BorderLayout.CENTER);
		
		//Finally we add the icon management widget
		Image image = loadIcon(jEdit.getJEditHome() + "/VisualFigaro/" + "test.ico");
		manageIcon = new GWidgetManageIcon(this, information, image);
		definitionPanel.add(manageIcon, BorderLayout.SOUTH);
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
	
	//http://cermics.enpc.fr/polys/info1/main/node33.html passage par valeur sous java!! BRAVO m(_ _)m quelle maitrise
	
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
				//Move the icon to the database folder and change the textfield
				System.out.println("ICON CHOOSED");
				break;
				
			case NOTIFYCHANGE:
				
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
					if(emiter == 1) {
						System.out.println("NOTIFYCHANGE : " + emiter + " : " + selectedItem);
						
						//Then we update the widget
						Vector<Object> arguments = new Vector<Object>();
						arguments.add(0);
						GMessage dummyMessage = new GMessage(information, Messages.REPLACEDEFAULTVALUES);
						for(String portName : xmlLoader.findPortsNames(selectedItem))
							dummyMessage.addArgument(portName);
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
		this.root = new Element(information.getLanguage().getBDCTranslation("POINT_CONNEXION"));
		
		//Now we have to check which checkbox is selected and trigger the appropriate behavior
		if(inheritanceItem.isSelected()) {
			
			//In case the inheritance panel is selected then we fill the appropriate fields in the xml
			GXMLElementFactory.saveElements(root, inheritanceGridForm.saveXML());
			
		} else {
			//If it is not inheritance which is selected then it is the user defined panel
			
			//First we save the general properties
			GXMLElementFactory.saveElements(root, nameTextField.saveXML("NOM"));
			
			//Then the position
			GXMLElementFactory.saveElements(root, manageIcon.saveXML());
			
			//Finally the content of the connections list
			GXMLElementFactory.saveElements(root, connectionList.saveXML());
		}
		
		return root;
	}
	
	public void loadXml(Element e) {
		//If the element to load is null we just select the define panel as the default panel otherwise we use the specific part of the xml
		if(e != null){
		
			//First we have to check which of the panel to select
			if(e.getChild(information.getLanguage().getBDCTranslation("HERITE_DU_TYPE")) != null) {
				//If there is an inherit node we have to select the inheritance panel
				inheritanceItem.setSelected(true);
				cardLayout.show(cardPanel, "inherit");
			} else {
				//If there is no inherit node we select the used defined panel
				definitionItem.setSelected(true);
				cardLayout.show(cardPanel, "defined");
			}
		}
		
		Vector<Element> elemVect = new Vector<Element>();
		elemVect.add(e);
		
		//We load the element related to the inheritance Pannel
		Vector<Element> inheritanceFields = new Vector<Element>();
		inheritanceFields.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("NOM")).get(0));
		inheritanceFields.add(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("HERITE_DU_TYPE")).get(0));
		inheritanceGridForm.loadXML(inheritanceFields, false);
		
		//We load the elements related to the general panel
		nameTextField.loadXML(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("NOM")), false);
		
		//Then we load all the accepted connections
		Vector<Element> elements = new Vector<Element>();
		elements.add(e);
		connectionList.loadXML(GXMLElementFactory.refactorElements(elements, information.getLanguage().getBDCTranslation("CONNEXION_ACCEPTEE")), false);

		//Finally we take care of the manageIcon widget
		manageIcon.loadXML(GXMLElementFactory.refactorElement(e, information.getLanguage().getBDCTranslation("POSITION")), false);
	}
}
