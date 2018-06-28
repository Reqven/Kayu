package com.reqven.kayu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentProduct extends Fragment {
    protected ArrayList<Nutriment> nutriments;
    protected Integer colorPrimary;
    protected Integer colorPrimaryDark;

    public FragmentProduct() {
        nutriments = new ArrayList<>();
        colorPrimary = R.color.colorPrimary;
        colorPrimaryDark = R.color.colorPrimaryDark;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_found, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.nutriments);

        NutrimentViewAdapter adapter = new NutrimentViewAdapter(nutriments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return view;
    }

    protected void setNutriments(ArrayList<Nutriment> nutriments) {
        this.nutriments = nutriments;
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
}
