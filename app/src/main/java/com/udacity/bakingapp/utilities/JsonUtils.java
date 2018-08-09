package com.udacity.bakingapp.utilities;

import android.app.Activity;

import com.google.gson.Gson;
import com.udacity.bakingapp.model.Recipe;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class JsonUtils {

    private static String loadJSONFromAsset(Activity activity) {
        String json;
        try {
            InputStream is = activity.getAssets().open("baking.json");
            final int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    private static Recipe[] getRecipes(Activity activity) {
        String json = loadJSONFromAsset(activity);
        Gson gson = new Gson();
        Recipe[] recipes = gson.fromJson(json, Recipe[].class);
        return recipes;
    }

    public static String[] getCardNames(Activity activity) {
        Recipe[] recipes = getRecipes(activity);
        return Arrays.stream(recipes)
                .map(recipe -> recipe.getName())
                .toArray(String[]::new);
    }
}
