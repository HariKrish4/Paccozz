package com.panamon.paccozz.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.panamon.paccozz.R;
import com.panamon.paccozz.common.Singleton;
import com.panamon.paccozz.dbadater.AddOnDBAdapter;
import com.panamon.paccozz.model.AddOnItemModel;
import com.panamon.paccozz.model.AddOnSubItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hari on 11/30/2016.
 */

public class AddOnItemAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<AddOnItemModel> addOnItemModels;

    public AddOnItemAdapter(Context context, List<AddOnItemModel> addOnItemModels) {
        this.context = context;
        this.addOnItemModels = addOnItemModels;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.addon_item, null);
        return new MyViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        AddOnItemModel addOnItemModel = addOnItemModels.get(position);
        myViewHolder.HeaderTxt.setText(addOnItemModel.AddOnName);
        AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
        ArrayList<AddOnSubItemModel> addOnSubItemModels = addOnDBAdapter.getAddOnSubItems(addOnItemModel.AddOnId);
        AddOnSubItemAdapter addOnSubItemAdapter = new AddOnSubItemAdapter(Singleton.getInstance().context, addOnSubItemModels);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().context, LinearLayoutManager.VERTICAL, false);
        myViewHolder.AddOnSubItemLists.setLayoutManager(mLayoutManager);
        myViewHolder.AddOnSubItemLists.setAdapter(addOnSubItemAdapter);
    }

    @Override
    public int getItemCount() {
        return addOnItemModels.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView HeaderTxt;
        public ImageView ExpandImg;
        public RecyclerView AddOnSubItemLists;

        public MyViewHolder(View itemView) {
            super(itemView);
            HeaderTxt = (TextView) itemView.findViewById(R.id.headerTxt);
            ExpandImg = (ImageView) itemView.findViewById(R.id.expandImg);
            AddOnSubItemLists = (RecyclerView) itemView.findViewById(R.id.addonSubItem_lists);
        }
    }
}
