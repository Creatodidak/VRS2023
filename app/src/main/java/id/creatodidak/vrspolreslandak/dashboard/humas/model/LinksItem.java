package id.creatodidak.vrspolreslandak.dashboard.humas.model;

import com.google.gson.annotations.SerializedName;

public class LinksItem{

	@SerializedName("active")
	private boolean active;

	@SerializedName("label")
	private String label;

	@SerializedName("url")
	private Object url;

	public void setActive(boolean active){
		this.active = active;
	}

	public boolean isActive(){
		return active;
	}

	public void setLabel(String label){
		this.label = label;
	}

	public String getLabel(){
		return label;
	}

	public void setUrl(Object url){
		this.url = url;
	}

	public Object getUrl(){
		return url;
	}
}