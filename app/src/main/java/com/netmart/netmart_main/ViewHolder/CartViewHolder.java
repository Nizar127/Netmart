package com.netmart.netmart_main.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netmart.netmart_main.Interface.ItemClickListener;
import com.netmart.netmart_main.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView prodImg;
    public TextView prodName, prodPrice, prodQuan;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        prodImg = itemView.findViewById(R.id.cart_prod_img);
        prodName = itemView.findViewById(R.id.cart_prod_name);
        prodQuan = itemView.findViewById(R.id.cart_prod_quan);
        prodPrice = itemView.findViewById(R.id.cart_prod_price);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);

    }
}
