package com.reqven.kayu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class HistoryViewHolder extends RecyclerView.ViewHolder {
    private TextView content;

    HistoryViewHolder(View itemView) {
        super(itemView);
        content = itemView.findViewById(R.id.content);
    }

    void bindValue(String text) {
        content.setText(text);
    }
}