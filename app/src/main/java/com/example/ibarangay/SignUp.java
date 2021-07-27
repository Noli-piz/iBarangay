package com.example.ibarangay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.UUID;

public class SignUp extends AppCompatActivity {

    TextInputEditText IETUsername, IETPassword, IETRpassword,  IETEmail;
    Button BTNNext;
    TextView tvBack;

    private ScrollView lytSV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        tvBack = findViewById(R.id.lblBack2);
        lytSV = findViewById(R.id.lytScrollView);

        IETEmail = findViewById(R.id.ETemail);
        IETUsername = findViewById(R.id.ETusername);
        IETPassword = findViewById(R.id.ETpassword);
        IETRpassword = findViewById(R.id.ETRpassword);
        BTNNext = findViewById(R.id.btnNext);

        //Validating
        BTNNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!IETEmail.getText().toString().equals("") && !IETUsername.getText().toString().equals("")) {
                    if (IETPassword.getText().toString().equals(IETRpassword.getText().toString())) {
                        CheckUsername();
                    } else if (!IETPassword.getText().toString().equals(IETRpassword.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Password is not equal to Re-enter password!" , Toast.LENGTH_SHORT).show();
                    } else if (IETPassword.toString().trim().equals("") || IETPassword.toString().trim().equals(null)) {
                        Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
                    } else if (IETRpassword.toString().trim().equals("") || IETRpassword.toString().trim().equals(null)) {
                        Toast.makeText(getApplicationContext(), "Please re-enter a password", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Back Button
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                builder.setTitle("Confirm");
                builder.setMessage(" Your data will not be save. Are you sure you want to exit?")
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
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });


    }

    private void CheckUsername(){
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                String field[] = new String[1];
                field[0] = "Username";

                String data[] = new String[1];
                data[0] = IETUsername.getText().toString();

                PutData putData = new PutData("http:/192.168.254.114/iBarangay/ibarangay_checkusername.php","POST", field, data);
                if(putData.startPut()){
                    if(putData.onComplete()){
                        String result = putData.getResult();
                        if(result.equals("Username is Available")){
                            InsertData();
                        }else if(result.equals("Username already Exist!")){
                            Snackbar.make(lytSV, "Username is already exist.", Snackbar.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void InsertData(){
        zsg_signup signup = new zsg_signup();
        signup.setEmail(IETEmail.getText().toString());
        signup.setUsername(IETUsername.getText().toString());
        signup.setPassword(IETPassword.getText().toString());

        Intent intent = new Intent(getApplicationContext(), SignUp2.class);
        startActivity(intent);
        finish();
    }

    public void Reenter(){
        zsg_signup signup = new zsg_signup();
        IETEmail.setText(signup.getEmail());
        IETUsername.setText(signup.getUsername());
        IETPassword.setText(signup.getPassword());
        IETRpassword.setText(signup.getPassword());
    }

    //Blocking some error
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }
}