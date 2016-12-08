package com.panamon.paccozz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.panamon.paccozz.R;
import com.panamon.paccozz.common.Singleton;
import com.panamon.paccozz.dbadater.AddOnDBAdapter;
import com.panamon.paccozz.dbadater.FoodItemDBAdapter;
import com.panamon.paccozz.interfaces.FoodItemChanged;
import com.panamon.paccozz.interfaces.SelectedFoodItemCountChange;
import com.panamon.paccozz.model.AddOnItemModel;
import com.panamon.paccozz.model.FoodItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hari on 11/18/2016.
 */

public class FoodItemAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<FoodItemModel> foodItemModels;
    private int itemCount = 0, itemCost = 0;
    private FoodItemDBAdapter foodItemDBAdapter;
    private FoodItemChanged foodItemCountChange;

    public FoodItemAdapter(Context context, List<FoodItemModel> foodItemModels, FoodItemChanged foodItemCountChange) {
        this.context = context;
        this.foodItemModels = foodItemModels;
        this.foodItemCountChange = foodItemCountChange;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.food_items, null);
        return new MyViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        final FoodItemModel foodItemModel = foodItemModels.get(position);
        myViewHolder.ItemCount.setText(foodItemModel.ItemCount);
        myViewHolder.ItemTitle.setText(foodItemModel.ItemName);
        myViewHolder.ItemRate.setText(foodItemModel.ItemCost);
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
                itemCost = Integer.parseInt(foodItemDBAdapter.getItemCost(itemId));
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
                itemCost = Integer.parseInt(foodItemDBAdapter.getItemCost(itemId));
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
                itemCost = Integer.parseInt(foodItemDBAdapter.getItemCost(itemId));
                if(itemCount == 0){
                    itemCount = Integer.parseInt(foodItemDBAdapter.getItemCount(itemId));
                    itemCount++;
                    calculateTotalCost(myViewHolder, clikedFoodItemModel);
                }else {
                    itemCost = Integer.parseInt(foodItemDBAdapter.getItemCost(itemId));
                    foodItemCountChange.onCustomizationClicked(clikedFoodItemModel.ItemId,itemCost);
                }
            }
        });


        if(Singleton.getInstance().AddOns.equalsIgnoreCase("1")){
            AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
            ArrayList<AddOnItemModel> addOnItemModels = addOnDBAdapter.getAddOnItems(foodItemModel.ItemId);
            if(addOnItemModels.size()>0) {
                myViewHolder.TxtCustomization.setVisibility(View.VISIBLE);
                myViewHolder.ViewCustomization.setVisibility(View.VISIBLE);
            }
        }
        else{
            myViewHolder.TxtCustomization.setVisibility(View.GONE);
            myViewHolder.ViewCustomization.setVisibility(View.GONE);
        }
    }

    private void calculateTotalCost(MyViewHolder myViewHolder, FoodItemModel clikedFoodItemModel) {
        myViewHolder.ItemCount.setText(itemCount + "");
        int totalCost = itemCost * itemCount;
        foodItemDBAdapter.updateItemCount(itemCount + "", clikedFoodItemModel.ItemId);
        foodItemDBAdapter.updateTotalCost(totalCost + "", clikedFoodItemModel.ItemId);
        foodItemCountChange.onFoodItemCountChanged(clikedFoodItemModel.ItemId,itemCost);
    }

    @Override
    public int getItemCount() {
        return foodItemModels.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView ItemTitle;
        public TextView ItemRate;
        public TextView ItemCount;
        public TextView TxtPlus;
        public TextView TxtMinus;
        public TextView TxtCustomization;
        public View ViewCustomization;

        public MyViewHolder(View itemView) {
            super(itemView);
            ItemTitle = (TextView) itemView.findViewById(R.id.item_title_txt);
            ItemRate = (TextView) itemView.findViewById(R.id.item_price_txt);
            ItemCount = (TextView) itemView.findViewById(R.id.item_count_txt);
            TxtMinus = (TextView) itemView.findViewById(R.id.minus);
            TxtPlus = (TextView) itemView.findViewById(R.id.plus);
            TxtCustomization = (TextView) itemView.findViewById(R.id.customization_txt);
            ViewCustomization = itemView.findViewById(R.id.customization_view);
        }
    }
}
