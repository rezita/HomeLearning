package com.github.rezita.homelearning.network

import android.net.Uri
import com.github.rezita.homelearning.BuildConfig

object URLBuilder {
    private val _baseURL = "script.google.com"
    private val _baseURL2 = "/macros/s/"

    private val scriptID = BuildConfig.scriptID
    private val sheetID = BuildConfig.sheetID

    fun getGetURLAddress(action: SheetAction, userName: String?): String{
        val uri = Uri.Builder()
        uri.scheme("https")
            .authority(_baseURL)
            .appendEncodedPath(_baseURL2)
            .appendPath(scriptID)
            .appendPath("exec")
            .appendQueryParameter("ssId", sheetID)
            .appendQueryParameter("action", action.value)
        if (userName != "") {
            uri.appendQueryParameter("userName", userName)
        }

        return uri.build().toString()
    }

    fun getPostURLAddress(): String{
        val uri = Uri.Builder()
        uri.scheme("https")
            .authority(_baseURL)
            .appendEncodedPath(_baseURL2)
            .appendPath(scriptID)
            .appendPath("exec")

        return uri.build().toString()
    }

    fun getSheetID(): String{
        return sheetID
    }
}