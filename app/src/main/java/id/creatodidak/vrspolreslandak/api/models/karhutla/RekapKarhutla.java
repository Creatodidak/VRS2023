package id.creatodidak.vrspolreslandak.api.models.karhutla;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RekapKarhutla{

	@SerializedName("rekap")
	private List<RekapItem> rekap;

	public void setRekap(List<RekapItem> rekap){
		this.rekap = rekap;
	}

	public List<RekapItem> getRekap(){
		return rekap;
	}
}