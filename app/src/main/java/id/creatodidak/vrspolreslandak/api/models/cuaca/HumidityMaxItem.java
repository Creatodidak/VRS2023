package id.creatodidak.vrspolreslandak.api.models.cuaca;

import com.google.gson.annotations.SerializedName;

public class HumidityMaxItem{

	@SerializedName("tanggal")
	private String tanggal;

	@SerializedName("hum_max")
	private String humMax;

	public void setTanggal(String tanggal){
		this.tanggal = tanggal;
	}

	public String getTanggal(){
		return tanggal;
	}

	public void setHumMax(String humMax){
		this.humMax = humMax;
	}

	public String getHumMax(){
		return humMax;
	}
}