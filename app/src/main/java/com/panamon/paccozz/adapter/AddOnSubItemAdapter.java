package com.panamon.paccozz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.panamon.paccozz.R;
import com.panamon.paccozz.dbadater.AddOnDBAdapter;
import com.panamon.paccozz.interfaces.AddonItemClicked;
import com.panamon.paccozz.model.AddOnItemModel;
import com.panamon.paccozz.model.AddOnSubItemModel;

import java.util.List;

/**
 * Created by Hari on 11/30/2016.
 */

public class AddOnSubItemAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<AddOnSubItemModel> addOnSubItemModels;
    private AddonItemClicked addonItemClicked;

    public AddOnSubItemAdapter(Context context, List<AddOnSubItemModel> addOnSubItemModels, AddonItemClicked addonItemClicked) {
        this.context = context;
        this.addOnSubItemModels = addOnSubItemModels;
        this.addonItemClicked = addonItemClicked;
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
        final AddOnSubItemModel addOnSubItemModel = addOnSubItemModels.get(position);
        final AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
        myViewHolder.HeaderTxt.setText(addOnSubItemModel.AddOnSubItemName);
        myViewHolder.PriceTxt.setText("₹ "+addOnSubItemModel.AddOnPrice);
        myViewHolder.SelectedBtn.setTag(position);
        myViewHolder.SelectedBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int pos = (int) compoundButton.getTag();
                AddOnSubItemModel addOnClickedSubItemModel =  addOnSubItemModels.get(pos);
                if(b){
                    addOnDBAdapter.updateIsItemSelected("1",addOnClickedSubItemModel.AddOnSubItemId);
                    String cost = addOnDBAdapter.getTotalSubItemCost();
                    addonItemClicked.onAddonSubItemClicked(cost,addOnClickedSubItemModel.AddOnCateroryId);
                }else{
                    addOnDBAdapter.updateIsItemSelected("0",addOnClickedSubItemModel.AddOnSubItemId);
                    String cost = addOnDBAdapter.getTotalSubItemCost();
                    addonItemClicked.onAddonSubItemClicked(cost,addOnClickedSubItemModel.AddOnCateroryId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return addOnSubItemModels.size();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView HeaderTxt, PriceTxt;
        public RadioButton SelectedBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            HeaderTxt = (TextView) itemView.findViewById(R.id.headerTxt);
            PriceTxt = (TextView) itemView.findViewById(R.id.priceTxt);
            SelectedBtn = (RadioButton) itemView.findViewById(R.id.radio_selected);
        }
    }
}
