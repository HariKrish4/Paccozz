package com.panamon.paccozz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.panamon.paccozz.R;
import com.panamon.paccozz.adapter.AddOnItemAdapter;
import com.panamon.paccozz.adapter.AddOnSubItemAdapter;
import com.panamon.paccozz.adapter.SelectedFoodItemAdapter;
import com.panamon.paccozz.common.Constants;
import com.panamon.paccozz.common.Singleton;
import com.panamon.paccozz.dbadater.AddOnDBAdapter;
import com.panamon.paccozz.dbadater.FoodItemDBAdapter;
import com.panamon.paccozz.interfaces.AddonItemClicked;
import com.panamon.paccozz.interfaces.FoodItemChanged;
import com.panamon.paccozz.model.AddOnItemModel;
import com.panamon.paccozz.model.AddOnSubItemModel;
import com.panamon.paccozz.model.FoodItemModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderReviewActivity extends AppCompatActivity implements FoodItemChanged,AddonItemClicked, View.OnClickListener {

    private RecyclerView selectedItemLists;
    private TextView textView_item, textView_proceed, textView_discount, textView_taxprice, textView_totalprice, textView_vendor_name;
    private EditText editText_applycoupon;
    private ImageView textView_apply;
    private int discount = 10, tax = 12;
    private FoodItemDBAdapter foodItemDBAdapter;
    private ImageView arrow;
    private double totalCost = 0.00;
    private ProgressBar progressBar;
    private LinearLayout bottomSheetLayout;
    private TextView addonPriceTxt, mainItemPriceTxt;
    private RecyclerView addOnsItemLists;
    private double itemCost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //views
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textView_vendor_name = (TextView) findViewById(R.id.vendor_name_txt);
        textView_item = (TextView) findViewById(R.id.item_txt);
        textView_discount = (TextView) findViewById(R.id.discount_txt);
        textView_taxprice = (TextView) findViewById(R.id.tax_txt);
        textView_totalprice = (TextView) findViewById(R.id.total_txt);
        editText_applycoupon = (EditText) findViewById(R.id.enter_coupon__txt);
        textView_apply = (ImageView) findViewById(R.id.apply_img);
        textView_proceed = (TextView) findViewById(R.id.textView_proceed);
        textView_proceed.setOnClickListener(this);
        textView_apply.setOnClickListener(this);
        arrow = (ImageView) findViewById(R.id.arrow);
        arrow.setOnClickListener(this);
        textView_apply.setOnClickListener(this);

        //displying seleced food items
        selectedItemLists = (RecyclerView) findViewById(R.id.food_item_lists);
        foodItemDBAdapter = new FoodItemDBAdapter();
        ArrayList<FoodItemModel> foodItemModels = foodItemDBAdapter.getSelectedFoodItems();

        SelectedFoodItemAdapter foodItemAdapter = new SelectedFoodItemAdapter(this, foodItemModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        selectedItemLists.setLayoutManager(mLayoutManager);
        selectedItemLists.setAdapter(foodItemAdapter);

        textView_vendor_name.setText(Singleton.getInstance().VendorName);

        calculateTotalCost();

        //bottom sheet layout
        bottomSheetLayout = (LinearLayout) findViewById(R.id.bottomSheetLayout);

        //displaying addons
        addOnsItemLists = (RecyclerView) findViewById(R.id.addon_lists);
        TextView doneTxt = (TextView) findViewById(R.id.doneTxt);
        addonPriceTxt = (TextView) findViewById(R.id.addon_price);
        mainItemPriceTxt = (TextView) findViewById(R.id.main_price);
        doneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetLayout.setVisibility(View.GONE);
                //behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }

    private void calculateTotalCost() {
        double totalItems = Double.parseDouble(foodItemDBAdapter.getTotalItems());
        totalCost = Double.parseDouble(foodItemDBAdapter.getTotalCost());
        int totalInt = (int) totalItems;
        double discountCost = (discount * totalCost / 100);
        double taxCost = (tax * totalCost / 100);
        totalCost = totalCost + taxCost;
        totalCost = totalCost - discountCost;
        textView_item.setText(totalInt + "");
        textView_totalprice.setText("₹ " + String.format("%.2f", totalCost));
    }

    @Override
    public void onFoodItemCountChanged() {
        calculateTotalCost();
    }

    @Override
    public void onFoodItemCountChanged(String itemId, int itemCost,int itemCount,boolean plus) {

    }

    @Override
    public void onCustomizationClicked(String itemId, int itemCost) {
        bottomSheetLayout.setVisibility(View.VISIBLE);
        this.itemCost = itemCost;
        AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
        ArrayList<AddOnItemModel> addOnItemModels = addOnDBAdapter.getAddOnItems(itemId);
        AddOnItemAdapter addOnItemAdapter = new AddOnItemAdapter(Singleton.getInstance().context, addOnItemModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().context, LinearLayoutManager.VERTICAL, false);
        addOnsItemLists.setLayoutManager(mLayoutManager);
        addOnsItemLists.setAdapter(addOnItemAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_proceed:
                Intent payment = new Intent(this, PaymentActivity.class);
                payment.putExtra("amount", totalCost);
                startActivity(payment);
                break;
            case R.id.apply_img:
                if (editText_applycoupon.getText().toString().length() > 0) {
                    applyCoupon();
                } else {
                    Snackbar.make(editText_applycoupon, "Please enter the coupon value", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            case R.id.arrow:
                onBackPressed();
                break;
        }
    }

    private void applyCoupon() {
        progressBar.setVisibility(View.VISIBLE);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.COUPON_URL;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("response", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equalsIgnoreCase("1")) {
                                int grnd_total = Integer.parseInt(jsonObject.getString("amount"));
                                String cupid = jsonObject.getString("couponid");
                                String message = jsonObject.getString("message");
                                textView_totalprice.setText(grnd_total+"");
                            } else {
                                Snackbar.make(editText_applycoupon, "Invalid Coupon", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(editText_applycoupon, error.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Singleton.getInstance().UserId);
                params.put("coupon", editText_applycoupon.getText().toString());
                params.put("amount", totalCost + "");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
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
    public void onAddonSubItemClicked(String subItemCost, String addOnItemId) {
        addonPriceTxt.setText("Addon Price:₹" + subItemCost);
        mainItemPriceTxt.setText("Main item price : ₹" + itemCost);
        itemCost = itemCost + Double.parseDouble(subItemCost);
        int itemCostInt = (int) itemCost;
        foodItemDBAdapter.updateTotalCost(itemCostInt + "", addOnItemId);
        //foodItemDBAdapter.updateItemCost(itemCostInt + "", addOnItemId);
    }
}
