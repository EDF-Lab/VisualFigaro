/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 21 September 2010                            
 * Author       : D.WEYAND/ALL4TEC                              
 * Bug Id       : 
 * Evol Id      : n°10                                      
 * Modification : Unique version number source (from .props file)
 * VF version   : 1.10
 * **************************************************************/
 
package GWindow;

import org.jdom.Element;

import GMessage.GMessage;

import jEditInterface.VisualFigaro;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
	private String propsFileContents;

	private String readFromJARFile(String filename)
	throws IOException
	{
	  InputStream is = getClass().getResourceAsStream(filename);
	  InputStreamReader isr = new InputStreamReader(is);
	  BufferedReader br = new BufferedReader(isr);
	  StringBuffer sb = new StringBuffer();
	  String line;
	  while ((line = br.readLine()) != null) 
	  {
	    sb.append(line);
	  }
	  br.close();
	  isr.close();
	  is.close();
	  return sb.toString();
	}
	
	public GWindowAbout(VisualFigaro vf){
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
		ImageIcon imageIcon = new ImageIcon("./VisualFigaro/" + "logoEDF.gif");
		
		//We add the logo to the logo panel
		JLabel iconLabel = new JLabel();
		iconLabel.setIcon(imageIcon);
		logoPanel.add(iconLabel, BorderLayout.WEST);
		
		//Then create the title
		try {
			propsFileContents = readFromJARFile("/VisualFigaro.props");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String versionString = "plugin.jEditInterface.VisualFigaroPlugin.version=";
		int versionIndex=propsFileContents.indexOf(versionString);
		int Start = versionIndex+ versionString.length();
		int End = propsFileContents.indexOf("#",Start);
		String VFversion = propsFileContents.substring(Start,End);
		
		JLabel titleLabel = new JLabel("     Visual Figaro Version : " + VFversion);
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