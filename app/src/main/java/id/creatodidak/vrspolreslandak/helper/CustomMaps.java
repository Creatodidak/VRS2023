package id.creatodidak.vrspolreslandak.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import id.creatodidak.vrspolreslandak.R;

public class CustomMaps {

    private static AlertDialog currentDialog;

    public static AlertDialog up(Context context, String type, String Nama, String Jarak, String Lokasi, double Latitude, double Longitude, String Kapasitas, String Foto, CustomMaps.AlertDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custommaps, null);
        TextView tvNama = view.findViewById(R.id.tvNama);
        TextView tvJarak = view.findViewById(R.id.tvJarak);
        TextView tvLokasi = view.findViewById(R.id.tvLokasi);
        TextView tvLatitude = view.findViewById(R.id.tvLatitude);
        TextView tvLongitude = view.findViewById(R.id.tvLongitude);
        TextView tvKapasitas = view.findViewById(R.id.tvKapasitas);
        TextView tvType = view.findViewById(R.id.tvType);
        Button btPetunjuk = view.findViewById(R.id.btPetunjuk);
        Button btLaporan = view.findViewById(R.id.btLaporan);
        ImageView ivFoto = view.findViewById(R.id.ivFoto);
        ImageView ivExpand = view.findViewById(R.id.ivExpand);
        ImageView ivClose = view.findViewById(R.id.ivClose);

        Glide.with(context)
                .load(Foto)
                .error(R.drawable.defaultimg)
                .placeholder(R.drawable.defaultimg)
                .into(ivFoto);

        tvNama.setText(Nama);
        tvJarak.setText(Jarak);
        tvLokasi.setText(Lokasi);
        tvLatitude.setText(String.valueOf(Latitude));
        tvLongitude.setText(String.valueOf(Longitude));
        tvKapasitas.setText(Kapasitas);
        tvType.setText(type);

        btLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {listener.onLaporan(currentDialog);}
        });

        btPetunjuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {listener.onMaps(currentDialog);}
        });

        ivExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {listener.onExpand(currentDialog);}
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {listener.onClose(currentDialog);}
        });


        builder.setCancelable(false);
        currentDialog = builder.create(); // Simpan objek AlertDialog yang baru dibuat
        currentDialog.setView(view, 50, 0, 50, 0);
        currentDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        return currentDialog;
    }

    public static void down(AlertDialog alertDialog) {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public interface AlertDialogListener {
        void onLaporan(AlertDialog alert);

        void onMaps(AlertDialog alert);
        void onExpand(AlertDialog alert);
        void onClose(AlertDialog alert);
    }
}

