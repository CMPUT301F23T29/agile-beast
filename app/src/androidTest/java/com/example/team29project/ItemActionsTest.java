package com.example.team29project;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import android.content.Intent;
import android.os.SystemClock;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import com.example.team29project.Model.Item;
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
        intent.putExtra("userId", "test1");

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
    public void editingItem() throws InterruptedException {
        addItemThroughUI("galaxy", "200", "Samsung", "15 Pro", "0011010", "good", "nice");
        Thread.sleep(1000);
        onData(is(instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(0).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.edit_button)).perform(click());
        onView(withId(R.id.edit_item_date)).perform(click());
        onView(withId(R.id.picker_month)).perform(swipeUp());
        onView(withId(R.id.ok__pick_button)).perform(click());
        onView(withId(R.id.edit_item_name)).perform(ViewActions.typeText("1"));
        onView(withId(R.id.edit_item_value)).perform(ViewActions.typeText("2"));
        onView(withId(R.id.edit_description)).perform(ViewActions.typeText("3"));
        onView(withId(R.id.edit_comment)).perform(ViewActions.typeText("4"));
        onView(withId(R.id.edit_serialno)).perform(ViewActions.typeText("5"));
        onView(withId(R.id.edit_item_make)).perform(ViewActions.typeText("6"));
        onView(withId(R.id.edit_item_model)).perform(ViewActions.typeText("7"));
        onView(withText("OK")).perform(click());
        Thread.sleep(500);
        onView(withText("galaxy1")).check(matches(isDisplayed()));
        onView(withText("$ 200.00")).check(matches(isDisplayed()));
        onView(withText("00110105")).check(matches(isDisplayed()));
        onView(withText("Samsung6")).check(matches(isDisplayed()));
        onView(withText("15 Pro7")).check(matches(isDisplayed()));
        onView(withText("good3")).check(matches(isDisplayed()));
        onView(withText("nice4")).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.delete_button)).perform(click());
        onView(withText("galaxy1")).perform(click());
    }
    @Test
    public void editingItemTag() throws InterruptedException {
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.edit_tag_item)).perform(click());
        for(int i=1; i<3; i++) {
            String newTagName = "NEWTAG"+i;
            onView(withId(R.id.add_tag)).perform(click());
            onView(withId(R.id.input_tag)).perform(typeText(newTagName));
            onView(withId(R.id.add_tag)).perform(click());
            Thread.sleep(400);
        }
        pressBack();
        onView(withText("OK")).perform(click());
        Thread.sleep(1000);
        pressBack();
        Thread.sleep(400);
        addItemThroughUI("galaxy", "200", "Samsung", "15 Pro", "0011010", "good", "nice");
        Thread.sleep(1000);
        onData(is(instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(0).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.edit_button)).perform(click());
        onView(withId(R.id.tag_chip)).perform(click());
        onView(withText("NEWTAG1")).perform(click());
        onView(withText("NEWTAG2")).perform(click());
        onView(withText("OK")).perform(click());
        Thread.sleep(500);
        onView(withText("NEWTAG1")).check(matches(isDisplayed()));
        onView(withText("NEWTAG2")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
        onView(withText("NEWTAG1")).check(matches(isDisplayed()));
        onView(withText("NEWTAG2")).check(matches(isDisplayed()));
        onView(withId(R.id.back_button)).perform(click());
        onView(withId(R.id.delete_button)).perform(click());
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.edit_tag_item)).perform(click());


    }

}
