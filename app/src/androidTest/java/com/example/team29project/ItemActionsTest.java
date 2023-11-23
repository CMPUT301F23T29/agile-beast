package com.example.team29project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.team29project.View.DatePicker;
import com.example.team29project.View.MainActivity;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ItemActionsTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario=
            new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void addItem() {

        // Open add item screen
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.add_new_item)).perform(click());

        // Write the item's attributes
        onView(withId(R.id.edit_item_name)).perform(typeText("Sample Item Name"));
        onView(withId(R.id.edit_item_value)).perform(typeText("201"));
        // TODO: figure out how to click date on DatePicker class
//        onView(withId(R.id.edit_item_date)).perform(click());
//        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
//                .perform(PickerActions.setDate(2013, 5, 1));

        onView(withId(R.id.edit_item_make)).perform(typeText("Apple"));
        onView(withId(R.id.edit_item_model)).perform(typeText("iPhone 12"));
        onView(withId(R.id.edit_serialno)).perform(typeText("1245423"));
        onView(withId(R.id.edit_description)).perform(typeText("A description of the item"));
        onView(withId(R.id.edit_comment)).perform(typeText("Comments about the item"));

        onView(withText("OK")).perform(click());
    }
    @Test
    public void deleteSingleItemTest(){

    }
}
