package istic.projet.estampille;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void testBases() {
        onView(withId(R.id.tile_look_around)).check(matches(isDisplayed()));
        onView(withId(R.id.tile_history)).check(matches(isDisplayed()));
        onView(withId(R.id.tile_search)).check(matches(isDisplayed()));
        onView(withId(R.id.tile_scan)).check(matches(isDisplayed()));
        onView(withId(R.id.textView11)).check(matches(withText(R.string.txt_look_around_tile)));
        onView(withId(R.id.textView10)).check(matches(withText(R.string.txt_hisotry_tile)));
        onView(withId(R.id.textView9)).check(matches(withText(R.string.txt_search_tile)));
        onView(withId(R.id.textView8)).check(matches(withText(R.string.txt_scan_tile)));
    }


    @Test
    public void testGoToLookAround() {
        onView(withId(R.id.tile_look_around)).perform(click());
        onView(withId(R.id.tv_look_around)).check(matches(isDisplayed()));
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void testGoToHistory() {
        onView(withId(R.id.tile_history)).perform(click());
        onView(withId(R.id.textEmptyHistory)).check(matches(isDisplayed()));
        onView(withId(R.id.textEmptyHistory)).check(matches(withText(R.string.txt_empty_history)));
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

    @Test
    public void testGoToWritingSearch() {
        onView(withId(R.id.tile_search)).perform(click());
        onView(withId(R.id.button_pckg_nb)).check(matches(isDisplayed()));
        onView(withId(R.id.button_pckg_nb)).check(matches(withText(R.string.button_search_estampille)));
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }

//    @Test
//    public void testGoToScanningSearch() {
//        //TODO
//    }
}
