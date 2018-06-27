package com.reqven.kayu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PreferencesActivity extends AppCompatActivity {
    private Boolean preferencesChanged;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);
        preferencesChanged = false;

        UserPreferences preferencesFragment = new UserPreferences();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        preferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
             @Override
             public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                 preferencesChanged = true;
             }
        });

        getSupportActionBar().setTitle("Paramètres");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, preferencesFragment)
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (preferencesChanged) {
            updateUserPreferences();
        }
    }

    public static void retrieveUserPreferences(final Context context) {
        final String url = "https://api.myjson.com/bins/g08da";

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String palmoil      = response.getString("palmOil");
                    String salt         = response.getString("salt");
                    String sugar        = response.getString("sugar");
                    String fat          = response.getString("fat");
                    String saturatedFat = response.getString("saturedFat");
                    //String additives    = response.getString("additives");

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor settings = prefs.edit();

                    settings.putBoolean("palmoil",     true);
                    settings.putString("salt",         salt);
                    settings.putString("sugar",        sugar);
                    settings.putString("fat",          fat);
                    settings.putString("saturatedFat", saturatedFat);
                    settings.apply();
                }
                catch (JSONException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void updateUserPreferences() {
        Toast.makeText(getApplicationContext(), "now sending new settings to the api", Toast.LENGTH_SHORT).show();
    }
}