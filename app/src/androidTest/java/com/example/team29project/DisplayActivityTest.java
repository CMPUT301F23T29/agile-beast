package com.example.team29project;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.team29project.Model.Item;

import org.junit.Test;
import org.junit.Rule;

public class DisplayActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario=
            new ActivityScenarioRule<>(MainActivity.class);

    @Test public void checkItemDetailsDisplayed(){
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.add_new_item)).perform(click());
        onView(withId(R.id.edit_item_date)).perform(click());
        onView(withId(R.id.picker_year)).perform(swipeDown());
        onView(withId(R.id.picker_month)).perform(swipeDown());
        onView(withId(R.id.picker_date)).perform(swipeDown());
        onView(withId(R.id.ok__pick_button)).perform(click());
        onView(withId(R.id.edit_item_name)).perform(ViewActions.typeText("Iphone"));
        onView(withId(R.id.edit_item_value)).perform(ViewActions.typeText("1000"));
        onView(withId(R.id.edit_description)).perform(ViewActions.typeText("Iphone"));
        onView(withId(R.id.edit_comment)).perform(ViewActions.typeText("Iphone"));
        onView(withId(R.id.edit_serialno)).perform(ViewActions.typeText("Iphone"));
        onView(withId(R.id.edit_item_make)).perform(ViewActions.typeText("Apple"));
        onView(withId(R.id.edit_item_model)).perform(ViewActions.typeText("15 Pro"));
        pressBack();
        onData(is(instanceOf(Item.class))).inAdapterView(withId(R.id.items)).atPosition(0).perform(click());

    }
}
