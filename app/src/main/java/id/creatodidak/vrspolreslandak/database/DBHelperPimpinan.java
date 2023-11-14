package id.creatodidak.vrspolreslandak.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperPimpinan extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DBPIMPINAN.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelperPimpinan(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    String tbkampanye = "CREATE TABLE IF NOT EXISTS kampanye (id INTEGER PRIMARY KEY, dataid TEXT NOT NULL, personil TEXT NOT NULL, nama_target TEXT NOT NULL DEFAULT 'BELUM ADA', lokasi TEXT NOT NULL DEFAULT 'BELUM ADA', analisa TEXT NOT NULL DEFAULT 'BELUM ADA', penyampaian TEXT NOT NULL DEFAULT 'BELUM ADA', prediksi TEXT NOT NULL DEFAULT 'BELUM ADA', rekomendasi TEXT NOT NULL DEFAULT 'BELUM ADA',  foto TEXT NOT NULL DEFAULT 'BELUM ADA', latitude double NOT NULL, longitude double NOT NULL, verifikasi TEXT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL);";
    String tbcekembung = "CREATE TABLE IF NOT EXISTS cekembung (id INTEGER PRIMARY KEY AUTOINCREMENT, dataid TEXT NOT NULL, kodeembung TEXT NOT NULL, latitude double NOT NULL, longitude double NOT NULL, geocoder TEXT NOT NULL, pelaksana TEXT NOT NULL, intensitasair TEXT NOT NULL, volumeair TEXT NOT NULL, cuaca TEXT NOT NULL, analisa TEXT NOT NULL, prediksi TEXT NOT NULL, rekomendasi TEXT NOT NULL, personil TEXT NOT NULL, foto TEXT NOT NULL, local TEXT NOT NULL, verifikasi TEXT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL);";

    String datahotspot = "CREATE TABLE IF NOT EXISTS datahotspot ( id INTEGER PRIMARY KEY, laths double NOT NULL, longhs double NOT NULL, confidence TEXT NOT NULL, lokasi TEXT NOT NULL, satelit TEXT NOT NULL, satker TEXT NOT NULL, kecamatan TEXT NOT NULL, responder TEXT NOT NULL, latlap double NOT NULL, longlap double NOT NULL, pemiliklahan TEXT NOT NULL, penyebabapi TEXT NOT NULL, luas TEXT NOT NULL, pelaksanakegiatan TEXT NOT NULL, tindakan TEXT NOT NULL, analisa TEXT NOT NULL, prediksi TEXT NOT NULL, rekomendasi TEXT NOT NULL, kondisiapi TEXT NOT NULL, foto TEXT NOT NULL, riwayat TEXT NOT NULL, verifikasi TEXT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL);";


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(tbkampanye);
        db.execSQL(tbcekembung);
        db.execSQL(datahotspot);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS kampanye");
        db.execSQL("DROP TABLE IF EXISTS hotspot");
        db.execSQL("DROP TABLE IF EXISTS draftcekhotspot");

        onCreate(db);
    }

    public void inisialisasi() {
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL(tbkampanye);
        db.execSQL(tbcekembung);
        db.execSQL(datahotspot);
    }


}
