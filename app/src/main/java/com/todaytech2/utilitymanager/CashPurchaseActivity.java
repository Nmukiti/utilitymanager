package com.todaytech2.utilitymanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

public class CashPurchaseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner7;
    private EditText meterno2,amount2,phoneno2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_purchase);
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
}
