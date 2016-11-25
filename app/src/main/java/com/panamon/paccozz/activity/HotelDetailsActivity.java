package com.panamon.paccozz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.panamon.paccozz.common.Singleton;
import com.panamon.paccozz.dbadater.CommonDBHelper;
import com.panamon.paccozz.dbadater.FoodItemDBAdapter;
import com.panamon.paccozz.model.CategoryModel;
import com.panamon.paccozz.model.FoodItemModel;
import com.panamon.paccozz.R;
import com.panamon.paccozz.adapter.ViewPagerAdapter;
import com.panamon.paccozz.common.Constants;
import com.panamon.paccozz.model.VendorDetailsModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HotelDetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ProgressBar progressBar;
    private ArrayList<VendorDetailsModel> vendorDetailsModels;
    private ImageView vendorImage;
    private TextView vendorNameTxt;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCollapsingToolbar();

        //views
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        vendorImage = (ImageView) findViewById(R.id.vendor_image);
        vendorNameTxt = (TextView) findViewById(R.id.vendor_name_txt);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        getVendorDetails(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orderReview = new Intent(HotelDetailsActivity.this, OrderReviewActivity.class);
                startActivity(orderReview);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hotel_details, menu);//Menu Resource, Menu
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        if (Singleton.getInstance().categories.size() > 0) {
            adapter = new ViewPagerAdapter(getSupportFragmentManager());

            for (int i = 0; i < Singleton.getInstance().categories.size(); i++) {
                adapter.addFragment(new FoodItemFragment(), Singleton.getInstance().categories.get(i).CategoryName);
            }
            viewPager.setAdapter(adapter);

        }
    }

    private void getVendorDetails(final ViewPager viewPager) {
        progressBar.setVisibility(View.VISIBLE);
        Singleton.getInstance().categories.clear();
        vendorDetailsModels = new ArrayList<>();
        CommonDBHelper.getInstance().truncateAllTables();
        final FoodItemDBAdapter foodItemDBAdapter = new FoodItemDBAdapter();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.VENDORDETAILS_URL;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("response", response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray vendorArray = jsonObject.getJSONArray("vendor");
                            for (int i = 0; i < vendorArray.length(); i++) {
                                VendorDetailsModel vendorDetailsModel = new VendorDetailsModel();
                                JSONObject vendorObject = vendorArray.getJSONObject(i);

                                //getting catergories in vendors
                                JSONObject categoryObject = vendorObject.getJSONObject("category");
                                JSONArray categoryArray = categoryObject.getJSONArray("cat");
                                for (int j = 0; j < categoryArray.length(); j++) {
                                    CategoryModel categoryModel = new CategoryModel();
                                    JSONObject categoryObject1 = categoryArray.getJSONObject(j);
                                    categoryModel.CategoryId = categoryObject1.getString("catpid");
                                    categoryModel.CategoryName = categoryObject1.getString("catname");
                                    Singleton.getInstance().categories.add(categoryModel);
                                }

                                //getting food items in vendor
                                JSONObject itemObject = vendorObject.getJSONObject("items");
                                JSONArray itemArray = itemObject.getJSONArray("item");
                                for (int k = 0; k < itemArray.length(); k++) {
                                    FoodItemModel foodItemModel = new FoodItemModel();
                                    JSONObject itemObject1 = itemArray.getJSONObject(k);
                                    foodItemModel.ItemId = itemObject1.getString("itemid");
                                    foodItemModel.ItemName = itemObject1.getString("itemname");
                                    foodItemModel.ItemCost = itemObject1.getString("itemcost");
                                    foodItemModel.ItemCount = "0";
                                    foodItemModel.IsItemSelected = "0";
                                    foodItemModel.ItemCategoryId = itemObject1.getString("catid");
                                    foodItemModel.ItemVendorId = itemObject1.getString("venid");
                                    foodItemModel.ItemType = itemObject1.getString("itemtype");
                                    foodItemDBAdapter.insertFoodItem(foodItemModel);
                                }
                                vendorDetailsModel.VenId = vendorObject.getString("venid");
                                vendorDetailsModel.VenName = vendorObject.getString("venname");
                                vendorDetailsModel.VenImage = vendorObject.getString("img");
                                vendorDetailsModel.categorys = Singleton.getInstance().categories;
                                vendorDetailsModels.add(vendorDetailsModel);
                                vendorNameTxt.setText(vendorDetailsModel.VenName);
                                Picasso.with(HotelDetailsActivity.this).load(vendorDetailsModel.VenImage).placeholder(R.drawable.image_placeholder).into(vendorImage);
                            }
                            setupViewPager(viewPager);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(HotelDetailsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("venid", Singleton.getInstance().VendorId);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
}
