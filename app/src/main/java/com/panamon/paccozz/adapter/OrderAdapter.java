package com.panamon.paccozz.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.panamon.paccozz.R;
import com.panamon.paccozz.activity.OrderDetailsActivity;
import com.panamon.paccozz.model.OrderHistoryModel;

import java.util.List;

/**
 * Created by Hari on 12/17/2016.
 */

public class OrderAdapter extends RecyclerView.Adapter {

    private List<OrderHistoryModel> orderHistoryModels;
    private Context context;

    public OrderAdapter(Context context, List<OrderHistoryModel> orderHistoryModels) {
        this.context = context;
        this.orderHistoryModels = orderHistoryModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.order_items, null);
        return new MyViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final OrderHistoryModel orderHistoryModel = orderHistoryModels.get(position);
        MyViewHolder viewholder = (MyViewHolder) holder;

        viewholder.VendorNameTxt.setText(orderHistoryModel.VendorName);
        viewholder.TotalCostTxt.setText("₹ "+orderHistoryModel.orgamount);
        viewholder.DateTimeTxt.setText(orderHistoryModel.OrderTime);
        viewholder.StatusTxt.setText(orderHistoryModel.OrderStatus);

        viewholder.ViewDetailsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orderDetailsIntent=new Intent(context, OrderDetailsActivity.class);
                orderDetailsIntent.putExtra("name",orderHistoryModel.VendorName);
                orderDetailsIntent.putExtra("price",orderHistoryModel.itemsrate);
                orderDetailsIntent.putExtra("orderid",orderHistoryModel.OrderId);
                orderDetailsIntent.putExtra("ordercode",orderHistoryModel.orderCode);
                orderDetailsIntent.putExtra("pushtime",orderHistoryModel.OrderTime);
                orderDetailsIntent.putExtra("status",orderHistoryModel.OrderStatus);
                orderDetailsIntent.putExtra("discount",orderHistoryModel.discount);
                orderDetailsIntent.putExtra("packagemode",orderHistoryModel.packtype);
                orderDetailsIntent.putExtra("totcost",orderHistoryModel.TotalCost);
                orderDetailsIntent.putExtra("item",orderHistoryModel.item);
                orderDetailsIntent.putExtra("itemcount",orderHistoryModel.itemcnt);
                orderDetailsIntent.putExtra("service",orderHistoryModel.service);
                orderDetailsIntent.putExtra("pay",orderHistoryModel.orgamount);
                orderDetailsIntent.putExtra("itemrate",orderHistoryModel.itemsrate);
                orderDetailsIntent.putExtra("paymode",orderHistoryModel.PayMode);
               context.startActivity(orderDetailsIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderHistoryModels.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView VendorNameTxt, TotalCostTxt, DateTimeTxt, ViewDetailsTxt, StatusTxt;
        public CardView CardView;


        public MyViewHolder(View itemView) {
            super(itemView);
            VendorNameTxt = (TextView) itemView.findViewById(R.id.vandorNameTxt);
            TotalCostTxt = (TextView) itemView.findViewById(R.id.totalAmountTxt);
            DateTimeTxt = (TextView) itemView.findViewById(R.id.dateTxt);
            ViewDetailsTxt = (TextView) itemView.findViewById(R.id.detailsTxt);
            StatusTxt = (TextView) itemView.findViewById(R.id.statusTxt);
            CardView =(CardView)itemView.findViewById(R.id.cardView);
        }
    }
}
