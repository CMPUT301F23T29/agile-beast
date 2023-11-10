package com.example.team29project;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isSelected;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.team29project.Model.Item;
import com.example.team29project.View.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;

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
