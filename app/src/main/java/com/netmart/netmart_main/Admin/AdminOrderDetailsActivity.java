package com.netmart.netmart_main.Admin;

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
import com.netmart.netmart_main.R;
import com.netmart.netmart_main.ViewHolder.CartViewHolder;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class AdminOrderDetailsActivity extends AppCompatActivity {

    private TextView orderDetailsAddress;

    private String orderPrimaryKey = "";
    private RecyclerView orderDetails;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference orderDetailsRef, orderAddress;
    DecimalFormat df = new DecimalFormat("#.00");

    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_details);

        orderDetailsAddress = findViewById(R.id.display_address);

        orderPrimaryKey = getIntent().getStringExtra("Order Primary Key");

        backBtn = findViewById(R.id.back_btn_order_details);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //RecyclerView initialization start
        orderDetails = findViewById(R.id.orders_details_list);
        orderDetails.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        orderDetails.setLayoutManager(layoutManager);
        //RecyclerView initialization end
        orderAddress = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(orderPrimaryKey);

        orderDetailsRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(orderPrimaryKey)
                .child("products");

        displayOrderAddress();
    }

    //RecyclerView adapter
    protected void onStart(){
        super.onStart();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(orderDetailsRef, Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {

                Picasso.get().load(model.getImageUrl()).into(holder.prodImg);
                //To hold the data into the model
                holder.prodName.setText(model.getName());
                holder.prodQuan.setText("Quantity = " + model.getAmount());
                //To round up to 2 dec place, df.format('ur variable here')
                holder.prodPrice.setText("Price = RM " + df.format(Float.valueOf(model.getPrice())));
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items_layout, viewGroup, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        orderDetails.setAdapter(adapter);
        adapter.startListening();
    }


    private void displayOrderAddress() {

        orderAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    String houseNoDisplay = dataSnapshot.child("houseNo").getValue().toString();
                    String streetDisplay = dataSnapshot.child("street").getValue().toString();
                    String houseAreaDisplay = dataSnapshot.child("houseArea").getValue().toString();
                    String cityDisplay = dataSnapshot.child("city").getValue().toString();
                    /*
                    String postcodeDisplay = dataSnapshot.child("postcode").getValue().toString();
                    String stateDisplay = dataSnapshot.child("state").getValue().toString();
                    */

                    orderDetailsAddress.setText("Address: " + houseNoDisplay + ", " + streetDisplay + ", " + houseAreaDisplay + ", " +
                            cityDisplay + ".");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminOrderDetailsActivity.this, AdminNewOrdersActivity.class);
        startActivity(intent);
    }
}
