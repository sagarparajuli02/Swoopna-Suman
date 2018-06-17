package com.sagar.swoopnasuman;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        android.support.v4.app.FragmentManager fm=getSupportFragmentManager();
        // FragmentManager fm =getFragmentManager();
        fm.beginTransaction().replace(R.id.frame,new HomeFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.update) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.sagar.swoopnasuman"));
            startActivity(intent);
            return true;
        }
        if (id == R.id.share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Swoopna Suman");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey , Check Out This Cool Nepali App: https://play.google.com/store/apps/details?id=com.sagar.swoopnasuman");
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
            return true;
        }
        if (id == R.id.disclaminar) {
            Intent dis = new Intent(MainActivity.this,Disclaminar.class);
            startActivity(dis);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        int id = item.getItemId();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if (id == R.id.nav_camera) {
            fm.beginTransaction().replace(R.id.frame,new HomeFragment()).commit();
        } else if (id == R.id.nav_gallery) {
            Intent photoGallery= new Intent(MainActivity.this,PhotoGallery.class);
            startActivity(photoGallery);

        } else if (id == R.id.nav_slideshow) {
            Intent i= new Intent(MainActivity.this,Playlist.class);
            i.putExtra("abc","android");
            startActivity(i);

        } else if (id == R.id.nav_manage) {
                Intent songs= new Intent(MainActivity.this,TenderActivity.class);
            startActivity(songs);
            
        } else if (id == R.id.nav_send) {
            Intent facts= new Intent(MainActivity.this,Facts.class);
            startActivity(facts);

        }
        else if (id == R.id.news) {
            fm.beginTransaction()
                    .add(new HomeFragment(),"HomeFragment")
                    .addToBackStack("HomeFragment")
                    .replace(R.id.frame,new News())
                    .commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
