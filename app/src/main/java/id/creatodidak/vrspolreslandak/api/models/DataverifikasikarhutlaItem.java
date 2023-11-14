package id.creatodidak.vrspolreslandak.api.models;

import com.google.gson.annotations.SerializedName;

public class DataverifikasikarhutlaItem{

	@SerializedName("verifikasi")
	private String verifikasi;

	@SerializedName("kondisi")
	private String kondisi;

	@SerializedName("rincian")
	private String rincian;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("luas")
	private String luas;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("terdeteksi")
	private String terdeteksi;

	@SerializedName("pangkat")
	private String pangkat;

	@SerializedName("geocoder")
	private String geocoder;

	@SerializedName("wilayah")
	private String wilayah;

	@SerializedName("foto")
	private String foto;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("nama")
	private String nama;

	@SerializedName("data_id")
	private String dataId;

	@SerializedName("lokasi")
	private String lokasi;

	@SerializedName("penyebab")
	private String penyebab;

	@SerializedName("satker")
	private String satker;

	@SerializedName("pemilik")
	private String pemilik;

	@SerializedName("id")
	private int id;

	@SerializedName("longitude")
	private String longitude;

	public void setVerifikasi(String verifikasi){
		this.verifikasi = verifikasi;
	}

	public String getVerifikasi(){
		return verifikasi;
	}

	public void setKondisi(String kondisi){
		this.kondisi = kondisi;
	}

	public String getKondisi(){
		return kondisi;
	}

	public void setRincian(String rincian){
		this.rincian = rincian;
	}

	public String getRincian(){
		return rincian;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setLuas(String luas){
		this.luas = luas;
	}

	public String getLuas(){
		return luas;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setTerdeteksi(String terdeteksi){
		this.terdeteksi = terdeteksi;
	}

	public String getTerdeteksi(){
		return terdeteksi;
	}

	public void setPangkat(String pangkat){
		this.pangkat = pangkat;
	}

	public String getPangkat(){
		return pangkat;
	}

	public void setGeocoder(String geocoder){
		this.geocoder = geocoder;
	}

	public String getGeocoder(){
		return geocoder;
	}

	public void setWilayah(String wilayah){
		this.wilayah = wilayah;
	}

	public String getWilayah(){
		return wilayah;
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

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setDataId(String dataId){
		this.dataId = dataId;
	}

	public String getDataId(){
		return dataId;
	}

	public void setLokasi(String lokasi){
		this.lokasi = lokasi;
	}

	public String getLokasi(){
		return lokasi;
	}

	public void setPenyebab(String penyebab){
		this.penyebab = penyebab;
	}

	public String getPenyebab(){
		return penyebab;
	}

	public void setSatker(String satker){
		this.satker = satker;
	}

	public String getSatker(){
		return satker;
	}

	public void setPemilik(String pemilik){
		this.pemilik = pemilik;
	}

	public String getPemilik(){
		return pemilik;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLongitude(){
		return longitude;
	}
}