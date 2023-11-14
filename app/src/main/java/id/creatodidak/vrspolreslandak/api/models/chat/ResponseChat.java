package id.creatodidak.vrspolreslandak.api.models.chat;

import com.google.gson.annotations.SerializedName;

public class ResponseChat{

	@SerializedName("msg")
	private String msg;

	@SerializedName("data")
	private String data;

	@SerializedName("berhasil")
	private boolean berhasil;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setData(String data){
		this.data = data;
	}

	public String getData(){
		return data;
	}

	public void setBerhasil(boolean berhasil){
		this.berhasil = berhasil;
	}

	public boolean isBerhasil(){
		return berhasil;
	}
}