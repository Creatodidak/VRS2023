package id.creatodidak.vrspolreslandak.api.models.cuaca;

import com.google.gson.annotations.SerializedName;

public class TemperatureMinItem{

	@SerializedName("temp_min")
	private String tempMin;

	@SerializedName("tanggal")
	private String tanggal;

	public void setTempMin(String tempMin){
		this.tempMin = tempMin;
	}

	public String getTempMin(){
		return tempMin;
	}

	public void setTanggal(String tanggal){
		this.tanggal = tanggal;
	}

	public String getTanggal(){
		return tanggal;
	}
}