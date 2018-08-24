package com.udacity.bakingapp.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.fragment.RecipeStepFragment;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class StepActivityInstrumentedTest {
    private static final String DESCRIPTION_TEXT = "Recipe Introduction";

    @Rule
    public ActivityTestRule<StepActivity> mActivityTestRule =
            new ActivityTestRule<StepActivity>(StepActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Intent intent = new Intent();
                    intent.putExtra(
                            RecipeStepFragment.EXTRA_MEDIA_URL,
                            "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4");
                    intent.putExtra(RecipeStepFragment.EXTRA_DESCRIPTION, DESCRIPTION_TEXT);
                    return intent;
                }
            };

    @Test
    public void exoplayer_isDisplayed() {
        onView(withId(R.id.player_view)).check(matches(isDisplayed()));
    }

    @Test
    public void description_isDisplayed() {
        onView(withId(R.id.tv_description)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_description)).check(matches(withText(DESCRIPTION_TEXT)));
    }
}
