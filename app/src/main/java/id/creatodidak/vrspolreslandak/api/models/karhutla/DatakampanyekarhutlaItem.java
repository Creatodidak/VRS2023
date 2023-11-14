package id.creatodidak.vrspolreslandak.api.models.karhutla;

import com.google.gson.annotations.SerializedName;

public class DatakampanyekarhutlaItem{

	@SerializedName("personil")
	private String personil;

	@SerializedName("foto")
	private String foto;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("nama_target")
	private String namaTarget;

	@SerializedName("lokasi")
	private String lokasi;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("dataid")
	private String dataid;
	@SerializedName("latitude")
	private double latitude;

	@SerializedName("verifikasi")
	private String verifikasi;

	private boolean isLocal;

	@SerializedName("analisa")
	private String analisa;

	@SerializedName("prediksi")
	private String prediksi;

	@SerializedName("rekomendasi")
	private String rekomendasi;

	@SerializedName("penyampaian")
	private String penyampaian;

	@SerializedName("longitude")
	private double longitude;

	public void setLocal(boolean local) {
		isLocal = local;
	}

	public boolean isLocal() {
		return isLocal;
	}

	public String getDataid() {
		return dataid;
	}

	public void setDataid(String dataid) {
		this.dataid = dataid;
	}

	public void setPersonil(String personil){
		this.personil = personil;
	}

	public String getPersonil(){
		return personil;
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

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setLatitude(double latitude){
		this.latitude = latitude;
	}

	public double getLatitude(){
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getAnalisa() {
		return analisa;
	}

	public String getPrediksi() {
		return prediksi;
	}

	public String getRekomendasi() {
		return rekomendasi;
	}

	public void setAnalisa(String analisa) {
		this.analisa = analisa;
	}

	public void setPrediksi(String prediksi) {
		this.prediksi = prediksi;
	}

	public void setRekomendasi(String rekomendasi) {
		this.rekomendasi = rekomendasi;
	}

	public String getVerifikasi(){
		return verifikasi;
	}

	public String getPenyampaian() {
		return penyampaian;
	}

	public void setPenyampaian(String penyampaian) {
		this.penyampaian = penyampaian;
	}
}