package id.creatodidak.vrspolreslandak.helper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Objects;

import id.creatodidak.vrspolreslandak.R;

@SuppressLint("StaticFieldLeak")
public class AtensiDialog {

    private static AlertDialog currentDialog;

    public static AlertDialog up(Context context, String titleText, String namapimpinan, String pesan, String okText,
                                 String cancelText, AlertDialogListener listener, boolean visibleok,
                                 boolean visibleno, boolean visiblepb) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.atensidialog, null);

        LinearLayout konfirmasi = view.findViewById(R.id.konfirmasi);

        TextView title = view.findViewById(R.id.txDialogTitle);
        TextView msg = view.findViewById(R.id.txDialogMsg);
        TextView isi = view.findViewById(R.id.txDialogIsi);
        TextView ok = view.findViewById(R.id.btDialogOk);
        TextView no = view.findViewById(R.id.btnOk);
        ProgressBar pb = view.findViewById(R.id.pbDialog);

        if (visibleok && visibleno) {
            view.findViewById(R.id.divider15).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.divider15).setVisibility(View.GONE);
        }

        if (visibleok) {
            ok.setVisibility(View.VISIBLE);
        }

        if (visibleno) {
            no.setVisibility(View.VISIBLE);
        }

        if (visiblepb) {
            view.findViewById(R.id.divider16).setVisibility(View.GONE);
            konfirmasi.setVisibility(View.GONE);
            pb.setVisibility(View.VISIBLE);
            msg.setVisibility(View.GONE);
        }

        title.setText(titleText);
        msg.setText(namapimpinan);
        isi.setText(pesan);
        ok.setText(okText);
        no.setText(cancelText);
        builder.setCancelable(false);
        currentDialog = builder.create(); // Simpan objek AlertDialog yang baru dibuat
        currentDialog.setView(view, 50, 0, 50, 0);
        Objects.requireNonNull(currentDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPositiveButtonClick(currentDialog);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onNegativeButtonClick(currentDialog);
            }
        });

        return currentDialog;
    }

    public static void down(AlertDialog alertDialog) {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public interface AlertDialogListener {
        void onPositiveButtonClick(AlertDialog alert);
        void onNegativeButtonClick(AlertDialog alert);
    }
}

