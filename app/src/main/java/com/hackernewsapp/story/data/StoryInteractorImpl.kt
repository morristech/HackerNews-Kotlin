package com.hackernewsapp.story.data

import android.app.Application
import com.hackernewsapp.Constants
import com.hackernewsapp.StoryInterface
import com.hackernewsapp.story.model.Story
import com.hackernewsapp.story.view.StoryView
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
class StoryInteractorImpl(private val application: Application) : StoryInteractor {

    private val logger = Logger.getLogger(javaClass)

    private val storyView: StoryView? = null

    private val totalNo = 0
    private val listStoryId = ArrayList<Long>()
    private val listArrayList = ArrayList<Story>()
    private val refreshedArrayList = ArrayList<Story>()
    private val mStoryObservable: Observable<Story>? = null


    override fun sayHello() {
        //
    }


    override fun getStorys(storyInterface: StoryInterface, storyIds: List<Long>): Observable<Story> {
        return if (storyIds.size > Constants.NO_OF_SPLIT_ITEMS * 2) {
            Observable.concat(
                    subListStories(storyInterface, storyIds.subList(0, Constants.NO_OF_SPLIT_ITEMS)),
                    subListStories(storyInterface, storyIds.subList(Constants.NO_OF_SPLIT_ITEMS,
                            Constants.NO_OF_SPLIT_ITEMS * 2)),
                    subListStories(storyInterface, storyIds.subList(Constants.NO_OF_SPLIT_ITEMS * 2,
                            storyIds.size)))
                    .flatMap { posts -> Observable.from(posts) }
        } else {

            subListStories(storyInterface, storyIds)
                    .flatMap { storys -> Observable.from(storys) }

        }
    }


    override fun subListStories(storyInterface: StoryInterface, storyIds: List<Long>): Observable<List<Story>> {
        return Observable.from(storyIds)
                .flatMap { aLong -> storyInterface.getStory(aLong.toString()) }
                .onErrorReturn { null }
                .filter { story -> story != null && story.title != null }
                .toList()
                .map { stories -> sortStories(stories, storyIds) }
    }


    override fun sortStories(storyList: List<Story>, storyIds: List<Long>): List<Story> {
        val storiesMap = HashMap<Long, Story>()
        val orderedStoryList = ArrayList<Story>()
        for (story in storyList) {
            storiesMap.put(story.id!!, story)
        }
        for (id in storyIds) {
            orderedStoryList.add(storiesMap[id]!!)
            //            listArrayList.add(storiesMap.get(id));
            //            refreshedArrayList.add(storiesMap.get(id));
        }

        return orderedStoryList
    }


}
