package com.reqven.kayu;

import android.content.Context;
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
    private Context context;

    NutrimentViewHolder(View itemView, Context context) {
        super(itemView);
        vName        = itemView.findViewById(R.id.palm_oil_text);
        vQuantity    = itemView.findViewById(R.id.quantity);
        vDescription = itemView.findViewById(R.id.description);
        vIcon        = itemView.findViewById(R.id.icon);
        vLevel       = itemView.findViewById(R.id.level);
        this.context = context;
    }

    void setNutriment(Nutriment nutriment) {
        int icon     = nutriment.getIcon();
        String label = nutriment.getLabel();
        String name  = nutriment.getName();
        String unit  = nutriment.getUnit();
        String level = nutriment.getLevel();
        String quantity = String.valueOf(nutriment.getQuantity()) + unit;

        vName.setText(label);
        vQuantity.setText(quantity);
        vIcon.setImageResource(icon);

        if (!nutriment.getPassed()) {
            Integer color = context.getResources().getColor(R.color.redPrimary);
            vName.setTextColor(color);
        }

        switch(level) {
            case "low": vLevel.setImageResource(R.drawable.ic_healthy_good_24dp); break;
            case "moderate": vLevel.setImageResource(R.drawable.ic_healthy_moderate_24dp); break;
            case "high": vLevel.setImageResource(R.drawable.ic_healthy_bad_24dp); break;
        }
        label = name.replace('-', '_');
        int identifier = context.getResources().getIdentifier(label + "_" + level, "string", context.getPackageName());
        String description = context.getResources().getString(identifier);
        vDescription.setText(description);
    }
}