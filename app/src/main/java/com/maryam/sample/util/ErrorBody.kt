package com.maryam.sample.util

import android.util.Log
import org.json.JSONObject

class ErrorBody(
    var code:String?="",
    var message:String?,
    var userMessage:String=""
) {


    companion object{
        fun convertToObject(errorBody:String?):ErrorBody{
            val error=ErrorBody("","","")
            try {

                val jsonError = JSONObject(errorBody!!)
                val errorJson = jsonError.getJSONObject("error")
                error.code=errorJson.getInt("code").toString()
                error.message=errorJson.getString("message")
                error.userMessage=errorJson.getString("userMessage")

            }catch (ex:Exception){
                Log.v("APIRepository",ex.toString())
            }
            return error
        }
    }

    override fun toString(): String {
       return "code: "+code+"\n"+
                "message: "+message+"\n"+
                "userMessage: "+userMessage
    }


}