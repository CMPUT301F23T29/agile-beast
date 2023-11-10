package com.example.team29project;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.rule.GrantPermissionRule.grant;


import android.app.Instrumentation;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class) @LargeTest
public class CustomCameraTest {
    @Rule
    public ActivityScenarioRule<CustomCameraActivity> scenario=
            new ActivityScenarioRule<CustomCameraActivity>(CustomCameraActivity.class);

    @Test
    public void startCameraTest(){
        onView(withId(R.id.capture_btn)).check(matches(isDisplayed()));


    }
    @Test
    public void captureImageTest(){
        onView(withId(R.id.capture_btn)).perform(click());
        onView(withId(R.id.ok_btn)).check(matches(isDisplayed()));
    }
    @Test
    public void retryCaptureTest(){
        onView(withId(R.id.capture_btn)).perform(click());
        onView(withId(R.id.retry_btn)).perform(click());
        onView(withId(R.id.customPreview)).check(matches(isDisplayed()));
    }

}
