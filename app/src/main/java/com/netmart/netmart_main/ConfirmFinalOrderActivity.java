package com.netmart.netmart_main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.netmart.netmart_main.Prevalent.Prevalant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText name, phone, houseNo, street, houseArea, city, postcode, state;
    private String orderDateTimeKey;
    private Button confirmBtn;
    private String totalAmount;
    private ImageView backBtn;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount = getIntent().getStringExtra("Total Price");

        name = findViewById(R.id.conf_final_order_name);
        phone = findViewById(R.id.conf_final_order_phone);
        houseNo = findViewById(R.id.conf_final_order_house_no);
        street = findViewById(R.id.conf_final_order_street);
        houseArea = findViewById(R.id.conf_final_order_house_area);
        city = findViewById(R.id.conf_final_order_city);


        confirmBtn = findViewById(R.id.conf_final_order_conf_btn);

        backBtn = findViewById(R.id.back_btn_final_order);

        loadingBar = new ProgressDialog(this);

        displayUserData(name, phone, houseNo, street, houseArea, city, postcode, state);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setTitle("Confirming Orders");
                loadingBar.setMessage("Checking the credentials...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                checkUserDeliveryInfo();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void checkUserDeliveryInfo() {
        if (TextUtils.isEmpty(name.getText().toString())) {
            Toast.makeText(this, "Please enter your house number.",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
        else if (TextUtils.isEmpty(phone.getText().toString())) {
            Toast.makeText(this, "Please enter your house number.",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
        else if (TextUtils.isEmpty(houseNo.getText().toString())) {
            Toast.makeText(this, "Please enter your house number.",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
        else if (TextUtils.isEmpty(street.getText().toString())) {
            Toast.makeText(this, "Please enter your street address.",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
        else if (TextUtils.isEmpty(houseArea.getText().toString())) {
            Toast.makeText(this, "Please enter your house area.",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
        else if (TextUtils.isEmpty(city.getText().toString())) {
            Toast.makeText(this, "Please enter your city.",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
        /*
        else if (TextUtils.isEmpty(postcode.getText().toString())) {
            Toast.makeText(this, "Please enter your postcode.",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
        else if (TextUtils.isEmpty(state.getText().toString())) {
            Toast.makeText(this, "Please enter your state.",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
        */
        else{
            confirmOrder();
        }
    }

    private void confirmOrder() {

        final String saveCurrentTime, saveCurrentDate;

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        //As unique key to distinguish with previous orders and
        // to enable users to purchase even after again without overwriting previous order
        orderDateTimeKey = saveCurrentDate + saveCurrentTime;

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(orderDateTimeKey);

        final DatabaseReference orderReceiptRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Prevalant.currentOnlineUser.getUsername())
                .child("orderReceipt")
                .child(orderDateTimeKey);

        //Transfer Firebase node to other node start
        DatabaseReference fromPath = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("User View")
                .child(Prevalant.currentOnlineUser.getUsername())
                .child("Products");

        DatabaseReference toPath = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(orderDateTimeKey)
                .child("products");

        DatabaseReference toUsersPath = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Prevalant.currentOnlineUser.getUsername())
                .child("orderReceipt")
                .child(orderDateTimeKey)
                .child("products");

        //Transfer function start
        moveFirebaseRecord(fromPath, toPath);
        moveToUserOrderReceipt(fromPath, toUsersPath);
        //Transfer Firebase node to other node end

        final HashMap<String, Object> orderMap = new HashMap<>();

        orderMap.put("username", Prevalant.currentOnlineUser.getUsername());
        orderMap.put("totalAmount", totalAmount);
        orderMap.put("name", name.getText().toString().trim());
        orderMap.put("phone", phone.getText().toString().trim());
        orderMap.put("time", saveCurrentTime);
        orderMap.put("date", saveCurrentDate);
        orderMap.put("houseNo", houseNo.getText().toString().trim());
        orderMap.put("street", street.getText().toString().trim());
        orderMap.put("houseArea", houseArea.getText().toString().trim());
        orderMap.put("city", city.getText().toString().trim());
        /*
        orderMap.put("postcode", postcode.getText().toString());
        orderMap.put("state", state.getText().toString());
        */
        orderMap.put("sent", "Currently preparing...");
        orderMap.put("received", "Undelivered");

        orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    orderReceiptRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                loadingBar.dismiss();
                            }
                            else {
                                loadingBar.dismiss();
                                Toast.makeText(ConfirmFinalOrderActivity.this, "Something went wrong, please try again later...", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                else {
                    loadingBar.dismiss();
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Something went wrong, please try again later...", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void moveFirebaseRecord(DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        FirebaseDatabase.getInstance().getReference()
                                .child("Cart List")
                                .child("User View")
                                .child(Prevalant.currentOnlineUser.getUsername())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(ConfirmFinalOrderActivity.this, "Thank you, your order currently being prepared.", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "onCancelled- copy fail", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void moveToUserOrderReceipt(DatabaseReference fromPath, final DatabaseReference toUsersPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toUsersPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Toast.makeText(getApplicationContext(), "COPY FAILED", Toast.LENGTH_LONG).show();
                        }
                        else {
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayUserData(final EditText name, final EditText phone, final EditText houseNo, final EditText street, final EditText houseArea, final EditText city,final EditText postcode, final EditText state) {


        final DatabaseReference userDisplayMap = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalant.currentOnlineUser.getUsername());
        final DatabaseReference addressDisplayMap = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalant.currentOnlineUser.getUsername()).child("address");

        userDisplayMap.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String nameDisplay = dataSnapshot.child("fullname").getValue().toString();
                    String phoneDisplay = dataSnapshot.child("phoneOrder").getValue().toString();

                    name.setText(nameDisplay);
                    phone.setText(phoneDisplay);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addressDisplayMap.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String houseNoDisplay = dataSnapshot.child("houseNo").getValue().toString();
                    String streetDisplay = dataSnapshot.child("street").getValue().toString();
                    String houseAreaDisplay = dataSnapshot.child("houseArea").getValue().toString();
                    String cityDisplay = dataSnapshot.child("city").getValue().toString();
                    /*
                    String postcodeDisplay = dataSnapshot.child("postcode").getValue().toString();
                    String stateDisplay = dataSnapshot.child("state").getValue().toString();
                    */

                    houseNo.setText(houseNoDisplay);
                    street.setText(streetDisplay);
                    houseArea.setText(houseAreaDisplay);
                    city.setText(cityDisplay);
                    /*
                    postcode.setText(postcodeDisplay);
                    state.setText(stateDisplay);
                    */

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ConfirmFinalOrderActivity.this, CartActivity.class);
        startActivity(intent);    }
}
