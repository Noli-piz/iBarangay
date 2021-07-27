package com.example.ibarangay.headerMenu;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ibarangay.R;

public class headerMenu extends AppCompatActivity {

    TextView tvname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvname = findViewById(R.id.tvMenuName);
        tvname.setText("sad");

    }



}
