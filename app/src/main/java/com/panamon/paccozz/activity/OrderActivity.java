package com.panamon.paccozz.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.panamon.paccozz.R;
import com.panamon.paccozz.adapter.OrderAdapter;
import com.panamon.paccozz.common.Constants;
import com.panamon.paccozz.common.Singleton;
import com.panamon.paccozz.model.OrderHistoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView orderListView;
    private TextView noDataTxt;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderListView = (RecyclerView) findViewById(R.id.orderLists);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        noDataTxt = (TextView) findViewById(R.id.no_data_txt);
        getOrderLists();
    }

    /**
     * Showing Hotel List from api using volley
     */
    private void getOrderLists() {
        progressBar.setVisibility(View.VISIBLE);
        final ArrayList<OrderHistoryModel> orderHistoryModels = new ArrayList<>();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.ORDERHISTORY_URL;

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
                                JSONArray jsonArray = jsonObject.getJSONArray("order");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject dataObject = jsonArray.getJSONObject(i);
                                    OrderHistoryModel orderHistoryModel = new OrderHistoryModel();
                                    orderHistoryModel.OrderId = dataObject.getString("orderid");
                                    orderHistoryModel.VendorName = dataObject.getString("vendorname");
                                    orderHistoryModel.TotalCost = dataObject.getString("totcost");
                                    orderHistoryModel.OrderStatus = dataObject.getString("status");
                                    orderHistoryModel.OrderTime = dataObject.getString("pushtime");
                                    orderHistoryModel.orderCode = dataObject.getString("ocode");
                                    orderHistoryModel.item = dataObject.getString("item");
                                    orderHistoryModel.username = dataObject.getString("username");
                                    orderHistoryModel.totitem = dataObject.getString("totitem");
                                    orderHistoryModel.itemsrate = dataObject.getString("itemsrate");
                                    orderHistoryModel.itemcnt = dataObject.getString("itemcnt");
                                    orderHistoryModel.itemcost = dataObject.getString("itemcost");
                                    orderHistoryModel.paystatus = dataObject.getString("paystatus");
                                    orderHistoryModel.packtype = dataObject.getString("packtype");
                                    orderHistoryModel.randnum = dataObject.getString("randnum");
                                    orderHistoryModel.orgamount = dataObject.getString("orgamount");
                                    orderHistoryModel.service = dataObject.getString("service");
                                    orderHistoryModel.discount = dataObject.getString("discount");
                                    orderHistoryModel.odate = dataObject.getString("odate");
                                    orderHistoryModel.otime = dataObject.getString("otime");
                                    orderHistoryModel.acctime = dataObject.getString("acctime");
                                    orderHistoryModel.deltime = dataObject.getString("deltime");
                                    orderHistoryModel.ostatus = dataObject.getString("ostatus");
                                    orderHistoryModel.reason = dataObject.getString("reason");

                                    orderHistoryModels.add(orderHistoryModel);
                                }
                                OrderAdapter orderAdapter = new OrderAdapter(OrderActivity.this, orderHistoryModels);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(OrderActivity.this, LinearLayoutManager.VERTICAL, false);
                                orderListView.setLayoutManager(mLayoutManager);
                                orderListView.setAdapter(orderAdapter);
                                noDataTxt.setVisibility(View.GONE);
                            } else if (success.equalsIgnoreCase("0")) {
                                noDataTxt.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
               // Toast.makeText(OrderActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(OrderActivity.this, "Internet connection is slow.Please check internet.", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Singleton.getInstance().UserId);
                params.put("ostatus", "5");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
