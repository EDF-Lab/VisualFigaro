/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 13 April 2010                            
 * Author       : D.WEYAND/ALL4TEC                              
 * Bug Id       :                                         
 * Modification : Code cleanup to avoid warnings
 * VF version   : 1.7
 * **************************************************************/

package figaroInterface;

/**
 * @author Guillaume Torrente & Marc Bouissou
 */

/*
 * GCell.java
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

import java.util.HashMap;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Map;

import java.awt.Point;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class GCell extends DefaultMutableTreeNode{
	
	private static final long serialVersionUID = 1L;
	
	//La liste des enfants et des parents
	//List of all the children and their parents
	/**
	 * @uml.property  name="parents"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="figaroInterface.GCell"
	 */
	Vector<GCell> parents;
	/**
	 * @uml.property  name="children"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="figaroInterface.GCell"
	 */
	Vector<GCell> children;
	
	//Un vecteur qui permet de stocker les valeurs qui sont attachees au noeud
	//A vector used to store the value attached to the node
	/**
	 * @uml.property  name="value"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	Vector<String> value;
	
	//Un booleen qui indique si le noeud est etendu ou pas au sein de l'arbre
	//A boolean indicating if the node has been expanded or not in the tree
	/**
	 * @uml.property  name="expanded"
	 */
	boolean expanded;
	
	//Le code unique de chaque noeud afin de pouvoir le referencer dans l'arbre
	//The unique identifier of the node in the tree
	/**
	 * @uml.property  name="uID"
	 */
	int UID;
	
	//Indique que le noeud a bien ete traduit en figaro (utile pour les versions ou le figaro est etendu au fur et a mesure du depliage de l'arbre)
	//Indicate that the node has been translated in Figaro (usefull for the version of Visual Figaro where Figaro code is expanded as the tree is expanded)
	/**
	 * @uml.property  name="isFigaro"
	 */
	int isFigaro;
	
	//Permet de materialiser l'arborescence depuis le noeud selectionne jusqu'a sa racine afin d'aider a la visualisation de l'arborescence. Utile pour la coloration de la chaine des ancetres
	//A way to give an idea of the position of the node in the path from a leaf to the root (usefull when we have to print this path in the tree).
	/**
	 * @uml.property  name="marque"
	 */
	int marque;

	//Permet de donner la profondeur du noeud sans evaluer tout l'arbre en remontant ses parents. Utile pour la coloration de la chaine des ancetres
	//The depth of the node. We store this value in order to avoid multiple calculation of this value.
	/**
	 * @uml.property  name="pathProfondeur"
	 */
	int pathProfondeur;

	//Permet d'indiquer si la cellule est virtuelle et donc ne correspond a aucun code figaro. Utile lors de la creation de l'arbre en mode debutant.
	//Indicate that this cell is virtual and therefore does not correspond to any real Figaro node. Usefull when we create the tree in beginner mode (deprecated).
	/**
	 * @uml.property  name="isVirtual"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	boolean isVirtual;
	
	//Permet d'indiquer la hierarchie qui doit se trouver sous chacun des noeuds.
	//Indicate the hierachy we have to find under this node.
	/**
	 * @uml.property  name="hierarchie"
	 * @uml.associationEnd  multiplicity="(0 -1)" ordering="true" elementType="java.lang.String" qualifier="constant:java.lang.String java.util.Vector"
	 */
	@SuppressWarnings("unchecked")
	Map hierarchie; 
	
	@SuppressWarnings("unchecked")
	public GCell() {
		//Initialisation des valeurs de la classe
		//Inialization of all the class members
		parents = new Vector<GCell>(0);
		children = new Vector<GCell>(0);
		
		value = new Vector<String>(0);
		
		expanded = false;
		
		UID = -1;
		
		isFigaro = 0;
		
		marque = 0;
		
		pathProfondeur = 0;
		
		isVirtual = false;
		
		//Initialisation un peu particuliere de la map
		//Specific initialization for the map
		Vector<String> tamponVect = new Vector<String>(0);
		hierarchie = new HashMap<String, Vector<String>>();
		tamponVect.add("CLASS");
		hierarchie.put("BDC", tamponVect.clone());
		tamponVect.clear();
		tamponVect.add("NAME");
		tamponVect.add("ATTRIBUT");
		tamponVect.add("INTERACTION");
		tamponVect.add("OCCURRENCE");
		tamponVect.add("CONSTANT");
		hierarchie.put("CLASS", tamponVect.clone());
		tamponVect.clear();
		tamponVect.add("NAME");
		tamponVect.add("DOMAIN");
		hierarchie.put("CONSTANT", tamponVect.clone());
		tamponVect.clear();
		tamponVect.add("NAME");
		tamponVect.add("CONDITION");
		tamponVect.add("THEN");
		tamponVect.add("STAGE");
		hierarchie.put("INTERACTION", tamponVect.clone());
		tamponVect.clear();
		tamponVect.add("NAME");
		tamponVect.add("ALTERNATIVE");
		hierarchie.put("OCCURRENCE", tamponVect.clone());
	}
	
	@SuppressWarnings("unchecked")
	public GCell(String s) {
		//Initialisation des valeurs de la classe
		//Initialization of all the class members
		parents = new Vector<GCell>(0);
		children = new Vector<GCell>(0);
		
		value = new Vector<String>(0);
		value.add(s);
		
		expanded = false;
		
		UID = -1;
		
		isFigaro = 0;
		
		marque = 0;
		
		pathProfondeur = 0;
		
		isVirtual = false;
		
		//Initialisation un peu particuliere de la map
		//Specific initialization for the map
		Vector<String> tamponVect = new Vector<String>(0);
		hierarchie = new HashMap<String, Vector<String>>();
		tamponVect.add("CLASS");
		hierarchie.put("BDC", tamponVect.clone());
		tamponVect.clear();
		tamponVect.add("NAME");
		tamponVect.add("ATTRIBUT");
		tamponVect.add("INTERACTION");
		tamponVect.add("OCCURRENCE");
		tamponVect.add("CONSTANT");
		hierarchie.put("CLASS", tamponVect.clone());
		tamponVect.clear();
		tamponVect.add("NAME");
		tamponVect.add("DOMAIN");
		hierarchie.put("CONSTANT", tamponVect.clone());
		tamponVect.clear();
		tamponVect.add("NAME");
		tamponVect.add("CONDITION");
		tamponVect.add("THEN");
		tamponVect.add("STAGE");
		hierarchie.put("INTERACTION", tamponVect.clone());
		tamponVect.clear();
		tamponVect.add("NAME");
		tamponVect.add("ALTERNATIVE");
		hierarchie.put("OCCURRENCE", tamponVect.clone());
	}
	
	
	public int addParent(GCell c) {
		
		//On test si le parent n'est pas deja present
		//We test if the parent does not already exist
		if(!parents.contains((GCell)c)) {
			//On ajoute la parent
			//We add the parent
			parents.add(c);
			//On oblige le parent a rajouter l'instance en cours en tant qu'enfant
			//We force the new parent to add the current instance as children
			c.addChild(this);
		}

		return parents.size();
	}
	
	public int addChild(GCell c) {
		
		//On test pour savoir si l'enfant n'est pas deja present
		//We test if the child does not already exist
		if(!children.contains(c)) {
			//On ajoute l'enfant
			//We add the child
			children.add(c);
			//On indique a l'enfant que l'instance en cours est un de ces parents
			//We force the new child to add the current instance as parent
			c.addParent(this);
		}

		return children.size();
	}
	
	public int addChild(GCell c, int pos) {
		
		//On test pour savoir si l'enfant n'est pas deja present
		//We test if the child does not already exist
		if(!children.contains(c)) {
			//On ajoute l'enfant a la position desiree
			//We add the child at the desired position
			children.add(pos, c);
			//On indique a l'enfant que l'instance en cours est un de ces parents
			//We force the new child to add the current instance as parent
			c.addParent(this);
		}

		return children.size();
	}
	
	public int remParents(GCell c) {
		if(parents.contains(c)) {
			parents.remove(c);
			//c.remChild(this);
		}
		
		return parents.indexOf(c);
	}
	
	public GCell remParents(int index) {
		GCell c = null;
		
		if(index < parents.size()) {
			c=parents.get(index);
			c.remChild(this);
			parents.remove(index);
		}
		
		return c; 
	}
	public int remChild(GCell c) {
		if(children.contains(c)) {
			children.remove(c);
			//c.remParents(this);
		}
		
		return children.indexOf(c);
	}
	
	public GCell remChild(int index) {
		GCell c = null;
		
		if(index < children.size()) {
			c=children.get(index);
			c.remParents(this);
			children.remove(index);
		}
		
		return c;
	}
	
	public int addValue(Object val) {
		if(value == null)
			return -1;
		value.add((String)val);
		return value.size();
	}
	
	public int setValue(int pos, Object val) {
		if(value.size() <= pos)
			return -1;
		value.set(pos, (String)val);
		return value.size();
	}
	
	public void modifyValue(int pos, Object newVal) {
		if(value.size() <= pos)
			return;
		value.set(pos, (String)newVal);
	}
	
	public int getParentsCount() {
		return parents.size();
	}
	
	public int getChildrenCount() {
		return children.size();
	}
	
	public GCell getParent(int index) {
		return parents.get(index);
	}
	
	public int getParent(GCell c) {
		for(int i=0; i<parents.size();i++)
			if(parents.get(i) == c)
				return i;
		
		return -1;
	}
	
	public int getChild(GCell c) {
		for(int i=0; i<children.size();i++)
			if(children.get(i) == c)
				return i;
		
		return -1;
	}
	
	public GCell getChild(int index) {
		if(index < children.size())
			return children.get(index);
		
		return null;
	}
	
	public Vector<String> getValue() {
		if(isVirtual)
			return null;
		else
			return value;
	}
	
	public Object getValue(int index) {
		if(isVirtual)
			return null;
		else
			if(index < value.size())
				return value.get(index);
			else
				return null;
	}
	
	public int getDepth() {
		if(children.size() == 0)
			return 0;
		else {
			int max = 0, maxPrim = 0;
			for(int i=0; i<children.size(); i++) {
				maxPrim = children.get(i).getDepth();
				if(maxPrim > max)
					max = maxPrim;
			}
			return max+1;
		}
	}
	
	/**
	 * @return
	 * @uml.property  name="uID"
	 */
	public int getUID() {
		return UID;
	}
	
	public boolean getVirtual() {
		return isVirtual;
	}
	
	public boolean setVirtual(boolean v) {
		isVirtual = v;
		return isVirtual;
	}
	
	public int isEquivalentToSelectedFigaro() {
		return isFigaro;
	}
	
	@SuppressWarnings("unchecked")
	public boolean LoadXmlSubPart(org.w3c.dom.Node node) {
		String strN = "", strV = "";
		//On charge les arguments qui nous interesse
		//We load the arguments in which we have interest
		value.add(node.getNodeName());
		value.add(node.getNodeValue());
		
		//On cree un vecteur pour tester si l'ensemble des fils sont presents pour le mode debutant
		//We create a vector for testing if all the sons are used for the beginner mode
		//boolean present = false;
		boolean present = true;
		Vector<String> filsDevantEtrePresent = (Vector<String>)hierarchie.get(node.getNodeName());
		Vector<Boolean> filsPresent;
		if(filsDevantEtrePresent != null && !isVirtual) {
			filsPresent = new Vector<Boolean>(filsDevantEtrePresent.size());
			for(int i=0; i<filsDevantEtrePresent.size(); i++) {
				for(int j=0; j<node.getChildNodes().getLength(); j++)
					if(node.getChildNodes().item(j).getNodeName() == filsDevantEtrePresent.get(i)) {
						present = true;
						break;
					}
				filsPresent.add(present);
				//present = false;
				present = true;
			}
				
				
		}
		else
			filsPresent = null;
		
		
		//On va tester chaque fils
		//We test all the sons
		for(int i=0; i<node.getChildNodes().getLength(); i++) {
			strN = node.getChildNodes().item(i).getNodeName();
			strV = node.getChildNodes().item(i).getNodeValue();
			
			//#############################################
			//ICI ON PEUT FAIRE LA SELECTION (SEULEMENT DU FILTRAGE) DES NOEUDS A FAIRE ENTRER DANS LA MODELISATION
			//HERE WE CAN MAKE A SELECTION (ONLY FILTERING) OF THE NODES THAT WILL BE USED TO CREATE THE TREE
			//#############################################
			
			if(!((strN == null ? true : strN == "#text") && (strV == null ? true : strV.indexOf("\n") == 0))) {
				//On cree une nouvelle cellule
				//We create the new cell
				GCell child = new GCell();
				
				//On cree l'arborescence qu'il y a en dessous
				//We create the hierachy under it
				child.LoadXmlSubPart(node.getChildNodes().item(i));						
				
				//On l'ajoute aux enfants
				//We add it to the children
				addChild(child);
			}			
		}
		
		if(filsPresent != null)
			if(filsPresent.contains(false)) {
				for(int i=0; i<filsDevantEtrePresent.size(); i++)
					if(!filsPresent.get(i)) {
						//On cree une nouvelle cellule
						//We create the new cell
						GCell child = new GCell(filsDevantEtrePresent.get(i));
						
						//On indique que la cellule est virtuelle
						//We indicate that the cell is virtual
						child.setVirtual(true);
						
						//On l'ajoute aux enfants
						//We add it to the children
						addChild(child);
					}
					
			
		}
		
		return true;
	}
	
	public String toString() {
		String output = /*"" + UID + */" ", buffer = "";
		int pos;
		
		//On concatene toutes les valeurs
		//Concatenation of all the values
		for(int i=0; i<value.size(); i++)
			if(value.get(i) != null) {
				buffer = value.get(i).toString();
				if(buffer != "#text") {
					pos = buffer.indexOf("\n");
					output += buffer.substring(0, pos > 0 ? pos : buffer.length());
				}
			}
		
		//Le cas specifique des objets qui ont des noms
		//The special case for the objects having a name
		if(children.size() > 0)
			if(children.get(0).getValue(0) == "NAME") {
				output += " " + children.get(0).getChild(0).getValue(1);
			}
		
		return output;
	}
	
	public String print(int indent) {
		String buffer = "";
		
		//On decale
		//Indentation
		for(int j=0; j<indent; j++)
			buffer += "	";
		
		buffer += (value.size() > 0 ? value.get(0) : "######") + " : " + (value.size() > 1 ? value.get(1) : "#####") + "\n";
		
		for(int i=0; i<children.size(); i++)
			buffer += children.get(i).print(indent+1);
		
		return buffer;
	}
	
	public String printFigaro(boolean father, ArrayList<Integer> al) {
		String preBuffer = "";
		String postBuffer = "";
		String buffer = "";
		boolean treatChildren = true;
		
		//System.out.println("Voici la cellule " + value.get(0) + " et ce qu'elle retourne : " + expanded);
		
		/*if(!(expanded || father) ) {
			return "";
		}*/
		
		if(isVirtual)
			return buffer;
		
		if(value.get(0) != null) {
			
			//Cas du noeud BDC
			if(value.get(0).toString() == "BDC") {
				//preBuffer = "BDC\n";
			}
			
			//Cas du noeud STAGES
			if(value.get(0).toString() == "STAGES") {
				preBuffer += "ORDRE_DES_ETAPES\n";
				postBuffer += "\n";
			}
			
			//Cas du noeud STAGE
			if(value.get(0).toString() == "STAGE") {
				if(!children.get(0).getChild(0).getValue(1).toString().equals("__ARBRE__")) {
					preBuffer += "	" + ((children.get(0).getChildrenCount() > 0) ? children.get(0).getChild(0).getValue(1) : children.get(0).getValue(1));
					postBuffer += " ;\n";
				}
			}
			
			if(value.get(0).toString() == "EFFECT") {
				preBuffer += "EFFET " + children.get(0).getChild(0).getValue(1);
				postBuffer += ";\n";
			}
			
			//Cas du noeud CLASS
			if(value.get(0).toString() == "CLASS") {
				
				//Si il s'agit de la classe FIGARO on ne l'affiche pas
				if(!children.get(0).getChild(0).getValue(1).toString().equals("FIGARO")) {
				
					preBuffer += "\nTYPE " + children.get(0).getChild(0).getValue(1);
					String intermediateBuffer = "";
					
					if(children.size()>1) {
						intermediateBuffer += ((children.get(1).getValue(0) == "FATHER" && !children.get(1).getChild(0).getValue(1).toString().equals("FIGARO")) ? " SORTE_DE " + children.get(1).getChild(0).getValue(1).toString() : "") + " ;\n";
						buffer += preBuffer + intermediateBuffer;
						
						for(int i=0; i<preBuffer.length(); i++)
							al.add(UID);
						for(int i=0; i<intermediateBuffer.length(); i++)
							al.add(children.get(1).getUID());
						
						for(int i=((children.get(1).getValue(0) == "FATHER") ? 2 : 1); i<children.size();i++)
							buffer += children.get(i).printFigaro(expanded, al);
						
						postBuffer = "\n";
					} else {
						buffer += preBuffer;
						for(int i=0; i<preBuffer.length(); i++)
							al.add(UID);
						postBuffer = " ;\n";
					}
					
					buffer += postBuffer;
					
					for(int i=0; i<postBuffer.length(); i++)
						al.add(UID);
				}
				
				treatChildren = false;
			}
			
			//Cas du noeud
			
			//Cas du noeud CONSTANT
			if(value.get(0).toString() == "CONSTANT") {
				preBuffer += "CONSTANTE " + children.get(0).getChild(0).getValue(1);
				postBuffer += ";\n";
			}
			
			//Cas du noeud OCCURRENCE
			if(value.get(0).toString() == "OCCURRENCE") {
				preBuffer += "OCCURRENCE " + children.get(0).getChild(0).getValue(1) + "\n";
				
				postBuffer += "";
			}
			
			if(value.get(0).toString() == "ALTERNATIVE" && expanded) {
				String bufferIntermediaire = "";
				preBuffer += "	IL_PEUT_SE_PRODUIRE\n";
				for(int i=0; i<21; i++)
					al.add(UID);
				buffer += preBuffer;
				
				for(int i=0; i<children.size(); i++)
					if(children.get(i).getValue(0).toString() == "TYPE") {
						for(int j=0; j<2; j++)
							al.add(UID);
						buffer += "		" + children.get(i).printFigaro(expanded, al);
						break;
					}
				
				bufferIntermediaire = " " + children.get(0).getChild(0).getValue(1).toString() + "\n";
				for(int j=0; j<bufferIntermediaire.length(); j++)
					al.add(UID);
				buffer += bufferIntermediaire;
				
				for(int i=0; i<children.size(); i++)
					if(children.get(i).getValue(0).toString() == "DIST") {
						for(int j=0; j<6; j++)
							al.add(UID);
						buffer += "		LOI " + children.get(i).printFigaro(expanded, al);
						break;
					}
				
				for(int i=0; i<children.size(); i++)
					if(children.get(i).getValue(0).toString() == "PARAMETER") {
						for(int j=0; j<2; j++)
							al.add(UID);
						buffer += " (" + children.get(i).printFigaro(expanded, al) + ")\n";
						for(int j=0; j<2; j++)
							al.add(UID);
						break;
					}
				
				for(int i=0; i<children.size(); i++)
					if(children.get(i).getValue(0).toString() == "INDUCING") {
						for(int j=0; j<11; j++)
							al.add(UID);
						buffer += "		PROVOQUE " + children.get(i).printFigaro(expanded, al) + "\n";
						al.add(UID);
						break;
					}
				
				buffer += ";\n";
				for(int j=0; j<2; j++)
					al.add(UID);
				
				treatChildren = false;
			}

			if(value.get(0).toString() == "DIST") {
				preBuffer += children.get(0).getValue(1);
				postBuffer = "";
			}
			
			if(value.get(0).toString() == "TYPE") {
				preBuffer += children.get(0).getValue(1);
				postBuffer = "";
			}
			
			if(value.get(0).toString() == "PARAMETER") {
				preBuffer += children.get(0).getValue(1);
				postBuffer = "";
			}
			
			if(value.get(0).toString() == "INDUCING") {
				preBuffer += children.get(0).getValue(1);
				postBuffer = "";
			}
			
			
			//Cas du noeud INTERACTION
			if(value.get(0).toString() == "INTERACTION") {
				int pos=0;
				String bufferIntermediaire = "";
				preBuffer += "INTERACTION " + children.get(0).getChild(0).getValue(1) + "\n";
				for(int i=0; i<preBuffer.length(); i++)
					al.add(UID);
				buffer += preBuffer;
				
				//Tout d'abord on doit recuperer l'etape dans laquelle elle est execute ###vois si on peut avoir plusieurs etapes qui appellent une regle
				for(int i=0; i<children.size(); i++)
					if(children.get(i).getValue(0).toString() == "STAGE") {
						bufferIntermediaire += "	ETAPE " + children.get(i).getChild(0).getValue(1) + "\n";
						for(int j=0; j<bufferIntermediaire.length(); j++)
							al.add(children.get(i).getUID());
						buffer += bufferIntermediaire;
						pos = i;
						break;
					}
				
				for(int i=0; i<children.size(); i++)
					if(i != pos)
						buffer += children.get(i).printFigaro(expanded, al);				
				
				postBuffer += ";\n";
				for(int i=0; i<postBuffer.length(); i++)
					al.add(UID);
				buffer += postBuffer;
				
				treatChildren = false;
			}
			
			//Cas du noeud FATHER
			if(value.get(0).toString() == "FATHER") {
				preBuffer += " SORTE_DE " + children.get(0).getValue(1);
				postBuffer += "\n";
			}
			
			//Cas du noeud INTERFACE
			if(value.get(0).toString() == "INTERFACE") {
				preBuffer += "INTERFACE " + children.get(0).getChild(0).getValue(1);
				postBuffer += ";\n";
			}
			
			//Cas du noeud KIND
			if(value.get(0).toString() == "KIND") {
				preBuffer += " GENRE " + children.get(0).getValue(1);
				postBuffer += "";
			}
			
			//Cas du noeud CARDINALITY_MIN
			if(value.get(0).toString() == "CARDINALITY_MIN") {
				preBuffer += " CARDINAL " + children.get(0).getValue(1);
				postBuffer += "";
			}
			
			//Cas du noeud CARDINALITY_MAX
			if(value.get(0).toString() == "CARDINALITY_MAX") {
				preBuffer += " JUSQUA " + children.get(0).getValue(1);
				postBuffer += "";
			}
			
			//Cas du noeud CONDITION
			if(value.get(0).toString() == "CONDITION") {
				preBuffer += "	SI " + children.get(0).getValue(1);
				postBuffer += "\n";
			}
			
			//Cas du noeud THEN
			if(value.get(0).toString() == "THEN") {
				preBuffer += "	ALORS " + children.get(0).getValue(1);
				postBuffer += "\n";
			}
			
			//Cas du noeud attribut
			if(value.get(0).toString() == "ATTRIBUT") {
				preBuffer += "ATTRIBUT " + children.get(0).getChild(0).getValue(1);
				postBuffer += ";\n";
			}
			
			//Cas du noeud DOMAIN
			if(value.get(0).toString() == "DOMAIN" && expanded) {
				preBuffer += " DOMAINE ";
				for(int i=0; i<9; i++)
					al.add(UID);
				buffer += preBuffer;
				
				for(int i=0; i<children.size(); i++)
					if(children.get(i).getValue(0) == "TYPE") {
						if(children.get(i).getChild(0).getValue(1).toString().equals("REEL")) {
							buffer += "REEL";
							for(int j=0; j<4; j++)
								al.add(children.get(i).getUID());
						} else {
							for(int j=0; j<children.size(); j++)
								if(i!=j) {
									al.add(UID);
									al.add(UID);
									buffer += " '" + children.get(j).printFigaro(expanded, al) + "'";
									al.add(UID);
								}
						}
						break;
					}
				postBuffer += "";
				treatChildren = false;
			}
			
			//Cas du noeud ENUM_VALUE
			if(value.get(0).toString() == "ENUM_VALUE") {
				preBuffer += children.get(0).getValue(1).toString();
			}
		
			//Le traitement general
			
			//On se reserve l'option de ne pas traiter les enfants en tant qu'enfants mais au sein du if precedent.
			//Dans ce cas il faut mettre treatChildren a false
			if(treatChildren) {
				for(int i=0; i<preBuffer.length(); i++)
					al.add(UID);
				buffer = preBuffer;
				for(int i=0; i<children.size();i++)
					buffer += children.get(i).printFigaro(expanded, al);
				buffer += postBuffer;
				for(int i=0; i<postBuffer.length(); i++)
					al.add(UID);
			}
			
			
				
		} else {
			if(value.get(1) != null) {
				
			} else {
				
			}
		}
		
		return buffer;
	}
	
	public void setExpanded() {
		expanded = true;
	}
	
	public void setCollapsed() {
		expanded = false;
	}
	
	/**
	 * @return
	 * @uml.property  name="expanded"
	 */
	public boolean isExpanded() {
		return expanded;
	}
	
	public int numeroter(int ID, ArrayList<GCell> al) {
		//On s'approprie l'ID
		UID = ID;
		
		al.add(this);
		
		//Cas d'une feuille
		if(children.size()<=0)
			return UID+1;
		
		//Cas general
		int last=children.get(0).numeroter(UID+1, al);
		
		for(int i=1; i<children.size(); i++)
				last=children.get(i).numeroter(last, al);
		
		//Pour chacun des fils on va lui assigner un nouvel ID
		return last;
	}
	
	/**
	 * Permet d'indiquer qu'un cellule a ete selectionnee et donc qu'elle doit apparaitre en bleu tres fonce.
	 * La fonction gere aussi le degrade de bleu qui doit s'en suivre jusqu'a revenir a l'etat initial.
	 * @param s
	 * @param reset
	 */
	public void setFigaroSelection(boolean s, boolean reset) {
		isFigaro = s ? 2 : (isFigaro-1 > 0 ? isFigaro-1 : 0);
		if(reset)
			for(int i=0; i<children.size(); i++)
				children.get(i).setFigaroSelection(s, true);
	}
	
	/**
	 * Permet de retrouver le chemin qui mene de la racine a la cellule en question. L'argument de retour est un type specifique aux arbre pour decrire les chemins.
	 * @return TreePath
	 */
	public TreePath findPathToSelected() {
		if(parents.size() == 0)
			return new TreePath(this);
		else
			return (parents.firstElement()).findPathToSelected().pathByAddingChild(this);
	}
	
	/**
	 * Permet de gerer l'affichage des tous les parents de la cellule en rouge avec un degrade qui se fonce plus on approche de la racine.
	 * @param m
	 * @return
	 */
	public int marqueParent(int m) {
		
		marque = m+1;
		
		if(parents.size() > 0)
			pathProfondeur = parents.get(0).marqueParent(marque);
		else
			pathProfondeur = marque;
		
		return pathProfondeur;
	}
	
	

	/**
	 * Permet de remettre a zero toutes les marques qui ont put etre assignees au parents de la cellule pour le marquage en rouge des parents. Justement afin de ne plus les afficher de cette couleur. 
	 *
	 */
	public void resetMarque() {
		marque = 0;
		if(parents.size() > 0)
			parents.get(0).resetMarque();
	}

	/**
	 * Permet de remettre a zero le calcul de la profondeur d'un noeud afin de traiter d'eventuels changements de structure.
	 *
	 */
	public void resetProfondeur() {
		pathProfondeur = 0;
		if(parents.size() > 0)
			parents.get(0).resetProfondeur();
	}

	/**
	 * Retourne un structure Point qui contient comme premier membre la profondeur du chemin et en second membre la position de la cellule sur ce chemin. La numerotation commence a partir du bas puis va croissante vers la racine.
	 * @return
	 */
	public Point getMarquage() {
		//System.out.println("Voici la point en question : (" + pathProfondeur + "," + marque + ")");
		return new Point(pathProfondeur, marque);
	}
}


