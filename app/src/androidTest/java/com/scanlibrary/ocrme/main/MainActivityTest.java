package com.scanlibrary.ocrme.main;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.scanlibrary.ocrme.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by iuliia on 5/29/17.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class, true, true);

    @Test
    public void btnPhotoTest() {

        onView(ViewMatchers.withId(R.id.photo_btn)).perform(click());
    }

}