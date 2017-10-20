package com.hackernewsapp.ui.discussion.view


import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.hackernewsapp.R
import com.hackernewsapp.data.model.Discussion
import com.hackernewsapp.util.Misc
import org.apache.commons.lang.StringEscapeUtils
import java.util.*


/**
 * @author Tosin Onikute.
 */

class DiscussionAdapter(internal var mContext: Context, private val mDiscussion: ArrayList<Discussion>?) : RecyclerView.Adapter<DiscussionAdapter.ViewHolder>() {

    private val mTypedValue = TypedValue()
    private val mBackground: Int
    internal var recyclerView: RecyclerView? = null


    internal var indentWidth: Int = 0
    internal var colorBg: Int = 0
    internal var colorOrange: Int = 0

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mAuthor: TextView
        val mCommentTime: TextView
        val theComment: TextView
        val txtIndent: TextView
        var relativeLayout: RelativeLayout
        // fonts
        var typeFace: Typeface? = null

        init {

            mAuthor = mView.findViewById(R.id.author) as TextView
            mCommentTime = mView.findViewById(R.id.time) as TextView
            theComment = mView.findViewById(R.id.the_comment) as TextView
            txtIndent = mView.findViewById(R.id.indent) as TextView
            relativeLayout = mView.findViewById(R.id.relative_comment_list) as RelativeLayout

        }

        override fun toString(): String {
            return super.toString() + " '" + mAuthor.text
        }
    }

    fun getValueAt(position: Int): String {
        return mDiscussion!![position].getId().toString()
    }

    init {
        mContext.theme.resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true)
        mBackground = mTypedValue.resourceId

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.comment_list, parent, false)
        view.setBackgroundResource(mBackground)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /* Set your values */

        val model = mDiscussion!![position]

        var author: String? = ""
        var time: Int? = 0
        var comment = ""

        if (model.by != null) author = model.by
        if (model.time != null) time = model.time
        if (model.text != null) {
            val commentSpanned = Html.fromHtml(StringEscapeUtils.unescapeHtml(model.text))
            comment = commentSpanned.toString()
        }


        holder.mAuthor.text = author
        holder.mCommentTime.text = Misc.formatTime(time!!.toLong()).toString()
        holder.theComment.text = comment.toString()


        // set indent margins
        if (model.level != 0) {
            val layoutParams = RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            var level = 0

            for (i in 0 until model.level) {
                level = level + 10
            }

            layoutParams.setMargins(level, 0, 0, 0) // left, top, right, bottom
            holder.relativeLayout.layoutParams = layoutParams
            holder.relativeLayout.requestLayout()

        }


    }

    override fun getItemCount(): Int {
        return mDiscussion?.size ?: 0
    }

    fun addAll(data: List<Discussion>) {
        //mDiscussion.addAll(data);
        notifyDataSetChanged()
    }


}

