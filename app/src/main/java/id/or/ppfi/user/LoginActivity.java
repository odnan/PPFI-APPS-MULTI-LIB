package id.or.ppfi.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import id.or.ppfi.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import id.or.ppfi.config.AppConfig;
import id.or.ppfi.config.AppController;
import id.or.ppfi.config.SQLiteHandler;
import id.or.ppfi.config.ServerRequest;
import id.or.ppfi.config.SessionManager;
import id.or.ppfi.main.AdvancedActivity;
import id.or.ppfi.main.AdvancedActivityAthlete;


public class LoginActivity extends Activity {
    private static int SPLASH_TIME_OUT = 5000;
    //private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private EditText inputUsername;
    private EditText inputPassword;
    ProgressBar progressDialog;
    ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private ServerRequest serverRequest;

    String urlProfile = "",nameUser = "",groupUser = "",userNameLogin;
    SessionManager sessionUrlProfile,sessionNameUser,sessionGroupUser,sessionRoleName,sessionGroupID,
            sessionGroupName,sessionRoleType,sessionPhone,sessionEmail,sessionAccess,sessionCover,sessionQtyCidera,sessionWellnessDate;

    SessionManager sessionCode,sessionFullName;
    String urlJsonRoleName = "",urlJsonGroupSC ="",master_group_id,master_group_name;
    Toolbar toolbar;
    TextView btnLinkToRegister;

    private String android_device_id;
    private String android_device_name;
    private String version_release;

    int ok = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);


        inputUsername = (EditText) findViewById(R.id.loginUsername);
        inputPassword = (EditText) findViewById(R.id.loginPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (TextView) findViewById(R.id.btn_register);

        session = new SessionManager(getApplicationContext());
        sessionCode = new SessionManager(getApplicationContext());
        sessionUrlProfile = new SessionManager(getApplicationContext());
        sessionFullName = new SessionManager(getApplicationContext());
        sessionGroupUser = new SessionManager(getApplicationContext());
        sessionRoleName = new SessionManager(getApplicationContext());
        sessionGroupID = new SessionManager(getApplicationContext());
        sessionGroupName = new SessionManager(getApplicationContext());
        sessionRoleType = new SessionManager(getApplicationContext());
        sessionPhone = new SessionManager(getApplicationContext());
        sessionEmail = new SessionManager(getApplicationContext());
        sessionAccess = new SessionManager(getApplicationContext());
        sessionCover = new SessionManager(getApplicationContext());
        sessionQtyCidera = new SessionManager(getApplicationContext());
        sessionWellnessDate = new SessionManager(getApplicationContext());

        android_device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        //android_device_name = DeviceName.getDeviceName();
        android_device_name = Build.MANUFACTURER + " - " + Build.MODEL;
        version_release = Build.VERSION.RELEASE;

        // Progress dialog
        progressDialog = (ProgressBar) findViewById(R.id.progressBar);
        progressDialog.setVisibility(View.GONE);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager


        // Check if user is already logged in or not

        if (session.isLoggedIn()) {

            if (!session.isAthleteIn()) {
                Intent intent = new Intent(LoginActivity.this, AdvancedActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(LoginActivity.this, AdvancedActivityAthlete.class);
                startActivity(intent);
                finish();
            }

        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form

                    if (!username.isEmpty() && !password.isEmpty()) {
                        if(username.contains("'")  || username.contains(":") || username.contains("?") ||
                                username.contains("!")){
                            Toast.makeText(getApplicationContext(),
                                    "Karakter Input tidak diijinkan!", Toast.LENGTH_LONG)
                                    .show();
                        }else {

                            urlJsonRoleName = "http://masterdata.iamprima.com/index.php/JsonRoleName/Username/"+username;
                            urlJsonGroupSC = "http://masterdata.iamprima.com/index.php/JsonGroupSC/Username/"+username;
                            //new GenerateUser().execute();
                            //new getGroupCaborSC().execute();
                            checkLoginAWD(username, password);

                        }


                    } else {
                        // Prompt user to enter credentials
                        Toast.makeText(getApplicationContext(),
                                "Mohon dilengkapi inputan anda!", Toast.LENGTH_LONG)
                                .show();
                    }


            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                LoginActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLoginAWD(final String username, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "login";

       // pDialog.setMessage("Logging in ...");
        showDialog();
        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN_AWD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
               // Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String username = user.getString("username");
                        String created_at = user.getString("created_at");
                        String gambar = user.getString("gambar");

                        // Inserting row in users table
                        db.addUser(name, username, uid, created_at);
                        //sessionCode.CreateSessionKode(inputUsername.getText().toString());

                        // Launch main activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new GenerateUser().execute();
                            }
                        }, 1000);



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
                urlJsonRoleName = "http://masterdata.iamprima.com/index.php/JsonRoleName/Username/"+username;
                urlJsonGroupSC = "http://masterdata.iamprima.com/index.php/JsonGroupSC/Username/"+username;
                checkLogin(username, password);
               // Log.e(TAG, "Login Error: " + error.getMessage());
                /*
                Toast.makeText(getApplicationContext(),
                        error.getMessage() + "Error", Toast.LENGTH_LONG).show();
                hideDialog();
                */
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                params.put("android_device_id", android_device_id);
                params.put("android_device_name", android_device_name);
                params.put("version_release", version_release);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String username, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "login";

        // pDialog.setMessage("Logging in ...");
        showDialog();
        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String username = user.getString("username");
                        String created_at = user.getString("created_at");
                        String gambar = user.getString("gambar");

                        // Inserting row in users table
                        db.addUser(name, username, uid, created_at);
                        //sessionCode.CreateSessionKode(inputUsername.getText().toString());

                        // Launch main activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new GenerateUser().execute();
                            }
                        }, 1000);

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
                /*
                Toast.makeText(getApplicationContext(),
                        error.getMessage() + "Error", Toast.LENGTH_LONG).show();
                hideDialog();
                */
                urlJsonRoleName = "http://masterdata.iamprima.com/index.php/JsonRoleName/Username/"+username;
                urlJsonGroupSC = "http://masterdata.iamprima.com/index.php/JsonGroupSC/Username/"+username;
                checkLogin(username, password);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                params.put("android_device_id", android_device_id);
                params.put("android_device_name", android_device_name);
                params.put("version_release", version_release);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!progressDialog.isShown())
            progressDialog.setVisibility(View.VISIBLE);
    }

    private void hideDialog() {
        if (progressDialog.isShown())
            progressDialog.setVisibility(View.GONE);

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
    }

    public class GenerateUser extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL(urlJsonRoleName);
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
            // tvWeatherJson.setText(s);
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setTitle("Generate Data...");
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.show();

            try {
                JSONObject jsonObj = new JSONObject(s);
                JSONArray jsonArray = jsonObj.getJSONArray("data");
                JSONObject obj = jsonArray.getJSONObject(0);


                sessionCode.CreateSessionKode(obj.getString("username"));
               sessionUrlProfile.CreateSessionUrlProfile(obj.getString("gambar"));
                //sessionUrlProfile.CreateSessionUrlProfile("http://iamprima.com/portal/assets/pictures/9731481551200.jpg");
                sessionGroupUser.CreateSessionGroupGRP(obj.getString("group_id"));
                sessionFullName.CreateSessionUserFullName(obj.getString("name"));
                sessionRoleName.CreateSessionRoleName(obj.getString("role_name"));
                sessionRoleType.CreateSessionRoleType(obj.getString("master_licence_user_groupcode"));
                sessionPhone.CreateSessionPhone(obj.getString("phone"));
                sessionEmail.CreateSessionEmail(obj.getString("email"));
                sessionAccess.CreateSessionAccess(obj.getString("access_all"));
                sessionCover.CreateSessionCover(obj.getString("cover_url"));
                //sessionCover.CreateSessionCover("http://masterdata.iamprima.com/images/bg.jpg");
                sessionQtyCidera.CreateSessionQtyCidera(obj.getString("total_cidera"));
                sessionWellnessDate.CreateSessionWellnessDate(obj.getString("wellness_date"));

                if(obj.getString("master_licence_user_groupcode").equals("ATL")){
                    session.setAthleteLogin(true);
                }


                if(obj.getString("master_licence_user_groupcode").equals("ATL")||
                        obj.getString("master_licence_user_groupcode").equals("CHC")){
                    sessionGroupID.CreateSessionGroupID(obj.getString("group_id"));
                    sessionGroupName.CreateSessionGroupName(obj.getString("role_name"));
                    goHome();

                }else if(obj.getString("master_licence_user_groupcode").equals("SATLAK")||
                        obj.getString("master_licence_user_groupcode").equals("PRIMA")){
                    if(obj.getString("group_id").equals("GW200")) {

                        sessionGroupID.CreateSessionGroupID("GW201");
                        sessionGroupName.CreateSessionGroupName("Taekwondo");

                        goHome();


                    }else if(obj.getString("group_id").equals("GW300")){

                        sessionGroupID.CreateSessionGroupID("GW301");
                        sessionGroupName.CreateSessionGroupName("Voli Pantai");

                        goHome();

                    } else if(obj.getString("group_id").equals("GW400")){

                        sessionGroupID.CreateSessionGroupID("GW401");
                        sessionGroupName.CreateSessionGroupName("Angkat Besi");

                        goHome();

                    }else{
                        sessionGroupID.CreateSessionGroupID("GW203");
                        sessionGroupName.CreateSessionGroupName("Karate");
                        goHome();
                    }
                }else if(obj.getString("master_licence_user_groupcode").equals("HPD")){
                    sessionGroupID.CreateSessionGroupID(obj.getString("group_id"));
                    sessionGroupName.CreateSessionGroupName(obj.getString("role_name"));

                    goHome();
                }else if(obj.getString("master_licence_user_groupcode").equals("RCV")||
                        obj.getString("master_licence_user_groupcode").equals("MLP")){
                    sessionGroupID.CreateSessionGroupID("GW201");
                    sessionGroupName.CreateSessionGroupName("Taekwondo");

                    goHome();

                }else{
                    new getGroupCaborSC().execute();
                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),
                        "Error String Input! ", Toast.LENGTH_LONG)
                        .show();
            }
//            Log.i("json", s);
        }
    }

    public class getGroupCaborSC extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL(urlJsonGroupSC);
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
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setTitle("Generate Data...");
            pDialog.setMessage("Preparing user...");
            pDialog.setIndeterminate(false);
            pDialog.show();

            try {

                JSONObject jsonObj = new JSONObject(s);
                JSONArray jsonArray = jsonObj.getJSONArray("data");
                JSONObject obj = jsonArray.getJSONObject(0);
                sessionGroupID.CreateSessionGroupID(obj.getString("master_group_id"));
                sessionGroupName.CreateSessionGroupName(obj.getString("master_group_name"));

                pDialog.dismiss();
                goHome();

            } catch (JSONException e) {
            }
        }
    }

    public class getGroupWaka extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL(urlJsonGroupSC);
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
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setTitle("Generate Data...");
            pDialog.setMessage("Preparing user...");
            pDialog.setIndeterminate(false);
            pDialog.show();

            try {

                JSONObject jsonObj = new JSONObject(s);
                JSONArray jsonArray = jsonObj.getJSONArray("data");
                JSONObject obj = jsonArray.getJSONObject(0);
                sessionGroupID.CreateSessionGroupID(obj.getString("master_group_id"));
                sessionGroupName.CreateSessionGroupName(obj.getString("master_group_name"));

                pDialog.dismiss();
                goHome();

            } catch (JSONException e) {
            }
        }
    }

    void goHome(){
        Intent login;

        Toast.makeText(getApplication(), "Login Success!", Toast.LENGTH_LONG).show();

        if (!session.isAthleteIn()) {
            login = new Intent(LoginActivity.this, AdvancedActivity.class);
        }else{
            login = new Intent(LoginActivity.this,AdvancedActivityAthlete.class);
        }

        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        finish();
        /** Fading Transition Effect */
        LoginActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }






}
