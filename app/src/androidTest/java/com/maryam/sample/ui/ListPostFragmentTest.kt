package com.maryam.sample.ui

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.launchActivity
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.maryam.sample.R
import com.maryam.sample.ui.postList.ListPostFragment
import com.maryam.sample.util.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maryam.sample.model.Post
import com.maryam.sample.model.PostResponse
import com.maryam.sample.ui.postDetail.DetailPostFragment
import com.maryam.sample.ui.postList.ListPostFragmentDirections
import com.maryam.sample.ui.postList.PostAdapter
import com.maryam.sample.util.EspressoIdlingResourceRule
import com.maryam.sample.util.JsonUtil
import com.maryam.sample.util.ToastMatcher
import org.mockito.Mockito.verify
import org.junit.Rule
import org.mockito.Mockito.mock
import javax.inject.Inject

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ListPostFragmentTest : BaseMainActivityTests(){

    @get: Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()


    lateinit var app:TestBaseApplication

    @Inject
    lateinit var jsonUtil: JsonUtil

    @Before
    fun setup(){
        app = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as TestBaseApplication
    }

    @Test
    fun loadItem_success(){
        val apiService = configureFakeApiService(
            blogsDataSource = Constants.BLOG_POSTS_DATA_FILENAME, // full list of data
            networkDelay = 0L,
            application = app
        )
        configureFakeRepository(apiService,app)
        injectTest(app)

        val scenario = launchFragmentInContainer<ListPostFragment>()

        onView(withId(R.id.rcy_post)).check(matches(isDisplayed()))
        onView(withText("Some titles")).check(matches(isDisplayed()))
        onView(withText("Some other titles")).check(matches(isDisplayed()))
    }


    @Test
    fun loadItem_empty(){
        val apiService = configureFakeApiService(
            blogsDataSource = Constants.EMPTY_LIST, // empty list
            networkDelay = 0L,
            application = app
        )
        configureFakeRepository(apiService,app)
        injectTest(app)

        val scenario = launchFragmentInContainer<ListPostFragment>()

        onView(withText("HTTP 204. Returned NOTHING.")).inRoot(ToastMatcher()).check(matches(isDisplayed()))
        onView(withText("Some titles")).check(doesNotExist())
    }

    @Test
    fun loadItem_error(){
        val apiService = configureFakeApiService(
            blogsDataSource = Constants.SERVER_ERROR_FILENAME, // error
            networkDelay = 0L,
            application = app
        )
        configureFakeRepository(apiService,app)
        injectTest(app)

        val scenario = launchFragmentInContainer<ListPostFragment>()

        onView(withText("SERVER ERROR")).inRoot(ToastMatcher()).check(matches(isDisplayed()))
        onView(withText("Some titles")).check(doesNotExist())
    }

    @Test
    fun detailPostFragment_showItem(){
        val bundle=Bundle().apply {
            putParcelable("postArg",Post(1,"path","title"))
        }

        val scenario = launchFragmentInContainer<DetailPostFragment>(bundle,R.style.AppTheme)

        onView(withId(R.id.txt_id)).check(matches(withText("1")))
        onView(withId(R.id.txt_title)).check(matches(withText("title")))
    }

    @Test
    fun clickItem_navigateToDetail(){

        val apiService = configureFakeApiService(
            blogsDataSource = Constants.BLOG_POSTS_DATA_FILENAME, // full list of data
            networkDelay = 0L,
            application = app
        )
        configureFakeRepository(apiService,app)
        injectTest(app)
        val SELECTED_LIST_INDEX=25

        val scenario = launchActivity<MainActivity>()

        onView(withId(R.id.rcy_post)).check(matches(isDisplayed()))

        onView(withId(R.id.rcy_post)).perform(
            RecyclerViewActions.scrollToPosition<PostAdapter.ViewHolder>(SELECTED_LIST_INDEX)
        )

        // Nav DetailFragment
        onView(withId(R.id.rcy_post)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PostAdapter.ViewHolder>(SELECTED_LIST_INDEX,
                ViewActions.click()
            )
        )

        onView(withId(R.id.txt_id)).check(matches(withText("38")))
        onView(withId(R.id.txt_title)).check(matches(withText("test title 26")))
        onView(withId(R.id.txt_body)).check(matches(withText("https://base_url/bqHgw1GPT6lxngcejPf7QWqKBsSFbHHn6vdQdLPKe.jpeg")))

        pressBack()

        onView(withId(R.id.rcy_post)).check(matches(isDisplayed()))

    }

    @Test
    fun navigationTest(){
        val apiService = configureFakeApiService(
            blogsDataSource = Constants.BLOG_POSTS_DATA_FILENAME, // full list of data
            networkDelay = 0L,
            application = app
        )
        configureFakeRepository(apiService,app)
        injectTest(app)
        val rawJson = jsonUtil.readJSONFromAsset(Constants.BLOG_POSTS_DATA_FILENAME)
        val postResponse = Gson().fromJson<PostResponse>(
            rawJson,
            object : TypeToken<PostResponse>() {}.type
        )
        val SELECTED_LIST_INDEX=16
        val post=postResponse.items[SELECTED_LIST_INDEX]

        // GIVEN - On the home screen
        val scenario = launchFragmentInContainer<ListPostFragment>()
        val navController = mock(NavController::class.java)

        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // WHEN - Click on the first list item
        onView(withId(R.id.rcy_post)).perform(
            RecyclerViewActions.scrollToPosition<PostAdapter.ViewHolder>(SELECTED_LIST_INDEX)
        )

        onView(withId(R.id.rcy_post)).perform(
            RecyclerViewActions.actionOnItemAtPosition<PostAdapter.ViewHolder>(SELECTED_LIST_INDEX,
                ViewActions.click()
            )
        )

        // THEN - Verify that we navigate to the first detail screen
        verify(navController).navigate(
            ListPostFragmentDirections.actionListPostFragmentToDetailPostFragment(post)
        )


    }

    override fun injectTest(application: TestBaseApplication) {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }
}