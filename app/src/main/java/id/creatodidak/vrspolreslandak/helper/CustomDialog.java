package id.creatodidak.vrspolreslandak.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import id.creatodidak.vrspolreslandak.R;

public class CustomDialog {

    private static AlertDialog currentDialog;
    private static TextView title, msg, ok, no;
    private static ProgressBar pb;
    private static LinearLayout konfirmasi;


    public static AlertDialog up(Context context, String titleText, String messageText, String okText,
                                 String cancelText, AlertDialogListener listener, boolean visibleok,
                                 boolean visibleno, boolean visiblepb) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.global_dialog, null);

        konfirmasi = view.findViewById(R.id.konfirmasi);

        title = view.findViewById(R.id.txDialogTitle);
        msg = view.findViewById(R.id.txDialogMsg);
        ok = view.findViewById(R.id.btDialogOk);
        no = view.findViewById(R.id.btnOk);
        pb = view.findViewById(R.id.pbDialog);

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
        msg.setText(messageText);
        ok.setText(okText);
        no.setText(cancelText);
        builder.setCancelable(false);
        currentDialog = builder.create(); // Simpan objek AlertDialog yang baru dibuat
        currentDialog.setView(view, 50, 0, 50, 0);
        currentDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

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

