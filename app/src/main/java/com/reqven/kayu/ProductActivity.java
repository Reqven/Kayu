package com.reqven.kayu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

public class ProductActivity extends AppCompatActivity {
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
                        if (product.isFound()) {
                            nutriments.add(product.getSalt());
                            nutriments.add(product.getSugar());
                            nutriments.add(product.getFat());
                            nutriments.add(product.getSaturated());
                        }
                        NutrimentViewAdapter adapter = new NutrimentViewAdapter(nutriments);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);

                        toolBar.setTitle(product.getName());
                        findViewById(R.id.loadingLayout).setVisibility(View.GONE);
                        findViewById(R.id.contentLayout).setVisibility(View.VISIBLE);
                        setProductLayout(product.isFound());
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

    public void setProductLayout(Boolean good) {
        int primary     = R.color.colorPrimary;
        int primaryDark = R.color.colorPrimaryDark;

        if (!good) {
            primary = R.color.redPrimary;
            primaryDark = R.color.redPrimaryDark;
        }
        int color = getResources().getColor(primary);
        toolBar.setBackgroundColor(color);
        tabLayout.setBackgroundColor(color);

        color = getResources().getColor(primaryDark);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
