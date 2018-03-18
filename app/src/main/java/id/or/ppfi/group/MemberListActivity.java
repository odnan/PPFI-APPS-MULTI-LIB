package id.or.ppfi.group;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.or.ppfi.R;
import id.or.ppfi.config.SQLiteHandler;
import id.or.ppfi.config.ServerRequest;
import id.or.ppfi.config.SessionManager;
import id.or.ppfi.dashboard.DetailPersonalActivity;
import id.or.ppfi.entities.M_Member;
import id.or.ppfi.listadapter.ListAdapterMember;


/**
 * Created by emergency on 09/10/2016.
 */

public class MemberListActivity extends AppCompatActivity implements android.widget.SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {
    private ProgressDialog pDialog;
    Toolbar toolbar;
    String groupCode,groupName;
    ListView listViewMember,listViewAllCabor;

    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    private SQLiteHandler db;

    private ServerRequest serverRequest;
    private List<M_Member> listDataMember;
    private M_Member selectedList;
    ListAdapterMember adapter;
    SearchView searchViewMember;
    TextView text1;

    String Username;
    SessionManager sessionCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_list);

        sessionCode = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionCode.GetSessionKode();
        Username = user.get(SessionManager.KEY_USERNAME);

        searchViewMember = (SearchView) findViewById(R.id.search);
        listViewMember = (ListView) findViewById(R.id.listview_main);
        text1 = (TextView) findViewById(R.id.text1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        Intent data = getIntent();
        groupCode = data.getStringExtra("group_code");
        groupName = data.getStringExtra("group_name");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(groupName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        refreshData();
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

    void refreshData(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new cekMemberAvailable().execute();
            }
        }, 1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                serverRequest = new ServerRequest();
                listDataMember = new ArrayList<M_Member>();
                new GenerateListMember().execute("load");
            }
        }, 1000);

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


        /*
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateNow = df.format(new Date());
        Toast.makeText(getApplication(), "dateNow Format "+dateNow, Toast.LENGTH_LONG).show();
        */


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                goToMainActivity();
                break;
            case R.id.refresh:
                refreshData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToMainActivity() {
        this.finish();
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
        adapter = new ListAdapterMember(MemberListActivity.this, listDataMember);
        listViewMember.setAdapter(adapter);
        listViewMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v,
                                    int pos, long id) {
                selectedList = (M_Member) adapter.getItem(pos);

                Intent i = new Intent(MemberListActivity.this, DetailPersonalActivity.class);


                String moduleURL = "http://account.iamprima.com/index.php/login_validation/index/id/"+Username+"/atlet/"+selectedList.getUserName();
                i.putExtra("nama_modul", "Highlight");
                i.putExtra("url_modul", moduleURL);
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
                Intent i = new Intent(MemberListActivity.this, ViewProfileActivity.class);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
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
            String response = serverRequest.sendGetRequest(ServerRequest.urlGroupMember + groupCode);
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


    public class cekMemberAvailable extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                URL url = new URL("http://masterdata.iamprima.com/index.php/JsonViewMember/cekMemberAvailable/"+groupCode);
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
                        listViewMember.setVisibility(View.VISIBLE);
                        text1.setVisibility(View.GONE);
                    }
                    else{
                        text1.setVisibility(View.VISIBLE);
                        text1.setText("Athlete not available");
                        listViewMember.setVisibility(View.GONE);
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


}