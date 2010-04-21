package GWidget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
//import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.jdom.Element;

import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;

public class GWidgetManageIcon extends GWidget {

private static final long serialVersionUID = 1L;
	
	//The image which will be drown in the widget
	/**
	 * @uml.property  name="image"
	 */
	private Image image;
	
	//The two button on the right
	/**
	 * @uml.property  name="posXText"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JTextField posXText;
	/**
	 * @uml.property  name="posYText"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JTextField posYText;
	
	//The check box to set the movement like if the dot was attracted by magnetic points in the icon
	/**
	 * @uml.property  name="iconMagneticCheck"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	//private JCheckBox iconMagneticCheck;
	/**
	 * @uml.property  name="posXIcon"
	 */
	private int posXIcon;
	/**
	 * @uml.property  name="posYIcon"
	 */
	private int posYIcon;
	
	public GWidgetManageIcon() {
		super();		
		
		image = null;
		
		initialization();
	}
	
	public GWidgetManageIcon(GObject p, GObjectInformation info, Image img) {
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
		JPanel gridPanel = new JPanel(new GridLayout(3,1,5,5));
		
		//We create and add the buttons
		//iconMagneticCheck = new JCheckBox("Magnetic");
		//gridPanel.add(iconMagneticCheck);
		posXText = new JTextField();
		posXText.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
				updatePanel();
			}
			public void keyReleased(KeyEvent arg0) {
				updatePanel();
			}
			public void keyTyped(KeyEvent arg0) {
				updatePanel();
			}
			private void updatePanel() {
				int value = 0;
				try {
					value = Integer.parseInt(GWidgetManageIcon.this.posXText.getText());
				} catch(NumberFormatException e) {
					return;
				}
				if(value > 100)
					value = 100;
				else if(value < -100)
					value = -100;
				GWidgetManageIcon.this.posXText.setText("" + value);
				GWidgetManageIcon.this.posXIcon = value; 
				GWidgetManageIcon.this.repaint();
			}
		});
		gridPanel.add(posXText);
		posYText = new JTextField();
		posYText.setPreferredSize(new Dimension(50,20));
		posYText.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
				updatePanel();
			}
			public void keyReleased(KeyEvent arg0) {
				updatePanel();
			}
			public void keyTyped(KeyEvent arg0) {
				updatePanel();
			}
			private void updatePanel() {
				int value = 0;
				try {
					value = Integer.parseInt(GWidgetManageIcon.this.posYText.getText());
				} catch(NumberFormatException e) {
					return;
				}
				if(value > 100)
					value = 100;
				else if(value < -100)
					value = -100;
				GWidgetManageIcon.this.posYText.setText("" + value);
				GWidgetManageIcon.this.posYIcon = value; 
				GWidgetManageIcon.this.repaint();
			}
		});
		gridPanel.add(posYText);
		//Then we add the grid panel to the rightpanel and the rightpanel to the widget
		rightPanel.add(gridPanel, BorderLayout.EAST);
		constraints.gridx = 1;
		add(rightPanel, constraints);
		
		//Finally we add a border to make it clean
		Border blackLine;
		blackLine = BorderFactory.createLineBorder(Color.black);
		setBorder(BorderFactory.createTitledBorder(blackLine, "Position", TitledBorder.LEFT, TitledBorder.DEFAULT_JUSTIFICATION , null));
		
		//We add a mouse listener and a mouse motion listener to get the user movement on the panel
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {}
			
			public void mouseEntered(MouseEvent e) {}
			
			public void mouseExited(MouseEvent e) {}
			
			public void mousePressed(MouseEvent e) {
				/*
				//Store the size of the icon
				Dimension imageDimension = new Dimension(image.getWidth(GWidgetManageIcon.this), image.getHeight(GWidgetManageIcon.this));
				
				//A point to store the actual coordinates of the dot
				Point p = new Point();
				
				//We retrieve the origin of the icon
				Point origin = new Point(GWidgetManageIcon.this.getWidth()/4-imageDimension.width/2, (GWidgetManageIcon.this.getHeight()-imageDimension.height)/2 + imageDimension.height);
				
				
				//We have to rebound the point
				if(e.getX() < origin.x)
					p.x = 0;
				else
					if((origin.x + imageDimension.width) < e.getX())
						p.x = imageDimension.width;
					else
						p.x = e.getX() - origin.x;
				
				if(e.getY() < origin.y - imageDimension.height)
					p.y = imageDimension.height;
				else
					if(origin.y < e.getY())
						p.y = 0;
					else
						p.y = origin.y - e.getY();*/
				
				//In the case that the constrained movement check box has been selected
				/*if(iconMagneticCheck.isSelected()) {
					
					//We compute the nearest attracting point
					int posXLeft = Math.abs(p.x - 0);
					int posXMiddle = Math.abs(p.x - imageDimension.width/2);
					int posXRight = Math.abs(p.x - imageDimension.width);
					
					int posYBottom = Math.abs(p.y - 0);
					int posYMiddle = Math.abs(p.y - imageDimension.height/2);
					int posYTop = Math.abs(p.y - imageDimension.height);
					
					if(posXLeft < posXMiddle)
						p.x = 0;
					else
						if(posXMiddle < posXRight)
							p.x = imageDimension.width/2;
						else
							p.x = imageDimension.width;
					
					if(posYBottom < posYMiddle)
						p.y = 0;
					else
						if(posYMiddle < posYTop)
							p.y = imageDimension.height/2;
						else
							p.y = imageDimension.height;
					
				}
				
				GWidgetManageIcon.this.posXIcon = (int)(((double)p.x / (double)imageDimension.width) * 200.0 - 100.0);
				GWidgetManageIcon.this.posYIcon = (int)(((double)p.y / (double)imageDimension.height) * 200.0 - 100.0);
				GWidgetManageIcon.this.posXText.setText("" + GWidgetManageIcon.this.posXIcon);
				GWidgetManageIcon.this.posYText.setText("" + GWidgetManageIcon.this.posYIcon);
				
				//portsWindow.this.paint(portsWindow.this.getGraphics());
				GWidgetManageIcon.this.repaint();*/
			}
			
			public void mouseReleased(MouseEvent e) {}
		});
		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				/*
				//Store the size of the icon
				Dimension imageDimension = new Dimension(image.getWidth(GWidgetManageIcon.this), image.getHeight(GWidgetManageIcon.this));
				
				//A point to store the actual coordinates of the dot
				Point p = new Point();
				
				//We retrieve the origin of the icon
				Point origin = new Point(GWidgetManageIcon.this.getWidth()/4-imageDimension.width/2, (GWidgetManageIcon.this.getHeight()-imageDimension.height)/2 + imageDimension.height);
				
				
				//We have to rebound the point
				if(e.getX() < origin.x)
					p.x = 0;
				else
					if((origin.x + imageDimension.width) < e.getX())
						p.x = imageDimension.width;
					else
						p.x = e.getX() - origin.x;
				
				if(e.getY() < origin.y - imageDimension.height)
					p.y = imageDimension.height;
				else
					if(origin.y < e.getY())
						p.y = 0;
					else
						p.y = origin.y - e.getY();
				*/
				//In the case that the constrained movement check box has been selected
				/*if(iconMagneticCheck.isSelected()) {
					
					//We compute the nearest attracting point
					int posXLeft = Math.abs(p.x - 0);
					int posXMiddle = Math.abs(p.x - imageDimension.width/2);
					int posXRight = Math.abs(p.x - imageDimension.width);
					
					int posYBottom = Math.abs(p.y - 0);
					int posYMiddle = Math.abs(p.y - imageDimension.height/2);
					int posYTop = Math.abs(p.y - imageDimension.height);
					
					if(posXLeft < posXMiddle)
						p.x = 0;
					else
						if(posXMiddle < posXRight)
							p.x = imageDimension.width/2;
						else
							p.x = imageDimension.width;
					
					if(posYBottom < posYMiddle)
						p.y = 0;
					else
						if(posYMiddle < posYTop)
							p.y = imageDimension.height/2;
						else
							p.y = imageDimension.height;
					
				}*/
				/*
				GWidgetManageIcon.this.posXIcon = (int)(((double)p.x / (double)imageDimension.width) * 200.0 - 100.0);
				GWidgetManageIcon.this.posYIcon = (int)(((double)p.y / (double)imageDimension.height) * 200.0 - 100.0);
				GWidgetManageIcon.this.posXText.setText("" + GWidgetManageIcon.this.posXIcon);
				GWidgetManageIcon.this.posYText.setText("" + GWidgetManageIcon.this.posYIcon);
				
				GWidgetManageIcon.this.repaint();*/
			}
			
			public void mouseMoved(MouseEvent e) {
			}
			
		});
	}
	
	public void paintComponent(Graphics g) {		
		super.paintComponent(g);

		if(image == null)
			return;
		
		Dimension imageDimension = new Dimension(image.getWidth(this), image.getHeight(this));
		Point origin = new Point(this.getWidth()/4-imageDimension.width/2, (this.getHeight()-imageDimension.height)/2 + imageDimension.height);
		
		if(g != null) {
			g.setColor(Color.BLACK);
			g.drawImage(image, origin.x, origin.y - imageDimension.height, imageDimension.width, imageDimension.height, this);
			g.drawRect(origin.x, origin.y - imageDimension.height, imageDimension.width, imageDimension.height);
			System.err.println("X : " + posXIcon);
			int realCursorPosX = (int)(((double)(this.posXIcon + 100) / 200.0) * imageDimension.width);
			int realCursorPosY = (int)(((double)(this.posYIcon + 100) / 200.0) * imageDimension.height);
			g.fillRect(origin.x + realCursorPosX - 2, origin.y - realCursorPosY - 2, 4, 4);
		} else
			System.err.println("VisualFigaro : GWidgetLoadIcon : Graphics Error");
	}
	
	public void translateMessage(GMessage message) {
	}
	
	public boolean loadXML(Vector<Element> e, boolean deeplyRooted) {
		
		//If the vector is null then fail
		if(e == null)
			return false;
		
		//If the vector is "strange" then fail
		if(e.size() <= 0)
			return false;
		
		//We know that the first element concerns the position with X and Y
		Element positionElement = e.get(0);

		if(positionElement != null) {
			//We retrieve the position
			posXText.setText(positionElement.getChildText(information.getLanguage().getBDCTranslation("X")));
			if(posXText.getText().equals(""))
				posXText.setText("0");
			posXIcon = Integer.parseInt(posXText.getText());
			posYText.setText(positionElement.getChildText(information.getLanguage().getBDCTranslation("Y")));
			if(posYText.getText().equals(""))
				posYText.setText("0");
			posYIcon = Integer.parseInt(posYText.getText());
		}
		
		//We repaint the window to update the position of the cursor
		repaint();
		
		return true;
	}
	
	public Vector<Element> saveXML() {
		
		Vector<Element> resultVector = new Vector<Element>();
		Element result = new Element(information.getLanguage().getBDCTranslation("POSITION"));

		//The element for X
		Element xElement = new Element(information.getLanguage().getBDCTranslation("X"));
		if(posXText.getText().equals(""))
			xElement.setText("0");
		else
			xElement.setText(posXText.getText());
		result.addContent(xElement);
		
		//The element for Y
		Element yElement = new Element(information.getLanguage().getBDCTranslation("Y"));
		if(posYText.getText().equals(""))
			yElement.setText("0");
		else
			yElement.setText(posYText.getText());
		result.addContent(yElement);
		
		//Now we add the result element to the resultVector
		resultVector.add(result);
		
		return resultVector;
	}
}
