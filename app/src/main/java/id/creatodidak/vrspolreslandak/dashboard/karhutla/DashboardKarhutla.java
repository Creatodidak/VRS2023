package id.creatodidak.vrspolreslandak.dashboard.karhutla;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.pimpinan.CekEmbungPimpinan;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.pimpinan.CekHotspotPimpinan;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.pimpinan.KampanyePimpinan;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.pimpinan.RekapKarhutla;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.verifikator.CekEmbungVerifikator;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.verifikator.CekHotspotVerifikator;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.verifikator.KampanyeVerifikator;

public class DashboardKarhutla extends AppCompatActivity implements View.OnClickListener {
    CardView MKPetaHotspot;
    CardView MKCekHotspot;
    CardView MKKampanye;
    CardView MKCekEmbungAir;
    CardView MKDaftarHotspot;
    CardView MKRekapitulasi;
    SharedPreferences sh;
    String jabatan, satfung;
    boolean isVerifikator;
    boolean isPimpinan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_karhutla);
        MKPetaHotspot = findViewById(R.id.MKPetaHotspot);
        MKCekHotspot = findViewById(R.id.MKCekHotspot);
        MKKampanye = findViewById(R.id.MKKampanye);
        MKCekEmbungAir = findViewById(R.id.MKCekEmbungAir);
        MKDaftarHotspot = findViewById(R.id.MKDaftarHotspot);
        MKRekapitulasi = findViewById(R.id.MKRekapitulasi);

        MKRekapitulasi.setOnClickListener(this);
        MKPetaHotspot.setOnClickListener(this);
        MKCekHotspot.setOnClickListener(this);
        MKKampanye.setOnClickListener(this);
        MKCekEmbungAir.setOnClickListener(this);
        MKDaftarHotspot.setOnClickListener(this);
        sh = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        jabatan = sh.getString("jabatan", "");
        satfung = sh.getString("satfung", "");

        isVerifikator = jabatan.contains("KABAG") || jabatan.contains("KAPOLSEK") || jabatan.contains("KAPOLSUBSEKTOR") || satfung.equals("SI TIK");
        isPimpinan = jabatan.contains("KAPOLRES");

        if(!isPimpinan){
            if(!satfung.equals("SI TIK")){
                MKRekapitulasi.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        Intent intent;

        if (viewId == R.id.MKPetaHotspot) {
            intent = new Intent(this, PetaHotspot.class);
            startActivity(intent);
        } else if (viewId == R.id.MKCekHotspot) {
            if(isPimpinan){
                intent = new Intent(this, CekHotspotPimpinan.class);
                startActivity(intent);
            }else if(jabatan.contains("KABAG") && satfung.contains("BAG OPS")|| jabatan.contains("KAPOLSEK") || jabatan.contains("KAPOLSUBSEKTOR")){
                intent = new Intent(this, CekHotspotVerifikator.class);
                startActivity(intent);
            }else{
                intent = new Intent(this, LaporanCekHotspot.class);
                startActivity(intent);
            }
        } else if (viewId == R.id.MKKampanye) {
            if(isPimpinan){
                intent = new Intent(this, KampanyePimpinan.class);
                startActivity(intent);
            }else if(jabatan.contains("KASAT") && satfung.contains("SAT BINMAS")|| jabatan.contains("KAPOLSEK") || jabatan.contains("KAPOLSUBSEKTOR")){
                intent = new Intent(this, KampanyeVerifikator.class);
                startActivity(intent);
            }else{
                intent = new Intent(this, LaporanKampanyeCegahKarhutla.class);
                startActivity(intent);
            }
        } else if (viewId == R.id.MKCekEmbungAir) {
            if(isPimpinan){
                intent = new Intent(this, CekEmbungPimpinan.class);
                startActivity(intent);
            }else if(jabatan.contains("KASAT") && satfung.contains("SAT BINMAS")|| jabatan.contains("KAPOLSEK") || jabatan.contains("KAPOLSUBSEKTOR")){
                intent = new Intent(this, CekEmbungVerifikator.class);
                startActivity(intent);
            }else{
                intent = new Intent(this, LaporanCekEmbung.class);
                startActivity(intent);
            }
        }else if(viewId == R.id.MKDaftarHotspot){
            intent = new Intent(this, DaftarHotspot.class);
            startActivity(intent);
        }else if(viewId == R.id.MKRekapitulasi){
            intent = new Intent(this, RekapKarhutla.class);
            startActivity(intent);
        }

    }

}