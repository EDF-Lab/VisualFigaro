package GObject;

import GMessage.GMessage;

/**
 * The mother of all graphical interface classes (<code>GWidget</code> and <code>GWindow</code>). It reveals which is common to all graphical interface components : the <code>translateMessage</code> method.
 * All the graphical interface components can communicate trough this interface common to every <code>GObject</code>.
 * @author Guillaume Torrente & Marc Bouissou
 * @see GWidget, GWindow
 */
public interface GObject {

	/**
	 * Method which serves the communication between all graphical components.
	 * @param message A <code>GMessage</code> containing all the instructions to be executed.
	 */
	public abstract void translateMessage(GMessage message);
}
