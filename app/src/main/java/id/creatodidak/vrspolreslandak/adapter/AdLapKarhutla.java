package id.creatodidak.vrspolreslandak.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.DataverifikasikarhutlaItem;
import id.creatodidak.vrspolreslandak.api.models.ServerResponse;
import id.creatodidak.vrspolreslandak.helper.DateUtils;
import id.creatodidak.vrspolreslandak.helper.Hitungjarak;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdLapKarhutla extends RecyclerView.Adapter<AdLapKarhutla.ViewHolder> {
    private final Context context;
    private final List<DataverifikasikarhutlaItem> dataList;
    List<String> sudah = new ArrayList<>();
    private final Endpoint endpoint;

    SharedPreferences sh;

    public AdLapKarhutla(Context context, List<DataverifikasikarhutlaItem> dataList) {
        this.context = context;
        this.dataList = dataList;

        endpoint = Client.getClient().create(Endpoint.class);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listdetilkarhutla2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataverifikasikarhutlaItem item = dataList.get(position);
        sh = context.getSharedPreferences("SESSION_DATA", Context.MODE_PRIVATE);

        holder.txJudul.setText("LAPORAN PENGECEKAN HOTSPOT DI WILAYAH HUKUM "+ item.getSatker()+" DISEKITAR "+item.getGeocoder().toUpperCase());
        String[] fotos = item.getFoto().split(",");

        Glide.with(context).load(fotos[0]).into(holder.foto);
        Glide.with(context).load(fotos[1]).into(holder.foto2);
        Glide.with(context).load(fotos[2]).into(holder.foto3);
        Glide.with(context).load(fotos[3]).into(holder.foto4);
        Glide.with(context).load(fotos[4]).into(holder.foto5);

        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showZoomableImageDialog(fotos[0], "FOTO 1");
            }
        });

        holder.foto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showZoomableImageDialog(fotos[1], "FOTO 2");
            }
        });

        holder.foto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showZoomableImageDialog(fotos[2], "FOTO 3");
            }
        });

        holder.foto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showZoomableImageDialog(fotos[3], "FOTO 4");
            }
        });

        holder.foto5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showZoomableImageDialog(fotos[4], "FOTO 5");
            }
        });

        String[] coord = item.getLokasi().split(",");

        holder.txDataId.setText(item.getDataId());
        holder.txTerdeteksi.setText("BRIN Fire Hotspot mendeteksi Titik Api dalam mode pixel pada "+DateUtils.tanggaldaricreatedatlocal(item.getTerdeteksi()));
        holder.txKoordinat.setText("LAT: "+item.getLatitude() + "\nLONG: " + item.getLongitude());
        holder.txNama.setText(item.getNama());
        holder.txLokasi.setText("LAT: "+coord[0] + "\nLONG: " + coord[1]);
        holder.txPemilik.setText(item.getPemilik());
        holder.txLuas.setText(item.getLuas()+" Ha");
        holder.txPenyebab.setText(item.getPenyebab().replace("\\n", "\n"));
        holder.txKondisi.setText(item.getKondisi().replace("\\n", "\n"));
        holder.txRincian.setText(item.getRincian().replace("\\n", "\n"));
        holder.txCreated.setText(DateUtils.tanggaldaricreatedatlocal(item.getCreatedAt()));
        double calculatedDistance = Hitungjarak.calculateDistance(
                Double.parseDouble(item.getLatitude()),
                Double.parseDouble(item.getLongitude()),
                Double.parseDouble(coord[0]),
                Double.parseDouble(coord[1])
        );

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedDistance = decimalFormat.format(calculatedDistance);

        holder.jarak.setText("±" + formattedDistance + " KM");
        holder.btTolak.setVisibility(View.GONE);
        holder.btVerifikasi.setVisibility(View.GONE);
        if(!isSudah(item.getDataId(), sudah)){
            Call<ServerResponse> call = endpoint.getLaporanCekHotspotVerifikasi(item.getDataId());
            call.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    if(response.body() != null & response.isSuccessful()) {
                        if (response.body().isStatus()) {
                            holder.verifikasi.setText(response.body().getMsg());
                        } else {
                            holder.verifikasi.setText("GAGAL MENGAMBIL STATUS LAPORAN");
                        }
                    }else{
                        holder.verifikasi.setText("GAGAL MENGAMBIL STATUS LAPORAN");
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    holder.verifikasi.setText("GAGAL MENGAMBIL STATUS LAPORAN");
                }
            });

            sudah.add(item.getDataId());
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txJudul, txDataId, txTerdeteksi, txKoordinat, txNama, txLokasi, txPemilik, txLuas, txPenyebab, txKondisi, txRincian, txCreated, jarak, verifikasi;
        ImageView foto, foto2, foto3, foto4, foto5;
        Button btTolak, btVerifikasi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txJudul = itemView.findViewById(R.id.txJudul);
            foto = itemView.findViewById(R.id.foto);
            foto2 = itemView.findViewById(R.id.foto2);
            foto3 = itemView.findViewById(R.id.foto3);
            foto4 = itemView.findViewById(R.id.foto4);
            foto5 = itemView.findViewById(R.id.foto5);
            txDataId = itemView.findViewById(R.id.txDataId);
            txTerdeteksi = itemView.findViewById(R.id.txTerdeteksi);
            txKoordinat = itemView.findViewById(R.id.txKoordinat);
            txNama = itemView.findViewById(R.id.txNama);
            txLokasi = itemView.findViewById(R.id.txLokasi);
            txPemilik = itemView.findViewById(R.id.txPemilik);
            txLuas = itemView.findViewById(R.id.txLuas);
            txPenyebab = itemView.findViewById(R.id.txPenyebab);
            txKondisi = itemView.findViewById(R.id.txKondisi);
            txRincian = itemView.findViewById(R.id.txRincian);
            txCreated = itemView.findViewById(R.id.txCreated);
            btTolak = itemView.findViewById(R.id.btEdit);
            btVerifikasi = itemView.findViewById(R.id.btVerifikasi);
            jarak = itemView.findViewById(R.id.txJarak);
            verifikasi = itemView.findViewById(R.id.txStatus);
        }
    }
    public static boolean isSudah(String nama, List<String> list) {
        return list.contains(nama);
    }

    private void showZoomableImageDialog(String imageUrl, String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.myFullscreenAlertDialogStyle);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_image_zoom, null);

        PhotoView zoomableImageView = view.findViewById(R.id.zoomableImageView);
        TextView judul = view.findViewById(R.id.textView51);
        judul.setText(s);
        Button btn = view.findViewById(R.id.button);
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.defaultimg)
                .error(R.drawable.failedimg)
                .apply(requestOptions)
                .into(zoomableImageView);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        view.setLayoutParams(layoutParams);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);

        WindowManager.LayoutParams layoutParamss = new WindowManager.LayoutParams();
        layoutParamss.copyFrom(dialog.getWindow().getAttributes());
        layoutParamss.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamss.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParamss);

        dialog.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearData() {
        dataList.clear();
        notifyDataSetChanged();
    }
}
