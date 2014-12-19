/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 2 April 2010                            
 * Author       : D.WEYAND/ALL4TEC                              
 * Bug Id       :                                         
 * Modification : Implements new KB3 translator
 * VF version   : 1.5
 * **************************************************************
 * Date         : 13 April 2010                            
 * Author       : D.WEYAND/ALL4TEC                              
 * Bug Id       :                                         
 * Modification : Fix TradBdC file implementation bug
 *                + Code cleanup to avoid warnings
 * VF version   : 1.7
 * **************************************************************
 * Date         : 16 April 2010                            
 * Author       : M.BOUISSOU/EDF                          
 * Bug Id       :                                         
 * Modification : Modify file organization for BdC translation
 * VF version   : 1.71
 * **************************************************************
 * Date         : 24 September 2010                            
 * Author       : D.WEYAND/ALL4TEC                          
 * Bug Id       : n° 57                                      
 * Modification : Fix Create Default File missing names
 *                this change has been cancelled in version 1.15 because it 
 *                created a new bug, when dealing with an english KB.
 * VF version   : 1.10
 * **************************************************************
 * Date         : 12 October 2010                            
 * Author       : D.WEYAND/ALL4TEC                          
 * Bug Id       : 
 * Evol Id      : n°4 and n°12                                   
 * Modification : Modify temp file destination folder (in user space)(evol n°4)
 *                Accept multiple .fi and .bdc in the same folder (evol n°12)
 * VF version   : 1.11
 * **************************************************************
 * Date         : 19 October 2010                            
 * Author       : D.WEYAND/ALL4TEC                          
 * Bug Id       : 
 * Evol Id      : n°4 and n°8                                  
 * Modification : use TMP environment variable for temp file saving (evol n°4 correction)
 *                memorize last KB file access through VisualFigaro.ini file (evol n°8)
 * VF version   : 1.12
 * **************************************************************
 * Date         : 20 October 2010                            
 * Author       : D.WEYAND/ALL4TEC                          
 * Bug Id       : 
 * Evol Id      : n°8                                  
 * Modification : Add direct access to prev KB directory in openKB dialog
 * VF version   : 1.12a
 * **************************************************************
 * Date         : 21 October 2010                            
 * Author       : D.WEYAND/ALL4TEC                          
 * Bug Id       : n°61
 * Evol Id      : n°8                                  
 * Modification : If folder and .ini file don't exist : create them (Evol n°8)
 *                Update TradBdc.ini file upon buffer switching (Bug n°61)
 * VF version   : 1.12b
 * **************************************************************
 * Date         : 2 November 2010                            
 * Author       : D.WEYAND/ALL4TEC                          
 * Bug Id       : 
 * Evol Id      : n°4                                 
 * Modification : Modify working directory (user space) for st.exe launching
 * VF version   : 1.14
 * **************************************************************
 * Date         : 8 November 2010                            
 * Author       : D.WEYAND/ALL4TEC                          
 * Bug Id       : n°61
 * Evol Id      :                                
 * Modification : 1. Fix TradBdc files update bug (upon null file manipulation)
 *                2. Modify TradBdc.ini strings construction method to insure
 *                   correct file naming
 *                3. Current file update upon KB buffer closing (case of multiple buffers)
 * VF version   : 1.14
 * ***************************************************************
 * Date         : 10 April 2012                            
 * Author       : M. Bouissou                          
 * Bug Id       : 
 * Evol Id      : n°14                               
 * Modification : 1. Use of a relative path to initialize the variable BdCPath which gives 
 *                   the directory of the TradBdC.exe program. 
 * VF version   : 1.15
 * ***************************************************************
 * Date         : 15 April 2012                            
 * Author       : M. Bouissou                          
 * Bug Id       : 
 * Bug Id       : n°67                               
 * Modification : 1. Delete the check about the existence of a .sym file without an .ico equivalent
 * VF version   : 1.15
 * ***************************************************************
 * Date         : 15 April 2012                            
 * Author       : M. Bouissou                          
 * Bug Id       : 
 * Bug Id       : n°68                               
 * Modification : 1. Correction of the origin directory for the copy of the icons directory in case of translation
 * VF version   : 1.15
 * ***************************************************************
 * Date         : 15 April 2012                            
 * Author       : M. Bouissou                          
 * Bug Id       : 
 * Bug Id       : n°69                               
 * Modification : 1. Delete an instruction which created the Francais directory without any action from the user
 * VF version   : 1.15
 * **************************************************************
 * Date			: 29 October 2014
 * Author		: L. RAFFAELLI/ALL4TEC
 * Bug Id		:
 * Bug Id		:
 * Modification : Implements models management
 * VF version	: 1.16 
 * ***************************************************************/

package jEditInterface;

/*
 * VisualFigaro.java
 * part of the VisualFigaro plugin for the jEdit text editor
 * Copyright (C) 2008 Guillaume Torrente & Marc Bouissou
 * guillaumetorrente@yahoo.fr
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Vector;
import java.util.List;




// from Swing:
import javax.swing.*;

// from jEdit:
import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.gui.*;
import org.gjt.sp.jedit.msg.EditPaneUpdate;
import org.gjt.sp.jedit.msg.BufferUpdate;
import org.jdom.Element;
import org.jdom.input.DOMBuilder;
import org.jdom.Document;
import org.jdom.filter.Filter;
import org.jdom.input.SAXBuilder;

import GModel.GModel;
import GKnowledgeBase.GKnowledgeBase;
import GLanguage.GLanguage;
import GObjectInformation.GObjectInformation;
import GWindow.GWindow;
import GWindow.GWindowAbout;
import GWindow.GWindowMain;
import GWindow.GWindowWizard;
import GWindow.GWindowModel;
import GWindow.GWindowOpenModel;
import GWindow.GWindowGenerateFig0;
import GWindow.GWindowFaultTree;
import GWindow.GWindowNewObject;
import GXMLLoader.GXMLLoader;
import GXMLLoader.GXMLLoaderDefaultFiles;
import GXMLLoader.GXMLLoaderFigaro;
import CopyFile.CopyFile;


// from DomEcho for me
import figaroInterface.*;

/**
 * This is the main class of the Visual Figaro plugin. It associates the file written in Figaro language to the tree representation and also to the bdc file trough the "Edit XML File" button.
 * @author Guillaume Torrente & Marc Bouissou
 */
public class VisualFigaro extends JPanel implements EBComponent, VisualFigaroActions, DefaultFocusComponent
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * @uml.property  name="view"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private View view;
	/**
	 * @uml.property  name="floating"
	 */
	private boolean floating;

	/**
	 * @uml.property  name="translateButton"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JButton translateButton;
	/**
	 * @uml.property  name="userInterface"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JPanel userInterface;
	/**
	 * @uml.property  name="comboTree"
	 * @uml.associationEnd  
	 */
	private JComboBox comboTree;
	
	//Takes care of all the Figaro language
	/**
	 * @uml.property  name="figTree"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="visualFigaro:figaroInterface.FigaroTree"
	 */
	private FigaroTree figTree;

	//The menu bar
	/**
	 * @uml.property  name="menuBar"
	 * @uml.associationEnd  
	 */
	private JMenuBar menuBar;
	/**
	 * @uml.property  name="languageMenu"
	 * @uml.associationEnd  qualifier="newLanguage:java.lang.String javax.swing.JRadioButtonMenuItem"
	 */
	//private Hashtable<String, JRadioButtonMenuItem> languageMenu;
	
	//The vector of all the opened knowledge base
	/**
	 * @uml.property  name="knowledgedBasesVector"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="GKnowledgeBase.GKnowledgeBase"
	 */
	private Vector<GKnowledgeBase> knowledgedBasesVector;
	
	//The vector of all the opened model
	private Vector<GModel> modelsVector;
	
	//Buffer which indicates which jEdit buffer (file) is currently used (modified, deleted...)
	/**
	 * @uml.property  name="currentFile"
	 */
	private String currentFile;

	//Indicate that we are currently opening a file. We set up this variable in order to avoid a lot of actionListener firing and therefore useless calculus from the comboBoxes
	/**
	 * @uml.property  name="isOpening"
	 */
	private boolean isOpening = false;
	
	private String languageName;
	private JMenuItem menuPrevKBFileItem;
	private JMenu menu;
	
	/**
	 * The constructor takes a View to interact with the jEdit part and a String representing the position of the window inside the jEdit window.
	 * @param viewArg The view used to display the Figaro text. Usually the view used in the jEdit program.
	 * @param position String indicating if the window is a floating window or if the plugin is docked in one of the side of the jEdit window.
	 */
	public VisualFigaro(View viewArg, String position)
	{
		super(new BorderLayout());
		
		//Initialization of the class variables
		this.view = viewArg;
		this.floating  = position.equals(DockableWindowManager.FLOATING);
		this.knowledgedBasesVector = new Vector<GKnowledgeBase>();
		this.modelsVector = new Vector<GModel>();
		
		//First we create the panel which will be on the top of the window and which contains the menu bar, the folding menu and some icones
		initializeUserInterface();
		
		//We add the userInterface to the current panel
		add(userInterface, BorderLayout.NORTH);
		
		//Now we are going to create the printer of the tree 
		figTree = new FigaroTree(this);
		add(BorderLayout.CENTER, figTree);
		
		//We add a mouseListener on the textArea
		this.view.getEditPane().getTextArea().getPainter().addMouseListener(new MouseListener () {
			public void mousePressed(MouseEvent me) {
				//First we have to set to zero all the colors which have been eventually associated with the tree previously
				if(comboTree.getComponentCount() > 0)
					if(comboTree.getSelectedItem() != null)
						if(comboTree.getSelectedItem().toString().equals(currentFile))
							figTree.mouseClicked();
			}
			public void mouseReleased(MouseEvent me) {
			}
			public void mouseEntered(MouseEvent me) {
			}
			public void mouseExited(MouseEvent me) {
			}
			public void mouseClicked(MouseEvent me) {
			}
		});
		
		//We initialize and add the translate button to the panel
		initializeTranslateButton();
		add(translateButton, BorderLayout.EAST);
		
		if(floating)
			this.setPreferredSize(new Dimension(500, 250));
	}
	
	/**
	 * Initialize the userInterface variable.
	 */
	private void initializeUserInterface() {
		
		//First we create the userInterface
		userInterface = new JPanel(new BorderLayout());
		
		//We initialize the menubar and add it to the interface
		initializeMenuBar();
		userInterface.add(menuBar, BorderLayout.NORTH);
		
		//We initialize the combobox and add it to the interface
		initializeFileCombo();
		userInterface.add(comboTree, BorderLayout.SOUTH);
	}
	
	/**
	 * Initialize the menuBar of the plugin.
	 */
	private void initializeMenuBar() {
		
		//We create the menu bar
		menuBar = new JMenuBar();
		//JMenu menu;
		JMenuItem menuItem;
		
		//The menu for the KB management
		menu = new JMenu("KB Management");
		
		//The menu concerning the management of the KB
		menuItem = new JMenuItem("New KB");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VisualFigaro.this.createKB();
			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Open KB");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VisualFigaro.this.openKB();
			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Close KB");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VisualFigaro.this.closeKB();
			}
		});
		menu.add(menuItem);
		menu.addSeparator();
		
		menuItem = new JMenuItem("Translate KB");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					VisualFigaro.this.translateKB();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		menu.add(menuItem);
		menu.addSeparator();
		
		menuPrevKBFileItem = new JMenuItem(getPrevKBFile());
		menuPrevKBFileItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File f = new File(getPrevKBFile());
				File directory = new File(f.getParent());
				openKB(directory, f);
			}
		});
		menu.add(menuPrevKBFileItem);
		menu.addSeparator();
		
		menuItem = new JMenuItem("Edit XML File");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VisualFigaro.this.editXMLFile();
			}
		});
		menu.add(menuItem);
		menu.addSeparator();
		
		menuItem = new JMenuItem("About Visual Figaro");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VisualFigaro.this.aboutVisualFigaro();
			}
		});
		menu.add(menuItem);
		menuBar.add(menu);
		
		//The new menu concerning the management of the objects of the KB and new functions
		menu = new JMenu("Object Management");
		menuItem = new JMenuItem("New Model");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VisualFigaro.this.createModel();
			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Open Model");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VisualFigaro.this.openModel();
			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Close Model");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VisualFigaro.this.closeModel();
			}
		});
		menu.add(menuItem);
		menu.addSeparator();
		
		menuItem = new JMenuItem("New Object");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VisualFigaro.this.createObject();
			}
		});
		menu.add(menuItem);
		menu.addSeparator();
		
		menuItem = new JMenuItem("Check Model");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VisualFigaro.this.checkModel();
			}
		});
		menu.add(menuItem);
		menu.addSeparator();
		
		menuItem = new JMenuItem("Generate Figaro 0");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VisualFigaro.this.generateFig0(0);
			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Run Fig0debug");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VisualFigaro.this.runFig0debug();
			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Run Figseq");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VisualFigaro.this.runFigseq();
			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Run Yams");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VisualFigaro.this.runYams();
			}
		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Generate Fault Tree");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VisualFigaro.this.generateFaultTree();
			}
		});
		menu.add(menuItem);
		menuBar.add(menu);
	}
	
	/**
	 * Initialize the comboBox containing the name of the files edited.
	 */
	private void initializeFileCombo() {
		
		comboTree = new JComboBox();
		comboTree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//First we check if the combo box has a valid item selected. If it is not the case we have to delete the tree in the current window.
				if(comboTree.getSelectedIndex() < 0) {

					//We load a null tree
					figTree.loadSpecificGTree(null);

				} else {
					
					int pos=0;
					
					//First we get the current selected item
					currentFile = comboTree.getSelectedItem().toString();
					
					//We look for the right position
					for(pos=0; pos<view.getEditPane().getBufferSwitcher().getItemCount(); pos++)
						if(((Buffer)view.getEditPane().getBufferSwitcher().getItemAt(pos)).getPath().equals(currentFile))
							break;
					view.getEditPane().getBufferSwitcher().setSelectedIndex(pos);
					view.getEditPane().getBufferSwitcher().updateBufferList();
					
					//We verify that the file is a KB or a model
					if (findKnowledgeBaseFromName(currentFile)==null){

						if (findModelFromName(currentFile)==null){
							//The file is not a KB either a model, we load a null tree
							figTree.loadSpecificGTree(null);
						}
						else {
							//Else we load the tree of the knowledgebase associate to the model
							figTree.loadSpecificGTree(findModelFromName(currentFile).getKnowledgeBase().getKnowledgeBaseTree());
						
							//We display an information window if the knowledge base associated to the model change
							if (findModelFromName(currentFile).isKBModify()){
								JOptionPane.showMessageDialog(VisualFigaro.this, "The knowledge base associated to the model has been modified !");
								findModelFromName(currentFile).setKBModify(false);
							}
						}
							
					} else {
					
						//Else we load the tree
						figTree.loadSpecificGTree(findKnowledgeBaseFromName(currentFile).getKnowledgeBaseTree());
					}
				}
			}
		});
	}
	
	/**
	 * Initialize the translate button in the right side of the plugin used to update the tree architecture.
	 */
	private void initializeTranslateButton() {
		
		//We create the button
		translateButton = new JButton("<=");
		
		//And we just add an action listener
		translateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Save the current buffer
				view.getBuffer().save(view, currentFile);

				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					System.err.println("Sleep()");
				}
												
				GKnowledgeBase knowledgeBase = findKnowledgeBaseFromName(currentFile);
				
				//If the file has not been opened through the usual KB management menu we show an error and ask the user to restart. Otherwise we update the tree representation.
				if(knowledgeBase != null) {
					comboTree.setSelectedItem(currentFile);
				
					String path = System.getenv("TMP") + "\\test1_fi.xml";
					
					if( precompileXML() ) {
						//System.err.println("Test : " + view.getEditPane().getTextArea().getText());
						figTree.loadTreeFromXML(path, view.getEditPane().getTextArea().getText(), knowledgeBase.getKnowledgeBaseLanguage());
						//figTree.loadTreeFromXML(path, getTextFromFile(new File(currentFile)), knowledgeBase.getKnowledgeBaseLanguage());
						knowledgeBase.setKnowledgeBaseTree(figTree.getGTree().clone());
					}
					
					//Then we update the icons of the tree
					setIconsUpToDate();
				}
			}
		});
	}
	
	/**
	 * This method is used to display and retrieve the information used to create a knowledge base from the user. The way used is to display a <code>GWindowWizard</code>. 
	 */
	private void createKB() {
		//In the case of a new KB we will launch the wizard
		GWindowWizard window = new GWindowWizard(this);
		window.setVisible(true);
		window.setAlwaysOnTop(true);
	}
	
	/**
	 * This method is used to display and retrieve the information used to open a knowledge base from the user. The way used is to display a <code>JFileChooser</code>. 
	 */
	private void openKB() {
		
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
		
		JFileChooser chooser = new JFileChooser(getPrevKBFile());
	    chooser.addChoosableFileFilter(new MyFilter());
	    chooser.setAcceptAllFileFilterUsed(false);
		int returnValue = chooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();
				System.out.println(selectedFile.getPath());
				File f = null;
				File directory = null;
			try {
				f = new File(selectedFile.getCanonicalPath());
				directory = new File(selectedFile.getParent());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			chooser.setSelectedFile(f);
			openKB(directory, f);
		} 
	}
	
	public void openKB(File directory) {
		
		System.err.println("Open KB : " + directory);
		
		//We have to check whether all files are consistent
		boolean integrityCheckResult = checkIntegrity(directory.getAbsolutePath());
		
		//We have to retrieve the name of the Figaro file.
		String[] filesName = directory.list(new FilenameFilter() {
			public boolean accept(File file, String filename) {
				if(filename.toLowerCase().endsWith(".fi"))
					return true;
				else
					return false;
			}
		});
		
		//If the integrity check is passed successfully then we open the kb else we show an error
		if(integrityCheckResult) {
			
			//We are sure than one and only one Figaro file exists because we have passed successfully the integrity check. 
			
			//The filename will be the concatenation of the directory path and the Figaro filename
			String figaroFileName = directory.getAbsolutePath() + "\\" + filesName[0];
			
			//We have to check that the database is not already opened
			if(findKnowledgeBaseFromName(figaroFileName) != null) {
				JOptionPane.showMessageDialog(this, "The Knowledge Base is already opened.");
				return;
			}
		
			 //We just have to open the file in jEdit. The isOpening value are shared by all the program and indicate that messages concerning the opening of the file returned by jEdit should not be considerated.
			System.err.println("Open Before : File : " + view.getBuffer().getDirectory() + view.getBuffer().getName());
			
			isOpening = true;
			jEdit.openFile(view, figaroFileName);
			isOpening = false;
			currentFile = figaroFileName;
			
			//System.err.println("Open After : File : " + view.getBuffer().getDirectory() + view.getBuffer().getName());
			//System.err.println("Voici la taille : " + view.getEditPane().getTextArea().getText());
			
			//We translate the file in the XML format using precompileXML and we update all the variables
			if( precompileXML() ) {
				
				System.err.println("Precompile OK");
				
				languageName = findKnowledgeBaseLanguage(directory.getAbsolutePath()); 
				GLanguage language = new GLanguage();
				language.setLanguage(languageName);
				//setLanguageInMenuBar(languageName);
				
				figTree.newGTree();
				figTree.loadTreeFromXML(System.getenv("TMP") + "\\test1_fi.xml", getTextFromFile(new File(currentFile)), language);
				
				GKnowledgeBase knowledgeBase = new GKnowledgeBase(figaroFileName, language, figTree.getGTree().clone());
				knowledgedBasesVector.add(knowledgeBase);
				
				comboTree.addItem(figaroFileName);
				comboTree.setSelectedItem(figaroFileName);
				
				setIconsUpToDate();
				
				linkToModel(knowledgeBase);
			} else {
				System.err.println("Precompile Failed");
			}
			
		}
	}
	
	public void openKB(File directory, File file) {
		
		
		//We have to check if all the files are consistent
		boolean integrityCheckResult = checkIntegrity(directory.getAbsolutePath());
		
		//If the integrity check is passed successfully then we open the kb else we show an error
		if(integrityCheckResult) {
			
			//The filename will be the concatenation of the directory path and the Figaro filename
			String figaroFileName = file.getAbsolutePath();
			
			//We have to check that the database is not already opened
			if(findKnowledgeBaseFromName(figaroFileName) != null) {
				JOptionPane.showMessageDialog(this, "The Knowledge Base is already opened.");
				return;
			}
		
			 //We just have to open the file in jEdit. The isOpening value are shared by all the program and indicate that messages concerning the opening of the file returned by jEdit should not be considerated.
			System.err.println("Open Before : File : " + view.getBuffer().getDirectory() + view.getBuffer().getName());
			
			isOpening = true;
			jEdit.openFile(view, figaroFileName);
			isOpening = false;
			currentFile = figaroFileName;
			
			//System.err.println("Open After : File : " + view.getBuffer().getDirectory() + view.getBuffer().getName());
			
			//System.err.println("Voici la taille : " + view.getEditPane().getTextArea().getText());
			
			//We translate the file in the XML format using precompileXML and we update all the variables
			if( precompileXML() ) {
				
				System.err.println("Precompile OK");
				
				languageName = findKnowledgeBaseLanguage(directory.getAbsolutePath()); 
				GLanguage language = new GLanguage();
				language.setLanguage(languageName);
				//setLanguageInMenuBar(languageName);
				
				figTree.newGTree();
				//figTree.loadTreeFromXML("./VisualFigaro/" + "test1_fi.xml", getTextFromFile(new File(currentFile)), language);
				figTree.loadTreeFromXML(System.getenv("TMP") + "\\test1_fi.xml", getTextFromFile(new File(currentFile)), language);
				
				GKnowledgeBase knowledgeBase = new GKnowledgeBase(figaroFileName, language, figTree.getGTree().clone());
				knowledgedBasesVector.add(knowledgeBase);
				
				comboTree.addItem(figaroFileName);
				comboTree.setSelectedItem(figaroFileName);
				
				setIconsUpToDate();
				
				linkToModel(knowledgeBase);
			} else {
				System.err.println("Precompile Failed");
			}
			
		}
	}
	
	//If a previously closed knowledge base is open, we overwrite the old instance of the knowledgebase in the open models when it is necessary
	private void linkToModel(GKnowledgeBase knowledgeBase) {
		for (GModel m : modelsVector)
			if (m.getKnowledgeBase().getKnowledgeBaseName().equals(knowledgeBase.getKnowledgeBaseName())){
					m.setModelKnowledgeBase(knowledgeBase);
					m.setKBModify(true);
			}
	}
	
	
	private void createModel() {
		
		//Create a new Model is forbidden if no KB is already opened
		if(knowledgedBasesVector.isEmpty()){
			JOptionPane.showMessageDialog(this, "Open a Knowledge Base first");
			return;
		}
		
		//In the case of a new Model we will launch the wizard
		GWindowModel window = new GWindowModel(this, this.knowledgedBasesVector);
		window.setVisible(true);
		window.setAlwaysOnTop(true);
	}
	
	private void openModel() {
		
		//Open a Model is forbidden if no KB is already opened
		if(knowledgedBasesVector.isEmpty()){
			JOptionPane.showMessageDialog(this, "Open a Knowledge Base first");
			return;
		}
		
		//In the case of Model opening we will launch the wizard
		GWindowOpenModel window = new GWindowOpenModel(this, this.knowledgedBasesVector);
		window.setVisible(true);
		window.setAlwaysOnTop(true);
		
	}
	
	public void openModel(File file, GKnowledgeBase kb){
		//We check that the model to load exist
		boolean modelExist = checkMdlExist(file);
		
		if(modelExist){
			
			//We retrieve the file name
			String figaroFileName = file.getAbsolutePath();;
		
			//We have to check that the database is not already opened
			if(findModelFromName(figaroFileName) != null) {
				JOptionPane.showMessageDialog(this, "The Model is already opened.");
				return;
			}
	
			//We just have to open the file in jEdit. The isOpening value are shared by all the program and indicate that messages concerning the opening of the file returned by jEdit should not be considerated.
			System.err.println("Open Before : File : " + view.getBuffer().getDirectory() + view.getBuffer().getName());
			
			isOpening = true;
			jEdit.openFile(view, figaroFileName);
			isOpening = false;
			currentFile = figaroFileName;
			
			GModel model = new GModel(figaroFileName, kb);
			modelsVector.add(model);
					
			comboTree.addItem(figaroFileName);			
			comboTree.setSelectedItem(figaroFileName);
						
			figTree.loadSpecificGTree(kb.getKnowledgeBaseTree());
		}
	}
	
	private boolean checkMdlExist(File file) {
		
		boolean mdlexist = true;
		String errorMessage = "";
		
		//If this file does not exist or is not a directory we have to return zero
		if(!file.exists() && !file.getName().toLowerCase().endsWith(".fi")) {
			errorMessage += "The Model does not exist.\n";
			mdlexist= false;
		}
		if(!mdlexist) {
			JOptionPane.showMessageDialog(this, errorMessage, "Model loading error", JOptionPane.ERROR_MESSAGE);
		}
		return mdlexist;
	}
	
	private void closeModel() {
		//First we retrieve the name of the model file being edited currently
		currentFile = view.getBuffer().getPath();
		
		//We check either the current file is a knowledgebase or a model
		
		//We try to find the knowledge base
		GKnowledgeBase knowledgeBase = findKnowledgeBaseFromName(currentFile);
		
		//We try to find the model
		GModel model = findModelFromName(currentFile);
		
		//We check if the file exists
		if(model != null) {
			
			// Update VisualFigaro.ini file
			try {
				saveToIniFile(currentFile,1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//Pay attention to the order because of the indexes extracted from the vectors
			Buffer  save= view.getBuffer(); 
			comboTree.removeItem(currentFile);
			jEdit.closeBuffer(view, save);
			modelsVector.remove(model);

		} 
		else {
		if (knowledgeBase !=null){
			
			//We display that the current file is a knowledge base and we ask if the user wants to close it
			//Create the JOptionPane
			Object[] options = {"Close", "Cancel"};
			int choix = JOptionPane.showOptionDialog(VisualFigaro.this, "The current open file is a Knowledge Base, do you want to close it ?", "Current File not a Knowledge Base", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
			
			//If the choice is to close the model, we call closeModel()
			if(choix == 0)
				closeKB();
			
		} else {
			comboTree.setSelectedIndex(-1);
			jEdit.closeAllBuffers(view);
		}}
		//switch to other buffer if exists
		currentFile = view.getBuffer().getPath();
	}
	
	private void createObject() {
		
		if (findModelFromName(currentFile)==null){
			JOptionPane.showMessageDialog(VisualFigaro.this, "Select a model before !");
			return;
		}
		
		GModel model = findModelFromName(currentFile);
		GKnowledgeBase knowledgeBase = model.getKnowledgeBase();
		String knowledgeBaseFileName = knowledgeBase.getKnowledgeBaseName();
		
		//We create and show the GWindowMain with its xml loader and figaro loader which will be shared by all other windows because they are static
		DOMBuilder builder = new DOMBuilder();
		
		GObjectInformation info = new GObjectInformation();
		info.setLanguage(knowledgeBase.getKnowledgeBaseLanguage());
		info.setKnowledgeBasePath(knowledgeBaseFileName.substring(0, knowledgeBaseFileName.lastIndexOf("\\")));
		
		GXMLLoader xmlLoader = new GXMLLoader(info.getLanguage());
		xmlLoader.loadXmlFile(knowledgeBaseFileName.substring(0, knowledgeBaseFileName.length() - 2).concat("bdc"));
		GWindow.setXmlLoader(xmlLoader);
		
		GXMLLoaderFigaro figaroLoader = new GXMLLoaderFigaro();
		figaroLoader.setXmlFile(builder.build(figTree.getGTree().getDocument()));
		GWindow.setFigaroLoader(figaroLoader);
		
		//If the current file is a model, we launch the figaro 0 generation
		GWindowNewObject window = new GWindowNewObject(this, null, info, model);
		window.setVisible(true);
		window.setAlwaysOnTop(true);
		
	}
	
	// Will be deploy in a future release
	/*private void editObject() {
		//Waiting for development
		JOptionPane.showMessageDialog(VisualFigaro.this, "This feature is not available for the moment");
	}*/
	
	private void executeCommand(String command, String title, Boolean block, Boolean output){
		
		//For debug purpose only
		//JOptionPane.showMessageDialog(VisualFigaro.this, "Cmd to execute : "+command);
		
		//Declaration of the two buffers filled respectively by the output stream and the error stream
		String outputBuffer = "", errorBuffer = "";
				
		//Boolean indicating if there is something to display. 
		boolean bufferNotEmpty= false;
				
		//We create a runtime environment
		Runtime r = Runtime.getRuntime();
				
		//We run the program and take care if some errors happen
		try {
			Process process = r.exec(command);

			if (block){
				//Now we retrieve both the possible error and the output
				try {

					InputStream in  = process.getInputStream();
					InputStream err = process.getErrorStream();
				         
					// Set to true when the process is finished
					boolean processFinished = false;

					//While the process is not finished we read error and output streams and fill the respective buffers
					while(!processFinished) {
						
						try {
							//While there is output available retrieve it and put it in the outputBuffer
							while( in.available() > 0) {
								// Print the output of our system call
								Character c = new Character( (char) in.read());
								outputBuffer += c;
							}
				            
							System.err.print("Voici le outputBuffer : \"" + outputBuffer + "\"");

							//While there is outputerror available retrieve it and put in in the errorBuffer
							while( err.available() > 0) {
								// Print the output of our system call
								Character c = new Character( (char) err.read());
								errorBuffer += c;
							}
				           
							System.err.print("Voici le errorBuffer : \"" + errorBuffer + "\"");
						
							if(outputBuffer.length() > 0 || errorBuffer.length() > 0) {
								bufferNotEmpty = true;
							} 
						
							// Ask the process for its exitValue. If the process
							// is not finished, an IllegalThreadStateException
							// is thrown. If it is finished, we fall through and
							// the variable finished is set to true.
							process.exitValue();
							processFinished  = true;

						} catch (IllegalThreadStateException e) {
							// Process is not finished yet;
							// Sleep a little to save CPU cycles
							Thread.sleep(500);
							}
					}
				
				} catch (Exception e) {
					System.err.println("Visual Figaro : VisualFigaro : Input-Output stream error : " + e);
				}
			}

		} catch(Exception e) {
			System.err.println("Visual Figaro : VisualFigaro : Server execution into file error : " + e);	
		}

		//If an error has occurred we have to show it to the user in order to detect the problem. We use a basic JFrame
		if(bufferNotEmpty&&output) {
			
			JFrame resultFrame;
			resultFrame = new JFrame();
			resultFrame.setTitle(title);
			final JTextArea resultFrameTextArea = new JTextArea();
			resultFrameTextArea.setLineWrap(true);
			resultFrameTextArea.addMouseListener(new MouseListener() {
				public void mousePressed(MouseEvent e) {
				}
				public void mouseReleased(MouseEvent e) {
				}
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						//We retrieve the first digit on the line
						int start = resultFrameTextArea.getText().lastIndexOf("\n", resultFrameTextArea.getCaretPosition());
						int end = resultFrameTextArea.getText().indexOf("\n", resultFrameTextArea.getCaretPosition());
						
						start = resultFrameTextArea.getText().indexOf("(", start);
			        			
						if(start < 0)
							return;
						
						end = resultFrameTextArea.getText().indexOf(")", start);
						if(end < start)
							return;

						String line = resultFrameTextArea.getText().substring(start+1, end);

						System.out.println("Voici la ligne : " + line);

						Integer entier = new Integer(line);

						//We go to the line "pos"
						int pos=0;
						for(int i=0; i<entier; i++, pos++)
							pos = view.getEditPane().getTextArea().getText().indexOf("\n", pos);
						
						if(pos>0)
							view.getEditPane().getTextArea().setCaretPosition(pos);
					}
				}
				public void mouseExited(MouseEvent e) {
				}
				public void mouseEntered(MouseEvent e) {
				}
			});
			
			JScrollPane sp = new JScrollPane(resultFrameTextArea);
			resultFrame.add(sp);
			
			resultFrameTextArea.setText(outputBuffer + errorBuffer);

			resultFrame.setSize(1200, 400);
			resultFrame.setLocation(300, 200);
			resultFrame.setVisible(true);
			resultFrame.setAlwaysOnTop(true);
		}
	}
	
	private void checkModel() {
		
		if (modelsVector.isEmpty()) {
				JOptionPane.showMessageDialog(VisualFigaro.this, "Open a Model first !");
			} 
		else {
			if (findModelFromName(currentFile)!=null){
				String figpPath = "./VisualFigaro/figp.exe";
				GModel model = findModelFromName(currentFile);
				GKnowledgeBase knowledgeBase = model.getKnowledgeBase();
				
				String modelName = model.getModelName();
				String knowledgeBaseName = knowledgeBase.getKnowledgeBaseName();
				
				String cmd = figpPath + " \"" + knowledgeBaseName + "\" -bdf \"" + modelName + "\"";                
				
				executeCommand(cmd, "Check Model", true, true);
			}
			else {
				JOptionPane.showMessageDialog(VisualFigaro.this, "The current File is not a model");
			}
		}
	}
	
	private void generateFig0(int postTreatment) {
		
		if (modelsVector.isEmpty()) {
				JOptionPane.showMessageDialog(VisualFigaro.this, "Open a Model first !");
			} else {
			GModel model = findModelFromName(currentFile);
			if (model != null){
				GKnowledgeBase knowledgeBase = model.getKnowledgeBase();
				String knowledgeBaseFileName = knowledgeBase.getKnowledgeBaseName();
				
				//We create and show the GWindowMain with its xml loader and figaro loader which will be shared by all other windows because they are static
				DOMBuilder builder = new DOMBuilder();
				
				GObjectInformation info = new GObjectInformation();
				info.setLanguage(knowledgeBase.getKnowledgeBaseLanguage());
				info.setKnowledgeBasePath(knowledgeBaseFileName.substring(0, knowledgeBaseFileName.lastIndexOf("\\")));
				
				GXMLLoader xmlLoader = new GXMLLoader(info.getLanguage());
				xmlLoader.loadXmlFile(knowledgeBaseFileName.substring(0, knowledgeBaseFileName.length() - 2).concat("bdc"));
				GWindow.setXmlLoader(xmlLoader);
				
				GXMLLoaderFigaro figaroLoader = new GXMLLoaderFigaro();
				figaroLoader.setXmlFile(builder.build(figTree.getGTree().getDocument()));
				GWindow.setFigaroLoader(figaroLoader);
				
				//If the current file is a model, we launch the figaro 0 generation
				GWindowGenerateFig0 window = new GWindowGenerateFig0(this, null, info, model, postTreatment);
				window.setVisible(true);
				window.setAlwaysOnTop(true);
			}
			else {
				JOptionPane.showMessageDialog(VisualFigaro.this, "The current File is not a model");
			}
		}
	}
	
	public void cmdFig0(String fig0FileName, int postTreatment) {
		File commands = new File("./VisualFigaro/figp_commands.xml");
		File fig0File = new File(fig0FileName);
		
		if (commands.exists()){
			
			// A temporary file is always overwritten
			if (fig0FileName.equals(System.getenv("TMP") + "\\fig0_temp.fi"))
				fig0File.delete();
			
			// If the output file already exist, we ask if the file must be overwritten
			if (fig0File.exists()){
				Object[] options = {"Cancel", "Overwrite"};
				int choix = JOptionPane.showOptionDialog(VisualFigaro.this, "The output file " + fig0FileName + " already exist, overwrite ?", "Figaro 0 file already exist", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
				
				//If the choice is to overwrite, we delete the fig0File, else we exit
				if(choix == 0)
					return;
				else
					fig0File.delete();
			}
			
			String appli = "./VisualFigaro/figp.exe";
			String commandsFile = "./VisualFigaro/figp_commands.xml";
			//String kBName = findModelFromName(currentFile).getKnowledgeBase().getKnowledgeBaseName();
			
			String cmd = appli + " -testxml " + commandsFile;
			
			executeCommand(cmd, "Generate Figaro 0", true, false);
			
			// If the figaro 0 file is successfully generated, we open the new file
			if (fig0File.exists()){
				
				//We store that the new figaro 0 file corresponds to the up to date model
				findModelFromName(currentFile).setModelModify(false);
				
				isOpening = true;
				jEdit.openFile(view, fig0FileName);
				isOpening = false;
				currentFile = fig0FileName;
				
				comboTree.setSelectedIndex(-1);
				
				switch(postTreatment){
				case 1:
					runFig0debug();
					break;
				case 2:
					runFigseq();
					break;
				case 3:
					runYams();
					break;
				default: break;
				}
			}
			else
				JOptionPane.showMessageDialog(VisualFigaro.this, "No file has been written");
		}
		else
			JOptionPane.showMessageDialog(VisualFigaro.this, "The commands file does not exist ! Abort.");
	}
	
	private void runFig0debug() {
		
		if (currentFile==null){
			JOptionPane.showMessageDialog(VisualFigaro.this, "Open a valid file first !");
			return;
		}
		
		// If the current file seems to be a figaro 0 file and is not a model and not a knowledge base
		if (findModelFromName(currentFile)==null && findKnowledgeBaseFromName(currentFile)==null && currentFile.endsWith(".fi")){
			String figpPath = "./VisualFigaro/Fig0Debug.exe";
			//GModel model = findModelFromName(currentFile);
		
			//String modelName = model.getModelName();
		
			String cmd = figpPath + " \"" + currentFile + "\"";                
			
			executeCommand(cmd, "Run Fig0debug", false, false);
		}
		else {
			// If the current file is a model, we can generate a new figaro 0 file
			if (findModelFromName(currentFile)!=null){
				
				GModel model = findModelFromName(currentFile);
				
				if (!model.isModelModify()&&!model.getFigaro0Settings().getFileName().equals("")&&!model.getFigaro0Settings().getFileName().equals("fig0_temp.fi")){
					String filename = model.getFigaro0Settings().getFileName();

					File figaro0 = new File(filename);
						
					if (figaro0.exists()){
						Object[] options = {"Ok", "Cancel"};
						int choix = JOptionPane.showOptionDialog(VisualFigaro.this, "A figaro 0 file exists for the current model, load " + filename + " ?", "Use previous figaro 0 file ?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
						
						//If the choice is to generate a new one, we call generateFig0, else we use the previous figaro 0 file
						if(choix == 1)
							generateFig0(1);
						else{
							String figpPath = "./VisualFigaro/Fig0Debug.exe";
							String cmd = figpPath + " \"" + filename + "\"";                
							
							executeCommand(cmd, "Run Fig0debug", false, false);
						}
					}
				}
				else {
				Object[] options = {"Ok", "Cancel"};
				int choix = JOptionPane.showOptionDialog(VisualFigaro.this, "Generate a new figaro 0 file for the current model ?", "A model is selected", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
			
				//If the choice is to overwrite, we delete the fig0File, else we exit
				if(choix == 1)
					return;
				
				// We generate the Figaro 0 file and call runFig0debug at the end
				generateFig0(1);
				}
			}
			else
				JOptionPane.showMessageDialog(VisualFigaro.this, "The current file is neither a figaro 0 file nor a model, please select a valid input");
		}
	}
	
	private void runFigseq() {
		
		if (currentFile==null){
			JOptionPane.showMessageDialog(VisualFigaro.this, "Open a valid file first !");
			return;
		}
		
		//We retrieve the Figseq program path
		String figPath = getPathConfiguration("FIGSEQ");
		
		if(figPath.equals(""))
			return;
		
		// If the current file seems to be a figaro 0 file and is not a model and not a knowledge base
		if (findModelFromName(currentFile)==null && findKnowledgeBaseFromName(currentFile)==null && currentFile.endsWith(".fi")){

			String cmd = "\"" + figPath + "\" \"" + currentFile + "\"";                  
					
			executeCommand(cmd, "Run Figseq", false, false);
		}
		else {
			
			GModel model = findModelFromName(currentFile);
			
			if (!model.isModelModify()&&!model.getFigaro0Settings().getFileName().equals("")&&!model.getFigaro0Settings().getFileName().equals("fig0_temp.fi")){
				String filename = model.getFigaro0Settings().getFileName();

				File figaro0 = new File(filename);
					
				if (figaro0.exists()){
					Object[] options = {"Ok", "Cancel"};
					int choix = JOptionPane.showOptionDialog(VisualFigaro.this, "A figaro 0 file exists for the current model, load " + filename + " ?", "Use previous figaro 0 file ?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
					
					//If the choice is to generate a new one, we call generateFig0, else we use the previous figaro 0 file
					if(choix == 1)
						generateFig0(2);
					else{
						String cmd = figPath + " \"" + filename + "\"";                
						
						executeCommand(cmd, "Run Figseq", false, false);
					}
				}
			}
			else {
				// If the current file is a model, we can generate a new figaro 0 file
				if (findModelFromName(currentFile)!=null){
				
					Object[] options = {"Ok", "Cancel"};
					int choix = JOptionPane.showOptionDialog(VisualFigaro.this, "Generate a new figaro 0 file for the current model ?", "A model is selected", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
					
					//If the choice is to overwrite, we delete the fig0File, else we exit
					if(choix == 1)
						return;
						
					// We generate the Figaro 0 file and call runFigseq at the end
					generateFig0(2);
				}
				else
					JOptionPane.showMessageDialog(VisualFigaro.this, "The current file is neither a figaro 0 file nor a model, please select a valid input");
			}
		}
	}
	
	private void runYams() {
		
		if (currentFile==null){
			JOptionPane.showMessageDialog(VisualFigaro.this, "Open a valid file first !");
			return;
		}
		
		//We retrieve the Yams program path
		String figPath = getPathConfiguration("YAMS");
		
		if(figPath.equals(""))
			return;
		
		// If the current file seems to be a figaro 0 file and is not a model and not a knowledge base
		if (findModelFromName(currentFile)==null && findKnowledgeBaseFromName(currentFile)==null && currentFile.endsWith(".fi")){
			
			String cmd = "\"" + figPath + "\" \"" + currentFile + "\"";                
					
			executeCommand(cmd, "Run Yams", false, false);
		}
		else {
			// If the current file is a model, we can generate a new figaro 0 file
			if (findModelFromName(currentFile)!=null){
				
				GModel model = findModelFromName(currentFile);
				
				if (!model.isModelModify()&&!model.getFigaro0Settings().getFileName().equals("")&&!model.getFigaro0Settings().getFileName().equals("fig0_temp.fi")){
					String filename = model.getFigaro0Settings().getFileName();

					File figaro0 = new File(filename);
						
					if (figaro0.exists()){
						Object[] options = {"Ok", "Cancel"};
						int choix = JOptionPane.showOptionDialog(VisualFigaro.this, "A figaro 0 file exists for the current model, load " + filename + " ?", "Use previous figaro 0 file ?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
						
						//If the choice is to generate a new one, we call generateFig0, else we use the previous figaro 0 file
						if(choix == 1)
							generateFig0(3);
						else{
							String cmd = figPath + " \"" + filename + "\"";                
							
							executeCommand(cmd, "Run Figseq", false, false);
						}
					}
				}
				else {
				
					Object[] options = {"Ok", "Cancel"};
					int choix = JOptionPane.showOptionDialog(VisualFigaro.this, "Generate a new figaro 0 file for the current model ?", "A model is selected", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
					
					//If the choice is to overwrite, we delete the fig0File, else we exit
					if(choix == 1)
						return;
						
					// We generate the Figaro 0 file and call runYams at the end
					generateFig0(3);
				}
			}
			else
				JOptionPane.showMessageDialog(VisualFigaro.this, "The current file is neither a figaro 0 file nor a model, please select a valid input");
		}
	}
	
	//Return the path of the selected application
	private String getPathConfiguration(String Appl){
		String path = "";
		String path1 = "";
		File fileVisual;
		
		//Try to find the application in the VisualFigaro folder
		if (Appl.equals("FIGSEQ")){
			path = "./VisualFigaro/FIGSEQ/figseq-gui_p.exe";
			fileVisual = new File(path);
			if (fileVisual.exists())
				return path;
			else {
				path = "./VisualFigaro/FIGSEQ/figseq-gui.exe";
				fileVisual = new File(path);
				if (fileVisual.exists())
					return path;
			}
		}
		else if (Appl.equals("YAMS")){
			path = "./VisualFigaro/YAMS/yams-gui.exe";
			fileVisual = new File(path);
			if (fileVisual.exists())
				return path;
			else {
				path = "./VisualFigaro/YAMS/MC_IHM.exe";
				fileVisual = new File(path);
				if (fileVisual.exists())
					return path;
			}
		}
		
		//Else, find the path in the KB3Configuration.xml files
		String userpath = System.getenv("APPDATA") + "\\EDF MRI TOOLS\\KB3Configuration.xml";
		String alluserpath = System.getenv("ALLUSERSPROFILE") + "\\EDF MRI TOOLS\\KB3Configuration.xml";
		
		File userp = new File(userpath);
		File alluserp = new File(alluserpath);
		
		Boolean foundPath = false;
		
		//Search of the Application path in the KB3Configuration.xml
		//First we try the KB3Configuration.xml user file
		if (userp.exists()){
			path1 = getPathFromFile(userpath, Appl);
				
			if (!path1.equals("")){
				File figPathFile = new File(path1);
				if (figPathFile.exists()){
					path = path1;
					foundPath = true;
				}
			}
		}
		
		//If application path was not found, we try the KB3Configuration.xml in the all users directory
		if (!foundPath && alluserp.exists()){
			path = getPathFromFile(alluserpath, Appl);

			if (path.equals("")){
				if (path1.equals(""))
					JOptionPane.showMessageDialog(VisualFigaro.this, Appl + " not found");
				else
					JOptionPane.showMessageDialog(VisualFigaro.this, "Invalid path, "+ Appl +" was not found at "+path1);
				return "";
			}
			File figPathFile = new File(path);
			if (!figPathFile.exists()){
				JOptionPane.showMessageDialog(VisualFigaro.this, "Invalid path, "+ Appl +" was not found at "+path);
				return "";
			}
		}
		else {
			if(!foundPath)
				JOptionPane.showMessageDialog(VisualFigaro.this, "KB3Configuration.xml file missing");
		}
		
		return path;
	}
	
	private String getPathFromFile(String filename, String appl){
		String path = "";
		Document document;
		Element root;
		List<Element> code;
		
		//On crée une instance de SAXBuilder
		//SAXBuilder instance creation
		SAXBuilder sxb = new SAXBuilder();
		try
		{
			//Creation of a new JDOM document with the XML file as argument
			document = sxb.build(new File(filename));
			
			//Search of the path
			root = document.getRootElement();
			
			code = root.getChildren("code");
			for(Element e: code){
				if(e.getChildText("nom").equals(appl)){
					path = e.getChildText("chemin_IHM");
				}
			}
		}
		catch(Exception e){
			System.out.println("Erreur lors du chargement du fichier suivant : " + path);
			JOptionPane.showMessageDialog(VisualFigaro.this, "An error occurs during the load of the file : " + path + " " + e.toString());
		}

		return path;
	}
	
	private void generateFaultTree() {
		if (modelsVector.isEmpty()) {
			JOptionPane.showMessageDialog(VisualFigaro.this, "Open a Model first !");
		} 
		else {
			GModel model = findModelFromName(currentFile);
			if (model != null){
				GKnowledgeBase knowledgeBase = model.getKnowledgeBase();
				String knowledgeBaseFileName = knowledgeBase.getKnowledgeBaseName();
			
				//We create and show the GWindowMain with its xml loader and figaro loader which will be shared by all other windows because they are static
				DOMBuilder builder = new DOMBuilder();
			
				GObjectInformation info = new GObjectInformation();
				info.setLanguage(knowledgeBase.getKnowledgeBaseLanguage());
				info.setKnowledgeBasePath(knowledgeBaseFileName.substring(0, knowledgeBaseFileName.lastIndexOf("\\")));
			
				GXMLLoader xmlLoader = new GXMLLoader(info.getLanguage());
				xmlLoader.loadXmlFile(knowledgeBaseFileName.substring(0, knowledgeBaseFileName.length() - 2).concat("bdc"));
				GWindow.setXmlLoader(xmlLoader);
			
				GXMLLoaderFigaro figaroLoader = new GXMLLoaderFigaro();
				figaroLoader.setXmlFile(builder.build(figTree.getGTree().getDocument()));
				GWindow.setFigaroLoader(figaroLoader);
			
				//If the current file is a model, we launch the figaro 0 generation
				GWindowFaultTree window = new GWindowFaultTree(this, null, info, model);
				window.setVisible(true);
				window.setAlwaysOnTop(true);
			}
			else {
				JOptionPane.showMessageDialog(VisualFigaro.this, "The current File is not a model");
			}
		}
	}
	
	public void generateFT(String ftFileName) {
		File commands = new File("./VisualFigaro/figp_commands.xml");
		File ftFile = new File(ftFileName);
		
		if (commands.exists()){
			
			// A temporary file is always overwritten
			if (ftFileName.equals(System.getenv("TMP") + "\\faulttree_temp.xml"))
				ftFile.delete();
			
			// If the output file already exist, we ask if the file must be overwritten
			if (ftFile.exists()){
				Object[] options = {"Cancel", "Overwrite"};
				int choix = JOptionPane.showOptionDialog(VisualFigaro.this, "The output file " + ftFileName + " already exist, overwrite ?", "Figaro 0 file already exist", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
				
				//If the choice is to overwrite, we delete the fig0File, else we exit
				if(choix == 0)
					return;
				else
					ftFile.delete();
			}
			
			String appli = "./VisualFigaro/figp.exe";
			String commandsFile = "./VisualFigaro/figp_commands.xml";
			
			String cmd = appli + " -testxml " + commandsFile;
			
			executeCommand(cmd, "Generate FT", true, false);
			
			// If the figaro 0 file is successfully generated, we open the new file
			if (ftFile.exists())
				JOptionPane.showMessageDialog(VisualFigaro.this, "File : "+ftFileName+" has been written");
			else
				JOptionPane.showMessageDialog(VisualFigaro.this, "No file has been written");
		}
		else
			JOptionPane.showMessageDialog(VisualFigaro.this, "The commands file does not exist ! Abort.");
	}	
	
	private void saveToIniFile(String filePath, int type) throws IOException {
		
		String AppliDataPath = System.getenv("AppData"); 
		String VFIniFilePath= AppliDataPath + "\\EDF MRI TOOLS\\VisualFigaro.ini";
		
		File inputFile = new File(VFIniFilePath);
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		
		File outputFile = new File(AppliDataPath + "\\EDF MRI TOOLS\\temp.ini");
		Charset charset = Charset.forName("UTF-8");
		Writer bw = new OutputStreamWriter(new FileOutputStream(outputFile), charset);
		
		String kbPath = "";
		String modelPath = "";
		
		switch(type){
		//in this case, we update the knowledge base path
		case 0:
			kbPath = filePath;
			modelPath = getPrevModelFile();
			break;
		//in this case, we update the model path
		case 1:
			kbPath = getPrevKBFile();
			modelPath = filePath;
			break;
		//do nothing
		default:
			break;
		}
		
	    bw.write("<PREV_PATH_KB>" + kbPath + "</PREV_PATH_KB>"+ "\n");
	    bw.write("<PREV_PATH_MD>" + modelPath + "</PREV_PATH_MD>" + "\n");
	    
		br.close();
		bw.flush();
		bw.close();

		inputFile.delete();
		outputFile.renameTo(new File(VFIniFilePath));
	}
	
	private String getPrevKBFile() {
		
		String AppliDataPath = System.getenv("AppData");
		String VFIniFilePath= AppliDataPath + "\\EDF MRI TOOLS\\VisualFigaro.ini";
		String ligne="";
		String result = "";
		
		File inputFile = new File(VFIniFilePath);
		if (!inputFile.getParentFile().exists()){
			if (!inputFile.getParentFile().mkdir()) {
				// Not possible to create the directory, probably due to user rights
				// Degraded mode, the previous KB feature is not available
				return result;
			}
		}
		
		if (!inputFile.exists()) {
			try {
				inputFile.createNewFile();
				saveToIniFile("",-1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			while ((ligne = br.readLine()) != null){
				  if(ligne.startsWith("<PREV_PATH_KB>")) {
					  int end = ligne.lastIndexOf("</PREV_PATH_KB>");
					  result =  ligne.substring(14,end);
			     }
			}
		    br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return(result);
	}
	
	public String getPrevModelFile() {
		
		String AppliDataPath = System.getenv("AppData");
		String VFIniFilePath= AppliDataPath + "\\EDF MRI TOOLS\\VisualFigaro.ini";
		String ligne="";
		String result = "";
		
		File inputFile = new File(VFIniFilePath);
		if (!inputFile.getParentFile().exists()){
			if (!inputFile.getParentFile().mkdir()) {
				// Not possible to create the directory, probably due to user rights
				// Degraded mode, the previous KB feature is not available
				return result;
			}
		}
		
		if (!inputFile.exists()) {
			try {
				inputFile.createNewFile();
				saveToIniFile("",-1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			while ((ligne = br.readLine()) != null){
				  if(ligne.startsWith("<PREV_PATH_MD>")) {
					  int end = ligne.lastIndexOf("</PREV_PATH_MD>");
					  result =  ligne.substring(14,end);
			     }
			}
		    br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return(result);
	}
	
	private void closeKB() {
		//First we retrieve the name of the bdc file being edited currently
		currentFile = view.getBuffer().getPath();
		
		//We check either the current file is a knowledgebase or a model
		
		//We try to find the knowledge base
		GKnowledgeBase knowledgeBase = findKnowledgeBaseFromName(currentFile);
		
		//We try to find the model
		GModel model = findModelFromName(currentFile);
		
		//We check if the file exists
		if(knowledgeBase != null) {

			//Update last open KB file
			JMenuItem item = menuBar.getMenu(0).getItem(6);
			item.setText(currentFile);
			
			// Update VisualFigaro.ini file
			try {
				saveToIniFile(currentFile,0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Pay attention to the order because of the indexes extracted from the vectors
			Buffer  save= view.getBuffer(); 
			comboTree.removeItem(currentFile);
			jEdit.closeBuffer(view, save);
			knowledgedBasesVector.remove(knowledgeBase);

		} 
		else {
		if (model !=null){
			
			//We display that the current file is a model and we ask if the user wants to close it
			//Create the JOptionPane
			Object[] options = {"Close", "Cancel"};
			int choix = JOptionPane.showOptionDialog(VisualFigaro.this, "The current open file is a model, do you want to close it ?", "Current File not a Knowledge Base", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
			
			//If the choice is to close the model, we call closeModel()
			if(choix == 0)
				closeModel();
			
		} else {
			comboTree.setSelectedIndex(-1);
			jEdit.closeAllBuffers(view);
		}}
		//switch to other buffer if exists
		currentFile = view.getBuffer().getPath();
	}
	
	private void translateKB() throws IOException {
		
		if (findKnowledgeBaseFromName(currentFile)!=null){
		
			String BdCPath = "./VisualFigaro/TradBdC/"; 
			//updateTradBdcIniFile(BdCPath);			
			
			// Test if the MotsClesBdCKB3V3.xml file is in the TradBdC directory
			File tradKB = new File(BdCPath+"MotsClesBdCKB3V3.xml");
			
			if (!tradKB.exists()){
				JOptionPane.showMessageDialog(VisualFigaro.this, "MotsClesBdCKB3V3.xml is missing");
				return;
			}
			
			// select UTF-8 format to generate correct "ç" in output file
			Charset charset = Charset.forName("UTF-8");

			String transType = "fe";
			String destDir = "English\\";
			String origDir = "Francais\\";
			String currentFilePath = "";
			String destFilePath = "";
			String TransFileName = "";
			String xsdFileName = "bdceng.xsd";
			
			if (languageName.equals("English")){
				transType = "ef";
				destDir = "Francais\\";
				origDir = "English\\";
				xsdFileName = "bdcfr.xsd";
			}
			
			// Construct destination translation directory and file
			int index = currentFile.lastIndexOf("\\");
			currentFilePath = currentFile.substring(0, index-1);
			String currentFileName = currentFile.substring(index+1);
			index = currentFileName.lastIndexOf(".");
			TransFileName = "Translation_" + currentFileName.substring(0,index) + ".xml";
			
			// apply second time to reach main project directory
			index = currentFilePath.lastIndexOf("\\");
			currentFilePath = currentFilePath.substring(0, index+1);

			destFilePath = currentFilePath + destDir;
			File destFile   = new File(destFilePath);
			File xmlFile    = new File(currentFilePath + TransFileName);
			
			// If the file containing translations does not exist, create it with the main tags
			// so that it can be loaded by the translator TradBdC.exe
			if (!xmlFile.exists()) {
				xmlFile.createNewFile();
				Writer xw = new OutputStreamWriter(new FileOutputStream(xmlFile), charset);
				xw.write("<TRADUCTIONS_BDC>" + "\n");
				xw.write(" <TRADUCTIONS_FIGARO></TRADUCTIONS_FIGARO>" + "\n");
				xw.write(" <TRADUCTIONS_VALEURS_XML></TRADUCTIONS_VALEURS_XML>" + "\n");
				xw.write("</TRADUCTIONS_BDC>" + "\n");
				xw.close();
			}
			
			// Create destination directory if it does not exist
			if (!destFile.exists()){
				destFile.mkdir();
			}
			
			// Copy the directory icons (if it already exists, existing icons are replaced, or left as
			// they are, depending on the existence or not of icons with the same name in the origin directory
			CopyFile.copy(new File(currentFilePath + origDir + "icons"),new File(destFilePath + "icons"), false);
			
			// Copy the schema from TradBdC directory to the target directory		
			CopyFile.copyFile(new File(BdCPath + xsdFileName),new File(destFilePath + xsdFileName), true);
			
			String prg = BdCPath + "TradBDC.exe";
			String kBFile = currentFile;
			String options = "-lang en -tf " + transType + " -tx " + transType + " -tb " + transType;
			String keyWordFile = "-ki "+ BdCPath + "MotsClesBdCKB3V3.xml";
			String keyWordBdC = "-bi \"" + currentFilePath + TransFileName + "\"";
			String destKBFile = "-o \"" + destFilePath;
			index = currentFile.lastIndexOf("\\");
			destKBFile += currentFile.substring(index+1,currentFile.length()) + "\"";
			
			// Launch the KB3 translator
			String cmd = prg + " \"" + kBFile + "\" " + options + " " + keyWordFile + " " + keyWordBdC + " " + destKBFile;
			
			executeCommand(cmd, "Run TradBDC", false, false);
		}
		else
			JOptionPane.showMessageDialog(VisualFigaro.this, "The selected file is not a knowledge base");
	}
	
	/*private void updateTradBdcIniFile(String tradBdCPath) throws IOException {
		
		// This method manages the needed files and directories before
		// KB3 translator tool launching
		
		//JOptionPane.showMessageDialog(VisualFigaro.this, "Identification du .ini");
		
		String TradBdCFileName = tradBdCPath + "TradBdC.ini";
		File inputFile = new File(TradBdCFileName);
		
		//JOptionPane.showMessageDialog(VisualFigaro.this, "Première étape réussie");
		
		if (currentFile == null) {
		    // The TradBdC.ini file shall not be modified	
			
		} else {
			//The TradBdC.ini file shall be updated with the current edited .fi file
			//The filename will be the concatenation of the directory path and the TradBdC .ini filename			
			//JOptionPane.showMessageDialog(VisualFigaro.this, "Deuxième étape réussie");
			
			File outputFile = new File(tradBdCPath + "temp.ini");
			
			// select UTF-8 format to generate correct "ç" in output file
			Charset charset = Charset.forName("UTF-8");
			Writer bw = new OutputStreamWriter(new FileOutputStream(outputFile), charset);
			
			//JOptionPane.showMessageDialog(VisualFigaro.this, "Troisième étape réussie : " + inputFile.getCanonicalPath());
			
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			JOptionPane.showMessageDialog(VisualFigaro.this, "Etape intermédiaire réussie");
			String ligne="";
			String transType = "\"Français -> Anglais\" ";
			String destDir = "English\\";
			String origDir = "Francais\\";
			String outLine;
			String currentFilePath = "";
			String destFilePath = "";
			String TransFileName = "";
			String xsdFileName = "bdceng.xsd";
			
			if (languageName.equals("English")){
				transType = "\"Anglais -> Français\" ";
				destDir = "Francais\\";
				origDir = "English\\";
				xsdFileName = "bdcfr.xsd";
			}
			
			//JOptionPane.showMessageDialog(VisualFigaro.this, "Quatrième étape réussie");
			
			// Construct destination translation directory and file
			int index = currentFile.lastIndexOf("\\");
			currentFilePath = currentFile.substring(0, index-1);
			String currentFileName = currentFile.substring(index+1);
			index = currentFileName.lastIndexOf(".");
			TransFileName = "Translation_" + currentFileName.substring(0,index) + ".xml";
			
			// apply second time to reach main project directory
			index = currentFilePath.lastIndexOf("\\");
			currentFilePath = currentFilePath.substring(0, index+1);

			destFilePath = currentFilePath + destDir;
			File destFile   = new File(destFilePath);
			File xmlFile    = new File(currentFilePath + TransFileName);
			
			//JOptionPane.showMessageDialog(VisualFigaro.this, "Cinquième étape réussie");
			
			// If the file containing translations does not exist, create it with the main tags
			// so that it can be loaded by the translator TradBdC.exe
			if (!xmlFile.exists()) {
				xmlFile.createNewFile();
				Writer xw = new OutputStreamWriter(new FileOutputStream(xmlFile), charset);
				xw.write("<TRADUCTIONS_BDC>" + "\n");
				xw.write(" <TRADUCTIONS_FIGARO></TRADUCTIONS_FIGARO>" + "\n");
				xw.write(" <TRADUCTIONS_VALEURS_XML></TRADUCTIONS_VALEURS_XML>" + "\n");
				xw.write("</TRADUCTIONS_BDC>" + "\n");
				xw.write(currentFilePath + "icons" + "fichier2 : " + destFilePath + "icons");
				xw.close();
			}
			
			// Create destination directory if it does not exist
			if (!destFile.exists()){
				destFile.mkdir();
			}
			
			//JOptionPane.showMessageDialog(VisualFigaro.this, "Sixième étape réussie");
			
			// Copy the directory icons (if it already exists, existing icons are replaced, or left as
			// they are, depending on the existence or not of icons with the same name in the origin directory
			CopyFile.copy(new File(currentFilePath + origDir + "icons"),new File(destFilePath + "icons"));

			//JOptionPane.showMessageDialog(VisualFigaro.this, "étape intermédiaire réussie : " + destFilePath + xsdFileName);
			
			// Copy the schema from TradBdC directory to the target directory		
			CopyFile.copyFile(new File(tradBdCPath + xsdFileName),new File(destFilePath + xsdFileName));

			// Construct the lines of the new .ini file
			outLine = " <BDC_OUT MOTS_CLES_XML=";
			outLine += transType;
			outLine += "MOTS_CLES_FIGARO=";
			outLine += transType;
			outLine += "MOTS_BDC=";
			outLine += transType;
			outLine += ">";
			outLine += destFilePath;
			index = currentFile.lastIndexOf("\\");
			outLine += currentFile.substring(index+1,currentFile.length());
			outLine += "</BDC_OUT>";
			
			//JOptionPane.showMessageDialog(VisualFigaro.this, "Septième étape réussie");
			
			while ((ligne = br.readLine()) != null){
			  if(ligne.startsWith(" <BDC_IN>")) {
			     bw.write(" <BDC_IN>" + currentFile + "</BDC_IN>"+ "\n");
			  }
			  else {
			    if (ligne.startsWith(" <FICHIER_MOTS_CLES>")) {
			        bw.write(" <FICHIER_MOTS_CLES>" + tradBdCPath + "MotsClesBdCKB3V3.xml</FICHIER_MOTS_CLES>" + "\n");
			    }
			    else {
			      if (ligne.startsWith(" <FICHIER_MOTS_BDC>")) {
			          bw.write(" <FICHIER_MOTS_BDC>" + currentFilePath + TransFileName + "</FICHIER_MOTS_BDC>" + "\n");
			      } 
			      else {
				    if(ligne.endsWith("BDC_OUT>")) {
					   bw.append(outLine + "\n");
				     } 
				     else {
			           bw.append(ligne + "\n");
				     }  
			      }
			    }
			  }
			}
			//JOptionPane.showMessageDialog(VisualFigaro.this, "Avant dernière étape réussie");
			bw.flush();
			bw.close();
			br.close(); 
			inputFile.delete();
			outputFile.renameTo(new File(TradBdCFileName));
			//JOptionPane.showMessageDialog(VisualFigaro.this, "Dernière étape réussie");
		}
	}*/
	
	
	private void aboutVisualFigaro() {
		GWindowAbout window = new GWindowAbout(this);
		window.setVisible(true);
		//window.setAlwaysOnTop(true);
		window.setLocationRelativeTo(null);
	}
	
	/**
	 * This method is used to display and retrieve the information used to create the bdc
	 * file associated to the knowledge base currently edited. The information is retrieved 
	 * through all the <code>GWindowMain</code> and its subwindows architecture.
	 */
	private void editXMLFile() {
		
		int type = -1;

		//If there is not knowledge base currently edited warn the user
		if(findKnowledgeBaseFromName(currentFile) == null) {
			JOptionPane.showMessageDialog(this, "There is no opened Knowledge Base. Open or select one before editing XML.");
			return;
		}
		
		//The filename of the bdc file
		String bdcFilename = currentFile.substring(0, currentFile.length() - 2) + "bdc";
		
		/*##############################
		 * Dialogbox in the case in which the bdc file does not exist
		 ##############################*/
		
		//So we try to open the bdc file
		File bdcFile = new File(bdcFilename);
		
		//If it does not exists then we popup a new window to ask to the user what to do
		if( !bdcFile.exists() ) {
			
			
			String phrase = "The BDC file associated with " + view.getBuffer().getName() + " does not exist.\nDo you want to create a file containing default information?";
			Object[] options = {"Create Default File", "Start From Scratch", "Cancel"};
			type = JOptionPane.showOptionDialog(VisualFigaro.this.getView(), phrase, "No BDC File", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			
			String data = "";
			
			//According to the type of the 
			switch(type) {
			
				//Case in which we have to create the default file
				case 0:
					
					//We create the command used to create a default bdc file from a figaro file
					String command = "\""+ "./VisualFigaro/" + "figp.exe\" \"" + currentFile + "\" -wse3";
					//String command = "\"" + "./VisualFigaro/" + "st.exe\" \"" + "./VisualFigaro/" + "test1.fi\" -wXe3 \"" + "./VisualFigaro/" + "test1_fi.xml\"";
					System.err.println("Command : "+command);
					System.err.println("Avant");
					data = executeServerWithResultIntoFile(command);
					break;
					
				//Case in which the user want to start from an empty base
				case 1:
					
					break;
					
				//Case in which the user has made a mistake
				case 2:
					
					return;
				
				//If there is already a bdc file we just have to open it
				default:
					
					break;
			}
			
			Charset charset = Charset.forName("ISO-8859-1");
			OutputStreamWriter writer = null;
			try {
			    writer = new OutputStreamWriter(new FileOutputStream(bdcFile),charset);
				writer.write(data);
			} catch (Exception e) {
				System.err.println("VisualFigaro : VisualFigaro : BDC file cannot be created : " + e);
			} finally {
				if(writer != null) {
					try {
						writer.close();
					} catch (Exception e) {
						System.err.println("VisualFigaro : VisualFigaro : Exception while closing the BDC file : " + e);
					}
				}
			}
		}
		/*##############################
		 * End of the configuration of the dialog box
		 ##############################*/

		
		//We create and show the GWindowMain with its xml loader and figaro loader which will be shared by all other windows because they are static
		DOMBuilder builder = new DOMBuilder();
		
		GObjectInformation info = new GObjectInformation();
		info.setLanguage(findKnowledgeBaseFromName(currentFile).getKnowledgeBaseLanguage());
		info.setKnowledgeBasePath(currentFile.substring(0, currentFile.lastIndexOf("\\")));
		
		System.err.println("Current File" + currentFile.substring(0, currentFile.lastIndexOf("\\")));
		
		GXMLLoader xmlLoader = new GXMLLoader(info.getLanguage());
		xmlLoader.loadXmlFile(currentFile.substring(0, currentFile.length() - 2).concat("bdc"));
		GWindow.setXmlLoader(xmlLoader);
		
		GXMLLoaderFigaro figaroLoader = new GXMLLoaderFigaro();
		figaroLoader.setXmlFile(builder.build(figTree.getGTree().getDocument()));
		GWindow.setFigaroLoader(figaroLoader);
		
		GWindowMain mainWindow = new GWindowMain(this, null, info, -1);
		mainWindow.setVisible(true);
		mainWindow.setAlwaysOnTop(true);
	}
	
	/**
	 * Find the language used in the knowledge base using the schema used in the folder containing the knowledge base.
	 * @param path The path to the knowledge base.
	 */
	private String findKnowledgeBaseLanguage(String path) {
		
		//First we have to open the directory
		File directory = new File(path);
		
		//If this file does not exist or is not a directory we have to return zero
		if(!directory.exists() || !directory.isDirectory())
			return "";
		
		//Now we retrieve the Figaro files and we have to check that there is one and only one figaro file
		String[] filesName = directory.list(new FilenameFilter() {
			public boolean accept(File file, String filename) {
				if(filename.toLowerCase().endsWith(".xsd"))
					return true;
				else
					return false;
			}
		});
		if(filesName.length <= 0 || filesName.length > 1)
			return "";
		
		GXMLLoaderDefaultFiles xl = new GXMLLoaderDefaultFiles();
		return xl.getLanguageForSchemaFile(filesName[0]);
	}
	
	private String findModelLanguage(GModel model){
		return model.getModelLanguage().getLanguage();
	}
	
	
	/**
	 * Check the integrity of a knowledge base. By integrity we means that all the files of the knowledge base are written in the same language.
	 * @param path The path to the knowledge base which has to be checked.
	 * @return True if knowledge base files are the right ones false otherwise.
	 */
	private boolean checkIntegrity(String path) {
		
		boolean errorOccured = false;
		String errorMessage = "";
		
		//First we have to open the file
		File directory = new File(path);
		
		//If this file does not exist or is not a directory we have to return zero
		if(!directory.exists() || !directory.isDirectory()) {
			errorMessage += "The Knowledge Base directory does not exist.\n";
			return false;
		}
		
		//Now we retrieve the Figaro files and we have to check that there is one and only one figaro file
		String[] figaroFilesName = directory.list(new FilenameFilter() {
			public boolean accept(File file, String filename) {
				if(filename.toLowerCase().endsWith(".fi"))
					return true;
				else
					return false;
			}
		});
		// We accept multiple .fi files in the same folder (evolution n°12)
		/*if(figaroFilesName.length <= 0 || figaroFilesName.length > 1) {
			errorMessage += "There is no or more than one Figaro file in the Knowledge Base.\n";
			errorOccured = true;
		}*/
		
		//Same test but with bdc file
		String[] bdcFilesName = directory.list(new FilenameFilter() {
			public boolean accept(File file, String filename) {
				if(filename.toLowerCase().endsWith(".bdc"))
					return true;
				else
					return false;
			}
		});
		// We accept multiple .bdc files in the same folder (evolution n°12)
		/*if(bdcFilesName.length > 1) {
			errorMessage += "There is more than one BDC file in the Knowledge Base.\n";
			errorOccured = true;
		}*/
		
		//The two bdc and figaro file must have the same name
		if(bdcFilesName.length > 0)
			if(!bdcFilesName[0].substring(0, bdcFilesName[0].indexOf(".bdc")).equals(figaroFilesName[0].substring(0, figaroFilesName[0].indexOf(".fi")))) {
				errorMessage += "The Figaro file and the BDC file don't have the same name.\n";
				errorOccured = true;
			}
		
		//We check that there is one and only one schema
		String[] schemaFilesName = directory.list(new FilenameFilter() {
			public boolean accept(File file, String filename) {
				if(filename.toLowerCase().endsWith(".xsd"))
					return true;
				else
					return false;
			}
		});
		if(schemaFilesName.length <= 0 || schemaFilesName.length > 1) {
			errorMessage += "There is no or more than one schema file in the Knowledge Base.\n";
			errorOccured = true;
		}
		
		//Now we have to check that the icons directory exists
		File iconsDirectory = new File(directory.getAbsolutePath() + "/icons");
		if(!iconsDirectory.exists()) {
			errorMessage += "The icons folder does not exist.\n";
			errorOccured = true;
		}

		if(errorOccured)
			JOptionPane.showMessageDialog(this, errorMessage, "KB loading error", JOptionPane.ERROR_MESSAGE);
		
		return !errorOccured;
	}
	
	/**
	 * Get all the types declared in the Figaro file of the knowledge base currently edited.
	 * @return A <code>Vector</code> of <code>GCell</code> containing all the types.
	 */
	//######A SUPPRIMER
	public Vector<GCell> getTypes() {
		if(figTree == null)
			return null;
		
		return figTree.getTypes();
	}
	
	//######A SUPPRIMER
	public GTree getGTree() {
		return figTree.getGTree();
	}
	
	public String getCurrentSelectedTree() {
		return comboTree.getSelectedItem().toString();
	}
	
	/**
	 * @return
	 * @uml.property  name="currentFile"
	 */
	public String getCurrentFile() {
		return this.currentFile;
	}
	
	/**
	 * @return
	 * @uml.property  name="view"
	 */
	public View getView() {
		return this.view;
	}

	public GLanguage getLanguage() {
		
		//First we retrieve the current knowledgeBase
		GKnowledgeBase knowledgeBase = findKnowledgeBaseFromName(currentFile);
		
		//Then we retrieve the language
		if(knowledgeBase == null)
			return null;
		else
			return knowledgeBase.getKnowledgeBaseLanguage();
	}
	
	private GKnowledgeBase findKnowledgeBaseFromName(String name) {
		for(GKnowledgeBase knowledgeBase : knowledgedBasesVector)
			if(knowledgeBase.getKnowledgeBaseName().equals(name))
				return knowledgeBase;
		
		return null;
	}
	
	private GModel findModelFromName(String name){
		for(GModel model : modelsVector)
			if(model.getModelName().equals(name))
				return model;
		
		return null;
	}
	
	public String getTextFromFile(File file) {
		//The result String
		String result = "";
		
		//We have to test that the two file exist
		if(!file.exists())
			return result;
		
		try {
			
			//The file reader and the file writer to manipulate the files
			FileReader reader = new FileReader(file);
			
			//The variable to store the characters read in the origin file and which will be written in the destination file
			int readCharacter;
			
			//Now we transfer the file. While the end character (-1) is not reached copy the character from the origin file to the destination file
			do {
				readCharacter = reader.read();
				if(readCharacter != -1)
					result += (char)readCharacter;
			} while(readCharacter != -1);
			
			reader.close();
		} catch (FileNotFoundException e) {
			System.err.println("Visual Figaro : VisualFigaro : The file cannot be written : " + e);
		} catch (IOException e) {
			System.err.println("Visual Figaro : VisualFigaro : The file cannot be written : " + e);
		}
		
		return result;
	}
	
	private boolean precompileXML() {
		
		//We will use the system TMP directory to store the temporary file
		String pathToTempFile = System.getenv("TMP") + "\\test1.fi";
		File tempFile = new File(pathToTempFile);
		
		if(tempFile.exists())
			System.err.println("Exist");
		else
			System.err.println("Not exist");
		
		//We copy the file in a temporary variable
		//####NEW
		
		//GKnowledgeBase knowledgeBase = findKnowledgeBaseFromName(currentFile);
		GModel model = findModelFromName(currentFile);
		
		String fileName = currentFile;
		
		if (model != null){
			fileName = model.getKnowledgeBase().getKnowledgeBaseName();
		}
		
		System.err.println("Voici le fichier a traiter : " + fileName);
		System.err.println("Voici la file dans le buffer : " + view.getBuffer().getDirectory() + view.getBuffer().getName());
		
		if(currentFile.equals(view.getBuffer().getDirectory() + view.getBuffer().getName())) {

			/*
			FileWriter tempFileFileWriter = null;			
			try {
				System.err.println("1");
				tempFileFileWriter = new FileWriter(tempFile);
				System.err.println("2");
				String text = view.getBuffer().getText(0, view.getBuffer().getLength());
				
				System.err.println("2bis");
				tempFileFileWriter.write(text);
				System.err.println("2tierce");
				tempFileFileWriter.close();
				System.err.println("3");
			} catch (Exception ex) {
				System.err.println("Le tempFilewriter : " + tempFileFileWriter);
				System.err.println("VisualFigaro : Visual Figaro : Exception in precompileXML : " + ex);
				return false;
			} finally {
				if(tempFileFileWriter != null)
					try {
						tempFileFileWriter.close();
					} catch(IOException ex) {
						System.err.println("VisualFigaro : Visual Figaro : Exception during closing of FileWriter in precompileXML : " + ex);
						return false;
					}
			}*/
			
			FileChannel inputFile = null;
			FileChannel outputFile = null;
			
			try {
				
				inputFile = new FileInputStream(fileName).getChannel();
				outputFile = new FileOutputStream(pathToTempFile).getChannel();
				
				inputFile.transferTo(0, inputFile.size(), outputFile);
			} catch(Exception e) {
				System.err.println("VisualFigaro : VisualFigaro : Exception during the transfer of file : " + fileName + " : " + e);
			} finally {
				if(inputFile != null) {
					try {
						inputFile.close();
					} catch(Exception ex) {
						System.err.println("VisualFigaro : VisualFigaro : Exception while closing the file : " + fileName + " : " + ex);
					}
				}
				
				if(outputFile != null) {
					try {
						outputFile.close();
					} catch (Exception ex) {
						System.err.println("VisualFigaro : VisualFigaro : Exception while closing the file : " + pathToTempFile);
					}
				}
			}
			
		} else {
			return false;
		}
		
		//Then we launch the processing trough the "serveur de traitement"
		String command = "\"" + "./VisualFigaro/" + "figp.exe\" \"" + System.getenv("TMP") + "\\test1.fi\" -wXe3 \"" + System.getenv("TMP") + "\\test1_fi.xml\"";
		executeServerWithResultIntoFile(command);
		return true;//executeServerWithResultIntoFile(command).length() > 0;
	}
	
	private String executeServerWithResultIntoFile(String command) {
		
		//Declaration of the two buffers filled respectively by the output stream and the error stream
		String outputBuffer = "", errorBuffer = "";
		
		//Boolean indicating if an error  occurred. 
		boolean errorOccured= false;
		
		//We create a runtime environment
		Runtime r = Runtime.getRuntime();
		
		//We run the program and take care if some errors happen
		try {
			File appData = new File(System.getenv("APPDATA"));
			Process process = r.exec(command,null,appData);

			//Now we retrieve both the possible error and the output
			try {

		         InputStream in  = process.getInputStream();
		         InputStream err = process.getErrorStream();
		         
		         // Set to true when the process is finished
		         boolean processFinished = false;

		         //While the process is not finished we read error and output streams and fill the respective buffers
		         while(!processFinished) {
		            
		        	 try {

		        	   //While there is output available retrieve it and put it in the outputBuffer
		               while( in.available() > 0) {
		                  // Print the output of our system call
		                  Character c = new Character( (char) in.read());
		                  outputBuffer += c;
		               }
		               
		               System.err.print("Voici le outputBuffer : \"" + outputBuffer + "\"");

		               //While there is outputerror available retrieve it and put in in the errorBuffer
		               while( err.available() > 0) {
		                  // Print the output of our system call
		                  Character c = new Character( (char) err.read());
		                  errorBuffer += c;
		               }
		               
		               if(errorBuffer.length() > 0) {
		            	   errorOccured = true;
		               }
		               
		               System.err.print("Voici le errorBuffer : \"" + errorBuffer + "\"");
		               
		               // Ask the process for its exitValue. If the process
		               // is not finished, an IllegalThreadStateException
		               // is thrown. If it is finished, we fall through and
		               // the variable finished is set to true.
		               process.exitValue();
		               processFinished  = true;

		            } catch (IllegalThreadStateException e) {
		                  // Process is not finished yet;
		                  // Sleep a little to save CPU cycles
		                  Thread.sleep(500);
		            }
		            
		         }
		         
			} catch (Exception e) {
	            System.err.println("Visual Figaro : VisualFigaro : Input-Output stream error : " + e);
	            return "";
			}

		} catch(Exception e) {
			System.err.println("Visual Figaro : VisualFigaro : Server execution into file error : " + e);	
		}
		
		//If an error has occurred we have to show it to the user in order to detect the problem. We use a basic JFrame
		if(errorOccured) {

			JFrame errorFrame;
	        errorFrame = new JFrame();
	        errorFrame.setTitle("Parsing error detected");
	        final JTextArea errorFrameTextArea = new JTextArea();
	        errorFrameTextArea.setLineWrap(true);
	        errorFrameTextArea.addMouseListener(new MouseListener() {
	        	public void mousePressed(MouseEvent e) {
	        	}
	        	public void mouseReleased(MouseEvent e) {
	        	}
	        	public void mouseClicked(MouseEvent e) {
	        		if(e.getClickCount() == 2) {
	        			//We retrieve the first digit on the line
	        			int start = errorFrameTextArea.getText().lastIndexOf("\n", errorFrameTextArea.getCaretPosition());
	        			int end = errorFrameTextArea.getText().indexOf("\n", errorFrameTextArea.getCaretPosition());
	        			
	        			start = errorFrameTextArea.getText().indexOf("(", start);
	        			
	        			if(start < 0)
	        				return;
	        			end = errorFrameTextArea.getText().indexOf(")", start);
	        			if(end < start)
	        				return;
	        			
	        			
	        			String line = errorFrameTextArea.getText().substring(start+1, end);
	        			
	        			System.out.println("Voici la ligne : " + line);
	        			
	        			Integer entier = new Integer(line);
	        				        			
	        			//We go to the line "pos"
	        			int pos=0;
	        			for(int i=0; i<entier; i++, pos++)
	        				pos = view.getEditPane().getTextArea().getText().indexOf("\n", pos);
	        			
	        			if(pos>0)
	        				view.getEditPane().getTextArea().setCaretPosition(pos);
	        		}
	        	}
	        	public void mouseExited(MouseEvent e) {
	        	}
	        	public void mouseEntered(MouseEvent e) {
	        	}
	        });
	        JScrollPane sp = new JScrollPane(errorFrameTextArea);
	        errorFrame.add(sp);
	        
	        errorFrameTextArea.setText("Fatal Error XML Parsing : \n\n" + errorBuffer);
	        
	        errorFrame.setSize(500, 200);
	        errorFrame.setLocation(300, 200);
	        errorFrame.setVisible(true);
	        errorFrame.setAlwaysOnTop(true);
		}
		
		return outputBuffer;
	}
	
/*private boolean executeServerWithResultIntoString(String command, String fileName) {
		
		//Declaration of the two buffers filled respectively by the output stream and the error stream
		String outputBuffer = "", errorBuffer = "";
		
		//Boolean indicating if an error  occurred. 
		boolean errorOccured= false;
		
		//We create a runtime environment
		Runtime r = Runtime.getRuntime();
		
		//We run the program and take care if some errors happen
		try {
			
			Process process = r.exec(command);
			
			//Now we retrieve both the possible error and the output
			try {

		         InputStream in  = process.getInputStream();
		         InputStream err = process.getErrorStream();
		         
		         // Set to true when the process is finished
		         boolean processFinished = false;

		         //While the process is not finished we read error and output streams and fill the respective buffers
		         while(!processFinished) {
		            
		        	 try {

		        	   //While there is output available retrieve it and put it in the outputBuffer
		               while( in.available() > 0) {
		                  // Print the output of our system call
		                  Character c = new Character( (char) in.read());
		                  outputBuffer += c;
		               }
		               
		               System.err.print("Voici le outputBuffer : \"" + outputBuffer + "\"");

		               //While there is outputerror available retrieve it and put in in the errorBuffer
		               while( err.available() > 0) {
		                  // Print the output of our system call
		                  Character c = new Character( (char) err.read());
		                  errorBuffer += c;
		               }
		               
		               if(errorBuffer.length() > 0) {
		            	   errorOccured = true;
		               }
		               
		               System.err.print("Voici le errorBuffer : \"" + errorBuffer + "\"");
		               
		               // Ask the process for its exitValue. If the process
		               // is not finished, an IllegalThreadStateException
		               // is thrown. If it is finished, we fall through and
		               // the variable finished is set to true.
		               process.exitValue();
		               processFinished  = true;

		            } catch (IllegalThreadStateException e) {
		                  // Process is not finished yet;
		                  // Sleep a little to save CPU cycles
		                  Thread.sleep(500);
		            }
		            
		         }
		         
			} catch (Exception e) {
	            System.err.println( "Visual Figaro : VisualFigaro : Input-Output stream error : " + e);
	            return false;
			}

		} catch(Exception e) {
			System.err.println("Visual Figaro : VisualFigaro : Server execution into string error : " + e);	
		}
		
		//If an error has occurred we have to show it to the user in order to detect the problem. We use a basic JFrame
		if(errorOccured) {

			JFrame errorFrame;
	        errorFrame = new JFrame();
	        errorFrame.setTitle("Parsing error");
	        final JTextArea errorFrameTextArea = new JTextArea();
	        errorFrameTextArea.setLineWrap(true);
	        errorFrameTextArea.addMouseListener(new MouseListener() {
	        	public void mousePressed(MouseEvent e) {
	        	}
	        	public void mouseReleased(MouseEvent e) {
	        	}
	        	public void mouseClicked(MouseEvent e) {
	        		if(e.getClickCount() == 2) {
	        			//We retrieve the first digit on the line
	        			int start = errorFrameTextArea.getText().lastIndexOf("\n", errorFrameTextArea.getCaretPosition());
	        			int end = errorFrameTextArea.getText().indexOf("\n", errorFrameTextArea.getCaretPosition());
	        			
	        			start = errorFrameTextArea.getText().indexOf("(", start);
	        			
	        			if(start < 0)
	        				return;
	        			end = errorFrameTextArea.getText().indexOf(")", start);
	        			if(end < start)
	        				return;
	        			
	        			
	        			String line = errorFrameTextArea.getText().substring(start+1, end);
	        			
	        			System.out.println("Voici la ligne : " + line);
	        			
	        			Integer entier = new Integer(line);
	        				        			
	        			//We go to the line "pos"
	        			int pos=0;
	        			for(int i=0; i<entier; i++, pos++)
	        				pos = view.getEditPane().getTextArea().getText().indexOf("\n", pos);
	        			
	        			if(pos>0)
	        				view.getEditPane().getTextArea().setCaretPosition(pos);
	        		}
	        	}
	        	public void mouseExited(MouseEvent e) {
	        	}
	        	public void mouseEntered(MouseEvent e) {
	        	}
	        });
	        JScrollPane sp = new JScrollPane(errorFrameTextArea);
	        errorFrame.add(sp);
	        
	        errorFrameTextArea.setText("Fatal Error XML Parsing : \n\n" + errorBuffer);
	        
	        errorFrame.setSize(450, 200);
	        errorFrame.setLocation(300, 200);
	        errorFrame.setVisible(true);
	        errorFrame.setAlwaysOnTop(true);
		} else {
			
			FileWriter outputFileFileWriter = null;
			
			try {
				outputFileFileWriter = new FileWriter(fileName);
				outputFileFileWriter.write(outputBuffer);
				outputFileFileWriter.close();
			} catch(Exception ex) {
				System.err.print("Visual Figaro : VisualFigaro : ");
			} finally {
				if(outputFileFileWriter != null)
					try {
						outputFileFileWriter.close();
					} catch (IOException ex) {
						System.err.println("Visual Figaro : GWindowWizard : Error while closing Figaro file : " + ex);
					}
			}
		}
		
		return errorOccured;
	}*/
	
	public void focusOnDefaultComponent()
	{
		//textArea.requestFocus();
	}

	public void handleMessage(EBMessage message)
	{
		System.err.println("Handle message");
		
		if(isOpening) {
			System.err.println("Handle message cancel");
			return;
		}

		System.err.println("Handle message continue");
			
		
		/*
		if (message instanceof PropertiesChanged) {
			//propertiesChanged();
		}*/
		/*
		if(message instanceof VFSUpdate) {
			System.out.println("#############1 Voici le message intercepte : " + message.paramString());
		}*/
		
		//If the message indicate that
		if(message instanceof EditPaneUpdate) {

			//First we test if the buffer has changed (in the sense that now we deal with another buffer)
			if(((EditPaneUpdate)message).getWhat().equals(org.gjt.sp.jedit.msg.EditPaneUpdate.BUFFER_CHANGED)) {
				
				//In this particular case we record that the current buffer has changed in order to modify only the tree concerned
				currentFile = ((EditPane)message.getSource()).getBuffer().getPath();
				String directory = ((EditPane)message.getSource()).getBuffer().getDirectory();
				System.err.println("buffer changed : " + currentFile);
				
				//First we find the knowledgeBase
				GKnowledgeBase knowledgeBase = findKnowledgeBaseFromName(currentFile);
				
				//Then we find the model if it is
				GModel model = findModelFromName(currentFile);
				
				//If we can find a name corresponding to the current buffer name we update the combobox to display this name otherwise we select a blank in the combobox
				if(knowledgeBase != null || model !=null)
					comboTree.setSelectedItem(currentFile);
				else
					comboTree.setSelectedIndex(-1);
			
				if (model !=null){
					languageName = findModelLanguage(model);
				} else {
					//Update language
					languageName = findKnowledgeBaseLanguage(directory); 
				}
				GLanguage language = new GLanguage();
				language.setLanguage(languageName);
				
			} else {
				
				//We dont know what to do for general message so we do nothing in particular.
			}
			
		}
		
		if(message instanceof BufferUpdate) {
			
			//When a file is closed we check if it is not associated with a knowledge base or a model. If the file is associated with a knowledge base or a model then we will close it
			//which is currently equivalent to close and to delete the tree associated. #######See the XML later
			if(((BufferUpdate)message).getWhat().equals( org.gjt.sp.jedit.msg.BufferUpdate.CLOSED)) {
		
				//((Buffer)message.getSource()).getDirectory();
				System.err.println("CLOSED");
				
				GKnowledgeBase knowledgeBase = findKnowledgeBaseFromName(((Buffer)message.getSource()).getPath());
				
				if(knowledgeBase != null) {
					
					/*
					//We have to ask to the user what to do. Is it an error or does he really want to close the database.
					int choice = JOptionPane.showConfirmDialog(this, "A database is attached to this file. You have to close it before closing this file. Do you want to close it now ?" + ((Buffer)message.getSource()).getPath());
					if(choice == 0) {
						closeKB();
					} else {
						return;
					}
					*/
					
					knowledgedBasesVector.remove(knowledgeBase);
					comboTree.removeItem(knowledgeBase.getKnowledgeBaseName());
				} else {
					GModel model = findModelFromName(((Buffer)message.getSource()).getPath());
					
					if(model !=null) {
						modelsVector.remove(model);
						comboTree.removeItem(model.getModelName());
					}
				}
				

			}
				
			//We test when a file is created. Currently it is useless because we have to wait until the user creates the tree and maps it to a knowledge base clicking on Modify button
			if(((BufferUpdate)message).getWhat().equals( org.gjt.sp.jedit.msg.BufferUpdate.CREATED)) {
				currentFile = ((Buffer)message.getSource()).getPath();
				System.err.println("CREATED : " + currentFile);
				//System.out.println("Le buffer a ete cree : " + this.view.getBuffer().getName() + " avec le path : " + this.view.getBuffer().getPath());
			}
			
			//We test when a file is saved.
			if(((BufferUpdate)message).getWhat().equals( org.gjt.sp.jedit.msg.BufferUpdate.SAVED)) {
				
				System.err.println("SAVED : " + currentFile);
				
				if (findKnowledgeBaseFromName(currentFile)!=null){
					for (GModel m : modelsVector)
						if (m.getKnowledgeBase().getKnowledgeBaseName().equals(currentFile))
								m.setKBModify(true);	
				}
				
				if (findModelFromName(currentFile)!=null){
					findModelFromName(currentFile).setModelModify(true);
				}
				
				//JOptionPane.showMessageDialog(VisualFigaro.this, "File : "+ currentFile + " has been saved");
				//System.out.println("DIXXXXXXXXXXXX : Voici la source : " + message.getSource().toString() + " et le currentFile : " + currentFile);
				/*String oldFile = currentFile;
				currentFile = message.getSource().toString();
				*/
				/*
				if(nomsDeFichier.contains(oldFile)) {
					
					nomsDeFichier.remove(oldFile);
					nomsDeFichier.add(currentFile);
					//Enleve car il faudrait savoir comment modifier un champ dans une combobox et je n'ai pas le temps
					//nomsDeFichier.set(nomsDeFichier.indexOf(oldFile), message.getSource().toString());
					comboTree.addItem(currentFile);
					comboTree.setSelectedItem(currentFile);
					comboTree.removeItem(oldFile);
					
					System.out.println("######Voici la taille du vecteur : " + nomsDeFichier.size());
				}
				*/
			
				//System.out.println("Le buffer a ete detruit : " + oldFile);
				
				//System.out.println("Le buffer a ete cree : " + currentFile + " avec le path : " + this.view.getBuffer().getPath());
				
			}
		}
	}
	
	public void setIconsUpToDate() {
		
		//First we have to check if there is a bdc file for the current knowledge base
		File bdcFile = new File(currentFile.substring(0, currentFile.length() - 2) + "bdc");
		if(!bdcFile.exists())
			return;
		
		//If the test is positive we create a hashtable
		Hashtable<String, String> typeIconPathTable = new Hashtable<String, String>();
		
		//We retrieve all the types name and we will store them in a hastable with the icons path corresponding
		Vector<String> typesName = new Vector<String>();
		Vector<GCell> types = figTree.getTypes();
		
		for(GCell c : types) {
			if(c.getChild(0) != null)
				if(c.getChild(0).getChild(0) != null) {
					//System.out.println("Voici la valeur de la cellule : " + (String)c.getChild(0).getChild(0).getValue(1));
					typesName.add((String)c.getChild(0).getChild(0).getValue(1));
				}
		}
		
		//Now we have to retrieve the name of the icones associated with each type by parsing the xml file
		GXMLLoader xl = new GXMLLoader(null);
		Element root = xl.loadXmlFileJDOM(currentFile.substring(0, currentFile.length() - 2) + "bdc").getRootElement();
		
		//We retrieve the knowledgeBase which has to be updated
		GKnowledgeBase knowledgeBase = findKnowledgeBaseFromName(currentFile);
		
		//First we retrieve all the VG with an icon
		Vector<Element> varianteGraphiqueDefautVect = xl.findElementWithSubBalise(root, 2, knowledgeBase.getKnowledgeBaseLanguage().getBDCTranslation("VARIANTE_GRAPHIQUE_DEFAUT"), knowledgeBase.getKnowledgeBaseLanguage().getBDCTranslation("ICONE"));
		
		//We are testing if there is a file associated. If yes we will retrieve the associated type
		for(Element e : varianteGraphiqueDefautVect)
			if(e.getChild(knowledgeBase.getKnowledgeBaseLanguage().getBDCTranslation("ICONE")).getChild(knowledgeBase.getKnowledgeBaseLanguage().getBDCTranslation("FICHIER")) != null)
				typeIconPathTable.put(((Element)e.getParent()).getChildText(knowledgeBase.getKnowledgeBaseLanguage().getBDCTranslation("NOM")), view.getBuffer().getDirectory() + "icons\\" + e.getChild(knowledgeBase.getKnowledgeBaseLanguage().getBDCTranslation("ICONE")).getChildText(knowledgeBase.getKnowledgeBaseLanguage().getBDCTranslation("FICHIER")) + ".ico");
		
		//If there is no such database just return
		if(knowledgeBase == null)
			return;
		
		//Otherwise update the knowledgeBase with the new hashtable
		knowledgeBase.setTypeIconPathTable(typeIconPathTable);
	}
		
	
	public String getIcon(String type) {
		
		//First we retrieve the knowledgeBase
		GKnowledgeBase knowledgeBase = findKnowledgeBaseFromName(currentFile);
		
		//if the currentFile is not a knowledgeBase, it is probably a model
		if(knowledgeBase == null) {
			GModel model = findModelFromName(currentFile);
			if (model != null)
				knowledgeBase = model.getKnowledgeBase();
		}
		
		//If there is no such database just return null. It usually does not happen but...
		if(knowledgeBase == null)
			return null;
		
		//Otherwise return the right icon
		if(knowledgeBase.getTypeIconPathTable() != null)
			return knowledgeBase.getTypeIconPathTable().get(type);
		else
			return null;
	}
	
	public void repaintFigTree() {
		figTree.repaint();
	}

	/////////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	//                            REMAINING PART OF THE EXAMPLE                    \\
	/////////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	// These JComponent methods provide the appropriate points
	// to subscribe and unsubscribe this object to the EditBus

	public void addNotify()
	{
		super.addNotify();
		EditBus.addToBus(this);
	}
	
	public void removeNotify()
	{
		super.removeNotify();
		EditBus.removeFromBus(this);
	}
	
	// <Esc> closes a floating window
	/*private class KeyHandler extends KeyAdapter {
		public void keyPressed(KeyEvent evt) {
			if(VisualFigaro.this.floating &&
				evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
				evt.consume();
				DockableWindowManager wm =
					VisualFigaro.this.view.getDockableWindowManager();
				wm.removeDockableWindow(VisualFigaroPlugin.NAME);
			}
		}
	}*/
	
}
