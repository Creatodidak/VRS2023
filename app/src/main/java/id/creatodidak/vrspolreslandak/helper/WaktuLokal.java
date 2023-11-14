package id.creatodidak.vrspolreslandak.helper;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class WaktuLokal {

    public static String get() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Jakarta");
        Calendar calendar = Calendar.getInstance(timeZone);
        long currentTimeMillis = calendar.getTimeInMillis();

        return DateFormat.format("yyyy-MM-dd HH:mm:ss", currentTimeMillis).toString();
    }

    public static String gettanggaldanjam() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Jakarta");
        Calendar calendar = Calendar.getInstance(timeZone);
        long currentTimeMillis = calendar.getTimeInMillis();

        return DateFormat.format("dd-MM-yyyy HH:mm:ss", currentTimeMillis).toString();
    }

    public static String getTanggal(){
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Jakarta");
        Calendar calendar = Calendar.getInstance(timeZone);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public static String getTanggalHari(String tanggal) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(tanggal);

            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, d MMMM yyyy", new Locale("id", "ID")); // Menggunakan Locale Indonesia untuk nama bulan
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return tanggal; // Jika terjadi kesalahan, kembalikan tanggal dalam format aslinya
        }
    }

    public static String formatDateTime(String dateTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
            Date date = inputFormat.parse(dateTime);

            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateTime; // Jika terjadi kesalahan, kembalikan tanggal dalam format aslinya
        }
    }


    public static String customTanggal(){
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Jakarta");
        Calendar calendar = Calendar.getInstance(timeZone);

        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        int hour;
        if (currentHour >= 0 && currentHour < 6) {
            hour = 0;
        } else if (currentHour >= 6 && currentHour < 12) {
            hour = 6;
        } else if (currentHour >= 12 && currentHour < 18) {
            hour = 12;
        } else {
            hour = 18;
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}