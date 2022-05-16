package com.example.flicker

import android.net.Uri
import kotlin.jvm.Volatile
import com.example.flicker.UrlManager

/**
 * UrlManager.java
 * FlickerPhotoSearch
 *
 * Created by Avanika Gabani on 11.05.2022.
 * Copyright © 2022 Outdooractive AG. All rights reserved.
 */
object UrlManager {

    const val API_KEY = "37ad288835e4c64fc0cb8af3f3a1a65d"
    const val PREF_SEARCH_QUERY = "searchQuery"

    private const val ENDPOINT = "https://api.flickr.com/services/rest/"
    private const val METHOD_GETRECENT = "flickr.photos.getRecent"
    private const val METHOD_SEARCH = "flickr.photos.search"

    @Volatile
    var instance: UrlManager? = null
        get() {
            if (field == null) {
                synchronized(UrlManager::class.java) {
                    if (field == null) {
                        field = UrlManager
                    }
                }
            }
            return field
        }
        private set

    fun getItemUrl(query: String?, page: Int): String {
        val url: String
        url = if (query != null) {
            Uri.parse(ENDPOINT).buildUpon()
                .appendQueryParameter("method", METHOD_SEARCH)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("text", query)
                .appendQueryParameter("page", page.toString())
                .appendQueryParameter("safe_search", "1")
                .build().toString()
        } else {
            Uri.parse(ENDPOINT).buildUpon()
                .appendQueryParameter("method", METHOD_GETRECENT)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("page", page.toString())
                .appendQueryParameter("safe_search", "1")
                .build().toString()
        }
        return url
    }
}