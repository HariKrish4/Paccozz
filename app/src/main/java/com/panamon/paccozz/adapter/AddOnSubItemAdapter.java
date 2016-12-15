package com.panamon.paccozz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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
    private int clickCount = 0;
    private AddOnDBAdapter addOnDBAdapter;

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
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        final AddOnSubItemModel addOnSubItemModel = addOnSubItemModels.get(position);
        addOnDBAdapter = new AddOnDBAdapter();
        myViewHolder.HeaderTxt.setText(addOnSubItemModel.AddOnSubItemName);
        myViewHolder.PriceTxt.setText("₹ " + addOnSubItemModel.AddOnPrice);
        myViewHolder.SelectedBtn.setTag(position);
        myViewHolder.SubItemLL.setTag(position);
        myViewHolder.SelectedBtn.setOnCheckedChangeListener(onCheckedChangeListener);

        myViewHolder.SubItemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myViewHolder.SelectedBtn.setOnCheckedChangeListener(onCheckedChangeListener);
                if (clickCount == 0) {
                    clickCount = 1;
                    myViewHolder.SelectedBtn.setChecked(true);

                } else {
                    myViewHolder.SelectedBtn.setChecked(false);
                    clickCount = 0;

                }
            }
        });
        if (addOnSubItemModel.IsItemSelected == null || addOnSubItemModel.IsItemSelected.equalsIgnoreCase("0")) {
            myViewHolder.SelectedBtn.setOnCheckedChangeListener(null);
            myViewHolder.SelectedBtn.setChecked(false);
        } else if (addOnSubItemModel.IsItemSelected.equalsIgnoreCase("1")) {
            myViewHolder.SelectedBtn.setOnCheckedChangeListener(null);
            myViewHolder.SelectedBtn.setChecked(true);
        }
        myViewHolder.SelectedBtn.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int pos = (int) compoundButton.getTag();
            AddOnSubItemModel addOnClickedSubItemModel = addOnSubItemModels.get(pos);
            if (b) {
                addOnDBAdapter.updateIsItemSelected("1", addOnClickedSubItemModel.AddOnSubItemId);
                String cost = addOnDBAdapter.getTotalSubItemCost(addOnClickedSubItemModel.AddOnSubItemId);
                addonItemClicked.onAddonSubItemClicked(cost, addOnClickedSubItemModel.AddOnCateroryId, addOnClickedSubItemModel.AddOnPrice, true);
            } else {
                addOnDBAdapter.updateIsItemSelected("0", addOnClickedSubItemModel.AddOnSubItemId);
                String cost = addOnDBAdapter.getTotalSubItemCost(addOnClickedSubItemModel.AddOnSubItemId);
                addonItemClicked.onAddonSubItemClicked(cost, addOnClickedSubItemModel.AddOnCateroryId, addOnClickedSubItemModel.AddOnPrice, false);
            }
        }
    };

    @Override
    public int getItemCount() {
        return addOnSubItemModels.size();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView HeaderTxt, PriceTxt;
        public RelativeLayout SubItemLL;
        public RadioButton SelectedBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            HeaderTxt = (TextView) itemView.findViewById(R.id.headerTxt);
            PriceTxt = (TextView) itemView.findViewById(R.id.priceTxt);
            SelectedBtn = (RadioButton) itemView.findViewById(R.id.radio_selected);
            SubItemLL = (RelativeLayout) itemView.findViewById(R.id.subitemLL);
        }
    }
}
