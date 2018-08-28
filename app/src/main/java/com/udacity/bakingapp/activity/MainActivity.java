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
import com.udacity.bakingapp.utilities.GetRecipes;
import com.udacity.bakingapp.utilities.JsonUtils;
import com.udacity.bakingapp.utilities.RetrofitUtils;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

        GetRecipes data = RetrofitUtils.getRetrofitInstance().create(GetRecipes.class);
        Call<List<Recipe>> call = data.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                // Cannot directly cast from Object[] to Recipe[]
                mRecipes = response.body().toArray(new Recipe[response.body().size()]);
                Timber.d("Received recipes from network: " + Arrays.toString(mRecipes));

                mGridView = findViewById(R.id.recipe_card_gridview);
                final String[] cardNames = JsonUtils.getCardNames(mRecipes);
                final RecipeCardAdapter adapter = new RecipeCardAdapter(MainActivity.this, cardNames);
                mGridView.setAdapter(adapter);
                mGridView.setOnItemClickListener(
                        (AdapterView<?> adapterView, View view, int position, long l)
                                -> launchRecipeActivity(position));

                // Set column number, onCreate called every orientation change
                // if configChanges not set in AndroidManifest
                Configuration config = getResources().getConfiguration();
                if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mGridView.setNumColumns(3);
                } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mGridView.setNumColumns(2);
                }
            }
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Timber.e("Error retrieving recipes from network", t);
            }
        });
    }

    // NOTE: all activities need to be included in AndroidManifest in order to launch properly
    private void launchRecipeActivity(int position) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RecipeActivity.EXTRA_RECIPE, mRecipes[position]);
        startActivity(intent);
    }
}
