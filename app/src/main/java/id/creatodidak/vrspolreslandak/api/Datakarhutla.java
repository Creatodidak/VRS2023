package id.creatodidak.vrspolreslandak.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datakarhutla{

	@SerializedName("daftarhotspot")
	private List<DaftarhotspotItem> daftarhotspot;

	@SerializedName("daftarwilayahkarhutla")
	private List<DaftarwilayahkarhutlaItem> daftarwilayahkarhutla;

	@SerializedName("tabeldata")
	private Tabeldata tabeldata;

	@SerializedName("ringkasan")
	private Ringkasan ringkasan;

	public void setDaftarhotspot(List<DaftarhotspotItem> daftarhotspot){
		this.daftarhotspot = daftarhotspot;
	}

	public List<DaftarhotspotItem> getDaftarhotspot(){
		return daftarhotspot;
	}

	public void setDaftarwilayahkarhutla(List<DaftarwilayahkarhutlaItem> daftarwilayahkarhutla){
		this.daftarwilayahkarhutla = daftarwilayahkarhutla;
	}

	public List<DaftarwilayahkarhutlaItem> getDaftarwilayahkarhutla(){
		return daftarwilayahkarhutla;
	}

	public void setTabeldata(Tabeldata tabeldata){
		this.tabeldata = tabeldata;
	}

	public Tabeldata getTabeldata(){
		return tabeldata;
	}

	public void setRingkasan(Ringkasan ringkasan){
		this.ringkasan = ringkasan;
	}

	public Ringkasan getRingkasan(){
		return ringkasan;
	}
}