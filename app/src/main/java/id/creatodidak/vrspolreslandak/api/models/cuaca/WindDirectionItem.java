package id.creatodidak.vrspolreslandak.api.models.cuaca;

import com.google.gson.annotations.SerializedName;

public class WindDirectionItem{

	@SerializedName("arahangin")
	private String arahangin;

	@SerializedName("tanggal")
	private String tanggal;

	public void setArahangin(String arahangin){
		this.arahangin = arahangin;
	}

	public String getArahangin(){
		return arahangin;
	}

	public void setTanggal(String tanggal){
		this.tanggal = tanggal;
	}

	public String getTanggal(){
		return tanggal;
	}
}