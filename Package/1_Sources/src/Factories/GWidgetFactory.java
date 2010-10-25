/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 25 October 2010                            
 * Author       : D.WEYAND/ALL4TEC                              
 * Bug Id       : 
 * Evol Id      : n°11                                   
 * Modification : External Treatments Models implementation 
 *                Use simple name acquisition scheme for code names
 * VF version   : 1.12c
 * **************************************************************/

package Factories;

import java.util.Vector;

import global.ControlTypes;
import global.ListTypes;
import global.NameRetrieverClasses;
import global.WidgetClasses;
import global.WindowClasses;
import GObject.GObject;
import GObjectInformation.GObjectInformation;
import GWidget.GWidget;
import GWidget.GWidgetAddDel;
import GWidget.GWidgetAddDelEdit;
import GWidget.GWidgetAddDelUpDown;
import GWidget.GWidgetComboBox;
import GWidget.GWidgetControledList;
import GWidget.GWidgetEditDefaultVar;
import GWidget.GWidgetFormattedTextField;
import GWidget.GWidgetListComplexArray;
import GWidget.GWidgetListSimpleArray;
import GWidget.GWidgetNodeLinkNeitherUpDownEdit;
import GWidget.GWidgetTextField;

/**
 * This class is a factory used to create particular types of <code>GWidget</code> objects
 * @author Torrente Guillaume & Marc Bouissou
 * @see GWidget
 * 
 */
public class GWidgetFactory {

	/**
	 * This method creates an object of type <code>GWidget</code> according to the type given in the first argument.
	 * The different types and their functions are the following :
	 * 	<p><code>RULERETRIEVER</code> will retrieve the name of a rule object</p>
	 * 	<p><code>EQUATIONRETRIEVER</code> will retrieve the name of an equation object</p>
	 * 	<p><code>NAMERETRIEVER</code> will retrieve the name of an object with the classic configuration where a son called <code>NAME</code> is located just under the root</p>
	 * 	<p><code>TYPERETRIEVER</code> will retrieve the name of a type object</p>
	 * 	<p><code>DIRECTRETRIEVER</code> will retrieve the tag of an object</p>
	 * 	<p><code>TEXTRETRIEVER</code> will retrieve a the text in an object</p>
	 * 	<p><code>TYPENODELINKRETRIEVER</code> will retrieve a the name of a node or a link object and place <code>NODE :</code> or <code>LINK :</code> in front of it according to its real type</p>
	 * 	<p><code>ALGORETRIEVER</code> will retrieve a the name of an algo object</p>
	 * @param cl The class of the <code>GWidget</code> which has to be created.
	 * @param p The parent of the <code>GWidget</code> which will be created.
	 * @param args A <code>Vector</code> of <code>Object</code> which contains different parameters the user want to give to the widget when it is created. In the <code>GWidget</code> constructor. They are used differently according to the widget constructed.
	 * @return A particular kind of <code>GNameRetriever</code>.
	 */
	public static GWidget createWidget(WidgetClasses cl, GObject p, GObjectInformation info, Vector<Object> args) {
		
		switch(cl) {
			case WIDGET:
				//GWidget is only an interface
				System.out.println("VisualFigaro : GWidgetFactory : GWidget is only an interface");
				return null;
				
			case ADDDEL:
				return new GWidgetAddDel(p, info);
				
			case ADDDELEDIT:
				return new GWidgetAddDelEdit(p, info);
			
			case ADDDELUPDOWN:
				return new GWidgetAddDelUpDown(p, info);
			
			case NODELINKNEITHERUPDOWNEDIT:
				return new GWidgetNodeLinkNeitherUpDownEdit(p, info);
				
			case CONTROL:
				//GWidgetControl is only an interface
				System.out.println("VisualFigaro : GWidgetFactory : GWidgetControl is only an interface");
			
			case CONTROLEDLIST:
				return new GWidgetControledList();
				
			case CONTROLEDLISTADDDEL:
				System.out.println("CONTEROLED");
				return new GWidgetControledList(p, info, null, null, ListTypes.SIMPLEARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDEL, WindowClasses.GROUPEREGLES, null);
			
			case CONTROLEDLISTADDDEL2:
				System.out.println("CONTEROLED2");
				return new GWidgetControledList(p, info, null, null, ListTypes.SIMPLEARRAY, NameRetrieverClasses.TEXTRETRIEVER, ControlTypes.ADDDEL, WindowClasses.NAMESIMPLE, null);	
			
			case COMBO:
				if(args == null) {
					return new GWidgetComboBox(p, info, null);
				} else {
					Vector<String> newArgs = new Vector<String>();
					for(Object obj : args)
						newArgs.add(obj.toString());
					return new GWidgetComboBox(p, info, newArgs);
				}
				
			case FORMATTEDTEXTFIELD:
				System.out.println("DATETEXT");
				if(p == null)
					return new GWidgetFormattedTextField();
				else
					return new GWidgetFormattedTextField(p, info);
				
			case LIST:
				//GWidgetList is only an interface
				System.out.println("VisualFigaro : GWidgetFactory : GWidgetList is only an interface");
				
			case LISTCOMPLEXARRAY:
				return new GWidgetListComplexArray();
				
			case LISTSIMPLEARRAY:
				return new GWidgetListSimpleArray();
				
			case TEXTFIELD:
				System.out.println("TEXT");
				if(p == null)
					return new GWidgetTextField();
				else
					return new GWidgetTextField(p, info);
				
			case EDITDEFAULTVAR:
				return new GWidgetEditDefaultVar(p, info, (String)args.get(0), (Boolean)args.get(1));
				
			default:
				System.out.println("VisualFigaro : GWindowFactory : GWindow type not recognized");
				return null;
		}
	}
}
