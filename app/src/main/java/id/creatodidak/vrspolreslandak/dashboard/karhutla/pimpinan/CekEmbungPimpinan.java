package id.creatodidak.vrspolreslandak.dashboard.karhutla.pimpinan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.adapter.karhutla.ListVerCekEmbungAdp;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.karhutla.ListVerCekEmbung;
import id.creatodidak.vrspolreslandak.api.models.karhutla.ListvercekembungItem;
import id.creatodidak.vrspolreslandak.helper.CustomDatePickerDialog;
import id.creatodidak.vrspolreslandak.helper.WaktuLokal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CekEmbungPimpinan extends AppCompatActivity {

    Endpoint endpoint;
    SharedPreferences sh;
    String nama, pangkat, jabatan, satfung, satker, currentTanggal;
    RecyclerView rv;
    LinearLayoutManager lm;
    ListVerCekEmbungAdp adapter;
    List<ListvercekembungItem> list = new ArrayList<>();
    TextView nodata, cTanggal;
    LinearLayout icCalendar;
    SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_embung_pimpinan);

        endpoint = Client.getClient().create(Endpoint.class);
        sh = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        nama = sh.getString("nama", "");
        pangkat = sh.getString("pangkat", "");
        jabatan = sh.getString("jabatan", "");
        satfung = sh.getString("satfung", "");
        satker = sh.getString("satker", "");
        currentTanggal = WaktuLokal.getTanggal();
        rv = findViewById(R.id.rvListLaporan);
        nodata = findViewById(R.id.tvBelumAdaData);
        swipe = findViewById(R.id.swipe);
        cTanggal = findViewById(R.id.currentTanggal);
        icCalendar = findViewById(R.id.icCalendar);
        cTanggal.setText(currentTanggal);
        lm = new LinearLayoutManager(this);
        adapter = new ListVerCekEmbungAdp(this, list, new ListVerCekEmbungAdp.OnItemClickListener() {
            @Override
            public void onClick(ListvercekembungItem data) {
                Intent intent = new Intent(CekEmbungPimpinan.this, ShowCekEmbungDataPimpinan.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });

        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadList(currentTanggal);
            }
        });

        icCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CustomDatePickerDialog(CekEmbungPimpinan.this, new CustomDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int month, int day) {
                        currentTanggal = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
                        cTanggal.setText(currentTanggal);
                        loadList(currentTanggal);
                    }
                }).show();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadList(String tanggal) {
        if (list.size() != 0) {
            list.clear();
            adapter.notifyDataSetChanged();
        }
        nodata.setVisibility(View.VISIBLE);
        nodata.setText("Memuat data...");
        Call<ListVerCekEmbung> call = endpoint.getListVerCekEmbung(tanggal);
        call.enqueue(new Callback<ListVerCekEmbung>() {
            @Override
            public void onResponse(Call<ListVerCekEmbung> call, Response<ListVerCekEmbung> response) {
                if (response.body() != null && response.isSuccessful()) {
                    fetchlist(response.body().getListvercekembung());
                } else {
                    fetchlist(response.body().getListvercekembung());
                }
            }

            @Override
            public void onFailure(Call<ListVerCekEmbung> call, Throwable t) {
                nodata.setText("Periksa Jaringan Internet!");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchlist(List<ListvercekembungItem> listVerCekHotspot) {
        if (swipe.isRefreshing()) {
            swipe.setRefreshing(false);
        }

        if (listVerCekHotspot.size() != 0) {
            for (ListvercekembungItem item : listVerCekHotspot) {
                if (item.getVerifikasi().contains("KAPOLRES")) {
                    list.add(item);
                }
            }
            adapter.notifyDataSetChanged();
        }

        if (list.size() == 0) {
            nodata.setVisibility(View.VISIBLE);
            nodata.setText("Belum ada data!");
        } else {
            nodata.setVisibility(View.GONE);
        }
    }

    public void onResume() {
        super.onResume();
        loadList(currentTanggal);
    }
}