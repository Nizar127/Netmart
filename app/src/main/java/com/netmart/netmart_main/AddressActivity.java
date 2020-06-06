package com.netmart.netmart_main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.netmart.netmart_main.Prevalent.Prevalant;

public class AddressActivity extends AppCompatActivity {

    private EditText houseNo, street, houseArea, city/*, postcode, state*/;
    private TextView updateBtn;
    private ImageView backBtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        houseNo = findViewById(R.id.address_house_no);
        street = findViewById(R.id.address_street);
        houseArea = findViewById(R.id.address_house_area);
        city = findViewById(R.id.address_city);
        /*postcode = findViewById(R.id.address_postcode);
        state = findViewById(R.id.address_state);*/

        backBtn = findViewById(R.id.back_btn_address);
        updateBtn = findViewById(R.id.update_account_address_btn);

        userAddressDisplay(houseNo, street, houseArea, city /*postcode, state*/);
        
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserAddress();
            }
        });

    }

    private void saveUserAddress() {
    }

    private void userAddressDisplay(final EditText houseNo, final EditText street, final EditText houseArea, final EditText city) {
        final DatabaseReference addressDisplayMap = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalant.currentOnlineUser.getUsername()).child("address");

        addressDisplayMap.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    String houseNoDisplay = dataSnapshot.child("houseNo").getValue().toString();
                    String streetDisplay = dataSnapshot.child("street").getValue().toString();
                    String houseAreaDisplay = dataSnapshot.child("houseArea").getValue().toString();
                    String cityDisplay = dataSnapshot.child("city").getValue().toString();
                    /*String postcodeDisplay = dataSnapshot.child("postcode").getValue().toString();
                    String stateDisplay = dataSnapshot.child("state").getValue().toString();*/

                    houseNo.setText(houseNoDisplay);
                    street.setText(streetDisplay);
                    houseArea.setText(houseAreaDisplay);
                    city.setText(cityDisplay);
                    //street.setText(streetDisplay);
                    //houseArea.setText(houseAreaDisplay);
                    //city.setText(cityDisplay);
                    /*postcode.setText(postcodeDisplay);
                    state.setText(stateDisplay);*/

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
