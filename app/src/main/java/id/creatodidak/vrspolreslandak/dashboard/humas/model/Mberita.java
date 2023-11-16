package id.creatodidak.vrspolreslandak.dashboard.humas.model;

import com.google.gson.annotations.SerializedName;

public class Mberita{

	@SerializedName("berita")
	private Berita berita;

	public void setBerita(Berita berita){
		this.berita = berita;
	}

	public Berita getBerita(){
		return berita;
	}
}