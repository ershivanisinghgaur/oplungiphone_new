package com.firerox.oplungiphone.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firerox.oplungiphone.R;
import com.firerox.oplungiphone.activity.CartActivity;
import com.firerox.oplungiphone.database.DBHelper;

import java.io.IOException;

/**
 * Created by TRUNGGUNNERs on 12/04/2016.
 */
public class AdapterCart extends BaseAdapter {
    private LayoutInflater layoutInflater;
    DBHelper dbHelper;
    Context context;
    int pos;

    public AdapterCart(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        dbHelper = new DBHelper(context);
    }

    @Override
    public int getCount() {
        return CartActivity.product_id.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.activity_order_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_product_id = (TextView) convertView.findViewById(R.id.tv_product_id);
            viewHolder.tvProductName = (TextView) convertView.findViewById(R.id.tvProductName);
            viewHolder.tv_quantity = (TextView) convertView.findViewById(R.id.tv_quantity);
            viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            viewHolder.image_delete = (ImageButton) convertView.findViewById(R.id.image_delete);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_product_id.setText(String.valueOf(CartActivity.product_id.get(position)));
        pos = Integer.parseInt(viewHolder.tv_product_id.getText().toString());
        viewHolder.tvProductName.setText(CartActivity.product_name.get(position));
        viewHolder.tv_quantity.setText(String.valueOf(CartActivity.product_quantity.get(position)));
        viewHolder.tv_price.setText(String.valueOf(CartActivity.product_price.get(position)));

        viewHolder.image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Integer.parseInt(viewHolder.tv_product_id.getText().toString()));
                //Toast.makeText(context, viewHolder.tv_product_id.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }



    public class ViewHolder {
        TextView tv_product_id, tvProductName, tv_quantity, tv_price;
        ImageButton image_delete;

    }

    public void showDialog(final int pro_id) {

        final Dialog dialog = new Dialog(context, R.style.MyDialogTheme);
        dialog.setContentView(R.layout.custom_dialog_delete);
        dialog.show();

        final Button btnDeleteP = (Button) dialog.findViewById(R.id.btnDeleteP);
        final Button btnCancelP = (Button) dialog.findViewById(R.id.btnCancelP);

        btnDeleteP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteData(pro_id);
                try {
                    dbHelper.getAllData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dialog.cancel();
            }
        });

        btnCancelP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }
}
