package id.creatodidak.vrspolreslandak.api.models.stunting;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dokumentasi{

	@SerializedName("dokumentasi")
	private List<DokumentasiItem> dokumentasi;

	@SerializedName("ada")
	private boolean ada;

	public List<DokumentasiItem> getDokumentasi(){
		return dokumentasi;
	}

	public boolean isAda(){
		return ada;
	}
}