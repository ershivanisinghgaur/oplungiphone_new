package com.firerox.oplungiphone.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.firerox.oplungiphone.R;
import com.firerox.oplungiphone.database.DBHelper;

/**
 * Created by TRUNGGUNNERs on 15/04/2016.
 */
public class CustomDialogAddCart extends Dialog implements View.OnClickListener {
    public DBHelper dbHelper;
    public Context context;
    public Dialog dialog;
    public Button btnOK, btnCancel;


    public CustomDialogAddCart(Context context) {
        super(context);
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_addcart);
        btnOK = (Button) findViewById(R.id.btnOK);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        dbHelper = new DBHelper(getContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK:

                break;
            case R.id.btnCancel:

                break;
            default:
                break;
        }
        dismiss();
    }
}