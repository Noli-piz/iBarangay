package com.example.ibarangay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.ibarangay.main.Req_SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class Request extends AppCompatActivity {

    DrawerLayout drawerLayout2;
    NavigationView navigationView2;
    ActionBarDrawerToggle actionBarDrawerToggle2;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(actionBarDrawerToggle2.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        //Navigation
        drawerLayout2 = findViewById(R.id.drawer_layout2);
        navigationView2 = findViewById(R.id.navigationView2);

        //Tab
        Req_SectionsPagerAdapter sectionsPagerAdapter = new Req_SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        ExtendedFloatingActionButton fab = findViewById(R.id.fab);

        //Navigation
        actionBarDrawerToggle2 = new ActionBarDrawerToggle(this,drawerLayout2 ,R.string.menu_Open,R.string.menu_Close);
        drawerLayout2.addDrawerListener(actionBarDrawerToggle2);
        actionBarDrawerToggle2.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView2.setCheckedItem(R.id.nav_request);
        navigationView2.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_announcement:
                        Intent Iannouncement = new Intent(Request.this, Announcement.class);
                        Iannouncement.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(Iannouncement);

                        drawerLayout2.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_request:
                        drawerLayout2.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_service:
                        Intent IService = new Intent(Request.this, Service.class);
                        IService.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(IService);

                        drawerLayout2.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(Request.this);
                        builder.setMessage("Are you sure you want to logout?")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })

                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        navigationView2.setCheckedItem(R.id.nav_request);
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();

                        drawerLayout2.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });


        //Fab Action
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), RequestModule.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Request.this);
        builder.setMessage("Are you sure you want to exit?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}