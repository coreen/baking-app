package com.udacity.bakingapp.activity;

import com.udacity.bakingapp.model.Measure;

public class RecipeActivity {
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
