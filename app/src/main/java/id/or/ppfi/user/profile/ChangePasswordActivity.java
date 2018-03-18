package id.or.ppfi.user.profile;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.or.ppfi.R;
import id.or.ppfi.config.AppConfig;
import id.or.ppfi.config.AppController;
import id.or.ppfi.config.SessionManager;


/**
 * Created by emergency on 09/10/2016.
 */

public class ChangePasswordActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    Toolbar toolbar;

    EditText oldPassword,newPassword,reTypePassword;
    Button save;
    TextView text_change_password;

    SessionManager sessionCode;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3E50B4")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        text_change_password = (TextView) findViewById(R.id.text_change_password);
        oldPassword = (EditText) findViewById(R.id.oldPassword);
        newPassword = (EditText) findViewById(R.id.newPassword);
        reTypePassword = (EditText) findViewById(R.id.reTypePassword);
        save = (Button) findViewById(R.id.save);

        sessionCode = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionCode.GetSessionKode();
        username = user.get(SessionManager.KEY_USERNAME);


        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                String oldPass = oldPassword.getText().toString();
                String newPass = newPassword.getText().toString();
                String rePass = reTypePassword.getText().toString();

                if(oldPass.equals("") || newPass.equals("") || rePass.equals("")){
                    Toast.makeText(getApplication(), "Please Input Field!", Toast.LENGTH_LONG).show();
                }else{
                    if(!rePass.equals(newPass))
                        Toast.makeText(getApplication(), "Re-Type Password Incorrect!", Toast.LENGTH_LONG).show();
                    else
                        changePassword(username,oldPass,newPass);
                }


            }
        });

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


    /**
     * function to verify login details in mysql db
     * */
    private void changePassword(final String username,final String oldPassword_, final String newPassword_) {
        // Tag used to cancel the request
        String tag_string_req = "change_password";

        // progressDialog.setMessage("Logging in ...");
        progressDialog = new ProgressDialog(ChangePasswordActivity.this);
        progressDialog.setTitle("Change Password...");
        progressDialog.setMessage("Preparing user...");
        progressDialog.setIndeterminate(false);
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHANGE_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Log.d(TAG, "Login Response: " + response.toString());
                progressDialog.dismiss();

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



                        Toast.makeText(getApplication(), "Password Changes", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                        goToMainActivity();
                        ChangePasswordActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);



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
                progressDialog.dismiss();
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


}