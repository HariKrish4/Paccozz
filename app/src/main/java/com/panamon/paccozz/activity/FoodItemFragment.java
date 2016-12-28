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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.panamon.paccozz.R;
import com.panamon.paccozz.adapter.AddOnItemAdapter;
import com.panamon.paccozz.adapter.AddOnSubItemAdapter;
import com.panamon.paccozz.adapter.FoodItemAdapter;
import com.panamon.paccozz.common.Singleton;
import com.panamon.paccozz.common.Utilities;
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

public class FoodItemFragment extends Fragment implements FoodItemChanged, AddonItemClicked, View.OnClickListener {

    private RecyclerView vendorFoodLists, addOnsItemLists;
    private FoodItemAdapter foodItemAdapter;
    private FoodItemDBAdapter foodItemDBAdapter;
    private List<FoodItemModel> foodItemModels;
    private RadioButton radioVeg, radioNonVeg;
    private ImageView allBtn, nonBtn, vegBtn;
    private TextView noDataTxt;
    private String categoryId;
    private RelativeLayout bottomSheetLayout;
    private TextView itemPriceTxt, doneTxt;
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
        allBtn = (ImageView) rootView.findViewById(R.id.allBtn);
        nonBtn = (ImageView) rootView.findViewById(R.id.nonBtn);
        vegBtn = (ImageView) rootView.findViewById(R.id.vegBtn);
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

        nonBtn.setOnClickListener(this);
        vegBtn.setOnClickListener(this);
        allBtn.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        displayFoodItems();
    }

    private void displayFoodItems() {
        foodItemModels = foodItemDBAdapter.getFoodItems(categoryId);
        foodItemAdapter = new FoodItemAdapter(this.getActivity(), foodItemModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().Context, LinearLayoutManager.VERTICAL, false);
        vendorFoodLists.setLayoutManager(mLayoutManager);
        vendorFoodLists.setAdapter(foodItemAdapter);
        noDataTxt.setVisibility(View.GONE);
        if (foodItemModels.size() == 0) {
            noDataTxt.setVisibility(View.VISIBLE);
        }
    }


    private void displayFoodItems(String itemType) {
        foodItemModels = foodItemDBAdapter.getFoodItems(categoryId, itemType);
        foodItemAdapter = new FoodItemAdapter(this.getActivity(), foodItemModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().Context, LinearLayoutManager.VERTICAL, false);
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
            foodItemDBAdapter.insertCartItem(Singleton.getInstance().VendorId);
            if (Singleton.getInstance().AddOns.equalsIgnoreCase("1")) {
                AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
                ArrayList<AddOnItemModel> addOnItemModels = addOnDBAdapter.getAddOnItems(itemId);
                if (addOnItemModels.size() > 0) {
                    //behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    this.itemCost = itemCost;
                    itemPriceTxt.setText("Item price : ₹" + itemCost);
                    bottomSheetLayout.setVisibility(View.VISIBLE);
                    vendorFoodLists.setVisibility(View.GONE);
                    AddOnItemAdapter addOnItemAdapter = new AddOnItemAdapter(Singleton.getInstance().Context, addOnItemModels, this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().Context, LinearLayoutManager.VERTICAL, false);
                    addOnsItemLists.setLayoutManager(mLayoutManager);
                    addOnsItemLists.setAdapter(addOnItemAdapter);
                    doneTxt.setText("Done");
                    doneClick(itemId);
                    HotelDetailsActivity.fab.setVisibility(View.GONE);
                }
            }
        }
    }

    private void doneClick(final String itemId) {
        doneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (doneTxt.getText().toString().equalsIgnoreCase("Apply")) {
                    bottomSheetLayout.setVisibility(View.VISIBLE);
                    vendorFoodLists.setVisibility(View.GONE);
                    AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
                    ArrayList<AddOnItemModel> addOnItemModels = addOnDBAdapter.getAddOnItems(itemId);
                    AddOnItemAdapter addOnItemAdapter = new AddOnItemAdapter(Singleton.getInstance().Context, addOnItemModels, FoodItemFragment.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().Context, LinearLayoutManager.VERTICAL, false);
                    addOnsItemLists.setLayoutManager(mLayoutManager);
                    addOnsItemLists.setAdapter(addOnItemAdapter);
                    doneTxt.setText("Done");
                } else {
                    bottomSheetLayout.setVisibility(View.GONE);
                    vendorFoodLists.setVisibility(View.VISIBLE);
                    HotelDetailsActivity.fab.setVisibility(View.VISIBLE);
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
        AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
        ArrayList<AddOnItemModel> addOnItemModels = addOnDBAdapter.getAddOnItems(itemId);
        AddOnItemAdapter addOnItemAdapter = new AddOnItemAdapter(Singleton.getInstance().Context, addOnItemModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().Context, LinearLayoutManager.VERTICAL, false);
        addOnsItemLists.setLayoutManager(mLayoutManager);
        addOnsItemLists.setAdapter(addOnItemAdapter);
        HotelDetailsActivity.fab.setVisibility(View.GONE);
        doneTxt.setText("Done");
        doneClick(itemId);
    }

    @Override
    public void onFoodItemCountChanged() {

    }

    @Override
    public void onAddonItemClicked(String addonId) {
        AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
        ArrayList<AddOnSubItemModel> addOnSubItemModels = addOnDBAdapter.getAddOnSubItems(addonId);
        AddOnSubItemAdapter addOnSubItemAdapter = new AddOnSubItemAdapter(Singleton.getInstance().Context, addOnSubItemModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().Context, LinearLayoutManager.VERTICAL, false);
        addOnsItemLists.setLayoutManager(mLayoutManager);
        addOnsItemLists.setAdapter(addOnSubItemAdapter);
        doneTxt.setText("Apply");
    }

    @Override
    public void onAddonSubItemClicked(String subItemCost, String addOnId, String cost, boolean isChecked) {
        if (isChecked) {
            itemCost = itemCost + Double.parseDouble(subItemCost);
        } else {
            itemCost = itemCost - Double.parseDouble(cost);
        }
        int itemCostInt = (int) itemCost;
        itemPriceTxt.setText("Item price : ₹" + itemCostInt);
        foodItemDBAdapter.updateTotalCost(itemCostInt + "", addOnId);
        foodItemDBAdapter.updateItemTotalCost(itemCostInt + "", addOnId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nonBtn:
                nonBtn.setImageResource(R.drawable.nv2);
                vegBtn.setImageResource(R.drawable.veg1);
                allBtn.setImageResource(R.drawable.all1);
                displayFoodItems("2");
                break;
            case R.id.vegBtn:
                nonBtn.setImageResource(R.drawable.nv1);
                vegBtn.setImageResource(R.drawable.veg2);
                allBtn.setImageResource(R.drawable.all1);
                displayFoodItems("1");
                break;
            case R.id.allBtn:
                nonBtn.setImageResource(R.drawable.nv1);
                vegBtn.setImageResource(R.drawable.veg1);
                allBtn.setImageResource(R.drawable.all2);
                displayFoodItems();
                break;
        }
    }
}
