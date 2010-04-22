/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 22 April 2010                            
 * Author       : D.WEYAND/ALL4TEC                              
 * Bug Id       : n°62                                        
 * Modification : Fix bug concerning wrong tag
 * VF version   : 1.9
 * **************************************************************/

package GWindow;

import global.Messages;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdom.Element;

import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidgetOKCancel;

public class GWindowInterface extends GWindow {

	private static final long serialVersionUID = 1L;
	
	//The OK/Cancel buttons
	/**
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetOKCancel okCancelWidget;	

	//The combobox
	/**
	 * @uml.property  name="comboBox"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JComboBox comboBox;

	//The name of the type from which the interfaces are retrieved
	/**
	 * @uml.property  name="typeName"
	 */
	private String typeName;
	
	@Deprecated
	public GWindowInterface(GObject p, GObjectInformation info) {
		super(p, info);
		
		typeName = "";
		
		initialization();
	}
	
	public GWindowInterface(GObject p, GObjectInformation info, String name, String typeName) {
		super(p, info);
		
		this.typeName = typeName;
		
		initialization();
		this.setTitle(name);
	}
	
	private void initialization() {
		
		//Initialization of the label and the comboBox
		JLabel label = new JLabel("Interface : ");
		comboBox = new JComboBox();
		if(typeName.length() > 0)
			for(String interfaceName : figaroLoader.findInterfaces(typeName))
				comboBox.addItem(interfaceName);
		
		//comboBox.addItem("Essai");
		
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
		this.setTitle("Visualization Form");
	}
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
			case OK:
				if(comboBox.getItemCount()>0){
					//Element element = new Element(information.getLanguage().getBDCTranslation("REGLE_NOEUD_LIEN"));
					Element element = new Element(information.getLanguage().getBDCTranslation("INTERFACE"));
					element.setText(comboBox.getSelectedItem().toString());
					parent.translateMessage(new GMessage(information, Messages.ADDSIMPLEELEMENT, new Object[]{-1, element}));
				}
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
		return null;
	}
	
	public void loadXml(Element e) {
	}
}
