package id.creatodidak.vrspolreslandak.dashboard.humas.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class News{

	@SerializedName("res")
	private List<ResItem> res;

	@SerializedName("other")
	private List<OtherItem> other;

	public void setRes(List<ResItem> res){
		this.res = res;
	}

	public List<ResItem> getRes(){
		return res;
	}

	public void setOther(List<OtherItem> other){
		this.other = other;
	}

	public List<OtherItem> getOther(){
		return other;
	}
}