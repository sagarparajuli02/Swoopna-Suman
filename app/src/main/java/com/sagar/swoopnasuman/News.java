package com.sagar.swoopnasuman;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class News extends Fragment {

    RecyclerView recyclerView;
    boolean internet_connection(){
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    ShimmerFrameLayout mShimmerViewContainer;
    public News() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("News");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mShimmerViewContainer = (ShimmerFrameLayout)view.findViewById(R.id.shimmer_view_container);

        //do all the important stuffs her liek on the activity :)
        recyclerView= (RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //Call Read rss asyntask to fetch rss
        if(internet_connection()) {
            new ReadsRss(getActivity(), recyclerView).execute();
        }
        else {
            Snackbar snackbar = Snackbar
                    .make(getView(), "No Internet Connection", Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }
    public class ReadsRss extends AsyncTask<Void, Void, Void> {
        Context context;
        String address = "http://feeds.feedburner.com/MusicCreativeNepal";
        ProgressDialog progressDialog;
        ArrayList<FeedItem> feedItems;
        RecyclerView recyclerView;
        URL url;

        public ReadsRss(Context context, RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            this.context = context;
            //progressDialog = new ProgressDialog(context);
            // progressDialog.setMessage("Loading...");
        }

        //before fetching of rss statrs show progress to user
        @Override
        protected void onPreExecute() {
            // progressDialog.show();
            mShimmerViewContainer.startShimmerAnimation();
            super.onPreExecute();
        }


        //This method will execute in background so in this method download rss feeds
        @Override
        protected Void doInBackground(Void... params) {
            //call process xml method to process document we downloaded from getData() method
            ProcessXml(Getdata());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // progressDialog.dismiss();
            mShimmerViewContainer.stopShimmerAnimation();
            FeedsAdapter adapter = new FeedsAdapter(context, feedItems);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);

        }

        // In this method we will process Rss feed  document we downloaded to parse useful information from it
        private void ProcessXml(Document data) {
            if (data != null) {
                feedItems = new ArrayList<>();
                Element root = data.getDocumentElement();
                Node channel = root.getChildNodes().item(1);
                NodeList items = channel.getChildNodes();
                for (int i = 0; i < items.getLength(); i++) {
                    Node cureentchild = items.item(i);
                    if (cureentchild.getNodeName().equalsIgnoreCase("item")) {
                        FeedItem item = new FeedItem();
                        NodeList itemchilds = cureentchild.getChildNodes();
                        for (int j = 0; j < itemchilds.getLength(); j++) {
                            Node cureent = itemchilds.item(j);
                            if (cureent.getNodeName().equalsIgnoreCase("title")) {
                                item.setTitle(cureent.getTextContent());
                            } else if (cureent.getNodeName().equalsIgnoreCase("description")) {
                                String desc= cureent.getTextContent();
                                item.setDescription(desc.substring(desc.indexOf("/>")+2));
                                // String imageUrl=desc.substring(desc.indexOf("src=")+5,desc.indexOf("jpg")+3)  ;
                                // item.setThumbnailUrl();


                            }

                            else if (cureent.getNodeName().equalsIgnoreCase("pubDate")) {
                                item.setPubDate(cureent.getTextContent());
                            } else if (cureent.getNodeName().equalsIgnoreCase("link")) {
                                item.setLink(cureent.getTextContent());
                            } else if (cureent.getNodeName().equalsIgnoreCase("media:thumbnail")) {
                                //this will return us thumbnail url
                                // String url = cureent.getAttributes().item(0).getTextContent();
                                //item.setThumbnailUrl(url);
                            }
                        }
                        feedItems.add(item);


                    }
                }
            }
        }

        //This method will download rss feed document from specified url
        public Document Getdata() {
            try {
                url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = builderFactory.newDocumentBuilder();
                Document xmlDoc = builder.parse(inputStream);
                return xmlDoc;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }


}
