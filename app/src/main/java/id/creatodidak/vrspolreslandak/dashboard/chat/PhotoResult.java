package id.creatodidak.vrspolreslandak.dashboard.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.helper.DateUtils;
import id.creatodidak.vrspolreslandak.helper.NumberHelper;
import id.creatodidak.vrspolreslandak.helper.QRHelper;
import id.creatodidak.vrspolreslandak.helper.RandomStringGenerator;
import id.creatodidak.vrspolreslandak.helper.TextHelper;
import id.creatodidak.vrspolreslandak.helper.WaktuLokal;

public class PhotoResult extends AppCompatActivity {

    String PHOTO_PATH;
    String JUDUL;
    String KOORDINAT;
    String STORAGE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getIntent().getExtras();
        if(data != null){
            PHOTO_PATH = data.getString("photopath");
            JUDUL = data.getString("judul");
            KOORDINAT = data.getString("koordinat");
            STORAGE = data.getString("storage");
        }
        setContentView(R.layout.photoresult);
        SharedPreferences sh = getSharedPreferences("SESSION_DATA", MODE_PRIVATE);
        String nrp = sh.getString("nrp", "00000000");
        String nama = sh.getString("pangkat", "ERROR")+" "+sh.getString("nama", "DATA!");
        TextView wmNrp = findViewById(R.id.wmNRP);
        TextView wmJudul = findViewById(R.id.wmJudul);
        TextView wmPers = findViewById(R.id.wmPers);
        TextView wmLoc = findViewById(R.id.wmLoc);
        TextView wmCreatedAt = findViewById(R.id.wmCreatedAt);
        ImageView wmqr = findViewById(R.id.wmQR);
        ImageView wmImg = findViewById(R.id.wmImg);
        LinearLayout RESULT = findViewById(R.id.RESULT);
        Button btnUlangi = findViewById(R.id.btnUlangi);
        Button btnLanjutkan = findViewById(R.id.btnLanjutkan);
        ConstraintLayout bingkai = findViewById(R.id.bingkai);
        String namaFoto = RandomStringGenerator.generateRandomString(30) + ".jpg";
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(PHOTO_PATH, options);
        int width = options.outWidth;
        int height = options.outHeight;

        int fpb = NumberHelper.FPB(width, height);
        int simplifiedWidth;
        int simplifiedHeight;

        if (fpb != 0) {
            simplifiedWidth = width / fpb;
            simplifiedHeight = height / fpb;
        } else {
            simplifiedWidth = 3;
            simplifiedHeight = 4;
        }
        Glide.with(wmImg).load(PHOTO_PATH).into(wmImg);
        Glide.with(wmqr).load(QRHelper.QRLogo(this, "https://servervrs.polreslandak.id/validate/img/"+namaFoto)).into(wmqr);

        String ratio = String.format(Locale.getDefault(), "%d:%d", simplifiedWidth, simplifiedHeight);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(bingkai);
        constraintSet.setDimensionRatio(R.id.wmImg, ratio);
        constraintSet.applyTo(bingkai);

        wmNrp.setText(nrp);
        wmPers.setText(TextHelper.capitalize(nama));
        wmLoc.setText(KOORDINAT);
        wmJudul.setText(JUDUL);
        wmCreatedAt.setText(DateUtils.tanggaldaricreatedatlocal(WaktuLokal.get()));

        btnLanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap bitmap = Bitmap.createBitmap(RESULT.getWidth(), RESULT.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                RESULT.draw(canvas);

                File imageFile = new File(STORAGE, namaFoto);

                try {
                    Log.d("SAVE BITMAP", "BERHASIL");
                    FileOutputStream fos = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("gambar", imageFile.getAbsolutePath()); // Menggunakan getAbsolutePath()
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        });

        btnUlangi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                setResult(RESULT_CANCELED, resultIntent);
                finish();
            }
        });
    }
}