package com.reqven.kayu;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class NutrimentViewHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView  vName;
    private AppCompatTextView  vQuantity;
    private AppCompatTextView  vDescription;
    private AppCompatImageView vIcon;
    private AppCompatImageView vLevel;

    NutrimentViewHolder(View itemView) {
        super(itemView);
        vName        = itemView.findViewById(R.id.name);
        vQuantity    = itemView.findViewById(R.id.quantity);
        vDescription = itemView.findViewById(R.id.description);
        vIcon        = itemView.findViewById(R.id.icon);
        vLevel       = itemView.findViewById(R.id.level);
    }

    void setNutriment(Nutriment nutriment) {
        int icon     = nutriment.getIcon();
        String name  = nutriment.getLabel();
        String unit  = nutriment.getUnit();
        String level = nutriment.getLevel();
        String quantity = String.valueOf(nutriment.getQuantity()) + unit;

        vName.setText(name);
        vQuantity.setText(quantity);
        vIcon.setImageResource(icon);

        switch(level) {
            case "low": vLevel.setImageResource(R.drawable.ic_healthy_good_24dp); break;
            case "moderate": vLevel.setImageResource(R.drawable.ic_healthy_moderate_24dp); break;
            case "high": vLevel.setImageResource(R.drawable.ic_healthy_bad_24dp); break;
        }
        vDescription.setText(nutriment.getLevel());
    }
}