package com.udacity.bakingapp.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.bakingapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void recipe_names_areDisplayed() {
        // Resource: https://developer.android.com/training/testing/espresso/basics#finding-view
        onView(allOf(withId(R.id.tv_recipe_card_name), withText("Nutella Pie")))
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.tv_recipe_card_name), withText("Brownies")))
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.tv_recipe_card_name), withText("Yellow Cake")))
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.tv_recipe_card_name), withText("Cheesecake")))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickGridViewItem_OpensRecipeActivity() {
        onData(anything())
                .inAdapterView(withId(R.id.recipe_card_gridview))
                .atPosition(1)
                .perform(click());
        onView(withId(R.id.tv_title_ingredients)).check(matches(withText("Ingredients")));
    }
}
