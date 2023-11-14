package id.creatodidak.vrspolreslandak.dashboard.karhutla;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.adapter.karhutla.AllMarker;
import id.creatodidak.vrspolreslandak.adapter.karhutla.EmbungmarkerItem;
import id.creatodidak.vrspolreslandak.adapter.karhutla.HotspotmarkerItem;
import id.creatodidak.vrspolreslandak.adapter.karhutla.MakoItem;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.cuaca.DataCuaca;
import id.creatodidak.vrspolreslandak.api.models.cuaca.DatacuacaItem;
import id.creatodidak.vrspolreslandak.api.models.cuaca.HumidityItem;
import id.creatodidak.vrspolreslandak.api.models.cuaca.TemperatureItem;
import id.creatodidak.vrspolreslandak.api.models.cuaca.WeatherItem;
import id.creatodidak.vrspolreslandak.api.models.cuaca.WindSpeedItem;
import id.creatodidak.vrspolreslandak.api.models.karhutla.RekapItem;
import id.creatodidak.vrspolreslandak.api.models.karhutla.RekapKarhutla;
import id.creatodidak.vrspolreslandak.helper.BitmapUtils;
import id.creatodidak.vrspolreslandak.helper.CustomDialog;
import id.creatodidak.vrspolreslandak.helper.DateUtils;
import id.creatodidak.vrspolreslandak.helper.Hitungjarak;
import id.creatodidak.vrspolreslandak.helper.MockDetector;
import id.creatodidak.vrspolreslandak.helper.WaktuLokal;
import id.creatodidak.vrspolreslandak.service.FirebaseMsg;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetaHotspot extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationProviderClient;
    Endpoint endpoint;
    SharedPreferences sh;
    GoogleMap map;
    LatLng userlocation;
    int totaldone = 0;
    int totallow = 0;
    int totalmed = 0;
    int totalhigh = 0;
    int total = 0;
    String satker;
    boolean show = false;
    boolean isMapReady = false;
    LinearLayout rincianpers, rincian;
    ProgressBar pb;
    TextView tmKeterangan, tmNama, tmPangkat, tmSatker, tmSatfung, tmConfidence, tmLokasi, tmLatitude, tmLongitude, tmKecamatan, tmWilkum, tmJarak, tmTerdeteksi, tmSatelit, tmBelumDiresponse, haritanggal, gagal;
    Button btntutuptable;
    ImageView tmFoto, trigger, showtable;
    TableLayout tbRincian;
    MapView mMap;
    List<Marker> allembung = new ArrayList<>();
    List<Marker> allmako = new ArrayList<>();
    List<Marker> alllow = new ArrayList<>();
    List<Marker> allmed = new ArrayList<>();
    List<Marker> allhigh = new ArrayList<>();
    List<Marker> alllowd = new ArrayList<>();
    List<Marker> allmedd = new ArrayList<>();
    List<Marker> allhighd = new ArrayList<>();

    LinearLayout btAll, btLow, btHigh, btMedium, btDirespon;
    TableLayout tbKarhutla;
    CardView tabledata;
    private boolean isPortrait = true;
    boolean isSudah = false;
    CardView lapisan;
    ImageView keteranganTable;
    ConstraintLayout utama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta_hotspot);
        if(FirebaseMsg.BG != null && FirebaseMsg.BG.isPlaying()){
            FirebaseMsg.BG.stop();
            FirebaseMsg.vibrator.cancel();
        }
        endpoint = Client.getClient().create(Endpoint.class);
        sh = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        satker = sh.getString("satker", null);

        utama = findViewById(R.id.layoututama);
        tmKeterangan = findViewById(R.id.tmKeterangan);
        rincianpers = findViewById(R.id.rincianpers);
        tmFoto = findViewById(R.id.tmFoto);
        tmNama = findViewById(R.id.tmNama);
        tmPangkat = findViewById(R.id.tmPangkat);
        tmSatker = findViewById(R.id.tmSatker);
        tmSatfung = findViewById(R.id.tmSatfung);
        tbRincian = findViewById(R.id.tbRincian);
        tmConfidence = findViewById(R.id.tmConfidence);
        tmLokasi = findViewById(R.id.tmLokasi);
        tmLatitude = findViewById(R.id.tmLatitude);
        tmLongitude = findViewById(R.id.tmLongitude);
        tmKecamatan = findViewById(R.id.tmKecamatan);
        tmWilkum = findViewById(R.id.tmWilkum);
        tmJarak = findViewById(R.id.tmJarak);
        tmTerdeteksi = findViewById(R.id.tmTerdeteksi);
        tmSatelit = findViewById(R.id.tmSatelit);
        tmBelumDiresponse = findViewById(R.id.tmKeterangan2);
        pb = findViewById(R.id.progressBar10);
        btAll = findViewById(R.id.btAll);
        btHigh = findViewById(R.id.btHigh);
        btMedium = findViewById(R.id.btMedium);
        btLow = findViewById(R.id.btLow);
        btDirespon = findViewById(R.id.btDirespon);
        lapisan = findViewById(R.id.lapisan);
        keteranganTable = findViewById(R.id.imageView20);

        rincianpers = findViewById(R.id.rincianpers);
        tmFoto = findViewById(R.id.tmFoto);
        tmNama = findViewById(R.id.tmNama);
        tmPangkat = findViewById(R.id.tmPangkat);
        tmSatker = findViewById(R.id.tmSatker);
        tmSatfung = findViewById(R.id.tmSatfung);
        tbRincian = findViewById(R.id.tbRincian);
        tmConfidence = findViewById(R.id.tmConfidence);
        tmLokasi = findViewById(R.id.tmLokasi);
        tmLatitude = findViewById(R.id.tmLatitude);
        tmLongitude = findViewById(R.id.tmLongitude);
        tmKecamatan = findViewById(R.id.tmKecamatan);
        tmWilkum = findViewById(R.id.tmWilkum);
        tmJarak = findViewById(R.id.tmJarak);
        tmTerdeteksi = findViewById(R.id.tmTerdeteksi);
        tbKarhutla = findViewById(R.id.tableKarhutla);
        showtable = findViewById(R.id.imageView26);
        tabledata = findViewById(R.id.tabledata);
        haritanggal = findViewById(R.id.haritanggal);
        gagal = findViewById(R.id.gagal);
        btntutuptable = findViewById(R.id.btnTutupTable);
        haritanggal.setText(DateUtils.getTodayFormatted());

        showtable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btntutuptable.setVisibility(View.VISIBLE);
                tabledata.setVisibility(View.VISIBLE);
                getTablerow();
                UiSettings ui = map.getUiSettings();
                ui.setScrollGesturesEnabled(false);
                ui.setZoomControlsEnabled(false);
                lapisan.setVisibility(View.VISIBLE);
            }
        });

        btntutuptable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btntutuptable.setVisibility(View.GONE);
                tabledata.setVisibility(View.GONE);
                tbKarhutla.setVisibility(View.GONE);
                UiSettings ui = map.getUiSettings();
                ui.setScrollGesturesEnabled(true);
                ui.setZoomControlsEnabled(true);
                lapisan.setVisibility(View.GONE);
                keteranganTable.setVisibility(View.GONE);
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        rincian = findViewById(R.id.rincian);
        trigger = findViewById(R.id.imageView30);

        mMap = findViewById(R.id.petahotspot);
        mMap.onCreate(savedInstanceState);
        mMap.getMapAsync(this);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            boolean gpsEnabled = isGpsEnabled();
            if (gpsEnabled) {
                loadlocation();
            } else {
                CustomDialog.up(
                        this,
                        "Informasi",
                        "Untuk mengakses data ini, anda wajib menghidupkan layanan lokasi (GPS)",
                        "HIDUPKAN",
                        "TOLAK",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog alert) {

                            }

                            @Override
                            public void onNegativeButtonClick(AlertDialog alert) {
                                alert.dismiss();

                            }
                        },
                        true, true, false
                ).show();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        loadcuaca();
    }

    private void getTablerow() {
        pb.setVisibility(View.VISIBLE);
        Call<RekapKarhutla> call = endpoint.getRekapKarhutlaNew();
        call.enqueue(new Callback<RekapKarhutla>() {
            @Override
            public void onResponse(Call<RekapKarhutla> call, Response<RekapKarhutla> response) {
                pb.setVisibility(View.GONE);
                int nomor = 0;
                int val4 = 0;
                int val5 = 0;
                int val6 = 0;
                int val7 = 0;
                int val8 = 0;
                int val9 = 0;
                if (response.body() != null && response.isSuccessful()) {
                    tbKarhutla.setVisibility(View.VISIBLE);
                    gagal.setVisibility(View.GONE);
                    List<RekapItem> list = response.body().getRekap();
                    if(!isSudah) {
                        for (RekapItem item : list) {
                            TableRow newRow = (TableRow) LayoutInflater.from(PetaHotspot.this).inflate(R.layout.tablelayoutitem, null);
                            nomor++;
                            TextView COL1 = newRow.findViewById(R.id.cellNo);
                            TextView COL2 = newRow.findViewById(R.id.cellKecamatan);
                            TextView COL3 = newRow.findViewById(R.id.cellPolsek);
                            TextView COL4 = newRow.findViewById(R.id.cellL);
                            TextView COL5 = newRow.findViewById(R.id.cellM);
                            TextView COL6 = newRow.findViewById(R.id.cellH);
                            TextView COL7 = newRow.findViewById(R.id.cellTotal);
                            TextView COL8 = newRow.findViewById(R.id.cellB);
                            TextView COL9 = newRow.findViewById(R.id.cellS);
                            COL1.setText(String.valueOf(nomor));
                            COL2.setText(item.getKecamatan());
                            COL3.setText(item.getWilkum());
                            COL4.setText(String.valueOf(item.getLow()));
                            COL5.setText(String.valueOf(item.getMedium()));
                            COL6.setText(String.valueOf(item.getHigh()));
                            COL7.setText(String.valueOf(item.getTotal()));
                            COL8.setText(String.valueOf(item.getDiresponse()));
                            COL9.setText(String.valueOf(item.getBdiresponse()));
                            val4 = val4 + item.getLow();
                            val5 = val5 + item.getMedium();
                            val6 = val6 + item.getHigh();
                            val7 = val7 + item.getTotal();
                            val8 = val8 + item.getDiresponse();
                            val9 = val9 + item.getBdiresponse();
                            tbKarhutla.addView(newRow);
                        }

                        TableRow newRow = (TableRow) LayoutInflater.from(PetaHotspot.this).inflate(R.layout.tablelayoutitemend, null);
                        TextView COL4 = newRow.findViewById(R.id.cellL);
                        TextView COL5 = newRow.findViewById(R.id.cellM);
                        TextView COL6 = newRow.findViewById(R.id.cellH);
                        TextView COL7 = newRow.findViewById(R.id.cellTotal);
                        TextView COL8 = newRow.findViewById(R.id.cellB);
                        TextView COL9 = newRow.findViewById(R.id.cellS);
                        COL4.setText(String.valueOf(val4));
                        COL5.setText(String.valueOf(val5));
                        COL6.setText(String.valueOf(val6));
                        COL7.setText(String.valueOf(val7));
                        COL8.setText(String.valueOf(val8));
                        COL9.setText(String.valueOf(val9));

                        tbKarhutla.addView(newRow);
                    }
                    isSudah = true;
                    tabledata.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            CustomDialog.up(
                                    PetaHotspot.this,
                                    "Konfirmasi",
                                    "Bagikan data ini?",
                                    "BAGIKAN",
                                    "BATAL",
                                    new CustomDialog.AlertDialogListener() {
                                        @Override
                                        public void onPositiveButtonClick(AlertDialog alert) {
                                            alert.dismiss();
                                            sharedata(tabledata);
                                        }

                                        @Override
                                        public void onNegativeButtonClick(AlertDialog alert) {
                                            alert.dismiss();
                                        }
                                    },
                                    true, true, false
                            ).show();
                            return true;
                        }
                    });
                    tbKarhutla.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            CustomDialog.up(
                                    PetaHotspot.this,
                                    "Konfirmasi",
                                    "Bagikan data ini?",
                                    "BAGIKAN",
                                    "BATAL",
                                    new CustomDialog.AlertDialogListener() {
                                        @Override
                                        public void onPositiveButtonClick(AlertDialog alert) {
                                            alert.dismiss();
                                            sharedata(tabledata);
                                        }

                                        @Override
                                        public void onNegativeButtonClick(AlertDialog alert) {
                                            alert.dismiss();
                                        }
                                    },
                                    true, true, false
                            ).show();
                            return true;
                        }
                    });
                    keteranganTable.setVisibility(View.VISIBLE);
                }else{
                    pb.setVisibility(View.GONE);
                    gagal.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<RekapKarhutla> call, Throwable t) {
                pb.setVisibility(View.GONE);
                gagal.setVisibility(View.VISIBLE);
            }
        });

    }

    private void sharedata(View view) {
        AlertDialog alerts = CustomDialog.up(
                this,
                "memproses data...",
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
        ); alerts.show();

        Bitmap datanya = getBitmapFromView(view);

        if(datanya != null){
            File file = savetocache(datanya);
            if(file != null){
                alerts.dismiss();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpeg"); // Tipe konten adalah gambar JPEG
                Uri imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                startActivity(Intent.createChooser(shareIntent, "Bagikan gambar melalui..."));
            }else {
                alerts.dismiss();
            }
        }else{
            alerts.dismiss();
        }

    }

    private File savetocache(Bitmap datanya) {
        String tempFilePath = getExternalCacheDir() + File.separator + "datakarhutla.jpg";
        File tempFile = new File(tempFilePath);

        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            datanya.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Mengembalikan null jika terjadi kesalahan
        }
    }


    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void loadcuaca() {
        TextView tvhum = findViewById(R.id.tvHumidity);
        TextView tvspeed = findViewById(R.id.tvWindspeed);
        TextView tvtemp = findViewById(R.id.tvSuhu);
        ImageView icCuaca = findViewById(R.id.icCuaca);

        Call<DataCuaca> call = endpoint.getCuaca();
        call.enqueue(new Callback<DataCuaca>() {
            @Override
            public void onResponse(Call<DataCuaca> call, Response<DataCuaca> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        List<DatacuacaItem> list = response.body().getDatacuaca();
                        List<HumidityItem> hums = new ArrayList<>();
                        List<WeatherItem> weather = new ArrayList<>();
                        List<TemperatureItem> temp = new ArrayList<>();
                        List<WindSpeedItem> wind = new ArrayList<>();

                        Log.d("CUACA:", "size ALL: " + list.size());
                        SharedPreferences preferences = getSharedPreferences("CACHE_CUACA", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        for (DatacuacaItem item : list) {
                            if (item.getKabupaten().equals("Landak")) {
                                hums.addAll(item.getHumidity());
                                weather.addAll(item.getWeather());
                                temp.addAll(item.getTemperature());
                                wind.addAll(item.getWindSpeed());
                                break;
                            }
                        }

                        Log.d("CUACA:", "size HUM: " + hums.size());
                        Log.d("CUACA:", "size WEA: " + weather.size());
                        Log.d("CUACA:", "size TMP: " + temp.size());
                        Log.d("CUACA:", "size WIN: " + wind.size());

                        for (TemperatureItem item : temp) {
                            if (item.getTanggal().equals(WaktuLokal.customTanggal())) {
                                tvtemp.setText(item.getTemp() + "°C");
                                editor.putString("TEMPERATUR", item.getTemp() + "°C");
                                break;
                            }
                        }

                        for (HumidityItem item : hums) {
                            if (item.getTanggal().equals(WaktuLokal.customTanggal())) {
                                tvhum.setText(item.getHum() + "%");
                                editor.putString("HUMIDITY", item.getHum() + "%");
                                break;
                            }
                        }

                        for (WindSpeedItem item : wind) {
                            if (item.getTanggal().equals(WaktuLokal.customTanggal())) {
                                String kecepatan = new DecimalFormat("#.##").format(Double.parseDouble(item.getKecepatanangin()) * 1.852);
                                tvspeed.setText(kecepatan + "Km/H");
                                editor.putString("KECEPATAN", kecepatan + "Km/H");
                                break;
                            }
                        }

                        for (WeatherItem item : weather) {
                            Log.d("CUACA:", "item size: " + weather.size());

                            if (item.getTanggal().equals(WaktuLokal.customTanggal())) {
                                Log.d("CUACA:", "Cuaca: " + item.getCuaca());
                                Log.d("CUACA:", "Kode: " + item.getKode());
                                editor.putString("CUACA", item.getCuaca());
                                if (item.getCuaca().contains("Cerah")) {
                                    icCuaca.setImageResource(R.drawable.cerah);
                                    editor.putInt("ICON", R.drawable.cerah);
                                } else if (item.getCuaca().contains("Cerah Berawan")) {
                                    icCuaca.setImageResource(R.drawable.cerahberawan);
                                    editor.putInt("ICON", R.drawable.cerahberawan);
                                } else if (item.getCuaca().contains("Berawan")) {
                                    icCuaca.setImageResource(R.drawable.berawan);
                                    editor.putInt("ICON", R.drawable.berawan);
                                } else if (item.getCuaca().contains("Berawan Tebal")) {
                                    icCuaca.setImageResource(R.drawable.berawantebal);
                                    editor.putInt("ICON", R.drawable.berawantebal);
                                } else if (item.getCuaca().contains("Udara Kabur")) {
                                    icCuaca.setImageResource(R.drawable.udarakabur);
                                    editor.putInt("ICON", R.drawable.udarakabur);
                                } else if (item.getCuaca().contains("Asap")) {
                                    icCuaca.setImageResource(R.drawable.berasap);
                                    editor.putInt("ICON", R.drawable.berasap);
                                } else if (item.getCuaca().contains("Kabut")) {
                                    icCuaca.setImageResource(R.drawable.berkabut);
                                    editor.putInt("ICON", R.drawable.berkabut);
                                } else if (item.getCuaca().contains("Hujan Ringan")) {
                                    icCuaca.setImageResource(R.drawable.hujanringan);
                                    editor.putInt("ICON", R.drawable.hujanringan);
                                } else if (item.getCuaca().contains("Hujan Sedang")) {
                                    icCuaca.setImageResource(R.drawable.hujansedang);
                                    editor.putInt("ICON", R.drawable.hujansedang);
                                } else if (item.getCuaca().contains("Hujan Lebat")) {
                                    icCuaca.setImageResource(R.drawable.hujanlebat);
                                    editor.putInt("ICON", R.drawable.hujanlebat);
                                } else if (item.getCuaca().contains("Hujan Lokal")) {
                                    icCuaca.setImageResource(R.drawable.hujanlokal);
                                    editor.putInt("ICON", R.drawable.hujanlokal);
                                } else if (item.getCuaca().contains("Hujan Petir")) {
                                    icCuaca.setImageResource(R.drawable.hujanpetir);
                                    editor.putInt("ICON", R.drawable.hujanpetir);
                                }
                                break;
                            }
                        }

                        editor.apply();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataCuaca> call, Throwable t) {

            }
        });
    }

    private void loadlocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                userlocation = new LatLng(location.getLatitude(), location.getLongitude());
                loadmarker(location);
            });
        }

    }

    private void loadmarker(Location location) {
        Call<AllMarker> calls = endpoint.getAllMarker();
        calls.enqueue(new Callback<AllMarker>() {
            @Override
            public void onResponse(Call<AllMarker> call, Response<AllMarker> response) {
                if (response.body() != null) {
                    List<EmbungmarkerItem> embungmarker = response.body().getEmbungmarker();
                    List<MakoItem> makomarker = response.body().getMako();
                    List<HotspotmarkerItem> hotspotmarker = response.body().getHotspotmarker();

                    if (embungmarker.size() != 0) {
                        for (EmbungmarkerItem item : embungmarker) {
                            Bundle bundle = new Bundle();
                            bundle.putString("kode", item.getKode());
                            bundle.putString("nama", item.getNama());
                            bundle.putString("type", item.getType());
                            bundle.putString("kecamatan", item.getKecamatan());
                            bundle.putString("kapasitas", item.getKapasitas());
                            bundle.putString("foto", item.getFoto());
                            bundle.putDouble("latitude", (Double) item.getLatitude());
                            bundle.putDouble("longitude", (Double) item.getLongitude());
                            Bitmap customMarkerBitmap = BitmapUtils.getBitmapFromVectorDrawable(PetaHotspot.this, R.drawable.markerembung);
                            Bitmap resizedBitmap = Bitmap.createScaledBitmap(customMarkerBitmap, 50, 50, false);
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(new LatLng((Double) item.getLatitude(), (Double) item.getLongitude()))
                                    .title(item.getNama())
                                    .anchor(0.5f, 0.5f)
                                    .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));
                            Marker marker = map.addMarker(markerOptions);
                            marker.setTag(bundle);
                            allembung.add(marker);
                        }
                    }
                    if (makomarker.size() != 0) {
                        for (MakoItem item : makomarker) {
                            Bitmap customMarkerBitmap = BitmapUtils.getBitmapFromVectorDrawable(PetaHotspot.this, R.drawable.markermako);
                            Bitmap resizedBitmap = Bitmap.createScaledBitmap(customMarkerBitmap, 70, 70, false);
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(new LatLng((Double) item.getLatitude(), (Double) item.getLongitude()))
                                    .title(item.getSatker())
                                    .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));
                            Marker marker = map.addMarker(markerOptions);
                            allmako.add(marker);
                        }
                    }

                    TextView cf9TextView = findViewById(R.id.cf9m);
                    TextView cf7TextView = findViewById(R.id.cf7m);
                    TextView cf8TextView = findViewById(R.id.cf8m);
                    TextView cfdoneTextView = findViewById(R.id.cfOk);
                    TextView totals = findViewById(R.id.cftotal);

                    for (HotspotmarkerItem item : hotspotmarker) {
                        total++;
                        double calculatedDistance = Hitungjarak.calculateDistance(
                                (Double) item.getLaths(),
                                (Double) item.getLonghs(),
                                location.getLatitude(),
                                location.getLongitude()
                        );

                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        String formattedDistance = decimalFormat.format(calculatedDistance);


                        Bundle bundle = new Bundle();
                        bundle.putString("geocoder", item.getLokasi());
                        bundle.putString("longitudes", String.valueOf(item.getLonghs()));
                        bundle.putString("latitudes", String.valueOf(item.getLaths()));
                        bundle.putString("keterangan", item.getResponder());
                        bundle.putString("foto", item.getFotopers());
                        bundle.putString("nama", item.getNama());
                        bundle.putString("pangkat", item.getPangkat());
                        bundle.putString("satker", item.getSatkerpers());
                        bundle.putString("satfung", item.getSatfungpers());
                        bundle.putString("kecamatan", item.getKecamatan());
                        bundle.putString("polsek", item.getSatker());
                        bundle.putString("satelit", item.getSatelit());
                        bundle.putString("terdeteksi", DateUtils.tanggaldaricreatedat(item.getCreatedAt()));
                        bundle.putString("jarak", "±" + formattedDistance + " KM dari posisi anda sekarang");
                        bundle.putString("nrp", item.getNrp());

                        String confidence = item.getConfidence();
                        if (!item.getResponder().equals("BELUM ADA")) {
                            totaldone++;
                            Bitmap customMarkerBitmap = BitmapUtils.getBitmapFromVectorDrawable(PetaHotspot.this, R.drawable.markerok);
                            Bitmap resizedBitmap = Bitmap.createScaledBitmap(customMarkerBitmap, 85, 85, false);
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(new LatLng((Double) item.getLaths(), (Double) item.getLonghs()))
                                    .title(null)
                                    .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));
                            Marker marker = map.addMarker(markerOptions);
                            if (confidence.equals("LOW")) {
                                totallow++;
                                bundle.putString("confidence", "LOW");
                                alllowd.add(marker);
                            } else if (confidence.equals("MEDIUM")) {
                                totalmed++;
                                bundle.putString("confidence", "MEDIUM");
                                allmedd.add(marker);
                            } else if (confidence.equals("HIGH")) {
                                totalhigh++;
                                bundle.putString("confidence", "HIGH");
                                allhighd.add(marker);
                            }
                            marker.setTag(bundle);
                        } else {
                            if (confidence.equals("LOW")) {
                                totallow++;
                                bundle.putString("confidence", "LOW");
                                Bitmap customMarkerBitmap = BitmapUtils.getBitmapFromVectorDrawable(PetaHotspot.this, R.drawable.markergreen);
                                Bitmap resizedBitmap = Bitmap.createScaledBitmap(customMarkerBitmap, 85, 85, false);
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(new LatLng((Double) item.getLaths(), (Double) item.getLonghs()))
                                        .title(null)
                                        .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));
                                Marker marker = map.addMarker(markerOptions);
                                marker.setTag(bundle);
                                alllow.add(marker);
                            } else if (confidence.equals("MEDIUM")) {
                                totalmed++;
                                bundle.putString("confidence", "MEDIUM");
                                Bitmap customMarkerBitmap = BitmapUtils.getBitmapFromVectorDrawable(PetaHotspot.this, R.drawable.markeryellow);
                                Bitmap resizedBitmap = Bitmap.createScaledBitmap(customMarkerBitmap, 85, 85, false);
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(new LatLng((Double) item.getLaths(), (Double) item.getLonghs()))
                                        .title(null)
                                        .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));
                                Marker marker = map.addMarker(markerOptions);
                                marker.setTag(bundle);
                                allmed.add(marker);
                            } else if (confidence.equals("HIGH")) {
                                totalhigh++;
                                bundle.putString("confidence", "HIGH");
                                Bitmap customMarkerBitmap = BitmapUtils.getBitmapFromVectorDrawable(PetaHotspot.this, R.drawable.markerred);
                                Bitmap resizedBitmap = Bitmap.createScaledBitmap(customMarkerBitmap, 85, 85, false);
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(new LatLng((Double) item.getLaths(), (Double) item.getLonghs()))
                                        .title(null)
                                        .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));
                                Marker marker = map.addMarker(markerOptions);
                                marker.setTag(bundle);
                                allhigh.add(marker);
                            }
                        }
                    }

                    cf7TextView.setText(String.valueOf(totallow));
                    cf8TextView.setText(String.valueOf(totalmed));
                    cf9TextView.setText(String.valueOf(totalhigh));
                    totals.setText(String.valueOf(total));
                    cfdoneTextView.setText(String.valueOf(totaldone));

                    btLow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!alllow.isEmpty()) {
                                for (Marker item : alllow) {
                                    item.setVisible(true);
                                }
                            }
                            if (!alllowd.isEmpty()) {
                                for (Marker item : alllowd) {
                                    item.setVisible(true);
                                }
                            }
                            if (!allmed.isEmpty()) {
                                for (Marker item : allmed) {
                                    item.setVisible(false);
                                }
                            }
                            if (!allmedd.isEmpty()) {
                                for (Marker item : allmedd) {
                                    item.setVisible(false);
                                }
                            }
                            if (!allhigh.isEmpty()) {
                                for (Marker item : allhigh) {
                                    item.setVisible(false);
                                }
                            }
                            if (!allhighd.isEmpty()) {
                                for (Marker item : allhighd) {
                                    item.setVisible(false);
                                }
                            }
                        }
                    });

                    btMedium.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!alllow.isEmpty()) {
                                for (Marker item : alllow) {
                                    item.setVisible(false);
                                }
                            }
                            if (!alllowd.isEmpty()) {
                                for (Marker item : alllowd) {
                                    item.setVisible(false);
                                }
                            }
                            if (!allmed.isEmpty()) {
                                for (Marker item : allmed) {
                                    item.setVisible(true);
                                }
                            }
                            if (!allmedd.isEmpty()) {
                                for (Marker item : allmedd) {
                                    item.setVisible(true);
                                }
                            }
                            if (!allhigh.isEmpty()) {
                                for (Marker item : allhigh) {
                                    item.setVisible(false);
                                }
                            }
                            if (!allhighd.isEmpty()) {
                                for (Marker item : allhighd) {
                                    item.setVisible(false);
                                }
                            }
                        }
                    });
                    btHigh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!alllow.isEmpty()) {
                                for (Marker item : alllow) {
                                    item.setVisible(false);
                                }
                            }
                            if (!alllowd.isEmpty()) {
                                for (Marker item : alllowd) {
                                    item.setVisible(false);
                                }
                            }
                            if (!allmed.isEmpty()) {
                                for (Marker item : allmed) {
                                    item.setVisible(false);
                                }
                            }
                            if (!allmedd.isEmpty()) {
                                for (Marker item : allmedd) {
                                    item.setVisible(false);
                                }
                            }
                            if (!allhigh.isEmpty()) {
                                for (Marker item : allhigh) {
                                    item.setVisible(true);
                                }
                            }
                            if (!allhighd.isEmpty()) {
                                for (Marker item : allhighd) {
                                    item.setVisible(true);
                                }
                            }
                        }
                    });
                    btDirespon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!alllow.isEmpty()) {
                                for (Marker item : alllow) {
                                    item.setVisible(false);
                                }
                            }
                            if (!alllowd.isEmpty()) {
                                for (Marker item : alllowd) {
                                    item.setVisible(true);
                                }
                            }
                            if (!allmed.isEmpty()) {
                                for (Marker item : allmed) {
                                    item.setVisible(false);
                                }
                            }
                            if (!allmedd.isEmpty()) {
                                for (Marker item : allmedd) {
                                    item.setVisible(true);
                                }
                            }
                            if (!allhigh.isEmpty()) {
                                for (Marker item : allhigh) {
                                    item.setVisible(false);
                                }
                            }
                            if (!allhighd.isEmpty()) {
                                for (Marker item : allhighd) {
                                    item.setVisible(true);
                                }
                            }
                        }
                    });
                    btAll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!alllow.isEmpty()) {
                                for (Marker item : alllow) {
                                    item.setVisible(true);
                                }
                            }
                            if (!alllowd.isEmpty()) {
                                for (Marker item : alllowd) {
                                    item.setVisible(true);
                                }
                            }
                            if (!allmed.isEmpty()) {
                                for (Marker item : allmed) {
                                    item.setVisible(true);
                                }
                            }
                            if (!allmedd.isEmpty()) {
                                for (Marker item : allmedd) {
                                    item.setVisible(true);
                                }
                            }
                            if (!allhigh.isEmpty()) {
                                for (Marker item : allhigh) {
                                    item.setVisible(true);
                                }
                            }
                            if (!allhighd.isEmpty()) {
                                for (Marker item : allhighd) {
                                    item.setVisible(true);
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AllMarker> call, Throwable t) {

            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        isMapReady = true;
        this.map = map;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setOnMarkerClickListener(this);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.setMinZoomPreference(7);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(false);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.getUiSettings().setCompassEnabled(true);
            map.getUiSettings().setTiltGesturesEnabled(false);
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-0.025275670920232873, 110.64676240835874), 7));

        map.setPadding(0, dpToPx(100), 0, dpToPx(100));
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        if (marker.getTitle() == null) {
            LatLng markerPosition = marker.getPosition();
            float zoomLevel = 17.0f;
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, zoomLevel));
            UiSettings ui = map.getUiSettings();
            ui.setScrollGesturesEnabled(false);
            ui.setZoomControlsEnabled(false);
            ui.setZoomGesturesEnabled(false);
            Circle circle = null;
            rincian.setVisibility(View.VISIBLE);
            Object tag = marker.getTag();

            if (tag instanceof Bundle) {
                Bundle bundle = (Bundle) tag;
                String confidence = bundle.getString("confidence");
                String satelit = bundle.getString("satelit");
                String nrp = bundle.getString("nrp");
                String geocoder = bundle.getString("geocoder");
                String keterangan = bundle.getString("keterangan");
                String kecamatan = bundle.getString("kecamatan");
                String polsek = bundle.getString("polsek");
                String terdeteksi = bundle.getString("terdeteksi");
                String jarak = bundle.getString("jarak");
                String latitudes = bundle.getString("latitudes");
                String longitudes = bundle.getString("longitudes");

                if(confidence.equals("LOW")){
                    CircleOptions circleOptions = new CircleOptions()
                            .center(markerPosition)
                            .radius(100)
                            .strokeWidth(10)
                            .clickable(false)
                            .fillColor(Color.argb(80, 0, 255, 48))
                            .strokeColor(Color.argb(100, 0, 255, 48));

                    circle = map.addCircle(circleOptions);
                }else if(confidence.equals("MEDIUM")){
                    CircleOptions circleOptions = new CircleOptions()
                            .center(markerPosition)
                            .radius(100)
                            .strokeWidth(10)
                            .clickable(false)
                            .fillColor(Color.argb(80, 241, 185, 3))
                            .strokeColor(Color.argb(100, 241, 185, 3));

                    circle = map.addCircle(circleOptions);
                }else if(confidence.equals("HIGH")){
                    CircleOptions circleOptions = new CircleOptions()
                            .center(markerPosition)
                            .radius(100)
                            .strokeWidth(10)
                            .clickable(false)
                            .fillColor(Color.argb(80, 206, 12, 12))
                            .strokeColor(Color.argb(100, 206, 12, 12));

                    circle = map.addCircle(circleOptions);
                }

                if (keterangan.equals("BELUM ADA")) {
                    tmBelumDiresponse.setVisibility(View.VISIBLE);
                    tmKeterangan.setVisibility(View.GONE);
                    rincianpers.setVisibility(View.GONE);
                } else {
                    tmBelumDiresponse.setVisibility(View.GONE);
                    rincianpers.setVisibility(View.VISIBLE);
                    tmKeterangan.setVisibility(View.VISIBLE);
                    String foto = bundle.getString("foto").replace("Https", "https");
                    String nama = bundle.getString("nama");
                    String pangkat = bundle.getString("pangkat");
                    String satkers = bundle.getString("satker");
                    String satfung = bundle.getString("satfung");
                    Glide.with(PetaHotspot.this)
                            .load("https://servervrs.polreslandak.id" + foto)
                            .error(R.drawable.unknown)
                            .into(tmFoto);
                    tmNama.setText(nama);
                    tmPangkat.setText(pangkat + "/" + nrp);
                    tmSatker.setText(satkers);
                    tmSatfung.setText(satfung);
                }
                tmSatelit.setText(satelit);
                tmConfidence.setText(confidence);
                tmLokasi.setText(geocoder);
                tmKecamatan.setText(kecamatan);
                tmWilkum.setText(polsek);
                tmJarak.setText(jarak);
                tmTerdeteksi.setText(terdeteksi);
                tmLongitude.setText(longitudes);
                tmLatitude.setText(latitudes);

                Circle finalCircle = circle;
                trigger.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        float zoomLevel = 8.0f; // Ubah level zoom sesuai kebutuhan Anda
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, zoomLevel));
                        rincian.setVisibility(View.GONE);
                        ui.setScrollGesturesEnabled(true);
                        ui.setZoomControlsEnabled(true);
                        ui.setZoomGesturesEnabled(true);
                        finalCircle.remove();
                    }
                });
            }
        }else{
            marker.showInfoWindow();
        }
        return true;
    }

    private boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    protected void onResume() {
        super.onResume();
        mMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMap.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMap.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMap.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMap.onLowMemory();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void toggleOrientation() {
        if (isPortrait) {

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        isPortrait = !isPortrait;
    }
}
