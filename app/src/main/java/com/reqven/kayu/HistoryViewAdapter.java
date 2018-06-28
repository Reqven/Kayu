package com.reqven.kayu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class HistoryViewAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    private final OnListItemClickListener listener;
    private final ArrayList<String> values;


    public HistoryViewAdapter(ArrayList<String> values, OnListItemClickListener listener) {
        this.values = values;
        this.listener = listener;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);

        return new HistoryViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    @Override
    public void onBindViewHolder(final HistoryViewHolder holder, int position) {

        HistoryViewHolder myHolder = (HistoryViewHolder) holder;
        myHolder.bindValue(values.get(position));

        /*holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               listener.onItemClicked(holder.getAdapterPosition());
           }
       });
    }

    public void addItem(String input) {
        values.add(input);
    }


    public interface OnListItemClickListener {
        void onItemClicked(int position);
    }
}
