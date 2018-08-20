package com.udacity.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

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

        // Create an Intent to launch MainActivity when clicked
//        Intent intent = new Intent(context, RecipeActivity.class);

        SharedPreferences preferences = context.getSharedPreferences(
                WidgetConfigActivity.WIDGET_SHARED_PREF, Context.MODE_PRIVATE);
//        TODO grab title and ingredients from preferences to persist on update
        final int recipeId = preferences.getInt(
                WidgetConfigActivity.WIDGET_KEY_RECIPE_ID + appWidgetId,
                1);
        final String recipeName = preferences.getString(
                WidgetConfigActivity.WIDGET_KEY_RECIPE_NAME + appWidgetId,
                "Baking App");

//        Timber.d("Before setting views");

        views.setCharSequence(R.id.widget_tv_title, "setText", recipeName);
        Intent listIntent = new Intent(context, WidgetService.class);
//        listIntent.putExtra(IngredientWidget.EXTRA_INGREDIENTS, new int[] {1, 2, 3});
        views.setRemoteAdapter(R.id.widget_list, listIntent);

//        Timber.d("After setting views");

//        intent.putExtra(RecipeActivity.EXTRA_RECIPE_ID, recipeId);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

//        Timber.d("Launching RecipeActivity with recipeId " + recipeId);

        // Widgets allow click handlers to only launch pending intents
        // TODO investigate why clicking widget doesn't open RecipeActivity
//        views.setOnClickPendingIntent(R.id.widget_tv_title, pendingIntent);

//        Timber.d("Setting onClickPendingIntent to widget_tv_title");

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

