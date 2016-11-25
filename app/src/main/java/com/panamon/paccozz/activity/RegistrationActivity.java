package com.panamon.paccozz.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.panamon.paccozz.R;
import com.panamon.paccozz.common.Constants;
import com.panamon.paccozz.model.CitySpinnerModel;
import com.panamon.paccozz.model.PlacesModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1000;
    private static final int MY_PERMISSION_READ_EXTERNAL_STORAGE = 100;
    private static final int MY_PERMISSION_WRITE_EXTERNAL_STORAGE = 101;
    private static final int SELECT_FILE = 1001;
    private EditText editText_name, editText_age, editText_mobile, editText_password, editText_emailid;
    private Button btn_submit;
    private TextView txt_male, txt_female, txt_other;
    private ImageView imageView_register;
    private RadioGroup radioGroup_sex;
    private RadioButton radio_male, radio_female, radio_others;
    private ProgressBar progressBar;
    private Spinner spn_city, spn_place;
    private ArrayList<CitySpinnerModel> cityList;
    private ArrayList<PlacesModel> placesLists;
    private String cityId;
    private String placeId = "0";
    private File destination;
    private String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //views
        editText_name = (EditText) findViewById(R.id.edt_name);
        editText_age = (EditText) findViewById(R.id.edt_age);
        editText_emailid = (EditText) findViewById(R.id.edt_email);
        editText_mobile = (EditText) findViewById(R.id.edt_email);
        editText_password = (EditText) findViewById(R.id.edt_password);
        imageView_register = (ImageView) findViewById(R.id.profile_imageView);
        txt_male = (TextView) findViewById(R.id.radio_textone_sex);
        txt_female = (TextView) findViewById(R.id.radio_texttwosex);
        txt_other = (TextView) findViewById(R.id.radio_textothers);
        radioGroup_sex = (RadioGroup) findViewById(R.id.group_radio_dia_sex);
        radio_male = (RadioButton) findViewById(R.id.radio_male);
        radio_female = (RadioButton) findViewById(R.id.radio_female);
        radio_others = (RadioButton) findViewById(R.id.radio_others);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        spn_city = (Spinner) findViewById(R.id.spn_city);
        spn_place = (Spinner) findViewById(R.id.spn_place);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //loading spinner city and setting itpark spinner
        loadCity();
        spn_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView selectedText = (TextView) adapterView.getChildAt(0);
                selectedText.setTextColor(Color.WHITE);
                cityId = cityList.get(i).CityId;
                loadITPlace();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_place.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView selectedText = (TextView) adapterView.getChildAt(0);
                selectedText.setTextColor(Color.WHITE);
                placeId = placesLists.get(i).Id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //selecting image
        imageView_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSION_READ_EXTERNAL_STORAGE);
                    ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_WRITE_EXTERNAL_STORAGE);
                } else {
                    selectImage();
                }
            }
        });

        //submit button click
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_name.getText().length() > 0 && editText_age.getText().length() > 0 && editText_emailid.getText().length() > 0 &&
                        editText_mobile.getText().length() > 0 && editText_password.getText().length() > 0) {
                    if (!placeId.equals("0") && placeId != null && !placeId.equalsIgnoreCase("null")) {
                        new RegisterUser().execute();
                    } else {
                        Snackbar.make(view, "Invalid city or place", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(view, "Please fill all the details", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                saveImage(bytes);
                imageView_register.setImageBitmap(bm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        saveImage(bytes);
        imageView_register.setImageBitmap(thumbnail);
    }

    private void saveImage(ByteArrayOutputStream bytes) {

        destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //building dialog to choose photos
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //calling camera
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    //calling gallery
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void loadITPlace() {
        progressBar.setVisibility(View.VISIBLE);
        placesLists = new ArrayList<>();
        final ArrayList<String> placeStringList = new ArrayList<>();
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
                                    placeStringList.add(placesLists.get(i).PlaceName);
                                }
                            } else {
                                placeId = "0";
                            }
                            // Creating adapter for spinner
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, placeStringList);

                            // Drop down layout style - list view with radio button
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // attaching data adapter to spinner
                            spn_place.setAdapter(dataAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegistrationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cityid", cityId);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void loadCity() {
        progressBar.setVisibility(View.VISIBLE);
        cityList = new ArrayList<>();
        final ArrayList<String> cityStringList = new ArrayList<>();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.GETCITY_URL;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            Log.e("response", response);
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("city")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("city");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    CitySpinnerModel citySpinnerModel = new CitySpinnerModel();
                                    JSONObject js = jsonArray.getJSONObject(i);
                                    citySpinnerModel.CityId = js.getString("cid");
                                    citySpinnerModel.CityName = js.getString("cname");
                                    cityList.add(citySpinnerModel);
                                }
                                for (int i = 0; i < cityList.size(); i++) {
                                    cityStringList.add(cityList.get(i).CityName);
                                }
                                // Creating adapter for spinner
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, cityStringList);

                                // Drop down layout style - list view with radio button
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
                                spn_city.setAdapter(dataAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegistrationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    class RegisterUser extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpPost post = new HttpPost(Constants.REGISTER_URL);
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();

//                code password name age mobile sex
//                emailid bloodgroup address city state country
//                ecnumber diabetes hypertension heartcondition asthma ecname ecrelation allergies
//                ailments medication cdname cdphone hospname hospnum
                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                entity.addPart("uname", new StringBody(editText_name.getText().toString()));
                entity.addPart("uage", new StringBody(editText_age.getText().toString()));
                if (radio_male.isChecked()) {
                    entity.addPart("usex", new StringBody("1"));
                } else if (radio_female.isChecked()) {
                    entity.addPart("usex", new StringBody("2"));
                } else {
                    entity.addPart("usex", new StringBody("3"));
                }
                entity.addPart("uemail", new StringBody(editText_emailid.getText().toString()));
                entity.addPart("upass", new StringBody(editText_password.getText().toString()));
                entity.addPart("uphone", new StringBody(editText_mobile.getText().toString()));
                entity.addPart("ucity", new StringBody(cityId));
                entity.addPart("upark", new StringBody(placeId));
                Log.e("--Path", destination.getPath() + "hhg");
                if (destination != null) {
                    entity.addPart("fimage", new FileBody(new File(destination.getPath()), "image/jpg"));
                }

                try {
                    // Log.e("imagepath", new FileBody(new File( MediaActivity.mFileTemp.getPath()), "image/jpg") + "");
                    HttpClient httpclient = new DefaultHttpClient();
                    post.setEntity(entity);
                    HttpResponse response = httpclient.execute(post);
                    HttpEntity entityres = response.getEntity();
                    Log.e("entity", entityres + "");
                    json = EntityUtils.toString(response.getEntity());

                } catch (FileNotFoundException as) {
                    as.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(String sResponse) {
            super.onPostExecute(sResponse);
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            try {
                Log.e("******RES_Register", json);

                //{"success": 1,"message": "Registration Successfull"}
                if (json != null) {
                    JSONObject jsonObject = new JSONObject(json);
                    String succcess = jsonObject.getString("success");
                    if (succcess.equalsIgnoreCase(String.valueOf(1))) {
                        Intent main = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(main);
                        finish();
                    } else {
                        Snackbar.make(progressBar, "Internal server error", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            } catch (JSONException js) {
                js.printStackTrace();
            } catch (Exception e) {
                Snackbar.make(progressBar, "Please add a image", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    selectImage();
                } else {
                    // permission denied
                    finish();
                }
                break;
            case MY_PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    selectImage();
                } else {
                    // permission denied
                    finish();
                }
                break;
        }
    }
}