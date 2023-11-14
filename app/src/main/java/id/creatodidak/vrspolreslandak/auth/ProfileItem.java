package id.creatodidak.vrspolreslandak.auth;

import com.google.gson.annotations.SerializedName;

public class ProfileItem{

	@SerializedName("nik")
	private String nik;

	@SerializedName("nama")
	private String nama;

	@SerializedName("foto")
	private String foto;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("jabatan")
	private String jabatan;

	@SerializedName("satker")
	private String satker;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("pangkat")
	private String pangkat;

	@SerializedName("wa")
	private String wa;

	@SerializedName("satfung")
	private String satfung;

	@SerializedName("nrp")
	private String nrp;

	@SerializedName("tanggal_lahir")
	private String tanggalLahir;

	public void setNik(String nik){
		this.nik = nik;
	}

	public String getNik(){
		return nik;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setFoto(String foto){
		this.foto = foto;
	}

	public String getFoto(){
		return foto;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setJabatan(String jabatan){
		this.jabatan = jabatan;
	}

	public String getJabatan(){
		return jabatan;
	}

	public void setSatker(String satker){
		this.satker = satker;
	}

	public String getSatker(){
		return satker;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setPangkat(String pangkat){
		this.pangkat = pangkat;
	}

	public String getPangkat(){
		return pangkat;
	}

	public void setWa(String wa){
		this.wa = wa;
	}

	public String getWa(){
		return wa;
	}

	public void setSatfung(String satfung){
		this.satfung = satfung;
	}

	public String getSatfung(){
		return satfung;
	}

	public void setNrp(String nrp){
		this.nrp = nrp;
	}

	public String getNrp(){
		return nrp;
	}

	public void setTanggalLahir(String tanggalLahir){
		this.tanggalLahir = tanggalLahir;
	}

	public String getTanggalLahir(){
		return tanggalLahir;
	}
}