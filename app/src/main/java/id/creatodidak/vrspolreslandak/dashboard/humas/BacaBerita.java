package id.creatodidak.vrspolreslandak.dashboard.humas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.Ldkserver;
import id.creatodidak.vrspolreslandak.dashboard.humas.model.BacaBeritaLink;
import id.creatodidak.vrspolreslandak.helper.CustomDialog;
import id.creatodidak.vrspolreslandak.helper.DateUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BacaBerita extends AppCompatActivity {
    TextView rnewsKategori;
    TextView rnewsJudul;
    TextView rnewsSatker;
    TextView rnewsWaktu;
    ImageView rnewsProfile;
    TextView rnewsPersonil;
    TextView rnewsSatkerPers;
    ImageView rnewsGambar;
    TextView rnewsCaption;
    TextView rnewsIsi;
    TextView rnewsShare;
    String link;
    News endpoint;
    ScrollView sv;
    LinearLayout refLy;
    TextView refBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baca_berita);
        Intent intent = getIntent();
        link = intent.getStringExtra("link");
        rnewsKategori = findViewById(R.id.rnewsKategori);
        rnewsJudul = findViewById(R.id.rnewsJudul);
        rnewsSatker = findViewById(R.id.rnewsSatker);
        rnewsWaktu = findViewById(R.id.rnewsWaktu);
        rnewsProfile = findViewById(R.id.rnewsProfile);
        rnewsPersonil = findViewById(R.id.rnewsPersonil);
        rnewsSatkerPers = findViewById(R.id.rnewsSatkerPers);
        rnewsGambar = findViewById(R.id.rnewsGambar);
        rnewsCaption = findViewById(R.id.rnewsCaption);
        rnewsIsi = findViewById(R.id.rnewsIsi);
        rnewsShare = findViewById(R.id.rnewsShare);
        endpoint = Ldkserver.getClient().create(News.class);
        sv = findViewById(R.id.scrollView3);
        refLy = findViewById(R.id.refLy);
        refBtn = findViewById(R.id.refBtn);
        refBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadData();

    }

    private void loadData() {
        android.app.AlertDialog alert = CustomDialog.up(
                BacaBerita.this,
                "Memuat Berita...",
                "",
                "",
                "",
                new CustomDialog.AlertDialogListener() {
                    @Override
                    public void onPositiveButtonClick(android.app.AlertDialog alert) {
                    }

                    @Override
                    public void onNegativeButtonClick(AlertDialog alert) {
                    }
                },
                false,
                false,
                true
        );
        alert.show();

        Call<BacaBeritaLink> call = endpoint.getByLink(link);
        call.enqueue(new Callback<BacaBeritaLink>() {
            @Override
            public void onResponse(Call<BacaBeritaLink> call, Response<BacaBeritaLink> response) {
                alert.dismiss();
                if(response.isSuccessful() && response.body() != null){
                    sv.setVisibility(View.VISIBLE);
                    rnewsKategori.setText(response.body().getRes().getKategori());
                    rnewsJudul.setText(response.body().getRes().getJudul());
                    rnewsSatker.setText(response.body().getRes().getSatker());
                    rnewsWaktu.setText(DateUtils.tanggaldaricreatedat(response.body().getRes().getCreatedAt()));
                    rnewsPersonil.setText(response.body().getUserdata().getNama());
                    rnewsSatkerPers.setText(response.body().getUserdata().getSatker());
                    rnewsCaption.setText(response.body().getRes().getCaption());
                    rnewsIsi.setText(response.body().getRes().getIsi().replaceAll("\\\\n", "\n").replaceAll("\\\\", ""));
                    Glide.with(BacaBerita.this)
                            .load(response.body().getRes().getGambar())
                            .into(rnewsGambar);
                    Glide.with(BacaBerita.this)
                            .load(response.body().getUserdata().getFoto())
                            .circleCrop()
                            .error(R.drawable.baseline_account_circle_24)
                            .into(rnewsProfile);
                }else{
                    refLy.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BacaBeritaLink> call, Throwable t) {
                alert.dismiss();
                refLy.setVisibility(View.VISIBLE);
            }
        });
    }
}