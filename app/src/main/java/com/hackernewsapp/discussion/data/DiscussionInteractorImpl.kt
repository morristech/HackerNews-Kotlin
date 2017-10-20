package com.hackernewsapp.discussion.data

import android.app.Application
import com.hackernewsapp.StoryInterface
import com.hackernewsapp.discussion.model.Discussion
import com.hackernewsapp.story.model.Story
import com.hackernewsapp.util.Logger
import rx.Observable
import java.util.*
import javax.inject.Singleton

/**
 * @author Tosin Onikute.
 *
 * This is a Data Manager implementer class which contains methods, exposed for all the story related data handling operations
 * to decouple your class, thus making it cleaner and testable
 */


@Singleton
class DiscussionInteractorImpl(private val application: Application) : DiscussionInteractor {

    private val logger = Logger.getLogger(javaClass)
    private val mCommentListObservable: Observable<List<Discussion>>? = null

    override fun fetchComment(mInterface: StoryInterface, level: Int, story: Story): Observable<List<Discussion>> {
        val allCommentIds = story.kids
        val descendants = story.descendants!!.toLong()



        return if (descendants > 15 && allCommentIds!!.size > 3) {
            // Get kids when kids size greater than 3";
            Observable.concat(getPartsComment(mInterface, level, allCommentIds!!.subList(0, 3)),
                    getAllComments(mInterface, level, allCommentIds.subList(3, allCommentIds.size)))
        } else if (descendants / allCommentIds!!.size > 15) {
            // Get kids when kids size greater than 15
            getPartsComment(mInterface, level, allCommentIds)
        } else {
            // If no criteria, use default
            getAllComments(mInterface, level, allCommentIds)
        }
    }

    override fun getPartsComment(mInterface: StoryInterface, level: Int, cmtIds: List<Long>): Observable<List<Discussion>> {
        return Observable.from(cmtIds)
                .flatMap { cmtId -> getSinglePartComments(mInterface, level, cmtId!!) }
    }

    override fun getSinglePartComments(mInterface: StoryInterface, level: Int, cmtId: Long): Observable<List<Discussion>> {
        return mInterface.getComment(cmtId)
                .onErrorReturn { null }
                .filter { cmt -> cmt != null && !cmt.removed && cmt.text != null }
                .flatMap { cmt -> getInnerLevelComments(mInterface, level, cmt) }
                .toList()
                .map { allDiscussions ->
                    val listFirstLevelComments = ArrayList<Long>()
                    listFirstLevelComments.add(cmtId)
                    sortComments(allDiscussions, listFirstLevelComments)
                }
    }

    override fun getAllComments(mInterface: StoryInterface, level: Int, firstLevelCmtIds: List<Long>): Observable<List<Discussion>> {

        return Observable.from(firstLevelCmtIds)
                .flatMap { commentId ->
                    mInterface.getComment(commentId!!)
                            .onErrorReturn { null }
                }
                .filter { cmt -> cmt != null && !cmt.removed && cmt.text != null }
                .flatMap { cmt -> getInnerLevelComments(mInterface, level, cmt) }
                .toList()
                .map { allDiscussions -> sortComments(allDiscussions, firstLevelCmtIds) }
    }

    override fun getInnerLevelComments(mInterface: StoryInterface, level: Int, cmt: Discussion): Observable<Discussion>? {
        if (cmt == null || cmt.removed || cmt.text == null) {
            return null
        }
        cmt.level = level
        return if (cmt.kids != null && !cmt.kids!!.isEmpty()) {
            Observable.just(cmt)
                    .mergeWith(Observable.from(cmt.kids)
                            .flatMap { cmtId ->
                                mInterface.getComment(cmtId!!)
                                        .onErrorReturn { null }
                            }
                            .filter { cmt -> cmt != null && !cmt.removed && cmt.text != null }
                            .flatMap { cmt ->
                                // get all the other level of comments
                                getInnerLevelComments(mInterface, level + 1, cmt)
                            }
                    )
        } else Observable.just(cmt)
    }


    override fun sortComments(allDiscussions: List<Discussion>, firstLevelCmtIds: List<Long>): List<Discussion> {
        val newHashMap = HashMap<Long, Discussion>()
        for (child in allDiscussions) {
            newHashMap.put(child.id, child)
        }
        val newlistFirstLevelCmt = ArrayList<Discussion>()
        for (id in firstLevelCmtIds) {
            val firstLevelCmt = newHashMap[id]
            if (firstLevelCmt != null && !firstLevelCmt.removed && firstLevelCmt.text != null) {
                newlistFirstLevelCmt.add(firstLevelCmt)
            }
        }
        return sortAllComments(newHashMap, newlistFirstLevelCmt)
    }

    override fun sortAllComments(allCommentsHashMap: HashMap<Long, Discussion>, listOfDiscussions: List<Discussion>): List<Discussion> {
        val sortedDiscussionList = ArrayList<Discussion>()

        for (discussion in listOfDiscussions) {
            sortedDiscussionList.add(discussion)
            if (discussion.kids != null && discussion.kids!!.size > 0) {
                val innerChildList = ArrayList<Discussion>()
                for (id in discussion.kids!!) {
                    val child = allCommentsHashMap[id]
                    if (child != null && !child.removed && child.text != null) {
                        innerChildList.add(child)
                    }
                }
                sortedDiscussionList.addAll(sortAllComments(allCommentsHashMap, innerChildList))
            }
        }

        //        discussionArrayList = new ArrayList<Discussion>(sortedDiscussionList);
        return sortedDiscussionList
    }


}
