package id.creatodidak.vrspolreslandak.dashboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.Client;
import id.creatodidak.vrspolreslandak.api.Endpoint;
import id.creatodidak.vrspolreslandak.api.models.chat.ResponseChat;
import id.creatodidak.vrspolreslandak.auth.JabatanItem;
import id.creatodidak.vrspolreslandak.auth.ModelProfile;
import id.creatodidak.vrspolreslandak.auth.ModelSatfungJabatan;
import id.creatodidak.vrspolreslandak.auth.PangkatItem;
import id.creatodidak.vrspolreslandak.auth.ProfileItem;
import id.creatodidak.vrspolreslandak.auth.SatfungItem;
import id.creatodidak.vrspolreslandak.auth.SatkerItem;
import id.creatodidak.vrspolreslandak.dashboard.karhutla.pimpinan.CekEmbungPimpinan;
import id.creatodidak.vrspolreslandak.helper.CustomDatePickerDialog;
import id.creatodidak.vrspolreslandak.helper.CustomDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String nrp, satfung, pangkat, jabatan, nama, foto, satker;
    Endpoint endpoint;

    EditText petNrp;
    EditText petNik;
    EditText petNama;
    EditText petWhatsapp;
    TextView ptvTanggalLahir;
    Spinner pspPangkat;
    Spinner pspSatker;
    Spinner pstSatfung;
    Spinner pspJabatan;
    Button pbtnBatal;
    Button pbtnUpdate;
    AlertDialog alerts;
    List<String> pangkats = new ArrayList<>();
    List<String> satkers = new ArrayList<>();
    List<String> satfungs = new ArrayList<>();
    List<String> jabatans = new ArrayList<>();
    List<SatkerItem> satkerwilayah = new ArrayList<>();
    ArrayAdapter<String> adpPangkat;
    ArrayAdapter<String> adpSatker;
    ArrayAdapter<String> adpSatfung;
    ArrayAdapter<String> adpJabatan;
    String currentSatker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        endpoint = Client.getClient().create(Endpoint.class);
        sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        nrp = sharedPreferences.getString("nrp", null);
        satfung = sharedPreferences.getString("satfung", null);
        pangkat = sharedPreferences.getString("pangkat", null);
        nama = sharedPreferences.getString("nama", null);
        jabatan = sharedPreferences.getString("jabatan", null);
        foto = sharedPreferences.getString("foto", null);
        satker = sharedPreferences.getString("satker", null);

        alerts = CustomDialog.up(
                this,
                "Memuat data...",
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

        petNrp = findViewById(R.id.petNrp);
        petNik = findViewById(R.id.petNik);
        petNama = findViewById(R.id.petNama);
        petWhatsapp = findViewById(R.id.petWhatsapp);
        ptvTanggalLahir = findViewById(R.id.ptvTanggalLahir);
        pspPangkat = findViewById(R.id.pspPangkat);
        pspSatker = findViewById(R.id.pspSatker);
        pstSatfung = findViewById(R.id.pspSatfung);
        pspJabatan = findViewById(R.id.pspJabatan);
        pbtnBatal = findViewById(R.id.pbtnBatal);
        pbtnUpdate = findViewById(R.id.pbtnUpdate);

        adpJabatan = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jabatans);
        adpJabatan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpPangkat = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pangkats);
        adpPangkat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpSatker = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, satkers);
        adpSatker.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpSatfung = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, satfungs);
        adpSatfung.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        pspJabatan.setAdapter(adpJabatan);
        pspPangkat.setAdapter(adpPangkat);
        pspSatker.setAdapter(adpSatker);
        pstSatfung.setAdapter(adpSatfung);

        loadProfile();

        pspSatker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(!currentSatker.equals(satker+"xxx")) {
                    loadSatfung(pspSatker.getSelectedItem().toString());
                    pspJabatan.setSelection(0);
                    pstSatfung.setSelection(0);
                }else{
                    currentSatker = pspSatker.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pbtnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog.up(
                        Profile.this,
                        "Konfirmasi",
                        "Data yang sudah anda ubah tidak akan dikirim ke server, lanjutkan?",
                        "LANJUTKAN",
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
            }
        });

        pbtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDataValid()){
                    CustomDialog.up(
                            Profile.this,
                            "Konfirmasi",
                            "Data akan dikirim ke server, lanjutkan?",
                            "LANJUTKAN",
                            "BATAL",
                            new CustomDialog.AlertDialogListener() {
                                @Override
                                public void onPositiveButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                    updatedata();
                                }

                                @Override
                                public void onNegativeButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                }
                            },
                            true, true, false
                    ).show();
                }else{
                    CustomDialog.up(
                            Profile.this,
                            "Peringatan",
                            "Isi / Pilih seluruh data jika ingin mengupdate!",
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
        ptvTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CustomDatePickerDialog(Profile.this, new CustomDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int month, int day) {
                        ptvTanggalLahir.setText(String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
                    }
                }).show();
            }
        });
    }

    public boolean isDataValid(){
        return !TextUtils.isEmpty(petNama.getText()) &&
                !TextUtils.isEmpty(petNik.getText()) &&
                !petNik.getText().toString().equals("0") &&
                !TextUtils.isEmpty(petNrp.getText()) &&
                !pstSatfung.getSelectedItem().equals("PILIH SATFUNG") &&
                !pspJabatan.getSelectedItem().equals("PILIH JABATAN") &&
                !TextUtils.isEmpty(petWhatsapp.getText());
    }

    private void updatedata() {
        AlertDialog xx = CustomDialog.up(
                this,
                "Mengirim data...",
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
        xx.show();

        String snrp = petNrp.getText().toString();
        String snik = petNik.getText().toString();
        String snama = petNama.getText().toString();
        String stanggal = ptvTanggalLahir.getText().toString();
        String spangkat = pspPangkat.getSelectedItem().toString();
        String ssatker = pspSatker.getSelectedItem().toString();
        String ssatfung = pstSatfung.getSelectedItem().toString();
        String sjabatan = pspJabatan.getSelectedItem().toString();
        String swhatsapp = petWhatsapp.getText().toString();
        String wilayah = "";

        for(SatkerItem item : satkerwilayah){
            if(item.getSatker().equals(ssatker)){
                wilayah = item.getWilayah();
            }
        }

        String finalWilayah = wilayah;
        Call<ResponseChat> call = endpoint.updateDataDiri(snrp, snik, snama, stanggal, spangkat, ssatker, ssatfung, sjabatan, swhatsapp);
        call.enqueue(new Callback<ResponseChat>() {
            @Override
            public void onResponse(Call<ResponseChat> call, Response<ResponseChat> response) {
                xx.dismiss();
                if(response.isSuccessful() && response.body() != null && response.body().isBerhasil()){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("nrp", snrp);
                    editor.putString("nama", snama);
                    editor.putString("pangkat", spangkat);
                    editor.putString("satker", ssatker);
                    editor.putString("satfung", ssatfung);
                    editor.putString("jabatan", sjabatan);
                    editor.putString("tanggal_lahir", stanggal);
                    editor.putString("wa", swhatsapp);
                    editor.putString("wilayah", finalWilayah);
                    editor.putString("nik", snik);
                    editor.apply();

                    CustomDialog.up(
                            Profile.this,
                            "Informasi",
                            "Berhasil mengupdate data diri!",
                            "LANJUTKAN",
                            "",
                            new CustomDialog.AlertDialogListener() {
                                @Override
                                public void onPositiveButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                    Intent resultIntent = new Intent();
                                    setResult(RESULT_OK, resultIntent);
                                    finish();
                                }

                                @Override
                                public void onNegativeButtonClick(AlertDialog alert) {

                                }
                            },
                            true, false, false
                    ).show();
                }else{
                    CustomDialog.up(
                            Profile.this,
                            "Informasi",
                            "Gagal mengirimkan data!",
                            "LANJUTKAN",
                            "",
                            new CustomDialog.AlertDialogListener() {
                                @Override
                                public void onPositiveButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                    finish();
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
                xx.dismiss();
                CustomDialog.up(
                        Profile.this,
                        "Informasi",
                        "Gagal mengirimkan data, periksa jaringan internet anda!",
                        "MENGERTI",
                        "",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog alert) {
                                alert.dismiss();
                                finish();
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

    private void loadSatfung(String satker) {
        AlertDialog xx = CustomDialog.up(
                this,
                "Memuat data...",
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
        xx.show();

        Call<ModelSatfungJabatan> call = endpoint.getSatfungJabatan(satker);
        call.enqueue(new Callback<ModelSatfungJabatan>() {
            @Override
            public void onResponse(Call<ModelSatfungJabatan> call, Response<ModelSatfungJabatan> response) {
                xx.dismiss();
                satfungs.clear();
                jabatans.clear();

                satfungs.add("PILIH SATFUNG");
                for(SatfungItem item : response.body().getSatfung()){
                    satfungs.add(item.getSatfung());
                }

                jabatans.add("PILIH JABATAN");
                for(JabatanItem item : response.body().getJabatan()){
                    jabatans.add(item.getJabatan());
                }

                adpSatfung.notifyDataSetChanged();
                adpJabatan.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ModelSatfungJabatan> call, Throwable t) {

            }
        });
    }

    private void loadProfile() {
        Call<ModelProfile> call = endpoint.getProfile(nrp);
        call.enqueue(new Callback<ModelProfile>() {
            @Override
            public void onResponse(Call<ModelProfile> call, Response<ModelProfile> response) {
                alerts.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    ProfileItem prf = response.body().getProfile().get(0);
                    petNrp.setText(prf.getNrp());
                    petNik.setText(prf.getNik());
                    petNama.setText(prf.getNama());
                    petWhatsapp.setText(prf.getWa());
                    ptvTanggalLahir.setText(prf.getTanggalLahir());

                    pangkats.clear();
                    for (PangkatItem pkt : response.body().getPangkat()){
                        pangkats.add(pkt.getJsonMemberShort());
                    }
                    adpPangkat.notifyDataSetChanged();
                    for(int i = 0; i < pangkats.size(); i++){
                        if(pangkats.get(i).equals(prf.getPangkat())){
                            pspPangkat.setSelection(i);
                        }
                    }

                    satkers.clear();
                    satkerwilayah.addAll(response.body().getSatker());
                    for (SatkerItem satker : response.body().getSatker()){
                        satkers.add(satker.getSatker());
                    }
                    adpSatker.notifyDataSetChanged();
                    for(int i = 0; i < satkers.size(); i++){
                        if(satkers.get(i).equals(prf.getSatker())){
                            pspSatker.setSelection(i);
                            currentSatker = prf.getSatker()+"xxx";
                        }
                    }

                    satfungs.clear();
                    for (SatfungItem satfung : response.body().getSatfung()){
                        satfungs.add(satfung.getSatfung());
                    }
                    adpSatfung.notifyDataSetChanged();
                    for(int i = 0; i < satfungs.size(); i++){
                        if(satfungs.get(i).equals(prf.getSatfung())){
                            pstSatfung.setSelection(i);
                        }
                    }

                    jabatans.clear();
                    for (JabatanItem jabatan : response.body().getJabatan()){
                        jabatans.add(jabatan.getJabatan());
                    }
                    adpJabatan.notifyDataSetChanged();
                    for(int i = 0; i < jabatans.size(); i++){
                        if(jabatans.get(i).equals(prf.getJabatan())){
                            pspJabatan.setSelection(i);
                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<ModelProfile> call, Throwable t) {
                alerts.dismiss();
                CustomDialog.up(
                        Profile.this,
                        "Informasi",
                        "Gagal memuat data, periksa jaringan internet anda!",
                        "MENGERTI",
                        "",
                        new CustomDialog.AlertDialogListener() {
                            @Override
                            public void onPositiveButtonClick(AlertDialog alert) {
                                alert.dismiss();
                                finish();
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