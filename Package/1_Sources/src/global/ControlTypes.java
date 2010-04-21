package global;

/**
 * The different constants representing the types of <code>GWidget</code> of type control which can be created. By control we mean buttons and group of buttons. The panels created in function of the tags are :
 * <p><code>ADDDEL</code> -> in vertical order, an "add" button and a "del" button</p>
 * <p><code>ADDDELEDIT</code> -> in vertical order, an "add" button, a "del" button and a "edit" button</p>
 * <p><code>ADDDELUPDOWN</code> -> vertical order, and "add" button and a "del" button and two buttons side by side. A button "up" represented by "/\" and a button down represented by "\/"</p>
 * <p><code>NODELINKNEITHERUPDOWNEDIT</code> -> in vertical order, a "node" button, a "link" button, a "neither" button, side by side a "up" and "down" buttons with the representation "/\" "\/" and finally an "edit" button</p>
 * @author Guillaume Torrente & Marc Bouissou
 *
 */
public enum ControlTypes {
	ADDDEL, 
	ADDDELEDIT,
	ADDDELUPDOWN,
	NODELINKNEITHERUPDOWNEDIT
};
