package id.creatodidak.vrspolreslandak.adapter.karhutla;

import android.content.Context;
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
import id.creatodidak.vrspolreslandak.api.models.karhutla.ListVerCekHotspot;
import id.creatodidak.vrspolreslandak.api.models.karhutla.ListvercekhotspotItem;

public class ListVerCekHotspotAdp extends RecyclerView.Adapter<ListVerCekHotspotAdp.ViewHolder> {
    private List<ListvercekhotspotItem> list;
    private ListVerCekHotspotAdp.OnItemClickListener onItemClickListener;
    private Context context;
    private SharedPreferences sh;
    private String jabatan;
    public ListVerCekHotspotAdp(Context context, List<ListvercekhotspotItem> list, ListVerCekHotspotAdp.OnItemClickListener onItemClickListener){
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;

        sh = context.getSharedPreferences("SESSION_DATA", Context.MODE_PRIVATE);
        jabatan = sh.getString("jabatan", "");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listchat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListVerCekHotspotAdp.ViewHolder holder, int position) {
        ListvercekhotspotItem item = list.get(position);
        String judul = "Laporan Kegiatan Pengecekan Hotspot Disekitar " + item.getLokasi();
        holder.tvJudul.setText(judul);
        if(jabatan.contains("KABAG") && item.getVerifikasi().equals("MENUNGGU VERIFIKASI KABAG OPS") || jabatan.contains("KAPOLSEK") && item.getVerifikasi().equals("MENUNGGU VERIFIKASI KAPOLSEK")  || jabatan.contains("KAPOLSUBSEKTOR") && item.getVerifikasi().equals("MENUNGGU VERIFIKASI KAPOLSUBSEKTOR") || jabatan.contains("KAPOLRES") && item.getVerifikasi().equals("MENUNGGU PERSETUJUAN KAPOLRES LANDAK")){
            holder.ivIndikator.setImageResource(R.drawable.baseline_report_24);
        }else{
            holder.ivIndikator.setImageResource(R.drawable.check);
        }
        holder.tvStatus.setText(item.getVerifikasi());
        holder.wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    public interface OnItemClickListener{
        public void onClick(ListvercekhotspotItem data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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
            tvLokasi.setVisibility(View.GONE);
        }
    }
}
