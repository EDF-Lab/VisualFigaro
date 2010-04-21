package GWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileFilter;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import GObject.GObject;
import GObjectInformation.GObjectInformation;

public class GWindowNameAsker extends JDialog {

	private static final long serialVersionUID = 1L;
	
	//The icon name
	/**
	 * @uml.property  name="iconName"
	 */
	private String iconName;
	
	//The textfield for the name
	/**
	 * @uml.property  name="nameTextField"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JTextField nameTextField;
	
	//The name validity label
	/**
	 * @uml.property  name="nameValidityLabel"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JLabel nameValidityLabel;
	
	//The two buttons
	/**
	 * @uml.property  name="okButton"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JButton okButton;
	/**
	 * @uml.property  name="cancelButton"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JButton cancelButton;
	
	//THe information about the knowledge base
	/**
	 * @uml.property  name="information"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GObjectInformation information;
	
	public GWindowNameAsker(GObject p, GObjectInformation info, String path) {
		
		//First we initialize the iconName and the information variable
		iconName = "";
		information = info;
		//Then we set the layout
		this.getContentPane().setLayout(new BorderLayout());
		
		
		//Now we have to create the window
		
		//The first element is the textfield
		nameTextField = new JTextField();
		nameTextField.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				GWindowNameAsker.this.checkIconName();
			}
			public void keyReleased(KeyEvent e) {
				GWindowNameAsker.this.checkIconName();
			}
			public void keyTyped(KeyEvent e) {
				GWindowNameAsker.this.checkIconName();
			}
		});
		nameTextField.setText(path.substring(path.lastIndexOf("\\")+1, path.indexOf(".")));
		this.getContentPane().add(nameTextField, BorderLayout.NORTH);
		
		//The second is the label saying if the name is valid or not
		nameValidityLabel = new JLabel();
		nameValidityLabel.setForeground(Color.green);
		nameValidityLabel.setText("Valid Name");
		this.getContentPane().add(nameValidityLabel, BorderLayout.CENTER);
		
		//Now we create the ok/cancel panel
		JPanel okCancelPanel = new JPanel(new BorderLayout());
		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWindowNameAsker.this.copyIcon();
			}
		});
		okCancelPanel.add(okButton, BorderLayout.WEST);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWindowNameAsker.this.discardIconName();
			}
		});
		okCancelPanel.add(cancelButton, BorderLayout.EAST);
		this.getContentPane().add(okCancelPanel, BorderLayout.SOUTH);
		
		this.setTitle("Icon Name");
		this.pack();
		
		//Last of the last we check the name
		this.checkIconName();
	}

	/**
	 * @return  the iconName
	 * @uml.property  name="iconName"
	 */
	public String getIconName() {
		return iconName;
	}

	public void copyIcon() {
		
		if(this.checkIconName()) {
			this.iconName = nameTextField.getText();
			this.dispose();
		}
	}
	
	public void discardIconName() {
		
		//Set the icon to "" and dispose the window
		this.iconName = "";
		this.dispose();
	}
	
	public boolean checkIconName() {
		
		//First we list all the icon in the icons folder
		File iconFolder = new File(information.getKnowledgeBasePath() + "\\icons");
		File[] iconFiles = iconFolder.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getAbsolutePath().endsWith(".ico");
			}
		});
		
		//Then we retriever their name
		Vector<String> iconsName = new Vector<String>();
		for(File file : iconFiles)
			iconsName.add(file.getName().substring(0, file.getName().indexOf(".")));
		
		//Now we have to check whether the name written in the textfield is already in the folder
		if(iconsName.contains(nameTextField.getText())) {
			nameValidityLabel.setForeground(Color.red);
			nameValidityLabel.setText("Unvalid Name");
			okButton.setEnabled(false);
			return false;
		} else {
			nameValidityLabel.setForeground(Color.green);
			nameValidityLabel.setText("Valid Name");
			okButton.setEnabled(true);
			return true;
		}
	}
}
