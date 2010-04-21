package figaroInterface;

/**
 * @author Guillaume Torrente & Marc Bouissou
 */

/*
 * GTree.java
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

import java.util.*;

//For the document
//import org.pvv.bcd.instrument.JTree.Instrumenter;
import org.w3c.dom.Document;

import GLanguage.GLanguage;
import GXMLLoader.GXMLLoader;

import figaroParser.*;

//For the tree
import javax.swing.tree.TreePath;;

public class GTree {
	/**
	 * @uml.property  name="root"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GCell root;
	/**
	 * @uml.property  name="document"
	 * @uml.associationEnd  
	 */
	private Document document;
	/**
	 * @uml.property  name="allCells"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="figaroInterface.GCell"
	 */
	private ArrayList<GCell> allCells;
	/**
	 * @uml.property  name="allCellsAssociations"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.Integer"
	 */
	private ArrayList<Integer> allCellsAssociations;
	/**
	 * @uml.property  name="figParser"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="gtree:figaroParser.FigaroParser"
	 */
	private FigaroParser figParser;
	/**
	 * @uml.property  name="language"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private GLanguage language;
	
	/**
	 * Constructeur par defaut
	 *
	 */
	public GTree() {
		root = new GCell();
		allCells = new ArrayList<GCell>();
		allCellsAssociations = new ArrayList<Integer>();
		this.figParser = new FigaroParser();
		this.language = new GLanguage();
	}
	
	/**
	 * Constructeur ...
	 * @param r
	 * @param d
	 * @param ac
	 * @param aca
	 */
	public GTree(GCell r, Document d, ArrayList<GCell> ac, ArrayList<Integer> aca) {
		root = r;
		document =d;
		allCells = ac;
		allCellsAssociations = aca;
		this.figParser = new FigaroParser();
		this.language = new GLanguage();
	}
	
	/**
	 * Permet de recuperer le document associe a l'arbre
	 * @return
	 * @uml.property  name="document"
	 */
	public Document getDocument() {
		return document;
	}
	
	/**
	 * Permet de recuperer l'ensemble des cellules qui constitues l'arbre
	 * @return
	 */
	public ArrayList<GCell> getAllCells() {
		return allCells;
	}
	
	public ArrayList<Integer> getAllCellsAssociations() {
		return allCellsAssociations;
	}
	
	/**
	 * Permet de recuperer la profondeur de l'arbre
	 * @return
	 */
	public int getDepth() {
		return root.getDepth();
	}
	
	/**
	 * Permet de charger un document XML contenu dans "path" afin que l'arbre le represente
	 * @param path
	 */
	public void LoadXmlFile(String path, GLanguage language) {
		
		this.language = language;
		
		GXMLLoader xl = new GXMLLoader(null);
		document = xl.loadXmlFile(path);
		
		if(document == null) {
			System.err.println("Le document a mal ete charge");
			return;			
		}
		
		root.LoadXmlSubPart(document);
		
		this.rearrangeTreeFigaro();
	}
	
	/**
	 * Ordonne l'affichage de la racine de l'arbre
	 */
	public String toString() {
		//Foireux
		return root.print(0);
	}
	
	/**
	 * Idem que toString...
	 * @return A string containing the representation of the GTree
	 */
	public String print() {
		return root.print(0);
	}
	
	/**
	 * Permet de recuperer la racine de l'arbre
	 * @return  The GCell
	 * @uml.property  name="root"
	 */
	public GCell getRoot() {
		return root;
	}
	
	/**
	 * @return  the language
	 * @uml.property  name="language"
	 */
	public GLanguage getLanguage() {
		return language;
	}

	/**
	 * @param language  the language to set
	 * @uml.property  name="language"
	 */
	public void setLanguage(GLanguage language) {
		this.language = language;
	}

	/**
	 * Permet de recuperer la position du curseur au sein de code figaro genere. La fonction n'est plus garantie.
	 */
	public int getCaretPosFromCell(int uid) {
		int pos = -1;
		for(int i=0; i<allCellsAssociations.size(); i++)
			if(allCellsAssociations.get(i) == uid) {
				pos = i;
				break;
			}
		return pos;
	}
	
	/**
	 * Permet de reaaranger l'arbre de maniere a ce qu'il epouse la forme classique d'un code Figaro
	 *
	 */
	public void rearrangeTreeFigaro() {
		
		int bdcPos=0;
		
		//On recherche le fils nomme BDC
		for(bdcPos=0; bdcPos<root.getChildrenCount(); bdcPos++)
			if(root.getChild(bdcPos).getValue(0).toString() == "BDC")
				break;
		
		//On va supprimer le type figaro
		for(int figPos=0; figPos<root.getChild(bdcPos).getChildrenCount(); figPos++) {
			if(root.getChild(bdcPos).getChild(figPos).getChildrenCount() > 0)
				if(root.getChild(bdcPos).getChild(figPos).getChild(0).getChildrenCount() > 0)
					if(root.getChild(bdcPos).getChild(figPos).getChild(0).getChild(0).getValue(1).toString().equals("FIGARO")) {
						root.getChild(bdcPos).remChild(figPos);
					}
				
		}
		
		//Ensuite on cree le noeud etape
		GCell etape = new GCell();
		etape.addValue("STEP_ORDER");
		etape.addValue(null);
		
		//On place le noeud etape sous bdc
		root.getChild(bdcPos).addChild(etape, 0);
		
		//Puis on deplace tous les noeuds stage
		for(int stagePos=0; stagePos<root.getChild(bdcPos).getChildrenCount(); stagePos++) {
			if((root.getChild(bdcPos).getChild(stagePos).getValue(0) == null ? "" : root.getChild(bdcPos).getChild(stagePos).getValue(0)).toString() == "STAGE") {
				root.getChild(bdcPos).getChild(stagePos).setValue(0, "STEP");
				etape.addChild(root.getChild(bdcPos).getChild(stagePos));
				root.getChild(bdcPos).remChild(root.getChild(bdcPos).getChild(stagePos));
				//On revient d'un cran en arriere
				stagePos--;
			}
		}
		
		//Ensuite on cree le noeud group
		GCell group = new GCell();
		group.addValue("GROUP_NAMES");
		group.addValue(null);
		
		
		//On place le noeud group sous bdc
		root.getChild(bdcPos).addChild(group, 0);
		
		//Puis on deplace tous les noeuds stage
		for(int stagePos=0; stagePos<root.getChild(bdcPos).getChildrenCount(); stagePos++) {
			if((root.getChild(bdcPos).getChild(stagePos).getValue(0) == null ? "" : root.getChild(bdcPos).getChild(stagePos).getValue(0)).toString() == "GROUP") {
				group.addChild(root.getChild(bdcPos).getChild(stagePos));
				root.getChild(bdcPos).remChild(root.getChild(bdcPos).getChild(stagePos));
				//On revient d'un cran en arriere
				stagePos--;
			}
		}
	
	
		//Ensuite on cree le noeud systemEq
		GCell systemEq = new GCell();
		systemEq.addValue("SYSTEM_NAMES");
		systemEq.addValue(null);
		
		//On place le noeud systemEq sous bdc
		root.getChild(bdcPos).addChild(systemEq, 0);
		
		//Puis on deplace tous les noeuds stage
		for(int stagePos=0; stagePos<root.getChild(bdcPos).getChildrenCount(); stagePos++) {
			if((root.getChild(bdcPos).getChild(stagePos).getValue(0) == null ? "" : root.getChild(bdcPos).getChild(stagePos).getValue(0)).toString() == "SYSTEM") {
				systemEq.addChild(root.getChild(bdcPos).getChild(stagePos));
				root.getChild(bdcPos).remChild(root.getChild(bdcPos).getChild(stagePos));
				//On revient d'un cran en arriere
				stagePos--;
			}
		}
	}
	
	public String printFigaro(String s) {
		allCellsAssociations.clear();
		this.allCellsAssociations = figParser.parseCode(s, this, allCells, allCellsAssociations, language);
		//String fig = root.printFigaro(true, allCellsAssociations);
		return "";
	}
	
	public void resetAssociations() {
		allCellsAssociations.clear();
	}
	
	public ArrayList<GCell> numeroter() {
		allCells = new ArrayList<GCell>();
		root.numeroter(0, allCells);
		return allCells;
	}
	
	public GCell setFigaroCell(int pos) {
		if(pos >= allCellsAssociations.size())
			return null;
		System.err.println("Voila la cellule : " + (Integer)allCellsAssociations.get(pos) + " qui est en fait " + ((GCell)allCells.get(((Integer)allCellsAssociations.get(pos)))).toString());
		((GCell)allCells.get((Integer)allCellsAssociations.get(pos))).setFigaroSelection(true, false);
	
		return (GCell)allCells.get((Integer)allCellsAssociations.get(pos));
	}
	
	public void unsetFigaroCell(int pos) {
		root.setFigaroSelection(false, true);
	}
	
	public TreePath findPathToSelected(int pos) {
		if(pos >= allCellsAssociations.size())
			return null;
		else {
			//System.err.println("Voila le allCellsAssociations : " + (Integer)allCellsAssociations.get(pos));
			return (allCells.get((Integer)allCellsAssociations.get(pos))).findPathToSelected();
		}
	}
	
	public void marquerCell(int pos) {
		if(pos < allCellsAssociations.size())
			allCells.get((Integer)allCellsAssociations.get(pos)).marqueParent(0);
	}
	
	public void resetMarqueCell(int pos) {
		if(pos < allCellsAssociations.size())
			allCells.get((Integer)allCellsAssociations.get(pos)).resetMarque();
	}
	
	public GTree clone() {
		return new GTree(root, document, allCells, allCellsAssociations);
	}
	
	public Vector<GCell> getFigaroAncestorsChain(GCell c) {
		GCell fP;
		Vector<GCell> tamponVect = new Vector<GCell>();
		Vector<GCell> parentsVect = new Vector<GCell>();
		
		System.out.println("On recherche des infos pour : " + c.toString());
		
		if(c != null) {
			//On va tout d'abord recuperer l'integralite des parents
			for(int i=0; i<c.getChildrenCount(); i++) {
				if(c.getChild(i).getValue(0) != null)
					if(c.getChild(i).getValue(0).equals("FATHER")) {
						fP = findFigaroType(c.getChild(i).getChild(0).getValue(1).toString());
						if(fP != null) {
							parentsVect.add(fP);
							tamponVect = getFigaroAncestorsChain(fP);
							for(int j=0; j<tamponVect.size(); j++)
								parentsVect.add(tamponVect.get(j));
						}
					}
						
						
			}
		}
		
		return parentsVect;
	}
	
	public GCell findFigaroType(String name) {
		//On recherche a la profondeur 2
		for(int i=0; i<root.getChild(0).getChildrenCount(); i++) {
			for(int j=0; j<root.getChild(0).getChild(i).getChildrenCount(); j++)
				for(int k=0; k<root.getChild(0).getChild(i).getChild(j).getChildrenCount(); k++)
					if(root.getChild(0).getChild(i).getChild(j).getValue(0).toString().equals("NAME"))
						if(root.getChild(0).getChild(i).getChild(j).getChild(0).getValue(1).toString().equals(name))
							return root.getChild(0).getChild(i);
					
		}
		
		return null;
	}
	
	public Vector<GCell> findFigaroInterfaces(String typeName) {
		GCell fT = findFigaroType(typeName);
		Vector<GCell> interfacesVect = new Vector<GCell>();
		
		if(fT != null)
			for(int i=0; i<fT.getChildrenCount(); i++) {
				System.out.println("DIXXXX : " + typeName);
				if(fT.getChild(i).getValue(0).toString().equals("INTERFACE"))
					interfacesVect.add(fT.getChild(i));
			}
		
		return interfacesVect;										
	}
	
	public Vector<GCell> findFigaroSteps() {
		Vector<GCell> stepsVect = new Vector<GCell>();
		
		for(int i=0; i<root.getChild(0).getChildrenCount(); i++)
			if(root.getChild(0).getChild(i).getValue(0).toString().equals("STEP_ORDER"))
				for(int j=0; j<root.getChild(0).getChild(i).getChildrenCount(); j++)
					stepsVect.add(root.getChild(0).getChild(i).getChild(j));
		
		return stepsVect;
	}
	
	public Vector<GCell> findFigaroGroups() {
		Vector<GCell> groupVect = new Vector<GCell>();
		
		for(int i=0; i<root.getChild(0).getChildrenCount(); i++)
			if(root.getChild(0).getChild(i).getValue(0).toString().equals("GROUP_NAMES"))
				for(int j=0; j<root.getChild(0).getChild(i).getChildrenCount(); j++)
					groupVect.add(root.getChild(0).getChild(i).getChild(j));
		
		return groupVect;
	}
	
	public String findFigaroNameOfGCell(GCell c) {
		if(c != null)
			for(int i=0; i<c.getChildrenCount(); i++)
				if(c.getChild(i).getValue(0) != null)
					if(c.getChild(i).getValue(0).equals("NAME"))
						for(int j=0; j<c.getChild(i).getChildrenCount(); j++)
							if(c.getChild(i).getChild(j).getValue(1) != null)
								return c.getChild(i).getChild(j).getValue(1).toString();
		
		return "";	
	}
	
	public String findFigaroNameOfGCellGroup(GCell c) {
		if(c != null)
			for(int i=0; i<c.getChildrenCount(); i++)
				if(c.getChild(i).getValue(0) != null)
					return c.getChild(i).getValue(1).toString();
		
		return "";	
	}
}

