package com.example.flickerphotosearch.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flicker.GalleryItem
import com.example.flickerphotosearch.R
import com.example.flickerphotosearch.activity.PhotoActivity

/**
 * GalleryAdapter.java
 * FlickerPhotoSearch
 *
 * Created by Avanika Gabani on 11.05.2022.
 * Copyright © 2022 Outdooractive AG. All rights reserved.
 */
class GalleryAdapter(private val mContext: Context, private val mList: MutableList<GalleryItem>) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mImageView: ImageView
        init {
            mImageView = itemView.findViewById<View>(R.id.gallery_item) as ImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]
        holder.mImageView.setOnClickListener {
            val intent = Intent(mContext, PhotoActivity::class.java)
            intent.putExtra("item", item)
            mContext.startActivity(intent)
        }
        Glide.with(mContext).load(item.url).placeholder(R.drawable.ic_launcher_background).dontAnimate().into(holder.mImageView)
        Log.d("tag", "loadimageurl" + item.url)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun addAll(newList: List<GalleryItem>?) {
        mList.addAll(newList!!)
    }

    fun clear() {
        mList.clear()
    }
}