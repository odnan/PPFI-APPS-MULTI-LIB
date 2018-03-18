package id.or.ppfi.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import id.or.ppfi.R;
import id.or.ppfi.config.SessionManager;


/**
 * Created by emergency on 09/10/2016.
 */

public class DetailPersonalActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private ProgressBar progressBar;
    Toolbar toolbar;
    String moduleName,urlModule,member_username,member_name,member_group_code,member_group_name;
    WebView webview;
    TextView text1;

    public Uri imageUri;

    private static final int FILECHOOSER_RESULTCODE   = 2888;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;

    SessionManager sessionCode;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_webview);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        sessionCode = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionCode.GetSessionKode();
        username = user.get(SessionManager.KEY_USERNAME);


        Intent data = getIntent();
        moduleName = data.getStringExtra("nama_modul");
        member_username = data.getStringExtra("member_username");
        member_name = data.getStringExtra("member_name");
        member_group_code = data.getStringExtra("member_group_code");
        member_group_name = data.getStringExtra("member_group_name");
        urlModule = "http://account.iamprima.com/index.php/login_validation/index/id/"+username+"/atlet/"+member_username;


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(moduleName);
        getSupportActionBar().setSubtitle(member_name);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3E50B4")));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b71c1c")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webview = (WebView) findViewById(R.id.webView);
        text1 = (TextView) findViewById(R.id.text1);
        if(!isNetworkAvailable()){
            webview.setVisibility(webview.GONE);
            text1.setVisibility(text1.VISIBLE);
        }else{
            webview.setVisibility(webview.VISIBLE);
            text1.setVisibility(text1.GONE);
        }
        refreshData();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_personal, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                goToMainActivity();
                break;
            case R.id.menu_defult:
                getSupportActionBar().setTitle("Highlight");
                urlModule = "http://account.iamprima.com/index.php/login_validation/index/id/"+username+"/atlet/"+member_username;
                refreshData();
                break;
            case R.id.menu_1:
                getSupportActionBar().setTitle("Performance Profilling");
                urlModule = "http://profiling.iamprima.com/index.php/login_validation/index/id/"+username+"/atlet/"+member_username;
                refreshData();
                break;
            case R.id.menu_2:
                getSupportActionBar().setTitle("Wellness");
                urlModule = "http://wellness.iamprima.com/index.php/login_validation/index/id/"+username+"/atlet/"+member_username;
                refreshData();
                break;
            case R.id.menu_3:
                getSupportActionBar().setTitle("Training Load");
                urlModule = "http://monotony.iamprima.com/index.php/login_validation/index/id/"+username+"/atlet/"+member_username;
                refreshData();
                break;
            case R.id.menu_4:
                getSupportActionBar().setTitle("PMC");

                urlModule = "http://pmc.iamprima.com/index.php/login_validation/index/id/"+username+"/atlet/"+member_username;
                refreshData();
                break;
            case R.id.menu_5:
                getSupportActionBar().setTitle("Meal Plan");
                urlModule = "http://mealplan.or.web.id";
                //urlModule = "http://mealplan.awd.web.id/index.php/login_validation/index/id/"+username+"/atlet/"+member_username;
                //urlModule = "";
                refreshData();
                break;
            case R.id.menu_6:
                getSupportActionBar().setTitle("Recovery");
                urlModule = "http://recovery.iamprima.com/index.php/login_validation/index/id/"+username+"/atlet/"+member_username;
                refreshData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToMainActivity() {
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

        /*
        // Define url that will open in webview
        String webViewUrl = urlModule;
        // Javascript inabled on webview
        webview.getSettings().setJavaScriptEnabled(true);
        // Other webview options
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        //Other webview settings
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.setScrollbarFadingEnabled(false);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setSupportZoom(true);
        //Load url in webview
        webview.loadUrl(webViewUrl);
        // Define Webview manage classes
        startWebView();
        */

        webview.loadUrl(urlModule);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setAppCacheMaxSize(1024 * 1024 * 10);
        webview.getSettings().setAppCachePath("/data/data/" + getPackageName());
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.setScrollbarFadingEnabled(false);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setWebViewClient(new WebViewClientDemo());
        webview.setScrollbarFadingEnabled(false);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.setScrollbarFadingEnabled(false);

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
            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(100);
            //pDialog.dismiss();
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            /*
            pDialog = new ProgressDialog(DetailPersonalActivity.this);
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
                    progressDialog = new ProgressDialog(DetailPersonalActivity.this);
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

}