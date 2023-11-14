package id.creatodidak.vrspolreslandak.auth;

import com.google.gson.annotations.SerializedName;

public class SatfungItem{

	@SerializedName("satker")
	private String satker;

	@SerializedName("id")
	private int id;

	@SerializedName("satfung")
	private String satfung;

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

	public void setSatfung(String satfung){
		this.satfung = satfung;
	}

	public String getSatfung(){
		return satfung;
	}
}