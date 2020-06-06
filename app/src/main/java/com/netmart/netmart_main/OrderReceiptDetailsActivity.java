package com.netmart.netmart_main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.netmart.netmart_main.Model.Cart;
import com.netmart.netmart_main.Prevalent.Prevalant;
import com.netmart.netmart_main.ViewHolder.CartViewHolder;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class OrderReceiptDetailsActivity extends AppCompatActivity {

    private String orderReceiptId;
    private ImageView backBtn;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference userDetailsRef, receiptDetailsRef;
    private TextView receiptIdDetails, receiptPhoneDetails, receiptAddressDetails, receiptTotalDetails, receiptDateTime;
    DecimalFormat df = new DecimalFormat("#.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_receipt_details);

        orderReceiptId = getIntent().getStringExtra("Receipt ID");

        receiptIdDetails = findViewById(R.id.receipt_id_details);
        receiptIdDetails.setText("Receipt ID : " + orderReceiptId);

        receiptPhoneDetails = findViewById(R.id.receipt_phone_details);
        receiptAddressDetails = findViewById(R.id.receipt_address_details);
        receiptTotalDetails = findViewById(R.id.receipt_total_details);
        receiptDateTime = findViewById(R.id.receipt_date_time_details);

        backBtn = findViewById(R.id.back_btn_receipt_details);

        recyclerView = findViewById(R.id.receipt_details_list);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        userDetailsRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Prevalant.currentOnlineUser.getUsername())
                .child("orderReceipt")
                .child(orderReceiptId);

        receiptDetailsRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Prevalant.currentOnlineUser.getUsername())
                .child("orderReceipt")
                .child(orderReceiptId)
                .child("products");

        displayUserDetails();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(receiptDetailsRef, Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items_layout, viewGroup, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                Picasso.get().load(model.getImageUrl()).into(holder.prodImg);
                //To hold the data into the model
                holder.prodName.setText(model.getName());
                holder.prodQuan.setText("Quantity = " + model.getAmount());
                //To round up to 2 dec place, df.format('ur variable here')
                holder.prodPrice.setText("Price = RM " + df.format(Float.valueOf(model.getPrice())));
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void displayUserDetails() {

        userDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String phone = dataSnapshot.child("phone").getValue().toString();
                    String total = dataSnapshot.child("totalAmount").getValue().toString();

                    String date = dataSnapshot.child("date").getValue().toString();
                    String time = dataSnapshot.child("time").getValue().toString();

                    String houseNoDisplay = dataSnapshot.child("houseNo").getValue().toString();
                    String streetDisplay = dataSnapshot.child("street").getValue().toString();
                    String houseAreaDisplay = dataSnapshot.child("houseArea").getValue().toString();
                    String cityDisplay = dataSnapshot.child("city").getValue().toString();
                    /*
                    String postcodeDisplay = dataSnapshot.child("postcode").getValue().toString();
                    String stateDisplay = dataSnapshot.child("state").getValue().toString();
                    */

                    receiptDateTime.setText("Date/Time : " + date + " / " + time);
                    receiptPhoneDetails.setText("Phone : " + phone);
                    String completeAdd = ("Address : \n" + houseNoDisplay + ", " + streetDisplay + ", " + houseAreaDisplay + ", " +
                            cityDisplay + ", " /*+ postcodeDisplay + ", " + stateDisplay*/);

                    receiptAddressDetails.setText(completeAdd);

                    receiptTotalDetails.setText("Total : RM " + df.format(Float.valueOf(total)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OrderReceiptDetailsActivity.this, OrderActivity.class);
        startActivity(intent);
    }
}
