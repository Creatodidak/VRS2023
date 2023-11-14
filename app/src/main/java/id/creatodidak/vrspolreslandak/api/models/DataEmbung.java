package id.creatodidak.vrspolreslandak.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataEmbung{

	@SerializedName("embung")
	private List<EmbungItem> embung;

	@SerializedName("status")
	private boolean status;

	public void setEmbung(List<EmbungItem> embung){
		this.embung = embung;
	}

	public List<EmbungItem> getEmbung(){
		return embung;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}
}