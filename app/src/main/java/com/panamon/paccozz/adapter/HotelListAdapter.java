package com.panamon.paccozz.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panamon.paccozz.activity.HotelDetailsActivity;
import com.panamon.paccozz.common.RoundRectCornerImageView;
import com.panamon.paccozz.common.Singleton;
import com.panamon.paccozz.model.HotelListModel;
import com.panamon.paccozz.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Hari on 11/17/2016.
 */

public class HotelListAdapter extends RecyclerView.Adapter {

    private List<HotelListModel> hotelListModels;
    private Context context;

    public HotelListAdapter(Context context, List<HotelListModel> hotelListModelList) {
        this.context = context;
        this.hotelListModels = hotelListModelList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.home_list_item, null);
        return new MyViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final HotelListModel hotelListModel = hotelListModels.get(position);
        MyViewHolder viewholder = (MyViewHolder) holder;
        Picasso.with(context).load(hotelListModel.HotelImage).placeholder(R.drawable.image_placeholder).into(viewholder.HotelImage);
        viewholder.HotelNameTxt.setText(hotelListModel.HotelName);
        viewholder.VendorListItemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Singleton.getInstance().VendorId = hotelListModel.HotelId;
                Singleton.getInstance().VendorName = hotelListModel.HotelName;
                Intent vendorDetails = new Intent(context, HotelDetailsActivity.class);
                context.startActivity(vendorDetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelListModels.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView HotelNameTxt;
        public RoundRectCornerImageView HotelImage;
        public RelativeLayout VendorListItemLL;

        public MyViewHolder(View itemView) {
            super(itemView);
            HotelNameTxt = (TextView) itemView.findViewById(R.id.hotel_name_txt);
            HotelImage = (RoundRectCornerImageView) itemView.findViewById(R.id.hotel_image);
            VendorListItemLL = (RelativeLayout) itemView.findViewById(R.id.vendor_item_ll);
        }
    }
}
