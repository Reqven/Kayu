package com.reqven.kayu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductActivity extends AppCompatActivity{
    private Product product;
    private ProductJSON productJSON;
    private ActionBar actionBar;
    private Toolbar toolBar;
    private TabLayout tabLayout;
    private AppCompatTextView description;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);

        Intent intent = getIntent();
        final String code = intent.getStringExtra("code");

        toolBar      = findViewById(R.id.main_toolbar);
        tabLayout    = findViewById(R.id.main_tabs);
        description  = findViewById(R.id.description);
        recyclerView = findViewById(R.id.nutriments);

        toolBar.setTitle("Fiche produit");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_baseline_subject_24px));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_baseline_local_dining_24px));

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://world-fr.openfoodfacts.org/api/v0.1/product/" + code + ".json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        productJSON = new ProductJSON(response, getBaseContext());
                        product     = productJSON.getProduct();

                        ArrayList<Nutriment> nutriments = new ArrayList<>();
                        nutriments.add(product.getSalt());
                        nutriments.add(product.getSugar());
                        nutriments.add(product.getFat());
                        nutriments.add(product.getSaturated());

                        NutrimentViewAdapter adapter = new NutrimentViewAdapter(nutriments);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);

                        toolBar.setTitle(product.getName());
                        findViewById(R.id.loadingLayout).setVisibility(View.GONE);
                        findViewById(R.id.contentLayout).setVisibility(View.VISIBLE);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
