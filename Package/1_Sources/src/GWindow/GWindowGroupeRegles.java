package GWindow;

import global.Messages;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdom.Element;

import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidgetOKCancel;

public class GWindowGroupeRegles extends GWindow {
	private static final long serialVersionUID = 1L;
	
	//The OK/Cancel buttons
	/**
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetOKCancel okCancelWidget;	

	//The textfield
	/**
	 * @uml.property  name="comboBox"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JComboBox comboBox;
	
	@Deprecated
	public GWindowGroupeRegles(GObject p, GObjectInformation info) {
		super(p, info);
		
		initialization();
	}
	
	public GWindowGroupeRegles(GObject p, GObjectInformation info, String name, int pos) {
		super(p, info);
		
		initialization();
		this.setTitle(name);
	}
	
	private void initialization() {
		
		//Initialization of the label and the textfield
		JLabel label = new JLabel("Name : ");
		comboBox = new JComboBox();
		Vector<String> groups = figaroLoader.findGroups();
		if(groups != null)
			for(String group : groups)
				comboBox.addItem(group);
		
		//Then we put them into a simple panel and put this simple panel into the top panel
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(label, BorderLayout.WEST);
		panel.add(comboBox, BorderLayout.CENTER);
		this.framePanel.add(panel, BorderLayout.CENTER);
		
		//Finally we initilize and put the ok/cancel widget and put it at the bottom of the top panel
		this.okCancelWidget = new GWidgetOKCancel(this, information);
		this.framePanel.add(okCancelWidget, BorderLayout.SOUTH);
		
		this.add(this.framePanel);
		
		this.pack();
		this.setTitle("Groupe Regles");
	}
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
			case OK:
				Element elem = new Element("DUMMY");
				elem.setText(comboBox.getSelectedItem().toString());
				parent.translateMessage(new GMessage(information, Messages.ADDSIMPLEELEMENT, new Object[]{-1, elem}));
				dispose();
				break;
			
			case CANCEL:
				dispose();
				break;
				
			default:
				System.out.println("VisualFigaro : GWindowGroupeRegles : Unknown message received");
		}
	}
	
	public Element fillDocument() {
		return null;
	}
	
	public void loadXml(Element e) {
	}
}
