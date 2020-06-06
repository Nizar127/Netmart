package com.netmart.netmart_main.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netmart.netmart_main.R;

public class ReceiptViewHolder extends RecyclerView.ViewHolder {

    public TextView receiptId, status, totalAmt;
    public Button receiptDetails, confirmReceived;

    public ReceiptViewHolder(@NonNull View itemView) {
        super(itemView);
        receiptId = itemView.findViewById(R.id.receipt_id);
        totalAmt = itemView.findViewById(R.id.totalAmt);
        status = itemView.findViewById(R.id.status);
        receiptDetails = itemView.findViewById(R.id.receipt_details);
        confirmReceived = itemView.findViewById(R.id.confirm_received);
    }
}
