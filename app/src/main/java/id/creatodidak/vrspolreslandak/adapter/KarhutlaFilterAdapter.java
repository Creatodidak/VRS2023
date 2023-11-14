package id.creatodidak.vrspolreslandak.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.models.DaftarhotspotItem;
import id.creatodidak.vrspolreslandak.database.DBHelper;

public class KarhutlaFilterAdapter extends RecyclerView.Adapter<KarhutlaFilterAdapter.DaftarhotspotItemViewHolder> {

    private final List<DaftarhotspotItem> daftarhotspotItemList;
    private final List<DaftarhotspotItem> daftarhotspotItemListFilter;
    private final Context context;
    private final KarhutlaFilterAdapter.OnItemClickListener onItemClickListener;
    private String nrp = null;
    DBHelper dbHelper;

    public KarhutlaFilterAdapter(Context context, String nrp, List<DaftarhotspotItem> daftarhotspotItemList, KarhutlaFilterAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.daftarhotspotItemList = new ArrayList<>(daftarhotspotItemList);
        this.daftarhotspotItemListFilter = new ArrayList<>(daftarhotspotItemList);
        this.onItemClickListener = onItemClickListener;
        this.nrp = nrp;
    }

    @NonNull
    @Override
    public DaftarhotspotItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listkarhutla, parent, false);
        return new DaftarhotspotItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DaftarhotspotItemViewHolder holder, int position) {
        final DaftarhotspotItem daftarhotspotItem = daftarhotspotItemListFilter.get(position);

        dbHelper = new DBHelper(context);
        dbHelper.getWritableDatabase();


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (String.valueOf(daftarhotspotItem.getResponse()) != "null" && daftarhotspotItem.getResponse().equals(nrp)) {
                    holder.btResp.setText("LIHAT");
                    holder.btResp.setBackgroundResource(R.drawable.buttonblue);
                } else if(String.valueOf(daftarhotspotItem.getResponse()) != "null" && !daftarhotspotItem.getResponse().equals(nrp)) {
                    holder.btResp.setText("DIRESPON");
                    holder.btResp.setBackgroundResource(R.drawable.buttongray);
                }else if (String.valueOf(daftarhotspotItem.getResponse()) == "null"){
                    holder.btResp.setText("RESPONSE");
                    holder.btResp.setBackgroundResource(R.drawable.buttonred);
                }
            }
            Log.d("RESPONSE", String.valueOf(daftarhotspotItem.getResponse()));


        holder.maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMaps(Double.parseDouble(daftarhotspotItem.getLatitude()), Double.parseDouble(daftarhotspotItem.getLongitude()));
            }
        });

        holder.status.setText(daftarhotspotItem.getConfidence());

        if (Integer.parseInt(daftarhotspotItem.getConfidence()) == 7) {
            holder.icon.setImageResource(R.drawable.firegreen);
            holder.status.setText("LOW");
        } else if (Integer.parseInt(daftarhotspotItem.getConfidence()) == 8) {
            holder.icon.setImageResource(R.drawable.fireyellow);
            holder.status.setText("MEDIUM");
        } else if (Integer.parseInt(daftarhotspotItem.getConfidence()) == 9) {
            holder.icon.setImageResource(R.drawable.firered);
            holder.status.setText("HIGH");
        } else {
            holder.icon.setImageResource(R.drawable.fireblack);
            holder.status.setText("UNKNOWN");
        }

        holder.lokasi.setText(daftarhotspotItem.getGeocoders());

        holder.koordinat.setText("(" + daftarhotspotItem.getLatitude() + ", " + daftarhotspotItem.getLongitude() + ")");

        holder.btResp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    if (String.valueOf(daftarhotspotItem.getResponse()) != "null" && daftarhotspotItem.getResponse().equals(nrp)) {
                        String availmsg = daftarhotspotItem.getStatus();
                        onItemClickListener.onResponseClick(daftarhotspotItem.getId(), daftarhotspotItem.getLocation(), false, availmsg, true);
                    } else if(String.valueOf(daftarhotspotItem.getResponse()) != "null" && !daftarhotspotItem.getResponse().equals(nrp)) {
                        String availmsg = daftarhotspotItem.getStatus();
                        onItemClickListener.onResponseClick(daftarhotspotItem.getId(), daftarhotspotItem.getLocation(), false, availmsg, false);
                    }else if (String.valueOf(daftarhotspotItem.getResponse()) == "null"){
                        String availmsg = "OK";
                        onItemClickListener.onResponseClick(daftarhotspotItem.getId(), daftarhotspotItem.getLocation(), true, availmsg, false);
                    }
                }
            }
        });
    }

    private void openGoogleMaps(double latitude, double longitude) {
        String uri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Google Maps tidak terpasang", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return daftarhotspotItemListFilter.size();
    }

    class DaftarhotspotItemViewHolder extends RecyclerView.ViewHolder {
        TextView status, lokasi, koordinat, btResp;
        ImageView icon;
        Button maps;
        DaftarhotspotItemViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status);
            lokasi = itemView.findViewById(R.id.lkLokasi);
            koordinat = itemView.findViewById(R.id.lkKoordinat);
            icon = itemView.findViewById(R.id.lkIcon);
            maps = itemView.findViewById(R.id.btnMapslk);
            btResp = itemView.findViewById(R.id.btnResponselk);

        }
    }

    public void filterByWilayah(String wilayah) {
        daftarhotspotItemListFilter.clear();
        for (DaftarhotspotItem item : daftarhotspotItemList) {
            if (item.getWilayah() != null && item.getWilayah().equalsIgnoreCase(wilayah)) {
                daftarhotspotItemListFilter.add(item);
            }
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onResponseClick(int Id, String alamat, boolean avail, String availmsg, boolean isMe);
    }
}
