package com.example.adm.pantallas;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by NgocTri on 10/22/2016.
 */

public class Tab1GridViewAdapter extends ArrayAdapter<Tab1Product> {
    public Tab1GridViewAdapter(Context context, int resource, List<Tab1Product> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.main_menu_tab1_grid_item, null);
        }
        Tab1Product product = getItem(position);
        ImageView img = (ImageView) v.findViewById(R.id.imageView);
        TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) v.findViewById(R.id.txtDescription);

        img.setImageResource(product.getImageId());
        txtTitle.setText(product.getTitle());
        //txtDescription.setText(product.getDescription());

        return v;
    }
}
