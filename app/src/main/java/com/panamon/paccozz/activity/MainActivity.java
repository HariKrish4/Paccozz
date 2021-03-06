package com.panamon.paccozz.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.facebook.stetho.Stetho;
import com.freshdesk.hotline.Hotline;
import com.freshdesk.hotline.HotlineConfig;
import com.freshdesk.hotline.HotlineUser;
import com.panamon.paccozz.R;
import com.panamon.paccozz.adapter.HotelListAdapter;
import com.panamon.paccozz.common.Constants;
import com.panamon.paccozz.common.SharedPref;
import com.panamon.paccozz.common.Singleton;
import com.panamon.paccozz.dbadater.CommonDBHelper;
import com.panamon.paccozz.dbadater.FoodItemDBAdapter;
import com.panamon.paccozz.model.FoodItemModel;
import com.panamon.paccozz.model.HotelListModel;
import com.panamon.paccozz.model.PlacesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private TextView place_txt;
    private ImageView place_img, placeHolder_img;
    private ProgressBar progressBar;
    private ArrayList<PlacesModel> placesLists;
    private RecyclerView hotelListView;
    private String placeId;
    private SharedPref sharedPref;
    private EditText searchEdt;
    private ArrayList<HotelListModel> hotelListModels;
    private FloatingActionButton fab;
    private MenuItem nav_wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intializing Stetho to check DB values.. Should be removed while generating APK
        Stetho.initializeWithDefaults(this);
        //Initializing the database
        CommonDBHelper.getInstance(this);

        sharedPref = new SharedPref(this);

        placeId = Singleton.getInstance().ParkId;
        Singleton.getInstance().Context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orderReview = new Intent(MainActivity.this, OrderReviewActivity.class);
                startActivity(orderReview);
            }
        });

        //drawer layout codes
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // get menu from navigationView
        navigationView.setItemIconTintList(null);
        Menu menu = navigationView.getMenu();

        // find MenuItem you want to change
        nav_wallet = menu.findItem(R.id.nav_wallet);


        navigationView.setNavigationItemSelectedListener(this);

        //title bar codes
        place_img = (ImageView) findViewById(R.id.choose_place_img);
        place_txt = (TextView) findViewById(R.id.place_txt);
        placeHolder_img = (ImageView) findViewById(R.id.imageView_list);

        place_img.setOnClickListener(this);
        place_txt.setText(Singleton.getInstance().ParkName);

        //progressbar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //hotel list views
        hotelListView = (RecyclerView) findViewById(R.id.hotelLists);
        getHotelLists();

        //search codes
        searchEdt = (EditText) findViewById(R.id.searchEdt);
        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchTxt = editable.toString().toLowerCase();
                if (searchTxt.trim().length() > 0) {
                    placeHolder_img.setVisibility(View.GONE);
                    searchEdt.setCursorVisible(true);
                } else {
                    placeHolder_img.setVisibility(View.VISIBLE);
                    searchEdt.setCursorVisible(false);
                }
                final ArrayList<HotelListModel> filteredList = new ArrayList<HotelListModel>();
                for (int i = 0; i < hotelListModels.size(); i++) {
                    String text = hotelListModels.get(i).HotelName.toLowerCase();
                    if (text.contains(searchTxt)) {
                        HotelListModel hotelListModel = hotelListModels.get(i);
                        filteredList.add(hotelListModel);
                    }
                }
                HotelListAdapter hotelListAdapter = new HotelListAdapter(MainActivity.this, filteredList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                hotelListView.setLayoutManager(mLayoutManager);
                hotelListView.setAdapter(hotelListAdapter);
            }
        });
        getUserDetails();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FoodItemDBAdapter foodItemDBAdapter = new FoodItemDBAdapter();
        ArrayList<FoodItemModel> foodItemModels = foodItemDBAdapter.getSelectedFoodItems();
        if (foodItemModels.size() > 0) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }
    }

    /**
     * Showing Hotel List from api using volley
     */
    private void getHotelLists() {
        progressBar.setVisibility(View.VISIBLE);
        hotelListModels = new ArrayList<>();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.HOTELLIST_URL;

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

                                    hotelListModels.add(hotelListModel);
                                }
                                HotelListAdapter hotelListAdapter = new HotelListAdapter(MainActivity.this, hotelListModels);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                                hotelListView.setLayoutManager(mLayoutManager);
                                hotelListView.setAdapter(hotelListAdapter);
                            } else if (success.equalsIgnoreCase("0")) {
                                hotelListModels.clear();
                                HotelListAdapter hotelListAdapter = new HotelListAdapter(MainActivity.this, hotelListModels);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                                hotelListView.setLayoutManager(mLayoutManager);
                                hotelListView.setAdapter(hotelListAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("park", placeId);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Showing popup menu when tapping on place_img
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_place, popup.getMenu());
        Menu placeMenu = popup.getMenu();
        placesLists = new ArrayList<>();
        placeMenu.clear();
        getPlaces(popup, placeMenu);
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
    }

    private void getUserDetails() {
        progressBar.setVisibility(View.VISIBLE);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.USERDETAILS_URL;

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
                            if (succcess.equalsIgnoreCase(String.valueOf(1))) {
                                // Singleton.getInstance().UserId = jsonObject.getString("uid");
                                String code = jsonObject.getString("ucode");
                                Singleton.getInstance().ParkId = jsonObject.getString("uparkid");
                                Singleton.getInstance().ParkName = jsonObject.getString("uparkname");
                                String cityid = jsonObject.getString("ucityid");
                                Singleton.getInstance().UserCity = jsonObject.getString("ucityname");
                                Singleton.getInstance().UserGender = jsonObject.getString("usex");
                                Singleton.getInstance().UserName = jsonObject.getString("uname");
                                Singleton.getInstance().UserEmail = jsonObject.getString("umail");
                                Singleton.getInstance().UserPass = jsonObject.getString("upass");
                                Singleton.getInstance().UserAge = jsonObject.getString("uage");
                                String dob = jsonObject.getString("udob");
                                Singleton.getInstance().UserMobile = jsonObject.getString("umobile");
                                Singleton.getInstance().WalletAmount = jsonObject.getString("uwallet");
                                Singleton.getInstance().ProfileImage = jsonObject.getString("uimg");
                                String message = jsonObject.getString("message");
                                // set new title to the MenuItem
                                nav_wallet.setTitle("Wallet                          ₹ " + Singleton.getInstance().WalletAmount);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("umobile", Singleton.getInstance().UserMobile);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getPlaces(final PopupMenu popup, final Menu placeMenu) {
        progressBar.setVisibility(View.VISIBLE);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.PLACE_URL;

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
                            if (succcess.equalsIgnoreCase(String.valueOf(1))) {
                                JSONArray jsonArray = jsonObject.getJSONArray("it");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    PlacesModel placesModel = new PlacesModel();
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String id = jsonObject1.getString("itid");
                                    String name = jsonObject1.getString("parkname");
                                    placesModel.Id = id;
                                    placesModel.PlaceName = name;
                                    placesLists.add(placesModel);
                                }
                                for (int i = 0; i < placesLists.size(); i++) {
                                    placeMenu.add(0, i, Menu.NONE, placesLists.get(i).PlaceName);
                                }
                                popup.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cityid", "1");

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            Intent profile = new Intent(this, RegistrationActivity.class);
            profile.putExtra("update", "update");
            startActivity(profile);
        } else if (id == R.id.nav_help) {
            showDialog();

        } else if (id == R.id.nav_order) {
            Intent order = new Intent(this, OrderActivity.class);
            startActivity(order);
        } else if (id == R.id.nav_wallet) {

        } else if (id == R.id.nav_fav) {
            Intent fav = new Intent(this, FavoritesActivity.class);
            startActivity(fav);
        } else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Alert!")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            sharedPref.setIsLogged(false);
                            CommonDBHelper.getInstance().truncateAllTables();
                            //Hotline.clearUserData(getApplicationContext());
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.help_dialog_layout);
        TextView cancel, chat, mail, faq;
        faq = (TextView) dialog.findViewById(R.id.faq);
        mail = (TextView) dialog.findViewById(R.id.mailus);
        chat = (TextView) dialog.findViewById(R.id.chat);
        cancel = (TextView) dialog.findViewById(R.id.cancel);
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent faq = new Intent(MainActivity.this, FaqActivity.class);
                startActivity(faq);
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("sdk version",Hotline.getSDKVersionCode()+"");

                HotlineConfig hlConfig = new HotlineConfig("9d5af2db-ed0a-43d5-826c-a23a6d82db11", "249ef5f7-9e48-42b6-b6d0-0dbd8eea0650");

                hlConfig.setVoiceMessagingEnabled(true);
                hlConfig.setCameraCaptureEnabled(true);
                hlConfig.setPictureMessagingEnabled(true);
                Hotline.getInstance(getApplicationContext()).init(hlConfig);


                //Update user information
                HotlineUser user = Hotline.getInstance(getApplicationContext()).getUser();
                user.setName("Android").setEmail("android@paccozz.com").setPhone("044", "25425424");
                Hotline.getInstance(getApplicationContext()).updateUser(user);

                Hotline.showConversations(MainActivity.this);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PackageManager pm = MainActivity.this.getPackageManager();
                PackageInfo pInfo = null;

                try {
                    pInfo = pm.getPackageInfo(MainActivity.this.getPackageName(), 0);

                } catch (PackageManager.NameNotFoundException e1) {
                    e1.printStackTrace();
                }
                String cuurentversion = pInfo.versionName;
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                String model = android.os.Build.MODEL;
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "support@paccozz.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Report Issue to Support Team");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Device Id -" + model + "\n" + "Android version -" + currentapiVersion + "\n" + "App version -" + cuurentversion);
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });


        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_place_img:
                showPopupMenu(place_img);
                break;
        }
    }

    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            placeId = placesLists.get(item.getItemId()).Id;
            place_txt.setText(placesLists.get(item.getItemId()).PlaceName);
            getHotelLists();
            return false;
        }
    }
}
