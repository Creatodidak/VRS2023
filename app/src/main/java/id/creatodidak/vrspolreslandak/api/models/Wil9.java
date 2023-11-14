package id.creatodidak.vrspolreslandak.api.models;

import com.google.gson.annotations.SerializedName;

public class Wil9{

	@SerializedName("polsek")
	private String polsek;

	@SerializedName("total")
	private int total;

	@SerializedName("high")
	private int high;

	@SerializedName("bdirespon")
	private int bdirespon;

	@SerializedName("low")
	private int low;

	@SerializedName("direspon")
	private int direspon;

	@SerializedName("selesai")
	private int selesai;

	@SerializedName("kecamatan")
	private String kecamatan;

	@SerializedName("med")
	private int med;

	public void setPolsek(String polsek){
		this.polsek = polsek;
	}

	public String getPolsek(){
		return polsek;
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

	public void setBdirespon(int bdirespon){
		this.bdirespon = bdirespon;
	}

	public int getBdirespon(){
		return bdirespon;
	}

	public void setLow(int low){
		this.low = low;
	}

	public int getLow(){
		return low;
	}

	public void setDirespon(int direspon){
		this.direspon = direspon;
	}

	public int getDirespon(){
		return direspon;
	}

	public void setSelesai(int selesai){
		this.selesai = selesai;
	}

	public int getSelesai(){
		return selesai;
	}

	public void setKecamatan(String kecamatan){
		this.kecamatan = kecamatan;
	}

	public String getKecamatan(){
		return kecamatan;
	}

	public void setMed(int med){
		this.med = med;
	}

	public int getMed(){
		return med;
	}
}