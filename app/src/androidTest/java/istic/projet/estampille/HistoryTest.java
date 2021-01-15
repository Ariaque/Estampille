package istic.projet.estampille;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HistoryTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testEraseHistory() {
        onView(withId(R.id.tile_history)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.buttonDeleteHistory)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.textEmptyHistory)).check(matches(isDisplayed()));
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
    }
}
