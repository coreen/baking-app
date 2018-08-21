package com.udacity.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.activity.RecipeActivity;
import com.udacity.bakingapp.model.Recipe;

import timber.log.Timber;

/**
 * Implementation of Recipe Widget Provider functionality.
 */
public class IngredientWidget extends AppWidgetProvider {
    public static final String EXTRA_INGREDIENTS = "ingredients";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient);

        // Grab shared preferences from initial widget config
        SharedPreferences preferences = context.getSharedPreferences(
                WidgetConfigActivity.WIDGET_SHARED_PREF, Context.MODE_PRIVATE);
        final Gson gson = new Gson();
        final String json = preferences.getString(
                WidgetConfigActivity.WIDGET_KEY_RECIPE + appWidgetId,
                "");
        final Recipe recipe = gson.fromJson(json, Recipe.class);

        if (recipe != null) {
            Timber.d("Received recipe from sharedPreferences with id: " + recipe.getId());

            views.setCharSequence(R.id.widget_tv_title, "setText", recipe.getName());
            Intent listIntent = new Intent(context, WidgetService.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArray(IngredientWidget.EXTRA_INGREDIENTS, recipe.getIngredients());
            listIntent.putExtra(IngredientWidget.EXTRA_INGREDIENTS, bundle);
            views.setRemoteAdapter(R.id.widget_list, listIntent);

            // Create an Intent to launch MainActivity when clicked
            Intent intent = new Intent(context, RecipeActivity.class);
            intent.putExtra(RecipeActivity.EXTRA_RECIPE, recipe);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Widgets allow click handlers to only launch pending intents
            views.setOnClickPendingIntent(R.id.widget_tv_title, pendingIntent);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

