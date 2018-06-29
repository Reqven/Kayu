package com.reqven.kayu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class HistoryViewAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    private final OnListItemClickListener listener;
    private final ArrayList<Product> products;


    public HistoryViewAdapter(ArrayList<Product> products, OnListItemClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);

        return new HistoryViewHolder(view, parent.getContext());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public void onBindViewHolder(final HistoryViewHolder holder, int position) {

        HistoryViewHolder myHolder = (HistoryViewHolder) holder;
        myHolder.bindValue(products.get(position));

        /*holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               listener.onItemClicked(holder.getAdapterPosition());
           }
       });
    }

    public void addItem(Product product) {
        products.add(product);
    }


    public interface OnListItemClickListener {
        void onItemClicked(int position);
    }
}
