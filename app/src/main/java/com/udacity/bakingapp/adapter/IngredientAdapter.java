package com.udacity.bakingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.model.Measure;

import static com.udacity.bakingapp.model.Recipe.Ingredient;

public class IngredientAdapter extends BaseAdapter {
    private Context mContext;
    private Ingredient[] mIngredients;

    public IngredientAdapter(Context mContext, Ingredient[] mIngredients) {
        this.mContext = mContext;
        this.mIngredients = mIngredients;
    }

    @Override
    public int getCount() {
        if (null == mIngredients) {
            return 0;
        }
        return mIngredients.length;
    }

    @Override
    public Object getItem(int position) {
        return mIngredients[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Resource: https://stackoverflow.com/questions/16099113/disable-click-event-on-android-listview-items
    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.ingredient_item, null);
        }

//        final TextView mQuantity = convertView.findViewById(R.id.tv_quantity);
//        final TextView mMeasure = convertView.findViewById(R.id.tv_measure);
//        final TextView mIngredient = convertView.findViewById(R.id.tv_ingredient);
        final TextView mIngredientItem = convertView.findViewById(R.id.tv_ingredient_item);

//        final Ingredient ingredient = mIngredients[position];
//        final double quantity = ingredient.getQuantity();

//        final String ingredientItem = Double.toString(quantity) + " " +
//                getDisplayTextFromMeasure(ingredient.getMeasure(), quantity) + " " +
//                ingredient.getIngredient();
        mIngredientItem.setText(Ingredient.getIngredientItem(mIngredients[position]));

//        mQuantity.setText(Double.toString(quantity));
//        mIngredient.setText(ingredient.getIngredient());
//        mMeasure.setText(getDisplayTextFromMeasure(ingredient.getMeasure(), quantity));

        return convertView;
    }
}
