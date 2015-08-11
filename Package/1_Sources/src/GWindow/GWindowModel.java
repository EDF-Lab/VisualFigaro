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

import CopyFile.CopyFile;
import GMessage.GMessage;
import jEditInterface.VisualFigaro;
import GKnowledgeBase.GKnowledgeBase;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
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
import GXMLLoader.GXMLLoaderDefaultFiles;

public class GWindowModel extends GWindow {
	
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
	
	//The JTextField for the path of the Model
	/**
	 * @uml.property  name="pathTextField"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JTextField pathTextField;
	
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
	
	public GWindowModel(VisualFigaro vf, Vector<GKnowledgeBase> knowledgedBases) {
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
		setTitle("Model Creation Wizard");
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
		JLabel titleLabel = new JLabel("Model Creation Wizard  ");
		logoPanel.add(titleLabel, BorderLayout.EAST);
	}
	
	private void initializeFieldPart() {
		
		//Initialization of the panel
		fieldsPanel = new JPanel(new BorderLayout());
		
		//The north panel containing all the field
		JPanel northPanel = new JPanel(new GridLayout());
		
		//The panel containing all the labels
		JPanel labelPanel = new JPanel(new GridLayout(2,1,5,5));
		labelPanel.add(new JLabel("Name : "));
		labelPanel.add(new JLabel("Knowledge Base : "));
		northPanel.add(labelPanel);
		
		//The panel containing the components corresponding to the labels
		JPanel componentPanel = new JPanel(new GridLayout(2,1,5,5));
		//Initialization of the name textfield
		nameTextField = new JTextField();
		componentPanel.add(nameTextField);
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
		northPanel.add(componentPanel);
		
		//Then we add the north panel to the fields panel
		fieldsPanel.add(northPanel, BorderLayout.NORTH);
		
		//Initialization of the name path panel
		JPanel explorePanel = new JPanel(new BorderLayout());
		pathTextField = new JTextField();
		explorePanel.add(pathTextField, BorderLayout.CENTER);
		openExplorerButton = new JButton("Explore");
		openExplorerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWindowModel.this.showFileChooser();
			}
		});
		explorePanel.add(openExplorerButton, BorderLayout.EAST);
		fieldsPanel.add(explorePanel, BorderLayout.SOUTH);
	}
	
	private void showFileChooser() {
		String filename = vfParent.getPrevModelFile();
		String directory = "";
		if (!filename.equals(""))
			directory = filename.substring(0, filename.lastIndexOf('\\'));
		JFileChooser fileChooser = new JFileChooser(directory);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.showOpenDialog(this);
		File file = fileChooser.getSelectedFile();
		pathTextField.setText(file.toString());
		fileChooser.setVisible(false);
	}
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
		case OK:
			
			if(pathTextField.getText().isEmpty()){
				JOptionPane.showMessageDialog(GWindowModel.this, "Give a directory first");
				return;
			}
			
			//First we check that the path has been correctly set
			File directory = new File(pathTextField.getText());
			
			//If the directory does not exist we ask the user if he wants to create it
			if(!directory.exists()) {
				
				//Create the JOptionPane
				Object[] options = {"OK", "Cancel"};
				int choix = JOptionPane.showOptionDialog(GWindowModel.this, "The directory does not exist. Do you want to create it ?", "Creation Problem", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
				
				//If the choice is to create the directory then we create it otherwise we return
				if(choix == 0) {
					
					//If the creation of the directory fails then we warn the user and return otherwise we just continue
					if(!directory.mkdirs()) {
						JOptionPane.showConfirmDialog(GWindowModel.this, "Error GWindow Model");
						return;
					}
					
				} else {
					return;
				}
			}
			
			//Now that we know that the directory does exist then we check that no Model of the same name already exists
			String filename;
			if(vfParent.isOSWindows())
				filename = pathTextField.getText() + "\\" + nameTextField.getText();
			else
				filename = pathTextField.getText() + "/" + nameTextField.getText();
			
			if (!filename.toLowerCase().endsWith(".fi")){
				filename = filename + ".fi";
			}
			
			File model = new File(filename);
			
			//If an existing model with the same name already exists we have to warn the user and ask if he wants to create it or want to abort the creation
			if(model.exists()) {
				
				//Create the JOptionPane
				Object[] options = {"Open", "Overwrite"};
				int choix = JOptionPane.showOptionDialog(GWindowModel.this, "An already existing model with the same name exists in the specified folder. Do you want to open it or overwrite it?", "Creation Problem", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
				
				//If the choice is to create the directory then we overwrite everything otherwise we open the model
				if(choix == 0) {
					
					GKnowledgeBase kb = knowledgedBasesVector.get(knowledgeBaseComboBox.getSelectedIndex());
					
					//We use the openModel method of the visualFigaro parent
					vfParent.openModel(model, kb);
					
					//And we delete the window
					dispose();
					break;
				
					
				} else {

					//Just delete the model we will rewrite everything later
					model.delete();
				}
			}
			
			//The next step is to create the figaro file and fill it with the default text defined in the copiedFiles.xml file
			String figaroFileName = model.getAbsolutePath();
			File figaroFile = new File(figaroFileName);
			FileWriter writer = null;
			
			try {
				writer = new FileWriter(figaroFile);
				writer.write("");
			} catch(IOException e) {
				System.err.println("VisualFigaro : GWindowWizard : Exception during the creation of the figaro file : " + e);
			} finally {
				if(writer != null)
					try {
						writer.close();
					} catch (IOException e) {
						System.err.println("VisualFigaro : GWindowWizard : Exception while closing the file : " + figaroFileName + " : " + e);
					}
			}
			
			//We retrieve the selected knowledge base
			GKnowledgeBase kb = knowledgedBasesVector.get(knowledgeBaseComboBox.getSelectedIndex());
			
			//And we close the window
			dispose();
			
			//Finally we open the model in the text editor
			vfParent.openModel(model, kb);
			
			break;
		
		case CANCEL:
			dispose();
			break;
			
		default:
			System.out.println("VisualFigaro : GWindowModel : Unknown message received");
		}
	}
	
	public Element fillDocument() {
		return null;
	}
	
	public void loadXml(Element e) {
	}
}