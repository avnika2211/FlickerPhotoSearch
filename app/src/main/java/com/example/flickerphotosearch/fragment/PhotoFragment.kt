package com.example.flickerphotosearch.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.flicker.GalleryItem
import com.example.flickerphotosearch.R

class PhotoFragment : Fragment() {
    private var mItem: GalleryItem? = null
    private var mPhoto: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo, container, false)
        mItem = requireActivity().intent.getSerializableExtra("item") as GalleryItem?
        mPhoto = view.findViewById<View>(R.id.photo) as ImageView
        Glide.with(this).load(mItem?.url).thumbnail(0.5f).into(mPhoto!!)
        return view
    }
}