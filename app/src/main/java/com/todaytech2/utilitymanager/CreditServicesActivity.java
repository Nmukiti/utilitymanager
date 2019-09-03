package com.todaytech2.utilitymanager;

import android.app.MediaRouteButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CreditServicesActivity extends AppCompatActivity implements OnItemSelectedListener {
    private Spinner spinner2, spinner3, spinner4, spinner5, spinner6;
    private EditText meterno, amount, paybillno, accountno, tillno, phoneno, option;
    private MediaRouteButton editTextObject;

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
        paybillno = (EditText) findViewById(R.id.paybill_no);
        accountno = (EditText) findViewById(R.id.acc_no);
        tillno = (EditText) findViewById(R.id.till_no);


        spinner4.setOnItemSelectedListener(this);

    }

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
            amount.setVisibility(View.VISIBLE);
            meterno.setVisibility(View.INVISIBLE);
            spinner6.setVisibility(View.INVISIBLE);
            //spinner5.setVisibility(View.VISIBLE);
            //paybillno.setVisibility(View.VISIBLE);
            //accountno.setVisibility(View.VISIBLE);
            tillno.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
