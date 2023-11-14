package id.creatodidak.vrspolreslandak.auth;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ModelProfile{

	@SerializedName("jabatan")
	private List<JabatanItem> jabatan;

	@SerializedName("profile")
	private List<ProfileItem> profile;

	@SerializedName("satker")
	private List<SatkerItem> satker;

	@SerializedName("pangkat")
	private List<PangkatItem> pangkat;

	@SerializedName("satfung")
	private List<SatfungItem> satfung;

	public void setJabatan(List<JabatanItem> jabatan){
		this.jabatan = jabatan;
	}

	public List<JabatanItem> getJabatan(){
		return jabatan;
	}

	public void setProfile(List<ProfileItem> profile){
		this.profile = profile;
	}

	public List<ProfileItem> getProfile(){
		return profile;
	}

	public void setSatker(List<SatkerItem> satker){
		this.satker = satker;
	}

	public List<SatkerItem> getSatker(){
		return satker;
	}

	public void setPangkat(List<PangkatItem> pangkat){
		this.pangkat = pangkat;
	}

	public List<PangkatItem> getPangkat(){
		return pangkat;
	}

	public void setSatfung(List<SatfungItem> satfung){
		this.satfung = satfung;
	}

	public List<SatfungItem> getSatfung(){
		return satfung;
	}
}