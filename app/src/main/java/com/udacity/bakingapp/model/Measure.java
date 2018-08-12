package com.udacity.bakingapp.model;

// TODO enums actually take up more space than primatives, use Android's TypeDef instead for better performance
// See https://android.jlelse.eu/android-performance-avoid-using-enum-on-android-326be0794dc3
public enum Measure {
    /**
     * Cup
     */
    CUP,
    /**
     * Tablespoon
     */
    TBLSP,
    /**
     * Teaspoon
     */
    TSP,
    /**
     * Kilogram
     */
    K,
    /**
     * Gram
     */
    G,
    /**
     * Ounce
     */
    OZ,
    /**
     * No show, measure implicit
     */
    UNIT
}
