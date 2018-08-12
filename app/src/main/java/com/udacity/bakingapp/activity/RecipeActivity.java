package com.udacity.bakingapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.fragment.InstructionListFragment;
import com.udacity.bakingapp.model.Recipe;

import timber.log.Timber;


public class RecipeActivity extends AppCompatActivity {
    public static final String EXTRA_RECIPE = "recipe";

    // TODO mTwoPane detection for when to show RecipeStepFragment side-by-side on tablet

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Recipe recipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
        Timber.d("created RecipeActivity for recipe id: " + recipe.getRecipeId());

        Bundle bundle = new Bundle();
        bundle.putParcelableArray(InstructionListFragment.EXTRA_INGREDIENTS, recipe.getIngredients());
        bundle.putParcelableArray(InstructionListFragment.EXTRA_STEPS, recipe.getSteps());

        InstructionListFragment instructionListFragment = new InstructionListFragment();
        instructionListFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.instruction_list_placeholder, instructionListFragment)
                .commit();
    }
}
