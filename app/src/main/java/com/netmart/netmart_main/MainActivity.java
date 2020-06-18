package com.netmart.netmart_main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.netmart.netmart_main.Model.Users;
import com.netmart.netmart_main.Prevalent.Prevalant;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button mainRegistBtn, mainLoginBtn;

    private ProgressDialog loadingBar;
    FirebaseAuth.AuthStateListener mAuthListener;


    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainRegistBtn = findViewById(R.id.main_register_btn);
        mainLoginBtn = findViewById(R.id.main_login_btn);

        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        mainLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        mainRegistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String UserUsername = Paper.book().read(Prevalant.UserUsername);
        String UserPasswordKey = Paper.book().read(Prevalant.UserPasswordKey);

        if(UserUsername != "" && UserPasswordKey != ""){

            if(!TextUtils.isEmpty(UserUsername) && !TextUtils.isEmpty(UserPasswordKey)){

                AllowAccess(UserUsername, UserPasswordKey);

                loadingBar.setTitle("Already Logged In");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }
            }
        };
    }

    private void AllowAccess(final String username, final String password){



//        final DatabaseReference RootRef;
//        RootRef = FirebaseDatabase.getInstance().getReference();
//
//        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child("Users").child(username).exists()){
//
//                    Users usersData = dataSnapshot.child("Users").child(username).getValue(Users.class);
//
//                    if (usersData.getUsername().equals(username)){
//                        if (usersData.getPassword().equals(password)){
//                            if(usersData.getFullname() != null) {
//                                Toast.makeText(MainActivity.this, "Welcome back, " + usersData.getFullname() + "!", Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
//                            }
//                            else {
//                                Toast.makeText(MainActivity.this, "Welcome back, " + usersData.getUsername() + "!", Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
//                            }
//
//                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                            Prevalant.currentOnlineUser = usersData;
//                            //com.example.netmart.Prevalent.Prevalent.currentOnlineUser = usersData;
//                            startActivity(intent);
//                        }
//
//                        else{
//                            Toast.makeText(MainActivity.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
