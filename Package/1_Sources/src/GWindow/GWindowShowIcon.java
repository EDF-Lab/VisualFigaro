package GWindow;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.ctreber.aclib.image.ico.BitmapDescriptor;
import com.ctreber.aclib.image.ico.ICOFile;

import GCellRenderer.GLabelListCellRenderer;
import GObject.GObject;
import GObjectInformation.GObjectInformation;

public class GWindowShowIcon extends JDialog {

	private static final long serialVersionUID = 1L;
	
	//The list and the list model used to display the icons
	/**
	 * @uml.property  name="iconList"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JList iconList;
	/**
	 * @uml.property  name="iconListModel"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="javax.swing.JLabel"
	 */
	private DefaultListModel iconListModel;
	
	//The selected icon
	/**
	 * @uml.property  name="selectedIcon"
	 */
	private String selectedIcon;
	
	public GWindowShowIcon(GObject p, GObjectInformation info) {
		
		//First we have to set the layout and initialize the class variables
		this.getContentPane().setLayout(new BorderLayout());
		//Initialize the JList
		this.iconListModel = new DefaultListModel();
		this.iconList = new JList(iconListModel);
		this.iconList.setCellRenderer(new GLabelListCellRenderer());
		//The selected icon is set by default to ""
		this.selectedIcon = "";
		
		//Then we have to open the icon folder and retrieve all the icons name
		File iconFolderFile = new File(info.getKnowledgeBasePath() + "\\icons");
		File[] icons = iconFolderFile.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getAbsolutePath().endsWith(".ico");
			}
		});
		
		//We put the icon in the JList using JLabels
		for(File iconFile : icons) {
			Image iconImage = loadIcon(iconFile);
			JLabel iconLabel = new JLabel(iconFile.getName().substring(0, iconFile.getName().indexOf(".")));
			iconLabel.setIcon(new ImageIcon(iconImage));
			iconListModel.addElement(iconLabel);
			//iconListModel.addElement(iconFile.getAbsoluteFile());
		}
		
		//We add the JList to the content pane
		JScrollPane scrollPane = new JScrollPane(this.iconList);
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		//Now we have to create the ok/cancel panel
		JPanel okCancelPanel = new JPanel(new BorderLayout());
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWindowShowIcon.this.setSelectedIcon();
				GWindowShowIcon.this.dispose();
			}
		});
		okCancelPanel.add(okButton, BorderLayout.WEST);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GWindowShowIcon.this.discardSelectedIcon();
				GWindowShowIcon.this.dispose();
			}
		});
		okCancelPanel.add(cancelButton, BorderLayout.EAST);
		this.getContentPane().add(okCancelPanel, BorderLayout.SOUTH);
		
		this.setTitle("Icon Browser");
		this.pack();
	}
	
	private Image loadIcon(File iconFile) {
		//The image we will try to fill
		Image image;
		
		//We create the file and return null if it does not exist
		if(!iconFile.exists())
			return null;

		//Otherwise we create an icon file and try to fill it with the resource
		ICOFile icon = null;
		try {
			icon = new ICOFile(iconFile.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("VisualFigaro : GWindowDefaultVarNode : Error while reading the icon file : " + iconFile + " ." + e);
		}
		
		//If everything succeeds then we try to extract the picture from the icon 
		BitmapDescriptor bmpdesc = icon.getDescriptor(0);
		image = bmpdesc.getImageRGB();
		
		return image;
	}
	
	public void setSelectedIcon() {
		if(this.iconList.getSelectedIndex() >= 0)
			this.selectedIcon = ((JLabel)this.iconListModel.getElementAt(this.iconList.getSelectedIndex())).getText();
		else
			this.selectedIcon = "";
	}
	
	public void discardSelectedIcon() {
		this.selectedIcon = "";
	}

	/**
	 * @return
	 * @uml.property  name="selectedIcon"
	 */
	public String getSelectedIcon() {
		return this.selectedIcon;
	}
}
