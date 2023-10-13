package com.udeldev.storyapp.view.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udeldev.storyapp.JsonConverter
import com.udeldev.storyapp.R
import com.udeldev.storyapp.helper.utils.EspressoIdlingResource
import com.udeldev.storyapp.provider.config.ApiConfig
import com.udeldev.storyapp.view.main.MainActivity
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
class LoginActivityTest {

    private val mockWebServer = MockWebServer()
    private val dummyEmail = "test@gmail.com"
    private val dummyPass = "123456789"


    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)

    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loginSuccess (){

        Intents.init()
        onView(withId(R.id.edit_login_email)).perform(replaceText(dummyEmail))
        onView(withId(R.id.edit_login_password)).perform(replaceText(dummyPass))
        onView(withId(R.id.button_login)).perform(click())

        val mockResponse = MockResponse()
            .setResponseCode(201)
            .setBody(JsonConverter.readStringFromFile("success_login.json"))
        mockWebServer.enqueue(mockResponse)

        Thread.sleep(1000)

        intended(hasComponent(MainActivity::class.java.name))
    }

    @Test
    fun loginFailed (){

        onView(withId(R.id.edit_login_email)).perform(replaceText(dummyEmail))
        onView(withId(R.id.edit_login_password)).perform(replaceText(dummyPass))
        onView(withId(R.id.button_login)).perform(click())

        val mockResponse = MockResponse()
            .setResponseCode(401)
            .setBody(JsonConverter.readStringFromFile("failed_login.json"))
        mockWebServer.enqueue(mockResponse)

        Thread.sleep(1000)

        onView(withText("Error")).check(matches(isDisplayed()))
    }

}