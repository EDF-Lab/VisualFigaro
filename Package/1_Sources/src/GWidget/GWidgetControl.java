package GWidget;


import java.util.Vector;

import org.jdom.Element;

import GMessage.GMessage;
import GObject.GObject;
import GObjectInformation.GObjectInformation;

public abstract class GWidgetControl extends GWidget {
	
	private static final long serialVersionUID = 1L;

	public GWidgetControl() {
		super();
	}
	
	public GWidgetControl(GObject p, GObjectInformation info) {
		super(p, info);
	}
	
	public void translateMessage(GMessage message) {
		System.out.println("VisualFigaro : GWidgetControl : A control does not have translate any message");
	}
	
	public boolean loadXML(Vector<Element> e, boolean deeplyRooted) {
		System.out.println("VisualFigaro : GWidgetControl : A control does not have to load any XML code");
		return false;
	}
	
	public Vector<Element> saveXML() {
		System.out.println("VisualFigaro : GWidgetControl : A control does not have to save any XML code");
		return null;
	}
}
