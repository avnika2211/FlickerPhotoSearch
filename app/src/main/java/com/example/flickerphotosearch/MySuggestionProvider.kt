package com.example.flickerphotosearch

import android.content.SearchRecentSuggestionsProvider
import com.example.flickerphotosearch.MySuggestionProvider

/**
 * MySuggestionProvider.java
 * FlickerPhotoSearch
 *
 * Created by Avanika Gabani on 13.05.2022.
 * Copyright © 2022 Outdooractive AG. All rights reserved.
 */
class MySuggestionProvider : SearchRecentSuggestionsProvider() {
    companion object {
        const val AUTHORITY = "com.example.flickerphotosearch.MySuggestionProvider"
        const val MODE = DATABASE_MODE_QUERIES
    }

    init {
        setupSuggestions(AUTHORITY, MODE)
    }
}