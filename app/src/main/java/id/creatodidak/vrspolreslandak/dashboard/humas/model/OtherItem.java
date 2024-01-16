package id.creatodidak.vrspolreslandak.dashboard.humas.model;

import com.google.gson.annotations.SerializedName;

public class OtherItem{

	@SerializedName("pers")
	private String pers;

	@SerializedName("link")
	private String link;

	@SerializedName("caption")
	private String caption;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("kategori")
	private String kategori;

	@SerializedName("gambar")
	private String gambar;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("satker")
	private String satker;

	@SerializedName("id")
	private int id;

	@SerializedName("judul")
	private String judul;

	@SerializedName("isi")
	private String isi;

	@SerializedName("views")
	private String views;

	@SerializedName("hashtag")
	private String hashtag;

	public void setPers(String pers){
		this.pers = pers;
	}

	public String getPers(){
		return pers;
	}

	public void setLink(String link){
		this.link = link;
	}

	public String getLink(){
		return link;
	}

	public void setCaption(String caption){
		this.caption = caption;
	}

	public String getCaption(){
		return caption;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setKategori(String kategori){
		this.kategori = kategori;
	}

	public String getKategori(){
		return kategori;
	}

	public void setGambar(String gambar){
		this.gambar = gambar;
	}

	public String getGambar(){
		return gambar;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setSatker(String satker){
		this.satker = satker;
	}

	public String getSatker(){
		return satker;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setJudul(String judul){
		this.judul = judul;
	}

	public String getJudul(){
		return judul;
	}

	public void setIsi(String isi){
		this.isi = isi;
	}

	public String getIsi(){
		return isi;
	}

	public void setViews(String views){
		this.views = views;
	}

	public String getViews(){
		return views;
	}

	public void setHashtag(String hashtag){
		this.hashtag = hashtag;
	}

	public String getHashtag(){
		return hashtag;
	}
}