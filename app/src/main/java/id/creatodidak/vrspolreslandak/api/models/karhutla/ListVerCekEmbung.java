package id.creatodidak.vrspolreslandak.api.models.karhutla;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ListVerCekEmbung{

	@SerializedName("listvercekembung")
	private List<ListvercekembungItem> listvercekembung;

	public void setListvercekembung(List<ListvercekembungItem> listvercekembung){
		this.listvercekembung = listvercekembung;
	}

	public List<ListvercekembungItem> getListvercekembung(){
		return listvercekembung;
	}
}