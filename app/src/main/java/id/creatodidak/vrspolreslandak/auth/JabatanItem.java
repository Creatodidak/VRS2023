package id.creatodidak.vrspolreslandak.auth;

import com.google.gson.annotations.SerializedName;

public class JabatanItem{

	@SerializedName("level")
	private String level;

	@SerializedName("jabatan")
	private String jabatan;

	@SerializedName("satker")
	private String satker;

	@SerializedName("id")
	private int id;

	public void setLevel(String level){
		this.level = level;
	}

	public String getLevel(){
		return level;
	}

	public void setJabatan(String jabatan){
		this.jabatan = jabatan;
	}

	public String getJabatan(){
		return jabatan;
	}

	public void setSatker(String satker){
		this.satker = satker;
	}

	public String getSatker(){
		return satker;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}