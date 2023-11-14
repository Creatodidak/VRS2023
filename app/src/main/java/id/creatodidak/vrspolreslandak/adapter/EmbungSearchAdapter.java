package id.creatodidak.vrspolreslandak.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
import id.creatodidak.vrspolreslandak.api.models.EmbungItem;

public class EmbungSearchAdapter extends RecyclerView.Adapter<EmbungSearchAdapter.EmbungViewHolder> {
    private final EmbungSearchAdapter.OnItemClickListener onItemClickListener;
    private final Context context;
    private final List<EmbungItem> embungItemList;

    public EmbungSearchAdapter(Context context, List<EmbungItem> embungItemList, OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.context = context;
        this.embungItemList = embungItemList;
    }

    @NonNull
    @Override
    public EmbungViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listembung, parent, false);
        return new EmbungViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmbungViewHolder holder, int position) {
        int posisi = position;
        EmbungItem embungItem = embungItemList.get(position);
        holder.srcjudul.setText(embungItem.getNama());

        if(embungItem.getKeterangan().equals("Buat laporan cek embung")){
            holder.srcketerangan.setTextColor(Color.parseColor("#FF039BE5"));
            holder.icon.setImageResource(R.drawable.embung);
            holder.icon2.setVisibility(View.VISIBLE);
            holder.wrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(embungItem, posisi);
                    }
                }
            });
        }else if(embungItem.getKeterangan().equals("nama Kecamatan atau nama Embung")){
            holder.srcketerangan.setTextColor(Color.parseColor("#FF000000"));
            holder.icon.setImageResource(R.drawable.quicktips);
            holder.icon2.setVisibility(View.INVISIBLE);
        }else if(embungItem.getKeterangan().equals("Jarak diatas 100M (Not Available)")){
            holder.srcketerangan.setTextColor(Color.parseColor("#fbce00"));
            holder.icon.setImageResource(R.drawable.baseline_warning_amber_24);
            holder.icon2.setVisibility(View.INVISIBLE);
        }else if(embungItem.getKeterangan().equals("Periksa jaringan internet anda!")){
            holder.srcketerangan.setTextColor(Color.parseColor("#fbce00"));
            holder.icon.setImageResource(R.drawable.baseline_signal_cellular_connected_no_internet_0_bar_24);
            holder.icon2.setVisibility(View.INVISIBLE);
        }else if(embungItem.getKeterangan().equals("Server Error!")){
            holder.srcketerangan.setTextColor(Color.parseColor("#fbce00"));
            holder.icon.setImageResource(R.drawable.baseline_cloud_off2_24);
            holder.icon2.setVisibility(View.INVISIBLE);
        }else{
            holder.srcketerangan.setTextColor(Color.parseColor("#c90204"));
            holder.icon.setImageResource(R.drawable.baseline_help_24);
            holder.icon2.setVisibility(View.INVISIBLE);
        }

        holder.srcketerangan.setText(embungItem.getKeterangan());
    }

    @Override
    public int getItemCount() {
        return embungItemList.size();
    }

    public interface OnItemClickListener {
        void onClick(EmbungItem embungItem, int position);
    }

    static class EmbungViewHolder extends RecyclerView.ViewHolder {
        LinearLayout wrapper;
        TextView srcjudul, srcketerangan;
        ImageView icon, icon2;

        public EmbungViewHolder(@NonNull View itemView) {
            super(itemView);

            srcjudul = itemView.findViewById(R.id.srcjudul);
            srcketerangan = itemView.findViewById(R.id.srcketerangan);
            wrapper = itemView.findViewById(R.id.wrapper);
            icon = itemView.findViewById(R.id.imageView15);
            icon2 = itemView.findViewById(R.id.imageView16);
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    public void clearData() {
        embungItemList.clear();
        notifyDataSetChanged();
    }
}
