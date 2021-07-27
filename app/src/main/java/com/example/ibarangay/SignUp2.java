package com.example.ibarangay;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class SignUp2 extends AppCompatActivity {

    TextInputEditText IETFname, IETMname, IETLname, IETSname, IETBirthPlace,IETCedulaNo, IETContactNo;
    TextInputEditText IETUsername, IETPassword, IETRpassword,  IETEmail;
    Button BTNSignup, BTNBirthDate, BTNDateOfRegistration;
    ImageView imgProfile;
    TextView tvBack;
    Spinner sprCivilStatus, sprGender, sprPurok, sprVoterStatus;

    private DatePickerDialog datePickerDialog, datePickerDialog2;

    //Firebase
    private String downloadUrl;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference, retrieveStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        //Datetime Picker
        initDatePicker();
        initDatePicker2();


        tvBack = findViewById(R.id.lblBack2);

        imgProfile = findViewById(R.id.img_Profile2);
        IETFname = findViewById(R.id.S2Fname);
        IETMname = findViewById(R.id.S2Mname);
        IETLname = findViewById(R.id.S2Lname);
        IETSname = findViewById(R.id.S2Sname);
        IETBirthPlace = findViewById(R.id.S2Birthplace);
        IETCedulaNo = findViewById(R.id.ETCedulaNo);
        IETContactNo = findViewById(R.id.ETContactNo);

        BTNSignup = findViewById(R.id.btnsignupS2);
        BTNBirthDate = findViewById(R.id.btnBirthDate);
        BTNDateOfRegistration = findViewById(R.id.btnDateOfRegistration);
        sprCivilStatus = findViewById(R.id.spnrCivilStatus);
        sprGender = findViewById(R.id.spnrGender);
        sprPurok = findViewById(R.id.spnrPurok);
        sprVoterStatus = findViewById(R.id.spnrVoterStatus);


        //Datetime Picker
        BTNBirthDate.setText(getTodaysDate());
        BTNDateOfRegistration.setText(getTodaysDate());

        //Initialize Spinner
        new RetrieveCV().execute();
        new RetrieveGender().execute();
        new RetrievePurok().execute();
        RetrieveVS();


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
            InsertData3();

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

                                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                                intent.putExtra("methodName","myMethod");
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

    //////////////////////////////////////// TETSTS ////////////////////////////////////////////////////////
    private void InsertData3(){
        zsg_signup signUp = new zsg_signup();

        try {
            signUp.setStrFname(IETFname.getText().toString());
            signUp.setStrMname(IETMname.getText().toString());
            signUp.setStrLname(IETLname.getText().toString());
            signUp.setStrSname(IETSname.getText().toString());
            signUp.setStrBirthplace(IETBirthPlace.getText().toString());
            signUp.setBirthdate(BTNBirthDate.getText().toString());
            signUp.setStrCivilStatus(sprCivilStatus.getSelectedItem().toString());
            signUp.setGender(sprGender.getSelectedItem().toString());
            signUp.setStrPurok(sprPurok.getSelectedItem().toString());
            signUp.setVoterStatus(sprVoterStatus.getSelectedItem().toString());
            signUp.setDateOfRegistration(BTNDateOfRegistration.getText().toString());
            signUp.setContactNo(IETContactNo.getText().toString());
            signUp.setStrCedulaNo(IETCedulaNo.getText().toString());

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"insert2" + e.getMessage(), Toast.LENGTH_SHORT).show();
            stop();
        }

        if( !signUp.getUsername().equals("") && !signUp.getPassword().equals("")  && !signUp.getEmail().equals("") && !signUp.getStatus().equals("") && !signUp.getStrFname().equals("") && !signUp.getStrMname().equals("") && !signUp.getStrLname().equals("") && !signUp.getStrBirthplace().equals("") &&
                !signUp.getStrBirthdate().equals("") && !signUp.getStrCivilStatus().equals("") && !signUp.getStrGender().equals("") && !signUp.getStrPurok().equals("") &&
                !signUp.getStrVoterStatus().equals("") && !signUp.getStrCedulaNo().equals("") && !signUp.getStrContactNo().equals("") && !signUp.getStrContactNo().equals("")) {

            if (signUp.getStrSname().equals("")) {
                signUp.setStrSname("NONE");
            }

            if (!signUp.getStrImage().equals("")) {

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        String[] field = new String[18];
                        field[0] = "Username";
                        field[1] = "Password";
                        field[2] = "Status";
                        field[3] = "Fname";
                        field[4] = "Mname";
                        field[5] = "Lname";
                        field[6] = "Sname";
                        field[7] = "Birthplace";
                        field[8] = "Birthdate";
                        field[9] = "CivilStatus";
                        field[10] = "Gender";
                        field[11] = "id_purok";
                        field[12] = "VoterStatus";
                        field[13] = "DateOfRegistration";
                        field[14] = "ContactNo";
                        field[15] = "CedulaNo";
                        field[16] = "Email";
                        field[17] = "Image";

                        String[] data = new String[18];
                        data[0] = signUp.getUsername();
                        data[1] = signUp.getPassword();
                        data[2] = signUp.getStatus();
                        data[3] = signUp.getStrFname();
                        data[4] = signUp.getStrMname();
                        data[5] = signUp.getStrLname();
                        data[6] = signUp.getStrSname();
                        data[7] = signUp.getStrBirthplace();
                        data[8] = signUp.getStrBirthdate();
                        data[9] = signUp.getStrCivilStatus();
                        data[10] = signUp.getStrGender();
                        data[11] = signUp.getStrPurok();
                        data[12] = signUp.getStrVoterStatus();
                        data[13] = signUp.getStrDateOfRegistration();
                        data[14] = signUp.getStrContactNo();
                        data[15] = signUp.getStrCedulaNo();
                        data[16] = signUp.getEmail();
                        data[17] = signUp.getStrImage();

                        PutData putData = new PutData("http:/192.168.254.114/iBarangay/ibarangay_signup3.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if (result.equals("Sign Up Success")) {
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }
    ///////////////////////////////////////////////////// TEST END ///////////////////////////////////////////////////////////////////////////


    //Inserting Data
    private void InsertData(){

        zsg_signup signUp = new zsg_signup();
        if(!signUp.getUsername().equals("") && !signUp.getPassword().equals("")  && !signUp.getEmail().equals("") && !signUp.getStatus().equals("")) {

            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {


                    String[] field = new String[4];
                    field[0] = "Username";
                    field[1] = "Password";
                    field[2] = "Email";
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

    private void stop(){}

    private void InsertData2(){
        zsg_signup signUp = new zsg_signup();

        try {
            signUp.setStrFname(IETFname.getText().toString());
            signUp.setStrMname(IETMname.getText().toString());
            signUp.setStrLname(IETLname.getText().toString());
            signUp.setStrSname(IETSname.getText().toString());
            signUp.setStrBirthplace(IETBirthPlace.getText().toString());
            signUp.setBirthdate(BTNBirthDate.getText().toString());
            signUp.setStrCivilStatus(sprCivilStatus.getSelectedItem().toString());
            signUp.setGender(sprGender.getSelectedItem().toString());
            signUp.setStrPurok(sprPurok.getSelectedItem().toString());
            signUp.setVoterStatus(sprVoterStatus.getSelectedItem().toString());
            signUp.setDateOfRegistration(BTNDateOfRegistration.getText().toString());
            signUp.setContactNo(IETContactNo.getText().toString());
            signUp.setStrCedulaNo(IETCedulaNo.getText().toString());
            signUp.setEmail(signUp.getEmail());

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"insert2" + e.getMessage(), Toast.LENGTH_SHORT).show();
            stop();
        }

        if(!signUp.getStrFname().equals("") && !signUp.getStrMname().equals("") && !signUp.getStrLname().equals("") && !signUp.getStrBirthplace().equals("") &&
           !signUp.getStrBirthdate().equals("") && !signUp.getStrCivilStatus().equals("") && !signUp.getStrGender().equals("") && !signUp.getStrPurok().equals("") &&
           !signUp.getStrVoterStatus().equals("") && !signUp.getStrCedulaNo().equals("") && !signUp.getStrContactNo().equals("") && !signUp.getStrContactNo().equals("")) {

            if (signUp.getStrSname().equals("")) {
                signUp.setStrSname("NONE");
            }

            if (!signUp.getStrImage().equals("")) {

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        String[] field = new String[15];
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
                        field[12] = "CedulaNo";
                        field[13] = "Email";
                        field[14] = "Image";

                        String[] data = new String[15];
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
                        data[12] = signUp.getStrCedulaNo();
                        data[13] = signUp.getEmail();
                        data[14] = signUp.getStrImage();

                        PutData putData = new PutData("http:/192.168.254.114/iBarangay/ibarangay_signup2.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if (result.equals("Sign Up Success")) {
                                    InsertData();
                                } else {
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_SHORT).show();
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
                        //String downloadUrls = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                        zsg_signup s = new zsg_signup();
                        s.setImage(randomKey);

                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Image Uploaded.", Toast.LENGTH_SHORT).show();
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
                pd.setMessage("Failed to upload image");
                pd.show();
            }
        });

    }

    //Datetime Picker Function
    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month +1;
                String date = makeDateString(day, month, year);
                BTNBirthDate.setText(date);
            }
        };

        Calendar cal =  Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = android.app.AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private void initDatePicker2(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month +1;
                String date = makeDateString(day, month, year);
                BTNDateOfRegistration.setText(date);
            }
        };

        Calendar cal =  Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = android.app.AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog2 = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month){
        switch (month){
            case 1: return "JAN";
            case 2: return "FEB";
            case 3: return "MAR";
            case 4: return "APR";
            case 5: return "MAY";
            case 6: return "JUN";
            case 7: return "JUL";
            case 8: return "AUG";
            case 9: return "SEP";
            case 10: return "OCT";
            case 11: return "NOV";
            case 12: return "DEC";
            default:return "JAN";
        }
    }

    public void opedDatePicker(View view){
        datePickerDialog.show();
    }
    public void opedDatePicker2(View view){
        datePickerDialog2.show();
    }

    private String getTodaysDate(){
        Calendar cal =  Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month +1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    //Initialized Spinner
    //Retrieve Civil Status
    class RetrieveCV extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String host = "http://192.168.254.114/iBarangay/ibarangay_civilstatus.php";
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

                        JSONArray civilstatus = jsonResult.getJSONArray("civilstatus");
                        ArrayList<String> ArrTypes = new ArrayList<>();

                        for (int i = 0; i < civilstatus.length(); i++) {
                            JSONObject cvstts = civilstatus.getJSONObject(i);
                            int strid_civilstatus = cvstts.getInt("id_civilstatus");
                            String strtypes = cvstts.getString("Types");

                            //Store here
                            ArrTypes.add(strtypes);
                        }

                        //Display
                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ArrTypes);
                        sprCivilStatus.setAdapter(adapter);

                    } else {
                        Toast.makeText(getApplicationContext(), "There is no availa yet", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage() + "Onpost", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Retrieve Gender
    class RetrieveGender extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String host = "http://192.168.254.114/iBarangay/ibarangay_gender.php";
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

                        JSONArray gender = jsonResult.getJSONArray("gender");
                        ArrayList<String> ArrIdentities = new ArrayList<>();

                        for (int i = 0; i < gender.length(); i++) {
                            JSONObject gndr = gender.getJSONObject(i);
                            int strid_gender = gndr.getInt("id_gender");
                            String stridentities = gndr.getString("Identities");

                            //Store here
                            ArrIdentities.add(stridentities);
                        }

                        //Display
                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ArrIdentities);
                        sprGender.setAdapter(adapter);

                    } else {
                        Toast.makeText(getApplicationContext(), "There is no availa yet", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage() + "Onpost", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Retrieve Purok
    class RetrievePurok extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String host = "http://192.168.254.114/iBarangay/ibarangay_purok.php";
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

                        JSONArray purok = jsonResult.getJSONArray("purok");
                        ArrayList<String> ArrName = new ArrayList<>();

                        for (int i = 0; i < purok.length(); i++) {
                            JSONObject prk = purok.getJSONObject(i);
                            int strid_purok = prk.getInt("id_purok");
                            String strname = prk.getString("Name");

                            //Store here
                            ArrName.add(strname);
                        }

                        //Display
                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ArrName);
                        sprPurok.setAdapter(adapter);

                    } else {
                        Toast.makeText(getApplicationContext(), "There is no availa yet", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage() + "Onpost", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Retrieved Voter Status
    private void RetrieveVS(){
        ArrayList<String> ArrOpt = new ArrayList<>();
        ArrOpt.add("Yes");
        ArrOpt.add("No");
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ArrOpt);
        sprVoterStatus.setAdapter(adapter);
    }

    //Blocking some error
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
        finish();
    }
}