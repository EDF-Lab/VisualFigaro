/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 02 December 2014                            
 * Author       : L.RAFFAELLI/ALL4TEC                              
 * Modification : Creation
 * VF version   : 1.16
 * **************************************************************/
 
package GWindow;

import global.ControlTypes;
import global.ListTypes;
import global.NameRetrieverClasses;
import global.WindowClasses;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.*;
import javax.swing.border.Border;

import jEditInterface.VisualFigaro;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import GKnowledgeBase.GKnowledgeBase;
import GMessage.GMessage;
import GModel.GModel;
import GModel.GModelDataOperation;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidgetControledList;
import GWidget.GWidgetOKCancel;

public class GWindowFaultTree extends GWindow {
	
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
	
	//The Button to open the explorer window to find the path
	/**
	 * @uml.property  name="openExplorerButton"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JButton openExplorerButton;
	
	private GWidgetControledList groupsList;
	
	private JComboBox coherenBx;
	
	private JComboBox simplBx;
	
	private JComboBox unloopBx;
	
	private JTextField objectNameTxt;
	
	private JTextField variableNameTxt;
	
	private JTextField treeNameTxt;
	
	private JTextField maxSonsGateTxt;
	
	//The root element of parameter file filled and explored in the window
	/**
	 * @uml.property  name="root2"
	 * @uml.associationEnd  
	 */
	protected Element root2;
	
	//Finally we have an ok/cancel widget
	/**
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetOKCancel okCancelWidget;
	
	public GWindowFaultTree(VisualFigaro vf, GObject p, GObjectInformation info, GModel model){
		super(p, info);
		
		//We initialize the parent
		vfParent = vf;
				
		//We retrieve the model
		currentModel = model;
				
		//We retrieve the knowledge base link to the model
		knowledgeBase = model.getKnowledgeBase();
		
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
		setSize(600, 500);
		setResizable(false);
		setTitle("Generate Fault Tree");
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
		JLabel titleLabel = new JLabel("Generate Fault Tree  ");
		logoPanel.add(titleLabel, BorderLayout.EAST);
	}
	
	private void initializeFieldPart() {
		
		//Initialization of the panel
		fieldsPanel = new JPanel(new BorderLayout());
		
		//JPanel northPanel = new JPanel(new BorderLayout());
		
		//The up-left panel containing all the field
		JPanel idPanel = new JPanel(new GridLayout(0,2));
		
		//Initialization of the names of the knowledge base and the model
		String kBName = knowledgeBase.getKnowledgeBaseName();
		int pos = kBName.lastIndexOf('\\');
		kBName =  kBName.substring(pos+1);
		
		String modelName = currentModel.getModelName();
		pos = modelName.lastIndexOf('\\');
		modelName =  modelName.substring(pos+1);
		
		//The panel containing all the labels
		idPanel.add(new JLabel("Knowledge Base : "));
		idPanel.add(new JLabel(kBName));
		idPanel.add(new JLabel("Model : "));
		idPanel.add(new JLabel(modelName));
		idPanel.add(new JLabel("Fault Tree File : "));
		
		//Initialization of the name textfield
		nameTextField = new JTextField();
		nameTextField.setText(currentModel.getFaultTreeSettings().getFileName());
		idPanel.add(nameTextField);
		
		idPanel.add(new JLabel(""));
		//Explore Button
		openExplorerButton = new JButton("Explore");
		openExplorerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWindowFaultTree.this.showFileChooser();
			}
		});
		idPanel.add(openExplorerButton);
		
		fieldsPanel.add(idPanel, BorderLayout.NORTH);
		
		//Building of the center Panel
		JPanel centerPanel = new JPanel(new GridLayout(1,2));
		
		//Initialization of the center-left Panel
		JPanel centerLeftPanel = new JPanel(new GridLayout(2,1));
		
		//Initialization of the Top Event Panel
		JPanel topEvent = new JPanel(new GridLayout(2,2));
		Border border = BorderFactory.createTitledBorder("Top Event");
		topEvent.setBorder(border);
		
		topEvent.add(new JLabel("Object : "));
		objectNameTxt = new JTextField();
		objectNameTxt.setText(currentModel.getFaultTreeSettings().getObject());
		topEvent.add(objectNameTxt);
		
		topEvent.add(new JLabel("Variable : "));
		variableNameTxt = new JTextField();
		variableNameTxt.setText(currentModel.getFaultTreeSettings().getVariable());
		topEvent.add(variableNameTxt);
		
		centerLeftPanel.add(topEvent);
		
		centerPanel.add(centerLeftPanel);
		
		//Groups list
		if(currentModel.getFaultTreeSettings().getGroupsList()==null)
			groupsList = new GWidgetControledList(this, information, "Groups list", null, ListTypes.SIMPLEARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDEL, WindowClasses.GROUPEREGLESSANSNOM, null);
		else
			groupsList = currentModel.getFaultTreeSettings().getGroupsList();
		
		centerPanel.add(groupsList);
		
		fieldsPanel.add(centerPanel, BorderLayout.CENTER);
		
		JPanel southPanel = new JPanel(new GridLayout(0,2));
		//Sub Panel
		JPanel leftPanel = new JPanel(new GridLayout(0,2));
		
		leftPanel.add(new JLabel("Simplification : "));
		simplBx = new JComboBox();
		simplBx.addItem("Complete");
		simplBx.addItem("Partial");
		simplBx.addItem("None");
		simplBx.setSelectedItem(currentModel.getFaultTreeSettings().getSimplification());
		leftPanel.add(simplBx);
		
		leftPanel.add(new JLabel("Unloop : "));
		unloopBx = new JComboBox();
		unloopBx.addItem("With");
		unloopBx.addItem("Without");
		unloopBx.setSelectedItem(currentModel.getFaultTreeSettings().getUnloop());
		leftPanel.add(unloopBx);
		
		leftPanel.add(new JLabel("Negation suppression : "));
		coherenBx = new JComboBox();
		coherenBx.addItem("Complete");
		coherenBx.addItem("Except leafs");
		coherenBx.addItem("None");
		coherenBx.setSelectedItem(currentModel.getFaultTreeSettings().getCoherency());
		leftPanel.add(coherenBx);
		
		southPanel.add(leftPanel);
		
		JPanel rightPanel = new JPanel(new GridLayout(0,2));
		
		rightPanel.add(new JLabel("Tree Name : "));
		treeNameTxt = new JTextField();
		treeNameTxt.setText(currentModel.getFaultTreeSettings().getTreeName());
		rightPanel.add(treeNameTxt);
		
		rightPanel.add(new JLabel("Max Sons Gate : "));
		maxSonsGateTxt = new JTextField();
		maxSonsGateTxt.setText(currentModel.getFaultTreeSettings().getMaxSonsGate());
		rightPanel.add(maxSonsGateTxt);
		
		southPanel.add(rightPanel);
		
		fieldsPanel.add(southPanel, BorderLayout.SOUTH);
	}
	
	private void showFileChooser() {		
		class MyFilter extends javax.swing.filechooser.FileFilter {
		    public boolean accept(File file) {
			    if(file.isDirectory()) {
				   return true;
				}	
		        String filename = file.getName();
		        return filename.endsWith(".xml");
		    }
		    public String getDescription() {
		        return "*.xml";
		    }
		}
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new MyFilter());
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.showOpenDialog(this);
		File file = fileChooser.getSelectedFile();
		if (file.toString().endsWith(".xml"))
			nameTextField.setText(file.toString());
		else
			nameTextField.setText(file.toString()+".xml");
		fileChooser.setVisible(false);
	}
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
		case OK:
			if((!maxSonsGateTxt.getText().isEmpty())&&maxSonsGateTxt.getText().matches("^\\p{Digit}+$")){
				if(!objectNameTxt.getText().isEmpty()){
					if(!variableNameTxt.getText().isEmpty()){
						saveXmlToFile();
						saveSettings();
						dispose();
						vfParent.generateFT(nameTextField.getText());
					}
					else
						JOptionPane.showMessageDialog(this, "Please specified a test variable name");
				}
				else
					JOptionPane.showMessageDialog(this, "Please specified an object name");
			}
			else
				JOptionPane.showMessageDialog(this, "Max Sons Gate value is not an integer");
			break;
		
		case CANCEL:
			dispose();
			break;
			
		default:
			System.out.println("VisualFigaro : GWindowFaultTree : Unknown message received");
		}
	}
	
	public Element fillDocument() {
		
		//Data preparation
		String simplification;
		String coherency;
		String unloop;
		
		if (simplBx.getSelectedItem().toString().equals("Complete"))
			simplification = "TOTALE";
		else if (simplBx.getSelectedItem().toString().equals("Partial"))
			simplification = "PARTIELLE";
		else
			simplification = "AUCUNE";
		
		if (coherenBx.getSelectedItem().toString().equals("Complete"))
			coherency = "TOTALE";
		else if (coherenBx.getSelectedItem().toString().equals("Except leafs"))
			coherency = "PARTIELLE";
		else
			coherency = "AUCUNE";

		if (unloopBx.getSelectedItem().toString().equals("With"))
			unloop = "VRAI";
		else
			unloop = "FAUX";
		
		//if there's no value in for the name of the fault tree file, we give the name of a temporary file
		if (nameTextField.getText().isEmpty())
			nameTextField.setText(System.getenv("TMP") + "\\faulttree_temp.xml");
		
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
		treatment.addContent(new Element("TREATMENT").setText("GENERATE_TREE"));
		treatment.addContent(new Element("FILE").setText(nameTextField.getText()));
		treatment.addContent(new Element("FILE_MACRO").setText("fiab_ADD.h"));
		treatment.addContent(new Element("RESOLVE_CONST").setText("TRUE"));
		treatment.addContent(new Element("RESOLVE_ATTR").setText("TRUE"));
		treatment.addContent(new Element("INST_RULE").setText("TRUE"));
		treatment.addContent(new Element("FILE_TREE_OPTIONS").setText("./VisualFigaro/figp_params.xml"));
		
		this.root.addContent(treatment);
		
		this.root2 = new Element("GEN_TREE_OPTIONS");
		
		root2.addContent(new Element("SIMPLIFICATION").setText(simplification));
		root2.addContent(new Element("COHERENCY").setText(coherency));
		root2.addContent(new Element("UNLOOP").setText(unloop));
		root2.addContent(new Element("TREE_NAME").setText(treeNameTxt.getText()));
		root2.addContent(new Element("PREFIX").setText("__ARBRE__"));

		Element testVariable = new Element("TEST_VARIABLE");
		testVariable.addContent(new Element("OBJECT").setText(objectNameTxt.getText()));
		testVariable.addContent(new Element("VARIABLE").setText(variableNameTxt.getText()));
		this.root2.addContent(testVariable);
		
		root2.addContent(new Element("MAX_SONS_GATE").setText(maxSonsGateTxt.getText()));
		
		if (groupsList.GetNumberOfElement()>0){
			Element listgroups = new Element("LIST_GROUPS");
			for (int i=0; i<groupsList.GetNumberOfElement();i++){
				listgroups.addContent(new Element("GROUP").setText(groupsList.getList().listGetString(i)));
			}
			root2.addContent(listgroups);
		}
		
		return root;
	}
	
	public void loadXml(Element e) {
		
	}
	
	public void saveXmlToFile() {
		fillDocument();
		
		String fileName1 = "./VisualFigaro/figp_commands.xml";
		String fileName2 = "./VisualFigaro/figp_params.xml";
		
		try {
			FileOutputStream fichier = new FileOutputStream(fileName1);
			
		    XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat().setEncoding("UTF-8"));
	
		    sortie.output(new Document(root), fichier);
		    fichier.close();
		    
		    FileOutputStream fichier2 = new FileOutputStream(fileName2);
		    
		    XMLOutputter sortie2 = new XMLOutputter(Format.getPrettyFormat().setEncoding("UTF-8"));
			
		    sortie2.output(new Document(root2), fichier2);
		    fichier2.close();
		    
		} catch (java.io.IOException ioe) {
			System.out.println("VisualFigaro : GWindowFaultTree : Cannot save xml to file : " + ioe);
			JOptionPane.showMessageDialog(this, "VisualFigaro : GWindowFaultTree : Cannot save xml to file : " + ioe);
		}
		
	}
	
	public void saveSettings(){
		
		//Save the name of the figaro 0 file if it is not a temporary file
		String filename;
		
		if(nameTextField.getText().endsWith("faulttree_temp.xml"))
			filename = "";
		else
			filename = nameTextField.getText();
		
		currentModel.setFaultTreeSettings(new GModelDataOperation(groupsList,filename,simplBx.getSelectedItem().toString(),unloopBx.getSelectedItem().toString(),coherenBx.getSelectedItem().toString(),treeNameTxt.getText(),maxSonsGateTxt.getText(),objectNameTxt.getText(),variableNameTxt.getText()));
	}
}