package com.example.ibarangay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

public class ServiceModule extends AppCompatActivity {

    Button BTNAdd, BTNMin;
    TextView TVQuantity;
    private Spinner sprDelivery, sprItem ;

    private int quantity= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_module);

        BTNAdd = findViewById(R.id.btnAdd);
        BTNMin = findViewById(R.id.btnMin);
        TVQuantity = findViewById(R.id.tvQuantity);
        sprItem = findViewById(R.id.spnrItem);
        sprDelivery = findViewById(R.id.spnerDelivery);

        //Retrieve Spinner
        new RetrieveDO().execute();
        new RetrieveItem().execute();

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

    //Retrieve Item
    class RetrieveItem extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String host = "http://192.168.254.114/iBarangay/ibarangay_items.php";
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
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

                        JSONArray items = jsonResult.getJSONArray("items");
                        ArrayList<String> ArrItems = new ArrayList<>();

                        for (int i = 0; i < items.length(); i++) {
                            JSONObject anncmnt = items.getJSONObject(i);
                            int strid_items = anncmnt.getInt("id_items");
                            String strItemNames = anncmnt.getString("ItemName");

                            //Store here
                            ArrItems.add(strItemNames);
                        }

                        //Display
                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ArrItems);
                        sprItem.setAdapter(adapter);

                    } else {
                        Toast.makeText(getApplicationContext(), "There is no availa yet", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage() + "Onpost", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //Retrieve DeliveryOption
    class RetrieveDO extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String host = "http://192.168.254.114/iBarangay/ibarangay_deliveryoptions.php";
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
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

                        JSONArray deliveryoption = jsonResult.getJSONArray("deliveryoptions");
                        ArrayList<String> ArrOptions = new ArrayList<>();

                        for (int i = 0; i < deliveryoption.length(); i++) {
                            JSONObject anncmnt = deliveryoption.getJSONObject(i);
                            int strid_deliveryoption = anncmnt.getInt("id_deliveryoption");
                            String stroptions = anncmnt.getString("Options");

                            //Store here
                            ArrOptions.add(stroptions);
                        }

                        //Display
                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ArrOptions);
                        sprDelivery.setAdapter(adapter);

                    } else {
                        Toast.makeText(getApplicationContext(), "There is no availa yet", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage() + "Onpost", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Service.class);
        startActivity(intent);
        finish();
    }
}