package global;


/**
 * The different constants representing the types of GNameRetriever which can be created. The kind of information retrieved in function of the tag is the following :
 * <p><code>RULERETRIEVER</code> -> name of a rule object</p>
 * <p><code>EQUATIONRETRIEVER</code> -> name of an equation object</p>
 * <p><code>NAMERETRIEVER</code> -> name of an object with the classic configuration where a son called <code>NAME</code> is located just under the root</p>
 * <p><code>TYPERETRIEVER</code> -> name of a type object</p>
 * <p><code>DIRECTRETRIEVER</code> -> tag of an object</p>
 * <p><code>TEXTRETRIEVER</code> -> text in an object</p>
 * <p><code>TYPENODELINKRETRIEVER</code> -> name of a node or a link object and place <code>NODE :</code> or <code>LINK :</code> in front of it according to its real type</p>
 * <p><code>ALGORETRIEVER</code> -> name of an algo object</p>
 * @author Guillaume Torrente & Marc Bouissou
 *
 */
public enum NameRetrieverClasses {
	RULERETRIEVER,
	EQUATIONRETRIEVER,
	NAMERETRIEVER,
	TYPERETRIEVER,
	DIRECTRETRIEVER,
	TEXTRETRIEVER,
	TYPENODELINKRETRIEVER,
	ALGORETRIEVER,
	INTERFACERETRIEVER
};
