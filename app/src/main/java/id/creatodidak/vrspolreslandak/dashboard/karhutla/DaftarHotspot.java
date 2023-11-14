package id.creatodidak.vrspolreslandak.dashboard.karhutla;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.adapter.karhutla.ListDaftarHotspotAdp;
import id.creatodidak.vrspolreslandak.adapter.karhutla.RServerResponseHotspot;
import id.creatodidak.vrspolreslandak.adapter.karhutla.UpdateLokasiHotspot;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.karhutla.AllKarhutla;
import id.creatodidak.vrspolreslandak.api.models.karhutla.ListHotspotItem;
import id.creatodidak.vrspolreslandak.database.DBHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarHotspot extends AppCompatActivity {
    SharedPreferences sh;
    String nrp, pangkat, nama, satker, satfung, namapangkat;
    Endpoint endpoint;
    DBHelper dbHelper;
    ListDaftarHotspotAdp adapter;
    List<ListHotspotItem> listHotspot = new ArrayList<>();
    List<UpdateLokasiHotspot> listUpdate = new ArrayList<>();
    LinearLayoutManager lm;
    Spinner filter;
    RecyclerView rv;
    TextView notifikasi;
    ArrayAdapter<String> polsek;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_hotspot);
        sh = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        nrp = sh.getString("nrp", "-");
        pangkat = sh.getString("pangkat", "-");
        nama = sh.getString("nama", "-");
        satker = sh.getString("satker", "-");
        satfung = sh.getString("satfung", "-");
        namapangkat = sh.getString("pangkat", "-") + " " + sh.getString("nama", "-");
        endpoint = Client.getClient().create(Endpoint.class);
        dbHelper = new DBHelper(this);
        dbHelper.inisialisasi();
        lm = new LinearLayoutManager(this);
        rv = findViewById(R.id.rvListHotspot);
        notifikasi = findViewById(R.id.tvNotifikasi);
        filter = findViewById(R.id.spFilter);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setEnabled(false);

        List<String> polseks = Arrays.asList("PILIH SATKER", "POLSEK NGABANG", "POLSUBSEKTOR JELIMPO", "POLSEK AIR BESAR", "POLSEK KUALA BEHE", "POLSEK SENGAH TEMILA", "POLSEK SEBANGKI", "POLSEK MENYUKE", "POLSEK MERANTI", "POLSEK MANDOR", "POLSEK MENJALIN", "POLSEK MEMPAWAH HULU", "POLSUBSEKTOR SOMPAK");

        if (satker.equals("POLRES LANDAK")) {
            polsek = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, polseks);
        } else {
            List<String> notpolres = Arrays.asList("PILIH SATKER", satker);
            polsek = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, notpolres);
        }
        polsek.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter.setAdapter(polsek);
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = filter.getSelectedItem().toString();
                if (!selected.equals("PILIH SATKER")) {
                    notifikasi.setText("memuat data...");
                    listUpdate.clear();
                    listHotspot.clear();
                    loadHotspot(selected);
                    swipeRefreshLayout.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String selected = filter.getSelectedItem().toString();
                if (!selected.equals("PILIH SATKER")) {
                    notifikasi.setText("memuat data...");
                    listUpdate.clear();
                    listHotspot.clear();
                    loadHotspot(selected);
                }
            }
        });
        rv.setLayoutManager(lm);
        adapter = new ListDaftarHotspotAdp(this, listHotspot);
        rv.setAdapter(adapter);
    }

    private void loadHotspot(String selected) {
        Call<AllKarhutla> call = endpoint.getAllHotspot();
        call.enqueue(new Callback<AllKarhutla>() {
            @Override
            public void onResponse(Call<AllKarhutla> call, Response<AllKarhutla> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Geocoder geocoder = new Geocoder(DaftarHotspot.this, new Locale("id", "ID"));

                    List<ListHotspotItem> list = response.body().getListHotspot();
                    for (ListHotspotItem item : list) {
                        if (item.getSatker().equals(selected)) {
                            if (item.getLokasi().contains("KALIMANTAN BARAT, LANDAK,")) {
                                try {
                                    List<Address> address = geocoder.getFromLocation(item.getLaths(), item.getLonghs(), 1);
                                    if (address != null) {
                                        UpdateLokasiHotspot upd = new UpdateLokasiHotspot();
                                        upd.setId(item.getId());
                                        upd.setLokasi(address.get(0).getAddressLine(0));
                                        if (listUpdate.size() < 10) {
                                            listUpdate.add(upd);
                                        }
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }

                    updatedata(selected);
                } else {
                    notifikasi.setText("Gagal mengambil data!");
                }
            }

            @Override
            public void onFailure(Call<AllKarhutla> call, Throwable t) {
                notifikasi.setText("Tidak ada internet!");
            }
        });
    }

    private void updatedata(String selected) {
        if (listUpdate.size() != 0) {
            notifikasi.setText("Mengupdate lokasi");
            Call<RServerResponseHotspot> call = endpoint.updateLocations(listUpdate);
            call.enqueue(new Callback<RServerResponseHotspot>() {
                @Override
                public void onResponse(Call<RServerResponseHotspot> call, Response<RServerResponseHotspot> response) {
                    loadAgain(selected);
                }

                @Override
                public void onFailure(Call<RServerResponseHotspot> call, Throwable t) {
                    loadAgain(selected);
                }
            });
        }else{
            loadAgain(selected);
        }
    }

    private void loadAgain(String selected) {
        notifikasi.setText("Menampilkan data");
        Call<AllKarhutla> call = endpoint.getAllHotspot();
        call.enqueue(new Callback<AllKarhutla>() {
            @Override
            public void onResponse(Call<AllKarhutla> call, Response<AllKarhutla> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ListHotspotItem> list = response.body().getListHotspot();
                    for (ListHotspotItem item : list) {
                        if (item.getSatker().equals(selected)) {
                            listHotspot.add(item);
                        }

                        if(item.getResponder().equals(nrp)){
                            if(dbHelper.addAllHotspot(item.getId(), item.getLaths(), item.getLonghs(), item.getConfidence(), item.getLokasi(), item.getSatelit(), item.getSatker(), item.getKecamatan(), nrp, item.getLatlap(), item.getLonglap(), item.getPemiliklahan(), item.getPenyebabapi(), item.getLuas(), item.getPelaksanakegiatan(), item.getTindakan(), item.getAnalisa(), item.getPrediksi(), item.getRekomendasi(), item.getKondisiapi(), item.getFoto(), item.getRiwayat(), item.getVerifikasi(), "NO", item.getCreatedAt(), item.getCreatedAt())){

                            }
                        }
                    }
                    fetchList();
                } else {
                    notifikasi.setText("Gagal mengambil data!");
                }
            }

            @Override
            public void onFailure(Call<AllKarhutla> call, Throwable t) {
                notifikasi.setText("Tidak ada internet!");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchList() {
        swipeRefreshLayout.setRefreshing(false);
        if (listHotspot.size() == 0) {
            notifikasi.setText("Belum ada hotspot!");
        } else {
            notifikasi.setText("Silahkan pilih data yang ingin ditampilkan dengan memilih satker pada filter diatas");
        }
        adapter.notifyDataSetChanged();
    }
}