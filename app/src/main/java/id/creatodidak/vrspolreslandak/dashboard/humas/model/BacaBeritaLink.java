package id.creatodidak.vrspolreslandak.dashboard.humas.model;

import com.google.gson.annotations.SerializedName;

public class BacaBeritaLink{

	@SerializedName("res")
	private Res res;

	@SerializedName("userdata")
	private Userdata userdata;

	@SerializedName("params")
	private String params;

	public void setRes(Res res){
		this.res = res;
	}

	public Res getRes(){
		return res;
	}

	public void setUserdata(Userdata userdata){
		this.userdata = userdata;
	}

	public Userdata getUserdata(){
		return userdata;
	}

	public void setParams(String params){
		this.params = params;
	}

	public String getParams(){
		return params;
	}
}