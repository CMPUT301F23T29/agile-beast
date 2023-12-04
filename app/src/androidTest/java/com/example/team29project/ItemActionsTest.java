package com.example.team29project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.containsString;

import android.content.Intent;
import android.os.SystemClock;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.ActivityTestRule;

import com.example.team29project.View.MainPageActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ItemActionsTest {
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

    private void deleteItems(String... itemName) {
        for (String name: itemName) {
            onView(withId(R.id.delete_button)).perform(click());
            onView(withText(name)).perform(click());
        }
    }

    private void resetFilter() {
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.confirm_filter_button)).perform(click());
    }

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
    public void addItem() {
        // Write the item's attributes
        addItemThroughUI("_Sample", "201", "Apple", "iPhone 12", "123", "descrip", "comment");

        onView(withText("_Sample")).check(matches(isDisplayed()));
    }
    @Test
    public void deleteSingleItemTest(){
        // NOTE: Requires item from addSingleItemTest to exist
        onView(withText("_Sample")).check(matches(isDisplayed()));

        onView(withId(R.id.delete_button)).perform(click());
        onView(withText("_Sample")).perform(click());

        onView(withText("_Sample")).check(doesNotExist());
    }

    @Test
    public void filterItemsByMakeTest() {
        // Set up test by adding items
        addItemThroughUI("_item0", "201", "_Apple", "_iPhone 12", "123", "descrip", "comment");
        addItemThroughUI("_item1", "201", "_Android", "_Samsung", "123", "descrip", "comment");
        addItemThroughUI("_item2", "201", "_Apple", "_iPhone 13", "123", "descrip", "comment");

        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_spinner)).perform(click());
        onView(withText(containsString("Make"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.filter_by_make_editview)).perform(typeText("_Apple"));
        onView(withId(R.id.confirm_filter_button)).perform(click());

        onView(withText("_item0")).check(matches(isDisplayed()));
        onView(withText("_item1")).check(doesNotExist());
        onView(withText("_item2")).check(matches(isDisplayed()));

        // Reset filter
        resetFilter();
    }

}
