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
import java.util.ArrayList;

public class ProductJSON {
    private Product product;
    private JSONObject levels;
    private JSONArray additives;
    private JSONObject nutrients;
    private JSONArray ingredients_tags;
    private Context context;

    public ProductJSON(JSONObject json, Context context) {
        product = new Product();
        this.context = context;
        try {
            JSONObject p = json.getJSONObject("product");
            JSONArray states = p.getJSONArray("states_tags");

            Boolean isCompleted = isProductComplete(states);
            product.setIsComplete(isCompleted);
            product.setBarcode(json.getString("code"));
            product.setName(p.getString("product_name_fr"));

            if (isCompleted) {
                nutrients       = p.getJSONObject("nutriments");
                levels           = p.getJSONObject("nutrient_levels");
                additives        = p.getJSONArray("additives_tags");
                ingredients_tags = p.getJSONArray("ingredients_tags");

                if (ingredients_tags.toString().contains("fr:huile-de-palme")) {
                    product.setPalmOil(true);
                }
                setProductNutriments();
                setProductAdditives();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            product.setIsFound(false);
        }
    }

    private Boolean isProductComplete(JSONArray json) {
        Boolean isComplete = true;
        String[] list = new String[]{
            "en:ingredients-to-be-completed",
            "en:quantity-to-be-completed",
            "en:product-name-to-be-completed"
        };
        String states = json.toString();
        for (String s : list) {
            if (states.contains(s)) {
                isComplete = false;
            }
        }
        return isComplete;
    }

    private void setProductNutriments() {
        String[] values = {"salt", "sugars", "fat", "saturated-fat"};
        for (String i : values) {
            try {
                Nutriment nutrient = new Nutriment();
                nutrient.setName(i);

                String label = i.replace('-', '_');
                int icon =  context.getResources().getIdentifier("ic_nutriment_" + label + "_24dp", "drawable", context.getPackageName());
                int identifier = context.getResources().getIdentifier("nutriment_" + label, "string", context.getPackageName());

                if (identifier != 0) {
                    label = context.getResources().getString(identifier);
                    nutrient.setLabel(label);
                }
                String quantity = nutrients.getString(i + "_100g");
                String unit     = nutrients.getString(i + "_unit");
                String level    = levels.getString(i);

                nutrient.setQuantity(Float.valueOf(quantity));
                nutrient.setUnit(unit);
                nutrient.setLevel(level);
                nutrient.setIcon(icon);

                switch(i) {
                    case "salt":          product.setSalt(nutrient);      break;
                    case "sugars":        product.setSugar(nutrient);     break;
                    case "fat":           product.setFat(nutrient);       break;
                    case "saturated-fat": product.setSaturated(nutrient); break;
                }
            } catch(JSONException e) {
                Log.d("Debug","ProductJSON:setProductNutriments: " + e.getMessage());
                e.printStackTrace();
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