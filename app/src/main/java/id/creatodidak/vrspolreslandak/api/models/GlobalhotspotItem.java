package id.creatodidak.vrspolreslandak.api.models;

import com.google.gson.annotations.SerializedName;

public class GlobalhotspotItem{

	@SerializedName("polsek")
	private String polsek;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("confidence")
	private String confidence;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("pangkat")
	private String pangkat;

	@SerializedName("geocoder")
	private String geocoder;

	@SerializedName("wilayah")
	private String wilayah;

	@SerializedName("satfung")
	private String satfung;

	@SerializedName("nrp")
	private String nrp;

	@SerializedName("notif")
	private String notif;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("nama")
	private String nama;

	@SerializedName("foto")
	private String foto;

	@SerializedName("data_id")
	private String dataId;

	@SerializedName("response")
	private String response;

	@SerializedName("satker")
	private String satker;

	@SerializedName("kecamatan")
	private String kecamatan;

	@SerializedName("location")
	private String location;

	@SerializedName("id")
	private int id;

	@SerializedName("radius")
	private String radius;

	@SerializedName("longitude")
	private String longitude;

	@SerializedName("status")
	private String status;

	public void setPolsek(String polsek){
		this.polsek = polsek;
	}

	public String getPolsek(){
		return polsek;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setConfidence(String confidence){
		this.confidence = confidence;
	}

	public String getConfidence(){
		return confidence;
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

	public void setNotif(String notif){
		this.notif = notif;
	}

	public String getNotif(){
		return notif;
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

	public void setFoto(String foto){
		this.foto = foto;
	}

	public String getFoto(){
		return foto;
	}

	public void setDataId(String dataId){
		this.dataId = dataId;
	}

	public String getDataId(){
		return dataId;
	}

	public void setResponse(String response){
		this.response = response;
	}

	public String getResponse(){
		return response;
	}

	public void setSatker(String satker){
		this.satker = satker;
	}

	public String getSatker(){
		return satker;
	}

	public void setKecamatan(String kecamatan){
		this.kecamatan = kecamatan;
	}

	public String getKecamatan(){
		return kecamatan;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getLocation(){
		return location;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setRadius(String radius){
		this.radius = radius;
	}

	public String getRadius(){
		return radius;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLongitude(){
		return longitude;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}