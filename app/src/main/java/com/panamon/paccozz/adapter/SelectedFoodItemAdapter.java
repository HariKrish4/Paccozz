package com.panamon.paccozz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panamon.paccozz.R;
import com.panamon.paccozz.common.Singleton;
import com.panamon.paccozz.dbadater.AddOnDBAdapter;
import com.panamon.paccozz.dbadater.FoodItemDBAdapter;
import com.panamon.paccozz.interfaces.FoodItemChanged;
import com.panamon.paccozz.model.AddOnItemModel;
import com.panamon.paccozz.model.AddOnSubItemModel;
import com.panamon.paccozz.model.FoodItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hari on 11/22/2016.
 */

public class SelectedFoodItemAdapter extends RecyclerView.Adapter {

    private Context context;
    private int itemCount = 0, itemCost = 0,itemPackageCost = 0;
    private FoodItemChanged foodItemCountChange;
    private FoodItemDBAdapter foodItemDBAdapter;
    private List<FoodItemModel> foodItemModels;
    private ArrayList<AddOnSubItemModel> addOnSubItemModels;
    private LayoutInflater inflater;

    public SelectedFoodItemAdapter(Context context, List<FoodItemModel> foodItemModels, FoodItemChanged foodItemCountChange) {
        this.context = context;
        this.foodItemModels = foodItemModels;
        this.foodItemCountChange = foodItemCountChange;
        addOnSubItemModels = new ArrayList<>();
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.selected_food_items, null);
        return new MyViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        final FoodItemModel foodItemModel = foodItemModels.get(position);
        myViewHolder.ItemCount.setText(foodItemModel.ItemCount);
        myViewHolder.ItemTitle.setText(foodItemModel.ItemName);
        myViewHolder.ItemRate.setText(foodItemModel.ItemTotalCost);
        myViewHolder.TxtPlus.setTag(position);
        myViewHolder.TxtMinus.setTag(position);
        myViewHolder.TxtCustomization.setTag(position);
        itemCount = Integer.parseInt(foodItemModel.ItemCount);
        foodItemDBAdapter = new FoodItemDBAdapter();
        myViewHolder.TxtPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                FoodItemModel clikedFoodItemModel = foodItemModels.get(pos);
                String itemId = clikedFoodItemModel.ItemId;
                itemCount = Integer.parseInt(foodItemDBAdapter.getItemCount(itemId));
                itemCost = Integer.parseInt(foodItemDBAdapter.getItemTotalCost(itemId));
                itemPackageCost = Integer.parseInt(clikedFoodItemModel.ItemPackageCharge);
                itemCount++;
                calculateTotalCost(myViewHolder, clikedFoodItemModel);
            }
        });

        myViewHolder.TxtMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                FoodItemModel clikedFoodItemModel = foodItemModels.get(pos);
                String itemId = clikedFoodItemModel.ItemId;
                itemCount = Integer.parseInt(foodItemDBAdapter.getItemCount(itemId));
                itemCost = Integer.parseInt(foodItemDBAdapter.getItemTotalCost(itemId));
                itemPackageCost = Integer.parseInt(clikedFoodItemModel.ItemPackageCharge);
                itemCount--;
                if (itemCount >= 0) {
                    calculateTotalCost(myViewHolder, clikedFoodItemModel);
                    if (itemCount == 0) {
                        foodItemDBAdapter.updateIsItemSelected("0", clikedFoodItemModel.ItemId);
                    }
                }
            }
        });

        myViewHolder.TxtCustomization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                FoodItemModel clikedFoodItemModel = foodItemModels.get(pos);
                String itemId = clikedFoodItemModel.ItemId;
                itemCost = Integer.parseInt(foodItemDBAdapter.getItemTotalCost(itemId));
                foodItemCountChange.onCustomizationClicked(clikedFoodItemModel.ItemId, itemCost);
            }
        });

        AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
        ArrayList<AddOnItemModel> addOnItemModels = addOnDBAdapter.getAddOnItems(foodItemModel.ItemId);
        if (addOnItemModels.size() > 0) {
            myViewHolder.TxtCustomization.setVisibility(View.VISIBLE);
            for (int i = 0; i < addOnItemModels.size(); i++) {
                addOnSubItemModels.addAll(addOnDBAdapter.getAddOnSelectedSubItems(addOnItemModels.get(i).AddOnId));
            }

            for (int i = 0; i < addOnSubItemModels.size(); i++) {
                View subItemView = inflater.inflate(R.layout.addon_list_item,
                        myViewHolder.food_item_ll, false);
                TextView subItemNameTxt = (TextView) subItemView.findViewById(R.id.addonName_txt);
                subItemNameTxt.setText(addOnSubItemModels.get(i).AddOnSubItemName);
                myViewHolder.addon_listsLL.addView(subItemView);
            }
            addOnSubItemModels.clear();
        } else {
            myViewHolder.TxtCustomization.setVisibility(View.GONE);
        }
    }

    private void calculateTotalCost(MyViewHolder myViewHolder, FoodItemModel clikedFoodItemModel) {
        myViewHolder.ItemCount.setText(itemCount + "");
        int totalCost = itemCost * itemCount;
        int packageCost = itemPackageCost * itemCount;
        foodItemDBAdapter.updateItemCount(itemCount + "", clikedFoodItemModel.ItemId);
        foodItemDBAdapter.updateTotalCost(totalCost + "", clikedFoodItemModel.ItemId);
        foodItemDBAdapter.updatePackageCharge(packageCost+"",clikedFoodItemModel.ItemId);
        foodItemCountChange.onFoodItemCountChanged();
    }

    @Override
    public int getItemCount() {
        return foodItemModels.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView ItemTitle;
        public TextView ItemRate;
        public TextView ItemCount;
        public ImageView TxtPlus;
        public ImageView TxtMinus;
        public TextView TxtCustomization;
        public LinearLayout addon_listsLL;
        public RelativeLayout food_item_ll;

        public MyViewHolder(View itemView) {
            super(itemView);
            ItemTitle = (TextView) itemView.findViewById(R.id.item_title_txt);
            ItemRate = (TextView) itemView.findViewById(R.id.item_price_txt);
            ItemCount = (TextView) itemView.findViewById(R.id.item_count_txt);
            TxtMinus = (ImageView) itemView.findViewById(R.id.minus);
            TxtPlus = (ImageView) itemView.findViewById(R.id.plus);
            TxtCustomization = (TextView) itemView.findViewById(R.id.customization_txt);
            food_item_ll = (RelativeLayout) itemView.findViewById(R.id.food_item_ll);
            addon_listsLL = (LinearLayout) itemView.findViewById(R.id.addon_lists);
        }
    }
}
