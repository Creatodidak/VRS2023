package id.creatodidak.vrspolreslandak.auth;

import com.google.gson.annotations.SerializedName;

public class PangkatItem{

	@SerializedName("level")
	private String level;

	@SerializedName("short")
	private String jsonMemberShort;

	@SerializedName("id")
	private int id;

	@SerializedName("long")
	private String jsonMemberLong;

	public void setLevel(String level){
		this.level = level;
	}

	public String getLevel(){
		return level;
	}

	public void setJsonMemberShort(String jsonMemberShort){
		this.jsonMemberShort = jsonMemberShort;
	}

	public String getJsonMemberShort(){
		return jsonMemberShort;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setJsonMemberLong(String jsonMemberLong){
		this.jsonMemberLong = jsonMemberLong;
	}

	public String getJsonMemberLong(){
		return jsonMemberLong;
	}
}