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

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class KnowMoreActivityTest1 {

    @Rule
    public ActivityScenarioRule<KnowMoreActivity> activityRule
            = new ActivityScenarioRule<>(KnowMoreActivity.class);
    @Rule
    public IntentsTestRule<KnowMoreActivity> intentsTestRule
            = new IntentsTestRule<KnowMoreActivity>(KnowMoreActivity.class, false, false);

    @Test
    public void testFixContent() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.imageSlider)).check(matches(isDisplayed()));
        onView(withId(R.id.imageButtonWebSite)).check(matches(isDisplayed()));
        onView(withId(R.id.imageButtonVideo)).check(matches(isDisplayed()));
        onView(withId(R.id.imageButtonFb)).check(matches(isDisplayed()));
        onView(withId(R.id.imageButtonIns)).check(matches(isDisplayed()));
        onView(withId(R.id.imageButtonTw)).check(matches(isDisplayed()));
        onView(withId(R.id.textViewTitleDescription)).check(matches(isDisplayed())).check(matches(withText(R.string.title_description)));
        onView(withId(R.id.buttonSeeMore)).check(matches(isDisplayed())).check(matches(withText(R.string.txt_see_more)));
        onView(withId(R.id.buttonPartners)).check(matches(isDisplayed())).check(matches(withText(R.string.txt_partners)));
        onView(withId(R.id.buttonOrigins)).check(matches(isDisplayed())).check(matches(withText(R.string.txt_origins)));
    }

    @Test
    public void testReceivedIntentSimple() {
        APITransformateur apiTransformateur = new APITransformateur(1, "01.006.009", "01236547896532", "Entreprise A", "7 rue des violettes", "75000", "Paris", "", "", "", "", "");
        APIInfosTransformateur apiInfosTransformateur = new APIInfosTransformateur(1, apiTransformateur, null, null,
                null, null, null, null,
                null, null, null, null, null, null, null);
        Intent i = new Intent();
        i.putExtra(Constants.KEY_INTENT_MORE_INFOS_TRANSFORMATEUR, apiInfosTransformateur);
        KnowMoreActivity knowMoreActivity = intentsTestRule.launchActivity(i);
        onView(withId(R.id.textViewTitle)).check(matches(withText(apiInfosTransformateur.getTransformateur().getRaisonSociale())));
        onView(withId(R.id.textViewDescription)).check(matches(not(isDisplayed())));
        onView(withId(R.id.imageButtonWebSite)).check(matches(not(isDisplayed())));
        onView(withId(R.id.imageButtonFb)).check(matches(not(isDisplayed())));
        onView(withId(R.id.imageButtonTw)).check(matches(not(isDisplayed())));
        onView(withId(R.id.imageButtonVideo)).check(matches(not(isDisplayed())));
        onView(withId(R.id.imageButtonIns)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textViewTitleLabels)).check(matches(not(isDisplayed())));
        onData(empty()).inAdapterView(withId(R.id.gridViewLabels));
        onView(withId(R.id.buttonPartners)).check(matches(not(isDisplayed())));
        onView(withId(R.id.buttonOrigins)).check(matches(not(isDisplayed())));
    }


    @Test
    public void testExpandeCollapseDescription() {
        onView(withId(R.id.buttonSeeMore)).check(matches(isDisplayed())).perform(click());
        onView(withText(R.string.txt_see_less)).check(matches(isDisplayed())).perform(click());
        onView(withText(R.string.txt_see_more)).check(matches(isDisplayed()));
    }

}