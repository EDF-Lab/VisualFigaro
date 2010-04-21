/* **************************************************************
 *              File modifications log                           
 * **************************************************************             
 * Date         : 29 March 2010                              
 * Author       : D.WEYAND/ALL4TEC                               
 * Bug Id       :                                         
 * Modification : Update element type (NODE or LINK replaces 
 *                NEITHER default type)
 * VF Version   : 1.4
 * **************************************************************/

package GWidget;


import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.jdom.Element;
//import org.jdom.transform.JDOMSource;

import Factories.GNameRetrieverFactory;
import Factories.GWindowFactory;
import global.ControlTypes;
import global.ListTypes;
import global.Messages;
import global.NameRetrieverClasses;
import global.WindowClasses;
import global.WindowTypes;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWindow.GWindow;

public class GWidgetControledList extends GWidget {

	private static final long serialVersionUID = 1L;

	//The list to be displayed
	/**
	 * @uml.property  name="list"
	 * @uml.associationEnd  
	 */
	private GWidgetList list;
	
	//The control used to interact with the list
	/**
	 * @uml.property  name="control"
	 * @uml.associationEnd  
	 */
	private GWidgetControl control;
	
	//The window which will be triggered in case the user add or edit something in the control
	/**
	 * @uml.property  name="interactionWindow"
	 * @uml.associationEnd  
	 */
	private WindowClasses interactionWindow;
	/**
	 * @uml.property  name="windowArguments"
	 */
	private Object windowArguments;
	
	//The name retriever used to retrieve the name from the argument passed back from the trigerred window
	/**
	 * @uml.property  name="nameRetriever"
	 * @uml.associationEnd  
	 */
	private NameRetrieverClasses nameRetriever;
	
	//To remind if the xml loaded was deeply rooted or not and in the case it was deeply rooted the string is used to retrieve the element name
	/**
	 * @uml.property  name="deeplyRooted"
	 */
	private boolean deeplyRooted;
	/**
	 * @uml.property  name="rootName"
	 */
	private String rootName;
	
	//To check if the list has been initialized with some strings or not
	/**
	 * @uml.property  name="isEditable"
	 */
	private boolean isEditable;
	
	public GWidgetControledList() {
		super();
		
		this.deeplyRooted = false;
		this.rootName = "";
		//this.initializedWithStrings = false;
		this.windowArguments = null;
		this.isEditable = false;
	}
	
	public GWidgetControledList(GObject p, GObjectInformation info, String name, Vector<String> initializationList, ListTypes listType, NameRetrieverClasses nr, ControlTypes controlType, WindowClasses windowClass, Object windowArgument) {
		super(p, info);
	
		this.deeplyRooted = false;
		this.rootName = "";
		//this.initializedWithStrings = false;
		this.windowArguments = windowArgument;
		this.isEditable = false;
		
		interactionWindow = windowClass;
		nameRetriever = nr;
		
		//Select the layout
		setLayout(new BorderLayout());
		
		
		
		//Create instances for the class variables
		
		//For the list :
		//0 - GWidgetListComplexArray
		//1 - GWidgetListSimpleArray
		switch(listType) {
			case COMPLEXARRAY:
				System.out.println("Name retriever : " + nameRetriever.toString());
				list = new GWidgetListComplexArray(this, info, null, GNameRetrieverFactory.createNameRetriever(information.getLanguage(), nameRetriever));
				System.out.println("Nombre d'element" + list.listGetNumberOfElement());
				break;
				
			case SIMPLEARRAY:
				System.out.println("Name retriever : " + nameRetriever.toString());
				list = new GWidgetListSimpleArray(this, info, null, GNameRetrieverFactory.createNameRetriever(information.getLanguage(), nameRetriever));
				System.out.println("Nombre d'element" + list.listGetNumberOfElement());
				break;
				
			default:
				System.out.println("VisualFigaro : GWidgetControledList : The list parameter is false");
		}

		//For the control :
		//0 - GWidgetAddDel
		//1 - GWidgetAddDelEdit
		//2 - GWidgetAddDelUpDown
		//3 - GWidgetNodeLinkNeitherUpDownEdit
		switch(controlType) {
			case ADDDEL:
				control = new GWidgetAddDel(this, information);
				break;			
			case ADDDELEDIT:
				control = new GWidgetAddDelEdit(this, information);
				isEditable = true;
				break;
			case ADDDELUPDOWN:
				control = new GWidgetAddDelUpDown(this, information);
				break;
			case NODELINKNEITHERUPDOWNEDIT:
				isEditable = true;
				control = new GWidgetNodeLinkNeitherUpDownEdit(this, info);
				break;
			default:
				System.out.println("VisualFigaro : GWidgetControledList : The control parameter is wrong");	
		}
		//We put the control in a separate panel in order to avoid the differences of size between the windows
		JPanel controlPanel = new JPanel(new BorderLayout());
		controlPanel.add(control, BorderLayout.NORTH);
		
		//The control and the list are now put in the controledList window
		add(list, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.EAST);
		
		if(name != null)
			if(name.length() > 0) {
				//We add a border using the name given in arguments list
				Border blackLine;
				blackLine = BorderFactory.createLineBorder(Color.black);
				setBorder(BorderFactory.createTitledBorder(blackLine, name, TitledBorder.LEFT, TitledBorder.DEFAULT_JUSTIFICATION , null));
			}
		
		if(initializationList != null)
			if(initializationList.size() > 0) {
			//If there is an initialization list we will change the list type from a simple list to an initialized list and add the initialization strings
			
			//We have to change the value of the initializedWithStrings variable to indicate that we have initialized the list
			//initializedWithStrings = true;
			
			list.initializeWithString(initializationList);
		}
	}
	
	public void translateMessage(GMessage message) {
		
		//Some local variables
		GWindow window = null;
		
		//The big switch between all the possibilities
		switch(message.getMessage()) {
			
			case ADD:
				System.err.println("ADD : windowARg : " + windowArguments);
				if(windowArguments == null)
					window = GWindowFactory.createWindow(interactionWindow, this, information, new Object[]{-1});
				else
					window = GWindowFactory.createWindow(interactionWindow, this, information, new Object[]{-1, windowArguments});
				window.setVisible(true);
				window.setAlwaysOnTop(true);
				System.out.println("ADD");
				break;
				
			case DEL:
				list.listDeleteElement(list.listGetSelectedIndex());
				System.out.println("DEL");
				
				//Notify the parent that something happened in the widget
				parent.translateMessage(new GMessage(information, Messages.NOTIFYCHANGE, "DEL"));
				break;
			
			case EDIT:
				if(isEditable) {
					if(interactionWindow != null) {
						if(windowArguments == null)
							window = GWindowFactory.createWindow(interactionWindow, this, information, new Object[]{list.listGetSelectedIndex(), list.listGetElement(list.listGetSelectedIndex())});
						else
							window = GWindowFactory.createWindow(interactionWindow, this, information, new Object[]{list.listGetSelectedIndex(), list.listGetElement(list.listGetSelectedIndex()), windowArguments});
					} else {
						
						String listSelectedString = list.listGetString(list.listGetSelectedIndex()); 
						int indexOfColon = listSelectedString.indexOf(" : ");
						if(indexOfColon >= 0) {
							
							if(listSelectedString.substring(0, indexOfColon).equals("NODE")) {
								this.translateMessage(new GMessage(this.information, Messages.NODE));
							} else if(listSelectedString.substring(0, indexOfColon).equals("LINK")) {
								this.translateMessage(new GMessage(this.information, Messages.LINK));
							} else {
								window = null;
							}
							
						} else {
							
							Object[] options = {"Node", "Link", "Cancel"};
							int result = JOptionPane.showOptionDialog(this, "The current classes is not categorized. Do you want to open the node or the link window?", "Type Choice", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[2]);					
							switch(result) {
								//The case 0 is for the node
								case 0:
									this.translateMessage(new GMessage(this.information, Messages.NODE));
									break;
									
								//The case 1 is for a link
								case 1:
									this.translateMessage(new GMessage(this.information, Messages.LINK));
									break;
									
								//The case 2 is to cancel. So don' do nothing at all
								case 2:
									break;
									
								default:
									System.err.println("VisualFigaro : GWidgetControledList : Unexpected message.");
							}
						}
					}
					
					if(window != null) {
						window.setVisible(true);
						window.setAlwaysOnTop(true);
					}
				}
				System.out.println("EDIT");
				break;
				
			case UP:
				list.listMoveUp();
				System.out.println("UP");
				break;
				
			case DOWN:
				list.listMoveDown();
				System.out.println("DOWN");
				break;
			
			case ADDELEMENT:
				if((Integer)message.getArguments().get(0) == -1)
					list.listAddElement((Element)message.getArguments().get(1));
 				else
					list.listReplaceElement((Integer)message.getArguments().get(0), (Element)message.getArguments().get(1));
				
				//We notify the parent that something happened in the widget
				parent.translateMessage(new GMessage(information, Messages.NOTIFYCHANGE, "ADD"));
				break;
				
			case ADDSIMPLEELEMENT:
				if((Integer)message.getArguments().get(0) == -1)
					list.listAddElement((Element)message.getArguments().get(1));
 				else
					list.listReplaceElement((Integer)message.getArguments().get(0), (Element)message.getArguments().get(1));
				
				//We notify the parent that something happened in the widget
				parent.translateMessage(new GMessage(information, Messages.NOTIFYCHANGE, "ADD"));
				break;
				
			case NODE:
				
				//If there is no item selected in the list just return
				if(list.listGetSelectedIndex() < 0)
					return;
				
				String listSelectedString = list.listGetString(list.listGetSelectedIndex());
				int indexOfColon = listSelectedString.indexOf(" : ");
				
				if(indexOfColon >= 0)
					if(listSelectedString.substring(0, indexOfColon).equals("LINK")) {
						Object[] options = {"Open as Node", "Open as Link", "Cancel"};
						int result = JOptionPane.showOptionDialog(this, "The current type is Link but the action performed will open the Node perspective.\nIf you open the window as a Node you will loose all information associated with the Link.\nWhat do you want to do?", "Type Choice", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[2]);
						
						switch(result) {
							case 0:
								break;
								
							case 1:
								this.translateMessage(new GMessage(this.information, Messages.LINK));
								return;
								
							case 2:
								return;
						}
					}
				
				//If the user selected node we have to load xml using the GWindowNoeud

				list.listGetElement(list.listGetSelectedIndex());
				GObjectInformation windowNodeInformation = new GObjectInformation(information);
				if(list.listGetString(list.listGetSelectedIndex()).indexOf(" : ") >= 0)
				{
					windowNodeInformation.setTypeConcerned(list.listGetString(list.listGetSelectedIndex()).substring(list.listGetString(list.listGetSelectedIndex()).indexOf(" : ") + 3, list.listGetString(list.listGetSelectedIndex()).length()));
					windowNodeInformation.setType(WindowTypes.NODE);
				}
				else
					windowNodeInformation.setTypeConcerned(list.listGetString(list.listGetSelectedIndex()));
	
				String typeName = listSelectedString.substring(indexOfColon+3, listSelectedString.length());
				window = GWindowFactory.createWindow(WindowClasses.NODE, this, windowNodeInformation, new Object[]{list.listGetSelectedIndex(), list.listGetElement(list.listGetElement(typeName))});
				window.setVisible(true);
				window.setAlwaysOnTop(true);
				System.out.println("NODE");
				break;
				
			case NODEMODIFICATION:
				list.listReplaceElement((Integer)message.getArguments().get(0), (Element)message.getArguments().get(1));
				break;
				
			case LINK:
				//If there is no item selected in the list just return
				if(list.listGetSelectedIndex() < 0)
					return;
				
				listSelectedString = list.listGetString(list.listGetSelectedIndex());
				indexOfColon = listSelectedString.indexOf(" : ");
				
				if(indexOfColon >= 0)
					if(listSelectedString.substring(0, indexOfColon).equals("NODE")) {
						Object[] options = {"Open as Node", "Open as Link", "Cancel"};
						int result = JOptionPane.showOptionDialog(this, "The current type is Node but the action performed will open the Link perspective.\nIf you open the window as a Link you will loose all information associated with the Node.\nWhat do you want to do?", "Type Choice", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[2]);
						
						switch(result) {
							case 0:
								this.translateMessage(new GMessage(this.information, Messages.NODE));
								return;
								
							case 1:
								break;
								
							case 2:
								return;
						}
					}
				
				//If the user selected node we have to load xml using the GWindowNoeud
				list.listGetElement(list.listGetSelectedIndex());
				GObjectInformation windowLinkInformation = new GObjectInformation(information);
				if(list.listGetString(list.listGetSelectedIndex()).indexOf(" : ") >= 0)
				{
					windowLinkInformation.setTypeConcerned(list.listGetString(list.listGetSelectedIndex()).substring(list.listGetString(list.listGetSelectedIndex()).indexOf(" : ") + 3, list.listGetString(list.listGetSelectedIndex()).length()));
					windowLinkInformation.setType(WindowTypes.LINK);
				}
				else
					windowLinkInformation.setTypeConcerned(list.listGetString(list.listGetSelectedIndex()));
				
				typeName = listSelectedString.substring(indexOfColon+3, listSelectedString.length());
				window = GWindowFactory.createWindow(WindowClasses.LINK, this, windowLinkInformation, new Object[]{list.listGetSelectedIndex(), list.listGetString(list.listGetSelectedIndex()), list.listGetElement(list.listGetElement(typeName))});
				window.setVisible(true);
				window.setAlwaysOnTop(true);
				System.out.println("LINK");
				break;
				
			case LINKMODIFICATION:
				list.listReplaceElement((Integer)message.getArguments().get(0), (Element)message.getArguments().get(1));
				break;
				
			case NEITHER:
				listSelectedString = list.listGetString(list.listGetSelectedIndex());
				indexOfColon = listSelectedString.indexOf(" : ");
				
				typeName = listSelectedString.substring(indexOfColon+3, listSelectedString.length());

				if(indexOfColon >= 0){
					Element element;
					int position;
					for(position=0; position<(list.listGetNumberOfElement()-1); position++){
						element= list.listGetElement(position);
						if(element!=null)
							if(element.getChildText("NOM").equals(typeName)){
								element.setName(typeName);
								list.listReplaceElementBis(list.listGetSelectedIndex(), element);
								list.DeleteXMLvalues(typeName);
								break;
							}
					}
					
				}
				
				break;
				
			case SETWINDOWARGUMENTS:
				windowArguments = message.getArguments().get(0);
				System.err.println("Argument : " + windowArguments);
				break;
				
			case NOTIFYCHANGE:
				parent.translateMessage(message);
				
			default:
				System.out.println("-1");
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean loadXML(Vector<Element> elements, boolean d) {
		
		//Reinitialize the deeplyRooted variable
		deeplyRooted = d;
		
		//#######A traiter : cas ou elements est null
		
		//In case it is deeply rooted we extract the root and continue the process using the children
		if(deeplyRooted) {
			rootName = elements.get(0).getName();
			return list.loadXML(new Vector<Element>(elements.get(0).getChildren()), false);
		} else {
			//Otherwise we just add the elements
			return list.loadXML(elements, false);
		}
		 
	}
	
	public Vector<Element> saveXML() {
		
		//If it is deeply rooted we add the root we have stored and put all the children recovered from the child widget under it
		if(deeplyRooted) {
			Vector<Element> bufferVector = new Vector<Element>();
			bufferVector.add((new Element(rootName)).addContent(list.saveXML()));
			return bufferVector;
		} else 
			//Otherwise we just return all the elements from the child widget
			return list.saveXML();
	}
	
	public int GetNumberOfElement(){
		return list.listGetNumberOfElement();
	}
	
}
