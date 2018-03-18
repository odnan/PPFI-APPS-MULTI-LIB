package id.or.ppfi.main;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.RecyclerViewCacheUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.or.ppfi.R;
import id.or.ppfi.alarmservice.AlarmReceiverAthlete;
import id.or.ppfi.authorization.ListAuthActivity;
import id.or.ppfi.config.AndroidMultiPartEntity;
import id.or.ppfi.config.AppConfig;
import id.or.ppfi.config.AppController;
import id.or.ppfi.config.CircularImageView;
import id.or.ppfi.config.CircularNetworkImageView;
import id.or.ppfi.config.Config;
import id.or.ppfi.config.FilePath;
import id.or.ppfi.config.SQLiteHandler;
import id.or.ppfi.config.ServerRequest;
import id.or.ppfi.config.SessionManager;
import id.or.ppfi.dashboard.DetailPersonalActivity;
import id.or.ppfi.dashboard.MealPlanActivity;
import id.or.ppfi.dashboard.WebviewActivity;
import id.or.ppfi.drawerItems.CustomPrimaryDrawerItem;
import id.or.ppfi.drawerItems.CustomUrlPrimaryDrawerItem;
import id.or.ppfi.drawerItems.OverflowMenuDrawerItem;
import id.or.ppfi.entities.M_AllCabor;
import id.or.ppfi.entities.M_CederaToday;
import id.or.ppfi.entities.M_GroupAccessPrima;
import id.or.ppfi.entities.M_GroupPrima;
import id.or.ppfi.entities.M_Member;
import id.or.ppfi.entities.M_Periodisasi;
import id.or.ppfi.entities.M_WellnessPercentage;
import id.or.ppfi.group.MemberListActivity;
import id.or.ppfi.group.ViewProfileActivity;
import id.or.ppfi.helper.UpdateSetting;
import id.or.ppfi.listadapter.ListAdapterAllCabor;
import id.or.ppfi.listadapter.ListAdapterCederaToday;
import id.or.ppfi.listadapter.ListAdapterMember;
import id.or.ppfi.listadapter.ListAdapterPeriodisasi;
import id.or.ppfi.listadapter.ListAdapterWellnessPercentage;
import id.or.ppfi.periodization.DetailPeriodPlanActivity;
import id.or.ppfi.sqlite.group.DBManagerGroup;
import id.or.ppfi.user.LoginActivity;
import id.or.ppfi.user.profile.ChangePasswordActivity;
import id.or.ppfi.user.profile.EditProfileActivity;
import id.or.ppfi.user.profile.UploadActivity;

public class AdvancedActivityAthlete extends AppCompatActivity implements OnQueryTextListener, AdapterView.OnItemSelectedListener {
    private static final int PROFILE_SETTING = 1;
    private static int SPLASH_TIME_OUT = 5000;

    volatile boolean running;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private boolean opened = false;

    private IProfile profile;
    TextView text1;
    ListView listViewMember,listViewAllCabor,listViewCideraToday,listViewWP,listViewPeriodisasi;

    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    private SQLiteHandler db;

    private List<M_Member> listDataMember;
    private M_Member selectedList;
    ListAdapterMember adapter;

    private List<M_AllCabor> listDataAllCabor;
    private M_AllCabor selectedListCabor;
    ListAdapterAllCabor adapterCabor;

    private List<M_CederaToday> listDataCideraToday;
    private M_CederaToday selectedListCideraToday;
    ListAdapterCederaToday adapterCideraToday;

    private List<M_WellnessPercentage> listDataWP;
    private M_WellnessPercentage selectedListWP;
    ListAdapterWellnessPercentage adapterWP;

    private List<M_Periodisasi> listDataPeriodisasi;
    private M_Periodisasi selectedListPeriodisasi;
    ListAdapterPeriodisasi adapterPeriodisasi;

    SearchView searchViewMember,searchViewAllCabor,searchViewCideraToday;
    String urlProfile = "",nameUser = "",groupUser = "",roleName = "", moduleURL = "",moduleName = "";
    SessionManager session,sessionCode,sessionUrlProfile,sessionNameUser,
            sessionGroupUser,sessionRoleName,sessionGroupID,sessionGroupName,
            sessionRoleType,sessionQtyCidera,sessionAccess,sessionCover,sessionNewVersionApp,
            sessionWellnessDate;
    private String Username,QtyCidera,access_all,cover_url,newVersionApp,
            wellnessDate = "";
    String urlJsonRoleName = "",urlJsonGroupSC = "",master_group_id,master_group_name,role_type;
    WebView webViewProfile;
    RelativeLayout layoutDashboard,layoutAccount,layoutUpdate,layoutPeriodization,layoutWellnessPercentage;

    String caborSC = "";

    LinearLayout btn_kpf,btn_daily,btn_monotony,btn_prediction,btn_recovery,btn_meal;
    //NetworkImageView thumbNail;

    TextView text_name,text_group,text_class;

    Dialog dialogChangePassword;
    Button btn_change_password,btn_edit_profile,btn_auth;


    private String APK_PATH = "/sdcard/Download/";
    private String nameFile = "", versionName = "";
    private String nameInstall = "iam-prima.apk";
    private Intent intent;
    private String urlDownload = "";
    ServerRequest serverRequest;

    private String versionCurrent = "";
    private String versionUpdate = "";
    private String idMenu = "";
    private String urlGeneralLog = "";

    public static Integer APP_VERSION = 0;
    public static String APP_VERSION_TEXT = "0.1";

    EditText oldPassword,newPassword;
    TextView save,text_change_password;

    LinearLayout fragment_update_header,fragment_update_second;


    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int RESULT_LOAD_IMG = 1;
    private String selectedImagePath;
    private Uri fileUri;
    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;


    CircularNetworkImageView thumbNail;
    CircularImageView thumbNail2;
    //ImageView thumbNail2;
    String imageName;

    private File temp_path;
    private final int COMPRESS = 100;
    private File file = null;

    private Uri outputFileUri;
    String mCurrentPhotoPath;
    private Uri selectedImageUri;
    private static final int SELECT_PICTURE_CAMARA = 101, SELECT_PICTURE = 201, CROP_IMAGE = 301,LOAD_PDF = 302;

    long totalSize = 0;

    private GoogleApiClient client;

    //DEKLARASI UNTUK WELLNESS RATE
    ImageView ic_arow_up,ic_arow_down,ic_arow_right,ic_arow_left;
    RelativeLayout relative_wellness_show_cabor,relative_wellness_show_category;
    private Spinner spinnerGroupWellness,spinner_group_category_wellness;
    private ArrayList<M_GroupAccessPrima> groupCategoryList;
    TextView text_group_code;
    String typeGroup;
    TextView text_wp_percentage_grey,text_wp_percentage_red,text_wp_percentage_yellow,
            text_wp_percentage_orange,text_wp_percentage_green,text_wp_percentage_green_arrow;


    //DEKLARASI UNTUK PERIODIZATION
    File file1;
    TextView text_1;
    private String selectedFilePath;
    TextView txtPercentage;
    private Spinner spinnerGroup;
    private ArrayList<M_GroupPrima> groupList;
    int lablesSpinner = 0;
    String GroupCodeTemp = "";


    //DEKLARASI UNTUK CEK CIDERA ATLET


    private String android_device_id;
    private String android_device_name;
    private String version_release;

    NestedScrollView scrollView;

    private final Handler mHandler = new Handler();
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    protected PowerManager.WakeLock mWakeLock;

    private int numRunningActivities = 0;

    DBManagerGroup dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // To avoid cpu-blocking, we create a background handler to run our service

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbManager = new DBManagerGroup(this);
        dbManager.open();

        android_device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        android_device_name = Build.MANUFACTURER + " - " + Build.MODEL;
        android_device_name = android_device_name.replace(" ","CEK");
        version_release = Build.VERSION.RELEASE;

        searchViewMember = (SearchView) findViewById(R.id.search);
        searchViewAllCabor = (SearchView) findViewById(R.id.search);
        searchViewCideraToday = (SearchView) findViewById(R.id.search);

        text1 = (TextView) findViewById(R.id.text1);
        text_1 = (TextView) findViewById(R.id.text_1);
        text_name = (TextView) findViewById(R.id.text_name);
        text_group = (TextView) findViewById(R.id.text_group);
        // text_class = (TextView) findViewById(R.id.text_class);

        webViewProfile = (WebView) findViewById(R.id.webViewProfile);
        scrollView = (NestedScrollView)  findViewById(R.id.scrollView);
        listViewMember = (ListView) findViewById(R.id.listview_member);
        listViewAllCabor = (ListView) findViewById(R.id.listview_member);
        listViewCideraToday = (ListView) findViewById(R.id.listview_member);
        listViewWP = (ListView) findViewById(R.id.listview_wellness);

        //ACTIVITY DASHBOARD WELLNESS PERCENTAGE
        text_wp_percentage_grey = (TextView) findViewById(R.id.text_percentage_grey);
        text_wp_percentage_red = (TextView) findViewById(R.id.text_percentage_red);
        text_wp_percentage_yellow = (TextView) findViewById(R.id.text_percentage_yellow);
        text_wp_percentage_orange = (TextView) findViewById(R.id.text_percentage_orange);
        text_wp_percentage_green = (TextView) findViewById(R.id.text_percentage_green);
        text_wp_percentage_green_arrow = (TextView) findViewById(R.id.text_percentage_green_arrow);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        layoutDashboard = (RelativeLayout) findViewById(R.id.dashboard);
        layoutAccount = (RelativeLayout) findViewById(R.id.account);
        layoutPeriodization = (RelativeLayout) findViewById(R.id.periodization);
        layoutWellnessPercentage = (RelativeLayout) findViewById(R.id.wellness);
        //listViewWellnessPercentage = (ListView) findViewById(R.id.listview_wellness);
        layoutUpdate = (RelativeLayout) findViewById(R.id.update_);
        thumbNail = (CircularNetworkImageView) findViewById(R.id.thumbnail_profile);
        thumbNail2 = (CircularImageView) findViewById(R.id.thumbnail_profile_2);

        relative_wellness_show_cabor = (RelativeLayout) findViewById(R.id.relative_wellness_show_cabor);
        relative_wellness_show_category = (RelativeLayout) findViewById(R.id.relative_wellness_show_category);
        ic_arow_up = (ImageView) findViewById(R.id.ic_arow_up);
        ic_arow_down = (ImageView) findViewById(R.id.ic_arow_down);
        ic_arow_right = (ImageView) findViewById(R.id.ic_arow_right);
        ic_arow_left = (ImageView) findViewById(R.id.ic_arow_left);
        text_group_code = (TextView) findViewById(R.id.text_group_code);

        //thumbNail2 = (ImageView) findViewById(R.id.thumbnail_profile_2);

        btn_change_password = (Button) findViewById(R.id.btn_change_password);
        btn_edit_profile = (Button) findViewById(R.id.btn_edit_profile);
        btn_auth = (Button) findViewById(R.id.btn_auth);

        fragment_update_header = (LinearLayout) findViewById(R.id.header_layout);
        fragment_update_second = (LinearLayout) findViewById(R.id.second_layout);

        btn_kpf = (LinearLayout) findViewById(R.id.btn_kpf);
        btn_daily = (LinearLayout) findViewById(R.id.btn_daily);
        btn_monotony = (LinearLayout) findViewById(R.id.btn_monotony);
        btn_prediction = (LinearLayout) findViewById(R.id.btn_prediction);
        btn_recovery = (LinearLayout) findViewById(R.id.btn_recovery);
        btn_meal = (LinearLayout) findViewById(R.id.btn_meal);

        progressBar.setVisibility(View.GONE);
        webViewProfile.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        listViewMember.setVisibility(View.GONE);
        searchViewMember.setVisibility(View.GONE);
        searchViewAllCabor.setVisibility(View.GONE);
        layoutAccount.setVisibility(View.GONE);
        layoutUpdate.setVisibility(View.GONE);
        layoutPeriodization.setVisibility(View.GONE);
        layoutWellnessPercentage.setVisibility(View.GONE);
        text1.setVisibility(View.GONE);
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // Session manager
        session = new SessionManager(getApplicationContext());
        sessionCode = new SessionManager(getApplicationContext());
        sessionNameUser = new SessionManager(getApplicationContext());
        sessionGroupUser = new SessionManager(getApplicationContext());
        sessionRoleName = new SessionManager(getApplicationContext());
        sessionUrlProfile = new SessionManager(getApplicationContext());
        sessionGroupID = new SessionManager(getApplicationContext());
        sessionGroupName = new SessionManager(getApplicationContext());
        sessionRoleType = new SessionManager(getApplicationContext());
        sessionQtyCidera = new SessionManager(getApplicationContext());
        sessionAccess = new SessionManager(getApplicationContext());
        sessionCover = new SessionManager(getApplicationContext());
        sessionNewVersionApp  = new SessionManager(getApplicationContext());
        sessionWellnessDate = new SessionManager(getApplicationContext());

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        if (SessionManager.isLoggedIn()) {
            HashMap<String, String> user = sessionCode.GetSessionKode();
            Username = user.get(SessionManager.KEY_USERNAME);
            //jalanin musik
            //final MediaPlayer mp = MediaPlayer.create(this, R.raw.sunny);
            //mp.start();

            if(isNetworkAvailable()){
                mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        // checking for type intent filter
                        if (intent.getAction().equals(id.or.ppfi.firebase.notification.app.Config.REGISTRATION_COMPLETE)) {
                            // gcm successfully registered
                            // now subscribe to `global` topic to receive app wide notifications
                            FirebaseMessaging.getInstance().subscribeToTopic(id.or.ppfi.firebase.notification.app.Config.TOPIC_GLOBAL);
                            displayFirebaseRegId();
                        } else if (intent.getAction().equals(id.or.ppfi.firebase.notification.app.Config.PUSH_NOTIFICATION)) {
                            // new push notification is received
                            String message = intent.getStringExtra("message");
                            Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                        }
                    }
                };
                displayFirebaseRegId();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        versionAPP();
                    }
                }, 1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {new GenerateUser().execute();
                    }
                }, 1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() { new getQtyCidera().execute();
                    }
                }, 1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() { new GenerateAppUpdate().execute();
                    }
                }, 1000);

            }



            /*
            Intent data = getIntent();

            if(data.getStringExtra("form").equals("Register")){

                urlJsonRoleName = "http://masterdata.iamprima.com/index.php/JsonRoleName/Username/"+Username;
                urlJsonGroupSC = "http://masterdata.iamprima.com/index.php/JsonGroupSC/Username/"+Username;
                new GenerateUser().execute();
            }*/

            HashMap<String, String> name = sessionNameUser.GetSessionUserFullName();
            nameUser = name.get(SessionManager.KEY_USER_FULLNAME);

            HashMap<String, String> group = sessionGroupUser.GetSessionGroupGRP();
            groupUser = group.get(SessionManager.KEY_GROUP_USERGRP);

            HashMap<String, String> role = sessionRoleName.GetSessionRoleName();
            roleName = role.get(SessionManager.KEY_ROLE_NAME);

            HashMap<String, String> urlP = sessionUrlProfile.GetSessionUrlProfile();
            urlProfile = urlP.get(SessionManager.KEY_URL_PROFILE);

            HashMap<String, String> groupName = sessionGroupName.GetSessionGroupName();
            master_group_name = groupName.get(SessionManager.KEY_GROUP_NAME);

            HashMap<String, String> groupID = sessionGroupID.GetSessionGroupID();
            master_group_id = groupID.get(SessionManager.KEY_GROUP_ID);

            HashMap<String, String> roleType = sessionRoleType.GetSessionRoleType();
            role_type = roleType.get(SessionManager.KEY_ROLE_TYPE);

            HashMap<String, String> cover = sessionCover.GetSessionCover();
            cover_url = cover.get(SessionManager.KEY_COVER);

            HashMap<String, String> qty = sessionQtyCidera.GetSessionQtyCidera();
            QtyCidera = qty.get(SessionManager.KEY_QTY_CIDERA);

            HashMap<String, String> wd = sessionWellnessDate.GetSessionWellnessDate();
            wellnessDate = wd.get(SessionManager.KEY_WELLNESS_DATE);

            DateFormat dfYear = new SimpleDateFormat("yyyy-MM-dd");
            String dateNow = dfYear.format(new Date());

            try{
                if(!wellnessDate.equals(dateNow)){
                    int interval = 1000 * 1;
                    Intent intent = new Intent(AdvancedActivityAthlete.this, AlarmReceiverAthlete.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            AdvancedActivityAthlete.this, interval, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                            + (interval), pendingIntent);
                }
            }catch (Exception e){
                int interval = 1000 * 1;
                Intent intent = new Intent(AdvancedActivityAthlete.this, AlarmReceiverAthlete.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        AdvancedActivityAthlete.this, interval, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                        + (interval), pendingIntent);
            }

            thumbNail.setVisibility(View.GONE);
            thumbNail2.setVisibility(View.GONE);

            if(isNetworkAvailable()){
                thumbNail.setVisibility(View.VISIBLE);
                thumbNail2.setVisibility(View.GONE);
                if(urlProfile.length() > 0){
                    if(urlProfile.contains("http://portal") || urlProfile.contains("https://portal"))
                        thumbNail.setImageUrl(urlProfile.replace("portal.iamprima.com","iamprima.com/portal"), imageLoader);
                    else
                        thumbNail.setImageUrl(urlProfile, imageLoader);
                }
                thumbNail.setDefaultImageResId(R.drawable._default);
                thumbNail.setErrorImageResId(R.drawable._corrupted);
            }else{
                thumbNail2.setVisibility(View.VISIBLE);
                thumbNail.setVisibility(View.GONE);
            }


            text_name.setText(nameUser);
            text_group.setText(roleName);


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
                   new DownloadImage().execute();
               }


        }
        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.drawer_item_advanced_drawer);


        spinnerGroup = (Spinner) findViewById(R.id.spinner_group);
        spinnerGroup.setOnItemSelectedListener(this);

        spinnerGroupWellness = (Spinner) findViewById(R.id.spinner_group_wellness);
        spinnerGroupWellness.setOnItemSelectedListener(this);

        spinner_group_category_wellness = (Spinner) findViewById(R.id.spinner_group_category_wellness);
        spinner_group_category_wellness.setOnItemSelectedListener(this);



        profile = new ProfileDrawerItem().withName(nameUser)
                .withEmail(roleName)
                .withIcon(Uri.parse(urlProfile));




        //  profile = new ProfileDrawerItem().withName("Nando").withEmail("test").withIcon(Uri.parse("http://portal.iamprima.com/assets/pictures/9731481551200.jpg"));

        // Create the AccountHeader
        buildHeader(false, savedInstanceState);

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(

                        new CustomPrimaryDrawerItem().withName("Group").withIcon(FontAwesome.Icon.faw_users).withIdentifier(1),


                        new OverflowMenuDrawerItem().withName("Dashboard")
                                .withDescription(master_group_name)
                                .withMenu(R.menu.fragment_menu)
                                .withOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        Toast.makeText(AdvancedActivityAthlete.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                }).withIcon(FontAwesome.Icon.faw_dashcube)
                                .withIdentifier(2),

                        //  new PrimaryDrawerItem().withName("Member").withIcon(FontAwesome.Icon.faw_users).withIdentifier(3),

                        new PrimaryDrawerItem().withName("Periodization").withIcon(FontAwesome.Icon.faw_user).withIdentifier(6),

                        new CustomUrlPrimaryDrawerItem().withName("User Profile").withDescription("Information Personality").withIcon(urlProfile).withIdentifier(4),

                        new SectionDrawerItem().withName(R.string.drawer_item_multi_drawer).withName(R.string.drawer_item_section_header).withSelectable(false),

                        new PrimaryDrawerItem().withName("Account").withIcon(FontAwesome.Icon.faw_user).withIdentifier(5),

                        new PrimaryDrawerItem().withName("Atlet Cidera").withIcon(GoogleMaterial.Icon.gmd_fire).withIdentifier(7)

                        // new PrimaryDrawerItem().withName("Wellness Perentage").withIcon(FontAwesome.Icon.faw_heartbeat).withIdentifier(8)

                        //   new PrimaryDrawerItem().withName("Setting").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(6)

                        //   new SectionDrawerItem().withName(R.string.drawer_item_multi_drawer).withName("Other").withSelectable(false),

                        //   new PrimaryDrawerItem().withName("Administration").withIcon(GoogleMaterial.Icon.gmd_book).withIdentifier(7),
                        //  new PrimaryDrawerItem().withName("Notification").withIcon(GoogleMaterial.Icon.gmd_info_outline).withIdentifier(8)

                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                urlGeneralLog = "http://dlog.iamprima.com/index.php/JsonInsertLog/GeneralLog/"+Username+"/"+android_device_id+"/"+android_device_name+"/"+version_release+"/1";

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        new InsertLog().execute();
                                    }
                                }, 1000);

                                //text1.setText("Home");

                                if (role_type.equals("ATL") || role_type.equals("CHC")) {
                                    text1.setVisibility(View.GONE);
                                    webViewProfile.setVisibility(View.GONE);
                                    scrollView.setVisibility(View.GONE);
                                    layoutDashboard.setVisibility(View.GONE);
                                    layoutAccount.setVisibility(View.GONE);
                                    searchViewMember.setVisibility(View.VISIBLE);
                                    listViewMember.setVisibility(View.VISIBLE);
                                    layoutUpdate.setVisibility(View.GONE);
                                    layoutPeriodization.setVisibility(View.GONE);
                                    layoutWellnessPercentage.setVisibility(View.GONE);

                                    serverRequest = new ServerRequest();
                                    listDataMember = new ArrayList<M_Member>();
                                    new GenerateListMember().execute("load");

                                    searchViewMember.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                        @Override
                                        public boolean onQueryTextSubmit(String query) {
                                            return false;
                                        }
                                        @Override
                                        public boolean onQueryTextChange(String newText) {
                                            adapter.getFilter().filter(newText);
                                            return false;
                                        }
                                    });
                                }else {
                                    if (groupUser.equals("GSC")) {
                                        text1.setVisibility(View.GONE);
                                        webViewProfile.setVisibility(View.GONE);
                                        scrollView.setVisibility(View.GONE);
                                        layoutDashboard.setVisibility(View.GONE);
                                        layoutAccount.setVisibility(View.GONE);
                                        searchViewMember.setVisibility(View.GONE);
                                        listViewMember.setVisibility(View.GONE);
                                        searchViewAllCabor.setVisibility(View.VISIBLE);
                                        listViewAllCabor.setVisibility(View.VISIBLE);
                                        layoutUpdate.setVisibility(View.GONE);
                                        layoutPeriodization.setVisibility(View.GONE);
                                        layoutWellnessPercentage.setVisibility(View.GONE);

                                        serverRequest = new ServerRequest();
                                        listDataAllCabor = new ArrayList<M_AllCabor>();
                                        new GenerateAllCabor().execute("load");

                                        searchViewAllCabor.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                            @Override
                                            public boolean onQueryTextSubmit(String query) {
                                                return false;
                                            }
                                            @Override
                                            public boolean onQueryTextChange(String newText) {
                                                adapterCabor.getFilter().filter(newText);
                                                return false;
                                            }
                                        });
                                    }else if (groupUser.equals("SATLAK") || groupUser.equals("PRIMA")) {
                                        text1.setVisibility(View.GONE);
                                        webViewProfile.setVisibility(View.GONE);
                                        scrollView.setVisibility(View.GONE);
                                        layoutDashboard.setVisibility(View.GONE);
                                        layoutAccount.setVisibility(View.GONE);
                                        searchViewMember.setVisibility(View.GONE);
                                        listViewMember.setVisibility(View.GONE);
                                        searchViewAllCabor.setVisibility(View.VISIBLE);
                                        listViewAllCabor.setVisibility(View.VISIBLE);
                                        layoutUpdate.setVisibility(View.GONE);
                                        layoutPeriodization.setVisibility(View.GONE);
                                        layoutWellnessPercentage.setVisibility(View.GONE);

                                        serverRequest = new ServerRequest();
                                        listDataAllCabor = new ArrayList<M_AllCabor>();
                                        new GenerateAllCabor().execute("load");

                                        searchViewAllCabor.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                            @Override
                                            public boolean onQueryTextSubmit(String query) {
                                                return false;
                                            }
                                            @Override
                                            public boolean onQueryTextChange(String newText) {
                                                adapterCabor.getFilter().filter(newText);
                                                return false;
                                            }
                                        });
                                    }else{
                                        text1.setVisibility(View.GONE);
                                        webViewProfile.setVisibility(View.GONE);
                                        scrollView.setVisibility(View.GONE);
                                        layoutDashboard.setVisibility(View.GONE);
                                        layoutAccount.setVisibility(View.GONE);
                                        searchViewMember.setVisibility(View.GONE);
                                        listViewMember.setVisibility(View.GONE);
                                        searchViewAllCabor.setVisibility(View.VISIBLE);
                                        listViewAllCabor.setVisibility(View.VISIBLE);
                                        layoutUpdate.setVisibility(View.GONE);
                                        layoutPeriodization.setVisibility(View.GONE);
                                        layoutWellnessPercentage.setVisibility(View.GONE);

                                        serverRequest = new ServerRequest();
                                        listDataAllCabor = new ArrayList<M_AllCabor>();
                                        new GenerateAllCabor().execute("load");

                                        searchViewAllCabor.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                            @Override
                                            public boolean onQueryTextSubmit(String query) {
                                                return false;
                                            }
                                            @Override
                                            public boolean onQueryTextChange(String newText) {
                                                adapterCabor.getFilter().filter(newText);
                                                return false;
                                            }
                                        });
                                    }
                                }




                            } else if (drawerItem.getIdentifier() == 2) {
                                urlGeneralLog = "http://dlog.iamprima.com/index.php/JsonInsertLog/GeneralLog/"+Username+"/"+android_device_id+"/"+android_device_name+"/"+version_release+"/4";

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        new InsertLog().execute();
                                    }
                                }, 1000);

                                text1.setText("Dashboard");
                                text1.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                webViewProfile.setVisibility(View.GONE);
                                scrollView.setVisibility(View.GONE);
                                listViewMember.setVisibility(View.GONE);
                                searchViewMember.setVisibility(View.GONE);
                                searchViewAllCabor.setVisibility(View.GONE);
                                layoutAccount.setVisibility(View.GONE);
                                layoutDashboard.setVisibility(View.VISIBLE);
                                layoutUpdate.setVisibility(View.GONE);
                                layoutPeriodization.setVisibility(View.GONE);
                                layoutWellnessPercentage.setVisibility(View.GONE);

                            } else if (drawerItem.getIdentifier() == 3) {
                                text1.setVisibility(View.GONE);
                                webViewProfile.setVisibility(View.GONE);
                                scrollView.setVisibility(View.GONE);
                                layoutDashboard.setVisibility(View.GONE);
                                layoutAccount.setVisibility(View.GONE);
                                searchViewAllCabor.setVisibility(View.GONE);
                                searchViewMember.setVisibility(View.VISIBLE);
                                listViewMember.setVisibility(View.VISIBLE);
                                layoutPeriodization.setVisibility(View.GONE);
                                layoutWellnessPercentage.setVisibility(View.GONE);

                                serverRequest = new ServerRequest();
                                listDataMember = new ArrayList<M_Member>();
                                new GenerateListMember().execute("load");

                                searchViewMember.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        return false;
                                    }
                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        adapter.getFilter().filter(newText);
                                        return false;
                                    }
                                });

                            } else if (drawerItem.getIdentifier() == 4) {
                                urlGeneralLog = "http://dlog.iamprima.com/index.php/JsonInsertLog/GeneralLog/"+Username+"/"+android_device_id+"/"+android_device_name+"/"+version_release+"/2";

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        new InsertLog().execute();
                                    }
                                }, 1000);
                                refreshDataProfile();
                                /*
                                urlJsonRoleName = "http://masterdata.iamprima.com/index.php/JsonRoleName/Username/"+Username;
                                new ChangeProfile().execute();
                                */

                            } else if (drawerItem.getIdentifier() == 5) {
                                urlGeneralLog = "http://dlog.iamprima.com/index.php/JsonInsertLog/GeneralLog/"+Username+"/"+android_device_id+"/"+android_device_name+"/"+version_release+"/3";

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        new InsertLog().execute();
                                    }
                                }, 1000);
                                text1.setVisibility(View.GONE);
                                webViewProfile.setVisibility(View.GONE);
                                scrollView.setVisibility(View.GONE);
                                layoutDashboard.setVisibility(View.GONE);
                                searchViewAllCabor.setVisibility(View.GONE);
                                searchViewMember.setVisibility(View.GONE);
                                listViewMember.setVisibility(View.GONE);
                                layoutAccount.setVisibility(View.VISIBLE);
//                                btn_change_password.setVisibility(View.VISIBLE);
                                layoutUpdate.setVisibility(View.GONE);
                                layoutPeriodization.setVisibility(View.GONE);
                                layoutWellnessPercentage.setVisibility(View.GONE);


                                btn_change_password.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        Intent i = new Intent(AdvancedActivityAthlete.this, ChangePasswordActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        AdvancedActivityAthlete.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    }
                                });


                                btn_edit_profile.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        Intent i = new Intent(AdvancedActivityAthlete.this, EditProfileActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        AdvancedActivityAthlete.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    }
                                });

                                btn_auth.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        Intent i = new Intent(AdvancedActivityAthlete.this, ListAuthActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        AdvancedActivityAthlete.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    }
                                });



                                thumbNail.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        //getImageGalery();
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            if (checkPermission()) {
                                                onClickGallery();
                                            } else {
                                                requestPermission();
                                            }
                                        }else{
                                            onClickGallery();
                                        }

                                    }
                                });


                            } else if (drawerItem.getIdentifier() == 6) {
                                text1.setVisibility(View.GONE);
                                webViewProfile.setVisibility(View.GONE);
                                scrollView.setVisibility(View.GONE);
                                layoutDashboard.setVisibility(View.GONE);
                                searchViewAllCabor.setVisibility(View.GONE);
                                searchViewMember.setVisibility(View.GONE);
                                listViewMember.setVisibility(View.GONE);
                                layoutAccount.setVisibility(View.GONE);
                                layoutUpdate.setVisibility(View.GONE);
                                txtPercentage = (TextView) findViewById(R.id.txtPercentage);

                                layoutPeriodization.setVisibility(View.VISIBLE);
                                layoutWellnessPercentage.setVisibility(View.GONE);

                                listViewPeriodisasi = (ListView) findViewById(R.id.listview_main);
                                listViewPeriodisasi.setVisibility(View.VISIBLE);

                                ImageView btnAdd = (ImageView) findViewById(R.id.btn_add);
                                btnAdd.setVisibility(View.GONE);

                                serverRequest = new ServerRequest();
                                listDataPeriodisasi = new ArrayList<M_Periodisasi>();
                                new GeneratePeriodisasi().execute("load");

                            } else if (drawerItem.getIdentifier() == 7) {

                                urlGeneralLog = "http://dlog.iamprima.com/index.php/JsonInsertLog/GeneralLog/"+Username+"/"+android_device_id+"/"+android_device_name+"/"+version_release+"/5";

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        new InsertLog().execute();
                                    }
                                }, 1000);
                                text1.setVisibility(View.GONE);
                                webViewProfile.setVisibility(View.GONE);
                                scrollView.setVisibility(View.GONE);
                                layoutDashboard.setVisibility(View.GONE);
                                layoutAccount.setVisibility(View.GONE);
                                searchViewMember.setVisibility(View.GONE);
                                listViewMember.setVisibility(View.GONE);
                                searchViewAllCabor.setVisibility(View.GONE);
                                listViewAllCabor.setVisibility(View.GONE);
                                layoutUpdate.setVisibility(View.GONE);
                                layoutPeriodization.setVisibility(View.GONE);
                                layoutWellnessPercentage.setVisibility(View.GONE);


                                searchViewCideraToday.setVisibility(View.VISIBLE);
                                listViewCideraToday.setVisibility(View.VISIBLE);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        new cekCideraAvailable().execute();
                                    }
                                }, 1000);


                                serverRequest = new ServerRequest();
                                listDataCideraToday = new ArrayList<M_CederaToday>();
                                new GenerateCideraToday().execute("load");

                                searchViewCideraToday.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        return false;
                                    }
                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        adapterCideraToday.getFilter().filter(newText);
                                        return false;
                                    }
                                });
                            } else if (drawerItem.getIdentifier() == 8) {
                                /*
                                urlGeneralLog = "http://masterdata.iamprima.com/index.php/JsonInsertLog/GeneralLog/"+Username+"/"+android_device_id+"/"+android_device_name+"/"+version_release+"/5";

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        new InsertLog().execute();
                                    }
                                }, 1000);
                                */

                                text1.setVisibility(View.GONE);
                                text1.setText("Testing");
                                webViewProfile.setVisibility(View.GONE);
                                scrollView.setVisibility(View.GONE);
                                layoutDashboard.setVisibility(View.GONE);
                                layoutAccount.setVisibility(View.GONE);
                                searchViewMember.setVisibility(View.GONE);
                                listViewMember.setVisibility(View.GONE);
                                listViewCideraToday.setVisibility(View.GONE);
                                searchViewAllCabor.setVisibility(View.GONE);
                                listViewAllCabor.setVisibility(View.GONE);
                                layoutUpdate.setVisibility(View.GONE);
                                layoutPeriodization.setVisibility(View.GONE);
                                layoutWellnessPercentage.setVisibility(View.VISIBLE);

                                listViewWP.setVisibility(View.GONE);
                                serverRequest = new ServerRequest();
                                //listDataWP = new ArrayList<M_WellnessPercentage>();
                                //new GenerateWellnessPercentage().execute("load");


                                if (lablesSpinner == 0){
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            groupList = new ArrayList<M_GroupPrima>();
                                            new GenerateGroupSpinner().execute();
                                        }
                                    }, 1000);
                                }


                                ic_arow_down.setVisibility(View.GONE);
                                ic_arow_up.setVisibility(View.VISIBLE);

                                ic_arow_left.setVisibility(View.GONE);
                                ic_arow_right.setVisibility(View.GONE);
                                relative_wellness_show_category.setVisibility(View.GONE);


                                ic_arow_up.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        ic_arow_up.setVisibility(View.GONE);
                                        ic_arow_down.setVisibility(View.VISIBLE);
                                        relative_wellness_show_cabor.setVisibility(View.GONE);

                                    }
                                });

                                ic_arow_down.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        ic_arow_up.setVisibility(View.VISIBLE);
                                        ic_arow_down.setVisibility(View.GONE);
                                        relative_wellness_show_cabor.setVisibility(View.VISIBLE);


                                    }
                                });

                                ic_arow_left.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        ic_arow_left.setVisibility(View.GONE);
                                        ic_arow_right.setVisibility(View.VISIBLE);
                                        relative_wellness_show_category.setVisibility(View.GONE);
                                    }
                                });

                                ic_arow_right.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        ic_arow_left.setVisibility(View.VISIBLE);
                                        ic_arow_right.setVisibility(View.GONE);
                                        relative_wellness_show_category.setVisibility(View.VISIBLE);

                                        groupCategoryList = new ArrayList<M_GroupAccessPrima>();
                                        new GenerateCategoryGroupSpinner().execute();

                                    }
                                });




                            } else if (drawerItem.getIdentifier() == 9) {
                                urlGeneralLog = "http://dlog.iamprima.com/index.php/JsonInsertLog/GeneralLog/"+Username+"/"+android_device_id+"/"+android_device_name+"/"+version_release+"/6";

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        new InsertLog().execute();
                                    }
                                }, 1000);
                                text1.setVisibility(View.GONE);
                                webViewProfile.setVisibility(View.GONE);
                                scrollView.setVisibility(View.GONE);
                                layoutDashboard.setVisibility(View.GONE);
                                searchViewAllCabor.setVisibility(View.GONE);
                                searchViewMember.setVisibility(View.GONE);
                                listViewMember.setVisibility(View.GONE);
                                layoutAccount.setVisibility(View.GONE);
                                layoutUpdate.setVisibility(View.VISIBLE);
                                layoutPeriodization.setVisibility(View.GONE);
                                layoutWellnessPercentage.setVisibility(View.GONE);


                                // new GetLinkUpdate().execute();

                                HashMap<String, String> app = sessionNewVersionApp.GetSessionNewVersionApp();
                                newVersionApp = app.get(SessionManager.KEY_APP_UPDATE);

                                if (Float.parseFloat(versionCurrent) < Float.parseFloat(newVersionApp)){
                                    //need to update
                                    fragment_update_header.setVisibility(View.VISIBLE);
                                    fragment_update_second.setVisibility(View.GONE);
                                } else{
                                    //no need to update
                                    fragment_update_header.setVisibility(View.GONE);
                                    fragment_update_second.setVisibility(View.GONE);
                                    text1.setVisibility(View.VISIBLE);
                                    text1.setText("No Update Available");
                                }




                            } else if (drawerItem.getIdentifier() == 10) {
                                //text1.setText("Sign Out");
                                progressDialog = new ProgressDialog(AdvancedActivityAthlete.this);
                                progressDialog.setTitle("Sign Out...");
                                progressDialog.setMessage("Please wait...");
                                progressDialog.setIndeterminate(false);
                                progressDialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        dbManager.deleteAll();
                                        dbManager.close();

                                        db.deleteUsers();
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
                                        Intent i = new Intent(AdvancedActivityAthlete.this, LoginActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        finish();
                                        AdvancedActivityAthlete.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                    }
                                }, SPLASH_TIME_OUT);
                            }
                            if (intent != null) {
                                AdvancedActivityAthlete.this.startActivity(intent);
                            }
                        }
                        return false;
                    }
                })
                .addStickyDrawerItems(
                        new SecondaryDrawerItem().withName("Update").withIcon(GoogleMaterial.Icon.gmd_collection_case_play).withIdentifier(9),
                        new SecondaryDrawerItem().withName("Sign Out").withIcon(FontAwesome.Icon.faw_sign_out).withIdentifier(10)
                )
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();

        //if you have many different types of DrawerItems you can magically pre-cache those items to get a better scroll performance
        //make sure to init the cache after the DrawerBuilder was created as this will first clear the cache to make sure no old elements are in
        RecyclerViewCacheUtil.getInstance().withCacheSize(2).init(result);

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            result.setSelection(21, false);

            //set the active profile
            headerResult.setActiveProfile(profile);
        }

        //buat angka di sebelah menu nav nya
        //result.updateBadge(4, new StringHolder("10" + ""));
        result.updateBadge(7, new StringHolder(QtyCidera + ""));




    }

    private void buildHeader(boolean compact, Bundle savedInstanceState) {
        // Create the AccountHeader



        if(cover_url == ""){
            headerResult = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withHeaderBackground(R.drawable.header)
                    .withCompactStyle(compact)
                    .addProfiles(
                            profile
                    )
                    .withSavedInstance(savedInstanceState)
                    .build();
        }else{


            try{
                headerResult = new AccountHeaderBuilder()
                        .withActivity(this)
                        //.withHeaderBackground(R.drawable.header)

                        .withCompactStyle(compact)
                        .addProfiles(
                                profile
                        )
                        .withSavedInstance(savedInstanceState)
                        .build();

                ImageView cover = headerResult.getHeaderBackgroundView(); //get your ImageView

                Glide.with(this).load(cover_url).into(cover);

            }catch (Exception e){
                headerResult = new AccountHeaderBuilder()
                        .withActivity(this)
                        .withHeaderBackground(R.drawable.header)
                        .withCompactStyle(compact)
                        .addProfiles(
                                profile
                        )
                        .withSavedInstance(savedInstanceState)
                        .build();
            }
        }

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Advanced Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap>{
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }

    private class DownloadImage extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... sUrl) {
            try {
                File folder = new File(Environment.getExternalStorageDirectory().toString()+"/Prima/Profile/Images");
                folder.mkdirs();
                String extStorageDirectory = folder.toString();

                URL url = new URL(urlProfile);
                URLConnection connection = url.openConnection();
                connection.connect();
                // this will be useful so that you can show a typical 0-100% progress bar
                int fileLength = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(extStorageDirectory+"/"+imageName);
                Log.e("Direktori : ", extStorageDirectory);
                System.setProperty("http.keepAlive", "false");
                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();


            } catch (Exception e) {
                // Log.e("EROR DOWNLOAD FILE : ", e.toString());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AdvancedActivityAthlete.this);
            progressDialog.setTitle("Generate Data...");
            progressDialog.setMessage("Preparing user...");
            progressDialog.setIndeterminate(false);
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressDialog.setProgress(progress[0]);
        }

        protected void onPostExecute(String result) {
            Toast.makeText(AdvancedActivityAthlete.this, "Done", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }


    public void loadUpdateYes(View v) {
        executeUpdate();
    }

    public void loadKPF(View v) {
        // Toast.makeText(this, "Layout KPF", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(AdvancedActivityAthlete.this, WebviewActivity.class);

        if (role_type.equals("ATL") || role_type.equals("CHC")) {
            moduleURL = "http://profiling.iamprima.com/index.php/login_validation/index/id/"+Username;
        }else{
            if (groupUser.equals("GSC"))
            {
                moduleURL = "http://profiling.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+master_group_id;
            }else if ( groupUser.equals("SATLAK") || groupUser.equals("PRIMA"))
            {
                moduleURL = "http://profiling.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+master_group_id;
            }else{
                moduleURL = "http://profiling.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+master_group_id;
                //  moduleURL = "http://performance.iamprima.com/index.php/login_validation/index/id/"+Username;
                //  moduleURL = "http://portal.iamprima.com/index.php/account";
                //  moduleURL = "http://www.androidexample.com/media/webview/details.html";
            }
        }


        i.putExtra("username", "UsernameMember");
        i.putExtra("name", "NameMember");
        i.putExtra("gambar", "GambarMember");
        //untuk kebutuhan pelatih teknik dan atlet
        i.putExtra("nama_modul", "Performance Profiling");
        i.putExtra("url_modul", moduleURL);
        startActivity(i);
        AdvancedActivityAthlete.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public void loadWellness(View v) {
        Intent i = new Intent(AdvancedActivityAthlete.this, WebviewActivity.class);
        //Intent i = new Intent(AdvancedActivityAthlete.this, WellnessActivity.class);

        if (role_type.equals("ATL") || role_type.equals("CHC")) {
            moduleURL = "http://wellness.iamprima.com/index.php/login_validation/index/id/"+Username;
        }else{
            if (groupUser.equals("GSC"))
            {
                moduleURL = "http://wellness.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+master_group_id;
            }else if ( groupUser.equals("SATLAK") || groupUser.equals("PRIMA"))
            {
                moduleURL = "http://wellness.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+master_group_id;
            }else{
                moduleURL = "http://wellness.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+master_group_id;

            }
        }


        i.putExtra("username", "UsernameMember");
        i.putExtra("name", "NameMember");
        i.putExtra("gambar", "GambarMember");
        //untuk kebutuhan pelatih teknik dan atlet
        i.putExtra("nama_modul", "Wellness");
        i.putExtra("url_modul", moduleURL);
        startActivity(i);
        AdvancedActivityAthlete.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public void loadTrainingLoad(View v) {
        Intent i = new Intent(AdvancedActivityAthlete.this, WebviewActivity.class);
        if (role_type.equals("ATL") || role_type.equals("CHC")) {
            moduleURL = "http://monotony.iamprima.com/index.php/login_validation/index/id/"+Username;
        }else{
            if (groupUser.equals("GSC"))
            {
                moduleURL = "http://monotony.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+master_group_id;
            }else if ( groupUser.equals("SATLAK") || groupUser.equals("PRIMA"))
            {
                moduleURL = "http://monotony.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+master_group_id;
            }else{
                moduleURL = "http://monotony.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+master_group_id;

            }
        }

        i.putExtra("username", "UsernameMember");
        i.putExtra("name", "NameMember");
        i.putExtra("gambar", "GambarMember");
        //untuk kebutuhan pelatih teknik dan atlet
        i.putExtra("nama_modul", "Training Load");
        i.putExtra("url_modul", moduleURL);
        startActivity(i);
        AdvancedActivityAthlete.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void loadPMC(View v) {
        Intent i = new Intent(AdvancedActivityAthlete.this, WebviewActivity.class);
        if (role_type.equals("ATL") || role_type.equals("CHC")) {
            moduleURL = "http://pmc.iamprima.com/index.php/login_validation/index/id/"+Username;
        }else{
            if (groupUser.equals("GSC"))
            {
                moduleURL = "http://pmc.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+master_group_id;
            }else if ( groupUser.equals("SATLAK") || groupUser.equals("PRIMA"))
            {
                moduleURL = "http://pmc.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+master_group_id;
            }else{
                moduleURL = "http://pmc.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+master_group_id;

            }
        }

        i.putExtra("username", "UsernameMember");
        i.putExtra("name", "NameMember");
        i.putExtra("gambar", "GambarMember");
        //untuk kebutuhan pelatih teknik dan atlet
        i.putExtra("nama_modul", "PMC");
        i.putExtra("url_modul", moduleURL);
        startActivity(i);
        AdvancedActivityAthlete.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void loadMealPlan(View v) {

        Intent i = new Intent(AdvancedActivityAthlete.this, MealPlanActivity.class);
        startActivity(i);
        AdvancedActivityAthlete.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


        // Intent i = new Intent(AdvancedActivity.this, WebviewActivity.class);

        // moduleURL = "http://mealplan.iamprima.com";
        // moduleURL = "http://www.humananatomyillustrations.com/human-anatomy-internal-external-illustration.html";
        //moduleURL = "http://portal.iamprima.com/assets/pictures/9731481551200.jpg";

        /*

        if (role_type.equals("ATL") || role_type.equals("CHC")) {
            moduleURL = "http://mealplan.iamprima.com/index.php/login_validation/index/id/"+Username;
        }else{
            if (groupUser.equals("GSC"))
            {
                moduleURL = "http://mealplan.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+master_group_id;
            }else if ( groupUser.equals("SATLAK") || groupUser.equals("PRIMA"))
            {
                moduleURL = "http://mealplan.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+master_group_id;
            }else{
                moduleURL = "http://mealplan.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+master_group_id;
            }
        }

         i.putExtra("username", "UsernameMember");
        i.putExtra("name", "NameMember");
        i.putExtra("gambar", "GambarMember");
        //untuk kebutuhan pelatih teknik dan atlet
        i.putExtra("nama_modul", "Meal Plan");
        i.putExtra("url_modul", moduleURL);
        startActivity(i);
        AdvancedActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        */




    }

    public void loadRecovery(View v) {
        Intent i = new Intent(AdvancedActivityAthlete.this, WebviewActivity.class);

        if (role_type.equals("ATL") || role_type.equals("CHC")) {
            moduleURL = "http://recovery.iamprima.com/index.php/login_validation/index/id/"+Username;
        }else{
            if (groupUser.equals("GSC"))
            {
                moduleURL = "http://recovery.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+master_group_id;
            }else if ( groupUser.equals("SATLAK") || groupUser.equals("PRIMA"))
            {
                moduleURL = "http://recovery.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+master_group_id;
            }else{
                moduleURL = "http://recovery.iamprima.com/index.php/login_validation/index/id/"+Username+"/group/"+master_group_id;
            }
        }

        i.putExtra("username", "UsernameMember");
        i.putExtra("name", "NameMember");
        i.putExtra("gambar", "GambarMember");
        //untuk kebutuhan pelatih teknik dan atlet
        i.putExtra("nama_modul", "Recovery Management");
        i.putExtra("url_modul", moduleURL);
        startActivity(i);
        AdvancedActivityAthlete.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_version, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_1:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private List<M_Member> processResponse(String response) {
        List<M_Member> list = new ArrayList<M_Member>();
        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            M_Member data = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                data = new M_Member();
                data.setUserName(obj.getString("username"));
                data.setName(obj.getString("name"));
                data.setLicenceCode(obj.getString("license_code"));
                data.setGambar(obj.getString("gambar"));
                data.setGroupCode(obj.getString("master_licence_group"));
                data.setGroupName(obj.getString("master_group_name"));
                data.setRoleName(obj.getString("role_name"));
                data.setValueWellness(obj.getString("value_wellness"));
                data.setNomorEvent(obj.getString("nomor_event"));
                data.setWellnessDate(obj.getString("wellness_date"));
                data.setCidera(obj.getString("cidera"));
                data.setWellnessTime(obj.getString("wellness_time"));
                list.add(data);
            }
        } catch (JSONException e) {
        }
        return list;
    }

    private void populateListView() {
        adapter = new ListAdapterMember(AdvancedActivityAthlete.this, listDataMember);
        listViewMember.setAdapter(adapter);
        listViewMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v,
                                    int pos, long id) {
                selectedList = (M_Member) adapter.getItem(pos);
                Intent i = new Intent(AdvancedActivityAthlete.this, DetailPersonalActivity.class);

                // String moduleURL = "http://personal.iamprima.com/index.php/login_validation/index/id/"+selectedList.getUserName();

                i.putExtra("nama_modul", "Highlight");
                // i.putExtra("url_modul", moduleURL);
                i.putExtra("member_username", selectedList.getUserName());
                i.putExtra("member_name", selectedList.getName());
                i.putExtra("member_group_code", selectedList.getGroupCode());
                i.putExtra("member_group_name", selectedList.getGroupName());
                startActivity(i);

            }
        });

        listViewMember.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

                selectedList = (M_Member) adapter.getItem(pos);
                Intent i = new Intent(AdvancedActivityAthlete.this, ViewProfileActivity.class);
                i.putExtra("member_username", selectedList.getUserName());
                i.putExtra("member_name", selectedList.getName());
                i.putExtra("member_nomor_event", selectedList.getNomorEvent());
                i.putExtra("member_role_name", selectedList.getRoleName());
                i.putExtra("member_wellness_date", selectedList.getWellnessDate());
                i.putExtra("member_value_wellness", selectedList.getValueWellness());
                startActivity(i);
                return true;

            }
        });
    }

    private class GenerateListMember extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //      progressDialog = new ProgressBar(AdvancedActivity.this);
//			progressDialog.setTitle("Processing...");
            // progressDialog.setMessage("Please wait...");
            // progressDialog.setCancelable(true);
            //           progressDialog.setIndeterminate(false);
//			progressDialog.setButton("Close", new DialogInterface.OnClickListener()
//			 {
//				 public void onClick(DialogInterface dialog, int which)
//				 {
//					 progressDialog.dismiss();
//					 }
//				 });
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

        @Override
        protected String doInBackground(String... params) {
            /** Mengirimkan request ke server dan memproses JSON response */
            String response = serverRequest.sendGetRequest(ServerRequest.urlGroupMember + groupUser);
            listDataMember = processResponse(response);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(100);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    populateListView();
                }
            });
        }

    }

    private List<M_AllCabor> processResponseCabor(String response) {
        List<M_AllCabor> list = new ArrayList<M_AllCabor>();

        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            //			Log.d(TAG, "data lengt: " + jsonArray.length());
            M_AllCabor data = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                data = new M_AllCabor();
                data.setGroupCode(obj.getString("master_group_id"));
                data.setGroupName(obj.getString("master_group_name"));
                data.setGroupCodeWaka(obj.getString("master_group_category"));
                data.setQtyAtlet(obj.getString("master_group_jlh_atlet"));
                data.setQtyPelatih(obj.getString("master_group_jlh_pelatih"));
                data.setGroupHead(obj.getString("master_group_head"));
                data.setGroupLogo(obj.getString("master_group_logo"));
                list.add(data);
            }
        } catch (JSONException e) {
            //			Log.d(TAG, e.getMessage());
        }

        return list;
    }

    private void populateListViewCabor() {
        adapterCabor = new ListAdapterAllCabor(AdvancedActivityAthlete.this, listDataAllCabor);
        listViewAllCabor.setAdapter(adapterCabor);

        // ketika ingin memilih data list
        listViewAllCabor.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View v,
                                    int pos, long id) {
                selectedListCabor = (M_AllCabor) adapterCabor.getItem(pos);
                Intent i = new Intent(AdvancedActivityAthlete.this, MemberListActivity.class);

                i.putExtra("group_code", selectedListCabor.getGroupCode());
                i.putExtra("group_name", selectedListCabor.getGroupName());
                startActivity(i);
                AdvancedActivityAthlete.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        listViewAllCabor.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

                selectedListCabor = (M_AllCabor) adapterCabor.getItem(pos);
                //Toast.makeText(AdvancedActivity.this, "Long Klik...", Toast.LENGTH_SHORT).show();

                sessionGroupID.CreateSessionGroupID(selectedListCabor.getGroupCode());
                sessionGroupName.CreateSessionGroupName(selectedListCabor.getGroupName());
                Toast.makeText(AdvancedActivityAthlete.this, "Dashboard diganti ke Group "+selectedListCabor.getGroupName(), Toast.LENGTH_SHORT).show();
                returnHome();
                return true;

            }
        });
    }

    private class GenerateAllCabor extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

        @Override
        protected String doInBackground(String... params) {
            /** Mengirimkan request ke server dan memproses JSON response */
            String response = "";

            response = serverRequest.sendGetRequest(ServerRequest.urlGroupSC+Username);

            if (role_type.equals("HPD")){
                response = serverRequest.sendGetRequest(ServerRequest.urlGroupSC+Username);
            }else{
                if (groupUser.equals("GSC"))
                {
                    response = serverRequest.sendGetRequest(ServerRequest.urlGroupSC+Username);
                }
                else if (groupUser.equals("SATLAK") || groupUser.equals("PRIMA"))
                {
                    response = serverRequest.sendGetRequest(ServerRequest.urlGroupAll);
                }else{
                    response = serverRequest.sendGetRequest(ServerRequest.urlGroupWaka+ master_group_id.substring(0,3) + "00"  );
                }
            }

            listDataAllCabor = processResponseCabor(response);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(100);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    populateListViewCabor();
                }
            });
        }

    }


    //MENU WELLNESS PERCENTAGE

    private void populateListViewWellnessPercentage() {
        adapterWP = new ListAdapterWellnessPercentage(AdvancedActivityAthlete.this, listDataWP);
        listViewWP.setAdapter(adapterWP);
    }

    private List<M_WellnessPercentage> processResponseWellnessPercentage(String response) {
        List<M_WellnessPercentage> list = new ArrayList<M_WellnessPercentage>();

        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            M_WellnessPercentage data = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                data = new M_WellnessPercentage();
                data.setUsername(obj.getString("username"));
                data.setWellness_rate(obj.getString("wellness_rate"));
                data.setTotal_atlet_sum(obj.getString("total_atlet_sum"));
                data.setTotal_atlet(obj.getString("total_atlet"));
                data.setTotal_percentage(obj.getString("total_percentage"));
                list.add(data);
            }
        } catch (JSONException e) {
        }

        return list;
    }


    private class GenerateWellnessPercentage extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

        @Override
        protected String doInBackground(String... params) {
            String response = serverRequest.sendGetRequest(ServerRequest.urlWellnessRate+Username);
            listDataWP = processResponseWellnessPercentage(response);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(100);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    populateListViewWellnessPercentage();
                }
            });
        }

    }


    //MENU WELLNESS PERCENTAGE



    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    void refreshDataProfile(){
        layoutDashboard.setVisibility(View.GONE);
        listViewMember.setVisibility(View.GONE);
        searchViewMember.setVisibility(View.GONE);

        if(!isNetworkAvailable()){
            webViewProfile.setVisibility(webViewProfile.GONE);
            scrollView.setVisibility(webViewProfile.GONE);
            text1.setVisibility(View.VISIBLE);
            text1.setText("Not Connected!");
        }else{
            scrollView.setVisibility(webViewProfile.VISIBLE);
            webViewProfile.setVisibility(webViewProfile.VISIBLE);
            text1.setVisibility(View.GONE);
        }

        webViewProfile.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webViewProfile.getSettings().setDomStorageEnabled(true);
        webViewProfile.getSettings().setAppCacheMaxSize(1024 * 1024 * 10);
        webViewProfile.getSettings().setAppCachePath("/data/data/" + getPackageName());
        webViewProfile.getSettings().setAllowFileAccess(true);
        webViewProfile.getSettings().setAppCacheEnabled(true);
        webViewProfile.getSettings().setJavaScriptEnabled(true);
        webViewProfile.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webViewProfile.setWebViewClient(new WebViewClientDemo());
        webViewProfile.loadUrl("http://account.iamprima.com/index.php/getin/edit/id/"+Username);
        webViewProfile.getSettings().setJavaScriptEnabled(true);
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
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), AdvancedActivityAthlete.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
        AdvancedActivityAthlete.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    /**
     * function to verify login details in mysql db
     * */
    private void changePassword(final String username,final String oldPassword_, final String newPassword_) {
        // Tag used to cancel the request
        String tag_string_req = "change_password";

        // progressDialog.setMessage("Logging in ...");
        progressDialog = new ProgressDialog(AdvancedActivityAthlete.this);
        progressDialog.setTitle("Change Password...");
        progressDialog.setMessage("Preparing user...");
        progressDialog.setIndeterminate(false);
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHANGE_PASSWORD, new Response.Listener<String>() {

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



                        // Launch main activity
                        oldPassword.setVisibility(View.GONE);
                        newPassword.setVisibility(View.GONE);
                        save.setVisibility(View.GONE);
                        btn_change_password.setVisibility(View.GONE);
                        text_change_password.setText("You Already Change Password!");
                        text_change_password.setVisibility(View.VISIBLE);

                        Toast.makeText(getApplication(), "Password Changes", Toast.LENGTH_LONG).show();

                        progressDialog.dismiss();


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
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", oldPassword_);
                params.put("newpassword", newPassword_);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    //FUNCTION UNTUK APP UPDATE
    public void executeUpdate(){
        /*
        if(isNewVersionExist()) {
            Log.i("version esists", "");

            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(APK_PATH + nameInstall)), "application/vnd.android.package-archive");
            startActivity(intent);

        } else {
            Log.i("version not esists", "");

            nameFile = APK_PATH + "iam-prima.apk";
            DownloadFile downloadFile = new DownloadFile();
            downloadFile.execute();

        }
        */

        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }

    }

    public class DownloadFile extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... sUrl) {
            try {
                URL url = new URL(urlDownload);
                Log.e("download file : ", url.toString());
                URLConnection connection = url.openConnection();
                connection.connect();
                // this will be useful so that you can show a typical 0-100% progress bar
                int fileLength = connection.getContentLength();
                Log.e("nameFile : ", nameFile);
                // download the file
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(nameFile);
                System.setProperty("http.keepAlive", "false");
                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("EROR DOWNLOAD FILE : ", e.toString());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AdvancedActivityAthlete.this);
            progressDialog.setMessage("Downloading new version");
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressDialog.setProgress(progress[0]);
        }

        protected void onPostExecute(String result) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(APK_PATH + nameInstall)), "application/vnd.android.package-archive");
            startActivity(intent);
            progressDialog.dismiss();
        }
    }

    public class GetLinkUpdate extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                // URL url = new URL("https://raw.githubusercontent.com/mobilesiri/JSON-Parsing-in-Android/master/index.html");

                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonGetUpdate/AppUpdate");

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
                JSONObject jsonObj = new JSONObject(s);
                JSONArray jsonArray = jsonObj.getJSONArray("data");
                JSONObject obj = jsonArray.getJSONObject(0);
                urlDownload = obj.getString("urlDownload");
                versionUpdate = obj.getString("versionNameUpdate");

                PackageInfo pInfo = null;
                try {
                    pInfo = AdvancedActivityAthlete.this.getPackageManager().getPackageInfo(AdvancedActivityAthlete.this.getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                versionCurrent = pInfo.versionName;

                if (Float.parseFloat(versionCurrent) < Float.parseFloat(versionUpdate)){
                    //need to update
                    APP_VERSION = 1;
                    APP_VERSION_TEXT = String.valueOf(Float.parseFloat(versionCurrent));
                } else{
                    APP_VERSION = 0;
                    APP_VERSION_TEXT = String.valueOf(Float.parseFloat(versionCurrent));
                }


            } catch (JSONException e) {
            }
//            Log.i("json", s);
        }
    }

    private boolean isNewVersionExist() {
        try {
            PackageManager pm = AdvancedActivityAthlete.this.getPackageManager();
            Float newVersion = Float.valueOf(pm.getPackageArchiveInfo(APK_PATH + nameInstall, 0).versionName);

            if (UpdateSetting.getCurrentVersion() < newVersion)
                return true;
        } catch (Exception e) {
            Log.i("ERROR", e.getStackTrace()[0].toString());
            e.printStackTrace();
        }
        return false;

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

                    sessionAccess.CreateSessionAccess(obj.getString("access_all"));
                    sessionCover.CreateSessionCover(obj.getString("cover_url"));
                    sessionQtyCidera.CreateSessionQtyCidera(obj.getString("total_cidera"));
                    sessionWellnessDate.CreateSessionWellnessDate(obj.getString("wellness_date"));
                    cover_url = obj.getString("cover_url");
                    QtyCidera = obj.getString("total_cidera");



                    /*
                    Toast.makeText(getApplicationContext(),
                            "cover_url "+cover_url, Toast.LENGTH_LONG)
                            .show();

                    Toast.makeText(getApplicationContext(),
                            "QtyCidera "+QtyCidera, Toast.LENGTH_LONG)
                            .show();
                            */

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error String Input!", Toast.LENGTH_LONG)
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
            try {
                JSONObject jsonObj = new JSONObject(s);
                JSONArray jsonArray = jsonObj.getJSONArray("data");
                JSONObject obj = jsonArray.getJSONObject(0);

                //master_group_id = obj.getString("master_group_id");
                //master_group_name = obj.getString("master_group_name");

                sessionGroupID.CreateSessionGroupID(obj.getString("master_group_id"));
                sessionGroupName.CreateSessionGroupName(obj.getString("master_group_name"));

            } catch (JSONException e) {
            }
        }
    }

    private void getImageGalery(){


        Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(pickImageIntent, RESULT_LOAD_IMG);

        /*

        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageIntent.setType("image/*");
            pickImageIntent.putExtra("crop", "true");
            pickImageIntent.putExtra("outputX", 250);
            pickImageIntent.putExtra("outputY", 250);
            pickImageIntent.putExtra("aspectX", 1);
            pickImageIntent.putExtra("aspectY", 1);
            pickImageIntent.putExtra("scale", true);
            pickImageIntent.putExtra("return-data", true);
            startActivityForResult(pickImageIntent, RESULT_LOAD_IMG);
        } else {
            startActivityForResult(getPickImageChooserIntent(), RESULT_LOAD_IMG);
        }
        */
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");

        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        //   outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        //   outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
        //Tambahan File Upload
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public String getPath_(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMG ) {

                selectedImageUri = data.getData();
                cropImage(selectedImageUri);

            } else if (requestCode == CROP_IMAGE) {


                /*
                Toast.makeText(getApplicationContext(), "outputFileUri " +outputFileUri,
                        Toast.LENGTH_LONG).show();

                Toast.makeText(getApplicationContext(), "selectedImageUri " +selectedImageUri,
                        Toast.LENGTH_LONG).show();

                Toast.makeText(getApplicationContext(), "mCurrentPhotoPath " +mCurrentPhotoPath,
                        Toast.LENGTH_LONG).show();

                        Toast.makeText(getApplicationContext(), "selectedImagePath " +selectedImagePath,
                        Toast.LENGTH_LONG).show();
                        */


                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                // imageUri = data.getData();
                selectedImagePath = imageUri.getPath();

                launchUploadActivity(true);
                // new UploadFileToServer().execute();



            } else if (requestCode == SELECT_PICTURE_CAMARA && resultCode == Activity.RESULT_OK) {
                //cropImage1();
            } else if (requestCode == LOAD_PDF ) {


                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this,selectedFileUri);
                // selectedImagePath = FilePath.getPath(this,selectedFileUri);
                // Log.i(TAG,"Selected File Path:" + selectedFilePath);

                if(selectedFilePath != null && !selectedFilePath.equals("")){
                    String filename=selectedFilePath.substring(selectedFilePath.lastIndexOf("/")+1);
                    text_1.setText(filename);
                    // launchUploadActivity(true);
                    //new UploadFilePDFToServer().execute();
                }else{
                    Toast.makeText(this,"Cannot upload file to server",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    protected boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    protected void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do your work
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    //copy sourceFile to destFile
    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source = new FileInputStream(sourceFile).getChannel();
        FileChannel destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    //file uri to real location in filesystem
    public String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void onClickGallery() {
        List<Intent> targets = new ArrayList<>();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        List<ResolveInfo> candidates = getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo candidate : candidates) {
            String packageName = candidate.activityInfo.packageName;
            if (!packageName.equals("com.google.android.apps.photos") && !packageName.equals("com.google.android.apps.plus") && !packageName.equals("com.android.documentsui")) {
                Intent iWantThis = new Intent();
                iWantThis.setType("image/*");
                iWantThis.setAction(Intent.ACTION_PICK);
                iWantThis.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                iWantThis.setPackage(packageName);
                targets.add(iWantThis);
            }
        }
        if (targets.size() > 0) {
            Intent chooser = Intent.createChooser(targets.remove(0), "Select Picture");
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targets.toArray(new Parcelable[targets.size()]));
            startActivityForResult(chooser, RESULT_LOAD_IMG);
        } else {
            Intent intent1 = new Intent(Intent.ACTION_PICK);
            intent1.setType("image/*");
            startActivityForResult(Intent.createChooser(intent1, "Select Picture"), RESULT_LOAD_IMG);
        }
    }

    private void launchUploadCameraActivity(boolean isImage){
        Intent i = new Intent(AdvancedActivityAthlete.this, UploadActivity.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("isImage", isImage);
        startActivity(i);
    }

    private void cropImage(Uri selectedImageUri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(selectedImageUri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("return-data", true);

        outputFileUri = Uri.fromFile(createCropFile());
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cropIntent, CROP_IMAGE);


    }

    private File createCropFile() {

        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        try {
            file = File.createTempFile(timeStamp, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCurrentPhotoPath = String.valueOf(Uri.fromFile(file));
        return file;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    private Drawable LoadImageFromWebOperations(String url)
    {
        try{
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        }catch (Exception e) {
            System.out.println("Exc="+e);
            return null;
        }
    }

    private void launchUploadActivity(boolean isImage){

        Intent i = new Intent(AdvancedActivityAthlete.this, UploadActivity.class);
        i.putExtra("filePath", selectedImagePath);
        i.putExtra("isImage", isImage);
        i.putExtra("fullName", nameUser);
        startActivity(i);


    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            // progressBar.setVisibility(View.VISIBLE);
            // updating progress bar value
            // progressBar.setProgress(progress[0]);
            // updating percentage value
            //  txtPercentage.setText(String.valueOf(progress[0]) + "%");

            //  if (progress[0] == 100) {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AdvancedActivityAthlete.this);
            progressDialog.setTitle("Upload Picture...");
            progressDialog.setMessage("Please wait...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
            //  }

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                File sourceFile = new File(selectedImagePath);
                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));
                // Extra parameters if you want to pass to server
                entity.addPart("username", new StringBody(Username));
                // entity.addPart("fullname", new StringBody(FullName));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            new RefreshProfile().execute();
        }

    }

    public class RefreshProfile extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.comiamprima.com/index.php/JsonRoleName/Username/" + Username);
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
                JSONObject jsonObj = new JSONObject(s);
                JSONArray jsonArray = jsonObj.getJSONArray("data");
                JSONObject obj = jsonArray.getJSONObject(0);

                sessionUrlProfile.ClearSessionUrlProfile();
                sessionUrlProfile.CreateSessionUrlProfile(obj.getString("gambar"));

                Toast.makeText(getApplicationContext(), "Picture Changed",
                        Toast.LENGTH_LONG).show();
                progressDialog.show();

                Intent home_intent = new Intent(getApplicationContext(), AdvancedActivityAthlete.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home_intent);
                AdvancedActivityAthlete.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            } catch (JSONException e) {
            }
//            Log.i("json", s);
        }
    }

    //UNTUK MENU PERIODIZATION
    private void showFileChooserPDF() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        // intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select FDP"),
                    LOAD_PDF);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(AdvancedActivityAthlete.this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class UploadFilePDFToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);
            // updating progress bar value
            progressBar.setProgress(progress[0]);
            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");

            if (progress[0] == 100) {
                txtPercentage.setVisibility(View.GONE);
            }

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                File sourceFile = new File(selectedFilePath);
                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));
                // Extra parameters if you want to pass to server
                entity.addPart("username", new StringBody(Username));
                // entity.addPart("fullname", new StringBody(FullName));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Done",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void populateSpinnerGroup() {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < groupList.size(); i++) {
            lables.add(groupList.get(i).getGroupName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, lables);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);

        spinnerGroup.setAdapter(spinnerAdapter);
        spinnerGroupWellness.setAdapter(spinnerAdapter);

        if(lables != null)
            lablesSpinner = 1;
        else
            lablesSpinner = 0;


    }

    private void populateSpinnerCategoryGroup() {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < groupCategoryList.size(); i++) {
            lables.add(groupCategoryList.get(i).getGroupName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,  R.layout.spinner_item, lables);
        spinnerAdapter .setDropDownViewResource(R.layout.spinner_item);
        spinner_group_category_wellness.setAdapter(spinnerAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(
                getApplicationContext(),
                parent.getItemAtPosition(position).toString() + " Selected" ,
                Toast.LENGTH_LONG).show();

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

    private class GenerateGroupSpinner extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AdvancedActivityAthlete.this);
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

    private class GenerateCategoryGroupSpinner extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AdvancedActivityAthlete.this);
            progressDialog.setMessage("Fetching Data...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String response = serverRequest.sendGetRequest(ServerRequest.urlGroupAccessByUser+Username);
            if (response != null) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    if (jsonObj != null) {
                        JSONArray categories = jsonObj
                                .getJSONArray("data");

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject catObj = (JSONObject) categories.get(i);
                            M_GroupAccessPrima groupCat = new M_GroupAccessPrima(
                                    catObj.getString("username"),
                                    catObj.getString("master_group_id"),
                                    catObj.getString("master_group_name"),
                                    catObj.getString("master_group_jlh_atlet"),
                                    catObj.getString("master_group_jlh_pelatih"),
                                    catObj.getString("total_ksc"),
                                    catObj.getString("total_hpd")
                            );

                            groupCategoryList.add(groupCat);
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
            populateSpinnerCategoryGroup();

        }

    }

    //UNTUK MENU PERIODIZATION



    //UNTUK CEK CIDERA ATLET

    public class getQtyCidera extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonViewCidera/CideraTodayByUser/"+Username);
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

                    sessionQtyCidera.CreateSessionQtyCidera(obj.getString("total_cidera"));


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error String Input!", Toast.LENGTH_LONG)
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


    //GENERATE LIST VIEW NYA
    private List<M_CederaToday> processResponseCidera(String response) {
        List<M_CederaToday> list = new ArrayList<M_CederaToday>();
        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            M_CederaToday data = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                data = new M_CederaToday();
                data.setUserName(obj.getString("username"));
                data.setUsernameAtlet(obj.getString("username_atlet"));
                data.setNameAtlet(obj.getString("name"));
                data.setCidera(obj.getString("cidera"));
                data.setCreatedDttmToday(obj.getString("created_dttm_today"));
                data.setCreatedDateToday(obj.getString("created_date"));
                data.setCreatedTimeToday(obj.getString("created_time"));
                data.setValueWellness(obj.getString("value_wellness"));
                data.setGambar(obj.getString("gambar"));
                data.setGroupCode(obj.getString("groupcode"));
                data.setGroupName(obj.getString("master_group_name"));
                data.setTimeOutNotif(obj.getString("time_out_notif"));
                list.add(data);
            }
        } catch (JSONException e) {
//			Log.d(TAG, e.getMessage());
        }
        return list;
    }


    private void populateListViewCideraToday() {
        adapterCideraToday = new ListAdapterCederaToday(AdvancedActivityAthlete.this, listDataCideraToday);
        listViewCideraToday.setAdapter(adapterCideraToday);


        // ketika ingin memilih data list
        listViewCideraToday.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v,
                                    int pos, long id) {
                selectedListCideraToday = (M_CederaToday) adapterCideraToday.getItem(pos);
                Intent i = new Intent(AdvancedActivityAthlete.this, DetailPersonalActivity.class);

                // String moduleURL = "http://personal.iamprima.com/index.php/login_validation/index/id/"+selectedList.getUserName();

                i.putExtra("nama_modul", "Highlight");
                // i.putExtra("url_modul", moduleURL);
                i.putExtra("member_username", selectedListCideraToday.getUsernameAtlet());
                i.putExtra("member_name", selectedListCideraToday.getNameAtlet());
                i.putExtra("member_group_code", selectedListCideraToday.getGroupCode());
                i.putExtra("member_group_name", selectedListCideraToday.getGroupName());
                startActivity(i);

            }
        });


        /*
        listViewCideraToday.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

                selectedList = (M_Member) adapter.getItem(pos);
                Intent i = new Intent(AdvancedActivity.this, ViewProfileActivity.class);
                i.putExtra("member_username", selectedListCideraToday.getUserName());
                i.putExtra("member_name", selectedListCideraToday.getName());
                i.putExtra("member_nomor_event", selectedListCideraToday.getNomorEvent());
                i.putExtra("member_role_name", selectedListCideraToday.getRoleName());
                i.putExtra("member_wellness_date", selectedListCideraToday.getWellnessDate());
                i.putExtra("member_value_wellness", selectedListCideraToday.getValueWellness());
                startActivity(i);
                return true;

            }
        });
        */
    }

    private class GenerateCideraToday extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

        @Override
        protected String doInBackground(String... params) {
            /** Mengirimkan request ke server dan memproses JSON response */
            String response = "";

            response = serverRequest.sendGetRequest(ServerRequest.urlCideraTodayByUser+Username);
            listDataCideraToday = processResponseCidera(response);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(100);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    populateListViewCideraToday();
                }
            });
        }

    }


    public class cekCideraAvailable extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonViewCidera/cekDataCideraAvailable/"+Username);
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

                    if (obj.getString("available").equals("1")){
                        listViewCideraToday.setVisibility(View.VISIBLE);
                        text1.setVisibility(View.GONE);
                    }
                    else{
                        text1.setVisibility(View.VISIBLE);
                        text1.setText("Tidak Ada Cidera");
                        listViewCideraToday.setVisibility(View.GONE);
                    }



                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error String Input!", Toast.LENGTH_LONG)
                            .show();
                }

            }catch (Exception e){
                text1.setVisibility(View.VISIBLE);
                text1.setText("Bad Connection!");
                listViewMember.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(),
                        "Bad Connection!", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    public class InsertWellnessPercentage extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonQtyWellness/GenerateWellnessSUM/"+Username);
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
            progressBar.setVisibility(View.VISIBLE);
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

                    progressBar.setVisibility(View.GONE);



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

                    progressBar.setVisibility(View.VISIBLE);


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

                    progressBar.setVisibility(View.GONE);



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

                    progressBar.setVisibility(View.GONE);

                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                        "Bad Connection!", Toast.LENGTH_LONG)
                        .show();
            }


//            Log.i("json", s);
        }
    }

    //UNTUK CEK CIDERA ATLET


    //GENERATE LOG LOGOUT
    public class insertLog_Logout extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonInsertLog/Logout/"+Username+"/"+android_device_name+"/"+version_release);
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
    //GENERATE LOG LOGOUT


    //CEK VERSION APP

    public void versionAPP(){
        PackageInfo pInfo = null;
        try {
            pInfo = AdvancedActivityAthlete.this.getPackageManager().getPackageInfo(AdvancedActivityAthlete.this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionCurrent = pInfo.versionName;
        /*
        Toast.makeText(getApplicationContext(),
                "versionCurrent "+versionCurrent, Toast.LENGTH_LONG)
                .show();
        if(Float.parseFloat(versionCurrent) < 10)
            Toast.makeText(getApplicationContext(),
                    "Version APP < 9", Toast.LENGTH_LONG)
                    .show();
                    */
    }


    public class GenerateAppUpdate extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonGetUpdate/GenerateAppUpdate/"+Username+"/"+android_device_id+"/"+android_device_name+"/"+version_release+"/"+versionCurrent);
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

                    newVersionApp = obj.getString("app_version_update");
                    sessionNewVersionApp.CreateSessionNewVersionApp(newVersionApp);

                    /*
                    Toast.makeText(getApplicationContext(),
                            "APP VERSION "+obj.getString("app_version"), Toast.LENGTH_LONG)
                            .show();

                    Toast.makeText(getApplicationContext(),
                            "APP VERSION UPDATE "+obj.getString("app_version_update"), Toast.LENGTH_LONG)
                            .show();
                            */

                    if(Float.parseFloat(versionCurrent) < Float.parseFloat(newVersionApp))
                        Toast.makeText(getApplicationContext(),
                                "New Update Available in Playstore", Toast.LENGTH_LONG)
                                .show();




                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error JSON Update!", Toast.LENGTH_LONG)
                            .show();
                }
                //            Log.i("json", s);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                        "Bad Connection!", Toast.LENGTH_LONG)
                        .show();
            }

        }
    }
    //CEK VERSION APP


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


    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(id.or.ppfi.firebase.notification.app.Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);


        /*
        if (!TextUtils.isEmpty(regId))
            Toast.makeText(getApplicationContext(), "Firebase Reg Id: " + regId, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "Firebase Reg Id is not received yet!", Toast.LENGTH_LONG).show();
        */
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(id.or.ppfi.firebase.notification.app.Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(id.or.ppfi.firebase.notification.app.Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        //  NotificationUtils.clearNotifications(getApplicationContext());

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    ///PERIODIZATION AREA
    private class GeneratePeriodisasi extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

        @Override
        protected String doInBackground(String... params) {
            /** Mengirimkan request ke server dan memproses JSON response */
            String response = "";

            response = serverRequest.sendGetRequest(ServerRequest.urlListPeriodization+Username);
            listDataPeriodisasi = processResponsePeriodisasi(response);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(100);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    populatedListPeriodization();
                }
            });
        }

    }


    private void populatedListPeriodization() {

        adapterPeriodisasi = new ListAdapterPeriodisasi(AdvancedActivityAthlete.this, listDataPeriodisasi);
        listViewPeriodisasi.setAdapter(adapterPeriodisasi);

        // ketika ingin memilih data list
        listViewPeriodisasi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v,
                                    int pos, long id) {
                selectedListPeriodisasi = (M_Periodisasi) adapterPeriodisasi.getItem(pos);
                Intent i = new Intent(AdvancedActivityAthlete.this, DetailPeriodPlanActivity.class);
                i.putExtra("planedtype", selectedListPeriodisasi.getPlannedType());
                i.putExtra("url_image", selectedListPeriodisasi.getUrlImage());
                i.putExtra("title", selectedListPeriodisasi.getTitle());
                i.putExtra("notes", selectedListPeriodisasi.getNotes());
                i.putExtra("groupname", selectedListPeriodisasi.getMaster_group_name());
                startActivity(i);
            }
        });


        /*
        listViewCideraToday.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

                selectedList = (M_Member) adapter.getItem(pos);
                Intent i = new Intent(AdvancedActivity.this, ViewProfileActivity.class);
                i.putExtra("member_username", selectedListCideraToday.getUserName());
                i.putExtra("member_name", selectedListCideraToday.getName());
                i.putExtra("member_nomor_event", selectedListCideraToday.getNomorEvent());
                i.putExtra("member_role_name", selectedListCideraToday.getRoleName());
                i.putExtra("member_wellness_date", selectedListCideraToday.getWellnessDate());
                i.putExtra("member_value_wellness", selectedListCideraToday.getValueWellness());
                startActivity(i);
                return true;

            }
        });
        */
    }

    //GENERATE LIST VIEW NYA
    private List<M_Periodisasi> processResponsePeriodisasi(String response) {
        List<M_Periodisasi> list = new ArrayList<M_Periodisasi>();
        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            M_Periodisasi data = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                data = new M_Periodisasi();
                data.setUid(obj.getString("uid"));
                data.setCreatedDate(obj.getString("createddate"));
                data.setCreatedTime(obj.getString("createdtime"));
                data.setPlannedType(obj.getString("plannedtype"));
                data.setUsername(obj.getString("username"));
                data.setGroupCode(obj.getString("groupcode"));
                data.setUrlImage(obj.getString("url_image"));
                data.setTitle(obj.getString("title"));
                data.setNotes(obj.getString("notes"));
                data.setName(obj.getString("name"));
                data.setMaster_group_name(obj.getString("master_group_name"));
                data.setUrl_profile(obj.getString("url_profile"));
                list.add(data);
            }
        } catch (JSONException e) {
//			Log.d(TAG, e.getMessage());
        }
        return list;
    }
    ///PERIODIZATION AREA




}
