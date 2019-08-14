package com.todaytech2.utilitymanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button creditServices,borrowCash,cashPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        creditServices = (Button) findViewById(R.id.credit_services);
        borrowCash = (Button) findViewById(R.id.borrow_cash);
        cashPurchase = (Button) findViewById(R.id.cash_purchase);

        creditServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(HomeActivity.this, CreditServicesActivity.class);
                startActivity(intent);
            }
        });
        borrowCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(HomeActivity.this, BorrowCashActivity.class);
                startActivity(intent);
            }
        });
        cashPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(HomeActivity.this,CashPurchaseActivity.class);
                startActivity(intent);
            }
        });
    }
}
