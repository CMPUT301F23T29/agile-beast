package com.example.team29project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.team29project.View.MainPageActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class) @LargeTest
public class MainPageActivityTest {

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
    public void testComponentsDisplayed(){
        onView(withId(R.id.items)).check(matches(isDisplayed()));
        onView(withId(R.id.value_label)).check(matches(isDisplayed()));
        onView(withId(R.id.menu)).check(matches(isDisplayed()));
        onView(withId(R.id.value_display)).check(matches(isDisplayed()));
        onView(withId(R.id.delete_button)).check(matches(isDisplayed()));
        onView(withId(R.id.sort_by_button)).check(matches(isDisplayed()));
        onView(withId(R.id.filter_button)).check(matches(isDisplayed()));
    }
}
