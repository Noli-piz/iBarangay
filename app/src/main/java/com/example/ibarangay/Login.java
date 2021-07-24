package com.example.ibarangay;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class Login extends AppCompatActivity {

    TextInputEditText IETUsername, IETPassword;
    Button BTNLogin, BTNSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        IETUsername = findViewById(R.id.ETusername);
        IETPassword = findViewById(R.id.ETpassword);
        BTNLogin = findViewById(R.id.btnlogin);
        BTNSignup = findViewById(R.id.btnsignup);

        BTNSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();

            }
        });


        BTNLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String strusername, strpassword;
                strusername = String.valueOf(IETUsername.getText());
                strpassword = String.valueOf(IETPassword.getText());

//                if(isNetworkConnected()==true){
//                    Toast.makeText(getApplicationContext(),"May internet", Toast.LENGTH_LONG).show();
//                }

                if(!strusername.equals("") && !strpassword.equals("")){
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String field[] = new String[2];
                            field[0] = "Username";
                            field[1] = "Password";

                            String data[] = new String[2];
                            data[0] = strusername;
                            data[1] = strpassword;

                            PutData putData = new PutData("http:/192.168.254.114/iBarangay/ibarangay_login.php","POST", field, data);
                            //PutData putData = new PutData("https://php1002001.000webhostapp.com/ibarangay_login.php","POST", field, data);
                            if(putData.startPut()){
                                if(putData.onComplete()){
                                    String result = putData.getResult();
                                    if(result.equals("Login Success")){
                                        //Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Announcement.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(getApplicationContext(),"All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

//Check internet connection
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("Are you sure you want to exit the app?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
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