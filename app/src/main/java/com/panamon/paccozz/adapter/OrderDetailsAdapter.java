package com.panamon.paccozz.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.panamon.paccozz.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Intern on 1/25/2017.
 */

public class OrderDetailsAdapter extends ArrayAdapter<String> {
    Context context;
   List<String> name;
   List<String> price;


    public OrderDetailsAdapter(Context context, int resource,List<String> names,List<String> pricesList) {
        super(context, resource, names);
        this.context = context;
        name = new ArrayList<>();
        name = names;
        price = new ArrayList<>();
        price = pricesList;
        Log.e("price1",price+"d");
        Log.e("price2",pricesList+"e");


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.order_details_item, null);

            TextView nameTextView = (TextView) convertView.findViewById(R.id.name);
            TextView priceTextView = (TextView) convertView.findViewById(R.id.price);
            nameTextView.setText(name.get(position));
            priceTextView.setText("₹ "+price.get(position));

            Log.e("price2",price+"d");





        }


        return convertView;
    }
}


