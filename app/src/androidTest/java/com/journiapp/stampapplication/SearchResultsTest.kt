package com.journiapp.stampapplication


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.journiapp.stampapplication.data.network.NetworkUtil
import com.journiapp.stampapplication.presentation.view.StampActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SearchResultsTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(StampActivity::class.java)

    @Before
    fun registerIdleResource() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val client = NetworkUtil.providesOkHttp(context)
        val idlingResource = OkHttp3IdlingResource.create(System.identityHashCode(client).toString(), client)
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @Test
    fun searchAustriaResultsTest() {
        val appCompatButton = onView(
            allOf(
                withId(R.id.btn_search), withText("Add Stamp"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.et_search_destination),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("Austria"), closeSoftKeyboard())

        val textView = onView(
            allOf(
                withId(R.id.tv_destination), withText("Austria"),
                withParent(withParent(withId(R.id.rv_search_suggestion))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Austria")))
    }

    @Test
    fun addAustriaStampTest() {
        TODO("Test that after searching, clicking the Done button returns the expected results")
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
