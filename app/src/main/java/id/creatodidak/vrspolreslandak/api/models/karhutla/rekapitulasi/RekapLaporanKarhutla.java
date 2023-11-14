package id.creatodidak.vrspolreslandak.api.models.karhutla.rekapitulasi;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RekapLaporanKarhutla{

	@SerializedName("rekapkampanye")
	private List<RekapkampanyeItem> rekapkampanye;

	@SerializedName("rekapcekembung")
	private List<RekapcekembungItem> rekapcekembung;

	@SerializedName("rekapkarhutla")
	private List<RekapkarhutlaItem> rekapkarhutla;

	@SerializedName("rekapcekhotspot")
	private List<RekapcekhotspotItem> rekapcekhotspot;

	@SerializedName("token")
	private String token;

	public String getToken() {
		return token;
	}

	public void setRekapkampanye(List<RekapkampanyeItem> rekapkampanye){
		this.rekapkampanye = rekapkampanye;
	}

	public List<RekapkampanyeItem> getRekapkampanye(){
		return rekapkampanye;
	}

	public void setRekapcekembung(List<RekapcekembungItem> rekapcekembung){
		this.rekapcekembung = rekapcekembung;
	}

	public List<RekapcekembungItem> getRekapcekembung(){
		return rekapcekembung;
	}

	public void setRekapkarhutla(List<RekapkarhutlaItem> rekapkarhutla){
		this.rekapkarhutla = rekapkarhutla;
	}

	public List<RekapkarhutlaItem> getRekapkarhutla(){
		return rekapkarhutla;
	}

	public void setRekapcekhotspot(List<RekapcekhotspotItem> rekapcekhotspot){
		this.rekapcekhotspot = rekapcekhotspot;
	}

	public List<RekapcekhotspotItem> getRekapcekhotspot(){
		return rekapcekhotspot;
	}
}