package com.netmart.netmart_main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.netmart.netmart_main.Model.Orders;
import com.netmart.netmart_main.Prevalent.Prevalant;
import com.netmart.netmart_main.ViewHolder.ReceiptViewHolder;

import java.text.DecimalFormat;
import java.util.HashMap;

public class fragment_delivered extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference deliveredList;
    DecimalFormat df = new DecimalFormat("#.00");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivered, container, false);

        deliveredList= FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Prevalant.currentOnlineUser.getUsername())
                .child("orderReceipt");

        recyclerView = view.findViewById(R.id.delivered_list);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Orders> options =
                new FirebaseRecyclerOptions.Builder<Orders>()
                        .setQuery(deliveredList.orderByChild("sent").equalTo("Delivered"), Orders.class)
                        .build();

        FirebaseRecyclerAdapter<Orders, ReceiptViewHolder> adapter =
                new FirebaseRecyclerAdapter<Orders, ReceiptViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ReceiptViewHolder holder, int position, @NonNull Orders model) {
                        final String receiptId = getRef(position).getKey();

                        holder.receiptId.setText("Receipt ID : " + receiptId);
                        holder.totalAmt.setText("Total : RM " + df.format(Float.valueOf(model.getTotalAmount())));
                        holder.status.setText("Status : " + model.getSent());

                        deliveredList
                                .child(receiptId)
                                .child("received")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()){
                                            String received = dataSnapshot.getValue().toString();
                                            if (received.equals("Undelivered")){
                                                holder.confirmReceived.setVisibility(View.VISIBLE);
                                                //holder.confirmReceived.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        else {
                                            holder.confirmReceived.setVisibility(View.INVISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                        holder.confirmReceived.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HashMap<String, Object> receivedMap = new HashMap<>();
                                receivedMap.put("received", "Received");
                                deliveredList.child(receiptId).updateChildren(receivedMap);
                            }
                        });

                        holder.receiptDetails.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getContext(), OrderReceiptDetailsActivity.class).putExtra("Receipt ID", receiptId));

                            }
                        });



                    }

                    @NonNull
                    @Override
                    public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delivered_list_items, viewGroup, false);
                        ReceiptViewHolder holder = new ReceiptViewHolder(view);
                        return holder;                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
