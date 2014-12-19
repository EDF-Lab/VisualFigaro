package GWindow;

import jEditInterface.VisualFigaro;
import CopyFile.CopyFile;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
import java.nio.channels.FileChannel;
//import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

//import org.gjt.sp.jedit.jEdit;
import org.jdom.Element;

import GMessage.GMessage;
import GWidget.GWidgetOKCancel;
import GXMLLoader.GXMLLoaderDefaultFiles;

public class GWindowWizard extends GWindow {

	private static final long serialVersionUID = 1L;

	//The variable visualFigaro to store the parent
	/**
	 * @uml.property  name="vfParent"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private VisualFigaro vfParent;
	
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

	//The loader for the copiedFiles
	/**
	 * @uml.property  name="xlDefaultFiles"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GXMLLoaderDefaultFiles xlDefaultFiles;
	
	//The lower part of the panel containing the fields
	/**
	 * @uml.property  name="fieldsPanel"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JPanel fieldsPanel;
	//The JTextField for the name of the KB
	/**
	 * @uml.property  name="nameTextField"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JTextField nameTextField;
	//The JComboBox for the language selection
	/**
	 * @uml.property  name="languageComboBox"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JComboBox languageComboBox;
	//The JTextField for the path of the KB
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
	
	public GWindowWizard(VisualFigaro vf) {
		super();
		
		//We initialize the parent
		vfParent = vf;
		
		//Initialization of the loader
		xlDefaultFiles = new GXMLLoaderDefaultFiles();
		
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
		setTitle("KB Creation Wizard");
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
		JLabel titleLabel = new JLabel("Knowledge Base Creation Wizard");
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
		labelPanel.add(new JLabel("Language : "));
		northPanel.add(labelPanel);
		
		//The panel containing the components corresponding to the labels
		JPanel componentPanel = new JPanel(new GridLayout(2,1,5,5));
		//Initialization of the name textfield
		nameTextField = new JTextField();
		componentPanel.add(nameTextField);
		//Initialization of the name language comboBoxs
		languageComboBox = new JComboBox();
		for(String language : xlDefaultFiles.getAvailableLanguages())
			languageComboBox.addItem(language);
		componentPanel.add(languageComboBox);
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
				GWindowWizard.this.showFileChooser();
			}
		});
		explorePanel.add(openExplorerButton, BorderLayout.EAST);
		fieldsPanel.add(explorePanel, BorderLayout.SOUTH);
	}
	
	private void showFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.showOpenDialog(this);
		File file = fileChooser.getSelectedFile();
		pathTextField.setText(file.toString());
		fileChooser.setVisible(false);
	}
	/*
	 public static void copy(final InputStream inStream, final OutputStream outStream, final int bufferSize) throws IOException {
		 final byte[] buffer = new byte[bufferSize];
		 int nbRead;
		 while ((nbRead = inStream.read(buffer)) != -1) {
		  outStream.write(buffer, 0, nbRead);
		 }
		}
		   
		public static void copyDirectory(final File from, final File to) throws IOException {
		 if (! to.exists()) {
		  to.mkdir();
		 }
		 final File[] inDir = from.listFiles();
		 for (int i = 0; i < inDir.length; i++) {
		  final File file = inDir[i];
		  copy(file, new File(to, file.getName()));
		 }
		}
		public static void copyFile(final File from, final File to) throws IOException {
		      final InputStream inStream = new FileInputStream(from);
		      final OutputStream outStream = new FileOutputStream(to);
		      if (from.length() > 0){
		        copy(inStream, outStream, (int) Math.min(from.length(), 4*1024));
		      }
		      inStream.close();
		      outStream.close();
		   } 
		public static void copy(final File from, final File to) throws IOException {
		 if (from.isFile()) {
		  copyFile(from, to);
		 } else if (from.isDirectory()){
		  copyDirectory(from, to);
		 } 
		} */
	
	public void translateMessage(GMessage message) {
		switch(message.getMessage()) {
		case OK:
			
			if(pathTextField.getText().isEmpty()){
				JOptionPane.showMessageDialog(GWindowWizard.this, "Give a directory first");
				return;
			}
			
			//First we check that the path has been correctly set
			File directory = new File(pathTextField.getText());
			
			//If the directory does not exist we ask the user if he wants to create it
			if(!directory.exists()) {
				
				//Create the JOptionPane
				Object[] options = {"OK", "Cancel"};
				int choix = JOptionPane.showOptionDialog(GWindowWizard.this, "The directory does not exist. Do you want to create it ?", "Creation Problem", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
				
				//If the choice is to create the directory then we create it otherwise we return
				if(choix == 0) {
					
					//If the creation of the directory fails then we warn the user and return otherwise we just continue
					if(!directory.mkdirs()) {
						JOptionPane.showConfirmDialog(GWindowWizard.this, "Error GWindow Wizard");
						return;
					}
					
				} else {
					return;
				}
			}
			
			//Now that we know that the directory does exist then we check that no KB of the same name already exists
			File kbDirectory = new File(pathTextField.getText() + "\\" + nameTextField.getText());
			
			//If an existing kb with the same name already exists we have to warn the user and ask if he wants to create it or want to abort the creation
			if(kbDirectory.exists()) {
				
				//Create the JOptionPane
				Object[] options = {"Open", "Overwrite"};
				int choix = JOptionPane.showOptionDialog(GWindowWizard.this, "An already existing knowledge base with the same name exists in the specified folder. Do you want to open it or overwrite it?", "Creation Problem", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
				
				//If the choice is to create the directory then we overwrite everything otherwise we return
				if(choix == 0) {
					
					//We use the openKB method of the visualFigaro parent
					vfParent.openKB(kbDirectory);
					
				} else {

					//Just delete everything we will rewrite everything later
					File[] content = kbDirectory.listFiles();
					for(int i=0; i<content.length; i++)
						content[i].delete();
				}
			}
			
			//If everything is ok create the directory
			kbDirectory.mkdir();
			
			//Now we have to copy the default folders
			Vector<String> defaultFolders = new Vector<String>((new GXMLLoaderDefaultFiles()).getDefaultFiles(languageComboBox.getSelectedItem().toString(),"folder"));
			for(String folderpath : defaultFolders) {
				
				String inputFolderPath = "./VisualFigaro/" + folderpath;
				String outputFolderPath = pathTextField.getText() + "\\" + nameTextField.getText() + "\\" + folderpath;
				
				File outputFolder=new File(outputFolderPath);
				File inputFolder=new File(inputFolderPath);
				
				outputFolder.mkdir();
				
				File[] allFiles = inputFolder.listFiles();
								
				for (int i = 0; i < allFiles.length; i++) {
					File file = allFiles[i];
					try {
						CopyFile.copy(file, new File(outputFolder, file.getName()), true);
					} catch (IOException e) {
						System.err.println("Erreur de copie d'un dossier");
					}
				}
			}
			
			//Now we have to copy the default files and the schema
			Vector<String> defaultFiles = new Vector<String>((new GXMLLoaderDefaultFiles()).getDefaultFiles(languageComboBox.getSelectedItem().toString(),"file"));
			
			defaultFiles.add((new GXMLLoaderDefaultFiles()).getSchemaFilenameForLanguage(languageComboBox.getSelectedItem().toString()));
			System.out.println("defaultFiles.size()"+defaultFiles.size());
			for(String filepath : defaultFiles) {
				
				FileChannel inputFile = null;
				FileChannel outputFile = null;
				
				String inputFilePath = "./VisualFigaro/" + filepath;
				String outputFilePath = pathTextField.getText() + "\\" + nameTextField.getText() + "\\" + filepath;
				String outputDirectoryPath = outputFilePath.substring(0, outputFilePath.lastIndexOf("\\")) + "\\";
				
				File outputDirectoryFile = new File(outputDirectoryPath);
				if(!outputDirectoryFile.exists())
					outputDirectoryFile.mkdirs();
				System.err.println("Voici le output file : " + outputDirectoryPath + "......." + outputDirectoryFile.exists());
				
				try {
					
					inputFile = new FileInputStream(inputFilePath).getChannel();
					outputFile = new FileOutputStream(outputFilePath).getChannel();
					
					inputFile.transferTo(0, inputFile.size(), outputFile);
				} catch(Exception e) {
					System.err.println("VisualFigaro : GWindowWizard : Exception during the transfer of file : " + filepath + " : " + e);
				} finally {
					if(inputFile != null) {
						try {
							inputFile.close();
						} catch(IOException e) {
							System.err.println("VisualFigaro : GWindowWizard : Exception while closing the file : " + filepath + " : " + e);
						}
					}
					if(outputFile != null) {
						try {
							outputFile.close();
						} catch(IOException e) {
							System.err.println("VisualFigaro : GWindowWizard : Exception while closing the file : " + filepath + " : " + e);
						}
					}
				}
			}
			
			//The next step is to create the figaro file and fill it with the default text defined in the copiedFiles.xml file
			String figaroFileName = kbDirectory.getAbsolutePath() + "\\" + nameTextField.getText() + ".fi";
			File figaroFile = new File(figaroFileName);
			FileWriter writer = null;
			
			try {
				writer = new FileWriter(figaroFile);
				writer.write(xlDefaultFiles.getDefaultTextForLanguage(languageComboBox.getSelectedItem().toString()));
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
			
			//Finally we open the database in both the text editor and the tree representation
			vfParent.openKB(kbDirectory);
			
			//And we delete the window
			dispose();

			break;
		
		case CANCEL:
			dispose();
			break;
			
		default:
			System.out.println("VisualFigaro : GWindowWizard : Unknown message received");
		}
	}
	
	public Element fillDocument() {
		return null;
	}
	
	public void loadXml(Element e) {
	}
}
