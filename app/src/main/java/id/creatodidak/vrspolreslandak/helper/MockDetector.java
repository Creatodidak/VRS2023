package id.creatodidak.vrspolreslandak.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import id.creatodidak.vrspolreslandak.auth.Blokir;
import id.creatodidak.vrspolreslandak.dashboard.Dashboard;

public class MockDetector {
    private final Context context;
    private final SharedPreferences sharedPreferences;
    private final boolean isMockLocationDetected;

    public MockDetector(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        isMockLocationDetected = false;
    }

    public void checkMockLocation() {
        Location location = getCurrentLocation();
        boolean isMockLocation = isMockLocation(location);

        if (isMockLocation && !isMockLocationDetected) {
            CustomDialog.up(
                    context,
                    "PELANGGARAN",
                    "Anda terdeteksi menggunakan Fake GPS!",
                    "MATIKAN FAKE GPS",
                    "",
                    new CustomDialog.AlertDialogListener() {
                        @Override
                        public void onPositiveButtonClick(AlertDialog alert) {
                            if (context instanceof Activity) {
                                ((Activity) context).finish();
                            }

                            alert.dismiss();
                        }

                        @Override
                        public void onNegativeButtonClick(AlertDialog alert) {

                        }
                    },
                    true, false, false
            ).show();
        } else {
            Log.i("MOCK", context.getClass().getSimpleName() + " | AMAN MASEEEEHHH");
        }
    }

    private Location getCurrentLocation() {
        Location currentLocation = null;

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation != null) {
                    currentLocation = lastKnownLocation;
                }
            }
        }

        return currentLocation;
    }

    private boolean isMockLocation(Location location) {
        return location != null && location.isFromMockProvider();
    }

    private void showMockLocationWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Peringatan");
        builder.setMessage("ANDA TERDETEKSI MENGGUNAKAN FAKE GPS / MOCK GPS / LOKASI PALSU, SILAHKAN AKTIFKAN LOKASI ASLI ANDA...!!!\n\n" +
                "PERINGATAN INI HANYA MUNCUL SEKALI, APABILA SETELAH ANDA MENDAPATKAN PERINGATAN INI ANDA MASIH MENGGUNAKAN LOKASI PALSU, " +
                "MAKA SECARA OTOMATIS APLIKASI ANDA AKAN TERBLOKIR!!!");

        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                System.exit(0);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showMockLocationAlertDialog(String tokens) {
        if (context instanceof Activity && !((Activity) context).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Oooopppsss!!!");
            builder.setMessage("ANDA TERDETEKSI MENGGUNAKAN FAKE GPS / MOCK GPS / LOKASI PALSU KEMBALI, SILAHKAN TEKAN TOMBOL OK!");

            builder.setCancelable(false);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("blocked", true);
                    editor.putString("token", tokens);
                    editor.apply();

                    Intent intent = new Intent(context, Blokir.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
