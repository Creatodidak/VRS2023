package id.creatodidak.vrspolreslandak.adapter.karhutla;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.models.karhutla.ListHotspotItem;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.chat.ChatLaporanCekHotspot;
import id.creatodidak.vrspolreslandak.helper.CustomDialog;
import id.creatodidak.vrspolreslandak.helper.WaktuLokal;

public class ListKarhutlaAdp extends RecyclerView.Adapter<ListKarhutlaAdp.ViewHolder> {

    private final List<ListHotspotItem> listHotspotItems;
    private final SharedPreferences sh;
    private final String nrp;
    private final String pangkat;
    private final String nama;
    private final String satker;
    private final String satfung;
    private final String namapangkat;
    Context context;

    public ListKarhutlaAdp(Context context, List<ListHotspotItem> listHotspotItems) {
        this.context = context;
        this.listHotspotItems = listHotspotItems;

        sh = context.getSharedPreferences("SESSION_DATA", Context.MODE_PRIVATE);
        nrp = sh.getString("nrp", "-");
        pangkat = sh.getString("pangkat", "-");
        nama = sh.getString("nama", "-");
        satker = sh.getString("satker", "-");
        satfung = sh.getString("satfung", "-");
        namapangkat = sh.getString("pangkat", "-") + " " + sh.getString("nama", "-");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listchat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListHotspotItem item = listHotspotItems.get(position);
        holder.tvJudul.setText("Laporan pelaksanaan pengecekan hotspot nomor "+ item.getId());
        holder.tvKoordinat.setText("Lat: "+ item.getLaths() +" Lat: "+item.getLonghs());
        holder.tvLokasi.setText(item.getLokasi());
        if(item.getVerifikasi().equals("BELUM ADA") && item.getLocal().equals("NO")){
            if(!item.getCreatedAt().contains(WaktuLokal.getTanggal())) {
                holder.tvStatus.setText("Tugas kedaluwarsa ");
                holder.ivIndikator.setImageResource(R.drawable.baseline_edit_off_24);
            }else{
                holder.tvStatus.setText("Belum ada laporan ");
                holder.ivIndikator.setImageResource(R.drawable.baseline_report_24);
            }
        }else if(!item.getVerifikasi().equals("BELUM ADA") && item.getLocal().equals("NO")){
            holder.ivIndikator.setImageResource(R.drawable.baseline_checklist_24);
        }else if(!item.getVerifikasi().equals("BELUM ADA") && item.getLocal().equals("YES")){
            if(!item.getCreatedAt().contains(WaktuLokal.getTanggal())) {
                holder.tvStatus.setText("Tugas kedaluwarsa ");
                holder.ivIndikator.setImageResource(R.drawable.baseline_edit_off_24);
            }else{
                holder.tvStatus.setText("Laporan belum dikirim ");
                holder.ivIndikator.setImageResource(R.drawable.baseline_access_time_filled_24);
            }
        }
        holder.wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nrp.equals("98070129")) {
                    if (!item.getCreatedAt().contains(WaktuLokal.getTanggal())) {
                        CustomDialog.up(
                                view.getContext(),
                                "Informasi",
                                "Tugas kedaluwarsa!",
                                "OK",
                                "",
                                new CustomDialog.AlertDialogListener() {
                                    @Override
                                    public void onPositiveButtonClick(AlertDialog alert) {
                                        alert.dismiss();
                                    }

                                    @Override
                                    public void onNegativeButtonClick(AlertDialog alert) {
                                        alert.dismiss();
                                    }
                                },
                                true, false, false
                        ).show();
                    } else {
                        Intent intent = new Intent(context, ChatLaporanCekHotspot.class);
                        intent.putExtra("id", item.getId());
                        intent.putExtra("activity", context.getClass().getSimpleName());
                        context.startActivity(intent);
                    }
                }else{
                    Intent intent = new Intent(context, ChatLaporanCekHotspot.class);
                    intent.putExtra("id", item.getId());
                    intent.putExtra("activity", context.getClass().getSimpleName());
                    context.startActivity(intent);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
//        return Math.min(listHotspotItems.size());
        return listHotspotItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul;
        TextView tvKoordinat;
        TextView tvLokasi;
        TextView tvStatus;
        ImageView ivIndikator;
        LinearLayout wrapper;


        public ViewHolder(@NonNull View v) {
            super(v);

            tvJudul = v.findViewById(R.id.tvJudul);
            tvKoordinat = v.findViewById(R.id.tvKoordinat);
            tvLokasi = v.findViewById(R.id.tvLokasi);
            tvStatus = v.findViewById(R.id.tvStatus);
            ivIndikator = v.findViewById(R.id.ivIndikator);
            wrapper = v.findViewById(R.id.wrapper);
            tvStatus.setVisibility(View.GONE);


        }
    }
}
