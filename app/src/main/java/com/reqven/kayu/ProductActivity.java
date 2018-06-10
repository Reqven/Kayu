package com.reqven.kayu;

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
    private ActionBar actionBar;
    private Toolbar toolBar;
    private TabLayout tabLayout;
    private AppCompatTextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);

        Intent intent = getIntent();
        final String code = intent.getStringExtra("code");

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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject product = response.getJSONObject("product");
                            JSONObject nutriments = product.getJSONObject("nutriments");
                            JSONObject nutriment_levels = product.getJSONObject("nutrient_levels");
                            JSONArray ingredients_tags = product.getJSONArray("ingredients_tags");

                            String matiere_grasse_lipides = nutriments.getString("fat_100g");
                            String acides_gras_satures = nutriments.getString("saturated-fat_100g");
                            String sucres = nutriments.getString("sugars_100g");
                            String sel = nutriments.getString("salt_100g");

                            Product product_ = new Product(code);
                            Nutriment salt          = new Nutriment("salt", Float.valueOf(sel), String.valueOf(nutriments.getString("salt_unit")), nutriment_levels.getString("salt"));
                            Nutriment sugars        = new Nutriment("sugar", Float.valueOf(sucres), String.valueOf(nutriments.getString("sugars_unit")), nutriment_levels.getString("sugars"));
                            Nutriment fat           = new Nutriment("fat", Float.valueOf(matiere_grasse_lipides), String.valueOf(nutriments.getString("fat_unit")), nutriment_levels.getString("fat"));
                            Nutriment saturated_fat = new Nutriment("saturated-fat", Float.valueOf(acides_gras_satures), String.valueOf(nutriments.getString("saturated-fat_unit")), nutriment_levels.getString("saturated-fat"));

                            product_.addNutriment(salt);
                            product_.addNutriment(sugars);
                            product_.addNutriment(fat);
                            product_.addNutriment(saturated_fat);

                            for (Nutriment n: product_.getNutriments()) {
                                int id = getResources().getIdentifier(n.getName(), "id", getPackageName());
                                View view = findViewById(id);

                                String q = String.valueOf(n.getQuantity()) + n.getUnit();

                                AppCompatTextView name = view.findViewById(R.id.name);
                                AppCompatImageView icon = view.findViewById(R.id.icon);
                                AppCompatTextView desc = view.findViewById(R.id.description);
                                AppCompatTextView quantity = view.findViewById(R.id.quantity);
                                quantity.setText(q);
                                desc.setText(n.getLevel());

                                switch (n.getName()) {
                                    case "salt":
                                        name.setText("Sel");
                                        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_salt_24dp));
                                        break;
                                    case "sugar":
                                        name.setText("Sucre");
                                        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_sugar_24dp));
                                        break;
                                    case "fat":
                                        name.setText("Matières grasses");
                                        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_fat_24dp));
                                        break;
                                    case "saturated-fat":
                                        name.setText("Graisses saturées");
                                        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_fat_24dp));
                                        break;
                                }
                            }

                            if (ingredients_tags.toString().contains("fr:huile-de-palme")) {
                                Toast.makeText(getBaseContext(), "HUILDE DE PALME", Toast.LENGTH_LONG).show();
                            }

                            toolBar.setTitle(product.getString("product_name_fr"));
                            findViewById(R.id.loadingLayout).setVisibility(View.GONE);
                            findViewById(R.id.contentLayout).setVisibility(View.VISIBLE);
                            //description.setText(product.getString("ingredients_text_fr"));
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


    public class NutrimentViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView name;
        private AppCompatTextView quantity;

        NutrimentViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
        }

        void bindValue(Nutriment nutriment) {
            name.setText(nutriment.getName());
            quantity.setText(nutriment.getQuantity() + nutriment.getUnit());
        }
    }

    public class NutrimentAdapter extends RecyclerView.Adapter<NutrimentViewHolder> {
        private ArrayList<Nutriment> nutriments;


        public NutrimentAdapter(ArrayList<Nutriment> nutriments) {
            Log.d("DEV", nutriments.toString());
            this.nutriments = nutriments;
        }

        @Override
        public NutrimentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nutriment_item, parent, false);

            return new NutrimentViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return nutriments.size();
        }

        @Override
        public void onBindViewHolder(final NutrimentViewHolder holder, int position) {

            NutrimentViewHolder myHolder = holder;
            myHolder.bindValue(nutriments.get(position));

        /*holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);*/
        }

        public void addItem(String input) {
            //values.add(input);
        }
    }
}
