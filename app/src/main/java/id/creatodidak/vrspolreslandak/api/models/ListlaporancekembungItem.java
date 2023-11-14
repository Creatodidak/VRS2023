package id.creatodidak.vrspolreslandak.api.models;

import com.google.gson.annotations.SerializedName;

public class ListlaporancekembungItem{

	@SerializedName("verifikasi")
	private String verifikasi;

	@SerializedName("cuaca")
	private String cuaca;

	@SerializedName("analisa")
	private String analisa;

	@SerializedName("pelaksana")
	private String pelaksana;

	@SerializedName("latitude")
	private double latitude;

	@SerializedName("kodeembung")
	private String kodeembung;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("geocoder")
	private String geocoder;

	@SerializedName("prediksi")
	private String prediksi;

	@SerializedName("personil")
	private String personil;

	@SerializedName("dataid")
	private String dataid;

	@SerializedName("foto")
	private String foto;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("volumeair")
	private String volumeair;

	@SerializedName("rekomendasi")
	private String rekomendasi;

	@SerializedName("id")
	private int id;

	@SerializedName("intensitasair")
	private String intensitasair;

	@SerializedName("longitude")
	private double longitude;

	private boolean local;

	public void setLocal(boolean local){this.local = local;}
	public boolean isLocal(){return local;}
	public void setVerifikasi(String verifikasi){
		this.verifikasi = verifikasi;
	}

	public String getVerifikasi(){
		return verifikasi;
	}

	public void setCuaca(String cuaca){
		this.cuaca = cuaca;
	}

	public String getCuaca(){
		return cuaca;
	}

	public void setAnalisa(String analisa){
		this.analisa = analisa;
	}

	public String getAnalisa(){
		return analisa;
	}

	public void setPelaksana(String pelaksana){
		this.pelaksana = pelaksana;
	}

	public String getPelaksana(){
		return pelaksana;
	}

	public void setLatitude(double latitude){
		this.latitude = latitude;
	}

	public double getLatitude(){
		return latitude;
	}

	public void setKodeembung(String kodeembung){
		this.kodeembung = kodeembung;
	}

	public String getKodeembung(){
		return kodeembung;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setGeocoder(String geocoder){
		this.geocoder = geocoder;
	}

	public String getGeocoder(){
		return geocoder;
	}

	public void setPrediksi(String prediksi){
		this.prediksi = prediksi;
	}

	public String getPrediksi(){
		return prediksi;
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

	public void setVolumeair(String volumeair){
		this.volumeair = volumeair;
	}

	public String getVolumeair(){
		return volumeair;
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

	public void setIntensitasair(String intensitasair){
		this.intensitasair = intensitasair;
	}

	public String getIntensitasair(){
		return intensitasair;
	}

	public void setLongitude(double longitude){
		this.longitude = longitude;
	}

	public double getLongitude(){
		return longitude;
	}
}