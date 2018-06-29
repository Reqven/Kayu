package com.reqven.kayu;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;


public class AdditiveViewHolder extends RecyclerView.ViewHolder {
    private AppCompatImageView vIcon;
    private AppCompatTextView  vLabel;
    private AppCompatTextView  vAmount;
    private Context context;

    AdditiveViewHolder(View itemView, Context context) {
        super(itemView);
        vIcon   = itemView.findViewById(R.id.icon);
        vLabel  = itemView.findViewById(R.id.label);
        vAmount = itemView.findViewById(R.id.amount);
        this.context = context;
    }

    void setAdditive(FragmentProduct.AdditiveList additive) {
        String toxicity = additive.getToxicity();
        String label    = additive.getLabel();
        String amount   = String.valueOf(additive.getSize());

        /*switch(toxicity) {
            case "clean":       vIcon.setImageResource(R.drawable.ic_healthy_good_24dp); break;
            case "no-abuse":    vIcon.setImageResource(R.drawable.ic_healthy_moderate_24dp); break;
            case "doubt":       vIcon.setImageResource(R.drawable.ic_healthy_moderate_24dp); break;
            case "toxic":       vIcon.setImageResource(R.drawable.ic_healthy_bad_24dp); break;
            case "super-toxic": vIcon.setImageResource(R.drawable.ic_healthy_bad_24dp); break;
        }*/
        vIcon.setImageResource(R.drawable.ic_healthy_black_24dp);
        vLabel.setText(label);
        vAmount.setText(amount);
    }
}