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
import id.creatodidak.vrspolreslandak.api.models.karhutla.ListvercekhotspotItem;
import id.creatodidak.vrspolreslandak.helper.CustomDialog;
import id.creatodidak.vrspolreslandak.helper.TextHelper;
import id.creatodidak.vrspolreslandak.helper.WaktuLokal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowCekHotspotDataPimpinan extends AppCompatActivity {
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
        ListvercekhotspotItem item = (ListvercekhotspotItem) intent.getSerializableExtra("data");

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

        String bulls = "";

        if (item.getConfidence().equals("HIGH")) {
            bulls = "\uD83D\uDD34";
        } else if (item.getConfidence().equals("MEDIUM")) {
            bulls = "\uD83D\uDFE1";
        } else if (item.getConfidence().equals("LOW")) {
            bulls = "\uD83D\uDFE2";
        }

        String judul = "Laporan Kegiatan Pengecekan Hotspot Disekitar " + item.getLokasi();
        String laporan = "Izin komandan melaporkan kegiatan Pengecekan Hotspot disekitar " + item.getLokasi().replace("\\n", "\n") + " dengan rincian sbb:\n\nA. Profile Titik Api\n\uD83C\uDD94 ID: " + String.valueOf(item.getId()) + "\n\uD83E\uDDED Latitude: " + String.valueOf(item.getLaths()) + "\n\uD83E\uDDED Longitude: " + String.valueOf(item.getLonghs()) + "\n\uD83D\uDCE1 Satelite: " + item.getSatelit().replace("\\n", "\n") + "\n" + bulls + " Confidence: " + TextHelper.capitalize(item.getConfidence().replace("\\n", "\n")) + "\n\uD83D\uDCCD Wilkum: " + TextHelper.capitalize(item.getSatker().replace("\\n", "\n")) + "\n\uD83D\uDCCD Kecamatan: " + TextHelper.capitalize(item.getKecamatan().replace("\\n", "\n")) + "\n\nB. Profile Responder\n➤ Nama: " + item.getNamapers().replace("\\n", "\n") + "\n➤ Pangkat / Nrp: " + item.getPangkatpers().replace("\\n", "\n") +"/"+item.getNrppers().replace("\\n", "\n")+ "\n➤ Jabatan: " +TextHelper.capitalize(item.getJabatanpers().replace("\\n", "\n"))+" "+TextHelper.capitalize(item.getSatfungpers().replace("\\n", "\n"))+" "+TextHelper.capitalize(item.getSatkerpers().replace("\\n", "\n"))+ "\n\nC. Hasil Pengecekan dilapangan:\n\uD83E\uDDED Real Latitude: " + String.valueOf(item.getLatlap()) + "\n\uD83E\uDDED Real Longitude: " + String.valueOf(item.getLonglap()) + "\n\n➤ Nama Pemilik Lahan: " + item.getPemiliklahan().replace("\\n", "\n") + "\n\n➤ Penyebab api: " + item.getPenyebabapi().replace("\\n", "\n") + "\n\n➤ Luas lahan terbakar: ±" + item.getLuas().replace("\\n", "\n") + " Hektar\n\n➤ Pelaksana Kegiatan:\n" + item.getPelaksanakegiatan().replace("\\n", "\n") + "\n\n➤ Tindakan di lapangan:\n" + item.getTindakan().replace("\\n", "\n") + "\n\n➤ Analisa:\n\n" + item.getAnalisa().replace("\\n", "\n") + "\n➤ Prediksi:\n" + item.getPrediksi().replace("\\n", "\n") + "\n\n➤ Rekomendasi:\n" + item.getRekomendasi().replace("\\n", "\n") + "\n\n➤ Kondisi api:\nSaat laporan ini dibuat kondisi api " + item.getKondisiapi().replace("\\n", "\n") + "\n\n➤ Status Verifikasi:\n"+ TextHelper.capitalize(item.getVerifikasi().replace("\\n", "\n")) +"\n\n\nDemikian komandan yang dapat kami laporkan beserta dokumentasi terlampir";

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
                        ShowCekHotspotDataPimpinan.this,
                        "Konfirmasi",
                        "Laporan akan anda verifikasi/diterima, lanjutkan?",
                        "LANJUTKAN",
                        "BATAL",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog alert) {
                                alert.dismiss();
                                verifikasi(item.getId(), diterima);
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
                        ShowCekHotspotDataPimpinan.this,
                        "Konfirmasi",
                        "Anda akan menolak laporan ini, lanjutkan?",
                        "LANJUTKAN",
                        "BATAL",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog alert) {
                                alert.dismiss();
                                verifikasi(item.getId(), ditolak);
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

    private void verifikasi(int id, String verifikasi) {
        AlertDialog alerts = CustomDialog.up(
                ShowCekHotspotDataPimpinan.this,
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

        Call<ResponseChat> call = endpoint.updVerifikasiCekHotspot(id, verifikasi, "Laporan "+verifikasi+" ("+ WaktuLokal.get()+")");

        call.enqueue(new Callback<ResponseChat>() {
            @Override
            public void onResponse(Call<ResponseChat> call, Response<ResponseChat> response) {
                if(response.isSuccessful() && response.body() != null && response.body().isBerhasil()){
                    alerts.dismiss();
                    CustomDialog.up(
                            ShowCekHotspotDataPimpinan.this,
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
                        ShowCekHotspotDataPimpinan.this,
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