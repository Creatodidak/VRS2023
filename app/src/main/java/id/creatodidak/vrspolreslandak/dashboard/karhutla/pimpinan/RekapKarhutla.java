package id.creatodidak.vrspolreslandak.dashboard.karhutla.pimpinan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.karhutla.rekapitulasi.RekapLaporanKarhutla;
import id.creatodidak.vrspolreslandak.api.models.karhutla.rekapitulasi.RekapcekembungItem;
import id.creatodidak.vrspolreslandak.api.models.karhutla.rekapitulasi.RekapcekhotspotItem;
import id.creatodidak.vrspolreslandak.api.models.karhutla.rekapitulasi.RekapkampanyeItem;
import id.creatodidak.vrspolreslandak.api.models.karhutla.rekapitulasi.RekapkarhutlaItem;
import id.creatodidak.vrspolreslandak.helper.CustomDatePickerDialog;
import id.creatodidak.vrspolreslandak.helper.CustomDialog;
import id.creatodidak.vrspolreslandak.helper.DateUtils;
import id.creatodidak.vrspolreslandak.helper.Hitungjarak;
import id.creatodidak.vrspolreslandak.helper.ShareWaUtils;
import id.creatodidak.vrspolreslandak.helper.TextHelper;
import id.creatodidak.vrspolreslandak.helper.WaktuLokal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RekapKarhutla extends AppCompatActivity {
    Endpoint endpoint;
    TextView isiLaporan, cTanggal;
    String currentTanggal;
    Button btShare;
    LinearLayout icCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekap_karhutla);

        endpoint = Client.getClient().create(Endpoint.class);
        isiLaporan = findViewById(R.id.isiLaporan);
        currentTanggal = WaktuLokal.getTanggal();
        btShare = findViewById(R.id.btShare);
        cTanggal = findViewById(R.id.currentTanggal);
        icCalendar = findViewById(R.id.icCalendar);
        cTanggal.setText(currentTanggal);

        isiLaporan.setMovementMethod(LinkMovementMethod.getInstance());
        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareWaUtils.shareTextToWhatsApp(RekapKarhutla.this, isiLaporan.getText().toString());
            }
        });

        icCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CustomDatePickerDialog(RekapKarhutla.this, new CustomDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int month, int day) {
                        currentTanggal = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
                        cTanggal.setText(currentTanggal);
                        loadlaporan(currentTanggal);
                    }
                }).show();
            }
        });
    }

    public void onResume(){
        super.onResume();
        loadlaporan(currentTanggal);
    }

    private void loadlaporan(String currentTanggal) {
        isiLaporan.setText("");
        AlertDialog alerts = CustomDialog.up(
                this,
                "Mengambil data...",
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


        Call<RekapLaporanKarhutla> call = endpoint.getRekapKarhutla(currentTanggal);
        call.enqueue(new Callback<RekapLaporanKarhutla>() {
            @Override
            public void onResponse(Call<RekapLaporanKarhutla> call, Response<RekapLaporanKarhutla> response) {
                alerts.dismiss();
                if(response.isSuccessful() && response.body() != null){
                    fetchlaporan(response.body());
                }else {
                    CustomDialog.up(
                            RekapKarhutla.this,
                            "Informasi",
                            "Server Error!",
                            "TUTUP",
                            "",
                            new CustomDialog.AlertDialogListener() {
                                @Override
                                public void onPositiveButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                    finish();
                                }

                                @Override
                                public void onNegativeButtonClick(AlertDialog alert) {
                                }
                            },
                            true, false, false
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<RekapLaporanKarhutla> call, Throwable t) {
                alerts.dismiss();
                CustomDialog.up(
                        RekapKarhutla.this,
                        "Informasi",
                        "Gagal memanggil server, periksa jaringan internet anda!",
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
                            }
                        },
                        true, false, false
                ).show();
            }
        });
    }

    private void fetchlaporan(RekapLaporanKarhutla res) {
        List<RekapkarhutlaItem> hotspot = res.getRekapkarhutla();
        List<RekapcekhotspotItem> cekhotspot = res.getRekapcekhotspot();
        List<RekapkampanyeItem> kampanye = res.getRekapkampanye();
        List<RekapcekembungItem> cekembung = res.getRekapcekembung();

        String pembukaan = "Kepada Yth: Kapolda Kalimantan Barat\nDari: Kapolres Landak\n\n"+ DateUtils.deteksiWaktu() +" Jenderal, izin melaporkan Rekapitulasi Kegiatan Polres Landak dalam rangka mengantisipasi dan mengendalikan Kebakaran Hutan dan Lahan di Wilayah Hukum Polres Landak pada "+ DateUtils.haridantanggallaporan(currentTanggal) +"\n\n";

        String Stitikapi = "A. Rekapitulasi Titik Api Per Kecamatan di Kabupaten Landak\n";
        String isiStitikapi = "";
        int notitikapi = 0;
        for(RekapkarhutlaItem item : hotspot){
            notitikapi++;
            isiStitikapi = isiStitikapi+notitikapi+". "+ TextHelper.capitalize(item.getKecamatan()) +" (Total "+String.valueOf(item.getTotal())+")\n\uD83C\uDD97 Responded: "+String.valueOf(item.getDiresponse())+"\n\uD83D\uDD34 High: "+String.valueOf(item.getHigh())+"\n\uD83D\uDFE1 Medium: "+String.valueOf(item.getMedium())+ "\n\uD83D\uDFE2 Low: " +String.valueOf(item.getLow())+"\n\n";
        }

        String Scekhotspot = "B. Rekapitulasi Penanganan Titik Api\n";
        String isiScekhotspot = "";
        if(cekhotspot.size() == 0){
            isiScekhotspot = "NIHIL";
        }else{
            isiScekhotspot = "Total Giat: "+String.valueOf(cekhotspot.size())+"\n\n";
            int nocekhotspot = 0;
            for(RekapcekhotspotItem item : cekhotspot){
                nocekhotspot++;
                isiScekhotspot = isiScekhotspot+String.valueOf(nocekhotspot)+". Aplikasi VRS Polres Landak melaporkan bahwa Satelit "+item.getSatelit()+" pada "+DateUtils.haridantanggallaporan(item.getCreatedAt())+" telah mendeteksi adanya titik api disekitar koordinat "+ String.valueOf(item.getLaths())+","+String.valueOf(item.getLonghs())+". Personil "+item.getSatkerpers()+", "+TextHelper.capitalize(item.getPangkatpers() +" "+ item.getNamapers())+" beserta pelaksana lainnya mengecek titik api dan didapati fakta sbb:\na. Koordinat pengecekan: "+ String.valueOf(item.getLatlap())+","+String.valueOf(item.getLonglap())+";\nb. Jarak dari koordinat satelit: ±"+new DecimalFormat("#.##").format(Hitungjarak.calculateDistance((Double) item.getLaths(), (Double) item.getLonghs(), item.getLatlap(), item.getLonglap()))+" Km;\nc. Pemilik lahan: "+item.getPemiliklahan()+";\nd. Luas lahan terbakar diperkirakan sekitar "+item.getLuas()+" Ha;\ne. Penyebab api: diduga "+item.getPenyebabapi()+";\nf. Tindakan di lapangan: \n"+item.getTindakan().replace("\\n", "\n")+"\ng. Saat laporan ini dikirim, titik api "+item.getKondisiapi()+"\n\n";
            }
        }

        String Skampanye = "C. Rekapitulasi Kampanye Mengajak Masyarakat Bersama Mencegah Terjadinya Kebakaran Hutan dan Lahan\n";
        String isiKampanye = "";

            int nokampanye = 0;
            int jumlahkampanye = 0;
            String rekapkampanye = "";
            for(RekapkampanyeItem item : kampanye){
                if(item.getJumlah() != 0){
                    nokampanye++;
                    jumlahkampanye = jumlahkampanye + item.getJumlah();
                    rekapkampanye = rekapkampanye+String.valueOf(nokampanye)+". "+TextHelper.capitalize(item.getSatker())+": "+String.valueOf(item.getJumlah())+" giat;\n";
                }
            }

        if(jumlahkampanye == 0){
            isiKampanye = "NIHIL";
        }else{
            isiKampanye = "Total Giat: "+String.valueOf(jumlahkampanye)+"\n\n"+rekapkampanye;
        }

        String Scekembung = "\nD. Rekapitulasi Pengecekan Embung Air\n";
        String isiCekEmbung = "";

        if(cekembung.size() == 0){
            isiCekEmbung = "NIHIL";
        }else{
            int nocekembung = 0;
            isiCekEmbung = "Total Giat: "+String.valueOf(cekembung.size())+"\n\n";
            for(RekapcekembungItem item : cekembung){
                nocekembung++;
                isiCekEmbung = isiCekEmbung+String.valueOf(nocekembung)+". Pengecekan "+TextHelper.capitalize(item.getXnama())+" oleh "+TextHelper.capitalize(item.getPangkatpers() +" "+ item.getNamapers()+" "+item.getJabatanpers() +" "+item.getSatfungpers()+ " " + item.getSatkerpers())+"\na. Koordinat: "+String.valueOf(item.getLatitude())+","+item.getLongitude()+";\nb. Intensitas air:\nIntensitas air di "+TextHelper.capitalize(item.getXnama())+" dianggap "+TextHelper.capitalize(item.getIntensitasair())+"; apabila sewaktu-waktu diperlukan dalam penanganan Karhutla disekitar embung.\nc. Volume Air: Diperkirakan sekitar "+item.getVolumeair()+"m³\n\n";
            }
        }

        String closing = "Demikian Jenderal yang dapat kami laporkan, link dan dokumentasi terlampir.\n\nTembusan:\n1. Karo Ops Polda Kalbar;\n2. Dir Binmas Polda Kalbar;\n3. Kabid Propam Polda Kalbar.\n\nLampiran:\n1. Link Rincian Lengkap dan dokumentasi:\nhttps://servervrs.polreslandak.id/laporan/karhutla/"+res.getToken()+"\n\n\n";
        String results = pembukaan+
                         Stitikapi+
                         isiStitikapi+
                         Scekhotspot+
                         isiScekhotspot+
                         Skampanye+
                         isiKampanye+
                         Scekembung+
                         isiCekEmbung+
                         closing
                ;

        isiLaporan.setText(results);
    }
}