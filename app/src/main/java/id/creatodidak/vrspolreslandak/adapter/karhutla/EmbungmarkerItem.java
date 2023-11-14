package id.creatodidak.vrspolreslandak.adapter.karhutla;

import com.google.gson.annotations.SerializedName;

public class EmbungmarkerItem{

	@SerializedName("nama")
	private String nama;

	@SerializedName("foto")
	private String foto;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("kode")
	private String kode;

	@SerializedName("latitude")
	private Object latitude;

	@SerializedName("kecamatan")
	private String kecamatan;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("kapasitas")
	private String kapasitas;

	@SerializedName("type")
	private String type;

	@SerializedName("longitude")
	private Object longitude;

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

	public void setKode(String kode){
		this.kode = kode;
	}

	public String getKode(){
		return kode;
	}

	public void setLatitude(Object latitude){
		this.latitude = latitude;
	}

	public Object getLatitude(){
		return latitude;
	}

	public void setKecamatan(String kecamatan){
		this.kecamatan = kecamatan;
	}

	public String getKecamatan(){
		return kecamatan;
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

	public void setKapasitas(String kapasitas){
		this.kapasitas = kapasitas;
	}

	public String getKapasitas(){
		return kapasitas;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setLongitude(Object longitude){
		this.longitude = longitude;
	}

	public Object getLongitude(){
		return longitude;
	}
}