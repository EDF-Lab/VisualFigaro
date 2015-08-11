/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 05 December 2014                            
 * Author       : L.RAFFAELLI/ALL4TEC                              
 * Modification : Creation
 * VF version   : 1.16
 * **************************************************************/
 
package GWindow;

import jEditInterface.VisualFigaro;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.border.*;

import java.util.Vector;
import java.util.List;

import org.gjt.sp.jedit.jEdit;
import org.jdom.Element;

import GKnowledgeBase.GKnowledgeBase;
import GMessage.GMessage;
import GModel.GModel;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidgetOKCancel;

public class GWindowNewObject extends GWindow {
	
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
	
	//Finally we have an ok/cancel widget
	/**
	 * @uml.property  name="okCancelWidget"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GWidgetOKCancel okCancelWidget;
	
	private JPanel centerPanel;
	
	private JTextField objectName;
	
	private JComboBox classBx;
	
	private JPanel constantPanel;
	
	private JPanel attributPanel;
	
	private JPanel interfacePanel;
	
	private Vector<JTextField> constantLabel;
	
	private Vector<JTextField> constantText;
	
	private Vector<JTextField> attributLabel;
	
	private Vector<JTextField> attributText;
	
	private Vector<JTextField> interfaceLabel;
	
	private Vector<JTextField> interfaceText;
	
	public GWindowNewObject(VisualFigaro vf, GObject p, GObjectInformation info, GModel model){
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
		setSize(700, 600);
		setResizable(false);
		setTitle("New Object");
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
		JLabel titleLabel = new JLabel("Create New Object  ");
		logoPanel.add(titleLabel, BorderLayout.EAST);
	}
	
	private void initializeFieldPart() {
		
		//Initialization of the panel
		fieldsPanel = new JPanel(new BorderLayout());
		
		JPanel northPanel = new JPanel(new GridLayout(0,2));
		northPanel.setPreferredSize(new Dimension(500,40));
		
		//Info Panel
		JPanel infoPanel = new JPanel(new GridLayout(0,2));
		
		//Initialization of the names of the knowledge base and the model
		String kBName = knowledgeBase.getKnowledgeBaseName();
		int pos = kBName.lastIndexOf('\\');
		kBName =  kBName.substring(pos+1);
		
		String modelName = currentModel.getModelName();
		pos = modelName.lastIndexOf('\\');
		modelName =  modelName.substring(pos+1);
		
		//The panel containing all the labels
		infoPanel.add(new JLabel("Knowledge Base : "));
		infoPanel.add(new JLabel(kBName));
		infoPanel.add(new JLabel("Model : "));
		infoPanel.add(new JLabel(modelName));
		
		northPanel.add(infoPanel);
		
		//Class selection Panel
		JPanel classPanel = new JPanel(new GridLayout(0,2));
		
		classPanel.add(new JLabel("Object Name : "));
		objectName = new JTextField();
		classPanel.add(objectName);
		classPanel.add(new JLabel("Object Class : "));
		classBx = selectKBClass();
		classBx.setSelectedIndex(-1);
		classPanel.add(classBx);
		
		northPanel.add(classPanel);
		
		fieldsPanel.add(northPanel,BorderLayout.NORTH);
		
	}
	
	//build the classBx combo
	private JComboBox selectKBClass(){
		JComboBox clbx = new JComboBox();
		Vector<String> cllist = figaroLoader.findTypesName();
		
		for(String cl : cllist)
			clbx.addItem(cl);
		
		clbx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					fieldsPanel.remove(centerPanel);
				}
				catch(Exception except){
					System.out.println("Initialization (centerPanel not added)");
				}
				
				//Initialization of the center Panel
				centerPanel = new JPanel(new GridLayout(1,3));
				centerPanel.setPreferredSize(new Dimension(500,420));
				
				constantPanel = buildConstantPanel();
				attributPanel = buildAttributPanel();
				interfacePanel = buildInterfacePanel();
				
				centerPanel.add(constantPanel);
				centerPanel.add(attributPanel);		
				centerPanel.add(interfacePanel);
				
				fieldsPanel.add(centerPanel, BorderLayout.CENTER);
				
				fieldsPanel.revalidate();
				fieldsPanel.repaint();
				framePanel.repaint();

			}
		});
		
		return clbx;
	}
	
	//build the panel which displays constants
	private JPanel buildConstantPanel(){
		JPanel constPanel = new JPanel(new GridLayout(0,2));
		Border border = BorderFactory.createTitledBorder("Constants");
		String selectedClass;
		String constname;
		Boolean overloaded;
		List<Element> constants;
		constantLabel = new Vector<JTextField>();
		constantText = new Vector<JTextField>();
		
		constPanel.setBorder(border);
		
		if (classBx.getSelectedIndex() > -1){
			selectedClass = classBx.getItemAt(classBx.getSelectedIndex()).toString();
			
			while(!selectedClass.equals("FIGARO")){
				constants = getClassElements(selectedClass,"CONSTANT");
			
				if(constants!=null&&!constants.isEmpty()){
					for(Element e: constants){
						overloaded = false;
						constname = e.getChild("NAME").getValue();
						if(!constantLabel.isEmpty())
							for(JTextField c:constantLabel)
								if(c.getText().equals(constname))
									overloaded = true;
						if(!overloaded){
							constantLabel.add(new JTextField(constname));
							constantText.add(new JTextField());
						}
					}
				}
				
				selectedClass = getClassFather(selectedClass);
			}
			
			int sizey = 15;
			if(constantLabel.size()>sizey)
				sizey = constantLabel.size();
			
			JPanel leftPanel = new JPanel(new GridLayout(sizey,0));
			JPanel rightPanel = new JPanel(new GridLayout(sizey,0));
			
			for(int i=0;i<constantLabel.size();i++){
				constantLabel.get(i).setEditable(false);
				leftPanel.add(constantLabel.get(i));
				rightPanel.add(constantText.get(i));
			}
			
			constPanel.add(leftPanel);
			constPanel.add(rightPanel);	
		}		
		
		return constPanel;
	}
	
	//build the panel which displays attributs
	private JPanel buildAttributPanel(){
		JPanel attrPanel = new JPanel(new GridLayout(0,2));
		Border border = BorderFactory.createTitledBorder("Attributes");
		String selectedClass;
		String attrname;
		Boolean overloaded;
		List<Element> attributs;
		attributLabel = new Vector<JTextField>();
		attributText = new Vector<JTextField>();
		
		attrPanel.setBorder(border);
		
		if (classBx.getSelectedIndex() > -1){
			selectedClass = classBx.getItemAt(classBx.getSelectedIndex()).toString();
			
			while(!selectedClass.equals("FIGARO")){
				attributs = getClassElements(selectedClass,"ATTRIBUT");
			
				if(attributs!=null&&!attributs.isEmpty()){
					for(Element e: attributs){
						overloaded = false;
						attrname = e.getChild("NAME").getValue();
						if(!attributLabel.isEmpty())
							for(JTextField c:attributLabel)
								if(c.getText().equals(attrname))
									overloaded = true;
						if(!overloaded){
							attributLabel.add(new JTextField(attrname));
							attributText.add(new JTextField());
						}
					}
				}
				
				selectedClass = getClassFather(selectedClass);
			}
			
			int sizey = 15;
			if(attributLabel.size()>sizey)
				sizey = attributLabel.size();
			
			JPanel leftPanel = new JPanel(new GridLayout(sizey,0));
			JPanel rightPanel = new JPanel(new GridLayout(sizey,0));
			
			for(int i=0;i<attributLabel.size();i++){
				attributLabel.get(i).setEditable(false);
				leftPanel.add(attributLabel.get(i));
				rightPanel.add(attributText.get(i));
			}
			
			attrPanel.add(leftPanel);
			attrPanel.add(rightPanel);
		}
		
		return attrPanel;
	}
	
	//build the panel which displays interfaces
	private JPanel buildInterfacePanel(){
		JPanel interPanel = new JPanel(new GridLayout(0,2));
		Border border = BorderFactory.createTitledBorder("Interfaces");
		String selectedClass;
		String intername;
		Boolean overloaded;
		List<Element> interfaces;
		interfaceLabel = new Vector<JTextField>();
		interfaceText = new Vector<JTextField>();
		
		interPanel.setBorder(border);
		
		if (classBx.getSelectedIndex() > -1){
			selectedClass = classBx.getItemAt(classBx.getSelectedIndex()).toString();
			
			while(!selectedClass.equals("FIGARO")){
				interfaces = getClassElements(selectedClass,"INTERFACE");
			
				if(interfaces!=null&&!interfaces.isEmpty()){
					for(Element e: interfaces){
						overloaded = false;
						intername = e.getChild("NAME").getValue();
						if(!interfaceLabel.isEmpty())
							for(JTextField c:interfaceLabel)
								if(c.getText().equals(intername))
									overloaded = true;
						if(!overloaded){
							interfaceLabel.add(new JTextField(intername));
							interfaceText.add(new JTextField());
						}
					}
				}
				
				selectedClass = getClassFather(selectedClass);
			}
			
			int sizey = 15;
			if(interfaceLabel.size()>sizey)
				sizey = interfaceLabel.size();
			
			JPanel leftPanel = new JPanel(new GridLayout(sizey,0));
			JPanel rightPanel = new JPanel(new GridLayout(sizey,0));
			
			for(int i=0;i<interfaceLabel.size();i++){
				interfaceLabel.get(i).setEditable(false);
				leftPanel.add(interfaceLabel.get(i));
				rightPanel.add(interfaceText.get(i));
			}
			
			interPanel.add(leftPanel);
			interPanel.add(rightPanel);
		}
		
		return interPanel;
	}
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
		case OK:
			if(!objectName.getText().isEmpty()){
				saveObject();
				dispose();
			}
			else
				JOptionPane.showMessageDialog(this, "Give a name to the object first !");
			break;
		
		case CANCEL:
			dispose();
			break;
			
		default:
			System.out.println("VisualFigaro : GWindowNewObject : Unknown message received");
		}
	}
	
	//Find the Parent of the class in parameter
	private String getClassFather(String clname){
		String father="";
		Element elclass;
		elclass = figaroLoader.findType(clname);
		
		father = elclass.getChild("FATHER").getValue();
		
		return father;
	}
	
	//Find the List of Constant of the class in parameter
	private List<Element> getClassElements(String clname, String key){
		List<Element> results;
		Element elclass;
		elclass = figaroLoader.findType(clname);
		
		results = elclass.getChildren(key);
		
		return results;
	}
	
	public void saveObject(){
		String Line = "";
		boolean firstLine;
		
		//For translation purpose
		String object;
		String isa;
		String constant;
		String attribut;
		String interfac;
		if (currentModel.getModelLanguage().getLanguage().equals("English")){
			object = "OBJECT";
			isa = "IS_A";
			constant = "CONSTANT";
			attribut = "ATTRIBUTE";
			interfac = "INTERFACE";
		}
		else {
			object = "OBJET";
			isa = "EST_UN";
			constant = "CONSTANTE";
			attribut = "ATTRIBUT";
			interfac = "INTERFACE";
		}
		
		//JOptionPane.showMessageDialog(this, "Constants : "+constantLabel.size() + "/Attributes : "+attributLabel.size() + "/Interfaces : "+interfaceLabel.size());
		//JOptionPane.showMessageDialog(this, "Constants : "+constantText.size() + "/Attributes : "+attributText.size() + "/Interfaces : "+interfaceText.size());
		
		vfParent.getView().getTextArea().goToBufferEnd(false);
		addLine("\n",true);
		
		//Name and class of the object
		Line = object + " " + objectName.getText() + " " + isa + " " + classBx.getItemAt(classBx.getSelectedIndex()) + " ;";
		addLine(Line,true);
		
		//Constants of the object
		Line = "\t" + constant;
		firstLine = true;
		for(int i=0;i<constantLabel.size();i++){
			if(!constantText.get(i).getText().isEmpty()){
				if(firstLine){
					firstLine = false;
					addLine(Line,true);
				}
				Line = "\t\t" + constantLabel.get(i).getText() + " = " + constantText.get(i).getText() + " ;";
				addLine(Line,true);
			}
			/*else
				JOptionPane.showMessageDialog(this, "Constante "+constantLabel.get(i).getText()+" is empty...");*/
		}
		
		//Attributs of the object
		Line = "\t" + attribut;
		firstLine = true;
		for(int i=0;i<attributLabel.size();i++){
			if(!attributText.get(i).getText().isEmpty()){
				if(firstLine){
					firstLine = false;
					addLine(Line,true);
				}
				Line = "\t\t" + attributLabel.get(i).getText() + " = " + attributText.get(i).getText() + " ;";
				addLine(Line,true);
			}
			/*else
				JOptionPane.showMessageDialog(this, "Attribut "+attributLabel.get(i).getText()+" is empty...");*/
		}
		
		//Interfaces of the object
		Line = "\t" + interfac;
		firstLine = true;
		for(int i=0;i<interfaceLabel.size();i++){
			if(!interfaceText.get(i).getText().isEmpty()){
				if(firstLine){
					firstLine = false;
					addLine(Line,true);
				}
				Line = "\t\t" + interfaceLabel.get(i).getText() + " = " + interfaceText.get(i).getText() + " ;";
				addLine(Line,true);
			}
			/*else
				JOptionPane.showMessageDialog(this, "Interface "+interfaceLabel.get(i).getText()+" is empty...");*/
		}
		 
	}
	
	public void addLine(String txt,boolean brk){
		vfParent.getView().getTextArea().setSelectedText(txt);
		if(brk)
			vfParent.getView().getTextArea().setSelectedText("\n");
	}
	
	public Element fillDocument() {
		return null;
	}
	
	public void loadXml(Element e) {
		
	}
}