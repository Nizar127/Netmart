package com.netmart.netmart_main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
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
    private TextView prodDesc, prodName, prodPrice, prodCategory;
    private String productID = "";
    private String productPrice;
    private String prodImgUrl;
    private String UserID, productTime;
    //private String statusID;
    private CollectionReference productDetailRef;
    private FirebaseFirestore fcart;
    Uri imageUri = null;
    private FirebaseAuth fAuth;
    StorageReference storageReference;

    Bitmap compressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("Pid");

        addToCartBtn = findViewById(R.id.add_to_cart_btn);
        numberButton = findViewById(R.id.number_btn);
        prodImg = findViewById(R.id.product_image_detail);
        prodName = findViewById(R.id.product_name_detail);
        prodDesc = findViewById(R.id.product_desc_detail);
        prodPrice = findViewById(R.id.product_price_detail);
        prodCategory = findViewById(R.id.product_category_detail);

        backBtn = findViewById(R.id.back_btn_product_details);

        fAuth = FirebaseAuth.getInstance();
        fcart = FirebaseFirestore.getInstance();

        UserID = fAuth.getCurrentUser().getUid();

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
                addToCartList();
            }
        });
    }

    private void addToCartList() {

        String saveCurrentTime, saveCurrentDate, delivererId;

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        //get datetime
        productTime = saveCurrentTime + saveCurrentDate;

        //final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        // cartListRef = FirebaseFirestore.getInstance();

        String totalProdAmt = numberButton.getNumber();
        float sendTotalAmt = Float.valueOf(totalProdAmt) * Float.valueOf(productPrice);

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("Pid", productID);
        cartMap.put("Uid",UserID);
        cartMap.put("Name", prodName.getText().toString());
        cartMap.put("Price", String.valueOf(sendTotalAmt));
        cartMap.put("Amount", numberButton.getNumber());
        cartMap.put("Date", saveCurrentDate);
        cartMap.put("Time", saveCurrentTime);
        cartMap.put("Discount", "");
        cartMap.put("ImageUrl", prodImgUrl);


        //fcart.collection("Orders").getId(UserID).document("Products").set(productID).update(cartMap).orderBy(productTime);
        fcart.collection("Orders").document("Products").update(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //if data is update to products path in collection order, it's time to send data to deliverer
                    CollectionReference deliveredRef = fcart.collection("Orders");
                    deliveredRef.document("Deliverer").set(productID).isComplete();
                }
            }
        });
//        cartListRef.child("User View")
//                .child(Prevalant.currentOnlineUser.getUsername())
//                .child("Products")
//                .child(productID)
//                .updateChildren(cartMap)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(ProductDetailsActivity.this, "Added to Cart List", Toast.LENGTH_SHORT).show();
//
//                            Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
//                            startActivity(intent);
//                        }
//                    }
//                });

    }

    private void getProductDetails(String productID) {

        productDetailRef = FirebaseFirestore.getInstance().collection("Products");
        productDetailRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (UserID.equals(documentSnapshot.getId())) {
                        String prodImgProfile = documentSnapshot.getString("productImage");
                        if(prodImgProfile != null){
                            Picasso.get().load(prodImgProfile).into(prodImg);
                        }
                        prodName.setText(documentSnapshot.getString("name"));
                        prodDesc.setText(documentSnapshot.getString("description"));
                        prodPrice.setText(documentSnapshot.getString("price"));
                        prodCategory.setText(documentSnapshot.getString("category"));
                        String statusID = documentSnapshot.getString("qty");
                        if(statusID != null){
                            //update the status if qty is not zero..change to available
                         //   statusID = documentSnapshot.getString("status" ? )
                        }
                    }
                }
            }
        });
//        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
//
//        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    Products products = dataSnapshot.getValue(Products.class);
//
//                    productPrice = products.getPrice();
//
//                    prodName.setText(products.getName());
//                    prodDesc.setText(products.getDescription());
//                    prodPrice.setText(products.getPrice());
//                    prodImgUrl = products.getImage();
//                    Picasso.get().load(products.getImage()).into(prodImg);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}

