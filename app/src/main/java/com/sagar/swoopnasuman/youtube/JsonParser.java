package com.sagar.swoopnasuman.youtube;


import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;

public class JsonParser {

    public String android = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=UUmFDenhA2kX5W8XGtLEhm2A&key=AIzaSyAD2LQdTihiwjiiyBLtVRp_FnXYYRNQ2CA";

    private static StringBuilder sb;
    private JSONObject jObj;
    public JsonParser(){

    }
    public JSONObject getJsonFromYoutube(String url) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        Log.e("android", this.android);

        HttpGet getRequest = new HttpGet(url);
        getRequest.setHeader("Accept", "application/json");
        getRequest.setHeader("Accept-Encoding", "gzip");
        try {
            HttpResponse response = httpclient.execute(getRequest);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                Header contentEncoding = response.getFirstHeader("Content-Encoding");
                if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                    instream = new GZIPInputStream(instream);
                }
                String result = readStream(instream);
                Log.i("JSON", result);
                instream.close();
                this.jObj = new JSONObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.jObj;
    }
    private static String readStream(InputStream is){
        BufferedReader reader;
        try{
            reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            sb= new StringBuilder();
            String line = null;
            try{
                while (( line= reader.readLine())!=null){
                    sb.append(line+"\n");

                }
            }
            catch (IOException e){
                e.printStackTrace();


            }finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch(UnsupportedEncodingException e1){
            e1.printStackTrace();
        }
        return sb.toString();
    }}
