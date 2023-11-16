package id.creatodidak.vrspolreslandak.dashboard.humas;

import id.creatodidak.vrspolreslandak.dashboard.humas.model.Mberita;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Berita {
    @FormUrlEncoded
    @POST("bd/berita")
    Call<Mberita> berita(
            @Field("page") String page
    );

    @GET("bd/berita/today")
    Call<Mberita> beritatoday();

    @GET("bd/berita/{satker}/{tahun}/{bulan}/{tanggal}")
    Call<Mberita> beritabydatebysatker();

    @GET("bd/berita/{tahun}/{bulan}/{tanggal}")
    Call<Mberita> beritabydate();

    @GET("bd/berita/cat/{cat}")
    Call<Mberita> beritabycat();

    @GET("bd/berita/show/{link}")
    Call<Mberita> showberita();

    @GET("bd/berita/beritabysatker")
    Call<Mberita> beritabysatker();

    @GET("bd/berita/beritapersatker")
    Call<Mberita> beritapersatker();

    @GET("bd/berita/beritaperkategori")
    Call<Mberita> beritaperkategori();

    @GET("bd/berita/delete/{id}")
    Call<Mberita> hapusberita();

    @GET("bd/berita/month/{bulan}")
    Call<Mberita> beritabymonth();

    @GET("bd/berita/year/{tahun}")
    Call<Mberita> beritabyyear();

    @GET("bd/berita/kategori")
    Call<Mberita> allkategori();

    @POST("bd/berita/upload")
    Call<Mberita> uploadberita();

}
