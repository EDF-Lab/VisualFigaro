package GIcon;

public class GIcon {
	
	//The name or the path of the icon
	/**
	 * @uml.property  name="path"
	 */
	private String path;
	
	//Indicate if the path variable indicate just the icon name or the icon path
	/**
	 * @uml.property  name="completePath"
	 */
	private boolean completePath;
	
	public GIcon(String path, boolean completePath) {
		this.path = path;
		this.completePath = completePath;
	}

	/**
	 * @return  the path
	 * @uml.property  name="path"
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path  the path to set
	 * @uml.property  name="path"
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return  the completePath
	 * @uml.property  name="completePath"
	 */
	public boolean isCompletePath() {
		return completePath;
	}

	/**
	 * @param completePath  the completePath to set
	 * @uml.property  name="completePath"
	 */
	public void setCompletePath(boolean completePath) {
		this.completePath = completePath;
	}
	
	
}
