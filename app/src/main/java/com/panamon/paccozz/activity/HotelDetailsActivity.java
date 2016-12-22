package com.panamon.paccozz.activity;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.panamon.paccozz.dbadater.AddOnDBAdapter;
import com.panamon.paccozz.dbadater.CommonDBHelper;
import com.panamon.paccozz.dbadater.FoodItemDBAdapter;
import com.panamon.paccozz.dbadater.TableConstants;
import com.panamon.paccozz.model.AddOnItemModel;
import com.panamon.paccozz.model.AddOnSubItemModel;
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
    private ArrayList<FoodItemModel> foodItemModels;
    private ArrayList<AddOnItemModel> addOnItemModels;
    private ArrayList<AddOnSubItemModel> addOnSubItemModels;
    private ImageView vendorImage;
    private TextView vendorNameTxt;
    private ViewPagerAdapter adapter;
    public static FloatingActionButton fab;

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

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        vendorDetailsModels = new ArrayList<>();
        foodItemModels = new ArrayList<>();
        addOnItemModels = new ArrayList<>();
        addOnSubItemModels = new ArrayList<>();

        getVendorDetails();

        fab = (FloatingActionButton) findViewById(R.id.fab);
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

    private void setupViewPager() {
        if (Singleton.getInstance().categories.size() > 0) {
            adapter = new ViewPagerAdapter(getSupportFragmentManager());
            for (int i = 0; i < Singleton.getInstance().categories.size(); i++) {
                adapter.addFragment(new FoodItemFragment(), Singleton.getInstance().categories.get(i).CategoryName);
            }
            viewPager.setAdapter(adapter);
        }
    }

    private void getVendorDetails() {
        progressBar.setVisibility(View.VISIBLE);
        Singleton.getInstance().categories.clear();
        //CommonDBHelper.getInstance().deleteTableValues(TableConstants.ADDON_SUBITEM_TABLE);
        //CommonDBHelper.getInstance().deleteTableValues(TableConstants.ADDON_ITEM_TABLE);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.VENDORDETAILS_URL;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
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
                                    foodItemModels.add(foodItemModel);
                                    //foodItemDBAdapter.insertFoodItem(foodItemModel);
                                }

                                //getting addons in vendor
                                Singleton.getInstance().AddOns = vendorObject.getString("addons");
                                if (Singleton.getInstance().AddOns.equalsIgnoreCase("1")) {
                                    JSONObject addOnObject = vendorObject.getJSONObject("headers");
                                    JSONArray addOnArray = addOnObject.getJSONArray("headerlist");
                                    for (int l = 0; l < addOnArray.length(); l++) {
                                        JSONObject addOnArrayObject = addOnArray.getJSONObject(l);
                                        AddOnItemModel addOnItemModel = new AddOnItemModel();
                                        addOnItemModel.AddOnCateroryId = addOnArrayObject.getString("hitemid");
                                        addOnItemModel.AddOnId = addOnArrayObject.getString("headerid");
                                        addOnItemModel.AddOnName = addOnArrayObject.getString("addonheader");
                                        JSONArray addOnSubItemArray = addOnArrayObject.getJSONArray("subitemslist");
                                        for (int m = 0; m < addOnSubItemArray.length(); m++) {
                                            JSONObject addOnSubItemArrayObject = addOnSubItemArray.getJSONObject(m);
                                            AddOnSubItemModel addOnSubItemModel = new AddOnSubItemModel();
                                            addOnSubItemModel.AddOnCateroryId = addOnArrayObject.getString("hitemid");
                                            addOnSubItemModel.AddOnSubItemId = addOnSubItemArrayObject.getString("addonid");
                                            addOnSubItemModel.AddOnSubItemName = addOnSubItemArrayObject.getString("addonname");
                                            addOnSubItemModel.AddOnId = addOnSubItemArrayObject.getString("headid");
                                            addOnSubItemModel.AddOnPrice = addOnSubItemArrayObject.getString("addonprice");
                                            addOnSubItemModels.add(addOnSubItemModel);
                                            //addOnDBAdapter.insertAddOnSubItem(addOnSubItemModel);
                                        }
                                        addOnItemModels.add(addOnItemModel);
                                        //addOnDBAdapter.insertAddOnItem(addOnItemModel);
                                    }
                                }
                                vendorDetailsModel.VenId = vendorObject.getString("venid");
                                vendorDetailsModel.VenName = vendorObject.getString("venname");
                                vendorDetailsModel.VenImage = vendorObject.getString("img");
                                vendorDetailsModel.categorys = Singleton.getInstance().categories;
                                vendorDetailsModels.add(vendorDetailsModel);
                                vendorNameTxt.setText(vendorDetailsModel.VenName);
                                Picasso.with(HotelDetailsActivity.this).load(vendorDetailsModel.VenImage).placeholder(R.drawable.image_placeholder).into(vendorImage);
                            }
                            new InsertVendorDetails(vendorDetailsModels.get(0).VenId).execute();
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

    private void insertVendorDetails(String venId) {
        FoodItemDBAdapter foodItemDBAdapter = new FoodItemDBAdapter();
        AddOnDBAdapter addOnDBAdapter = new AddOnDBAdapter();

        if (!foodItemDBAdapter.checkRecordExists(venId)) {
            for (int i = 0; i < foodItemModels.size(); i++) {
                foodItemDBAdapter.insertFoodItem(foodItemModels.get(i));
            }
            for (int i = 0; i < addOnItemModels.size(); i++) {
                addOnDBAdapter.insertAddOnItem(addOnItemModels.get(i));
            }
            for (int i = 0; i < addOnSubItemModels.size(); i++) {
                addOnDBAdapter.insertAddOnSubItem(addOnSubItemModels.get(i));
            }
        } else {
            for (int i = 0; i < foodItemModels.size(); i++) {
                if (foodItemDBAdapter.checkFoodRecordExists(foodItemModels.get(i).ItemId)) {
                    foodItemDBAdapter.updateFoodItem(foodItemModels.get(i).ItemId, foodItemModels.get(i));
                } else {
                    foodItemDBAdapter.insertFoodItem(foodItemModels.get(i));
                }
            }
            for (int i = 0; i < addOnItemModels.size(); i++) {
                if (addOnDBAdapter.checkAddOnRecordExists(addOnItemModels.get(i).AddOnId)) {
                    addOnDBAdapter.updateAddOnItem(addOnItemModels.get(i).AddOnId, addOnItemModels.get(i));
                } else {
                    addOnDBAdapter.insertAddOnItem(addOnItemModels.get(i));
                }
            }
            for (int i = 0; i < addOnSubItemModels.size(); i++) {
                if (addOnDBAdapter.checkAddOnSubItemRecordExists(addOnSubItemModels.get(i).AddOnSubItemId)) {
                    addOnDBAdapter.updateAddOnSubItem(addOnSubItemModels.get(i).AddOnSubItemId, addOnSubItemModels.get(i));
                } else {
                    addOnDBAdapter.insertAddOnSubItem(addOnSubItemModels.get(i));
                }
            }
        }
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

    private class InsertVendorDetails extends AsyncTask<Void, Void, Void> {

        private String venId = "";

        public InsertVendorDetails(String venId) {
            this.venId = venId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            insertVendorDetails(venId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            setupViewPager();
        }
    }
}
