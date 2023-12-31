package com.example.team29project;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotSelected;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.team29project.Model.Item;
import com.example.team29project.Model.Tag;
import com.example.team29project.View.MainPageActivity;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class) @LargeTest
public class FilterTest {

    @Rule
    public ActivityTestRule<MainPageActivity> mainPageActivityActivityTestRule =
            new ActivityTestRule<>(MainPageActivity.class, false, false);

    @Before
    public void setUp() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainPageActivity.class);
        intent.putExtra("userId", "test1");
        mainPageActivityActivityTestRule.launchActivity(intent);

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
    private void addItemThroughUIWithTag(String name, String value, String make, String model, String serialNum, String desc, String comment,String tag) throws InterruptedException {
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
        onView(withId(R.id.tag_chip)).perform(click());
        Thread.sleep(1000);
        onView(withText(tag)).perform(click());
        onView(withText("OK")).perform(click());
        Thread.sleep(500);
        onView(withText("OK")).perform(click());
    }


    @Test
    public void checkFilterFragFilterdisplay(){
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_spinner)).check(matches(isDisplayed()));
        onView(withId(R.id.confirm_filter_button)).check(matches(isDisplayed()));
        onView(withId(R.id.cancel_filter_button)).check(matches(isDisplayed()));
    }


    @Test public void checkFilterByMake() throws InterruptedException {
        addItemThroughUI("Sample", "205", "Apple", "iPhone 11", "123", "aescrip", "Domment");
        addItemThroughUI("Zample", "201", "Bpple", "iPhone 12", "125", "descrip", "comment");
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).inRoot(RootMatchers.isPlatformPopup()).atPosition(2).perform(click());
        onView(withId(R.id.filter_by_make_editview)).perform(typeText("Bpple"));
        onView(withId(R.id.confirm_filter_button)).perform(click());
        Thread.sleep(1000);
        onData(CoreMatchers.is(CoreMatchers.instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(0).perform(click());
        Thread.sleep(1000);
        onView(withText("Zample")).check(matches(isDisplayed()));
        onView(withId(R.id.back_button)).perform(click());
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).inRoot(RootMatchers.isPlatformPopup()).atPosition(0).perform(click());
        onView(withId(R.id.confirm_filter_button)).perform(click());
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.select_item)).perform(click());
        onData(CoreMatchers.is(CoreMatchers.instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(0).perform(click());
        onData(CoreMatchers.is(CoreMatchers.instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(1).perform(click());
        onView(withId(R.id.delete_button)).perform(click());

    }
    @Test public void checkFilterByDescription()throws InterruptedException {
        addItemThroughUI("Sample", "205", "Apple", "iPhone 11", "123", "a escrip", "Domment");
        addItemThroughUI("Zample", "201", "Bpple", "iPhone 12", "125", "d escrip", "comment");
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).inRoot(RootMatchers.isPlatformPopup()).atPosition(3).perform(click());
        onView(withId(R.id.filter_by_description_editview)).perform(typeText("d"));
        onView(withId(R.id.confirm_filter_button)).perform(click());
        Thread.sleep(1000);
        onData(CoreMatchers.is(CoreMatchers.instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(0).perform(click());
        Thread.sleep(1000);
        onView(withText("Zample")).check(matches(isDisplayed()));
        onView(withId(R.id.back_button)).perform(click());
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).inRoot(RootMatchers.isPlatformPopup()).atPosition(0).perform(click());
        onView(withId(R.id.confirm_filter_button)).perform(click());
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.select_item)).perform(click());
        onData(CoreMatchers.is(CoreMatchers.instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(0).perform(click());
        onData(CoreMatchers.is(CoreMatchers.instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(1).perform(click());
        onView(withId(R.id.delete_button)).perform(click());



    }
    @Test public void filterByDateTest() throws InterruptedException {
        addItemThroughUI("Zample", "201", "Bpple", "iPhone 12", "125", "descrip", "comment");
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.add_new_item)).perform(click());

        // Write the item's attributes
        onView(withId(R.id.edit_item_name)).perform(typeText("Sample"));
        onView(withId(R.id.edit_item_value)).perform(typeText("205"));
        onView(withId(R.id.edit_item_date)).perform(click());
        onView(withId(R.id.picker_year)).perform(swipeDown());
        onView(withId(R.id.picker_month)).perform(swipeDown());
        onView(withId(R.id.picker_date)).perform(swipeDown());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.edit_item_make)).perform(typeText("Apple"));
        onView(withId(R.id.edit_item_model)).perform(typeText("iPhone 12"));
        onView(withId(R.id.edit_serialno)).perform(typeText("123"));
        onView(withId(R.id.edit_description)).perform(typeText("aescrip"));
        onView(withId(R.id.edit_comment)).perform(typeText("comment"));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).inRoot(RootMatchers.isPlatformPopup()).atPosition(1).perform(click());
        onView(withId(R.id.filter_by_start_date_editview)).perform(click());
        onView(withId(R.id.picker_month)).perform(swipeDown());
        onView(withId(R.id.picker_date)).perform(swipeDown());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.filter_by_end_date_editview)).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.confirm_filter_button)).perform(click());
        Thread.sleep(1000);
        onData(CoreMatchers.is(CoreMatchers.instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(0).perform(click());
        Thread.sleep(1000);
        onView(withText("Zample")).check(matches(isDisplayed()));
        onView(withId(R.id.back_button)).perform(click());
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).inRoot(RootMatchers.isPlatformPopup()).atPosition(0).perform(click());
        onView(withId(R.id.confirm_filter_button)).perform(click());
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.select_item)).perform(click());
        onData(CoreMatchers.is(CoreMatchers.instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(0).perform(click());
        onData(CoreMatchers.is(CoreMatchers.instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(1).perform(click());
        onView(withId(R.id.delete_button)).perform(click());



    }


    //If it does not work need to wipe the data of the devices
    @Test public void filterByTagTest() throws InterruptedException {

        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.edit_tag_item)).perform(click());
        for(int i=1; i<3; i++) {
            String newTagName = "NEWTAG"+i;
            onView(withId(R.id.add_tag)).perform(click());
            onView(withId(R.id.input_tag)).perform(typeText(newTagName),closeSoftKeyboard());
            onView(withId(R.id.add_tag)).perform(click());

        }
        onView(withText("OK")).perform(click());
        pressBack();
        Thread.sleep(1000);
        addItemThroughUIWithTag("Sample", "205", "Apple", "iPhone 11", "123", "descrip", "Domment","NEWTAG1");
        addItemThroughUIWithTag("Zample", "201", "Bpple", "iPhone 12", "125", "aescrip", "comment","NEWTAG2");

        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).inRoot(RootMatchers.isPlatformPopup()).atPosition(4).perform(click());
        onView(withId(R.id.filter_by_tags)).perform(click());
        onData(allOf(is(instanceOf(Tag.class)))).inRoot(RootMatchers.isPlatformPopup()).atPosition(1).perform(click());
        onView(withId(R.id.confirm_filter_button)).perform(click());
        Thread.sleep(1000);
        onData(CoreMatchers.is(CoreMatchers.instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(0).perform(click());
        Thread.sleep(1000);
        onView(withText("Zample")).check(matches(isDisplayed()));
        onView(withId(R.id.back_button)).perform(click());
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.filter_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).inRoot(RootMatchers.isPlatformPopup()).atPosition(0).perform(click());
        onView(withId(R.id.confirm_filter_button)).perform(click());
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.select_item)).perform(click());
        onData(CoreMatchers.is(CoreMatchers.instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(0).perform(click());
        onData(CoreMatchers.is(CoreMatchers.instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(1).perform(click());
        onView(withId(R.id.delete_button)).perform(click());

        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.edit_tag_item)).perform(click());
        for(int i=1; i<3; i++) {
            String newTagName = "NEWTAG"+i;
            onView(withId(R.id.delete_tag)).perform(click());
            onData(CoreMatchers.is(CoreMatchers.instanceOf(Tag.class))).inAdapterView(withId(R.id.tag_listview)).atPosition(0).perform(click());
            Thread.sleep(1000);
        }
        onView(withText("OK")).perform(click());
    }



}