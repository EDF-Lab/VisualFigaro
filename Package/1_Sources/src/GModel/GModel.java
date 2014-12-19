/* **************************************************************
 *              File modifications log                           
 * **************************************************************
 * Date         : 29 October 2014                            
 * Author       : L.RAFFAELLI/ALL4TEC                                                                   
 * Modification : Creation
 * VF version   : 1.16
 * **************************************************************/

package GModel;

import GKnowledgeBase.GKnowledgeBase;
import GLanguage.GLanguage;

public class GModel{
	//name of the Model
	private String modelName;
	
	//the KnowledgeBase used for the Model
	private GKnowledgeBase modelKnowledgeBase;
	
	//status of the associated knowledge base
	private boolean kBModify;
	
	//status of the model
	private boolean modelModify;
	
	//figaro 0 generation settings
	private GModelDataOperation figaro0Settings;
	
	//fault tree generation settings
	private GModelDataOperation faultTreeSettings;
	
	public GModel(){
		modelName = "";
		modelKnowledgeBase = new GKnowledgeBase();
		kBModify = false;
		modelModify = false;
		figaro0Settings = new GModelDataOperation();
		faultTreeSettings = new GModelDataOperation();
	}
	
	public GModel(String name, GKnowledgeBase knowledgeBase){
		modelName = name;
		modelKnowledgeBase = knowledgeBase;
		kBModify = false;
		modelModify = false;
		figaro0Settings = new GModelDataOperation();
		faultTreeSettings = new GModelDataOperation();
	}
	
	public String getModelName() {
		return this.modelName;
	}
	
	public GLanguage getModelLanguage(){
		return this.modelKnowledgeBase.getKnowledgeBaseLanguage();
	}
	
	public GKnowledgeBase getKnowledgeBase(){
		return this.modelKnowledgeBase;
	}
	
	public GModelDataOperation getFigaro0Settings(){
		return this.figaro0Settings;
	}
	
	public GModelDataOperation getFaultTreeSettings(){
		return this.faultTreeSettings;
	}
	
	public boolean isKBModify(){
		return this.kBModify;
	}
	
	public boolean isModelModify(){
		return this.modelModify;
	}
	
	public void setModelName(String name){
		this.modelName = name;
	}
	
	public void setModelKnowledgeBase(GKnowledgeBase knowledgeBase){
		this.modelKnowledgeBase = knowledgeBase;
	}
	
	public void setKBModify(boolean mod){
		this.kBModify = mod;
	}
	
	public void setModelModify(boolean mod){
		this.modelModify = mod;
	}
	
	public void setFigaro0Settings(GModelDataOperation figaro0){
		this.figaro0Settings = figaro0;
	}
	
	public void setFaultTreeSettings(GModelDataOperation faultTree){
		this.faultTreeSettings = faultTree;
	}
}
