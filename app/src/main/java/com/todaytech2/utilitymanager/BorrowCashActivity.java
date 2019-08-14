package com.todaytech2.utilitymanager;

import android.app.ProgressDialog;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class BorrowCashActivity extends AppCompatActivity {
    private Button submitButton1;
    private EditText InputPhoneNumber, InputAmount;
    private Spinner spinnerid;
    private ProgressDialog loadingBar;
    private DatabaseReference demoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_cash);
        submitButton1 = (Button) findViewById(R.id.submit_btn1);
        InputAmount = (EditText) findViewById(R.id.amount);
        InputPhoneNumber = (EditText) findViewById(R.id.phone_number);
        loadingBar = new ProgressDialog(this);

    }
}
