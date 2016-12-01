package com.panamon.paccozz.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panamon.paccozz.R;
import com.panamon.paccozz.common.Singleton;
import com.panamon.paccozz.dbadater.AddOnDBAdapter;
import com.panamon.paccozz.interfaces.AddonItemClicked;
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
    private AddonItemClicked addonItemClicked;

    public AddOnItemAdapter(Context context, List<AddOnItemModel> addOnItemModels, AddonItemClicked addonItemClicked) {
        this.context = context;
        this.addOnItemModels = addOnItemModels;
        this.addonItemClicked = addonItemClicked;
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
        final AddOnItemModel addOnItemModel = addOnItemModels.get(position);
        myViewHolder.HeaderTxt.setText(addOnItemModel.AddOnName);
        myViewHolder.ItemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addonItemClicked.onAddonItemClicked(addOnItemModel.AddOnId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addOnItemModels.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView HeaderTxt;
        public ImageView ExpandImg;
        public RelativeLayout ItemLL;
        public RecyclerView AddOnSubItemLists;

        public MyViewHolder(View itemView) {
            super(itemView);
            HeaderTxt = (TextView) itemView.findViewById(R.id.headerTxt);
            ExpandImg = (ImageView) itemView.findViewById(R.id.expandImg);
            ItemLL = (RelativeLayout) itemView.findViewById(R.id.item_ll);
            AddOnSubItemLists = (RecyclerView) itemView.findViewById(R.id.addonSubItem_lists);
        }
    }
}
