package id.creatodidak.vrspolreslandak.api.models;

import com.google.gson.annotations.SerializedName;

public class DaftarwilayahkarhutlaItem{

	@SerializedName("satker")
	private String satker;

	@SerializedName("id")
	private int id;

	@SerializedName("wilayah")
	private String wilayah;

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

	public void setWilayah(String wilayah){
		this.wilayah = wilayah;
	}

	public String getWilayah(){
		return wilayah;
	}
}