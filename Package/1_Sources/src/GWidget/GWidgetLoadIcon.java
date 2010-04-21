/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 29 March 2010                              
 * Author       : D.WEYAND/ALL4TEC                               
 * Bug Id       : 49                                        
 * Modification : Add .sym file list selection for GWindowVarNode
 * VF Version   : 1.4
 * **************************************************************
 * Date         : 13 April 2010                            
 * Author       : D.WEYAND/ALL4TEC                              
 * Bug Id       :                                         
 * Modification : Code cleanup to avoid warnings
 * VF version   : 1.7
 * **************************************************************/

package GWidget;

import global.Messages;

//import global.WindowTypes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
//import java.io.FileFilter;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.jdom.Element;

import GIcon.GIcon;
import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWindow.GWindowShowIcon;

public class GWidgetLoadIcon extends GWidget {

	private static final long serialVersionUID = 1L;
	
	//The image which will be drown in the widget
	/**
	 * @uml.property  name="image"
	 */
	private Image image;
	
	//The two button on the right
	/**
	 * @uml.property  name="chooseButton"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JButton chooseButton;
	/**
	 * @uml.property  name="editButton"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JButton editButton;
	
	public GWidgetLoadIcon() {
		super();		
		
		image = null;
		
		initialization();
	}
	
	public GWidgetLoadIcon(GObject p, GObjectInformation info, Image img) {
		super(p, info);

		if(img != null)
			image = img;
		
		initialization();
	}
	
	private void initialization() {
		
		//We initialize the top panel
		setLayout(new GridBagLayout());

		//Define some constraints for the gridbag
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		
		//On the right part of the top panel we add a gridlayout in a borderlayout
		JPanel rightPanel = new JPanel(new BorderLayout());
		JPanel gridPanel = new JPanel(new GridLayout(2,1,5,5));
		
		//We create and add the buttons
		chooseButton = new JButton("Choose New Icon");
		chooseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetLoadIcon.this.showFileChooser();
			}
		});
		gridPanel.add(chooseButton);
		editButton = new JButton("Choose Existing Icon");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWidgetLoadIcon.this.showIconChooser();
			}
		});
		gridPanel.add(editButton);
		//Then we add the grid panel to the rightpanel and the rightpanel to the widget
		rightPanel.add(gridPanel, BorderLayout.EAST);
		constraints.gridx = 1;
		add(rightPanel, constraints);
		
		//Finally we add a border to make it clean
		Border blackLine;
		blackLine = BorderFactory.createLineBorder(Color.black);
		setBorder(BorderFactory.createTitledBorder(blackLine, "Icon", TitledBorder.LEFT, TitledBorder.DEFAULT_JUSTIFICATION , null));
	}
	
	public void paintComponent(Graphics g) {		
		super.paintComponent(g);

		if(image == null)
			return;
		
		Dimension imageDimension = new Dimension(image.getWidth(this), image.getHeight(this));
		//Point origin = new Point(topPanel.getWidth()/4-imageDimension.width/2, (topPanel.getHeight()-imageDimension.height)/2 + imageDimension.height);
		Point origin = new Point(this.getWidth()/4-imageDimension.width/2, (this.getHeight()-imageDimension.height)/2 + imageDimension.height);
		
		//Graphics2D graphics = (Graphics2D)this.getGraphics();
		if(g != null) {
			g.setColor(Color.BLACK);
			g.drawImage(image, origin.x, origin.y - imageDimension.height, imageDimension.width, imageDimension.height, this);
			g.drawRect(origin.x, origin.y - imageDimension.height, imageDimension.width, imageDimension.height);
		} else
			System.err.println("VisualFigaro : GWidgetLoadIcon : Graphics Error");
	}
	
	public void showFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(this);
		
		if(returnValue == JFileChooser.APPROVE_OPTION)
			parent.translateMessage(new GMessage(information, Messages.ICONCHOOSED, new GIcon(fileChooser.getSelectedFile().getAbsolutePath(), true)));
	}
	
	public void showFileChooser(String path) {

		class MyFilter extends javax.swing.filechooser.FileFilter {
		    public boolean accept(File file) {
		        String filename = file.getName();
		        return filename.endsWith(".sym");
		    }
		    public String getDescription() {
		        return "*.sym";
		    }
		}
		JFileChooser fileChooser = new JFileChooser(path);
		fileChooser.addChoosableFileFilter(new MyFilter());

		int returnValue = fileChooser.showOpenDialog(this);
		if(returnValue == JFileChooser.APPROVE_OPTION)
			parent.translateMessage(new GMessage(information, Messages.ICONCHOOSED, new GIcon(fileChooser.getSelectedFile().getAbsolutePath(), false)));
	}
	
	
	public void showIconChooser() {
		
		String parentname = parent.getClass().toString();
		parentname = parentname.substring(parentname.indexOf(".")+1,parentname.length());
		if (parentname.equals("GWindowVarNode"))
		{
			// For Node Graphic Variant type propose .sym files list
			GWidgetLoadIcon.this.showFileChooser(information.getKnowledgeBasePath() + "\\icons\\");
		}
		else
		{
			// For Link type propose .ico list
			GWindowShowIcon showIconWindow = new GWindowShowIcon(this, this.information);
			showIconWindow.setModal(true);
			showIconWindow.setAlwaysOnTop(true);
			showIconWindow.setVisible(true);
		
			if(!showIconWindow.getSelectedIcon().equals(""))
				parent.translateMessage(new GMessage(information, Messages.ICONCHOOSED, new GIcon(showIconWindow.getSelectedIcon(), false)));
		}

	}
	
	public void translateMessage(GMessage message) {
	}
	
	public boolean loadXML(Vector<Element> e, boolean deeplyRooted) {
		if(e == null)
			return false;
		
		if(e.size() <= 0)
			return false;
		
		return true;
	}
	
	public Vector<Element> saveXML() {
		return null;
	}
}
