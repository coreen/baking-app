package com.udacity.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.fragment.InstructionListFragment;
import com.udacity.bakingapp.fragment.RecipeStepFragment;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.utilities.JsonUtils;

import java.util.Arrays;

import timber.log.Timber;


public class RecipeActivity extends AppCompatActivity
        implements InstructionListFragment.OnStepClickListener {
    public static final String EXTRA_RECIPE_ID = "recipeId";
    public static final int RECIPE_ID_DEFAULT = 0;

    // boolean indicating when to show RecipeStepFragment side-by-side on tablet
    private boolean mTwoPane;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);


        final int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, RECIPE_ID_DEFAULT);
        Timber.d("created RecipeActivity for recipe id: " + recipeId);

        final Recipe recipe = Arrays.stream(JsonUtils.getRecipes(this))
                .filter(r -> (r.getId() == recipeId))
                .findAny()
                .get();

        // Set title of activity to recipe name
        setTitle(recipe.getName());

        final Bundle instructionBundle = new Bundle();
        instructionBundle.putParcelableArray(
                InstructionListFragment.EXTRA_INGREDIENTS,
                recipe.getIngredients());
        instructionBundle.putParcelableArray(
                InstructionListFragment.EXTRA_STEPS,
                recipe.getSteps());

        final InstructionListFragment instructionListFragment = new InstructionListFragment();
        instructionListFragment.setArguments(instructionBundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.instruction_list_placeholder, instructionListFragment)
                .commit();

        // mTwoPane detection
        if(findViewById(R.id.recipe_step_placeholder) != null) {
            mTwoPane = true;
            // create fragment
            final RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
            recipeStepFragment.setArguments(populateBundleFromStep(recipe.getSteps()[0]));
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.recipe_step_placeholder, recipeStepFragment)
                    .commit();
        } else {
            mTwoPane = false;
        }
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
