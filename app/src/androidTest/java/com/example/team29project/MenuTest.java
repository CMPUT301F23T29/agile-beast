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
import com.example.team29project.Model.Item;
import com.example.team29project.View.MainPageActivity;

import org.hamcrest.CoreMatchers;
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
        intent.putExtra("userId", "test1");

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

    @Test public void tagButtonAddAndDeleteTagTest() {
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

    private void addItemThroughUI(String name, String value, String make, String model, String serialNum, String desc, String comment) {
        // Open add item screen
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.add_new_item)).perform(click());

        // Write the item's attributes
        onView(withId(R.id.edit_item_name)).perform(typeText(name));
        onView(withId(R.id.edit_item_value)).perform(typeText(value));
        onView(withId(R.id.edit_item_date)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.edit_item_make)).perform(typeText(make));
        onView(withId(R.id.edit_item_model)).perform(typeText(model));
        onView(withId(R.id.edit_serialno)).perform(typeText(serialNum));
        onView(withId(R.id.edit_description)).perform(typeText(desc));
        onView(withId(R.id.edit_comment)).perform(typeText(comment));
        onView(withText("OK")).perform(click());
    }

    @Test
    public void deleteMultipleItemsTest() {
        addItemThroughUI("Sample", "205", "Apple", "iPhone 11", "123", "aescrip", "Domment");
        addItemThroughUI("Zample", "201", "Bpple", "iPhone 12", "125", "descrip", "comment");
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.select_item)).perform(click());
        onData(CoreMatchers.is(CoreMatchers.instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(0).perform(click());
        onData(CoreMatchers.is(CoreMatchers.instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(1).perform(click());
        onView(withId(R.id.delete_button)).perform(click());
    }

}
