package com.todaytech2.utilitymanager;

import android.app.MediaRouteButton;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class CreditServicesActivity extends AppCompatActivity{
    String apiUrl = "http://mtrans.mobile-transact.com:8090/jsortcash/v1/creditpurchase";
    ArrayList<String> selectbill;
    ArrayList<String> repaymentrate;
    ArrayList<String> yesno;
    ArrayList<String> lineprovider;
    ArrayList<String> accountype;

    Spinner spinnera, spinnerb, spinnerc, spinnerd, spinnere, spinnerf;


    private Spinner spinner2, spinner3, spinner4, spinner5, spinner6;
    private EditText meterno, amount, paybillno, accountno, tillno, phoneno, option,amount2;
    private MediaRouteButton editTextObject;
    ProgressDialog progressDialog;

    private RadioGroup rg1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_services);
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        spinner6 = (Spinner) findViewById(R.id.spinner6);
        spinner5 = (Spinner) findViewById(R.id.spinner5);
        meterno = (EditText) findViewById(R.id.meter_no);
        phoneno = (EditText) findViewById(R.id.phone_no);
        amount = (EditText) findViewById(R.id.amount);
        amount2 = (EditText) findViewById(R.id.amount2);
        paybillno = (EditText) findViewById(R.id.paybill_no);
        accountno = (EditText) findViewById(R.id.acc_no);
        tillno = (EditText) findViewById(R.id.till_no);


       /* spinner4.setOnItemSelectedListener(this);
        spinner5.setOnItemSelectedListener(this);  */

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner4.getItemAtPosition(position).toString().equalsIgnoreCase("KPLC Tokens")) {
                    phoneno.setVisibility(View.VISIBLE);
                    amount.setVisibility(View.VISIBLE);
                    meterno.setVisibility(View.VISIBLE);
                    paybillno.setVisibility(View.INVISIBLE);
                    accountno.setVisibility(View.INVISIBLE);
                    tillno.setVisibility(View.INVISIBLE);
                    spinner5.setVisibility(View.INVISIBLE);
                }
                if (spinner4.getItemAtPosition(position).toString().equalsIgnoreCase("KPLC Bill")) {
                    phoneno.setVisibility(View.INVISIBLE);
                    amount.setVisibility(View.VISIBLE);
                    meterno.setVisibility(View.VISIBLE);
                    paybillno.setVisibility(View.INVISIBLE);
                    accountno.setVisibility(View.INVISIBLE);
                    tillno.setVisibility(View.INVISIBLE);
                    spinner5.setVisibility(View.INVISIBLE);
                }
                if (spinner4.getItemAtPosition(position).toString().equalsIgnoreCase("Airtime")) {
                    phoneno.setVisibility(View.VISIBLE);
                    amount.setVisibility(View.VISIBLE);
                    meterno.setVisibility(View.INVISIBLE);
                    spinner6.setVisibility(View.VISIBLE);
                    spinner5.setVisibility(View.INVISIBLE);
                    paybillno.setVisibility(View.INVISIBLE);
                    accountno.setVisibility(View.INVISIBLE);
                    tillno.setVisibility(View.INVISIBLE);
                }
                if (spinner4.getItemAtPosition(position).toString().equalsIgnoreCase("Others")) {
                    phoneno.setVisibility(View.INVISIBLE);
                    amount.setVisibility(View.INVISIBLE);
                    meterno.setVisibility(View.INVISIBLE);
                    spinner6.setVisibility(View.INVISIBLE);
                    spinner5.setVisibility(View.VISIBLE);
                    paybillno.setVisibility(View.INVISIBLE);
                    accountno.setVisibility(View.INVISIBLE);
                    tillno.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner5.getItemAtPosition(position).toString().equalsIgnoreCase("PayBill")) {
                    paybillno.setVisibility(View.VISIBLE);
                    amount2.setVisibility(View.VISIBLE);
                    accountno.setVisibility(View.VISIBLE);
                    tillno.setVisibility(View.INVISIBLE);
                    phoneno.setVisibility(View.INVISIBLE);
                    meterno.setVisibility(View.INVISIBLE);
                    spinner6.setVisibility(View.INVISIBLE);
                    spinner4.setVisibility(View.INVISIBLE);
                }
                if (spinner5.getItemAtPosition(position).toString().equalsIgnoreCase("Till Number")) {
                    tillno.setVisibility(View.VISIBLE);
                    amount2.setVisibility(View.VISIBLE);
                    accountno.setVisibility(View.INVISIBLE);
                    phoneno.setVisibility(View.INVISIBLE);
                    paybillno.setVisibility(View.INVISIBLE);
                    meterno.setVisibility(View.INVISIBLE);
                    spinner6.setVisibility(View.INVISIBLE);
                    spinner4.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
        private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // display a progress dialog for good user experiance
                progressDialog = new ProgressDialog(CreditServicesActivity.this);
                progressDialog.setMessage("Please Wait");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                // implement API in background and store the response in current variable
                String current = "";
                try {
                    URL url;
                    HttpURLConnection urlConnection = null;
                    try {

                        JSONObject jsRegister = new JSONObject();
                        jsRegister.put("phonenumber", ((EditText) findViewById(R.id.phone_no)).getText().toString());
                        jsRegister.put("amount", ((EditText) findViewById(R.id.amount)).getText().toString());
                        jsRegister.put("amount2", ((EditText) findViewById(R.id.amount2)).getText().toString());
                        jsRegister.put("meterno", ((EditText) findViewById(R.id.meter_no)).getText().toString());
                        jsRegister.put("tillnumber", ((EditText) findViewById(R.id.till_no)).getText().toString());
                        jsRegister.put("accountnumber", ((EditText) findViewById(R.id.acc_no)).getText().toString());
                        jsRegister.put("paybillnumber", ((EditText) findViewById(R.id.paybill_no)).getText().toString());
                        if (jsRegister.getInt("success") == 1) {
                            JSONArray jsonArray = jsRegister.getJSONArray("bill");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsRegister1 = jsonArray.getJSONObject(i);
                                String bill = jsRegister1.getString("id");
                                selectbill.add("name");
                            }
                        }
                        if (jsRegister.getInt("success") == 1) {
                            JSONArray jsonArray = jsRegister.getJSONArray("interest");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsRegister1 = jsonArray.getJSONObject(i);
                                String bill = jsRegister1.getString("id");
                                repaymentrate.add("name");
                            }
                        }
                        if (jsRegister.getInt("success") == 1) {
                            JSONArray jsonArray = jsRegister.getJSONArray("serviceprovider");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsRegister1 = jsonArray.getJSONObject(i);
                                String bill = jsRegister1.getString("id");
                                lineprovider.add("name");
                            }
                        }
                        if (jsRegister.getInt("success") == 1) {
                            JSONArray jsonArray = jsRegister.getJSONArray("option");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsRegister1 = jsonArray.getJSONObject(i);
                                String bill = jsRegister1.getString("id");
                                yesno.add("name");
                            }
                        }
                        if (jsRegister.getInt("success") == 1) {
                            JSONArray jsonArray = jsRegister.getJSONArray("accountype");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsRegister1 = jsonArray.getJSONObject(i);
                                String bill = jsRegister1.getString("id");
                                accountype.add("name");
                            }
                        }
                        spinner2.setAdapter(new ArrayAdapter<String>(CreditServicesActivity.this, android.R.layout.simple_spinner_dropdown_item, repaymentrate));
                        spinner3.setAdapter(new ArrayAdapter<String>(CreditServicesActivity.this, android.R.layout.simple_spinner_dropdown_item, yesno));
                        spinner4.setAdapter(new ArrayAdapter<String>(CreditServicesActivity.this, android.R.layout.simple_spinner_dropdown_item, selectbill));
                        spinner6.setAdapter(new ArrayAdapter<String>(CreditServicesActivity.this, android.R.layout.simple_spinner_dropdown_item, lineprovider));
                        spinner5.setAdapter(new ArrayAdapter<String>(CreditServicesActivity.this, android.R.layout.simple_spinner_dropdown_item, accountype));

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
                        int responseCode = 0;

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

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
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

