package com.example.team29project;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Predicates.not;
import static org.junit.Assert.assertTrue;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.team29project.Controller.DatabaseController;
import com.example.team29project.View.LoginActivity;
import com.example.team29project.View.MainPageActivity;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.UUID;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LogInTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginPageActivityActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class, false, false);

    @Before
    public void setUp() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), LoginActivity.class);
        intent.putExtra("userId", "a");
        loginPageActivityActivityTestRule.launchActivity(intent);


    }

    @Test
    public void loginTest() throws InterruptedException {
        onView(withId(R.id.usernameEdt)).perform(ViewActions.typeText("test1"));
        onView(withId(R.id.passwordEdt)).perform(ViewActions.typeText("test"));
        onView(withId(R.id.login_button)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.main_page)).check(matches(isDisplayed()));
    }
    @Test
    public void InvalidLoginTest() throws InterruptedException {
        onView(withId(R.id.usernameEdt)).perform(ViewActions.typeText("test1ssss"));
        onView(withId(R.id.passwordEdt)).perform(ViewActions.typeText("test"));
        onView(withId(R.id.login_button)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.main_page)).check(doesNotExist());
    }
    @Test
    public void WrongPasswordLoginTest() throws InterruptedException {
        onView(withId(R.id.usernameEdt)).perform(ViewActions.typeText("test1"));
        onView(withId(R.id.passwordEdt)).perform(ViewActions.typeText("aaa"));
        onView(withId(R.id.login_button)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.main_page)).check(doesNotExist());
    }

    @Test
    public void Signup() throws InterruptedException {
        String uniqueId = UUID.randomUUID().toString();
        onView(withId(R.id.usernameEdt)).perform(ViewActions.typeText(uniqueId));
        onView(withId(R.id.passwordEdt)).perform(ViewActions.typeText("test"));
        onView(withId(R.id.sign_up_button)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.main_page)).check(matches(isDisplayed()));

    }
    @Test
    public void InvalidSignup() throws InterruptedException {
        onView(withId(R.id.usernameEdt)).perform(ViewActions.typeText("test1"));
        onView(withId(R.id.passwordEdt)).perform(ViewActions.typeText("testsss"));
        onView(withId(R.id.sign_up_button)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.main_page)).check(doesNotExist());

    }


}
