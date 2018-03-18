package id.or.ppfi.authorization;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import id.or.ppfi.R;


/**
 * Created by emergency on 09/10/2016.
 */

public class ListAuthActivity extends AppCompatActivity  {
    private ProgressDialog progressDialog;
    Toolbar toolbar;
    Button btn_cek_role,btn_reset,btn_change_username,btn_change_access,btn_add_group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_auth);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Area");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b71c1c")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btn_cek_role = (Button) findViewById(R.id.btn_cek_role);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_change_username = (Button) findViewById(R.id.btn_change_username);
        btn_change_access = (Button) findViewById(R.id.btn_change_access);
        btn_add_group = (Button) findViewById(R.id.btn_add_group);

        btn_cek_role.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(ListAuthActivity.this, CekRoleActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                ListAuthActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });



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


}