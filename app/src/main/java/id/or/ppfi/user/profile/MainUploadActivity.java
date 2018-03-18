package id.or.ppfi.user.profile;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import id.or.ppfi.R;
import id.or.ppfi.config.AppController;
import id.or.ppfi.config.CircularNetworkImageView;
import id.or.ppfi.config.Config;
import id.or.ppfi.config.ServerRequest;
import id.or.ppfi.config.SessionManager;


/**
 * Created by emergency on 09/10/2016.
 */

public class MainUploadActivity extends AppCompatActivity {
    private ProgressDialog pDialog, progressDialog;

    // LogCat tag
    private static final String TAG = MainUploadActivity.class.getSimpleName();

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int RESULT_LOAD_IMG = 1;
    private String selectedImagePath;

    private ServerRequest serverRequest;
    private String gambar,Username,UserFullName;
    SessionManager sessionCode,sessionUserFullName;

    String imgDecodableString;

    EditText fullName;
    private Uri fileUri; // file url to store image/video
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_main);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        //set action bar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // changing action bar color
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#212121")));
//      		Drawable drawable = LoadImageFromWebOperations(travelImage);
//      		getActionBar().setIcon(drawable);
        actionBar.setTitle("Change Picture");

//        if (imageLoader == null)
//			imageLoader = AppController.getInstance().getImageLoader();
//		 NetworkImageView thumbNail = (NetworkImageView) findViewById(R.id.thumbnail);

        fullName = (EditText) findViewById(R.id.name);
        CircularNetworkImageView imageView1 = (CircularNetworkImageView) findViewById(R.id.imageView1);
//        ImageView  imageView1 = (ImageView) findViewById(R.id.imageView1);
        ImageView imageNameChange = (ImageView) findViewById(R.id.imgNameChange);

        serverRequest = new ServerRequest();

        sessionCode = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionCode.GetSessionKode();
        Username = user.get(SessionManager.KEY_USERNAME);


        sessionUserFullName = new SessionManager(getApplicationContext());
        HashMap<String, String> userFName = sessionUserFullName.GetSessionUserFullName();
        UserFullName = userFName.get(SessionManager.KEY_USER_FULLNAME);
        fullName.setText(UserFullName);

        String response = serverRequest.sendGetRequest(ServerRequest.urlGetRoleName + Username);
        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray jsonArray = jsonObj.getJSONArray("data");
           // Log.d(TAG, "data lengt: " + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                gambar = obj.getString("gambar");
            }
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }

        //bikin dari url
        Drawable drawable = LoadImageFromWebOperations(gambar);
        //bikin circle
        Bitmap bm = ((BitmapDrawable) drawable).getBitmap();
//		Bitmap circleBitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
//		BitmapShader shader = new BitmapShader (bm,  TileMode.CLAMP, TileMode.CLAMP);
//		Paint paint = new Paint();
//		paint.setShader(shader);
//		Canvas c = new Canvas(circleBitmap);
//		c.drawCircle(bm.getWidth()/2, bm.getHeight()/2, bm.getWidth()/2, paint);

        try {
            imageView1.setImageUrl(gambar, imageLoader);
        } catch (Exception e) {
            imageView1.setImageUrl(gambar, imageLoader);
//			imageView1.setImageBitmap(bm);
        } finally {
//			imageView1.setImageUrl("", imageLoader);
            imageView1.setImageUrl("", imageLoader);
        }

        Button btnGalery = (Button) findViewById(R.id.btn_galery);
        Button btnCamera = (Button) findViewById(R.id.btn_camera);

        btnGalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // capture picture
                getImageGalery();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });

        imageNameChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent i = new Intent(MainUploadActivity.this, ChangeNameActivity.class);
               // i.putExtra("fullName", UserFullName);
               // startActivity(i);
            }
        });

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_simple, menu);

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
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Launching camera app to capture image
     */
    private void getImageGalery() {

//    	Intent intent = new Intent();

        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(intent, RESULT_LOAD_IMG);


//    	  intent.setType("image/*");
//    	  intent.setAction(Intent.ACTION_GET_CONTENT);
//	        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//	        intent.putExtra(selectedImagePath, fileUri);
//    	  startActivityForResult(Intent.createChooser(intent, "Select Picture"),RESULT_LOAD_IMG);


//    	Intent intent = new Intent(Intent.ACTION_PICK,
//				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent

        // External sdcard location
//        File mediaStorageDir = new File(
//                Environment
//                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                Config.IMAGE_DIRECTORY_NAME);
//    	// Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
//                Locale.getDefault()).format(new Date());
//        File mediaFile;
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "IMG_" + timeStamp + ".jpg");

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

//        intent.putExtra(mediaStorageDir.getPath(), fileUri);

//        startActivityForResult(intent, RESULT_LOAD_IMG);
        // start the image capture Intent
//        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // successfully captured the image
                // launching upload activity
                launchUploadCameraActivity(true);


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }else  if (requestCode == RESULT_LOAD_IMG) {
            if (resultCode == RESULT_OK
                    && null != data) {

                fileUri = data.getData();
                selectedImagePath = getPath(fileUri);
//                System.out.println("Image Path : " + selectedImagePath);

                // successfully captured the image
                // launching upload activity
                launchUploadActivity(true);


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
//                Toast.makeText(getApplicationContext(),
//                        "User cancelled image capture", Toast.LENGTH_SHORT)
//                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }


    private void launchUploadActivity(boolean isImage){
        Intent i = new Intent(MainUploadActivity.this, UploadActivity.class);
//        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("filePath", selectedImagePath);
//    	String Fpath = selectedImagePath ;
//    	File file = new File(Fpath);
//    	i.putExtra("filename", file.getName());
        i.putExtra("isImage", isImage);
        i.putExtra("fullName", UserFullName);
        startActivity(i);
        goToMainActivity();
    }

    private void launchUploadCameraActivity(boolean isImage){
        Intent i = new Intent(MainUploadActivity.this, UploadActivity.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("isImage", isImage);
        startActivity(i);
    }

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
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

}