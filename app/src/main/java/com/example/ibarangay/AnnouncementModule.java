package com.example.ibarangay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class AnnouncementModule extends AppCompatActivity {

    TextView TVSubject, TVDate, TVDetails, TVBack;
    ImageView IVimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_module);

        TVSubject = findViewById(R.id.tvSubject);
        TVDate = findViewById(R.id.tvDate);
        TVDetails = findViewById(R.id.tvDetails);
        TVBack = findViewById(R.id.tv_Back);
        IVimg = findViewById(R.id.imageView);


        TVSubject.setText(getIntent().getExtras().getString("subject"));
        TVDate.setText(getIntent().getExtras().getString("date"));
        TVDetails.setText(getIntent().getExtras().getString("details"));

        downloadPicture(getIntent().getExtras().getString("imageid"));

        TVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
                            Bitmap s = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            IVimg.setImageBitmap(s);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });

        }catch (Exception e){
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Announcement.class);
        startActivity(intent);
        finish();
    }
}