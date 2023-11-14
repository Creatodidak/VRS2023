package id.creatodidak.vrspolreslandak.adapter.karhutla;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.karhutla.ListHotspotItem;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.LaporanCekHotspot;
import id.creatodidak.vrspolreslandak.database.DBHelper;
import id.creatodidak.vrspolreslandak.helper.CustomDialog;
import id.creatodidak.vrspolreslandak.helper.DateUtils;
import id.creatodidak.vrspolreslandak.helper.WaktuLokal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListDaftarHotspotAdp extends RecyclerView.Adapter<ListDaftarHotspotAdp.ViewHolder> {

    private final List<ListHotspotItem> listHotspotItems;
    private final SharedPreferences sh;
    private final String nrp;
    private final String pangkat;
    private final String nama;
    private final String satker;
    private final String satfung;
    private final String namapangkat;
    Context context;
    Endpoint endpoint;
    boolean res;
    DBHelper dbHelper;
    public ListDaftarHotspotAdp(Context context, List<ListHotspotItem> listHotspotItems) {
        this.context = context;
        this.listHotspotItems = listHotspotItems;

        sh = context.getSharedPreferences("SESSION_DATA", Context.MODE_PRIVATE);
        nrp = sh.getString("nrp", "-");
        pangkat = sh.getString("pangkat", "-");
        nama = sh.getString("nama", "-");
        satker = sh.getString("satker", "-");
        satfung = sh.getString("satfung", "-");
        namapangkat = sh.getString("pangkat", "-")+" "+sh.getString("nama", "-");
        endpoint = Client.getClient().create(Endpoint.class);
        dbHelper = new DBHelper(context);
        dbHelper.inisialisasi();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listkarhutla, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListHotspotItem item = listHotspotItems.get(position);
        if (item.getConfidence().equals("LOW")) {
            holder.icon.setImageResource(R.drawable.firegreen);
            holder.status.setText("LOW");
        } else if (item.getConfidence().equals("MEDIUM")) {
            holder.icon.setImageResource(R.drawable.fireyellow);
            holder.status.setText("MEDIUM");
        } else if (item.getConfidence().equals("HIGH")) {
            holder.icon.setImageResource(R.drawable.firered);
            holder.status.setText("HIGH");
        } else {
            holder.icon.setImageResource(R.drawable.fireblack);
            holder.status.setText("UNKNOWN");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!item.getResponder().equals("BELUM ADA") && item.getResponder().equals(nrp)) {
                holder.btResp.setText("LAPORAN");
                holder.btResp.setBackgroundResource(R.drawable.buttonblue);
            } else if(!item.getResponder().equals("BELUM ADA") && !item.getResponder().equals(nrp)) {
                holder.btResp.setText("DIRESPON");
                holder.btResp.setBackgroundResource(R.drawable.buttongray);
            }else if (String.valueOf(item.getResponder()).equals("BELUM ADA") ){
                holder.btResp.setText("RESPONSE");
                holder.btResp.setBackgroundResource(R.drawable.buttonred);
            }
        }

        holder.tvjudul.setText("Satelite "+item.getSatelit()+" menangkap adanya titik api disekitar "+ item.getLokasi() +" pada hari "+ DateUtils.tanggaldaricreatedat(item.getCreatedAt()));
        holder.koordinat.setText(" "+ item.getLaths() +","+ item.getLonghs());
        holder.btResp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (!item.getResponder().equals("BELUM ADA") && item.getResponder().equals(nrp)) {
                    intent = new Intent(context, LaporanCekHotspot.class);
                    context.startActivity(intent);
                } else if(!item.getResponder().equals("BELUM ADA") && !item.getResponder().equals(nrp)) {
                    String[] riw = new String[]{item.getRiwayat()};
                    CustomDialog.up(
                            context,
                            "Informasi",
                            "Hotspot ini telah "+riw[0],
                            "OK",
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
                            true, false, false
                    ).show();
                }else if (String.valueOf(item.getResponder()).equals("BELUM ADA") ){
                    CustomDialog.up(
                            context,
                            "Konfirmasi",
                            "Anda ingin meresponse titik api ini?\nID: "+ item.getId() +"\n"+item.getLokasi()+"\nAnda tidak dapat membatalkannya jika sudah terkirim ke server, lanjutkan?",
                            "LANJUTKAN",
                            "BATAL",
                            new CustomDialog.AlertDialogListener() {
                                @Override
                                public void onPositiveButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                    if(dbHelper.addAllHotspot(item.getId(), item.getLaths(), item.getLonghs(), item.getConfidence(), item.getLokasi(), item.getSatelit(), item.getSatker(), item.getKecamatan(), nrp, item.getLatlap(), item.getLonglap(), item.getPemiliklahan(), item.getPenyebabapi(), item.getLuas(), item.getPelaksanakegiatan(), item.getTindakan(), item.getAnalisa(), item.getPrediksi(), item.getRekomendasi(), item.getKondisiapi(), item.getFoto(), "Diresponse oleh "+namapangkat+" ("+ WaktuLokal.gettanggaldanjam()+")", item.getVerifikasi(), "NO", item.getCreatedAt(), item.getCreatedAt())) {
                                        responseData(item.getId(), nrp, holder.btResp);
                                    }
                                }

                                @Override
                                public void onNegativeButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                }
                            },
                            true, true, false
                    ).show();
                }
            }
        });
    }

    private void successDialog() {
        CustomDialog.up(
                context,
                "Informasi",
                "Berhasil meresponse titik api!\nSilahkan refresh halaman dengan cara menarik list kebawah",
                "OK",
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
                true, false, false
        ).show();


    }

    private void failedDialog() {
        CustomDialog.up(
                context,
                "Informasi",
                "Gagal meresponse titik api!\nSilahkan refresh halaman dengan cara menarik list kebawah",
                "OK",
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
                true, false, false
        ).show();
    }

    private void responseData(int id, String nrp, TextView btResp) {
        AlertDialog alerts = CustomDialog.up(
                context,
                "Meresponse titik api",
                "",
                "OK",
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
                false, false, true
        );
        alerts.show();
        Call<RServerResponseHotspot> call = endpoint.responseHotspot(nrp, id);
        call.enqueue(new Callback<RServerResponseHotspot>() {
            @Override
            public void onResponse(Call<RServerResponseHotspot> call, Response<RServerResponseHotspot> response) {
                if (response.body() != null && response.body().isBerhasil()) {
                    alerts.dismiss();
                    successDialog();
                    btResp.setText("LAPORAN");
                    btResp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, LaporanCekHotspot.class);
                            context.startActivity(intent);
                        }
                    });
                    btResp.setBackgroundResource(R.drawable.buttonblue);

                }else{
                    alerts.dismiss();
                    if(dbHelper.delDataHotspot(id)) {
                        failedDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<RServerResponseHotspot> call, Throwable t) {
                alerts.dismiss();
                if(dbHelper.delDataHotspot(id)) {
                    failedDialog();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listHotspotItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView status, lokasi, koordinat, btResp, tvjudul;
        ImageView icon;
        Button maps;

        public ViewHolder(@NonNull View v) {
            super(v);

            status = v.findViewById(R.id.status);
            lokasi = v.findViewById(R.id.lkLokasi);
            koordinat = v.findViewById(R.id.lkKoordinat);
            icon = v.findViewById(R.id.lkIcon);
            maps = v.findViewById(R.id.btnMapslk);
            btResp = v.findViewById(R.id.btnResponselk);
            tvjudul = v.findViewById(R.id.tvJudul);
        }
    }
}
