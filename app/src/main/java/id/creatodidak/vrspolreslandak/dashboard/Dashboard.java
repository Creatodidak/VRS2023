package id.creatodidak.vrspolreslandak.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.adapter.SliderAdapterExample;
import id.creatodidak.vrspolreslandak.admin.NotifikasiUpdate;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.AtensiResponse;
import id.creatodidak.vrspolreslandak.api.models.ServerResponse;
import id.creatodidak.vrspolreslandak.api.models.SliderItem;
import id.creatodidak.vrspolreslandak.api.models.chat.ResponseChat;
import id.creatodidak.vrspolreslandak.api.models.cuaca.DataCuaca;
import id.creatodidak.vrspolreslandak.api.models.cuaca.DatacuacaItem;
import id.creatodidak.vrspolreslandak.api.models.cuaca.TemperatureItem;
import id.creatodidak.vrspolreslandak.api.models.cuaca.WeatherItem;
import id.creatodidak.vrspolreslandak.auth.Login;
import id.creatodidak.vrspolreslandak.dashboard.harkamtibmas.Harkamtibmas;
import id.creatodidak.vrspolreslandak.dashboard.humas.Humas;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.DashboardKarhutla;
import id.creatodidak.vrspolreslandak.dashboard.lantas.Lantas;
import id.creatodidak.vrspolreslandak.dashboard.pedulistunting.DashboardPeduliStunting;
import id.creatodidak.vrspolreslandak.dashboard.pengamanan.Pengamanan;
import id.creatodidak.vrspolreslandak.dashboard.pimpinan.Pimpinan;
import id.creatodidak.vrspolreslandak.dashboard.situasi.Situasi;
import id.creatodidak.vrspolreslandak.dashboard.tahanan.Tahanan;
import id.creatodidak.vrspolreslandak.database.DBHelper;
import id.creatodidak.vrspolreslandak.helper.AtensiDialog;
import id.creatodidak.vrspolreslandak.helper.CustomDialog;
import id.creatodidak.vrspolreslandak.helper.DateUtils;
import id.creatodidak.vrspolreslandak.helper.MockDetector;
import id.creatodidak.vrspolreslandak.helper.RandomStringGenerator;
import id.creatodidak.vrspolreslandak.helper.TextHelper;
import id.creatodidak.vrspolreslandak.helper.WaktuLokal;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private static final int RC_ALL_PERMISSIONS = 123;
    @SuppressLint("InlinedApi")
    private final String[] allPermissions = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.USE_FINGERPRINT,
            Manifest.permission.USE_BIOMETRIC,
            Manifest.permission.VIBRATE,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.WAKE_LOCK,
            "com.google.android.c2dm.permission.RECEIVE",
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.ACCESS_NOTIFICATION_POLICY,
            Manifest.permission.WAKE_LOCK
    };
    ImageView ivprofile, iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9, iv11, iv12;
    Button btAtensi;
    SharedPreferences sharedPreferences;
    String nrp, satfung, pangkat, jabatan, nama, foto, satker;
    int click = 0;
    SliderAdapterExample adapter;
    SliderView sliderView;
    Endpoint endpoint;
    TextView wthTanggal;
    ImageView wthIcDH;
    ImageView wthIcP;
    ImageView wthIcS;
    ImageView wthIcM;
    TextView wthSuhuDH;
    TextView wthSuhuP;
    TextView wthSuhuS;
    TextView wthSuhuM;

    EditText etAtensiNew;
    Button btAtensiBatal;
    Button btAtensiPublish;
    CardView cvAtensi;

    ImageView fotoProfile;
    TextView namaProfile;
    TextView pangkatProfile;
    TextView jabatanProfile;
    TextView satkerProfile;
    TextView ubahProfile;
    File storageDir;
    String PHOTO_PATH;
    ActivityResultLauncher<Intent> opencamera, opengallery, updatedatadiri;

    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        endpoint = Client.getClient().create(Endpoint.class);
        sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        nrp = sharedPreferences.getString("nrp", null);
        satfung = sharedPreferences.getString("satfung", null);
        pangkat = sharedPreferences.getString("pangkat", null);
        nama = sharedPreferences.getString("nama", null);
        jabatan = sharedPreferences.getString("jabatan", null);
        foto = sharedPreferences.getString("foto", null);
        satker = sharedPreferences.getString("satker", null);
        storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "VRS/Profile");

        requestAllPermissions();
        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);
        iv3 = findViewById(R.id.iv3);
        iv4 = findViewById(R.id.iv4);
        iv5 = findViewById(R.id.iv5);
        iv6 = findViewById(R.id.iv6);
        iv7 = findViewById(R.id.iv7);
        iv8 = findViewById(R.id.iv8);
        iv9 = findViewById(R.id.iv9);
        iv11 = findViewById(R.id.iv11);
        iv12 = findViewById(R.id.iv12);
        btAtensi = findViewById(R.id.btAtensiBaru);
        etAtensiNew = findViewById(R.id.etAtensiNew);
        btAtensiBatal = findViewById(R.id.btAtensiBatal);
        btAtensiPublish = findViewById(R.id.btAtensiPublish);
        cvAtensi = findViewById(R.id.cvAtensi);
        cvAtensi.setVisibility(View.GONE);

        fotoProfile = findViewById(R.id.fotoProfile);
        namaProfile = findViewById(R.id.namaProfile);
        pangkatProfile = findViewById(R.id.pangkatProfile);
        jabatanProfile = findViewById(R.id.jabatanProfile);
        satkerProfile = findViewById(R.id.satkerProfile);
        ubahProfile = findViewById(R.id.ubahProfile);

        Glide.with(this)
                .load(foto)
                .placeholder(R.drawable.noprofilepic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.noprofilepic)
                .into(fotoProfile);

        namaProfile.setText(nama.toUpperCase());
        pangkatProfile.setText(pangkat+" / "+nrp);
        jabatanProfile.setText(TextHelper.capitalize(jabatan+" "+satfung));
        satkerProfile.setText(TextHelper.capitalize(satker));

        fotoProfile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                CustomDialog.up(
                        Dashboard.this,
                        "Konfirmasi",
                        "Pilih sumber gambar",
                        "KAMERA",
                        "GALERI",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog alert) {
                                alert.dismiss();
                                openCamera();
                            }

                            @Override
                            public void onNegativeButtonClick(AlertDialog alert) {
                                alert.dismiss();
                                openGallery();
                            }
                        },
                        true, true, false
                ).show();
                return true;
            }
        });

        updatedatadiri = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == RESULT_OK){
                this.recreate();
            }
        });

        ubahProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, Profile.class);
                updatedatadiri.launch(intent);
            }
        });

        wthTanggal = findViewById(R.id.wthTanggal);
        wthIcDH = findViewById(R.id.wthIcDH);
        wthIcP = findViewById(R.id.wthIcP);
        wthIcS = findViewById(R.id.wthIcS);
        wthIcM = findViewById(R.id.wthICM);
        wthSuhuDH = findViewById(R.id.wthSuhuDH);
        wthSuhuP = findViewById(R.id.wthSuhuP);
        wthSuhuS = findViewById(R.id.wthSuhuS);
        wthSuhuM = findViewById(R.id.wthSuhuM);
        loadCuaca();

        if (!nrp.equals("98070129")) {
            iv11.setVisibility(View.INVISIBLE);
            iv12.setVisibility(View.INVISIBLE);
            if (!jabatan.contains("KAPOLRES")) {
                btAtensi.setVisibility(View.GONE);
            }
        } else {
            iv11.setOnClickListener(this);
            iv12.setOnClickListener(this);
        }


        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
        iv4.setOnClickListener(this);
        iv5.setOnClickListener(this);
        iv6.setOnClickListener(this);
        iv7.setOnClickListener(this);
        iv8.setOnClickListener(this);
        iv9.setOnClickListener(this);

        btAtensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvAtensi.setVisibility(View.VISIBLE);
            }
        });

        btAtensiBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog.up(
                        Dashboard.this,
                        "Konfirmasi",
                        "Anda akan membatalkan atensi baru, Lanjutkan?",
                        "LANJUTKAN",
                        "BATAL",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog alert) {
                                alert.dismiss();
                                cvAtensi.setVisibility(View.GONE);
                                etAtensiNew.setText("");
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

        btAtensiPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog.up(
                        Dashboard.this,
                        "Konfirmasi",
                        "Kirim atensi ini?",
                        "KIRIM",
                        "BATAL",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog alert) {
                                alert.dismiss();
                                kirimAtensi(etAtensiNew.getText().toString());
                                cvAtensi.setVisibility(View.GONE);
                                etAtensiNew.setText("");
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

        sliderView = findViewById(R.id.imageSlider);


        List<SliderItem> ss = new ArrayList<>();

        SliderItem sliderItem = new SliderItem();
        sliderItem.setDescription("ok");
        sliderItem.setImageUrl("https://servervrs.polreslandak.id/ka1.jpg");
        sliderItem.setLocalimage(R.drawable.bg);
        ss.add(sliderItem);

        SliderItem sliderItem2 = new SliderItem();
        sliderItem2.setDescription("ok");
        sliderItem2.setImageUrl("https://servervrs.polreslandak.id/ka2.jpg");
        sliderItem2.setLocalimage(R.drawable.bg);
        ss.add(sliderItem2);

        adapter = new SliderAdapterExample(this, ss);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(10);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition());
            }
        });

        opencamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Uri sourceUri = Uri.fromFile(new File(PHOTO_PATH));
                Uri destinationUri = Uri.fromFile(new File(getCacheDir(), RandomStringGenerator.generateRandomString(10)+".jpg"));

                UCrop.of(sourceUri, destinationUri)
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(500, 500)
                        .start(this);
            }
        });

        opengallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri selectedImageUri = result.getData().getData();
                Uri destinationUri = Uri.fromFile(new File(getCacheDir(), RandomStringGenerator.generateRandomString(10)+".jpg"));

                if (selectedImageUri != null) {
                    UCrop.of(selectedImageUri, destinationUri)
                            .withAspectRatio(1, 1)
                            .withMaxResultSize(500, 500)
                            .start(this);
                }
            }
        });

        MockDetector mock = new MockDetector(this);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("CEK MOCK", "MOCK");
                mock.checkMockLocation();
                handler.postDelayed(this, 10000);
            }
        };
        handler.postDelayed(runnable, 10000);
    }

    @SuppressLint("IntentReset")
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        opengallery.launch(intent);
    }


    private void openCamera() {
        String imageFileName = RandomStringGenerator.generateRandomString(30);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File imageFile;
        try {
            imageFile = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PHOTO_PATH = imageFile.getAbsolutePath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, this.getPackageName() + ".provider", imageFile));
        opencamera.launch(intent);
    }

    private void loadCuaca() {
        String[] dates = DateUtils.getTodayFormatted().split(", ");
        wthTanggal.setText(dates[0] + ",\n" + dates[1]);
        Call<DataCuaca> call = endpoint.getCuaca();
        call.enqueue(new Callback<DataCuaca>() {
            @Override
            public void onResponse(Call<DataCuaca> call, Response<DataCuaca> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DatacuacaItem> list = response.body().getDatacuaca();
                    List<WeatherItem> weather = new ArrayList<>();
                    List<TemperatureItem> temp = new ArrayList<>();

                    for (DatacuacaItem item : list) {
                        if (item.getKabupaten().equals("Landak")) {
                            weather.addAll(item.getWeather());
                            temp.addAll(item.getTemperature());
                            break;
                        }
                    }

                    Log.d("CUACA", "SIZE: " + weather.size());
                    for (WeatherItem item : weather) {
                        if (item.getTanggal().contains(WaktuLokal.getTanggal())) {
                            Log.d("CUACAX", item.getCuaca());
                            if (item.getTanggal().contains("00:00")) {
                                if (item.getCuaca().contains("Cerah")) {
                                    wthIcDH.setImageResource(R.drawable.rcerah);
                                } else if (item.getCuaca().contains("Cerah Berawan")) {
                                    wthIcDH.setImageResource(R.drawable.rcerahberawan);
                                } else if (item.getCuaca().contains("Berawan")) {
                                    wthIcDH.setImageResource(R.drawable.rberawan);
                                } else if (item.getCuaca().contains("Berawan Tebal")) {
                                    wthIcDH.setImageResource(R.drawable.rberawantebal);
                                } else if (item.getCuaca().contains("Udara Kabur")) {
                                    wthIcDH.setImageResource(R.drawable.rudarakabur);
                                } else if (item.getCuaca().contains("Asap")) {
                                    wthIcDH.setImageResource(R.drawable.rberasap);
                                } else if (item.getCuaca().contains("Kabut")) {
                                    wthIcDH.setImageResource(R.drawable.rberkabut);
                                } else if (item.getCuaca().contains("Hujan Ringan")) {
                                    wthIcDH.setImageResource(R.drawable.rhujanringan);
                                } else if (item.getCuaca().contains("Hujan Sedang")) {
                                    wthIcDH.setImageResource(R.drawable.rhujansedang);
                                } else if (item.getCuaca().contains("Hujan Lebat")) {
                                    wthIcDH.setImageResource(R.drawable.rhujanlebat);
                                } else if (item.getCuaca().contains("Hujan Lokal")) {
                                    wthIcDH.setImageResource(R.drawable.rhujanlokal);
                                } else if (item.getCuaca().contains("Hujan Petir")) {
                                    wthIcDH.setImageResource(R.drawable.rhujanpetir);
                                } else {
                                    wthIcDH.setImageResource(R.drawable.rbelumtau);
                                }
                                Log.d("CUACA", item.getCuaca());
                            } else if (item.getTanggal().contains("06:00")) {
                                if (item.getCuaca().contains("Cerah")) {
                                    wthIcP.setImageResource(R.drawable.rcerah);
                                } else if (item.getCuaca().contains("Cerah Berawan")) {
                                    wthIcP.setImageResource(R.drawable.rcerahberawan);
                                } else if (item.getCuaca().contains("Berawan")) {
                                    wthIcP.setImageResource(R.drawable.rberawan);
                                } else if (item.getCuaca().contains("Berawan Tebal")) {
                                    wthIcP.setImageResource(R.drawable.rberawantebal);
                                } else if (item.getCuaca().contains("Udara Kabur")) {
                                    wthIcP.setImageResource(R.drawable.rudarakabur);
                                } else if (item.getCuaca().contains("Asap")) {
                                    wthIcP.setImageResource(R.drawable.rberasap);
                                } else if (item.getCuaca().contains("Kabut")) {
                                    wthIcP.setImageResource(R.drawable.rberkabut);
                                } else if (item.getCuaca().contains("Hujan Ringan")) {
                                    wthIcP.setImageResource(R.drawable.rhujanringan);
                                } else if (item.getCuaca().contains("Hujan Sedang")) {
                                    wthIcP.setImageResource(R.drawable.rhujansedang);
                                } else if (item.getCuaca().contains("Hujan Lebat")) {
                                    wthIcP.setImageResource(R.drawable.rhujanlebat);
                                } else if (item.getCuaca().contains("Hujan Lokal")) {
                                    wthIcP.setImageResource(R.drawable.rhujanlokal);
                                } else if (item.getCuaca().contains("Hujan Petir")) {
                                    wthIcP.setImageResource(R.drawable.rhujanpetir);
                                } else {
                                    wthIcP.setImageResource(R.drawable.rbelumtau);
                                }
                                Log.d("CUACA", item.getCuaca());
                            } else if (item.getTanggal().contains("12:00")) {
                                if (item.getCuaca().contains("Cerah")) {
                                    wthIcS.setImageResource(R.drawable.rcerah);
                                } else if (item.getCuaca().contains("Cerah Berawan")) {
                                    wthIcS.setImageResource(R.drawable.rcerahberawan);
                                } else if (item.getCuaca().contains("Berawan")) {
                                    wthIcS.setImageResource(R.drawable.rberawan);
                                } else if (item.getCuaca().contains("Berawan Tebal")) {
                                    wthIcS.setImageResource(R.drawable.rberawantebal);
                                } else if (item.getCuaca().contains("Udara Kabur")) {
                                    wthIcS.setImageResource(R.drawable.rudarakabur);
                                } else if (item.getCuaca().contains("Asap")) {
                                    wthIcS.setImageResource(R.drawable.rberasap);
                                } else if (item.getCuaca().contains("Kabut")) {
                                    wthIcS.setImageResource(R.drawable.rberkabut);
                                } else if (item.getCuaca().contains("Hujan Ringan")) {
                                    wthIcS.setImageResource(R.drawable.rhujanringan);
                                } else if (item.getCuaca().contains("Hujan Sedang")) {
                                    wthIcS.setImageResource(R.drawable.rhujansedang);
                                } else if (item.getCuaca().contains("Hujan Lebat")) {
                                    wthIcS.setImageResource(R.drawable.rhujanlebat);
                                } else if (item.getCuaca().contains("Hujan Lokal")) {
                                    wthIcS.setImageResource(R.drawable.rhujanlokal);
                                } else if (item.getCuaca().contains("Hujan Petir")) {
                                    wthIcS.setImageResource(R.drawable.rhujanpetir);
                                } else {
                                    wthIcS.setImageResource(R.drawable.rbelumtau);
                                }
                                Log.d("CUACA", item.getCuaca());
                            } else if (item.getTanggal().contains("18:00")) {
                                if (item.getCuaca().contains("Cerah")) {
                                    wthIcM.setImageResource(R.drawable.rcerah);
                                } else if (item.getCuaca().contains("Cerah Berawan")) {
                                    wthIcM.setImageResource(R.drawable.rcerahberawan);
                                } else if (item.getCuaca().contains("Berawan")) {
                                    wthIcM.setImageResource(R.drawable.rberawan);
                                } else if (item.getCuaca().contains("Berawan Tebal")) {
                                    wthIcM.setImageResource(R.drawable.rberawantebal);
                                } else if (item.getCuaca().contains("Udara Kabur")) {
                                    wthIcM.setImageResource(R.drawable.rudarakabur);
                                } else if (item.getCuaca().contains("Asap")) {
                                    wthIcM.setImageResource(R.drawable.rberasap);
                                } else if (item.getCuaca().contains("Kabut")) {
                                    wthIcM.setImageResource(R.drawable.rberkabut);
                                } else if (item.getCuaca().contains("Hujan Ringan")) {
                                    wthIcM.setImageResource(R.drawable.rhujanringan);
                                } else if (item.getCuaca().contains("Hujan Sedang")) {
                                    wthIcM.setImageResource(R.drawable.rhujansedang);
                                } else if (item.getCuaca().contains("Hujan Lebat")) {
                                    wthIcM.setImageResource(R.drawable.rhujanlebat);
                                } else if (item.getCuaca().contains("Hujan Lokal")) {
                                    wthIcM.setImageResource(R.drawable.rhujanlokal);
                                } else if (item.getCuaca().contains("Hujan Petir")) {
                                    wthIcM.setImageResource(R.drawable.rhujanpetir);
                                } else {
                                    wthIcM.setImageResource(R.drawable.rbelumtau);
                                }
                                Log.d("CUACA", item.getCuaca());
                            }
                        }
                    }

                    for (TemperatureItem item : temp) {
                        if (item.getTanggal().contains(WaktuLokal.getTanggal())) {
                            Log.d("CUACA", item.getTemp());
                            if (item.getTanggal().contains("00:00")) {
                                wthSuhuDH.setText(item.getTemp() + "°C");
                            } else if (item.getTanggal().contains("06:00")) {
                                wthSuhuP.setText(item.getTemp() + "°C");
                            } else if (item.getTanggal().contains("12:00")) {
                                wthSuhuS.setText(item.getTemp() + "°C");
                            } else if (item.getTanggal().contains("18:00")) {
                                wthSuhuM.setText(item.getTemp() + "°C");
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DataCuaca> call, Throwable t) {

            }
        });
    }

    private void kirimAtensi(String atensi) {
        AlertDialog alerts = CustomDialog.up(
                this,
                "Mengirimkan atensi...",
                "",
                "",
                "",
                new CustomDialog.AlertDialogListener() {
                    @Override
                    public void onPositiveButtonClick(AlertDialog alert) {

                    }

                    @Override
                    public void onNegativeButtonClick(AlertDialog alert) {

                    }
                }, false, false, true
        );

        alerts.show();

        Call<ServerResponse> call = endpoint.kirimAtensi(atensi, pangkat + " " + nama);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    alerts.dismiss();
                    CustomDialog.up(
                            Dashboard.this,
                            "Informasi",
                            "Atensi berhasil dikirim kepada seluruh personil Polres Landak!",
                            "LANJUTKAN",
                            "",
                            new CustomDialog.AlertDialogListener() {
                                @Override
                                public void onPositiveButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                }

                                @Override
                                public void onNegativeButtonClick(AlertDialog alert) {

                                }
                            }, true, false, false
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                alerts.dismiss();
                CustomDialog.up(
                        Dashboard.this,
                        "Informasi",
                        "Atensi gagal dikirim!",
                        "LANJUTKAN",
                        "",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog alert) {
                                alert.dismiss();
                            }

                            @Override
                            public void onNegativeButtonClick(AlertDialog alert) {

                            }
                        }, true, false, false
                ).show();
            }
        });
    }

    private void loadAtensi() {
        Call<AtensiResponse> call = endpoint.getAtensi();
        call.enqueue(new Callback<AtensiResponse>() {
            @Override
            public void onResponse(Call<AtensiResponse> call, Response<AtensiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SharedPreferences sh = getSharedPreferences("ATENSI", MODE_PRIVATE);

                    if (sh.getBoolean(response.body().getMsg(), true)) {
                        SharedPreferences.Editor ed = sh.edit();
                        ed.putBoolean(response.body().getMsg(), false);
                        ed.apply();
                        showAtensi(response.body().getSender(), response.body().getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<AtensiResponse> call, Throwable t) {
            }
        });
    }

    private void showAtensi(String sender, String msg) {
        AtensiDialog.up(
                this,
                "ATENSI PIMPINAN",
                sender,
                msg,
                "SIAP 86",
                "",
                new AtensiDialog.AlertDialogListener() {
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv1) {
            Intent intent = new Intent(Dashboard.this, Pimpinan.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv2) {
            Intent intent = new Intent(Dashboard.this, DashboardKarhutla.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv3) {
            Intent intent = new Intent(Dashboard.this, Situasi.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv4) {
            Intent intent = new Intent(Dashboard.this, Harkamtibmas.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv5) {
            Intent intent = new Intent(Dashboard.this, Humas.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv6) {
            Intent intent = new Intent(Dashboard.this, Lantas.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv7) {
            Intent intent = new Intent(Dashboard.this, Pengamanan.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv8) {
            Intent intent = new Intent(Dashboard.this, Tahanan.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv9) {
            Intent intent = new Intent(Dashboard.this, DashboardPeduliStunting.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv11) {
            Intent intent = new Intent(Dashboard.this, NotifikasiUpdate.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv12) {
            DBHelper dbHelper = new DBHelper(Dashboard.this);
            dbHelper.inisialisasi();

            CustomDialog.up(
                    Dashboard.this,
                    "KONFIRMASI",
                    "Yakin logout?",
                    "LOG OUT!",
                    "BATAL",
                    new CustomDialog.AlertDialogListener() {
                        @Override
                        public void onPositiveButtonClick(AlertDialog alert) {
                            if (dbHelper.resetDb()) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();

                                Intent intent = new Intent(Dashboard.this, Login.class);
                                startActivity(intent);
                                finish();
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (EasyPermissions.hasPermissions(this, allPermissions)) {
            loadAtensi();
        }
    }

    @AfterPermissionGranted(RC_ALL_PERMISSIONS)
    private void requestAllPermissions() {
        if (EasyPermissions.hasPermissions(this, allPermissions)) {

        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "App requires certain permissions to function properly.",
                    RC_ALL_PERMISSIONS,
                    allPermissions
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri croppedUri = UCrop.getOutput(data);
            updateFoto(croppedUri);

        } else if (resultCode == UCrop.RESULT_ERROR) {
            Throwable error = UCrop.getError(data);
        }
    }

    private void updateFoto(Uri croppedUri) {
        File img = new File(Objects.requireNonNull(croppedUri.getPath())).getAbsoluteFile();
        if (img.exists()) {
            AlertDialog alerts = CustomDialog.up(
                    this,
                    "Mengupload foto...",
                    "",
                    "",
                    "",
                    new CustomDialog.AlertDialogListener() {
                        @Override
                        public void onPositiveButtonClick(AlertDialog alert) {

                        }

                        @Override
                        public void onNegativeButtonClick(AlertDialog alert) {

                        }
                    },
                    false, false, true
            );
            alerts.show();

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), img);
            MultipartBody.Part image = MultipartBody.Part.createFormData("image", img.getName(), requestFile);

            Call<ResponseChat> call = endpoint.updateFotoProfile(image, nrp);
            call.enqueue(new Callback<ResponseChat>() {
                @Override
                public void onResponse(Call<ResponseChat> call, Response<ResponseChat> response) {
                    alerts.dismiss();
                    if(response.isSuccessful() && response.body() != null && response.body().isBerhasil()){
                        SharedPreferences.Editor ed = sharedPreferences.edit();
                        ed.putString("foto", response.body().getData());
                        ed.apply();
                        Glide.with(Dashboard.this)
                                .load(response.body().getData())
                                .error(R.drawable.noprofilepic)
                                .placeholder(R.drawable.noprofilepic)
                                .into(fotoProfile);

                        CustomDialog.up(
                                Dashboard.this,
                                "Informasi",
                                "Berhasil mengupload foto!",
                                "LANJUTKAN",
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
                    }else{
                        CustomDialog.up(
                                Dashboard.this,
                                "Informasi",
                                "Gagal mengupload foto!",
                                "MENGERTI",
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
                public void onFailure(Call<ResponseChat> call, Throwable t) {
                    alerts.dismiss();
                    CustomDialog.up(
                            Dashboard.this,
                            "Informasi",
                            "Gagal mengupload foto, periksa internet anda!",
                            "MENGERTI",
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
        }else{
            CustomDialog.up(
                    Dashboard.this,
                    "Informasi",
                    "Gagal mengupload foto!",
                    "MENGERTI",
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

}