package id.creatodidak.vrspolreslandak.api.models;

import com.google.gson.annotations.SerializedName;

public class DraftHotspotItem{

	@SerializedName("kondisi")
	private String kondisi;

	@SerializedName("foto")
	private String foto;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("data_id")
	private String dataId;

	@SerializedName("rincian")
	private String rincian;

	@SerializedName("lokasi")
	private String lokasi;

	@SerializedName("penyebab")
	private String penyebab;

	@SerializedName("pemilik")
	private String pemilik;

	@SerializedName("luas")
	private String luas;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	public void setKondisi(String kondisi){
		this.kondisi = kondisi;
	}

	public String getKondisi(){
		return kondisi;
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

	public void setDataId(String dataId){
		this.dataId = dataId;
	}

	public String getDataId(){
		return dataId;
	}

	public void setRincian(String rincian){
		this.rincian = rincian;
	}

	public String getRincian(){
		return rincian;
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

	public void setPemilik(String pemilik){
		this.pemilik = pemilik;
	}

	public String getPemilik(){
		return pemilik;
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

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}