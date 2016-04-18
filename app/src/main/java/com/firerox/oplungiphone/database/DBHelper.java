package com.firerox.oplungiphone.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by TRUNGGUNNERs on 09/04/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static String DB_PATH;
    private final Context context;
    public SQLiteDatabase db;
    public final static String DB_NAME = "cp_order";
    public final static int DB_VERSION = 1;
    public final static String TABLE_NAME = "table_order";
    public final static String ID = "id";
    public final static String PRODUCT_NAME = "product_name";
    public final static String PRODUCT_QUANTITY = "product_quantity";
    public final static String TOTAL_PRICE = "total_price";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        DB_PATH = "/data/data/com.firerox.oplungiphone/databases/";
    }

    public void createDatabase() throws IOException {
        boolean dbExist = checkDatabase();
        SQLiteDatabase dbRead = null;
        if(dbExist) {
            // do nothing, database already exist
        } else {
            dbRead = this.getReadableDatabase();
            dbRead.close();

            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }

        }
    }

    public boolean checkDatabase() {
        File file = new File(DB_PATH + DB_NAME);
        return file.exists();
    }

    public void copyDatabase() throws IOException {
        InputStream inputStream = context.getAssets().open(DB_NAME);
        String outputFileName = DB_PATH + DB_NAME;
        OutputStream outputStream = new FileOutputStream(outputFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    public void openDatabase() throws SQLException {
        String path = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public ArrayList<ArrayList<Object>> getAllData() throws IOException{
        ArrayList<ArrayList<Object>> arrayList = new ArrayList<ArrayList<Object>>();
        Cursor cursor = null;

        cursor = db.query(TABLE_NAME, new String[] {ID, PRODUCT_NAME, PRODUCT_QUANTITY,
                        TOTAL_PRICE}, null, null, null, null, null);

        cursor.moveToFirst();

        if(!cursor.isAfterLast()) {
            do {
                ArrayList<Object> object = new ArrayList<Object>();
                object.add(cursor.getLong(0));
                object.add(cursor.getString(1));
                object.add(cursor.getString(2));
                object.add(cursor.getString(3));

                arrayList.add(object);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return arrayList;
    }

    public boolean isDataExist(long id) {
        boolean exist = false;
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, new String[] {ID}, "id = " + id, null, null, null, null);
            if(cursor.getCount() > 0) {
                exist = true;
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return exist;
    }

    public boolean isPreviousDataExist() {
        boolean exist = false;
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, new String[] {ID}, null, null, null, null, null);
            if(cursor.getCount() > 0) {
                exist = true;
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return exist;
    }

    public void addData(long id, String product_name, int product_quantity, double total_price) {
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(PRODUCT_NAME, product_name);
        values.put(PRODUCT_QUANTITY, product_quantity);
        values.put(TOTAL_PRICE, total_price);

        try {
            db.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public long deleteData(int id) {
        try {
            db = this.getWritableDatabase();
            long rows = db.delete(TABLE_NAME, " id =? ", new String[]{String.valueOf(id)});
            return rows;
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            return -1;
        }
    }

    public long deleteAllData() {
        try {
            db = this.getWritableDatabase();
            long rows = db.delete(TABLE_NAME, null, null);
            return rows;
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            return -1;
        }
    }

    public long updateData(long id, int product_quantity, double total_price) {
        ContentValues values = new ContentValues();
        values.put(PRODUCT_QUANTITY, product_quantity);
        values.put(TOTAL_PRICE, total_price);
        try {
            long rows = db.update(TABLE_NAME, values, " id =? ", new String[]{String.valueOf(id)});
            return rows;

        } catch (Exception e) {
            return -1;
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void close() {
        db.close();
    }
}
