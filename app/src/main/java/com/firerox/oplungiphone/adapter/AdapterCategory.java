package com.firerox.oplungiphone.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firerox.oplungiphone.SettingConfig;
import com.firerox.oplungiphone.R;
import com.firerox.oplungiphone.item.ItemProductCategory;
import com.firerox.oplungiphone.utility.ImageLoader;

import java.util.List;

public class AdapterCategory extends ArrayAdapter<ItemProductCategory> {

    public ImageLoader imageLoader;
    ItemProductCategory object;
    private Activity activity;
    private List<ItemProductCategory> item;
    private int row;

    public AdapterCategory(Activity act, int resource, List<ItemProductCategory> arrayList) {
        super(act, resource, arrayList);
        this.activity = act;
        this.row = resource;
        this.item = arrayList;
        imageLoader = new ImageLoader(activity);
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

        holder.title = (TextView) view.findViewById(R.id.category_title);
        holder.image = (ImageView) view.findViewById(R.id.category_image);

        holder.title.setText(object.getCategoryName());
        imageLoader.DisplayImage(SettingConfig.SERVER + "/uploads/products/" + object.getCategoryImageurl(), holder.image);

        return view;

    }

    public class ViewHolder {

        public TextView title;
        public ImageView image;

    }

}
