package com.fthiery.mareu;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("com.fthiery.mareu", appContext.getPackageName());
    }

    /**
     * Ensures that the RecyclerView is not empty
     */
    @Test
    public void meetingList_shouldNotBeEmpty() {
        onView(withId(R.id.meeting_recycler_view)).check(matches(hasMinimumChildCount(4)));
    }

    /**
     * When we click on the recycler icon, the item is deleted
     */
    @Test
    public void deleteButton_shouldRemoveItem() {
        // Given
        onView(withId(R.id.meeting_recycler_view)).check(matches(hasChildCount(4)));
        // When
        onView(withId(R.id.meeting_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1,new DeleteViewAction()));
        // Then
        onView(withId(R.id.meeting_recycler_view)).check(matches(hasChildCount(3)));
    }

    /**
     * Check that we can add an item
     */
    @Test
    public void floatingActionButton_shouldAddItem() {
        // Given
        onView(withId(R.id.meeting_recycler_view)).check(matches(hasChildCount(4)));
        // When
        onView(withId(R.id.fab_add_meeting)).perform(click());
        onView(withId(R.id.meeting_title_edit)).perform(typeText("Test"));
        onView(withId(R.id.meeting_room_edit)).perform(typeText("Test"));
        onView(withId(R.id.meeting_participants_edit)).perform(typeText("Test"));
        onView(withId(R.id.new_meeting_save_button)).perform(click());
        // Then
        onView(withId(R.id.meeting_recycler_view)).check(matches(hasChildCount(5)));
    }

    /**
     * Check that we can filter the list by date
     */
    @Test
    public void filterByDateAction_shouldFilterTheList() {
        // Given
        onView(withId(R.id.meeting_recycler_view)).check(matches(hasChildCount(4)));
        // When
        onView(withId(R.id.filter_menu_item)).perform(click());
        onView(withText(R.string.filter_by_date_menu_item)).perform(click());
        onView(withId(R.id.mtrl_picker_header_toggle)).perform(click());
        onView(childAtPosition(childAtPosition(withId(R.id.mtrl_picker_text_input_range_start),0),1)).perform(replaceText("25/07/2021"));
        onView(childAtPosition(childAtPosition(withId(R.id.mtrl_picker_text_input_range_end), 0),1)).perform(replaceText("26/07/2021"));
        onView(withTagValue(equalTo("CONFIRM_BUTTON_TAG"))).perform(click());
        // Then
        onView(withId(R.id.meeting_recycler_view)).check(matches(hasChildCount(1)));
    }

    /**
     * Check that we can filter the list by place
     */
    @Test
    @Ignore
    public void filterByPlaceAction_shouldFilterTheList() throws InterruptedException {
        // Laisser tomber ce test à moins de trouver comment réveiller Espresso après l'ouverture du dialog
        // Given
        onView(withId(R.id.meeting_recycler_view)).check(matches(hasChildCount(4)));
        // When
        onView(withId(R.id.filter_menu_item)).perform(click());
        onView(withText(R.string.filter_by_place_menu_item)).perform(click());
        onView(withId(R.id.meeting_room_edit)).perform(typeText("Salle Mario"));
        onView(withId(android.R.id.button1)).perform(click());
        // Then
        onView(withId(R.id.meeting_recycler_view)).check(matches(hasChildCount(1)));
    }

    /**
     * Check that we can reset the filter
     */
    @Test
    public void resetFilterAction_shouldResetTheList() {
        // Given
        onView(withId(R.id.meeting_recycler_view)).check(matches(hasChildCount(4)));
        // When
        onView(withId(R.id.filter_menu_item)).perform(click());
        onView(withText(R.string.filter_by_date_menu_item)).perform(click());
        onView(withTagValue(equalTo("CONFIRM_BUTTON_TAG"))).perform(click());
        onView(withId(R.id.reset_filter)).perform(click());
        // Then
        onView(withId(R.id.meeting_recycler_view)).check(matches(hasChildCount(4)));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}