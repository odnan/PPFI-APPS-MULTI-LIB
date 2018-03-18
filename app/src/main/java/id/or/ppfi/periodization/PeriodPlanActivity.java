package id.or.ppfi.periodization;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import id.or.ppfi.R;
import id.or.ppfi.config.AndroidMultiPartEntity;
import id.or.ppfi.config.Config;
import id.or.ppfi.config.FilePath;
import id.or.ppfi.config.SQLiteHandler;
import id.or.ppfi.config.ServerRequest;
import id.or.ppfi.config.SessionManager;
import id.or.ppfi.entities.M_GroupPrima;
import id.or.ppfi.main.AdvancedActivityAthlete;
import id.or.ppfi.sqlite.group.DBManagerGroup;
import id.or.ppfi.user.profile.MainUploadActivity;

import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by emergency on 09/10/2016.
 */

public class PeriodPlanActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // LogCat tag
    private static final String TAG = MainUploadActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private ProgressDialog pDialog, progressDialog;
    private String filePath = null;
    //private ImageView imgPreview;
    private ImageView imgPreview;
    // private VideoView vidPreview;
    private Button btnUpload;
    // EditText fullName;
    private String FullName;

    private ServerRequest serverRequest;
    private String gambar, Username,role_type;

    private int replyCode;

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";

    Button btn_submit;

    Toolbar toolbar;
    String newPath;

    SessionManager session, sessionCode, sessionUrlProfile, sessionNameUser,
            sessionGroupUser, sessionRoleName, sessionGroupID, sessionGroupName,
            sessionRoleType;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    EditText editTitle,editNotes;
    RadioGroup radioGroup;
    TextView textHint;

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int RESULT_LOAD_IMG = 1;

    private File file = null;
    private Uri outputFileUri;
    String mCurrentPhotoPath;
    private Uri selectedImageUri;
    private String selectedImagePath;
    private String selectedFilePath;
    TextView txtPercentage;
    private Uri fileUri;
    private static final int SELECT_PICTURE_CAMARA = 101, SELECT_PICTURE = 201, CROP_IMAGE = 301,LOAD_PDF = 302;

    long totalSize = 0;

    private Spinner spinnerGroup;
    private ArrayList<M_GroupPrima> groupList;

    private SQLiteHandler db;
    DBManagerGroup dbManager;
    private static int SPLASH_TIME_OUT = 5000;
    List<String> groupListLocal = new ArrayList<String>();

    TextView text1_alias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodization);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Periodization");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b71c1c")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        editTitle = (EditText) findViewById(R.id.edit_title);
        editNotes = (EditText) findViewById(R.id.edit_notes);
        btn_submit = (Button) findViewById(R.id.btnSubmit);
        textHint = (TextView) findViewById(R.id.text_hint);
        text1_alias = (TextView) findViewById(R.id.text_1);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb=(RadioButton)findViewById(checkedId);

                if(rb.getText().equals("Makro"))
                    textHint.setText("MAKRO");
                else if(rb.getText().equals("Mikro"))
                    textHint.setText("MIKRO");
                else
                    textHint.setText("MESO");


            }
        });

        sessionCode = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionCode.GetSessionKode();
        Username = user.get(SessionManager.KEY_USERNAME);

        sessionRoleType = new SessionManager(getApplicationContext());
        HashMap<String, String> roleType = sessionRoleType.GetSessionRoleType();
        role_type = roleType.get(SessionManager.KEY_ROLE_TYPE);

        sessionUrlProfile = new SessionManager(getApplicationContext());

        spinnerGroup = (Spinner) findViewById(R.id.spinner_group);
        spinnerGroup.setOnItemSelectedListener(this);

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

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, lables);
            spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
            spinnerGroup.setAdapter(spinnerAdapter);

        }else{
            serverRequest = new ServerRequest();
            groupList = new ArrayList<M_GroupPrima>();
            new GenerateGroupSpinner().execute();
        }

        /*
        serverRequest = new ServerRequest();
        groupList = new ArrayList<M_GroupPrima>();
        new GenerateGroupSpinner().execute();
        */

        imgPreview.setOnClickListener(new View.OnClickListener() {
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


        btn_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(text1_alias.getText().toString().equals("ALL"))
                    Toast.makeText(getApplicationContext(), "Choose Group!", Toast.LENGTH_SHORT).show();
                else
                    new UploadFileToServer().execute();
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //GroupCodeTemp = parent.getItemAtPosition(position).toString();
        //Toast.makeText(getApplicationContext(),groupList.get(position).getGroupCode() + " Selected" ,Toast.LENGTH_LONG).show();
        //IF NOT GROUPCODE, THIS STRING WILL BE "ALL"
        if (spinnerGroup.getItemAtPosition(position).toString().contains("(")){
            try{
                text1_alias.setText(groupListLocal.get(position));
            }catch (Exception e){
                text1_alias.setText("ALL");
            }
        }else{
            text1_alias.setText(groupList.get(position).getGroupCode());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMG ) {

                selectedImageUri = data.getData();
                cropImage(selectedImageUri);

            } else if (requestCode == CROP_IMAGE) {

                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                selectedImagePath = imageUri.getPath();

                Intent i = getIntent();
                filePath = selectedImagePath;
                boolean isImage = true;

                if (filePath != null) {
                    previewMedia(isImage);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
                }



            } else if (requestCode == SELECT_PICTURE_CAMARA && resultCode == Activity.RESULT_OK) {
                //cropImage1();
            } else if (requestCode == LOAD_PDF ) {
                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this,selectedFileUri);
                // selectedImagePath = FilePath.getPath(this,selectedFileUri);
                // Log.i(TAG,"Selected File Path:" + selectedFilePath);

                if(selectedFilePath != null && !selectedFilePath.equals("")){
                    String filename=selectedFilePath.substring(selectedFilePath.lastIndexOf("/")+1);
                   // text_1.setText(filename);
                    // launchUploadActivity(true);
                    //new UploadFilePDFToServer().execute();
                }else{
                    Toast.makeText(this,"Cannot upload file to server",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void cropImage(Uri selectedImageUri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(selectedImageUri, "image/*");
        cropIntent.putExtra("crop", "true");
       // cropIntent.putExtra("aspectX", 1);
       // cropIntent.putExtra("aspectY", 1);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    /**
     * Displaying captured image/video on the screen
     */
    private void previewMedia(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {
            imgPreview.setVisibility(View.VISIBLE);
            //vidPreview.setVisibility(View.GONE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;


            Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
            imgPreview.setImageBitmap(myBitmap);

            //final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            // imgPreview.setImageBitmap(bitmap);

            /*
            File file = new File(filePath);
            try {
                InputStream ims = new FileInputStream(file);
                imgPreview.setImageBitmap(BitmapFactory.decodeStream(ims));
            } catch (FileNotFoundException e) {
                return;
            }

*/
            newPath = filePath;

            /*
            int width = myBitmap.getWidth();
            int height = myBitmap.getHeight();
            int newWidth = (height > width) ? width : height;
            int newHeight = (height > width) ? height - (height - width) : height;
            int cropW = (width - height) / 2;
            cropW = (cropW < 0) ? 0 : cropW;
            int cropH = (height - width) / 2;
            cropH = (cropH < 0) ? 0 : cropH;
            Bitmap cropImg = Bitmap.createBitmap(myBitmap, cropW, cropH, newWidth, newHeight);
            imgPreview.setImageBitmap(cropImg);

            Uri tempUri = getImageUri(getApplicationContext(), cropImg);



            // Toast.makeText(getApplication(), getRealPathFromURI(tempUri),Toast.LENGTH_LONG).show();


            newPath = getRealPathFromURI(tempUri);
            */


            /*
            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            imgPreview.setImageBitmap(bitmap);
            */
        } else {
            imgPreview.setVisibility(View.GONE);
            //vidPreview.setVisibility(View.VISIBLE);
            //vidPreview.setVideoPath(filePath);
            // start playing
            //vidPreview.start();
        }
    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Upload Page") // TODO: Define a title for the content shown.
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



    /**
     * Uploading the file to server
     */
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
            progressBar.setVisibility(View.VISIBLE);
            // updating progress bar value
            progressBar.setProgress(progress[0]);
            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");

            if (progress[0] == 100) {
                super.onPreExecute();
                progressDialog = new ProgressDialog(PeriodPlanActivity.this);
                progressDialog.setTitle("Generate Data...");
                progressDialog.setMessage("Preparing user...");
                progressDialog.setIndeterminate(false);
                progressDialog.show();
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
            HttpPost httppost = new HttpPost(Config.URL_UPLOAD_PERIODISASI);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                File sourceFile = new File(newPath);
                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));
                // Extra parameters if you want to pass to server
                entity.addPart("username", new StringBody(Username));
                entity.addPart("plannedtype", new StringBody(textHint.getText().toString()));
                entity.addPart("title", new StringBody(editTitle.getText().toString()));
                entity.addPart("notes", new StringBody(editNotes.getText().toString()));
                entity.addPart("username", new StringBody(Username));
                entity.addPart("groupcode", new StringBody(text1_alias.getText().toString()));
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
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonRoleName/Username/" + Username);
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
                /*
                urlProfile = obj.getString("gambar");
                nameUser = obj.getString("name");
                groupUser = obj.getString("role_name");
                */


                sessionUrlProfile.ClearSessionUrlProfile();
                sessionUrlProfile.CreateSessionUrlProfile(obj.getString("gambar"));

                Toast.makeText(getApplicationContext(), "Task Success",
                        Toast.LENGTH_LONG).show();

                progressDialog.show();
                if (role_type.equals("ATL")) {
                    Intent i = new Intent(getApplicationContext(), AdvancedActivityAthlete.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                    PeriodPlanActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else{
                    //Intent i = new Intent(getApplicationContext(), AdvancedActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //startActivity(i);
                    finish();
                    PeriodPlanActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }

                /*
                HashMap<String, String> urlP = sessionUrlProfile.GetSessionUrlProfile();
                urlProfile = urlP.get(SessionManager.KEY_URL_PROFILE);
                */


                // Toast.makeText(getApplication(), "urlProfile "+urlProfile, Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
            }
//            Log.i("json", s);
        }
    }

    /**
     * Method to show alert dialog
     */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                        finish();
                    }
                });
        AlertDialog alert = builder.create();

        alert.show();
    }


    //CREATE SPINNER GROUP
    private class GenerateGroupSpinner extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PeriodPlanActivity.this);
            progressDialog.setMessage("Loading Group...");
            progressDialog.setCancelable(false);
//            progressDialog.show();

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
            populateSpinnerGroup();
        }

    }

    private void populateSpinnerGroup() {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < groupList.size(); i++) {
            lables.add(groupList.get(i).getGroupName());
            dbManager.insert(groupList.get(i).getGroupCode(), groupList.get(i).getGroupName(), groupList.get(i).getQtyAtlet());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, lables);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerGroup.setAdapter(spinnerAdapter);

    }

}