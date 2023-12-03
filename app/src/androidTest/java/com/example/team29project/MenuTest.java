package com.example.team29project;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.team29project.View.ItemViewActivity;
import com.example.team29project.View.MainActivity;
import com.example.team29project.View.MainPageActivity;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MenuTest {
    @Rule
    public ActivityTestRule<MainPageActivity> mainPageActivityActivityTestRule =
            new ActivityTestRule<>(MainPageActivity.class, false, false);

    @Before
    public void setUp() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainPageActivity.class);
        intent.putExtra("userId", "a");

        mainPageActivityActivityTestRule.launchActivity(intent);
    }

    @Test
    public void openMenuTest() {
        // Open menu
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.fragment_main_menu)).check(matches(isDisplayed()));
    }

    @Test
    public void addItemButtonTest() {
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.add_new_item)).perform(click());
        onView(withId(R.id.fragment_input)).check(matches(isDisplayed()));
    }

    @Test
    public void tagButtonLaunchesTagListTest() {
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.edit_tag_item)).perform(click());

        onView(withText("Delete")).check(matches(isDisplayed()));
    }

    @Test
    public void selectItemButtonTest() throws NoSuchFieldException, IllegalAccessException {
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.select_item)).perform(click());
        onData(Matchers.anything())
                .inAdapterView(withId(R.id.items))
                .atPosition(0)
                .perform(click());

        onData(Matchers.anything())
                .inAdapterView(withId(R.id.items))
                .atPosition(0)
                .perform(click());

    }

}
