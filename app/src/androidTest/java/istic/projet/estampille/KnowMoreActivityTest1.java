package istic.projet.estampille;

import android.content.Intent;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import istic.projet.estampille.models.APIInfosTransformateur;
import istic.projet.estampille.models.APITransformateur;
import istic.projet.estampille.utils.Constants;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

/**
 * Testing Know more page.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class KnowMoreActivityTest1 {

    private static APIInfosTransformateur apiInfosTransformateur;
    @Rule
    public ActivityScenarioRule<KnowMoreActivity> activityRule
            = new ActivityScenarioRule<>(KnowMoreActivity.class);
    @Rule
    public IntentsTestRule<KnowMoreActivity> intentsTestRule
            = new IntentsTestRule<KnowMoreActivity>(KnowMoreActivity.class, false, false);

    @BeforeClass
    public static void before() {
        APITransformateur apiTransformateur = new APITransformateur(1, "01.006.009", "01236547896532", "Entreprise A", "7 rue des violettes", "75000", "Paris", "", "", "", "", "");
        apiInfosTransformateur = new APIInfosTransformateur(1, apiTransformateur, null, null,
                null, null, null, null,
                null, null, null, null, null, null, null);
    }

    @After
    public void after() {
        this.intentsTestRule.finishActivity();
    }

    /**
     * Check if all the components are displayed according to the content of the intent received.
     */
    @Test
    public void testReceivedIntentSimple() {
        Intent i = new Intent();
        i.putExtra(Constants.KEY_INTENT_MORE_INFOS_TRANSFORMATEUR, apiInfosTransformateur);
        intentsTestRule.launchActivity(i);
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

    /**
     * Testing the description handling.
     */
    @Test
    public void testExpandeCollapseDescription() {
        Intent i = new Intent();
        apiInfosTransformateur.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed nibh dui, elementum vitae molestie sed, faucibus ut velit. Proin interdum enim eu libero consequat consequat. Curabitur lacinia at velit nec aliquam. Curabitur elementum massa metus. Donec faucibus, nunc nec lacinia gravida, orci ligula efficitur ligula, ac lobortis mi nisl eu elit. Phasellus convallis lorem vel viverra ornare. Praesent ut mollis magna. Praesent pretium vitae arcu ac pharetra. Praesent nec lacus sit amet diam tristique tempus vehicula et dui. Aenean sagittis a velit ac iaculis. Cras rhoncus, nisi a pellentesque porttitor, justo metus molestie leo, et consequat velit ante eget tortor. Aliquam finibus libero quis dui rhoncus volutpat. Nam sed justo quis dui luctus cursus. Phasellus id hendrerit nibh. Proin ultrices pellentesque lorem a tincidunt. Phasellus vulputate ipsum nisi, eu dignissim justo sagittis at. Nullam laoreet sit amet sem vel auctor. Integer lobortis vitae dolor nec efficitur. Vestibulum sit amet sollicitudin nunc. Vivamus eu eros eget sem feugiat accumsan. Nulla viverra porta leo sed commodo. Pellentesque nunc enim, blandit consequat sagittis id, facilisis quis sapien. Morbi in arcu quis ex viverra gravida. Morbi nisi diam, semper id volutpat et, blandit ac odio. Proin sed elit tempus, rutrum sem a, dignissim erat. ");
        i.putExtra(Constants.KEY_INTENT_MORE_INFOS_TRANSFORMATEUR, apiInfosTransformateur);
        intentsTestRule.launchActivity(i);
        onView(withId(R.id.buttonSeeMore)).check(matches(isDisplayed())).perform(click());
        onView(withText(R.string.txt_see_less)).perform(scrollTo(), click());
        onView(withText(R.string.txt_see_more)).check(matches(isDisplayed()));
    }

}