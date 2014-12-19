/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 04 December 2014                            
 * Author       : L.RAFFAELLI/ALL4TEC                                                                   
 * Modification : Creation
 * VF version   : 1.16
 * **************************************************************/

package GModel;

import java.util.Vector;

import GWidget.GWidgetControledList;

import org.jdom.Element;

public class GModelDataOperation{

	private boolean rslvCnsts;
	
	private boolean rslvAttrbts;
	
	private boolean instrules;
	
	private GWidgetControledList groupslist;
	
	private String filename;
	
	private String simplification;
	
	private String unloop;
	
	private String coherency;
	
	private String treename;
	
	private String maxSonsGate;
	
	private String object;
	
	private String variable;
	
	//generic builder
	public GModelDataOperation(){
		groupslist = null;
		
		rslvCnsts = true;
		rslvAttrbts = true;
		instrules = true;
		filename = "";
		simplification = "Complete";
		unloop = "Without";
		coherency = "Complete";
		treename = "";
		maxSonsGate = "";
		object = "";
		variable = "";
	}
	
	//builder for figaro0 generation settings
	public GModelDataOperation(boolean rslConst, boolean rslAttr, boolean instrul, GWidgetControledList groups, String filnam){
			
		rslvCnsts = rslConst;
		rslvAttrbts = rslAttr;
		instrules = instrul;
		groupslist = groups;
		filename = filnam;
		simplification = "Complete";
		unloop = "Without";
		coherency = "Complete";
		treename = "";
		maxSonsGate = "";
		object = "";
		variable = "";
	}
	
	//builder for fault tree generation settings
	public GModelDataOperation(GWidgetControledList groups, String filnam, String simpl, String unlp, String coheren, String tree, String mxsons, String obj, String var){
		
		rslvCnsts = true;
		rslvAttrbts = true;
		instrules = true;
		groupslist = groups;
		filename = filnam;
		simplification = simpl;
		unloop = unlp;
		coherency = coheren;
		treename = tree;
		maxSonsGate = mxsons;
		object = obj;
		variable = var;
	}
	
	public boolean getResolveConstants(){
		return this.rslvCnsts;
	}
	
	public boolean getResolveAttributs(){
		return this.rslvAttrbts;
	}
	
	public boolean getInstanciateRules(){
		return this.instrules;
	}
	
	public GWidgetControledList getGroupsList(){
		return this.groupslist;
	}
	
	public String getFileName(){
		return this.filename;
	}
	
	public String getSimplification(){
		return this.simplification;
	}
	
	public String getUnloop(){
		return this.unloop;
	}
	
	public String getCoherency(){
		return this.coherency;
	}
	
	public String getTreeName(){
		return this.treename;
	}
	
	public String getMaxSonsGate(){
		return this.maxSonsGate;
	}
	
	public String getObject(){
		return this.object;
	}
	
	public String getVariable(){
		return this.variable;
	}
}