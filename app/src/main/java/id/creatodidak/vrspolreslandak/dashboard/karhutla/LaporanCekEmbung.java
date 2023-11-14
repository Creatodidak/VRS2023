package id.creatodidak.vrspolreslandak.dashboard.karhutla;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.adapter.karhutla.LaporanCekEmbungAdapter;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.ListlaporancekembungItem;
import id.creatodidak.vrspolreslandak.api.models.ServerREmbung;
import id.creatodidak.vrspolreslandak.api.models.chat.MChatid;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.chat.ChatLaporanCekEmbung;
import id.creatodidak.vrspolreslandak.database.DBHelper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporanCekEmbung extends AppCompatActivity {
    Button btLaporanBaru;
    RecyclerView rv;
    TextView tvBelumAdaData;
    ActivityResultLauncher<Intent> tambahlaporan;
    DBHelper dbHelper;
    SharedPreferences sh;
    List<MChatid> chatlist = new ArrayList<>();
    List<ListlaporancekembungItem> list = new ArrayList<>();
    LaporanCekEmbungAdapter adapter;
    LinearLayoutManager lm;
    SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_cek_embung);
        dbHelper = new DBHelper(this);
        dbHelper.inisialisasi();
        sh = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);

        rv = findViewById(R.id.rvListLaporan);
        btLaporanBaru = findViewById(R.id.btLaporanBaru);
        tvBelumAdaData = findViewById(R.id.tvBelumAdaData);
        swipe = findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                chatlist.clear();
                list.clear();
                loadChatCekEmbung();
            }
        });

        tambahlaporan = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        chatlist.clear();
                        list.clear();
                        loadChatCekEmbung();
                    } else {
                        Toast.makeText(this, "Batal Menambah Laporan", Toast.LENGTH_SHORT).show();
                    }
                });

        btLaporanBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LaporanCekEmbung.this, PetaEmbungNew.class);
                tambahlaporan.launch(intent);
            }
        });
        adapter = new LaporanCekEmbungAdapter(this, list, new LaporanCekEmbungAdapter.OnItemClickListener() {
            @Override
            public void onClick(String dataid) {
//                Toast.makeText(LaporanCekEmbung.this, dataid, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LaporanCekEmbung.this, ChatLaporanCekEmbung.class);
                intent.putExtra("dataid", dataid);
                startActivity(intent);
            }
        });
        lm = new LinearLayoutManager(this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(lm);

        cekofflineembung();
    }

    @SuppressLint("Range")
    private void cekofflineembung() {
        Cursor cursor = dbHelper.getAllEmbung();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex("local")).equals("yes")) {
                    String nama = cursor.getString(cursor.getColumnIndex("nama"));
                    double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
                    double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
                    String type = cursor.getString(cursor.getColumnIndex("type"));
                    String kapasitas = cursor.getString(cursor.getColumnIndex("kapasitas"));
                    String kecamatan = cursor.getString(cursor.getColumnIndex("kecamatan"));
                    String kode = cursor.getString(cursor.getColumnIndex("kode"));
                    String foto = cursor.getString(cursor.getColumnIndex("foto"));


                    File file = new File(Objects.requireNonNull(Uri.parse(foto)).getPath()).getAbsoluteFile();

                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                    Endpoint endpoint = Client.getClient().create(Endpoint.class);
                    Call<ServerREmbung> call = endpoint.addembung(kode, nama, latitude, longitude, type, kapasitas, kecamatan, image);
                    call.enqueue(new Callback<ServerREmbung>() {
                        @Override
                        public void onResponse(Call<ServerREmbung> call, Response<ServerREmbung> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                if (response.body().isStatus()) {
                                    dbHelper.updEmbung(kode, response.body().getFoto());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ServerREmbung> call, Throwable t) {
                        }
                    });
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        loadChatCekEmbung();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadChatCekEmbung() {
        tvBelumAdaData.setText("Memuat data...");
        chatlist = dbHelper.getChatIDbyActivity("LaporanCekEmbung");
        List<ListlaporancekembungItem> data = dbHelper.getAllCekEmbung();

        for (ListlaporancekembungItem lap : data) {
            for (MChatid id : chatlist) {
                if (id.getDataid().equals(lap.getDataid())) {
                    list.add(lap);
                }
            }
        }

        if (chatlist.size() == 0) {
            tvBelumAdaData.setText("Belum ada data!");
        } else {
            tvBelumAdaData.setVisibility(View.GONE);
        }

        adapter.notifyDataSetChanged();
        if (swipe.isRefreshing()) {
            swipe.setRefreshing(false);
        }
    }

    public void onResume(){
        super.onResume();
        list.clear();
        loadChatCekEmbung();
    }
}