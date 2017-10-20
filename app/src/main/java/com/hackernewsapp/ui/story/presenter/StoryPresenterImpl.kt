package com.hackernewsapp.ui.story.presenter

import android.app.Application
import com.hackernewsapp.Constants
import com.hackernewsapp.StoryInterface
import com.hackernewsapp.data.StoryInteractor
import com.hackernewsapp.data.model.Story
import com.hackernewsapp.ui.story.view.StoryView
import com.hackernewsapp.util.Logger
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*

/**
 * @author Tosin Onikute.
 */

class StoryPresenterImpl(private val application: Application, private val storyInteractor: StoryInteractor) : StoryPresenter {

    private val logger = Logger.getLogger(javaClass)

    private var storyView: StoryView? = null


    private var totalNo = 0
    private val listStoryId = ArrayList<Long>()
    private val listArrayList = ArrayList<Story>()
    private val refreshedArrayList = ArrayList<Story>()
    private var mStoryObservable: Observable<Story>? = null

    override fun setView(storyView: StoryView) {
        this.storyView = storyView
    }

    // Method for repopulating recycler view
    override fun updateRecyclerView(storyInterface: StoryInterface, mCompositeSubscription: CompositeSubscription,
                                    fromIndex: Int?, toIndex: Int?) {

        // Show Progress Layout
        refreshedArrayList.clear()
        storyView!!.setLayoutVisibility()

        logger.debug(fromIndex.toString() + " " + toIndex.toString())
        fetchStories(storyInterface, mCompositeSubscription, true, true, listStoryId.subList(fromIndex!!, toIndex!!))
    }


    override fun getStoryIds(storyInterface: StoryInterface, storyTypeUrl: String,
                             mCompositeSubscription: CompositeSubscription, refresh: Boolean) {
        if (refresh) {
            listArrayList.clear()
            refreshedArrayList.clear()
        }

        if (storyInterface != null) {

            mCompositeSubscription.add(storyInterface.getStories(storyTypeUrl)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ longs ->
                        listStoryId.clear()
                        listStoryId.addAll(longs)
                        totalNo = listStoryId.size

                        fetchStories(storyInterface, mCompositeSubscription,
                                true, false, listStoryId.subList(0, Constants.NO_OF_ITEMS_LOADING))
                    }) { throwable -> logger.debug(throwable.localizedMessage) })

        }
    }


    fun fetchStories(storyInterface: StoryInterface, mCompositeSubscription: CompositeSubscription,
                     updateObservable: Boolean, loadmore: Boolean, list: List<Long>) {
        if (mStoryObservable == null || updateObservable) {
            mStoryObservable = storyInteractor.getStorys(storyInterface, list).cache()
        }

        mCompositeSubscription.add(mStoryObservable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Story>() {
                    override fun onCompleted() {
                        logger.debug("completed")

                        storyView!!.doAfterFetchStory()
                        val storiesLoaded = listArrayList.size

                        storyView!!.setAdapter(storiesLoaded, listArrayList, refreshedArrayList, loadmore, totalNo)

                    }

                    override fun onError(throwable: Throwable) {
                        logger.debug(throwable.localizedMessage)
                    }

                    override fun onNext(story: Story?) {
                        if (story != null) {
                            listArrayList.add(story)
                            refreshedArrayList.add(story)
                        }
                    }
                }))


    }


}
