package id.creatodidak.vrspolreslandak.api.models.chat;

import com.google.gson.annotations.SerializedName;

public class DaftartugasItem{

	@SerializedName("id")
	private int id;

	@SerializedName("parameter")
	private String parameter;

	@SerializedName("value")
	private String value;

	@SerializedName("status")
	private boolean status;

	@SerializedName("type")
	private String type;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setParameter(String parameter){
		this.parameter = parameter;
	}

	public String getParameter(){
		return parameter;
	}

	public void setValue(String value){
		this.value = value;
	}

	public String getValue(){
		return value;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}
}