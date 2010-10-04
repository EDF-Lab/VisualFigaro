/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 30 March 2010                                  
 * Author       : D.WEYAND/ALL4TEC                               
 * Bug Id       : n° 3                                           
 * Modification : Optimize graphic/text alignment 
 * VF Version   : 1.4                
 * **************************************************************
 * Date         : 6 April 2010                            
 * Author       : D.WEYAND/ALL4TEC                              
 * Bug Id       :                                         
 * Modification : Optimize graphic/text alignment (tree collapsing)
 *                + Fix bug for initial graphic updating (cell assoc)
 * VF version   : 1.6
 * **************************************************************
 * Date         : 13 April 2010                            
 * Author       : D.WEYAND/ALL4TEC                              
 * Bug Id       :                                         
 * Modification : Code cleanup to avoid warnings
 * VF version   : 1.7
 * **************************************************************
 * Date         : 20 September 2010                            
 * Author       : D.WEYAND/ALL4TEC                              
 * Bug Id       : n°65                                        
 * Modification : Reactivate owner test and put text focus outside
 *                the pos and owner condition
 * VF version   : 1.10
 * **************************************************************/

package figaroInterface;


/*
 * DomEcho.java
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

//For the GUI
import jEditInterface.VisualFigaro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.jEdit;

import GLanguage.GLanguage;

import com.ctreber.aclib.image.ico.BitmapDescriptor;
import com.ctreber.aclib.image.ico.ICOFile;

/**
 * This class display GTree in the classical JTree representation style and make possible the interaction with the cells and the interaction between the JTree and the jEdit view part.
 * @author Guillaume Torrente & Marc Bouissou
 */
public class FigaroTree extends JPanel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @uml.property  name="test"
	 */
	int test = 0;
	
	/**
	 * @uml.property  name="eb"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private EmptyBorder eb;
	/**
	 * @uml.property  name="bb"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private BevelBorder bb;
	/**
	 * @uml.property  name="tree"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JTree tree;
	/**
	 * @uml.property  name="treeView"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JScrollPane treeView;
	/**
	 * @uml.property  name="popup"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private JPopupMenu popup;
	
	
	/*************************\
	      FOR THE COPY BOX
	\*************************/
	static GTree gtree;
	static GExpListener expListener;
	
	static final int windowHeight = 150;
	static final int leftWidth = 100;
	static final int rightWidth = 150;
	static final int windowWidth = leftWidth + rightWidth;
	

	
	//To remember where was the previous position of the cursor to avoid repainting the area twice
	/**
	 * @uml.property  name="previousPosition"
	 */
	int previousPosition = -1;
	
	static final String[] typeName = {
		"none",
		"Element",
		"Attr",
		"Text",
		"CDATA",
		"EntityRef",
		"Entity",
		"ProcInstr",
		"Comment",
		"Document",
		"DocType",
		"DocFragment",
		"Notation"
	};
	
	static final String[] figaroType = {
		
	};
	
	//This help to keep the contact with JEdit
	/**
	 * @uml.property  name="view"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	View view;
	
	//The VisualFigaro plugin
	/**
	 * @uml.property  name="visualFigaro"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="figTree:jEditInterface.VisualFigaro"
	 */
	VisualFigaro visualFigaro;
	
	//Usual constructor
	public FigaroTree(VisualFigaro ep) {
		
		//We save the component in order to interact with it in the future
		this.visualFigaro = ep;
		
		//We save the view in order to keep the contact with JEdit
		this.view = ep.getView();
		
		//Parser initialization
		jEdit.newFile(view);
		eb = new EmptyBorder(5,5,5,5);
		bb = new BevelBorder(BevelBorder.LOWERED);
		CompoundBorder cb = new CompoundBorder(eb, bb);
		this.setBorder(new CompoundBorder(cb, eb));
		
		//XML tree initialization
		gtree = new GTree();
		
		//Listener for the tree modification
		expListener = new GExpListener();
				
		/***************************\
		  INITIALISATION DE L'ARBRE
		\***************************/
		
		//The JTree which will be printed in the plugin
		
		tree = new JTree(new DomToTreeModelAdapter());
		tree.setCellRenderer(new KBTreeCellRenderer(this));
		tree.setRootVisible(false);
		tree.addTreeExpansionListener(expListener);
		tree.setShowsRootHandles(true);
		tree.setEditable(false);
		
		/**************************\
		    POPUP INITIALIZATION
		\**************************/
		
		popup = new JPopupMenu();
		tree.addMouseListener(new MouseListener () {
			public void mousePressed(MouseEvent me) {
				if(me.isPopupTrigger()) {
					popup.show((JComponent)me.getSource(), me.getX(), me.getY());
				}
					
			}
			public void mouseReleased(MouseEvent me) {
				if(me.isPopupTrigger()) {
					
					//First we remove all the item in the previous menu which will be reused
					popup.removeAll();
					
					//We also have to get the path from the root to the leaf
					TreePath path = tree.getPathForLocation(me.getX(), me.getY());
					
					//Then we get the name of the leaf
					String treeTag = ((GCell)path.getLastPathComponent()).getValue(0).toString();
					
					//###mettre le test
					for(JMenu menu : visualFigaro.getLanguage().getCompletionMenu(treeTag))
						popup.add(menu);
					
					//Last test. If there is no element in the popup menu we do not have to show it
					if(popup.getComponentCount() > 0) {
						popup.show((JComponent)me.getSource(), me.getX(), me.getY());
					}
				}
					
			}
			public void mouseEntered(MouseEvent me) {
			}
			public void mouseExited(MouseEvent me) {
			}
			public void mouseClicked(MouseEvent me) {
			}
		});
		
		//Creation of the scrollpane containing the tree
		treeView = new JScrollPane(tree);
		treeView.setPreferredSize(new Dimension(leftWidth, windowHeight));
		
		/**************************\
			FINALIZATION
		\**************************/
		
		this.setLayout(new BorderLayout());
		this.add(treeView);
	}
	
	
	/**
	 * Mouse click handling
	 *
	 */
	public void mouseClicked() {

		//Recherche tout les TYPE
		//printFigaro(view.getEditPane().getTextArea().getText());	
		
		//First we reset all the previous colors existing in the current tree		
		if(previousPosition != -1) {
			gtree.unsetFigaroCell(previousPosition);
			gtree.resetMarqueCell(previousPosition);
		}
			
		//Then we store the path corresponding to the item the user has clicked

		//TreePath path = gtree.findPathToSelected(previousPosition = formatCaretPosition(view.getEditPane().getTextArea().getText(), view.getEditPane().getTextArea().getCaretPosition()));
		TreePath path = gtree.findPathToSelected(previousPosition = view.getEditPane().getTextArea().getCaretPosition());
	
		//We mark the cells in order to update the tree later
		gtree.setFigaroCell(previousPosition);
				
		//Then the tree is updated in order to have the cell visible in the tree
		tree.expandPath(path);
		tree.setSelectionPath(path);
		
		//Center the cell in the scrollpane
		tree.scrollRectToVisible(new Rectangle(treeView.getVisibleRect().x, tree.getRowForPath(path)*(tree.getBounds().height - tree.getVisibleRect().height)/tree.getRowCount(), tree.getVisibleRect().width, tree.getVisibleRect().height));
		
		//Ultimately we repaint the tree
		treeView.repaint();
		
	}
	
	/*private int formatCaretPosition(String text, int caretPosition) {
		
		//First we have to find the encoding used in the text.
		if(text.contains("\n")) {
			
			//If the text use windows encoding we have to remove one character every line separator we find.
			int lineSeparatorCount = -1;
			int fromIndex = 0;
			//boolean nextLineSeparatorExists = false;
			
			do {
				fromIndex = text.indexOf("\n", fromIndex);
				//nextLineSeparatorExists = fromIndex >= 0 && fromIndex < caretPosition;
				lineSeparatorCount++;
				fromIndex++;
			} while(fromIndex<=(caretPosition + lineSeparatorCount));
			//In this case we return the caretPosition plus the number of line separators found
			return caretPosition + lineSeparatorCount;
		}
		
		//If the encoding is the usual one just return the caret position
		return caretPosition;
	}*/
	
	/**
	 * Load the GTree t in the tree
	 * @param t
	 */
	public void loadSpecificGTree(GTree t) {
		
		//If the tree passed as argument is null then we have to create a fake root containing nothing at all and print it.
		if(t == null)
			gtree = new GTree();
		else
			gtree = t;
		
		//Then we have to select the model again and repaint the scrollpane
		tree.setModel(new DomToTreeModelAdapter());
		treeView.repaint();
	}
	
	/**
	 * Erase the GTree and therefore allow the user to restart from an empty skeleton
	 *
	 */
	public void newGTree() {
		gtree = new GTree();
	}
	
	/**
	 * Return all the type contained in the hierarchy
	 * @return
	 */
	public Vector<GCell> getTypes() {
		Vector<GCell> tamponVect = new Vector<GCell>();
		
		//First we test if the root is existing
		if(gtree == null)
			return null;
		
		//Then we check if the root is really the root of a well formed tree
		if(gtree.getRoot() == null)
			return null;
		
		//Finally we test if their is existing children
		if(gtree.getRoot().getChildrenCount() <= 0)
			return null;
		
		for(int i=0; i<gtree.getRoot().getChild(0).getChildrenCount(); i++) {
			if(gtree.getRoot().getChild(0).getChild(i).getValue(0).toString().equals("CLASS"))
				tamponVect.add(gtree.getRoot().getChild(0).getChild(i));
		}
		
		return tamponVect;
	}
	
	/**
	 * Return the entire GTree
	 * @return
	 */
	public GTree getGTree() {
		return gtree;
	}
	
	/**
	 * Allow the user to load the XML contained in variable "path" in a GTree and show it
	 * @param path
	 */
	public void loadTreeFromXML(String path, String fileText, GLanguage language) {
		gtree = new GTree();
		gtree.LoadXmlFile(path, language);
		gtree.numeroter();
		
		printFigaro(fileText);
		
		tree.setModel(new DomToTreeModelAdapter());
		
		treeView.repaint();
	}
	
	/**
	 * Call the GTree function making the traduction from GTree to Figaro langage. It does work properly.
	 *
	 */
	public void printFigaro(String fileText) {
		//Print the Figaro code in jEdit
		//gtree.printFigaro(this.view.getEditPane().getTextArea().getText());
		gtree.printFigaro(fileText);
	}
	
	public VisualFigaro getParent() {
		return this.visualFigaro;
	}
	
	public class DomToTreeModelAdapter implements javax.swing.tree.TreeModel {
		private Vector<TreeModelListener> listenerList = new Vector<TreeModelListener>();

		public Object getRoot() {
			return gtree.getRoot();
		}
		
		public boolean isLeaf(Object aNode) {
			GCell node = (GCell)aNode;
			if(node.getChildrenCount()>0)
				return false;
			return true;
		}
		
		public int getChildCount(Object parent) {
			GCell node = (GCell)parent;
			return node.getChildrenCount();
		}
		
		public Object getChild(Object parent, int index) {
			GCell node = (GCell)parent;
			return node.getChild(index);
		}
		
		public int getIndexOfChild(Object parent, Object child) {
			GCell node = (GCell)parent;
			return node.getChild((GCell)child);
		}
		
		public void valueForPathChanged(TreePath path, Object newValue) {
			
		}
		
		public void addTreeModelListener(TreeModelListener listener) {
			if(listener != null && !listenerList.contains(listener)) {
				listenerList.addElement(listener);
			}
		}
		
		public void removeTreeModelListener(TreeModelListener listener) {
			if(listener != null)
				listenerList.removeElement(listener);
		}
		
		public void fireTreeNodesChanged(TreeModelEvent e) {
			Enumeration<TreeModelListener> listeners = listenerList.elements();
			while(listeners.hasMoreElements()) {
				TreeModelListener listener = (TreeModelListener)listeners.nextElement();
				listener.treeNodesChanged(e);
			}
		}
		
		public void fireTreeNodesInserted(TreeModelEvent e) {
			Enumeration<TreeModelListener> listeners = listenerList.elements();
			while(listeners.hasMoreElements()) {
				TreeModelListener listener = (TreeModelListener)listeners.nextElement();
				listener.treeNodesInserted(e);
			}
		}
		
		public void fireTreeNodesRemoved(TreeModelEvent e) {
			Enumeration<TreeModelListener> listeners = listenerList.elements();
			while(listeners.hasMoreElements()) {
				TreeModelListener listener = (TreeModelListener)listeners.nextElement();
				listener.treeNodesRemoved(e);
			}
		}
		
		public void fireTreeStructureChanged(TreeModelEvent e) {
			Enumeration<TreeModelListener> listeners = listenerList.elements();
			while(listeners.hasMoreElements()) {
				TreeModelListener listener = (TreeModelListener)listeners.nextElement();
				listener.treeStructureChanged(e);
			}
		}
		
		public void fireEditingStopped(TreeModelEvent e) {
		}
	}
	
	/**
	 * @author  nribot
	 */
	public class KBTreeCellRenderer extends DefaultTreeCellRenderer {
		
		private static final long serialVersionUID = 1L;
		
		//The parent
		/**
		 * @uml.property  name="parent"
		 * @uml.associationEnd  
		 */
		FigaroTree parent;
		
		public KBTreeCellRenderer(FigaroTree p) {
			super();
			parent = p;
		}
		
        public Component getTreeCellRendererComponent(JTree tree, Object obj, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			
			if(((GCell)obj).isEquivalentToSelectedFigaro() > 0) {
				setTextSelectionColor(Color.WHITE);
				//200-((GCell)obj).isEquivalentToSelectedFigaro()*50
				setBackgroundSelectionColor(new Color(255-((GCell)obj).isEquivalentToSelectedFigaro()*50, 255-((GCell)obj).isEquivalentToSelectedFigaro()*50, 255));
				setTextNonSelectionColor(Color.WHITE);
				setBackgroundNonSelectionColor(new Color(255-((GCell)obj).isEquivalentToSelectedFigaro()*50, 255-((GCell)obj).isEquivalentToSelectedFigaro()*50, 255));
			} else {
				
				if(((GCell)obj).getMarquage().y > 0) {
					Point p = ((GCell)obj).getMarquage();
					
					setTextSelectionColor(Color.WHITE);
					//200-((GCell)obj).isEquivalentToSelectedFigaro()*50
					setBackgroundSelectionColor(new Color(255, 255-(int)((((float)p.y+1)/((float)p.x+1))*200), 255-(int)((((float)p.y+1)/((float)p.x+1))*200)));
					setTextNonSelectionColor(Color.WHITE);
					setBackgroundNonSelectionColor(new Color(255, 255-(int)((((float)p.y+1)/((float)p.x+1))*200), 255-(int)((((float)p.y+1)/((float)p.x+1))*200)));
					//System.out.println("Voici la marque de la cellule " + ((GCell)obj).toString() + " : " + p.x + " " + p.y);
				} else {
					//###Il faut d'abord recuperer les couleur par defaut les enregistrer et les reutiliser ici
					//###First we have to get back the default colors, save them and reuse them here
					setTextSelectionColor(Color.BLACK);
					setBackgroundSelectionColor(Color.WHITE);
					setTextNonSelectionColor(Color.BLACK);
					setBackgroundNonSelectionColor(Color.WHITE);
				}
			}
			
			if(((GCell)obj).getVirtual()) {
				setTextSelectionColor(Color.GREEN);
				setTextNonSelectionColor(Color.GREEN);
			}
			
			super.getTreeCellRendererComponent(tree, obj, selected, expanded, leaf, row, hasFocus);

			//Useless code
			/*
			if(((GCell)obj).isExpanded()) {
				tree.expandPath(((GCell)obj).findPathToSelected());
				System.err.println("VVVVVVVVVOICE LE PATH : " + ((GCell)obj).findPathToSelected());
			} else {
				tree.collapsePath(((GCell)obj).findPathToSelected());
				System.err.println("VVVVVVVVVOICE LE PATH : " + ((GCell)obj).findPathToSelected());
			}*/
			
			//System.out.println("Voici le nombre ligne dans l'arbre : " + tree.getRowCount() + " et la ligne selectionnee : " + tree.getRowForPath(tree.getSelectionPath()));
			
			if(((GCell)obj).getValue(0) != null) {
				if(((String)((GCell)obj).getValue(0)).equals("CLASS")) {
					if(parent.getParent() == null)
						System.out.println("Le parent DomEcho est null");
					else
						System.out.println("Le parent DomEcho n'est pas null");
					
					System.out.println("VOOICI LE STRING : " + ((String)((GCell)obj).getValue(0)));
					
					if(parent.getParent().getIcon((String)((GCell)obj).getChild(0).getChild(0).getValue(1)) != null) {
						File fichier = new File(parent.getParent().getIcon((String)((GCell)obj).getChild(0).getChild(0).getValue(1)));
						if(fichier.exists()) {
							ICOFile icone = null;
							try {
								icone = new ICOFile(fichier.getAbsolutePath());
							} catch (IOException e) {
								System.err.println("Erreur lors de la lecture du fichier icone : " + fichier + " ." + e);
							}
							
							BitmapDescriptor bmpdesc = icone.getDescriptor(0);
							Image image = bmpdesc.getImageRGB();
							
							if(image == null)
								System.err.println("Visual Figaro : FigaroTree : Error during loading of icon file : " + fichier);
							else {
								Icon icon = new ImageIcon(image);
								setIcon(icon);
							}
						}
					}
				}
			}
			
			return this;
		}
	}
	
	public class GExpListener implements TreeExpansionListener {
		
		public void treeExpanded(TreeExpansionEvent ee) {
			//On indique que la cellule a ete etendue
			//We indicate that the cell has been expended
			((GCell)ee.getPath().getLastPathComponent()).setExpanded();
			view.getEditPane().focusOnTextArea();
			if(!visualFigaro.getCurrentSelectedTree().equals(view.getBuffer().getPath()))
				return;
			
			if(view.getEditPane().getTextArea() != null && ((GCell)ee.getPath().getLastPathComponent()).getValue(0) == "BDC" ) {
				//Nothing in particular
				//printFigaro();
				//gtree.resetAssociations();
			} else {
				//On recherche la cellule qui est la source
				//We search the cell which is the source of this event
				int pos = gtree.getCaretPosFromCell(((GCell)ee.getPath().getLastPathComponent()).getUID());
				if(pos >= 0 && tree.isFocusOwner()) {
					view.getEditPane().getTextArea().setCaretPosition(pos+5);				
				}
				view.getEditPane().focusOnTextArea();
			}
		}
		
		public void treeCollapsed(TreeExpansionEvent ee) {
			//On indique que la cellule a ete retractee
			//We indicate that the cell has been collapsed
			int pos;
			((GCell)ee.getPath().getLastPathComponent()).setCollapsed();

			if(view.getEditPane().getTextArea() != null) {
				pos = gtree.getCaretPosFromCell(((GCell)ee.getPath().getLastPathComponent()).getUID());
				//if(pos >= 0 && tree.isFocusOwner())
				if(pos >= 0 )
					view.getEditPane().getTextArea().setCaretPosition(pos+5);
					view.getEditPane().focusOnTextArea();
			}
				
			if(view.getEditPane().getTextArea() != null && ((GCell)ee.getPath().getLastPathComponent()).getValue(0) == "BDC" ) {
				{
				   //Nothing in particular
				   //view.getEditPane().getTextArea().setText("");
				}
			}
		}
	}
}

