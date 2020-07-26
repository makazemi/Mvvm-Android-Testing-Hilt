package com.maryam.sample.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.launchActivity
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.maryam.sample.R
import com.maryam.sample.ui.postList.ListPostFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
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
import com.maryam.sample.api.FakeApiService
import com.maryam.sample.di.AppModule
import com.maryam.sample.model.Post
import com.maryam.sample.model.PostResponse
import com.maryam.sample.repository.FakeMainRepositoryImpl
import com.maryam.sample.ui.postDetail.DetailPostFragment
import com.maryam.sample.ui.postList.ListPostFragmentDirections
import com.maryam.sample.ui.postList.PostAdapter
import com.maryam.sample.util.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.mockito.Mockito.verify
import org.junit.Rule
import org.mockito.Mockito.mock
import javax.inject.Inject

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@UninstallModules(AppModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ListPostFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get: Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Inject
    lateinit var apiService: FakeApiService

    @Inject
    lateinit var mainRepository: FakeMainRepositoryImpl

    @Inject
    lateinit var jsonUtil: JsonUtil


    @Test
    fun loadItem_success(){
        val apiService = configureFakeApiService(
            blogsDataSource = Constants.BLOG_POSTS_DATA_FILENAME, // full list of data
            networkDelay = 0L
        )
        configureFakeRepository(apiService)


        val scenario = launchFragmentInHiltContainer<ListPostFragment>()

        onView(withId(R.id.rcy_post)).check(matches(isDisplayed()))
        onView(withText("Some titles")).check(matches(isDisplayed()))
        onView(withText("Some other titles")).check(matches(isDisplayed()))
    }


    @Test
    fun loadItem_empty(){
        val apiService = configureFakeApiService(
            blogsDataSource = Constants.EMPTY_LIST, // empty list
            networkDelay = 0L
        )
        configureFakeRepository(apiService)


        val scenario = launchFragmentInHiltContainer<ListPostFragment>()

        onView(withText("HTTP 204. Returned NOTHING.")).inRoot(ToastMatcher()).check(matches(isDisplayed()))
        onView(withText("Some titles")).check(doesNotExist())
    }

    @Test
    fun loadItem_error(){
        val apiService = configureFakeApiService(
            blogsDataSource = Constants.SERVER_ERROR_FILENAME, // error
            networkDelay = 0L
        )
        configureFakeRepository(apiService)

        val scenario = launchFragmentInHiltContainer<ListPostFragment>()

        onView(withText("SERVER ERROR")).inRoot(ToastMatcher()).check(matches(isDisplayed()))
        onView(withText("Some titles")).check(doesNotExist())
    }

    @Test
    fun detailPostFragment_showItem(){
        val bundle=Bundle().apply {
            putParcelable("postArg",Post(1,"path","title"))
        }

        val scenario = launchFragmentInHiltContainer<DetailPostFragment>(bundle,R.style.AppTheme)

        onView(withId(R.id.txt_id)).check(matches(withText("1")))
        onView(withId(R.id.txt_title)).check(matches(withText("title")))
    }

    @Test
    fun clickItem_navigateToDetail(){

        val apiService = configureFakeApiService(
            blogsDataSource = Constants.BLOG_POSTS_DATA_FILENAME, // full list of data
            networkDelay = 0L
        )
        configureFakeRepository(apiService)

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
            networkDelay = 0L
        )
        configureFakeRepository(apiService)

        val rawJson = jsonUtil.readJSONFromAsset(Constants.BLOG_POSTS_DATA_FILENAME)
        val postResponse = Gson().fromJson<PostResponse>(
            rawJson,
            object : TypeToken<PostResponse>() {}.type
        )
        val SELECTED_LIST_INDEX=16
        val post=postResponse.items[SELECTED_LIST_INDEX]

        // GIVEN - On the home screen

        val navController = mock(NavController::class.java)
        val scenario = launchFragmentInHiltContainer<ListPostFragment>(){
            Navigation.setViewNavController(this.requireView(), navController)
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
    fun configureFakeApiService(
        blogsDataSource: String? = null,
        networkDelay: Long? = null
    ): FakeApiService {
        blogsDataSource?.let { apiService.blogPostsJsonFileName = it }
        networkDelay?.let { apiService.networkDelay = it }
        return apiService
    }

    fun configureFakeRepository(
        apiService: FakeApiService
    ): FakeMainRepositoryImpl {
        mainRepository.apiService = apiService
        return mainRepository
    }

}