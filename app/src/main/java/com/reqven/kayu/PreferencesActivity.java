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

import org.json.JSONArray;
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

        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
                preferencesChanged = true;
            }
        };
        preferences.registerOnSharedPreferenceChangeListener(listener);

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
        if (preferencesChanged) {
            updateUserPreferences();
        }
        super.onDestroy();
    }

    public static void retrieveUserPreferences(final Context context) {
        final String url = "http://kayu-dev.tryfcomet.com/api/user/1/preferences";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject user        = response.getJSONObject("user");
                        JSONObject preferences = user.getJSONObject("preferences");
                        JSONObject nutrients   = preferences.getJSONObject("nutrients");
                        String     additives   = preferences.getString("additives");

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor settings = prefs.edit();

                        settings.putBoolean("palmoil",      nutrients.getBoolean("palm_oil"));
                        settings.putString("salt",          nutrients.getString("salt"));
                        settings.putString("sugars",        nutrients.getString("sugar"));
                        settings.putString("fat",           nutrients.getString("fat"));
                        settings.putString("saturated_fat", nutrients.getString("saturated_fat"));
                        settings.putString("calories",      nutrients.getString("calories"));
                        //settings.putString("additives",     additives);
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
            }
        );
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void updateUserPreferences() {
        final String url = "http://kayu-dev.tryfcomet.com/api/user/1/preferences";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor settings = prefs.edit();

        try {
            JSONObject data = new JSONObject();
            JSONObject user = new JSONObject();
            JSONObject preferences = new JSONObject();
            JSONObject nutrients = new JSONObject();

            nutrients.put("palm_oil",       prefs.getBoolean("palmoil",       true));
            nutrients.put("salt",           prefs.getString( "salt",          "low"));
            nutrients.put("sugar",          prefs.getString( "sugars",        "low"));
            nutrients.put("fat",            prefs.getString( "fat",           "low"));
            nutrients.put("saturated_fat",  prefs.getString( "saturated_fat", "low"));
            //nutrients.put( "calories",      prefs.getString( "calories",      null));

            preferences.put("nutrients", nutrients);
            preferences.put("additives", prefs.getString("additives", "dangerous"));

            user.put("preferences", preferences);
            data.put("user", user);


            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Réglages mis à jour !", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
            );
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}