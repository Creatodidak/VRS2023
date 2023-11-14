package id.creatodidak.vrspolreslandak.api.models.cuaca;

import com.google.gson.annotations.SerializedName;

public class WindSpeedItem{

	@SerializedName("tanggal")
	private String tanggal;

	@SerializedName("kecepatanangin")
	private String kecepatanangin;

	public void setTanggal(String tanggal){
		this.tanggal = tanggal;
	}

	public String getTanggal(){
		return tanggal;
	}

	public void setKecepatanangin(String kecepatanangin){
		this.kecepatanangin = kecepatanangin;
	}

	public String getKecepatanangin(){
		return kecepatanangin;
	}
}