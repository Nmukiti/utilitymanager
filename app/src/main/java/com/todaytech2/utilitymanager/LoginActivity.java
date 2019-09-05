package com.todaytech2.utilitymanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.todaytech2.utilitymanager.Model.Users;
import com.todaytech2.utilitymanager.Prevalent.Prevalent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginActivity extends AppCompatActivity {
    String apiUrl= "http://mtrans.mobile-transact.com:8090/jsortcash/v1/login";

    private EditText InputPhoneNumber,Inputlocation,Inputid, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView forgotPassword,changePassword;
    ProgressDialog progressDialog;
    AwesomeValidation awesomeValidation;


    private String parentDbName = "Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        LoginButton = (Button) findViewById(R.id.login_btn);
        forgotPassword = (TextView) findViewById(R.id.forget_password_link);
        changePassword = (TextView) findViewById(R.id.change_password_link);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);
        Inputid = (EditText) findViewById(R.id.login_id_number_input);
        loadingBar = new ProgressDialog(this);

        awesomeValidation.addValidation(LoginActivity.this,R.id.login_phone_number_input,R.id.register_phone_number_input, R.string.phoneno1);
        awesomeValidation.addValidation(LoginActivity.this,R.id.login_password_input,R.id.register_password_input,R.string.pass3);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // create object of HTTPAsyncTask class and execute it
                HTTPAsyncTask myAsyncTasks = new HTTPAsyncTask();
                myAsyncTasks.execute();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

    }
    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(LoginActivity.this);
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
                    jsRegister.put("phonenumber",((EditText)findViewById(R.id.register_phone_number_input)).getText().toString());
                    jsRegister.put("idnumber",((EditText)findViewById(R.id.idno_input)).getText().toString());
                    jsRegister.put("password",((EditText)findViewById(R.id.register_password_input)).getText().toString());

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
                Toast.makeText(LoginActivity.this,"Log in successfull",Toast.LENGTH_SHORT);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }else
            {
                Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_SHORT);
            }
        }
    }


  /*  private void AllowAccessToAccount(final String phone, final String password)
    {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {

                            if (parentDbName.equals("Users"))
                            {
                                Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }  */
}
