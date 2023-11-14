package id.creatodidak.vrspolreslandak.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllLaporanCekEmbung{

	@SerializedName("listlaporancekembung")
	private List<ListlaporancekembungItem> listlaporancekembung;

	public void setListlaporancekembung(List<ListlaporancekembungItem> listlaporancekembung){
		this.listlaporancekembung = listlaporancekembung;
	}

	public List<ListlaporancekembungItem> getListlaporancekembung(){
		return listlaporancekembung;
	}
}