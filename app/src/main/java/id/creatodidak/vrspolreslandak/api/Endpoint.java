package id.creatodidak.vrspolreslandak.api;

import java.util.List;

import id.creatodidak.vrspolreslandak.adapter.karhutla.AllMarker;
import id.creatodidak.vrspolreslandak.adapter.karhutla.RServerResponseHotspot;
import id.creatodidak.vrspolreslandak.adapter.karhutla.UpdateLokasiHotspot;
import id.creatodidak.vrspolreslandak.admin.model.ResponseNotif;
import id.creatodidak.vrspolreslandak.api.models.AllLaporanCekEmbung;
import id.creatodidak.vrspolreslandak.api.models.AtensiResponse;
import id.creatodidak.vrspolreslandak.api.models.DataEmbung;
import id.creatodidak.vrspolreslandak.api.models.GlobalHotspot;
import id.creatodidak.vrspolreslandak.api.models.LoginResponse;
import id.creatodidak.vrspolreslandak.api.models.RGetKampanyeKarhutla;
import id.creatodidak.vrspolreslandak.api.models.RespAddResponse;
import id.creatodidak.vrspolreslandak.api.models.RespKarhutla;
import id.creatodidak.vrspolreslandak.api.models.RespVerifikasiKarhutla;
import id.creatodidak.vrspolreslandak.api.models.ServerREmbung;
import id.creatodidak.vrspolreslandak.api.models.ServerResponse;
import id.creatodidak.vrspolreslandak.api.models.UpdateDataHotspot;
import id.creatodidak.vrspolreslandak.api.models.chat.ResponseChat;
import id.creatodidak.vrspolreslandak.api.models.cuaca.DataCuaca;
import id.creatodidak.vrspolreslandak.api.models.karhutla.AllKarhutla;
import id.creatodidak.vrspolreslandak.api.models.karhutla.ListVerCekEmbung;
import id.creatodidak.vrspolreslandak.api.models.karhutla.ListVerCekHotspot;
import id.creatodidak.vrspolreslandak.api.models.karhutla.ListVerKampanye;
import id.creatodidak.vrspolreslandak.api.models.karhutla.RekapKarhutla;
import id.creatodidak.vrspolreslandak.api.models.karhutla.rekapitulasi.RekapLaporanKarhutla;
import id.creatodidak.vrspolreslandak.auth.ModelProfile;
import id.creatodidak.vrspolreslandak.auth.ModelSatfungJabatan;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Endpoint {

    @FormUrlEncoded
    @POST("auth/login")
    Call<LoginResponse> login(
            @Field("nrp") String nrp,
            @Field("pass") String pass
    );


    @FormUrlEncoded
    @POST("gethotspot")
    Call<RespKarhutla> gethotspot(
            @Field("satker") String satker
    );

    @Multipart
    @POST("karhutla/addkampanye")
    Call<ServerResponse> updKampanyeKarhutla(
            @Part("verifikasi") String verifikasi,
            @Part("nrp") String nrp,
            @Part("koord") String koord,
            @Part("namatarget") String namatarget,
            @Part("lokasi") String lokasikampanye,
            @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("karhutla/getkampanye")
    Call<RGetKampanyeKarhutla> getKampanye(
            @Field("nrp") String nrp
    );

    @FormUrlEncoded
    @POST("karhutla/delkampanye")
    Call<ServerResponse> delKampanye(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("auth/tokenfcm")
    Call<ServerResponse> savetoken(
            @Field("nrp") String nrp,
            @Field("wilayah") String wilayah,
            @Field("token") String token);

    @FormUrlEncoded
    @POST("msg/notif")
    Call<ResponseNotif> kirimUpdateNotif(
            @Field("judul") String title,
            @Field("isi") String body,
            @Field("topic") String topic,
            @Field("channel") String channel
    );

    @FormUrlEncoded
    @POST("karhutla/updresponse")
    Call<RespAddResponse> responseKarhutla(
            @Field("id") int id,
            @Field("nrp") String nrp,
            @Field("status") String status);

    @Multipart
    @POST("karhutla/addcekhotspot")
    Call<ServerResponse> addCekHotspot(
            @Part("dataid") String dataId,
            @Part("nama") String dnama,
            @Part("lokasi") String lokasi,
            @Part("penyebab") String dpenyebab,
            @Part("rincian") String drincian,
            @Part("luas") String dluas,
            @Part("kondisi") String dkondisi,
            @Part MultipartBody.Part image,
            @Part("status") String statuss,
            @Part("created") String currentTimestamp,
            @Part("verifikasi") String verifikasi,
            @Part("geocoder") String geocoders,
            @Part("satker") String satker,
            @Part MultipartBody.Part image2,
            @Part MultipartBody.Part image3,
            @Part MultipartBody.Part image4,
            @Part MultipartBody.Part image5);

    @FormUrlEncoded
    @POST("karhutla/getcekhotspotstatus")
    Call<ServerResponse> getLaporanCekHotspotVerifikasi(
            @Field("dataid") String dataId);

    @GET("token/get")
    Call<ServerResponse> getToken();

    @FormUrlEncoded
    @POST("karhutla/getvercekhotspot")
    Call<RespVerifikasiKarhutla> getVerifikasiKarhutla(
            @Field("token") String token,
            @Field("wilayah") String wilayah,
            @Field("jenis") String jenis);

    @POST("karhutla/updbatchhotspot")
    Call<Void> updateDataHotspot(@Body List<UpdateDataHotspot> updates);

    @FormUrlEncoded
    @POST("karhutla/verifikasicekhotspot")
    Call<ServerResponse> verCekHotspot(
            @Field("dataid") String dataId,
            @Field("verifikasi") String newverifikasi);

    @FormUrlEncoded
    @POST("karhutla/verifikasikampanyekarhutla")
    Call<ServerResponse> verKampanyeKarhutla(
            @Field("dataid") String dataId,
            @Field("verifikasi") String newverifikasi);

    @GET("karhutla/getalllaporankarhutla")
    Call<ServerResponse> getLaporanKarhutlaToday();

    @FormUrlEncoded
    @POST("atensi/add")
    Call<ServerResponse> kirimAtensi(
            @Field("atensi") String atensi,
            @Field("jabatan") String jabatan
    );

    @GET("atensi/get")
    Call<AtensiResponse> getAtensi();

    @GET("karhutla/getallhotspot")
    Call<GlobalHotspot> gethotspotglobal();

    @GET("karhutla/getallembung")
    Call<DataEmbung> getAllEmbung();

    @Multipart
    @POST("karhutla/addcekembung")
    Call<ServerResponse> addcekembung(
            @Part("kodeembung") String kodeembung,
            @Part("dataid") String dataid,
            @Part("latitude") double latitude,
            @Part("longitude") double longitude,
            @Part("geocoder") String geocoder,
            @Part("pelaksana") String pelaksana,
            @Part("intensitasair") String intensitasair,
            @Part("volumeair") String volumeair,
            @Part("cuaca") String cuaca,
            @Part("analisa") String analisa,
            @Part("prediksi") String prediksi,
            @Part("rekomendasi") String rekomendasi,
            @Part("nrp") String nrp,
            @Part("verifikasi") String verifikasi,
            @Part("satker") String satker,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("karhutla/verifikasicekembung")
    Call<ServerResponse> verifikasicekembung(
            @Field("nrp") String nrp,
            @Field("id") int id,
            @Field("verifikasi") String verifikasi
    );

    @Multipart
    @POST("karhutla/addembung")
    Call<ServerREmbung> addembung(
            @Part("kode") String kode,
            @Part("nama") String nama,
            @Part("latitude") double latitude,
            @Part("longitude") double longitude,
            @Part("type") String type,
            @Part("kapasitas") String kapasitas,
            @Part("kecamatan") String kecamatan,
            @Part MultipartBody.Part image
    );

    @GET("karhutla/getcuaca")
    Call<DataCuaca> getCuaca();

    @GET("karhutla/getlaporancekembung")
    Call<AllLaporanCekEmbung> getallcekembung();

    @GET("hotspotcenter/getallhotspot")
    Call<AllKarhutla> getAllHotspot();

    @FormUrlEncoded
    @POST("hotspotcenter/responsehotspot")
    Call<RServerResponseHotspot> responseHotspot(
            @Field("nrp") String nrp,
            @Field("id") int id
    );

    @POST("hotspotcenter/updatelokasi")
    Call<RServerResponseHotspot> updateLocations(@Body List<UpdateLokasiHotspot> updateLokasiList);

    @GET("hotspotcenter/getmarker")
    Call<AllMarker> getAllMarker();

    @GET("hotspotcenter/rekapkarhutla")
    Call<RekapKarhutla> getRekapKarhutlaNew();

    @FormUrlEncoded
    @POST("hotspotcenter/addtextcekhotspot")
    Call<ResponseChat> sendDataCekHotspot(
            @Field("id") int extraID,
            @Field("kolom") String parameter,
            @Field("data") String msg,
            @Field("riwayat") String riwayat
    );

    @Multipart
    @POST("hotspotcenter/addfotocekhotspot")
    Call<ResponseChat> uploadFotoCekHotspot(
            @Part("id") int extraID,
            @Part("riwayat") String s,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("hotspotcenter/updverifikasicekhotspot")
    Call<ResponseChat> verifikasiCekHotspot(
            @Field("id") int extraID,
            @Field("verifikasi") String verifikasi,
            @Field("riwayat") String newRiwayat
    );

    @Multipart
    @POST("hotspotcenter/addcekembung")
    Call<ResponseChat> addcekembungNew(
            @Part List<MultipartBody.Part> imageParts,
            @Part("dataid") String dataid,
            @Part("kodeembung") String kodeembung,
            @Part("latitude") double latitude,
            @Part("longitude") double longitude,
            @Part("geocoder") String geocoder,
            @Part("pelaksana") String pelaksana,
            @Part("intensitasair") String intensitasair,
            @Part("volumeair") String volumeair,
            @Part("cuaca") String cuaca,
            @Part("analisa") String analisa,
            @Part("prediksi") String prediksi,
            @Part("rekomendasi") String rekomendasi,
            @Part("personil") String personil,
            @Part("verifikasi") String verifikasi
    );

    @Multipart
    @POST("hotspotcenter/addkampanye")
    Call<ResponseChat> addKampanye(
            @Part List<MultipartBody.Part> imageParts,
            @Part("personil") String personil,
            @Part("dataid") String dataid,
            @Part("namatarget") String namaTarget,
            @Part("latitude") double latitude,
            @Part("longitude") double longitude,
            @Part("lokasi") String lokasi,
            @Part("analisa") String analisa,
            @Part("penyampaian") String penyampaian,
            @Part("prediksi") String prediksi,
            @Part("rekomendasi") String rekomendasi,
            @Part("verifikasi") String verifikasi
    );

    @FormUrlEncoded
    @POST("hotspotcenter/getverifikasicekhotspot")
    Call<ListVerCekHotspot> getListVerCekHotspot(@Field("tanggal") String tanggal);

    @FormUrlEncoded
    @POST("hotspotcenter/updverifikasicekhotspot")
    Call<ResponseChat> updVerifikasiCekHotspot(
            @Field("id") int id,
            @Field("verifikasi") String verifikasi,
            @Field("riwayat") String s
    );

    @FormUrlEncoded
    @POST("hotspotcenter/getverifikasicekembung")
    Call<ListVerCekEmbung> getListVerCekEmbung(@Field("tanggal") String tanggal);

    @FormUrlEncoded
    @POST("hotspotcenter/updverifikasicekembung")
    Call<ResponseChat> updVerifikasiCekEmbung(
            @Field("dataid") String dataid,
            @Field("verifikasi") String verifikasi
    );

    @FormUrlEncoded
    @POST("hotspotcenter/getverifikasikampanye")
    Call<ListVerKampanye> getListVerKampanye(@Field("tanggal") String tanggal);

    @FormUrlEncoded
    @POST("hotspotcenter/updverifikasikampanye")
    Call<ResponseChat> updVerifikasiKampanye(
            @Field("dataid") String dataid,
            @Field("verifikasi") String verifikasi
    );

    @FormUrlEncoded
    @POST("hotspotcenter/rekapitulasi")
    Call<RekapLaporanKarhutla> getRekapKarhutla(@Field("tanggal") String currentTanggal);

    @Multipart
    @POST("personil/updatefoto")
    Call<ResponseChat> updateFotoProfile(
            @Part MultipartBody.Part image,
            @Part("nrp") String nrp
    );

    @FormUrlEncoded
    @POST("personil/getprofile")
    Call<ModelProfile> getProfile(@Field("nrp") String nrp);

    @FormUrlEncoded
    @POST("personil/getsatfung")
    Call<ModelSatfungJabatan> getSatfungJabatan(@Field("satker") String satker);

    @FormUrlEncoded
    @POST("personil/updatedatadiri")
    Call<ResponseChat> updateDataDiri(
            @Field("nrp") String snrp,
            @Field("nik") String snik,
            @Field("nama") String snama,
            @Field("tanggal_lahir") String stanggal,
            @Field("pangkat") String spangkat,
            @Field("satker") String ssatker,
            @Field("satfung") String ssatfung,
            @Field("jabatan") String sjabatan,
            @Field("wa") String swhatsapp);
}
