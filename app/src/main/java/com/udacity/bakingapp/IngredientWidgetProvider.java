package com.udacity.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.udacity.bakingapp.activity.RecipeActivity;

import timber.log.Timber;

/**
 * Implementation of Recipe Widget Provider functionality.
 */
public class IngredientWidgetProvider extends AppWidgetProvider {

//    private static int mRecipeId;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int[] appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient);

        // Set mRecipeId to correctly call in overridden onUpdate
//        mRecipeId = recipeId;

        // Create an Intent to launch MainActivity when clicked
        Intent intent = new Intent(context, RecipeActivity.class);

        SharedPreferences preferences = context.getSharedPreferences(
                WidgetConfigActivity.WIDGET_SHARED_PREF, Context.MODE_PRIVATE);

//        intent.putExtra(RecipeActivity.EXTRA_RECIPE_ID, recipeId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

//        Timber.d("Launching RecipeActivity with recipeId " + recipeId);

        // Widgets allow click handlers to only launch pending intents
        // TODO investigate why clicking widget doesn't open RecipeActivity
        views.setOnClickPendingIntent(R.id.widget_layout_main, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, new int[]{appWidgetId});
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

