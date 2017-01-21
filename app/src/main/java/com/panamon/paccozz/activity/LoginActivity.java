package com.panamon.paccozz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.panamon.paccozz.R;
import com.panamon.paccozz.common.Constants;
import com.panamon.paccozz.common.SharedPref;
import com.panamon.paccozz.common.Singleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private EditText editText_phonenumber, editText_otp;
    private ImageView image_go;
    private ProgressBar progressBar;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = new SharedPref(this);

        //views
        editText_phonenumber = (EditText) findViewById(R.id.editText_number);
        editText_otp = (EditText) findViewById(R.id.enter_otp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        image_go = (ImageView) findViewById(R.id.imageView_go);

        image_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_otp.isShown()) {
                    if (editText_otp.getText().toString().trim().length() > 0) {
                        if(editText_otp.getHint().toString().equalsIgnoreCase("Enter password")) {
                            loginUser();
                        }else{
                            Singleton.getInstance().mobileNumberStr=editText_phonenumber.getText().toString();
                            registerUser();
                        }
                    }
                } else {
                    getOtp();
                }
            }
        });
    }

    private void getOtp() {
        progressBar.setVisibility(View.VISIBLE);
        final String token = FirebaseInstanceId.getInstance().getToken();
        Log.e("token",token);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.OTP_URL;

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
                                editText_otp.setVisibility(View.VISIBLE);
                                editText_otp.setHint("Enter password");
                                editText_otp.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            }
                            if (succcess.equalsIgnoreCase(String.valueOf(2))) {
                                editText_otp.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("usermobile", editText_phonenumber.getText().toString());
                params.put("deviceid", token);
                params.put("devicetype","2");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void loginUser() {
        progressBar.setVisibility(View.VISIBLE);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.PASSWORD_URL;

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
                                Singleton.getInstance().UserId = jsonObject.getString("uid");
                                getUserDetails();
                            } /*else if (succcess.equalsIgnoreCase("2")) {
                                Intent tw = new Intent(LoginActivity.this, RegistrationActivity.class);
                                startActivity(tw);
                                finish();
                            }*/ else if (succcess.equalsIgnoreCase("0")) {
                                Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("umobile", editText_phonenumber.getText().toString());
                params.put("pass", editText_otp.getText().toString());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void registerUser() {
        progressBar.setVisibility(View.VISIBLE);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.OTPCHECK_URL;

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
                                Intent tw = new Intent(LoginActivity.this, RegistrationActivity.class);
                                startActivity(tw);
                                finish();
                            } /*else if (succcess.equalsIgnoreCase("2")) {
                                Intent tw = new Intent(LoginActivity.this, RegistrationActivity.class);
                                startActivity(tw);
                                finish();
                            }*/ else if (succcess.equalsIgnoreCase("0")) {
                                Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("umobile", editText_phonenumber.getText().toString());
                params.put("otp", editText_otp.getText().toString());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
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
                                Singleton.getInstance().UserId = jsonObject.getString("uid");
                                String code = jsonObject.getString("ucode");
                                Singleton.getInstance().ParkId = jsonObject.getString("uparkid");
                                Singleton.getInstance().ParkName = jsonObject.getString("uparkname");
                                String cityid = jsonObject.getString("ucityid");
                                String cityname = jsonObject.getString("ucityname");
                                String gender = jsonObject.getString("usex");
                                Singleton.getInstance().UserName = jsonObject.getString("uname");
                                Singleton.getInstance().UserEmail = jsonObject.getString("umail");
                                Singleton.getInstance().UserPass = jsonObject.getString("upass");
                                String dob = jsonObject.getString("udob");
                                Singleton.getInstance().UserMobile = jsonObject.getString("umobile");
                                Singleton.getInstance().WalletAmount = jsonObject.getString("uwallet");
                                Singleton.getInstance().ProfileImage = jsonObject.getString("uimg");
                                String message = jsonObject.getString("message");

                                sharedPref.setIsLogged(true);
                                sharedPref.setLoginData(Singleton.getInstance().UserId, Singleton.getInstance().UserName, Singleton.getInstance().UserMobile,
                                        Singleton.getInstance().UserEmail, Singleton.getInstance().ParkId, Singleton.getInstance().ParkName,Singleton.getInstance().WalletAmount,Singleton.getInstance().ProfileImage,Singleton.getInstance().UserPass);
                                Intent as = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(as);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("umobile", editText_phonenumber.getText().toString());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}
