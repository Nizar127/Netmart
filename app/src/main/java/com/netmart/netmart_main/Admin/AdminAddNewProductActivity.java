package com.netmart.netmart_main.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.netmart.netmart_main.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private TextView getCategory;

    private String categoryName, desc, price, name, saveCurrentDate, saveCurrentTime, productRandomKey, downloadImgUrl;
    private Button addNewProdBtn;
    private ImageView inputProdImg, backBtn;
    private EditText inputProdName, inputProdDesc, inputProdPrice;
    private static final int GalleryPick = 1;
    private Uri imgUri;
    private StorageReference prodImgRef;
    private DatabaseReference prodRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);


        categoryName = getIntent().getExtras().get("category").toString();
        prodImgRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        prodRef = FirebaseDatabase.getInstance().getReference().child("Products");

        addNewProdBtn = findViewById(R.id.add_new_product);

        inputProdImg = findViewById(R.id.select_product_image);
        backBtn = findViewById(R.id.back_btn_add_new);
        inputProdName = findViewById(R.id.product_name);
        inputProdDesc = findViewById(R.id.product_desc);
        inputProdPrice = findViewById(R.id.product_price);
        getCategory = findViewById(R.id.get_category);

        getCategory.setText(categoryName.toUpperCase());

        loadingBar = new ProgressDialog(this);

        inputProdImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openGallery()
                CropImage.activity(imgUri)
                        .setAspectRatio(1,1)
                        .start(AdminAddNewProductActivity.this);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addNewProdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imgUri = result.getUri();

            inputProdImg.setImageURI(imgUri);
        }
        else{
            Toast.makeText(this, "Error occurred, please try again.", Toast.LENGTH_SHORT).show();
        }

    }

    private void ValidateProductData() {

        name = inputProdName.getText().toString().trim();
        desc = inputProdDesc.getText().toString().trim();
        price = inputProdPrice.getText().toString().trim();

        if (imgUri == null) {
            Toast.makeText(this, "Product image required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Product name required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(desc)) {
            Toast.makeText(this, "Product description required", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Product price required", Toast.LENGTH_SHORT).show();
        }
        else {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {
        loadingBar.setTitle("Adding New Product");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //To create a unique product random key, so that it doesn't overwrite other product
        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = prodImgRef.child(imgUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imgUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "Product Image uploaded", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImgUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImgUrl = task.getResult().toString();

                            Toast.makeText(AdminAddNewProductActivity.this, "Product image url received", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("image", downloadImgUrl);
        productMap.put("category", categoryName);
        productMap.put("name", name);
        productMap.put("nameLower", name.toLowerCase());
        productMap.put("description", desc);
        productMap.put("price", price);

        prodRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);
                            finish();

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
        startActivity(intent);
        finish();
    }
}
