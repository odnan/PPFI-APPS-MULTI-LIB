package id.or.ppfi.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.or.ppfi.R;
import id.or.ppfi.config.ServerRequest;
import id.or.ppfi.config.SessionManager;
import id.or.ppfi.entities.M_WellnessRate;
import id.or.ppfi.listadapter.ListAdapterWellnessRate;


/**
 * Created by emergency on 09/10/2016.
 */

public class ListWellnessActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private ProgressBar progressBar;
    Toolbar toolbar;
    SearchView searchView;

    ListView listView;
    private List<M_WellnessRate> listData;
    private M_WellnessRate selectedList;
    ListAdapterWellnessRate adapter;
    ServerRequest serverRequest;

    String wellnessRate = "",allCabor = "",groupCode ="",noData="";

    private String Username,GroupName;
    SessionManager sessionCode,sessionWeekly;
    TextView text1;
    int pageCount = 1;
    View footer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_list_recycle);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wellness");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b71c1c")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        searchView = (SearchView) findViewById(R.id.search);
        listView = (ListView) findViewById(R.id.listview_main);
        text1 = (TextView) findViewById(R.id.text1);

        progressBar.setVisibility(View.GONE);
        searchView.setVisibility(View.GONE);

        sessionCode = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionCode.GetSessionKode();
        Username = user.get(SessionManager.KEY_USERNAME);

        Intent data = getIntent();
        wellnessRate = data.getStringExtra("wellnessRate");
        allCabor = data.getStringExtra("allCabor");
        groupCode = data.getStringExtra("groupCode");
        noData = data.getStringExtra("noData");



        text1.setText("");
        serverRequest = new ServerRequest();
        listData = new ArrayList<M_WellnessRate>();
        new GenerateListWellnessRate().execute("load");



       // Toast.makeText(getApplication(), " wellnessRate "+wellnessRate+" Username "+Username,Toast.LENGTH_LONG).show();

    }

    private List<M_WellnessRate> processResponse(String response) {
        List<M_WellnessRate> list = new ArrayList<M_WellnessRate>();
        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            M_WellnessRate data = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                data = new M_WellnessRate();
                data.setUsername_atlet(obj.getString("username_atlet"));
                data.setName(obj.getString("name"));
                data.setCidera(obj.getString("cidera"));
                data.setCreated_dttm_today(obj.getString("created_dttm_today"));
                data.setCreated_date(obj.getString("created_date"));
                data.setCreated_time(obj.getString("created_time"));
                data.setValue_wellness(obj.getString("value_wellness"));
                data.setGambar(obj.getString("gambar"));
                data.setGroupcode(obj.getString("groupcode"));
                data.setMaster_group_name(obj.getString("master_group_name"));
                data.setGroup_category(obj.getString("group_category"));
                data.setWellness_rate(obj.getString("wellness_rate"));
                list.add(data);
            }
        } catch (JSONException e) {
        }
        return list;
    }

    private void populateListView() {


        adapter = new ListAdapterWellnessRate(ListWellnessActivity.this, listData);
         listView.setAdapter(adapter);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v,
                                    int pos, long id) {
                selectedList = (M_WellnessRate) adapter.getItem(pos);

                Intent i = new Intent(ListWellnessActivity.this, DetailWellnessActivity.class);
                String moduleURL = "http://wellness.iamprima.com/index.php/login_validation/index/id/"+Username+"/atlet/"+selectedList.getUsername_atlet();
                i.putExtra("nama_modul", "Wellness Detail");
                i.putExtra("url_modul", moduleURL);
                i.putExtra("member_username", selectedList.getUsername_atlet());
                i.putExtra("member_name", selectedList.getName());
                i.putExtra("member_group_code", selectedList.getGroupcode());
                i.putExtra("member_group_name", selectedList.getMaster_group_name());
                startActivity(i);
                /*
                Intent i = new Intent(ListWellnessActivity.this, WebviewActivity.class);
                i.putExtra("nama_modul", "Meal Plan");
                i.putExtra("sub_nama_modul", selectedList.getImageDesc());
                i.putExtra("url_modul", selectedList.getImageURL());
                startActivity(i);
                */

            }
        });
    }

    private class GenerateListWellnessRate extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

        @Override
        protected String doInBackground(String... params) {
            /** Mengirimkan request ke server dan memproses JSON response */
            String response = "";

            if (allCabor.equals("1")){
                response = serverRequest.sendGetRequest(ServerRequest.urlWellnessRateByParamRate + Username +"/" + wellnessRate);

            }
              else{
                response = serverRequest.sendGetRequest(ServerRequest.urlWellnessRateByParamGroupRate + groupCode + "/" + wellnessRate);

            }

            listData = processResponse(response);

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


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                goToMainActivity();
                break;
            case R.id.refresh:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToMainActivity() {
        this.finish();
    }

}