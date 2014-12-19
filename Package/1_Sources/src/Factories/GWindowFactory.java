/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 24 March 2010                                  
 * Author       : D.WEYAND/ALL4TEC                               
 * Bug Id       : 47                                           
 * Modification : fix "Group" window title for GWindowGroupeRegles
 * VF Version   : 1.3               
 * **************************************************************
 * Date         :                                  
 * Author       :                                
 * Bug Id       :                                           
 * Modification :    
 * **************************************************************/
package Factories;

import org.jdom.Element;

import global.WindowClasses;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWindow.GWindow;
import GWindow.GWindowAlgo;
import GWindow.GWindowConnection;
import GWindow.GWindowDefaultVarLink;
import GWindow.GWindowDefaultVarNode;
import GWindow.GWindowGroupeRegles;
import GWindow.GWindowGroupeReglesSansNom;
import GWindow.GWindowInterface;
import GWindow.GWindowLink;
import GWindow.GWindowLinkAcceptedConnections;
import GWindow.GWindowLinkPort;
import GWindow.GWindowMenu;
import GWindow.GWindowModeleGenerationADD;
import GWindow.GWindowModeleInstanciationFigaro0;
import GWindow.GWindowModeleSimulation;
import GWindow.GWindowModeleTraitementExterne;
import GWindow.GWindowName;
import GWindow.GWindowNameSimple;
import GWindow.GWindowNodePort;
import GWindow.GWindowNode;
import GWindow.GWindowReglesType;
import GWindow.GWindowReglesTypeIfThen;
import GWindow.GWindowReglesTypeIfThenElse;
import GWindow.GWindowVarLink;
import GWindow.GWindowVarNode;
import GWindow.GWindowVisualizations;

/**
 * This class is a factory used to create particular types of <code>GWindow</code> objects
 * @author Torrente Guillaume & Marc Bouissou
 * @see GWindow
 * 
 */
public class GWindowFactory {

	/**
	 * This method creates an object of type <code>GWindow</code> according to the type given in the first argument.
	 * The different constants and the function of the windows created are the following :
	 * 	<p><code>GWINDOW</code> will return <code>null</code> because this type cannot be instanced. It is just the mother class of all windows. The windwo</p>
	 * 	<p><code>ALGO</code> -> used to edit the xml associated with the xml tag ALGORITHM_OD</p>
	 * 	<p><code>DEPART</code> -> used to edit the xml associated with the xml tag DEPART</p>
	 * @param cl The class of the <code>GWidget</code> which has to be created.
	 * @param p The parent of the <code>GWidget</code> which will be created.
	 * @param args A <code>Vector</code> of <code>Object</code> which contains different parameters the user want to give to the widget when it is created. In the <code>GWidget</code> constructor. They are used differently according to the widget constructed.
	 * @return A particular kind of <code>GNameRetriever</code>.
	 */
	public static GWindow createWindow(WindowClasses cl, GObject p, GObjectInformation info, Object[] args) {
		
		GWindow window = null;
		switch(cl) {
			case GWINDOW:
				System.out.println("VisualFigaro : GWindowFactory : GWindow creation impossible (abstract class)");
				window = null;
				break;
				
			case ALGO:
				System.out.println("Creation d'une fenetre Algo");
				window = new GWindowAlgo(p, info, (Integer)args[0]);
				if((Integer)args[0] == -1)
					window.loadXml(new Element("ALGORITHME_OD"));
				else
					window.loadXml((Element)args[1]);
				break;
			
			case DEPART:
				System.out.println("Creation d'une fenetre Depart");
				window = new GWindowReglesTypeIfThenElse(p, info, "Depart", (Integer)args[0]);
				if((Integer)args[0] == -1)
					window.loadXml(new Element("REGLE"));
				else
					window.loadXml((Element)args[1]);
				break;
			
			case ENTREE:
				System.out.println("Creation d'une fenetre Entree");
				window =  new GWindowReglesTypeIfThenElse(p, info, "Entree", (Integer)args[0]);
				if((Integer)args[0] == -1)
					window.loadXml(new Element("REGLE"));
				else
					window.loadXml((Element)args[1]);
				break;
				
			case EQUATION:
				System.out.println("Creation d'une fenetre Equation");
				window = new GWindowReglesTypeIfThen(p, info, "Equations", (Integer)args[0]);
				if((Integer)args[0] == -1)
					window.loadXml(new Element("EQUATION"));
				else
					window.loadXml((Element)args[1]);
				break;
			
			case REGLESTYPE:
				System.out.println("Creation d'une fenetre RegleType");
				window = new GWindowReglesType(p, info, (Integer)args[0]);
				if((Integer)args[0] == -1)
					window.loadXml(null);
				else
					window.loadXml((Element)args[1]);
				break;
				
			case SORTIE:
				System.out.println("Creation d'une fenetre Sortie");
				window = new GWindowReglesTypeIfThenElse(p, info, "Sortie", (Integer)args[0]);
				if((Integer)args[0] == -1)
					window.loadXml(new Element("REGLE"));
				else
					window.loadXml((Element)args[1]);
				break;
			
			case NAME:
				System.out.println("Creation d'une fenetre Name");
				window = new GWindowName(p, info, "Name", -1);
				window.loadXml(null);
				break;
				
			case NAMESIMPLE:
				System.out.println("Creation d'une fenetre Name");
				window = new GWindowNameSimple(p, info, "Name", -1);
				window.loadXml(null);
				break;
				
			case MENU:
				System.out.println("Creation d'une fenetre Menu");
				window = new GWindowMenu(p, info);
				window.loadXml(null);
				break;
			
			case FAULTTREEGENERATIONMODEL:
				System.out.println("Creation d'une fenetre de fault tree");
				window = new GWindowModeleGenerationADD(p, info, null, (Integer)args[0]);
				if((Integer)args[0] == -1)
					window.loadXml(new Element("MODELE_GENERATION_ADD"));
				else
					window.loadXml((Element)args[1]);
				break;
			
			case SIMULATIONMODEL:
				System.out.println("Creation d'une fenetre de simulation");
				window = new GWindowModeleSimulation(p, info, null, (Integer)args[0]);
				if((Integer)args[0] == -1)
					window.loadXml(new Element("MODELE_SIMULATION"));
				else
					window.loadXml((Element)args[1]);
				break;
			
			case FIGARO0INSTANCIATIONMODEL:
				System.out.println("Creation d'une fenetre de figaro 0");
				window = new GWindowModeleInstanciationFigaro0(p, info, null, (Integer)args[0]);
				if((Integer)args[0] == -1)
					window.loadXml(new Element("MODELE_INST_FIG0"));
				else
					window.loadXml((Element)args[1]);
				break;
			
			case EXTERNALTREATMENTMODEL:
				System.out.println("Creation d'une fenetre de traitement");
				window = new GWindowModeleTraitementExterne(p, info, null, (Integer)args[0]);
				if((Integer)args[0] == -1){
					window.loadXml(new Element("MODELE_TRAITEMENT_EXTERNE"));
				}
				else
					window.loadXml((Element)args[1]);
				break;
			
			case GROUPEREGLES:
				System.out.println("Creation d'une fenetre Groupe Regles");
				window = new GWindowGroupeRegles(p, info, "Group", -1);
				window.loadXml(null);
				break;
				
			case GROUPEREGLESSANSNOM:
				System.out.println("Creation d'une fenetre Groupe Regles avec SANS_NOM");
				window = new GWindowGroupeReglesSansNom(p, info, "Group", -1);
				window.loadXml(null);
				break;
			
			case NODE:
				System.out.println("Creation d'une fenetre Node");
				if((Integer)args[0] == -1)
					return null;
				window = new GWindowNode(p, info, (Integer)args[0]);
				window.loadXml((Element)args[1]);
				break;
			
			case DEFAULTVARNODE:
				System.out.println("Creation d'une fenetre Default Var Node");
				window = new GWindowDefaultVarNode(p, info, -1);
				window.loadXml(null);
				break;
			
			case VARNODE:
				System.out.println("Creation d'une fenetre Var Node");
				window = new GWindowVarNode(p, info, (Integer)args[0]);
				if((Integer)args[0] == -1)
					window.loadXml(null);
				else
					window.loadXml((Element)args[1]);
				break;
			
			case PORTNODE:
				System.out.println("Creation d'une fenetre Port Node");
				window = new GWindowNodePort(p, info, (Integer)args[0]);
				if((Integer)args[0] == -1)
					window.loadXml(null);
				else
					window.loadXml((Element)args[1]);
				break;
			
			case LINKCONNECTION:
				System.out.println("Creation d'une fenetre Link Connection");
				window = new GWindowLinkAcceptedConnections(p, info, (Integer)args[0], (Boolean)args[1]);
				if((Integer)args[0] == -1)
					window.loadXml(null);
				else
					window.loadXml((Element)args[1]);
				break;
			
			case VISUALIZATION:
				System.out.println("Creation d'une fenetre Visualization Node");
				window = new GWindowVisualizations(p, info, "Visualization");
				break;
			
			case INTERFACE:
				System.err.println("Creation d'une fenetre Interface");
				window = new GWindowInterface(p, info, "Interface", (String)args[1]);
				System.out.println("OK");
				break;
			
			case LINK:
				System.out.println("Creation d'une fenetre Link");
				if((Integer)args[0] == -1)
					return null;
				window = new GWindowLink(p, info, (Integer)args[0]);
				window.loadXml((Element)args[2]);
				break;
			
			case DEFAULTVARLINK:
				System.out.println("Creation d'une fenetre Default Var Link");
				window = new GWindowDefaultVarLink(p, info, -1);
				window.loadXml(null);
				break;
			
			case VARLINK:
				System.out.println("Creation d'une fenetre Var Link");
				window = new GWindowVarLink(p, info, (Integer)args[0]);
				if((Integer)args[0] == -1)
					window.loadXml(null);
				else
					window.loadXml((Element)args[1]);
				break;
			
			case PORTLINK:
				System.out.println("Creation d'une fenetre Port Link");
				if(args.length <= 2) {
					window = new GWindowLinkPort(p, info, (Integer)args[0], (Boolean)args[1]);
					window.loadXml(null);
				} else {
					window = new GWindowLinkPort(p, info, (Integer)args[0], (Boolean)args[2]);
					window.loadXml((Element)args[1]);
				}
				break;
			
			case CONNECTION:
				System.out.println("Creation d'une fenetre Connection");
				window = new GWindowConnection(p, info, (Integer)args[0]);
				if((Integer)args[0] == -1)
					window.loadXml(null);
				else
					window.loadXml((Element)args[1]);
				break;
				
			default:
				System.out.println("VisualFigaro : GWindowFactory : Type de fenetre non reconnu");
				window = null;
				break;
		}
		
		return window;
	}
}
