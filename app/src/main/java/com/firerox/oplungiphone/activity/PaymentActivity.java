package com.firerox.oplungiphone.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.firerox.oplungiphone.R;
import com.firerox.oplungiphone.SettingConfig;
import com.firerox.oplungiphone.api.SendOrderAPI;
import com.firerox.oplungiphone.database.DBHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by TRUNGGUNNERs on 10/04/2016.
 */
public class PaymentActivity extends AppCompatActivity {
    LinearLayout layout_payment;
    RelativeLayout layout_payment_null;
    ScrollView scrollView;
    Button btnSetDate, btnSetTime, btnSendOrder;
    EditText customer_name, customer_email, customer_phone, customer_address, customer_info;
    TextView detail, total_order;
    public static int day, month, year, hour, minute;
    public static final String TIME_DIALOG_ID = "timePicker";
    public static final String DATE_DIALOG_ID = "datePicker";

    static DBHelper dbHelper;
    ArrayList<ArrayList<Object>> data;
    String ORDER_LIST = "";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        layout_payment = (LinearLayout) findViewById(R.id.layout_payment);
        scrollView = (ScrollView) findViewById(R.id.scrollViewPayment);
        layout_payment_null = (RelativeLayout) findViewById(R.id.layout_payment_null);
        btnSetDate = (Button) findViewById(R.id.btnSetDate);
        btnSetTime = (Button) findViewById(R.id.btnSetTime);
        btnSendOrder = (Button) findViewById(R.id.btnSendOrder);
        customer_name = (EditText) findViewById(R.id.customer_name);
        customer_email = (EditText) findViewById(R.id.customer_email);
        customer_phone = (EditText) findViewById(R.id.customer_phone);
        customer_address = (EditText) findViewById(R.id.customer_address);
        customer_info = (EditText) findViewById(R.id.customer_msg);
        detail = (TextView) findViewById(R.id.customer_order_list);
        total_order = (TextView) findViewById(R.id.total_order);

        setupActionBar();

        //renderViewBanner();

        dbHelper = new DBHelper(this);

        // open database
        try{
            dbHelper.openDatabase();
        }catch(SQLException sqle){
            throw sqle;
        }

        new getDataTask().execute();

        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), DATE_DIALOG_ID);
            }
        });

        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new TimePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID);
            }
        });

        btnSendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(customer_name.getText().toString().length()==0) {
                    customer_name.requestFocus();
                    customer_name.setError("Vui lòng nhập họ tên");

                } else if(!customer_email.getText().toString().trim().matches(
                        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
                        || customer_email.getText().toString().trim().length() == 0) {
                    customer_email.requestFocus();
                    customer_email.setError("Email không hợp lệ");

                } else if(customer_phone.getText().toString().length()==0
                        || customer_phone.getText().toString().length() < 10
                        || customer_phone.getText().toString().length() > 11) {
                    customer_phone.requestFocus();
                    customer_phone.setError("Vui lòng nhập số điện thoại");

                } else if(customer_address.getText().toString().length()==0) {
                    customer_address.requestFocus();
                    customer_address.setError("Vui lòng nhập địa chỉ");

                } else if(btnSetDate.getText().toString().equals("Chọn ngày")
                        || btnSetTime.getText().toString().equals("Chọn giờ")) {

                }
                else {

                    insertOrder();
                    dbHelper.deleteAllData();
                }

            }
        });

    }

    //Admob Banner
    /*private void renderViewBanner() {
        final AdView adView = (AdView) findViewById(R.id.adView);
        final ViewGroup bannerViewGroup = (ViewGroup) findViewById(R.id.container_admob);

        if (SettingConfig.ADMOB_RECIPE_BANNER && JsonUtils.isNetworkAvailable(PaymentActivity.this)) {

            AdRequest adRequest = new AdRequest.Builder().build();
            bannerViewGroup.setVisibility(ViewGroup.VISIBLE);
            adView.setVisibility(AdView.VISIBLE);
            adView.loadAd(adRequest);
        } else {
            adView.setVisibility(AdView.GONE);
            bannerViewGroup.setVisibility(ViewGroup.GONE);
        }
    }*/



    // asynctask class to get data from database in background
    public class getDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            getDataFromDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(ORDER_LIST.toString().length()==0) {
                scrollView.setVisibility(ScrollView.GONE);
                layout_payment_null.setVisibility(RelativeLayout.VISIBLE);
            } else {
                scrollView.setVisibility(ScrollView.VISIBLE);
                layout_payment_null.setVisibility(RelativeLayout.GONE);
            }

        }
    }

    //get data from database
    public void getDataFromDatabase() {
        double TOTAL_PRICE = 0;
        try {
            data = dbHelper.getAllData();

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < data.size(); i++) {
            ArrayList<Object> row = data.get(i);
            String product_id = row.get(0).toString();
            String product_name = row.get(1).toString();
            String product_quantity = row.get(2).toString();
            double product_price = Double.parseDouble(row.get(3).toString());
            TOTAL_PRICE += product_price;

            //calculate order price
            ORDER_LIST += (" - TÊN SP: " + product_name + " - " + "SỐ LƯỢNG: " + product_quantity
                    + " - " + "GIÁ: " + product_price + "Đ" + " \n");
        }

        detail.setText("GIỎ HÀNG CỦA BẠN: \n\n" + ORDER_LIST);
        total_order.setText("TỔNG CỘNG: " + String.valueOf(TOTAL_PRICE) + "Đ");
    }

    //Show Dialog
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Gửi đơn hàng thành công!");
        builder.setMessage("Chúng tôi sẽ xử lý đơn hàng của bạn. Cảm ơn bạn đã mua hàng của chúng tôi.");
        builder.setCancelable(false);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Gửi đơn hàng thất bại!");
        builder.setMessage("Bạn vui lòng kiểm tra lại thông tin vừa nhập!");
        builder.setCancelable(false);

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void insertOrder() {
        //Here we will handle the http request to insert order to mysql database
        //Creating a RestAdapter
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SettingConfig.API_SERVER)
                .build();

        //Creating object for our interface
        SendOrderAPI api = restAdapter.create(SendOrderAPI.class);

        //Defining the method insertOrder of our interface
        api.insertOrder(
                customer_name.getText().toString(),
                customer_email.getText().toString(),
                customer_phone.getText().toString(),
                customer_address.getText().toString(),
                customer_info.getText().toString(),
                "ĐƠN HÀNG TỪ ỨNG DỤNG:\n\n"
                        + detail.getText().toString()
                        + total_order.getText().toString(),
                        btnSetDate.getText().toString() + " "
                        + btnSetTime.getText().toString(),

                //Creating an anonymous callback
                new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        //On success we will read the server's output using bufferedreader
                        //Creating a bufferedreader object
                        BufferedReader reader = null;

                        //An string to store output from the server
                        String output = "";

                        try {
                            //Initializing buffered reader
                            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

                            //Reading the output in the string
                            output = reader.readLine();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        progressDialog = new ProgressDialog(PaymentActivity.this);
                        progressDialog.setMessage("Đang xử lý...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        if (null != progressDialog && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        showSuccessDialog();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressDialog = new ProgressDialog(PaymentActivity.this);
                        progressDialog.setMessage("Đang xử lý...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        if (null != progressDialog && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        showErrorDialog();
                    }
                }
        );

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
        bar.setTitle(R.string.title_activity_payment);
    }

    private class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            btnSetDate.setText(new StringBuilder()
            .append(year).append("-")
                    .append(monthOfYear + 1).append("-")
                    .append(dayOfMonth).append(" ")
            );
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
    }

    private class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            btnSetTime.setText(new StringBuilder()
                    .append(pad(hourOfDay)).append(":")
                    .append(pad(minute)).append(":")
                    .append("00"));
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR);
            minute = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }
    }

    private static String pad(int c) {
        if (c >= 10){
            return String.valueOf(c);
        }else{
            return "0" + String.valueOf(c);
        }
    }
}
