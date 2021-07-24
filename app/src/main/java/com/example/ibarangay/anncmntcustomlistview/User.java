package com.example.ibarangay.anncmntcustomlistview;

import android.graphics.Bitmap;

import com.google.firebase.storage.StorageReference;

import java.io.File;

public class User {

    String subject, date, details;
    int imageId;

    public User(String subject, String date, String details, int imageId) {
        this.subject = subject;
        this.date = date;
        this.details = details;
        this.imageId = imageId;
    }
}
