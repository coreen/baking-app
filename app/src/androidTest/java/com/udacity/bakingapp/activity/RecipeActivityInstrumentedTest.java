package com.udacity.bakingapp.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.model.Recipe;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class RecipeActivityInstrumentedTest {
    private static final String STEP_SHORT_DESCRIPTION = "Test Step Short Description";

    // Resource: https://stackoverflow.com/questions/31752303/espresso-startactivity-that-depends-on-intent
    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule =
            new ActivityTestRule<RecipeActivity>(RecipeActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Intent intent = new Intent();
                    intent.putExtra(
                            RecipeActivity.EXTRA_RECIPE,
                            new Recipe("TestName", STEP_SHORT_DESCRIPTION));
                    return intent;
                }
            };

    @Test
    public void ingredients_areDisplayed() {
        onView(withId(R.id.tv_title_ingredients)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_title_ingredients)).check(matches(withText("Ingredients")));
        onView(withId(R.id.tv_ingredient_item)).check(matches(isDisplayed()));
    }

    @Test
    public void steps_areDisplayed() {
        onView(withId(R.id.tv_step_short_description)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_step_short_description))
                .check(matches(withText(STEP_SHORT_DESCRIPTION)));
    }

    @Test
    public void clickRecyclerViewItem_OpensStepActivity() {
        onView(withId(R.id.tv_step_short_description)).perform(click());
        onView(withId(R.id.tv_description)).check(matches(isDisplayed()));
    }
}
