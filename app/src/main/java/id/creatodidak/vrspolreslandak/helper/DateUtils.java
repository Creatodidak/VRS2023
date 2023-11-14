package id.creatodidak.vrspolreslandak.helper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static String getTodayFormatted() {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM yyyy", new Locale("id"));
        return sdf.format(today);
    }

    public static String deteksiWaktu() {
        Calendar calendar = Calendar.getInstance();
        int jam = calendar.get(Calendar.HOUR_OF_DAY);

        if (jam >= 0 && jam < 12) {
            return "Selamat Pagi";
        } else if (jam >= 12 && jam < 17) {
            return "Selamat Siang";
        } else if (jam >= 17 && jam < 20) {
            return "Selamat Sore";
        } else {
            return "Selamat Malam";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getCurrentTimeWithFormat() {
        // Get the current time
        Date currentTime = new Date();

        // Create a SimpleDateFormat object with the desired format
        SimpleDateFormat sdf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat("HH:mm:ss z");
        }
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7")); // Set the desired time zone

        // Format the current time using the SimpleDateFormat object
        String formattedTime = sdf.format(currentTime);

        return formattedTime;
    }

    public static String tanggaldaricreatedat(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy 'pukul' HH:mm:ss zzz", new Locale("id", "ID"));

        try {
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ""; // Return empty string if parsing fails
    }

    public static String tanggaldaricreatedatlocal(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy 'Pukul' HH:mm:ss 'WIB'", new Locale("id", "ID"));

        try {
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ""; // Return empty string if parsing fails
    }

    public static String haridantanggallaporan(String tanggal) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("id"));
        SimpleDateFormat outputFormat = new SimpleDateFormat("'hari 'EEEE 'tanggal' dd MMMM yyyy", new Locale("id", "ID"));

        try {
            Date date = inputFormat.parse(tanggal);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String waktuchat(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", new Locale("id", "ID"));

        try {
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ""; // Return empty string if parsing fails
    }

    public static String waktuchatkemaren(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd mm' ' HH:mm:ss", new Locale("id", "ID"));

        try {
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ""; // Return empty string if parsing fails
    }

}
