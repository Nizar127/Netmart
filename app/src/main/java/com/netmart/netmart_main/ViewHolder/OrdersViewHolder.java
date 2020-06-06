package com.netmart.netmart_main.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netmart.netmart_main.R;

public class OrdersViewHolder extends RecyclerView.ViewHolder {

    public TextView name, phone, totalPrice, dateTime, details, delivery;

    public OrdersViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.orderer_name);
        phone = itemView.findViewById(R.id.orderer_phone);
        totalPrice = itemView.findViewById(R.id.orderer_total_price);
        dateTime = itemView.findViewById(R.id.orderer_date_time);
        details = itemView.findViewById(R.id.show_orderer_details);
        delivery = itemView.findViewById(R.id.deliver_btn);
    }
}
