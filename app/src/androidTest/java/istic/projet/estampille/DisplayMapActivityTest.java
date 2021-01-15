package istic.projet.estampille;

import android.app.Instrumentation;
import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import istic.projet.estampille.models.APITransformateur;
import istic.projet.estampille.utils.Constants;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

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
        apiTransformateur.setIsKnowMoreActive(true);
    }

    @After
    public void after() {
        intentsTestRule.finishActivity();
        apiTransformateur = null;
    }

    @Test
    public void getAPItransformateurTest() {
        Intent i = new Intent();
        i.putExtra(Constants.KEY_INTENT_SEARCHED_TRANSFORMATEUR, apiTransformateur);
        intentsTestRule.launchActivity(i);
        onView(withId(R.id.textView14)).check(matches(isDisplayed()));
        onView(withText(R.string.info_title)).check(matches(isDisplayed()));
//        String text =  activityRule.getScenario().onActivity(activity -> {activity.getResources()
//                .getString(R.string.adress)
//                + apiTransformateur.getAdresse();});
//        String add = Resources.getSystem().getString(R.string.adress);
        onView(withText(apiTransformateur.getAdresse())).check(matches(isDisplayed()));
        onView(withText(apiTransformateur.getRaisonSociale())).check(matches(isDisplayed()));
    }

    @Test
    public void testClickKnowMoreNotActive() {
        Intent i = new Intent();
        apiTransformateur.setIsKnowMoreActive(false);
        i.putExtra(Constants.KEY_INTENT_SEARCHED_TRANSFORMATEUR, apiTransformateur);
        intentsTestRule.launchActivity(i);
        onView(withId(R.id.button_know_more)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testClickKnowMoreActive() {
        Intent i = new Intent();
        i.putExtra(Constants.KEY_INTENT_SEARCHED_TRANSFORMATEUR, apiTransformateur);
        intentsTestRule.launchActivity(i);

        Instrumentation.ActivityMonitor am = new Instrumentation.ActivityMonitor(KnowMoreActivity.class.getName(), null, true);
        InstrumentationRegistry.getInstrumentation().addMonitor(am);
        onView(withId(R.id.button_know_more)).check(matches(isDisplayed())).perform(click());
        Intents.intended(allOf(
                hasAction(Matchers.equalTo(KnowMoreActivity.class.getName())),
                hasExtraWithKey(Constants.KEY_INTENT_MORE_INFOS_TRANSFORMATEUR)));
        InstrumentationRegistry.getInstrumentation().removeMonitor(am);
    }


}
