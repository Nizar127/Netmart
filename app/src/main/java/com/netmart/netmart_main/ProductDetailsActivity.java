package com.netmart.netmart_main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.netmart.netmart_main.Model.Products;
import com.netmart.netmart_main.Prevalent.Prevalant;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addToCartBtn;
    private ImageView prodImg, backBtn;
    private ElegantNumberButton numberButton;
    private TextView prodDesc, prodName, prodPrice;
    private String productID = "";
    private String productPrice;
    private String prodImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");

        addToCartBtn = findViewById(R.id.add_to_cart_btn);
        numberButton = findViewById(R.id.number_btn);
        prodImg = findViewById(R.id.product_image_detail);
        prodName = findViewById(R.id.product_name_detail);
        prodDesc = findViewById(R.id.product_desc_detail);
        prodPrice = findViewById(R.id.product_price_detail);

        backBtn = findViewById(R.id.back_btn_product_details);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getProductDetails(productID);

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCarList();
            }
        });
    }

    private void addToCarList() {

        String saveCurrentTime, saveCurrentDate;

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        String totalProdAmt = numberButton.getNumber();
        float sendTotalAmt = Float.valueOf(totalProdAmt) * Float.valueOf(productPrice);

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("name", prodName.getText().toString());
        cartMap.put("price", String.valueOf(sendTotalAmt));
        cartMap.put("amount", numberButton.getNumber());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("discount", "");
        cartMap.put("imageUrl", prodImgUrl);

        cartListRef.child("User View")
                .child(Prevalant.currentOnlineUser.getUsername())
                .child("Products")
                .child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProductDetailsActivity.this, "Added to Cart List", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }
                });

    }

    private void getProductDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Products products = dataSnapshot.getValue(Products.class);

                    productPrice = products.getPrice();

                    prodName.setText(products.getName());
                    prodDesc.setText(products.getDescription());
                    prodPrice.setText(products.getPrice());
                    prodImgUrl = products.getImage();
                    Picasso.get().load(products.getImage()).into(prodImg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}

