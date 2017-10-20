package com.hackernewsapp.discussion

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.*
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.hackernewsapp.BaseApplication
import com.hackernewsapp.R
import com.hackernewsapp.R.id.appbar
import com.hackernewsapp.StoryInterface
import com.hackernewsapp.adapter.DiscussionAdapter
import com.hackernewsapp.discussion.model.Discussion
import com.hackernewsapp.discussion.presenter.DiscussionPresenter
import com.hackernewsapp.discussion.view.DiscussionView
import com.hackernewsapp.story.model.Story
import com.hackernewsapp.util.Logger
import com.hackernewsapp.util.Misc
import com.hackernewsapp.util.NetworkUtil
import com.hackernewsapp.util.ui.MaterialProgressBar
import rx.subscriptions.CompositeSubscription
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import javax.inject.Inject

class DiscussionActivity : AppCompatActivity(), DiscussionView {

    private val logger = Logger.getLogger(javaClass)
    private var commentLayout: CoordinatorLayout? = null
    private var position: Int = 0
    private var mStory: ArrayList<Story>? = null
    private var mCompositeSubscription: CompositeSubscription? = null

    private var aTitle: String? = null

    private var headerTitle: TextView? = null
    private var headerUrl: TextView? = null
    private var headerPoints: TextView? = null
    private var headerComments: TextView? = null
    private var headerTime: TextView? = null
    private var headerPoster: TextView? = null
    private var headerContent: TextView? = null

    private val discussionArrayList: ArrayList<Discussion>? = null
    private var adapter: DiscussionAdapter? = null
    private var commentRecyclerView: RecyclerView? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var appBarLayout: AppBarLayout? = null
    private var collapsingToolbar: CollapsingToolbarLayout? = null
    private var layoutComment: RelativeLayout? = null
    private var progressBar: MaterialProgressBar? = null
    private var noComment: TextView? = null
    private var snackbarOffline: Snackbar? = null
    private var mainStory: Story? = null
    private var floatingActionButton: FloatingActionButton? = null
    private var storyDirectUrl: String? = ""

    @Inject
    lateinit var storyInterface: StoryInterface

    @field:[Inject]
    lateinit var discussionPresenter: DiscussionPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discussion)
        BaseApplication.storyComponent.inject(this)

        discussionPresenter!!.setView(this)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras
        if (extras != null) {
            position = extras.getInt("position")
            mStory = intent.getSerializableExtra("mStory") as ArrayList<Story>
            aTitle = extras.getString("title")

            if (mStory != null) {
                storyDirectUrl = mStory!![position].url
            }

            if (aTitle != null) {
                setCollapseToolbar(aTitle.toString())
            }
        }


        init()
        mCompositeSubscription = CompositeSubscription()
        mainStory = mStory!![position]
        progressBar = findViewById(R.id.material_progress_bar) as MaterialProgressBar
        progressBar!!.bringToFront()
        noComment = findViewById(R.id.no_comment_text) as TextView
        noComment!!.bringToFront()
        loadView()

    }

    // Initialize the view
    override fun init() {

        fabButtonSetup()


        commentLayout = findViewById(R.id.main_comment_content) as CoordinatorLayout
        mLayoutManager = LinearLayoutManager(applicationContext)
        commentRecyclerView = findViewById(R.id.comment_recyclerview) as RecyclerView
        commentRecyclerView!!.setHasFixedSize(true)
        commentRecyclerView!!.layoutManager = mLayoutManager

        headerTitle = findViewById(R.id.header_title) as TextView
        headerUrl = findViewById(R.id.header_url) as TextView
        headerPoints = findViewById(R.id.header_points) as TextView
        headerComments = findViewById(R.id.header_comments) as TextView
        headerTime = findViewById(R.id.header_time) as TextView
        headerPoster = findViewById(R.id.header_poster) as TextView
        headerContent = findViewById(R.id.header_content) as TextView

    }

    override fun loadView() {
        if (NetworkUtil.isConnected(applicationContext)) {
            if (mainStory != null) {
                setCommentHeader(mainStory)
                discussionPresenter!!.getComments(storyInterface, mCompositeSubscription, applicationContext, mainStory, true)
            }
        } else {
            displayOfflineSnackbar()
        }
    }

    override fun setCommentHeader(story: Story?) {
        var title = story?.title
        var url = story?.url
        var points: Int? = story?.score
        var time: Int? = story?.time
        var commentsNo: Int? = story?.descendants
        var poster = story?.by

        // extract urls to show domain name instead of full url
        try {
            val aURL = URL(url)
            val host = aURL.host
            val protocol = aURL.protocol
            if (host != null && protocol != null) {
                url = protocol + "://" + host
            }

        } catch (e: MalformedURLException) {
            logger.debug(e.localizedMessage)
        }


        headerTitle!!.text = title
        headerUrl!!.text = url
        headerPoints!!.text = points!!.toString() + resources.getString(R.string.story_point_p)
        if (points < 2) {
            headerComments!!.text = commentsNo!!.toString() + " " + resources.getString(R.string.comment_string_one)
        } else {
            headerComments!!.text = commentsNo!!.toString() + " " + resources.getString(R.string.comment_string)
        }
        headerTime!!.text = Misc.formatTime(time!!.toLong()).toString()
        headerPoster!!.text = resources.getString(R.string.comment_by) + poster

    }


    override fun setCollapseToolbar(title: String) {

        collapsingToolbar = findViewById(R.id.collapsing_toolbar) as CollapsingToolbarLayout
        layoutComment = findViewById(R.id.layout_comment_header_content) as RelativeLayout
        collapsingToolbar!!.setExpandedTitleColor(Color.TRANSPARENT)
        //collapsingToolbar.setTitle(title);
        collapsingToolbar!!.title = ""


        appBarLayout = findViewById(appbar) as AppBarLayout
        appBarLayout!!.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (Math.abs(verticalOffset) == appBarLayout.totalScrollRange) {
                // Collapsed
                layoutComment!!.visibility = View.GONE
            } else if (verticalOffset == 0) {
                // Expanded
                layoutComment!!.visibility = View.VISIBLE
            } else {
                // Somewhere in between
                layoutComment!!.visibility = View.VISIBLE
            }
        }

        // set app bar layout
        setAppBarLayout()
    }

    fun setAppBarLayout() {

        if (mStory != null) {

            val orientation = this.resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {

                var heightDp = 392
                var titleLen = 0
                var urlLen = 0

                if (mStory!![position].title != null) titleLen = mStory!![position].title!!.length
                if (mStory!![position].url != null) urlLen = mStory!![position].url!!.length

                if (titleLen > 70) {
                    heightDp = heightDp + 50
                }

                if (urlLen > 70) {
                    heightDp = heightDp + 50
                }

                if (titleLen > 50) {
                    setHeight(heightDp)
                }
            }
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val heightDp = 352
            setHeight(heightDp)
        }
    }

    fun setHeight(heightDp: Int) {
        val lp = appBarLayout!!.layoutParams as CoordinatorLayout.LayoutParams
        lp.height = heightDp.toInt()
        appBarLayout!!.layoutParams = CoordinatorLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, heightDp)
    }


    override fun displayOfflineSnackbar() {
        snackbarOffline = Snackbar.make(commentLayout!!, R.string.no_connection_snackbar, Snackbar.LENGTH_INDEFINITE)
        val snackbarText = snackbarOffline!!.view.findViewById(android.support.design.R.id.snackbar_text) as TextView
        snackbarText.setTextColor(applicationContext.resources.getColor(android.R.color.white))
        snackbarOffline!!.setAction(R.string.snackbar_action_retry) { loadView() }
        snackbarOffline!!.setActionTextColor(resources.getColor(R.color.colorPrimary))
        snackbarOffline!!.show()
    }

    override fun setProgressBarVisible() {
        progressBar!!.visibility = View.VISIBLE
    }

    override fun setProgressBarGone() {
        progressBar!!.visibility = View.GONE
    }

    override fun sayNoComment() {
        progressBar!!.visibility = View.GONE
        if (progressBar!!.visibility == View.GONE) {
            noComment!!.visibility = View.VISIBLE
        }
    }

    override fun setAdapter(discussionArrayList: ArrayList<Discussion>) {
        if (discussionArrayList.size != 0) {

            adapter = DiscussionAdapter(applicationContext, discussionArrayList)
            commentRecyclerView!!.adapter = adapter// set adapter on recyclerview
            adapter!!.notifyDataSetChanged()// Notify the adapter
        }
    }

    override fun fabButtonSetup() {
        floatingActionButton = findViewById(R.id.float_button) as FloatingActionButton
        floatingActionButton!!.setOnClickListener { v -> fabButtonLink(v) }
    }

    override fun fabButtonLink(v: View?) {
        if (v != null && storyDirectUrl != null) {
            Misc.displayLongToast(applicationContext, "Opening web view!")
            val context = v.context
            val intent = Intent(context, WebviewActivity::class.java)
            intent.putExtra("EXTRA_URL", storyDirectUrl)
            val activity = v.context as Activity
            activity.startActivityForResult(intent, 500)
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun shareLink() {
        //sharing implementation here
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = "https://news.ycombinator.com/item?id=" + mStory!![position].id!!
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, aTitle)
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.share_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.action_share -> shareLink()
        }
        return super.onOptionsItemSelected(item)
    }


}
