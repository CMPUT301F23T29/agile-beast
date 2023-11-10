package com.example.team29project;

import android.view.View;
import android.widget.NumberPicker;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class NumberPickerMatchers {
    // Custom matcher to check if a NumberPicker has a specific value
    public static Matcher<View> withValue(final int value) {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                return view instanceof NumberPicker && ((NumberPicker) view).getValue() == value;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with value: " + value);
            }
        };
    }

    // Custom ViewAssertion to check if a NumberPicker has a specific value
    public static ViewAssertion hasValue(final int value) {
        return new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                if (noViewFoundException != null) {
                    throw noViewFoundException;
                }

                if (!(view instanceof NumberPicker)) {
                    throw new IllegalArgumentException("The asserted view must be a NumberPicker");
                }

                NumberPicker numberPicker = (NumberPicker) view;

                if (numberPicker.getValue() != value) {
                    throw new AssertionError("Expected value: " + value +
                            ". Got: " + numberPicker.getValue());
                }
            }
        };
    }
}
