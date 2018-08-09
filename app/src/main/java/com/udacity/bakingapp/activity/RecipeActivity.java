package com.udacity.bakingapp.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.model.Measure;

import timber.log.Timber;

public class RecipeActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "position";
    public static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO uncomment once setup fragments to startup correctly
//        setContentView(R.layout.activity_recipe);

        int position = getIntent().getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        Timber.d("created RecipeActivity for position: " + position);
    }

    private String getDisplayTextFromMeasure(Measure measure, int amount) {
        String displayText;
        switch(measure) {
            case CUP:
                displayText = "cup";
                break;
            case TBLSP:
                displayText = "tablespoon";
                break;
            case TSP:
                displayText = "teaspoon";
                break;
            case K:
                displayText = "kilogram";
                break;
            case G:
                displayText = "gram";
                break;
            case OZ:
                displayText = "ounce";
                break;
            case UNIT:
            default:
                return "";
        }
        if (amount > 1) {
            displayText += "s";
        }
        return displayText;
    }
}
