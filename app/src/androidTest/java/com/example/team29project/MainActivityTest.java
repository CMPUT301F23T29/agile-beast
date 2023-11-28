package com.example.team29project;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.team29project.Model.Item;
import com.example.team29project.View.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class) @LargeTest
public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario=
            new ActivityScenarioRule<MainActivity>(MainActivity.class);
    @Test
    public void displayTest(){
        onView(withId(R.id.items)).check(matches(isDisplayed()));
        onView(withId(R.id.value_label)).check(matches(isDisplayed()));
        onView(withId(R.id.menu)).check(matches(isDisplayed()));
        onView(withId(R.id.value_display)).check(matches(isDisplayed()));
        onView(withId(R.id.delete_button)).check(matches(isDisplayed()));
        onView(withId(R.id.sort_by_button)).check(matches(isDisplayed()));
        onView(withId(R.id.filter_button)).check(matches(isDisplayed()));
    }

    @Test
    public void deleteTest(){

    }
}
