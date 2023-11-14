package id.creatodidak.vrspolreslandak.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import id.creatodidak.vrspolreslandak.api.models.karhutla.DatakampanyekarhutlaItem;

public class RGetKampanyeKarhutla{

	@SerializedName("datakampanyekarhutla")
	private List<DatakampanyekarhutlaItem> datakampanyekarhutla;

	@SerializedName("ada")
	private boolean ada;

	public void setDatakampanyekarhutla(List<DatakampanyekarhutlaItem> datakampanyekarhutla){
		this.datakampanyekarhutla = datakampanyekarhutla;
	}

	public List<DatakampanyekarhutlaItem> getDatakampanyekarhutla(){
		return datakampanyekarhutla;
	}

	public void setAda(boolean ada){
		this.ada = ada;
	}

	public boolean isAda(){
		return ada;
	}
}