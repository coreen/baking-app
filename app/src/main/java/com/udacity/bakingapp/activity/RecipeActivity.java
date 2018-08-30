package com.udacity.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.fragment.InstructionListFragment;
import com.udacity.bakingapp.fragment.RecipeStepFragment;
import com.udacity.bakingapp.model.Recipe;

import timber.log.Timber;


public class RecipeActivity extends AppCompatActivity
        implements InstructionListFragment.OnStepClickListener {
    public static final String EXTRA_RECIPE = "recipe";

    // boolean indicating when to show RecipeStepFragment side-by-side on tablet
    private boolean mTwoPane;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Recipe recipe = getIntent().getParcelableExtra(EXTRA_RECIPE);

        // Set title of activity to recipe name
        setTitle(recipe.getName());

        final Bundle instructionBundle = new Bundle();
        instructionBundle.putParcelableArray(
                InstructionListFragment.EXTRA_INGREDIENTS,
                recipe.getIngredients());
        instructionBundle.putParcelableArray(
                InstructionListFragment.EXTRA_STEPS,
                recipe.getSteps());

        InstructionListFragment instructionListFragment =
                (InstructionListFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.instruction_list_placeholder);
        if (instructionListFragment == null) {
            instructionListFragment = new InstructionListFragment();
            instructionListFragment.setArguments(instructionBundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.instruction_list_placeholder, instructionListFragment)
                    .commit();
        }

        // mTwoPane detection
        if(findViewById(R.id.recipe_step_placeholder) != null) {
            Timber.d("Detected tablet in horizontal mode, using 2 panes");
            mTwoPane = true;
            // create fragment if does not exist
            RecipeStepFragment recipeStepFragment = (RecipeStepFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.recipe_step_placeholder);
            if (recipeStepFragment == null) {
                recipeStepFragment = new RecipeStepFragment();
                recipeStepFragment.setArguments(populateBundleFromStep(recipe.getSteps()[0]));
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.recipe_step_placeholder, recipeStepFragment)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onStepSelected(Recipe.Step step) {
        Bundle stepBundle = populateBundleFromStep(step);
        if (mTwoPane) {
            // Populate fragment
            final RecipeStepFragment newRecipeStepFragment = new RecipeStepFragment();
            newRecipeStepFragment.setArguments(stepBundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.recipe_step_placeholder, newRecipeStepFragment)
                    .commit();
        } else {
            // Launch activity
            final Intent intent = new Intent(this, StepActivity.class);
            intent.putExtras(stepBundle);
            startActivity(intent);
        }
    }

    private Bundle populateBundleFromStep(Recipe.Step step) {
        final Bundle stepBundle = new Bundle();
        stepBundle.putString(RecipeStepFragment.EXTRA_DESCRIPTION, step.getDescription());
        String mediaURL = step.getVideoURL();
        if (mediaURL.length() == 0) {
            mediaURL = step.getThumbnailURL();
        }
        stepBundle.putString(RecipeStepFragment.EXTRA_MEDIA_URL, mediaURL);
        return stepBundle;
    }
}
