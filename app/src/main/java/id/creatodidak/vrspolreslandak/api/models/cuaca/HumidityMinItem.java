package id.creatodidak.vrspolreslandak.api.models.cuaca;

import com.google.gson.annotations.SerializedName;

public class HumidityMinItem{

	@SerializedName("hum_min")
	private String humMin;

	@SerializedName("tanggal")
	private String tanggal;

	public void setHumMin(String humMin){
		this.humMin = humMin;
	}

	public String getHumMin(){
		return humMin;
	}

	public void setTanggal(String tanggal){
		this.tanggal = tanggal;
	}

	public String getTanggal(){
		return tanggal;
	}
}