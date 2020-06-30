package com.maryam.sample.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.maryam.sample.repository.FakeMainRepository
import com.maryam.sample.ui.postList.PostListViewModel
import com.maryam.sample.util.MainCoroutineRule
import com.maryam.sample.util.TestUtil
import com.maryam.sample.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class PostListViewModelTest {

    // Subject under test
    private lateinit var postListViewModel: PostListViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var repository: FakeMainRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        // We initialise the tasks to 3, with one active and two completed
        repository = FakeMainRepository()
        postListViewModel = PostListViewModel(repository)
    }

    @Test
    fun listPostApiOnly_loading() = mainCoroutineRule.runBlockingTest {

        assertThat(
            postListViewModel.listPostApiOnly.getOrAwaitValue().loading.isLoading,
            `is`(true)
        )

        assertThat(
            postListViewModel.listPostApiOnly.getOrAwaitValue().loading.isLoading,
            `is`(false)
        )

        assertThat(
            postListViewModel.listPostApiOnly.getOrAwaitValue().data?.peekContent(),
            not(nullValue())
        )


        assertThat(
            postListViewModel.listPostApiOnly.getOrAwaitValue().data?.getContentIfNotHandled(),
            `is`(TestUtil.createListPost())
        )
    }

    @Test
    fun listPostApiOnly_error() = mainCoroutineRule.runBlockingTest {
        repository.setReturnError(true)

        assertThat(postListViewModel.listPostApiOnly.getOrAwaitValue().error?.peekContent(), not(nullValue()))
        assertThat(postListViewModel.listPostApiOnly.getOrAwaitValue().error?.peekContent()?.message,`is`("ERROR IN TEST"))
    }
}