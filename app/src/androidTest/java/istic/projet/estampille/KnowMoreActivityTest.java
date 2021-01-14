package istic.projet.estampille;

import android.content.Intent;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import istic.projet.estampille.models.APIInfosTransformateur;
import istic.projet.estampille.models.APITransformateur;
import istic.projet.estampille.utils.Constants;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class KnowMoreActivityTest {

    private static APIInfosTransformateur apiInfosTransformateur;
    @Rule
    public ActivityScenarioRule<KnowMoreActivity> activityRule
            = new ActivityScenarioRule<>(KnowMoreActivity.class);
    @Rule
    public IntentsTestRule<KnowMoreActivity> intentsTestRule
            = new IntentsTestRule<KnowMoreActivity>(KnowMoreActivity.class, false, false);

    @Test
    public void testReceivedIntent1() {
        APITransformateur apiTransformateur = new APITransformateur(1, "01.006.009", "01236547896532", "Entreprise A", "7 rue des violettes", "75000", "Paris", "", "", "", "", "");
        apiInfosTransformateur = new APIInfosTransformateur(1, apiTransformateur, "7", "www", "www.google.fr", "www", "www", "dd", false, null, null, null, null, null, null);
        Intent i = new Intent();
        i.putExtra(Constants.KEY_INTENT_MORE_INFOS_TRANSFORMATEUR, apiInfosTransformateur);
        intentsTestRule.launchActivity(i);
        onView(withId(R.id.textViewTitle)).check(matches(withText(apiInfosTransformateur.getTransformateur().getRaisonSociale())));
        onView(withId(R.id.textViewDescription)).check(matches(withText(apiInfosTransformateur.getDescription())));
        onView(withId(R.id.textViewTitleLabels)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testFixContent() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.imageSlider)).check(matches(isDisplayed()));
        onView(withId(R.id.imageButtonWebSite)).check(matches(isDisplayed()));
        onView(withId(R.id.imageButtonVideo)).check(matches(isDisplayed()));
        onView(withId(R.id.imageButtonFb)).check(matches(isDisplayed()));
        onView(withId(R.id.imageButtonIns)).check(matches(isDisplayed()));
        onView(withId(R.id.imageButtonTw)).check(matches(isDisplayed()));
        onView(withId(R.id.textViewTitleDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.textViewTitleDescription)).check(matches(withText(R.string.title_description)));
        onView(withId(R.id.buttonSeeMore)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonSeeMore)).check(matches(withText(R.string.txt_see_more)));
        onView(withId(R.id.buttonPartners)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonPartners)).check(matches(withText(R.string.txt_partners)));
        onView(withId(R.id.buttonOrigins)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonOrigins)).check(matches(withText(R.string.txt_origins)));

    }

    @Test
    public void testExpandeCollapseDescription() {
        onView(withId(R.id.buttonSeeMore)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.buttonSeeMore)).check(matches(withText(R.string.txt_see_less)));
        onView(withId(R.id.buttonSeeMore)).perform(click());
        onView(withId(R.id.buttonSeeMore)).check(matches(withText(R.string.txt_see_more)));
    }

}
