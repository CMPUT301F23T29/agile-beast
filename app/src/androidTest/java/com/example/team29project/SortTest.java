package com.example.team29project;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class) @LargeTest
public class SortTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario=
            new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test public void checkSortFrag(){
        onView(withId(R.id.sort_spinner)).check(matches(isDisplayed()));
    }
}
