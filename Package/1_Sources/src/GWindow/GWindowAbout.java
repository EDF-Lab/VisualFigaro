package GWindow;

import org.jdom.Element;

import GMessage.GMessage;

import jEditInterface.VisualFigaro;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GWidget.GWidgetOKCancel;

public class GWindowAbout extends GWindow {

	private static final long serialVersionUID = 1L;

	//The framePanel
	/**
	 * @uml.property  name="framePanel"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JPanel framePanel;
	
	//The upper part panel containing the different logo
	/**
	 * @uml.property  name="logoPanel"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JPanel logoPanel;
	
	//Finally we have an ok/cancel widget
	/**
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetOKCancel okCancelWidget;
	
	public GWindowAbout(VisualFigaro vf) {
		super();
		
		//Initialization of the frame panel
		framePanel = new JPanel(new BorderLayout());
		
		//The first step is to create the upper part of the wizard
		initializeLogoPart();
		framePanel.add(logoPanel, BorderLayout.NORTH);
		
		okCancelWidget = new GWidgetOKCancel(this, information);
		framePanel.add(okCancelWidget, BorderLayout.SOUTH);
		
		add(framePanel);
		setSize(250, 180);
		setResizable(false);
		setTitle("About Visual Figaro");
	}
	
	private void initializeLogoPart() {
	
		//Initialization of the panel
		logoPanel = new JPanel(new BorderLayout());
		
		//First we retrieve the EDF icon
		ImageIcon imageIcon = new ImageIcon(System.getenv("VISUAL_FIGARO") + "logoEDF.gif");
		
		//We add the logo to the logo panel
		JLabel iconLabel = new JLabel();
		iconLabel.setIcon(imageIcon);
		logoPanel.add(iconLabel, BorderLayout.WEST);
		
		//Then create the title
		JLabel titleLabel = new JLabel("     Visual Figaro Version 1.8");
		logoPanel.add(titleLabel, BorderLayout.CENTER);
	}
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
		case OK:			
			dispose();
			break;
		
		case CANCEL:
			dispose();
			break;
			
		default:
			System.out.println("VisualFigaro : GWindowAbout : Unknown message received");
		}
	}
	
	public Element fillDocument() {
		return null;
	}
	
	public void loadXml(Element e) {
		
	}
}