package id.creatodidak.vrspolreslandak.auth;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ModelJabatan{

	@SerializedName("jabatan")
	private List<JabatanItem> jabatan;

	public void setJabatan(List<JabatanItem> jabatan){
		this.jabatan = jabatan;
	}

	public List<JabatanItem> getJabatan(){
		return jabatan;
	}
}