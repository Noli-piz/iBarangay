package com.example.ibarangay.anncmntcustomlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ibarangay.R;

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

        imageView.setImageResource(user.imageId);
        tvSubject.setText("Subject: " + user.subject);
        tvDetails.setText("Details: " + user.details);
        tvDate.setText("Date: " +user.date);


        return convertView;
    }
}
