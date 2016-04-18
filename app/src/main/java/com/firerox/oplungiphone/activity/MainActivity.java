package com.firerox.oplungiphone.activity;

import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firerox.oplungiphone.R;
import com.firerox.oplungiphone.database.DBHelper;
import com.firerox.oplungiphone.dialog.CustomDialogMessage;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Drawer result = null;
    static DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);

        // create database
        try {
            dbHelper.createDatabase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        // open database
        try {
            dbHelper.openDatabase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Handle Toolbar
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).
                                withIcon(R.drawable.com_garena_shopee_ic_me_product).withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.title_activity_cart).
                                withIcon(R.drawable.com_garena_shopee_ic_me_cart).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.title_activity_payment).
                                withIcon(R.drawable.btn_me_income).withIdentifier(3).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.title_activity_call).
                                withIcon(R.drawable.ic_me_prompt_phone).withIdentifier(4).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.title_activity_sms).
                                withIcon(R.drawable.com_garena_shopee_ic_chatsettings).withIdentifier(5).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.title_activity_rate).
                                withIcon(R.drawable.com_garena_shopee_ic_me_like).withIdentifier(6).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.title_activity_app).
                                withIcon(R.drawable.com_garena_shopee_ic_me_assistant).withIdentifier(7).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.title_activity_settings).
                                withIcon(R.drawable.btn_me_settings).withIdentifier(8).withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(MainActivity.this, MainActivity.class);
                            } else if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(MainActivity.this, CartActivity.class);
                            } else if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(MainActivity.this, PaymentActivity.class);
                            } else if (drawerItem.getIdentifier() == 4) {
                                Uri uri = Uri.parse("tel:" + "0965151515");
                                intent = new Intent(Intent.ACTION_CALL, uri);
                                try {
                                    startActivity(intent);
                                }
                                    catch (Exception ex) {
                                }

                            } else if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(Intent.ACTION_SEND);
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"phuongbacphukien@gmail.com"});
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Đóng góp ý kiến về oplungiphone.net");
                                intent.putExtra(Intent.EXTRA_TEXT, "Nội dung email \n\nEmail được gửi từ ứng dụng.");
                                intent.setType("message/rfc822");
                                startActivity(Intent.createChooser(intent, "Chọn tài khoản: "));

                            } else if (drawerItem.getIdentifier() == 6) {
                                Toast.makeText(MainActivity.this, "Chức năng đang cập nhật!",
                                        Toast.LENGTH_SHORT).show();
                            } else if (drawerItem.getIdentifier() == 7) {
                                Toast.makeText(MainActivity.this, "Chức năng đang cập nhật!",
                                        Toast.LENGTH_SHORT).show();
                            } else if (drawerItem.getIdentifier() == 8) {

                                CustomDialogMessage message = new CustomDialogMessage(MainActivity.this);
                                message.setCancelable(false);
                                message.show();

                            }
                            if (intent != null) {
                                MainActivity.this.startActivity(intent);
                            }
                        }

                        return false;
                    }
                }).build();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_refresh:
                finish();
                startActivity(getIntent());
                overridePendingTransition(R.anim.open_next, R.anim.close_next);
                return true;

            case R.id.action_cart:
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.open_next, R.anim.close_next);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {

        }
    }

    // Code xử lý thoát khi nhấn 2 lần vào nút back
    private long lastPressedTime;
    private static final int PERIOD = 2000;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        dbHelper.deleteAllData();
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                    } else {
                        Toast.makeText(getApplicationContext(), "Nhấn lại nút Back lần nữa để thoát",
                                Toast.LENGTH_SHORT).show();

                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;
    }
}
