package com.maryam.sample.util

import com.maryam.sample.model.Post
import com.maryam.sample.model.PostResponse

object TestUtil {

    fun createPostResponse():PostResponse{
        val list=ArrayList<Post>()
        list.add(Post(1,title = "test title 1",imagePath = "test body body 1"))
        list.add(Post(2,title = "test title 2",imagePath = "test body body 2"))
        list.add(Post(3,title = "test title 3",imagePath = "test body body 3"))
        list.add(Post(4,title = "test title 4",imagePath = "test body body 4"))
        list.add(Post(5,title = "test title 5",imagePath = "test body body 5"))
        return PostResponse(list)
    }

    fun createListPost():List<Post>{
        val list=ArrayList<Post>()
        list.add(Post(1,title = "test title 1",imagePath = "test body body 1"))
        list.add(Post(2,title = "test title 2",imagePath = "test body body 2"))
        list.add(Post(3,title = "test title 3",imagePath = "test body body 3"))
        list.add(Post(4,title = "test title 4",imagePath = "test body body 4"))
        list.add(Post(5,title = "test title 5",imagePath = "test body body 5"))
       return list
    }

    fun createPost(id:Int,title:String,imagePath:String):Post{
        return Post(id,title,imagePath)
    }

}