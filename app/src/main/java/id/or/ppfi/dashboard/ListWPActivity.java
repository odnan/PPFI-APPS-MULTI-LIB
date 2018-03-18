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
import java.util.List;

import id.or.ppfi.R;
import id.or.ppfi.config.ServerRequest;
import id.or.ppfi.entities.M_MealPlan;
import id.or.ppfi.listadapter.ListAdapterMealPlan;


/**
 * Created by emergency on 09/10/2016.
 */

public class ListWPActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private ProgressBar progressBar;
    Toolbar toolbar;
    TextView text1;
    SearchView searchView;

    ListView listView;
    private List<M_MealPlan> listData;
    private M_MealPlan selectedList;
    ListAdapterMealPlan adapter;
    ServerRequest serverRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wellness");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3E50B4")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        searchView = (SearchView) findViewById(R.id.search);
        listView = (ListView) findViewById(R.id.listview_main);

        progressBar.setVisibility(View.GONE);
        searchView.setVisibility(View.GONE);

        serverRequest = new ServerRequest();
        listData = new ArrayList<M_MealPlan>();
        new GenerateListMealPlan().execute("load");



    }

    private List<M_MealPlan> processResponse(String response) {
        List<M_MealPlan> list = new ArrayList<M_MealPlan>();
        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            M_MealPlan data = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                data = new M_MealPlan();
                data.setImageName(obj.getString("image_name"));
                data.setImageDesc(obj.getString("image_desc"));
                data.setImageURL(obj.getString("image_url"));
                data.setIsShow(obj.getString("is_show"));
                data.setUpdateAt(obj.getString("update_at"));
                data.setUpdateBy(obj.getString("update_by"));
                list.add(data);
            }
        } catch (JSONException e) {
        }
        return list;
    }

    private void populateListView() {
        adapter = new ListAdapterMealPlan(ListWPActivity.this, listData);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v,
                                    int pos, long id) {
                selectedList = (M_MealPlan) adapter.getItem(pos);

                Intent i = new Intent(ListWPActivity.this, WebviewActivity.class);
                i.putExtra("nama_modul", "Meal Plan");
                i.putExtra("sub_nama_modul", selectedList.getImageDesc());
                i.putExtra("url_modul", selectedList.getImageURL());
                startActivity(i);

            }
        });
    }

    private class GenerateListMealPlan extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

        @Override
        protected String doInBackground(String... params) {
            /** Mengirimkan request ke server dan memproses JSON response */
            String response = serverRequest.sendGetRequest(ServerRequest.urlMealPlan);
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