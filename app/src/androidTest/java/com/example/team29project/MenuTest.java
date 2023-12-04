package com.example.team29project;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Predicates.not;
import static org.junit.Assert.assertTrue;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.team29project.Controller.DatabaseController;
import com.example.team29project.View.MainPageActivity;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MenuTest {
    private MainPageActivity activity;
    private DatabaseController db;
    @Rule
    public ActivityTestRule<MainPageActivity> mainPageActivityActivityTestRule =
            new ActivityTestRule<>(MainPageActivity.class, false, false);

    @Before
    public void setUp() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainPageActivity.class);
        intent.putExtra("userId", "a");

        mainPageActivityActivityTestRule.launchActivity(intent);

        MainPageActivity activity = mainPageActivityActivityTestRule.getActivity();
        db = activity.getDb();
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
    public void tagButtonAddTagTest() {
        String newTagName = "__NEWTAG";
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.edit_tag_item)).perform(click());
        onView(withId(R.id.add_tag)).perform(click());
        onView(withId(R.id.input_tag)).perform(typeText(newTagName));
        onView(withId(R.id.add_tag)).perform(click());

        onView(withText(newTagName)).check(matches(isDisplayed()));

        // TODO: no clean up function
    }

    @Test public void tagButtonDeleteTagTest() {
        String newTagName = "__NEWTAG";
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.edit_tag_item)).perform(click());
        onView(withId(R.id.add_tag)).perform(click());
        onView(withId(R.id.input_tag)).perform(typeText(newTagName));
        onView(withId(R.id.add_tag)).perform(click());

        onView(withText(newTagName)).check(matches(isDisplayed()));

        onView(withId(R.id.delete_tag)).perform(click());
        onView(withText(newTagName)).perform(click());

        onView(withText(newTagName)).check(doesNotExist());


    }

    @Test
    public void selectItemButtonTest() {
        ArrayList<Integer> selectedItems = activity.getSelectedItems();

        // Selected items should initially be empty
        assertThat(selectedItems, Matchers.empty());

        // Check that items can be selected
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.select_item)).perform(click());
        onData(Matchers.anything())
                .inAdapterView(withId(R.id.items))
                .atPosition(0)
                .perform(click());

        onData(Matchers.anything())
                .inAdapterView(withId(R.id.items))
                .atPosition(1)
                .perform(click());
        assertTrue(selectedItems.contains(0));
        assertTrue(selectedItems.contains(1));

        // Check that selected items can be deselected
        onData(Matchers.anything())
                .inAdapterView(withId(R.id.items))
                .atPosition(0)
                .perform(click());

        onData(Matchers.anything())
                .inAdapterView(withId(R.id.items))
                .atPosition(1)
                .perform(click());
        assertThat(selectedItems, Matchers.empty());
    }

}
