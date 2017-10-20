package com.hackernewsapp.ui.story

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AbsListView
import android.widget.RelativeLayout
import com.hackernewsapp.BaseApplication
import com.hackernewsapp.Constants
import com.hackernewsapp.R
import com.hackernewsapp.StoryInterface
import com.hackernewsapp.data.model.Story
import com.hackernewsapp.ui.base.BaseActivity
import com.hackernewsapp.ui.story.presenter.StoryPresenter
import com.hackernewsapp.ui.story.view.ListingAdapter
import com.hackernewsapp.ui.story.view.StoryView
import com.hackernewsapp.util.NetworkUtil
import com.hackernewsapp.util.ui.MaterialProgressBar
import rx.subscriptions.CompositeSubscription
import java.util.*
import javax.inject.Inject




class MainActivity : BaseActivity(), StoryView {

    @field:[Inject]
    lateinit var mSharedPreferences: SharedPreferences

    @field:[Inject]
    lateinit var storyInterface: StoryInterface

    @field:[Inject]
    lateinit var storyPresenter: StoryPresenter

    private var storyLayout: RelativeLayout? = null
    private var listRecyclerView: RecyclerView? = null


    private val storyList: List<Story>? = null
    private var adapter: ListingAdapter? = null
    private var bottomLayout: RelativeLayout? = null
    private var mLayoutManager: LinearLayoutManager? = null

    // Variables for scroll listener
    private var userScrolled = true
    internal var pastVisiblesItems: Int = 0
    internal var visibleItemCount: Int = 0
    internal var totalItemCount: Int = 0

    private var storiesLoaded = 0
    private var page = 1
    private val data = ArrayList<Long>()

    private var mCompositeSubscription: CompositeSubscription? = null
    private var totalNo = 0

    private var progressBar: MaterialProgressBar? = null
    private var swipeContainer: SwipeRefreshLayout? = null
    private val tempNo = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BaseApplication.storyComponent.inject(this)
        storyPresenter!!.setView(this)
        storyLayout = findViewById(R.id.layout_story_root) as RelativeLayout
        mCompositeSubscription = CompositeSubscription()

        init()
        loadView()
    }

    override fun loadView() {
        if (NetworkUtil.isConnected(applicationContext)) {
            populateRecyclerView()
            implementScrollListener()
            pullToRefresh()
            hideOfflineSnackBar()
        } else {
            displayOfflineSnackbar()
        }
    }

    // Initialize the view
    override fun init() {
        bottomLayout = findViewById(R.id.load_more_items) as RelativeLayout
        progressBar = findViewById(R.id.material_progress_bar) as MaterialProgressBar
        mLayoutManager = LinearLayoutManager(applicationContext)
        listRecyclerView = findViewById(R.id.stories_recyclerview) as RecyclerView
        listRecyclerView!!.setHasFixedSize(true)
        listRecyclerView!!.layoutManager = mLayoutManager// for
    }

    // populate the list view by adding data to arraylist
    override fun populateRecyclerView() {

        progressBar!!.visibility = View.VISIBLE
        storyPresenter!!.getStoryIds(storyInterface!!, Constants.TOP_STORIES, mCompositeSubscription!!, false)

    }

    override fun pullToRefresh() {
        // Pull to refresh
        if (NetworkUtil.isConnected(applicationContext)) {

            // Lookup the swipe container view
            swipeContainer = findViewById(R.id.swipeContainer) as SwipeRefreshLayout
            // Setup refresh listener which triggers new data loading
            swipeContainer!!.setOnRefreshListener {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                refresh(Constants.TOP_STORIES, true)
            }
            // Configure the refreshing colors
            swipeContainer!!.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light)
        } else {
            displayOfflineSnackbar()
        }
    }

    override fun refresh(topStories: String, refresh: Boolean) {
        totalNo = 0
        storiesLoaded = 0
        mCompositeSubscription!!.clear()
        // reset the adapter
        storyPresenter!!.getStoryIds(storyInterface!!, topStories, mCompositeSubscription!!, refresh)
    }


    override fun implementScrollListener() {

        listRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                // If scroll state is touch scroll then set userScrolled
                // true
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
                // Here get the child count, item count and visibleitems
                // from layout manager

                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisiblesItems = mLayoutManager!!.findFirstVisibleItemPosition()

                // Now check if userScrolled is true and also check if
                // the item is end then update recycler view and set
                // userScrolled to false
                if (userScrolled && visibleItemCount + pastVisiblesItems == totalItemCount) {
                    userScrolled = false
                    page = page + 1

                    val nextNumber = storiesLoaded + 1
                    val remaining = totalNo - storiesLoaded

                    if (remaining >= storiesLoaded + Constants.NO_OF_ITEMS_LOADING) {
                        storyPresenter!!.updateRecyclerView(storyInterface!!, mCompositeSubscription!!, storiesLoaded, storiesLoaded + Constants.NO_OF_ITEMS_LOADING)

                    } else {
                        storyPresenter!!.updateRecyclerView(storyInterface!!, mCompositeSubscription!!, storiesLoaded, totalNo)
                    }
                }

            }

        })

    }


    override fun setLayoutVisibility() {
        bottomLayout!!.visibility = View.VISIBLE
    }


    override fun setAdapter(storLoaded: Int?, listArrayList: ArrayList<Story>,
                            refreshedArrayList: ArrayList<Story>, loadmore: Boolean, totalNum: Int?) {
        storiesLoaded = storLoaded!!
        totalNo = totalNum!!
        if (listArrayList.size != 0) {
            if (!loadmore) {

                adapter = ListingAdapter(applicationContext, listArrayList)
                listRecyclerView!!.adapter = adapter// set adapter on recyclerview
                adapter!!.notifyDataSetChanged()// Notify the adapter
            } else {
                adapter!!.addAll(refreshedArrayList)
            }
        }
    }

    override fun doAfterFetchStory() {

        progressBar!!.visibility = View.GONE
        bottomLayout!!.visibility = View.GONE
        swipeContainer!!.isRefreshing = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }


}
