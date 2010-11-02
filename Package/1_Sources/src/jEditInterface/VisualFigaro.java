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
 * **************************************************************/

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
import java.lang.StringBuffer;

// from Swing:
import javax.swing.*;

// from jEdit:
import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.gui.*;
import org.gjt.sp.jedit.msg.EditPaneUpdate;
import org.gjt.sp.jedit.msg.BufferUpdate;

import org.jdom.Element;
import org.jdom.input.DOMBuilder;

import GKnowledgeBase.GKnowledgeBase;
import GLanguage.GLanguage;
import GObjectInformation.GObjectInformation;
import GWindow.GWindow;
import GWindow.GWindowAbout;
import GWindow.GWindowMain;
import GWindow.GWindowWizard;
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
		
		menuItem = new JMenuItem("About Visual Figaro");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VisualFigaro.this.aboutVisualFigaro();
			}
		});
		menu.add(menuItem);
		menuBar.add(menu);
		
		//The menu concerning the management of the XML part of the KB
		menu = new JMenu("XML Management");
		menuItem = new JMenuItem("Edit XML File");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VisualFigaro.this.editXMLFile();
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
					
					//And finally we load the tree
					figTree.loadSpecificGTree(findKnowledgeBaseFromName(currentFile).getKnowledgeBaseTree());
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
				if(knowledgeBase == null) {
					
					//Show an error message
					JOptionPane.showMessageDialog(VisualFigaro.this, "The Figaro file does not correspond to any opened Knowledge Base. Please open the corresponding Knowledge Base first.");
					
				} else {
					comboTree.setSelectedItem(currentFile);
				
					//String path = System.getenv("VISUAL_FIGARO") + "test1_fi.xml";
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
		
		//First we open a classical open dialogbox
		/*JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int choice = fileChooser.showOpenDialog(this);
		
		if(choice != 0)
			return;
		
		//Then we retrieve the name of the directory selected
		File directory = fileChooser.getSelectedFile();
		
		openKB(directory);*/
		
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
		
		//We have to check if all the files are consistant
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
				//figTree.loadTreeFromXML(System.getenv("VISUAL_FIGARO") + "test1_fi.xml", getTextFromFile(new File(currentFile)), language);
				figTree.loadTreeFromXML(System.getenv("TMP") + "\\test1_fi.xml", getTextFromFile(new File(currentFile)), language);
				
				GKnowledgeBase knowledgeBase = new GKnowledgeBase(figaroFileName, language, figTree.getGTree().clone());
				knowledgedBasesVector.add(knowledgeBase);
				
				comboTree.addItem(figaroFileName);
				comboTree.setSelectedItem(figaroFileName);
				
				setIconsUpToDate();
			} else {
				System.err.println("Precompile Failed");
			}
			
		}
	}
	
	public void openKB(File directory, File file) {
		
		
		//We have to check if all the files are consistant
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
				//figTree.loadTreeFromXML(System.getenv("VISUAL_FIGARO") + "test1_fi.xml", getTextFromFile(new File(currentFile)), language);
				figTree.loadTreeFromXML(System.getenv("TMP") + "\\test1_fi.xml", getTextFromFile(new File(currentFile)), language);
				
				GKnowledgeBase knowledgeBase = new GKnowledgeBase(figaroFileName, language, figTree.getGTree().clone());
				knowledgedBasesVector.add(knowledgeBase);
				
				comboTree.addItem(figaroFileName);
				comboTree.setSelectedItem(figaroFileName);
				
				setIconsUpToDate();
			} else {
				System.err.println("Precompile Failed");
			}
			
		}
	}
	
	private void saveToIniFile(String filePath) throws IOException {
		
		String AppliDataPath = System.getenv("AppData"); 
		String VFIniFilePath= AppliDataPath + "\\EDF MRI TOOLS\\VisualFigaro.ini";
		
		File inputFile = new File(VFIniFilePath);
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		
		File outputFile = new File(AppliDataPath + "\\EDF MRI TOOLS\\temp.ini");
		Charset charset = Charset.forName("UTF-8");
		Writer bw = new OutputStreamWriter(new FileOutputStream(outputFile), charset);
		
	    bw.write("<PREV_PATH>" + filePath + "</PREV_PATH>"+ "\n");
	    
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
		String result = null;
		
		
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
				saveToIniFile("");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if ((ligne = br.readLine()) != null){
				  if(ligne.startsWith("<PREV_PATH>")) {
					  int end = ligne.lastIndexOf("</PREV_PATH>");
					  result =  ligne.substring(11,end);
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
		
		//Update last open KB file
		JMenuItem item = menuBar.getMenu(0).getItem(6);
		item.setText(currentFile);
		
		// Update VisualFigaro.ini file
		try {
			saveToIniFile(currentFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//We find the knowledge base
		GKnowledgeBase knowledgeBase = findKnowledgeBaseFromName(currentFile);
		
		//We check if the file exist
		if(knowledgeBase != null) {

			//Pay attention to the order because of the indexes extracted from the vectors
			Buffer  save= view.getBuffer(); 
			comboTree.removeItem(currentFile);
			jEdit.closeBuffer(view, save);
			knowledgedBasesVector.remove(knowledgeBase);

		} else {
			comboTree.setSelectedIndex(-1);
			jEdit.closeAllBuffers(view);
		}
		currentFile = null;
	}
	
	private void translateKB() throws IOException {
		
		String BdCPath = "C:\\Program Files\\jEdit\\VisualFigaro\\TradBdC\\"; 
		updateTradBdcIniFile(BdCPath);
        		
		// Launch the KB3 translator
		String cmd = BdCPath + "\\TradBDC.exe";                
		try {
			Runtime r = Runtime.getRuntime();
		    Process p = r.exec(cmd);
		    p.waitFor();  // The application shall wait end of translator process
			}catch(Exception e) {
			  System.out.println("erreur d'execution " + cmd + e.toString());
		    }
	}
	
	private void updateTradBdcIniFile(String tradBdCPath) throws IOException {
		
		// This method manages the needed files and directories before
		// KB3 translator tool launching
		
		String TradBdCFileName = tradBdCPath + "TradBdC.ini";
		File inputFile = new File(TradBdCFileName);
		
		if (currentFile == null) {
		    // The TradBdC.ini file shall not be modified	
			
		} else {
			//The TradBdC.ini file shall be updated with the current edited .fi file
			//The filename will be the concatenation of the directory path and the TradBdC .ini filename			
			File outputFile = new File(tradBdCPath + "temp.ini");
			
			// select UTF-8 format to generate correct "ç" in output file
			Charset charset = Charset.forName("UTF-8");
			Writer bw = new OutputStreamWriter(new FileOutputStream(outputFile), charset);
			
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			String ligne="";
			String transType = "\"Français -> Anglais\" ";
			String destDir = "..\\English\\";
			String outLine;
			String currentFilePath = "";
			String destFilePath = "";
			String TransFileName = "";
			String xsdFileName = "bdceng.xsd";
			
			if (languageName.equals("English")){
				transType = "\"Anglais -> Français\" ";
				destDir = "..\\Francais\\";
				xsdFileName = "bdcfr.xsd";
			}
			
			// Construct destination translation directory and file
			int index = currentFile.lastIndexOf("\\");
			currentFilePath = currentFile.substring(0, index+1);
			TransFileName = "..\\Translation_" + currentFile.substring(index+1,currentFile.length()-3) + ".xml"; 
			destFilePath = currentFilePath + destDir;
			File destFile   = new File(destFilePath);
			File xmlFile    = new File(currentFilePath + TransFileName);
			
			// Check if xml file exists
			if (!xmlFile.exists()) {
				xmlFile.createNewFile();
				Writer xw = new OutputStreamWriter(new FileOutputStream(xmlFile), charset);
				xw.write("<TRADUCTIONS_BDC>" + "\n");
				xw.write(" <TRADUCTIONS_FIGARO></TRADUCTIONS_FIGARO>" + "\n");
				xw.write(" <TRADUCTIONS_VALEURS_XML></TRADUCTIONS_VALEURS_XML>" + "\n");
				xw.write("</TRADUCTIONS_BDC>" + "\n");
				xw.close();
			}
			
			// Check destination directory exists
			if (!destFile.exists()){
				destFile.mkdir();
			}
			
			// Copy files to the destination directory
			CopyFile.copy(new File(currentFilePath + "icons"),new File(destFilePath + "icons"));
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
			outLine += currentFile.substring(index+1,currentFile.length());
			outLine += "</BDC_OUT>";
				
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
			bw.flush();
			bw.close();
			br.close(); 
			inputFile.delete();
			outputFile.renameTo(new File(TradBdCFileName));
		}
	}
	
	/*private void setLanguageInMenuBar(String newLanguage) {
		
		System.err.println("On change le langage du menu : " + newLanguage);
		
		languageMenu.get(newLanguage).setSelected(true);
		
	*/
	
	private void aboutVisualFigaro() {
		GWindowAbout window = new GWindowAbout(this);
		window.setVisible(true);
		//window.setAlwaysOnTop(true);
		window.setLocationRelativeTo(null);
	}
	
	/**
	 * This method is used to display and retrieve the information used to create the bdc file associated to the knowledge base currently edited. The information is retrieved through all the <code>GWindowMain</code> and its subwindows architecture.
	 */
	private void editXMLFile() {
		
		int type = -1;

		//If there is not knowledge base currently edited warn the user
		if(currentFile == null) {
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
					String command = "\""+ System.getenv("VISUAL_FIGARO") + "st.exe\" \"" + currentFile + "\" -wse3";
					//String command = "\"" + System.getenv("VISUAL_FIGARO") + "st.exe\" \"" + System.getenv("VISUAL_FIGARO") + "test1.fi\" -wXe3 \"" + System.getenv("VISUAL_FIGARO") + "test1_fi.xml\"";
					System.err.println("Command : "+command);
					System.err.println("Avant");
					data = executeServerWithResultIntoFile(command);
					
					// Add missing names for FTA generation, Simulation and Fig0 generation
					// since the ST server does not create these fields. Once the ST server
					// is updated, the following string insertion can be suppressed
					
					String toFind = "<MODELE_GENERATION_ADD>";
					int Index= data.indexOf(toFind);
					int Start = Index+ toFind.length();
					String toBeInserted = "\n\t\t<NOM>Génération AdD</NOM>";
					data = new StringBuffer(data).insert(Start,toBeInserted).toString();
					
					toFind = "<MODELE_SIMULATION>";
					Index= data.indexOf(toFind);
					Start = Index+ toFind.length();
					toBeInserted = "\n\t\t<NOM>Simulation</NOM>";
					data = new StringBuffer(data).insert(Start,toBeInserted).toString();
					
					toFind = "<MODELE_INST_FIG0>";
					Index= data.indexOf(toFind);
					Start = Index+ toFind.length();
					toBeInserted = "\n\t\t<NOM>Génération Fig0</NOM>";
					data = new StringBuffer(data).insert(Start,toBeInserted).toString();
					
					System.err.println("########################## Apres : " + data);
					
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
		
		//Now we have to check that each existing .sym file has its corresponding .ico file
		File iconsDirectory = new File(directory.getAbsolutePath() + "\\icons");
		if(!iconsDirectory.exists()) {
			errorMessage += "The icons folder does not exist.\n";
			errorOccured = true;
		}
		
		String[] symFilesName = iconsDirectory.list(new FilenameFilter() {
			public boolean accept(File file, String filename) {
				if(filename.toLowerCase().endsWith(".sym"))
					return true;
				else
					return false;
			}
		});
		for(String symFileName : symFilesName) {
			File icoFile = new File(iconsDirectory.getAbsolutePath() + "\\" + symFileName.substring(0, symFileName.lastIndexOf(".sym")) + ".ico");
			if(!icoFile.exists()) {
				errorMessage += "The " + symFileName + ".sym file does not correspond to any .ico file.\n";
				errorOccured = true;
			}
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
	
	/*private boolean copyFile(File origin, File destination) {
		
		try {
			
			//The file reader and the file writer to manipulate the files
			FileReader reader = new FileReader(origin);
			FileWriter writer = new FileWriter(destination);
			
			//The variable to store the characters read in the origin file and which will be written in the destination file
			int readCharacter;
			
			//Now we transfer the file. While the end character (-1) is not reached copy the character from the origin file to the destination file
			do {
				readCharacter = reader.read();
				if(readCharacter != -1)
					writer.write(readCharacter);
			} while(readCharacter != -1);
			
			reader.close();
			writer.close();
		} catch (FileNotFoundException e) {
			System.err.println("Visual Figaro : VisualFigaro : The file cannot be copied : " + e);
		} catch (IOException e) {
			System.err.println("Visual Figaro : VisualFigaro : The file cannot be copied : " + e);
		}
		
		return true;
	}*/
	
	/*private boolean translateKB(String language) {
		
		//We have to compare the current knowledge base language to the new one in order to avoid extra work if they are both the same
		String currentDirectoryName = currentFile.substring(0, currentFile.lastIndexOf("\\"));
		String currentLanguage = findKnowledgeBaseLanguage(currentDirectoryName);
		if(currentLanguage.equals(language))
			return true;
		
		//Then we create a new language to convert the kb
		GLanguage newLanguage = new GLanguage();
		
		//First we have to test if the language exists or not
		if(!newLanguage.setLanguage(language))
			return false;
		
		//If everything goes fine
		GXMLLoaderDefaultFiles xlDefaultFiles = new GXMLLoaderDefaultFiles();
		String currentSchemaFileName = xlDefaultFiles.getSchemaFilenameForLanguage(currentLanguage);
		String currentBDCFileName = currentFile.substring(currentFile.lastIndexOf("\\"), currentFile.lastIndexOf(".")).concat(".bdc");
		
		//Remove the schema and put the right one using the copiedFile.xml file
		File oldSchema = new File(currentDirectoryName + "\\" + currentSchemaFileName);
		if(!oldSchema.delete())
			return false;
		copyFile(new File(System.getenv("VISUAL_FIGARO") + xlDefaultFiles.getSchemaFilenameForLanguage(language)), new File(currentDirectoryName + "\\" + xlDefaultFiles.getSchemaFilenameForLanguage(language)));
		
		//Translate figaro file, if exists, using the equivalent between the language
		
		//Translate the bdc file, if exists, using the equivalent between the language
		translateBDCFile(currentDirectoryName + "\\" + currentBDCFileName, currentLanguage, language);
		//####translateFigaroFile(currentFile, currentLanguage, language);
		
		findKnowledgeBaseFromName(currentFile).setKnowledgeBaseLanguage(newLanguage);
		
		return true;
	}*/
	
	/*private boolean translateBDCFile(String bdcFileName, String oldLanguageName, String newLanguageName) {
		
		//First we create to GLanguage object to ease the manipulation of the language files
		GLanguage oldLanguage = new GLanguage();
		if(!oldLanguage.setLanguage(oldLanguageName))
			return false;
		GLanguage newLanguage = new GLanguage();
		if(!newLanguage.setLanguage(newLanguageName))
			return false;
		
		//We retrieve the text of the bdc file
		String bdcText = readTextFromFile(bdcFileName);

		System.err.println("Voila la file " + bdcFileName + " avant : " + bdcText);
		
		// Modify xsd file name
	    String bdceng = new String("bdceng");
	    String bdcfr = new String("bdcfr");
	   
	    
		if (oldLanguageName.equals("English"))
		{
			bdcText = bdcText.replaceAll(bdceng, bdcfr);
		}
		else
		{
			bdcText = bdcText.replaceAll(bdcfr, bdceng);
		}
		
		//Then we have to go through all the keywords of old language. Locate them in the bdc file and replace them with the new keyword
		for(String oldKeyword : oldLanguage.getAllBDCKeywords())
			bdcText = replaceWordInBDCFile(bdcText, oldKeyword, newLanguage.getBDCTranslation(oldKeyword));
		
		//Then we write the text to the file
		writeTextToFile(bdcFileName, bdcText);
		
		return true;
	}*/
	
	/*private boolean translateFigaroFile(String figaroFileName, String oldLanguageName, String newLanguageName) {
		
		//First we create to GLanguage object to ease the manipulation of the language files
		GLanguage oldLanguage = new GLanguage();
		if(!oldLanguage.setLanguage(oldLanguageName))
			return false;
		GLanguage newLanguage = new GLanguage();
		if(!newLanguage.setLanguage(newLanguageName))
			return false;
		
		//We retrieve the text of the bdc file
		String figaroText = readTextFromFile(figaroFileName);
		
		//Then we have to go through all the keywords of old language. Locate them in the bdc file and replace them with the new keyword
		for(String oldKeyword : oldLanguage.getAllBDCKeywords())
			figaroText = replaceWordInBDCFile(figaroText, oldKeyword, newLanguage.getBDCTranslation(oldKeyword));
		
		//Then we write the text to the file
		writeTextToFile(figaroFileName, figaroText);
		
		return true;
	}*/
	
	/*private String readTextFromFile(String fileName) {
		//First we open the file
		File file = new File(fileName);
		if(!file.exists())
			return "";
		
		//The whole text variable
		String text = "";
		
		//Otherwise we retrieve the text of the file
		try {
			
			//The reader to the file
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			
			//We retrieve all the line of the file
			String line = "";
			while((line = bufferedReader.readLine()) != null) {
				text += line + "\n";
			}
			
			//Then we close the reader
			bufferedReader.close();
			
		} catch(Exception e) {
			System.err.println("VisualFigaro : VisualFIgaro : Text wasn't retrieved from file : " + fileName);
		}
		
		return text;
	}*/
	
	/*private boolean writeTextToFile(String fileName, String text) {
		
		//Otherwise we retrieve the text of the file
		try {
			
			//The writer to the file
			FileWriter fileWriter = new FileWriter(fileName);
			
			//Then we write the text to he file
			fileWriter.write(text);
			
			//Finally we close the file
			fileWriter.close();
			
		} catch(Exception e) {
			System.err.println("VisualFigaro : VisualFigaro : Text wasn't written to the file : " + fileName);
			return false;
		}
		
		return true;
	}*/
	
	/*private String replaceWordInBDCFile(String fileText, String oldWord, String newWord) {
		
		if(oldWord.contains(":") || oldWord.contains("\\") || oldWord.contains(".") || oldWord.contains("/") || oldWord.contains("\""))
			return fileText;
		
		String regularExpression = "\\b" + oldWord + "\\b";
		
		return fileText.replaceAll(regularExpression, newWord);
	}*/
	
	/*private String replaceWordInFigaroFile(String fileText, String oldWord, String newWord) {
		
		if(oldWord.contains(":") || oldWord.contains("\\") || oldWord.contains(".") || oldWord.contains("/") || oldWord.contains("\""))
			return fileText;
		
		String regularExpression = "\\b" + oldWord + "\\b";
		
		return fileText.replaceAll(regularExpression, newWord);
	}*/
	
	private boolean precompileXML() {
		
		//We will use the Visual Figaro directory to store the temporary file
		//String pathToTempFile = System.getenv("VISUAL_FIGARO") + "test1.fi";
		String pathToTempFile = System.getenv("TMP") + "\\test1.fi";
		File tempFile = new File(pathToTempFile);
		
		if(tempFile.exists())
			System.err.println("Exist");
		else
			System.err.println("Not exist");
		
		//We copy the file in a temporary variable
		//####NEW
		
		System.err.println("Voici le fichier a traiter : " + currentFile);
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
				
				inputFile = new FileInputStream(currentFile).getChannel();
				outputFile = new FileOutputStream(pathToTempFile).getChannel();
				
				inputFile.transferTo(0, inputFile.size(), outputFile);
			} catch(Exception e) {
				System.err.println("VisualFigaro : VisualFigaro : Exception during the transfer of file : " + currentFile + " : " + e);
			} finally {
				if(inputFile != null) {
					try {
						inputFile.close();
					} catch(Exception ex) {
						System.err.println("VisualFigaro : VisualFigaro : Exception while closing the file : " + currentFile + " : " + ex);
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
		//####NEW
		//OLD
		//copyFile(new File(currentFile), temp);
		//OLD
		
		//Then we launch the processing trough the "serveur de traitement"
		//String command = "\"" + System.getenv("VISUAL_FIGARO") + "st.exe\" \"" + System.getenv("VISUAL_FIGARO") + "test1.fi\" -wXe3 \"" + System.getenv("VISUAL_FIGARO") + "test1_fi.xml\"";
		String command = "\"" + System.getenv("VISUAL_FIGARO") + "st.exe\" \"" + System.getenv("TMP") + "\\test1.fi\" -wXe3 \"" + System.getenv("TMP") + "\\test1_fi.xml\"";
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
	        
	        errorFrame.setSize(450, 200);
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
				
				//If we can find a name corresponding to the current buffer name we update the combobox to display this name otherwise we select a blank in the combobox
				if(knowledgeBase != null)
					comboTree.setSelectedItem(currentFile);
				else
					comboTree.setSelectedIndex(-1);
				
				//Update language
				languageName = findKnowledgeBaseLanguage(directory); 
				GLanguage language = new GLanguage();
				language.setLanguage(languageName);
				
				// Update the TradBdC.ini file
				String BdCPath = "C:\\Program Files\\jEdit\\VisualFigaro\\TradBdC\\"; 
				try {
					updateTradBdcIniFile(BdCPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {
				
				//We dont know what to do for general message so we do nothing in particular.
			}
		}
		
		if(message instanceof BufferUpdate) {
			
			//When a file is closed we check if it is not associated with a knowledge base. If the file is associated with a knowledge base then we will close the knowledge base
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
