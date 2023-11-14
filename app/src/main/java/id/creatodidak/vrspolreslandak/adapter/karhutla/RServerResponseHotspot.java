package id.creatodidak.vrspolreslandak.adapter.karhutla;

import com.google.gson.annotations.SerializedName;

public class RServerResponseHotspot{

	@SerializedName("msg")
	private String msg;

	@SerializedName("berhasil")
	private boolean berhasil;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setBerhasil(boolean berhasil){
		this.berhasil = berhasil;
	}

	public boolean isBerhasil(){
		return berhasil;
	}
}