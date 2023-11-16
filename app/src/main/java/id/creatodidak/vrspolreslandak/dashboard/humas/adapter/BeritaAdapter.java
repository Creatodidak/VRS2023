package id.creatodidak.vrspolreslandak.dashboard.humas.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.dashboard.humas.model.DataItem;
import id.creatodidak.vrspolreslandak.helper.DateUtils;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.BeritaViewHolder> {

    private Context context;
    private List<DataItem> beritaList;

    public BeritaAdapter(Context context, List<DataItem> beritaList) {
        this.context = context;
        this.beritaList = beritaList;
    }

    @NonNull
    @Override
    public BeritaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.beritaitem, parent, false);
        return new BeritaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BeritaViewHolder holder, int position) {
        DataItem beritaItem = beritaList.get(position);

        holder.newsView.setText(String.valueOf(beritaItem.getView()));
        holder.newsCategory.setText(beritaItem.getKategori());
        holder.newsSatker.setText(beritaItem.getSatker());
        holder.newsJudul.setText(beritaItem.getJudul().toUpperCase());
        holder.newsPubName.setText(beritaItem.getPublisher());
        holder.newsDate.setText(DateUtils.tanggaldaricreatedat(beritaItem.getCreatedAt()));
        Glide.with(context)
                .load(beritaItem.getFoto())
                .into(holder.newsImg);
    }

    @Override
    public int getItemCount() {
        return beritaList.size();
    }

    public static class BeritaViewHolder extends RecyclerView.ViewHolder {
        CardView newsWrapper;
        ImageView newsImg, newsPubImg;
        TextView newsView, newsCategory, newsSatker, newsJudul, newsPubName, newsDate;

        public BeritaViewHolder(@NonNull View itemView) {
            super(itemView);

            newsWrapper = itemView.findViewById(R.id.newsWrapper);
            newsImg = itemView.findViewById(R.id.newsImg);
            newsView = itemView.findViewById(R.id.newsView);
            newsCategory = itemView.findViewById(R.id.newsCategory);
            newsSatker = itemView.findViewById(R.id.newsSatker);
            newsJudul = itemView.findViewById(R.id.newsJudul);
            newsPubImg = itemView.findViewById(R.id.newsPubImg);
            newsPubName = itemView.findViewById(R.id.newsPubName);
            newsDate = itemView.findViewById(R.id.newsDate);
        }
    }
}
