package com.sagar.swoopnasuman;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TenderActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    TextView tvRQPoint;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<TenderUtils> tenderUtilsList;
    boolean internet_connection(){
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager)TenderActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    RequestQueue rq;

    String request_url = "https://celibate-acid.000webhostapp.com/lyris.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tender);



tvRQPoint=(TextView)findViewById(R.id.textview);
        tvRQPoint.postDelayed(new Runnable() {
                                  public void run() {
                                      tvRQPoint.setVisibility(View.INVISIBLE);
                                  }
                              }, 2200);

        setTitle("Lyrics");
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);;
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        if(!internet_connection()){
            Toast.makeText(TenderActivity.this, "no connection", Toast.LENGTH_SHORT).show();
        }

        else {

            rq = Volley.newRequestQueue(this);


            recyclerView = (RecyclerView) findViewById(R.id.recycleViewContainer);
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(this);

            recyclerView.setLayoutManager(layoutManager);

            tenderUtilsList = new ArrayList<>();


            sendRequest();



        }


    }


    public void sendRequest(){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for(int i = 0; i < response.length(); i++){

                    TenderUtils tenderUtils = new TenderUtils();

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        tenderUtils.setPersonFirstName(jsonObject.getString("title"));
                        tenderUtils.setJobProfile(jsonObject.getString("description"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    tenderUtilsList.add(tenderUtils);

                }

                mAdapter = new CustomRecyclerAdapterTender(TenderActivity.this, tenderUtilsList);

                recyclerView.setAdapter(mAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        rq.add(jsonArrayRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home);
        finish();
        return super.onOptionsItemSelected(item);
    }

}
