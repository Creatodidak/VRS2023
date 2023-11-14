package id.creatodidak.vrspolreslandak.api.models.karhutla;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ListverkampanyeItem implements Serializable {

	@SerializedName("verifikasi")
	private String verifikasi;

	@SerializedName("analisa")
	private String analisa;

	@SerializedName("jabatanpers")
	private String jabatanpers;

	@SerializedName("latitude")
	private Object latitude;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("penyampaian")
	private String penyampaian;

	@SerializedName("satkerpers")
	private String satkerpers;

	@SerializedName("prediksi")
	private String prediksi;

	@SerializedName("pangkatpers")
	private String pangkatpers;

	@SerializedName("personil")
	private String personil;

	@SerializedName("dataid")
	private String dataid;

	@SerializedName("foto")
	private String foto;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("namapers")
	private String namapers;

	@SerializedName("fotopers")
	private String fotopers;

	@SerializedName("nama_target")
	private String namaTarget;

	@SerializedName("lokasi")
	private String lokasi;

	@SerializedName("nrppers")
	private String nrppers;

	@SerializedName("rekomendasi")
	private String rekomendasi;

	@SerializedName("id")
	private int id;

	@SerializedName("satfungpers")
	private String satfungpers;

	@SerializedName("longitude")
	private Object longitude;

	public void setVerifikasi(String verifikasi){
		this.verifikasi = verifikasi;
	}

	public String getVerifikasi(){
		return verifikasi;
	}

	public void setAnalisa(String analisa){
		this.analisa = analisa;
	}

	public String getAnalisa(){
		return analisa;
	}

	public void setJabatanpers(String jabatanpers){
		this.jabatanpers = jabatanpers;
	}

	public String getJabatanpers(){
		return jabatanpers;
	}

	public void setLatitude(Object latitude){
		this.latitude = latitude;
	}

	public Object getLatitude(){
		return latitude;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setPenyampaian(String penyampaian){
		this.penyampaian = penyampaian;
	}

	public String getPenyampaian(){
		return penyampaian;
	}

	public void setSatkerpers(String satkerpers){
		this.satkerpers = satkerpers;
	}

	public String getSatkerpers(){
		return satkerpers;
	}

	public void setPrediksi(String prediksi){
		this.prediksi = prediksi;
	}

	public String getPrediksi(){
		return prediksi;
	}

	public void setPangkatpers(String pangkatpers){
		this.pangkatpers = pangkatpers;
	}

	public String getPangkatpers(){
		return pangkatpers;
	}

	public void setPersonil(String personil){
		this.personil = personil;
	}

	public String getPersonil(){
		return personil;
	}

	public void setDataid(String dataid){
		this.dataid = dataid;
	}

	public String getDataid(){
		return dataid;
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

	public void setNamapers(String namapers){
		this.namapers = namapers;
	}

	public String getNamapers(){
		return namapers;
	}

	public void setFotopers(String fotopers){
		this.fotopers = fotopers;
	}

	public String getFotopers(){
		return fotopers;
	}

	public void setNamaTarget(String namaTarget){
		this.namaTarget = namaTarget;
	}

	public String getNamaTarget(){
		return namaTarget;
	}

	public void setLokasi(String lokasi){
		this.lokasi = lokasi;
	}

	public String getLokasi(){
		return lokasi;
	}

	public void setNrppers(String nrppers){
		this.nrppers = nrppers;
	}

	public String getNrppers(){
		return nrppers;
	}

	public void setRekomendasi(String rekomendasi){
		this.rekomendasi = rekomendasi;
	}

	public String getRekomendasi(){
		return rekomendasi;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setSatfungpers(String satfungpers){
		this.satfungpers = satfungpers;
	}

	public String getSatfungpers(){
		return satfungpers;
	}

	public void setLongitude(Object longitude){
		this.longitude = longitude;
	}

	public Object getLongitude(){
		return longitude;
	}
}