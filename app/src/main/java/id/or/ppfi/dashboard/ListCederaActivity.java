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
import id.or.ppfi.entities.M_CederaToday;
import id.or.ppfi.listadapter.ListAdapterCederaToday;


/**
 * Created by emergency on 09/10/2016.
 */

public class ListCederaActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private ProgressBar progressBar;
    Toolbar toolbar;

    ServerRequest serverRequest;

    String wellnessRate = "",allCabor = "",groupCode ="",highlight,group_name,noData;

    private String Username,GroupName;
    SessionManager sessionCode,sessionWeekly;

    SearchView searchViewCideraToday;
    private List<M_CederaToday> listDataCideraToday;
    private M_CederaToday selectedListCideraToday;
    ListAdapterCederaToday adapterCideraToday;
    ListView listViewCideraToday;
    TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_list_recycle);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cedera");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b71c1c")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        searchViewCideraToday = (SearchView) findViewById(R.id.search);
        listViewCideraToday = (ListView) findViewById(R.id.listview_main);
        text1 = (TextView) findViewById(R.id.text1);

        progressBar.setVisibility(View.GONE);
        searchViewCideraToday.setVisibility(View.GONE);

        sessionCode = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionCode.GetSessionKode();
        Username = user.get(SessionManager.KEY_USERNAME);

        Intent data = getIntent();
        wellnessRate = data.getStringExtra("wellnessRate");
        allCabor = data.getStringExtra("allCabor");
        groupCode = data.getStringExtra("groupCode");
        highlight = data.getStringExtra("highlight");
        group_name = data.getStringExtra("group_name");
        noData = data.getStringExtra("noData");

        try {
            if(highlight.equals("1"))
                getSupportActionBar().setSubtitle(group_name);
            else
                getSupportActionBar().setSubtitle(GroupName);
        }catch (Exception e){
            getSupportActionBar().setSubtitle(GroupName);
        }



        serverRequest = new ServerRequest();
        listDataCideraToday = new ArrayList<M_CederaToday>();

        if(noData.equals("1")){
            text1.setText("No Data");
        }else{
            text1.setText("");
            if(allCabor.equals("1"))
                new GenerateCideraToday().execute("load");
            else
                new GenerateCideraTodayByGroup().execute("load");
        }

        /*
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
*/


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
        adapterCideraToday = new ListAdapterCederaToday(ListCederaActivity.this, listDataCideraToday);
        listViewCideraToday.setAdapter(adapterCideraToday);


        // ketika ingin memilih data list
        listViewCideraToday.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v,
                                    int pos, long id) {
                selectedListCideraToday = (M_CederaToday) adapterCideraToday.getItem(pos);
                Intent i = new Intent(ListCederaActivity.this, DetailPersonalActivity.class);

                // String moduleURL = "http://personal.awd.web.id/index.php/login_validation/index/id/"+selectedList.getUserName();

                i.putExtra("nama_modul", "Highlight");
                // i.putExtra("url_modul", moduleURL);
                i.putExtra("member_username", selectedListCideraToday.getUsernameAtlet());
                i.putExtra("member_name", selectedListCideraToday.getNameAtlet());
                i.putExtra("member_group_code", selectedListCideraToday.getGroupCode());
                i.putExtra("member_group_name", selectedListCideraToday.getGroupName());
                startActivity(i);

            }
        });

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

    private void populateListViewCideraTodayByGroup() {
        adapterCideraToday = new ListAdapterCederaToday(ListCederaActivity.this, listDataCideraToday);
        listViewCideraToday.setAdapter(adapterCideraToday);


        // ketika ingin memilih data list
        listViewCideraToday.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v,
                                    int pos, long id) {
                selectedListCideraToday = (M_CederaToday) adapterCideraToday.getItem(pos);
                Intent i = new Intent(ListCederaActivity.this, DetailPersonalActivity.class);

                // String moduleURL = "http://personal.awd.web.id/index.php/login_validation/index/id/"+selectedList.getUserName();

                i.putExtra("nama_modul", "Highlight");
                // i.putExtra("url_modul", moduleURL);
                i.putExtra("member_username", selectedListCideraToday.getUsernameAtlet());
                i.putExtra("member_name", selectedListCideraToday.getNameAtlet());
                i.putExtra("member_group_code", selectedListCideraToday.getGroupCode());
                i.putExtra("member_group_name", selectedListCideraToday.getGroupName());
                startActivity(i);

            }
        });

    }

    private class GenerateCideraTodayByGroup extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

        @Override
        protected String doInBackground(String... params) {
            /** Mengirimkan request ke server dan memproses JSON response */
            String response = "";

            response = serverRequest.sendGetRequest(ServerRequest.urlCideraByUserGroup+Username+"/"+groupCode);
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
                    populateListViewCideraTodayByGroup();
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