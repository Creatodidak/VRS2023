package id.creatodidak.vrspolreslandak.api.models.cuaca;

import com.google.gson.annotations.SerializedName;

public class WeatherItem{

	@SerializedName("cuaca")
	private String cuaca;

	@SerializedName("kode")
	private String kode;

	@SerializedName("tanggal")
	private String tanggal;

	public void setCuaca(String cuaca){
		this.cuaca = cuaca;
	}

	public String getCuaca(){
		return cuaca;
	}

	public void setKode(String kode){
		this.kode = kode;
	}

	public String getKode(){
		return kode;
	}

	public void setTanggal(String tanggal){
		this.tanggal = tanggal;
	}

	public String getTanggal(){
		return tanggal;
	}
}