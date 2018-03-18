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
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
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
import java.util.HashMap;

import id.or.ppfi.R;
import id.or.ppfi.config.SessionManager;


/**
 * Created by emergency on 09/10/2016.
 */

public class WebviewActivity extends AppCompatActivity {
    private static final double PIC_WIDTH = 30;
    private ProgressDialog pDialog;
    private ProgressBar progressBar;
    Toolbar toolbar;
    String moduleName,moduleSubName,urlModule,highlight,group_name;
    WebView webview;
    TextView text1;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_webview);

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

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        Intent data = getIntent();
        moduleName = data.getStringExtra("nama_modul");
        moduleSubName = data.getStringExtra("sub_nama_modul");
        urlModule = data.getStringExtra("url_modul");
        highlight = data.getStringExtra("highlight");
        group_name = data.getStringExtra("group_name");


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(moduleName);

        try {
            if(highlight.equals("1"))
                getSupportActionBar().setSubtitle(group_name);
            else
                getSupportActionBar().setSubtitle(GroupName);
        }catch (Exception e){
            getSupportActionBar().setSubtitle(GroupName);
        }


        if(moduleName.equals("Meal Plan"))
            getSupportActionBar().setSubtitle(moduleSubName);

       // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3E50B4")));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b71c1c")));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webview = (WebView) findViewById(R.id.webView);
        text1 = (TextView) findViewById(R.id.text1);


        if(!isNetworkAvailable()){
            webview.setVisibility(webview.GONE);
            text1.setVisibility(text1.VISIBLE);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(0);

                    new GenerateUser().execute();
                }
            }, 1000);

        }

       // refreshData();
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

        if(moduleName.equals("Performance Profiling"))
             id_menu = 9;
        else if(moduleName.equals("Wellness"))
            id_menu = 8;
        else if(moduleName.equals("Training Load"))
            id_menu = 10;
        else if(moduleName.equals("PMC"))
            id_menu = 13;
        else if(moduleName.equals("Meal Plan"))
            id_menu = 12;
        else if(moduleName.equals("Recovery Management"))
            id_menu = 11;

        urlGeneralLog = "http://dlog.iamprima.com/index.php/JsonInsertLog/GeneralLogClose/"+Username+"/"+android_device_id+"/"+android_device_name+"/"+version_release+"/"+id_menu;

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

    private void startWebView() {



        // Create new webview Client to show progress dialog
        // Called When opening a url or click on link
        // You can create external class extends with WebViewClient
        // Taking WebViewClient as inner class

        webview.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are open in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // Check if Url contains ExternalLinks string in url
                // then open url in new browser
                // else all webview links will open in webview browser
                if(url.contains("google")){

                    // Could be cleverer and use a regex
                    //Open links in new browser
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    // Here we can open new activity

                    return true;

                } else {

                    // Stay within this webview and load url
                    view.loadUrl(url);
                    return true;
                }

            }



            //Show loader on url load
            public void onLoadResource (WebView view, String url) {

                // if url contains string androidexample
                // Then show progress  Dialog
                if (progressDialog == null && url.contains("androidexample")
                        ) {

                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(WebviewActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }

            // Called when all page resources loaded
            public void onPageFinished(WebView view, String url) {

                try{
                    // Close progressDialog
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }

        });


        // You can create external class extends with WebChromeClient
        // Taking WebViewClient as inner class
        // we will define openFileChooser for select file from camera or sdcard

        webview.setWebChromeClient(new WebChromeClient() {
        /*
            // openFileChooser for Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType){

                // Update message
                mUploadMessage = uploadMsg;

                try{

                    // Create AndroidExampleFolder at sdcard

                    File imageStorageDir = new File(
                            Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES)
                            , "PrimaFolderImg");

                    if (!imageStorageDir.exists()) {
                        // Create AndroidExampleFolder at sdcard
                        imageStorageDir.mkdirs();
                    }

                    // Create camera captured image file path and name
                    File file = new File(
                            imageStorageDir + File.separator + "IMG_"
                                    + String.valueOf(System.currentTimeMillis())
                                    + ".jpg");

                    mCapturedImageURI = Uri.fromFile(file);

                    // Camera capture image intent
                    final Intent captureIntent = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");

                    // Create file chooser intent
                    Intent chooserIntent = Intent.createChooser(i, "Image Chooser");

                    // Set camera intent to file chooser
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                            , new Parcelable[] { captureIntent });

                    // On select image call onActivityResult method of activity
                    startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);

                }
                catch(Exception e){
                    Toast.makeText(getBaseContext(), "Exception:"+e,
                            Toast.LENGTH_LONG).show();
                }

            }

            // openFileChooser for Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg){
                openFileChooser(uploadMsg, "");
            }

            //openFileChooser for other Android versions
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType,
                                        String capture) {

                openFileChooser(uploadMsg, acceptType);
            }
            */

            //For Android 4.1
            //For Android 4.1+ only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }




            // The webPage has 2 filechoosers and will send a
            // console message informing what action to perform,
            // taking a photo or updating the file

            public boolean onConsoleMessage(ConsoleMessage cm) {

                onConsoleMessage(cm.message(), cm.lineNumber(), cm.sourceId());
                return true;
            }

            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                //Log.d("androidruntime", "Show console messages, Used for debugging: " + message);

            }
        });   // End setWebChromeClient

    }

    // Return here when file selected from camera or from SDcard

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

        if(webview.canGoBack()) {

            webview.goBack();

        } else {
            // Let the system handle the back button
            super.onBackPressed();
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

                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonRoleName/Username/"+Username);
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



}