package com.netmart.netmart_main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccBtn;
    private TextView hintName, hintPhone, hintEmail, hintPass, hintRepass;
    private EditText inputName, inputEmail, inputPhoneNo, inputPass, inputRepass;
    private ProgressDialog loadingBar;
    private FirebaseAuth firebaseAuth;
    private  FirebaseFirestore fStore;
    private String user_id;
    private Boolean status;

    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputName = findViewById(R.id.register_username_input);
        inputEmail = findViewById(R.id.register_email_input);
        inputPhoneNo = findViewById(R.id.register_phone_number_input);
        inputPass = findViewById(R.id.register_password_input);
        inputRepass = findViewById(R.id.register_repass_input);

        hintName = findViewById(R.id.register_hint_name);
        hintEmail = findViewById(R.id.register_hint_email);
        hintPhone = findViewById(R.id.register_hint_phone);
        hintPass = findViewById(R.id.register_hint_pass);
        hintRepass = findViewById(R.id.register_hint_repass);


        backBtn = findViewById(R.id.back_to_main_btn);

        firebaseAuth = FirebaseAuth.getInstance();

        user_id = firebaseAuth.getCurrentUser().getUid();

        fStore = FirebaseFirestore.getInstance();

       // LoginAccBtn = findViewById(R.id.main_login_btn);

        createAccBtn = findViewById(R.id.register_btn);

        loadingBar = new ProgressDialog(this);

        inputName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    hintName.setVisibility(View.VISIBLE);
                    inputName.setHint("");
                }
                else {
                    hintName.setVisibility(View.GONE);
                    inputName.setHint("Username");
                }
            }
        });

        inputEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    hintEmail.setVisibility(View.VISIBLE);
                    inputEmail.setHint("");
                }
                else {
                    hintEmail.setVisibility(View.GONE);
                    inputEmail.setHint("Email");
                }
            }
        });

        inputPhoneNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    hintPhone.setVisibility(View.VISIBLE);
                    inputPhoneNo.setHint("");
                }
                else {
                    hintPhone.setVisibility(View.GONE);
                    inputPhoneNo.setHint("Phone Number");
                }
            }
        });

        inputPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    hintPass.setVisibility(View.VISIBLE);
                    inputPass.setHint("");
                }
                else {
                    hintPass.setVisibility(View.GONE);
                    inputPass.setHint("Password");
                }
            }
        });

        inputRepass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    hintRepass.setVisibility(View.VISIBLE);
                    inputRepass.setHint("");
                }
                else {
                    hintRepass.setVisibility(View.GONE);
                    inputRepass.setHint("Retype Password");
                }
            }
        });



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));

            }
        });

        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {

        String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String phone = inputPhoneNo.getText().toString().trim();
        String password = inputPass.getText().toString().trim();
        String repass = inputRepass.getText().toString().trim();
        int passLength = password.length();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please enter your phone number.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(repass)){
            Toast.makeText(this, "Please re-enter your password.", Toast.LENGTH_SHORT).show();
        }
        else if (passLength<6){
            Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
        }
        else if (!(password.equals(repass))) {
            Toast.makeText(RegisterActivity.this, "Your password didn't match.", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Checking the credentials...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateUserData(name, email, phone, password);
        }
    }

    private void ValidateUserData(final String name, final String email, final String phone, final String password) {


        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    saveUserData(name,email,phone,password);
                    //if the data is save, then user is bring to new activity
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                } else{
                    Toast.makeText(RegisterActivity.this, "Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveUserData(final String name, final String email, final String phone, final String password) {
        //check whether there are existing data in collection

        //status = firebaseAuth.equals()

        fStore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        Toast.makeText(RegisterActivity.this, "You already have your data Stored in database",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        //save data in collection


                        Map<String, String> userData = new HashMap<>();
                        userData.put("Username", name);
                        userData.put("Fullname", name);
                        userData.put("Email", email);
                        userData.put("PhoneOrder", phone);
                        userData.put("Password", password);
                      //  userData.put("Status", String.valueOf(status));

                        fStore.collection("Users").document(user_id).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "User Data is Stored Successfully", Toast.LENGTH_LONG).show();
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                }
            }
        });
    }


//    private void ValidateUserData(final String name, final email, final String phone, final String password) {
//
//        if(password.equals(inputRepass)){
//            firebaseAuth.createUserWithEmailAndPassword(emai)
//        }

//        Map<String, String> userData = new HashMap<>();
//        userData.put("username", name);
//        userData.put("fullname", name);
//        userData.put("phoneOrder", phone);
//        userData.put("password", password);
//
//        fStore.collection("Users").add(userData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentReference> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(RegisterActivity.this, "Account Created!" + name, Toast.LENGTH_SHORT).show();
//                      loadingBar.dismiss();
//                      Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                      startActivity(intent);
//                } else{
//                    loadingBar.dismiss();
//                    Toast.makeText(RegisterActivity.this, "We're sorry, Something happen, please try again.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        })

        //                final DatabaseReference rootRef;
//        rootRef = FirebaseDatabase.getInstance().getReference();

//        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (!(dataSnapshot.child("Users").child(name).exists())){
//
//                    HashMap<String, Object> userdataMap = new HashMap<>();
//
//                    userdataMap.put("username", name);
//                    userdataMap.put("fullname", name);
//                    userdataMap.put("phoneOrder", phone);
//                    userdataMap.put("password", password);
//                    //userdataMap.put("phoneOrder", phone);
//                    //userdataMap.put("password", password);
//
//                    //.child("Users") - create new child/table in db with "Users" as name
//                    //.child(name) - create a child under Users child/table in db with named after user's name number
//                    rootRef.child("Users").child(name).updateChildren(userdataMap)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()){
//                                        Toast.makeText(RegisterActivity.this, "Account Created!" + name, Toast.LENGTH_SHORT).show();
//                                        loadingBar.dismiss();
//
//                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                        startActivity(intent);
//                                    }
//
//                                    else {
//                                        loadingBar.dismiss();
//                                        Toast.makeText(RegisterActivity.this, "We're sorry, Something happen, please try again.", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                }
//                else{
//                    Toast.makeText(RegisterActivity.this, "This username already exists.", Toast.LENGTH_SHORT).show();
//                    loadingBar.dismiss();
//                    Toast.makeText(RegisterActivity.this, "Please use other username.", Toast.LENGTH_SHORT).show();
//
//                    /*
//                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    */
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
   // }
//
//    private void CreateAccount() {
//
//        String name = inputName.getText().toString().trim();
//        String phone = inputPhoneNo.getText().toString().trim();
//        String password = inputPass.getText().toString().trim();
//        String repass = inputRepass.getText().toString().trim();
//        int passLength = password.length();
//
//        if (TextUtils.isEmpty(name)){
//            Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show();
//        }
//
//        else if (TextUtils.isEmpty(phone)){
//            Toast.makeText(this, "Please enter your phone number.", Toast.LENGTH_SHORT).show();
//        }
//
//        else if (TextUtils.isEmpty(password)){
//            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
//        }
//
//        else if (TextUtils.isEmpty(repass)){
//            Toast.makeText(this, "Please re-enter your password.", Toast.LENGTH_SHORT).show();
//        }
//
//        else if (passLength<6){
//            Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
//        }
//
//        else if (!(password.equals(repass))) {
//            Toast.makeText(RegisterActivity.this, "Your password didn't match.", Toast.LENGTH_SHORT).show();
//        }
//
//        else{
//            loadingBar.setTitle("Create Account");
//            loadingBar.setMessage("Checking the credentials...");
//            loadingBar.setCanceledOnTouchOutside(false);
//            loadingBar.show();
//
//            ValidatePhoneNumber(name, phone, password);
//        }
//    }
//
//    private void ValidatePhoneNumber(final String name, final String phone, final String password) {
//
//        final DatabaseReference rootRef;
//        rootRef = FirebaseDatabase.getInstance().getReference();
//
//        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (!(dataSnapshot.child("Users").child(name).exists())){
//
//                    HashMap<String, Object> userdataMap = new HashMap<>();
//
//                    userdataMap.put("username", name);
//                    userdataMap.put("fullname", name);
//                    userdataMap.put("phoneOrder", phone);
//                    userdataMap.put("password", password);
//                    //userdataMap.put("phoneOrder", phone);
//                    //userdataMap.put("password", password);
//
//                    //.child("Users") - create new child/table in db with "Users" as name
//                    //.child(name) - create a child under Users child/table in db with named after user's name number
//                    rootRef.child("Users").child(name).updateChildren(userdataMap)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()){
//                                        Toast.makeText(RegisterActivity.this, "Account Created!" + name, Toast.LENGTH_SHORT).show();
//                                        loadingBar.dismiss();
//
//                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                        startActivity(intent);
//                                    }
//
//                                    else {
//                                        loadingBar.dismiss();
//                                        Toast.makeText(RegisterActivity.this, "We're sorry, Something happen, please try again.", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                }
//                else{
//                    Toast.makeText(RegisterActivity.this, "This username already exists.", Toast.LENGTH_SHORT).show();
//                    loadingBar.dismiss();
//                    Toast.makeText(RegisterActivity.this, "Please use other username.", Toast.LENGTH_SHORT).show();
//
//                    /*
//                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    */
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
