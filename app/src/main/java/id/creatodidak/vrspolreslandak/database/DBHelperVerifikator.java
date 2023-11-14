package id.creatodidak.vrspolreslandak.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import id.creatodidak.vrspolreslandak.api.models.ListlaporancekembungItem;
import id.creatodidak.vrspolreslandak.api.models.karhutla.DatakampanyekarhutlaItem;
import id.creatodidak.vrspolreslandak.api.models.karhutla.ListHotspotItem;

public class DBHelperVerifikator extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DBVERIFIKATOR.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelperVerifikator(Context context) {
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

    public boolean isDataHotspotExist(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM datahotspot WHERE id = ?", new String[]{String.valueOf(id)});
        boolean exist = cursor.getCount() > 0;
        cursor.close();
        return exist;
    }

    public void addDataHotspot(ListHotspotItem item){
        SQLiteDatabase db = this.getWritableDatabase();
        if(isDataHotspotExist(item.getId())){
            ContentValues v = new ContentValues();
            v.put("id", item.getId());
            v.put("laths", item.getLaths());
            v.put("longhs", item.getLonghs());
            v.put("confidence", item.getConfidence());
            v.put("lokasi", item.getLokasi());
            v.put("satelit", item.getSatelit());
            v.put("satker", item.getSatker());
            v.put("kecamatan", item.getKecamatan());
            v.put("responder", item.getResponder());
            v.put("latlap", item.getLatlap());
            v.put("longlap", item.getLonglap());
            v.put("pemiliklahan", item.getPemiliklahan());
            v.put("penyebabapi", item.getPenyebabapi());
            v.put("luas", item.getLuas());
            v.put("pelaksanakegiatan", item.getPelaksanakegiatan());
            v.put("tindakan", item.getTindakan());
            v.put("analisa", item.getAnalisa());
            v.put("prediksi", item.getPrediksi());
            v.put("rekomendasi", item.getRekomendasi());
            v.put("kondisiapi", item.getKondisiapi());
            v.put("foto", item.getFoto());
            v.put("riwayat", item.getRiwayat());
            v.put("verifikasi", item.getVerifikasi());
            v.put("created_at", item.getCreatedAt());
            v.put("updated_at", item.getUpdatedAt());
            db.insert("datahotspot", null, v);
            db.close();
        }
    }

    public boolean isKampanyeExist(String dataid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM kampanye WHERE dataid = ?", new String[]{dataid});
        boolean exist = cursor.getCount() > 0;
        cursor.close();
        return exist;
    }

    public void addDataKampanye(DatakampanyekarhutlaItem item){
        SQLiteDatabase db = this.getWritableDatabase();
        if(isKampanyeExist(item.getDataid())){
            ContentValues v = new ContentValues();
            v.put("dataid", item.getDataid());
            v.put("personil", item.getPersonil());
            v.put("lokasi", item.getLokasi());
            v.put("penyampaian", item.getPenyampaian());
            v.put("analisa", item.getAnalisa());
            v.put("prediksi", item.getPrediksi());
            v.put("rekomendasi", item.getRekomendasi());
            v.put("foto", item.getFoto());
            v.put("latitude", item.getLatitude());
            v.put("longitude", item.getLongitude());
            v.put("verifikasi", item.getVerifikasi());
            v.put("created_at", item.getCreatedAt());
            v.put("updated_at", item.getUpdatedAt());
            db.insert("kampanye", null, v);
            db.close();
        }
    }

    public boolean isCekEmbungExist(String dataid){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cekembung WHERE dataid = ?", new String[]{dataid});
        boolean exist = cursor.getCount() > 0;
        cursor.close();
        return exist;
    }

    public void addDataCekEmbung(ListlaporancekembungItem item){
        SQLiteDatabase db = this.getWritableDatabase();
        if(isCekEmbungExist(item.getDataid())){
            ContentValues v = new ContentValues();
            v.put("dataid", item.getDataid());
            v.put("kodeembung", item.getKodeembung());
            v.put("latitude", item.getLatitude());
            v.put("longitude", item.getLongitude());
            v.put("geocoder", item.getGeocoder());
            v.put("pelaksana", item.getPelaksana());
            v.put("intensitasair", item.getIntensitasair());
            v.put("volumeair", item.getVolumeair());
            v.put("cuaca", item.getCuaca());
            v.put("analisa", item.getAnalisa());
            v.put("prediksi", item.getPrediksi());
            v.put("rekomendasi", item.getRekomendasi());
            v.put("personil", item.getPersonil());
            v.put("foto", item.getFoto());
            v.put("verifikasi", item.getVerifikasi());
            v.put("created_at", item.getCreatedAt());
            v.put("updated_at", item.getUpdatedAt());
            db.insert("cekembung", null, v);
        }
    }
}
