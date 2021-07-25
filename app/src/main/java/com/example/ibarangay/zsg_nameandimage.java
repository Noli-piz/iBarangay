package com.example.ibarangay;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class zsg_nameandimage {

    private static String strname, stremail, strImg;
    private Bitmap bitImg;

    private StorageReference retrieveStorageReference;


    public void RetrieveInfo(){

    }

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

    public String getStrname() {
        return strname;
    }

    public void setStrname(String strname) {
        this.strname = strname;
    }

    public String getStremail() {
        return stremail;
    }

    public void setStremail(String stremail) {
        this.stremail = stremail;
    }

    public String getStrImg() {
        return strImg;
    }

    public void setStrImg(String strImg) {
        this.strImg = strImg;
    }

    public Bitmap getImg() {
        return bitImg;
    }

    public void setImg(Bitmap img) {
        this.bitImg = img;
    }


}
