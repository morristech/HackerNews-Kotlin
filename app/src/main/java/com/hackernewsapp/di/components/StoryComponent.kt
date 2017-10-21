package com.hackernewsapp.di.components


import com.hackernewsapp.data.StoryInteractor
import com.hackernewsapp.di.modules.DiscussionModule
import com.hackernewsapp.di.modules.RetrofitModule
import com.hackernewsapp.di.modules.StoryModule
import com.hackernewsapp.di.scopes.UserScope
import com.hackernewsapp.ui.discussion.DiscussionActivity
import com.hackernewsapp.ui.story.MainActivity
import com.hackernewsapp.ui.story.presenter.StoryPresenter
import dagger.Component

/**
 * @author Tosin Onikute.
 */


@UserScope
@Component(dependencies = arrayOf(NetComponent::class), modules = arrayOf(RetrofitModule::class, StoryModule::class, DiscussionModule::class))
interface StoryComponent {

    fun inject(activity: MainActivity)
    fun inject(activity: DiscussionActivity)
    fun inject(storyPresenter: StoryPresenter)
    fun inject(storyInteractor: StoryInteractor)


}


