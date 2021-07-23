package com.example.ibarangay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ServiceModule extends AppCompatActivity {

    Button BTNAdd, BTNMin;
    TextView TVQuantity;
    private int quantity= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_module);

        BTNAdd = findViewById(R.id.btnAdd);
        BTNMin = findViewById(R.id.btnMin);
        TVQuantity = findViewById(R.id.tvQuantity);

        BTNMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity>0){
                    quantity--;
                    TVQuantity.setText(String.valueOf(quantity));
                }
            }
        });

        BTNAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity>=0){
                    quantity++;
                    TVQuantity.setText(String.valueOf(quantity));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Service.class);
        startActivity(intent);
        finish();
    }
}