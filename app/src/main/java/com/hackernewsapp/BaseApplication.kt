package com.hackernewsapp

import android.app.Application
import com.hackernewsapp.di.components.DaggerNetComponent
import com.hackernewsapp.di.components.DaggerStoryComponent
import com.hackernewsapp.di.components.NetComponent
import com.hackernewsapp.di.components.StoryComponent
import com.hackernewsapp.di.modules.*

/**
 * @author Tosin Onikute.
 */

class BaseApplication : Application() {

    companion object {
        @JvmStatic lateinit var netComponent: NetComponent
        @JvmStatic lateinit var storyComponent: StoryComponent
    }

    override fun onCreate() {
        super.onCreate()

        netComponent = DaggerNetComponent.builder()
                .appModule(AppModule(this))
                .netModule(NetModule())
                .build()

        storyComponent = DaggerStoryComponent.builder()
                .netComponent(netComponent)
                .retrofitModule(RetrofitModule())
                .storyModule(StoryModule(this))
                .discussionModule(DiscussionModule(this))
                .build()

    }


}