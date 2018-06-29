package com.reqven.kayu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import java.util.Map;

import android.util.Log;
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
    private ArrayList<Nutriment> nutriments;
    private Context context;


    private FragmentProduct fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);

        Intent intent = getIntent();
        final String code = intent.getStringExtra("code");
        nutriments = new ArrayList<>();

        context      = getApplicationContext();
        toolBar      = findViewById(R.id.main_toolbar);
        tabLayout    = findViewById(R.id.main_tabs);
        description  = findViewById(R.id.description);

        toolBar.setTitle("Fiche produit");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_baseline_subject_24px));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_baseline_local_dining_24px));

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://world-fr.openfoodfacts.org/api/v0.1/product/" + code + ".json";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        productJSON = new ProductJSON(response, getBaseContext());
                        product     = productJSON.getProduct();

                        if (product.isFound()) {
                            toolBar.setTitle(product.getName());
                            if (product.isComplete()) {

                                if (product.getPassed()) {
                                    fragment = new FragmentProduct.Passed();
                                } else {
                                    fragment = new FragmentProduct.NotPassed();
                                }
                                fragment.setProduct(product);
                            } else {
                                fragment = new FragmentProduct.Incomplete();
                            }
                        } else {
                            fragment = new FragmentProduct.NotFound();
                        }
                        setLayoutColor(fragment);
                        findViewById(R.id.loadingCircle).setVisibility(View.GONE);

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .commit();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setLayoutColor(FragmentProduct fragment) {
        Map<String, Integer> colors = fragment.getColors();
        Integer colorPrimary = getResources().getColor(colors.get("primary"));
        Integer colorPrimaryDark = getResources().getColor(colors.get("primaryDark"));

        toolBar.setBackgroundColor(colorPrimary);
        tabLayout.setBackgroundColor(colorPrimary);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(colorPrimaryDark);
        }
    }
}
