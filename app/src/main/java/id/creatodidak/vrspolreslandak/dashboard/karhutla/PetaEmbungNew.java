package id.creatodidak.vrspolreslandak.dashboard.karhutla;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.adapter.EmbungSearchAdapter;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.DataEmbung;
import id.creatodidak.vrspolreslandak.api.models.EmbungItem;
import id.creatodidak.vrspolreslandak.api.models.ServerREmbung;
import id.creatodidak.vrspolreslandak.api.models.cuaca.DataCuaca;
import id.creatodidak.vrspolreslandak.api.models.cuaca.DatacuacaItem;
import id.creatodidak.vrspolreslandak.api.models.cuaca.HumidityItem;
import id.creatodidak.vrspolreslandak.api.models.cuaca.TemperatureItem;
import id.creatodidak.vrspolreslandak.api.models.cuaca.WeatherItem;
import id.creatodidak.vrspolreslandak.api.models.cuaca.WindSpeedItem;
import id.creatodidak.vrspolreslandak.database.DBHelper;
import id.creatodidak.vrspolreslandak.helper.BitmapUtils;
import id.creatodidak.vrspolreslandak.helper.CustomDialog;
import id.creatodidak.vrspolreslandak.helper.CustomMaps;
import id.creatodidak.vrspolreslandak.helper.Hitungjarak;
import id.creatodidak.vrspolreslandak.helper.RandomStringGenerator;
import id.creatodidak.vrspolreslandak.helper.WaktuLokal;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetaEmbungNew extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    Endpoint endpoint;
    TextView tvhum, tvtemp, tvspeed;
    ImageView icCuaca;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location myloc;
    MapView mapView;
    GoogleMap map;
    Circle userCir;
    Circle selectedMarker;
    Marker userMarker;
    List<Marker> embungMarker = new ArrayList<>();
    DBHelper dbHelper;
    Animator slideIn, slideOut;
    ImageView logo;
    CardView trigger, searchbar;
    EditText etSearchTrigger, etNamaEmbung, etKapasitas;
    List<EmbungItem> listEmbung = new ArrayList<>();
    List<EmbungItem> hasilpencarian = new ArrayList<>();
    EmbungSearchAdapter adapter;
    RecyclerView rv;
    LinearLayout lyTrigger, tambahdata;
    ConstraintLayout layerpilihlokasi;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    String LOKASI_FILE = "VRS/KARHUTLA/EMBUNG";
    String LABEL_FILE = "EMBUNG_AIR_";
    private String currentPhotoPath;
    ConstraintSet constraintSet;
    Button btnPilih, btKirimData, btnPilihCancel;
    CardView formTambahdata;
    Spinner spJenisEmbung, spKecamatan;
    List<String> listkecamatan = Arrays.asList("PILIH KECAMATAN", "NGABANG", "MEMPAWAH HULU", "MENJALIN", "MANDOR", "AIR BESAR", "MENYUKE", "SENGAH TEMILA", "MERANTI", "KUALA BEHE", "SEBANGKI", "JELIMPO", "BANYUKE HULU", "SOMPAK");
    List<String> jenisembung = Arrays.asList("PILIH JENIS EMBUNG AIR", "EMBUNG ALAMI", "EMBUNG BUATAN");
    ImageView btcloseform;
    ConstraintLayout fotos;
    ImageView wm1, fotonya;
    Button btnFoto;
    String HASIL_NAMA = "";
    Uri contentUri;
    TextView latlongPilihLokasi;
    String HUMIDITY = "";
    String TEMPERATUR = "";
    String KECEPATAN = "";
    String CUACA = "";
    String nrp;

    boolean isMapReady = false;
    private final Handler handler = new Handler();
    private final Runnable getmyloc = new Runnable() {
        @Override
        public void run() {

            if (ActivityCompat.checkSelfPermission(PetaEmbungNew.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PetaEmbungNew.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    myloc = location;

                    if(userCir != null){userCir.remove();}
                    if(userMarker != null){userMarker.remove();}

                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

                    CircleOptions circleOptions = new CircleOptions()
                            .center(loc)
                            .radius(100)
                            .strokeWidth(10)
                            .clickable(false)
                            .strokeColor(Color.argb(100, 255, 0, 0));

                    userCir = map.addCircle(circleOptions);

                    Bitmap customMarkerBitmap = BitmapUtils.getBitmapFromVectorDrawable(PetaEmbungNew.this, R.drawable.usermarker);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(customMarkerBitmap, 90, 90, false);

                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(loc)
                            .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap))
                            .anchor(0.5f, 0.5f);

                    userMarker = map.addMarker(markerOptions);

                    handler.postDelayed(getmyloc, 5000);
                }
            });
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta_embung_new);
        endpoint = Client.getClient().create(Endpoint.class);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        dbHelper = new DBHelper(this);
        dbHelper.inisialisasi();
        constraintSet = new ConstraintSet();

        tvhum = findViewById(R.id.tvHumidity);
        tvspeed = findViewById(R.id.tvWindspeed);
        tvtemp = findViewById(R.id.tvSuhu);
        icCuaca = findViewById(R.id.icCuaca);
        mapView = findViewById(R.id.petaEmbungNew);
        slideIn = AnimatorInflater.loadAnimator(PetaEmbungNew.this, R.animator.slideinfromtop);
        slideIn.setTarget(searchbar);
        slideOut = AnimatorInflater.loadAnimator(PetaEmbungNew.this, R.animator.slideouttotop);
        slideOut.setTarget(searchbar);
        searchbar = findViewById(R.id.searchbar);
        logo = findViewById(R.id.imageView11);
        etSearchTrigger = findViewById(R.id.triggerSearch);
        rv = findViewById(R.id.rv);
        tambahdata = findViewById(R.id.wrappernewdata);
        layerpilihlokasi = findViewById(R.id.layerPilihLokasi);
        btnPilih = findViewById(R.id.btnPilihLokasi);
        btnPilihCancel = findViewById(R.id.btnPilihLokasiCancel);
        btKirimData = findViewById(R.id.btKirimData);
        formTambahdata = findViewById(R.id.formTambahdata);
        etNamaEmbung = findViewById(R.id.etNamaEmbung);
        etKapasitas = findViewById(R.id.etKapasitas);
        spJenisEmbung = findViewById(R.id.spJenisEmbung);
        spKecamatan = findViewById(R.id.spKecamatan);
        btcloseform = findViewById(R.id.btcloseform);
        fotos = findViewById(R.id.fotoPenanganan);
        fotonya = findViewById(R.id.fotonya);
        wm1 = findViewById(R.id.wm1);
        btnFoto = findViewById(R.id.btnFoto);
        latlongPilihLokasi = findViewById(R.id.latlongPilihLokasi);
        SharedPreferences sh = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        nrp = sh.getString("nrp", "00000000");

        SharedPreferences sharedPreferences = getSharedPreferences("CACHE_CUACA", MODE_PRIVATE);
        CUACA = sharedPreferences.getString("CUACA", null);
        KECEPATAN = sharedPreferences.getString("KECEPATAN", null);
        TEMPERATUR = sharedPreferences.getString("TEMPERATUR", null);
        HUMIDITY = sharedPreferences.getString("HUMIDITY", null);

        ArrayAdapter<String> adpkecamatan = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listkecamatan);
        adpkecamatan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKecamatan.setAdapter(adpkecamatan);

        ArrayAdapter<String> adpjenis = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jenisembung);
        adpjenis.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spJenisEmbung.setAdapter(adpjenis);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        etSearchTrigger.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (searchbar.getVisibility() == View.GONE) {
                    opensearchbar();
                }
                return false;
            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchbar.getVisibility() == View.GONE) {
                    opensearchbar();
                } else {
                    logo.setImageResource(R.drawable.embung);
                    searchbar.setVisibility(View.GONE);
                    slideOut.start();
                    etSearchTrigger.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    etSearchTrigger.clearFocus();
                }
            }
        });

        adapter = new EmbungSearchAdapter(this, hasilpencarian, new EmbungSearchAdapter.OnItemClickListener() {
            @Override
            public void onClick(EmbungItem embungItem, int position) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng((Double) embungItem.getLatitude(), (Double) embungItem.getLongitude()), 18));
                    etSearchTrigger.setHint(embungItem.getNama());
                    etSearchTrigger.setText(embungItem.getNama());
                    logo.setImageResource(R.drawable.embung);
                    searchbar.setVisibility(View.GONE);
                    slideOut.start();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSearchTrigger.getWindowToken(), 0);
                    etSearchTrigger.clearFocus();

                    for(Marker mark : embungMarker){
                        if(mark.getPosition().equals(new LatLng((Double) embungItem.getLatitude(), (Double) embungItem.getLongitude()))){
                            if(selectedMarker != null){
                                selectedMarker.remove();
                            }
                            CircleOptions circleOptions = new CircleOptions()
                                    .center(mark.getPosition())
                                    .radius(10)
                                    .strokeWidth(5)
                                    .clickable(false)
                                    .fillColor(Color.argb(100, 255, 241, 0))
                                    .strokeColor(Color.argb(100, 255, 241, 0));


                            selectedMarker = map.addCircle(circleOptions);
                        }
                    }
            }
        });

        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);

        tambahdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layerpilihlokasi.setVisibility(View.VISIBLE);
                logo.setImageResource(R.drawable.embung);
                searchbar.setVisibility(View.GONE);
                slideOut.start();
                etSearchTrigger.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                etSearchTrigger.clearFocus();
            }
        });
        btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.up(
                        PetaEmbungNew.this,
                        "Konfirmasi",
                        "Pilih lokasi ini sebagai embung air baru?",
                        "PILIH",
                        "BATAL",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog alert) {
                                alert.dismiss();
                                LatLng newLatLng = map.getCameraPosition().target;
                                double latitude = newLatLng.latitude;
                                double longitude = newLatLng.longitude;
                                double jarak = Hitungjarak.calculateDistancemeters(latitude, longitude, myloc.getLatitude(), myloc.getLongitude());
                                if (jarak <= 100) {
                                    LatLng center = map.getCameraPosition().target;
                                    bukaformtambah(center);
                                }
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

    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        handler.post(getmyloc);
        this.map = map;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setOnMarkerClickListener(this);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.setMinZoomPreference(7);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(false);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setTiltGesturesEnabled(false);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-0.025275670920232873, 110.64676240835874), 7));

        if (ActivityCompat.checkSelfPermission(PetaEmbungNew.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PetaEmbungNew.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
                map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {
                        LatLng newLatLng = map.getCameraPosition().target;
                        double latitudes = newLatLng.latitude;
                        double longitudes = newLatLng.longitude;

                        if (Hitungjarak.calculateDistancemeters(latitudes, longitudes, location.getLatitude(), location.getLongitude()) > 100) {
                            latlongPilihLokasi.setText("DILUAR BATAS RADIUS 100M!");
                            btnPilih.setEnabled(false);
                            btnPilih.setText("---");
                        } else {
                            btnPilih.setEnabled(true);
                            btnPilih.setText("PILIH TITIK INI");
                            String coordinates = "Coord: " + latitudes + ", " + longitudes + "\n Jarak ± " + new DecimalFormat("#.##").format(Hitungjarak.calculateDistancemeters(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude, location.getLatitude(), location.getLongitude())) + "M";
                            latlongPilihLokasi.setText(coordinates);
                        }
                    }
                });
            }
        });
        loadEmbungFromServer();
        loadcuaca();
    }

    private void loadEmbungFromServer() {
        List<EmbungItem> server = new ArrayList<>();

        Call<DataEmbung> call = endpoint.getAllEmbung();
        call.enqueue(new Callback<DataEmbung>() {
            @Override
            public void onResponse(Call<DataEmbung> call, Response<DataEmbung> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<EmbungItem> serverdata = response.body().getEmbung();
                    for(EmbungItem item : serverdata){
                        if(!dbHelper.isEmbung(item.getKode())){
                            dbHelper.addEmbungs(item);
                            server.add(item);
                        }
                    }
                    loadEmbungFromLocal();
                }
            }

            @Override
            public void onFailure(Call<DataEmbung> call, Throwable t) {
                loadEmbungFromLocal();
            }
        });

    }

    @SuppressLint("Range")
    private void loadEmbungFromLocal() {
        Cursor cursor = dbHelper.getAllEmbung();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Bundle bundle = new Bundle();
                bundle.putString("type", cursor.getString(cursor.getColumnIndex("type")));
                bundle.putString("nama", cursor.getString(cursor.getColumnIndex("nama")));
                bundle.putString("kode", cursor.getString(cursor.getColumnIndex("kode")));
                bundle.putString("kecamatan", cursor.getString(cursor.getColumnIndex("kecamatan")));
                bundle.putString("kapasitas", cursor.getString(cursor.getColumnIndex("kapasitas")));
                bundle.putString("foto", cursor.getString(cursor.getColumnIndex("foto")));
                bundle.putDouble("latitude", cursor.getDouble(cursor.getColumnIndex("latitude")));
                bundle.putDouble("longitude", cursor.getDouble(cursor.getColumnIndex("longitude")));
                if (cursor.getString(cursor.getColumnIndex("local")).equals("yes")) {
                    Bitmap customMarkerBitmap = BitmapUtils.getBitmapFromVectorDrawable(PetaEmbungNew.this, R.drawable.markerembunglocal);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(customMarkerBitmap, 90, 90, false);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(new LatLng(cursor.getDouble(cursor.getColumnIndex("latitude")), cursor.getDouble(cursor.getColumnIndex("longitude"))))
                            .title(null)
                            .anchor(0.5f, 0.5f)
                            .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));
                    Marker marker = map.addMarker(markerOptions);
                    marker.setTag(bundle);
                    embungMarker.add(marker);
                } else {
                    Bitmap customMarkerBitmap = BitmapUtils.getBitmapFromVectorDrawable(PetaEmbungNew.this, R.drawable.markerembung);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(customMarkerBitmap, 90, 90, false);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(new LatLng(cursor.getDouble(cursor.getColumnIndex("latitude")), cursor.getDouble(cursor.getColumnIndex("longitude"))))
                            .title(null)
                            .anchor(0.5f, 0.5f)
                            .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));
                    Marker marker = map.addMarker(markerOptions);
                    marker.setTag(bundle);
                    embungMarker.add(marker);
                }

                EmbungItem embung = new EmbungItem();
                embung.setId(cursor.getInt(cursor.getColumnIndex("id")));
                embung.setType(cursor.getString(cursor.getColumnIndex("type")).toUpperCase());
                embung.setNama(cursor.getString(cursor.getColumnIndex("nama")).toUpperCase());
                embung.setKode(cursor.getString(cursor.getColumnIndex("kode")).toUpperCase());
                embung.setKecamatan(cursor.getString(cursor.getColumnIndex("kecamatan")).toUpperCase());
                embung.setKapasitas(cursor.getString(cursor.getColumnIndex("kapasitas")).toUpperCase());
                embung.setFoto(cursor.getString(cursor.getColumnIndex("foto")));
                embung.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                embung.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                listEmbung.add(embung);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void loadcuaca() {
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
                SharedPreferences sh = getSharedPreferences("CACHE_CUACA", MODE_PRIVATE);
                int ICON = sh.getInt("ICON", 0);

                tvtemp.setText(TEMPERATUR);
                tvhum.setText(HUMIDITY);
                tvspeed.setText(KECEPATAN);
                icCuaca.setImageResource(ICON);

            }
        });
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Object tag = marker.getTag();

        if (tag instanceof Bundle) {
            Bundle bundle = (Bundle) tag;
            String NAMA = bundle.getString("nama");
            String KODE = bundle.getString("kode");
            String TYPE = bundle.getString("type");
            String KECAMATAN = bundle.getString("kecamatan");
            String KAPASITAS = bundle.getString("kapasitas");
            String FOTO = bundle.getString("foto");
            double LATITUDE = bundle.getDouble("latitude");
            double LONGITUDE = bundle.getDouble("longitude");
            LatLng markerPosition = marker.getPosition();
            float zoomLevel = 18.0f;
            LatLng myLatlng = new LatLng(myloc.getLatitude(), myloc.getLongitude());
            String jarak = new DecimalFormat("#.##").format(Hitungjarak.calculateDistancemeters(LATITUDE, LONGITUDE, myLatlng.latitude, myLatlng.longitude));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, zoomLevel));

            if(selectedMarker != null){
                selectedMarker.remove();
            }
            CircleOptions circleOptions = new CircleOptions()
                    .center(marker.getPosition())
                    .radius(10)
                    .strokeWidth(5)
                    .clickable(false)
                    .zIndex(marker.getZIndex()+1)
                    .fillColor(Color.argb(100, 255, 241, 0))
                    .strokeColor(Color.argb(100, 255, 241, 0));

            selectedMarker = map.addCircle(circleOptions);

            CustomMaps.up(
                    this,
                    TYPE,
                    NAMA,
                    "±" + jarak + " Meter dari posisi anda saat ini...",
                    KECAMATAN,
                    markerPosition.latitude,
                    markerPosition.longitude,
                    KAPASITAS + "m³",
                    FOTO,
                    new CustomMaps.AlertDialogListener() {
                        @Override
                        public void onLaporan(AlertDialog alert) {
                            alert.dismiss();
                            CustomDialog.up(
                                    PetaEmbungNew.this,
                                    "Konfirmasi",
                                    "Pilih embung air ini sebagai ojek laporan?",
                                    "YA",
                                    "BATAL",
                                    new CustomDialog.AlertDialogListener() {
                                        @Override
                                        public void onPositiveButtonClick(AlertDialog alert) {
                                            alert.dismiss();
                                            String cuaca = "Pada saat laporan dibuat, menurut data BMKG kondisi cuaca di sekitar koordinat adalah:\n- Cuaca :"+CUACA+"\n- Suhu: "+TEMPERATUR+"\n- Humidity: "+HUMIDITY+"\n- Kecepatan angin: "+KECEPATAN;
                                            long newid = RandomStringGenerator.randomNumber(10);
                                            newLaporan(KODE, cuaca, newid);
                                        }

                                        @Override
                                        public void onNegativeButtonClick(AlertDialog alert) {
                                            alert.dismiss();
                                        }
                                    },
                                    true, true, false
                            ).show();
                        }

                        @Override
                        public void onMaps(AlertDialog alert) {
                            String uri = "http://maps.google.com/maps?q=loc:" + LATITUDE + "," + LONGITUDE;
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            intent.setPackage("com.google.android.apps.maps");

                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            } else {
                                Toast.makeText(PetaEmbungNew.this, "Google Maps tidak terpasang", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onExpand(AlertDialog alert) {
                            expandImage(FOTO, NAMA);
                        }

                        @Override
                        public void onClose(AlertDialog alert) {
                            alert.dismiss();
                        }
                    }
            ).show();
        }

        return true;
    }

    private void newLaporan(String kode, String cuaca, long id) {
        AlertDialog alerts = CustomDialog.up(
                PetaEmbungNew.this,
                "Memproses...",
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


        if(dbHelper.registerChatid(id, "LaporanCekEmbung")){
            dbHelper.newSingleChatData(String.valueOf(id), "bot", "SERVER", "Silahkan masukan detil laporan anda sesuai dengan instruksi saya...\nPerlu diingat bahwa laporan ini tidak akan masuk dalam Database Server jika anda belum menyelesaikan dan mengirim laporan ini kepada Verifikator!\n\nmari mulai proses pelaporan");
            String alamatgeo = "UNKNOWN";
            Geocoder geocoder = new Geocoder(PetaEmbungNew.this, new Locale("id"));
            try {
                List<Address> addresses = geocoder.getFromLocation(myloc.getLatitude(), myloc.getLongitude(), 1);
                if(addresses.size() != 0){
                    alamatgeo = addresses.get(0).getAddressLine(0);
                }else{
                    alamatgeo = "BELUM ADA";
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            dbHelper.addDataCekEmbung(String.valueOf(id), kode, myloc.getLatitude(), myloc.getLongitude(), alamatgeo, "BELUM ADA", "BELUM ADA", "BELUM ADA", cuaca, "BELUM ADA", "BELUM ADA", "BELUM ADA", nrp, "BELUM ADA", "yes", WaktuLokal.get(), WaktuLokal.get());
            alerts.dismiss();
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    private void opensearchbar() {
        logo.setImageResource(R.drawable.baseline_arrow_back_24);
        searchbar.setVisibility(View.VISIBLE);
        slideIn.start();
        loadsearch(myloc);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadsearch(Location myloc) {
        if(myloc != null) {
            double lat = myloc.getLatitude();
            double longs = myloc.getLongitude();
            hasilpencarian.clear();
            adapter.notifyDataSetChanged();

            etSearchTrigger.setHint("Cari Lokasi Embung Air Disini");
            for (EmbungItem item : listEmbung) {
                double jarak = Hitungjarak.calculateDistancemeters((Double) item.getLatitude(), (Double) item.getLongitude(), lat, longs);
                EmbungItem data = new EmbungItem();
                data.setNama("(" + item.getType() + ") " + item.getNama());
                if (jarak <= 100) {
                    data.setLatitude(item.getLatitude());
                    data.setLongitude(item.getLongitude());
                    data.setKeterangan("Buat laporan cek embung");
                } else {
                    data.setKeterangan("Jarak diatas 100M (Not Available)");
                }
                hasilpencarian.add(data);
            }
            adapter.notifyDataSetChanged();

            etSearchTrigger.addTextChangedListener(new TextWatcher() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    hasilpencarian.clear();
                    String searchText = charSequence.toString().trim().toUpperCase();
                    if (!searchText.isEmpty()) {
                        for (EmbungItem item : listEmbung) {
                            if (item.getKecamatan().contains(searchText) || item.getNama().contains(searchText)) {
                                EmbungItem data = new EmbungItem();
                                double jarak = Hitungjarak.calculateDistancemeters((Double) item.getLatitude(), (Double) item.getLongitude(), lat, longs);

                                data.setNama("(" + item.getType() + ") " + item.getNama());
                                if (jarak <= 100) {
                                    data.setLatitude(item.getLatitude());
                                    data.setLongitude(item.getLongitude());
                                    data.setKeterangan("Buat laporan cek embung");
                                } else {
                                    data.setKeterangan("Jarak diatas 100M (Not Available)");
                                }
                                hasilpencarian.add(data);
                            }
                        }

                        if (hasilpencarian.size() == 0) {
                            EmbungItem data = new EmbungItem();
                            data.setNama("DATA TIDAK DITEMUKAN");
                            data.setKeterangan("Silahkan masukan keyword lain!");
                            data.setType("NEW");
                            hasilpencarian.add(data);
                        }
                    } else {
                        List<EmbungItem> embungdata = new ArrayList<>();

                        for (EmbungItem item : listEmbung) {
                            EmbungItem data = new EmbungItem();
                            double jarak = Hitungjarak.calculateDistancemeters((Double) item.getLatitude(), (Double) item.getLongitude(), lat, longs);
                            data.setNama("(" + item.getType() + ") " + item.getNama());
                            if (jarak <= 100) {
                                data.setLatitude(item.getLatitude());
                                data.setLongitude(item.getLongitude());
                                data.setKeterangan("Buat laporan cek embung");
                            } else {
                                data.setKeterangan("Jarak diatas 100M (Not Available)");
                            }
                        }

                        hasilpencarian.addAll(embungdata);
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    private void bukaformtambah(LatLng center) {
        map.getUiSettings().setAllGesturesEnabled(false);
        btnPilih.setVisibility(View.GONE);
        btnPilihCancel.setVisibility(View.GONE);
        formTambahdata.setVisibility(View.VISIBLE);
        btKirimData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(etNamaEmbung.getText()) &&
                        !TextUtils.isEmpty(etKapasitas.getText()) &&
                        !spJenisEmbung.getSelectedItem().toString().equals("PILIH JENIS EMBUNG AIR") &&
                        !spKecamatan.getSelectedItem().toString().equals("PILIH KECAMATAN")
                ) {
                    CustomDialog.up(
                            PetaEmbungNew.this,
                            "Konfirmasi",
                            "Kirim data ini?",
                            "KIRIM",
                            "BATAL",
                            new CustomDialog.AlertDialogListener() {
                                @Override
                                public void onPositiveButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                    addembung(center);
                                }

                                @Override
                                public void onNegativeButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                }
                            },
                            true, true, false
                    ).show();
                } else {
                    CustomDialog.up(
                            PetaEmbungNew.this,
                            "Peringatan",
                            "Data harus diisi / dipilih semua sebelum dikirim!",
                            "PERBAIKI",
                            "",
                            new CustomDialog.AlertDialogListener() {
                                @Override
                                public void onPositiveButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                }

                                @Override
                                public void onNegativeButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                }
                            },
                            true, false, false
                    ).show();
                }
            }
        });

        btcloseform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog.up(
                        PetaEmbungNew.this,
                        "Konfirmasi",
                        "Jika anda batal menginput data maka akan hilang, lanjutkan?",
                        "LANJUTKAN",
                        "BATAL",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog alert) {
                                alert.dismiss();
                                map.getUiSettings().setAllGesturesEnabled(true);
                                btnPilih.setVisibility(View.VISIBLE);
                                layerpilihlokasi.setVisibility(View.GONE);
                                formTambahdata.setVisibility(View.GONE);
                                ConstraintSet constraintSets = new ConstraintSet();
                                String ratio = String.format(Locale.getDefault(), "%d:%d", 0,0);
                                constraintSets.clone(fotos);
                                constraintSets.setDimensionRatio(R.id.fotonya, ratio);
                                constraintSets.applyTo(fotos);
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

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchCameraIntent();
            }
        });
    }

    private void dispatchCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(PetaEmbungNew.this.getPackageManager()) != null) {
            try {
                File photoFile = createImageFile(PetaEmbungNew.this);
                if (photoFile != null) {
                    Uri photoUri = FileProvider.getUriForFile(PetaEmbungNew.this,
                            PetaEmbungNew.this.getPackageName() + ".provider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = LABEL_FILE + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), LOKASI_FILE);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Glide.with(this)
                    .load(currentPhotoPath)
                    .into(fotonya);

            wm1.setVisibility(View.VISIBLE);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(currentPhotoPath, options);
            int width = options.outWidth;
            int height = options.outHeight;

            int fpb = findGreatestCommonDivisor(width, height);
            int simplifiedWidth;
            int simplifiedHeight;

            if (fpb != 0) {
                simplifiedWidth = width / fpb;
                simplifiedHeight = height / fpb;
            } else {
                simplifiedWidth = 3;
                simplifiedHeight = 4;
            }

            String ratio = String.format(Locale.getDefault(), "%d:%d", simplifiedWidth, simplifiedHeight);

            constraintSet.clone(fotos);
            constraintSet.setDimensionRatio(R.id.fotonya, ratio);
            constraintSet.applyTo(fotos);

            btnFoto.setText("GANTI FOTO");
        }
    }

    private void addembung(LatLng center) {
        AlertDialog alerts = CustomDialog.up(
                PetaEmbungNew.this,
                "Mengirim data",
                "",
                "",
                "",
                new CustomDialog.AlertDialogListener() {
                    @Override
                    public void onPositiveButtonClick(AlertDialog alert) {
                    }

                    @Override
                    public void onNegativeButtonClick(AlertDialog alert) {
                        alert.dismiss();
                    }
                },
                false, false, true
        );

        alerts.show();
        String nama = etNamaEmbung.getText().toString();
        double latitude = center.latitude;
        double longitude = center.longitude;
        String type = spJenisEmbung.getSelectedItem().toString();
        String kapasitas = etKapasitas.getText().toString();
        String kecamatan = spKecamatan.getSelectedItem().toString();
        String kode = RandomStringGenerator.generateRandomString(20);


        Bitmap bitmap = getBitmapFromView(fotos);
        saveBitmapToGallery(bitmap);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image", "image.jpg", imageRequestBody);

        Call<ServerREmbung> call = endpoint.addembung(kode, nama, latitude, longitude, type, kapasitas, kecamatan, image);
        call.enqueue(new Callback<ServerREmbung>() {
            @Override
            public void onResponse(Call<ServerREmbung> call, Response<ServerREmbung> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        alerts.dismiss();
                        CustomDialog.up(
                                PetaEmbungNew.this,
                                "Informasi",
                                response.body().getMsg(),
                                "OK",
                                "",
                                new CustomDialog.AlertDialogListener() {
                                    @Override
                                    public void onPositiveButtonClick(AlertDialog alert) {
                                        dbHelper.addEmbung(nama, latitude, longitude, type, kapasitas, kecamatan, kode, "no", response.body().getFoto());
                                        loadEmbungFromServer();
                                        alert.dismiss();
                                        clearformembung();
                                    }

                                    @Override
                                    public void onNegativeButtonClick(AlertDialog alert) {
                                        alert.dismiss();
                                    }
                                },
                                true, false, false
                        ).show();
                    } else {
                        addtodraft();
                    }
                } else {
                    addtodraft();
                }
            }

            @Override
            public void onFailure(Call<ServerREmbung> call, Throwable t) {
                addtodraft();
            }

            private void addtodraft() {
                alerts.dismiss();
                if (dbHelper.addEmbung(nama, latitude, longitude, type, kapasitas, kecamatan, kode, "yes", HASIL_NAMA)) {
                    CustomDialog.up(
                            PetaEmbungNew.this,
                            "Informasi",
                            "Data gagal dikirim, namun tersimpan di draft dan akan otomatis terkirim setelah koneksi internet lebih baik!",
                            "MENGERTI",
                            "",
                            new CustomDialog.AlertDialogListener() {
                                @Override
                                public void onPositiveButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                    clearformembung();
                                    loadEmbungFromServer();
                                }

                                @Override
                                public void onNegativeButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                }
                            },
                            true, false, false
                    ).show();
                }
            }
        });
    }

    private void clearformembung() {
        Location loc = new Location("update");
        loc.setLatitude(myloc.getLatitude());
        loc.setLongitude(myloc.getLongitude());
        layerpilihlokasi.setVisibility(View.GONE);
        map.getUiSettings().setAllGesturesEnabled(true);
        formTambahdata.setVisibility(View.GONE);
        etNamaEmbung.setText("");
        spJenisEmbung.setSelection(0);
        etKapasitas.setText("");
        spKecamatan.setSelection(0);
        wm1.setVisibility(View.GONE);
        ConstraintSet constraintSets = new ConstraintSet();
        String ratio = String.format(Locale.getDefault(), "%d:%d", 0,0);
        constraintSets.clone(fotos);
        constraintSets.setDimensionRatio(R.id.fotonya, ratio);
        constraintSets.applyTo(fotos);
        btnFoto.setText("AMBIL FOTO");
        btnPilih.setVisibility(View.VISIBLE);
        btnPilihCancel.setVisibility(View.VISIBLE);
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void saveBitmapToGallery(Bitmap bitmap) {
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), LOKASI_FILE);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = LABEL_FILE + timeStamp + ".jpg";
        File imageFile = new File(storageDir, imageFileName);

        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            contentUri = Uri.fromFile(imageFile);
            mediaScanIntent.setData(contentUri);
            HASIL_NAMA = String.valueOf(contentUri);
            PetaEmbungNew.this.sendBroadcast(mediaScanIntent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void expandImage(String imageUrl, String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PetaEmbungNew.this, R.style.myFullscreenAlertDialogStyle);
        LayoutInflater inflater = LayoutInflater.from(PetaEmbungNew.this);
        View view = inflater.inflate(R.layout.dialog_image_zoom, null);

        PhotoView zoomableImageView = view.findViewById(R.id.zoomableImageView);
        TextView judul = view.findViewById(R.id.textView51);
        judul.setText(s);
        Button btn = view.findViewById(R.id.button);
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(PetaEmbungNew.this)
                .load(imageUrl)
                .placeholder(R.drawable.defaultimg)
                .error(R.drawable.failedimg)
                .apply(requestOptions)
                .into(zoomableImageView);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        view.setLayoutParams(layoutParams);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);

        WindowManager.LayoutParams layoutParamss = new WindowManager.LayoutParams();
        layoutParamss.copyFrom(dialog.getWindow().getAttributes());
        layoutParamss.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamss.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParamss);

        dialog.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private int findGreatestCommonDivisor(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}