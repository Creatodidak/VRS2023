package id.creatodidak.vrspolreslandak.api.models.karhutla;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ListVerCekHotspot{

	@SerializedName("listvercekhotspot")
	private List<ListvercekhotspotItem> listvercekhotspot;

	public void setListvercekhotspot(List<ListvercekhotspotItem> listvercekhotspot){
		this.listvercekhotspot = listvercekhotspot;
	}

	public List<ListvercekhotspotItem> getListvercekhotspot(){
		return listvercekhotspot;
	}
}