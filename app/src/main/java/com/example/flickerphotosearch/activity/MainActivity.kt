package com.example.flickerphotosearch.activity

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.SearchRecentSuggestions
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.flicker.UrlManager
import com.example.flickerphotosearch.MySuggestionProvider
import com.example.flickerphotosearch.R
import com.example.flickerphotosearch.fragment.GalleryFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            Log.d(TAG, "Received_a_new_search_query:" + query)
            val searchRecentSuggestions = SearchRecentSuggestions(this, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
            searchRecentSuggestions.saveRecentQuery(query, null)
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(UrlManager.PREF_SEARCH_QUERY, query).commit()
            val fm = supportFragmentManager
            val fragment = fm.findFragmentById(R.id.gallery_fragment)
            if (fragment != null) {
                (fragment as GalleryFragment).refresh()
            }
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}