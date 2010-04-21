package GWidget;

import global.WindowClasses;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;

import org.jdom.Element;

import Factories.GWindowFactory;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWindow.GWindow;

public class GWidgetEditDefaultVar extends GWidget {

	private static final long serialVersionUID = 1L;

	//The textfield
	/**
	 * @uml.property  name="button"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JButton button;
	
	//An element to store the data in case of xml loading into the widget because it will be displayed only when the user will click on the button
	/**
	 * @uml.property  name="root"
	 * @uml.associationEnd  
	 */
	private Element root;
	
	//Indicate if the button has to trigger a link default variante graphique or a node's one
	/**
	 * @uml.property  name="isNode"
	 */
	private boolean isNode;
	
	public GWidgetEditDefaultVar() {
		super();		
		
		initialization();
		
		isNode = true;
	}
	
	public GWidgetEditDefaultVar(GObject p, GObjectInformation info, String name, boolean isNode) {
		super(p, info);

		initialization();
		
		if(name != null)
			if(name.length() > 0)
				button.setText(name);
		
		this.isNode = isNode;
	}
	
	private void initialization() {
		
		setLayout(new BorderLayout());
		button = new JButton();
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWindow window;
				if(GWidgetEditDefaultVar.this.isNode)
					window = GWindowFactory.createWindow(WindowClasses.DEFAULTVARNODE, GWidgetEditDefaultVar.this, GWidgetEditDefaultVar.this.information, new Object[]{-1});
				else
					window = GWindowFactory.createWindow(WindowClasses.DEFAULTVARLINK, GWidgetEditDefaultVar.this, GWidgetEditDefaultVar.this.information, new Object[]{-1});
				window.loadXml(GWidgetEditDefaultVar.this.root);
				window.setVisible(true);
				window.setAlwaysOnTop(true);
				System.out.println("EDIT DEFAULT VAR");
			}
		});
		add(button);
	}
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
			case ADDELEMENT:
				if(message.getArguments().size() >= 2)
					this.root = (Element)message.getArguments().get(1);
				break;
				
			default:
				System.out.println("VisualFigaro : GWidgetEditDefaultVar : Unhandled message");
		}
	}
	
	public boolean loadXML(Vector<Element> e, boolean deeplyRooted) {
		root = null;
		
		//Some tests about the value
		if(e == null)
			return false;
		if(e.size() <= 0)
			return false;
		
		//If there is really a value in the vector we change the root
		root = e.get(0);
		
		return true;
	}
	
	public Vector<Element> saveXML() {
		//In every cases we just have to return the root //#######Maybe with a small detach
		Vector<Element> result = new Vector<Element>();
		result.add((Element)root.detach());
		return result;
	}
}
