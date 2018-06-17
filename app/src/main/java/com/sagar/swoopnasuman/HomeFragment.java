package com.sagar.swoopnasuman;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.app_name);
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        ImageView fb=(ImageView)view.findViewById(R.id.facebook);
        ImageView in=(ImageView)view.findViewById(R.id.instagram);
        ImageView yt=(ImageView)view.findViewById(R.id.youtube);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.facebook.com/Swoopnasuman/"));
                startActivity(i);

            }
        });
        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Intent.ACTION_VIEW);
                in.setData(Uri.parse("https://www.instagram.com/swoopnasumanofficial/"));
                startActivity(in);

            }
        });
        yt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent y = new Intent(Intent.ACTION_VIEW);
                y.setData(Uri.parse("https://www.youtube.com/channel/UCmFDenhA2kX5W8XGtLEhm2A"));
                startActivity(y);

            }
        });

        return  view;
    }


}
