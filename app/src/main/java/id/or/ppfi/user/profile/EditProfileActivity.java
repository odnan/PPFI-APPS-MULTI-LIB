package id.or.ppfi.user.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.or.ppfi.R;
import id.or.ppfi.config.AppConfig;
import id.or.ppfi.config.AppController;
import id.or.ppfi.config.CircularImageView;
import id.or.ppfi.config.CircularNetworkImageView;
import id.or.ppfi.config.SessionManager;
import id.or.ppfi.dashboard.WebviewActivity;
import id.or.ppfi.main.AdvancedActivity;
import id.or.ppfi.main.AdvancedActivityAthlete;


/**
 * Created by emergency on 09/10/2016.
 */

public class EditProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ProgressDialog progressDialog;
    Toolbar toolbar;
    private static final int RESULT_LOAD_IMG = 1;

    EditText userName,name,email,phone,gender;
    TextView changePhoto;
    Button save,cancel;
    Spinner spinnerGender;

    SessionManager sessionCode,sessionUrlProfile,sessionNameUser,sessionPhone,sessionEmail,sessionRoleType;
    private String username,role_type;
    String imageName;

    CircularNetworkImageView thumbNail;
    CircularImageView thumbNail2;
    String urlProfile = "",nameUser = "",phoneNumber="",emailUser="";
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contact Info");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3E50B4")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changePhoto = (TextView) findViewById(R.id.text_change_photo);
        userName = (EditText) findViewById(R.id.edit_username);
        name = (EditText) findViewById(R.id.edit_name);
        email = (EditText) findViewById(R.id.edit_email);
        phone = (EditText) findViewById(R.id.edit_phone);
        gender = (EditText) findViewById(R.id.edit_gender);
        spinnerGender = (Spinner) findViewById(R.id.spinner_gender);

        thumbNail = (CircularNetworkImageView) findViewById(R.id.thumbnail_profile);
        thumbNail2 = (CircularImageView) findViewById(R.id.thumbnail_profile_2);

        save = (Button) findViewById(R.id.btn_save);
        cancel = (Button) findViewById(R.id.btn_cancel);
        //save.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);

        sessionCode = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionCode.GetSessionKode();
        username = user.get(SessionManager.KEY_USERNAME);

        sessionUrlProfile = new SessionManager(getApplicationContext());
        HashMap<String, String> urlP = sessionUrlProfile.GetSessionUrlProfile();
        urlProfile = urlP.get(SessionManager.KEY_URL_PROFILE);

        sessionNameUser = new SessionManager(getApplicationContext());
        HashMap<String, String> name_ = sessionNameUser.GetSessionUserFullName();
        nameUser = name_.get(SessionManager.KEY_USER_FULLNAME);

        sessionPhone = new SessionManager(getApplicationContext());
        HashMap<String, String> hp = sessionPhone.GetSessionPhone();
        phoneNumber = hp.get(SessionManager.KEY_PHONE);

        sessionEmail = new SessionManager(getApplicationContext());
        HashMap<String, String> mail = sessionEmail.GetSessionEmail();
        emailUser = mail.get(SessionManager.KEY_EMAIL);

        sessionRoleType = new SessionManager(getApplicationContext());
        HashMap<String, String> roleType = sessionRoleType.GetSessionRoleType();
        role_type = roleType.get(SessionManager.KEY_ROLE_TYPE);


        thumbNail.setVisibility(View.GONE);
        thumbNail2.setVisibility(View.GONE);

        if(isNetworkAvailable()){
            new GenerateUser().execute();
            thumbNail.setVisibility(View.VISIBLE);
            thumbNail2.setVisibility(View.GONE);
            thumbNail.setImageUrl(urlProfile, imageLoader);
        }else{
            thumbNail2.setVisibility(View.VISIBLE);
            thumbNail.setVisibility(View.GONE);
        }

        userName.setText(username);
        name.setText(nameUser);

        try{
            if(phoneNumber.contains("+62"))
                phone.setText(phoneNumber.replace("+62",""));
            else{
                String res = phoneNumber.substring(1);
                phone.setText(res);
            }
        } catch (Exception e) {
            phone.setText(phoneNumber);
        }

        email.setText(emailUser);

        imageName = urlProfile;
        if (imageName.contains("portal.iamprima.com"))
            imageName = imageName.replace("http://portal.iamprima.com/assets/pictures/","");
        else
            imageName = imageName.replace("http://masterdata.iamprima.com/upload/uploads/","");

        File imgFile = new  File(Environment.getExternalStorageDirectory().toString()+"/Prima/Profile/Images/"+imageName);
        Log.e("imgFile : ", imgFile.toString());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            thumbNail2.setImageBitmap(myBitmap);
        }else{
           // new AdvancedActivity.DownloadImage().execute();
        }


        // Spinner click listener
        spinnerGender.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Male");
        categories.add("Female");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerGender.setAdapter(dataAdapter);

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name_ = name.getText().toString();
                String email_ = email.getText().toString();
                String phone_ = phone.getText().toString();

                if(name_.equals("") || email_.equals("") || phone_.equals("")){
                    Toast.makeText(getApplication(), "Please Input Field!", Toast.LENGTH_LONG).show();
                }else{
                    changeContact(username,name_,email_,"+62"+phone_);
                }

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                goToMainActivity();
            }
        });

        thumbNail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(EditProfileActivity.this, WebviewActivity.class);
                i.putExtra("nama_modul", "Profile Picture");
                i.putExtra("sub_nama_modul", name.getText().toString());
                i.putExtra("url_modul", urlProfile);
                startActivity(i);
                EditProfileActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        if(item.equals("Male"))
            gender.setText("laki");
        else
            gender.setText("perempuan");
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_version, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                goToMainActivity();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void goToMainActivity() {
        this.finish();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    public class GenerateUser extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonRoleName/Username/"+username);
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                //Toast.makeText(getApplication(), "forecastJsonStr "+buffer.toString(),Toast.LENGTH_LONG).show();
                return forecastJsonStr;
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObj = new JSONObject(s);
                JSONArray jsonArray = jsonObj.getJSONArray("data");
                JSONObject obj = jsonArray.getJSONObject(0);

                name.setText(obj.getString("name"));


                try{
                    if(obj.getString("phone").contains("+62"))
                        phone.setText(obj.getString("phone").replace("+62",""));
                    else{
                        String res = obj.getString("phone").substring(1);
                        phone.setText(res);
                    }
                } catch (Exception e) {
                    phone.setText(obj.getString("phone"));
                }



                email.setText(obj.getString("email"));

                sessionNameUser.CreateSessionUserFullName(obj.getString("name"));
                sessionEmail.CreateSessionEmail(obj.getString("email"));
                sessionPhone.CreateSessionPhone(obj.getString("phone"));

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),
                        "Error String Input!", Toast.LENGTH_LONG)
                        .show();
            }
//            Log.i("json", s);
        }
    }



    /**
     * function to verify login details in mysql db
     * */
    private void changeContact(final String username_,final String name_, final String email_,final String phone_) {
        // Tag used to cancel the request
        String tag_string_req = "edit_profile";

        // progressDialog.setMessage("Logging in ...");
        progressDialog = new ProgressDialog(EditProfileActivity.this);
        progressDialog.setTitle("Generate Data...");
        progressDialog.setMessage("Preparing user...");
        progressDialog.setIndeterminate(false);
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHANGE_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Log.d(TAG, "Login Response: " + response.toString());
                progressDialog.dismiss();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        // session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String username = user.getString("username");
                        String created_at = user.getString("created_at");

                        // Inserting row in users table
                        //  db.addUser(name, username, uid, created_at);
                        //  sessionCode.CreateSessionKode(inputUsername.getText().toString());


                        sessionNameUser.CreateSessionUserFullName(name_);
                        sessionEmail.CreateSessionEmail(email_);
                        sessionPhone.CreateSessionPhone(phone_);

                        Toast.makeText(getApplication(), "Done", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        progressDialog.show();
                        if (role_type.equals("ATL")) {
                            Intent i = new Intent(getApplicationContext(), AdvancedActivityAthlete.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                            EditProfileActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }else{
                            Intent i = new Intent(getApplicationContext(), AdvancedActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                            EditProfileActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage() + "Error", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username_);
                params.put("name", name_);
                params.put("email", email_);
                params.put("phone", phone_);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}