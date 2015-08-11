/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 01 July 2015                            
 * Author       : L.RAFFAELLI/ALL4TEC                              
 * Bug Id       : 
 * Evol Id      : n°23                                     
 * Modification : Selection of a color via a graphical interface
 * VF version   : 2.00
 * **************************************************************/
 
package GWindow;

import org.jdom.Element;

import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import global.Messages;
import jEditInterface.VisualFigaro;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JColorChooser;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import GWidget.GWidgetOKCancel;

public class GWindowColorChooser extends GWindow {

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
	
	private JColorChooser jcc;
	
	//Finally we have an ok/cancel widget
	/**
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetOKCancel okCancelWidget;
	
	public GWindowColorChooser(GObject p, GObjectInformation info, String strColor){
		super(p, info);
		
		Color defColor = createColorFromString(strColor);
		
		//Initialization of the frame panel
		framePanel = new JPanel(new BorderLayout());
		
		//The first step is to create the upper part of the wizard
		//initializeLogoPart();
		//framePanel.add(logoPanel, BorderLayout.NORTH);
		
		//add the color chooser
		if (defColor != null)
			jcc = new JColorChooser(defColor);
		else
			jcc = new JColorChooser();
		jcc.setPreviewPanel(new JPanel());
		
		//removing chooser panels
		AbstractColorChooserPanel[] oldPanels = jcc.getChooserPanels();
		jcc.removeChooserPanel(oldPanels[4]);
		jcc.removeChooserPanel(oldPanels[2]);
		jcc.removeChooserPanel(oldPanels[1]);
		
		//removing alpha
		
		Component[] list = oldPanels[3].getComponents();
		/*for (Component cm : list)
			JOptionPane.showMessageDialog(this, "Classes = " + cm.getClass().toString());*/
		JPanel pane = (JPanel) list[0];
		pane.remove(14);
		pane.remove(13);
		pane.remove(9);
		pane.remove(8);
		pane.remove(3);
		
		/*boolean error= false;
		
		Field f1 = null;
		try {
			f1 = oldPanels[3].getClass().getDeclaredField("panel");
		} catch (Exception e) {
			error = true;
			System.out.println("Error : " + e);
		}
		f1.setAccessible(true);
		Object colorPanel = null;
		try {
			colorPanel = f1.get(oldPanels[3]);
		} catch (Exception ex) {
			error = true;
			System.out.println("Error : " + ex);
		}
		Field f2 = null;
		try {
			f2 = colorPanel.getClass().getDeclaredField("spinners");
		} catch (Exception e1) {
			error = true;
			System.out.println("Error : " + e1);
		}
		if (!error){
			f2.setAccessible(true);
			Object rows = null;
			try {
				rows = f2.get(colorPanel);
			} catch (Exception e2) {
				error = true;
				System.out.println("Error : " + e2);
			}
			if (!error){
				final Object transpSlispinner = Array.get(rows, 3);
				Field f3 = null;
				try {
					f3 = transpSlispinner.getClass().getDeclaredField("slider");
				} catch (Exception e3) {
					System.out.println("Error : " + e3);
				}
				f3.setAccessible(true);
				JSlider slider = null;
				try {
					slider = (JSlider) f3.get(transpSlispinner);
				} catch (Exception e4) {
					System.out.println("Error : " + e4);
				}
				slider.setVisible(false);
				Field f4 = null;
				try {
					f4 = transpSlispinner.getClass().getDeclaredField("spinner");
				} catch (Exception e5) {
					System.out.println("Error : " + e5);
				}
				f4.setAccessible(true);
				JSpinner spinner = null;
				try {
					spinner = (JSpinner) f4.get(transpSlispinner);
				} catch (Exception e6) {
					System.out.println("Error : " + e6);
				}
				spinner.setVisible(false);
				Field f5 = null;
				try {
					f5 = transpSlispinner.getClass().getDeclaredField("label");
				} catch (Exception e7) {
					System.out.println("Error : " + e7);
				}
				f5.setAccessible(true);
				JLabel label = null;
				try {
					label = (JLabel) f5.get(transpSlispinner);
				} catch (Exception e8) {
					System.out.println("Error : " + e8);
				}
				label.setVisible(false);						
			}
		}*/
		
		//removing memory palette
		
		Component[] cmp = oldPanels[0].getComponents();
		JPanel panel = (JPanel) cmp[0];
		panel.remove(2);
		panel.remove(1);
		
		framePanel.add(jcc, BorderLayout.CENTER);
		
		okCancelWidget = new GWidgetOKCancel(this, information);
		framePanel.add(okCancelWidget, BorderLayout.SOUTH);
		
		add(framePanel);
		setSize(600, 400);
		setResizable(false);
		setTitle("Choose a color");
	}
	
	/*private void initializeLogoPart() {

		//Initialization of the panel
		logoPanel = new JPanel(new BorderLayout());
		
		//First we retrieve the EDF icon
		ImageIcon imageIcon = new ImageIcon("./VisualFigaro/" + "logoEDF.gif");
		
		//We add the logo to the logo panel
		JLabel iconLabel = new JLabel();
		iconLabel.setIcon(imageIcon);
		logoPanel.add(iconLabel, BorderLayout.WEST);

	}*/
	
	//create a color object from a string (#RRGGBB)
	private Color createColorFromString(String strColor){
		Color col = null;
		
		if (!strColor.equals("Empty")){
			if (strColor.length()==6){
				try{
					int r = Integer.parseInt(strColor.substring(0, 2), 16);
					int g = Integer.parseInt(strColor.substring(2, 4), 16);
					int b = Integer.parseInt(strColor.substring(4, 6), 16);
					
					col = new Color(r,g,b);
				}
				catch (Exception e){
					System.out.println("Erreur : " + e);
				}
			}
		}
		
		return col;
	}
	
	//create a string (#RRGGBB) from the last selected color
	private String createStringFromColor(){
		String res = "Empty";
		
		try{
			Color newColor = jcc.getColor();
			
			String interm1 = Integer.toHexString(newColor.getRed());
			if (interm1.length()<2)
				interm1 = "0"+interm1;
			
			String interm2 = Integer.toHexString(newColor.getGreen());
			if (interm2.length()<2)
				interm2 = "0"+interm2;
			
			String interm3 = Integer.toHexString(newColor.getBlue());
			if (interm3.length()<2)
				interm3 = "0"+interm3;
			
			res = interm1 + interm2 + interm3;
			res = res.toUpperCase();
		}
		catch(Exception e){
			System.out.println("Erreur : " + e);
		}
		
		return res;
	}
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
		case OK:
			String strColor = createStringFromColor();
			parent.translateMessage(new GMessage(information, Messages.REPLACEDEFAULTVALUES, new Object[]{strColor}));
			dispose();
			break;
		
		case CANCEL:
			dispose();
			break;
			
		default:
			System.out.println("VisualFigaro : GWindowColorChooser : Unknown message received");
		}
	}
	
	public Element fillDocument() {
		return null;
	}
	
	public void loadXml(Element e) {
		
	}
}