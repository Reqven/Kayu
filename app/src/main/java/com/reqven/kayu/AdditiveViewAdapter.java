package com.reqven.kayu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class AdditiveViewAdapter extends RecyclerView.Adapter<AdditiveViewHolder> {

    protected ArrayList<FragmentProduct.AdditiveList> additives;
    private Context context;


    public AdditiveViewAdapter(ArrayList<FragmentProduct.AdditiveList> additives, Context context) {
        this.additives = additives;
        this.context = context;
    }

    @Override
    public AdditiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int layout = R.layout.additive_item;

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new AdditiveViewHolder(view, context);
    }

    @Override
    public int getItemCount() {
        return additives.size();
    }

    @Override
    public void onBindViewHolder(final AdditiveViewHolder holder, int position) {
        holder.setAdditive(additives.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}
