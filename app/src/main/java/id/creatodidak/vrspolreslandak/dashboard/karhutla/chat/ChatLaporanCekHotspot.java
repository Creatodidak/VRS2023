package id.creatodidak.vrspolreslandak.dashboard.karhutla.chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.chat.ChatAdapter;
import id.creatodidak.vrspolreslandak.api.models.chat.DaftarTugasAdapter;
import id.creatodidak.vrspolreslandak.api.models.chat.DaftartugasItem;
import id.creatodidak.vrspolreslandak.api.models.chat.MChatdata;
import id.creatodidak.vrspolreslandak.api.models.chat.ResponseChat;
import id.creatodidak.vrspolreslandak.api.models.karhutla.ListHotspotItem;
import id.creatodidak.vrspolreslandak.dashboard.chat.PhotoResult;
import id.creatodidak.vrspolreslandak.database.DBHelper;
import id.creatodidak.vrspolreslandak.helper.CustomDialog;
import id.creatodidak.vrspolreslandak.helper.Hitungjarak;
import id.creatodidak.vrspolreslandak.helper.RandomStringGenerator;
import id.creatodidak.vrspolreslandak.helper.TextHelper;
import id.creatodidak.vrspolreslandak.helper.WaktuLokal;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatLaporanCekHotspot extends AppCompatActivity {

    String ExtraActivity;
    int ExtraID;
    List<MChatdata> chatdata = new ArrayList<>();
    List<DaftartugasItem> tugaslist = new ArrayList<>();
    DBHelper dbHelper;
    String chatid;
    TextView idchat;
    RecyclerView rv, rvtugas;
    ChatAdapter adapter;
    DaftarTugasAdapter adaptertugas;
    CardView wrapperdaftartugas;
    ConstraintLayout wrapper;
    LinearLayoutManager lm, lmtugas;
    ImageView btnSend, btnCamera;
    EditText etMsg;
    boolean isDokumentasiExist = false;
    boolean isDaftarTugasVisible = false;
    boolean onSelectedTugas = false;
    TextView btnKeterangan;
    LinearLayout keterangan;
    ImageView cancelaction;
    String PHOTO_PATH;
    ActivityResultLauncher<Intent> opencamera, openresult;
    File storageDir;
    LinearLayout first;
    Button mengerti;
    String nama, namapangkat, nrp, satker, pangkat, jabatan;
    SharedPreferences sh;
    Endpoint endpoint;
    boolean isJarak = false;
    LatLng currentLocation;
    int imgPosition;
    LinearLayout editting;
    ImageView btnBagikan, btnBack;
    @SuppressLint({"NotifyDataSetChanged", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_laporan_cek_hotspot);
        endpoint = Client.getClient().create(Endpoint.class);

        storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "VRS/Laporan Cek Hotspot");
        Bundle data = getIntent().getExtras();
        if (data != null) {
            ExtraID = data.getInt("id");
            ExtraActivity = data.getString("activity");
        }

        mengerti = findViewById(R.id.btMengerti);
        first = findViewById(R.id.first);
        sh = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        namapangkat = sh.getString("pangkat", "-") + " " + sh.getString("nama", "-");
        nama = sh.getString("nama", "-");
        pangkat = sh.getString("pangkat", "00000000");
        jabatan = sh.getString("jabatan", "-") + " " + sh.getString("satker", "-");
        nrp = sh.getString("nrp", "00000000");
        satker = sh.getString("satker", "Polres Landak");
        boolean isMengerti = sh.getBoolean("mengerti", false);

        if (!isMengerti) {
            first.setVisibility(View.VISIBLE);
            mengerti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = sh.edit();
                    editor.putBoolean("mengerti", true);
                    editor.apply();

                    first.setVisibility(View.GONE);
                }
            });
        } else {
            first.setVisibility(View.GONE);
        }

        dbHelper = new DBHelper(this);
        dbHelper.inisialisasi();
        idchat = findViewById(R.id.idChat);
        idchat.setText("Laporan #" + ExtraID);

        editting = findViewById(R.id.editting);
        etMsg = findViewById(R.id.etMsg);
        btnSend = findViewById(R.id.btnSend);
        btnCamera = findViewById(R.id.btnCamera);
        btnKeterangan = findViewById(R.id.btnKeterangan);
        keterangan = findViewById(R.id.keterangan);
        cancelaction = findViewById(R.id.cancelAction);
        btnBagikan = findViewById(R.id.btnBagikan);
        btnBack = findViewById(R.id.btnBack);
        wrapper = findViewById(R.id.LaporanCekHotspot);
        rv = findViewById(R.id.rvPesan);
        rvtugas = findViewById(R.id.daftartugas);

        lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        lm.setStackFromEnd(true);

        lmtugas = new LinearLayoutManager(this);

        RecyclerView.ItemAnimator ia = new DefaultItemAnimator();
        rv.setLayoutManager(lm);
        rv.setItemAnimator(ia);
        wrapperdaftartugas = findViewById(R.id.WrapperDaftarTugas);
        adapter = new ChatAdapter(this, chatdata, new ChatAdapter.OnItemClickListener() {
            @Override
            public void onBtnClick() {
                CustomDialog.up(
                        ChatLaporanCekHotspot.this,
                        "Konfirmasi",
                        "Jika laporan ini anda kirim ke verifikator, maka anda tidak dapat menambah dokumentasi lagi...\nLanjutkan?",
                        "LANJUTKAN",
                        "BATAL",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog alert) {
                                alert.dismiss();
                                generateLaporan();
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
            public void onResendClick(String data, int position, int id, String type) {
                chatdata.get(position).setStatus("LOADING");
                adapter.notifyItemChanged(position);
                if(type.equals("text")){
                    resend(data, position, id);
                }else{
                    resendImage(data, position, id);
                }
            }
        });

        rv.setAdapter(adapter);

        opencamera = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        showPreview(PHOTO_PATH);
                    }
                }
        );

        openresult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null) {
                            String gambar = result.getData().getStringExtra("gambar");

                            if (gambar != null && !gambar.isEmpty()) {
                                File imageFile = new File(gambar);
                                Uri uri = Uri.fromFile(imageFile);
                                MChatdata newchat = new MChatdata();
                                newchat.setChatId(chatid);
                                newchat.setType("image");
                                newchat.setData("Dokumentasi###" + uri);
                                newchat.setStatus("LOADING");
                                newchat.setCreatedAt(WaktuLokal.get());
                                chatdata.add(newchat);
                                adapter.notifyItemInserted(chatdata.size() - 1);
                                rv.scrollToPosition(chatdata.size() - 1);
                                sendImage(imageFile);
                                onSelectedTugas = false;
                            }
                            deleteCache();
                        }
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        deleteCache();
                    }
                }
        );

        wrapper.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (isDaftarTugasVisible) {
                    wrapperdaftartugas.setVisibility(View.GONE);
                    isDaftarTugasVisible = false;
                }
                return false;
            }
        });
        rv.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (isDaftarTugasVisible) {
                    wrapperdaftartugas.setVisibility(View.GONE);
                    isDaftarTugasVisible = false;
                }
                return false;
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSelectedTugas) {
                    CustomDialog.up(
                            ChatLaporanCekHotspot.this,
                            "Konfirmasi",
                            "Anda sedang dalam mode mengetik laporan, jika anda keluar maka data yang sudah anda ketik akan hilang...\nKeluar?",
                            "KELUAR",
                            "BATAL",
                            new CustomDialog.AlertDialogListener() {
                                @Override
                                public void onPositiveButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                    finish();
                                }

                                @Override
                                public void onNegativeButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                }
                            },
                            true, true, false
                    ).show();
                } else {
                    finish();
                }
            }
        });

        cekChatId();
    }

    private void cekChatId() {
        if (!dbHelper.cekchatid(ExtraID, ExtraActivity)) {
            if (dbHelper.registerChatid(ExtraID, ExtraActivity)) {
                chatid = dbHelper.getChatId(String.valueOf(ExtraID), ExtraActivity);
                fetchChat();
            }
        } else {
            chatid = dbHelper.getChatId(String.valueOf(ExtraID), ExtraActivity);
            fetchChat();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchChat() {
        List<MChatdata> chat = dbHelper.getChatData(chatid);
        ListHotspotItem item = dbHelper.getDataHotspotById(String.valueOf(ExtraID));

        if(item.getVerifikasi().equals("BELUM ADA")) {
            if (item.getPemiliklahan().equals("BELUM ADA")) {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(false);
                list.setValue("Nama Pemilik Lahan");
                list.setParameter("pemiliklahan");
                list.setType("text");
                tugaslist.add(list);
            } else {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(true);
                list.setValue("Nama Pemilik Lahan");
                list.setParameter("pemiliklahan");
                list.setType("text");
                tugaslist.add(list);
            }

            if (item.getPenyebabapi().equals("BELUM ADA")) {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(false);
                list.setValue("Penyebab Api");
                list.setParameter("penyebabapi");
                list.setType("text");
                tugaslist.add(list);
            } else {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(true);
                list.setValue("Penyebab Api");
                list.setParameter("penyebabapi");
                list.setType("text");
                tugaslist.add(list);
            }

            if (item.getLuas().equals("BELUM ADA")) {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(false);
                list.setValue("Luas Lahan Yang Terbakar");
                list.setParameter("luas");
                list.setType("text");
                tugaslist.add(list);
            } else {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(true);
                list.setValue("Luas Lahan Yang Terbakar");
                list.setParameter("luas");
                list.setType("text");
                tugaslist.add(list);
            }

            if (item.getPelaksanakegiatan().equals("BELUM ADA")) {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(false);
                list.setValue("Pelaksana Kegiatan");
                list.setParameter("pelaksanakegiatan");
                list.setType("text");
                tugaslist.add(list);
            } else {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(true);
                list.setValue("Pelaksana Kegiatan");
                list.setParameter("kegiatan");
                list.setType("text");
                tugaslist.add(list);
            }

            if (item.getTindakan().equals("BELUM ADA")) {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(false);
                list.setValue("Tindakan Di Lapangan");
                list.setParameter("tindakan");
                list.setType("text");
                tugaslist.add(list);
            } else {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(true);
                list.setValue("Tindakan Di Lapangan");
                list.setParameter("tindakan");
                list.setType("text");
                tugaslist.add(list);
            }

            if (item.getAnalisa().equals("BELUM ADA")) {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(false);
                list.setValue("Analisa");
                list.setParameter("analisa");
                list.setType("text");
                tugaslist.add(list);
            } else {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(true);
                list.setValue("Analisa");
                list.setParameter("analisa");
                list.setType("text");
                tugaslist.add(list);
            }

            if (item.getPrediksi().equals("BELUM ADA")) {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(false);
                list.setValue("Prediksi");
                list.setParameter("prediksi");
                list.setType("text");
                tugaslist.add(list);
            } else {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(true);
                list.setValue("Prediksi");
                list.setParameter("prediksi");
                list.setType("text");
                tugaslist.add(list);
            }

            if (item.getRekomendasi().equals("BELUM ADA")) {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(false);
                list.setValue("Rekomendasi");
                list.setParameter("rekomendasi");
                list.setType("text");
                tugaslist.add(list);
            } else {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(true);
                list.setValue("Rekomendasi");
                list.setParameter("rekomendasi");
                list.setType("text");
                tugaslist.add(list);
            }

            if (item.getKondisiapi().equals("BELUM ADA")) {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(false);
                list.setValue("Kondisi Titik Api Terakhir");
                list.setParameter("kondisiapi");
                list.setType("text");
                tugaslist.add(list);
            } else {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(true);
                list.setValue("Kondisi Titik Api Terakhir");
                list.setParameter("kondisiapi");
                list.setType("text");
                tugaslist.add(list);
            }

            if (item.getFoto().equals("BELUM ADA")) {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(false);
                list.setValue("Dokumentasi");
                list.setParameter("foto");
                list.setType("image");
                tugaslist.add(list);
            } else {
                DaftartugasItem list = new DaftartugasItem();
                list.setId(ExtraID);
                list.setStatus(true);
                list.setValue("Dokumentasi lainnya...");
                list.setParameter("foto");
                list.setType("image");
                tugaslist.add(list);
                isDokumentasiExist = true;
            }

            chatdata.addAll(chat);
            adapter.notifyDataSetChanged();
            rv.scrollToPosition(chatdata.size()-1);

            adaptertugas = new DaftarTugasAdapter(this, tugaslist, new DaftarTugasAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String parameter, String type, boolean status, String value, int position) {
                    onSelectedTugas = true;
                    if (type.equals("image")) {
                        imgPosition = position;
                        CustomDialog.up(
                                ChatLaporanCekHotspot.this,
                                "Konfirmasi",
                                "Pilih Jenis Dokumentasi",
                                "FOTO",
                                "VIDEO",
                                new CustomDialog.AlertDialogListener() {
                                    @Override
                                    public void onPositiveButtonClick(AlertDialog alert) {
                                        alert.dismiss();
                                        LatLng koordinaths = dbHelper.getKoordinatHotspot(ExtraID);
                                        if (koordinaths != null) {
                                            if (getKoordinat(koordinaths)) {
                                                showcamera();
                                            } else {
                                                if (nrp.equals("98070129") || nrp.equals("66060389")) {
                                                    showcamera();
                                                } else {
                                                    CustomDialog.up(
                                                            ChatLaporanCekHotspot.this,
                                                            "Informasi",
                                                            "jarak anda terlalu jauh dari titik api, tidak dapat melanjutkan!\nMaksimal jarak pengambilan dokumentasi 500M dari titik api",
                                                            "OK",
                                                            "",
                                                            new CustomDialog.AlertDialogListener() {
                                                                @Override
                                                                public void onPositiveButtonClick(AlertDialog alerts) {
                                                                    alerts.dismiss();
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
                                    }

                                    @Override
                                    public void onNegativeButtonClick(AlertDialog alert) {
                                        alert.dismiss();
                                        onSelectedTugas = false;
                                    }
                                },
                                true, true, false
                        ).show();
                    } else {
                        showKeyboard();
                        btnSend.setVisibility(View.VISIBLE);
                        keterangan.setVisibility(View.VISIBLE);
                        btnKeterangan.setText(value);
                    }

                    wrapperdaftartugas.setVisibility(View.GONE);
                    cancelaction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CustomDialog.up(
                                    ChatLaporanCekHotspot.this,
                                    "Ganti laporan?",
                                    "data yang belum dikirim akan hilang, lanjutkan?",
                                    "LANJUTKAN",
                                    "BATAL",
                                    new CustomDialog.AlertDialogListener() {
                                        @Override
                                        public void onPositiveButtonClick(AlertDialog alert) {
                                            onSelectedTugas = false;
                                            alert.dismiss();
                                            hideKeyboard();
                                            btnSend.setVisibility(View.GONE);
                                            btnKeterangan.setText("");
                                            keterangan.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onNegativeButtonClick(AlertDialog alert) {
                                            alert.dismiss();
                                            showKeyboard();
                                        }
                                    },
                                    true, true, false
                            ).show();
                        }
                    });
                    btnSend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!TextUtils.isEmpty(etMsg.getText())) {
                                sendData(etMsg.getText().toString(), parameter, value, position, type);
                                MChatdata newchat = new MChatdata();
                                newchat.setChatId(chatid);
                                newchat.setType(type);
                                newchat.setData(getParameterHeading(value, etMsg.getText().toString()));
                                newchat.setStatus("LOADING");
                                newchat.setCreatedAt(WaktuLokal.get());
                                chatdata.add(newchat);
                                adapter.notifyItemInserted(chatdata.size() - 1);
                                rv.scrollToPosition(chatdata.size() - 1);
                                hideKeyboard();
                                keterangan.setVisibility(View.GONE);
                                btnSend.setVisibility(View.GONE);
                                onSelectedTugas = false;
                            } else {
                                CustomDialog.up(
                                        ChatLaporanCekHotspot.this,
                                        "Peringatan",
                                        "Data tidak boleh kosong!",
                                        "PERBAIKI",
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
                    });
                }
            });
            rvtugas.setLayoutManager(lmtugas);
            rvtugas.setAdapter(adaptertugas);

            btnCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!onSelectedTugas) {
                        if (!isDaftarTugasVisible) {
                            wrapperdaftartugas.setVisibility(View.VISIBLE);
                            isDaftarTugasVisible = true;
                        } else {
                            wrapperdaftartugas.setVisibility(View.GONE);
                            isDaftarTugasVisible = false;
                        }
                    } else {
                        CustomDialog.up(
                                ChatLaporanCekHotspot.this,
                                "Ganti laporan?",
                                "data yang belum dikirim akan hilang, lanjutkan?",
                                "LANJUTKAN",
                                "BATAL",
                                new CustomDialog.AlertDialogListener() {
                                    @Override
                                    public void onPositiveButtonClick(AlertDialog alert) {
                                        alert.dismiss();
                                        hideKeyboard();
                                        wrapperdaftartugas.setVisibility(View.VISIBLE);
                                        keterangan.setVisibility(View.GONE);
                                        btnSend.setVisibility(View.GONE);
                                        onSelectedTugas = false;
                                    }

                                    @Override
                                    public void onNegativeButtonClick(AlertDialog alert) {
                                        alert.dismiss();
                                        showKeyboard();
                                    }
                                },
                                true, true, false
                        ).show();
                    }
                }
            });

            cekKelengkapan();
        }else{
            editting.setVisibility(View.GONE);
            chatdata.addAll(chat);
            adapter.notifyDataSetChanged();
            rv.scrollToPosition(chatdata.size()-1);
        }
    }

//    TEXT TYPE
    private void sendData(String msg, String parameter, String value, int position, String type) {
        String riwayat = namapangkat + " menambahkan " + value;
        Call<ResponseChat> call = endpoint.sendDataCekHotspot(ExtraID, parameter, msg, riwayat);
        call.enqueue(new Callback<ResponseChat>() {
            @Override
            public void onResponse(Call<ResponseChat> call, Response<ResponseChat> response) {
                if (response.body() != null && response.isSuccessful() && response.body().isBerhasil()) {
                    addnewchat(msg, position, type, "SERVER", riwayat, parameter, value);
                } else {
                    addnewchat(msg, position, type, "PENDING", riwayat, parameter, value);
                }
            }

            @Override
            public void onFailure(Call<ResponseChat> call, Throwable t) {
                addnewchat(msg, position, type, "PENDING", riwayat, parameter, value);
            }
        });
    }

    private void resend(String data, int position, int id) {
        String[] curdata = data.split("###");

        String riwayat = namapangkat + " menambahkan " + curdata[0];
        Call<ResponseChat> call = endpoint.sendDataCekHotspot(ExtraID, getKolom(curdata[0]), curdata[1], riwayat);
        call.enqueue(new Callback<ResponseChat>() {
            @Override
            public void onResponse(Call<ResponseChat> call, Response<ResponseChat> response) {
                if (response.body() != null && response.isSuccessful() && response.body().isBerhasil()) {
                    cekKelengkapan();

                    if (dbHelper.updChatData(id, "SERVER")) {
                        chatdata.get(position).setStatus("SERVER");
                        adapter.notifyItemChanged(position);
                    } else {
                        chatdata.get(position).setStatus("PENDING");
                        adapter.notifyItemChanged(position);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseChat> call, Throwable t) {
                cekKelengkapan();
                chatdata.get(position).setStatus("PENDING");
                adapter.notifyItemChanged(position);
            }
        });
    }

    private void addnewchat(String data, int position, String type, String status, String riwayat, String parameter, String value) {
        if (dbHelper.updHotspotData(ExtraID, parameter, data, riwayat)) {
            if (dbHelper.newSingleChatData(chatid, type, status, getParameterHeading(value, data))) {
                chatdata.get(chatdata.size() - 1).setStatus(status);
                adapter.notifyItemChanged(chatdata.size() - 1);

                tugaslist.get(position).setStatus(true);
                adaptertugas.notifyItemChanged(position);
                cekKelengkapan();
            }
        }
    }


//    IMAGE TYPE
    private void showcamera() {
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

    private void showPreview(String photoPath) {

        Intent intent = new Intent(this, PhotoResult.class);
        intent.putExtra("photopath", photoPath);
        intent.putExtra("judul", "Laporan Pengecekan Hotspot disekitar wilkum " + TextHelper.capitalize(satker));
        intent.putExtra("koordinat", currentLocation.latitude + "," + currentLocation.longitude);
        intent.putExtra("storage", storageDir.getAbsolutePath());
        openresult.launch(intent);
    }

    private void sendImage(File imageFile) {
        Uri uri = Uri.fromFile(imageFile);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

        Call<ResponseChat> call = endpoint.uploadFotoCekHotspot(ExtraID, namapangkat + " menambahkan dokumentasi (" + WaktuLokal.gettanggaldanjam() + ")", image);
        call.enqueue(new Callback<ResponseChat>() {
            @Override
            public void onResponse(Call<ResponseChat> call, Response<ResponseChat> response) {
                if (response.body() != null && response.body().isBerhasil() && response.isSuccessful()) {
                    addnewImgchat(String.valueOf(uri), namapangkat + " menambahkan dokumentasi (" + WaktuLokal.gettanggaldanjam() + ")","SERVER");
                } else {
                    addnewImgchat(String.valueOf(uri), namapangkat + " menambahkan dokumentasi (" + WaktuLokal.gettanggaldanjam() + ")", "PENDING");
                }
            }

            @Override
            public void onFailure(Call<ResponseChat> call, Throwable t) {
                addnewImgchat(String.valueOf(uri), namapangkat + " menambahkan dokumentasi (" + WaktuLokal.gettanggaldanjam() + ")","PENDING");
            }
        });
    }

    private void resendImage(String data, int position, int id) {
        String[] curdata = data.split("###");
        File file = new File(Objects.requireNonNull(Uri.parse(curdata[1]).getPath())).getAbsoluteFile();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        Call<ResponseChat> call = endpoint.uploadFotoCekHotspot(ExtraID, namapangkat + " menambahkan dokumentasi (" + WaktuLokal.gettanggaldanjam() + ")", image);

        call.enqueue(new Callback<ResponseChat>() {
            @Override
            public void onResponse(Call<ResponseChat> call, Response<ResponseChat> response) {
                if (response.body() != null && response.isSuccessful() && response.body().isBerhasil()) {
                    cekKelengkapan();

                    if (dbHelper.updChatData(id, "SERVER")) {
                        chatdata.get(position).setStatus("SERVER");
                        adapter.notifyItemChanged(position);
                    } else {
                        chatdata.get(position).setStatus("PENDING");
                        adapter.notifyItemChanged(position);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseChat> call, Throwable t) {
                cekKelengkapan();
                chatdata.get(position).setStatus("PENDING");
                adapter.notifyItemChanged(position);
            }
        });
    }

    private void addnewImgchat(String data, String riwayat, String status) {
        if (dbHelper.updHotspotData(ExtraID, "foto", data, riwayat)) {
            if (dbHelper.newSingleChatData(chatid, "image", status, "Dokumentasi###" + data)) {
                chatdata.get(chatdata.size() - 1).setStatus(status);
                adapter.notifyItemChanged(chatdata.size() - 1);

                tugaslist.get(imgPosition).setStatus(true);
                adaptertugas.notifyItemChanged(imgPosition);
                cekKelengkapan();
            }
        }
    }

//    UTILITY
    public boolean getKoordinat(LatLng hotspot) {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(ChatLaporanCekHotspot.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        dbHelper.updHotspotData(ExtraID, "latlap", String.valueOf(location.getLatitude()), namapangkat + " menambahkan Latitude Laporan");
                        dbHelper.updHotspotData(ExtraID, "longlap", String.valueOf(location.getLongitude()), namapangkat + " menambahkan Longitude Laporan");
                        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        double jarak = Hitungjarak.calculateDistancemeters(hotspot.latitude, hotspot.longitude, location.getLatitude(), location.getLongitude());
                        isJarak = !(jarak > 500);
                    });
        }

        return isJarak;
    }

    private String getKolom(String data) {
        if (data.equals("Nama Pemilik Lahan")) {
            return "pemiliklahan";
        } else if (data.equals("Penyebab Api")) {
            return "penyebabapi";
        } else if (data.equals("Luas Lahan Yang Terbakar")) {
            return "luas";
        } else if (data.equals("Pelaksana Kegiatan")) {
            return "pelaksanakegiatan";
        } else if (data.equals("Tindakan Di Lapangan")) {
            return "tindakan";
        } else if (data.equals("Analisa")) {
            return "analisa";
        } else if (data.equals("Prediksi")) {
            return "prediksi";
        } else if (data.equals("Rekomendasi")) {
            return "rekomendasi";
        } else if (data.equals("Kondisi Titik Api Terakhir")) {
            return "kondisiapi";
        } else {
            return "xxx";
        }
    }

    private String getParameterHeading(String parameter, String data) {

        if (parameter.equals("Nama Pemilik Lahan")) {
            return "Nama Pemilik Lahan###" + data;
        } else if (parameter.equals("Penyebab Api")) {
            return "Penyebab Api###" + data;
        } else if (parameter.equals("Luas Lahan Yang Terbakar")) {
            return "Luas Lahan Yang Terbakar###" + data;
        } else if (parameter.equals("Pelaksana Kegiatan")) {
            return "Pelaksana Kegiatan###" + data;
        } else if (parameter.equals("Tindakan Di Lapangan")) {
            return "Tindakan Di Lapangan###" + data;
        } else if (parameter.equals("Analisa")) {
            return "Analisa###" + data;
        } else if (parameter.equals("Prediksi")) {
            return "Prediksi###" + data;
        } else if (parameter.equals("Rekomendasi")) {
            return "Rekomendasi###" + data;
        } else if (parameter.equals("Kondisi Titik Api Terakhir")) {
            return "Kondisi Titik Api Terakhir###" + data;
        } else {
            return "You";
        }
    }

    private void showKeyboard() {
        etMsg.setEnabled(true);
        etMsg.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(etMsg, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideKeyboard() {
        etMsg.setEnabled(false);
        etMsg.clearFocus();
        etMsg.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(etMsg.getWindowToken(), 0);
        }
    }

    public void deleteCache() {
        File fileToDelete = new File(PHOTO_PATH);
        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                Log.i("DELETE CACHE", "BERHASIL");
            } else {
                Log.i("DELETE CACHE", "GAGAL");
            }
        } else {
            Log.i("DELETE CACHE", "TIDAK DITEMUKAN");
        }
    }

    @Override
    public void onBackPressed() {
        if (onSelectedTugas) {
            CustomDialog.up(
                    this,
                    "Konfirmasi",
                    "Anda sedang dalam mode mengetik laporan, jika anda keluar maka data yang sudah anda ketik akan hilang...\nKeluar?",
                    "KELUAR",
                    "BATAL",
                    new CustomDialog.AlertDialogListener() {
                        @Override
                        public void onPositiveButtonClick(AlertDialog alert) {
                            alert.dismiss();
                            finish();
                        }

                        @Override
                        public void onNegativeButtonClick(AlertDialog alert) {
                            alert.dismiss();
                        }
                    },
                    true, true, false
            ).show();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void cekKelengkapan() {
        ListHotspotItem item = dbHelper.getDataHotspotById(String.valueOf(ExtraID));

        if (item.getPemiliklahan().equals("BELUM ADA") || item.getLatlap() == 0 || item.getLonglap() == 0 || item.getTindakan().equals("BELUM ADA") || item.getPenyebabapi().equals("BELUM ADA") || item.getLuas().equals("BELUM ADA") || item.getPelaksanakegiatan().equals("BELUM ADA") || item.getAnalisa().equals("BELUM ADA") || item.getPrediksi().equals("BELUM ADA") || item.getRekomendasi().equals("BELUM ADA") || item.getKondisiapi().equals("BELUM ADA") || item.getFoto().equals("BELUM ADA")) {
            btnCamera.setImageResource(R.drawable.taskwarning);
        } else {
            if(item.getVerifikasi().equals("BELUM ADA")) {
                btnCamera.setImageResource(R.drawable.taskadd);

                List<MChatdata> bot = new ArrayList<>();

                MChatdata nc = new MChatdata();
                nc.setId(999);
                nc.setChatId(chatid);
                nc.setType("bot");
                nc.setStatus("SERVER");
                nc.setCreatedAt(WaktuLokal.get());
                nc.setData("Wah, laporanmu sudah lengkap...\nKamu bisa langsung mengirimkan laporan ini kepada Verifikator dengan menekan tombol dibawah ini... \nkamu juga masih bisa menambahkan dokumentasi lainnya jika laporanmu belum dikirimkan kepada Verifikator...\n\n\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47");

                MChatdata nb = new MChatdata();
                nb.setId(999);
                nb.setChatId(chatid);
                nb.setType("button");
                nb.setStatus("SERVER");
                nb.setCreatedAt(WaktuLokal.get());
                nb.setData("KIRIM KE VERIFIKATOR");

                bot.add(nc);
                bot.add(nb);

                chatdata.addAll(bot);
                adapter.notifyDataSetChanged();
                rv.scrollToPosition(chatdata.size() - 1);
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void generateLaporan() {
        AlertDialog alert = CustomDialog.up(
                this,
                "Generate laporan",
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

        alert.show();
        String verifikasi = "";

        if (satker.equals("POLRES LANDAK")) {
            verifikasi = "MENUNGGU VERIFIKASI KABAG OPS";
        } else if (satker.contains("POLSEK")) {
            verifikasi = "MENUNGGU VERIFIKASI KAPOLSEK";
        } else {
            verifikasi = "MENUNGGU VERIFIKASI KAPOLSUBSEKTOR";
        }

        String newRiwayat = namapangkat + " mengirimkan laporan kepada verifikator (" + WaktuLokal.gettanggaldanjam() + ")";

        Call<ResponseChat> call = endpoint.verifikasiCekHotspot(ExtraID, verifikasi, newRiwayat);
        String finalVerifikasi = verifikasi;
        call.enqueue(new Callback<ResponseChat>() {
            @Override
            public void onResponse(Call<ResponseChat> call, Response<ResponseChat> response) {
                if(response.isSuccessful() && response.body() != null && response.body().isBerhasil()){
                    if(dbHelper.generatedLapCekHotspot(ExtraID, chatid, TextHelper.capitalize(nama), pangkat+"/"+nrp, TextHelper.capitalize(jabatan), finalVerifikasi, newRiwayat)){
                        alert.dismiss();
                        CustomDialog.up(
                                ChatLaporanCekHotspot.this,
                                "Informasi",
                                "Berhasil mengirimkan laporan kepada Verifikator!",
                                "LANJUTKAN",
                                "",
                                new CustomDialog.AlertDialogListener() {
                                    @Override
                                    public void onPositiveButtonClick(AlertDialog alerts) {
                                        alerts.dismiss();
                                        chatdata.clear();
                                        adapter.notifyDataSetChanged();
                                        afterGenerated();
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

            @Override
            public void onFailure(Call<ResponseChat> call, Throwable t) {
                alert.dismiss();
                CustomDialog.up(
                        ChatLaporanCekHotspot.this,
                        "Informasi",
                        "Gagal mengirimkan laporan kepada Verifikator!\nCoba lagi nanti...",
                        "MENGERTI",
                        "",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog alerts) {
                                alerts.dismiss();
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

    @SuppressLint("NotifyDataSetChanged")
    private void afterGenerated() {
        List<MChatdata> chat = dbHelper.getChatData(chatid);
        chatdata.addAll(chat);
        adapter.notifyDataSetChanged();
        editting.setVisibility(View.GONE);
    }
}