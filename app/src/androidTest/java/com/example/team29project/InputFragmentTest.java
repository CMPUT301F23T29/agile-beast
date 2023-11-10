package com.example.team29project;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;



import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.test.filters.LargeTest;


import com.example.team29project.View.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class) @LargeTest
public class InputFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario=
            new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void addValidItemTest(){
        //Test case 1 (valid)
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
        onView(withText("OK")) .perform(click());
        onView(withText("Iphone")).check(matches(isDisplayed()));





    }
    //Test case 2 (Invalid)
    @Test
    public void addInvalidItemTest() {
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.add_new_item)).perform(click());
        onView(withId(R.id.edit_item_date)).perform(click());
        onView(withId(R.id.picker_month)).perform(swipeUp());
        onView(withId(R.id.ok__pick_button)).perform(click());
        onView(withId(R.id.edit_item_name)).perform(ViewActions.typeText("Nexus"));
        onView(withId(R.id.edit_item_value)).perform(ViewActions.typeText("1000.."));
        onView(withId(R.id.edit_description)).perform(ViewActions.typeText("Nexus"));
        onView(withId(R.id.edit_comment)).perform(ViewActions.typeText("Nexus"));
        onView(withId(R.id.edit_serialno)).perform(ViewActions.typeText("Nexus"));
        onView(withId(R.id.edit_item_make)).perform(ViewActions.typeText("Google"));
        onView(withId(R.id.edit_item_model)).perform(ViewActions.typeText("15 Pro"));
        pressBack();
        onView(withText("OK")).perform(click());
        onView(withText("Nexus")).check(doesNotExist());

    }









}
