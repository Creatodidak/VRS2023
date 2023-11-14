package id.creatodidak.vrspolreslandak.adapter.karhutla;

import com.google.gson.annotations.SerializedName;

public class UpdateLokasiHotspot{

	@SerializedName("lokasi")
	private String lokasi;

	@SerializedName("id")
	private int id;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setLokasi(String lokasi){
		this.lokasi = lokasi;
	}

	public String getLokasi(){
		return lokasi;
	}
}