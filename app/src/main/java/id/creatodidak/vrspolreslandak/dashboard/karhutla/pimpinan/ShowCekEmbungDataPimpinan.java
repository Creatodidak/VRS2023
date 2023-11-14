package id.creatodidak.vrspolreslandak.dashboard.karhutla.pimpinan;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.adapter.SliderLaporan;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.SliderItem;
import id.creatodidak.vrspolreslandak.api.models.chat.ResponseChat;
import id.creatodidak.vrspolreslandak.api.models.karhutla.ListvercekembungItem;
import id.creatodidak.vrspolreslandak.helper.CustomDialog;
import id.creatodidak.vrspolreslandak.helper.DateUtils;
import id.creatodidak.vrspolreslandak.helper.TextHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowCekEmbungDataPimpinan extends AppCompatActivity {
    SliderView fotoLaporan;
    TextView tvJudulLaporan, tvIsiLaporan;
    Button btnTolak, btnTerima;
    LinearLayout wrapperbtn;
    ImageView btnBack;
    Endpoint endpoint;
    String nama, pangkat, jabatan, satfung, satker;
    SharedPreferences sh;
    SliderLaporan adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cek_hotspot_data_verifikator);
        Intent intent = getIntent();
        ListvercekembungItem item = (ListvercekembungItem) intent.getSerializableExtra("data");

        sh = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        nama = sh.getString("nama", "");
        pangkat = sh.getString("pangkat", "");
        jabatan = sh.getString("jabatan", "");
        satfung = sh.getString("satfung", "");
        satker = sh.getString("satker", "");

        endpoint = Client.getClient().create(Endpoint.class);

        fotoLaporan = findViewById(R.id.fotoLaporan);
        tvJudulLaporan = findViewById(R.id.tvJudulLaporan);
        tvIsiLaporan = findViewById(R.id.tvIsiLaporan);
        btnBack = findViewById(R.id.btnBack);
        btnTerima = findViewById(R.id.btnTerima);
        btnTolak = findViewById(R.id.btnTolak);
        wrapperbtn = findViewById(R.id.wrapperbtn);

        if (item.getVerifikasi().equals("MENUNGGU PERSETUJUAN KAPOLRES LANDAK")) {
            wrapperbtn.setVisibility(View.VISIBLE);
        } else {
            wrapperbtn.setVisibility(View.GONE);
        }

        String judul = "Laporan Kegiatan Pengecekan " + item.getXnama();
        String laporan = "Izin komandan melaporkan kegiatan Pengecekan " + TextHelper.capitalize(item.getXnama().replace("\\n", "\n")) + " (" + TextHelper.capitalize(item.getXtype().replace("\\n", "\n")) + ") pada " + DateUtils.tanggaldaricreatedatlocal(item.getCreatedAt().replace("\\n", "\n")) + " dengan rincian sbb:\n\nA. Profile Embung Air\n➤ Nama: " + TextHelper.capitalize(item.getXnama().replace("\\n", "\n")) + "\n➤ Koordinat: \nLat: " + item.getXlatitude()+ " \nLong: " + item.getXlongitude() + "\n➤ Kapasitas: ±" + item.getXkapasitas().replace("\\n", "\n") + " m³\n➤ Type: " + TextHelper.capitalize(item.getXtype().replace("\\n", "\n")) + "\n➤ Kecamatan: " + TextHelper.capitalize(item.getXkecamatan().replace("\\n", "\n")) + "\n\nB. Rincian Kegiatan\n➤ Koordinat: \nLat: " + item.getLatitude() + " \nLong: " + item.getLongitude()+ "\n\n➤ Geocoder: \n" + item.getGeocoder().replace("\\n", "\n") + "\n\n➤ Intensitas Air: \nMenurut hasil pantauan dilapangan, saat laporan ini dibuat Intensitas Air dianggap " + item.getIntensitasair().replace("\\n", "\n") + ", apabila sewaktu-waktu digunakan untuk memadamkan titik api\n\n➤ Volume Air: ±" + item.getVolumeair().replace("\\n", "\n") + " m³\n\n➤ Pelaksana Kegiatan: \n" + item.getPelaksana().replace("\\n", "\n") + "\n\n➤ Cuaca: \n" + item.getCuaca().replace("\\n", "\n") + "\n\n➤ Analisa: \n" + item.getAnalisa().replace("\\n", "\n") + "\n\n➤ Prediksi: \n" + item.getPrediksi().replace("\\n", "\n") + "\n\n➤ Rekomendasi: \n" + item.getRekomendasi().replace("\\n", "\n") + "\n\n➤ Status Verifikasi:\n"+ TextHelper.capitalize(item.getVerifikasi().replace("\\n", "\n")) + "\n\n\nDemikian komandan yang dapat kami laporkan beserta dokumentasi terlampir";


        String[] foto = item.getFoto().split(",");

        List<SliderItem> ss = new ArrayList<>();

        for(int i = 0; i < foto.length; i++){
            SliderItem sliderItem = new SliderItem();
            sliderItem.setDescription("Dokumentasi");
            sliderItem.setImageUrl(foto[i]);
            sliderItem.setLocalimage(R.drawable.bg);
            ss.add(sliderItem);
        }

        adapter = new SliderLaporan(this, ss);
        fotoLaporan.setSliderAdapter(adapter);
        fotoLaporan.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
        fotoLaporan.setSliderTransformAnimation(SliderAnimations.TOSSTRANSFORMATION);
        fotoLaporan.setAutoCycle(false);

        tvJudulLaporan.setText(judul);
        tvIsiLaporan.setText(laporan);

        fotoLaporan.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                Log.i("GGG", "onIndicatorClicked: " + fotoLaporan.getCurrentPagePosition());
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        String ditolak = "DITOLAK KAPOLRES LANDAK";
        String diterima = "DITERIMA KAPOLRES LANDAK";


        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog.up(
                        ShowCekEmbungDataPimpinan.this,
                        "Konfirmasi",
                        "Laporan akan anda verifikasi/diterima, lanjutkan?",
                        "LANJUTKAN",
                        "BATAL",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog alert) {
                                alert.dismiss();
                                verifikasi(item.getDataid(), diterima);
                            }

                            @Override
                            public void onNegativeButtonClick(AlertDialog alert) {
                                alert.dismiss();
                            }
                        },
                        true, true, false
                ).show();
            }
        });

        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog.up(
                        ShowCekEmbungDataPimpinan.this,
                        "Konfirmasi",
                        "Anda akan menolak laporan ini, lanjutkan?",
                        "LANJUTKAN",
                        "BATAL",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog alert) {
                                alert.dismiss();
                                verifikasi(item.getDataid(), ditolak);
                            }

                            @Override
                            public void onNegativeButtonClick(AlertDialog alert) {
                                alert.dismiss();
                            }
                        },
                        true, true, false
                ).show();
            }
        });
    }

    private void verifikasi(String id, String verifikasi) {
        AlertDialog alerts = CustomDialog.up(
                ShowCekEmbungDataPimpinan.this,
                "Memverifikasi data",
                "",
                "",
                "",
                new CustomDialog.AlertDialogListener() {
                    @Override
                    public void onPositiveButtonClick(AlertDialog alert) {

                    }

                    @Override
                    public void onNegativeButtonClick(AlertDialog alert) {

                    }
                },
                false, false, true
        );

        alerts.show();

        Call<ResponseChat> call = endpoint.updVerifikasiCekEmbung(id, verifikasi);

        call.enqueue(new Callback<ResponseChat>() {
            @Override
            public void onResponse(Call<ResponseChat> call, Response<ResponseChat> response) {
                if(response.isSuccessful() && response.body() != null && response.body().isBerhasil()){
                    alerts.dismiss();
                    CustomDialog.up(
                            ShowCekEmbungDataPimpinan.this,
                            "Informasi",
                            "Laporan berhasil diverifikasi!",
                            "LANJUTKAN",
                            "",
                            new CustomDialog.AlertDialogListener() {
                                @Override
                                public void onPositiveButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                    finish();
                                }

                                @Override
                                public void onNegativeButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                }
                            },
                            true, false, false
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseChat> call, Throwable t) {
                alerts.dismiss();
                CustomDialog.up(
                        ShowCekEmbungDataPimpinan.this,
                        "Informasi",
                        "Laporan gagal diverifikasi, periksa jaringan internet anda!",
                        "MENGERTI",
                        "",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog alert) {
                                alert.dismiss();
                                finish();
                            }

                            @Override
                            public void onNegativeButtonClick(AlertDialog alert) {
                                alert.dismiss();
                                finish();
                            }
                        },
                        true, false, false
                ).show();
            }
        });
    }
}