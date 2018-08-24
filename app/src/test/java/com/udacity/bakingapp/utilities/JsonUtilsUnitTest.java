package com.udacity.bakingapp.utilities;

import com.udacity.bakingapp.model.Recipe;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class JsonUtilsUnitTest {
    @Test
    public void getCardNames_isCorrect() {
        final String expectedName = "test_recipe";
        final Recipe[] recipes = new Recipe[] {
                new Recipe(expectedName)
        };

        assertArrayEquals(JsonUtils.getCardNames(recipes), new String[] { expectedName });
    }
}
