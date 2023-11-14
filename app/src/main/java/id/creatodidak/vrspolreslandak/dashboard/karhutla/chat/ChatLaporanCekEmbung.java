package id.creatodidak.vrspolreslandak.dashboard.karhutla.chat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.EmbungItem;
import id.creatodidak.vrspolreslandak.api.models.ListlaporancekembungItem;
import id.creatodidak.vrspolreslandak.api.models.chat.ChatAdapter;
import id.creatodidak.vrspolreslandak.api.models.chat.MChatdata;
import id.creatodidak.vrspolreslandak.api.models.chat.ResponseChat;
import id.creatodidak.vrspolreslandak.dashboard.chat.PhotoResult;
import id.creatodidak.vrspolreslandak.database.DBHelper;
import id.creatodidak.vrspolreslandak.helper.CustomDialog;
import id.creatodidak.vrspolreslandak.helper.RandomStringGenerator;
import id.creatodidak.vrspolreslandak.helper.TextHelper;
import id.creatodidak.vrspolreslandak.helper.WaktuLokal;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatLaporanCekEmbung extends AppCompatActivity {

    String ExtraID;
    String kodeembung;
    List<MChatdata> chatdata = new ArrayList<>();
    DBHelper dbHelper;
    String chatid;
    TextView idchat;
    RecyclerView rv;
    ChatAdapter adapter;
    CardView wrapperdaftartugas;
    ConstraintLayout wrapper;
    LinearLayoutManager lm, lmtugas;
    ImageView btnSend, btnCamera;
    EditText etMsg;
    boolean isDaftarTugasVisible = false;
    boolean onSelectedTugas = false;
    boolean isGeocoder = false;
    boolean isPelaksana = false;
    boolean isIntensitasAir = false;
    boolean isVolumeAir = false;
    boolean isAnalisa = false;
    boolean isPrediksi = false;
    boolean isRekomendasi = false;
    boolean isFoto = false;
    boolean isLocal = false;
    String KOLOM = "";
    String HEADER = "";
    TextView btnKeterangan;
    LinearLayout keterangan;
    ImageView cancelaction;
    String PHOTO_PATH;
    ActivityResultLauncher<Intent> opencamera, openresult;
    File storageDir;
    String nama, namapangkat, nrp, satker, pangkat, jabatan, namaembung, typeembung;
    SharedPreferences sh;
    Endpoint endpoint;
    LatLng currentLocation;
    LinearLayout editting;
    ImageView btnBagikan, btnBack;

    @SuppressLint({"NotifyDataSetChanged", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_laporan_cek_embung);
        endpoint = Client.getClient().create(Endpoint.class);

        storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "VRS/Laporan Cek Hotspot");
        Bundle data = getIntent().getExtras();
        if (data != null) {
            ExtraID = data.getString("dataid");
        }

        sh = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        namapangkat = sh.getString("pangkat", "-") + " " + sh.getString("nama", "-");
        nama = sh.getString("nama", "-");
        pangkat = sh.getString("pangkat", "00000000");
        jabatan = sh.getString("jabatan", "-") + " " + sh.getString("satker", "-");
        nrp = sh.getString("nrp", "00000000");
        satker = sh.getString("satker", "Polres Landak");


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

        lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        lm.setStackFromEnd(true);

        lmtugas = new LinearLayoutManager(this);

        RecyclerView.ItemAnimator ia = new DefaultItemAnimator();
        rv.setLayoutManager(lm);
        rv.setItemAnimator(ia);
        adapter = new ChatAdapter(this, chatdata, new ChatAdapter.OnItemClickListener() {
            @Override
            public void onBtnClick() {
                KirimData();
            }

            @Override
            public void onResendClick(String data, int position, int id, String type) {
                chatdata.get(position).setStatus("LOADING");
                adapter.notifyItemChanged(position);
                if (type.equals("text")) {
//                    resend(data, position, id);
                } else {
//                    resendImage(data, position, id);
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
                                newchat.setStatus("PENDING");
                                newchat.setCreatedAt(WaktuLokal.get());
                                chatdata.add(newchat);
                                adapter.notifyItemInserted(chatdata.size() - 1);
                                rv.scrollToPosition(chatdata.size() - 1);
                                if (dbHelper.updCekEmbung(ExtraID, "foto", String.valueOf(uri))) {
                                    if (dbHelper.newSingleChatData(ExtraID, "image", "PENDING", "Dokumentasi###" + uri)) {
                                        chatdata.get(chatdata.size() - 1).setStatus("PENDING");
                                        adapter.notifyItemChanged(chatdata.size() - 1);

                                        checklast();
                                    }
                                }
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
                            ChatLaporanCekEmbung.this,
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

        fetchChat();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchChat() {
        List<MChatdata> data = dbHelper.getChatData(ExtraID);
        chatdata.addAll(data);
        adapter.notifyDataSetChanged();
        Log.i("JUMLAH", "fetchChat: " + chatdata.size());
        checklast();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(etMsg.getText())) {
                    if (!KOLOM.equals("foto")) {
                        String text = etMsg.getText().toString();
                        String data;

                        if (KOLOM.equals("volumeair")) {
                            data = HEADER + "###" + "±" + text + " m³";
                        } else {
                            data = HEADER + "###" + text;
                        }
                        MChatdata c = new MChatdata();
                        c.setChatId(ExtraID);
                        c.setType("text");
                        c.setStatus("LOADING");
                        c.setData(data);
                        c.setCreatedAt(WaktuLokal.get());
                        chatdata.add(c);
                        adapter.notifyItemInserted(chatdata.size() - 1);
                        rv.scrollToPosition(chatdata.size() - 1);
                        hideKeyboard();

                        if (dbHelper.updCekEmbung(ExtraID, KOLOM, text)) {
                            if (dbHelper.newSingleChatData(ExtraID, "text", "PENDING", data)) {
                                chatdata.get(chatdata.size() - 1).setStatus("PENDING");
                                adapter.notifyItemChanged(chatdata.size() - 1);

                                checklast();
                            }
                        }
                    }
                } else {
                    CustomDialog.up(
                            ChatLaporanCekEmbung.this,
                            "Peringatan",
                            "Tidak diperkenankan mengirimkan data kosong!",
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
        });
    }

    private void KirimData() {
        AlertDialog alert = CustomDialog.up(
                this,
                "Memproses data...",
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

        ListlaporancekembungItem item = dbHelper.getCekEmbungByDataId(ExtraID);
        List<MultipartBody.Part> imageParts = new ArrayList<>();
        String[] uris = item.getFoto().split(",");

        for (String uri : uris) {
            File img = new File(Uri.parse(uri).getPath()).getAbsoluteFile();
            if (img.exists()) {
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), img);
                MultipartBody.Part imagePart =
                        MultipartBody.Part.createFormData("image[]", img.getName(), requestFile);
                imageParts.add(imagePart);
            }
        }
        String verifikasi = "";

        if (satker.equals("POLRES LANDAK")) {
            verifikasi = "MENUNGGU VERIFIKASI KASAT BINMAS";
        } else if (satker.contains("POLSEK")) {
            verifikasi = "MENUNGGU VERIFIKASI KAPOLSEK";
        } else {
            verifikasi = "MENUNGGU VERIFIKASI KAPOLSUBSEKTOR";
        }
        Call<ResponseChat> call = endpoint.addcekembungNew(imageParts, item.getDataid(), item.getKodeembung(), item.getLatitude(), item.getLongitude(), item.getGeocoder(), item.getPelaksana(), item.getIntensitasair(), item.getVolumeair(), item.getCuaca(), item.getAnalisa(), item.getPrediksi(), item.getRekomendasi(), item.getPersonil(), verifikasi);

        String finalVerifikasi = verifikasi;
        call.enqueue(new Callback<ResponseChat>() {
            @Override
            public void onResponse(Call<ResponseChat> call, Response<ResponseChat> response) {
                if (response.body().isBerhasil() && response.isSuccessful() && response.body() != null) {
                    alert.dismiss();
                    if (dbHelper.updCekEmbung(ExtraID, "local", "no")) {
                        generatedlaporan(response.body().getData(), finalVerifikasi);
                        hideKeyboard();
                        editting.setVisibility(View.GONE);
                    }

                } else {
                    alert.dismiss();
                    CustomDialog.up(
                            ChatLaporanCekEmbung.this,
                            "Informasi",
                            "Gagal mengirimkan laporan kepada verifikator, coba lagi nanti!",
                            "MENGERTI",
                            "",
                            new CustomDialog.AlertDialogListener() {
                                @Override
                                public void onPositiveButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                    hideKeyboard();
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
                alert.dismiss();
                CustomDialog.up(
                        ChatLaporanCekEmbung.this,
                        "Informasi",
                        "Gagal mengirimkan laporan kepada verifikator, coba lagi nanti!",
                        "MENGERTI",
                        "",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog alert) {
                                alert.dismiss();
                                hideKeyboard();
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
    private void generatedlaporan(String data, String finalVerifikasi) {
        if (dbHelper.generatedLapCekEmbung(ExtraID)) {
            if (dbHelper.newSingleChatData(ExtraID, "bot", "SERVER", "Status verifikasi:\n" + finalVerifikasi)) {
                List<MChatdata> newchat = dbHelper.getChatData(ExtraID);
                chatdata.addAll(newchat);
                adapter.notifyDataSetChanged();
                rv.scrollToPosition(chatdata.size() - 1);
            }
        }
    }

    private void checklast() {
        ListlaporancekembungItem item = dbHelper.getCekEmbungByDataId(ExtraID);
        isGeocoder = item.getGeocoder().equals("BELUM ADA");
        isPelaksana = item.getPelaksana().equals("BELUM ADA");
        isIntensitasAir = item.getIntensitasair().equals("BELUM ADA");
        isVolumeAir = item.getVolumeair().equals("BELUM ADA");
        isAnalisa = item.getAnalisa().equals("BELUM ADA");
        isPrediksi = item.getPrediksi().equals("BELUM ADA");
        isRekomendasi = item.getRekomendasi().equals("BELUM ADA");
        isFoto = item.getFoto().equals("BELUM ADA");
        isLocal = item.isLocal();
        currentLocation = new LatLng(item.getLatitude(), item.getLongitude());
        kodeembung = item.getKodeembung();

        EmbungItem emb = dbHelper.embungbyid(item.getKodeembung());
        namaembung = TextHelper.capitalize(emb.getNama() + " (" + emb.getType() + ")");

        if (isLocal) {
            if (isGeocoder) {
                Geocoder geocoder = new Geocoder(ChatLaporanCekEmbung.this, new Locale("id"));
                try {
                    List<Address> addresses = geocoder.getFromLocation(item.getLatitude(), item.getLongitude(), 1);
                    if (addresses.size() != 0) {
                        dbHelper.updCekEmbung(ExtraID, KOLOM, addresses.get(0).getAddressLine(0));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (isPelaksana) {
                    KOLOM = "pelaksana";
                    HEADER = "Pelaksana Kegiatan";
                    MChatdata c = new MChatdata();
                    c.setChatId(ExtraID);
                    c.setType("bot");
                    c.setStatus("SERVER");
                    c.setData("Siapa saja yang terlibat dalam tugas ini?");
                    c.setCreatedAt(WaktuLokal.get());
                    chatdata.add(c);
                    adapter.notifyItemInserted(chatdata.size() - 1);

                    etMsg.requestFocus();
                } else {
                    if (isIntensitasAir) {
                        KOLOM = "intensitasair";
                        HEADER = "Intensitas Air";
                        MChatdata c = new MChatdata();
                        c.setChatId(ExtraID);
                        c.setType("bot");
                        c.setStatus("SERVER");
                        c.setData("Bagaimana intensitas air jika digunakan dalam upaya pemadaman api menurut pendapat anda?\n\nSANGAT CUKUP\nCUKUP\nKURANG\nSANGAT KURANG");
                        c.setCreatedAt(WaktuLokal.get());
                        chatdata.add(c);
                        adapter.notifyItemInserted(chatdata.size() - 1);

                        etMsg.requestFocus();
                    } else {
                        if (isVolumeAir) {
                            KOLOM = "volumeair";
                            HEADER = "Volume Air";
                            MChatdata c = new MChatdata();
                            c.setChatId(ExtraID);
                            c.setType("bot");
                            c.setStatus("SERVER");
                            c.setData("Berapa perkiraan volume air menurut anda?\n\nnb:\n-Dalam m³\n- Ketik angkanya saja!");
                            c.setCreatedAt(WaktuLokal.get());
                            chatdata.add(c);
                            adapter.notifyItemInserted(chatdata.size() - 1);

                            etMsg.requestFocus();
                        } else {
                            if (isAnalisa) {
                                KOLOM = "analisa";
                                HEADER = "Analisa";
                                MChatdata c = new MChatdata();
                                c.setChatId(ExtraID);
                                c.setType("bot");
                                c.setStatus("SERVER");
                                c.setData("Apa Analisa anda?");
                                c.setCreatedAt(WaktuLokal.get());
                                chatdata.add(c);
                                adapter.notifyItemInserted(chatdata.size() - 1);

                                etMsg.requestFocus();
                            } else {
                                if (isPrediksi) {
                                    KOLOM = "prediksi";
                                    HEADER = "Prediksi";
                                    MChatdata c = new MChatdata();
                                    c.setChatId(ExtraID);
                                    c.setType("bot");
                                    c.setStatus("SERVER");
                                    c.setData("Apa prediksi anda?");
                                    c.setCreatedAt(WaktuLokal.get());
                                    chatdata.add(c);
                                    adapter.notifyItemInserted(chatdata.size() - 1);

                                    etMsg.requestFocus();
                                } else {
                                    if (isRekomendasi) {
                                        KOLOM = "rekomendasi";
                                        HEADER = "Rekomendasi";
                                        MChatdata c = new MChatdata();
                                        c.setChatId(ExtraID);
                                        c.setType("bot");
                                        c.setStatus("SERVER");
                                        c.setData("Apa rekomendasi anda kepada pimpinan?");
                                        c.setCreatedAt(WaktuLokal.get());
                                        chatdata.add(c);
                                        adapter.notifyItemInserted(chatdata.size() - 1);

                                        etMsg.requestFocus();
                                    } else {
                                        if (isFoto) {
                                            etMsg.setEnabled(false);
                                            btnSend.setVisibility(View.GONE);
                                            KOLOM = "foto";
                                            MChatdata c = new MChatdata();
                                            c.setChatId(ExtraID);
                                            c.setType("bot");
                                            c.setStatus("SERVER");
                                            c.setData("Silahkan tekan icon kamera untuk menambahkan dokumentasi");
                                            c.setCreatedAt(WaktuLokal.get());
                                            chatdata.add(c);
                                            adapter.notifyItemInserted(chatdata.size() - 1);

                                            etMsg.clearFocus();
                                        } else {
                                            etMsg.setEnabled(false);
                                            btnSend.setVisibility(View.GONE);
                                            KOLOM = "foto";
                                            MChatdata c = new MChatdata();
                                            c.setChatId(ExtraID);
                                            c.setType("bot");
                                            c.setStatus("SERVER");
                                            c.setData("Wah, laporanmu sudah lengkap...\nKamu bisa langsung mengirimkan laporan ini kepada Verifikator dengan menekan tombol dibawah ini... \nkamu juga masih bisa menambahkan dokumentasi lainnya jika laporanmu belum dikirimkan kepada Verifikator...\n\n\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47");
                                            c.setCreatedAt(WaktuLokal.get());
                                            chatdata.add(c);
                                            adapter.notifyItemInserted(chatdata.size() - 1);

                                            MChatdata nb = new MChatdata();
                                            nb.setId(999);
                                            nb.setChatId(chatid);
                                            nb.setType("button");
                                            nb.setStatus("SERVER");
                                            nb.setCreatedAt(WaktuLokal.get());
                                            nb.setData("KIRIM KE VERIFIKATOR");
                                            chatdata.add(nb);
                                            adapter.notifyItemInserted(chatdata.size() - 1);

                                            etMsg.clearFocus();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            rv.scrollToPosition(chatdata.size() - 1);
            btnCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isGeocoder && !isPelaksana && !isIntensitasAir && !isVolumeAir && !isAnalisa && !isPrediksi && !isRekomendasi) {
                        showcamera();
                    } else {
                        CustomDialog.up(
                                ChatLaporanCekEmbung.this,
                                "Informasi",
                                "Anda harus mengisi item lainnya sesuai petunjuk sebelum bisa menambahkan dokumentasi!",
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
            });

        }else{
            editting.setVisibility(View.GONE);
        }

        if(chatdata.size() != 0) {
            rv.scrollToPosition(chatdata.size() - 1);
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
        intent.putExtra("judul", "Laporan Pengecekan " + namaembung);
        intent.putExtra("koordinat", currentLocation.latitude + "," + currentLocation.longitude);
        intent.putExtra("storage", storageDir.getAbsolutePath());
        openresult.launch(intent);
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

    private void showKeyboard() {
        etMsg.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(etMsg, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideKeyboard() {
        etMsg.clearFocus();
        etMsg.setText("");
    }
}