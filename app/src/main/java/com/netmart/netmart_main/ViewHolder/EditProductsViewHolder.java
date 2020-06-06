package com.netmart.netmart_main.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.netmart.netmart_main.R;

public class EditProductsViewHolder extends RecyclerView.ViewHolder {

    public TextView editProdName, editProdDesc, editProdPrice;
    public ImageView editImgView;
    public FloatingActionButton editProd, removeProd;

    public EditProductsViewHolder(@NonNull View itemView) {
        super(itemView);
        editImgView = itemView.findViewById(R.id.edit_prod_img);
        editProdName = itemView.findViewById(R.id.edit_prod_name);
        editProdPrice = itemView.findViewById(R.id.edit_prod_price);
        editProdDesc = itemView.findViewById(R.id.edit_prod_desc);
        editProd = itemView.findViewById(R.id.edit_product_details_btn);
        removeProd = itemView.findViewById(R.id.edit_remove_products_btn);
    }
}
