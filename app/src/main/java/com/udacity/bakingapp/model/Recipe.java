package com.udacity.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

// TODO use hrisey plugin (Lombok wrapper) so can get @Parceable annotation
// See https://github.com/mg6maciej/hrisey/wiki/Parcelable
public @Data class Recipe implements Parcelable {
    // Variable names need to match keys in baking.json asset
    private int id;
    private String name;
    private int servings;
    private Ingredient[] ingredients;
    private Step[] steps;

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(servings);
        dest.writeTypedArray(ingredients, 0);
        dest.writeTypedArray(steps, 0);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
        // Resource: https://stackoverflow.com/questions/10071502/read-writing-arrays-of-parcelable-objects
        ingredients = (Ingredient[]) in.createTypedArray(Ingredient.CREATOR);
        steps = (Step[]) in.createTypedArray(Step.CREATOR);
    }

    // For testing only
    public Recipe(String name, String stepShortDescription) {
        this.id = 0;
        this.name = name;
        this.servings = 8;
        this.ingredients = new Ingredient[] {
                new Ingredient()
        };
        this.steps = new Step[] {
                new Step(stepShortDescription)
        };
    }

    public static @Data class Ingredient implements Parcelable {
        private double quantity;
        private Measure measure;
        private String ingredient;

        @Override
        public int describeContents() {
            return this.hashCode();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(quantity);
            // Resource: https://stackoverflow.com/questions/38174961/how-to-read-and-write-enum-into-parcel-on-android
            dest.writeString(measure.name());
            dest.writeString(ingredient);
        }

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public Ingredient createFromParcel(Parcel in) {
                return new Ingredient(in);
            }

            public Ingredient[] newArray(int size) {
                return new Ingredient[size];
            }
        };

        public Ingredient(Parcel in) {
            quantity = in.readDouble();
            measure = Measure.valueOf(in.readString());
            ingredient = in.readString();
        }

        // For testing only
        public Ingredient() {
            this.quantity = 1.8;
            this.measure = Measure.UNIT;
            this.ingredient = "eggs";
        }

        public static String getIngredientItem(Ingredient ingredient) {
            return Double.toString(ingredient.getQuantity()) +
                    getDisplayTextFromMeasure(ingredient.getMeasure(), ingredient.getQuantity()) +
                    " " + ingredient.getIngredient();
        }

        private static String getDisplayTextFromMeasure(Measure measure, double amount) {
            String displayText;
            switch(measure) {
                case CUP:
                    displayText = " cup";
                    break;
                case TBLSP:
                    displayText = " tablespoon";
                    break;
                case TSP:
                    displayText = " teaspoon";
                    break;
                case K:
                    displayText = " kilogram";
                    break;
                case G:
                    displayText = " gram";
                    break;
                case OZ:
                    displayText = " ounce";
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

    public static @Data class Step implements Parcelable {
        private int stepId;
        private String shortDescription;
        private String description;
        private String videoURL;
        private String thumbnailURL;

        @Override
        public int describeContents() {
            return this.hashCode();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(stepId);
            dest.writeString(shortDescription);
            dest.writeString(description);
            dest.writeString(videoURL);
            dest.writeString(thumbnailURL);
        }

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public Step createFromParcel(Parcel in) {
                return new Step(in);
            }

            public Step[] newArray(int size) {
                return new Step[size];
            }
        };

        public Step(Parcel in) {
            stepId = in.readInt();
            shortDescription = in.readString();
            description = in.readString();
            videoURL = in.readString();
            thumbnailURL = in.readString();
        }

        // For testing only
        public Step(String shortDescription) {
            this.stepId = 1;
            this.shortDescription = shortDescription;
            this.description = shortDescription;
            this.videoURL = "";
            this.thumbnailURL = "";
        }
    }
}
