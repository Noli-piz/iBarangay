package com.example.ibarangay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.ibarangay.anncmntcustomlistview.ListAdapter;
import com.example.ibarangay.anncmntcustomlistview.User;
import com.example.ibarangay.databinding.ActivityAnnouncementBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

public class Announcement extends AppCompatActivity {

    ActivityAnnouncementBinding binding;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnnouncementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Navigation
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);


        //Navigation
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout ,R.string.menu_Open,R.string.menu_Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setCheckedItem(R.id.nav_announcement);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_announcement:

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_request:
                        Intent IRequest = new Intent(Announcement.this, Request.class);
                        IRequest.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(IRequest);

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_service:
                        Intent IService = new Intent(Announcement.this, Service.class);
                        IService.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(IService);

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(Announcement.this);
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

                                        navigationView.setCheckedItem(R.id.nav_announcement);
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });


        //Retrieving of Data for Announcement
        new Connection().execute();
    }


    //Retrieving of data from database
    class Connection extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String host = "http://192.168.254.114/iBarangay/ibarangay_announcement.php";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(host));
                HttpResponse response = client.execute(request);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer stringBuffer = new StringBuffer("");
                String line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                    break;
                }
                reader.close();
                result = stringBuffer.toString();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                super.onPostExecute(result);
                if (result != null) {
                    JSONObject jsonResult = new JSONObject(result);
                    int success = jsonResult.getInt("success");
                    if (success == 1) {
                        //Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_SHORT).show();
                        ArrayList<Integer> ArrImageID = new ArrayList<>();
                        ArrayList<String> ArrSubject = new ArrayList<>();
                        ArrayList<String> ArrDate = new ArrayList<>();
                        ArrayList<String> ArrDetails = new ArrayList<>();

                        JSONArray announcement = jsonResult.getJSONArray("announcement");

                        //Retrieving of information
                        for (int i = 0; i < announcement.length(); i++) {
                            JSONObject anncmnt = announcement.getJSONObject(i);
                            int strid_announcement = anncmnt.getInt("id_announcement");
                            String strsubject = anncmnt.getString("Subject");
                            String strdate = anncmnt.getString("Date");
                            String strlevel = anncmnt.getString("Level");
                            String strextention = anncmnt.getString("Extension");
                            String strdetails = anncmnt.getString("Details");

                            //Retrieving of images
                            if (strlevel.equals("Dangerous")) {
                                ArrImageID.add(R.drawable.ic_logo);
                            } else if (strlevel.equals("Normal")) {
                                ArrImageID.add(R.drawable.ic_logout);
                            } else {
                                ArrImageID.add(R.drawable.ic_service);
                            }


//                            try {
//
//                                StorageReference retrieveStorageReference = FirebaseStorage.getInstance().getReference().child("images/noli");
//
//                                File localFile = File.createTempFile("noli", "jpg");
//                                retrieveStorageReference.getFile(localFile)
//                                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                                            @Override
//                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//                                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                                                ArrImageID.add(bitmap);
//                                                Toast.makeText(getApplicationContext(), "Working",Toast.LENGTH_LONG).show();
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception exception) {
//
//                                                Toast.makeText(getApplicationContext(), "Ann",Toast.LENGTH_LONG).show();
//                                                //Drawable drawable = getResources().getDrawable(R.drawable.img_noimage);
//                                                //Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//                                                //ArrImageID.add(bitmap);
//                                            }
//                                        });
//
//                            }catch (Exception e){
//                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                            }

                            ArrSubject.add(strsubject);
                            ArrDate.add(strdate);
                            ArrDetails.add(strdetails);
                        }

                        ArrayList<User> userArrayList = new ArrayList<>();
                        for (int i = 0; i < ArrImageID.size(); i++) {
                            User user = new User(ArrSubject.get(i),ArrDate.get(i), ArrDetails.get(i), ArrImageID.get(i));
                            userArrayList.add(user);
                        }

                        ListAdapter listAdapter = new ListAdapter(Announcement.this, userArrayList);
                        binding.listview.setAdapter(listAdapter);
                        binding.listview.setClickable(true);
                        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Intent i = new Intent(Announcement.this,AnnouncementModule.class);
                                i.putExtra("subject",ArrSubject.get(position));
                                i.putExtra("date",ArrDate.get(position));
                                i.putExtra("details",ArrDetails.get(position));
                                i.putExtra("imageid",ArrImageID.get(position));
                                startActivity(i);

                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "There is no announcement yet", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage() + "Onpost", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Announcement.this);
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

                        navigationView.setCheckedItem(R.id.nav_service);
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}