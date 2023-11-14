package id.creatodidak.vrspolreslandak.api.models.karhutla;

import com.google.gson.annotations.SerializedName;

public class RekapItem{

	@SerializedName("wilkum")
	private String wilkum;

	@SerializedName("total")
	private int total;

	@SerializedName("high")
	private int high;

	@SerializedName("low")
	private int low;

	@SerializedName("kecamatan")
	private String kecamatan;

	@SerializedName("medium")
	private int medium;

	@SerializedName("bdiresponse")
	private int bdiresponse;

	@SerializedName("diresponse")
	private int diresponse;

	public void setWilkum(String wilkum){
		this.wilkum = wilkum;
	}

	public String getWilkum(){
		return wilkum;
	}

	public void setTotal(int total){
		this.total = total;
	}

	public int getTotal(){
		return total;
	}

	public void setHigh(int high){
		this.high = high;
	}

	public int getHigh(){
		return high;
	}

	public void setLow(int low){
		this.low = low;
	}

	public int getLow(){
		return low;
	}

	public void setKecamatan(String kecamatan){
		this.kecamatan = kecamatan;
	}

	public String getKecamatan(){
		return kecamatan;
	}

	public void setMedium(int medium){
		this.medium = medium;
	}

	public int getMedium(){
		return medium;
	}

	public void setBdiresponse(int bdiresponse){
		this.bdiresponse = bdiresponse;
	}

	public int getBdiresponse(){
		return bdiresponse;
	}

	public void setDiresponse(int diresponse){
		this.diresponse = diresponse;
	}

	public int getDiresponse(){
		return diresponse;
	}
}