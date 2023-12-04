package com.example.team29project;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.team29project.Controller.DatabaseController;
import com.example.team29project.Model.Item;
import com.example.team29project.View.ItemViewActivity;
import com.example.team29project.View.MainPageActivity;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Rule;

public class ItemViewActivityTest {
    @Rule
    public GrantPermissionRule permissionCamera = GrantPermissionRule.grant(android.Manifest.permission.CAMERA);
    @Rule
    public GrantPermissionRule permissionRead = GrantPermissionRule.grant(android.Manifest.permission.READ_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule permissionWrite = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Rule
    public ActivityTestRule<MainPageActivity> mainPageActivityActivityTestRule =
            new ActivityTestRule<>(MainPageActivity.class, false, false);

    private static boolean isInit = false;

    @Before
    public void setUp() throws InterruptedException {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainPageActivity.class);
        intent.putExtra("userId", "test1");
        mainPageActivityActivityTestRule.launchActivity(intent);
        MainPageActivity activity = mainPageActivityActivityTestRule.getActivity();
        if(isInit ==false){
            onView(withId(R.id.menu)).perform(click());
            onView(withId(R.id.add_new_item)).perform(click());
            onView(withId(R.id.edit_item_date)).perform(click());
            onView(withId(R.id.picker_year)).perform(swipeDown());
            onView(withId(R.id.picker_month)).perform(swipeDown());
            onView(withId(R.id.picker_date)).perform(swipeDown());
            onView(withId(R.id.ok__pick_button)).perform(click());
            onView(withId(R.id.edit_item_name)).perform(ViewActions.typeText("Iphone"));
            onView(withId(R.id.edit_item_value)).perform(ViewActions.typeText("1000"));
            onView(withId(R.id.edit_description)).perform(ViewActions.typeText("good"));
            onView(withId(R.id.edit_comment)).perform(ViewActions.typeText("nice"));
            onView(withId(R.id.edit_serialno)).perform(ViewActions.typeText("0011010"));
            onView(withId(R.id.edit_item_make)).perform(ViewActions.typeText("Apple"));
            onView(withId(R.id.edit_item_model)).perform(ViewActions.typeText("15 Pro"));
            onView(withText("OK")) .perform(click());
            this.isInit= true;
        }
    }

    @After
    public void tearDown() {
        // clear item
        onView(withId(R.id.delete_button)).perform(click());
        onView(withText("Iphone")).perform(click());
        this.isInit =false;
    }

    @Test public void checkItemDetailsDisplayed() throws InterruptedException {
        Thread.sleep(1000);
        onData(is(instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(0).perform(click());
        Thread.sleep(1000);
        onView(withText("Iphone")).check(matches(isDisplayed()));
        onView(withText("$ 1000.00")).check(matches(isDisplayed()));
        onView(withText("0011010")).check(matches(isDisplayed()));
        onView(withText("Apple")).check(matches(isDisplayed()));
        onView(withText("15 Pro")).check(matches(isDisplayed()));
        onView(withText("nice")).check(matches(isDisplayed()));
        onView(withText("good")).check(matches(isDisplayed()));
        pressBack();
    }
    @Test public void ImageDisplayedTest() throws InterruptedException {
        Thread.sleep(1000);
        onData(is(instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(0).perform(click());
        Thread.sleep(1000);
        onView(ViewMatchers.withId(R.id.photo_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
        onView(withText("Camera")).check(matches(isDisplayed()));
        onView(withText("Gallery")).check(matches(isDisplayed()));
        pressBack();
        pressBack();
    }
    @Test
    public void RetryCaptureImageTest() throws InterruptedException {
        Thread.sleep(1000);
        onData(is(instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(0).perform(click());
        Thread.sleep(1000);
        onView(ViewMatchers.withId(R.id.photo_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
        onView(withText("Camera")).perform(click());

        Thread.sleep(1000);
        onView(withId(R.id.capture_btn)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.retry_btn)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.capture_btn)).check(matches(isDisplayed()));
        pressBack();
        pressBack();

    }
    @Test
    public void takePhotoAndDeleteTest() throws InterruptedException {
        Thread.sleep(1000);
        onData(is(instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(0).perform(click());
        Thread.sleep(1000);
        onView(ViewMatchers.withId(R.id.photo_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
        onView(withText("Camera")).perform(click());

        Thread.sleep(1000);
        onView(withId(R.id.capture_btn)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.ok_btn)).perform(click());
        Thread.sleep(3000);
        onView(ViewMatchers.withId(R.id.photo_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,
                        click()));
        onView(withId(R.id.delete_Image)).check(matches(isDisplayed()));
        try {
            onView(ViewMatchers.withId(R.id.photo_view))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(1,
                            click()));
            assertEquals(1,0);
        } catch(Exception e){
            assertEquals(1,1);
        }
        pressBack();
        pressBack();
    }


    @Test
    public void editPressedTest() throws InterruptedException{
        Thread.sleep(1000);
        onData(is(instanceOf(Item.class))).inAdapterView(withId(R.id.items )).atPosition(0).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.edit_button)).perform(click());
        onView(withId(R.id.fragment_input)).check(matches(isDisplayed()));
        pressBack();
        pressBack();

    }

}
