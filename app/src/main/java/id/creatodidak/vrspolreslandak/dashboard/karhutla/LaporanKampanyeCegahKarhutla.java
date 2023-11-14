package id.creatodidak.vrspolreslandak.dashboard.karhutla;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.adapter.karhutla.LaporanKampanyeCegahKarhutlaAdapter;
import id.creatodidak.vrspolreslandak.api.models.karhutla.DatakampanyekarhutlaItem;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.chat.ChatLaporankampanyeCegahKarhutla;
import id.creatodidak.vrspolreslandak.database.DBHelper;
import id.creatodidak.vrspolreslandak.helper.CustomDialog;
import id.creatodidak.vrspolreslandak.helper.RandomStringGenerator;

public class LaporanKampanyeCegahKarhutla extends AppCompatActivity {
    Button btLaporanBaru;
    RecyclerView rv;
    TextView tvBelumAdaData;
    DBHelper dbHelper;
    SharedPreferences sh;
    List<DatakampanyekarhutlaItem> list = new ArrayList<>();
    LaporanKampanyeCegahKarhutlaAdapter adapter;
    LinearLayoutManager lm;
    SwipeRefreshLayout swipe;
    FusedLocationProviderClient fusedLocationProviderClient;
    String nrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_kampanye_cegah_karhutla);

        dbHelper = new DBHelper(this);
        dbHelper.inisialisasi();
        sh = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        nrp = sh.getString("nrp", "");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        rv = findViewById(R.id.rvListLaporan);
        btLaporanBaru = findViewById(R.id.btLaporanBaru);
        tvBelumAdaData = findViewById(R.id.tvBelumAdaData);
        swipe = findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                loadChatKampanyeKarhutla();
            }
        });

        btLaporanBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLaporan();
            }
        });

        lm = new LinearLayoutManager(this);
        adapter = new LaporanKampanyeCegahKarhutlaAdapter(this, list, new LaporanKampanyeCegahKarhutlaAdapter.OnItemClickListener() {
            @Override
            public void onClick(String dataid) {
                Intent intent = new Intent(LaporanKampanyeCegahKarhutla.this, ChatLaporankampanyeCegahKarhutla.class);
                intent.putExtra("dataid", dataid);
                startActivity(intent);
            }
        });
        rv.setAdapter(adapter);
        rv.setLayoutManager(lm);
        loadChatKampanyeKarhutla();
    }

    private void addLaporan() {
        AlertDialog alerts = CustomDialog.up(
                this,
                "Memproses...",
                "",
                "",
                "",
                new CustomDialog.AlertDialogListener() {
                    @Override
                    public void onPositiveButtonClick(android.app.AlertDialog alert) {

                    }

                    @Override
                    public void onNegativeButtonClick(android.app.AlertDialog alert) {

                    }
                },
                false, false, true
        );
        alerts.show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {return;}
        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    alerts.dismiss();
                    Geocoder geocoder = new Geocoder(LaporanKampanyeCegahKarhutla.this, new Locale("ID"));
                    String lokasi;
                    try {
                        List<Address> alamat = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if(alamat != null){
                            lokasi = alamat.get(0).getAddressLine(0);
                        }else{
                            lokasi = "BELUM ADA";
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    long dataid = RandomStringGenerator.randomNumber(10);
                    if(dbHelper.addDataKampanye(dataid, lokasi, location.getLatitude(), location.getLongitude(), nrp)){
                        if(dbHelper.registerChatid(dataid, "LaporanKampanyeCegahKarhutla")){
                            Intent intent = new Intent(LaporanKampanyeCegahKarhutla.this, ChatLaporankampanyeCegahKarhutla.class);
                            intent.putExtra("dataid", String.valueOf(dataid));
                            startActivity(intent);
                        }
                    }
                }else{
                    alerts.dismiss();
                    CustomDialog.up(
                            LaporanKampanyeCegahKarhutla.this,
                            "Informasi",
                            "Gagal mendapatkan lokasi, periksa GPS anda atau ulangi lagi menekan tombol tambah laporan sampai berhasil",
                            "MENGERTI",
                            "",
                            new CustomDialog.AlertDialogListener() {
                                @Override
                                public void onPositiveButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                }

                                @Override
                                public void onNegativeButtonClick(AlertDialog alert) {

                                }
                            },
                            true, false,false
                    ).show();
                }
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadChatKampanyeKarhutla() {
        if(swipe.isRefreshing()){
            swipe.setRefreshing(false);
        }

        List<DatakampanyekarhutlaItem> item = dbHelper.getDataKampanye();
        list.addAll(item);
        adapter.notifyDataSetChanged();

        if(list.size() == 0){
            tvBelumAdaData.setVisibility(View.VISIBLE);
        }else{
            tvBelumAdaData.setVisibility(View.GONE);
        }
    }

    public void onResume(){
        super.onResume();
        list.clear();
        loadChatKampanyeKarhutla();
    }
}