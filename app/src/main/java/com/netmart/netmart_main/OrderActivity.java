package com.netmart.netmart_main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OrderActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ImageView backBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        backBtn = findViewById(R.id.back_btn_order_receipt);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bottomNavigationView = findViewById(R.id.bot_nav_order);
        bottomNavigationView.setOnNavigationItemSelectedListener(navBottomListener);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new fragment_undelivered()).commit();
        }


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OrderActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navBottomListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.delivered:
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new fragment_delivered()).commit();
                            return true;
                        case R.id.undelivered:
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new fragment_undelivered()).commit();
                            return true;
                    }
                    return false;
                }
            };
}
