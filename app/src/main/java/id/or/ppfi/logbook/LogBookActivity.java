package id.or.ppfi.logbook;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import id.or.ppfi.R;
import id.or.ppfi.config.ServerRequest;
import id.or.ppfi.config.SessionManager;
import id.or.ppfi.entities.M_Provinsi;
import id.or.ppfi.main.AdvancedActivity;


public class LogBookActivity extends Activity{
    //private static final String TAG = RegisterActivity.class.getSimpleName();

    private ProgressBar progressBar,progressBarList;
    private ProgressDialog progressDialog;

    private String android_device_id;
    private String android_device_name;
    private String version_release;

    ServerRequest serverRequest;
    DatePickerDialog datePickerDialog;

    private Spinner spinnerProvince;
    // array list for spinner adapter
    private ArrayList<M_Provinsi> provinceList;

    SessionManager sessionCode,sessionFullName,sessionUrlProfile,sessionGroupUser,sessionRoleName,sessionGroupID,
            sessionGroupName,sessionRoleType;

    EditText kategori,detail;
    Button btnSubmit;
    TextView btnChoose;

    private static final int SELECT_PICTURE_CAMARA = 101, SELECT_PICTURE = 201, CROP_IMAGE = 301,LOAD_PDF = 302;


    Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logbook);

        kategori = (EditText) findViewById(R.id.edit_text_1);
        detail = (EditText) findViewById(R.id.edit_text_2);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnChoose = (TextView) findViewById(R.id.btn_choose_file);

        sessionCode = new SessionManager(getApplication());
        sessionFullName = new SessionManager(getApplication());
        sessionUrlProfile = new SessionManager(getApplicationContext());
        sessionFullName = new SessionManager(getApplicationContext());
        sessionGroupUser = new SessionManager(getApplicationContext());
        sessionRoleName = new SessionManager(getApplicationContext());
        sessionGroupID = new SessionManager(getApplicationContext());
        sessionGroupName = new SessionManager(getApplicationContext());
        sessionRoleType = new SessionManager(getApplicationContext());

        android_device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        android_device_name = Build.MANUFACTURER + " - " + Build.MODEL;
        version_release = Build.VERSION.RELEASE;
        // Progress dialog
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // Login button Click Event
        btnChoose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showFileChooserPDF();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

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


    void goHome(){
        Toast.makeText(getApplication(), "Register Success!", Toast.LENGTH_LONG).show();
        Intent login = new Intent(LogBookActivity.this,AdvancedActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        finish();
        /** Fading Transition Effect */
        LogBookActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    ///KODING KODING UNTUK UPLOAD FILE
    private void showFileChooserPDF() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        // intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select FDP"),
                    LOAD_PDF);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(LogBookActivity.this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }


}
