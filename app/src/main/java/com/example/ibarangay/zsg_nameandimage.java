package com.example.ibarangay;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class zsg_nameandimage {


    private static String strusername,strname, stremail, strImg;
    private static Bitmap bitImg;

    private StorageReference retrieveStorageReference;

    public void nameandimage(){
        new RetrieveInfo().execute();
    }

    public void reset(){
        this.stremail ="";
        this.strusername="";
        this.strname="";
        this.stremail="";
        this.strImg="";
        this.bitImg = null;
    }

    /////INFORMATION
    class RetrieveInfo extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String host = "http://192.168.254.114/iBarangay/ibarangay_personalinfo.php?Username=" + strusername;
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

                        JSONArray information = jsonResult.getJSONArray("info");
                        String ArrTypes, Fname, Mname, Lname, Sname, Image;

                        JSONObject info = information.getJSONObject(0);
                        Fname = info.getString("Fname");
                        Mname = info.getString("Mname");
                        Lname = info.getString("Lname");
                        Sname = info.getString("Sname");
                        Image = info.getString("Image");

                        strname = Fname +" "+Mname+" "+Lname+" "+Sname;
                        strImg = Image;
                        RetrieveImg();
                    } else {
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    //// IMAGE
    public void RetrieveImg(){
        try {

            retrieveStorageReference = FirebaseStorage.getInstance().getReference().child("images/" + strImg);

            File localFile = File.createTempFile(strImg, "jpg");
            retrieveStorageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            bitImg = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });

        }catch (Exception e){

        }
    }

    //Setters Getters
    public String getStrusername() {
        return strusername;
    }

    public void setStrusername(String strusername) {
        zsg_nameandimage.strusername = strusername;
    }

    public String getStrname() {
        return strname;
    }

    public String getStremail() {
        return stremail;
    }

    public String getStrImg() {
        return strImg;
    }

    public Bitmap getImg() {
        return bitImg;
    }
}
