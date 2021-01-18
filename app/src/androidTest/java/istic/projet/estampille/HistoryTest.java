package istic.projet.estampille;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Testing history page.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class HistoryTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void before() {
        onView(withId(R.id.tile_history)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.listView)).check(matches(isDisplayed()));
    }

    /**
     * Checks that the delete button is not displayed when the history is empty.
     * Fail if the history isn't empty.
     */
    @Test
    public void testEraseEmptyHistory() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonDeleteHistory)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textEmptyHistory)).check(matches(isDisplayed()));
    }

    /**
     * Checks that the delete button is displayed when the history is empty.
     * Performs the action of deleting the history.
     * Fail if the history is empty.
     */
    @Test
    public void testEraseNotEmptyHistory() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonDeleteHistory)).check(matches(isDisplayed())).perform(click());
        onView(withText(R.string.dialog_delete_message)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(R.string.yes)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.textEmptyHistory)).check(matches(isDisplayed()));
    }
}
