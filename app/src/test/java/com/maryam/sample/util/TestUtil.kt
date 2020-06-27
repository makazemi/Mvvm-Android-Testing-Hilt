package com.maryam.sample.util

import com.maryam.sample.model.Post
import com.maryam.sample.model.PostResponse

object TestUtil {

    fun createPostResponse():PostResponse{
        val list=ArrayList<Post>()
        list.add(Post(1,1,"test title 1","test body body 1"))
        list.add(Post(2,2,"test title 2","test body body 2"))
        list.add(Post(3,3,"test title 3","test body body 3"))
        list.add(Post(4,4,"test title 4","test body body 4"))
        list.add(Post(5,5,"test title 5","test body body 5"))
        return PostResponse(list)
    }

    fun createListPost():List<Post>{
        val list=ArrayList<Post>()
        list.add(Post(1,1,"test title 1","test body body 1"))
        list.add(Post(2,2,"test title 2","test body body 2"))
        list.add(Post(3,3,"test title 3","test body body 3"))
        list.add(Post(4,4,"test title 4","test body body 4"))
        list.add(Post(5,5,"test title 5","test body body 5"))
       return list
    }

    fun createPost(id:Int,userId:Int,title:String,body:String):Post{
        return Post(id,userId,title,body)
    }

}