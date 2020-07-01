package com.maryam.sample.ui

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.maryam.sample.R
import com.maryam.sample.TestBaseApplication
import com.maryam.sample.di.TestAppComponent
import com.maryam.sample.ui.postList.ListPostFragment
import com.maryam.sample.util.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.maryam.sample.model.Post
import com.maryam.sample.ui.postDetail.DetailPostFragment
import com.maryam.sample.util.EspressoIdlingResourceRule
import org.junit.Rule

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ListPostFragmentTest : BaseMainActivityTests(){

    @get: Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    lateinit var app:TestBaseApplication
    @Before
    fun setup(){
        app = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as TestBaseApplication
    }

    @Test
    fun loadPost_success(){
        val apiService = configureFakeApiService(
            blogsDataSource = Constants.BLOG_POSTS_DATA_FILENAME, // full list of data
            networkDelay = 0L,
            application = app
        )
        configureFakeRepository(apiService,app)
        injectTest(app)

        val scenario = launchFragmentInContainer<ListPostFragment>()

        onView(withId(R.id.rcy_post)).check(matches(isDisplayed()))
    }

    override fun injectTest(application: TestBaseApplication) {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }

    @Test
    fun detailPostFragment_showItem(){
        val bundle=Bundle().apply {
            putParcelable("postArg",Post(1,1,"title","body"))
        }

        val scenario = launchFragmentInContainer<DetailPostFragment>(bundle,R.style.AppTheme)

        onView(withId(R.id.txt_id)).check(matches(withText("1")))
        onView(withId(R.id.txt_user_id)).check(matches(withText("1")))
        onView(withId(R.id.txt_title)).check(matches(withText("title")))
        onView(withId(R.id.txt_body)).check(matches(withText("body")))
    }

}