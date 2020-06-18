package com.netmart.netmart_main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.netmart.netmart_main.Admin.AdminCategoryActivity;
import com.netmart.netmart_main.Model.Users;
import com.netmart.netmart_main.Prevalent.Prevalant;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText inputUserEmail, inputPass;
    private Button loginBtn;
    private ProgressDialog loadingBar;
    private String parentDbName = "Users";
    private CheckBox rememberMe;
    private TextView adminLink, userLink, hintUser, hintPass, forgetPass;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.login_btn);
        inputUserEmail = findViewById(R.id.login_email_input);
        inputPass = findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);

        hintUser = findViewById(R.id.login_hint_name);
        hintPass = findViewById(R.id.login_hint_pass);

        rememberMe = findViewById(R.id.remember_me);
        forgetPass = findViewById(R.id.forget_password_link);

        inputUserEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    hintUser.setVisibility(View.VISIBLE);
                    inputUserEmail.setHint("");
                }
                else {
                    hintUser.setVisibility(View.GONE);
                    inputUserEmail.setHint("Username");
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

        fAuth = FirebaseAuth.getInstance();

        Paper.init(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtn.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                userLink.setVisibility(View.VISIBLE);
                rememberMe.setVisibility(View.INVISIBLE);
                parentDbName = "Admins";
            }
        });

        userLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtn.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                userLink.setVisibility(View.INVISIBLE);
                rememberMe.setVisibility(View.VISIBLE);
                parentDbName = "Users";
            }
        });

    }

    private void LoginUser() {
        //String username = inputUserName.getText().toString();
        String email = inputUserEmail.getText().toString();
        String password = inputPass.getText().toString();
        int passLength = password.length();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
        }

        else if (passLength<6){
            Toast.makeText(LoginActivity.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
        }

        else{
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Checking the credentials...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(email, password);
        }
    }

    private void AllowAccessToAccount(String email, String password) {

        //allow access is use the firebase auth but we need to update the status the logged on status
        //if login, change the logged on to Available..
        // put authlistener to user all the time so user  just need to login once
        // if user not active for 2 days straight, sign user out and change status to Not Available
        fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                 if(task.isSuccessful()){
                     startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                     finish();
                 } else {
                     Toast.makeText(LoginActivity.this,"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                 }
            }
        });
    }

//    private void AllowAccessToAccount(final String username, final String password) {
//
//        fAuth.signInWithEmailAndPassword(email)
//
//        final DatabaseReference rootRef;
//        rootRef = FirebaseDatabase.getInstance().getReference();
//
//        if(rememberMe.isChecked()){
//            Paper.book().write(Prevalant.UserUsername, username);
//            Paper.book().write(Prevalant.UserPasswordKey, password);
//        }
//
//        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child(parentDbName).child(username).exists()){
//
//                    Users usersData = dataSnapshot.child(parentDbName).child(username).getValue(Users.class);
//
//                    if (usersData.getUsername().equals(username)){
//                        if (usersData.getPassword().equals(password)){
//
//                            if (parentDbName.equals("Admins")){
//                                Toast.makeText(LoginActivity.this, "Welcome, " + usersData.getFullname() + "!", Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
//
//                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
//                                startActivity(intent);
//                            }
//
//
//                        }
//                        else{
//                            Toast.makeText(LoginActivity.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
//                            loadingBar.dismiss();
//                        }
//                    }
//                }
//                else{
//                    Toast.makeText(LoginActivity.this, "Incorrect username.", Toast.LENGTH_SHORT).show();
//                    loadingBar.dismiss();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(LoginActivity.this, "Account doesn't exist.", Toast.LENGTH_SHORT).show();
//                loadingBar.dismiss();
//            }
//        });
//

   // }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);    }
}
