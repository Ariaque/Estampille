package istic.projet.estampille;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import istic.projet.estampille.models.APITransformateur;
import istic.projet.estampille.utils.Constants;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

/**
 * Testing "Information fabrication site" page.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DisplayMapActivityTest {

    @ClassRule
    public static IntentsTestRule<DisplayMapActivity> intentsTestRule
            = new IntentsTestRule<DisplayMapActivity>(DisplayMapActivity.class, false, false);

    private static APITransformateur apiTransformateur;

    @Rule
    public ActivityScenarioRule<DisplayMapActivity> activityRule
            = new ActivityScenarioRule<>(DisplayMapActivity.class);

    @Before
    public void before() {
        apiTransformateur = new APITransformateur(1, "01.006.009", "01236547896532", "Entreprise A", "7 rue des violettes", "75000", "Paris", "", "", "", "", "");
        apiTransformateur.setKnowMoreActive(true);
    }

    @After
    public void after() {
        intentsTestRule.finishActivity();
        apiTransformateur = null;
    }

    /**
     * Testing display according to the received object in intent.
     */
    @Test
    public void getAPItransformateurTest() {
        Intent i = new Intent();
        i.putExtra(Constants.KEY_INTENT_SEARCHED_TRANSFORMATEUR, apiTransformateur);
        intentsTestRule.launchActivity(i);
        onView(withId(R.id.textView14)).check(matches(isDisplayed()));
        onView(withText(R.string.info_title)).check(matches(isDisplayed()));
        onView(withId(R.id.textViewName)).check(matches(withText(containsString(apiTransformateur.getRaisonSociale()))));
        onView(withId(R.id.textViewAdress)).check(matches(withText(containsString(apiTransformateur.getAdresse()))));
    }

    /**
     * Checks if "know more" button is not displayed when a user account is inactive.
     */
    @Test
    public void testClickKnowMoreNotActive() {
        Intent i = new Intent();
        apiTransformateur.setKnowMoreActive(false);
        i.putExtra(Constants.KEY_INTENT_SEARCHED_TRANSFORMATEUR, apiTransformateur);
        intentsTestRule.launchActivity(i);
        onView(withId(R.id.button_know_more)).check(matches(not(isDisplayed())));
    }

    /**
     * Checks if "know more" button is displayed when a user account is active.
     * Perform click on this button. Checks if {@link KnowMoreActivity} is launched.
     */
    @Test
    public void testClickKnowMoreActive() {

        Intent i = new Intent();
        i.putExtra(Constants.KEY_INTENT_SEARCHED_TRANSFORMATEUR, apiTransformateur);
        intentsTestRule.launchActivity(i);
        onView(withId(R.id.button_know_more)).check(matches(isDisplayed())).perform(click());
        //onView(withText(containsString("Chargement en cours…"))).inRoot(isPlatformPopup()).check(matches(isDisplayed()));
        onView(withText(R.string.loading_dialog_message)).inRoot(isPlatformPopup()).check(matches(isDisplayed()));
    }


}
