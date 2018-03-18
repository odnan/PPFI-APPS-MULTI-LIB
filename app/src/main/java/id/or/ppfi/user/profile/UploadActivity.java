package id.or.ppfi.user.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import java.util.HashMap;

import id.or.ppfi.R;
import id.or.ppfi.config.AndroidMultiPartEntity;
import id.or.ppfi.config.CircularImageView;
import id.or.ppfi.config.Config;
import id.or.ppfi.config.ServerRequest;
import id.or.ppfi.config.SessionManager;
import id.or.ppfi.main.AdvancedActivity;
import id.or.ppfi.main.AdvancedActivityAthlete;

import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by emergency on 09/10/2016.
 */

public class UploadActivity extends AppCompatActivity {
    // LogCat tag
    private static final String TAG = MainUploadActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private ProgressDialog pDialog, progressDialog;
    private String filePath = null;
    private TextView txtPercentage;
    //private ImageView imgPreview;
    private CircularImageView imgPreview;
    // private VideoView vidPreview;
    private Button btnUpload;
    // EditText fullName;
    long totalSize = 0;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_upload);
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgPreview = (CircularImageView) findViewById(R.id.imgPreview);
        //fullName = (EditText) findViewById(R.id.name);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        sessionCode = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionCode.GetSessionKode();
        Username = user.get(SessionManager.KEY_USERNAME);

        sessionRoleType = new SessionManager(getApplicationContext());
        HashMap<String, String> roleType = sessionRoleType.GetSessionRoleType();
        role_type = roleType.get(SessionManager.KEY_ROLE_TYPE);

        sessionUrlProfile = new SessionManager(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Preview");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3E50B4")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Receiving the data from previous activity
        Intent i = getIntent();
        // image or video path that is captured in previous activity
        filePath = i.getStringExtra("filePath");
        // boolean flag to identify the media type, image or video
        boolean isImage = i.getBooleanExtra("isImage", true);

        if (filePath != null) {
            // Displaying the image or video on the screen
            previewMedia(isImage);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new UploadFileToServer().execute();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
                progressDialog = new ProgressDialog(UploadActivity.this);
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
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

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

                Toast.makeText(getApplicationContext(), "Picture Changed",
                        Toast.LENGTH_LONG).show();
                progressDialog.show();
                if (role_type.equals("ATL")) {
                    Intent i = new Intent(getApplicationContext(), AdvancedActivityAthlete.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                    UploadActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else{
                    Intent i = new Intent(getApplicationContext(), AdvancedActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                    UploadActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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

}