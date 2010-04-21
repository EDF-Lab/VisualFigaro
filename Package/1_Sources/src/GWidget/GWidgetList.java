package GWidget;

import global.Messages;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.Vector;


import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.jdom.Element;

import GMessage.GMessage;
import GNameRetriever.GNameRetriever;
import GObject.GObject;
import GObjectInformation.GObjectInformation;

public abstract class GWidgetList extends GWidget {
	
	private static final long serialVersionUID = 1L;
	
	//Declare the list and the list model used to store the elements
	/**
	 * @uml.property  name="listModel"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	protected DefaultListModel listModel;
	/**
	 * @uml.property  name="list"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected JList list;
	
	//The name retriever to find the name from the element
	/**
	 * @uml.property  name="nameRetriever"
	 * @uml.associationEnd  
	 */
	protected GNameRetriever nameRetriever;
	
	//To check if the list has been initialized with some strings or not
	/**
	 * @uml.property  name="initializedWithStrings"
	 */
	protected boolean initializedWithStrings;
	
	public GWidgetList() {
		super();

		initialize();
	}
	
	public GWidgetList(GObject p, GObjectInformation info, GNameRetriever n) {
		super(p, info);
		
		nameRetriever = n;
		
		initialize();
	}
	
	private void initialize() {
		setLayout(new BorderLayout());

		//Initialization of the list
		listModel = new DefaultListModel();
		list = new JList(listModel);
		initializedWithStrings = false;
		
		//Creation of a scrollpane in order to scroll all the list
		JScrollPane scrollPane = new JScrollPane(list);
		
		//Then we display the scrollpane
		add(scrollPane, BorderLayout.CENTER);
		
		//We add an mouse listener in order to handle the double click
		list.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				
				//If it is a double click we have to send the GMessage EDIT to the parent
				if(e.getClickCount() == 2) {
					GWidgetList.this.parent.translateMessage(new GMessage(information, Messages.EDIT));
				}
			}
			public void mouseEntered(MouseEvent arg0) {
			}
			public void mouseExited(MouseEvent arg0) {
			}
			public void mousePressed(MouseEvent arg0) {
			}
			public void mouseReleased(MouseEvent arg0) {
			}
		});
	}
	
	public abstract boolean listAddElement(Element e);
	
	public abstract boolean listReplaceElement(int position, Element e);
	
	public abstract boolean listReplaceElementBis(int position, Element e);
	
	public abstract boolean listDeleteElement(int position);
	
	public abstract Element listGetElement(int position);
	
	public abstract boolean DeleteXMLvalues(String name);
	
	public abstract int listGetElement(String name);
	
	public String listGetString(int position) {
		
		//Then we have to check if the list has been initialized with strings or not and trigger the appropriate behavior
		if(initializedWithStrings)
			if(position > listModel.size())
				return null;
			
		//Rebound the position to get an existing index
		if(position < 0)
			position = 0;
		if(position >= listModel.size())
			position = listModel.size() - 1;
		
		return listModel.get(position).toString();
	}
	
	public abstract boolean listMoveUp();
	
	public abstract boolean listMoveDown();
	
	public abstract int listGetSelectedIndex();
	
	public void translateMessage(GMessage message) {
		System.out.println("VisualFigaro : GWidgetList : A list does not have to translate a message");
	}
	
	public int listGetNumberOfElement() {
		return this.listModel.size();
	}
	
	public void initializeWithString(Vector<String> args) {
		
		//We indicate that the list has received some strings in order to be initialized
		initializedWithStrings = true;
		
		//And we print add the values in the list
		for(Iterator<String> iter = args.iterator(); iter.hasNext();)
			listModel.addElement(iter.next());
	}
}
