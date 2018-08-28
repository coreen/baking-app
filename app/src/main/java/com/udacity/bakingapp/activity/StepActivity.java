package com.udacity.bakingapp.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.fragment.RecipeStepFragment;

import timber.log.Timber;

public class StepActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Bundle arguments = getIntent().getExtras();
        Timber.d("Received bundle in StepActivity: " + arguments);


        final RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        recipeStepFragment.setArguments(arguments);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.recipe_step_placeholder, recipeStepFragment)
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        RecipeStepFragment recipeStepFragment = (RecipeStepFragment) getSupportFragmentManager()
                .findFragmentById(R.id.recipe_step_placeholder);
        recipeStepFragment.onConfigurationChanged(newConfig);
    }
}
