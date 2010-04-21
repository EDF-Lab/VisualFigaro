package GMessage;

import global.Messages;

import java.util.Vector;

import GObjectInformation.GObjectInformation;

/**
 * Messages are used to communicate between <code>GWidget</code>. They contain two particular fields.
 * <p>The first one is the type of message sent which is one of the <code>Messages</code> enum.</p>
 * <p>The second one is the message body itself. It is represented as a <code>Vector</code> of <code>Object</code>. This body is used differently according to the type of message.
 * @author Guillaume Torrente & Marc Bouissou
 * @see Messages
 */
public class GMessage {
	//The three main arguments of a message
	/**
	 * @uml.property  name="message"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected Messages message;
	/**
	 * @uml.property  name="arguments"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	protected Vector<Object> arguments;
	/**
	 * @uml.property  name="sender"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected GObjectInformation sender;
	
	/**
	 * The default constructor of a <code>GMessage</code> just contains the message type.
	 * @param sender The sender of the message
	 * @param message Message type
	 */
	public GMessage(GObjectInformation sender, Messages message) {
		this.message = message;
		this.arguments = new Vector<Object>();
		this.sender = sender;
	}
	
	/**
	 * This constructor creates a <code>GMessage</code> from the message type and an <code>Object</code> which inserted at the end of the argument <code>Vector</code>.
	 * @param sender The sender of the message
	 * @param message Message type
	 * @param arg Arguments
	 */
	public GMessage(GObjectInformation sender, Messages message, Object arg) {
		this.message = message;
		this.arguments = new Vector<Object>();
		this.arguments.add(arg);
		this.sender = sender;
	}
	
	/**
	 * This constructor creates a <code>GMessage</code> from the message type and a <code>Vector</code> of <code>Object</code>.
	 * @param message Message type
	 * @param args Arguments
	 */
	public GMessage(GObjectInformation sender, Messages message, Vector<Object> args) {
		this.message = message;
		this.arguments = new Vector<Object>();
		this.arguments = args;
		this.sender = sender;
	}
	
	/**
	 * This constructor creates a <code>GMessage</code> from the message type and an array of <code>Object</code> which will be converted in a <code>Vector</code> of <code>Object</code>.
	 * @param message Message type
	 * @param args Arguments
	 */
	public GMessage(GObjectInformation sender, Messages message, Object[] args) {
		this.message = message;
		this.arguments = new Vector<Object>();
		for(int i=0; i<args.length; ++i)
			this.arguments.add(args[i]);
		this.sender = sender;
	}
	
	/**
	 * Retrieves the argument of the <code>GMessage</code>.
	 * @return Arguments
	 */
	public Vector<Object> getArguments() {
		return arguments;
	}
	
	/**
	 * Set the argument of the <code>GMessage</code>
	 * @param args Arguments
	 */
	public void setArguments(Vector<Object> args) {
		this.arguments = args;
	}
	
	/**
	 * Adds an argument to the argument <code>Vector</code>
	 * @param arg Object
	 */
	public void addArgument(Object arg) {
		arguments.add(arg);
	}
	
	/**
	 * Retrieves the message type of the <code>GMessage</code>.
	 * @return  Message type
	 * @uml.property  name="message"
	 */
	public Messages getMessage() {
		return message;
	}
	
	/**
	 * Set the message type of the <code>GMessage</code>
	 * @param message  Message type
	 * @uml.property  name="message"
	 */
	public void setMessage(Messages message) {
		this.message = message;
	}
	
	/**
	 * Get the sender of the <code>GMessage</code>
	 * @return  Sender
	 * @uml.property  name="sender"
	 */
	public GObjectInformation getSender() {
		return sender;
	}
	
	/**
	 * Set the sender of the <code>GMessage</code>.
	 * @param sender  New sender
	 * @uml.property  name="sender"
	 */
	public void setSender(GObjectInformation sender) {
		this.sender = sender;
	}
}
