package id.or.ppfi.carousel.menu;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.or.ppfi.R;
import id.or.ppfi.carousel.entities.Member;
import id.or.ppfi.carousel.listadapter.ListAdapterMember;
import id.or.ppfi.config.ServerRequest;

//import android.widget.SearchView;


/**
 * Created by emergency on 09/10/2016.
 */

public class MemberActivity extends AppCompatActivity implements OnQueryTextListener {
    Toolbar toolbar;
    TextView textView;
    private ListView listView;
    private ProgressDialog progressDialog;
    private ServerRequest serverRequest;
    private List<Member> list;
    private Member selectedList;
    ListAdapterMember adapter;
    SearchView searchView;
    EditText inputSearch;

    //private DBManagerKepwal dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_perda);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Membership");
       //
        // getSupportActionBar().setSubtitle("keanggotan pelatih fisik terregistrasi");
      //  getSupportActionBar().setIcon(R.drawable.ic_logo_siaph);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listview_main);
        searchView = (SearchView) findViewById(R.id.search);
       // inputSearch = (EditText) findViewById(R.id.inputSearch);

        /*
        dbManager = new DBManagerKepwal(this);
        dbManager.open();
        */


        serverRequest = new ServerRequest();
        list = new ArrayList<Member>();
        new MainActivityAsync().execute("load");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                // When user changed the Text
                adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        */


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                goToMainActivity();
                break;
            case R.id.home:
                goToMainActivity();
                break;
            case R.id.refresh:
                serverRequest = new ServerRequest();
                //list = new ArrayList<Member>();
                new MainActivityAsync().execute("load");
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void goToMainActivity() {
        this.finish();
    }

    private List<Member> processResponse(String response) {
        List<Member> list = new ArrayList<Member>();
        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            Member data = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                data = new Member();
                data.setMember_id(obj.getString("member_id"));
                data.setName(obj.getString("name"));
                data.setLevel(obj.getString("level"));
                data.setExperience(obj.getString("experience"));
                data.setUrl_image(obj.getString("url_image"));

                list.add(data);
            }
        } catch (JSONException e) {
        }
        return list;
    }

    private void populateListView() {
        adapter = new ListAdapterMember(MemberActivity.this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v,
                                    int pos, long id) {
                selectedList = (Member) adapter.getItem(pos);

                /*
                Intent in = new Intent(PerdaActivity.this, PerdaActivityOff.class);
                in.putExtra("file_name_server", selectedList.getFile_name_server());
                startActivity(in);
                */

                //Intent in = new Intent(MemberActivity.this, DetailKepwal.class);

                //startActivity(in);



            }
        });

        /*
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                selectedList = (Member) adapter.getItem(pos);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MemberActivity.this);
                alertDialog.setTitle("Tambah Data");
                alertDialog.setMessage("Data ini akan disimpan?");
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbManager.insert("Produk Hukum Daerah No : " + selectedList.getNo_produk_hukum(), selectedList.getTentang(), selectedList.getTahun_pembuatan(), selectedList.getName(),  selectedList.getFile_name_server());
                        Toast.makeText(MemberActivity.this, "Data Tersimpan...", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
                return true;

            }
        });
        */
    }

    public void startDownload(String urlDownload, String fileName) {
        DownloadManager mManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request mRqRequest = new DownloadManager.Request(Uri.parse(urlDownload));
        mRqRequest.setDescription("processing download");
        mRqRequest.setDestinationUri(Uri.parse("give your local path"));
        mRqRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        long idDownLoad=mManager.enqueue(mRqRequest);
    }



    private class MainActivityAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MemberActivity.this);
//			progressDialog.setTitle("Processing...");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(false);
//			progressDialog.setButton("Close", new DialogInterface.OnClickListener()
//			 {
//				 public void onClick(DialogInterface dialog, int which)
//				 {
//					 progressDialog.dismiss();
//					 }
//				 });
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            /** Mengirimkan request ke server dan memproses JSON response */
            String response = serverRequest.sendGetRequest(ServerRequest.urlGetMember);
            list = processResponse(response);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    populateListView();
                }
            });
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.getFilter().filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


}