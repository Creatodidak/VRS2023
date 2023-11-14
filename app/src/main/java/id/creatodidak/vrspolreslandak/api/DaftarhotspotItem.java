package id.creatodidak.vrspolreslandak.api;

import com.google.gson.annotations.SerializedName;

public class DaftarhotspotItem{

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("confidence")
	private String confidence;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("wilayah")
	private String wilayah;

	@SerializedName("notif")
	private String notif;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("data_id")
	private String dataId;

	@SerializedName("response")
	private Object response;

	@SerializedName("location")
	private String location;

	@SerializedName("id")
	private int id;

	@SerializedName("radius")
	private String radius;

	@SerializedName("longitude")
	private String longitude;

	@SerializedName("status")
	private Object status;

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

	public void setWilayah(String wilayah){
		this.wilayah = wilayah;
	}

	public String getWilayah(){
		return wilayah;
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

	public void setDataId(String dataId){
		this.dataId = dataId;
	}

	public String getDataId(){
		return dataId;
	}

	public void setResponse(Object response){
		this.response = response;
	}

	public Object getResponse(){
		return response;
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

	public void setStatus(Object status){
		this.status = status;
	}

	public Object getStatus(){
		return status;
	}
}