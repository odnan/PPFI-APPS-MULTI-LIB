package id.or.ppfi.user;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.or.ppfi.R;
import id.or.ppfi.config.AppConfig;
import id.or.ppfi.config.AppController;
import id.or.ppfi.config.SQLiteHandler;
import id.or.ppfi.config.ServerRequest;
import id.or.ppfi.config.SessionManager;
import id.or.ppfi.entities.M_Provinsi;
import id.or.ppfi.listadapter.ListAdapterProvinsi;
import id.or.ppfi.main.AdvancedActivity;
import id.or.ppfi.main.AdvancedActivityAthlete;


public class RegisterActivity extends Activity implements AdapterView.OnItemSelectedListener {
    //private static final String TAG = RegisterActivity.class.getSimpleName();
    Button btnRegister;
    TextView btnLinkToLogin;
    EditText inputFullName;
    EditText inputUsername;
    EditText inputLicenseCode;
    EditText inputPassword;
    EditText inputBirthDate;
    private  String insertBirthDate;
    EditText inputProvince;
    private ProgressBar progressBar,progressBarList;
    private ProgressDialog progressDialog;
    private SessionManager session;
    private SQLiteHandler db;

    private String android_device_id;
    private String android_device_name;
    private String version_release;



    private List<M_Provinsi> listDataProvinsi;
    private M_Provinsi selectedListProvinsi;
    ListAdapterProvinsi adapterProvinsi;
    SearchView searchViewProvinsi;
    ListView listViewProvinsi;
    ServerRequest serverRequest;

    DatePickerDialog datePickerDialog;

    private Spinner spinnerProvince;
    // array list for spinner adapter
    private ArrayList<M_Provinsi> provinceList;

    TextView text1,text2;

    RadioGroup radioGroup;


    SessionManager sessionCode,sessionFullName,sessionUrlProfile,sessionGroupUser,sessionRoleName,sessionGroupID,
            sessionGroupName,sessionRoleType,sessionAccess,sessionCover,sessionQtyCidera;

    String urlJsonRoleName = "",urlJsonGroupSC ="",master_group_id,master_group_name;
    Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        inputFullName = (EditText) findViewById(R.id.registerName);
        inputUsername = (EditText) findViewById(R.id.registerUserName);
        inputPassword = (EditText) findViewById(R.id.registerPassword);
        inputLicenseCode = (EditText) findViewById(R.id.registerLicenseCode);
        inputBirthDate = (EditText) findViewById(R.id.registerBirthDate);
       // inputProvince = (EditText) findViewById(R.id.registerProvince);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb=(RadioButton)findViewById(checkedId);

                if(rb.getText().equals("Male"))
                    text2.setText("laki");
                else
                    text2.setText("perempuan");
                //Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (TextView) findViewById(R.id.btnLinkToLoginScreen);

        spinnerProvince = (Spinner) findViewById(R.id.spinner);

        sessionCode = new SessionManager(getApplication());
        sessionFullName = new SessionManager(getApplication());
        sessionUrlProfile = new SessionManager(getApplicationContext());
        sessionFullName = new SessionManager(getApplicationContext());
        sessionGroupUser = new SessionManager(getApplicationContext());
        sessionRoleName = new SessionManager(getApplicationContext());
        sessionGroupID = new SessionManager(getApplicationContext());
        sessionGroupName = new SessionManager(getApplicationContext());
        sessionRoleType = new SessionManager(getApplicationContext());
        sessionAccess = new SessionManager(getApplicationContext());
        sessionCover = new SessionManager(getApplicationContext());
        sessionQtyCidera = new SessionManager(getApplicationContext());


        android_device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        //android_device_name = DeviceName.getDeviceName();
        android_device_name = Build.MANUFACTURER + " - " + Build.MODEL;
        version_release = Build.VERSION.RELEASE;

        // Progress dialog
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        /*
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, AlarmMainActivity.class);
            startActivity(intent);
            finish();
        }


*/

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String dateNow = df.format(new Date());
        inputBirthDate.setText(dateNow);

       inputBirthDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
               // calender class's instance and get current date , month and year from calender
               final Calendar c = Calendar.getInstance();
               int mYear = c.get(Calendar.YEAR); // current year
               int mMonth = c.get(Calendar.MONTH); // current month
               int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

               // date picker dialog
               datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                       new DatePickerDialog.OnDateSetListener() {

                           @Override
                           public void onDateSet(DatePicker view, int year,
                                                 int monthOfYear, int dayOfMonth) {
                               // set day of month , month and year value in the edit text

                               String stringMonth = "";
                               int month = (monthOfYear + 1);

                               if (month >0 && month <10)
                                   stringMonth = "0" + String.valueOf(month);
                               else
                                   stringMonth = String.valueOf(month);

                               String stringdayOfMonth = "";
                               if (dayOfMonth >0 && dayOfMonth <10)
                                   stringdayOfMonth = "0" + String.valueOf(dayOfMonth);
                               else
                                   stringdayOfMonth = String.valueOf(dayOfMonth);

                               inputBirthDate.setText(stringdayOfMonth + "-" + stringMonth + "-" +  year);
                               insertBirthDate = year + "-" + stringMonth + "-" + stringdayOfMonth ;

                               /*
                               Toast.makeText(getApplicationContext(),
                                       "insertBirthDate " + insertBirthDate, Toast.LENGTH_LONG).show();
                                       */

                           }
                       }, mYear, mMonth, mDay);
               datePickerDialog.show();
           }
       });

        // Login button Click Event
        btnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String name = inputFullName.getText().toString().trim();
                String licence_code = inputLicenseCode.getText().toString().trim();
                //String birthdate = inputBirthDate.getText().toString();
                String gender = text2.getText().toString();
                String province = text1.getText().toString();

                insertBirthDate = inputBirthDate.getText().toString();

                /*
                Toast.makeText(getApplicationContext(),
                        "insertBirthDate " + insertBirthDate, Toast.LENGTH_LONG).show();
                        */


                // Check for empty data in the form
                if (!username.isEmpty() && !password.isEmpty() && !name.isEmpty() && !licence_code.isEmpty()
                        && !insertBirthDate.isEmpty() && !gender.isEmpty() && !province.isEmpty()) {
                    // login user
                   // sessionCode.CreateSessionKode(username);
                   // sessionFullName.CreateSessionUserFullName(name);
                   // sessionUrlProfile.CreateSessionUrlProfile("http://portal.iamprima.com/assets/pictures/9731481551200.jpg");
                    urlJsonRoleName = "http://masterdata.iamprima.com/index.php/JsonRoleName/Username/"+username;
                    urlJsonGroupSC = "http://masterdata.iamprima.com/index.php/JsonGroupSC/Username/"+username;

                    if (containsWhiteSpace(username))
                        errorDialogUsernameSpace();
                    else
                    checkRegister(username,name,licence_code, password,insertBirthDate,gender,province);


                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                RegisterActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        serverRequest = new ServerRequest();
        provinceList = new ArrayList<M_Provinsi>();
        spinnerProvince.setOnItemSelectedListener(this);
        new GenerateProvinsi().execute();


    }


    public static boolean containsWhiteSpace(final String testCode){
        if(testCode != null){
            for(int i = 0; i < testCode.length(); i++){
                if(Character.isWhitespace(testCode.charAt(i))){
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * function to verify login details in mysql db
     * */
    private void checkRegister(final String username, final String name,final String licence_code,final String password, final String birthdate,final String gender,final String province) {
        // Tag used to cancel the request
        String tag_string_req = "register";

       // pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER_AWD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
               // Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // Launch main activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new GenerateUser().execute();
                            }
                        }, 1000);
                        /*
                        // Launch main activity
                        Toast.makeText(getApplication(), "Register sukses ", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(RegisterActivity.this,LoginActivity2.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("form", "Register");
                        i.putExtra("username", username);
                        i.putExtra("password", password);
                        startActivity(i);
                        finish();
                        RegisterActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        */
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        /*
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                                */


                        if(errorMsg.equals("User already existed"))
                            errorDialogUsername();
                        else if(errorMsg.equals("License Code already existed"))
                            errorDialogLicenceExisted();
                        else if(errorMsg.equals("License Code didn't match"))
                            errorDialogLicenceDidntMatch();
                        else if(errorMsg.equals("Licence Code Was Suspended"))
                            errorDialogLicenceSuspend();


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
              //  Toast.makeText(getApplicationContext(),error.getMessage() + "Error", Toast.LENGTH_LONG).show();
                // Launch main activity

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new GenerateUser().execute();
                    }
                }, 1000);
                /*
                Toast.makeText(getApplication(), "Register Success!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(RegisterActivity.this,LoginActivity2.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("form", "Register");
                i.putExtra("username", username);
                i.putExtra("password", password);
                startActivity(i);
                finish();
                RegisterActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                hideDialog();
                */
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("name", name);
                params.put("password", password);
                params.put("license_code", licence_code);
                params.put("birthdate", birthdate);
                params.put("gender", gender);
                params.put("province", province);
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
        if (!progressBar.isShown())
            progressBar.setVisibility(View.VISIBLE);
    }

    private void hideDialog() {
        if (progressBar.isShown())
            progressBar.setVisibility(View.GONE);

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
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setTitle("Generate Data...");
            progressDialog.setMessage("Please wait...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();

            try {
                JSONObject jsonObj = new JSONObject(s);
                JSONArray jsonArray = jsonObj.getJSONArray("data");
                JSONObject obj = jsonArray.getJSONObject(0);


                sessionCode.CreateSessionKode(obj.getString("username"));
                sessionUrlProfile.CreateSessionUrlProfile("http://iamprima.com/portal/assets/pictures/9731481551200.jpg");
                sessionGroupUser.CreateSessionGroupGRP(obj.getString("group_id"));
                sessionFullName.CreateSessionUserFullName(obj.getString("name"));
                sessionRoleName.CreateSessionRoleName(obj.getString("role_name"));
                sessionRoleType.CreateSessionRoleType(obj.getString("master_licence_user_groupcode"));
                sessionAccess.CreateSessionAccess(obj.getString("access_all"));
                sessionCover.CreateSessionCover("http://masterdata.iamprima.com/images/bg.jpg");
                sessionQtyCidera.CreateSessionQtyCidera(obj.getString("total_cidera"));


                if(obj.getString("master_licence_user_groupcode").equals("ATL")){
                    try{
                        sessionGroupID.CreateSessionGroupID(obj.getString("group_id"));
                        sessionGroupName.CreateSessionGroupName(obj.getString("role_name"));
                        session.setAthleteLogin(true);
                        goHomeAthlete();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),
                                "Something Wrong", Toast.LENGTH_LONG)
                                .show();
                    }
                }else if(obj.getString("master_licence_user_groupcode").equals("CHC")){
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

                } else{
                    new getGroupCaborSC().execute();
                }


            } catch (JSONException e) {
               // e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Error String Input!", Toast.LENGTH_LONG)
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
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setTitle("Generate Data...");
            progressDialog.setMessage("Preparing user...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();

            try {

                JSONObject jsonObj = new JSONObject(s);
                JSONArray jsonArray = jsonObj.getJSONArray("data");
                JSONObject obj = jsonArray.getJSONObject(0);
                sessionGroupID.CreateSessionGroupID(obj.getString("master_group_id"));
                sessionGroupName.CreateSessionGroupName(obj.getString("master_group_name"));
                goHome();

            } catch (JSONException e) {
            }
        }
    }

    void goHome(){
        Toast.makeText(getApplication(), "Register Success!", Toast.LENGTH_LONG).show();
        Intent login = new Intent(RegisterActivity.this,AdvancedActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        finish();
        /** Fading Transition Effect */
        RegisterActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    void goHomeAthlete(){
        Toast.makeText(getApplication(), "Register Athlete Success!", Toast.LENGTH_LONG).show();
        Intent login = new Intent(RegisterActivity.this,AdvancedActivityAthlete.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        finish();
        /** Fading Transition Effect */
        RegisterActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    private List<M_Provinsi> processResponseProvince(String response) {
        List<M_Provinsi> list = new ArrayList<M_Provinsi>();
        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            M_Provinsi data = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                data = new M_Provinsi();
                data.setProvinsiID(obj.getString("provinsi_id"));
                data.setProvinsiName(obj.getString("provinsi_nama"));
                list.add(data);
            }
        } catch (JSONException e) {
        }
        return list;
    }

    private void populateSpinnerProvince() {
        List<String> lables = new ArrayList<String>();



        for (int i = 0; i < provinceList.size(); i++) {
            lables.add(provinceList.get(i).getProvinsiName());

        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerProvince.setAdapter(spinnerAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

       /* Toast.makeText(
                getApplicationContext(),
                parent.getItemAtPosition(position).toString() + " Selected" ,
                Toast.LENGTH_LONG).show();
                */

            text1.setText(provinceList.get(position).getProvinsiID());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class GenerateProvinsi extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Fetching Data...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String response = serverRequest.sendGetRequest(ServerRequest.urlProvinsi);


            if (response != null) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("data");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);
                            M_Provinsi cat = new M_Provinsi(catObj.getString("provinsi_id"),
                                    catObj.getString("provinsi_nama"));
                            provinceList.add(cat);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            populateSpinnerProvince();
        }

    }

    public void errorDialogUsernameSpace(){
        new AlertDialog.Builder(RegisterActivity.this)
                .setTitle("Username mengandung karakter spasi!")
                .setMessage("Silakan menambahkan angka atau karakter lain selain spasi dan simbol")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Whatever...
                    }
                }).show();
    }

    public void errorDialogUsername(){
        new AlertDialog.Builder(RegisterActivity.this)
                .setTitle("Username tidak dapat digunakan!")
                .setMessage("Silakan menambahkan angka atau karakter lain selain spasi dan simbol")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Whatever...
                    }
                }).show();
    }

    public void errorDialogLicenceExisted(){
        new AlertDialog.Builder(RegisterActivity.this)
                .setTitle("Licence Code Existed!")
                .setMessage("Gunakan Licence Code yang lain...")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Whatever...
                    }
                }).show();
    }

    public void errorDialogLicenceSuspend(){
        new AlertDialog.Builder(RegisterActivity.this)
                .setTitle("Licence Code Was Suspended!")
                .setMessage("Try another licence or Call IAM PRIMA team")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Whatever...
                    }
                }).show();
    }


    public void errorDialogLicenceDidntMatch(){
        new AlertDialog.Builder(RegisterActivity.this)
                .setTitle("Licence Code Didn't Match!")
                .setMessage("Try Again or Call IAM PRIMA team")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Whatever...
                    }
                }).show();
    }



}
