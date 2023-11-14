package id.creatodidak.vrspolreslandak.auth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.LoginResponse;
import id.creatodidak.vrspolreslandak.api.models.ServerResponse;
import id.creatodidak.vrspolreslandak.dashboard.Dashboard;
import id.creatodidak.vrspolreslandak.database.DBHelper;
import id.creatodidak.vrspolreslandak.helper.CustomDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    EditText nrp, pass;
    Button btnlogin;
    SharedPreferences sharedPreferences;
    int versionCode;
    Endpoint endpoint;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.P)
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new DBHelper(this).inisialisasi();
        sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        SharedPreferences sh = getSharedPreferences("ATENSI", MODE_PRIVATE);
        SharedPreferences.Editor ed = sh.edit();
        ed.putBoolean("INISIALISASI", true);
        ed.apply();
        nrp = findViewById(R.id.etNrp);
        pass = findViewById(R.id.etPass);
        btnlogin = findViewById(R.id.btLogin);
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        versionCode = packageInfo.versionCode;

        endpoint = Client.getClient().create(Endpoint.class);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(pass.getWindowToken(), 0);
                }
                AlertDialog alerts = CustomDialog.up(
                        Login.this,
                        "Mencoba login...",
                        "",
                        "",
                        "",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(android.app.AlertDialog alert) {

                            }

                            @Override
                            public void onNegativeButtonClick(android.app.AlertDialog alert) {

                            }
                        },
                        false, false, true
                );

                alerts.show();

                Call<LoginResponse> call = endpoint.login(nrp.getText().toString(), pass.getText().toString());
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                        if (response.isSuccessful()) {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse.isStatus()) {
                                List<LoginResponse.LoginDetail> loginDetails = loginResponse.getLoginDetails();
                                if (loginDetails != null && !loginDetails.isEmpty()) {
                                    alerts.dismiss();
                                    LoginResponse.LoginDetail data = loginDetails.get(0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("nrp", data.getNrp());
                                    editor.putString("nama", data.getNama());
                                    editor.putString("pangkat", data.getPangkat());
                                    editor.putString("satker", data.getSatker());
                                    editor.putString("satfung", data.getSatfung());
                                    editor.putString("jabatan", data.getJabatan());
                                    editor.putString("tanggal_lahir", data.getTanggalLahir());
                                    editor.putString("foto", data.getFoto());
                                    editor.putString("wa", data.getWa());
                                    editor.putString("wilayah", data.getWilayah());
                                    editor.putString("nik", data.getNik());
                                    editor.putBoolean("UPGRADE " + versionCode, true);
                                    editor.apply();

                                    getAndSaveFCMToken(data.getNrp(), data.getSatker());
                                } else {
                                    alerts.dismiss();
                                    CustomDialog.up(
                                            Login.this,
                                            "Informasi",
                                            "Gagal login ke aplikasi, pastikan NRP dan Password benar!",
                                            "ULANGI",
                                            "",
                                            new CustomDialog.AlertDialogListener() {
                                                @Override
                                                public void onPositiveButtonClick(AlertDialog alert) {
                                                    alert.dismiss();
                                                }

                                                @Override
                                                public void onNegativeButtonClick(AlertDialog alert) {

                                                }
                                            },
                                            true, false, false
                                    ).show();
                                }
                            } else {
                                alerts.dismiss();
                                CustomDialog.up(
                                        Login.this,
                                        "Informasi",
                                        "Gagal login ke aplikasi, pastikan NRP dan Password benar!",
                                        "ULANGI",
                                        "",
                                        new CustomDialog.AlertDialogListener() {
                                            @Override
                                            public void onPositiveButtonClick(AlertDialog alert) {
                                                alert.dismiss();
                                            }

                                            @Override
                                            public void onNegativeButtonClick(AlertDialog alert) {

                                            }
                                        },
                                        true, false, false
                                ).show();
                            }
                        } else {
                            alerts.dismiss();
                            CustomDialog.up(
                                    Login.this,
                                    "Informasi",
                                    "Gagal login ke aplikasi, pastikan NRP dan Password benar!",
                                    "ULANGI",
                                    "",
                                    new CustomDialog.AlertDialogListener() {
                                        @Override
                                        public void onPositiveButtonClick(AlertDialog alert) {
                                            alert.dismiss();
                                        }

                                        @Override
                                        public void onNegativeButtonClick(AlertDialog alert) {

                                        }
                                    },
                                    true, false, false
                            ).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        alerts.dismiss();
                        CustomDialog.up(
                                Login.this,
                                "Informasi",
                                "Gagal login ke aplikasi, periksa jaringan internet anda!",
                                "ULANGI",
                                "",
                                new CustomDialog.AlertDialogListener() {
                                    @Override
                                    public void onPositiveButtonClick(AlertDialog alert) {
                                        alert.dismiss();
                                    }

                                    @Override
                                    public void onNegativeButtonClick(AlertDialog alert) {

                                    }
                                },
                                true, false, false
                        ).show();
                    }
                });

            }
        });

        if (sharedPreferences.getBoolean("UPGRADE " + versionCode, false)) {
            checklogin();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            checklogin();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void checklogin() {
        if (sharedPreferences.getBoolean("login", false)) {
            if (sharedPreferences.getString("PIN", null) != null) {
                if (!sharedPreferences.getString("nrp", "0").equals("0") && sharedPreferences.getString("nrp", "0").equals("98070129")) {
                    Intent intent = new Intent(Login.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Login.this, Login2.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Intent intent = new Intent(Login.this, Setlogin2.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void getAndSaveFCMToken(String nrp, String wilayah) {
        AlertDialog alerts = CustomDialog.up(
                Login.this,
                "Mengambil Token FCM...",
                "",
                "",
                "",
                new CustomDialog.AlertDialogListener() {
                    @Override
                    public void onPositiveButtonClick(android.app.AlertDialog alert) {

                    }

                    @Override
                    public void onNegativeButtonClick(android.app.AlertDialog alert) {

                    }
                },
                false, false, true
        );

        alerts.show();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        alerts.dismiss();
                        CustomDialog.up(
                                Login.this,
                                "Informasi",
                                "Gagal mengambil Token FCM, silahkan Login Ulang",
                                "ULANGI",
                                "",
                                new CustomDialog.AlertDialogListener() {
                                    @Override
                                    public void onPositiveButtonClick(AlertDialog alert) {
                                        SharedPreferences sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.apply();
                                        alert.dismiss();
                                    }

                                    @Override
                                    public void onNegativeButtonClick(AlertDialog alert) {

                                    }
                                },
                                true, false, false
                        ).show();
                        return;
                    }

                    String token = task.getResult();

                    if (!token.isEmpty()) {
                        Call<ServerResponse> call = endpoint.savetoken(nrp, wilayah, token);

                        call.enqueue(new Callback<ServerResponse>() {
                            @RequiresApi(api = Build.VERSION_CODES.P)
                            @Override
                            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    if (response.body().isStatus()) {
                                        alerts.dismiss();
                                        SharedPreferences sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("FCM_TOKEN", token);
                                        editor.putBoolean("login", true);
                                        editor.apply();

                                        Intent intent = new Intent(Login.this, Setlogin2.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        alerts.dismiss();
                                        CustomDialog.up(
                                                Login.this,
                                                "Informasi",
                                                "Gagal mendaftarkan Token FCM ke Server, silahkan Login Ulang",
                                                "ULANGI",
                                                "",
                                                new CustomDialog.AlertDialogListener() {
                                                    @Override
                                                    public void onPositiveButtonClick(AlertDialog alert) {
                                                        SharedPreferences sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.clear();
                                                        editor.apply();
                                                        alert.dismiss();
                                                    }

                                                    @Override
                                                    public void onNegativeButtonClick(AlertDialog alert) {

                                                    }
                                                },
                                                true, false, false
                                        ).show();
                                    }
                                } else {
                                    alerts.dismiss();
                                    CustomDialog.up(
                                            Login.this,
                                            "Informasi",
                                            "Gagal mendaftarkan Token FCM ke Server, silahkan Login Ulang",
                                            "ULANGI",
                                            "",
                                            new CustomDialog.AlertDialogListener() {
                                                @Override
                                                public void onPositiveButtonClick(AlertDialog alert) {
                                                    SharedPreferences sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.clear();
                                                    editor.apply();
                                                    alert.dismiss();
                                                }

                                                @Override
                                                public void onNegativeButtonClick(AlertDialog alert) {

                                                }
                                            },
                                            true, false, false
                                    ).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ServerResponse> call, Throwable t) {
                                alerts.dismiss();
                                CustomDialog.up(
                                        Login.this,
                                        "Informasi",
                                        "Gagal mendaftarkan Token FCM ke Server, periksa jaringan internet anda!",
                                        "ULANGI",
                                        "",
                                        new CustomDialog.AlertDialogListener() {
                                            @Override
                                            public void onPositiveButtonClick(AlertDialog alert) {
                                                SharedPreferences sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.clear();
                                                editor.apply();
                                                alert.dismiss();
                                            }

                                            @Override
                                            public void onNegativeButtonClick(AlertDialog alert) {

                                            }
                                        },
                                        true, false, false
                                ).show();
                            }
                        });
                    } else {
                        alerts.dismiss();
                        CustomDialog.up(
                                Login.this,
                                "Informasi",
                                "Gagal mengambil Token FCM, silahkan Login Ulang",
                                "ULANGI",
                                "",
                                new CustomDialog.AlertDialogListener() {
                                    @Override
                                    public void onPositiveButtonClick(AlertDialog alert) {
                                        SharedPreferences sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.apply();
                                        alert.dismiss();
                                    }

                                    @Override
                                    public void onNegativeButtonClick(AlertDialog alert) {

                                    }
                                },
                                true, false, false
                        ).show();
                    }
                });
    }
}