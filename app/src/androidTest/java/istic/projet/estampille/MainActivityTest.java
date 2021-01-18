package istic.projet.estampille;

import android.app.Instrumentation;
import android.content.Intent;
import android.provider.MediaStore;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Testing main page.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    @ClassRule
    public static IntentsTestRule<MainActivity> intentsTestRule
            = new IntentsTestRule<MainActivity>(MainActivity.class, false, false);

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @After
    public void after() {
        intentsTestRule.finishActivity();
    }

    /**
     * Testing display of all the components.
     */
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

    /**
     * Go to look around page with button and go back to home page.
     */
    @Test
    public void testGoToLookAround() {
        onView(withId(R.id.tile_look_around)).perform(click());
        onView(withId(R.id.tv_look_around)).check(matches(isDisplayed())).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.action_write_code)).check(matches(isDisplayed()));
    }

    /**
     * Go to history page with button and go back to home page.
     */
    @Test
    public void testGoToHistory() {
        onView(withId(R.id.tile_history)).perform(click());
        onView(withId(R.id.tile_history)).check(matches(not(isDisplayed())));
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.action_write_code)).check(matches(isDisplayed()));
    }

    /**
     * Go to manual search page with button and go back to home page.
     */
    @Test
    public void testGoToWritingSearch() {
        onView(withId(R.id.tile_search)).perform(click());
        onView(withId(R.id.button_pckg_nb)).check(matches(isDisplayed())).check(matches(withText(R.string.button_search_estampille)));
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.action_write_code)).check(matches(isDisplayed()));
    }

    /**
     * Go to scan page with button, check opening camera.
     */
    @Test
    public void testGoToScanningSearch() {
        intentsTestRule.launchActivity(null);
        Instrumentation.ActivityMonitor am = new Instrumentation.ActivityMonitor(String.valueOf(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)), null, true);
        InstrumentationRegistry.getInstrumentation().addMonitor(am);
        onView(withId(R.id.tile_scan)).perform(click());
        Intents.intended(hasAction(Matchers.equalTo(MediaStore.ACTION_IMAGE_CAPTURE)));
        InstrumentationRegistry.getInstrumentation().removeMonitor(am);
    }
}
