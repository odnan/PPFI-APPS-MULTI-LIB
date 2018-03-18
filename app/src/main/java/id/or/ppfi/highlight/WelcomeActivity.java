package id.or.ppfi.highlight;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import id.or.ppfi.R;
import id.or.ppfi.config.SQLiteHandler;
import id.or.ppfi.config.ServerRequest;
import id.or.ppfi.config.SessionManager;
import id.or.ppfi.dashboard.ListCederaActivity;
import id.or.ppfi.dashboard.ListWellnessActivity;
import id.or.ppfi.dashboard.WebviewActivity;
import id.or.ppfi.entities.M_GroupPrima;
import id.or.ppfi.main.AdvancedActivity;
import id.or.ppfi.sqlite.group.DBManagerGroup;
import id.or.ppfi.sqlite.group.DatabaseHelperGroup;
import id.or.ppfi.user.LoginActivity;


public class WelcomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private PrefManager prefManager;

    private SessionManager session;
    Toolbar toolbar;

    private Spinner spinnerGroupWellness;
    private ArrayList<M_GroupPrima> groupList;
    int lablesSpinner = 0;
    String GroupCodeTemp = "",GroupNameTemp = "";

    private String Username,GroupName;
    SessionManager sessionCode,sessionWeekly,
            sessionWellnessGrey,sessionWellnessRed,sessionWellnessOrange,sessionWellnessYellow,sessionWellnessGreen,
            sessionWellnessGreenArrow,sessionWellnessLastUpdate,sessionWellnessCidera;
    private String urlGeneralLog = "";
    int id_menu = 0;
    private String android_device_id;
    private String android_device_name;
    private String version_release;

    SessionManager sessionGroupName;
    ImageView ic_arow_up,ic_arow_down;
    RelativeLayout relative_wellness_show_cabor;

    TextView text_wp_percentage_grey,text_wp_percentage_red,text_wp_percentage_yellow,
            text_wp_percentage_orange,text_wp_percentage_green,text_wp_percentage_green_arrow,
            text3,text_last_update;

    TextView text_tl_weekly,progress_circle_text_total_atlet,text_tl_incomplete;
    TextView text_rcv_today,progress_circle_rcv,text_rcv_incomplete;
    TextView progress_circle_pmc_teknik,progress_circle_pmc_fisik,text_pmc_total_atlet,text_atlet_teknik,text_atlet_fisik;

    TextView text_kpf_fisik,text_atlet_kpf_fisik,progress_circle_kpf_fisik,
            text_kpf_teknik,text_atlet_kpf_teknik,progress_circle_kpf_teknik,
            text_kpf_mental,text_atlet_kpf_mental,progress_circle_kpf_mental;
    ProgressBar progressBarKPF_Fisik,progressBarKPF_Teknik,progressBarKPF_Mental;

    String total_atlet = "";

    ServerRequest serverRequest;
    private ProgressDialog progressDialog;
    private LayoutInflater inflater;

    DBManagerGroup dbManager;
    private SimpleCursorAdapter adapter;

    private DatabaseHelperGroup dbHelper;

    private Context context;
    private SQLiteDatabase database;

    ProgressBar progressBarTL,progressBarCedera,progressBarRCV,progressBarPMC_Teknik,progressBarPMC_Fisik;


    SessionManager sessionQtyCidera,sessionTotalAtlet,sessionRoleType,sessionGroupUser,sessionGroupID;
    String QtyCidera,TotalAtlet,role_type,groupUser;
    TextView text_cedera;

    String wellness_grey,wellness_red,wellness_orange,wellness_yellow,wellness_green,wellness_green_arrow,
            wellness_last_update,wellness_cidera;

    String getUrlGeneralLog;

    String dateYear,dateMonth,dateWeekly;

    private SQLiteHandler db;
    private static int SPLASH_TIME_OUT = 5000;
    List<String> groupListLocal = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android_device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        android_device_name = Build.MANUFACTURER + " - " + Build.MODEL;
        android_device_name = android_device_name.replace(" ","CEK");
        version_release = Build.VERSION.RELEASE;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sessionWeekly = new SessionManager(getApplicationContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new getWeekly().execute();
            }
        }, 1000);

        sessionCode = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionCode.GetSessionKode();
        Username = user.get(SessionManager.KEY_USERNAME);

        sessionGroupName = new SessionManager(getApplicationContext());
        HashMap<String, String> group = sessionGroupName.GetSessionGroupName();
        GroupName = group.get(SessionManager.KEY_GROUP_NAME);

        sessionRoleType = new SessionManager(getApplicationContext());
        HashMap<String, String> roleType = sessionRoleType.GetSessionRoleType();
        role_type = roleType.get(SessionManager.KEY_ROLE_TYPE);

        sessionGroupUser = new SessionManager(getApplicationContext());
        HashMap<String, String> gu = sessionGroupUser.GetSessionGroupGRP();
        groupUser = gu.get(SessionManager.KEY_GROUP_USERGRP);

        session = new SessionManager(getApplicationContext());
        sessionGroupID = new SessionManager(getApplicationContext());

        DateFormat dfYear = new SimpleDateFormat("yyyy");
        dateYear = dfYear.format(new Date());

        DateFormat dfMonth = new SimpleDateFormat("MM");
        dateMonth = dfMonth.format(new Date());

        urlGeneralLog = "http://dlog.iamprima.com/index.php/JsonInsertLog/GeneralLog/"+Username+"/"+android_device_id+"/"+android_device_name+"/"+version_release+"/20";

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new InsertLog().execute();
            }
        }, 1000);

        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new InsertMonotony().execute();
            }
        }, 1000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new InsertWellnessPercentage().execute();
            }
        }, 1000);
        */


        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome_fragment);

        /*
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("IAM PRIMA");
        //getSupportActionBar().setSubtitle("Highlight");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b71c1c")));
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        //      getSupportActionBar().setDisplayShowTitleEnabled(true);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#D03033")));
        /*
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon_depan);
        */


        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide4
//                R.layout.activity_dashboard_reference
                //R.layout.welcome_slide4
        };


        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeActivity.this, AdvancedActivity.class);
                startActivity(i);
                WelcomeActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });

        componentWellness();

        spinnerGroupWellness = (Spinner) findViewById(R.id.spinner_group_wellness);
        text3 = (TextView) findViewById(R.id.text_3);
        spinnerGroupWellness.setOnItemSelectedListener(this);

        dbManager = new DBManagerGroup(this);
        dbManager.open();
        String[] spinnerLists = dbManager.getAllSpinnerContent();

        if (spinnerLists.length > 0){
            List<String> lables = new ArrayList<String>();
            if(spinnerLists.length != 1){
                for (int i = 0; i < spinnerLists.length; i++) {
                    if(i != 0){
                        lables.add(spinnerLists[i].substring(5));
                        groupListLocal.add(spinnerLists[i].substring(0, 5));
                    }
                    else
                    {
                        lables.add(spinnerLists[0].substring(3));
                        groupListLocal.add(spinnerLists[0].substring(0, 3));
                    }
                }
            }else{
                lables.add(spinnerLists[0].substring(5));
                groupListLocal.add(spinnerLists[0].substring(0, 5));
            }

            CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter
                    (WelcomeActivity.this, (ArrayList<String>) lables);
            spinnerGroupWellness.setAdapter(customSpinnerAdapter);

        }else{
            //dbManager.deleteAll();
            serverRequest = new ServerRequest();
            groupList = new ArrayList<M_GroupPrima>();
            new GenerateGroupSpinner().execute();

        }

/*
        Toast.makeText(
                getApplicationContext(),
                " spinnerLists "+spinnerLists.length,
                Toast.LENGTH_LONG).show();
                */


    }

    //CREATE OPTION MENU BAR

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_highlight, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String[] spinnerLists = dbManager.getAllSpinnerContent();
        switch (item.getItemId()) {
            case android.R.id.home:
                break;
            case R.id.menu_refresh:

                generateCalculatingWellness();

                if(GroupCodeTemp.equals("ALL")){
                    if(spinnerLists.length != 1){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new InsertWellnessPercentage().execute();
                            }
                        }, 1000);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataWelnessWP().execute();
                            }
                        }, 1000);
                        Toast.makeText(WelcomeActivity.this, "Generate Wellness All Group...", Toast.LENGTH_SHORT).show();
                    }else{
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataWelnessWPByGroup().execute();
                            }
                        }, 1000);
                        Toast.makeText(WelcomeActivity.this, "Generate Wellness...", Toast.LENGTH_SHORT).show();
                    }
                }else if(GroupCodeTemp.contains("All Cabor")){
                    if(spinnerLists.length != 1){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new InsertWellnessPercentage().execute();
                            }
                        }, 1000);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataWelnessWP().execute();
                            }
                        }, 1000);
                        Toast.makeText(WelcomeActivity.this, "Generate Wellness All Group...", Toast.LENGTH_SHORT).show();
                    }else{
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataWelnessWPByGroup().execute();
                            }
                        }, 1000);
                        Toast.makeText(WelcomeActivity.this, "Generate Wellness...", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new getDataWelnessWPByGroup().execute();
                        }
                    }, 1000);
                    Toast.makeText(WelcomeActivity.this, "Generate Wellness...", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.menu_dashbard:
                finish();
                Intent i = new Intent(WelcomeActivity.this, AdvancedActivity.class);
                startActivity(i);
                WelcomeActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.menu_generate_wellness:
                if(spinnerLists.length != 1){
                    componentWellness();
                    if(GroupCodeTemp.equals("ALL")){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new InsertWellnessPercentage().execute();
                            }
                        }, 1000);
                    }else  if(GroupCodeTemp.contains("All Cabor")){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new InsertWellnessPercentage().execute();
                            }
                        }, 1000);
                    }else{
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataWelnessWPByGroup().execute();
                            }
                        }, 1000);
                    }
                }else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new getDataWelnessWPByGroup().execute();
                        }
                    }, 1000);
                }
                break;
            case R.id.menu_generate_module:

                generateCalculatingModules();
                if(spinnerLists.length != 1){

                    if(GroupCodeTemp.equals("ALL")){
                        generateNoData();
                             /*
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new InsertMonotony().execute();
                        }
                    }, 1000);
                    */
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataTrainingLoad().execute();
                            }
                        }, 1000);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataRecovery().execute();
                            }
                        }, 1000);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataPMC().execute();
                            }
                        }, 1000);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataPMCFisik().execute();
                            }
                        }, 1000);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataPerformance().execute();
                            }
                        }, 1000);

                    }else{
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataTrainingLoadPerGroup().execute();
                            }
                        }, 1000);


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataRecoveryPerGroup().execute();
                            }
                        }, 1000);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataPMCGroup().execute();
                            }
                        }, 1000);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataPMCFisikGroup().execute();
                            }
                        }, 1000);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataPerformanceGroup().execute();
                            }
                        }, 1000);
                    }

                }else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new getDataWelnessWPByGroup().execute();
                        }
                    }, 1000);
                }
                break;
            case R.id.menu_logout:
                progressDialog = new ProgressDialog(WelcomeActivity.this);
                progressDialog.setTitle("Sign Out...");
                progressDialog.setMessage("Please wait...");
                progressDialog.setIndeterminate(false);
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                db = new SQLiteHandler(getApplicationContext());
                                db.deleteUsers();
                                dbManager.deleteAll();
                                dbManager.close();
                                session.logoutUser();
                                sessionGroupID.ClearSessionGroupID();
                                sessionGroupName.ClearSessionGroupName();
                                urlGeneralLog = "http://dlog.iamprima.com/index.php/JsonInsertLog/GeneralLog/"+Username+"/"+android_device_id+"/"+android_device_name+"/"+version_release+"/7";
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        new InsertLog().execute();
                                    }
                                }, 1000);
                                Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                                WelcomeActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        }, SPLASH_TIME_OUT);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //CREATE OPTION MENU BAR


    private void loadSpinnerData() {
        DatabaseHelperGroup db = new DatabaseHelperGroup(getApplicationContext());
        List<String> labels = db.getAllLabels();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerGroupWellness.setAdapter(dataAdapter);
    }

    private void populateSpinnerGroup() {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < groupList.size(); i++) {
            lables.add(groupList.get(i).getGroupName());

            dbManager.insert(groupList.get(i).getGroupCode(), groupList.get(i).getGroupName(), groupList.get(i).getQtyAtlet());
            Toast.makeText(WelcomeActivity.this, "Data Tersimpan..."+groupList.get(i).getGroupName(), Toast.LENGTH_SHORT).show();

        }

        // ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, lables);
        // spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, lables);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerGroupWellness.setAdapter(spinnerAdapter);

        if(lables != null)
            lablesSpinner = 1;
        else
            lablesSpinner = 0;

    }

    private void initCustomSpinner() {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < groupList.size(); i++) {
            lables.add(groupList.get(i).getGroupName() + "(" + groupList.get(i).getQtyAtlet() + ")" );
            dbManager.insert(groupList.get(i).getGroupCode(), groupList.get(i).getGroupName(), groupList.get(i).getQtyAtlet());
        }
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(WelcomeActivity.this, (ArrayList<String>) lables);
        spinnerGroupWellness.setAdapter(customSpinnerAdapter);
    }

    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<String> asr;

        public CustomSpinnerAdapter(Context context,ArrayList<String> asr) {
            this.asr=asr;
            activity = context;
        }

        public int getCount()
        {
            return asr.size();
        }

        public Object getItem(int i)
        {
            return asr.get(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }

        //Untuk yang tampil di dropdown setelah di expand
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(WelcomeActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(15);
            txt.setGravity(Gravity.CENTER_VERTICAL);

            String[] spinnerLists = dbManager.getAllSpinnerContent();
            if (GroupCodeTemp.equals("ALL")){
                    txt.setText(asr.get(position));
            }else{
                try{
                        txt.setText(asr.get(position));
                }catch (Exception e){
                        txt.setText(asr.get(position));
                }

            }

            txt.setTextColor(Color.parseColor("#000000"));
            txt.setBackgroundResource(R.drawable.custom_spinner_border_standard);
            return  txt;
        }

        //Untuk yang tampil di dropdown sebelum di expand
        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(WelcomeActivity.this);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(12, 12, 12, 12);
            txt.setTextSize(15);
            txt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_ag_circle_48, 0, R.drawable.ic_arrow_down_red , 0);
            String[] spinnerLists = dbManager.getAllSpinnerContent();
            if (GroupCodeTemp.equals("ALL")){
                    txt.setText(asr.get(i));
            }else{
                try{
                        txt.setText(asr.get(i));
                }catch (Exception e){
                        txt.setText(asr.get(i));
                }


            }
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        GroupCodeTemp = parent.getItemAtPosition(position).toString();
        // GroupNameTemp =  groupList.get(position).getGroupName();
        //groupListLocal

       // Toast.makeText(WelcomeActivity.this, "GroupCodeTemp "+GroupCodeTemp, Toast.LENGTH_SHORT).show();
        if(!GroupCodeTemp.equals("ALL")) {
            //GroupCodeTemp = parent.getItemAtPosition(position).toString();
            String[] spinnerLists = dbManager.getAllSpinnerContent();
            if (spinnerLists.length == 0) {
                dbManager.deleteAll();
                groupList = new ArrayList<M_GroupPrima>();
                new GenerateGroupSpinner().execute();
            } else {
                List<String> lables = new ArrayList<String>();
                for (int i = 0; i < spinnerLists.length; i++) {
                    if (i != 0) {
                        lables.add(spinnerLists[i].substring(5));
                        groupListLocal.add(spinnerLists[i].substring(0, 5));
                    } else {
                        lables.add(spinnerLists[0].substring(3));
                        groupListLocal.add(spinnerLists[0].substring(0, 3));
                    }
                }
            }

            /*

        } else if (GroupCodeTemp.contains("All Cabor")) {
            GroupCodeTemp = "ALL";
        } else if (GroupCodeTemp.equals("")) {
            GroupCodeTemp =  groupListLocal.get(position);
        }

                */
        }else{
            String[] spinnerLists = dbManager.getAllSpinnerContent();
            List<String> lables = new ArrayList<String>();
            for (int i = 0; i < spinnerLists.length; i++) {
                if (i != 0) {
                    lables.add(spinnerLists[i].substring(5));
                    groupListLocal.add(spinnerLists[i].substring(0, 5));
                } else {
                    lables.add(spinnerLists[0].substring(3));
                    groupListLocal.add(spinnerLists[0].substring(0, 3));
                }
            }
            GroupCodeTemp =  parent.getItemAtPosition(position).toString();

        }


        //DEKLARASI TEXTVIEW TRAINING LOAD
        text_tl_weekly = (TextView) findViewById(R.id.text_tl_weekly);
        progress_circle_text_total_atlet = (TextView) findViewById(R.id.progress_circle_text_total_atlet);
        text_tl_incomplete = (TextView) findViewById(R.id.text_tl_incomplete);
        progressBarTL = (ProgressBar) findViewById(R.id.progressBarTL);

        progressBarTL.setProgress(0);
        progress_circle_text_total_atlet.setText("Get Data...");
        progress_circle_text_total_atlet.setTextColor(Color.parseColor("#FAFAFA"));

        //DEKLARASI TEXTVIEW RECOVERY
        text_rcv_today = (TextView) findViewById(R.id.text_rcv_today);
        progress_circle_rcv = (TextView) findViewById(R.id.progress_circle_rcv);
        text_rcv_incomplete = (TextView) findViewById(R.id.text_rcv_incomplete);
        progressBarRCV = (ProgressBar) findViewById(R.id.progressBarRCV);

        progressBarRCV.setProgress(0);
        progress_circle_rcv.setText("Get Data...");
        progress_circle_rcv.setTextColor(Color.parseColor("#FAFAFA"));

        //DEKLARASI TEXVIEW CEDERA
        text_cedera = (TextView) findViewById(R.id.progress_circle_text4);
        progressBarCedera = (ProgressBar) findViewById(R.id.progressBarCedera);
        progressBarCedera.setVisibility(View.VISIBLE);

        progressBarCedera.setProgress(0);
        text_cedera.setText("Get Data...");
        text_cedera.setTextColor(Color.parseColor("#FAFAFA"));

        //DEKLARASI TEXTVIEW WELLNESS
        text_wp_percentage_grey = (TextView) findViewById(R.id.text_percentage_grey);
        text_wp_percentage_red = (TextView) findViewById(R.id.text_percentage_red);
        text_wp_percentage_yellow = (TextView) findViewById(R.id.text_percentage_yellow);
        text_wp_percentage_orange = (TextView) findViewById(R.id.text_percentage_orange);
        text_wp_percentage_green = (TextView) findViewById(R.id.text_percentage_green);
        text_wp_percentage_green_arrow = (TextView) findViewById(R.id.text_percentage_green_arrow);

        text_wp_percentage_grey.setText("Calculating...");
        text_wp_percentage_grey.setTextColor(Color.parseColor("#424242"));

        text_wp_percentage_red.setText("Calculating...");
        text_wp_percentage_red.setTextColor(Color.parseColor("#424242"));

        text_wp_percentage_yellow.setText("Calculating...");
        text_wp_percentage_yellow.setTextColor(Color.parseColor("#424242"));

        text_wp_percentage_orange.setText("Calculating...");
        text_wp_percentage_orange.setTextColor(Color.parseColor("#424242"));

        text_wp_percentage_green.setText("Calculating...");
        text_wp_percentage_green.setTextColor(Color.parseColor("#424242"));

        text_wp_percentage_green_arrow.setText("Calculating...");
        text_wp_percentage_green_arrow.setTextColor(Color.parseColor("#424242"));

        progress_circle_pmc_teknik = (TextView) findViewById(R.id.progress_circle_pmc_teknik);
        progressBarPMC_Teknik = (ProgressBar) findViewById(R.id.progressBarPMC_Teknik);

        progressBarPMC_Teknik.setProgress(0);
        progress_circle_pmc_teknik.setText("Get...");
        progress_circle_pmc_teknik.setTextColor(Color.parseColor("#ecf0f1"));

        progress_circle_pmc_fisik = (TextView) findViewById(R.id.progress_circle_pmc_fisik);
        progressBarPMC_Fisik = (ProgressBar) findViewById(R.id.progressBarPMC_Fisik);

        progressBarPMC_Fisik.setProgress(0);
        progress_circle_pmc_fisik.setText("Get...");
        progress_circle_pmc_fisik.setTextColor(Color.parseColor("#ecf0f1"));

        text_pmc_total_atlet = (TextView) findViewById(R.id.text_pmc_total_atlet);
        text_pmc_total_atlet.setText("-");
        text_pmc_total_atlet.setTextColor(Color.parseColor("#424242"));

        text_atlet_teknik = (TextView) findViewById(R.id.text_atlet_teknik);
        text_atlet_fisik = (TextView) findViewById(R.id.text_atlet_fisik);

        text_atlet_teknik.setText("-");
        text_atlet_fisik.setText("-");

        progress_circle_kpf_fisik = (TextView) findViewById(R.id.progress_circle_kpf_fisik);
        progress_circle_kpf_teknik = (TextView) findViewById(R.id.progress_circle_kpf_teknik);
        progress_circle_kpf_mental = (TextView) findViewById(R.id.progress_circle_kpf_mental);

        progressBarKPF_Fisik = (ProgressBar) findViewById(R.id.progressBarKPF_Fisik);
        progressBarKPF_Teknik = (ProgressBar) findViewById(R.id.progressBarKPF_Teknik);
        progressBarKPF_Mental = (ProgressBar) findViewById(R.id.progressBarKPF_Mental);

        progressBarKPF_Fisik.setProgress(0);
        progressBarKPF_Teknik.setProgress(0);
        progressBarKPF_Mental.setProgress(0);

        progress_circle_kpf_fisik.setText("Get...");
        progress_circle_kpf_teknik.setText("Get...");
        progress_circle_kpf_mental.setText("Get...");

        progress_circle_kpf_fisik.setTextColor(Color.parseColor("#ecf0f1"));
        progress_circle_kpf_teknik.setTextColor(Color.parseColor("#ecf0f1"));
        progress_circle_kpf_mental.setTextColor(Color.parseColor("#ecf0f1"));

        text_atlet_kpf_fisik = (TextView) findViewById(R.id.text_atlet_kpf_fisik);
        text_atlet_kpf_fisik.setText("-");
        text_atlet_kpf_teknik = (TextView) findViewById(R.id.text_atlet_kpf_teknik);
        text_atlet_kpf_teknik.setText("-");
        text_atlet_kpf_mental = (TextView) findViewById(R.id.text_atlet_kpf_mental);
        text_atlet_kpf_mental.setText("-");

        if(GroupCodeTemp.equals("ALL")){
         // Toast.makeText(WelcomeActivity.this, "MASUK ALL ", Toast.LENGTH_SHORT).show();
            if(wellness_grey == null && wellness_red == null && wellness_orange == null &&
                    wellness_yellow == null &&  wellness_green == null && wellness_green_arrow == null){

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new getDataWelnessWP().execute();
                    }
                }, 1000);

            }else{
                generateWellnessLocal();
            }
            generateNoData();
            /*
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new getDataTrainingLoad().execute();
                }
            }, 1000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new getDataRecovery().execute();
                }
            }, 1000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new getDataPMC().execute();
                }
            }, 1000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new getDataPMCFisik().execute();
                }
            }, 1000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new getDataPerformance().execute();
                }
            }, 1000);

            */
        } else if(GroupCodeTemp.contains("All Cabor")){
         //   Toast.makeText(WelcomeActivity.this, "All Cabor "+GroupCodeTemp, Toast.LENGTH_SHORT).show();

            if(wellness_grey == null && wellness_red == null && wellness_orange == null &&
                    wellness_yellow == null &&  wellness_green == null && wellness_green_arrow == null){

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new InsertWellnessPercentage().execute();
                    }
                }, 1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new getDataWelnessWP().execute();
                    }
                }, 1000);

            }else{
                generateWellnessLocal();
            }
            generateNoData();

            if(GroupCodeTemp.equals(""))
              GroupCodeTemp =  groupListLocal.get(position);
            else
                GroupCodeTemp = "ALL";

        } else {

          //  GroupCodeTemp = parent.getItemAtPosition(position).toString();
            GroupCodeTemp =  groupListLocal.get(position) ;
          //  Toast.makeText(WelcomeActivity.this, "MASUK ADA GROUP "+GroupCodeTemp, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new getDataWelnessWPByGroup().execute();
                }
            }, 1000);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new getDataTrainingLoadPerGroup().execute();
                }
            }, 1000);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new getDataRecoveryPerGroup().execute();
                }
            }, 1000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new getDataPMCGroup().execute();
                }
            }, 1000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new getDataPMCFisikGroup().execute();
                }
            }, 1000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new getDataPerformanceGroup().execute();
                }
            }, 1000);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    ///
    ///
    ///   SPINNER ....
    ///
    ///

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        super.onBackPressed();
    }

    //DARI WELCOME SILDE 3

    public void loadCedera(View v) {
        Intent i = new Intent(WelcomeActivity.this, ListCederaActivity.class);
        i.putExtra("wellnessRate", "0");
        if(GroupCodeTemp.equals("ALL"))
            i.putExtra("allCabor", "1");
        else
            i.putExtra("allCabor", "0");

        if(text_cedera.getText().toString().equals("No Data") || text_cedera.getText().toString().equals("Calculating..."))
            i.putExtra("noData", "1");
        else
            i.putExtra("noData", "0");

        i.putExtra("highlight", "1");
        i.putExtra("group_name", GroupNameTemp);
        i.putExtra("groupCode", GroupCodeTemp);

        startActivity(i);
        WelcomeActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public void loadTrainingLoad(View v) {
        String moduleURL;

        if(GroupCodeTemp.equals("ALL"))
            Toast.makeText(WelcomeActivity.this, "Mohon Pilih Cabor", Toast.LENGTH_SHORT).show();
        else{

            Intent i = new Intent(WelcomeActivity.this, WebviewActivity.class);
            if (role_type.equals("CHC")) {
                moduleURL = "http://monotony.iamprima.com/index.php/login_validation/index/id/"+Username;
            }else{
                if (groupUser.equals("GSC"))
                {
                    moduleURL = "http://monotony.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+GroupCodeTemp;
                }else if ( groupUser.equals("SATLAK") || groupUser.equals("PRIMA"))
                {
                    moduleURL = "http://monotony.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+GroupCodeTemp;
                }else{
                    moduleURL = "http://monotony.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+GroupCodeTemp;

                }
            }


            i.putExtra("highlight", "1");
            i.putExtra("username", "UsernameMember");
            i.putExtra("name", "NameMember");
            i.putExtra("gambar", "GambarMember");
            i.putExtra("group_name", GroupNameTemp);
            //untuk kebutuhan pelatih teknik dan atlet
            i.putExtra("nama_modul", "Training Load");
            i.putExtra("url_modul", moduleURL);
            startActivity(i);
            WelcomeActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }


    }

    public void loadPMC(View v) {
        String moduleURL;

        if(GroupCodeTemp.equals("ALL"))
            Toast.makeText(WelcomeActivity.this, "Mohon Pilih Cabor", Toast.LENGTH_SHORT).show();
        else{

            Intent i = new Intent(WelcomeActivity.this, WebviewActivity.class);
            if (role_type.equals("CHC")) {
                moduleURL = "http://pmc.iamprima.com/index.php/login_validation/index/id/"+Username;
            }else{
                if (groupUser.equals("GSC"))
                {
                    moduleURL = "http://pmc.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+GroupCodeTemp;
                }else if ( groupUser.equals("SATLAK") || groupUser.equals("PRIMA"))
                {
                    moduleURL = "http://pmc.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+GroupCodeTemp;
                }else{
                    moduleURL = "http://pmc.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+GroupCodeTemp;

                }
            }


            i.putExtra("highlight", "1");
            i.putExtra("username", "UsernameMember");
            i.putExtra("name", "NameMember");
            i.putExtra("gambar", "GambarMember");
            i.putExtra("group_name", GroupNameTemp);
            //untuk kebutuhan pelatih teknik dan atlet
            i.putExtra("nama_modul", "PMC");
            i.putExtra("url_modul", moduleURL);
            startActivity(i);
            WelcomeActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    public void loadRecovery(View v) {
        String moduleURL;

        if(GroupCodeTemp.equals("ALL"))
            Toast.makeText(WelcomeActivity.this, "Mohon Pilih Cabor", Toast.LENGTH_SHORT).show();
        else{

            Intent i = new Intent(WelcomeActivity.this, WebviewActivity.class);
            if (role_type.equals("CHC")) {
                moduleURL = "http://recovery.iamprima.com/index.php/login_validation/index/id/"+Username;
            }else{
                if (groupUser.equals("GSC"))
                {
                    moduleURL = "http://recovery.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+GroupCodeTemp;
                }else if ( groupUser.equals("SATLAK") || groupUser.equals("PRIMA"))
                {
                    moduleURL = "http://recovery.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+GroupCodeTemp;
                }else{
                    moduleURL = "http://recovery.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+GroupCodeTemp;

                }
            }


            i.putExtra("highlight", "1");
            i.putExtra("username", "UsernameMember");
            i.putExtra("name", "NameMember");
            i.putExtra("gambar", "GambarMember");
            i.putExtra("group_name", GroupNameTemp);
            //untuk kebutuhan pelatih teknik dan atlet
            i.putExtra("nama_modul", "Recovery");
            i.putExtra("url_modul", moduleURL);
            startActivity(i);
            WelcomeActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }


    }

    public void loadPerformance(View v) {
        String moduleURL;

        if(GroupCodeTemp.equals("ALL"))
            Toast.makeText(WelcomeActivity.this, "Mohon Pilih Cabor", Toast.LENGTH_SHORT).show();
        else{

            Intent i = new Intent(WelcomeActivity.this, WebviewActivity.class);
            if (role_type.equals("CHC")) {
                moduleURL = "http://profiling.iamprima.com/index.php/login_validation/index/id/"+Username;
            }else{
                if (groupUser.equals("GSC"))
                {
                    moduleURL = "http://profiling.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+GroupCodeTemp;
                }else if ( groupUser.equals("SATLAK") || groupUser.equals("PRIMA"))
                {
                    moduleURL = "http://profiling.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+GroupCodeTemp;
                }else{
                    moduleURL = "http://profiling.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+GroupCodeTemp;

                }
            }


            i.putExtra("highlight", "1");
            i.putExtra("username", "UsernameMember");
            i.putExtra("name", "NameMember");
            i.putExtra("gambar", "GambarMember");
            i.putExtra("group_name", GroupNameTemp);
            //untuk kebutuhan pelatih teknik dan atlet
            i.putExtra("nama_modul", "Performance Profiling");
            i.putExtra("url_modul", moduleURL);
            startActivity(i);
            WelcomeActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    //DARI WELCOME SILDE 3
    public void loadGreyWP(View v) {
        Intent i = new Intent(WelcomeActivity.this, ListWellnessActivity.class);

        i.putExtra("wellnessRate", "0");

        if(GroupCodeTemp.equals("ALL"))
            i.putExtra("allCabor", "1");
        else if(GroupCodeTemp.contains("All Cabor"))
            i.putExtra("allCabor", "1");
        else
            i.putExtra("allCabor", "0");

        i.putExtra("groupCode", GroupCodeTemp);
        startActivity(i);
        WelcomeActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public void loadRedWP(View v) {
        Intent i = new Intent(WelcomeActivity.this, ListWellnessActivity.class);
        i.putExtra("wellnessRate", "1");

        if(GroupCodeTemp.equals("ALL"))
            i.putExtra("allCabor", "1");
        else if(GroupCodeTemp.contains("All Cabor"))
            i.putExtra("allCabor", "1");
        else
            i.putExtra("allCabor", "0");



        i.putExtra("groupCode", GroupCodeTemp);
        startActivity(i);
        WelcomeActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public void loadYellowWP(View v) {
        Intent i = new Intent(WelcomeActivity.this, ListWellnessActivity.class);
        i.putExtra("wellnessRate", "3");

        if(GroupCodeTemp.equals("ALL"))
            i.putExtra("allCabor", "1");
        else if(GroupCodeTemp.contains("All Cabor"))
            i.putExtra("allCabor", "1");
        else
            i.putExtra("allCabor", "0");

        i.putExtra("groupCode", GroupCodeTemp);
        startActivity(i);
        WelcomeActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public void loadOrangeWP(View v) {
        Intent i = new Intent(WelcomeActivity.this, ListWellnessActivity.class);
        i.putExtra("wellnessRate", "2");

        if(GroupCodeTemp.equals("ALL"))
            i.putExtra("allCabor", "1");
        else if(GroupCodeTemp.contains("All Cabor"))
            i.putExtra("allCabor", "1");
        else
            i.putExtra("allCabor", "0");

        i.putExtra("groupCode", GroupCodeTemp);
        startActivity(i);
        WelcomeActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public void loadGreenWP(View v) {
        Intent i = new Intent(WelcomeActivity.this, ListWellnessActivity.class);
        i.putExtra("wellnessRate", "4");

        if(GroupCodeTemp.equals("ALL"))
            i.putExtra("allCabor", "1");
        else if(GroupCodeTemp.contains("All Cabor"))
            i.putExtra("allCabor", "1");
        else
            i.putExtra("allCabor", "0");

        i.putExtra("groupCode", GroupCodeTemp);
        startActivity(i);
        WelcomeActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public void loadGreenArrowWP(View v) {
        Intent i = new Intent(WelcomeActivity.this, ListWellnessActivity.class);
        i.putExtra("wellnessRate", "5");

        if(GroupCodeTemp.equals("ALL"))
            i.putExtra("allCabor", "1");
        else if(GroupCodeTemp.contains("All Cabor"))
            i.putExtra("allCabor", "1");
        else
            i.putExtra("allCabor", "0");

        i.putExtra("groupCode", GroupCodeTemp);
        startActivity(i);
        WelcomeActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);

                /*

               if (position > 2){
                   if(GroupCodeTemp.equals("All Cabor")){
                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               new getDataWelnessWP().execute();
                           }
                       }, 1000);

                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               new getDataTrainingLoad().execute();
                           }
                       }, 1000);

                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               new getDataRecovery().execute();
                           }
                       }, 1000);
                   } else {
                       if(GroupCodeTemp != ""){
                           new Handler().postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   new getDataWelnessWPByGroup().execute();
                               }
                           }, 1000);
                       }

                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               new getDataTrainingLoadPerGroup().execute();
                           }
                       }, 1000);
                   }
               }

                */


            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.GONE);

                if (position == 2){
                    if(GroupCodeTemp.equals("ALL")){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataWelnessWP().execute();
                            }
                        }, 1000);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataTrainingLoad().execute();
                            }
                        }, 1000);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataRecovery().execute();
                            }
                        }, 1000);
                    } else {
                        if(GroupCodeTemp != ""){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    new getDataWelnessWPByGroup().execute();
                                }
                            }, 1000);
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataTrainingLoadPerGroup().execute();
                            }
                        }, 1000);
                    }
                }
            }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }


    public class generateGROUP extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonGroupPrima/SpinnerAllGroup/"+Username);
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
            progressDialog = new ProgressDialog(WelcomeActivity.this);
            progressDialog.setMessage("Generating Group...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();

            try {

                JSONObject jsonObj = new JSONObject(s);
                JSONArray jsonArray = jsonObj.getJSONArray("data");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject catObj = (JSONObject) jsonArray.get(i);
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

                //belom dipanggil
                initCustomSpinner();
                progressDialog.dismiss();

            } catch (JSONException e) {
            }
        }
    }


    public class getDataWelnessWP extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonQtyWellness/WellnessPercentage/"+Username);
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
                    sessionWellnessRed.ClearSessionWellnessGrey();
                    sessionWellnessOrange.ClearSessionWellnessOrange();
                    sessionWellnessYellow.ClearSessionWellnessYellow();
                    sessionWellnessGreen.ClearSessionWellnessGreen();
                    sessionWellnessGreenArrow.ClearSessionWellnessGreenArrow();
                    sessionWellnessRed.ClearSessionWellnessGrey();
                    sessionWellnessLastUpdate.ClearSessionWellnessLastUpdate();

                    text_wp_percentage_grey = (TextView) findViewById(R.id.text_percentage_grey);
                    text_wp_percentage_red = (TextView) findViewById(R.id.text_percentage_red);
                    text_wp_percentage_yellow = (TextView) findViewById(R.id.text_percentage_yellow);
                    text_wp_percentage_orange = (TextView) findViewById(R.id.text_percentage_orange);
                    text_wp_percentage_green = (TextView) findViewById(R.id.text_percentage_green);
                    text_wp_percentage_green_arrow = (TextView) findViewById(R.id.text_percentage_green_arrow);
                    text_last_update = (TextView) findViewById(R.id.last_update);


                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("data");

                    JSONObject objTA = jsonArray.getJSONObject(0);
                    text3.setText("Total "+objTA.getString("total_atlet_sum")+" Atlet");
                    total_atlet = objTA.getString("total_atlet_sum");
                    QtyCidera = objTA.getString("total_cidera");


                    //set wellness grey

                    JSONObject obj = jsonArray.getJSONObject(0);
                    sessionWellnessGrey.CreateSessionWellnessGrey(obj.getString("total_atlet") +" Atlet\n"+
                            obj.getString("total_percentage")+" %");
                    text_wp_percentage_grey.setText(obj.getString("total_atlet") +" Atlet\n"+
                            obj.getString("total_percentage")+" %");


                    //set wellness red

                    JSONObject obj1 = jsonArray.getJSONObject(1);
                    sessionWellnessRed.CreateSessionWellnessRed(obj1.getString("total_atlet") +" Atlet\n"+
                            obj1.getString("total_percentage")+" %");
                    text_wp_percentage_red.setText(obj1.getString("total_atlet") +" Atlet\n"+
                            obj1.getString("total_percentage")+" %");

                    JSONObject obj3 = jsonArray.getJSONObject(2);
                    sessionWellnessOrange.CreateSessionWellnessOrange(obj3.getString("total_atlet") +" Atlet\n"+
                            obj3.getString("total_percentage")+" %");
                    text_wp_percentage_orange.setText(obj3.getString("total_atlet") +" Atlet\n"+
                            obj3.getString("total_percentage")+" %");

                    JSONObject obj2 = jsonArray.getJSONObject(3);
                    sessionWellnessYellow.CreateSessionWellnessYellow(obj2.getString("total_atlet") +" Atlet\n"+
                            obj2.getString("total_percentage")+" %");
                    text_wp_percentage_yellow.setText(obj2.getString("total_atlet") +" Atlet\n"+
                            obj2.getString("total_percentage")+" %");


                    JSONObject obj4 = jsonArray.getJSONObject(4);
                    sessionWellnessGreen.CreateSessionWellnessGreen(obj4.getString("total_atlet") +" Atlet\n"+
                            obj4.getString("total_percentage")+" %");
                    text_wp_percentage_green.setText(obj4.getString("total_atlet") +" Atlet\n"+
                            obj4.getString("total_percentage")+" %");

                    JSONObject obj5 = jsonArray.getJSONObject(5);
                    sessionWellnessGreenArrow.CreateSessionWellnessGreenArrow(obj5.getString("total_atlet") +" Atlet\n"+
                            obj5.getString("total_percentage")+" %");
                    text_wp_percentage_green_arrow .setText(obj5.getString("total_atlet") +" Atlet\n"+
                            obj5.getString("total_percentage")+" %");

                    sessionWellnessLastUpdate.CreateSessionWellnessLastUpdate("Last Update From Server\n"+obj.getString("last_update"));
                    text_last_update.setText("Last Update From Server\n"+obj.getString("last_update"));

                    /*
                    Toast.makeText(getApplicationContext(),
                            "Last Update " + obj.getString("last_update"), Toast.LENGTH_LONG)
                            .show();

                    */

                    DecimalFormat df = new DecimalFormat("#.00");
                    text_cedera = (TextView) findViewById(R.id.progress_circle_text4);
                    progressBarCedera = (ProgressBar) findViewById(R.id.progressBarCedera);
                    progressBarCedera.setVisibility(View.VISIBLE);

                    if(QtyCidera.equals("0")){
                        text_cedera.setText("No Cedera");
                        text_cedera.setTextColor(Color.parseColor("#33691E"));
                        progressBarCedera.setProgress(0);
                    }else{
                        Double X = 0.00;
                        X = (Double.parseDouble(QtyCidera) / Double.parseDouble(total_atlet)) * 100;
                        text_cedera.setText(
                                QtyCidera + " Atlet \n" +
                                        String.valueOf(df.format(X)) +
                                        " %"
                        );
                        text_cedera.setTextColor(Color.parseColor("#DD2C00"));
                        progressBarCedera.setProgress(Integer.parseInt(String.valueOf(df.format(X)).replace(".","")));

                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error Something! Get Data wellness General...", Toast.LENGTH_LONG)
                            .show();
                }
           }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Bad Connection!!!", Toast.LENGTH_LONG)
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
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonQtyWellness/WellnessPercentageByGroup/"+GroupCodeTemp);
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

                    text_wp_percentage_grey = (TextView) findViewById(R.id.text_percentage_grey);
                    text_wp_percentage_red = (TextView) findViewById(R.id.text_percentage_red);
                    text_wp_percentage_yellow = (TextView) findViewById(R.id.text_percentage_yellow);
                    text_wp_percentage_orange = (TextView) findViewById(R.id.text_percentage_orange);
                    text_wp_percentage_green = (TextView) findViewById(R.id.text_percentage_green);
                    text_wp_percentage_green_arrow = (TextView) findViewById(R.id.text_percentage_green_arrow);
                    text_last_update = (TextView) findViewById(R.id.last_update);


                    JSONObject objTA = jsonArray.getJSONObject(0);
                    text3.setText("Total "+objTA.getString("total_atlet_sum")+" Atlet");
                    total_atlet = objTA.getString("total_atlet_sum");
                    QtyCidera = objTA.getString("total_cidera");

                    JSONObject obj = jsonArray.getJSONObject(0);
                    text_wp_percentage_grey.setText(obj.getString("total_atlet") +" Atlet\n"+
                            obj.getString("total_percentage")+" %");

                    JSONObject obj1 = jsonArray.getJSONObject(1);
                    text_wp_percentage_red.setText(obj1.getString("total_atlet") +" Atlet\n"+
                            obj1.getString("total_percentage")+" %");

                    JSONObject obj3 = jsonArray.getJSONObject(2);
                    text_wp_percentage_orange.setText(obj3.getString("total_atlet") +" Atlet\n"+
                            obj3.getString("total_percentage")+" %");

                    JSONObject obj2 = jsonArray.getJSONObject(3);
                    text_wp_percentage_yellow.setText(obj2.getString("total_atlet") +" Atlet\n"+
                            obj2.getString("total_percentage")+" %");


                    JSONObject obj4 = jsonArray.getJSONObject(4);
                    text_wp_percentage_green.setText(obj4.getString("total_atlet") +" Atlet\n"+
                            obj4.getString("total_percentage")+" %");

                    JSONObject obj5 = jsonArray.getJSONObject(5);
                    text_wp_percentage_green_arrow .setText(obj5.getString("total_atlet") +" Atlet\n"+
                            obj5.getString("total_percentage")+" %");

                    text_last_update.setText("Last Update\n"+obj.getString("last_update"));

                    DecimalFormat df = new DecimalFormat("#.00");
                    text_cedera = (TextView) findViewById(R.id.progress_circle_text4);

                    progressBarCedera = (ProgressBar) findViewById(R.id.progressBarCedera);
                    progressBarCedera.setVisibility(View.VISIBLE);

                    if(QtyCidera.equals("0")){
                        text_cedera.setText("No Cedera");
                        text_cedera.setTextColor(Color.parseColor("#33691E"));
                        progressBarCedera.setProgress(0);
                    }else{
                        Double X = 0.00;
                        X = (Double.parseDouble(QtyCidera) / Double.parseDouble(total_atlet)) * 100;
                        text_cedera.setText(
                                QtyCidera + " Atlet \n" +
                                        String.valueOf(df.format(X)) +
                                        " %"
                        );
                        text_cedera.setTextColor(Color.parseColor("#DD2C00"));
                        progressBarCedera.setProgress(Integer.parseInt(String.valueOf(df.format(X)).replace(".","")));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "No Wellness Data!", Toast.LENGTH_LONG)
                            .show();

                    text_wp_percentage_grey.setText("No Data");

                    text_wp_percentage_red.setText("No Data");

                    text_wp_percentage_yellow.setText("No Data");

                    text_wp_percentage_orange.setText("No Data");

                    text_wp_percentage_green.setText("No Data");

                    text_wp_percentage_green_arrow.setText("No Data");

                    text3.setText(" 0 Atlet");

                    text_cedera.setText("No Data");

                    text_wp_percentage_grey.setTextColor(Color.parseColor("#2c3e50"));
                    text_wp_percentage_red.setTextColor(Color.parseColor("#2c3e50"));
                    text_wp_percentage_yellow.setTextColor(Color.parseColor("#2c3e50"));
                    text_wp_percentage_orange.setTextColor(Color.parseColor("#2c3e50"));
                    text_wp_percentage_green.setTextColor(Color.parseColor("#2c3e50"));
                    text_wp_percentage_green_arrow.setTextColor(Color.parseColor("#2c3e50"));
                    text_cedera.setTextColor(Color.parseColor("#ecf0f1"));

                    progressBarCedera.setProgress(0);


                }
            }catch (Exception e){
                /*
                Toast.makeText(getApplicationContext(),
                        "Bad Connection!2", Toast.LENGTH_LONG)
                        .show();
                                    */
                if(GroupCodeTemp.equals(""))
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new getDataWelnessWP().execute();
                    }
                }, 1000);
                else
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new getDataWelnessWPByGroup().execute();
                    }
                }, 1000);
            }


//            Log.i("json", s);
        }
    }



    private class GenerateGroupSpinner extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(WelcomeActivity.this);
            progressDialog.setMessage("Loading Group...");
            progressDialog.setCancelable(false);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
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
            // populateSpinnerGroup();
            initCustomSpinner();
        }

    }

    //GENERATE WELLNESS SUM
    public class InsertWellnessPercentage extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonQtyWellness/WellnessToday/"+Username);
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
           // progressDialog = new ProgressDialog(WelcomeActivity.this);
           // progressDialog.setMessage("Generating Data Wellness...");
          //  progressDialog.setCancelable(false);
         //   progressDialog.show();
            try {
                try {
                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("data");
                    JSONObject obj = jsonArray.getJSONObject(0);

                    if(obj.getString("generate_data_wellness").equals("SUCCESS")){
                        Toast.makeText(getApplication(), "Done",Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new getDataWelnessWP().execute();
                            }
                        }, 1000);
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error Generate", Toast.LENGTH_LONG)
                            .show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Error Connection!!!", Toast.LENGTH_LONG)
                        .show();
            }

        }
    }
    //GENERATE WELLNESS SUM


    ////GENERATE TRAINING LOAD PERCENTAGE

    public class getDataTrainingLoad extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonHighlight/TrainingLoad/"+Username);
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

                    text_tl_weekly = (TextView) findViewById(R.id.text_tl_weekly);
                    progress_circle_text_total_atlet = (TextView) findViewById(R.id.progress_circle_text_total_atlet);
                    text_tl_incomplete = (TextView) findViewById(R.id.text_tl_incomplete);
                    progressBarTL = (ProgressBar) findViewById(R.id.progressBarTL);

                    progress_circle_text_total_atlet.setText("Calculating...");

                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("data");

                    JSONObject objTA = jsonArray.getJSONObject(0);
                    text_tl_weekly.setText("Weekly "+objTA.getString("weekly"));

                    JSONObject obj = jsonArray.getJSONObject(0);
                    try{
                        if(obj.getString("tdkIsi").equals("0")){
                            progress_circle_text_total_atlet.setText("100%\nCompleted");
                            progress_circle_text_total_atlet.setTextColor(Color.parseColor("#33691E"));
                        }else{
                            progress_circle_text_total_atlet.setTextColor(Color.parseColor("#DD2C00"));
                            progress_circle_text_total_atlet.setText(obj.getString("tdkIsi") +" Atlet\n\n"+
                                    obj.getString("tdkIsi_percentage")+" %"+"\nNot Complete");
                        }
                    }catch (Exception e){
                        progress_circle_text_total_atlet.setTextColor(Color.parseColor("#DD2C00"));
                        progress_circle_text_total_atlet.setText("0" +" Atlet\n\n"+
                                "0"+" %"+"\nNot Complete");
                    }


                    progressBarTL.setVisibility(View.VISIBLE);
                    try{
                        progressBarTL.setProgress(Integer.parseInt(obj.getString("tdkIsi_percentage").replace(".","")));
                    }catch (Exception e){
                        progressBarTL.setProgress(0);
                    }



                } catch (JSONException e) {
                    progress_circle_text_total_atlet.setText("No Data");
                    progress_circle_text_total_atlet.setTextColor(Color.parseColor("#ecf0f1"));
                    progressBarTL.setProgress(0);
                    Toast.makeText(getApplicationContext(),
                            "No Data Training Load", Toast.LENGTH_LONG)
                            .show();
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                        "Bad Connection!3", Toast.LENGTH_LONG)
                        .show();
            }


//            Log.i("json", s);
        }
    }


    public class getDataTrainingLoadPerGroup extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonHighlight/TrainingLoadPerGroup/"+Username+"/"+GroupCodeTemp);
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


                    text_tl_weekly = (TextView) findViewById(R.id.text_tl_weekly);
                    progress_circle_text_total_atlet = (TextView) findViewById(R.id.progress_circle_text_total_atlet);
                    text_tl_incomplete = (TextView) findViewById(R.id.text_tl_incomplete);
                    progressBarTL = (ProgressBar) findViewById(R.id.progressBarTL);

                    progressBarTL.setProgress(0);
                    progress_circle_text_total_atlet.setText("Calculating...");

                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("data");

                    JSONObject objTA = jsonArray.getJSONObject(0);
                    text_tl_weekly.setText("Weekly "+objTA.getString("weekly"));

                    JSONObject obj = jsonArray.getJSONObject(0);

                    progressBarTL.setVisibility(View.VISIBLE);
                    progressBarTL.setProgress(Integer.parseInt(obj.getString("tdkIsi_percentage").replace(".","")));

                    if(obj.getString("tdkIsi").equals("0")){
                        progress_circle_text_total_atlet.setText("100%\nCompleted");
                        progress_circle_text_total_atlet.setTextColor(Color.parseColor("#33691E"));
                    }else{
                        progress_circle_text_total_atlet.setTextColor(Color.parseColor("#DD2C00"));
                        progress_circle_text_total_atlet.setText(obj.getString("tdkIsi") +" Atlet\n\n"+
                                obj.getString("tdkIsi_percentage")+" %"+"\nNot Complete");
                    }



                } catch (JSONException e) {
                    progress_circle_text_total_atlet.setText("No Data");
                    progress_circle_text_total_atlet.setTextColor(Color.parseColor("#ecf0f1"));
                    progressBarTL.setProgress(0);
                    Toast.makeText(getApplicationContext(),
                            "No Data Training Load", Toast.LENGTH_LONG)
                            .show();
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                        "Bad Connection!4", Toast.LENGTH_LONG)
                        .show();
            }


//            Log.i("json", s);
        }
    }

    ////GENERATE TRAINING LOAD PERCENTAGE

////RECOVERY

    public class getDataRecovery extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonHighlight/Recovery/"+Username);
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

                    text_rcv_today = (TextView) findViewById(R.id.text_rcv_today);
                    progress_circle_rcv = (TextView) findViewById(R.id.progress_circle_rcv);
                    text_rcv_incomplete = (TextView) findViewById(R.id.text_rcv_incomplete);
                    progressBarRCV = (ProgressBar) findViewById(R.id.progressBarRCV);

                    progress_circle_rcv.setText("Calculating...");


                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("data");

                    JSONObject objTA = jsonArray.getJSONObject(0);
                    //text_tl_weekly.setText("Weekly "+objTA.getString("weekly"));

                    JSONObject obj = jsonArray.getJSONObject(0);
                    if(obj.getString("total_tidak_isi").equals("0")){
                        progress_circle_rcv.setText("No Data");
                        progress_circle_rcv.setTextColor(Color.parseColor("#ecf0f1"));
                    }else{
                        progress_circle_rcv.setTextColor(Color.parseColor("#DD2C00"));
                        progress_circle_rcv.setText(obj.getString("total_tidak_isi") +" Atlet\n\n"+
                                obj.getString("tidak_isi_percentage")+" %\n" +
                                "No Recovery Point");
                    }

                    progressBarRCV.setVisibility(View.VISIBLE);
                    progressBarRCV.setProgress(Integer.parseInt(obj.getString("tidak_isi_percentage").replace(".","")));


                } catch (JSONException e) {
                    progress_circle_rcv.setText("No Data");
                    progress_circle_rcv.setTextColor(Color.parseColor("#ecf0f1"));
                    progressBarTL.setProgress(0);
                    Toast.makeText(getApplicationContext(),
                            "No Data Recovery", Toast.LENGTH_LONG)
                            .show();
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                        "Bad Connection!5", Toast.LENGTH_LONG)
                        .show();
            }


//            Log.i("json", s);
        }
    }

    public class getDataRecoveryPerGroup extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonHighlight/RecoveryPerGroup/"+Username+"/"+GroupCodeTemp);
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

                    text_rcv_today = (TextView) findViewById(R.id.text_rcv_today);
                    progress_circle_rcv = (TextView) findViewById(R.id.progress_circle_rcv);
                    text_rcv_incomplete = (TextView) findViewById(R.id.text_rcv_incomplete);
                    progressBarRCV = (ProgressBar) findViewById(R.id.progressBarRCV);

                    progress_circle_rcv.setText("Calculating...");


                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("data");

                    JSONObject objTA = jsonArray.getJSONObject(0);
                    //text_tl_weekly.setText("Weekly "+objTA.getString("weekly"));

                    JSONObject obj = jsonArray.getJSONObject(0);
                    if(obj.getString("total_tidak_isi").equals("0")){
                        progress_circle_rcv.setText("No Data");
                        progress_circle_rcv.setTextColor(Color.parseColor("#ecf0f1"));
                    }else{
                        progress_circle_rcv.setTextColor(Color.parseColor("#DD2C00"));
                        progress_circle_rcv.setText(obj.getString("total_tidak_isi") +" Atlet\n\n"+
                                obj.getString("tidak_isi_percentage")+" %\n" +
                                "No Recovery Point");
                    }

                    progressBarRCV.setVisibility(View.VISIBLE);
                    progressBarRCV.setProgress(Integer.parseInt(obj.getString("tidak_isi_percentage").replace(".","")));


                } catch (JSONException e) {
                    progress_circle_rcv.setText("No Data");
                    progress_circle_rcv.setTextColor(Color.parseColor("#ecf0f1"));
                    progressBarTL.setProgress(0);
                    Toast.makeText(getApplicationContext(),
                            "No Data Recovery", Toast.LENGTH_LONG)
                            .show();
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                        "Bad Connection!6", Toast.LENGTH_LONG)
                        .show();
            }


//            Log.i("json", s);
        }
    }

    /////RECOVERY


    ////PMC

    public class getDataPMC extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonHighlight/PMC/"+Username+"/T");
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

                    progress_circle_pmc_teknik = (TextView) findViewById(R.id.progress_circle_pmc_teknik);
                    progressBarPMC_Teknik = (ProgressBar) findViewById(R.id.progressBarPMC_Teknik);
                    text_pmc_total_atlet = (TextView) findViewById(R.id.text_pmc_total_atlet);

                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("data");
                    JSONObject objTA = jsonArray.getJSONObject(0);

                    JSONObject obj = jsonArray.getJSONObject(0);
                    if(obj.getString("total_tidak_isi").equals("0")){
                        progress_circle_pmc_teknik.setText("No Data");
                        progress_circle_pmc_teknik.setTextColor(Color.parseColor("#ecf0f1"));
                        text_atlet_teknik.setText("-");
                    }else{
                        progress_circle_pmc_teknik.setTextColor(Color.parseColor("#DD2C00"));
                        progress_circle_pmc_teknik.setText(obj.getString("tidak_isi_percentage")+" %");
                        text_pmc_total_atlet.setText(obj.getString("total_atlet")+" Atlet");
                        text_atlet_teknik.setText(obj.getString("total_tidak_isi")+" Atlet");
                    }

                    progressBarPMC_Teknik.setVisibility(View.VISIBLE);
                    progressBarPMC_Teknik.setProgress(Integer.parseInt(obj.getString("tidak_isi_percentage").replace(".","")));


                } catch (JSONException e) {
                    progress_circle_pmc_teknik.setText("No Data");
                    progress_circle_pmc_teknik.setTextColor(Color.parseColor("#ecf0f1"));
                    progressBarPMC_Teknik.setProgress(0);
                    Toast.makeText(getApplicationContext(),
                            "No Data PMC Teknik", Toast.LENGTH_LONG)
                            .show();
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                        "Bad Connection!7", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }


    public class getDataPMCFisik extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonHighlight/PMC/"+Username+"/F");
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

                    progress_circle_pmc_fisik = (TextView) findViewById(R.id.progress_circle_pmc_fisik);
                    progressBarPMC_Fisik = (ProgressBar) findViewById(R.id.progressBarPMC_Fisik);

                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("data");
                    JSONObject objTA = jsonArray.getJSONObject(0);

                    JSONObject obj = jsonArray.getJSONObject(0);
                    if(obj.getString("total_tidak_isi").equals("0")){
                        progress_circle_pmc_fisik.setText("No Data");
                        progress_circle_pmc_fisik.setTextColor(Color.parseColor("#ecf0f1"));
                        text_atlet_fisik.setText("-");
                    }else{
                        progress_circle_pmc_fisik.setTextColor(Color.parseColor("#DD2C00"));
                        progress_circle_pmc_fisik.setText(obj.getString("tidak_isi_percentage")+" %");
                        text_atlet_fisik.setText(obj.getString("total_tidak_isi")+" Atlet");
                    }

                    progressBarPMC_Fisik.setVisibility(View.VISIBLE);
                    progressBarPMC_Fisik.setProgress(Integer.parseInt(obj.getString("tidak_isi_percentage").replace(".","")));


                } catch (JSONException e) {
                    progress_circle_pmc_fisik.setText("No Data");
                    progress_circle_pmc_fisik.setTextColor(Color.parseColor("#ecf0f1"));
                    progressBarPMC_Fisik.setProgress(0);
                    Toast.makeText(getApplicationContext(),
                            "No Data PMC Fisik", Toast.LENGTH_LONG)
                            .show();
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                        "Bad Connection!8", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }


    public class getDataPMCGroup extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonHighlight/PMCPerGroup/"+Username+"/"+GroupCodeTemp+"/T");
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

                    progress_circle_pmc_teknik = (TextView) findViewById(R.id.progress_circle_pmc_teknik);
                    progressBarPMC_Teknik = (ProgressBar) findViewById(R.id.progressBarPMC_Teknik);
                    text_pmc_total_atlet = (TextView) findViewById(R.id.text_pmc_total_atlet);

                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("data");
                    JSONObject objTA = jsonArray.getJSONObject(0);

                    JSONObject obj = jsonArray.getJSONObject(0);
                    if(obj.getString("total_tidak_isi").equals("0")){
                        progress_circle_pmc_teknik.setText("No Data");
                        progress_circle_pmc_teknik.setTextColor(Color.parseColor("#ecf0f1"));
                        text_atlet_teknik.setText("-");
                    }else{
                        progress_circle_pmc_teknik.setTextColor(Color.parseColor("#DD2C00"));
                        progress_circle_pmc_teknik.setText(obj.getString("tidak_isi_percentage")+" %");
                        text_pmc_total_atlet.setText(obj.getString("total_atlet")+" Atlet");
                        text_atlet_teknik.setText(obj.getString("total_tidak_isi")+" Atlet");
                    }

                    progressBarPMC_Teknik.setVisibility(View.VISIBLE);
                    progressBarPMC_Teknik.setProgress(Integer.parseInt(obj.getString("tidak_isi_percentage").replace(".","")));


                } catch (JSONException e) {
                    progress_circle_pmc_teknik.setText("No Data");
                    progress_circle_pmc_teknik.setTextColor(Color.parseColor("#ecf0f1"));
                    progressBarPMC_Teknik.setProgress(0);
                    Toast.makeText(getApplicationContext(),
                            "No Data PMC Teknik", Toast.LENGTH_LONG)
                            .show();
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                        "Bad Connection!9", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }


    public class getDataPMCFisikGroup extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonHighlight/PMCPerGroup/"+Username+"/"+GroupCodeTemp+"/F");
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

                    progress_circle_pmc_fisik = (TextView) findViewById(R.id.progress_circle_pmc_fisik);
                    progressBarPMC_Fisik = (ProgressBar) findViewById(R.id.progressBarPMC_Fisik);

                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("data");
                    JSONObject objTA = jsonArray.getJSONObject(0);

                    JSONObject obj = jsonArray.getJSONObject(0);
                    if(obj.getString("total_tidak_isi").equals("0")){
                        progress_circle_pmc_fisik.setText("No Data");
                        progress_circle_pmc_fisik.setTextColor(Color.parseColor("#ecf0f1"));
                        text_atlet_fisik.setText("-");
                    }else{
                        progress_circle_pmc_fisik.setTextColor(Color.parseColor("#DD2C00"));
                        progress_circle_pmc_fisik.setText(obj.getString("tidak_isi_percentage")+" %");
                        text_atlet_fisik.setText(obj.getString("total_tidak_isi")+" Atlet");
                    }

                    progressBarPMC_Fisik.setVisibility(View.VISIBLE);
                    progressBarPMC_Fisik.setProgress(Integer.parseInt(obj.getString("tidak_isi_percentage").replace(".","")));


                } catch (JSONException e) {
                    progress_circle_pmc_fisik.setText("No Data");
                    progress_circle_pmc_fisik.setTextColor(Color.parseColor("#ecf0f1"));
                    progressBarPMC_Fisik.setProgress(0);
                    Toast.makeText(getApplicationContext(),
                            "No Data PMC Fisik", Toast.LENGTH_LONG)
                            .show();
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                        "Bad Connection!10", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }




    /////PMC


    ////PERFORMANCE


    public class getDataPerformance extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonHighlight/Performance/"+Username);
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

                    progress_circle_kpf_fisik = (TextView) findViewById(R.id.progress_circle_kpf_fisik);
                    progressBarKPF_Fisik = (ProgressBar) findViewById(R.id.progressBarKPF_Fisik);
                    text_atlet_kpf_fisik = (TextView) findViewById(R.id.text_atlet_kpf_fisik);

                    progress_circle_kpf_teknik = (TextView) findViewById(R.id.progress_circle_kpf_teknik);
                    progressBarKPF_Teknik = (ProgressBar) findViewById(R.id.progressBarKPF_Teknik);
                    text_atlet_kpf_teknik = (TextView) findViewById(R.id.text_atlet_kpf_teknik);

                    progress_circle_kpf_mental = (TextView) findViewById(R.id.progress_circle_kpf_mental);
                    progressBarKPF_Mental = (ProgressBar) findViewById(R.id.progressBarKPF_Mental);
                    text_atlet_kpf_mental = (TextView) findViewById(R.id.text_atlet_kpf_mental);

                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("data");

                    JSONObject obj = jsonArray.getJSONObject(0);
                    if(obj.getString("total_tidak_isi").equals("0")){
                        progress_circle_kpf_fisik.setText("Completed");
                        progress_circle_kpf_fisik.setTextColor(Color.parseColor("#33691E"));
                        text_atlet_kpf_fisik.setText(obj.getString("total_tidak_isi")+" Atlet");
                    }else{
                        progress_circle_kpf_fisik.setTextColor(Color.parseColor("#DD2C00"));
                        progress_circle_kpf_fisik.setText(obj.getString("tidak_isi_percentage")+" %\n not complete");
                        text_atlet_kpf_fisik.setText(obj.getString("total_tidak_isi")+" Atlet");
                        progressBarKPF_Fisik.setVisibility(View.VISIBLE);
                        progressBarKPF_Fisik.setProgress(Integer.parseInt(obj.getString("tidak_isi_percentage").replace(".","")));
                    }


                    JSONObject objTeknik = jsonArray.getJSONObject(1);
                    if(objTeknik.getString("total_tidak_isi").equals("0")){
                        progress_circle_kpf_teknik.setText("Completed");
                        progress_circle_kpf_teknik.setTextColor(Color.parseColor("#33691E"));
                        text_atlet_kpf_teknik.setText(objTeknik.getString("total_tidak_isi")+" Atlet");

                    }else{
                        progress_circle_kpf_teknik.setTextColor(Color.parseColor("#DD2C00"));
                        progress_circle_kpf_teknik.setText(objTeknik.getString("tidak_isi_percentage")+" %\n not complete");
                        text_atlet_kpf_teknik.setText(objTeknik.getString("total_tidak_isi")+" Atlet");
                        progressBarKPF_Teknik.setVisibility(View.VISIBLE);
                        progressBarKPF_Teknik.setProgress(Integer.parseInt(objTeknik.getString("tidak_isi_percentage").replace(".","")));
                    }

                    JSONObject objMental = jsonArray.getJSONObject(2);
                    if(objMental.getString("total_tidak_isi").equals("0")){
                        progress_circle_kpf_mental.setText("Completed");
                        progress_circle_kpf_mental.setTextColor(Color.parseColor("#33691E"));
                        text_atlet_kpf_mental.setText(objMental.getString("total_tidak_isi")+" Atlet");
                    }else{
                        progress_circle_kpf_mental.setTextColor(Color.parseColor("#DD2C00"));
                        progress_circle_kpf_mental.setText(objMental.getString("tidak_isi_percentage")+" %\n not complete");
                        text_atlet_kpf_mental.setText(objMental.getString("total_tidak_isi")+" Atlet");
                        progressBarKPF_Mental.setVisibility(View.VISIBLE);
                        progressBarKPF_Mental.setProgress(Integer.parseInt(objMental.getString("tidak_isi_percentage").replace(".","")));
                    }


                } catch (JSONException e) {
                    progress_circle_kpf_fisik.setText("No Data");
                    progress_circle_kpf_fisik.setTextColor(Color.parseColor("#ecf0f1"));
                    progressBarKPF_Fisik.setProgress(0);

                    progress_circle_kpf_teknik.setText("No Data");
                    progress_circle_kpf_teknik.setTextColor(Color.parseColor("#ecf0f1"));
                    progressBarKPF_Teknik.setProgress(0);

                    progress_circle_kpf_mental.setText("No Data");
                    progress_circle_kpf_mental.setTextColor(Color.parseColor("#ecf0f1"));
                    progressBarKPF_Mental.setProgress(0);
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                        "Bad Connection!11", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }


    public class getDataPerformanceGroup extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonHighlight/PerformancePerGroup/"+Username+"/"+GroupCodeTemp);
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

                    progress_circle_kpf_fisik = (TextView) findViewById(R.id.progress_circle_kpf_fisik);
                    progressBarKPF_Fisik = (ProgressBar) findViewById(R.id.progressBarKPF_Fisik);
                    text_atlet_kpf_fisik = (TextView) findViewById(R.id.text_atlet_kpf_fisik);

                    progress_circle_kpf_teknik = (TextView) findViewById(R.id.progress_circle_kpf_teknik);
                    progressBarKPF_Teknik = (ProgressBar) findViewById(R.id.progressBarKPF_Teknik);
                    text_atlet_kpf_teknik = (TextView) findViewById(R.id.text_atlet_kpf_teknik);

                    progress_circle_kpf_mental = (TextView) findViewById(R.id.progress_circle_kpf_mental);
                    progressBarKPF_Mental = (ProgressBar) findViewById(R.id.progressBarKPF_Mental);
                    text_atlet_kpf_mental = (TextView) findViewById(R.id.text_atlet_kpf_mental);

                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("data");

                    JSONObject obj = jsonArray.getJSONObject(0);
                    if(obj.getString("total_atlet").equals("0")){
                        progress_circle_kpf_fisik.setText("No Data");
                        progress_circle_kpf_fisik.setTextColor(Color.parseColor("#ecf0f1"));
                        progressBarKPF_Fisik.setProgress(0);

                    }else{
                        if(obj.getString("total_tidak_isi").equals("0")){
                            progress_circle_kpf_fisik.setText("Completed");
                            progress_circle_kpf_fisik.setTextColor(Color.parseColor("#33691E"));
                            text_atlet_kpf_fisik.setText(obj.getString("total_tidak_isi")+" Atlet");
                        }else{
                            progress_circle_kpf_fisik.setTextColor(Color.parseColor("#DD2C00"));
                            progress_circle_kpf_fisik.setText(obj.getString("tidak_isi_percentage")+" %\nnot complete");
                            text_atlet_kpf_fisik.setText(obj.getString("total_tidak_isi")+" Atlet");
                            progressBarKPF_Fisik.setVisibility(View.VISIBLE);
                            progressBarKPF_Fisik.setProgress(Integer.parseInt(obj.getString("tidak_isi_percentage").replace(".","")));
                        }
                    }



                    JSONObject objTeknik = jsonArray.getJSONObject(1);
                    if(objTeknik.getString("total_atlet").equals("0")){

                        progress_circle_kpf_teknik.setText("No Data");
                        progress_circle_kpf_teknik.setTextColor(Color.parseColor("#ecf0f1"));
                        progressBarKPF_Teknik.setProgress(0);

                    }else{
                        if(objTeknik.getString("total_tidak_isi").equals("0")){
                            progress_circle_kpf_teknik.setText("Completed");
                            progress_circle_kpf_teknik.setTextColor(Color.parseColor("#33691E"));
                            text_atlet_kpf_teknik.setText(objTeknik.getString("total_tidak_isi")+" Atlet");

                        }else{
                            progress_circle_kpf_teknik.setTextColor(Color.parseColor("#DD2C00"));
                            progress_circle_kpf_teknik.setText(objTeknik.getString("tidak_isi_percentage")+" %\nnot complete");
                            text_atlet_kpf_teknik.setText(objTeknik.getString("total_tidak_isi")+" Atlet");
                            progressBarKPF_Teknik.setVisibility(View.VISIBLE);
                            progressBarKPF_Teknik.setProgress(Integer.parseInt(objTeknik.getString("tidak_isi_percentage").replace(".","")));
                        }
                    }


                    JSONObject objMental = jsonArray.getJSONObject(2);
                    if(objMental.getString("total_atlet").equals("0")){
                        progress_circle_kpf_mental.setText("No Data");
                        progress_circle_kpf_mental.setTextColor(Color.parseColor("#ecf0f1"));
                        progressBarKPF_Mental.setProgress(0);
                    }else{
                        if(objMental.getString("total_tidak_isi").equals("0")){
                            progress_circle_kpf_mental.setText("Completed");
                            progress_circle_kpf_mental.setTextColor(Color.parseColor("#33691E"));
                            text_atlet_kpf_mental.setText(objMental.getString("total_tidak_isi")+" Atlet");
                        }else{
                            progress_circle_kpf_mental.setTextColor(Color.parseColor("#DD2C00"));
                            progress_circle_kpf_mental.setText(objMental.getString("tidak_isi_percentage")+" %\nnot complete");
                            text_atlet_kpf_mental.setText(objMental.getString("total_tidak_isi")+" Atlet");
                            progressBarKPF_Mental.setVisibility(View.VISIBLE);
                            progressBarKPF_Mental.setProgress(Integer.parseInt(objMental.getString("tidak_isi_percentage").replace(".","")));
                        }
                    }


                } catch (JSONException e) {
                    progress_circle_kpf_fisik.setText("No Data");
                    progress_circle_kpf_fisik.setTextColor(Color.parseColor("#ecf0f1"));
                    progressBarKPF_Fisik.setProgress(0);

                    progress_circle_kpf_teknik.setText("No Data");
                    progress_circle_kpf_teknik.setTextColor(Color.parseColor("#ecf0f1"));
                    progressBarKPF_Teknik.setProgress(0);

                    progress_circle_kpf_mental.setText("No Data");
                    progress_circle_kpf_mental.setTextColor(Color.parseColor("#ecf0f1"));
                    progressBarKPF_Mental.setProgress(0);
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                        "Bad Connection!12", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }




    /////PERFORMANCE


    //GENERATE GENERAL MONOTONY
    public class InsertMonotony extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> X = sessionWeekly.GetSessionWeekly();
            dateWeekly = X.get(SessionManager.KEY_WEEKLY);

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonGenerateMonotony/generateMonotonyAll/"+dateWeekly+"/"+dateYear);
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
    //GENERATE GENERAL MONOTONY


    //CEK WEEKLY NOW

    public class getWeekly extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonWeekly/GetWeekly");
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
            } finally {
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
                try {
                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("data");
                    JSONObject obj = jsonArray.getJSONObject(0);

                    sessionWeekly.CreateSessionWeekly(obj.getString("weekly"));
                    dateWeekly = obj.getString("weekly");

                    //Toast.makeText(getApplication(), "dateWeekly "+dateWeekly,Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error String Input!", Toast.LENGTH_LONG)
                            .show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Bad Connection!13", Toast.LENGTH_LONG)
                        .show();
            }


//            Log.i("json", s);
        }
    }

    //CEK WEEKLY NOW

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


    ///GENERATE WELLNESS LOCAL
    public void generateWellnessLocal(){

        text_wp_percentage_grey = (TextView) findViewById(R.id.text_percentage_grey);
        text_wp_percentage_red = (TextView) findViewById(R.id.text_percentage_red);
        text_wp_percentage_yellow = (TextView) findViewById(R.id.text_percentage_yellow);
        text_wp_percentage_orange = (TextView) findViewById(R.id.text_percentage_orange);
        text_wp_percentage_green = (TextView) findViewById(R.id.text_percentage_green);
        text_wp_percentage_green_arrow = (TextView) findViewById(R.id.text_percentage_green_arrow);
        text_last_update = (TextView) findViewById(R.id.last_update);

        //set wellness grey
        text_wp_percentage_grey.setText(wellness_grey);
        text_wp_percentage_red.setText(wellness_red);
        text_wp_percentage_yellow.setText(wellness_yellow);
        text_wp_percentage_orange.setText(wellness_orange);
        text_wp_percentage_green.setText(wellness_green);
        text_wp_percentage_green_arrow.setText(wellness_green_arrow);
        text_last_update.setText(wellness_last_update);


        DecimalFormat df = new DecimalFormat("#.00");
        text_cedera = (TextView) findViewById(R.id.progress_circle_text4);
        progressBarCedera = (ProgressBar) findViewById(R.id.progressBarCedera);
        progressBarCedera.setVisibility(View.VISIBLE);

        if(QtyCidera.equals("0")){
            text_cedera.setText("No Cedera");
            text_cedera.setTextColor(Color.parseColor("#33691E"));
            progressBarCedera.setProgress(0);
        }else{
            try {
                Double X = 0.00;
                X = (Double.parseDouble(QtyCidera) / Double.parseDouble(total_atlet)) * 100;
                text_cedera.setText(
                        QtyCidera + " Atlet \n" +
                                String.valueOf(df.format(X)) +
                                " %"
                );
                text_cedera.setTextColor(Color.parseColor("#DD2C00"));
                progressBarCedera.setProgress(Integer.parseInt(String.valueOf(df.format(X)).replace(".","")));
            }catch (Exception e){
                text_cedera.setText("No Data");
                text_cedera.setTextColor(Color.parseColor("#33691E"));
                progressBarCedera.setProgress(0);
            }

        }
    }

    public void generateCalculatingWellness(){

        text_wp_percentage_grey = (TextView) findViewById(R.id.text_percentage_grey);
        text_wp_percentage_red = (TextView) findViewById(R.id.text_percentage_red);
        text_wp_percentage_yellow = (TextView) findViewById(R.id.text_percentage_yellow);
        text_wp_percentage_orange = (TextView) findViewById(R.id.text_percentage_orange);
        text_wp_percentage_green = (TextView) findViewById(R.id.text_percentage_green);
        text_wp_percentage_green_arrow = (TextView) findViewById(R.id.text_percentage_green_arrow);
        text_last_update = (TextView) findViewById(R.id.last_update);

        //set wellness grey
        text_wp_percentage_grey.setText("Calculating...");
        text_wp_percentage_red.setText("Calculating...");
        text_wp_percentage_yellow.setText("Calculating...");
        text_wp_percentage_orange.setText("Calculating...");
        text_wp_percentage_green.setText("Calculating...");
        text_wp_percentage_green_arrow.setText("Calculating...");
        text_last_update.setText("Updating...");

        text_cedera.setText("Calculating...");
        text_cedera.setTextColor(Color.parseColor("#33691E"));
        progressBarCedera.setProgress(0);
    }


    public void generateNoData(){

        //DEKLARASI TEXTVIEW TRAINING LOAD
        text_tl_weekly = (TextView) findViewById(R.id.text_tl_weekly);
        progress_circle_text_total_atlet = (TextView) findViewById(R.id.progress_circle_text_total_atlet);
        text_tl_incomplete = (TextView) findViewById(R.id.text_tl_incomplete);
        progressBarTL = (ProgressBar) findViewById(R.id.progressBarTL);

        progressBarTL.setProgress(0);
        progress_circle_text_total_atlet.setText("...");
        progress_circle_text_total_atlet.setTextColor(Color.parseColor("#FAFAFA"));

        //DEKLARASI TEXTVIEW RECOVERY
        text_rcv_today = (TextView) findViewById(R.id.text_rcv_today);
        progress_circle_rcv = (TextView) findViewById(R.id.progress_circle_rcv);
        text_rcv_incomplete = (TextView) findViewById(R.id.text_rcv_incomplete);
        progressBarRCV = (ProgressBar) findViewById(R.id.progressBarRCV);

        progressBarRCV.setProgress(0);
        progress_circle_rcv.setText("...");
        progress_circle_rcv.setTextColor(Color.parseColor("#FAFAFA"));

        progress_circle_pmc_teknik = (TextView) findViewById(R.id.progress_circle_pmc_teknik);
        progressBarPMC_Teknik = (ProgressBar) findViewById(R.id.progressBarPMC_Teknik);

        progressBarPMC_Teknik.setProgress(0);
        progress_circle_pmc_teknik.setText("...");
        progress_circle_pmc_teknik.setTextColor(Color.parseColor("#ecf0f1"));

        progress_circle_pmc_fisik = (TextView) findViewById(R.id.progress_circle_pmc_fisik);
        progressBarPMC_Fisik = (ProgressBar) findViewById(R.id.progressBarPMC_Fisik);

        progressBarPMC_Fisik.setProgress(0);
        progress_circle_pmc_fisik.setText("...");
        progress_circle_pmc_fisik.setTextColor(Color.parseColor("#ecf0f1"));


        text_pmc_total_atlet = (TextView) findViewById(R.id.text_pmc_total_atlet);
        text_pmc_total_atlet.setText("-");
        text_pmc_total_atlet.setTextColor(Color.parseColor("#424242"));

        text_atlet_teknik = (TextView) findViewById(R.id.text_atlet_teknik);
        text_atlet_fisik = (TextView) findViewById(R.id.text_atlet_fisik);

        text_atlet_teknik.setText("-");
        text_atlet_fisik.setText("-");

        progress_circle_kpf_fisik = (TextView) findViewById(R.id.progress_circle_kpf_fisik);
        progress_circle_kpf_teknik = (TextView) findViewById(R.id.progress_circle_kpf_teknik);
        progress_circle_kpf_mental = (TextView) findViewById(R.id.progress_circle_kpf_mental);

        progressBarKPF_Fisik = (ProgressBar) findViewById(R.id.progressBarKPF_Fisik);
        progressBarKPF_Teknik = (ProgressBar) findViewById(R.id.progressBarKPF_Teknik);
        progressBarKPF_Mental = (ProgressBar) findViewById(R.id.progressBarKPF_Mental);

        progressBarKPF_Fisik.setProgress(0);
        progressBarKPF_Teknik.setProgress(0);
        progressBarKPF_Mental.setProgress(0);

        progress_circle_kpf_fisik.setText("...");
        progress_circle_kpf_teknik.setText("...");
        progress_circle_kpf_mental.setText("...");

        progress_circle_kpf_fisik.setTextColor(Color.parseColor("#ecf0f1"));
        progress_circle_kpf_teknik.setTextColor(Color.parseColor("#ecf0f1"));
        progress_circle_kpf_mental.setTextColor(Color.parseColor("#ecf0f1"));



        text_atlet_kpf_fisik = (TextView) findViewById(R.id.text_atlet_kpf_fisik);
        text_atlet_kpf_fisik.setText("-");
        text_atlet_kpf_teknik = (TextView) findViewById(R.id.text_atlet_kpf_teknik);
        text_atlet_kpf_teknik.setText("-");
        text_atlet_kpf_mental = (TextView) findViewById(R.id.text_atlet_kpf_mental);
        text_atlet_kpf_mental.setText("-");
    }

    public void generateCalculatingModules(){

        //DEKLARASI TEXTVIEW TRAINING LOAD
        text_tl_weekly = (TextView) findViewById(R.id.text_tl_weekly);
        progress_circle_text_total_atlet = (TextView) findViewById(R.id.progress_circle_text_total_atlet);
        text_tl_incomplete = (TextView) findViewById(R.id.text_tl_incomplete);
        progressBarTL = (ProgressBar) findViewById(R.id.progressBarTL);

        progressBarTL.setProgress(0);
        progress_circle_text_total_atlet.setText("Get...");
        progress_circle_text_total_atlet.setTextColor(Color.parseColor("#FAFAFA"));

        //DEKLARASI TEXTVIEW RECOVERY
        text_rcv_today = (TextView) findViewById(R.id.text_rcv_today);
        progress_circle_rcv = (TextView) findViewById(R.id.progress_circle_rcv);
        text_rcv_incomplete = (TextView) findViewById(R.id.text_rcv_incomplete);
        progressBarRCV = (ProgressBar) findViewById(R.id.progressBarRCV);

        progressBarRCV.setProgress(0);
        progress_circle_rcv.setText("Get...");
        progress_circle_rcv.setTextColor(Color.parseColor("#FAFAFA"));

        progress_circle_pmc_teknik = (TextView) findViewById(R.id.progress_circle_pmc_teknik);
        progressBarPMC_Teknik = (ProgressBar) findViewById(R.id.progressBarPMC_Teknik);

        progressBarPMC_Teknik.setProgress(0);
        progress_circle_pmc_teknik.setText("Get...");
        progress_circle_pmc_teknik.setTextColor(Color.parseColor("#ecf0f1"));

        progress_circle_pmc_fisik = (TextView) findViewById(R.id.progress_circle_pmc_fisik);
        progressBarPMC_Fisik = (ProgressBar) findViewById(R.id.progressBarPMC_Fisik);

        progressBarPMC_Fisik.setProgress(0);
        progress_circle_pmc_fisik.setText("Get...");
        progress_circle_pmc_fisik.setTextColor(Color.parseColor("#ecf0f1"));


        text_pmc_total_atlet = (TextView) findViewById(R.id.text_pmc_total_atlet);
        text_pmc_total_atlet.setText("-");
        text_pmc_total_atlet.setTextColor(Color.parseColor("#424242"));

        text_atlet_teknik = (TextView) findViewById(R.id.text_atlet_teknik);
        text_atlet_fisik = (TextView) findViewById(R.id.text_atlet_fisik);

        text_atlet_teknik.setText("-");
        text_atlet_fisik.setText("-");

        progress_circle_kpf_fisik = (TextView) findViewById(R.id.progress_circle_kpf_fisik);
        progress_circle_kpf_teknik = (TextView) findViewById(R.id.progress_circle_kpf_teknik);
        progress_circle_kpf_mental = (TextView) findViewById(R.id.progress_circle_kpf_mental);

        progressBarKPF_Fisik = (ProgressBar) findViewById(R.id.progressBarKPF_Fisik);
        progressBarKPF_Teknik = (ProgressBar) findViewById(R.id.progressBarKPF_Teknik);
        progressBarKPF_Mental = (ProgressBar) findViewById(R.id.progressBarKPF_Mental);

        progressBarKPF_Fisik.setProgress(0);
        progressBarKPF_Teknik.setProgress(0);
        progressBarKPF_Mental.setProgress(0);

        progress_circle_kpf_fisik.setText("Get...");
        progress_circle_kpf_teknik.setText("Get...");
        progress_circle_kpf_mental.setText("Get...");

        progress_circle_kpf_fisik.setTextColor(Color.parseColor("#ecf0f1"));
        progress_circle_kpf_teknik.setTextColor(Color.parseColor("#ecf0f1"));
        progress_circle_kpf_mental.setTextColor(Color.parseColor("#ecf0f1"));



        text_atlet_kpf_fisik = (TextView) findViewById(R.id.text_atlet_kpf_fisik);
        text_atlet_kpf_fisik.setText("-");
        text_atlet_kpf_teknik = (TextView) findViewById(R.id.text_atlet_kpf_teknik);
        text_atlet_kpf_teknik.setText("-");
        text_atlet_kpf_mental = (TextView) findViewById(R.id.text_atlet_kpf_mental);
        text_atlet_kpf_mental.setText("-");
    }



    public void componentWellness(){
        sessionWellnessGrey = new SessionManager(getApplicationContext());
        HashMap<String, String> wg = sessionWellnessGrey.GetSessionWellnessGrey();
        wellness_grey = wg.get(SessionManager.KEY_WELLNESS_GREY);

        sessionWellnessRed = new SessionManager(getApplicationContext());
        HashMap<String, String> wr = sessionWellnessRed.GetSessionWellnessRed();
        wellness_red = wr.get(SessionManager.KEY_WELLNESS_RED);

        sessionWellnessOrange = new SessionManager(getApplicationContext());
        HashMap<String, String> wo = sessionWellnessOrange.GetSessionWellnessOrange();
        wellness_orange = wo.get(SessionManager.KEY_WELLNESS_ORANGE);

        sessionWellnessYellow = new SessionManager(getApplicationContext());
        HashMap<String, String> wy = sessionWellnessYellow.GetSessionWellnessYellow();
        wellness_yellow = wy.get(SessionManager.KEY_WELLNESS_YELLOW);

        sessionWellnessGreen = new SessionManager(getApplicationContext());
        HashMap<String, String> wgg = sessionWellnessGreen.GetSessionWellnessGreen();
        wellness_green = wgg.get(SessionManager.KEY_WELLNESS_GREEN);

        sessionWellnessGreenArrow = new SessionManager(getApplicationContext());
        HashMap<String, String> wga = sessionWellnessGreenArrow.GetSessionWellnessGreenArrow();
        wellness_green_arrow = wga.get(SessionManager.KEY_WELLNESS_GREEN_ARROW);

        sessionWellnessLastUpdate = new SessionManager(getApplicationContext());
        HashMap<String, String> wlu = sessionWellnessLastUpdate.GetSessionWellnessLastUpdate();
        wellness_last_update = wlu.get(SessionManager.KEY_WELLNESS_LAST_UPDATE);

        sessionQtyCidera = new SessionManager(getApplicationContext());
        HashMap<String, String> qty = sessionQtyCidera.GetSessionQtyCidera();
        QtyCidera = qty.get(SessionManager.KEY_QTY_CIDERA);



    }


}
