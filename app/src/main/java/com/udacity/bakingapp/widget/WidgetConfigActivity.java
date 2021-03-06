package com.udacity.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.activity.RecipeActivity;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.utilities.JsonUtils;

import timber.log.Timber;

// Resource: https://www.youtube.com/watch?v=C7IW49jejUY
public class WidgetConfigActivity extends AppCompatActivity {

    public static String WIDGET_SHARED_PREF = "com.udacity.bakingapp.shared.preferences.widget";
    public static String WIDGET_KEY_RECIPE = "com.udacity.bakingapp.shared.preferences.recipe";

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private ListView mWidgetConfigList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_config_activity);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // Handling when user backs out of config activity
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        // Something went wrong
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        mWidgetConfigList = findViewById(R.id.widget_config_list);
        setupConfigList();
    }

    private void setupConfigList() {
        final Recipe[] recipes = JsonUtils.getRecipes(this);
        final String[] cardNames = JsonUtils.getCardNames(recipes);
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter.addAll(cardNames);
        mWidgetConfigList.setAdapter(adapter);
        mWidgetConfigList.setOnItemClickListener(
                (AdapterView<?> adapterView, View view, int position, long l) -> {
                    // Set the widget contents based on selected recipe
                    Timber.d("Setup config list, selected recipe: " + recipes[position]);
                    saveRecipeSelection(recipes[position]);
                });
    }

    private void saveRecipeSelection(Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.widget_ingredient);

        // Clicking anywhere on widget will launch the recipe's activity
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RecipeActivity.EXTRA_RECIPE, recipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent, 0);
        views.setOnClickPendingIntent(R.id.widget_tv_title, pendingIntent);

        // Initial widget configuration, need to duplicate in IngredientWidget.updateAppWidget
        views.setCharSequence(R.id.widget_tv_title, "setText", recipe.getName());
        Timber.d("Set widget title to " + recipe.getName());

        Intent listIntent = new Intent(this, WidgetService.class);
        // Passing ingredients array in directly gives consistent parse error, use Bundle wrapper
        // Resource: https://stackoverflow.com/questions/13363046/passing-custom-parcelable-object-extra-or-in-arraylist-to-remoteviewsservice-bre
        Bundle bundle = new Bundle();
        bundle.putParcelableArray(IngredientWidget.EXTRA_INGREDIENTS, recipe.getIngredients());
        listIntent.putExtra(IngredientWidget.EXTRA_INGREDIENTS, bundle);
        views.setRemoteAdapter(R.id.widget_list, listIntent);
        Timber.d("Set widget list to " + recipe.getIngredients());

        // Persist this widget's preferences, store objects as JSON string
        // Resource: https://stackoverflow.com/questions/7145606/how-android-sharedpreferences-save-store-object
        SharedPreferences preferences = getSharedPreferences(WIDGET_SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        editor.putString(WIDGET_KEY_RECIPE + mAppWidgetId, gson.toJson(recipe));
        editor.apply();

        Timber.d("Saved recipe info in sharedPreferences for appWidgetId: " + mAppWidgetId);

        // Successful creation
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);

        appWidgetManager.updateAppWidget(mAppWidgetId, views);
        finish();
    }
}
