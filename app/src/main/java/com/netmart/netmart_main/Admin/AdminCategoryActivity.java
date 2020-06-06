package com.netmart.netmart_main.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.netmart.netmart_main.MainActivity;
import com.netmart.netmart_main.R;

public class AdminCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView chicken, meat, vegetable, dairy,
            fruit, grain, noodle, spices,
            frozen, bakery, beverage, seafood;
    private Button logOutBtn, checkOrderBtn, editProductsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        logOutBtn = findViewById(R.id.admin_logout_btn);
        checkOrderBtn = findViewById(R.id.check_orders_btn);
        editProductsBtn = findViewById(R.id.edit_products_btn);

        chicken = findViewById(R.id.chicken);
        meat =  findViewById(R.id.meat);
        vegetable = findViewById(R.id.vegetable);
        dairy = findViewById(R.id.dairy);

        fruit = findViewById(R.id.fruit);
        grain = findViewById(R.id.grain);
        noodle = findViewById(R.id.noodle);
        spices = findViewById(R.id.spices);

        frozen = findViewById(R.id.frozen);
        bakery = findViewById(R.id.bakery);
        beverage = findViewById(R.id.beverage);
        seafood = findViewById(R.id.seafood);

        chicken.setOnClickListener(this);
        meat.setOnClickListener(this);
        vegetable.setOnClickListener(this);
        dairy.setOnClickListener(this);

        fruit.setOnClickListener(this);
        grain.setOnClickListener(this);
        noodle.setOnClickListener(this);
        spices.setOnClickListener(this);

        frozen.setOnClickListener(this);
        bakery.setOnClickListener(this);
        beverage.setOnClickListener(this);
        seafood.setOnClickListener(this);

        editProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, EditProductActivity.class);
                startActivity(intent);
            }
        });

        checkOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                startActivity(intent);
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

        switch (v.getId()) {

            case R.id.chicken:
                intent.putExtra("category", "chicken");
                startActivity(intent);
                break;
            case R.id.meat:
                intent.putExtra("category", "meat");
                startActivity(intent);
                break;
            case R.id.vegetable:
                intent.putExtra("category", "vegetable");
                startActivity(intent);
                break;
            case R.id.dairy:
                intent.putExtra("category", "dairy");
                startActivity(intent);
                break;
            case R.id.fruit:
                intent.putExtra("category", "fruits");
                startActivity(intent);
                break;
            case R.id.grain:
                intent.putExtra("category", "grains");
                startActivity(intent);
                break;
            case R.id.noodle:
                intent.putExtra("category", "noodles");
                startActivity(intent);
                break;
            case R.id.spices:
                intent.putExtra("category", "spices");
                startActivity(intent);
                break;
            case R.id.frozen:
                intent.putExtra("category", "frozen");
                startActivity(intent);
                break;
            case R.id.bakery:
                intent.putExtra("category", "bakery");
                startActivity(intent);
                break;
            case R.id.beverage:
                intent.putExtra("category", "beverage");
                startActivity(intent);
                break;
            case R.id.seafood:
                intent.putExtra("category", "seafood");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        CharSequence options[] = new CharSequence[]{
                "Yes", "No"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminCategoryActivity.this);
        builder.setTitle("Log Out??");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //If admins press Yes
                if (which == 0){
                    Toast.makeText(AdminCategoryActivity.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                //else admins pressed No
                else {
                }
            }
        });
        builder.show();
    }
}
