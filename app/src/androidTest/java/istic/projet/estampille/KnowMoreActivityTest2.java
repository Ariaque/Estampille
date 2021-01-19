package istic.projet.estampille;

import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import istic.projet.estampille.models.APICertification;
import istic.projet.estampille.models.APIDenreeAnimale;
import istic.projet.estampille.models.APIFermePartenaire;
import istic.projet.estampille.models.APIInfosTransformateur;
import istic.projet.estampille.models.APILabel;
import istic.projet.estampille.models.APITransformateur;
import istic.projet.estampille.models.APIVideo;
import istic.projet.estampille.utils.Constants;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;

/**
 * Testing Know more page.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class KnowMoreActivityTest2 {

    @ClassRule
    public static IntentsTestRule<KnowMoreActivity> intentsTestRule
            = new IntentsTestRule<KnowMoreActivity>(KnowMoreActivity.class, false, false);
    private static APIInfosTransformateur apiInfosTransformateur;
    private static APILabel apiLabel;
    private static APILabel apiLabel2;
    private static APICertification apiCertification;
    private static APIFermePartenaire apiFermePartenaire;
    private static APIFermePartenaire apiFermePartenaire2;
    private static APIDenreeAnimale apiDenreeAnimale;
    private static APIVideo apiVideo;
    @Rule
    public ActivityScenarioRule<KnowMoreActivity> activityRule
            = new ActivityScenarioRule<>(KnowMoreActivity.class);

    @BeforeClass
    public static void before() {
        apiLabel = new APILabel((long) 1, "un label");
        apiLabel2 = new APILabel((long) 1, "un deuxieme label");
        List<APILabel> apiLabelList = new ArrayList<APILabel>();
        apiLabelList.add(apiLabel);
        apiLabelList.add(apiLabel2);
        apiCertification = new APICertification(1, "une certification");
        List<APICertification> apiCertificationList = new ArrayList<APICertification>();
        apiCertificationList.add(apiCertification);
        apiFermePartenaire = new APIFermePartenaire((long) 1, "ma ferme", "description de ma ferme", "https://www.google.com/");
        apiFermePartenaire2 = new APIFermePartenaire((long) 2, "ma ferme 2", "description de ma ferme 2", "https://ent.univ-rennes1.fr");
        List<APIFermePartenaire> apiFermePartenaireList = new ArrayList<>();
        apiFermePartenaireList.add(apiFermePartenaire);
        apiFermePartenaireList.add(apiFermePartenaire2);
        apiDenreeAnimale = new APIDenreeAnimale((long) 1, "boeuf", "origine du bouef");
        List<APIDenreeAnimale> apiDenreeAnimaleList = new ArrayList<>();
        apiDenreeAnimaleList.add(apiDenreeAnimale);
        apiVideo = new APIVideo((long) 1, "https://www.youtube.com/watch?v=gt_a7OfxBLI", "titre de ma video");
        List<APIVideo> apiVideoList = new ArrayList<>();
        apiVideoList.add(apiVideo);
        APITransformateur apiTransformateur = new APITransformateur(1, "01.006.009", "01236547896532", "Entreprise A", "7 rue des violettes", "75000", "Paris", "", "", "", "", "");
        apiInfosTransformateur = new APIInfosTransformateur(1, apiTransformateur, "7", "www",
                "https://www.google.com/", "https://www.facebook.com/pages/Centre-viande-Beauvallet/274439846047509", "https://twitter.com/univrennes1", "https://www.instagram.com/sau_siege/?hl=fr",
                false, null, apiLabelList, apiCertificationList, apiVideoList,
                apiFermePartenaireList, apiDenreeAnimaleList);
    }

    @After
    public void after() {
        intentsTestRule.finishActivity();
    }

    /**
     * Check if all the components are displayed according to the content of the intent received.
     */
    @Test
    public void testDisplayed() {
        Intent i = new Intent();
        i.putExtra(Constants.KEY_INTENT_MORE_INFOS_TRANSFORMATEUR, apiInfosTransformateur);
        intentsTestRule.launchActivity(i);
        onView(withId(R.id.textViewTitle)).check(matches(withText(apiInfosTransformateur.getTransformateur().getRaisonSociale())));
        onView(withId(R.id.textViewDescription)).check(matches(withText(apiInfosTransformateur.getDescription())));
        onView(withId(R.id.imageButtonWebSite)).check(matches(isDisplayed()));
        onView(withId(R.id.imageButtonFb)).check(matches(isDisplayed()));
        onView(withId(R.id.imageButtonTw)).check(matches(isDisplayed()));
        onView(withId(R.id.imageButtonIns)).check(matches(isDisplayed()));
        onView(withId(R.id.imageButtonVideo)).check(matches(isDisplayed()));
        // "Labels & Certifications
        onView(withId(R.id.textViewTitleLabels)).check(matches(isDisplayed()));
        onData(contains(apiLabel.getLibelle()))
                .inAdapterView(withId(R.id.gridViewLabels));
        onData(contains(apiLabel2.getLibelle()))
                .inAdapterView(withId(R.id.gridViewLabels));
        onData(contains(apiCertification.getLibelle()))
                .inAdapterView(withId(R.id.gridViewLabels));
    }

    /**
     * Test of the click on video icon.
     */
    @Test
    public void testPopUpVideo() {
        Intent i = new Intent();
        i.putExtra(Constants.KEY_INTENT_MORE_INFOS_TRANSFORMATEUR, apiInfosTransformateur);
        intentsTestRule.launchActivity(i);
        // "Videos" pop up
        onView(withId(R.id.imageButtonVideo)).perform(click());
        onView(withText(R.string.txt_videos)).inRoot(isDialog()).check(matches(isDisplayed()));
        onData(contains(apiVideo.getTitre()));
    }

    /**
     * Test of the click on "Our partners" button.
     */
    @Test
    public void testPartnerPopUp() {
        Intent i = new Intent();
        i.putExtra(Constants.KEY_INTENT_MORE_INFOS_TRANSFORMATEUR, apiInfosTransformateur);
        intentsTestRule.launchActivity(i);
        // "Fermes partenaires" pop up
        onView(withId(R.id.buttonPartners)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonPartners)).perform(click());
        onView(withText(R.string.txt_partners)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(R.string.txt_origins)).inRoot(not(isDialog()));
        onData(contains(apiFermePartenaire.getDescription()));
        onData(contains(apiFermePartenaire2.getDescription()));
        onView(withText(apiFermePartenaire.getDescription())).inRoot(isDialog()).perform(click());
        Instrumentation.ActivityMonitor am = new Instrumentation.ActivityMonitor(Intent.ACTION_VIEW, null, true);
        InstrumentationRegistry.getInstrumentation().addMonitor(am);
        onView(withText(R.string.txt_website)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        intended(allOf(
                hasAction(Matchers.equalTo(Intent.ACTION_VIEW)),
                hasData(Matchers.equalTo(Uri.parse(apiFermePartenaire.getUrl())))));
        InstrumentationRegistry.getInstrumentation().removeMonitor(am);
    }

    /**
     * Test of the click on "Animals products" button.
     */
    @Test
    public void testProductsPopUp() {
        Intent i = new Intent();
        i.putExtra(Constants.KEY_INTENT_MORE_INFOS_TRANSFORMATEUR, apiInfosTransformateur);
        intentsTestRule.launchActivity(i);
        // "Denrees animals" pop up
        onView(withId(R.id.buttonOrigins)).check(matches(isDisplayed())).perform(click());
        onView(withText(R.string.txt_origins)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(R.string.txt_partners)).inRoot(not(isDialog()));
        pressBack();
        onView(withText(R.string.txt_origins)).inRoot(not(isDialog()));
    }

    /**
     * Test of the click on website icon.
     */
    @Test
    public void testOpenWebsite() {
        Intent i = new Intent();
        i.putExtra(Constants.KEY_INTENT_MORE_INFOS_TRANSFORMATEUR, apiInfosTransformateur);
        intentsTestRule.launchActivity(i);
        Instrumentation.ActivityMonitor am = new Instrumentation.ActivityMonitor(Intent.ACTION_VIEW, null, true);
        InstrumentationRegistry.getInstrumentation().addMonitor(am);
        onView(withId(R.id.imageButtonWebSite)).check(matches(isDisplayed())).perform(click());
        intended(allOf(
                hasAction(Matchers.equalTo(Intent.ACTION_VIEW)),
                hasData(Matchers.equalTo(Uri.parse(apiInfosTransformateur.getUrlSite())))));
        InstrumentationRegistry.getInstrumentation().removeMonitor(am);
    }

    /**
     * Test of the click on facebook icon.
     */
    @Test
    public void testOpenFacebook() {
        Intent i = new Intent();
        i.putExtra(Constants.KEY_INTENT_MORE_INFOS_TRANSFORMATEUR, apiInfosTransformateur);
        intentsTestRule.launchActivity(i);
        Instrumentation.ActivityMonitor am = new Instrumentation.ActivityMonitor(Intent.ACTION_VIEW, null, true);
        InstrumentationRegistry.getInstrumentation().addMonitor(am);
        onView(withId(R.id.imageButtonFb)).check(matches(isDisplayed())).perform(click());
        intended(allOf(
                hasAction(Matchers.equalTo(Intent.ACTION_VIEW)),
                hasData(Matchers.equalTo(Uri.parse(apiInfosTransformateur.getUrlFacebook())))));
        InstrumentationRegistry.getInstrumentation().removeMonitor(am);
    }

    /**
     * Test of the click on Insta icon.
     */
    @Test
    public void testOpenInstagram() {
        Intent i = new Intent();
        i.putExtra(Constants.KEY_INTENT_MORE_INFOS_TRANSFORMATEUR, apiInfosTransformateur);
        intentsTestRule.launchActivity(i);
        Instrumentation.ActivityMonitor am = new Instrumentation.ActivityMonitor(Intent.ACTION_VIEW, null, true);
        InstrumentationRegistry.getInstrumentation().addMonitor(am);
        onView(withId(R.id.imageButtonIns)).check(matches(isDisplayed())).perform(click());
        intended(allOf(
                hasAction(Matchers.equalTo(Intent.ACTION_VIEW)),
                hasData(Matchers.equalTo(Uri.parse(apiInfosTransformateur.getUrlInstagram())))));
        InstrumentationRegistry.getInstrumentation().removeMonitor(am);
    }

    /**
     * Test of the click on Twitter icon.
     */
    @Test
    public void testOpenTwitter() {
        Intent i = new Intent();
        i.putExtra(Constants.KEY_INTENT_MORE_INFOS_TRANSFORMATEUR, apiInfosTransformateur);
        intentsTestRule.launchActivity(i);
        Instrumentation.ActivityMonitor am = new Instrumentation.ActivityMonitor(Intent.ACTION_VIEW, null, true);
        InstrumentationRegistry.getInstrumentation().addMonitor(am);
        onView(withId(R.id.imageButtonTw)).check(matches(isDisplayed())).perform(click());
        intended(allOf(
                hasAction(Matchers.equalTo(Intent.ACTION_VIEW)),
                hasData(Matchers.equalTo(Uri.parse(apiInfosTransformateur.getUrlTwitter())))));
        InstrumentationRegistry.getInstrumentation().removeMonitor(am);
    }
}
