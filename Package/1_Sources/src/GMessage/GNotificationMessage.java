package GMessage;

import global.Messages;

import java.util.Vector;

import GObjectInformation.GObjectInformation;

public class GNotificationMessage extends GMessage {

	//The widget number
	/**
	 * @uml.property  name="widgetNumber"
	 */
	private int widgetNumber;
	
	public GNotificationMessage(GObjectInformation sender, Messages message, Vector<Object> args, int widgetNumber) {
		super(sender, message, args);
		this.widgetNumber = widgetNumber;
	}
	
	/**
	 * @return
	 * @uml.property  name="widgetNumber"
	 */
	public int getWidgetNumber() {
		return widgetNumber;
	}
	
	/**
	 * @param widgetNumber
	 * @uml.property  name="widgetNumber"
	 */
	public void setWidgetNumber(int widgetNumber) {
		this.widgetNumber = widgetNumber; 
	}
}
