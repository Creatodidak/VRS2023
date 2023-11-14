package id.creatodidak.vrspolreslandak.api.models.karhutla.rekapitulasi;

import com.google.gson.annotations.SerializedName;

public class RekapkampanyeItem{

	@SerializedName("jumlah")
	private int jumlah;

	@SerializedName("satker")
	private String satker;

	public void setJumlah(int jumlah){
		this.jumlah = jumlah;
	}

	public int getJumlah(){
		return jumlah;
	}

	public void setSatker(String satker){
		this.satker = satker;
	}

	public String getSatker(){
		return satker;
	}
}