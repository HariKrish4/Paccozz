package com.panamon.paccozz.activity;

import android.os.Bundle;
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
    private TextView itemPriceTxt,doneTxt;
    private double itemCost = 0;
    private boolean addonSubitemsIsShown = false;

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
        doneTxt = (TextView) rootView.findViewById(R.id.doneTxt);
        itemPriceTxt = (TextView) rootView.findViewById(R.id.main_price);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        displayFoodItems();
    }

    private void displayFoodItems() {
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
    public void onFoodItemCountChanged(String itemId, int itemCost, int itemCount, boolean plus) {
        if (itemCount == 1 && plus) {
            if (Singleton.getInstance().AddOns.equalsIgnoreCase("1")) {
                AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
                ArrayList<AddOnItemModel> addOnItemModels = addOnDBAdapter.getAddOnItems(itemId);
                if (addOnItemModels.size() > 0) {
                    //behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    this.itemCost = itemCost;
                    itemPriceTxt.setText("Item price : ₹" + itemCost);
                    bottomSheetLayout.setVisibility(View.VISIBLE);
                    vendorFoodLists.setVisibility(View.GONE);
                    AddOnItemAdapter addOnItemAdapter = new AddOnItemAdapter(Singleton.getInstance().context, addOnItemModels, this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().context, LinearLayoutManager.VERTICAL, false);
                    addOnsItemLists.setLayoutManager(mLayoutManager);
                    addOnsItemLists.setAdapter(addOnItemAdapter);
                    doneClick(itemId);
                }
            }
        }
    }

    private void doneClick(final String itemId) {
        doneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addonSubitemsIsShown){
                    addonSubitemsIsShown = false;
                    doneTxt.setText("Apply");
                    bottomSheetLayout.setVisibility(View.VISIBLE);
                    vendorFoodLists.setVisibility(View.GONE);
                    AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
                    ArrayList<AddOnItemModel> addOnItemModels = addOnDBAdapter.getAddOnItems(itemId);
                    AddOnItemAdapter addOnItemAdapter = new AddOnItemAdapter(Singleton.getInstance().context, addOnItemModels, FoodItemFragment.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().context, LinearLayoutManager.VERTICAL, false);
                    addOnsItemLists.setLayoutManager(mLayoutManager);
                    addOnsItemLists.setAdapter(addOnItemAdapter);
                }
                else {
                    bottomSheetLayout.setVisibility(View.GONE);
                    vendorFoodLists.setVisibility(View.VISIBLE);
                    doneTxt.setText("Done");
                }
                //behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

    }

    @Override
    public void onCustomizationClicked(String itemId, int itemCost) {
        bottomSheetLayout.setVisibility(View.VISIBLE);
        vendorFoodLists.setVisibility(View.GONE);
        this.itemCost = itemCost;
        itemPriceTxt.setText("Item price : ₹" + itemCost);
        addonSubitemsIsShown = false;
        AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
        ArrayList<AddOnItemModel> addOnItemModels = addOnDBAdapter.getAddOnItems(itemId);
        AddOnItemAdapter addOnItemAdapter = new AddOnItemAdapter(Singleton.getInstance().context, addOnItemModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().context, LinearLayoutManager.VERTICAL, false);
        addOnsItemLists.setLayoutManager(mLayoutManager);
        addOnsItemLists.setAdapter(addOnItemAdapter);
        doneClick(itemId);
    }

    @Override
    public void onFoodItemCountChanged() {

    }

    @Override
    public void onAddonItemClicked(String addonId) {
        addonSubitemsIsShown = true;
        AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
        ArrayList<AddOnSubItemModel> addOnSubItemModels = addOnDBAdapter.getAddOnSubItems(addonId);
        AddOnSubItemAdapter addOnSubItemAdapter = new AddOnSubItemAdapter(Singleton.getInstance().context, addOnSubItemModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().context, LinearLayoutManager.VERTICAL, false);
        addOnsItemLists.setLayoutManager(mLayoutManager);
        addOnsItemLists.setAdapter(addOnSubItemAdapter);
    }

    @Override
    public void onAddonSubItemClicked(String subItemCost, String addOnId) {
        itemCost = itemCost + Double.parseDouble(subItemCost);
        int itemCostInt = (int) itemCost;
        itemPriceTxt.setText("Item price : ₹" + itemCostInt);
        foodItemDBAdapter.updateTotalCost(itemCostInt + "", addOnId);
        //foodItemDBAdapter.updateItemCost(itemCostInt + "", addOnId);
        //displayFoodItems();
    }
}
