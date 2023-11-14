package id.creatodidak.vrspolreslandak.auth;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ModelSatfungJabatan{

	@SerializedName("jabatan")
	private List<JabatanItem> jabatan;

	@SerializedName("satfung")
	private List<SatfungItem> satfung;

	public void setJabatan(List<JabatanItem> jabatan){
		this.jabatan = jabatan;
	}

	public List<JabatanItem> getJabatan(){
		return jabatan;
	}

	public void setSatfung(List<SatfungItem> satfung){
		this.satfung = satfung;
	}

	public List<SatfungItem> getSatfung(){
		return satfung;
	}
}