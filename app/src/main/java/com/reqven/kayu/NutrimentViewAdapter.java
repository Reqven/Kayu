package com.reqven.kayu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class NutrimentViewAdapter extends RecyclerView.Adapter<NutrimentViewHolder> {

    private final ArrayList<Nutriment> nutriments;
    private Context context;
    public final int TYPE_DIVIDER = 0;
    public final int TYPE_NO_DIVIDER = 1;


    public NutrimentViewAdapter(ArrayList<Nutriment> nutriments, Context context) {
        this.nutriments = nutriments;
        this.context = context;
    }

    @Override
    public NutrimentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int layout = R.layout.nutriment_item_divider;

        if (viewType == TYPE_NO_DIVIDER) {
            layout = R.layout.nutriment_item;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new NutrimentViewHolder(view, context);
    }

    @Override
    public int getItemCount() {
        return nutriments.size();
    }

    @Override
    public void onBindViewHolder(final NutrimentViewHolder holder, int position) {
        holder.setNutriment(nutriments.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == nutriments.size() -1) {
            return TYPE_NO_DIVIDER;
        }
        return TYPE_DIVIDER;
    }
}
