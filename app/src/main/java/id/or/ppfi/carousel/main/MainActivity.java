package id.or.ppfi.carousel.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;


import java.util.ArrayList;

import id.or.ppfi.R;
import id.or.ppfi.carousel.menu.MemberActivity;
import id.or.ppfi.config.ServerRequest;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class MainActivity extends AppCompatActivity {
private ProgressDialog progressDialog;

    private String APK_PATH = "/sdcard/Download/";
    private String nameFile = "", versionName = "";
    private String nameInstall = "jdih-mobile.apk";
    private Intent intent;
    private String urlDownload = "";
    ServerRequest serverRequest;

    private String versionCurrent = "";
    private String versionUpdate = "";

    private FeatureCoverFlow coverFlow;
    private CoverFlowAdapter adapter;
    private ArrayList<Game> games;

    public static Integer APP_VERSION = 0;
    public static String APP_VERSION_TEXT = "1.0";

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }


        coverFlow = (FeatureCoverFlow) findViewById(R.id.coverflow);

        settingDummyData();
        adapter = new CoverFlowAdapter(this, games);
        coverFlow.setAdapter(adapter);
        coverFlow.setOnScrollPositionListener(onScrollListener());
        coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v,
                                    int pos, long id) {
              //  Toast.makeText(MainActivity.this, "position: " + pos, Toast.LENGTH_SHORT).show();
                if(pos == 0){
                } else
                if(pos==1){
                    if(isNetworkAvailable()){
                        Intent in = new Intent(MainActivity.this, MemberActivity.class);
                        startActivity(in);
                    }else{
                        Intent in = new Intent(MainActivity.this, MemberActivity.class);
                        startActivity(in);
                    }
                }else
                if(pos==2){
                    if(isNetworkAvailable()){
                        Intent in = new Intent(MainActivity.this, MemberActivity.class);
                        startActivity(in);
                    }else{
                        Intent in = new Intent(MainActivity.this, MemberActivity.class);
                        startActivity(in);
                    }
                }else
                if(pos==3){
                    if(isNetworkAvailable()){
                        Intent in = new Intent(MainActivity.this, MemberActivity.class);
                        startActivity(in);
                    }else{
                        Intent in = new Intent(MainActivity.this, MemberActivity.class);
                        startActivity(in);
                    }
                }else  if(pos==4){
                    if(isNetworkAvailable()){
                        Intent in = new Intent(MainActivity.this, MemberActivity.class);
                        startActivity(in);
                    }else{
                        Intent in = new Intent(MainActivity.this, MemberActivity.class);
                        startActivity(in);
                    }
                }else if(pos == 8){

                } else if(pos==5){

                }else if(pos==6){

                }




            }
        });
    }

    private FeatureCoverFlow.OnScrollPositionListener onScrollListener() {
        return new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                Log.v("MainActiivty", "position: " + position);
            }

            @Override
            public void onScrolling() {
                Log.i("MainActivity", "scrolling");
            }
        };
    }

    private void settingDummyData() {
        games = new ArrayList<>();
        games.add(new Game(R.mipmap.bg_membership_2, "STRENGTH\n&\nCONDITIONING"));
        games.add(new Game(R.mipmap.bg_membership_2, "MEMBERSHIP"));
        //first open
        games.add(new Game(R.mipmap.bg_membership_2, "LICENCE"));
        games.add(new Game(R.mipmap.bg_membership_2, "NEWS"));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}