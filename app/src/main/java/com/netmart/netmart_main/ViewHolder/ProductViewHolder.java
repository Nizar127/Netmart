package com.netmart.netmart_main.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netmart.netmart_main.Interface.ItemClickListener;
import com.netmart.netmart_main.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    public TextView txtProductName, txtProductDesc, txtProductPrice;
    public ImageView imageView;
    public ItemClickListener listener;


    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        //replacing the parameter from java class to be display on product_items_layout.xml
        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
        txtProductDesc = (TextView) itemView.findViewById(R.id.product_desc);
    }
}
