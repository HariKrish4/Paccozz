package com.panamon.paccozz.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.panamon.paccozz.R;
import com.panamon.paccozz.common.Constants;
import com.panamon.paccozz.common.Singleton;
import com.panamon.paccozz.dbadater.AddOnDBAdapter;
import com.panamon.paccozz.dbadater.FoodItemDBAdapter;
import com.panamon.paccozz.model.AddOnSubItemModel;
import com.panamon.paccozz.model.FoodItemModel;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private ProgressBar progressBar;
    private ArrayList<FoodItemModel> foodItemModels;
    private ArrayList<String> addOnsId;
    private ArrayList<String> itemsCount;
    private ArrayList<String> itemsCost;
    private ArrayList<String> itemsId;
    private double totcost;
    private String transactionId;
    private String item_id;
    private String item_count;
    private String item_cost;
    private String addOn_id;
    private boolean wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        itemsId = new ArrayList<>();
        itemsCount = new ArrayList<>();
        itemsCost = new ArrayList<>();
        addOnsId = new ArrayList<>();
        totcost = getIntent().getDoubleExtra("amount", 0);
        FoodItemDBAdapter foodItemDBAdapter = new FoodItemDBAdapter();
        foodItemModels = foodItemDBAdapter.getSelectedFoodItems();
        try {
            double walletAmount = Double.parseDouble(Singleton.getInstance().WalletAmount);
            if (walletAmount>=totcost) {
                getOrderData();
                wallet = true;
            }else{
                startPayment();
                wallet = false;
            }
        }catch (Exception e){
            e.printStackTrace();
            startPayment();
        }
    }

    private void getOrderData() {
        AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();
        for (int i = 0; i < foodItemModels.size(); i++) {
            itemsId.add(foodItemModels.get(i).ItemId);
            itemsCount.add(foodItemModels.get(i).ItemCount);
            itemsCost.add(foodItemModels.get(i).ItemCost);
        }
        ArrayList<AddOnSubItemModel> selectedAddOns = addOnDBAdapter.getAddOnSelectedSubItems();
        for (int i = 0; i < selectedAddOns.size(); i++) {
            addOnsId.add(selectedAddOns.get(i).AddOnSubItemId);
        }
        item_id = TextUtils.join(",", itemsId);
        item_count = TextUtils.join(",", itemsCount);
        item_cost = TextUtils.join(",", itemsCost);
        addOn_id = TextUtils.join(",", addOnsId);
        createOrder();
    }


    @Override
    public void onPaymentSuccess(String s) {

        Snackbar.make(progressBar, "Payment Success: " + s, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        transactionId = s;
        getOrderData();
    }

    private void createOrder() {
        progressBar.setVisibility(View.VISIBLE);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.CREATEORDER_URL;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("response", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String succcess = jsonObject.getString("success");
                            if (succcess.equalsIgnoreCase("1")) {

                            } else if (succcess.equalsIgnoreCase("2")) {

                            } else if (succcess.equalsIgnoreCase("0")) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PaymentActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", Singleton.getInstance().UserId);
                params.put("itemid", item_id);
                params.put("venid", foodItemModels.get(0).ItemVendorId);
                params.put("count", item_count);
                params.put("cost", item_cost);
                params.put("totcost", totcost + "");
                params.put("addons", addOn_id);
                params.put("orgamount", totcost + "");
                params.put("servicetax", totcost + "");
                params.put("discount", totcost + "");
               // params.put("transid", "100");
                if(!wallet) {
                    params.put("transid", transactionId);
                    params.put("tranamount", totcost + "");
                    params.put("transtatus", "1");
                    params.put("tranpaytype", "card");
                }else{
                    params.put("transid", "wallet");
                    params.put("tranamount", totcost + "");
                    params.put("transtatus", "1");
                    params.put("tranpaytype", "wallet");
                }
                params.put("packtype", "1");

                Log.e("params", params + "");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Snackbar.make(progressBar, "Payment Error: " + s, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void startPayment() {

        progressBar.setVisibility(View.VISIBLE);
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        checkout.setImage(R.mipmap.ic_launcher);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: Rentomojo || HasGeek etc.
             */
            options.put("name", Singleton.getInstance().UserName);

            /**
             * Description can be anything
             * eg: Order #123123
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Order #123456");
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            /**
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
            double amount = getIntent().getDoubleExtra("amount", 0);
            int amountInt = (int) amount * 100;
            options.put("amount", amountInt + "");

            JSONObject preFill = new JSONObject();
            preFill.put("email", Singleton.getInstance().UserEmail);
            preFill.put("contact", Singleton.getInstance().UserMobile);

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("pay", "Error in starting Razorpay Checkout", e);
        }

        progressBar.setVisibility(View.GONE);
    }
}
