package id.creatodidak.vrspolreslandak.api.models.cuaca;

import com.google.gson.annotations.SerializedName;

public class TemperatureItem{

	@SerializedName("temp")
	private String temp;

	@SerializedName("tanggal")
	private String tanggal;

	public void setTemp(String temp){
		this.temp = temp;
	}

	public String getTemp(){
		return temp;
	}

	public void setTanggal(String tanggal){
		this.tanggal = tanggal;
	}

	public String getTanggal(){
		return tanggal;
	}
}