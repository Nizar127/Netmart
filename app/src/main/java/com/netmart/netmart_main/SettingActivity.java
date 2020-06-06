package com.netmart.netmart_main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.netmart.netmart_main.Prevalent.Prevalant;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private CircleImageView profileImg;
    private EditText fullNameEdit, userPhoneEdit, emailEdit;
    private TextView changeBtn, saveBtn;
    private Uri imgUri;
    private String myUrl = "";
    private StorageReference storageProfilePictureReference;
    private String checker = "";
    private StorageTask uploadTask;
    private Button secureQuestBtn;
    private ProgressDialog progressDialog;

    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        storageProfilePictureReference = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        profileImg = findViewById(R.id.settings_profile_image);

        userPhoneEdit = findViewById(R.id.settings_phone_number);
        fullNameEdit = findViewById(R.id.settings_full_name);
        emailEdit = findViewById(R.id.settings_email);

        changeBtn = findViewById(R.id.profile_image_change_btn);
        backBtn = findViewById(R.id.back_btn_setting);
        saveBtn = findViewById(R.id.update_account_settings_btn);

        progressDialog = new ProgressDialog(this);

        //secureQuestBtn = findViewById(R.id.security_quest_btn);

        userInfoDisplay(profileImg, fullNameEdit, userPhoneEdit, emailEdit);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //To change/upload profile picture
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When user click this button/text, it will update the checker from
                //null to clicked which will execute savedBtn
                checker = "clicked";

                CropImage.activity(imgUri)
                        .setAspectRatio(1,1)
                        .start(SettingActivity.this);
            }
        });

        //To update new profile info
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We have two things here because:
                //1) for user who updated both their info and profile picture
                //2) for user who update only their info
                progressDialog.setTitle("Update Profile");
                progressDialog.setMessage("Updating account information...");
                progressDialog.setCanceledOnTouchOutside(false);

                if (checker.equals("clicked")){
                    //this is when user update their profile picture
                    userInfoSaved();
                }
                else {
                    //this is when user only update their info, to prevent picture from being overwrite
                    updateOnlyUserInfo();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imgUri = result.getUri();

            profileImg.setImageURI(imgUri);
        } else {
            Toast.makeText(this, "Error occurred, please try again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettingActivity.this, SettingActivity.class));
            finish();
        }


    }

    private void userInfoDisplay(final CircleImageView profileImg, final EditText fullNameEdit, final EditText userPhoneEdit, final EditText emailEdit) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalant.currentOnlineUser.getUsername());

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name, phone;


                //Display user info only when user image exist
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("image").exists()){

                        String image = dataSnapshot.child("image").getValue().toString();

                        if (dataSnapshot.child("fullname").exists()){
                            name = dataSnapshot.child("fullname").getValue().toString();
                        }
                        else {
                            name = dataSnapshot.child("username").getValue().toString();
                        }

                        phone = dataSnapshot.child("phoneOrder").getValue().toString();
                        //String pass = dataSnapshot.child("password").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();

                        Picasso.get().load(image).into(profileImg);
                        fullNameEdit.setText(name);
                        userPhoneEdit.setText(phone);
                        emailEdit.setText(email);
                    }
                    else if (!dataSnapshot.child("image").exists()) {
                        if (dataSnapshot.child("fullname").exists()) {
                            if (dataSnapshot.child("phoneOrder").exists()){

                                name = dataSnapshot.child("fullname").getValue().toString();
                                phone = dataSnapshot.child("phoneOrder").getValue().toString();

                                fullNameEdit.setText(name);
                                userPhoneEdit.setText(phone);

                            }
                        } else {
                            name = dataSnapshot.child("username").getValue().toString();
                            fullNameEdit.setText(name);
                        }
                    }
                }
                else {
                    Toast.makeText(SettingActivity.this, "Your information is empty.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //For those who only update their profile info without uploading picture, checker== ""
    private void updateOnlyUserInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("phoneOrder", userPhoneEdit.getText().toString().trim());//For contact purposes
        userMap.put("fullname", fullNameEdit.getText().toString().trim());
        userMap.put("email", emailEdit.getText().toString().trim());
        ref.child(Prevalant.currentOnlineUser.getUsername()).updateChildren(userMap);

        progressDialog.dismiss();
        Toast.makeText(SettingActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(SettingActivity.this, HomeActivity.class));
        finish();



    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(fullNameEdit.getText().toString())) {
            Toast.makeText(this, "Please enter your full name.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userPhoneEdit.getText().toString())) {
            Toast.makeText(this, "Please enter your phone number.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(emailEdit.getText().toString())) {
            Toast.makeText(this, "Please enter your email.",Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked")){
            uploadImage();
        }
    }

    private void uploadImage() {
        //Storing image into Firebase Storage
        if (imgUri!=null){
            final StorageReference fileRef = storageProfilePictureReference
                    .child(Prevalant.currentOnlineUser.getUsername() + ".jpg");

            uploadTask = fileRef.putFile(imgUri);

            //What happen if uploadTask is success or not

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        //If not, the app will throw the code exception for debugging
                        throw task.getException();
                    }

                    //Return download url of image as reference
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    //Success
                    if(task.isSuccessful()){

                        //Getting result, which is from the return value above
                       // Uri downloadUrl = task.getResult();
                        //cast to android URI
                        Uri downloadUrl = (Uri) task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();

                        userMap.put("image", myUrl);

                        userMap.put("phoneOrder", userPhoneEdit.getText().toString().trim());//For contact purposes
                        userMap.put("fullname", fullNameEdit.getText().toString().trim());
                        userMap.put("email", emailEdit.getText().toString().trim());
                        //To update the current online user remember me
                        ref.child(Prevalant.currentOnlineUser.getUsername()).updateChildren(userMap);

                        progressDialog.dismiss();

                        Toast.makeText(SettingActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                        finish();
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(SettingActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else{
            Toast.makeText(SettingActivity.this, "Image is not selected", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();    }
}

