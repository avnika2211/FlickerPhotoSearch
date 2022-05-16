package com.example.flicker

import java.io.Serializable

/**
 * GalleryItem.java
 * FlickerPhotoSearch
 *
 * Created by Avanika Gabani on 10.05.2022.
 * Copyright © 2022 Outdooractive AG. All rights reserved.
 */
class GalleryItem(val id: String, private val secret: String, private val server: String, private val farm: String) : Serializable {
    val url: String
        get() = "http://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret + ".jpg"
}