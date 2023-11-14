package id.creatodidak.vrspolreslandak.adapter.karhutla;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllMarker{

	@SerializedName("embungmarker")
	private List<EmbungmarkerItem> embungmarker;

	@SerializedName("hotspotmarker")
	private List<HotspotmarkerItem> hotspotmarker;

	@SerializedName("mako")
	private List<MakoItem> mako;

	public void setEmbungmarker(List<EmbungmarkerItem> embungmarker){
		this.embungmarker = embungmarker;
	}

	public List<EmbungmarkerItem> getEmbungmarker(){
		return embungmarker;
	}

	public void setHotspotmarker(List<HotspotmarkerItem> hotspotmarker){
		this.hotspotmarker = hotspotmarker;
	}

	public List<HotspotmarkerItem> getHotspotmarker(){
		return hotspotmarker;
	}

	public void setMako(List<MakoItem> mako){
		this.mako = mako;
	}

	public List<MakoItem> getMako(){
		return mako;
	}
}