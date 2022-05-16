package com.example.flickerphotosearch.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.flickerphotosearch.R
import com.example.flickerphotosearch.fragment.PhotoFragment

class PhotoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)
        if (fragment == null) {
            fragment = PhotoFragment()
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
    }
}