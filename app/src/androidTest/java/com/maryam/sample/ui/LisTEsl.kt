package com.maryam.sample.ui

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.maryam.sample.TestBaseApplication
import com.maryam.sample.di.TestAppComponent
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@InternalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
class LisTEsl /*: BaseMainActivityTests()*/{

    @Test
    fun doNothing(){
        assertEquals(1,1)
    }

//    override fun injectTest(application: TestBaseApplication) {
//        (application.appComponent as TestAppComponent)
//            .inject(this)
//    }
}