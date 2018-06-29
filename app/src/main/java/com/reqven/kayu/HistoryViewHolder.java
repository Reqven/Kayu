package com.reqven.kayu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class HistoryViewHolder extends RecyclerView.ViewHolder {
    private TextView name;
    private TextView barcode;

    HistoryViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        barcode = itemView.findViewById(R.id.barcode);
    }

    void bindValue(Product product) {
        name.setText(product.getName());
        barcode.setText(product.getBarcode());
    }
}