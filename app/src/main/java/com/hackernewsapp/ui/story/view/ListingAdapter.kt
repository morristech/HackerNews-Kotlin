package com.hackernewsapp.ui.story.view


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hackernewsapp.R
import com.hackernewsapp.data.model.Story
import com.hackernewsapp.ui.discussion.DiscussionActivity
import com.hackernewsapp.util.Logger
import com.hackernewsapp.util.Misc
import java.util.*


/**
 * @author Tosin Onikute.
 */

class ListingAdapter(internal var mContext: Context, private val mStory: ArrayList<Story>?) : RecyclerView.Adapter<ListingAdapter.ViewHolder>() {

    private val logger = Logger.getLogger(javaClass)
    private val mTypedValue = TypedValue()
    private val mBackground: Int
    internal var imageUrlList = ArrayList<String>()
    internal var recyclerView: RecyclerView? = null
    private var aTitle: String? = null


    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mhotStory: ImageView
        val mStoryComments: TextView
        val mStoryTitle: TextView
        val mStoryPrettyUrl: TextView
        val mStoryPoints: TextView
        val mStoryTime: TextView
        // fonts
        var typeFace: Typeface? = null

        init {

            mhotStory = mView.findViewById(R.id.hot_story) as ImageView
            mStoryComments = mView.findViewById(R.id.story_comments) as TextView
            mStoryTitle = mView.findViewById(R.id.story_title) as TextView
            mStoryPrettyUrl = mView.findViewById(R.id.story_pretty_url) as TextView
            mStoryPoints = mView.findViewById(R.id.story_point) as TextView
            mStoryTime = mView.findViewById(R.id.story_time) as TextView

        }

        override fun toString(): String {
            return super.toString() + " '" + mStoryTitle.text
        }
    }

    fun getValueAt(position: Int): String {
        return mStory!![position].id.toString()
    }

    init {
        mContext.theme.resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true)
        mBackground = mTypedValue.resourceId
        //recyclerView = recyclerV;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.display_list, parent, false)
        view.setBackgroundResource(mBackground)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /* Set your values */

        val model = mStory!![position]

        var title: String? = ""
        var url: String? = ""
        var commentsNo: Int? = 0
        var points: Int? = 0
        var time: Int? = 0

        if (model.title != null) title = model.title
        if (model.url != null) url = model.url
        if (model.descendants != null) commentsNo = model.descendants
        if (model.score != null) points = model.score
        if (model.time != null) time = model.time

        aTitle = title

        val tLength = title!!.length
        if (tLength >= 80) {
            title = title.substring(0, 80).toLowerCase() + "..."
            title = title.substring(0, 1).toUpperCase() + title.substring(1)
        } else {
            if (tLength > 3) {
                title = title.substring(0, tLength).toLowerCase()
                title = title.substring(0, 1).toUpperCase() + title.substring(1)
            }
        }



        holder.mStoryTitle.text = title

        holder.mStoryPrettyUrl.text = url
        holder.mStoryComments.text = commentsNo.toString()
        holder.mStoryPoints.text = points.toString() + mContext.getString(R.string.story_point_p)
        holder.mStoryTime.text = Misc.formatTime(time!!.toLong()).toString()

        if (points!! > 50) {
            holder.mhotStory.setImageResource(R.drawable.ic_fire)
        } else {
            holder.mhotStory.setImageResource(R.drawable.ic_fire_grey)
        }

        logger.debug(holder.adapterPosition.toString())

        holder.mView.setOnClickListener { v ->
            val context = v.context
            val intent = Intent(context, DiscussionActivity::class.java)
            intent.putExtra("position", holder.adapterPosition)
            intent.putExtra("mStory", mStory)
            intent.putExtra("title", aTitle)
            val activity = v.context as Activity
            activity.startActivityForResult(intent, 500)
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }

    override fun getItemCount(): Int {
        return mStory?.size ?: 0
    }

    fun addAll(data: List<Story>) {
        //mStory.addAll(data);
        notifyDataSetChanged()
    }

    fun clear() {
        mStory!!.clear()
    }

}

