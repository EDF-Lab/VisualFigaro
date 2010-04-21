package global;

/**
 * The different constants representing the types of <code>GFilter</code> which can be created. By control we mean buttons and group of buttons. The filter function in function of the tags are :
 * <p><code>STEPS</code> -> </p>
 * <p><code>TYPES</code> -> in vertical order, an "add" button, a "del" button and a "edit" button</p>
 * <p><code>VISUALIZATION</code> -> vertical order, and "add" button and a "del" button and two buttons side by side. A button "up" represented by "/\" and a button down represented by "\/"</p>
 * @author Guillaume Torrente & Marc Bouissou
 *
 */
public enum FilterTypes {
	STEP,
	TYPE,
	GROUP,
	VISUALIZATION
}
