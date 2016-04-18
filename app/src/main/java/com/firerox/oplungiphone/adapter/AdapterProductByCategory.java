package com.firerox.oplungiphone.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.firerox.oplungiphone.R;
import com.firerox.oplungiphone.SettingConfig;
import com.firerox.oplungiphone.item.ItemProduct;
import com.firerox.oplungiphone.utility.ImageLoader;

import java.util.List;
import java.util.Random;

public class AdapterProductByCategory extends ArrayAdapter<ItemProduct> {

    private static final String TAG = "AdapterProducts";
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    private final Random mRandom;
    public ImageLoader imageLoader;
    ItemProduct object;
    private Activity activity;
    private List<ItemProduct> item;
    private int row;

    public AdapterProductByCategory(Activity activity, int resource, List<ItemProduct> arrayList) {
        super(activity, resource, arrayList);
        this.activity = activity;
        this.row = resource;
        this.item = arrayList;
        imageLoader = new ImageLoader(activity);
        mRandom = new Random();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(row, null);
            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if ((item == null) || ((position + 1) > item.size()))
            return view;

        object = item.get(position);

        double positionHeight = getPositionRatio(position);

        holder.title = (TextView) view.findViewById(R.id.product_title);
        holder.price = (TextView) view.findViewById(R.id.product_price);
        holder.code = (TextView) view.findViewById(R.id.product_code);
        holder.image = (DynamicHeightImageView) view.findViewById(R.id.worcipe_image);

        holder.image.setHeightRatio(positionHeight);
        holder.title.setText(object.getProductTitle());
        holder.price.setText("Giá: " + object.getProductPrice() + "Đ");
        holder.code.setText("MSP: "+ object.getProductCode());

        imageLoader.DisplayImage(SettingConfig.SERVER + "/uploads/products/" + object.getProductImage(), holder.image);

        return view;

    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }

    public class ViewHolder {

        public TextView title;
        public TextView price;
        public TextView code;
        public DynamicHeightImageView image;

    }
}
