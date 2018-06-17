package com.sagar.swoopnasuman;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sagar.swoopnasuman.youtube.JsonParser;
import com.sagar.swoopnasuman.youtube.Video;
import com.sagar.swoopnasuman.youtube.VideoAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Playlist extends AppCompatActivity implements AdapterView.OnClickListener, AdapterView.OnItemClickListener {
    String abc = null;
    Toolbar toolbar;
    private ListView listVideo;
    private JsonParser parserVideo;
    private ProgressDialog progress;
    private Video vObject;
    private VideoAdapter videoAdapter;
    private ArrayList<Video> videoArrayList;
    boolean internet_connection(){
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager)Playlist.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        setTitle("Latest Videos");
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);;
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
      //  toolbar= (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);


        //this is fro video player fragment block
        if(!internet_connection()){
            new AlertDialog.Builder(this)
                    .setMessage("No Internet Connection")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .show();
        }

        videoArrayList = new ArrayList<Video>();
        listVideo= (ListView)findViewById(R.id.list_item);
        listVideo.setOnItemClickListener(this);
        parserVideo = new JsonParser();
        listVideo.setAdapter(videoAdapter);
   abc= getIntent().getExtras().get("abc").toString();
        new Async().execute();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }
    public void onBackPressed() {
        Intent myIntent = new Intent(Playlist.this, MainActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        finish();
        return ;
    }
    public ArrayList<Video> parsingJson(String videoUrl) {
        try {
            JSONObject json = parserVideo.getJsonFromYoutube(videoUrl);
            JSONArray jArray = new JSONArray(json.getString("items"));
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject thumbnail= jArray.getJSONObject(i);
                JSONObject snippets = thumbnail.getJSONObject("snippet");
                JSONObject defaulturl= snippets.getJSONObject("thumbnails");
                JSONObject url = defaulturl.getJSONObject("medium");
                JSONObject resourceId = snippets.getJSONObject("resourceId");
                String videoId = resourceId.getString("videoId");

                String imageurl= url.getString("url");
                String title= snippets.getString("title");
                vObject= new Video(title,imageurl,videoId);
                videoArrayList.add(i, vObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.videoArrayList;
    }

    public void onItemClick(AdapterView<?>parent , View view, int position, long id) {
        vObject = videoArrayList.get(position);
        String video = vObject.getVideoId();
        String videoTitle= vObject.getVideoTitle();
        Intent intent = new Intent(Playlist.this, FullScreenDemoActivity.class);
        intent.putExtra("video", video);
        intent.putExtra("title", videoTitle);

        intent.putExtra("abc", abc);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


        // youTubePlayerView.initialize(DeveloperKey.DEVELOPER_KEY,onInitializedListener);
    }

    @Override
    public void onClick(View view) {

    }


    public class Async extends AsyncTask<String, String, String> {
        ArrayList<Video> videolist;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(Playlist.this);
            progress.setMessage("Loading data...");
            progress.show();
        }

        protected String doInBackground(String... params) {
            try {
                if (abc.equals("android")) {
                    videolist = parsingJson(parserVideo.android);
                } else {
                    Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                finish();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                videoAdapter = new VideoAdapter(Playlist.this, this.videolist, Playlist.this.getContentResolver(), Playlist.this.getResources());
                listVideo.setAdapter(videoAdapter);
                listVideo.setFastScrollEnabled(true);
                progress.dismiss();

            } catch (Exception e) {
            }
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home);
        finish();
        return super.onOptionsItemSelected(item);
    }

}