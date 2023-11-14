package id.creatodidak.vrspolreslandak.api.models.chat;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class DaftarTugasAdapter extends RecyclerView.Adapter<DaftarTugasAdapter.ViewHolder> {

    private final List<DaftartugasItem> list;
    private final Context context;
    private final DaftarTugasAdapter.OnItemClickListener onItemClickListener;

    public DaftarTugasAdapter(Context context, List<DaftartugasItem> list, DaftarTugasAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listdaftartugas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DaftartugasItem item = list.get(position);
        holder.tugas.setText(item.getValue());
        if(item.isStatus()){
            if(item.getType().equals("image")){
                holder.ic.setImageResource(R.drawable.baseline_add_24);
            }else{
                holder.ic.setImageResource(R.drawable.check);
            }
        }else{
            holder.ic.setImageResource(R.drawable.baseline_report_24);
        }
        holder.wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!item.getType().equals("image")) {
                    if (!item.isStatus()) {
                        onItemClickListener.onItemClick(item.getParameter(), item.getType(), item.isStatus(), item.getValue(), position);
                    }
                }else{
                    onItemClickListener.onItemClick(item.getParameter(), item.getType(), item.isStatus(), item.getValue(), position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface OnItemClickListener {
        void onItemClick(String parameter, String type, boolean status, String value, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tugas;
        LinearLayout wrapper;
        ImageView ic;

        public ViewHolder(@NonNull View v) {
           super(v);
           tugas = v.findViewById(R.id.namaTugas);
           ic = v.findViewById(R.id.icTugas);
           wrapper = v.findViewById(R.id.wrapper);
        }
    }
}
