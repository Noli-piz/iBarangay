package com.example.ibarangay;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.UUID;

public class SignUp2 extends AppCompatActivity {

    TextInputEditText IETFname, IETMname, IETLname, IETSname,IETUsername, IETPassword, IETRpassword,  IETEmail;
    Button BTNSignup;
    ImageView imgProfile;
    TextView tvBack;

    //Firebase
    private String downloadUrl;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference, retrieveStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        tvBack = findViewById(R.id.lblBack);

        imgProfile = findViewById(R.id.img_Profile);
        IETFname = findViewById(R.id.ETFname);
        IETUsername = findViewById(R.id.ETusername);
        IETPassword = findViewById(R.id.ETpassword);
        IETRpassword = findViewById(R.id.ETRpassword);
        IETEmail = findViewById(R.id.ETemail);
        BTNSignup = findViewById(R.id.btnsignup);

        //Uploading Image in Firebase
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    choosePicture();
            }
        });

        //Inserting Data in Database
        BTNSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(IETPassword.getText().toString().equals(IETRpassword.getText().toString())) {
                    InsertData();
                }else if(!IETPassword.getText().toString().equals(IETRpassword.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Password is not equal to Re-enter password!"+IETPassword.getText()+" "+IETRpassword.getText(), Toast.LENGTH_SHORT).show();
                }else if(IETPassword.toString().trim().equals("") || IETPassword.toString().trim().equals(null)){
                    Toast.makeText(getApplicationContext(),"Please enter a password", Toast.LENGTH_SHORT).show();
                }else if(IETRpassword.toString().trim().equals("") || IETRpassword.toString().trim().equals(null)){
                    Toast.makeText(getApplicationContext(),"Please re-enter a password", Toast.LENGTH_SHORT).show();
                }


            }
        });



        //Back Button
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp2.this);
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


    //Inserting Data
    private void InsertData(){
        zsg_signup signUp = new zsg_signup();

                try{
//                    signUp.setStrFname(IETFname.getText().toString());
//                    signUp.setStrMname(IETMname.getText().toString());
//                    signUp.setStrLname(IETLname.getText().toString());
//                    signUp.setStrSname(IETSname.getText().toString());
                    signUp.setUsername(IETUsername.getText().toString());
                    signUp.setPassword(IETPassword.getText().toString());
                    signUp.setEmail(IETEmail.getText().toString());
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"All fields are required", Toast.LENGTH_SHORT).show();
                }

//                strfullname = IETFullname.getText().toString();
//                strusername = IETUsername.getText().toString();
//                strpassword = IETPassword.getText().toString();
//                stremail = IETEmail.getText().toString();


        if(!signUp.getUsername().equals("") && !signUp.getPassword().equals("")  && !signUp.getEmail().equals("") && !signUp.getStatus().equals("")) {

            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {


                    String[] field = new String[4];
                    field[0] = "Username";
                    field[1] = "Password";
                    field[2] = "id_residentinfo";
                    field[3] = "Status";

                    String[] data = new String[4];
                    data[0] = signUp.getUsername();
                    data[1] = signUp.getPassword();
                    data[2] = signUp.getEmail();
                    data[3] = signUp.getStatus();

                    PutData putData = new PutData("http:/192.168.254.114/iBarangay/ibarangay_signup.php","POST", field, data);
                    if(putData.startPut()){
                        if(putData.onComplete()){
                            String result = putData.getResult();
                            if(result.equals("Sign Up Success")){
                                Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Login.class);
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

    private void InsertData2(){
        zsg_signup signUp = new zsg_signup();

        try{
//                    signUp.setStrFname(IETFname.getText().toString());
//                    signUp.setStrMname(IETMname.getText().toString());
//                    signUp.setStrLname(IETLname.getText().toString());
//                    signUp.setStrSname(IETSname.getText().toString());
            signUp.setUsername(IETUsername.getText().toString());
            signUp.setPassword(IETPassword.getText().toString());
            signUp.setEmail(IETEmail.getText().toString());
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"All fields are required", Toast.LENGTH_SHORT).show();
        }

//                strfullname = IETFullname.getText().toString();
//                strusername = IETUsername.getText().toString();
//                strpassword = IETPassword.getText().toString();
//                stremail = IETEmail.getText().toString();


        if(!signUp.getUsername().equals("") && !signUp.getPassword().equals("")  && !signUp.getEmail().equals("") && !signUp.getStatus().equals("")) {

            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {


                    String[] field = new String[14];
                    field[0] = "Fname";
                    field[1] = "Mname";
                    field[2] = "Lname";
                    field[3] = "Sname";
                    field[4] = "Birthplace";
                    field[5] = "Birthdate";
                    field[6] = "CivilStatus";
                    field[7] = "Gender";
                    field[8] = "id_purok";
                    field[9] = "VoterStatus";
                    field[10] = "DateOfRegistration";
                    field[11] = "ContactNo";
                    field[12] = "Email";
                    field[13] = "Image";

                    String[] data = new String[14];
                    data[0] = signUp.getStrFname();
                    data[1] = signUp.getStrMname();
                    data[2] = signUp.getStrLname();
                    data[3] = signUp.getStrSname();
                    data[4] = signUp.getStrBirthplace();
                    data[5] = signUp.getStrBirthdate();
                    data[6] = signUp.getStrCivilStatus();
                    data[7] = signUp.getStrGender();
                    data[8] = signUp.getStrPurok();
                    data[9] = signUp.getStrVoterStatus();
                    data[10] = signUp.getStrDateOfRegistration();
                    data[11] = signUp.getStrContactNo();
                    data[12] = signUp.getEmail();
                    data[13] = signUp.getStrImage();

                    PutData putData = new PutData("http:/192.168.254.114/iBarangay/ibarangay_signup2.php","POST", field, data);
                    if(putData.startPut()){
                        if(putData.onComplete()){
                            String result = putData.getResult();
                            if(result.equals("Sign Up Success")){
                                Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Login.class);
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


    //Uploading Images in Firebase
    private void choosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            imgProfile.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/" + randomKey);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Image Uploaded." + downloadUrl, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to Upload Image.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        pd.setMessage("Percentage: " + (int)progressPercent + "%");
//                        if(progressPercent==0){
//                            pd.setMessage("Failde to upload image");
//                            pd.show();
//                        }
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                pd.setMessage("Failde to upload image");
                pd.show();
            }
        });

    }


    //Blocking some error
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }
}