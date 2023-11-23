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
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.team29project.Model.Item;
import com.example.team29project.View.MainActivity;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MenuTest {
    private MainActivity getActivityInstance(ActivityScenarioRule<MainActivity> scenarioRule) {
        final MainActivity[] activity = new MainActivity[1];
        scenarioRule.getScenario().onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity mainActivity) {
                activity[0] = mainActivity;
            }
        });
        return activity[0];
    }
    @Rule
    public ActivityScenarioRule<MainActivity> scenario=
            new ActivityScenarioRule<MainActivity>(MainActivity.class);
    @Test
    public void openMenuTest() {
        // Open menu
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.fragment_main_menu)).check(matches(isDisplayed()));
    }

    @Test
    public void addItemButtonTest() {
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.add_new_item)).perform(click());
        onView(withId(R.id.fragment_input)).check(matches(isDisplayed()));
    }

    @Test
    public void editTagButtonTest() throws Exception {
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.edit_tag_item)).perform(click());

        // TODO: Wait until edit tag is implemented
        throw new Exception();
    }

    @Test
    public void selectItemButtonTest() throws NoSuchFieldException, IllegalAccessException {
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.select_item)).perform(click());
        onData(Matchers.anything())
                .inAdapterView(withId(R.id.items))
                .atPosition(0)
                .perform(click());

        onData(Matchers.anything())
                .inAdapterView(withId(R.id.items))
                .atPosition(0)
                .perform(click());

        MainActivity mainActivity = getActivityInstance(scenario);
        Field isSelectField = MainActivity.class.getDeclaredField("isSelect");
        isSelectField.setAccessible(true);
        boolean isSelectValue = (boolean) isSelectField.get(mainActivity);
        assertTrue(isSelectValue);
    }

}
