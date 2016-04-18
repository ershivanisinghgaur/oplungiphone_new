package com.firerox.oplungiphone.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.firerox.oplungiphone.R;
import com.firerox.oplungiphone.database.DBHelper;

/**
 * Created by TRUNGGUNNERs on 14/04/2016.
 */
public class CustomDialogMessage extends Dialog implements View.OnClickListener {
    public DBHelper dbHelper;
    public Activity activity;
    public Dialog dialog;
    public Button btnCancel, btnExit;


    public CustomDialogMessage(Activity activity) {
        super(activity);
        this.activity = activity;
        dbHelper = new DBHelper(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_message);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnExit = (Button) findViewById(R.id.btnExit);

        btnCancel.setOnClickListener(this);
        btnExit.setOnClickListener(this);

        dbHelper = new DBHelper(getContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                dismiss();

                break;
            case R.id.btnExit:
                dbHelper.deleteAllData();
                activity.finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                break;
            default:
                break;
        }
        dismiss();
    }
}
