package com.udacity.bakingapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.utilities.JsonUtils;

import java.util.Arrays;

import timber.log.Timber;
import static timber.log.Timber.DebugTree;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.plant(new DebugTree());

        String[] json = JsonUtils.getCardNames(this);
        Timber.d("json from baking.json file: " + Arrays.toString(json));
    }
}
