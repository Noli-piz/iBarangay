package com.example.ibarangay.anncmntcustomlistview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ibarangay.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<com.example.ibarangay.anncmntcustomlistview.User> {


    public ListAdapter(Context context, ArrayList<User> userArrayList){

        super(context, R.layout.announcement_list_item,userArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        com.example.ibarangay.anncmntcustomlistview.User user = getItem(position);

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.announcement_list_item,parent,false);

        }
        TextView tvSubject, tvDetails, tvDate;

        ImageView imageView = convertView.findViewById(R.id.profile_pic);
        tvSubject = convertView.findViewById(R.id.subject);
        tvDetails = convertView.findViewById(R.id.details);
        tvDate = convertView.findViewById(R.id.date);


        //imageView.setImageResource(user.imageId);
        tvSubject.setText("Subject: " + user.subject);
        tvDetails.setText("Details: " + user.details);
        tvDate.setText("Date: " +user.date);


        return convertView;
    }



//    private void shit() {
//        try {
//
//            StorageReference retrieveStorageReference = FirebaseStorage.getInstance().getReference().child("images/Noli");
//
//            File localFile = File.createTempFile("noli", "jpg");
//            retrieveStorageReference.getFile(localFile)
//                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                            imageView.setImageBitmap(bitmap);
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//
//                    //Drawable drawable = getResources().getDrawable(R.drawable.img_noimage);
//                    //Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//                    //ArrImageID.add(bitmap);
//                }
//            });
//
//        } catch (Exception e) {
//        }
//    }
}
