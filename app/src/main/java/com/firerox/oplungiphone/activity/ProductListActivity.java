package com.firerox.oplungiphone.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.etsy.android.grid.StaggeredGridView;
import com.firerox.oplungiphone.R;
import com.firerox.oplungiphone.SettingApplication;
import com.firerox.oplungiphone.SettingConfig;
import com.firerox.oplungiphone.adapter.AdapterProductByCategory;
import com.firerox.oplungiphone.item.ItemProduct;
import com.firerox.oplungiphone.utility.JsonConfig;
import com.firerox.oplungiphone.utility.JsonUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    StaggeredGridView gridView;
    List<ItemProduct> list;
    AdapterProductByCategory adapter;
    ArrayList<String> array_products, array_products_category_name, array_products_id,
            array_category_id, array_category_image, array_category_name, array_title, array_image,
            array_detail, array_price, array_code, array_state;
    String[] str_products, str_products_category_name;
    String[] str_products_id, str_category_id, str_category_image, str_category_name, str_title,
            str_image, str_detail, str_price, str_code, str_state;
    JsonUtils util;
    int textlength = 0;
    private ItemProduct object;
    private CoordinatorLayout coordinatorLayout;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setupActionBar();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        gridView = (StaggeredGridView) findViewById(R.id.gridView2);

        list = new ArrayList<ItemProduct>();
        array_products = new ArrayList<String>();
        array_products_category_name = new ArrayList<String>();
        array_products_id = new ArrayList<String>();
        array_category_id = new ArrayList<String>();
        array_category_image = new ArrayList<String>();
        array_category_name = new ArrayList<String>();
        array_title = new ArrayList<String>();
        array_image = new ArrayList<String>();
        array_detail = new ArrayList<String>();
        array_price = new ArrayList<String>();
        array_code = new ArrayList<String>();
        array_state = new ArrayList<String>();

        str_products = new String[array_products.size()];
        str_products_category_name = new String[array_products_category_name.size()];
        str_products_id = new String[array_products_id.size()];
        str_category_id = new String[array_category_id.size()];
        str_category_image = new String[array_category_image.size()];
        str_category_name = new String[array_category_name.size()];
        str_title = new String[array_title.size()];
        str_image = new String[array_image.size()];
        str_detail = new String[array_detail.size()];
        str_price = new String[array_price.size()];
        str_code = new String[array_code.size()];
        str_state = new String[array_state.size()];

        util = new JsonUtils(getApplicationContext());

        renderViewBanner();

        if (JsonUtils.isNetworkAvailable(ProductListActivity.this)) {
            new MyTask().execute(SettingConfig.SERVER_URL + "/api.php?categoryId=" + JsonConfig.CATEGORY_ID);
        } else {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, R.string.nonet1, Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                object = list.get(position);
                int pos = Integer.parseInt(object.getProductId());

                Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                intent.putExtra("POSITION", pos);
                intent.putExtra("CATEGORY_ITEM_CATEGORY_ID", str_category_id);
                intent.putExtra("CATEGORY_ITEM_NAME", str_category_name);

                intent.putExtra("CATEGORY_ITEM_PRODUCT_ID", str_products_id);
                intent.putExtra("CATEGORY_ITEM_PRODUCT_IMAGE", str_image);
                intent.putExtra("CATEGORY_ITEM_PRODUCT_TITLE", str_title);
                intent.putExtra("CATEGORY_ITEM_PRODUCT_DETAIL", str_detail);
                intent.putExtra("CATEGORY_ITEM_PRODUCT_STATE", str_state);
                intent.putExtra("CATEGORY_ITEM_PRODUCT_PRICE", str_price);
                intent.putExtra("CATEGORY_ITEM_PRODUCT_CODE", str_code);

                startActivity(intent);
            }
        });

        // init analytics tracker
        ((SettingApplication) getApplication()).getTracker();
    }

    public void setAdapterToListview() {
        adapter = new AdapterProductByCategory(ProductListActivity.this, R.layout.activity_product_list_item, list);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // analytics
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        // analytics
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);

        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView)
                MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    searchMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                }
            }
        });

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {

                textlength = newText.length();
                list.clear();

                for (int i = 0; i < str_title.length; i++) {
                    if (textlength <= str_title[i].length()) {
                        if (str_title[i].toLowerCase().contains(newText.toLowerCase())) {

                            ItemProduct objItem = new ItemProduct();

                            objItem.setCategoryName(str_category_name[i]);
                            objItem.setCategoryId(str_category_id[i]);
                            objItem.setProductId(str_products_id[i]);
                            objItem.setProductDetail(str_detail[i]);
                            objItem.setProductTitle(str_title[i]);
                            objItem.setProductImage(str_image[i]);
                            objItem.setProductPrice(str_price[i]);
                            objItem.setProductCode(str_code[i]);

                            list.add(objItem);

                        }
                    }
                }

                setAdapterToListview();
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do something
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // action bar menu behaviour
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_cart:
                Intent intent = new Intent(ProductListActivity.this, CartActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void renderViewBanner() {
        final AdView adView = (AdView) findViewById(R.id.adView);
        final ViewGroup bannerViewGroup = (ViewGroup) findViewById(R.id.container_admob);

        if (SettingConfig.ADMOB_RECIPE_BANNER && JsonUtils.isNetworkAvailable(ProductListActivity.this)) {

            AdRequest adRequest = new AdRequest.Builder().build();
            bannerViewGroup.setVisibility(View.VISIBLE);
            adView.setVisibility(View.VISIBLE);
            adView.loadAd(adRequest);
        } else {
            adView.setVisibility(View.GONE);
            bannerViewGroup.setVisibility(View.GONE);
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
        bar.setTitle(R.string.recipes);
    }

    private class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ProductListActivity.this);
            progressDialog.setMessage("Đang tải sản phẩm...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (null != progressDialog && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (null == result || result.length() == 0) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, R.string.nonet1, Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(JsonConfig.CATEGORY_ARRAY_NAME);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);

                        ItemProduct objItem = new ItemProduct();

                        objItem.setCategoryId(objJson.getString(JsonConfig.CATEGORY_CID));
                        //objItem.setCategoryName(objJson.getString(JsonConfig.CATEGORY_NAME));
                        //objItem.setCategoryImage(objJson.getString(JsonConfig.CATEGORY_IMAGE));
                        objItem.setProductId(objJson.getString(JsonConfig.CATEGORY_ITEM_PRODUCT_ID));
                        objItem.setProductImage(objJson.getString(JsonConfig.CATEGORY_ITEM_PRODUCT_IMAGE));
                        objItem.setProductTitle(objJson.getString(JsonConfig.CATEGORY_ITEM_PRODUCT_TITLE));
                        objItem.setProductDetail(objJson.getString(JsonConfig.CATEGORY_ITEM_PRODUCT_DETAIL));
                        objItem.setProductPrice(objJson.getString(JsonConfig.CATEGORY_ITEM_PRODUCT_PRICE));
                        objItem.setProductCode(objJson.getString(JsonConfig.CATEGORY_ITEM_PRODUCT_CODE));
                        objItem.setProductState(objJson.getString(JsonConfig.CATEGORY_ITEM_PRODUCT_STATE));

                        list.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < list.size(); j++) {

                    object = list.get(j);

                    array_category_id.add(object.getCategoryId());
                    str_category_id = array_category_id.toArray(str_category_id);

                    array_category_name.add(object.getCategoryName());
                    str_category_name = array_category_name.toArray(str_category_name);

                    array_products_id.add(String.valueOf(object.getProductId()));
                    str_products_id = array_products_id.toArray(str_products_id);

                    array_image.add(String.valueOf(object.getProductImage()));
                    str_image = array_image.toArray(str_image);

                    array_title.add(String.valueOf(object.getProductTitle()));
                    str_title = array_title.toArray(str_title);

                    array_detail.add(String.valueOf(object.getProductDetail()));
                    str_detail = array_detail.toArray(str_detail);

                    array_price.add(String.valueOf(object.getProductPrice()));
                    str_price = array_price.toArray(str_price);

                    array_code.add(String.valueOf(object.getProductCode()));
                    str_code = array_code.toArray(str_code);

                    array_state.add(String.valueOf(object.getProductCode()));
                    str_state = array_state.toArray(str_state);

                }

                setAdapterToListview();
            }

        }
    }
}