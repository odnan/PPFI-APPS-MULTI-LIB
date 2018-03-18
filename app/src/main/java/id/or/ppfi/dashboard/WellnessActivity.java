package id.or.ppfi.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.or.ppfi.R;
import id.or.ppfi.config.ServerRequest;
import id.or.ppfi.config.SessionManager;
import id.or.ppfi.entities.M_GroupPrima;


/**
 * Created by emergency on 09/10/2016.
 */

public class WellnessActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final double PIC_WIDTH = 30;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    Toolbar toolbar;
    String moduleName,moduleSubName,urlModule;
    WebView webview;

    public Uri imageUri;

    private static final int FILECHOOSER_RESULTCODE   = 2888;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;

    private String Username,GroupName;
    SessionManager sessionCode;
    private String urlGeneralLog = "";
    int id_menu = 0;
    private String android_device_id;
    private String android_device_name;
    private String version_release;

    SessionManager sessionGroupName;
    ImageView ic_arow_up,ic_arow_down;
    RelativeLayout relative_wellness_show_cabor;


    private Spinner spinnerGroupWellness;
    private ArrayList<M_GroupPrima> groupList;
    int lablesSpinner = 0;
    String GroupCodeTemp = "";

    TextView text_wp_percentage_grey,text_wp_percentage_red,text_wp_percentage_yellow,
            text_wp_percentage_orange,text_wp_percentage_green,text_wp_percentage_green_arrow;

    ServerRequest serverRequest;
    TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_module_wellness_percentage);

        android_device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        android_device_name = Build.MANUFACTURER + " - " + Build.MODEL;
        android_device_name = android_device_name.replace(" ","CEK");
        version_release = Build.VERSION.RELEASE;

        sessionCode = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionCode.GetSessionKode();
        Username = user.get(SessionManager.KEY_USERNAME);

        sessionGroupName = new SessionManager(getApplicationContext());
        HashMap<String, String> group = sessionGroupName.GetSessionGroupName();
        GroupName = group.get(SessionManager.KEY_GROUP_NAME);

        //   progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //   progressBar.setVisibility(View.GONE);

        Intent data = getIntent();
        moduleName = data.getStringExtra("nama_modul");
        moduleSubName = data.getStringExtra("sub_nama_modul");
        urlModule = data.getStringExtra("url_modul");


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wellness");
        getSupportActionBar().setSubtitle("Percentage Preview");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b71c1c")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        spinnerGroupWellness = (Spinner) findViewById(R.id.spinner_group_wellness);
        spinnerGroupWellness.setOnItemSelectedListener(this);


        //ACTIVITY DASHBOARD WELLNESS PERCENTAGE
        text_wp_percentage_grey = (TextView) findViewById(R.id.text_percentage_grey);
        text_wp_percentage_red = (TextView) findViewById(R.id.text_percentage_red);
        text_wp_percentage_yellow = (TextView) findViewById(R.id.text_percentage_yellow);
        text_wp_percentage_orange = (TextView) findViewById(R.id.text_percentage_orange);
        text_wp_percentage_green = (TextView) findViewById(R.id.text_percentage_green);
        text_wp_percentage_green_arrow = (TextView) findViewById(R.id.text_percentage_green_arrow);

        text1 = (TextView) findViewById(R.id.text_1);


        ic_arow_up = (ImageView) findViewById(R.id.ic_arow_up);
        ic_arow_down = (ImageView) findViewById(R.id.ic_arow_down);
        relative_wellness_show_cabor = (RelativeLayout) findViewById(R.id.relative_wellness_show_cabor);


        ic_arow_down.setVisibility(View.VISIBLE);
        ic_arow_up.setVisibility(View.GONE);
        relative_wellness_show_cabor.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new generateWellnessSUM().execute();
            }
        }, 1000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new getDataWelnessWP().execute();
            }
        }, 1000);


        ic_arow_up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ic_arow_up.setVisibility(View.GONE);
                ic_arow_down.setVisibility(View.VISIBLE);
                relative_wellness_show_cabor.setVisibility(View.GONE);

            }
        });

        ic_arow_down.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (lablesSpinner == 0){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            serverRequest = new ServerRequest();
                            groupList = new ArrayList<M_GroupPrima>();
                            new GenerateGroupSpinner().execute();
                        }
                    }, 1000);
                }

                ic_arow_up.setVisibility(View.VISIBLE);
                ic_arow_down.setVisibility(View.GONE);
                relative_wellness_show_cabor.setVisibility(View.VISIBLE);

            }
        });

    }

    public void loadGreyWP(View v) {
        int allCabor = 0;
        if(GroupCodeTemp.equals("All Cabor")){
            allCabor = 1;
        }else{
            allCabor = 0;
        }
        Intent i = new Intent(WellnessActivity.this, ListWellnessActivity.class);
        i.putExtra("wellnessRate", "0");
        i.putExtra("allCabor", allCabor);
        i.putExtra("groupCode", GroupCodeTemp);
        startActivity(i);
        WellnessActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public void loadRedWP(View v) {

    }

    public void loadYellowWP(View v) {

    }

    public void loadOrangeWP(View v) {

    }

    public void loadGreenWP(View v) {

    }

    public void loadGreenArrowWP(View v) {

    }

    private void populateSpinnerGroup() {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < groupList.size(); i++) {
            lables.add(groupList.get(i).getGroupName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, lables);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);

        spinnerGroupWellness.setAdapter(spinnerAdapter);

        if(lables != null)
            lablesSpinner = 1;
        else
            lablesSpinner = 0;


    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                goToMainActivity();
                break;
            case R.id.refresh:
               // refreshData();

                webview.setVisibility(webview.VISIBLE);
                text1.setVisibility(text1.GONE);

                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);

                new GenerateUser().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToMainActivity() {
        id_menu = 8;
        urlGeneralLog = "http://masterdata.or.web.id/index.php/JsonInsertLog/GeneralLogClose/"+Username+"/"+android_device_id+"/"+android_device_name+"/"+version_release+"/"+id_menu;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new InsertLog().execute();
            }
        }, 1000);
        this.finish();
    }


    void refreshData(){
        if(!isNetworkAvailable()){
            webview.setVisibility(webview.GONE);
            text1.setVisibility(text1.VISIBLE);
        }else{
            webview.setVisibility(webview.VISIBLE);
            text1.setVisibility(text1.GONE);
        }


        webview.loadUrl(urlModule);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setWebViewClient(new WebViewClientDemo());
        webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        webview.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
           webview.setWebContentsDebuggingEnabled(true);
        }

        webview.requestFocus(View.FOCUS_DOWN);
        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });


    }

    private int getScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(PIC_WIDTH);
        val = val * 100d;
        return val.intValue();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        /*
        Toast.makeText(
                getApplicationContext(),
                parent.getItemAtPosition(position).toString() + " Selected" ,
                Toast.LENGTH_LONG).show();
        */

        text1.setText(parent.getItemAtPosition(position).toString());

        if(parent.getItemAtPosition(position).toString().equals("All Cabor")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new getDataWelnessWP().execute();
                }
            }, 1000);
        } else {
            GroupCodeTemp = groupList.get(position).getGroupCode();

            if(GroupCodeTemp != ""){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new getDataWelnessWPByGroup().execute();
                    }
                }, 1000);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class WebViewClientDemo extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //pDialog.dismiss();
            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(100);
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            /*
            pDialog = new ProgressDialog(WebviewActivity.this);
            pDialog.setMessage("Loading Document...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            */
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        if(requestCode==FILECHOOSER_RESULTCODE)
        {

            if (null == this.mUploadMessage) {
                return;

            }

            Uri result=null;

            try{
                if (resultCode != RESULT_OK) {

                    result = null;

                } else {

                    // retrieve from the private variable if the intent is null
                    result = intent == null ? mCapturedImageURI : intent.getData();
                }
            }
            catch(Exception e)
            {
                Toast.makeText(getApplicationContext(), "activity :"+e,
                        Toast.LENGTH_LONG).show();
            }

            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        }

    }

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {

        try {
            if(webview.canGoBack()) {
                 webview.goBack();

            } else {
                // Let the system handle the back button
                super.onBackPressed();
            }
        }catch (Exception e){

        }

    }


    public class GenerateUser extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {

                URL url = new URL("http://masterdata.or.web.id/index.php/JsonRoleName/Username/"+Username);
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

            try {

                try{
                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("data");
                    JSONObject obj = jsonArray.getJSONObject(0);

                    if(obj.getString("username") != null)
                        refreshData();
                    else
                        Toast.makeText(getApplicationContext(),
                            "Webview Failed Request Data", Toast.LENGTH_LONG)
                            .show();

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error Parsing Rolename , Username : "+Username, Toast.LENGTH_LONG)
                            .show();
                }

            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                        "Please Cek Your Wifi or Data Connection!", Toast.LENGTH_LONG)
                        .show();

                webview.setVisibility(webview.GONE);
                text1.setVisibility(text1.VISIBLE);
                text1.setText("Bad Connection!");

                progressBar.setVisibility(View.GONE);
                progressBar.setProgress(100);
            }


//            Log.i("json", s);
        }
    }

    //GENERATE GENERAL LOG
    public class InsertLog extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL(urlGeneralLog);
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

        }
    }
    //GENERATE GENERAL LOG


    public class getDataWelnessWP extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.or.web.id/index.php/JsonQtyWellness/WellnessPercentage/"+Username);
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
            try{
                try {


                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("data");

                    JSONObject obj = jsonArray.getJSONObject(0);
                    text_wp_percentage_grey.setText(obj.getString("total_atlet") +" Atlet\n"+
                            obj.getString("total_percentage")+" %");

                    JSONObject obj1 = jsonArray.getJSONObject(1);
                    text_wp_percentage_red.setText(obj1.getString("total_atlet") +" Atlet\n"+
                            obj1.getString("total_percentage")+" %");

                    JSONObject obj2 = jsonArray.getJSONObject(2);
                    text_wp_percentage_yellow.setText(obj2.getString("total_atlet") +" Atlet\n"+
                            obj2.getString("total_percentage")+" %");

                    JSONObject obj3 = jsonArray.getJSONObject(3);
                    text_wp_percentage_orange.setText(obj3.getString("total_atlet") +" Atlet\n"+
                            obj3.getString("total_percentage")+" %");

                    JSONObject obj4 = jsonArray.getJSONObject(4);
                    text_wp_percentage_green.setText(obj4.getString("total_atlet") +" Atlet\n"+
                            obj4.getString("total_percentage")+" %");

                    JSONObject obj5 = jsonArray.getJSONObject(5);
                    text_wp_percentage_green_arrow .setText(obj5.getString("total_atlet") +" Atlet\n"+
                            obj5.getString("total_percentage")+" %");



                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error String Input! Get Data wellness... Denis Oke Oce", Toast.LENGTH_LONG)
                            .show();
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                        "Bad Connection!", Toast.LENGTH_LONG)
                        .show();
            }


//            Log.i("json", s);
        }
    }

    public class getDataWelnessWPByGroup extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.or.web.id/index.php/JsonQtyWellness/WellnessPercentageByGroup/"+GroupCodeTemp);
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
            try{
                try {

                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("data");


                    JSONObject obj = jsonArray.getJSONObject(0);
                    text_wp_percentage_grey.setText(obj.getString("total_atlet") +" Atlet\n"+
                            obj.getString("total_percentage")+" %");

                    JSONObject obj1 = jsonArray.getJSONObject(1);
                    text_wp_percentage_red.setText(obj1.getString("total_atlet") +" Atlet\n"+
                            obj1.getString("total_percentage")+" %");

                    JSONObject obj2 = jsonArray.getJSONObject(2);
                    text_wp_percentage_yellow.setText(obj2.getString("total_atlet") +" Atlet\n"+
                            obj2.getString("total_percentage")+" %");

                    JSONObject obj3 = jsonArray.getJSONObject(3);
                    text_wp_percentage_orange.setText(obj3.getString("total_atlet") +" Atlet\n"+
                            obj3.getString("total_percentage")+" %");

                    JSONObject obj4 = jsonArray.getJSONObject(4);
                    text_wp_percentage_green.setText(obj4.getString("total_atlet") +" Atlet\n"+
                            obj4.getString("total_percentage")+" %");

                    JSONObject obj5 = jsonArray.getJSONObject(5);
                    text_wp_percentage_green_arrow .setText(obj5.getString("total_atlet") +" Atlet\n"+
                            obj5.getString("total_percentage")+" %");



                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "No Data", Toast.LENGTH_LONG)
                            .show();

                    text_wp_percentage_grey.setText(" 0 Atlet\n 0.00 %");

                    text_wp_percentage_red.setText(" 0 Atlet\n 0.00 %");

                    text_wp_percentage_yellow.setText(" 0 Atlet\n 0.00 %");

                    text_wp_percentage_orange.setText(" 0 Atlet\n 0.00 %");

                    text_wp_percentage_green.setText(" 0 Atlet\n 0.00 %");

                    text_wp_percentage_green_arrow.setText(" 0 Atlet\n 0.00 %");


                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                        "Bad Connection!", Toast.LENGTH_LONG)
                        .show();
            }


//            Log.i("json", s);
        }
    }


    private class GenerateGroupSpinner extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(WellnessActivity.this);
            progressDialog.setMessage("Loading Group...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String response =  serverRequest.sendGetRequest(ServerRequest.urlSpinnerAllGroup+Username);

            if (response != null) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("data");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);
                            M_GroupPrima cat = new M_GroupPrima(
                                    catObj.getString("username"),
                                    catObj.getString("master_group_id"),
                                    catObj.getString("master_group_name"),
                                    catObj.getString("master_group_jlh_atlet"),
                                    catObj.getString("master_group_jlh_pelatih"),
                                    catObj.getString("master_group_head"),
                                    catObj.getString("master_group_logo"),
                                    catObj.getString("master_group_type"),
                                    catObj.getString("master_group_status"),
                                    catObj.getString("total_ksc"),
                                    catObj.getString("total_ssc"),
                                    catObj.getString("total_hpd"),
                                    catObj.getString("master_group_category")
                            );

                            groupList.add(cat);
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
            populateSpinnerGroup();
        }

    }

    //GENERATE WELLNESS SUM
    public class generateWellnessSUM extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.or.web.id/index.php/JsonQtyWellness/GenerateWellnessSUM/"+Username);
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

        }
    }
    //GENERATE WELLNESS SUM


}