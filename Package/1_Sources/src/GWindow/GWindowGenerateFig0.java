/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 03 November 2014                            
 * Author       : L.RAFFAELLI/ALL4TEC                                                                   
 * Modification : Creation
 * VF version   : 1.16
 * **************************************************************/

package GWindow;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import Factories.GNameRetrieverFactory;
import Factories.GXMLElementFactory;
import GMessage.GMessage;
import global.ControlTypes;
import global.ListTypes;
import global.NameRetrieverClasses;
import global.WindowClasses;
import jEditInterface.VisualFigaro;
import GModel.*;
import GKnowledgeBase.GKnowledgeBase;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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
import javax.swing.JCheckBox;

import GWidget.GWidgetControledList;
import GWidget.GWidgetListSimpleArray;
import GWidget.GWidgetOKCancel;
import GObject.GObject;
import GObjectInformation.GObjectInformation;

public class GWindowGenerateFig0 extends GWindow {
	
	private static final long serialVersionUID = 1L;

	//The variable visualFigaro to store the parent
	/**
	 * @uml.property  name="vfParent"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private VisualFigaro vfParent;
	
	//The model
	/**
	 * @uml.property  name="currentModel"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="GModel.GModel"
	 */
	private GModel currentModel;
	
	//The knowledge base linked to the model
	/**
	 * @uml.property  name="knowledgeBases"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="GKnowledgeBase.GKnowledgeBase"
	 */
	private GKnowledgeBase knowledgeBase;
	
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
	
	//The JTextField for the name of the Model
	/**
	 * @uml.property  name="nameTextField"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JTextField nameTextField;
	
	//The CheckBox resolveConst
	/**
	 * @uml.property  name="resolveConstChBx"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JCheckBox resolveConstChBx;
	
	//The CheckBox resolveAttr
	/**
	 * @uml.property  name="resolveAttrChBx"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JCheckBox resolveAttrChBx;
	
	//The CheckBox instanciateRule
	/**
	 * @uml.property  name="instanciateRuleChBx"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JCheckBox instanciateRuleChBx;
	
	//The Button to open the explorer window to find the path
	/**
	 * @uml.property  name="openExplorerButton"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JButton openExplorerButton;
	
	private GWidgetControledList groupsList;
	
	private int postTreatment;
	
	//Finally we have an ok/cancel widget
	/**
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetOKCancel okCancelWidget;
	
	public GWindowGenerateFig0(VisualFigaro vf, GObject p, GObjectInformation info, GModel model, int pTreatment) {
		super(p, info);
		
		//We initialize the parent
		vfParent = vf;
		
		//We retrieve the model
		currentModel = model;
		
		//We retrieve the knowledge base link to the model
		knowledgeBase = model.getKnowledgeBase();
		
		//We retrieve the post treatment to execute after figaro 0 file generation
		postTreatment = pTreatment;
		
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
		setSize(400, 450);
		setResizable(false);
		setTitle("Generate Figaro 0");
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
		JLabel titleLabel = new JLabel("Generate Figaro 0  ");
		logoPanel.add(titleLabel, BorderLayout.EAST);
	}
	
	private void initializeFieldPart() {
		
		//Initialization of the panel
		fieldsPanel = new JPanel(new BorderLayout());
		
		//The north panel containing all the field
		JPanel northPanel = new JPanel(new GridLayout(0,2));
		
		//Initialization of the names of the knowledge base and the model
		String kBName = knowledgeBase.getKnowledgeBaseName();
		int pos = kBName.lastIndexOf('\\');
		kBName =  kBName.substring(pos+1);
		
		String modelName = currentModel.getModelName();
		pos = modelName.lastIndexOf('\\');
		modelName =  modelName.substring(pos+1);
		
		//The panel containing all the labels
		northPanel.add(new JLabel("Knowledge Base : "));
		northPanel.add(new JLabel(kBName));
		northPanel.add(new JLabel("Model : "));
		northPanel.add(new JLabel(modelName));
		northPanel.add(new JLabel("Figaro 0 File : "));

		//Initialization of the name textfield
		nameTextField = new JTextField();
		nameTextField.setText(currentModel.getFigaro0Settings().getFileName());
		northPanel.add(nameTextField);
		
		//Then we add the north panel to the fields panel
		fieldsPanel.add(northPanel, BorderLayout.NORTH);
		
		//Initialization of the chxbox and name path panel
		JPanel explorePanel = new JPanel(new BorderLayout());
		
		//Initialization of the checkbox panel
		JPanel chBxPanel = new JPanel(new GridLayout(3,0));
		resolveConstChBx = new JCheckBox("Resolve Constants", currentModel.getFigaro0Settings().getResolveConstants());
		chBxPanel.add(resolveConstChBx);
		resolveAttrChBx = new JCheckBox("Resolve Attributes", currentModel.getFigaro0Settings().getResolveAttributs());
		chBxPanel.add(resolveAttrChBx);
		instanciateRuleChBx = new JCheckBox("Instanciate Rules", currentModel.getFigaro0Settings().getInstanciateRules());
		chBxPanel.add(instanciateRuleChBx);
		explorePanel.add(chBxPanel, BorderLayout.WEST);
		
		//Explore Button
		openExplorerButton = new JButton("Explore");
		openExplorerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWindowGenerateFig0.this.showFileChooser();
			}
		});
		explorePanel.add(openExplorerButton, BorderLayout.EAST);
		fieldsPanel.add(explorePanel, BorderLayout.CENTER);
		
		//Groups list
		if(currentModel.getFigaro0Settings().getGroupsList()==null)
			groupsList = new GWidgetControledList(this, information, "Groups list", null, ListTypes.SIMPLEARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDEL, WindowClasses.GROUPEREGLESSANSNOM, null);
		else
			groupsList = currentModel.getFigaro0Settings().getGroupsList();
			
		fieldsPanel.add(groupsList, BorderLayout.SOUTH);
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
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new MyFilter());
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.showOpenDialog(this);
		File file = fileChooser.getSelectedFile();
		if (file.toString().endsWith(".fi"))
			nameTextField.setText(file.toString());
		else
			nameTextField.setText(file.toString()+".fi");
		fileChooser.setVisible(false);
	}
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
		case OK:
			saveXmlToFile();
			saveSettings();
			dispose();
			vfParent.cmdFig0(nameTextField.getText(), postTreatment);
			break;
		
		case CANCEL:
			dispose();
			break;
			
		default:
			System.out.println("VisualFigaro : GWindowGenerateFig0 : Unknown message received");
		}
	}
	
	public Element fillDocument() {
		
		//Data preparation
		String resolve_Const;
		String resolve_Attr;
		String inst_Rule;
		
		if (resolveConstChBx.isSelected())
			resolve_Const = "VRAI";
		else
			resolve_Const = "FAUX";
		
		if (resolveAttrChBx.isSelected())
			resolve_Attr = "VRAI";
		else
			resolve_Attr = "FAUX";
		
		if (instanciateRuleChBx.isSelected())
			inst_Rule = "VRAI";
		else
			inst_Rule = "FAUX";
		
		//if there's no value in for the name of the figaro 0 file, we give the name of a temporary file
		if (nameTextField.getText().isEmpty())
			nameTextField.setText(System.getenv("TMP") + "\\fig0_temp.fi");
		
		this.root = new Element("REQUESTS");
		
		//Add the knowledgebase file name
		Element loadBDCFI = new Element("LOAD_BDC_FI");
		loadBDCFI.addContent(new Element("FILE_FI").setText(knowledgeBase.getKnowledgeBaseName()));
		this.root.addContent(loadBDCFI);
		
		//Add the model file name
		Element loadBDFFI = new Element("LOAD_BDF_FI");
		loadBDFFI.addContent(new Element("FILE").setText(currentModel.getModelName()));
		this.root.addContent(loadBDFFI);
		
		//Add the parameters of the treatment
		Element treatment = new Element("RUN_TREATMENT");
		treatment.addContent(new Element("TREATMENT").setText("GENERER_FIG0"));
		treatment.addContent(new Element("FILE").setText(nameTextField.getText()));
		treatment.addContent(new Element("FILE_MACRO").setText("fiabsimu.h"));
		treatment.addContent(new Element("RESOLVE_CONST").setText(resolve_Const));
		treatment.addContent(new Element("RESOLVE_ATTR").setText(resolve_Attr));
		treatment.addContent(new Element("INST_RULE").setText(inst_Rule));
		
		if (groupsList.GetNumberOfElement()>0){
			Element listgroups = new Element("LIST_GROUPS");
			for (int i=0; i<groupsList.GetNumberOfElement();i++){
				listgroups.addContent(new Element("GROUP").setText(groupsList.getList().listGetString(i)));
			}
			treatment.addContent(listgroups);
		}
		
		this.root.addContent(treatment);
		
		return root;
	}
	
	public void loadXml(Element e) {
	}
	
	public void saveXmlToFile() {
		fillDocument();
		
		String fileName = "./VisualFigaro/figp_commands.xml";
		
		try {
			FileOutputStream fichier = new FileOutputStream(fileName);
			
		    XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat().setEncoding("UTF-8"));
	
		    sortie.output(new Document(root), fichier);
		    fichier.close();
		    
		} catch (java.io.IOException ioe) {
			System.out.println("VisualFigaro : GWindowGenerateFig0 : Cannot save xml to file : " + ioe);
			JOptionPane.showMessageDialog(this, "VisualFigaro : GWindowGenerateFig0 : Cannot save xml to file : " + ioe);
		}
		
	}
	
	public void saveSettings(){
		
		//Save the name of the figaro 0 file if it is not a temporary file
		String filename;
		
		if(nameTextField.getText().endsWith("fig0_temp.fi"))
			filename = "";
		else
			filename = nameTextField.getText();
		
		currentModel.setFigaro0Settings(new GModelDataOperation(resolveConstChBx.isSelected(),resolveAttrChBx.isSelected(),instanciateRuleChBx.isSelected(),groupsList,filename));
	}
}