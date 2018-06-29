package com.reqven.kayu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class FragmentProduct extends Fragment {
    protected ArrayList<Nutriment> nutriments;
    protected ArrayList<AdditiveList> additivesList;
    protected Integer colorPrimary;
    protected Integer colorPrimaryDark;
    protected Context context;
    protected Product product;

    public FragmentProduct() {
        context = getContext();
        nutriments = new ArrayList<>();
        additivesList = new ArrayList<>();
        colorPrimary = R.color.colorPrimary;
        colorPrimaryDark = R.color.colorPrimaryDark;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_found, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        RecyclerView recyclerView_nutriments = view.findViewById(R.id.nutriments);
        RecyclerView recyclerView_additives  = view.findViewById(R.id.additives);
        AppCompatTextView palmoil = view.findViewById(R.id.palm_oil_text);

        String text;
        if (product.containsPalmOil()) {
            text = getContext().getResources().getString(R.string.palm_oil_yes);
            if (!preferences.getBoolean("palmoil", true)) {
                Integer color = getResources().getColor(R.color.redPrimary);
                palmoil.setTextColor(color);
            }
        } else {
            text = getContext().getResources().getString(R.string.palm_oil_no);
        }
        palmoil.setText(text);

        NutrimentViewAdapter adapter_nutriments = new NutrimentViewAdapter(nutriments, getContext());
        recyclerView_nutriments.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_nutriments.setItemAnimator(new DefaultItemAnimator());
        recyclerView_nutriments.setAdapter(adapter_nutriments);

        if (additivesList.size() > 0) {
            AdditiveViewAdapter adapter_additives = new AdditiveViewAdapter(additivesList, getContext());
            recyclerView_additives.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView_additives.setItemAnimator(new DefaultItemAnimator());
            recyclerView_additives.setAdapter(adapter_additives);
        } else {
            view.findViewById(R.id.textView10).setVisibility(View.GONE);
            view.findViewById(R.id.additivesLayout).setVisibility(View.GONE);
            view.findViewById(R.id.borderToppp).setVisibility(View.GONE);
            view.findViewById(R.id.borderBottommm).setVisibility(View.GONE);
        }
        return view;
    }

    protected void setProduct(Product product) {
        this.product = product;
        nutriments = new ArrayList<>();
        nutriments.add(product.getSalt());
        nutriments.add(product.getSugar());
        nutriments.add(product.getFat());
        nutriments.add(product.getSaturated());

        HashMap<String, AdditiveList> hashMap = new HashMap<>();
        for (Additive a : product.getAdditives()) {
            String toxicity = a.getToxicity();
            if (!hashMap.containsKey(toxicity)) {
                AdditiveList additiveList = new AdditiveList();
                additiveList.setToxicity(toxicity);
                additiveList.addAdditive(a);
                hashMap.put(toxicity, additiveList);
            } else {
                hashMap.get(toxicity).addAdditive(a);
            }
        }
        this.additivesList.addAll(hashMap.values());
    }

    protected Map<String, Integer> getColors() {
        HashMap<String, Integer> colors = new HashMap<>();
        colors.put("primary", colorPrimary);
        colors.put("primaryDark", colorPrimaryDark);
        return  colors;
    }


    public static class Passed extends FragmentProduct {

    }

    public static class NotPassed extends FragmentProduct {
        public NotPassed() {
            super();
            colorPrimary = R.color.redPrimary;
            colorPrimaryDark = R.color.redPrimaryDark;
        }
    }

    public static class NotFound extends FragmentProduct {
        public NotFound() {
            super();
            colorPrimary = R.color.orangePrimary;
            colorPrimaryDark = R.color.orangePrimaryDark;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.product_not_found, container, false);
        }
    }
    public static class Incomplete extends FragmentProduct {
        public Incomplete() {
            super();
            colorPrimary = R.color.orangePrimary;
            colorPrimaryDark = R.color.orangePrimaryDark;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.product_incomplete, container, false);
        }
    }

    public class AdditiveList {
        private String toxicity;
        private ArrayList<Additive> additives;

        public AdditiveList() {
            additives = new ArrayList<>();
        }

        AdditiveList(String toxicity, ArrayList<Additive> additives) {
            this.toxicity = toxicity;
            this.additives = additives;
        }

        public void setToxicity(String toxicity) {
            this.toxicity = toxicity;
        }

        public void addAdditive(Additive additive) {
            additives.add(additive);
        }

        public String getLabel() {
            return additives.get(0).getLabel();
        }
        public String getToxicity() {
            return toxicity;
        }
        public Integer getSize() {
            return additives.size();
        }
    }
}
