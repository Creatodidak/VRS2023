package id.creatodidak.vrspolreslandak.api.models.cuaca;

import com.google.gson.annotations.SerializedName;

public class TemperatureMaxItem{

	@SerializedName("tanggal")
	private String tanggal;

	@SerializedName("temp_max")
	private String tempMax;

	public void setTanggal(String tanggal){
		this.tanggal = tanggal;
	}

	public String getTanggal(){
		return tanggal;
	}

	public void setTempMax(String tempMax){
		this.tempMax = tempMax;
	}

	public String getTempMax(){
		return tempMax;
	}
}