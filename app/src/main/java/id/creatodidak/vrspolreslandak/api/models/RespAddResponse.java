package id.creatodidak.vrspolreslandak.api.models;

import com.google.gson.annotations.SerializedName;

public class RespAddResponse{

	@SerializedName("msg")
	private String msg;

	@SerializedName("newresponse")
	private Newresponse newresponse;

	@SerializedName("success")
	private boolean success;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setNewresponse(Newresponse newresponse){
		this.newresponse = newresponse;
	}

	public Newresponse getNewresponse(){
		return newresponse;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}
}