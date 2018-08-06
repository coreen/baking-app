package com.udacity.bakingapp.model;

import lombok.Data;

public @Data class Recipe {
    private int recipeId;
    private String name;
    private int servings;
    private Ingredients[] ingredients;
    private Steps[] steps;

    public static class Ingredients {
        private double quantity;
        private Measure measure;
        private String ingredient;
    }

    public static class Steps {
        private int stepId;
        private String shortDescription;
        private String description;
        private String videoURL;
        private String thumbnailURL;
    }
}
