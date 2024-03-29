package id.creatodidak.vrspolreslandak.dashboard.humas;

import id.creatodidak.vrspolreslandak.api.models.ServerResponse;
import id.creatodidak.vrspolreslandak.dashboard.humas.model.BacaBeritaLink;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface News {
    @GET("beritaandroid")
    Call<id.creatodidak.vrspolreslandak.dashboard.humas.model.News> allnews();

    @Multipart
    @POST("berita/tambah")
    Call<ServerResponse> uploadBerita(
            @Part MultipartBody.Part image,
            @Part("judul") String judul,
            @Part("isi") String isi,
            @Part("caption") String captions,
            @Part("satker") String satker,
            @Part("pers") String pers,
            @Part("kategori") String kategoris,
            @Part("hashtag") String hashtag
    );

    @GET("beritaandroid/get/{link}")
    Call<BacaBeritaLink> getByLink(
            @Path("link") String link
    );
}
