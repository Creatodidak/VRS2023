package id.creatodidak.vrspolreslandak.dashboard.karhutla;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.adapter.karhutla.ListKarhutlaAdp;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.karhutla.ListHotspotItem;
import id.creatodidak.vrspolreslandak.database.DBHelper;
import id.creatodidak.vrspolreslandak.helper.WaktuLokal;

public class LaporanCekHotspot extends AppCompatActivity {

    RecyclerView rvListLaporan;
    TextView tvBelumAdaData;
    Button btLaporanBaru;
    SharedPreferences sh;
    String nrp, pangkat, nama, satker, satfung, namapangkat;
    Endpoint endpoint;
    DBHelper dbHelper;
    ListKarhutlaAdp adapter;
    List<ListHotspotItem> listHotspot = new ArrayList<>();
    LinearLayoutManager lm;
    int countLocal = 0;
    int countDraft = 0;
    int countNew = 0;
    int countNewPers = 0;

    SwipeRefreshLayout swipe;
    ImageView kalender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_cek_hotspot);
        rvListLaporan = findViewById(R.id.rvListLaporan);
        btLaporanBaru = findViewById(R.id.btLaporanBaru);
        tvBelumAdaData = findViewById(R.id.tvBelumAdaData);
        sh = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        nrp = sh.getString("nrp", "-");
        pangkat = sh.getString("pangkat", "-");
        nama = sh.getString("nama", "-");
        satker = sh.getString("satker", "-");
        satfung = sh.getString("satfung", "-");
        namapangkat = sh.getString("pangkat", "-") + " " + sh.getString("nama", "-");
        swipe = findViewById(R.id.swipe);
        swipe.setRefreshing(false);
        swipe.setEnabled(false);
        kalender = findViewById(R.id.kalender);
        endpoint = Client.getClient().create(Endpoint.class);
        dbHelper = new DBHelper(this);
        dbHelper.inisialisasi();
        lm = new LinearLayoutManager(this);
        rvListLaporan.setLayoutManager(lm);
        adapter = new ListKarhutlaAdp(this, listHotspot);
        rvListLaporan.setAdapter(adapter);
        loadFromLocal();
    }

    @SuppressLint("Range")
    private void loadFromLocal() {
        Cursor cursor = dbHelper.getListHotSpot(nrp);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ListHotspotItem item = new ListHotspotItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("id")));
                item.setLaths(cursor.getDouble(cursor.getColumnIndex("laths")));
                item.setLonghs(cursor.getDouble(cursor.getColumnIndex("longhs")));
                item.setConfidence(cursor.getString(cursor.getColumnIndex("confidence")));
                item.setLokasi(cursor.getString(cursor.getColumnIndex("lokasi")));
                item.setSatelit(cursor.getString(cursor.getColumnIndex("satelit")));
                item.setSatker(cursor.getString(cursor.getColumnIndex("satker")));
                item.setKecamatan(cursor.getString(cursor.getColumnIndex("kecamatan")));
                item.setResponder(cursor.getString(cursor.getColumnIndex("responder")));
                item.setLatlap(cursor.getDouble(cursor.getColumnIndex("latlap")));
                item.setLonglap(cursor.getDouble(cursor.getColumnIndex("longlap")));
                item.setPemiliklahan(cursor.getString(cursor.getColumnIndex("pemiliklahan")));
                item.setPenyebabapi(cursor.getString(cursor.getColumnIndex("penyebabapi")));
                item.setLuas(cursor.getString(cursor.getColumnIndex("luas")));
                item.setPelaksanakegiatan(cursor.getString(cursor.getColumnIndex("pelaksanakegiatan")));
                item.setTindakan(cursor.getString(cursor.getColumnIndex("tindakan")));
                item.setAnalisa(cursor.getString(cursor.getColumnIndex("analisa")));
                item.setPrediksi(cursor.getString(cursor.getColumnIndex("prediksi")));
                item.setRekomendasi(cursor.getString(cursor.getColumnIndex("rekomendasi")));
                item.setKondisiapi(cursor.getString(cursor.getColumnIndex("kondisiapi")));
                item.setFoto(cursor.getString(cursor.getColumnIndex("foto")));
                item.setRiwayat(cursor.getString(cursor.getColumnIndex("riwayat")));
                item.setVerifikasi(cursor.getString(cursor.getColumnIndex("verifikasi")));
                item.setLocal(cursor.getString(cursor.getColumnIndex("local")));
                item.setCreatedAt(cursor.getString(cursor.getColumnIndex("created_at")));
                item.setUpdatedAt(cursor.getString(cursor.getColumnIndex("updated_at")));
               
                if(cursor.getString(cursor.getColumnIndex("created_at")).contains(WaktuLokal.getTanggal()) &&
                        cursor.getString(cursor.getColumnIndex("verifikasi")).equals("BELUM ADA") &&
                        cursor.getString(cursor.getColumnIndex("local")).equals("NO")){
                    countLocal++;
                }else if(cursor.getString(cursor.getColumnIndex("created_at")).contains(WaktuLokal.getTanggal()) &&
                        !cursor.getString(cursor.getColumnIndex("verifikasi")).equals("BELUM ADA") &&
                        cursor.getString(cursor.getColumnIndex("local")).equals("YES")){
                    countDraft++;
                }
                listHotspot.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }
        fetchData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchData() {
        if (listHotspot.size() == 0) {
            tvBelumAdaData.setText("Belum ada data");
        } else if(countLocal != 0 && countDraft != 0) {
            tvBelumAdaData.setText(countLocal +" Laporan belum dibuat dan "+ countDraft +" Drafts belum dikirim!");
        } else if(countLocal == 0 && countDraft != 0) {
            tvBelumAdaData.setText(countDraft +" Drafts belum dikirim!");
        } else if(countLocal != 0 && countDraft == 0) {
            tvBelumAdaData.setText(countLocal +" Laporan belum dibuat!");
        } else if(countLocal == 0 && countDraft == 0) {
            tvBelumAdaData.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }
}
