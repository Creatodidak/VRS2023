package id.creatodidak.vrspolreslandak.api;

import com.google.gson.annotations.SerializedName;

public class Ringkasan{

	@SerializedName("total")
	private int total;

	@SerializedName("high")
	private int high;

	@SerializedName("low")
	private int low;

	@SerializedName("med")
	private int med;

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

	public void setMed(int med){
		this.med = med;
	}

	public int getMed(){
		return med;
	}
}