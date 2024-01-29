package id.creatodidak.vrspolreslandak.dashboard.humas.model;

import com.google.gson.annotations.SerializedName;

public class Userdata{

	@SerializedName("nama")
	private String nama;

	@SerializedName("foto")
	private String foto;

	@SerializedName("satker")
	private String satker;

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setFoto(String foto){
		this.foto = foto;
	}

	public String getFoto(){
		return foto;
	}

	public void setSatker(String satker){
		this.satker = satker;
	}

	public String getSatker(){
		return satker;
	}
}