package id.creatodidak.vrspolreslandak.helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;

import id.creatodidak.vrspolreslandak.R;

public class CustomDatePickerDialog extends Dialog {

    private DatePicker datePicker;
    private TextView btnCancel, btnOk;
    private final OnDateSetListener dateSetListener;

    public CustomDatePickerDialog(@NonNull Context context, OnDateSetListener listener) {
        super(context);
        dateSetListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.custom_date_picker, null);
        datePicker = contentView.findViewById(R.id.customDatePicker);
        btnCancel = contentView.findViewById(R.id.btnCancel);
        btnOk = contentView.findViewById(R.id.btnOk);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth()+1;
                int day = datePicker.getDayOfMonth();
                if (dateSetListener != null) {
                    dateSetListener.onDateSet(year, month, day);
                }
                dismiss();
            }
        });

        setContentView(contentView);
    }

    // Implementasikan logika lainnya sesuai kebutuhan

    public interface OnDateSetListener {
        void onDateSet(int year, int month, int day);
    }
}
