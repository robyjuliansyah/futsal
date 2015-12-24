package com.tantan.depootsee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseManager {
    private static final String ROW_NOMOR = "nomor";
    private static final String ROW_NAMA = "nama";
    private static final String ROW_JENIS = "jenis";
    private static final String ROW_KEGIATAN = "kegiatan";
    private static String ROW_TANGGAL = "tanggal";
    private static final String ROW_UANG = "uang";
    private static final String NAMA_DB = "DB";
    private static final String NAMATABELPEMAIN = "pemain";
    private static final String NAMATABELKAS = "kas";
    private static final String NAMATABELLAPORAN = "laporan";
    private static final int DB_VERSION = 1;
    private static final String CREATETABLEPEMAIN = "create table "
            + NAMATABELPEMAIN + " (" + ROW_NOMOR + " integer PRIMARY KEY,"
            + ROW_NAMA + " text)";
    private static final String CREATETABLELAPORAN = "create table "
            + NAMATABELLAPORAN + " (" + ROW_TANGGAL + " date," + ROW_NAMA
            + " text)";
    private static final String CREATETABLEKAS = "create table " + NAMATABELKAS
            + " (" + ROW_TANGGAL + " date, " + ROW_UANG + " integer,"
            + ROW_KEGIATAN + " text," + ROW_JENIS + " text)";
    private final Context context;
    private DatabaseOpenHelper dbHelper;
    private SQLiteDatabase db;

    public DatabaseManager(Context ctx) {
        this.context = ctx;
        dbHelper = new DatabaseOpenHelper(ctx);
        db = dbHelper.getWritableDatabase();
    }

    private static class DatabaseOpenHelper extends SQLiteOpenHelper {
        public DatabaseOpenHelper(Context context) {
            super(context, NAMA_DB, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATETABLEPEMAIN);
            db.execSQL(CREATETABLELAPORAN);
            db.execSQL(CREATETABLEKAS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
            db.execSQL("DROP TABLE IF EXISTS " + NAMATABELPEMAIN);
            db.execSQL("DROP TABLE IF EXISTS " + NAMATABELKAS);
            db.execSQL("DROP TABLE IF EXISTS " + NAMATABELLAPORAN);
            onCreate(db);
        }
    }

    public void close() {
        dbHelper.close();
    }

    public void addRowPemain(String nomor, String nama) {
        ContentValues values = new ContentValues();
        values.put(ROW_NOMOR, nomor);
        values.put(ROW_NAMA, nama);
        try {
            db.insert(NAMATABELPEMAIN, null, values);
        } catch (Exception e) {
            Log.e("Insert Pemain Error", e.toString());
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Object>> ambilSemuaBarisPemain() {
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
        Cursor cur;
        try {
            cur = db.query(NAMATABELPEMAIN,
                    new String[] { ROW_NOMOR, ROW_NAMA }, null, null, null,
                    null, null);
            cur.moveToFirst();
            if (!cur.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();
                    dataList.add(cur.getInt(0));
                    dataList.add(cur.getString(1));
                    dataArray.add(dataList);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Load Pemain Error", e.toString());
            Toast.makeText(context,
                    "gagal ambil semua baris pemain" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
        return dataArray;
    }

    public List<Pemain> getAllPemain() {
        List<Pemain> daftarpemain = new ArrayList<Pemain>();

        Cursor cursor = db.query(NAMATABELPEMAIN, new String[] { ROW_NOMOR,
                ROW_NOMOR, ROW_NAMA }, null, null, null, null, ROW_NAMA);
        if (cursor.getCount() >= 1) {
            cursor.moveToFirst();

            do {
                Pemain p = new Pemain();
                p.setnomor(cursor.getString(cursor
                        .getColumnIndexOrThrow(ROW_NOMOR)));
                p.setnama(cursor.getString(cursor
                        .getColumnIndexOrThrow(ROW_NAMA)));
                daftarpemain.add(p);

            } while (cursor.moveToNext());
        }
        return daftarpemain;
    }

    public int getJumlahPemain() {
        int hasil = 0;
        Cursor cursor = db.query(NAMATABELPEMAIN, new String[] { ROW_NOMOR,
                ROW_NOMOR, ROW_NAMA }, null, null, null, null, ROW_NAMA);
        if (cursor.getCount() >= 1) {
            hasil = cursor.getCount();
        }
        return hasil;
    }

    public void deleteBarisPemain(long idBaris) {
        try {
            db.delete(NAMATABELPEMAIN, ROW_NOMOR + "=" + idBaris, null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Delete Pemain Error", e.toString());
        }
    }

    public void addRowKas(String tanggal, int uang, String kegiatan,
                          String jenis) {
        ContentValues values = new ContentValues();
        values.put(ROW_TANGGAL, tanggal);
        values.put(ROW_UANG, uang);
        values.put(ROW_KEGIATAN, kegiatan);
        values.put(ROW_JENIS, jenis);
        try {
            db.insert(NAMATABELKAS, null, values);
        } catch (Exception e) {
            Log.e("Insert Kas Error", e.toString());
            e.printStackTrace();
        }
    }

    public int ambilKas() {
        int hasil = 0;
        Cursor cur;
        try {
            cur = db.rawQuery("select sum (" + ROW_UANG + ") from "
                    + NAMATABELKAS, null);
            cur.moveToFirst();
            if (!cur.isAfterLast()) {
                hasil = cur.getInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Load Kas Error", e.toString());
            Toast.makeText(context, "gagal ambil jumlah kas" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
        return hasil;
    }

    public void ClearKas() {
        try {
            db.delete(NAMATABELKAS, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Clear Kas Error", e.toString());
        }
    }

    public void addRowLaporan(String tanggal, String nama) {
        ContentValues values = new ContentValues();
        values.put(ROW_TANGGAL, tanggal);
        values.put(ROW_NAMA, nama);
        try {
            db.insert(NAMATABELLAPORAN, null, values);
        } catch (Exception e) {
            Log.e("Insert Laporan Error", e.toString());
            e.printStackTrace();
        }
    }

    public String[] tanggalLaporan() {
        String[] hasil = new String[2];
        Cursor tgl;
        try {
            tgl = db.query(true, NAMATABELLAPORAN,
                    new String[] { ROW_TANGGAL }, null, null, null, null, null,
                    null);
            tgl.moveToFirst();
            hasil = new String[tgl.getCount()];
            int i = 0;
            if (!tgl.isAfterLast()) {
                do {
                    hasil[i] = tgl.getString(0);
                    i++;
                } while (tgl.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Load Tanggal Error", e.toString());
            Toast.makeText(context,
                    "gagal ambil semua baris Laporan" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
        return hasil;
    }

    public String[][] ambilSemuaBarisLaporan() {
        String[] tanggal = tanggalLaporan();
        String[][] hasil = new String[2][2];
        hasil = new String[tanggal.length][getJumlahPemain()];
        Cursor cur;
        try {
            for (int i = 0; i < tanggal.length; i++) {
                cur = db.rawQuery("select * from " + NAMATABELLAPORAN
                                + " where " + ROW_TANGGAL + " = '" + tanggal[i] + "'",
                        null);
                cur.moveToFirst();
                int x = 0;
                if (!cur.isAfterLast()) {
                    do {
                        hasil[i][x] = cur.getString(1);
                        // System.out.println(i+" "+x+" "+hasil[i][x]+" "+cur.getString(1));
                        x++;
                    } while (cur.moveToNext());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Load Laporan Error", e.toString());
            Toast.makeText(context,
                    "gagal ambil semua baris Laporan" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
        return hasil;
    }

    public boolean cekfutsal() {
        Date current = new Date();
        SimpleDateFormat frmt = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = frmt.format(current);
        boolean x = false;
        Cursor cur;
        try {
            cur = db.rawQuery("select * from " + NAMATABELLAPORAN + " where "
                    + ROW_TANGGAL + " = '" + dateString + "'", null);
            cur.moveToFirst();
            if (!cur.isAfterLast()) {
                x = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Cek Futsal Error", e.toString());
            Toast.makeText(context,
                    "gagal ambil semua baris Laporan" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
        return x;
    }

    public void ClearLaporan() {
        try {
            db.delete(NAMATABELLAPORAN, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Clear Laporan Error", e.toString());
        }
    }

}