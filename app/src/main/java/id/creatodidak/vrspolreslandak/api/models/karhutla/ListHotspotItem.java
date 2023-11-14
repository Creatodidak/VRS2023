package id.creatodidak.vrspolreslandak.api.models.karhutla;

import com.google.gson.annotations.SerializedName;

public class ListHotspotItem{

	@SerializedName("verifikasi")
	private String verifikasi;

	@SerializedName("latlap")
	private double latlap;

	@SerializedName("pelaksanakegiatan")
	private String pelaksanakegiatan;

	@SerializedName("luas")
	private String luas;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("prediksi")
	private String prediksi;

	@SerializedName("kondisiapi")
	private String kondisiapi;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("longlap")
	private double longlap;

	@SerializedName("tindakan")
	private String tindakan;

	@SerializedName("rekomendasi")
	private String rekomendasi;

	@SerializedName("id")
	private int id;

	@SerializedName("longhs")
	private double longhs;

	@SerializedName("notifikasi")
	private String notifikasi;

	@SerializedName("analisa")
	private String analisa;

	@SerializedName("riwayat")
	private String riwayat;

	@SerializedName("confidence")
	private String confidence;

	@SerializedName("responder")
	private String responder;

	@SerializedName("penyebabapi")
	private String penyebabapi;

	@SerializedName("pemiliklahan")
	private String pemiliklahan;

	@SerializedName("foto")
	private String foto;

	@SerializedName("laths")
	private double laths;

	@SerializedName("lokasi")
	private String lokasi;

	@SerializedName("satker")
	private String satker;

	@SerializedName("kecamatan")
	private String kecamatan;

	@SerializedName("satelit")
	private String satelit;

	private String local;
	public void setLocal(String local){
		this.local = local;
	}

	public String getLocal(){
		return local;
	}

	private final boolean isViewed = false;
	public void setVerifikasi(String verifikasi){
		this.verifikasi = verifikasi;
	}

	public String getVerifikasi(){
		return verifikasi;
	}

	public void setLatlap(double latlap){
		this.latlap = latlap;
	}

	public double getLatlap(){
		return latlap;
	}

	public void setPelaksanakegiatan(String pelaksanakegiatan){
		this.pelaksanakegiatan = pelaksanakegiatan;
	}

	public String getPelaksanakegiatan(){
		return pelaksanakegiatan;
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

	public void setPrediksi(String prediksi){
		this.prediksi = prediksi;
	}

	public String getPrediksi(){
		return prediksi;
	}

	public void setKondisiapi(String kondisiapi){
		this.kondisiapi = kondisiapi;
	}

	public String getKondisiapi(){
		return kondisiapi;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setLonglap(double longlap){
		this.longlap = longlap;
	}

	public double getLonglap(){
		return longlap;
	}

	public void setTindakan(String tindakan){
		this.tindakan = tindakan;
	}

	public String getTindakan(){
		return tindakan;
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


	public void setLonghs(double longhs){
		this.longhs = longhs;
	}

	public double getLonghs(){
		return longhs;
	}

	public void setNotifikasi(String notifikasi){
		this.notifikasi = notifikasi;
	}

	public String getNotifikasi(){
		return notifikasi;
	}

	public void setAnalisa(String analisa){
		this.analisa = analisa;
	}

	public String getAnalisa(){
		return analisa;
	}

	public void setRiwayat(String riwayat){
		this.riwayat = riwayat;
	}

	public String getRiwayat(){
		return riwayat;
	}

	public void setConfidence(String confidence){
		this.confidence = confidence;
	}

	public String getConfidence(){
		return confidence;
	}

	public void setResponder(String responder){
		this.responder = responder;
	}

	public String getResponder(){
		return responder;
	}

	public void setPenyebabapi(String penyebabapi){
		this.penyebabapi = penyebabapi;
	}

	public String getPenyebabapi(){
		return penyebabapi;
	}

	public void setPemiliklahan(String pemiliklahan){
		this.pemiliklahan = pemiliklahan;
	}

	public String getPemiliklahan(){
		return pemiliklahan;
	}

	public void setFoto(String foto){
		this.foto = foto;
	}

	public String getFoto(){
		return foto;
	}

	public void setLaths(double laths){
		this.laths = laths;
	}

	public double getLaths(){
		return laths;
	}

	public void setLokasi(String lokasi){
		this.lokasi = lokasi;
	}

	public String getLokasi(){
		return lokasi;
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

	public void setSatelit(String satelit){
		this.satelit = satelit;
	}

	public String getSatelit(){
		return satelit;
	}
}