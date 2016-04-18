package com.firerox.oplungiphone.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AddtoFav";
    private static final String TABLE_NAME = "Favorite";
    private static final String KEY_ID = "id";
    private static final String KEY_CATEGORYID = "categoryId";
    private static final String KEY_PRODUCTID = "productId";
    private static final String KEY_CATEGORYNAME = "categoryName";
    private static final String KEY_PRODUCTTITLE = "productTitle";
    private static final String KEY_PRODUCTIMAGE = "productImage";
    private static final String KEY_PRODUCTDETAIL = "productDetail";
    private static final String KEY_PRODUCTSTATE = "productState";
    private static final String KEY_PRODUCTPRICE = "productPrice";
    private static final String KEY_PRODUCTCODE = "productCode";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CATEGORYID + " TEXT,"
                + KEY_PRODUCTID + " TEXT,"
                + KEY_CATEGORYNAME + " TEXT,"
                + KEY_PRODUCTTITLE + " TEXT,"
                + KEY_PRODUCTIMAGE + " TEXT,"
                + KEY_PRODUCTDETAIL + " TEXT,"
                + KEY_PRODUCTSTATE + " TEXT,"
                + KEY_PRODUCTPRICE + " TEXT,"
                + KEY_PRODUCTCODE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    //Adding Record in Database

    public void AddtoFavorite(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORYID, product.getCategoryId());
        values.put(KEY_PRODUCTID, product.getProductId());
        values.put(KEY_CATEGORYNAME, product.getCategoryName());
        values.put(KEY_PRODUCTTITLE, product.getProductTitle());
        values.put(KEY_PRODUCTIMAGE, product.getProductImage());
        values.put(KEY_PRODUCTDETAIL, product.getProductDetail());
        values.put(KEY_PRODUCTSTATE, product.getProductState());
        values.put(KEY_PRODUCTPRICE, product.getProductPrice());
        values.put(KEY_PRODUCTCODE, product.getProductCode());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection

    }

    // Getting All Data
    public List<Product> getAllData() {
        List<Product> dataList = new ArrayList<Product>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(Integer.parseInt(cursor.getString(0)));
                product.setCategoryId(cursor.getString(1));
                product.setProductId(cursor.getString(2));
                product.setCategoryName(cursor.getString(3));
                product.setProductTitle(cursor.getString(4));
                product.setProductImage(cursor.getString(5));
                product.setProductDetail(cursor.getString(6));
                product.setProductState(cursor.getString(7));
                product.setProductPrice(cursor.getString(8));
                product.setProductCode(cursor.getString(9));

                // Adding product to list
                dataList.add(product);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dataList;
    }

    //getting single row

    public List<Product> getFavRow(String id) {
        List<Product> dataList = new ArrayList<Product>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE categoryId=" + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(Integer.parseInt(cursor.getString(0)));
                product.setCategoryId(cursor.getString(1));
                product.setProductId(cursor.getString(2));
                product.setCategoryName(cursor.getString(3));
                product.setProductTitle(cursor.getString(4));
                product.setProductImage(cursor.getString(5));
                product.setProductDetail(cursor.getString(6));
                product.setProductState(cursor.getString(7));
                product.setProductPrice(cursor.getString(8));
                product.setProductCode(cursor.getString(9));

                // Adding contact to list
                dataList.add(product);
            } while (cursor.moveToNext());
        }

        // return product list
        return dataList;
    }

    //for remove favorite

    public void RemoveFav(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_CATEGORYID + " = ?",
                new String[]{String.valueOf(product.getCategoryId())});
        db.close();
    }

    public enum DatabaseManager {
        INSTANCE;
        DatabaseHandler dbHelper;
        private SQLiteDatabase db;
        private boolean isDbClosed = true;

        public void init(Context context) {
            dbHelper = new DatabaseHandler(context);
            if (isDbClosed) {
                isDbClosed = false;
                this.db = dbHelper.getWritableDatabase();
            }

        }


        public boolean isDatabaseClosed() {
            return isDbClosed;
        }

        public void closeDatabase() {
            if (!isDbClosed && db != null) {
                isDbClosed = true;
                db.close();
                dbHelper.close();
            }
        }
    }
}
