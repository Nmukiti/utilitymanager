package com.todaytech2.utilitymanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CashPurchaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String apiUrl= "http://mtrans.mobile-transact.com:8090/jsortcash/v1/cashpurchase";
    ArrayList<String> selectbill;

    private Spinner spinner7;
    private EditText meterno2,amount2,phoneno2;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_purchase);
        selectbill=new ArrayList<>();
        spinner7=(Spinner)findViewById(R.id.spinner7);
        meterno2 = (EditText) findViewById(R.id.meter_no2);
        phoneno2 = (EditText) findViewById(R.id.phone_no2);
        amount2 = (EditText) findViewById(R.id.amount2);

        spinner7.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (spinner7.getItemAtPosition(position).toString().equalsIgnoreCase("KPLC Tokens")) {
            phoneno2.setVisibility(View.VISIBLE);
            amount2.setVisibility(View.VISIBLE);
            meterno2.setVisibility(View.VISIBLE);
        }
        if (spinner7.getItemAtPosition(position).toString().equalsIgnoreCase("KPLC Bill")) {
            phoneno2.setVisibility(View.INVISIBLE);
            amount2.setVisibility(View.VISIBLE);
            meterno2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(CashPurchaseActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params ) {
            // implement API in background and store the response in current variable
            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {

                    JSONObject jsRegister  = new JSONObject();
                    jsRegister.put("phonenumber",((EditText)findViewById(R.id.phone_no2)).getText().toString());
                    jsRegister.put("amount",((EditText)findViewById(R.id.amount2)).getText().toString());
                    jsRegister.put("meterno",((EditText)findViewById(R.id.meter_no2)).getText().toString());
                   if (jsRegister.getInt("success")==1){
                       JSONArray jsonArray=jsRegister.getJSONArray("bill");
                       for (int i=0;i<jsonArray.length();i++){
                           JSONObject jsRegister1=jsonArray.getJSONObject(i);
                           String bill=jsRegister1.getString("id");
                           selectbill.add("name");
                       }
                   }
                   spinner7.setAdapter(new ArrayAdapter<String>(CashPurchaseActivity.this,android.R.layout.simple_spinner_dropdown_item,selectbill));

                    url = new URL(apiUrl);

                    urlConnection = (HttpURLConnection) url
                            .openConnection();

                  /*  InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();
                        System.out.print(current);

                    } */
                    int responseCode=0;

                    urlConnection.setRequestMethod("POST");
                    urlConnection.setConnectTimeout(16000);
                    urlConnection.setReadTimeout(16000);
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.connect();
                    OutputStream dStream = new BufferedOutputStream(urlConnection.getOutputStream());
                    dStream.write(jsRegister.toString().getBytes());
                    dStream.flush();
                    dStream.close();
                    responseCode = urlConnection.getResponseCode();
                    // return the data to onPostExecute method
                    return current;

                }catch (JSONException e) { e.printStackTrace();
                }
                catch (Exception e) { e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {

            Log.d("data", s.toString());
            // dismiss the progress dialog after receiving data from API
            progressDialog.dismiss();
        }
    }
}

