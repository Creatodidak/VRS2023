package id.creatodidak.vrspolreslandak.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DraftHotspot{

	@SerializedName("DraftHotspot")
	private List<DraftHotspotItem> draftHotspot;

	public void setDraftHotspot(List<DraftHotspotItem> draftHotspot){
		this.draftHotspot = draftHotspot;
	}

	public List<DraftHotspotItem> getDraftHotspot(){
		return draftHotspot;
	}
}