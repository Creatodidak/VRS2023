package id.creatodidak.vrspolreslandak.dashboard.humas;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.Ldkserver;
import id.creatodidak.vrspolreslandak.api.models.ServerResponse;
import id.creatodidak.vrspolreslandak.api.models.stunting.HapusItem;
import id.creatodidak.vrspolreslandak.dashboard.pedulistunting.Dokstunting;
import id.creatodidak.vrspolreslandak.helper.CustomDialog;
import id.creatodidak.vrspolreslandak.helper.DateUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublishBerita extends AppCompatActivity {

    Button ezPaste, publishNews, btPilihGambarNews;
    EditText isiNews, judulNews, hashtags, caption;
    List<String> kategoriList = Arrays.asList("PILIH KATEGORI BERITA", "PIMPINAN", "HARKAMTIBMAS", "PENEGAKAN HUKUM", "LALU LINTAS", "PEMILU DAMAI", "BHABINKAMTIBMAS", "BENCANA", "LAINNYA");
    Spinner kategori;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    String LOKASI_FILE = "VRS/BERITA";
    String LABEL_FILE = "NEWS";
    private String currentPhotoPath;
    ConstraintSet constraintSet;
    String HASIL_NAMA = "";
    Uri contentUri;
    ConstraintLayout fotos;
    LinearLayout fotosection;
    ImageView wm1, fotonya;
    private ActivityResultLauncher<Intent> galleryLauncher;
    SharedPreferences sharedPreferences;
    News endpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_berita);
        ezPaste = findViewById(R.id.ezPaste);
        publishNews = findViewById(R.id.publishNews);
        btPilihGambarNews = findViewById(R.id.btPilihGambarNews);
        isiNews = findViewById(R.id.isiNews);
        kategori = findViewById(R.id.spKategoriNews);
        ArrayAdapter<String> adpkategori = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategoriList);
        adpkategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategori.setAdapter(adpkategori);
        judulNews = findViewById(R.id.judulNews);
        hashtags = findViewById(R.id.hashtagNews);
        caption = findViewById(R.id.captionNews);
        ezPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasteFromClipboard();
            }
        });
        fotonya = findViewById(R.id.fotonya);
        wm1 = findViewById(R.id.wm1);
        btPilihGambarNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchCameraIntent();
            }
        });
        fotos = findViewById(R.id.fotoNews);
        fotosection = findViewById(R.id.fotoSection);
        constraintSet = new ConstraintSet();
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                handleGalleryResult(data);
            }
        });
        publishNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cekdata()){
                    CustomDialog.up(
                            PublishBerita.this,
                            "Konfirmasi",
                            "Anda yakin akan mempublish berita ini?",
                            "YA, LANJUTKAN",
                            "Batalkan",
                            new CustomDialog.AlertDialogListener() {
                                @Override
                                public void onPositiveButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                    publishNow();
                                }

                                @Override
                                public void onNegativeButtonClick(AlertDialog alert) {
                                    alert.dismiss();
                                }
                            },
                            true,
                            true,
                            false
                    ).show();
                }else{
                    CustomDialog.up(
                            PublishBerita.this,
                            "Peringatan",
                            "Data yang anda input belum lengkap, silahkan periksa kembali data anda!",
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
                            true,
                            false,
                            false
                    ).show();
                }
            }
        });

        sharedPreferences = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        endpoint = Ldkserver.getClient().create(News.class);
    }

    private void publishNow() {
        Bitmap bitmap = getBitmapFromView(fotos);
        AlertDialog alert = CustomDialog.up(
                PublishBerita.this,
                "Mempublish Berita...",
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
                false,
                false,
                true
        );
        alert.show();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image", "image.jpg", imageRequestBody);
        String judul = judulNews.getText().toString();
        String isi = isiNews.getText().toString();
        String captions = caption.getText().toString();
        String satker = sharedPreferences.getString("satker", "");
        String pers = sharedPreferences.getString("nrp", "");
        String kategoris = kategori.getSelectedItem().toString();
        String hashtag = hashtags.getText().toString();

        Call<ServerResponse> call = endpoint.uploadBerita(image, judul, isi, captions, satker, pers, kategoris, hashtag);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if(response.isSuccessful()){
                   alert.dismiss();
                    CustomDialog.up(
                            PublishBerita.this,
                            "Informasi",
                            response.body().getMsg(),
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
                            true,
                            false,
                            false
                    ).show();
                }else{
                    alert.dismiss();
                    CustomDialog.up(
                            PublishBerita.this,
                            "Informasi",
                            "Gagal mempublish berita, periksa jaringan anda!",
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
                            true,
                            false,
                            false
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                alert.dismiss();
                CustomDialog.up(
                        PublishBerita.this,
                        "Informasi",
                        "Gagal mempublish berita, periksa jaringan anda!",
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
                        true,
                        false,
                        false
                ).show();
            }
        });

    }

    private boolean cekdata() {
        return !TextUtils.isEmpty(judulNews.getText()) &&
                !TextUtils.isEmpty(isiNews.getText()) &&
                !TextUtils.isEmpty(caption.getText()) &&
                !TextUtils.isEmpty(hashtags.getText()) &&
                !kategori.getSelectedItem().toString().equals("PILIH KATEGORI BERITA") &&
                fotosection.getVisibility() != View.GONE;
    }

    private void dispatchCameraIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private void handleGalleryResult(Intent data) {
        if (data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    fotosection.setVisibility(View.VISIBLE);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    String imagePath = cursor.getString(column_index);

                    // Handle the image file obtained from the gallery
                    if (imagePath != null) { // Check if imagePath is not null
                        currentPhotoPath = imagePath; // Update currentPhotoPath with the selected image path

                        File imageFile = new File(imagePath);
                        Uri imageUri = Uri.fromFile(imageFile);
                        Glide.with(this)
                                .load(imageUri)
                                .into(fotonya);

                        wm1.setVisibility(View.VISIBLE);

                        btPilihGambarNews.setText("GANTI FOTO");

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(currentPhotoPath, options);
                        int width = options.outWidth;
                        int height = options.outHeight;
                        int fpb = findGreatestCommonDivisor(width, height);
                        int simplifiedWidth = width / fpb;
                        int simplifiedHeight = height / fpb;
                        String ratio = String.format(Locale.getDefault(), "%d:%d", simplifiedWidth, simplifiedHeight);
                        constraintSet.clone(fotos);
                        constraintSet.setDimensionRatio(R.id.fotonya, ratio);
                        constraintSet.applyTo(fotos);
                    } else {
                        Toast.makeText(this, "Image path is null", Toast.LENGTH_SHORT).show();
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }
    private void pasteFromClipboard() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        if (clipboardManager != null && clipboardManager.hasPrimaryClip()) {
            ClipData clipData = clipboardManager.getPrimaryClip();

            if (clipData != null && clipData.getItemCount() > 0) {
                CharSequence text = clipData.getItemAt(0).coerceToText(this);
                isiNews.setText(text);
                Toast.makeText(this, "Berhasil!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Clipboard kosong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Clipboard tidak tersedia", Toast.LENGTH_SHORT).show();
        }
    }

    private int findGreatestCommonDivisor(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
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
            PublishBerita.this.sendBroadcast(mediaScanIntent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void expandImage(String imageUrl, String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PublishBerita.this, R.style.myFullscreenAlertDialogStyle);
        LayoutInflater inflater = LayoutInflater.from(PublishBerita.this);
        View view = inflater.inflate(R.layout.dialog_image_zoom, null);

        PhotoView zoomableImageView = view.findViewById(R.id.zoomableImageView);
        TextView judul = view.findViewById(R.id.textView51);
        judul.setText(s);
        Button btn = view.findViewById(R.id.button);
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(PublishBerita.this)
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
    
}