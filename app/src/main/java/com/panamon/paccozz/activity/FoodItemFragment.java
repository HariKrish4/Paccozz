package com.panamon.paccozz.activity;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panamon.paccozz.R;
import com.panamon.paccozz.adapter.AddOnItemAdapter;
import com.panamon.paccozz.adapter.AddOnSubItemAdapter;
import com.panamon.paccozz.adapter.FoodItemAdapter;
import com.panamon.paccozz.common.Singleton;
import com.panamon.paccozz.dbadater.AddOnDBAdapter;
import com.panamon.paccozz.dbadater.FoodItemDBAdapter;
import com.panamon.paccozz.interfaces.AddonItemClicked;
import com.panamon.paccozz.interfaces.FoodItemChanged;
import com.panamon.paccozz.interfaces.SelectedFoodItemCountChange;
import com.panamon.paccozz.model.AddOnItemModel;
import com.panamon.paccozz.model.AddOnSubItemModel;
import com.panamon.paccozz.model.FoodItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hari on 11/18/2016.
 */

public class FoodItemFragment extends Fragment implements FoodItemChanged, AddonItemClicked {

    private RecyclerView vendorFoodLists, addOnsItemLists;
    private FoodItemAdapter foodItemAdapter;
    private FoodItemDBAdapter foodItemDBAdapter;
    private List<FoodItemModel> foodItemModels;
    private RadioButton radioVeg, radioNonVeg;
    private TextView noDataTxt;
    private String categoryId;
    private RelativeLayout bottomSheetLayout;
    private boolean addonsChoosed = false;
    private TextView addonPriceTxt;
    private double itemCost = 0;

    public FoodItemFragment() {
        // Required empty public constructor
    }

    public static FoodItemFragment newInstance(String categoryId) {
        Bundle args = new Bundle();
        args.putString("categoryid", categoryId);
        FoodItemFragment fragment = new FoodItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.food_item_fragment, container, false);
        vendorFoodLists = (RecyclerView) rootView.findViewById(R.id.vendor_item_lists);
        radioVeg = (RadioButton) rootView.findViewById(R.id.radio_veg);
        radioNonVeg = (RadioButton) rootView.findViewById(R.id.radio_nonveg);
        noDataTxt = (TextView) rootView.findViewById(R.id.no_data_txt);

        categoryId = getArguments().getString("categoryid");
        Log.e("Fragment", categoryId);
        foodItemDBAdapter = new FoodItemDBAdapter();

        radioNonVeg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    displayFoodItems("2");
                } else {
                    displayFoodItems("1");
                }
            }
        });

        //bottom sheet layout
        //behavior = BottomSheetBehavior.from(rootView.findViewById(R.id.bottomSheetLayout));
        bottomSheetLayout = (RelativeLayout) rootView.findViewById(R.id.bottomSheetLayout);
        //behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        //displaying addons
        addOnsItemLists = (RecyclerView) rootView.findViewById(R.id.addon_lists);
        TextView doneTxt = (TextView) rootView.findViewById(R.id.doneTxt);
        addonPriceTxt = (TextView) rootView.findViewById(R.id.addon_price);
        doneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetLayout.setVisibility(View.GONE);
                vendorFoodLists.setVisibility(View.VISIBLE);
                //behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        displayFoodItems();
    }

    private void displayFoodItems() {
        AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
        ArrayList<AddOnItemModel> addOnItemModels = addOnDBAdapter.getAddOnItems(categoryId);
        if (addOnItemModels.size() > 0) {
            Singleton.getInstance().AddOns = "1";
        } else {

        }
        foodItemModels = foodItemDBAdapter.getFoodItems(categoryId);
        foodItemAdapter = new FoodItemAdapter(Singleton.getInstance().context, foodItemModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().context, LinearLayoutManager.VERTICAL, false);
        vendorFoodLists.setLayoutManager(mLayoutManager);
        vendorFoodLists.setAdapter(foodItemAdapter);
        noDataTxt.setVisibility(View.GONE);
        if (foodItemModels.size() == 0) {
            noDataTxt.setVisibility(View.VISIBLE);
        }
    }


    private void displayFoodItems(String itemType) {
        foodItemModels = foodItemDBAdapter.getFoodItems(categoryId, itemType);
        foodItemAdapter = new FoodItemAdapter(Singleton.getInstance().context, foodItemModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().context, LinearLayoutManager.VERTICAL, false);
        vendorFoodLists.setLayoutManager(mLayoutManager);
        vendorFoodLists.setAdapter(foodItemAdapter);
        noDataTxt.setVisibility(View.GONE);
        if (foodItemModels.size() == 0) {
            noDataTxt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFoodItemCountChanged(String itemId, int itemCost) {
        if (!addonsChoosed && Singleton.getInstance().AddOns.equalsIgnoreCase("1")) {
            //behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            this.itemCost = itemCost;
            bottomSheetLayout.setVisibility(View.VISIBLE);
            vendorFoodLists.setVisibility(View.GONE);
            AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
            ArrayList<AddOnItemModel> addOnItemModels = addOnDBAdapter.getAddOnItems(itemId);
            AddOnItemAdapter addOnItemAdapter = new AddOnItemAdapter(Singleton.getInstance().context, addOnItemModels, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().context, LinearLayoutManager.VERTICAL, false);
            addOnsItemLists.setLayoutManager(mLayoutManager);
            addOnsItemLists.setAdapter(addOnItemAdapter);
            addonsChoosed = true;
        }

    }

    @Override
    public void onCustomizationClicked(String itemId) {
        bottomSheetLayout.setVisibility(View.VISIBLE);
        vendorFoodLists.setVisibility(View.GONE);
        AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
        ArrayList<AddOnItemModel> addOnItemModels = addOnDBAdapter.getAddOnItems(itemId);
        AddOnItemAdapter addOnItemAdapter = new AddOnItemAdapter(Singleton.getInstance().context, addOnItemModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().context, LinearLayoutManager.VERTICAL, false);
        addOnsItemLists.setLayoutManager(mLayoutManager);
        addOnsItemLists.setAdapter(addOnItemAdapter);
    }

    @Override
    public void onAddonItemClicked(String addonId) {
        AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
        ArrayList<AddOnSubItemModel> addOnSubItemModels = addOnDBAdapter.getAddOnSubItems(addonId);
        AddOnSubItemAdapter addOnSubItemAdapter = new AddOnSubItemAdapter(Singleton.getInstance().context, addOnSubItemModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().context, LinearLayoutManager.VERTICAL, false);
        addOnsItemLists.setLayoutManager(mLayoutManager);
        addOnsItemLists.setAdapter(addOnSubItemAdapter);
    }

    @Override
    public void onAddonSubItemClicked(String subItemCost, String addOnId) {
        addonPriceTxt.setText("₹" + subItemCost);
        itemCost = itemCost + Double.parseDouble(subItemCost);
        int itemCostInt = (int) itemCost;
        foodItemDBAdapter.updateTotalCost(itemCostInt + "", addOnId);
        foodItemDBAdapter.updateItemCost(itemCostInt + "", addOnId);
        displayFoodItems();
    }
}
