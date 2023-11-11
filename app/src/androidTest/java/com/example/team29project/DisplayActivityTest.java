package com.example.team29project;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import android.content.Intent;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.team29project.Model.Item;
import com.example.team29project.View.DisplayActivity;
import com.example.team29project.View.MainActivity;

import org.junit.Test;
import org.junit.Rule;

public class DisplayActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<>(MainActivity.class);

    @Test public void checkEditFragmentDisplayed(){
        onView(withId(R.id.edit_button)).perform(click());
        onView(withId(R.id.fragment_input)).check(matches(isDisplayed()));
    }
    @Test public void checkItemDetailsDisplayed(){
        onData(allOf(instanceOf(Item.class))).inAdapterView(withId(R.id.items)).atPosition(0).perform(click());

    }
}
