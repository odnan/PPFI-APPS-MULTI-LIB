package id.or.ppfi.group;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

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
import java.util.Date;
import java.util.HashMap;

import id.or.ppfi.R;
import id.or.ppfi.config.AppController;
import id.or.ppfi.config.CircularImageView;
import id.or.ppfi.config.CircularNetworkImageView;
import id.or.ppfi.config.SessionManager;
import id.or.ppfi.dashboard.WebviewActivity;


/**
 * Created by emergency on 09/10/2016.
 */

public class ViewProfileActivity extends AppCompatActivity  {
    private ProgressDialog progressDialog;
    Toolbar toolbar;
    private static final int RESULT_LOAD_IMG = 1;


    TextView text1,text2;
    Button call,sms,sendMail,addContact;
    Spinner spinnerGender;

    SessionManager sessionCode;
    private String username;
    String imageNameMember;

    CircularNetworkImageView thumbNail;
    CircularImageView thumbNail2;
    EditText user_name,name_,email_,phone_,gender_;
    String urlProfileMember = "",usernameMember = "", nameMember = "",phoneNumberMember="",emailUserMember="";
    String member_username, member_name,member_nomor_event,member_role_name,member_wellness_date, member_value_wellness;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    ImageView imageWellness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Contact");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3E50B4")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        text1 = (TextView) findViewById(R.id.text_1);
        text2 = (TextView) findViewById(R.id.text_2);
        user_name = (EditText) findViewById(R.id.edit_username);
        name_ = (EditText) findViewById(R.id.edit_name);
        email_ = (EditText) findViewById(R.id.edit_email);
        phone_ = (EditText) findViewById(R.id.edit_phone);
        gender_ = (EditText) findViewById(R.id.edit_gender);
        spinnerGender = (Spinner) findViewById(R.id.spinner_gender);

        thumbNail = (CircularNetworkImageView) findViewById(R.id.thumbnail_profile);
        thumbNail2 = (CircularImageView) findViewById(R.id.thumbnail_profile_2);

        imageWellness = (ImageView) findViewById(R.id.image_wellness);

        call = (Button) findViewById(R.id.btn_call);
        sms = (Button) findViewById(R.id.btn_sms);
        sendMail = (Button) findViewById(R.id.btn_email);
        addContact = (Button) findViewById(R.id.btn_add_contact);

        sessionCode = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionCode.GetSessionKode();
        username = user.get(SessionManager.KEY_USERNAME);


        Intent data = getIntent();
        member_username = data.getStringExtra("member_username");
        member_name = data.getStringExtra("member_name");
        member_nomor_event = data.getStringExtra("member_nomor_event");
        member_role_name = data.getStringExtra("member_role_name");
        member_wellness_date = data.getStringExtra("member_wellness_date");
        member_value_wellness = data.getStringExtra("member_value_wellness");

        text1.setText(member_name);
        text2.setText(member_role_name + " | " + member_nomor_event);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateNow = df.format(new Date());

        if(!member_wellness_date.equals(dateNow)){
            imageWellness.setImageResource(R.drawable.ic_grey);
        }else{
            if(Integer.parseInt(member_value_wellness) > 0 &&
                    Integer.parseInt(member_value_wellness) <= 59){
                imageWellness.setImageResource(R.drawable.ic_red);
            }else if(Integer.parseInt(member_value_wellness) >= 60 &&
                    Integer.parseInt(member_value_wellness) <= 69){
                imageWellness.setImageResource(R.drawable.ic_orange);
            }else if(Integer.parseInt(member_value_wellness) >= 70 &&
                    Integer.parseInt(member_value_wellness) <= 79){
                imageWellness.setImageResource(R.drawable.ic_yellow);
            }else if(Integer.parseInt(member_value_wellness) >= 80 &&
                    Integer.parseInt(member_value_wellness) <= 89){
                imageWellness.setImageResource(R.drawable.ic_green);
            }else if(Integer.parseInt(member_value_wellness) >= 90 &&
                    Integer.parseInt(member_value_wellness) <= 100){
                imageWellness.setImageResource(R.drawable.ic_green_arrow);
            }
            else {
                imageWellness.setImageResource(R.drawable.ic_grey);
            }
        }

        if(isNetworkAvailable()){
            new GenerateUser().execute();
            thumbNail.setVisibility(View.VISIBLE);
            thumbNail2.setVisibility(View.GONE);
        }else{
            thumbNail2.setVisibility(View.VISIBLE);
            thumbNail.setVisibility(View.GONE);
        }

        thumbNail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(ViewProfileActivity.this, WebviewActivity.class);
                i.putExtra("nama_modul", "Profile Picture");
                i.putExtra("sub_nama_modul", member_name);
                i.putExtra("url_modul", urlProfileMember);
                startActivity(i);
                ViewProfileActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


        call.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                callPhone();
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendSMS();
            }
        });

        sendMail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendEmailContact(email_.getText().toString());
            }
        });

        addContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addContact();
            }
        });


    }

    private void sendEmailContact(String email_) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",email_, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void addContact() {

        Intent addContactIntent = new Intent(Intent.ACTION_INSERT);
        addContactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE,phoneNumberMember);
        addContactIntent.putExtra(ContactsContract.Intents.Insert.NAME, name_.getText().toString());
        addContactIntent.putExtra(ContactsContract.Intents.Insert.EMAIL, email_.getText().toString());
        startActivity(addContactIntent);

    }

    private void callPhone() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+Uri.encode(phoneNumberMember)));
        // callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(callIntent);
    }

    private void sendSMS() {
        Uri uri = Uri.parse("smsto:"+phoneNumberMember);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", "Text here...");
        startActivity(it);

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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


    public class GenerateUser extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.or.web.id/index.php/JsonRoleName/Username/"+member_username);
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

                thumbNail.setImageUrl(obj.getString("gambar"), imageLoader);
                if(obj.getString("gambar").length() > 0){
                    if(obj.getString("gambar").contains("http://portal") || obj.getString("gambar").contains("https://portal"))
                        thumbNail.setImageUrl(obj.getString("gambar").replace("portal.iamprima.com","iamprima.com/portal"), imageLoader);
                    else
                        thumbNail.setImageUrl(obj.getString("gambar"), imageLoader);
                }
                thumbNail.setDefaultImageResId(R.drawable._default);
                thumbNail.setErrorImageResId(R.drawable._corrupted);

                user_name.setText(obj.getString("username"));
                name_.setText(obj.getString("name"));
                email_.setText(obj.getString("email"));

                urlProfileMember = obj.getString("gambar");

                phoneNumberMember = obj.getString("phone");

                try{
                    if(obj.getString("phone").contains("+62"))
                        phone_.setText(obj.getString("phone").replace("+62",""));
                    else{
                        String res = obj.getString("phone").substring(1);
                        phone_.setText(res);
                    }
                } catch (Exception e) {
                    phone_.setText(phoneNumberMember);
                }


            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),
                        "Error String Input!", Toast.LENGTH_LONG)
                        .show();
            }
//            Log.i("json", s);
        }
    }

}