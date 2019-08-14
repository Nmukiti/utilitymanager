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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private static final String TAG = "RegisterActivity";

    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber, InputPassword,InputConfirmPassword,Inputkra,Inputnhif,Inputnssf,Inputidno,Inputemail;
    private TextView Inputdob;
    private ProgressDialog loadingBar;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        loadingBar = new ProgressDialog(this);

        try{
            CreateAccountButton = (Button) findViewById(R.id.register_btn);
            InputName = (EditText) findViewById(R.id.register_username_input);
            InputPassword = (EditText) findViewById(R.id.register_password_input);
            InputConfirmPassword = (EditText) findViewById(R.id.register_password_input2);
            Inputemail = (EditText)findViewById(R.id.email_input);
            Inputkra = (EditText)findViewById(R.id.kra_input);
            Inputidno = (EditText)findViewById(R.id.idno_input);
            Inputdob=(TextView) findViewById(R.id.dob_input);
            InputPhoneNumber = (EditText) findViewById(R.id.register_phone_number_input);


           /* URL url= new URL("http://delivery.mobile-transact.com:8090/jsortcash/v1/register");
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();

            String urlParameters="sn=CO2G8416DRJM&cn=&locale=&caller=&num=12345";
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Key","Value");
            connection.setDoOutput(true);
            DataOutputStream dStream=new DataOutputStream(connection.getOutputStream());
            dStream.writeBytes(urlParameters);
            dStream.flush();
            dStream.close();

            int responseCode=connection.getResponseCode();
            String output="Request URL"+ url;
            output += System.getProperty("line.separator")+"Request Parameters " +urlParameters;
            output += System.getProperty("line.separator")+"Request Code " +responseCode;

            BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line="";
            StringBuilder responseOutput=new StringBuilder();

            while ((line=br.readLine())!=null){
                responseOutput.append(line);
            }
            br.close();
            output +=System.getProperty("line.separator")+responseOutput.toString();
            CreateAccountButton.setText(output);


        }catch (MalformedURLException e){
            e.printStackTrace();*/
        }catch (Exception e){
            e.printStackTrace();
        }

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
                CreateAccount();
            }
        });
    }
    private void CreateAccount() {
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String email = Inputemail.getText().toString();
        String password = InputPassword.getText().toString();
        String password2 = InputConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Email address required...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
                Toast.makeText(this, "Enter Password ...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password2))
        {
            Toast.makeText(this, "Confirm Password cannot be empty ...", Toast.LENGTH_SHORT).show();
        }

        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

           // ValidatephoneNumber(name, phone, password,email);
           /* try{
            URL url= new URL("http://delivery.mobile-transact.com:8090/jsortcash/v1/register");
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();

            String urlParameters="sn=CO2G8416DRJM&cn=&locale=&caller=&num=12345";
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Key","Value");
            connection.setDoOutput(true);
            DataOutputStream dStream=new DataOutputStream(connection.getOutputStream());
            dStream.writeBytes(urlParameters);
            dStream.flush();
            dStream.close();

            int responseCode=connection.getResponseCode();
            String output="Request URL"+ url;
            output += System.getProperty("line.separator")+"Request Parameters " +urlParameters;
            output += System.getProperty("line.separator")+"Request Code " +responseCode;

            BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line="";
            StringBuilder responseOutput=new StringBuilder();

            while ((line=br.readLine())!=null){
                responseOutput.append(line);
            }
            br.close();
            output +=System.getProperty("line.separator")+responseOutput.toString();
            CreateAccountButton.setText(output);


           }catch (MalformedURLException e){
            e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();  */
            }

    }

}

   /* private void ValidatephoneNumber(final String name, final String phone,final String password, final String email) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);
                    userdataMap.put("email", email);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });  */

