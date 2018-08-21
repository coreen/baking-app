package com.udacity.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.model.Recipe;

import timber.log.Timber;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Recipe.Ingredient[] mIngredients;

    public WidgetDataProvider(Context mContext, Intent intent) {
        this.mContext = mContext;
        Timber.d("Grabbing extra ingredients from intent: " + intent.getExtras());
        final Bundle bundle = intent.getBundleExtra(IngredientWidget.EXTRA_INGREDIENTS);
        // Cannot cast parcelable array directly to Recipe.Ingredient model, need to arraycopy
        // Resource: https://stackoverflow.com/questions/8745893/i-dont-get-why-this-classcastexception-occurs
        final Parcelable[] parcelables = bundle.getParcelableArray(IngredientWidget.EXTRA_INGREDIENTS);
        this.mIngredients = new Recipe.Ingredient[parcelables.length];
        System.arraycopy(parcelables, 0, mIngredients, 0, parcelables.length);
        Timber.d("Found ingredients: " + mIngredients);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (null == mIngredients) {
            return 0;
        }
        return mIngredients.length;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                R.layout.ingredient_item);
        final Recipe.Ingredient ingredient = mIngredients[position];
        Timber.d("Getting view for ingredient: " + ingredient);
        view.setTextViewText(R.id.tv_ingredient_item, Recipe.Ingredient.getIngredientItem(ingredient));
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
