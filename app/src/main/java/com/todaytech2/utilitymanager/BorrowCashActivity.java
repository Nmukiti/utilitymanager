package com.todaytech2.utilitymanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BorrowCashActivity extends AppCompatActivity {
    String apiUrl= "http://mtrans.mobile-transact.com:8090/jsortcash/v1/cashborrow";
    String amount,phonenumber;
    ArrayList<String> selectinterest;
    ArrayList<String> borrowrepaymentrate;

    private static final String TAG =BorrowCashActivity.class.getSimpleName();

    private Button submitButton1;
    private EditText InputPhoneNumber, InputAmount;
    private Spinner spinnerid;
    private RadioGroup radioG;
    private ProgressDialog loadingBar;
    private DatabaseReference demoRef;
    private RadioButton radio1,radio2,radio3;
    private Spinner spinner8;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_cash);

       // addPreferencesFromResource(R.xml.preferences);

        ArrayList<String> entries = new ArrayList<>();
        ArrayList<String> entryValues = new ArrayList<>();
        String jsonString = "{\"interest\":[{\"id\":\"1\",\"name\":\"0% for 2days \"},{\"id\":\"2\",\"name\":\"5% for 15days\"},{\"id\":\"3\",\"name\":\"10% for 25days\"}],\"success\":1}";

        submitButton1 = (Button) findViewById(R.id.submit_btn1);
        InputAmount = (EditText) findViewById(R.id.amount);
        InputPhoneNumber = (EditText) findViewById(R.id.phone_number);
        spinner8 = (Spinner) findViewById(R.id.spinner8);
        loadingBar = new ProgressDialog(this);

        /*rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                 RadioButton selectedRadioButton = (RadioButton) group.findViewById(checkedId);
                 String text = RadioButton.getTag();
                 JSONObject jsonObject = new JSONObject();
                 jsonObject.put("maritalstatus", text);
           // Now you need to send the json to the server through an AsyncTask
                 SendJsonTask sendJsonTask = new SendJsonTask();
                 sendJsonTask.execute(jsonObject.toString());
            }
        });  */

        submitButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // create object of HTTPAsyncTask class and execute it
                SendJsonTask myAsyncTasks = new SendJsonTask();
                myAsyncTasks.execute();
            }
        });
    }
    private class SendJsonTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(BorrowCashActivity.this);
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
                    jsRegister.put("phonenumber",((EditText)findViewById(R.id.phone_number)).getText().toString());
                    jsRegister.put("amount",((EditText)findViewById(R.id.amount)).getText().toString());
                    if (jsRegister.getInt("success")==1){
                        JSONArray jsonArray=jsRegister.getJSONArray("interest");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsRegister1=jsonArray.getJSONObject(i);
                            String bill=jsRegister1.getString("id");
                            borrowrepaymentrate.add("name");
                        }
                    }
                    spinner8.setAdapter(new ArrayAdapter<String>(BorrowCashActivity.this,android.R.layout.simple_spinner_dropdown_item,borrowrepaymentrate));

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


