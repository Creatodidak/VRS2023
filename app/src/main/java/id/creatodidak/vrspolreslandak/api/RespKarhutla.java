package id.creatodidak.vrspolreslandak.api;

import com.google.gson.annotations.SerializedName;

public class RespKarhutla{

	@SerializedName("success")
	private boolean success;

	@SerializedName("datakarhutla")
	private Datakarhutla datakarhutla;

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setDatakarhutla(Datakarhutla datakarhutla){
		this.datakarhutla = datakarhutla;
	}

	public Datakarhutla getDatakarhutla(){
		return datakarhutla;
	}
}