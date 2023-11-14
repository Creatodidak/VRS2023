package id.creatodidak.vrspolreslandak.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.DaftarhotspotItem;
import id.creatodidak.vrspolreslandak.api.models.ServerResponse;
import id.creatodidak.vrspolreslandak.database.DBHelper;
import id.creatodidak.vrspolreslandak.helper.CustomDialog;
import id.creatodidak.vrspolreslandak.helper.DateUtils;
import id.creatodidak.vrspolreslandak.helper.Hitungjarak;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListKarhutlaAdapter extends RecyclerView.Adapter<ListKarhutlaAdapter.DaftarhotspotItemViewHolder> {

    private final OnItemClickListener onItemClickListener;
    private final List<DaftarhotspotItem> daftarhotspotItemList;
    private final Context context;
    private boolean isTable = false;
    private final DBHelper dbHelper;
    private final Endpoint endpoint;
    SharedPreferences sharedPreferences;
    String wilayah, nrp, namapangkat;

    List<String> sudah = new ArrayList<>();

    public ListKarhutlaAdapter(Context context, List<DaftarhotspotItem> daftarhotspotItemList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.daftarhotspotItemList = daftarhotspotItemList;
        this.onItemClickListener = onItemClickListener;

        dbHelper = new DBHelper(context);
        dbHelper.getWritableDatabase();
        isTable = dbHelper.isTableExists("draftcekhotspot");
        if (!isTable) {
            dbHelper.createDataDraftCekHotspot();
        }

        endpoint = Client.getClient().create(Endpoint.class);
        sharedPreferences = context.getSharedPreferences("SESSION_DATA", Context.MODE_PRIVATE);

        wilayah = sharedPreferences.getString("satker", null);
        nrp = sharedPreferences.getString("nrp", null);
        namapangkat = sharedPreferences.getString("pangkat", null) + " " + sharedPreferences.getString("nama", null);
    }

    @NonNull
    @Override
    public DaftarhotspotItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listresponse, parent, false);
        return new DaftarhotspotItemViewHolder(view);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull DaftarhotspotItemViewHolder holder, int position) {
        DaftarhotspotItem DaftarhotspotItem = daftarhotspotItemList.get(position);

        if (isTable) {

            if (Integer.parseInt(DaftarhotspotItem.getConfidence()) == 7) {
                holder.icon.setImageResource(R.drawable.firegreen);
                holder.status.setText("LOW");
            } else if (Integer.parseInt(DaftarhotspotItem.getConfidence()) == 8) {
                holder.icon.setImageResource(R.drawable.fireyellow);
                holder.status.setText("MEDIUM");
            } else if (Integer.parseInt(DaftarhotspotItem.getConfidence()) == 9) {
                holder.icon.setImageResource(R.drawable.firered);
                holder.status.setText("HIGH");
            } else {
                holder.icon.setImageResource(R.drawable.fireblack);
                holder.status.setText("UNKNOWN");
            }

            Log.d("PENANGANAN KARHUTLA ", "lokasi:" + DaftarhotspotItem.getLocation() + " | draft:" + dbHelper.cekDataDraftCekHotspot(DaftarhotspotItem.getDataId()) + " |status:" + DaftarhotspotItem.getStatus());

            if (DaftarhotspotItem.getPosisi().equals("LOCAL")) {
                if (cekdraft(DaftarhotspotItem.getDataId())) {
                    holder.drafts.setVisibility(View.VISIBLE);
                    holder.tambah.setVisibility(View.GONE);
                    holder.verifikasi.setText("DRAFT");
                    holder.kirim.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onItemClickListener != null) {
                                onItemClickListener.onKirimDraft(DaftarhotspotItem.getDataId());
                            }
                        }
                    });

                    Cursor cursor = dbHelper.getSingleDataCekhotspot(DaftarhotspotItem.getDataId());
                    if (cursor != null && cursor.moveToFirst()) {
                        String[] coord = cursor.getString(cursor.getColumnIndex("lokasi")).split(",");
                        String[] foto = cursor.getString(cursor.getColumnIndex("foto")).split(",");
                        Glide.with(context).load(foto[0]).into(holder.dfoto);
                        Glide.with(context).load(foto[1]).into(holder.dfoto2);
                        Glide.with(context).load(foto[2]).into(holder.dfoto3);
                        Glide.with(context).load(foto[3]).into(holder.dfoto4);
                        Glide.with(context).load(foto[4]).into(holder.dfoto5);

                        holder.dfoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showZoomableImageDialog(foto[0], "FOTO 1");
                            }
                        });

                        holder.dfoto2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showZoomableImageDialog(foto[1], "FOTO 2");
                            }
                        });

                        holder.dfoto3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showZoomableImageDialog(foto[2], "FOTO 3");
                            }
                        });

                        holder.dfoto4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showZoomableImageDialog(foto[3], "FOTO 4");
                            }
                        });

                        holder.dfoto5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showZoomableImageDialog(foto[4], "FOTO 5");
                            }
                        });

                        holder.dtxLokasi.setText("LAT: " + coord[0] + "\nLONG: " + coord[1]);
                        double calculatedDistance = Hitungjarak.calculateDistance(
                                Double.parseDouble(cursor.getString(cursor.getColumnIndex("latitude"))),
                                Double.parseDouble(cursor.getString(cursor.getColumnIndex("longitude"))),
                                Double.parseDouble(coord[0]),
                                Double.parseDouble(coord[1])
                        );
                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        String formattedDistance = decimalFormat.format(calculatedDistance);
                        holder.dtxJarak.setText("±" + formattedDistance + " KM");
                        holder.dtxDataId.setText(cursor.getString(cursor.getColumnIndex("data_id")));
                        holder.dtxTerdeteksi.setText(cursor.getString(cursor.getColumnIndex("terdeteksi")));
                        holder.dtxKoordinat.setText(cursor.getString(cursor.getColumnIndex("latitude")) + ", " + cursor.getString(cursor.getColumnIndex("longitude")));
                        holder.dtxNama.setText(namapangkat);
                        holder.detPemilik.setText(cursor.getString(cursor.getColumnIndex("pemilik")));
                        holder.detLuas.setText(cursor.getString(cursor.getColumnIndex("luas")));
                        holder.detPenyebab.setText(cursor.getString(cursor.getColumnIndex("penyebab")));
                        holder.detKondisi.setText(cursor.getString(cursor.getColumnIndex("kondisi")));
                        holder.detRincian.setText(cursor.getString(cursor.getColumnIndex("rincian")));

                        holder.dtxCreated.setText(DateUtils.tanggaldaricreatedatlocal(cursor.getString(cursor.getColumnIndex("created_at"))));
                        cursor.close();
                    }

                    holder.edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.simpan.setVisibility(View.VISIBLE);
                            holder.batal.setVisibility(View.VISIBLE);
                            holder.edit.setVisibility(View.GONE);
                            holder.kirim.setVisibility(View.GONE);
                            holder.detPemilik.setEnabled(true);
                            holder.detLuas.setEnabled(true);
                            holder.detPenyebab.setEnabled(true);
                            holder.detKondisi.setEnabled(true);
                            holder.detRincian.setEnabled(true);
                        }
                    });

                    holder.simpan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CustomDialog.up(
                                    view.getContext(),
                                    "Konfirmasi",
                                    "Anda yakin ingin mengubah data ini?",
                                    "YA, UBAH",
                                    "BATAL",
                                    new CustomDialog.AlertDialogListener() {
                                        @Override
                                        public void onPositiveButtonClick(AlertDialog alert) {
                                            holder.detPemilik.setEnabled(false);
                                            holder.detLuas.setEnabled(false);
                                            holder.detPenyebab.setEnabled(false);
                                            holder.detKondisi.setEnabled(false);
                                            holder.detRincian.setEnabled(false);

                                            String PEMILIK = holder.detPemilik.getText().toString();
                                            String LUAS = holder.detLuas.getText().toString();
                                            String PENYEBAB = holder.detPenyebab.getText().toString();
                                            String KONDISI = holder.detKondisi.getText().toString();
                                            String RINCIAN = holder.detRincian.getText().toString();
                                            String DATAID = holder.dtxDataId.getText().toString();

                                            if (updateDB(DATAID, PEMILIK, LUAS, PENYEBAB, KONDISI, RINCIAN)) {
                                                TimeZone timeZone = TimeZone.getTimeZone("Asia/Jakarta");
                                                Calendar calendar = Calendar.getInstance(timeZone);
                                                long currentTimeMillis = calendar.getTimeInMillis();

                                                String formattedAdjustedTime = DateFormat.format("yyyy-MM-dd HH:mm:ss", currentTimeMillis).toString();
                                                holder.edit.setVisibility(View.VISIBLE);
                                                holder.kirim.setVisibility(View.VISIBLE);
                                                holder.simpan.setVisibility(View.GONE);
                                                holder.batal.setVisibility(View.GONE);
                                                holder.dtxCreated.setText(DateUtils.tanggaldaricreatedatlocal(formattedAdjustedTime));
                                                alert.dismiss();
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
                    });

                    holder.batal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CustomDialog.up(
                                    view.getContext(),
                                    "Konfirmasi",
                                    "Data yang sudah anda ubah akan hilang dan data tidak akan diperbarui, lanjutkan?",
                                    "LANJUTKAN",
                                    "BATAL",
                                    new CustomDialog.AlertDialogListener() {
                                        @Override
                                        public void onPositiveButtonClick(AlertDialog alert) {
                                            holder.detPemilik.setEnabled(false);
                                            holder.detLuas.setEnabled(false);
                                            holder.detPenyebab.setEnabled(false);
                                            holder.detKondisi.setEnabled(false);
                                            holder.detRincian.setEnabled(false);

                                            holder.edit.setVisibility(View.VISIBLE);
                                            holder.kirim.setVisibility(View.VISIBLE);
                                            holder.simpan.setVisibility(View.GONE);
                                            holder.batal.setVisibility(View.GONE);
                                            Cursor cursor2 = dbHelper.getSingleDataCekhotspot(DaftarhotspotItem.getDataId());
                                            if (cursor2 != null && cursor2.moveToFirst()) {
                                                String[] coord = cursor2.getString(cursor2.getColumnIndex("lokasi")).split(",");
                                                Glide.with(context).load(cursor2.getString(cursor2.getColumnIndex("foto"))).into(holder.dfoto);
                                                holder.dtxLokasi.setText("LAT: " + coord[0] + "\nLONG: " + coord[1]);
                                                double calculatedDistance = Hitungjarak.calculateDistance(
                                                        Double.parseDouble(cursor2.getString(cursor2.getColumnIndex("latitude"))),
                                                        Double.parseDouble(cursor2.getString(cursor2.getColumnIndex("longitude"))),
                                                        Double.parseDouble(coord[0]),
                                                        Double.parseDouble(coord[1])
                                                );
                                                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                                String formattedDistance = decimalFormat.format(calculatedDistance);
                                                holder.dtxJarak.setText("±" + formattedDistance + " KM");
                                                holder.dtxDataId.setText(cursor2.getString(cursor2.getColumnIndex("data_id")));
                                                holder.dtxTerdeteksi.setText(cursor2.getString(cursor2.getColumnIndex("terdeteksi")));
                                                holder.dtxKoordinat.setText(cursor2.getString(cursor2.getColumnIndex("latitude")) + ", " + cursor2.getString(cursor2.getColumnIndex("longitude")));
                                                holder.dtxNama.setText(namapangkat);
                                                holder.detPemilik.setText(cursor2.getString(cursor2.getColumnIndex("pemilik")));
                                                holder.detLuas.setText(cursor2.getString(cursor2.getColumnIndex("luas")));
                                                holder.detPenyebab.setText(cursor2.getString(cursor2.getColumnIndex("penyebab")));
                                                holder.detKondisi.setText(cursor2.getString(cursor2.getColumnIndex("kondisi")));
                                                holder.detRincian.setText(cursor2.getString(cursor2.getColumnIndex("rincian")));

                                                holder.dtxCreated.setText(DateUtils.tanggaldaricreatedatlocal(cursor2.getString(cursor2.getColumnIndex("created_at"))));
                                                cursor2.close();
                                            }
                                            alert.dismiss();
                                        }

                                        @Override
                                        public void onNegativeButtonClick(AlertDialog alert) {
                                            alert.dismiss();
                                        }
                                    },
                                    true, true, false
                            ).show();
                        }
                    });
                } else {
                    holder.verifikasi.setText("BELUM ADA LAPORAN");
                    holder.drafts.setVisibility(View.GONE);
                    holder.tambah.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onItemClickListener != null) {
                                onItemClickListener.onResponseClick(DaftarhotspotItem.getDataId(), DaftarhotspotItem.getLocation());
                            }
                        }
                    });
                }
            } else if (DaftarhotspotItem.getPosisi().equals("SERVER")) {
                holder.drafts.setVisibility(View.GONE);
                holder.tambah.setVisibility(View.GONE);
                if (!isSudah(DaftarhotspotItem.getDataId(), sudah)) {
                    Call<ServerResponse> call = endpoint.getLaporanCekHotspotVerifikasi(DaftarhotspotItem.getDataId());
                    call.enqueue(new Callback<ServerResponse>() {
                        @Override
                        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                            if (response.body() != null & response.isSuccessful()) {
                                if (response.body().isStatus()) {
                                    holder.verifikasi.setText(response.body().getMsg());
                                } else {
                                    holder.verifikasi.setText("GAGAL MENGAMBIL STATUS LAPORAN");
                                }
                            } else {
                                holder.verifikasi.setText("GAGAL MENGAMBIL STATUS LAPORAN");
                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResponse> call, Throwable t) {
                            holder.verifikasi.setText("GAGAL MENGAMBIL STATUS LAPORAN");
                        }
                    });

                    sudah.add(DaftarhotspotItem.getDataId());
                }
            }

            holder.koordinat.setText("(" + DaftarhotspotItem.getLatitude() + " , " + DaftarhotspotItem.getLongitude() + ")");
            holder.lokasi.setText(DaftarhotspotItem.getLocation());

        }
    }

    private boolean updateDB(String dataid, String pemilik, String luas, String penyebab, String kondisi, String rincian) {
        return dbHelper.updDataDraftExist(dataid, pemilik, luas, penyebab, kondisi, rincian);
    }

    @Override
    public int getItemCount() {
        return daftarhotspotItemList.size();
    }

    static class DaftarhotspotItemViewHolder extends RecyclerView.ViewHolder {
        TextView status, lokasi, koordinat, verifikasi;
        ImageView icon, dfoto, dfoto2, dfoto3, dfoto4, dfoto5;
        Button tambah, kirim, edit, simpan, batal;
        LinearLayout drafts;
        TextView dtxNama, dtxDataId, dtxTerdeteksi, dtxKoordinat, dtxLokasi, dtxJarak, dtxCreated;
        EditText detPemilik, detLuas, detPenyebab, detKondisi, detRincian;

        DaftarhotspotItemViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status);
            lokasi = itemView.findViewById(R.id.lkLokasi);
            koordinat = itemView.findViewById(R.id.lkKoordinat);
            icon = itemView.findViewById(R.id.lkIcon);
            tambah = itemView.findViewById(R.id.btnResponseTambahLaporan);
            drafts = itemView.findViewById(R.id.drafts);
            kirim = itemView.findViewById(R.id.btnKirimDraft);
            verifikasi = itemView.findViewById(R.id.tugasselesai);
            dtxNama = itemView.findViewById(R.id.txNama);
            dtxDataId = itemView.findViewById(R.id.txDataId);
            dtxTerdeteksi = itemView.findViewById(R.id.txTerdeteksi);
            dtxKoordinat = itemView.findViewById(R.id.txKoordinat);
            dtxLokasi = itemView.findViewById(R.id.txLokasi);
            dtxJarak = itemView.findViewById(R.id.txJarak);
            detPemilik = itemView.findViewById(R.id.etPemilik);
            detLuas = itemView.findViewById(R.id.etLuas);
            detPenyebab = itemView.findViewById(R.id.etPenyebab);
            detKondisi = itemView.findViewById(R.id.etKondisi);
            detRincian = itemView.findViewById(R.id.etRincian);
            dtxCreated = itemView.findViewById(R.id.txCreated);
            dfoto = itemView.findViewById(R.id.foto);
            dfoto2 = itemView.findViewById(R.id.foto2);
            dfoto3 = itemView.findViewById(R.id.foto3);
            dfoto4 = itemView.findViewById(R.id.foto4);
            dfoto5 = itemView.findViewById(R.id.foto5);
            edit = itemView.findViewById(R.id.btEdit);
            simpan = itemView.findViewById(R.id.btSimpan);
            batal = itemView.findViewById(R.id.btBatal);
        }
    }

    public interface OnItemClickListener {
        void onResponseClick(String itemId, String geocoder);

        void onKirimDraft(String itemId);
    }

    private boolean cekdraft(String data_id) {
        return dbHelper.cekDataDraftCekHotspot(data_id);
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
        daftarhotspotItemList.clear();
        notifyDataSetChanged();
    }
}
