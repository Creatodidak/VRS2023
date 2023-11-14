package id.creatodidak.vrspolreslandak.api.models.karhutla;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllKarhutla{

	@SerializedName("ListHotspot")
	private List<ListHotspotItem> listHotspot;

	public void setListHotspot(List<ListHotspotItem> listHotspot){
		this.listHotspot = listHotspot;
	}

	public List<ListHotspotItem> getListHotspot(){
		return listHotspot;
	}
}