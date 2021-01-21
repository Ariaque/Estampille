package istic.projet.estampille;

import android.app.Instrumentation;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Testing the manual search page.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class WritePackagingNumberTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule
            = new IntentsTestRule<MainActivity>(MainActivity.class, false, false);

    @Before
    public void before() {
        // go to search
        onView(withId(R.id.action_write_code)).perform(click());
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.button_pckg_nb)).check(matches(isDisplayed()));
        onView(withId(R.id.button_pckg_nb)).check(matches(withText(R.string.button_search_estampille)));
    }

    @After
    public void after() {
        this.intentsTestRule.finishActivity();
    }

    /**
     * Launch search with valid stamp.
     */
    @Test
    public void testSearchValid() {
        // write a stamp
        onView(withId(R.id.tf_estampille_1)).check(matches(isDisplayed())).perform(typeText("01"));
        onView(withId(R.id.tf_estampille_2)).check(matches(isDisplayed())).perform(typeText("004"));
        onView(withId(R.id.tf_estampille_3)).check(matches(isDisplayed())).perform(typeText("002"));
        // launch search
        Instrumentation.ActivityMonitor am = new Instrumentation.ActivityMonitor(DisplayMapActivity.class.getName(), null, true);
        InstrumentationRegistry.getInstrumentation().addMonitor(am);
        closeSoftKeyboard();
        onView(withId(R.id.button_pckg_nb)).check(matches(isDisplayed())).perform(click());
        onView(withText(R.string.loading_dialog_message)).inRoot(isDialog()).check(matches(isDisplayed()));
        InstrumentationRegistry.getInstrumentation().removeMonitor(am);
    }
}