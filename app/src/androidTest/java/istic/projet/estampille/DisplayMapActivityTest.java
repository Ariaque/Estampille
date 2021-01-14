package istic.projet.estampille;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import istic.projet.estampille.models.APITransformateur;
import istic.projet.estampille.utils.Constants;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DisplayMapActivityTest {

    private APITransformateur apiTransformateur;
    //    @Rule
//    public ActivityScenarioRule<DisplayMapActivity> activityRule
//            = new ActivityScenarioRule<>(DisplayMapActivity.class);
    @Rule
    public IntentsTestRule<DisplayMapActivity> intentRule
            = new IntentsTestRule<DisplayMapActivity>(DisplayMapActivity.class) {

        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            apiTransformateur = new APITransformateur(1, "01.006.009", "01236547896532", "Entreprise A", "7 rue des violettes", "75000", "Paris", "", "", "", "", "");
            apiTransformateur.setIsKnowMoreActive(true);
            Intent result = new Intent(targetContext, DisplayMapActivity.class);
            result.putExtra(Constants.KEY_INTENT_SEARCHED_TRANSFORMATEUR, apiTransformateur);
            return result;
        }
    };

    @Test
    public void getAPItransformateurTest() {
        Intents.intended(Matchers.allOf(
                hasExtra(Constants.KEY_INTENT_SEARCHED_TRANSFORMATEUR, apiTransformateur)));
        onView(withId(R.id.textViewAdress)).check(matches(withText(R.string.adress)));
        onView(withId(R.id.textViewAdress)).check(matches(withText(R.string.name)));
        onView(withId(R.id.textViewAdress)).check(matches(withText("Adresse : 8 rue des violettes")));
        onView(withId(R.id.textViewName)).check(matches(withText("Industriel : Entreprise A")));
    }


}
