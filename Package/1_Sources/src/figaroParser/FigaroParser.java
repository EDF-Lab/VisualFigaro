/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 6 April 2010                            
 * Author       : D.WEYAND/ALL4TEC                              
 * Bug Id       :                                         
 * Modification : Optimize French/English parser char jump
 * VF version   : 1.6
 * **************************************************************
 * Date         : 13 April 2010                            
 * Author       : D.WEYAND/ALL4TEC                              
 * Bug Id       :                                         
 * Modification : Code cleanup to avoid warnings
 * VF version   : 1.7
 * **************************************************************/

package figaroParser;

/**
 * @author Guillaume Torrente & Marc Bouissou
 */

/*
 * FigaroParser.java
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

import figaroInterface.*;

import java.util.ArrayList;

import GLanguage.GLanguage;
import GObjectInformation.GObjectInformation;

public class FigaroParser {
	/**
	 * @uml.property  name="information"
	 * @uml.associationEnd  
	 */
	protected GObjectInformation information; 
	/**
	 * @uml.property  name="commentairePosition"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.Boolean"
	 */
	private ArrayList<Boolean> commentairePosition;
	/**
	 * @uml.property  name="gtree"
	 * @uml.associationEnd  inverse="figParser:figaroInterface.GTree"
	 */
	private GTree gtree;
	/**
	 * @uml.property  name="allCells"
	 */
	private ArrayList<GCell> allCells;
	/**
	 * @uml.property  name="allCellsAssociations"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.Integer"
	 */
	private ArrayList<Integer> allCellsAssociations;
	
	public FigaroParser(GTree gtree, ArrayList<GCell> allCells, ArrayList<Integer> allCellsAssociations) {
		this.gtree = gtree;
		this.allCells = allCells;
		this.allCellsAssociations = allCellsAssociations;
	}

	public FigaroParser() {
		this.commentairePosition = new ArrayList<Boolean>();
		this.gtree = null;
		this.allCells = new ArrayList<GCell>();
		this.allCellsAssociations = new ArrayList<Integer>();
	}
	
	public ArrayList<Integer> parseCode(String s, GTree gtree, ArrayList<GCell> allCells, ArrayList<Integer> allCellsAssociations, GLanguage language) {
		this.gtree = gtree;
		this.allCells = allCells;
		this.allCellsAssociations = allCellsAssociations;
		
		return parseCode(s, language);
	}
	
	private int tokuBetsuMin(int a, int b) {
		if(a < 0)
			return b;
		
		if(b < 0)
			return a;
		
		return Math.min(a, b);
	}
	
	private ArrayList<Integer> parseCode(String s, GLanguage language) {
		//Some variables
		int indexDepart = 0, indexFin = 0;
		int typeNumero = 0;
		int charJump = ("TYPE").length();
		
		String currentlanguage = new String(language.getLanguage().toString());
		if (currentlanguage.equals("English"))
		  charJump = ("CLASS").length();
		
		//Tout d'abord on doit initialiser la taille de l'arraylist des commentaires a la bonne taille
		commentairePosition = new ArrayList<Boolean>(s.length());
		for(int i=0; i<s.length(); i++)
			commentairePosition.add(false);
		
		allCellsAssociations = new ArrayList<Integer>(s.length());
		for(int i=0; i<s.length(); i++)
			allCellsAssociations.add(0);
		
		//On va ensuite faire appel a la fonction trouverCommentaire qui va permettre de definir quels caracteres prendre en compte dans le parsing
		//et lesquels exclure car ils font partis des commentaires
		trouverCommentaire(s);

		//String test_string = language.getFigaroTranslation("TYPE");
		//Maintenant il va falloir trouver toutes les occurences du mot "TYPE" afin de determiner les blocs de type
		while((indexDepart = tokuBetsuMin(tokuBetsuMin(s.indexOf("\n" + language.getFigaroTranslation("TYPE") + " ", indexDepart), 
				s.indexOf(" " + language.getFigaroTranslation("TYPE") + " ", indexDepart)), 
				s.indexOf("\t" + language.getFigaroTranslation("TYPE") + " ", indexDepart))) >= 0)
		{
			//On teste si l'indice n'est pas dans un commentaire
			if(commentairePosition.get(indexDepart)) {
				
				//On est dans le cas ou le type est dans un commentaire. On va donc passer au prochaine type
				indexDepart+=charJump;
				
			} else {
				//On est dans le cas ou le type est bien declare dans le corps du fichier
			
				//On se place juste apres la declaration pour commencer la recherche
				
				indexFin = indexDepart + charJump;
				indexDepart+=1;
				//On lance une premiere recherche
				
				//On va alors determiner le prochain type valide
				while((indexFin = tokuBetsuMin(tokuBetsuMin(s.indexOf("\n" + language.getFigaroTranslation("TYPE") + " ", indexFin), s.indexOf(" " + language.getFigaroTranslation("TYPE") + " ", indexFin)), s.indexOf("\t" + language.getFigaroTranslation("TYPE") +  " ", indexFin))) > 0 )
				{
					if(commentairePosition.get(indexFin)) {
						indexFin += charJump;
					} else {
						break;
					}
				}
				
				indexFin=indexFin+1;
				
				//On retrouve le typeNumero ieme type dans l'arbre.
				GCell cell = trouverIemeType(typeNumero);
				
				if(cell == null) {
					System.err.println("The cell is null");
					return null;
				}

				//System.err.println("Type num : " + typeNumero + " et les deux variables depart : " + indexDepart + " et l'index de fin : " + indexFin + " et la chaine : " + s.substring(indexDepart, indexDepart+20));
				
				if(indexFin > 0) {
					
					//System.out.println("Voici la fin : " + indexFin);
					
					if(indexFin > commentairePosition.size())
						indexFin = commentairePosition.size();
					
					//On va maintenant remplir le arrayList
					for(int i=indexDepart; i<indexFin; i++)
						allCellsAssociations.set(i, cell.getUID());
					
					//System.err.println("Voila les deux : " + indexDepart + " : " + indexFin);
					
					indexDepart = (indexFin-1);
				} else {
					//On va maintenant remplir le arrayList
					for(int i=indexDepart; i<commentairePosition.size(); i++)
						allCellsAssociations.set(i, cell.getUID());
					
					break;
				}
				
				//On passe au type suivant
				typeNumero++;

				//On passe a la recherche du prochain bloc
			}
					
		}
		
//System.out.println("Voici le allCells : " + this.allCellsAssociations);
		/*for(int val : allCellsAssociations)
			System.err.print(val + " ");*/
		return this.allCellsAssociations;
	}
	
	private GCell trouverIemeType(int pos) {
		
		int searchPos=0;
		
		//On va recuperer le nombre d'enfants de la racine. Attention il s'agit de la deuxieme racine a cause du type #document
		int nbChild = gtree.getRoot().getChild(0).getChildrenCount();
		
		//On va parcourir les types dans l'arbre. Il faut faire attention car il y a aussi un ordre_des_etapes
		for(int i=0; i<nbChild; i++) { 

			if(gtree.getRoot().getChild(0).getChild(i).getValue(0).toString().equals("CLASS")) {
				
				if(searchPos == pos)
					return gtree.getRoot().getChild(0).getChild(i);
				searchPos++;
			}
		}
		
		return null;
	}
	
	private boolean trouverCommentaire(String s) {
		
		//Variables for the index of the beginning and the end of the sections
		int indexDepart=0, indexFin=0;
		
		//Tant que l'on peut trouver la chaine de caracteres "(*"
		while((indexDepart = s.indexOf("(*", indexDepart)) >= 0) {
			
			//On cherche maintenant la fin du commentaire
			indexFin = s.indexOf("*)", indexDepart+2);
			
			//On va mettre a 1 tout les booleens dans cette zone de commentairesPosition
			//Il y a deux cas soit on trouve un tag de fermeture et dans ce cas on continue la recherche pour un tag d'ouverture
			//Soit il n'y a pas de tag de fermeture et on va completer par des 1 jusqu'a la fin du tableau avant de sortir de la fonction
			if(indexFin < 0) {
				for(int i=indexDepart; i<commentairePosition.size(); i++) {
					commentairePosition.set(i, true);
					//System.out.print("(" + i + ")");
				}
				//Toolkit.getDefaultToolkit().beep();
				return true;
			}
			
			if(indexFin > commentairePosition.size())
				indexFin = commentairePosition.size();
			
			for(int i=indexDepart; i<indexFin; i++) {
				commentairePosition.set(i, true);
			}
						
			//On deplace le debut de l'analyse au bloc suivant
			indexDepart = indexFin + 2;
		}
		
		return true;
	}

	public ArrayList<GCell> getAllCells() {
		return allCells;
	}
	
	public ArrayList<Integer> getAllCellsAssociations() {
		return allCellsAssociations;
	}

	public ArrayList<Boolean> getCommentairePosition() {
		return commentairePosition;
	}

	/**
	 * @return
	 * @uml.property  name="gtree"
	 */
	public GTree getGtree() {
		return gtree;
	}

	public void setAllCells(ArrayList<GCell> allCells) {
		this.allCells = allCells;
	}

	public void setAllCellsAssociations(ArrayList<Integer> allCellsAssociations) {
		this.allCellsAssociations = allCellsAssociations;
	}
	
	public void setCommentairePosition(ArrayList<Boolean> commentairePosition) {
		this.commentairePosition = commentairePosition;
	}

	/**
	 * @param gtree
	 * @uml.property  name="gtree"
	 */
	public void setGtree(GTree gtree) {
		this.gtree = gtree;
	}
}
