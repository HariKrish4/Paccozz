package com.panamon.paccozz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.panamon.paccozz.R;
import com.panamon.paccozz.model.AddOnItemModel;
import com.panamon.paccozz.model.AddOnSubItemModel;

import java.util.List;

/**
 * Created by Hari on 11/30/2016.
 */

public class AddOnSubItemAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<AddOnSubItemModel> addOnSubItemModels;

    public AddOnSubItemAdapter(Context context, List<AddOnSubItemModel> addOnSubItemModels) {
        this.context = context;
        this.addOnSubItemModels = addOnSubItemModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.addon_subitem, null);
        return new MyViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        AddOnSubItemModel addOnSubItemModel = addOnSubItemModels.get(position);
        myViewHolder.HeaderTxt.setText(addOnSubItemModel.AddOnSubItemName);
    }

    @Override
    public int getItemCount() {
        return addOnSubItemModels.size();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView HeaderTxt;

        public MyViewHolder(View itemView) {
            super(itemView);
            HeaderTxt = (TextView) itemView.findViewById(R.id.headerTxt);

        }
    }
}
