package com.reqven.kayu;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class HistoryViewHolder extends RecyclerView.ViewHolder {
    private AppCompatImageView status;
    private TextView name;
    private TextView barcode;
    private Context context;

    HistoryViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        status = itemView.findViewById(R.id.status);
        name = itemView.findViewById(R.id.name);
        barcode = itemView.findViewById(R.id.barcode);
    }

    void bindValue(Product product) {
        if (!product.isFound() || !product.isComplete()) {
            status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_healthy_moderate_24dp));
        } else {
            if (!product.getPassed()) {
                status.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_healthy_bad_24dp));
            }
        }
        name.setText(product.getName());
        barcode.setText(product.getBarcode());
    }
}