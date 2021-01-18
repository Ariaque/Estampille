package istic.projet.estampille;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

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

/**
 * Testing the navigation between page with the toolbar.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ToolbarTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Test toolbar content.
     */
    @Test
    public void testToolbar() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.action_home)).check(matches(isDisplayed()));
        onView(withId(R.id.action_write_code)).check(matches(isDisplayed()));
        onView(withId(R.id.action_history)).check(matches(isDisplayed()));
        onView(withId(R.id.action_look_around)).check(matches(isDisplayed()));
        onView(withId(R.id.action_history)).check(matches(isDisplayed()));
    }

    /**
     * Go to manual search page and go back to home page.
     */
    @Test
    public void testGoToWriteCode() {
        onView(withId(R.id.action_write_code)).perform(click());
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.button_pckg_nb)).check(matches(isDisplayed()));
        onView(withId(R.id.button_pckg_nb)).check(matches(withText(R.string.button_search_estampille)));
        pressBack();
        onView(withId(R.id.action_write_code)).check(matches(isDisplayed()));
    }

    /**
     * Go to history page and go back to home page.
     */
    @Test
    public void testGoToHistory() {
        onView(withId(R.id.action_history)).perform(click());
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.listView)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.action_write_code)).check(matches(isDisplayed()));
    }

    /**
     * Go to look around page and go back to home page.
     */
    @Test
    public void testGoToLookAround() {
        onView(withId(R.id.action_look_around)).perform(click());
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_look_around)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.action_write_code)).check(matches(isDisplayed()));
    }

    /**
     * Go to tutorial page and go back to home page.
     */
    @Test
    public void testGoToTutorial() {
        onView(withId(R.id.helpButton)).perform(click());
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.tuto_image)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.action_write_code)).check(matches(isDisplayed()));
    }
}
