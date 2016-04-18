package com.firerox.oplungiphone.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.firerox.oplungiphone.R;
import com.firerox.oplungiphone.adapter.AdapterCart;
import com.firerox.oplungiphone.database.DBHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by TRUNGGUNNERs on 12/04/2016.
 */
public class CartActivity extends AppCompatActivity {
    InterstitialAd mInterstitialAd;
    ListView listViewOrder;
    Button btnCheckout;
    RelativeLayout layout_main, layout_null;
    DBHelper dbHelper;
    AdapterCart adapterCart;
    double TOTAL_PRICE;

    ProgressDialog progressDialog;

    public static ArrayList<ArrayList<Object>> data;
    public static ArrayList<Integer> product_id = new ArrayList<Integer>();
    public static ArrayList<String> product_name = new ArrayList<String>();
    public static ArrayList<Integer> product_quantity = new ArrayList<Integer>();
    public static ArrayList<Double> product_price = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        layout_main = (RelativeLayout) findViewById(R.id.layout_main);
        layout_null = (RelativeLayout) findViewById(R.id.layout_null);
        btnCheckout = (Button) findViewById(R.id.btnCheckout);
        listViewOrder = (ListView) findViewById(R.id.listViewOrder);

        //Fullscreen Admob Banner
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1490401564391885/7073764455");
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdClosed() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdLeftApplication() {

            }

            @Override
            public void onAdOpened() {

            }
        });

        adapterCart = new AdapterCart(this);
        dbHelper = new DBHelper(this);

        //open database
        try {
            dbHelper.openDatabase();
        } catch (SQLException e) {
            throw e;
        }

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
            }
        });

        new GetTask().execute();

        setupActionBar();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update_cart:
                progressDialog = new ProgressDialog(CartActivity.this);
                progressDialog.setMessage("Cập nhật giỏ hàng...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                if (null != progressDialog && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                new GetTask().execute();
                return true;

            case R.id.action_delete_cart:
                if(product_id.size() > 0) {
                    showDialog();
                } else {

                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showDialog() {

        final Dialog dialog = new Dialog(CartActivity.this, R.style.MyDialogTheme);
        dialog.setContentView(R.layout.custom_dialog_clear_order);
        dialog.show();
        dialog.setCancelable(false);

        final Button btnClearOrder = (Button) dialog.findViewById(R.id.btnClearOrder);
        final Button btnCancelOrder = (Button) dialog.findViewById(R.id.btnCancelOrder);

        btnClearOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new GetTask().execute();
                try {
                    dbHelper.deleteAllData();
                    listViewOrder.invalidateViews();
                    new GetTask().execute();
                } catch (SQLException e) {
                    throw e;
                }
                dialog.cancel();
            }
        });

        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    public class GetTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            getDataFromDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            btnCheckout.setText("THANH TOÁN NGAY");
            if(product_id.size() > 0) {
                layout_null.setVisibility(RelativeLayout.GONE);
                listViewOrder.setAdapter(adapterCart);
                adapterCart.notifyDataSetChanged();
            } else {
                listViewOrder.setVisibility(ListView.GONE);
                btnCheckout.setVisibility(Button.GONE);
                layout_null.setVisibility(RelativeLayout.VISIBLE);
            }

        }
    }

    //get data from database
    public void getDataFromDatabase() {
        TOTAL_PRICE = 0;
        clearData();
        try {
            data = dbHelper.getAllData();

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < data.size(); i++) {
            ArrayList<Object> row = data.get(i);
            product_id.add(Integer.parseInt(row.get(0).toString()));
            product_name.add(row.get(1).toString());
            product_quantity.add(Integer.parseInt(row.get(2).toString()));
            product_price.add(Double.parseDouble(row.get(3).toString()));
            TOTAL_PRICE += product_price.get(i);

        }

    }

    public void clearData() {
        product_id.clear();
        product_name.clear();
        product_quantity.clear();
        product_price.clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dbHelper.close();
        finish();
        overridePendingTransition(R.anim.open_next, R.anim.close_next);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

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
        bar.setTitle(R.string.title_activity_cart);
    }
}
