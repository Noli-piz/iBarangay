package com.example.ibarangay;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.ibarangay.main.Req_SectionsPagerAdapter;
import com.example.ibarangay.main.Ser_SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class Service extends AppCompatActivity {

    DrawerLayout drawerLayout3;
    NavigationView navigationView3;
    ActionBarDrawerToggle actionBarDrawerToggle3;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(actionBarDrawerToggle3.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        //Navigation
        drawerLayout3 = findViewById(R.id.drawer_layout3);
        navigationView3 = findViewById(R.id.navigationView3);

        //Tab
        Ser_SectionsPagerAdapter sectionsPagerAdapter = new Ser_SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.service_view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.service_tab);
        tabs.setupWithViewPager(viewPager);
        ExtendedFloatingActionButton fab = findViewById(R.id.service_fab);

        //Navigation
        actionBarDrawerToggle3 = new ActionBarDrawerToggle(this,drawerLayout3 ,R.string.menu_Open,R.string.menu_Close);
        drawerLayout3.addDrawerListener(actionBarDrawerToggle3);
        actionBarDrawerToggle3.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView3.setCheckedItem(R.id.nav_service);
        navigationView3.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_announcement:
                        Intent Iannouncement = new Intent(Service.this, Announcement.class);
                        Iannouncement.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(Iannouncement);

                        drawerLayout3.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_request:
                        Intent IRequest = new Intent(Service.this, Request.class);
                        IRequest.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(IRequest);

                        drawerLayout3.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_service:


                        drawerLayout3.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(Service.this);
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

                                        navigationView3.setCheckedItem(R.id.nav_service);
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();

                        drawerLayout3.closeDrawer(GravityCompat.START);
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
                Intent intent = new Intent(getApplicationContext(), ServiceModule.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Service.this);
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

                        navigationView3.setCheckedItem(R.id.nav_service);
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}