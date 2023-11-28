package com.example.team29project;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotSelected;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.team29project.View.MainActivity;
import com.example.team29project.View.MainPageActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class) @LargeTest
public class SortTest {

    @Rule
    public ActivityTestRule<MainPageActivity> scenario=
            new ActivityTestRule<MainPageActivity>(MainPageActivity.class);

    @Before
    public void init(){
        scenario.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }


    @Test public void checkSortFragSortdisplay(){
        onView(withId(R.id.sort_by_button)).perform(click());
        onView(withId(R.id.sort_spinner)).check(matches(isDisplayed()));
        onView(withId(R.id.sort_asc_radiobutton)).check(matches(isDisplayed()));
        onView(withId(R.id.sort_desc_radiobutton)).check(matches(isDisplayed()));
        onView(withId(R.id.confirm_sort_button)).check(matches(isDisplayed()));
        onView(withId(R.id.cancel_sort_button)).check(matches(isDisplayed()));
    }

    @Test public void checkSortFragClickable(){
        onView(withId(R.id.sort_by_button)).perform(click());
        onView(withId(R.id.confirm_sort_button)).check(matches(isClickable()));
        onView(withId(R.id.cancel_sort_button)).check(matches(isClickable()));
    }

    @Test public void checkSortFragAscSelectable(){
        onView(withId(R.id.sort_by_button)).perform(click());
        onView(withId(R.id.sort_asc_radiobutton)).check(matches(isNotSelected()));
    }

    @Test public void checkSortFragDescSelectable() {
        onView(withId(R.id.sort_by_button)).perform(click());
        onView(withId(R.id.sort_desc_radiobutton)).check(matches(isNotSelected()));
    }
}
