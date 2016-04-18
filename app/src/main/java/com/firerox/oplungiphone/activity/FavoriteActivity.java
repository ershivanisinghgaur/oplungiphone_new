package com.firerox.oplungiphone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.firerox.oplungiphone.R;
import com.firerox.oplungiphone.SettingConfig;
import com.firerox.oplungiphone.adapter.AdapterFavorite;
import com.firerox.oplungiphone.utility.DatabaseHandler;
import com.firerox.oplungiphone.utility.JsonUtils;
import com.firerox.oplungiphone.utility.Product;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    StaggeredGridView gridView;
    DatabaseHandler db;
    AdapterFavorite adapter;
    TextView textView;
    JsonUtils util;
    List<Product> list;
    ArrayList<String> array_products, array_products_category_name, array_product_id, array_category_id,
            array_category_name, array_title, array_image, array_detail, array_state, array_price, array_code;
    String[] str_products, str_products_category_name, str_product_id, str_category_id, str_category_name,
            str_title, str_image, str_detail, str_state, str_price, str_code;
    int textlength = 0;
    Product product;
    private DatabaseHandler.DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        setupActionBar();

        gridView = (StaggeredGridView) findViewById(R.id.gridView3);
        db = new DatabaseHandler(getApplicationContext());
        dbManager = DatabaseHandler.DatabaseManager.INSTANCE;
        dbManager.init(getApplicationContext());
        util = new JsonUtils(getApplicationContext());

        list = db.getAllData();
        adapter = new AdapterFavorite(FavoriteActivity.this,
                R.layout.activity_favorite_items, list);
        gridView.setAdapter(adapter);

        renderViewBanner();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                product = list.get(position);
                int pos = Integer.parseInt(product.getCategoryId());

                Intent intplay = new Intent(FavoriteActivity.this, ProductDetailActivity.class);
                intplay.putExtra("POSITION", pos);
                intplay.putExtra("CATEGORY_ITEM_PRODUCT_ID", str_product_id);
                intplay.putExtra("CATEGORY_ITEM_NAME", str_category_name);
                intplay.putExtra("CATEGORY_ITEM_CATEGORY_ID", str_category_id);
                intplay.putExtra("CATEGORY_ITEM_IMAGE", str_image);
                intplay.putExtra("CATEGORY_ITEM_TITLE", str_title);
                intplay.putExtra("CATEGORY_ITEM_DETAIL", str_detail);
                intplay.putExtra("CATEGORY_ITEM_STATE", str_state);
                intplay.putExtra("CATEGORY_ITEM_PRICE", str_price);
                intplay.putExtra("CATEGORY_ITEM_CODE", str_code);

                startActivity(intplay);
            }
        });

    }

    public void onDestroy() {
        if (!dbManager.isDatabaseClosed())
            dbManager.closeDatabase();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!dbManager.isDatabaseClosed())
            dbManager.closeDatabase();
    }

    @Override
    public void onResume() {
        super.onResume();

        list = db.getAllData();
        adapter = new AdapterFavorite(FavoriteActivity.this, R.layout.activity_favorite_items, list);
        gridView.setAdapter(adapter);

        array_products = new ArrayList<String>();
        array_products_category_name = new ArrayList<String>();
        array_product_id = new ArrayList<String>();
        array_category_id = new ArrayList<String>();
        array_category_name = new ArrayList<String>();
        array_title = new ArrayList<String>();
        array_image = new ArrayList<String>();
        array_detail = new ArrayList<String>();
        array_state = new ArrayList<String>();
        array_price = new ArrayList<String>();
        array_code = new ArrayList<String>();

        str_products = new String[array_products.size()];
        str_products_category_name = new String[array_products_category_name.size()];
        str_product_id = new String[array_product_id.size()];
        str_category_id = new String[array_category_id.size()];
        str_category_name = new String[array_category_name.size()];
        str_title = new String[array_title.size()];
        str_image = new String[array_image.size()];
        str_detail = new String[array_detail.size()];
        str_state = new String[array_state.size()];
        str_price = new String[array_price.size()];
        str_code = new String[array_code.size()];

        for (int j = 0; j < list.size(); j++) {
            Product object = list.get(j);

            array_category_id.add(object.getCategoryId());
            str_category_id = array_category_id.toArray(str_category_id);

            array_product_id.add(String.valueOf(object.getProductId()));
            str_product_id = array_product_id.toArray(str_product_id);

            array_category_name.add(object.getCategoryName());
            str_category_name = array_category_name.toArray(str_category_name);

            array_title.add(String.valueOf(object.getProductTitle()));
            str_title = array_title.toArray(str_title);

            array_image.add(String.valueOf(object.getProductImage()));
            str_image = array_image.toArray(str_image);

            array_detail.add(String.valueOf(object.getProductDetail()));
            str_detail = array_detail.toArray(str_detail);

            array_state.add(String.valueOf(object.getProductState()));
            str_state = array_state.toArray(str_state);

            array_price.add(String.valueOf(object.getProductPrice()));
            str_price = array_price.toArray(str_price);

            array_code.add(String.valueOf(object.getProductCode()));
            str_code = array_code.toArray(str_code);

        }
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle(R.string.favorites);
    }

    private void renderViewBanner() {
        final AdView adView = (AdView) findViewById(R.id.adView);
        final ViewGroup bannerViewGroup = (ViewGroup) findViewById(R.id.container_admob);

        if (SettingConfig.ADMOB_RECIPE_BANNER && JsonUtils.isNetworkAvailable(FavoriteActivity.this)) {

            AdRequest adRequest = new AdRequest.Builder().build();
            bannerViewGroup.setVisibility(View.VISIBLE);
            adView.setVisibility(View.VISIBLE);
            adView.loadAd(adRequest);
        } else {
            adView.setVisibility(View.GONE);
            bannerViewGroup.setVisibility(View.GONE);
        }
    }
}