package com.udacity.bakingapp.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.adapter.RecipeCardAdapter;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.utilities.JsonUtils;

import timber.log.Timber;
import static timber.log.Timber.DebugTree;

public class MainActivity extends AppCompatActivity {
    private Recipe[] mRecipes;
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.plant(new DebugTree());

        mRecipes = JsonUtils.getRecipes(this);
        mGridView = findViewById(R.id.recipe_card_gridview);
        final String[] cardNames = JsonUtils.getCardNames(mRecipes);
        final RecipeCardAdapter adapter = new RecipeCardAdapter(this, cardNames);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(
                (AdapterView<?> adapterView, View view, int position, long l)
                        -> launchRecipeActivity(position));
        // Set initial column number
        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridView.setNumColumns(3);
        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridView.setNumColumns(2);
        }
    }

    // Resource: https://developer.android.com/guide/topics/resources/runtime-changes#HandlingTheChange
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Timber.d("onConfigurationChanged orientation: " + newConfig.orientation);

        // Checks the orientation of the screen and updates column number accordingly
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridView.setNumColumns(3);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridView.setNumColumns(2);
        }
    }

    // NOTE: all activities need to be included in AndroidManifest in order to launch properly
    private void launchRecipeActivity(int position) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RecipeActivity.EXTRA_RECIPE, mRecipes[position]);
        startActivity(intent);
    }
}
