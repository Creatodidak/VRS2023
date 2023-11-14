package id.creatodidak.vrspolreslandak.adapter.karhutla;

import android.app.AlertDialog;
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
import id.creatodidak.vrspolreslandak.api.models.karhutla.DatakampanyekarhutlaItem;
import id.creatodidak.vrspolreslandak.database.DBHelper;
import id.creatodidak.vrspolreslandak.helper.CustomDialog;
import id.creatodidak.vrspolreslandak.helper.WaktuLokal;

public class LaporanKampanyeCegahKarhutlaAdapter extends RecyclerView.Adapter<LaporanKampanyeCegahKarhutlaAdapter.ViewHolder> {
    private final List<DatakampanyekarhutlaItem> list;
    private final Context context;
    private final LaporanKampanyeCegahKarhutlaAdapter.OnItemClickListener onItemClickListener;
    private final DBHelper dbHelper;
    private final SharedPreferences sh;
    private final String nrp;
    private final String pangkat;
    private final String nama;
    private final String satker;
    private final String satfung;
    private final String namapangkat;
    public LaporanKampanyeCegahKarhutlaAdapter(Context context, List<DatakampanyekarhutlaItem> list, LaporanKampanyeCegahKarhutlaAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;

        dbHelper = new DBHelper(context);
        dbHelper.inisialisasi();

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
    public LaporanKampanyeCegahKarhutlaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listchat, parent, false);
        return new LaporanKampanyeCegahKarhutlaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DatakampanyekarhutlaItem item = list.get(position);
        holder.tvJudul.setText("Laporan Kampanye Mengajak Masyarakat Bersama Mencegah Karhutla di "+ item.getLokasi());
        holder.tvKoordinat.setText("ID: #"+item.getDataid());
        holder.tvStatus.setVisibility(View.GONE);

        if(item.getCreatedAt().contains(WaktuLokal.getTanggal())){
            if(item.isLocal()){
                holder.ivIndikator.setImageResource(R.drawable.baseline_report_24);
            }else{
                holder.ivIndikator.setImageResource(R.drawable.baseline_checklist_24);
            }

            holder.wrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(item.getDataid());
                }
            });
        }else{
            holder.tvStatus.setText("Tugas kedaluwarsa ");
            holder.ivIndikator.setImageResource(R.drawable.baseline_edit_off_24);
            if(!nrp.equals("98070129")) {
                holder.wrapper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                    }
                });
            }else{
                holder.wrapper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onClick(item.getDataid());
                    }
                });
            }
        }
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
            tvLokasi.setVisibility(View.GONE);
        }
    }

    public interface OnItemClickListener {
        void onClick(String dataid);
    }
}
