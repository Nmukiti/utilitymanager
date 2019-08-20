package com.todaytech2.utilitymanager;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Range;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity{
    String apiUrl= "http://lend.mobile-transact.com:8090/jsortcash/v1/register";
    String name,email,kra,dob,nssf,nhif,phonenumber;

    private static final String TAG = "RegisterActivity";

    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber, InputPassword,InputConfirmPassword,Inputkra,Inputnhif,Inputnssf,Inputidno,Inputemail;
    private TextView Inputdob;
    private ProgressDialog loadingBar;
    ProgressDialog progressDialog;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        loadingBar = new ProgressDialog(this);
        CreateAccountButton = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        InputConfirmPassword = (EditText) findViewById(R.id.register_password_input2);
        Inputemail = (EditText)findViewById(R.id.email_input);
        Inputkra = (EditText)findViewById(R.id.kra_input);
        Inputidno = (EditText)findViewById(R.id.idno_input);
        Inputdob=(TextView) findViewById(R.id.dob_input);
        InputPhoneNumber = (EditText) findViewById(R.id.register_phone_number_input);

        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(RegisterActivity.this, R.id.register_username_input, "[a-zA-Z\\s]+", R.string.fullname);
        awesomeValidation.addValidation(RegisterActivity.this, R.id.email_input, android.util.Patterns.EMAIL_ADDRESS, R.string.email);
        awesomeValidation.addValidation(RegisterActivity.this,R.id.register_phone_number_input, "[0-9\\s]+", R.string.phoneno);
        awesomeValidation.addValidation(RegisterActivity.this,R.id.idno_input, "[0-9\\s]+", R.string.idno);
        awesomeValidation.addValidation(RegisterActivity.this, R.id.dob_input, Range.closed(1900, Calendar.getInstance().get(Calendar.YEAR)), R.string.dob);
        awesomeValidation.addValidation(RegisterActivity.this,R.id.register_password_input, regexPassword, R.string.pass1);
        awesomeValidation.addValidation(RegisterActivity.this,R.id.register_password_input2,R.id.register_password_input,R.string.pass2);



        Inputdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                Inputdob.setText(date);
            }
        };
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // create object of HTTPAsyncTask class and execute it
                HTTPAsyncTask myAsyncTasks = new HTTPAsyncTask();
                myAsyncTasks.execute();
            }
        });
    }
    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(RegisterActivity.this);
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
                    jsRegister.put("customername", ((EditText)findViewById(R.id.register_username_input)).getText().toString());
                    jsRegister.put("phonenumber",((EditText)findViewById(R.id.register_phone_number_input)).getText().toString());
                    jsRegister.put("idnumber",((EditText)findViewById(R.id.idno_input)).getText().toString());
                    jsRegister.put("emailaddress",((EditText)findViewById(R.id.email_input)).getText().toString());
                    jsRegister.put("krapin",((EditText)findViewById(R.id.kra_input)).getText().toString());
                    jsRegister.put("nssfnumber",((EditText)findViewById(R.id.nssf_input)).getText().toString());
                    jsRegister.put("nhifnumber",((EditText)findViewById(R.id.nhif_input)).getText().toString());
                    jsRegister.put("password",((EditText)findViewById(R.id.register_password_input)).getText().toString());
                    jsRegister.put("userdateofbirth",((TextView)findViewById(R.id.dob_input)).getText().toString());

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

                }catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
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

            if (awesomeValidation.validate()){
                Toast.makeText(RegisterActivity.this,"Registration successfull",Toast.LENGTH_SHORT);
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }else
            {
                Toast.makeText(RegisterActivity.this,"Error",Toast.LENGTH_SHORT);
            }
        }
    }

}
