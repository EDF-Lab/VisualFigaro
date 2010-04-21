package Factories;

import global.NameRetrieverClasses;
import GLanguage.GLanguage;
import GNameRetriever.GNameRetriever;
import GNameRetriever.GNameRetrieverMethodAlgo;
import GNameRetriever.GNameRetrieverMethodDirect;
import GNameRetriever.GNameRetrieverMethodEquationName;
import GNameRetriever.GNameRetrieverMethodInterface;
import GNameRetriever.GNameRetrieverMethodName;
import GNameRetriever.GNameRetrieverMethodRuleName;
import GNameRetriever.GNameRetrieverMethodText;
import GNameRetriever.GNameRetrieverMethodTypeName;
import GNameRetriever.GNameRetrieverMethodTypeNodeLink;

/**
 * This class is a factory used to create particular types of <code>GNameRetriever</code> objects
 * @author Torrente Guillaume & Marc Bouissou
 * @see GNameRetriever
 * 
 */
public class GNameRetrieverFactory {
	/**
	 * Default constructor for the factory.
	 */
	public GNameRetrieverFactory() {
	}
	
	/**
	 * This method creates an object of type <code>GNameRetriever</code> according to the type given in the first argument.
	 * The different types and how to use them are detailed directly in their class :
	 * <p><code>RULERETRIEVER</code> will retrieve the name of a rule object</p>
	 * <p><code>EQUATIONRETRIEVER</code> will retrieve the name of an equation object</p>
	 * <p><code>NAMERETRIEVER</code> will retrieve the name of an object with the classic configuration where a son called <code>NAME</code> is located just under the root</p>
	 * <p><code>TYPERETRIEVER</code> will retrieve the name of a type object</p>
	 * <p><code>DIRECTRETRIEVER</code> will retrieve the tag of an object</p>
	 * <p><code>TEXTRETRIEVER</code> will retrieve a the text in an object</p>
	 * <p><code>TYPENODELINKRETRIEVER</code> will retrieve a the name of a node or a link object and place <code>NODE :</code> or <code>LINK :</code> in front of it according to its real type</p>
	 * <p><code>ALGORETRIEVER</code> will retrieve a the name of an algo object	</p>
	 * <p><code>INTERFACERETRIEVER</code> will retrieve a the name of an interface object</p>
	 * @param nameRetrieverClass The class of the <code>GNameRetriever</code> which has to be created.
	 * @return A particular kind of <code>GNameRetriever</code>. 
	 */
	public static GNameRetriever createNameRetriever(GLanguage language, NameRetrieverClasses nameRetrieverClass) {
		
		switch(nameRetrieverClass) {
			case RULERETRIEVER:
				System.out.println("Rule");
				return new GNameRetriever(new GNameRetrieverMethodRuleName(language));
			
			case EQUATIONRETRIEVER:
				System.out.println("Equa");
				return new GNameRetriever(new GNameRetrieverMethodEquationName(language));
			
			case NAMERETRIEVER:
				System.out.println("Name");
				return new GNameRetriever(new GNameRetrieverMethodName(language));
			
			case TYPERETRIEVER:
				System.out.println("Type");
				return new GNameRetriever(new GNameRetrieverMethodTypeName(language));
				
			case DIRECTRETRIEVER:
				System.out.println("Direct");
				return new GNameRetriever(new GNameRetrieverMethodDirect(language));
				
			case TEXTRETRIEVER:
				System.out.println("Text");
				return new GNameRetriever(new GNameRetrieverMethodText(language));
				
			case TYPENODELINKRETRIEVER:
				System.out.println("NodeLink");
				return new GNameRetriever(new GNameRetrieverMethodTypeNodeLink(language));
			
			case ALGORETRIEVER:
				System.out.println("Algo");
				return new GNameRetriever(new GNameRetrieverMethodAlgo(language));
				
			case INTERFACERETRIEVER:
				System.out.println("Interface");
				return new GNameRetriever(new GNameRetrieverMethodInterface(language));
				
			default:
				System.out.println("VisualFigaro : GNameRetrieverFactory : GNameRetrieverType type not recognized");
				return null;
		}
	}
}
