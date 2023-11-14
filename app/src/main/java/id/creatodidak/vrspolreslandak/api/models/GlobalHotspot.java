package id.creatodidak.vrspolreslandak.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GlobalHotspot{

	@SerializedName("globalhotspotsatker")
	private List<GlobalhotspotsatkerItem> globalhotspotsatker;

	@SerializedName("globalhotspotembung")
	private List<GlobalhotspotembungItem> globalhotspotembung;

	@SerializedName("globalhotspot")
	private List<GlobalhotspotItem> globalhotspot;

	public void setGlobalhotspotsatker(List<GlobalhotspotsatkerItem> globalhotspotsatker){
		this.globalhotspotsatker = globalhotspotsatker;
	}

	public List<GlobalhotspotsatkerItem> getGlobalhotspotsatker(){
		return globalhotspotsatker;
	}

	public void setGlobalhotspotembung(List<GlobalhotspotembungItem> globalhotspotembung){
		this.globalhotspotembung = globalhotspotembung;
	}

	public List<GlobalhotspotembungItem> getGlobalhotspotembung(){
		return globalhotspotembung;
	}

	public void setGlobalhotspot(List<GlobalhotspotItem> globalhotspot){
		this.globalhotspot = globalhotspot;
	}

	public List<GlobalhotspotItem> getGlobalhotspot(){
		return globalhotspot;
	}
}