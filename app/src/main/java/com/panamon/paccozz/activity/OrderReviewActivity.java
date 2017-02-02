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

public class OrderReviewActivity extends AppCompatActivity implements FoodItemChanged, AddonItemClicked, View.OnClickListener {

    private RecyclerView selectedItemLists;
    private TextView textView_itemTotal, textView_wallet, textView_item, textView_proceed, textView_discount, textView_taxprice, textView_totalprice, textView_vendor_name,textView_packageCharge;
    private EditText editText_applycoupon;
    private TextView textView_apply;
    private int discount = 10, tax = 12;
    private FoodItemDBAdapter foodItemDBAdapter;
    private ImageView arrow;
    private double totalCost = 0.00;
    private ProgressBar progressBar;
    private LinearLayout bottomSheetLayout;
    private TextView itemPriceTxt, doneTxt;
    private RecyclerView addOnsItemLists;
    private double itemCost = 0;
    private ArrayList<FoodItemModel> foodItemModels;
    private LinearLayout dineInll, takeAwayll,packagell;
    private String packType = "1";
    private double taxCost,discountCost,packageCost;
    private int orgAmount;
    private LinearLayout walletll;

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
        textView_itemTotal = (TextView) findViewById(R.id.item_total_txt);
        textView_discount = (TextView) findViewById(R.id.discount_txt);
        textView_taxprice = (TextView) findViewById(R.id.tax_txt);
        textView_totalprice = (TextView) findViewById(R.id.total_txt);
        editText_applycoupon = (EditText) findViewById(R.id.enter_coupon__txt);
        textView_apply = (TextView) findViewById(R.id.apply_img);
        textView_proceed = (TextView) findViewById(R.id.textView_proceed);
        textView_packageCharge = (TextView) findViewById(R.id.item_package_txt);
        textView_wallet = (TextView) findViewById(R.id.wallet_txt);
        dineInll = (LinearLayout) findViewById(R.id.dineinll);
        takeAwayll = (LinearLayout) findViewById(R.id.takeawayll);
        packagell = (LinearLayout) findViewById(R.id.package_ll);
        walletll = (LinearLayout) findViewById(R.id.wallet_ll);
        arrow = (ImageView) findViewById(R.id.arrow);
        textView_proceed.setOnClickListener(this);
        textView_apply.setOnClickListener(this);
        dineInll.setOnClickListener(this);
        takeAwayll.setOnClickListener(this);
        arrow.setOnClickListener(this);
        textView_apply.setOnClickListener(this);

        //displying seleced food items
        selectedItemLists = (RecyclerView) findViewById(R.id.food_item_lists);

        foodItemDBAdapter = new FoodItemDBAdapter();
        showFoodItems();

        //bottom sheet layout
        bottomSheetLayout = (LinearLayout) findViewById(R.id.bottomSheetLayout);

        //displaying addons
        addOnsItemLists = (RecyclerView) findViewById(R.id.addon_lists);
        doneTxt = (TextView) findViewById(R.id.doneTxt);
        itemPriceTxt = (TextView) findViewById(R.id.main_price);

    }

    private void showFoodItems() {
        foodItemModels = foodItemDBAdapter.getSelectedFoodItems();
        if (foodItemModels.size() > 0) {
            textView_vendor_name.setText(foodItemModels.get(0).ItemVendorName);
            discount = Integer.parseInt(foodItemModels.get(0).ItemDiscount);
            tax = Integer.parseInt(foodItemModels.get(0).ItemServiceTax);
            SelectedFoodItemAdapter foodItemAdapter = new SelectedFoodItemAdapter(this, foodItemModels, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            selectedItemLists.setLayoutManager(mLayoutManager);
            selectedItemLists.setAdapter(foodItemAdapter);
            calculateTotalCost();
        }
    }

    private void calculateTotalCost() {
        double totalItems = Double.parseDouble(foodItemDBAdapter.getTotalItems());
        totalCost = Double.parseDouble(foodItemDBAdapter.getTotalCost());
        packageCost = Double.parseDouble(foodItemDBAdapter.getTotalPackageCharge());;
        orgAmount = (int) totalCost;
        textView_itemTotal.setText("₹ " + totalCost);
        int totalInt = (int) totalItems;
        discountCost = (discount * totalCost / 100);
        taxCost = (tax * totalCost / 100);
        textView_discount.setText("- ₹ " + String.format("%.2f", discountCost));
        textView_taxprice.setText("₹ " + String.format("%.2f", taxCost));
        textView_packageCharge.setText("₹ " + String.format("%.2f", packageCost));
        totalCost = totalCost + taxCost;
        totalCost = totalCost - discountCost;
        if(totalCost>0) {
            totalCost = totalCost + 1;
        }
        if(packType.equalsIgnoreCase("2")){
            totalCost = totalCost + packageCost;
        }
        textView_item.setText(totalInt + "");
        textView_totalprice.setText("₹ " + (int) totalCost);
        calculateWalletAmount();
    }

    private void calculateWalletAmount() {
        try {
            double walletAmount = Double.parseDouble(Singleton.getInstance().WalletAmount);
            if (walletAmount >= totalCost) {
                textView_wallet.setText("- ₹ " + (int)totalCost);
                textView_totalprice.setText("₹ 0");
            }
            else if(walletAmount == 0) {
                walletll.setVisibility(View.GONE);
                textView_totalprice.setText("₹ " + (int) totalCost);
            } else if(walletAmount <= totalCost){
                double totcost = totalCost - walletAmount;
                textView_wallet.setText("- ₹ " + (int)walletAmount);
                textView_totalprice.setText("₹ " + (int)totcost);
            }
        } catch (Exception e) {
            e.printStackTrace();
            walletll.setVisibility(View.GONE);
            textView_wallet.setText("- ₹ " + Singleton.getInstance().WalletAmount);
            textView_totalprice.setText("₹ " + (int) totalCost);
        }
    }

    @Override
    public void onFoodItemCountChanged() {
        calculateTotalCost();
    }

    @Override
    public void onFoodItemCountChanged(String itemId, int itemCost, int itemCount, boolean plus) {

    }

    @Override
    public void onCustomizationClicked(String itemId, int itemCost) {
        bottomSheetLayout.setVisibility(View.VISIBLE);
        this.itemCost = itemCost;
        itemPriceTxt.setText("Item price : ₹" + itemCost);
        AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
        ArrayList<AddOnItemModel> addOnItemModels = addOnDBAdapter.getAddOnItems(itemId);
        AddOnItemAdapter addOnItemAdapter = new AddOnItemAdapter(Singleton.getInstance().Context, addOnItemModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().Context, LinearLayoutManager.VERTICAL, false);
        addOnsItemLists.setLayoutManager(mLayoutManager);
        addOnsItemLists.setAdapter(addOnItemAdapter);
        doneTxt.setText("Done");
        doneClick(itemId);
    }

    private void doneClick(final String itemId) {
        doneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (doneTxt.getText().toString().equalsIgnoreCase("Apply")) {
                    bottomSheetLayout.setVisibility(View.VISIBLE);
                    AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
                    ArrayList<AddOnItemModel> addOnItemModels = addOnDBAdapter.getAddOnItems(itemId);
                    AddOnItemAdapter addOnItemAdapter = new AddOnItemAdapter(Singleton.getInstance().Context, addOnItemModels, OrderReviewActivity.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().Context, LinearLayoutManager.VERTICAL, false);
                    addOnsItemLists.setLayoutManager(mLayoutManager);
                    addOnsItemLists.setAdapter(addOnItemAdapter);
                    doneTxt.setText("Done");
                } else {
                    bottomSheetLayout.setVisibility(View.GONE);
                    showFoodItems();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_proceed:
                foodItemModels = foodItemDBAdapter.getSelectedFoodItems();
                if(foodItemModels.size()>0) {
                    Intent payment = new Intent(this, PaymentActivity.class);
                    payment.putExtra("amount", totalCost);
                    payment.putExtra("discount", discountCost);
                    payment.putExtra("tax", taxCost);
                    payment.putExtra("packtype", packType);
                    payment.putExtra("orgamount", orgAmount);
                    startActivity(payment);
                }else{
                    Snackbar.make(editText_applycoupon, "Please add item to cart, before clicking proceed", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
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
            case R.id.dineinll:
                if(packType.equalsIgnoreCase("2")){
                    totalCost = totalCost - packageCost;
                    calculateWalletAmount();
                }
                packType = "1";
                dineInll.setAlpha(1);
                takeAwayll.setAlpha(0.5f);
                packagell.setVisibility(View.GONE);
                break;
            case R.id.takeawayll:
                if(packType.equalsIgnoreCase("1")){
                    totalCost = totalCost + packageCost;
                    calculateWalletAmount();
                }
                packType = "2";
                dineInll.setAlpha(0.5f);
                takeAwayll.setAlpha(1);
                packagell.setVisibility(View.VISIBLE);
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
                                double grnd_total = Double.parseDouble(jsonObject.getString("amount"));
                                String cupid = jsonObject.getString("couponid");
                                String message = jsonObject.getString("message");
                                totalCost = grnd_total;
                                //textView_totalprice.setText(grnd_total + "");
                                calculateWalletAmount();
                                editText_applycoupon.setText("");
                                editText_applycoupon.setFocusable(false);
                                textView_apply.setEnabled(false);
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
        AddOnSubItemAdapter addOnSubItemAdapter = new AddOnSubItemAdapter(Singleton.getInstance().Context, addOnSubItemModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Singleton.getInstance().Context, LinearLayoutManager.VERTICAL, false);
        addOnsItemLists.setLayoutManager(mLayoutManager);
        addOnsItemLists.setAdapter(addOnSubItemAdapter);
        doneTxt.setText("Apply");
    }

    @Override
    public void onAddonSubItemClicked(String subItemCost, String addOnItemId, String cost, boolean isChecked) {
        if (isChecked) {
            itemCost = itemCost + Double.parseDouble(subItemCost);
        } else {
            itemCost = itemCost - Double.parseDouble(cost);
        }
        int itemCostInt = (int) itemCost;
        itemPriceTxt.setText("Item price : ₹" + itemCostInt);
        foodItemDBAdapter.updateTotalCost(itemCostInt + "", addOnItemId);
        foodItemDBAdapter.updateItemTotalCost(itemCostInt + "", addOnItemId);
    }
}
