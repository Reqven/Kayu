package com.reqven.kayu;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProductJSON {
    private Product product;
    private JSONObject levels;
    private JSONArray additives;
    private JSONObject nutriments;
    private JSONArray ingredients_tags;
    private Context context;

    public ProductJSON(JSONObject json, Context context) {
        product = new Product();
        try {
            if (json.getString("status").equals("1")) {
                JSONObject p = json.getJSONObject("product");
                this.context = context;

                product.setBarcode(json.getString("code"));
                product.setName(p.getString("product_name_fr"));

                nutriments       = p.getJSONObject("nutriments");
                levels           = p.getJSONObject("nutrient_levels");
                additives        = p.getJSONArray("additives_tags");
                ingredients_tags = p.getJSONArray("ingredients_tags");
                setProductNutriments();
                setProductAdditives();
            } else {
                product.setIsFound(false);
            }
        } catch (JSONException e) {
            Log.d("Debug","ProductJSON:__construct: " + e.getMessage());
            product.setIsFound(false);
        }
    }

    private void setProductNutriments() {
        String[] values = {"salt", "sugars", "fat", "saturated-fat"};
        for (String i : values) {
            try {
                Nutriment nutriment = new Nutriment();
                nutriment.setName(i);

                String label = i.replace('-', '_');
                int icon =  context.getResources().getIdentifier("ic_nutriment_" + label + "_24dp", "drawable", context.getPackageName());
                int identifier = context.getResources().getIdentifier("nutriment_" + label, "string", context.getPackageName());

                if (identifier != 0) {
                    label = context.getResources().getString(identifier);
                    nutriment.setLabel(label);
                }
                String quantity = nutriments.getString(i + "_100g");
                String unit     = nutriments.getString(i + "_unit");
                String level    = levels.getString(i);

                nutriment.setQuantity(Float.valueOf(quantity));
                nutriment.setUnit(unit);
                nutriment.setLevel(level);
                nutriment.setIcon(icon);

                if (ingredients_tags.toString().contains("fr:huile-de-palme")) {
                    product.setPalmOil(true);
                }
                switch(i) {
                    case "salt":          product.setSalt(nutriment);      break;
                    case "sugars":        product.setSugar(nutriment);     break;
                    case "fat":           product.setFat(nutriment);       break;
                    case "saturated-fat": product.setSaturated(nutriment); break;
                }
            } catch(JSONException e) {
                Log.d("Debug","ProductJSON:setProductNutriments: " + e.getMessage());
            }
        }
    }

    private void setProductAdditives() {
        final String filename = "additives.json";
        JSONObject file = new JSONObject();

        try {
            InputStream inputStream = context.openFileInput(filename);
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                file = new JSONObject(stringBuilder.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < additives.length(); i++) {
            try {
                String tag = additives.getString(i);
                String id  = tag.substring(tag.lastIndexOf(':') + 1).toUpperCase();

                JSONObject a = file.getJSONObject(id);
                String name     = a.getString("name");
                String label    = a.getString("label");
                String toxicity = a.getString("toxicity");

                Additive additive = new Additive();
                additive.setId(id);
                additive.setName(name);
                additive.setLabel(label);
                additive.setToxicity(toxicity);

                product.addAdditive(additive);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public Product getProduct() {
        return product;
    }
}