package id.creatodidak.vrspolreslandak.api.models.cuaca;

import com.google.gson.annotations.SerializedName;

public class HumidityItem{

	@SerializedName("hum")
	private String hum;

	@SerializedName("tanggal")
	private String tanggal;

	public void setHum(String hum){
		this.hum = hum;
	}

	public String getHum(){
		return hum;
	}

	public void setTanggal(String tanggal){
		this.tanggal = tanggal;
	}

	public String getTanggal(){
		return tanggal;
	}
}