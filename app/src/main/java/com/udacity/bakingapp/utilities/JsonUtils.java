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
            final InputStream is = activity.getAssets().open("baking.json");
            final int size = is.available();
            final byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static Recipe[] getRecipes(Activity activity) {
        final String json = loadJSONFromAsset(activity);
        final Gson gson = new Gson();
        final Recipe[] recipes = gson.fromJson(json, Recipe[].class);
        return recipes;
    }

    public static String[] getCardNames(Recipe[] recipes) {
        return Arrays.stream(recipes)
                .map(recipe -> recipe.getName())
                .toArray(String[]::new);
    }
}
