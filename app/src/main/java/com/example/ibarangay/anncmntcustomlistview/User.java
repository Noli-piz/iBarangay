package com.example.ibarangay.anncmntcustomlistview;

import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class User {

    String subject, date, details;
    String imageId;

    public User(String subject, String date, String details, String imageId) {
        this.subject = subject;
        this.date = date;
        this.details = details;
        downloadPicture(imageId);
    }

    private void downloadPicture(String picName){
        try {

            StorageReference rSF = FirebaseStorage.getInstance().getReference().child("systemicon/"+picName );

            File localFile = File.createTempFile("Dangerous", "png");
            rSF.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Successfully downloaded data to local file
                            imageId = localFile.getAbsolutePath();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failed download
                }
            });

        }catch (Exception e){
        }
    }
}
