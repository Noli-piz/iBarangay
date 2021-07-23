package com.example.ibarangay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AnnouncementModule extends AppCompatActivity {

    TextView TVSubject, TVDate, TVDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_module);

        TVSubject = findViewById(R.id.tvSubject);
        TVDate = findViewById(R.id.tvDate);
        TVDetails = findViewById(R.id.tvDetails);


        TVSubject.setText(getIntent().getExtras().getString("subject"));
        TVDate.setText(getIntent().getExtras().getString("date"));
        TVDetails.setText(getIntent().getExtras().getString("details"));

    }
}