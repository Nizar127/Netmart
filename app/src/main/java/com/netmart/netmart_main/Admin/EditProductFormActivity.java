package com.netmart.netmart_main.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.netmart.netmart_main.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditProductFormActivity extends AppCompatActivity {

    private ImageView imageView, backBtn;
    private EditText inputProdName, inputProdPrice, inputProdDesc;
    private Button confirmChanges;
    private DatabaseReference productsRef;

    private String productID = "";

    private String displayImage, displayName, displayPrice, displayDesc, inputProdNameLower;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_form);

        productID = getIntent().getStringExtra("Product ID");

        imageView = findViewById(R.id.edit_display_product_image);

        inputProdName = findViewById(R.id.edit_input_product_name);
        inputProdPrice = findViewById(R.id.edit_input_product_price);
        inputProdDesc = findViewById(R.id.edit_input_product_desc);

        confirmChanges = findViewById(R.id.edit_confirm_changes_btn);

        backBtn = findViewById(R.id.back_btn_edit_form);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        confirmChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmpty();
            }
        });

        displayProductInfo(imageView, inputProdName, inputProdPrice, inputProdDesc, productID);
    }

    private void checkEmpty() {

        if (TextUtils.isEmpty(inputProdName.getText().toString())) {
            Toast.makeText(this, "Please enter product's name.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(inputProdPrice.getText().toString())) {
            Toast.makeText(this, "Please enter the price",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(inputProdDesc.getText().toString())) {
            Toast.makeText(this, "Please enter the description.",Toast.LENGTH_SHORT).show();
        }
        else{
            confirmChanges();
        }
    }

    private void confirmChanges() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Updating account information...");
        progressDialog.setCanceledOnTouchOutside(false);

        HashMap<String, Object> editMap = new HashMap<>();

        inputProdNameLower = inputProdName.getText().toString();

        editMap.put("name", inputProdName.getText().toString());
        editMap.put("nameLower", inputProdNameLower.toLowerCase());
        editMap.put("price", inputProdPrice.getText().toString());
        editMap.put("description", inputProdDesc.getText().toString());

        productsRef.updateChildren(editMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditProductFormActivity.this, "Changes updated.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProductFormActivity.this, EditProductActivity.class);
                finish();
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProductFormActivity.this, "Error, " + e, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void displayProductInfo(final ImageView imageView, final EditText inputProdName, final EditText inputProdPrice, final EditText inputProdDesc, final String productID) {

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    displayImage = dataSnapshot.child("image").getValue().toString();
                    displayName = dataSnapshot.child("name").getValue().toString();
                    displayPrice = dataSnapshot.child("price").getValue().toString();
                    displayDesc = dataSnapshot.child("description").getValue().toString();

                    Picasso.get().load(displayImage).into(imageView);
                    inputProdName.setText(displayName);
                    inputProdPrice.setText(displayPrice);
                    inputProdDesc.setText(displayDesc);
                }
                catch (Exception e){
                    //Toast.makeText(EditProductFormActivity.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditProductFormActivity.this, EditProductActivity.class);
        finish();
        startActivity(intent);    }
}
