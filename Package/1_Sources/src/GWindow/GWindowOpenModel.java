/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 30 October 2014                            
 * Author       : L.RAFFAELLI/ALL4TEC                                                                   
 * Modification : Creation
 * VF version   : 1.16
 * **************************************************************/

package GWindow;

import org.gjt.sp.jedit.jEdit;
import org.jdom.Element;

import GMessage.GMessage;
import jEditInterface.VisualFigaro;
import GKnowledgeBase.GKnowledgeBase;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import GWidget.GWidgetOKCancel;

public class GWindowOpenModel extends GWindow {
	
	private static final long serialVersionUID = 1L;

	//The variable visualFigaro to store the parent
	/**
	 * @uml.property  name="vfParent"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private VisualFigaro vfParent;
	
	//The vector of all the opened knowledge base
	/**
	 * @uml.property  name="knowledgedBasesVector"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="GKnowledgeBase.GKnowledgeBase"
	 */
	private Vector<GKnowledgeBase> knowledgedBasesVector;
	
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
	
	//The lower part of the panel containing the fields
	/**
	 * @uml.property  name="fieldsPanel"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JPanel fieldsPanel;
	
	/**
	 * @uml.property  name="knowledgeBaseComboBox"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JComboBox knowledgeBaseComboBox;
	
	//The JTextField for the name of the Model
	/**
	 * @uml.property  name="nameTextField"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JTextField nameTextField;
	
	//The Button to open the explorer window to find the path
	/**
	 * @uml.property  name="openExplorerButton"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JButton openExplorerButton;
	
	//Finally we have an ok/cancel widget
	/**
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetOKCancel okCancelWidget;
	
	public GWindowOpenModel(VisualFigaro vf, Vector<GKnowledgeBase> knowledgedBases) {
		super();
		
		//We initialize the parent
		vfParent = vf;
		
		//We retrieve the list of available knowledge bases
		knowledgedBasesVector = knowledgedBases;
		
		//Initialization of the frame panel
		framePanel = new JPanel(new BorderLayout());
		
		//The first step is to create the upper part of the wizard
		initializeLogoPart();
		framePanel.add(logoPanel, BorderLayout.NORTH);
		
		//The second step is to create the lower part of the wizard
		initializeFieldPart();
		framePanel.add(fieldsPanel, BorderLayout.CENTER);
		
		okCancelWidget = new GWidgetOKCancel(this, information);
		framePanel.add(okCancelWidget, BorderLayout.SOUTH);
		
		add(framePanel);
		setSize(310, 225);
		setResizable(false);
		setTitle("Open a Model");
	}
	
	private void initializeLogoPart() {
		
		//Initialization of the panel
		logoPanel = new JPanel(new BorderLayout());
		
		//First we retrieve the EDF icon
		ImageIcon imageIcon = new ImageIcon(jEdit.getJEditHome() + "/VisualFigaro/" + "logoEDF.gif");
		
		//We add the logo to the logo panel
		JLabel iconLabel = new JLabel();
		iconLabel.setIcon(imageIcon);
		logoPanel.add(iconLabel, BorderLayout.WEST);
		
		//Then create the title
		JLabel titleLabel = new JLabel("Model Opening Wizard  ");
		logoPanel.add(titleLabel, BorderLayout.EAST);
	}
	
	private void initializeFieldPart() {
		
		//Initialization of the panel
		fieldsPanel = new JPanel(new BorderLayout());
		
		//The north panel containing all the field
		JPanel northPanel = new JPanel(new GridLayout());
		
		//The panel containing all the labels
		JPanel labelPanel = new JPanel(new GridLayout(2,1,5,5));
		labelPanel.add(new JLabel("Knowledge Base : "));
		labelPanel.add(new JLabel("Name : "));
		northPanel.add(labelPanel);
		
		//The panel containing the components corresponding to the labels
		JPanel componentPanel = new JPanel(new GridLayout(2,1,5,5));
		//Initialization of the names of the knowledgebases comboBoxs
		knowledgeBaseComboBox = new JComboBox();
		try{
		for(GKnowledgeBase knowledgeBase : knowledgedBasesVector){
			String kBName = knowledgeBase.getKnowledgeBaseName();
			int pos = kBName.lastIndexOf('\\');
			kBName =  kBName.substring(pos+1);
			knowledgeBaseComboBox.addItem(kBName);}}
		catch(Exception e){
			JOptionPane.showMessageDialog(vfParent, "An error occured !");
			return;
		}
		componentPanel.add(knowledgeBaseComboBox);
		//Initialization of the name textfield
		nameTextField = new JTextField();
		componentPanel.add(nameTextField);
		northPanel.add(componentPanel);
		
		//Then we add the north panel to the fields panel
		fieldsPanel.add(northPanel, BorderLayout.NORTH);
		
		//Initialization of the name path panel
		JPanel explorePanel = new JPanel(new BorderLayout());
		openExplorerButton = new JButton("Explore");
		openExplorerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWindowOpenModel.this.showFileChooser();
			}
		});
		explorePanel.add(openExplorerButton, BorderLayout.EAST);
		fieldsPanel.add(explorePanel, BorderLayout.SOUTH);
	}
	
	private void showFileChooser() {		
		class MyFilter extends javax.swing.filechooser.FileFilter {
		    public boolean accept(File file) {
			    if(file.isDirectory()) {
				   return true;
				}	
		        String filename = file.getName();
		        return filename.endsWith(".fi");
		    }
		    public String getDescription() {
		        return "*.fi";
		    }
		}
		
		JFileChooser fileChooser = new JFileChooser(vfParent.getPrevModelFile());
		fileChooser.addChoosableFileFilter(new MyFilter());
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.showOpenDialog(this);
		File file = fileChooser.getSelectedFile();
		nameTextField.setText(file.toString());
		fileChooser.setVisible(false);
	}
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
		case OK:
			
			if(nameTextField.getText().isEmpty()){
				JOptionPane.showMessageDialog(GWindowOpenModel.this, "Select a model file first");
				return;
			}
			
			//We retrieve the knowledge base and the name of the model
			GKnowledgeBase kb = knowledgedBasesVector.get(knowledgeBaseComboBox.getSelectedIndex());
			File model = new File(nameTextField.getText());
				
			//we close the windows
			dispose();
			
			//We use the openModel method of the visualFigaro parent
			vfParent.openModel(model, kb);
			
			break;
		
		case CANCEL:
			dispose();
			break;
			
		default:
			System.out.println("VisualFigaro : GWindowOpenModel : Unknown message received");
		}
	}
	
	public Element fillDocument() {
		return null;
	}
	
	public void loadXml(Element e) {
	}
}