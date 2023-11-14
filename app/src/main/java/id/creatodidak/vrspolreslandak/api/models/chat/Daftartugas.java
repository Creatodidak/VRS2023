package id.creatodidak.vrspolreslandak.api.models.chat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Daftartugas{

	@SerializedName("daftartugas")
	private List<DaftartugasItem> daftartugas;

	public void setDaftartugas(List<DaftartugasItem> daftartugas){
		this.daftartugas = daftartugas;
	}

	public List<DaftartugasItem> getDaftartugas(){
		return daftartugas;
	}
}