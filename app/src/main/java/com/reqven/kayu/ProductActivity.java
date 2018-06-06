package com.reqven.kayu;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductActivity extends AppCompatActivity{
    private ActionBar actionBar;
    private Toolbar toolBar;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);

        Intent intent = getIntent();
        final String code = intent.getStringExtra("code");

        toolBar   = findViewById(R.id.main_toolbar);
        tabLayout = findViewById(R.id.main_tabs);
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
                        findViewById(R.id.loadingCircle).setVisibility(View.GONE);
                        try {
                            JSONObject labels = response.getJSONObject("product");
                            toolBar.setTitle(labels.getString("product_name_fr"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //mTextView.setText("Response: " + response.toString());
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
