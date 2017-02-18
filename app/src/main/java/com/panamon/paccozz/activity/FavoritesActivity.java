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
import com.panamon.paccozz.adapter.HotelListAdapter;
import com.panamon.paccozz.adapter.OrderAdapter;
import com.panamon.paccozz.common.Constants;
import com.panamon.paccozz.common.Singleton;
import com.panamon.paccozz.model.HotelListModel;
import com.panamon.paccozz.model.OrderHistoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView orderListView;
    private TextView noDataTxt;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
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

        orderListView = (RecyclerView) findViewById(R.id.favLists);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        noDataTxt = (TextView) findViewById(R.id.no_data_txt);
        getFavorites();
    }

    /**
     * Showing Hotel List from api using volley
     */
    private void getFavorites() {
        progressBar.setVisibility(View.VISIBLE);
        final ArrayList<HotelListModel> orderHistoryModels = new ArrayList<>();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.LISTFAV_URL;

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
                                JSONArray jsonArray = jsonObject.getJSONArray("vendor");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    HotelListModel hotelListModel = new HotelListModel();
                                    JSONObject js = jsonArray.getJSONObject(i);
                                    hotelListModel.HotelId = js.getString("venid");
                                    hotelListModel.HotelName = js.getString("venname");
                                    hotelListModel.HotelRate = js.getString("venrate");
                                    hotelListModel.HotelType = js.getString("fctype");
                                    hotelListModel.HotelRecipe = js.getString("recipe");
                                    hotelListModel.HotelTime = js.getString("time");
                                    hotelListModel.HotelExpensive = js.getString("expensive");
                                    hotelListModel.HotelImage = js.getString("img");

                                    orderHistoryModels.add(hotelListModel);
                                }
                                HotelListAdapter hotelListAdapter = new HotelListAdapter(FavoritesActivity.this, orderHistoryModels);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(FavoritesActivity.this, LinearLayoutManager.VERTICAL, false);
                                orderListView.setLayoutManager(mLayoutManager);
                                orderListView.setAdapter(hotelListAdapter);
                                if(orderHistoryModels.size()==0){
                                    noDataTxt.setVisibility(View.GONE);
                                }
                            } else if (success.equalsIgnoreCase("0")) {
                                    noDataTxt.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(FavoritesActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Singleton.getInstance().UserId);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
