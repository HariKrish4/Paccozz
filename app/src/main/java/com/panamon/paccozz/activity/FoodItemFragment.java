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
import android.widget.TextView;

import com.panamon.paccozz.R;
import com.panamon.paccozz.adapter.FoodItemAdapter;
import com.panamon.paccozz.common.Singleton;
import com.panamon.paccozz.dbadater.FoodItemDBAdapter;
import com.panamon.paccozz.model.FoodItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hari on 11/18/2016.
 */

public class FoodItemFragment extends Fragment {

    private RecyclerView vendorFoodLists;
    private FoodItemAdapter foodItemAdapter;
    private FoodItemDBAdapter foodItemDBAdapter;
    private List<FoodItemModel> foodItemModels;
    private RadioButton radioVeg,radioNonVeg;
    private TextView noDataTxt;
    private String categoryId;

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
                if(b){
                    displayFoodItems("2");
                }
                else{
                    displayFoodItems("1");
                }
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
        foodItemModels = foodItemDBAdapter.getFoodItems(categoryId);
        foodItemAdapter = new FoodItemAdapter(Singleton.getInstance().context, foodItemModels);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().context, LinearLayoutManager.VERTICAL, false);
        vendorFoodLists.setLayoutManager(mLayoutManager);
        vendorFoodLists.setAdapter(foodItemAdapter);
        noDataTxt.setVisibility(View.GONE);
        if(foodItemModels.size()==0){
            noDataTxt.setVisibility(View.VISIBLE);
        }
    }


    private void displayFoodItems(String itemType) {
        foodItemModels = foodItemDBAdapter.getFoodItems(categoryId,itemType);
        foodItemAdapter = new FoodItemAdapter(Singleton.getInstance().context, foodItemModels);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().context, LinearLayoutManager.VERTICAL, false);
        vendorFoodLists.setLayoutManager(mLayoutManager);
        vendorFoodLists.setAdapter(foodItemAdapter);
        noDataTxt.setVisibility(View.GONE);
        if(foodItemModels.size()==0){
            noDataTxt.setVisibility(View.VISIBLE);
        }
    }
}
