package com.example.flickerphotosearch.fragment

import android.app.SearchManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.RequestQueue
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.flicker.GalleryItem
import com.example.flicker.UrlManager
import com.example.flickerphotosearch.R
import com.example.flickerphotosearch.adapter.GalleryAdapter
import org.json.JSONException
import org.json.JSONObject

class GalleryFragment : Fragment() {
    private var mRequestQueue: RequestQueue? = null
    private var mRecyclerView: RecyclerView? = null
    private var mLayoutManager: GridLayoutManager? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var mAdapter: GalleryAdapter? = null
    var result: MutableList<GalleryItem>? = null
    private var mLoading = false
    private var mHasMore = true
    private var mSearchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        mRequestQueue = Volley.newRequestQueue(activity)
        mRecyclerView = view.findViewById<View>(R.id.recycler_view) as RecyclerView
        mRecyclerView?.setHasFixedSize(true)
        mLayoutManager = GridLayoutManager(activity, COLUMN_NUM)
        mRecyclerView?.layoutManager = mLayoutManager
        mAdapter = GalleryAdapter(requireActivity(), ArrayList())
        mRecyclerView?.adapter = mAdapter

        mRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItem = mLayoutManager!!.itemCount
                val lastItemPos = mLayoutManager?.findLastVisibleItemPosition()
                if (mHasMore && !mLoading && totalItem - 1 != lastItemPos) {
                    startLoading()
                }
            }
        })

        mSwipeRefreshLayout = view.findViewById<View>(R.id.swipe_refresh) as SwipeRefreshLayout
        mSwipeRefreshLayout?.setOnRefreshListener { refresh() }
        startLoading()
        return view
    }

    fun refresh() {
        mAdapter?.clear()
        startLoading()
    }

    private fun startLoading() {
        Log.d(TAG, "startLoading")
        mLoading = true
        val totalItem = mLayoutManager!!.itemCount
        val page = totalItem / ITEM_PER_PAGE + 1
        val query = PreferenceManager.getDefaultSharedPreferences(activity).getString(UrlManager.PREF_SEARCH_QUERY, null)
        val url = UrlManager.getItemUrl(query, page)

        val stringRequest = StringRequest(url, { response ->
            try {
                val jsonObject = JSONObject(response)
                val photos = jsonObject.getJSONObject("photos")
                Log.d("tag", "photos"+photos)
                if (photos.getInt("pages") == page) {
                    mHasMore = false
                }
                result = ArrayList()
                val photo = photos.getJSONArray("photo")
                for (i in 0 until photo.length()) {
                    val itemObj = photo.getJSONObject(i)
                    val item = GalleryItem(itemObj.getString("id"), itemObj.getString("secret"), itemObj.getString("server"), itemObj.getString("farm"))
                    Log.d("tag", "galleryitem" + item)
                    (result as ArrayList<GalleryItem>).add(item)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            mAdapter?.addAll(result)
            mAdapter?.notifyDataSetChanged()
            mLoading = false
            mSwipeRefreshLayout?.isRefreshing = false
        }) { error ->
            VolleyLog.d(TAG, "Error: " + error.message)
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }
        stringRequest.tag = TAG
        mRequestQueue?.add(stringRequest)
    }

    private fun stopLoading() {
        if (mRequestQueue != null) {
            mRequestQueue?.cancelAll(TAG)
        }
    }

    override fun onStop() {
        super.onStop()
        stopLoading()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu.findItem(R.id.menu_item_search)
        mSearchView = searchItem.actionView as SearchView
        if (mSearchView != null) {
        }
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val name = requireActivity().componentName
        val searchInfo = searchManager.getSearchableInfo(name)
        mSearchView?.setSearchableInfo(searchInfo)
        mSearchView?.setQueryHint(getResources().getString(R.string.search_hint));
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var selectionHandled = false
        selectionHandled = when (item.itemId) {
            R.id.menu_item_search -> {
                requireActivity().onSearchRequested()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return selectionHandled
    }

    companion object {
        private val TAG = GalleryFragment::class.java.simpleName
        private const val COLUMN_NUM = 2
        private const val ITEM_PER_PAGE = 100
    }
}