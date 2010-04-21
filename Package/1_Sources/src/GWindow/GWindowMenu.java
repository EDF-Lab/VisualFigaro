/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 13 April 2010                            
 * Author       : D.WEYAND/ALL4TEC                              
 * Bug Id       :                                        
 * Modification : code cleanup to avoid warnings
 * VF version   : 1.7
 * **************************************************************/

package GWindow;

import global.Messages;

import java.awt.BorderLayout;
import java.util.Iterator;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdom.Element;

//import Factories.GXMLElementFactory;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidgetOKCancel;

public class GWindowMenu extends GWindow {

	private static final long serialVersionUID = 1L;
	
	//The comboBox
	/**
	 * @uml.property  name="comboBox"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JComboBox comboBox;
	
	//The okCancel widget
	/**
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetOKCancel okCancelWidget;
	
	public GWindowMenu(GObject p, GObjectInformation info) {
		super(p, info);
		
		//Initialization of the label and the comboBox
		JLabel label = new JLabel("Menu : ");
		comboBox = new JComboBox();
		for(Iterator<String> iter = xmlLoader.findMenus().iterator(); iter.hasNext();)
			comboBox.addItem(iter.next());
		
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
		this.setTitle("Menu Form");
	}
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
			case OK:
				//fillDocument();
				Element element = new Element("FAMILLE_TYPE_PALETTE");
				element.setText(comboBox.getSelectedItem().toString());
				parent.translateMessage(new GMessage(information, Messages.ADDELEMENT, new Object[]{-1, element}));
				dispose();
				break;
			
			case CANCEL:
				dispose();
				break;
				
			default:
				System.out.println("VisualFigaro : GWindowMenu : Unknown message received");
		}
	}
	
	public Element fillDocument() {
		return null;
	}
	
	public void loadXml(Element e) {
	}

}
