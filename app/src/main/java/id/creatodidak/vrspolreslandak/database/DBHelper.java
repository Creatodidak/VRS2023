package id.creatodidak.vrspolreslandak.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import id.creatodidak.vrspolreslandak.api.models.DaftarhotspotItem;
import id.creatodidak.vrspolreslandak.api.models.EmbungItem;
import id.creatodidak.vrspolreslandak.api.models.ListlaporancekembungItem;
import id.creatodidak.vrspolreslandak.api.models.chat.MChatdata;
import id.creatodidak.vrspolreslandak.api.models.chat.MChatid;
import id.creatodidak.vrspolreslandak.api.models.karhutla.DatakampanyekarhutlaItem;
import id.creatodidak.vrspolreslandak.api.models.karhutla.ListHotspotItem;
import id.creatodidak.vrspolreslandak.helper.DateUtils;
import id.creatodidak.vrspolreslandak.helper.RandomStringGenerator;
import id.creatodidak.vrspolreslandak.helper.TextHelper;
import id.creatodidak.vrspolreslandak.helper.WaktuLokal;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "vrsnew.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    String tbkampanye = "CREATE TABLE IF NOT EXISTS kampanye (id INTEGER PRIMARY KEY, dataid TEXT NOT NULL, personil TEXT NOT NULL, nama_target TEXT NOT NULL DEFAULT 'BELUM ADA', lokasi TEXT NOT NULL DEFAULT 'BELUM ADA', analisa TEXT NOT NULL DEFAULT 'BELUM ADA', penyampaian TEXT NOT NULL DEFAULT 'BELUM ADA', prediksi TEXT NOT NULL DEFAULT 'BELUM ADA', rekomendasi TEXT NOT NULL DEFAULT 'BELUM ADA',  foto TEXT NOT NULL DEFAULT 'BELUM ADA', latitude double NOT NULL, longitude double NOT NULL, local TEXT NOT NULL DEFAULT 'yes', created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL);";
    String tbhotspot = "CREATE TABLE IF NOT EXISTS hotspot (id INTEGER PRIMARY KEY AUTOINCREMENT, data_id TEXT NOT NULL, latitude TEXT NOT NULL, longitude TEXT NOT NULL, confidence TEXT NOT NULL, radius TEXT NOT NULL, location TEXT NOT NULL, wilayah TEXT NOT NULL, response TEXT, status TEXT, notif TEXT, posisi TEXT, created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL);";
    String tbdraftcekhotspot = "CREATE TABLE IF NOT EXISTS draftcekhotspot (id INTEGER PRIMARY KEY AUTOINCREMENT, data_id TEXT NOT NULL, pemilik TEXT, geocoder TEXT, lokasi TEXT, penyebab TEXT, rincian TEXT, luas TEXT, kondisi TEXT, foto TEXT, posisi TEXT, created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL);";
    String tbembung = "CREATE TABLE IF NOT EXISTS petaembung ( id INTEGER  PRIMARY KEY AUTOINCREMENT, kode TEXT NOT NULL, nama TEXT NOT NULL, latitude double NOT NULL, longitude double NOT NULL, type TEXT NOT NULL, kapasitas TEXT NOT NULL, kecamatan TEXT NOT NULL, local TEXT NOT NULL, foto TEXT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL);";
    String tbcekembung = "CREATE TABLE IF NOT EXISTS cekembung (id INTEGER PRIMARY KEY AUTOINCREMENT, dataid TEXT NOT NULL, kodeembung TEXT NOT NULL, latitude double NOT NULL, longitude double NOT NULL, geocoder TEXT NOT NULL, pelaksana TEXT NOT NULL, intensitasair TEXT NOT NULL, volumeair TEXT NOT NULL, cuaca TEXT NOT NULL, analisa TEXT NOT NULL, prediksi TEXT NOT NULL, rekomendasi TEXT NOT NULL, personil TEXT NOT NULL, foto TEXT NOT NULL, local TEXT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL);";

    //    NEW
    String datahotspot = "CREATE TABLE IF NOT EXISTS datahotspot ( id INTEGER PRIMARY KEY, laths double NOT NULL, longhs double NOT NULL, confidence TEXT NOT NULL, lokasi TEXT NOT NULL, satelit TEXT NOT NULL, satker TEXT NOT NULL, kecamatan TEXT NOT NULL, responder TEXT NOT NULL, latlap double NOT NULL, longlap double NOT NULL, pemiliklahan TEXT NOT NULL, penyebabapi TEXT NOT NULL, luas TEXT NOT NULL, pelaksanakegiatan TEXT NOT NULL, tindakan TEXT NOT NULL, analisa TEXT NOT NULL, prediksi TEXT NOT NULL, rekomendasi TEXT NOT NULL, kondisiapi TEXT NOT NULL, foto TEXT NOT NULL, riwayat TEXT NOT NULL, verifikasi TEXT NOT NULL, local TEXT NOT NULL, created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL);";

    String chatid = "CREATE TABLE IF NOT EXISTS chatid ( chatid TEXT PRIMARY KEY, activity TEXT NOT NULL, dataid TEXT NOT NULL);";

    //    type chat GAMBAR, TEKS, AKSI, bot
    String chatdata = "CREATE TABLE IF NOT EXISTS chatdata ( id INTEGER PRIMARY KEY AUTOINCREMENT, chatid TEXT NOT NULL, type TEXT NOT NULL, data TEXT NOT NULL, status TEXT NOT NULL, created_at DATETIME NOT NULL);";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(tbkampanye);
        db.execSQL(tbhotspot);
        db.execSQL(tbdraftcekhotspot);
        db.execSQL(tbembung);
        db.execSQL(tbcekembung);
        db.execSQL(chatid);
        db.execSQL(chatdata);
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
        db.execSQL(tbhotspot);
        db.execSQL(tbdraftcekhotspot);
        db.execSQL(tbembung);
        db.execSQL(tbcekembung);
        db.execSQL(chatid);
        db.execSQL(chatdata);
        db.execSQL(datahotspot);
    }

    public boolean resetDb() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS kampanye");
            db.execSQL("DROP TABLE IF EXISTS hotspot");
            db.execSQL("DROP TABLE IF EXISTS draftcekhotspot");
            onCreate(db);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isTableExists(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        boolean tableExists = cursor.getCount() > 0;
        cursor.close();
        return tableExists;
    }

    //    KAMPANYE
    public boolean addDataKampanye(long dataid, String lokasi, double latitude, double longitude, String nrp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("dataid", dataid);
        values.put("personil", nrp);
        values.put("lokasi", lokasi);
        values.put("latitude", latitude);
        values.put("longitude", longitude);
        values.put("created_at", WaktuLokal.get());
        values.put("updated_at", WaktuLokal.get());

        long result = db.insert("kampanye", null, values);
        db.close();

        return result != -1;
    }

    public boolean updDataKampanye(String dataid, String kolom, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(kolom.equals("foto")){
            DatakampanyekarhutlaItem old = getDataKampanyeByDataId(dataid);
            String oldfoto = old.getFoto();
            if(oldfoto.equals("BELUM ADA")){
                values.put(kolom, data);
            }else{
                values.put(kolom, oldfoto+","+data);
            }
        }else{
            values.put(kolom, data);
        }
        values.put("updated_at", WaktuLokal.get());
        String whereClause = "dataid = ?";
        String[] whereArgs = {dataid};

        long result = db.update("kampanye", values, whereClause, whereArgs);
        db.close();

        return result > 0;
    }

    @SuppressLint("Range")
    public List<DatakampanyekarhutlaItem> getDataKampanye() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<DatakampanyekarhutlaItem> data = new ArrayList<>();
        String query = "SELECT * FROM kampanye ORDER BY created_at DESC";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null && cursor.moveToFirst()){
            do {
                DatakampanyekarhutlaItem item = new DatakampanyekarhutlaItem();
                item.setDataid(cursor.getString(cursor.getColumnIndex("dataid")));
                item.setPersonil(cursor.getString(cursor.getColumnIndex("personil")));
                item.setNamaTarget(cursor.getString(cursor.getColumnIndex("nama_target")));
                item.setLokasi(cursor.getString(cursor.getColumnIndex("lokasi")));
                item.setFoto(cursor.getString(cursor.getColumnIndex("foto")));
                item.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                item.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                item.setAnalisa(cursor.getString(cursor.getColumnIndex("analisa")));
                item.setPrediksi(cursor.getString(cursor.getColumnIndex("prediksi")));
                item.setRekomendasi(cursor.getString(cursor.getColumnIndex("rekomendasi")));
                item.setPenyampaian(cursor.getString(cursor.getColumnIndex("penyampaian")));
                item.setLocal(cursor.getString(cursor.getColumnIndex("local")).equals("yes"));
                item.setCreatedAt(cursor.getString(cursor.getColumnIndex("created_at")));
                data.add(item);
            }while (cursor.moveToNext());
            cursor.close();
        }

        return data;
    }

    @SuppressLint("Range")
    public DatakampanyekarhutlaItem getDataKampanyeByDataId(String dataid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM kampanye WHERE dataid = ?";
        String[] selectionArgs = {dataid};
        DatakampanyekarhutlaItem data = new DatakampanyekarhutlaItem();

        Cursor cursor = db.rawQuery(query, selectionArgs);
        if(cursor != null && cursor.moveToFirst()){
            do {
                data.setDataid(cursor.getString(cursor.getColumnIndex("dataid")));
                data.setPersonil(cursor.getString(cursor.getColumnIndex("personil")));
                data.setNamaTarget(cursor.getString(cursor.getColumnIndex("nama_target")));
                data.setLokasi(cursor.getString(cursor.getColumnIndex("lokasi")));
                data.setFoto(cursor.getString(cursor.getColumnIndex("foto")));
                data.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                data.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                data.setAnalisa(cursor.getString(cursor.getColumnIndex("analisa")));
                data.setPrediksi(cursor.getString(cursor.getColumnIndex("prediksi")));
                data.setRekomendasi(cursor.getString(cursor.getColumnIndex("rekomendasi")));
                data.setPenyampaian(cursor.getString(cursor.getColumnIndex("penyampaian")));
                data.setLocal(cursor.getString(cursor.getColumnIndex("local")).equals("yes"));
                data.setCreatedAt(cursor.getString(cursor.getColumnIndex("created_at")));
                data.setLocal(cursor.getString(cursor.getColumnIndex("local")).equals("yes"));
            }while (cursor.moveToNext());
            cursor.close();
        }

        return data;
    }

    public boolean delDataKampanye(String dataid) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "dataid = ?";
        String[] whereArgs = {dataid};

        int rowsDeleted = db.delete("kampanye", whereClause, whereArgs);

        return rowsDeleted > 0;
    }

    public boolean generatedLapKampanye(String extraID, String nama, String pangkat, String nrp, String jabatan) {
        DatakampanyekarhutlaItem item = getDataKampanyeByDataId(extraID);
        String newlaporan = "Izin komandan melaporkan kegiatan Kampanye Mengajak Masyarakat Bersama Mencegah Karhutla di " + TextHelper.capitalize(item.getLokasi()) + " pada " + DateUtils.tanggaldaricreatedatlocal(item.getCreatedAt()) + " dengan rincian sbb:\n\nA. Personil Pelaksana\n➤ Nama: " + TextHelper.capitalize(nama) + "\n➤ Pangkat / NRP :"+pangkat+"/"+nrp+"\n➤ Jabatan: "+TextHelper.capitalize(jabatan)+"\n\nB. Rincian Kegiatan\n➤ Koordinat: \nLat: " + item.getLatitude() + " \nLong: " + item.getLongitude() + "\n\n➤ Geocoder: \n" + item.getLokasi() + "\n\n➤ Nama Target Kampanye: \n" + item.getNamaTarget() + "\n\n➤ Penyampaian: \n" + item.getPenyampaian() + "\n\n➤ Analisa: \n" + item.getAnalisa() + "\n\n➤ Prediksi: \n" + item.getPrediksi() + "\n\n➤ Rekomendasi: \n" + item.getRekomendasi() + "\n\n\nDemikian komandan yang dapat kami laporkan beserta dokumentasi terlampir";

        if (delChatData(extraID)) {
            if (newSingleChatData(extraID, "text", "SERVER", newlaporan)) {
                String[] foto = item.getFoto().split(",");
                for (int i = 0; i < foto.length; i++) {
                    newSingleChatData(extraID, "image", "SERVER", foto[i]);
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //    HOTSPOT
    public Cursor getDataHotspot() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM hotspot WHERE DATE(created_at) = ? ORDER BY created_at DESC";
        String[] selectionArgs = {WaktuLokal.getTanggal()};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        return cursor;
    }

    public boolean updDataHotspot(String dataId, String newStatus, String newPosisi, String newVerifikasi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", newStatus);
        values.put("posisi", newPosisi);

        TimeZone timeZone = TimeZone.getTimeZone("Asia/Jakarta");
        Calendar calendar = Calendar.getInstance(timeZone);
        long currentTimeMillis = calendar.getTimeInMillis();

        String formattedAdjustedTime = DateFormat.format("yyyy-MM-dd HH:mm:ss", currentTimeMillis).toString();

        values.put("updated_at", formattedAdjustedTime);
        String whereClause = "data_id = ?";
        String[] whereArgs = {dataId};

        int rowsAffected = db.update("hotspot", values, whereClause, whereArgs);

        return rowsAffected > 0;
    }

    public void insertDaftarhotspotItem(DaftarhotspotItem newItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isDataIdExists = checkDataIdExists(newItem.getDataId());
        if (!isDataIdExists) {
            try {
                ContentValues values = new ContentValues();
                values.put("data_id", newItem.getDataId());
                values.put("latitude", newItem.getLatitude());
                values.put("longitude", newItem.getLongitude());
                values.put("confidence", newItem.getConfidence());
                values.put("radius", newItem.getRadius());
                values.put("location", newItem.getLocation());
                values.put("wilayah", newItem.getWilayah());
                values.put("response", newItem.getResponse());
                values.put("status", newItem.getStatus());
                values.put("notif", newItem.getNotif());
                values.put("posisi", newItem.getPosisi());
                values.put("created_at", WaktuLokal.get());
                values.put("updated_at", WaktuLokal.get());

                long result = db.insert("hotspot", null, values);

                if (result == -1) {
                    // Insertion failed
                    Log.d("DBHelper", "Failed to insert DaftarhotspotItem into database");
                } else {
                    // Insertion successful
                    Log.d("DBHelper", "Inserted DaftarhotspotItem into database");
                }
            } finally {
                db.close(); // Always close the database connection in a finally block
            }
        } else {
            Log.d("DBHelper", "Data_id already exists, skipping insertion");
        }
    }

    private boolean checkDataIdExists(String dataId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM hotspot WHERE data_id = ?", new String[]{dataId});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean createDataDraftCekHotspot() {
        SQLiteDatabase db = this.getWritableDatabase();

        String tbdraftcekhotspot = "CREATE TABLE IF NOT EXISTS draftcekhotspot (id INTEGER PRIMARY KEY AUTOINCREMENT, data_id TEXT NOT NULL, pemilik TEXT, geocoder TEXT, lokasi TEXT, penyebab TEXT, rincian TEXT, luas TEXT, kondisi TEXT, foto TEXT, posisi TEXT, created_at DATETIME NOT NULL, updated_at DATETIME NOT NULL);";
        db.execSQL(tbdraftcekhotspot);

        return true;
    }

    public boolean cekDataDraftCekHotspot(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM draftcekhotspot WHERE data_id = ?";
        String[] selectionArgs = {id};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        boolean dataExists = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

        return dataExists;
    }

    public Cursor getDataDraftCekHotspot(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM draftcekhotspot WHERE data_id = ?";
        String[] selectionArgs = {id};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        return cursor;
    }

    public boolean addDataDraftCekHotspot(String dataId, String pemilik, String lokasi, String geocoder, String penyebab, String rincian, String luas, String kondisi, String foto, String posisi) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("data_id", dataId);
        values.put("pemilik", pemilik);
        values.put("lokasi", lokasi);
        values.put("geocoder", geocoder);
        values.put("penyebab", penyebab);
        values.put("rincian", rincian);
        values.put("luas", luas);
        values.put("kondisi", kondisi);
        values.put("foto", foto);
        values.put("posisi", posisi);
        values.put("created_at", WaktuLokal.get());
        values.put("updated_at", WaktuLokal.get());

        long result = db.insert("draftcekhotspot", null, values);
        return result != -1;
    }

    public boolean updDataDraft(String dataId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("posisi", "SERVER");
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Jakarta");
        Calendar calendar = Calendar.getInstance(timeZone);
        long currentTimeMillis = calendar.getTimeInMillis();

        String formattedAdjustedTime = DateFormat.format("yyyy-MM-dd HH:mm:ss", currentTimeMillis).toString();

        values.put("updated_at", formattedAdjustedTime);

        String whereClause = "data_id = ?";
        String[] whereArgs = {dataId};

        int rowsAffected = db.update("draftcekhotspot", values, whereClause, whereArgs);

        return rowsAffected > 0;
    }

    public Cursor getDataCekhotspot() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT draftcekhotspot.*, hotspot.wilayah, hotspot.latitude, hotspot.longitude, hotspot.created_at AS terdeteksi " +
                "FROM hotspot " +
                "JOIN draftcekhotspot ON draftcekhotspot.data_id = hotspot.data_id " +
                "WHERE DATE(hotspot.created_at) = ?" +
                "ORDER BY draftcekhotspot.created_at DESC";

        String[] selectionArgs = {WaktuLokal.getTanggal()};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        return cursor;
    }

    public Cursor getSingleDataCekhotspot(String data_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT draftcekhotspot.*, hotspot.wilayah, hotspot.latitude, hotspot.longitude, hotspot.created_at AS terdeteksi " +
                "FROM hotspot " +
                "JOIN draftcekhotspot ON draftcekhotspot.data_id = hotspot.data_id " +
                "WHERE hotspot.data_id = ?";

        String[] selectionArgs = {data_id};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        return cursor;
    }

    public boolean updDataDraftExist(String dataid, String pemilik, String luas, String penyebab, String kondisi, String rincian) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("pemilik", pemilik);
        values.put("luas", luas);
        values.put("penyebab", penyebab);
        values.put("kondisi", kondisi);
        values.put("rincian", rincian);
        values.put("created_at", WaktuLokal.get());
        values.put("updated_at", WaktuLokal.get());

        int rowsAffected = db.update("draftcekhotspot", values, "data_id = ?", new String[]{dataid});

        db.close();

        return rowsAffected > 0;
    }

    public Cursor getAllHotSpot(String responder, String tanggal) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM datahotspot WHERE responder = ? AND DATE(created_at) = ? ORDER BY updated_at DESC";
        String[] selectionArgs = {responder, tanggal};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        return cursor;
    }

    public Cursor getListHotSpot(String responder) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM datahotspot WHERE responder = ? ORDER BY updated_at DESC";
        String[] selectionArgs = {responder};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        return cursor;
    }

    public boolean addAllHotspot(int id, double laths, double longhs, String confidence, String lokasi, String satelit, String satker, String kecamatan, String responder, double latlap, double longlap, String pemiliklahan, String penyebabapi, String luas, String pelaksanakegiatan, String tindakan, String analisa, String prediksi, String rekomendasi, String kondisiapi, String foto, String riwayat, String verifikasi, String local, String created_at, String updated_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("laths", laths);
        values.put("longhs", longhs);
        values.put("confidence", confidence);
        values.put("lokasi", lokasi);
        values.put("satelit", satelit);
        values.put("satker", satker);
        values.put("kecamatan", kecamatan);
        values.put("responder", responder);
        values.put("latlap", latlap);
        values.put("longlap", longlap);
        values.put("pemiliklahan", pemiliklahan);
        values.put("penyebabapi", penyebabapi);
        values.put("luas", luas);
        values.put("pelaksanakegiatan", pelaksanakegiatan);
        values.put("tindakan", tindakan);
        values.put("analisa", analisa);
        values.put("prediksi", prediksi);
        values.put("rekomendasi", rekomendasi);
        values.put("kondisiapi", kondisiapi);
        values.put("foto", foto);
        values.put("riwayat", riwayat);
        values.put("verifikasi", verifikasi);
        values.put("local", local);
        values.put("created_at", created_at);
        values.put("updated_at", updated_at);
        long result = db.insert("datahotspot", null, values);
        return result != -1;
    }

    public boolean isHotspotExist(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM datahotspot WHERE id = ?", new String[]{String.valueOf(id)});

        boolean found = cursor.moveToFirst();

        cursor.close();
        db.close();

        return found;
    }

    @SuppressLint("Range")
    public ListHotspotItem getDataHotspotById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ListHotspotItem list = null;

        Cursor cursor = db.rawQuery("SELECT * FROM datahotspot WHERE id = ? ", new String[]{id});
        if (cursor != null && cursor.moveToFirst()) {
            list = new ListHotspotItem();
            list.setLatlap(cursor.getDouble(cursor.getColumnIndex("latlap")));
            list.setLonglap(cursor.getDouble(cursor.getColumnIndex("longlap")));
            list.setPemiliklahan(cursor.getString(cursor.getColumnIndex("pemiliklahan")));
            list.setPenyebabapi(cursor.getString(cursor.getColumnIndex("penyebabapi")));
            list.setLuas(cursor.getString(cursor.getColumnIndex("luas")));
            list.setPelaksanakegiatan(cursor.getString(cursor.getColumnIndex("pelaksanakegiatan")));
            list.setTindakan(cursor.getString(cursor.getColumnIndex("tindakan")));
            list.setAnalisa(cursor.getString(cursor.getColumnIndex("analisa")));
            list.setPrediksi(cursor.getString(cursor.getColumnIndex("prediksi")));
            list.setKondisiapi(cursor.getString(cursor.getColumnIndex("kondisiapi")));
            list.setFoto(cursor.getString(cursor.getColumnIndex("foto")));
            list.setRekomendasi(cursor.getString(cursor.getColumnIndex("rekomendasi")));
            list.setVerifikasi(cursor.getString(cursor.getColumnIndex("verifikasi")));
            cursor.close();
        }

        return list;
    }

    public boolean updHotspotData(int id, String kolom, String data, String riwayat) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM datahotspot WHERE id =?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String rwyt = cursor.getString(cursor.getColumnIndex("riwayat"));
            @SuppressLint("Range") String ft = cursor.getString(cursor.getColumnIndex("foto"));
            ContentValues values = new ContentValues();
            if (kolom.equals("foto")) {
                if (!ft.equals("BELUM ADA")) {
                    values.put(kolom, ft + "," + data);
                } else {
                    values.put(kolom, data);
                }
            } else {
                values.put(kolom, data);
            }
            values.put("updated_at", WaktuLokal.get());
            values.put("riwayat", rwyt + "," + riwayat);
            int rowsAffected = db.update("datahotspot", values, "id = ?", new String[]{String.valueOf(id)});
            db.close();

            return rowsAffected > 0;
        } else {
            return false;
        }
    }


    public boolean delDataHotspot(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};

        int rowsDeleted = db.delete("datahotspot", whereClause, whereArgs);

        return rowsDeleted > 0;
    }


    @SuppressLint("Range")
    public LatLng getKoordinatHotspot(int extraID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM datahotspot WHERE id = ?", new String[]{String.valueOf(extraID)});

        if (cursor != null && cursor.moveToFirst()) {
            double latitude = cursor.getDouble(cursor.getColumnIndex("laths"));
            double longitude = cursor.getDouble(cursor.getColumnIndex("longhs"));
            cursor.close();
            return new LatLng(latitude, longitude);
        }
        return null;
    }

    @SuppressLint("Range")
    public boolean generatedLapCekHotspot(int id, String chatid, String nama, String pangkatnrp, String jabatan, String newverifikasi, String newriwayat) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM datahotspot WHERE id = ?", new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            String ids = String.valueOf(cursor.getInt(cursor.getColumnIndex("id")));
            String laths = String.valueOf(cursor.getDouble(cursor.getColumnIndex("laths")));
            String longhs = String.valueOf(cursor.getDouble(cursor.getColumnIndex("longhs")));
            String confidence = cursor.getString(cursor.getColumnIndex("confidence"));
            String lokasi = cursor.getString(cursor.getColumnIndex("lokasi"));
            String satelit = cursor.getString(cursor.getColumnIndex("satelit"));
            String satker = cursor.getString(cursor.getColumnIndex("satker"));
            String kecamatan = cursor.getString(cursor.getColumnIndex("kecamatan"));
            String latlap = String.valueOf(cursor.getDouble(cursor.getColumnIndex("latlap")));
            String longlap = String.valueOf(cursor.getDouble(cursor.getColumnIndex("longlap")));
            String pemiliklahan = cursor.getString(cursor.getColumnIndex("pemiliklahan"));
            String penyebabapi = cursor.getString(cursor.getColumnIndex("penyebabapi"));
            String luas = cursor.getString(cursor.getColumnIndex("luas"));
            String pelaksanakegiatan = cursor.getString(cursor.getColumnIndex("pelaksanakegiatan"));
            String tindakan = cursor.getString(cursor.getColumnIndex("tindakan"));
            String analisa = cursor.getString(cursor.getColumnIndex("analisa"));
            String prediksi = cursor.getString(cursor.getColumnIndex("prediksi"));
            String rekomendasi = cursor.getString(cursor.getColumnIndex("rekomendasi"));
            String kondisiapi = cursor.getString(cursor.getColumnIndex("kondisiapi"));
            String foto = cursor.getString(cursor.getColumnIndex("foto"));

            String bulls = "";

            if (confidence.equals("HIGH")) {
                bulls = "\uD83D\uDD34";
            } else if (confidence.equals("MEDIUM")) {
                bulls = "\uD83D\uDFE1";
            } else if (confidence.equals("LOW")) {
                bulls = "\uD83D\uDFE2";
            }

            String newlaporan = "Izin komandan melaporkan kegiatan Pengecekan Hotspot disekitar " + lokasi + " dengan rincian sbb:\n\nA. Profile Titik Api\n\uD83C\uDD94 ID: " + ids + "\n\uD83E\uDDED Latitude: " + laths + "\n\uD83E\uDDED Longitude: " + longhs + "\n\uD83D\uDCE1 Satelite: " + satelit + "\n" + bulls + " Confidence: " + TextHelper.capitalize(confidence) + "\n\uD83D\uDCCD Wilkum: " + TextHelper.capitalize(satker) + "\n\uD83D\uDCCD Kecamatan: " + TextHelper.capitalize(kecamatan) + "\n\nB. Profile Responder\n➤ Nama: " + nama + "\n➤ Pangkat / Nrp: " + pangkatnrp + "\n➤ Jabatan: " + jabatan + "\n\nC. Hasil Pengecekan dilapangan:\n\uD83E\uDDED Real Latitude: " + latlap + "\n\uD83E\uDDED Real Longitude: " + longlap + "\n\n➤ Nama Pemilik Lahan: " + pemiliklahan + "\n\n➤ Penyebab api: " + penyebabapi + "\n\n➤ Luas lahan terbakar: ±" + luas + " Hektar\n\n➤ Pelaksana Kegiatan:\n" + pelaksanakegiatan + "\n\n➤ Tindakan di lapangan:\n" + tindakan + "\n\n➤ Analisa:\n\n" + analisa + "\n➤ Prediksi:\n" + prediksi + "\n\n➤ Rekomendasi:\n" + rekomendasi + "\n\n➤ Kondisi api:\nSaat laporan ini dibuat kondisi api " + kondisiapi + "\n\n\nDemikian komandan yang dapat kami laporkan beserta dokumentasi terlampir";

            String[] newfoto = foto.split(",");
            Log.e("GENERATED", "JUMLAH FOTO: " + newfoto.length);

            if (updHotspotData(id, "verifikasi", newverifikasi, newriwayat)) {

                if (delChatData(chatid)) {
                    if (newSingleChatData(chatid, "text", "SERVER", newlaporan)) {
                        Log.e("GENERATED", "Berhasil generate Text");
                        for (int i = 0; i < newfoto.length; i++) {
                            if (newSingleChatData(chatid, "image", "SERVER", newfoto[i])) {
                                Log.e("GENERATED", "Foto " + i + " berhasil");
                            } else {
                                Log.e("GENERATED", "Foto " + i + " gagal");
                            }
                        }
                    } else {
                        Log.e("GENERATED", "Gagal generate Text");
                    }

                    newSingleChatData(chatid, "bot", "SERVER", "Status verifikasi: \n" + newverifikasi);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    //    EMBUNG
    public boolean addEmbung(String nama, double latitude, double longitude, String type, String kapasitas, String kecamatan, String kode, String local, String foto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nama", nama);
        values.put("latitude", latitude);
        values.put("longitude", longitude);
        values.put("type", type);
        values.put("kapasitas", kapasitas);
        values.put("kecamatan", kecamatan);
        values.put("local", local);
        values.put("foto", foto);
        values.put("created_at", WaktuLokal.get());
        values.put("updated_at", WaktuLokal.get());
        values.put("kode", kode);

        long result = db.insert("petaembung", null, values);
        db.close();
        return result != -1;
    }

    public void addEmbungs(EmbungItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nama", item.getNama());
        values.put("latitude", (Double) item.getLatitude());
        values.put("longitude", (Double) item.getLongitude());
        values.put("type", item.getType());
        values.put("kapasitas", item.getKapasitas());
        values.put("kecamatan", item.getKecamatan());
        values.put("local", "no");
        values.put("foto", item.getFoto());
        values.put("created_at", WaktuLokal.get());
        values.put("updated_at", WaktuLokal.get());
        values.put("kode", item.getKode());

        db.insert("petaembung", null, values);
        Log.i("ADD EMBUNG", "OK");
        db.close();
    }

    public boolean updEmbung(String kode, String foto) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("local", "no");
        values.put("foto", foto);
        values.put("updated_at", WaktuLokal.get());

        int rowsAffected = db.update("petaembung", values, "kode = ?", new String[]{String.valueOf(kode)});

        db.close();

        return rowsAffected > 0;
    }

    public boolean isEmbung(String kode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM petaembung WHERE kode = ?", new String[]{kode});

        boolean found = cursor.moveToFirst();

        cursor.close();
        db.close();

        return found;
    }

    public Cursor getAllEmbung() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM petaembung ORDER BY created_at DESC";

        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public boolean addDataCekEmbung(String dataid, String kodeembung, double latitude, double longitude, String geocoder, String pelaksana, String intensitasair, String volumeair, String cuaca, String analisa, String prediksi, String rekomendasi, String personil, String foto, String local, String created_at, String updated_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("dataid", dataid);
        values.put("kodeembung", kodeembung);
        values.put("latitude", latitude);
        values.put("longitude", longitude);
        values.put("geocoder", geocoder);
        values.put("pelaksana", pelaksana);
        values.put("intensitasair", intensitasair);
        values.put("volumeair", volumeair);
        values.put("cuaca", cuaca);
        values.put("analisa", analisa);
        values.put("prediksi", prediksi);
        values.put("rekomendasi", rekomendasi);
        values.put("personil", personil);
        values.put("foto", foto);
        values.put("local", local);
        if (local.equals("yes")) {
            values.put("created_at", WaktuLokal.get());
            values.put("updated_at", WaktuLokal.get());
        } else {
            values.put("created_at", created_at);
            values.put("updated_at", updated_at);
        }

        long result = db.insert("cekembung", null, values);
        return result != -1;
    }

    @SuppressLint("Range")
    public EmbungItem embungbyid(String dataid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM petaembung WHERE kode = ?";
        String[] selectionArgs = {dataid};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        EmbungItem item = new EmbungItem();

        if (cursor != null && cursor.moveToFirst()) {
            item.setKode(cursor.getString(cursor.getColumnIndex("kode")));
            item.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
            item.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
            item.setNama(cursor.getString(cursor.getColumnIndex("nama")));
            item.setType(cursor.getString(cursor.getColumnIndex("type")));
            item.setKapasitas(cursor.getString(cursor.getColumnIndex("kapasitas")));
            item.setKecamatan(cursor.getString(cursor.getColumnIndex("kecamatan")));
            item.setFoto(cursor.getString(cursor.getColumnIndex("foto")));
        }

        return item;
    }

    @SuppressLint("Range")
    public ListlaporancekembungItem getCekEmbungByDataId(String dataid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM cekembung WHERE dataid = ?";
        String[] selectionArgs = {dataid};
        ListlaporancekembungItem item = new ListlaporancekembungItem();

        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor != null && cursor.moveToFirst()) {
            item.setDataid(cursor.getString(cursor.getColumnIndex("dataid")));
            item.setKodeembung(cursor.getString(cursor.getColumnIndex("kodeembung")));
            item.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
            item.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
            item.setGeocoder(cursor.getString(cursor.getColumnIndex("geocoder")));
            item.setPelaksana(cursor.getString(cursor.getColumnIndex("pelaksana")));
            item.setIntensitasair(cursor.getString(cursor.getColumnIndex("intensitasair")));
            item.setVolumeair(cursor.getString(cursor.getColumnIndex("volumeair")));
            item.setCuaca(cursor.getString(cursor.getColumnIndex("cuaca")));
            item.setAnalisa(cursor.getString(cursor.getColumnIndex("analisa")));
            item.setPrediksi(cursor.getString(cursor.getColumnIndex("prediksi")));
            item.setRekomendasi(cursor.getString(cursor.getColumnIndex("rekomendasi")));
            item.setPersonil(cursor.getString(cursor.getColumnIndex("personil")));
            item.setFoto(cursor.getString(cursor.getColumnIndex("foto")));
            item.setCreatedAt(cursor.getString(cursor.getColumnIndex("created_at")));
            item.setUpdatedAt(cursor.getString(cursor.getColumnIndex("created_at")));
            item.setLocal(cursor.getString(cursor.getColumnIndex("local")).equals("yes"));
        }

        return item;
    }

    public boolean getCekLocalCekEmbung(String dataid, String local) {
        SQLiteDatabase db = this.getReadableDatabase();

        // First, check if there's a matching dataid in the cekembung table
        String query = "SELECT * FROM cekembung WHERE dataid = ?";
        String[] selectionArgs = {dataid};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor != null && cursor.getCount() > 0) {
            // There's a matching dataid, now check if there's a matching local
            query = "SELECT * FROM cekembung WHERE dataid = ? AND local = ?";
            selectionArgs = new String[]{dataid, local};
            cursor = db.rawQuery(query, selectionArgs);

            // If there's a matching local, return false; otherwise, return true
            return cursor == null || cursor.getCount() == 0;
        }

        // If no matching dataid found, return true
        return true;
    }

    @SuppressLint("Range")
    public List<ListlaporancekembungItem> getAllCekEmbung() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ListlaporancekembungItem> data = new ArrayList<>();

        String query = "SELECT * FROM cekembung ORDER BY created_at DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ListlaporancekembungItem item = new ListlaporancekembungItem();
                item.setDataid(cursor.getString(cursor.getColumnIndex("dataid")));
                item.setKodeembung(cursor.getString(cursor.getColumnIndex("kodeembung")));
                item.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                item.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
                item.setGeocoder(cursor.getString(cursor.getColumnIndex("geocoder")));
                item.setPelaksana(cursor.getString(cursor.getColumnIndex("pelaksana")));
                item.setIntensitasair(cursor.getString(cursor.getColumnIndex("intensitasair")));
                item.setVolumeair(cursor.getString(cursor.getColumnIndex("volumeair")));
                item.setCuaca(cursor.getString(cursor.getColumnIndex("cuaca")));
                item.setAnalisa(cursor.getString(cursor.getColumnIndex("analisa")));
                item.setPrediksi(cursor.getString(cursor.getColumnIndex("prediksi")));
                item.setRekomendasi(cursor.getString(cursor.getColumnIndex("rekomendasi")));
                item.setPersonil(cursor.getString(cursor.getColumnIndex("personil")));
                item.setFoto(cursor.getString(cursor.getColumnIndex("foto")));
                item.setCreatedAt(cursor.getString(cursor.getColumnIndex("created_at")));
                item.setUpdatedAt(cursor.getString(cursor.getColumnIndex("created_at")));
                item.setLocal(cursor.getString(cursor.getColumnIndex("local")).equals("yes"));
                data.add(item);
            } while (cursor.moveToNext());
            cursor.close();
        }
        Log.i("JUMLAH DATA", String.valueOf(data.size()));
        return data;
    }

    public boolean updCekEmbung(String dataid, String kolom, String data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (kolom.equals("foto")) {
            ListlaporancekembungItem item = getCekEmbungByDataId(dataid);
            if (item.getFoto().equals("BELUM ADA")) {
                values.put(kolom, data);
            } else {
                values.put(kolom, item.getFoto() + "," + data);
            }
        } else {
            values.put(kolom, data);
        }
        values.put("updated_at", WaktuLokal.get());

        int rowsAffected = db.update("cekembung", values, "dataid = ?", new String[]{dataid});

        db.close();

        return rowsAffected > 0;
    }

    public boolean generatedLapCekEmbung(String chatid) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (delChatData(chatid)) {
            ListlaporancekembungItem item = getCekEmbungByDataId(chatid);
            EmbungItem emb = embungbyid(item.getKodeembung());

            String newlaporan = "Izin komandan melaporkan kegiatan Pengecekan " + TextHelper.capitalize(emb.getNama()) + " (" + TextHelper.capitalize(emb.getType()) + ") pada " + DateUtils.tanggaldaricreatedatlocal(item.getCreatedAt()) + " dengan rincian sbb:\n\nA. Profile Embung Air\n➤ Nama: " + TextHelper.capitalize(emb.getNama()) + "\n➤ Koordinat: \nLat: " + emb.getLatitude() + " \nLong: " + emb.getLongitude() + "\n➤ Kapasitas: ±" + emb.getKapasitas() + " m³\n➤ Type: " + TextHelper.capitalize(emb.getType()) + "\n➤ Kecamatan: " + TextHelper.capitalize(emb.getKecamatan()) + "\n\nB. Rincian Kegiatan\n➤ Koordinat: \nLat: " + item.getLatitude() + " \nLong: " + item.getLongitude() + "\n\n➤ Geocoder: \n" + item.getGeocoder() + "\n\n➤ Intensitas Air: \nMenurut hasil pantauan dilapangan, saat laporan ini dibuat Intensitas Air dianggap " + item.getIntensitasair() + ", apabila sewaktu-waktu digunakan untuk memadamkan titik api\n\n➤ Volume Air: ±" + item.getVolumeair() + " m³\n\n➤ Pelaksana Kegiatan: \n" + item.getPelaksana() + "\n\n➤ Cuaca: \n" + item.getCuaca() + "\n\n➤ Analisa: \n" + item.getAnalisa() + "\n\n➤ Prediksi: \n" + item.getPrediksi() + "\n\n➤ Rekomendasi: \n" + item.getRekomendasi() + "\n\n\nDemikian komandan yang dapat kami laporkan beserta dokumentasi terlampir";

            if (newSingleChatData(chatid, "text", "SERVER", newlaporan)) {
                String[] foto = item.getFoto().split(",");
                for (int i = 0; i < foto.length; i++) {
                    newSingleChatData(chatid, "image", "SERVER", foto[i]);
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //    CHAT
    public boolean cekchatid(int dataid, String activity) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM chatid WHERE dataid = ? AND activity = ?", new String[]{String.valueOf(dataid), activity});

        boolean found = cursor.moveToFirst();

        cursor.close();
        db.close();

        return found;
    }

    public boolean isChatExist(String chatid, String data) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM chatdata WHERE chatid = ? AND data = ?", new String[]{chatid, data});

        boolean found = cursor.moveToFirst();

        cursor.close();
        db.close();

        return found;
    }

    public boolean registerChatid(long id, String activity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("chatid", RandomStringGenerator.generateRandomString(30));
        values.put("dataid", String.valueOf(id));
        values.put("activity", activity);
        long result = db.insert("chatid", null, values);
        return result != -1;
    }

    @SuppressLint("Range")
    public String getChatId(String dataid, String activity) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT chatid FROM chatid WHERE dataid = ? AND activity = ?",
                new String[]{dataid, activity});

        String chatId = "NIHIL";

        if (cursor.moveToFirst()) {
            chatId = cursor.getString(cursor.getColumnIndex("chatid"));
        }

        cursor.close();
        db.close();

        return chatId;
    }

    @SuppressLint("Range")
    public String getActChatId(String dataid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT activity FROM chatid WHERE dataid = ?",
                new String[]{String.valueOf(dataid)});

        String chatId = "NIHIL";

        if (cursor.moveToFirst()) {
            chatId = cursor.getString(cursor.getColumnIndex("activity"));
        }

        cursor.close();
        db.close();

        return chatId;
    }

    @SuppressLint("Range")
    public List<MChatdata> getChatData(String chatid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM chatdata WHERE chatid = ? ORDER BY created_at ASC", new String[]{chatid});
        List<MChatdata> chatdata = new ArrayList<>();

        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String chatids = cursor.getString(cursor.getColumnIndex("chatid"));
                String data = cursor.getString(cursor.getColumnIndex("data"));
                String status = cursor.getString(cursor.getColumnIndex("status"));
                String createdAt = cursor.getString(cursor.getColumnIndex("created_at"));

                MChatdata chat = new MChatdata();
                chat.setId(id);
                chat.setChatId(chatids);
                chat.setType(type);
                chat.setData(data);
                chat.setStatus(status);
                chat.setCreatedAt(createdAt);

                chatdata.add(chat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }

        return chatdata;
    }

    @SuppressLint("Range")
    public List<MChatid> getChatIDbyActivity(String activity) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM chatid WHERE activity LIKE ?", new String[]{"%" + activity + "%"});
        List<MChatid> list = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                MChatid data = new MChatid(
                        cursor.getString(cursor.getColumnIndex("chatid")),
                        cursor.getString(cursor.getColumnIndex("activity")),
                        cursor.getString(cursor.getColumnIndex("dataid"))
                );

                list.add(data);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return list;
    }

    public boolean newSingleChatData(String chatid, String type, String status, String data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("created_at", WaktuLokal.get());
        values.put("chatid", chatid);
        values.put("type", type);
        values.put("status", status);
        values.put("data", data);
        long result = db.insert("chatdata", null, values);
        return result != -1;
    }

    public boolean newChatData(List<MChatdata> newchat) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = true;

        try {
            for (MChatdata item : newchat) {
                ContentValues values = new ContentValues();
                values.put("created_at", item.getCreatedAt());
                values.put("chatid", item.getChatId());
                values.put("type", item.getType());
                values.put("status", item.getStatus());
                values.put("data", item.getData());
                long result = db.insert("chatdata", null, values);

                if (result == -1) {
                    success = false;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        } finally {
            db.close();
        }

        return success;
    }

    public boolean updChatData(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);

        int rowsAffected = db.update("chatdata", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();

        return rowsAffected > 0;
    }

    public boolean delChatData(String chatid) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "chatid = ?";
        String[] whereArgs = {chatid};

        int rowsDeleted = db.delete("chatdata", whereClause, whereArgs);
        return rowsDeleted > 0;
    }

}