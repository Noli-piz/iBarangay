package com.example.ibarangay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class RequestModule extends AppCompatActivity {

    Spinner sprCertificate, sprDeliveryOption;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_module);

        sprCertificate = findViewById(R.id.spnrCertificate);
        sprDeliveryOption = findViewById(R.id.spnrDeliveryOption);
        btnBack = findViewById(R.id.btnRequest);

        //Retrieve Data
        new RetrieveDO().execute();
        new RetrieveToP().execute();
    }

    //Retrieve DeliveryOption
    class RetrieveToP extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String host = "http://192.168.254.114/iBarangay/ibarangay_certificate.php";
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

                        JSONArray certificate = jsonResult.getJSONArray("certificate");
                        ArrayList<String> ArrTypes = new ArrayList<>();

                        for (int i = 0; i < certificate.length(); i++) {
                            JSONObject crtfct = certificate.getJSONObject(i);
                            int strid_certificate = crtfct.getInt("id_certificate");
                            String strtypes = crtfct.getString("Types");

                            //Store here
                            ArrTypes.add(strtypes);
                        }

                        //Display
                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ArrTypes);
                        sprCertificate.setAdapter(adapter);

                    } else {
                        Toast.makeText(getApplicationContext(), "There is no availa yet", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage() + "Onpost", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //END


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
                        sprDeliveryOption.setAdapter(adapter);

                    } else {
                        Toast.makeText(getApplicationContext(), "There is no availa yet", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage() + "Onpost", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //Back Button
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Request.class);
        startActivity(intent);
        finish();
    }
}